package com.leading.baselibrary.database;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.Log;

import com.j256.ormlite.dao.CloseableIterator;
import com.j256.ormlite.dao.ForeignCollection;
import com.leading.baselibrary.R;
import com.leading.baselibrary.database.bean.CacheRecord;
import com.leading.baselibrary.database.bean.CacheServerAppBean;
import com.leading.baselibrary.database.bean.DownloadInfo;
import com.leading.baselibrary.database.bean.MsgBean;
import com.leading.baselibrary.database.bean.MsgGroup;

public class DataBaseManager {
	
	private Context mainPlatContext=null;
	private int configXMLResId=com.leading.baselibrary.R.xml.db;
	private MsgGroupDAO msgGroupDAO=null;
	private MsgBeanDAO msgBeanDAO=null;
	private DownloadInfoDAO downloadInfoDAO=null; 
	private CacheServerAppBeanDAO cacheServerAppBeanDAO=null;
	private CacheRecordDAO cacheRecordDAO=null;
	
	public DownloadInfoDAO getFiledownLogDAO(){return this.downloadInfoDAO;}
	public CacheRecordDAO getCacheRecordDAO(){return this.cacheRecordDAO;}
	public CacheServerAppBeanDAO getCacheServerAppBeanDAO(){return this.cacheServerAppBeanDAO;}
	public MsgGroupDAO getMsgGroupDAO() {
		return msgGroupDAO;
	}
	public MsgBeanDAO getMsgBeanDAO() {
		return msgBeanDAO;
	}
	public DataBaseManager(Context context){
		try {
			mainPlatContext=context.createPackageContext(context.getString(R.string.MainPlatAPK), Context.CONTEXT_IGNORE_SECURITY);
		} catch (NameNotFoundException e) {
			Log.v("DataBaseManager","Create FriendContext failed!");
			e.printStackTrace();
		}
		msgGroupDAO=new MsgGroupDAO(mainPlatContext, configXMLResId);
		msgBeanDAO=new MsgBeanDAO(mainPlatContext,configXMLResId);
		downloadInfoDAO=new DownloadInfoDAO(mainPlatContext,configXMLResId);
		cacheServerAppBeanDAO=new CacheServerAppBeanDAO(mainPlatContext,configXMLResId);
		cacheRecordDAO=new CacheRecordDAO(mainPlatContext,configXMLResId);
	}
	public void insertMessage(MsgBean _msgBean){msgBeanDAO.createOrUpdate(_msgBean);}
	public MsgGroup getMsgGroup(String sourceApkName){
		if(sourceApkName==null)sourceApkName="";
		MsgGroup MG=msgGroupDAO.query("sourceApkName", sourceApkName);
		if(MG==null){
			MG=new MsgGroup();
			MG.setSourceApkName(sourceApkName);
			if(sourceApkName.equals(""))MG.setGroupName("系统消息");
			else{
				PackageManager pm = mainPlatContext.getPackageManager();
				//String groupName=pm.getInstallerPackageName(sourceApkName);
				String groupName="";
				try {
					groupName = pm.getApplicationInfo(sourceApkName, PackageManager.GET_ACTIVITIES).loadLabel(pm).toString();
				} catch (NameNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				MG.setGroupName(groupName);
			}
			msgGroupDAO.create(MG);
		}
		return MG;
	}
	public List<MsgGroup> getAllMsgGroup(String userId){
		List<MsgGroup> msgGroupList = msgGroupDAO.queryForAll();
		for(MsgGroup mg:msgGroupList){
			ForeignCollection<MsgBean> msgs=mg.getMsgCollection();
			CloseableIterator<MsgBean> iterator=msgs.closeableIterator();
			while(iterator.hasNext()){
				MsgBean mb=iterator.next();
				if(mb.getUserId().equals(userId))
				mg.addMsg(mb);
			}
			if(mg.getMsgList().size()==0)msgGroupList.remove(mg);
		}
		return msgGroupList;
	}
	public int getUnReadCount(String sourceApkName,String userId){
		int count=0;
		if(sourceApkName!=null){
			MsgGroup MG=msgGroupDAO.query("sourceApkName", sourceApkName);
			if(MG!=null){
				ForeignCollection<MsgBean> msgs=MG.getMsgCollection();
				CloseableIterator<MsgBean> iterator=msgs.closeableIterator();
				while(iterator.hasNext()){
					MsgBean mb=iterator.next();
					if(!mb.getReadState()&&mb.getUserId().equals(userId))count++;
				}
			}
		}
		return count;		
	}
	public List<MsgGroup> getUnReadMsgGroup(String userId){
		List<MsgGroup> msgGroupList = msgGroupDAO.queryForAll();
		for(MsgGroup mg:msgGroupList){
			ForeignCollection<MsgBean> msgs=mg.getMsgCollection();
			CloseableIterator<MsgBean> iterator=msgs.closeableIterator();
			while(iterator.hasNext()){
				MsgBean mb=iterator.next();
				if(!mb.getReadState()&&mb.getUserId().equals(userId))mg.addMsg(mb);
			}
			if(mg.getMsgList().size()==0)msgGroupList.remove(mg);
		}
		return msgGroupList;
	}
	public void deleteAllReadMsgBean(){
		List<MsgBean> msgBeanList=msgBeanDAO.queryForAll();
		for(MsgBean mb : msgBeanList){
			if(mb.getReadState())msgBeanDAO.remove(mb);
		}
	}
/*	public List<MsgBean> getUnReadMsgBean(){
		List<MsgBean> msgBeanList=msgBeanDAO.queryForAllOrderby("readState", false, "msgPubTime", true);
		return msgBeanList;
	}*/
	/**
	 * 更新消息是否已读的状态
	 * @param businessInstanceId
	 */
	public void setStateByBusinessInstanceId(String businessInstanceId){
		HashMap<String,Object> stateValue=new HashMap<String,Object>();
		stateValue.put("readState", true);
		msgBeanDAO.update(stateValue, "businessInstanceId", businessInstanceId);
	}
	public void setStateById(String MsgId){
		HashMap<String,Object> stateValue=new HashMap<String,Object>();
		stateValue.put("readState", true);
		msgBeanDAO.update(stateValue, "msgId", MsgId);
	}
	public void deleteMessageByBusinessId(String businessInstanceId){
		try {
			List<MsgBean> msgList=msgBeanDAO.getDao().queryForEq("businessInstanceId", businessInstanceId);
			msgBeanDAO.getDao().delete(msgList);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void deleteMessage(long msgBeanId){
		try {
			msgBeanDAO.getDao().deleteById(msgBeanId);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * 测试用，清库
	 * @throws SQLException 
	 */
	public void deleteALL() {
		List<MsgGroup> msgGroupList = msgGroupDAO.queryForAll();
		for(MsgGroup mg:msgGroupList){
			ForeignCollection<MsgBean> msgs=mg.getMsgCollection();
			CloseableIterator<MsgBean> iterator=msgs.closeableIterator();
			while(iterator.hasNext()){
				MsgBean mb=iterator.next();
				if(mb.getReadState()==false)
					try {
						msgBeanDAO.getDao().delete(mb);
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			}
			try {
				msgGroupDAO.getDao().delete(mg);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	
	public class MsgGroupDAO extends  LzDAO<MsgGroup>{
		public MsgGroupDAO(Context context, int configXMLResId) {super(context, configXMLResId);}
	}
	public class MsgBeanDAO extends  LzDAO<MsgBean>{
		public MsgBeanDAO(Context context, int configXMLResId) {super(context, configXMLResId);}
	}
	public class DownloadInfoDAO extends  LzDAO<DownloadInfo>{
		public DownloadInfoDAO(Context context, int configXMLResId) {super(context, configXMLResId);}
	}
	public class CacheRecordDAO extends LzDAO<CacheRecord>{
		public CacheRecordDAO(Context context,int configXMLResId){super(context,configXMLResId);}
	}
	public class CacheServerAppBeanDAO extends LzDAO<CacheServerAppBean>{
		public CacheServerAppBeanDAO(Context context,int configXMLResId){super(context,configXMLResId);}
	}
}
