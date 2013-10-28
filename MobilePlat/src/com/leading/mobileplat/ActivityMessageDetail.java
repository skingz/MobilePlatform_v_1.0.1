package com.leading.mobileplat;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.leading.baselibrary.ui.ActivityTemplat;
import com.leading.xmpp_client.tools.Constants;

public class ActivityMessageDetail extends ActivityTemplat {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addSelfContentView(R.layout.activity_message_detail);
		initPage();
	}
	@Override
	public void initPage(){
		 Intent intent = getIntent();
	     String notificationTitle = intent.getStringExtra(Constants.NOTIFICATION_TITLE);
	     String notificationMessage = intent.getStringExtra(Constants.NOTIFICATION_MESSAGE);
	     final String notificationUri = intent.getStringExtra(Constants.NOTIFICATION_URI);
	     final String notificationViewApp = intent.getStringExtra(Constants.NOTIFICATION_VIEWAPP);
	     final String notificationFSIID=intent.getStringExtra(Constants.NOTIFICATION_FSIID);
	     
	     TextView tv_Message=(TextView)this.findViewById(R.id.tv_MessageDetail);
	     tv_Message.setText(notificationMessage);
	     this.getTitleTextView().setText(notificationTitle);
	     
	     this.getTitleLeftBtn().setVisibility(View.VISIBLE);
	     this.getTitleLeftBtn().setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				ActivityMessageDetail.this.finish();
			}});
	     if(com.leading.baselibrary.util.StringUtils.isNotNull(notificationViewApp)&&com.leading.baselibrary.util.StringUtils.isNotNull(notificationUri)){
		     this.getTitleRightBtn().setVisibility(View.VISIBLE);
		     this.getTitleRightBtn().setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View arg0) {
					try{
						ComponentName toActivity = new ComponentName(notificationViewApp,notificationUri);
						Intent intent=new Intent();
						intent.setComponent(toActivity);
						intent.putExtra("fsiid", notificationFSIID);
						intent.setAction(notificationUri);
						startActivity(intent);
						ActivityMessageDetail.this.finish();
					}catch(Exception ex){
						ex.printStackTrace();
					}
			}});
	     }
	}
}
