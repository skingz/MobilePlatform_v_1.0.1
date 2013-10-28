package com.leading.xmpp_client.server;

import java.util.Properties;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

import com.leading.xmpp_client.NotificationSettingsActivity;
import com.leading.xmpp_client.tools.Constants;
import com.leading.xmpp_client.tools.LogUtil;

public class ServiceManager {
	private static final String LOGTAG = LogUtil.makeLogTag(ServiceManager.class);

    private Context context;
    private SharedPreferences sharedPrefs;
    private Properties props;
    private String version = "0.5.0";
    private String apiKey;
    private String callbackActivityPackageName;
    private String callbackActivityClassName;
    
    private String userName;
    private String userPwd;
    
    public static ServiceManager XMPPServerInstance=null;
    public static ServiceManager getXMPPServerInstance(Context context,String showDetailActivity){
    	synchronized (ServiceManager.class ){
			if (XMPPServerInstance == null) {  
				XMPPServerInstance = new ServiceManager(context,showDetailActivity);  
	        }  
		}
		return XMPPServerInstance;
    }
    
    
    private ServiceManager(Context context,String showDetailActivity) {
        this.context = context;

        if (context instanceof Activity) {
            Log.i(LOGTAG, "Callback Activity...");
            Activity callbackActivity = (Activity) context;
            callbackActivityPackageName = callbackActivity.getPackageName();
            callbackActivityClassName = callbackActivity.getClass().getName();
        }

        //        apiKey = getMetaDataValue("ANDROIDPN_API_KEY");
        //        Log.i(LOGTAG, "apiKey=" + apiKey);
        //        //        if (apiKey == null) {
        //        //            Log.e(LOGTAG, "Please set the androidpn api key in the manifest file.");
        //        //            throw new RuntimeException();
        //        //        }

        props = loadProperties();
        apiKey = props.getProperty("apiKey", "1234567890");
        Log.i(LOGTAG, "apiKey=" + apiKey);
        sharedPrefs = context.getSharedPreferences(Constants.SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
        Editor editor = sharedPrefs.edit();
        editor.putString(Constants.API_KEY, apiKey);
        editor.putString(Constants.VERSION, version);
        editor.putString(Constants.CALLBACK_ACTIVITY_PACKAGE_NAME,callbackActivityPackageName);
        editor.putString(Constants.CALLBACK_ACTIVITY_CLASS_NAME,callbackActivityClassName);
        editor.putString(Constants.SHOW_DETAIL_ACTIVITY,showDetailActivity);
        editor.commit();
        // Log.i(LOGTAG, "sharedPrefs=" + sharedPrefs.toString());
    }

    public void startService() {
        Thread serviceThread = new Thread(new Runnable() {
            @Override
            public void run() {
                Intent intent = NotificationService.getIntent();
                intent.putExtra(Constants.XMPP_USERNAME, ServiceManager.this.userName);
                intent.putExtra(Constants.XMPP_PASSWORD, ServiceManager.this.userPwd);
                context.startService(intent);
            }
        });
        serviceThread.start();
    }

    public void stopService() {
        Intent intent = NotificationService.getIntent();
        context.stopService(intent);
    }

    private Properties loadProperties() {

        Properties props = new Properties();
        try {
            int id = context.getResources().getIdentifier("androidpn", "raw",context.getPackageName());
            props.load(context.getResources().openRawResource(id));
        } catch (Exception e) {
            Log.e(LOGTAG, "Could not find the properties file.", e);
        }
        return props;
    }


    public void setNotificationIcon(int iconId) {
        Editor editor = sharedPrefs.edit();
        editor.putInt(Constants.NOTIFICATION_ICON, iconId);
        editor.commit();
    }

    public static void viewNotificationSettings(Context context) {
        Intent intent = new Intent().setClass(context,NotificationSettingsActivity.class);
        context.startActivity(intent);
    }

    public void setUserInfo(String userName,String userPwd,String xmppHost,String xmppPort){
    	this.userName=userName;
    	this.userPwd=userPwd;
        Log.i(LOGTAG, "xmppHost=" + xmppHost);
        Log.i(LOGTAG, "xmppPort=" + xmppPort);
    	Editor editor =sharedPrefs.edit();
    	editor.putString(Constants.XMPP_HOST, xmppHost);
        editor.putInt(Constants.XMPP_PORT, Integer.parseInt(xmppPort));
    	editor.remove(Constants.XMPP_USERNAME);
    	editor.remove(Constants.XMPP_PASSWORD);
    	editor.commit();
    }
}
