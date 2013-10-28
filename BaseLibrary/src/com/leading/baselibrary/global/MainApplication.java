package com.leading.baselibrary.global;

import java.util.HashMap;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.Context;

import com.leading.baselibrary.entity.ConfigureEntity;
import com.leading.baselibrary.util.ConfigureUtil;

/**
 * 全局Application.
 * @author JianTao.tu
 *
 */
public class MainApplication extends Application {

	protected static Context ctx;
	
	protected static ConfigureEntity config;
	
	protected static Map<Long,Activity> activitys;

	@Override
	public void onCreate() {
		super.onCreate();
		ctx = this;
	}
	
	public static Context getContext() {
		return ctx;
	}

	@Override
	public void onLowMemory() {
		// TODO Auto-generated method stub
		super.onLowMemory();
	}
	
	public static ConfigureEntity getConfig(){
		if(config==null)
			config=ConfigureUtil.getConfigureUtil().get(getContext());
		return config;
	}
	
	public static void setConfig(ConfigureEntity config) {
		MainApplication.config = config;
	}
	
	public static Map<Long,Activity> getActivitys() {
		return activitys;
	}

	public static void setActivitys(Map<Long,Activity> activitys) {
		MainApplication.activitys = activitys;
	}
	
	@SuppressLint("UseSparseArrays")
	public static void addActivity(Long key,Activity activity){
		if(activitys==null){
			activitys=new HashMap<Long,Activity>();
			activitys.put(key, activity);
		}
		else
			activitys.put(key, activity);
	}
	
	public static void removeActivity(Long key){
		activitys.remove(key);
	}
}
