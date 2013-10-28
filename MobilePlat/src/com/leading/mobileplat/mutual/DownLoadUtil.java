package com.leading.mobileplat.mutual;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.leading.baselibrary.download.DownLoadFileInfo;
import com.leading.baselibrary.download.Downloader;



/**
 * 调用此类 必须配合页面上锁带的TextView和ProgressBar使用
 * @author sjz
 *
 */
public class DownLoadUtil {
	
	private Context context;
	// 固定存放下载的音乐的路径：SD卡目录下
	private static final String SD_PATH = Environment.getExternalStorageDirectory()+"/MobilePlat/ServerApps";
	// 存放各个下载器
	private Map<String, Downloader> downloaders = new HashMap<String, Downloader>();
	// 存放与下载器对应的进度条
	private Map<String, ListItemView> ListItemViews = new HashMap<String, ListItemView>();
	
	private static DownLoadUtil sInstance=null;
	public static DownLoadUtil getInstance(Context context){
		synchronized(DownLoadUtil.class){
			if(sInstance==null){ 
				sInstance=new DownLoadUtil(context);
			}
		}
		return sInstance;
	}
	
	private DownLoadUtil(Context context){
		this.context=context;
	}
	/**
	 * 31 * 利用消息处理机制适时更新进度条 32
	 */
	private Handler downloadHandler = new Handler() {
		public void handleMessage(Message msg) {
			if(msg.what==0){
				String url = (String) msg.obj;
				DownLoadFileInfo loadInfo=downloaders.get(url).getDownloaderInfors();
				if(loadInfo==null){
					Log.e("downloadHandler","loadInfo is null");
					return ;
				}
				ListItemViews.get(url).beginDownload(loadInfo);
				downloaders.get(url).download();
			}
			else if (msg.what == 1) {
				String url = (String) msg.obj;
				int length = msg.arg1;
				ListItemView llv=ListItemViews.get(url);
				llv.showProgressValue(length);
				if(llv.hasFinished()){
					Toast.makeText(context, "下载完成！", Toast.LENGTH_SHORT).show();
					llv.showFinishLoad(url);
					ListItemViews.remove(url);
					downloaders.get(url).delete(url);
					downloaders.get(url).reset();
					downloaders.remove(url);
					installApk(url);
				}
			}
		}
	};

	/**
	 * 安装
	 * @param strUrl
	 */
	private void installApk(String strUrl) { 
		String localfile = SD_PATH + strUrl.substring(strUrl.lastIndexOf("/"));
		File file=new File(localfile);
		if(file.exists()){
	        Intent intent = new Intent(); 
	        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 
	        intent.setAction(android.content.Intent.ACTION_VIEW); 
	        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive"); 
	        context.startActivity(intent); 
		}
}
	/**
	 * 83 * 响应开始下载按钮的点击事件 84
	 */
	public void startDownload(String strUrl,ProgressBar pb,TextView pbValue,TextView remark,TextView btnstart, TextView btnpause) {

		File fl=new File(SD_PATH);
		fl.mkdirs();
		String localfile = SD_PATH + strUrl.substring(strUrl.lastIndexOf("/"));
		// 设置下载线程数为4，这里是我为了方便随便固定的
		int threadcount = 4;
		// 初始化一个downloader下载器
		Downloader downloader = downloaders.get(strUrl);
		if (downloader == null) {
			ListItemViews.put(strUrl, new ListItemView(pb,pbValue,remark,btnstart,btnpause));
			downloader = new Downloader(strUrl, localfile, threadcount, context,downloadHandler);
			downloaders.put(strUrl, downloader);
		}
		if(downloader.isIniting())return;
		if (downloader.isDownloading())return;
		downloader.init();
	}

	public void puseDownload(String strUrl){
		downloaders.get(strUrl).pause();
	}
	
	
	public class ListItemView{
		ProgressBar pb_progressBar;
		TextView  tv_progressValue;
		TextView tv_State;
		TextView btn_Start;
		TextView btn_Pasue;
		
		public ListItemView(ProgressBar pb,TextView pbValue,TextView remark,TextView btnstart, TextView btnpause){
			this.pb_progressBar=pb;
			this.tv_progressValue=pbValue;
			this.tv_State=remark;
			this.btn_Pasue=btnpause;
			this.btn_Start=btnstart;
			showInitMsg();
		}
		
		public void showInitMsg(){
			tv_State.setText("正在连接 ....");
			btn_Start.setVisibility(View.GONE);
		}
		public void beginDownload(DownLoadFileInfo loadInfo) {
			tv_State.setText("正在下载 ....");
			pb_progressBar.setVisibility(View.VISIBLE);
			tv_progressValue.setVisibility(View.VISIBLE);
			btn_Pasue.setVisibility(View.VISIBLE);
			btn_Start.setVisibility(View.GONE);
			
			pb_progressBar.setMax(loadInfo.getFileSize());
			pb_progressBar.setProgress(loadInfo.getComplete());
				Log.v("",loadInfo.getFileSize()+"--"+loadInfo.getComplete());

			int barValue=0;
			if(loadInfo.getComplete()>0)
				barValue=loadInfo.getFileSize()*100/loadInfo.getComplete();
			tv_progressValue.setText(barValue+"%");
		}
		public void showProgressValue(int length ){
			pb_progressBar.incrementProgressBy(length);
			int barValue=0;
			if(pb_progressBar.getMax()>0) barValue=pb_progressBar.getProgress()*100/pb_progressBar.getMax();
			tv_progressValue.setText(barValue+"%");
		}
		public boolean hasFinished(){
			return (pb_progressBar.getProgress() == pb_progressBar.getMax());
		}
		public void showFinishLoad(String connetUrl){
			tv_State.setText("下载完成");
			tv_State.setTag(connetUrl);
			tv_progressValue.setVisibility(View.GONE);
			btn_Pasue.setVisibility(View.GONE);
			pb_progressBar.setVisibility(View.GONE);
			btn_Start.setVisibility(View.GONE);
			tv_State.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View v) {
					String url=String.valueOf(v.getTag());
					installApk(url);
				}
				
			});
		}
		
	}
}
