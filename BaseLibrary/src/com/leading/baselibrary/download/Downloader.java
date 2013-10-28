package com.leading.baselibrary.download;

import java.io.File;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.Where;
import com.leading.baselibrary.database.DataBaseManager;
import com.leading.baselibrary.database.bean.DownloadInfo;

public class Downloader {
	private String urlstr;// 下载的地址
	private String localfile;// 保存路径
	private int threadcount;// 线程数
	private Handler mHandler;// 消息处理器
	private FiledownUtil dao;// 工具类
	private int fileSize;// 所要下载的文件的大小
	private List<DownloadInfo> infos;// 存放下载信息类的集合
	private static final int INIT = 1;// 定义三种下载的状态：初始化状态，正在下载状态，暂停状态
	private static final int DOWNLOADING = 2;
	private static final int PAUSE = 3;
	private int state = 0;
	private DownLoadFileInfo dLoadInfo;

	public Downloader(String urlstr, String localfile, int threadcount,Context context, Handler mHandler) {
		this.urlstr = urlstr;
		this.localfile = localfile;
		this.threadcount = threadcount;
		this.mHandler = mHandler;
		dao = new FiledownUtil(context);
	}
	/**
	 * 判断是否正在初始化
	 */
	public boolean isIniting() {
		return state == INIT;
	}
	/**
	 * 判断是否正在下载
	 */
	public boolean isDownloading() {
		return state == DOWNLOADING;
	}
	public DownLoadFileInfo getDownloaderInfors(){
		return dLoadInfo;
	}
	/**
	 * 得到downloader里的信息 首先进行判断是否是第一次下载，如果是第一次就要进行初始化，并将下载器的信息保存到数据库中
	 * 如果不是第一次下载，那就要从数据库中读出之前下载的信息（起始位置，结束为止，文件大小等），并将下载信息返回给下载器
	 */
	private void DealDownloaderInfors() {
		if (isFirst(urlstr)) {
			initData();
			int range = fileSize / threadcount;
			int last=fileSize % threadcount;
			infos = new ArrayList<DownloadInfo>();
			for (int i = 0; i < threadcount-1 ; i++) {
				DownloadInfo info = getNewDownloadInfo(i,i * range,(i + 1)* range,0,urlstr);		
				infos.add(info);
			}
			DownloadInfo info_2 = getNewDownloadInfo(threadcount-1 ,(threadcount-1) * range,fileSize,0,urlstr);		
			infos.add(info_2);
			// 保存infos中的数据到数据库
			dao.save(infos);
			// 创建一个LoadInfo对象记载下载器的具体信息
			dLoadInfo = new DownLoadFileInfo(fileSize, 0, urlstr);
		} else {
			// 得到数据库中已有的urlstr的下载器的具体信息
			infos = dao.getData(urlstr);
			Log.v("TAG", "not isFirst size=" + infos.size());
			int size = 0;
			int compeleteSize = 0;
			for (DownloadInfo info : infos) {
				compeleteSize += info.getCompeleteSize();
				size += info.getEndPos() - info.getStartPos() + 1;
			}
			dLoadInfo= new DownLoadFileInfo(size, compeleteSize, urlstr);
		}
	}
	private DownloadInfo getNewDownloadInfo(int threadId, int startPos, int endPos,int compeleteSize, String url){
		DownloadInfo info_2 = new DownloadInfo();
		info_2.setThreadId(threadId);
		info_2.setStartPos(startPos);
		info_2.setEndPos(endPos);
		info_2.setCompeleteSize(compeleteSize);
		info_2.setDownPath(url);
		return info_2;
	}

