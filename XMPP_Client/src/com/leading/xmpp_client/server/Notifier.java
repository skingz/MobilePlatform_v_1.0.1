package com.leading.xmpp_client.server;

import java.util.Random;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.leading.xmpp_client.NotificationDetailsActivity;
import com.leading.xmpp_client.tools.Constants;
import com.leading.xmpp_client.tools.LogUtil;

/** 
 * This class is to notify the user of messages with NotificationManager.
 *
 */
public class Notifier {

    private static final String LOGTAG = LogUtil.makeLogTag(Notifier.class);

    private static final Random random = new Random(System.currentTimeMillis());

    private Context context;

    private SharedPreferences sharedPrefs;

    private NotificationManager notificationManager;

    public Notifier(Context context) {
        this.context = context;
        this.sharedPrefs = context.getSharedPreferences(Constants.SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
        this.notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    }

    public void notify(String notificationId, String apiKey, String title,String message, String uri,String viewApp,String fsiid) {
        Log.d(LOGTAG, "notify()...");

        Log.d(LOGTAG, "notificationId=" + notificationId);
        Log.d(LOGTAG, "notificationApiKey=" + apiKey);
        Log.d(LOGTAG, "notificationTitle=" + title);
        Log.d(LOGTAG, "notificationMessage=" + message);
        Log.d(LOGTAG, "notificationUri=" + uri);
        Log.d(LOGTAG, "notificationViewApp=" + uri);

        if (isNotificationEnabled()) {
            // Show the toast
            if (isNotificationToastEnabled()) {
                Toast.makeText(context, message, Toast.LENGTH_LONG).show();
            }

            // Notification
            Notification notification = new Notification();
            notification.icon = getNotificationIcon();
            notification.defaults = Notification.DEFAULT_LIGHTS;
            if (isNotificationSoundEnabled()) {
                notification.defaults |= Notification.DEFAULT_SOUND;
            }
            if (isNotificationVibrateEnabled()) {
                notification.defaults |= Notification.DEFAULT_VIBRATE;
            }
            notification.flags |= Notification.FLAG_AUTO_CANCEL;
            notification.when = System.currentTimeMillis();
            notification.tickerText = message;

            //            Intent intent;
            //            if (uri != null
            //                    && uri.length() > 0
            //                    && (uri.startsWith("http:") || uri.startsWith("https:")
            //                            || uri.startsWith("tel:") || uri.startsWith("geo:"))) {
            //                intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
            //            } else {
            //                String callbackActivityPackageName = sharedPrefs.getString(
            //                        Constants.CALLBACK_ACTIVITY_PACKAGE_NAME, "");
            //                String callbackActivityClassName = sharedPrefs.getString(
            //                        Constants.CALLBACK_ACTIVITY_CLASS_NAME, "");
            //                intent = new Intent().setClassName(callbackActivityPackageName,
            //                        callbackActivityClassName);
            //                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            //                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            //            }
            
	        String showActivityClassName = getShowDetailActivity();
	        Intent intent;

			try {
				if (uri !=null&& uri.length() > 0 &&viewApp!=null&& viewApp.length()>0) {
					ComponentName toActivity = new ComponentName(viewApp, uri);
					intent = new Intent();
					intent.setComponent(toActivity);
					intent.setAction(uri);
				}
				else 
					intent = new Intent(context,Class.forName(showActivityClassName));
				
			} catch (Exception ex) {
				try{
					intent = new Intent(context,Class.forName(showActivityClassName));
				} catch (ClassNotFoundException e) {
					 Log.d(LOGTAG, "通知页面显示出错:" + e.toString());
					intent = new Intent(context,NotificationDetailsActivity.class);
				}
			}	
				
            intent.putExtra(Constants.NOTIFICATION_ID, notificationId);
            intent.putExtra(Constants.NOTIFICATION_API_KEY, apiKey);
            intent.putExtra(Constants.NOTIFICATION_TITLE, title);
            intent.putExtra(Constants.NOTIFICATION_MESSAGE, message);
            intent.putExtra(Constants.NOTIFICATION_URI, uri);
            intent.putExtra(Constants.NOTIFICATION_VIEWAPP, viewApp);
            intent.putExtra(Constants.NOTIFICATION_FSIID, fsiid);
            intent.putExtra("fsiid", fsiid);
            intent.putExtra("ifNotification", "1");
            
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            
            PendingIntent contentIntent = PendingIntent.getActivity(context, 0,intent, PendingIntent.FLAG_UPDATE_CURRENT);

            notification.setLatestEventInfo(context, title, message,contentIntent);
            notificationManager.notify(random.nextInt(), notification);

        } else {
            Log.w(LOGTAG, "Notificaitons disabled.");
        }
    }

    private int getNotificationIcon() {
        return sharedPrefs.getInt(Constants.NOTIFICATION_ICON, 0);
    }
    private String getShowDetailActivity(){
    	return sharedPrefs.getString(Constants.SHOW_DETAIL_ACTIVITY, NotificationDetailsActivity.class.getName());
    }

    private boolean isNotificationEnabled() {
        return sharedPrefs.getBoolean(Constants.SETTINGS_NOTIFICATION_ENABLED,true);
    }

    private boolean isNotificationSoundEnabled() {
        return sharedPrefs.getBoolean(Constants.SETTINGS_SOUND_ENABLED, true);
    }

    private boolean isNotificationVibrateEnabled() {
        return sharedPrefs.getBoolean(Constants.SETTINGS_VIBRATE_ENABLED, true);
    }

    private boolean isNotificationToastEnabled() {
        return sharedPrefs.getBoolean(Constants.SETTINGS_TOAST_ENABLED, false);
    }

}
