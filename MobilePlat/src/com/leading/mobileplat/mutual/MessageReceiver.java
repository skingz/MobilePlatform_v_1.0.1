package com.leading.mobileplat.mutual;

import java.util.Date;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.leading.baselibrary.database.DataBaseManager;
import com.leading.baselibrary.database.bean.MsgBean;
import com.leading.baselibrary.database.bean.MsgGroup;
import com.leading.baselibrary.global.MainApplication;
import com.leading.baselibrary.util.DateUtil;
import com.leading.xmpp_client.tools.Constants;
import com.leading.xmpp_client.tools.LogUtil;

public class MessageReceiver extends BroadcastReceiver {
	private static final String LOGTAG = LogUtil.makeLogTag(MessageReceiver.class);
	@Override
	public void onReceive(Context context, Intent intent) {
		 Log.d(LOGTAG, "MessageReceiver.onReceive()...");
	        String action = intent.getAction();
	        Log.d(LOGTAG, "action=" + action);

	        if (Constants.ACTION_SHOW_NOTIFICATION.equals(action)) {

	            String notificationTitle = intent.getStringExtra(Constants.NOTIFICATION_TITLE);
	            String notificationMessage = intent.getStringExtra(Constants.NOTIFICATION_MESSAGE);
	            String notificationUri = intent.getStringExtra(Constants.NOTIFICATION_URI);
	            String notificationViewApp=intent.getStringExtra(Constants.NOTIFICATION_VIEWAPP);
	            String notificationBusinessInstanceId=intent.getStringExtra(Constants.NOTIFICATION_FSIID);

	            Log.d(LOGTAG, "notificationTitle=" + notificationTitle);	            
	            
	            DataBaseManager msgM = new DataBaseManager(context);
	            MsgGroup mg =msgM.getMsgGroup(notificationViewApp);
	            MsgBean mb = new MsgBean();
				mb.setMsgTitle(notificationTitle);
				mb.setReadState(false);
				mb.setMsgDetail(notificationMessage);
				mb.setRedrectUri(notificationUri);
				mb.setMsgPubTime(DateUtil.getDateToString(new Date(),DateUtil.YMDHM));
				mb.setBusinessInstanceid(notificationBusinessInstanceId);
				mb.setUserId(MainApplication.getConfig().getUserId());
				mb.setMsgGroup(mg);
				mg.addMsg(mb);
				
				msgM.insertMessage(mb);
	        }
		
	}

}
