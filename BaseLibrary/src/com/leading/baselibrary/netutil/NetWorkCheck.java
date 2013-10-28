package com.leading.baselibrary.netutil;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.widget.Toast;

/**
 * 用于检测网络，设置网络状态变化的监听
 * @author: skingz
 */
public class NetWorkCheck {
	private Context context;
	private NetMonitor netMonitor;
	private Boolean isAvailable=false;
	/**
	 * 获取当前网络状态
	 * @return true 可用，false 不可用
	 */
	public Boolean getState(){CheckState();return isAvailable;}
	
	/**
	 * 单例模式 返回本对象
	 */
	private static NetWorkCheck sInstance=null;
	/**
	 * 得到网络检查类的单实例对象
	 * @param _context  寄用上下文
	 * @return 返回本类型的单实例
	 */
	public static NetWorkCheck getInstance(Context _context){
		synchronized (NetWorkCheck.class ){
			if (sInstance == null) {  
	            sInstance = new NetWorkCheck(_context);  
	        }  
		}
		return sInstance;
	}
	private NetWorkCheck(Context _context){
		this.context=_context;
		netMonitor=new NetMonitor();
		IntentFilter filter=new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
		context.registerReceiver(netMonitor, filter);
	}
	/**
	 * 检查网络状态，是否有网络可用
	 */
	public void CheckState(){
		ConnectivityManager connectManager=(ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo=connectManager.getActiveNetworkInfo();
		if(networkInfo.isAvailable()){
			//Toast.makeText(context, "网络通畅！", Toast.LENGTH_LONG).show();
			isAvailable=true;
		}else {
			Toast.makeText(context, "当前网络不可用！", Toast.LENGTH_LONG).show();
			isAvailable=false;
		}
		/*
		if(State.CONNECTED==connectManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState()){
			Toast.makeText(context, "Mobile net is connected", Toast.LENGTH_LONG).show();
			isAvailable=true;
		}
		if(State.CONNECTED==connectManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState()){
			Toast.makeText(context, "WIFI is connected", Toast.LENGTH_LONG).show();
			isAvailable=true;
		}
		*/
	}
	/**
	 * 销毁事件，取消监听
	 */
	public void onDestroy(){context.unregisterReceiver(netMonitor);sInstance=null;}
	/**
	 * 设置网络状态改变的监听
	 * @author skingz
	 */
	class NetMonitor extends BroadcastReceiver{
		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			Bundle data=intent.getExtras();
			boolean unAvailable=data.getBoolean(ConnectivityManager.EXTRA_NO_CONNECTIVITY);
			if(unAvailable){
//				CheckState();
				isAvailable=ConnectionUtils.isNet(context);
			}
		}
		
	} 
}