	/**
      */
	private void initData() {
		try {
			URL url = new URL(urlstr);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setConnectTimeout(4000);
			connection.setRequestMethod("GET");
			fileSize = connection.getContentLength();
			File file = new File(localfile);
			if (!file.exists()) {file.createNewFile();}
				// 本地访问文件
				RandomAccessFile accessFile = new RandomAccessFile(file, "rwd");
				accessFile.setLength(fileSize);
				accessFile.close();
			connection.disconnect();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void init(){
		new Thread(){
			@Override
			public void run() {
				dao.delete(urlstr);
				DealDownloaderInfors();
				Message message = Message.obtain();
				message.what = 0;
				message.obj = urlstr;
				mHandler.sendMessage(message);
			}
		}.start();
	}

	/**
	 * 判断是否是第一次 下载
	 */
	private boolean isFirst(String urlstr) {
		return !dao.isHasInfors(urlstr);
	}

	/**
	 * 114 * 利用线程开始下载数据 115
	 */
	public void download() {
		if (infos != null) {
			if (state == DOWNLOADING)return;
			state = DOWNLOADING;
			for (DownloadInfo info : infos) {
				new DownLoadThread(info.getThreadId(), info.getStartPos(),
						info.getEndPos(), info.getCompeleteSize(),
						info.getDownPath()).start();
			}
		}
	}

	public class DownLoadThread extends Thread {
		private int threadId;
		private int startPos;
		private int endPos;
		private int compeleteSize;
		private String urlstr;

		public DownLoadThread(int threadId, int startPos, int endPos,
				int compeleteSize, String urlstr) {
			this.threadId = threadId;
			this.startPos = startPos;
			this.endPos = endPos;
			this.compeleteSize = compeleteSize;
			this.urlstr = urlstr;
		}

		@Override
		public void run() {
			HttpURLConnection connection = null;
			RandomAccessFile randomAccessFile = null;
			InputStream is = null;
			try {
				URL url = new URL(urlstr);
				connection = (HttpURLConnection) url.openConnection();
				connection.setConnectTimeout(5000);
				connection.setRequestMethod("GET");
				// 设置范围，格式为Range：bytes x-y;
				connection.setRequestProperty("Range", "bytes="
						+ (startPos + compeleteSize) + "-" + endPos);

				randomAccessFile = new RandomAccessFile(localfile, "rwd");
				randomAccessFile.seek(startPos + compeleteSize);
				// 将要下载的文件写到保存在保存路径下的文件中
				is = connection.getInputStream();
				byte[] buffer = new byte[4096];
				int length = -1;
				while ((length = is.read(buffer)) != -1) {
					randomAccessFile.write(buffer, 0, length);
					compeleteSize += length;
					// 更新数据库中的下载信息
					dao.updateDownloadInfo(threadId, urlstr,compeleteSize);
					// 用消息将下载信息传给进度条，对进度条进行更新
					Message message = Message.obtain();
					message.what = 1;
					message.obj = urlstr;
					message.arg1 = length;
					mHandler.sendMessage(message);
					if (state == PAUSE) {return;}
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					is.close();
					randomAccessFile.close();
					connection.disconnect();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		}
	}

	// 删除数据库中urlstr对应的下载器信息
	public void delete(String urlstr) {
		dao.delete(urlstr);
	}

	// 设置暂停
	public void pause() {
		state = PAUSE;
	}

	// 重置下载状态
	public void reset() {
		state = 0;
	}

	/**
	 * 记录各线程下载状态的辅助类
	 * @author sjz
	 *
	 */
	 class FiledownUtil {
			private DataBaseManager dbManager;

		    public FiledownUtil(Context context) {
		    	dbManager = new DataBaseManager(context);
		    }
		    
		    /**
		     * 获取每条线程已经下载的文件长度
		     * @param path
		     * @return
		     */
		    public List<DownloadInfo> getData(String path){
		    	List<DownloadInfo> downLogs=dbManager.getFiledownLogDAO().queryForAll("url", path);
		        return downLogs;
		    }
		    
		    /**
		     * 保存每条线程已经下载的文件长度
		     * @param path
		     * @param map
		     */
		    public void save(List<DownloadInfo> map){//int threadid, int position
		        for(DownloadInfo downlog : map){
		            dbManager.getFiledownLogDAO().createOrUpdate(downlog);
		        }
		    }
		    
		    /**
		     * 实时更新每条线程已经下载的文件长度
		     * @param path
		     * @param map
		     */
		    public void update(DownloadInfo downLog){
		    	dbManager.getFiledownLogDAO().createOrUpdate(downLog);
		    }
		    
		    /**
		     * 当文件下载完成后，删除对应的下载记录
		     * @param path
		     */
		    public void delete(String path){
		    	Collection<DownloadInfo> tmp=getData(path);
		    	dbManager.getFiledownLogDAO().remove(tmp);
		    }
		    /**
		     * 当前文件是否下载过
		     * @param path
		     * @return
		     */
		    public boolean isHasInfors(String path){
		    	return dbManager.getFiledownLogDAO().queryForAll("url", path).size()>0?true:false;
		    }
		    public void updateDownloadInfo(int threadId,String strUrl,int completeSize){
		    	Dao<DownloadInfo,Long> loadDAO=dbManager.getFiledownLogDAO().getDao();
		    	Where<DownloadInfo,Long> daoWhere=loadDAO.queryBuilder().where();
				try {
					DownloadInfo dInfo = daoWhere.eq("threadId", threadId).and().eq("url", strUrl).queryForFirst();
					if(dInfo!=null){
						dInfo.setCompeleteSize(completeSize);
						loadDAO.update(dInfo);
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
		    }
		}

}

