package com.leading.mobileplat.plugin;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.Log;

public class PluginManager {
	private Context context=null;
	private String CurrentAPKName=null;
	private String CurrentShareUserId=null;
	public List<PluginBean> pluginList=null;

	public PluginManager(Context context) {
		this.context = context;
		this.CurrentAPKName = context.getApplicationInfo().packageName;
		//findPlugins();
	}

	public void findPlugins() {
		pluginList = new ArrayList<PluginBean>();
		PackageManager pm = context.getPackageManager();
		try {
			this.CurrentShareUserId=pm.getPackageInfo(this.CurrentAPKName, 0).sharedUserId;
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		List<PackageInfo> pkInfoList = pm.getInstalledPackages(PackageManager.GET_UNINSTALLED_PACKAGES);
		for (PackageInfo pkInfo : pkInfoList) {
			if(pkInfo.sharedUserId!=null&&this.CurrentShareUserId!=null){
				if (pkInfo.sharedUserId.equals(this.CurrentShareUserId)&& !pkInfo.packageName.equals(this.CurrentAPKName)
						) {
					PluginBean plugin = new PluginBean();
					plugin.setApkName(pkInfo.packageName);
					plugin.setVersion(pkInfo.versionName);
					plugin.setApkIcon(pkInfo.applicationInfo.loadIcon(pm));
					plugin.setApkLable(pkInfo.applicationInfo.loadLabel(pm).toString());
					pluginList.add(plugin);
	Log.v("findPlugins",pkInfo.packageName);
				}
			}
		}
	}
	public void UnInstallPlugins(){
		
	}
	public PluginBean getCurrentBean(){
		PackageManager pm = this.context.getPackageManager();    
        PackageInfo pkInfo=null;
        PluginBean plugin=null;
		try {
			pkInfo = pm.getPackageInfo(this.context.getPackageName(), 0);
			
			plugin = new PluginBean();
			plugin.setApkName(this.CurrentAPKName);
			plugin.setVersion(pkInfo.versionName);
			plugin.setApkIcon(pkInfo.applicationInfo.loadIcon(pm));
			plugin.setApkLable(pkInfo.applicationInfo.loadLabel(pm).toString());
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		return plugin;
	}
}
