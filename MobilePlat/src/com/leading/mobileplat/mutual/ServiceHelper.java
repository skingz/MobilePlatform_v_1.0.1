package com.leading.mobileplat.mutual;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.google.gson.Gson;
import com.leading.baselibrary.database.DataBaseManager;
import com.leading.baselibrary.database.DataBaseManager.CacheRecordDAO;
import com.leading.baselibrary.database.DataBaseManager.CacheServerAppBeanDAO;
import com.leading.baselibrary.database.bean.CacheRecord;
import com.leading.baselibrary.database.bean.CacheServerAppBean;
import com.leading.baselibrary.global.MainApplication;
import com.leading.baselibrary.netutil.NetUtil;
import com.leading.baselibrary.util.EncryptUtil;
import com.leading.baselibrary.util.FileBeanMakeUp;
import com.leading.baselibrary.util.StringUtils;
import com.leading.mobileplat.entity.UserBo;
import com.leading.mobileplat.plugin.PluginBean;
import com.leading.mobileplat.plugin.PluginManager;


public class ServiceHelper {
	private Context context;
	private String ipAdress;
	public ServiceHelper(Context context){this.context=context;}
	
	/**
	 * 登录功能.
	 * @param ipAdress 服务器地址
	 * @param userName 用户名
	 * @param pwd 用户密码
	 * @return 返回是否成功（error,1）、用户真实姓名
	 */
	public UserBo login(String ipAdress,String userName,String pwd){
		this.ipAdress=ipAdress;
		try {
			JSONObject obj = new JSONObject();
			obj.put("LoginName", userName);
			obj.put("PassWord",EncryptUtil.md5One(pwd)); 
			String login_Return=NetUtil.getResponseForPost(ServiceMap.PROTOCOL+this.ipAdress+ServiceMap.Segment_LoginUri,obj.toString(), context);
			if(StringUtils.isNotNull(login_Return)){
				if("null".equals(login_Return))return new UserBo(login_Return);
//				Log.v("............",login_Return);
				Gson gson=new Gson();
				return gson.fromJson(login_Return, UserBo.class);
			}//else Log.e("MainPlat-----login_Return","返回为空!");
		} catch (Exception e) {
//			Log.e("MainPlat-----Login",e.toString());
//			e.printStackTrace();
		}
		return null;
	}
	public String ModifyPwd(String oldPwd,String newPwd){
		String str_Return="error";
		try {
			JSONObject obj = new JSONObject();
			obj.put("LoginName", MainApplication.getConfig().getUsername());
			obj.put("OldPassWord",EncryptUtil.md5One(oldPwd)); 
			obj.put("NewPassWord",EncryptUtil.md5One(newPwd)); 
			String login_Return=NetUtil.getResponseForPost(ServiceMap.PROTOCOL+MainApplication.getConfig().getServerAddress()+ServiceMap.Segment_ModifyPWDUri, obj.toString(), context);
			obj=new JSONObject(login_Return);
			str_Return=obj.getString("ResultTag");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return str_Return;
	}
	
	public String feedBack(String massage,String contactManner){
		String str_Return="error";
		try {
			JSONObject obj = new JSONObject();
			obj.put("Message", massage);
			obj.put("LoginName",MainApplication.getConfig().getUsername());
			obj.put("ContactManner",contactManner); 
			String login_Return=NetUtil.getResponseForPost(ServiceMap.PROTOCOL+MainApplication.getConfig().getServerAddress()+ServiceMap.Segment_FeedbackUri,obj.toString(), context);
			obj=new JSONObject(login_Return);
			str_Return=obj.getString("ResultTag");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		return str_Return;
	}
	/**
	 * 下载列表 先去缓存表里面去取，如果有缓存且缓存时间不超过2天，则直接从数据库取；否则从服务器取，并存入缓存
	 * @return
	 */
	public List<CacheServerAppBean> getAppList(List<PluginBean> _localList){
		List<PluginBean> localList=new ArrayList<PluginBean>();
		if(_localList!=null){
			for(PluginBean  pb : _localList){
				localList.add(pb);
			}
		}
		List<CacheServerAppBean> appList=new ArrayList<CacheServerAppBean>();
		boolean ifHavaCache=false;
		DataBaseManager dbManager=new DataBaseManager(context);
		CacheServerAppBeanDAO cacheServerAppBeanDAO=dbManager.getCacheServerAppBeanDAO();
		CacheRecordDAO cacheRecordDAO=dbManager.getCacheRecordDAO();
		//查询是否有缓存记录
		CacheRecord record=cacheRecordDAO.query("cacheName", "CacheServerAppBean");
		if(record!=null){
			Date dtNow=new Date();
			Long diffTime=dtNow.getTime()-record.getUpdateTime().getTime();
			long diffDay=diffTime/24/60/60/1000;
			ifHavaCache=(diffDay<0);
		}else {
			record=new CacheRecord();
			record.setCacheName("CacheServerAppBean");
		}	
		//如果有缓存且缓存时间不超过2天，则直接从数据库取；否则从服务器取，并重新存入缓存
		List<CacheServerAppBean> tmp_appList=new ArrayList<CacheServerAppBean>();
		tmp_appList=cacheServerAppBeanDAO.queryForAll();
		if(ifHavaCache){
			appList=tmp_appList;
		}else {
			appList=getAppListFromServer();
			cacheServerAppBeanDAO.remove(tmp_appList);
			for(CacheServerAppBean appBean:appList ){
				cacheServerAppBeanDAO.create(appBean);
			}
			record.setUpdateTimeNow();
			cacheRecordDAO.createOrUpdate(record);
		}
		//与本地程序对比，查看是否需要更新
			PluginManager pm=new PluginManager(this.context);
			localList.add(pm.getCurrentBean());
			for(CacheServerAppBean appBean:appList ){
				for(PluginBean localebean:localList){
					if(appBean.getAppName_EN().equals(localebean.getApkName())){
						appBean.setHasInstalled(true);
						if(!appBean.getEditionNo().equals(localebean.getVersion())){
							appBean.setNeedUpdate(true);
						}
					}
				}
			}
		return appList;
	}
	/**
	 * 
	 * @return
	 */
	private List<CacheServerAppBean> getAppListFromServer(){
		List<CacheServerAppBean> appList=new ArrayList<CacheServerAppBean>();
		try{
			JSONObject obj = new JSONObject();
			String login_Return=NetUtil.getResponseForPost(ServiceMap.PROTOCOL+MainApplication.getConfig().getServerAddress()+ServiceMap.Segment_GetAppList, obj.toString(), context);
			obj =new JSONObject(login_Return);
			String str_Return=obj.getString("ResultTag");
			if(str_Return.equals("1")){
				
				JSONArray obj_Arr =new JSONArray(obj.getString("AppList"));
				for(int i=0;i<obj_Arr.length();i++){
					CacheServerAppBean sa=new CacheServerAppBean();
					JSONObject obj_child=obj_Arr.getJSONObject(i);
					sa.setKeyId(obj_child.getString("KeyId"));
					sa.setAppName_CN(obj_child.getString("AppName_CN"));
					sa.setAppName_EN(obj_child.getString("AppName_EN"));
					sa.setAppSize(obj_child.getString("AppSize"));
					sa.setAppType(obj_child.getString("AppType"));
					sa.setEditionType(obj_child.getString("EditionType"));
					sa.setDescrible(obj_child.getString("Describle"));
					sa.setEditionNo(obj_child.getString("EditionNo"));
					sa.setIconUrl(obj_child.getString("IcoUrl"));
					sa.setStrUrl(obj_child.getString("StrUrl"));
					sa.setUpdateTime(obj_child.getString("UpdateTime"));
					appList.add(sa);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return appList;
	}

	
	///====================================test
	
	public String testUploadPic(){
		String filePath=Environment.getExternalStorageDirectory()+"/MobilePlat/Penguins.jpg";
		String strHttpPath=ServiceMap.PROTOCOL+MainApplication.getConfig().getServerAddress()+ServiceMap.Segment_UploadAffix;
		String str_Return="error";
		try {
			JSONObject obj = new JSONObject();
			String login_Return=NetUtil.getResposeForPostStream( strHttpPath,filePath, context);
			Log.v("............",login_Return);
			obj =new JSONObject(login_Return);
			str_Return=obj.getString("QsAffixUploadResult");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return str_Return;
	}
	public void testDownLoad(){
		String filePath=Environment.getExternalStorageDirectory()+"/MobilePlat/";
		String strHttpPath=ServiceMap.PROTOCOL+MainApplication.getConfig().getServerAddress()+ServiceMap.Segment_DownloadAffic+"/llll";
		try{
			byte[] returnByte=NetUtil.getStreamForGet(strHttpPath,context);
			if(returnByte!=null)
			  FileBeanMakeUp.saveFileByByte(filePath,returnByte);
		}catch(Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
