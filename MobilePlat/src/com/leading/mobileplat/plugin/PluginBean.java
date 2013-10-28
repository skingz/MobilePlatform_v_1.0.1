package com.leading.mobileplat.plugin;

import java.io.File;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;

public class PluginBean {
	private String apkName;
	private String version;
	private String apkLable;
	private Drawable apkIcon;
	
	public void setApkName(String apkName){this.apkName=apkName;}
	public void setVersion(String version){this.version=version;}
	public void setApkLable(String apkLable){this.apkLable=apkLable;}
	public void setApkIcon(Drawable apkDrawable){this.apkIcon=apkDrawable;}
	
	public String getApkName(){return this.apkName;}
	public String getApkLable(){return this.apkLable;}
	public String getVersion(){return this.version;}
	public Drawable getIcon(){return this.apkIcon;}
	
	/**
	 * uninstall ApK
	 * @param context
	 */
	public void uninstall(Context context){
		Uri packageURI = Uri.parse("package:" + this.apkName);  
		Intent uninstallIntent = new Intent(Intent.ACTION_DELETE, packageURI);  
		context.startActivity(uninstallIntent);  
	}
	
	/**
	 * installAPK
	 * @param context
	 * @param path
	 */
	public void installAPK(Context context,String path){
		Uri uri = Uri.fromFile(new File(path));
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setDataAndType(uri, "application/vnd.android.package-archive");
		context.startActivity(intent);
	}
}
