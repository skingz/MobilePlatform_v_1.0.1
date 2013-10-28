package com.leading.mobileplat;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.leading.baselibrary.global.MainApplication;
import com.leading.baselibrary.ui.ActivityTemplat;
import com.leading.baselibrary.ui.DialogModal;
import com.leading.mobileplat.ui.BottomBar;
import com.leading.mobileplat.ui.WidgetAppMger;
import com.leading.mobileplat.ui.WidgetHome;
import com.leading.mobileplat.ui.WidgetMsg;
import com.leading.mobileplat.ui.WidgetSetting;
import com.leading.xmpp_client.tools.Constants;

public class ActivityProgram extends ActivityTemplat {
	public static final Long activityId=0626L;
	private TextView tv;
	public WidgetMsg widgetMsg=null;
	private WidgetHome widgetHome=null;
	private WidgetSetting widgetSetting=null;
	private WidgetAppMger widgetAppMger=null;
	private BottomBar btmBar;
	private RelativeLayout LayoutArea;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		MainApplication.addActivity(activityId, this);
		super.addSelfContentView(R.layout.activity_program);
		super.getTitleTextView().setText("移动办公");
		
		IntentFilter filter = new IntentFilter();  
		filter.addAction(Constants.ACTION_SHOW_NOTIFICATION);   
		registerReceiver(MessageReceiver, filter);  
		
		btmBar=(BottomBar)this.findViewById(R.id.homeBottomBar);
		InitBottomListener();
		LayoutArea=(RelativeLayout)this.findViewById(R.id.Main_Content);
		if(MainApplication.IfConfigureEntity())
			initPage(new WidgetMsg(ActivityProgram.this,null),btmBar.tv_Msg);
		else //安装卸载程序后 application被清空...
			initPage(new WidgetAppMger(ActivityProgram.this, null),btmBar.tv_AppMger);
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode==KeyEvent.KEYCODE_BACK&& event.getRepeatCount() == 0){
			
			DialogModal md=new DialogModal(this);
			md.showMessage("确定退出么？");
			md.btnOK.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View arg0) {
					ActivityProgram.this.exitApp();
					//ActivityProgram.this.finish();
				}
				
			});
		}
		return false;
	}
	 @Override
	public boolean onCreateOptionsMenu(Menu menu) {return true;}
	 @Override
	protected void onNewIntent(Intent intent) {
	    super.onNewIntent(intent);
	    if(widgetMsg!=null)initPage(widgetMsg,btmBar.tv_Msg);
	    else if(widgetHome!=null)initPage(widgetHome,btmBar.tv_Home);
	    else if(widgetAppMger!=null)initPage(widgetAppMger,btmBar.tv_AppMger);
	    else initPage(widgetSetting,btmBar.tv_Setting);
	}

	private void initPage(ViewGroup widgetView,TextView currentTV){
		btmBar.setCurrentIcon(currentTV);
		LayoutArea.removeAllViews();
		LayoutArea.addView(widgetView);
		this.getTitleTextView().setText(currentTV.getText());
	}
	protected void InitBottomListener(){
		btmBar.tv_Msg.setOnClickListener(BottomBarOnClick);
		btmBar.tv_AppMger.setOnClickListener(BottomBarOnClick);
		btmBar.tv_Home.setOnClickListener(BottomBarOnClick);
		btmBar.tv_Setting.setOnClickListener(BottomBarOnClick);
	}
	/**
	 * 对于BottomBar的按钮事件
	 */
	View.OnClickListener BottomBarOnClick=new View.OnClickListener(){
		@Override
		public void onClick(View v) {
			switch(v.getId()){
			case R.id.imgBtnMsg:
				widgetMsg=new WidgetMsg(ActivityProgram.this,null);
				initPage(widgetMsg,btmBar.tv_Msg);
				widgetHome=null;widgetAppMger=null;
				btmBar.hideNewMsgSite();
				break;
			case R.id.imgBtnHome:
				widgetHome=new WidgetHome(ActivityProgram.this,null);
				initPage(widgetHome,btmBar.tv_Home);
				widgetMsg=null;widgetAppMger=null;
				break;
			case R.id.imgBtnAppManger:
				widgetAppMger=new WidgetAppMger(ActivityProgram.this, null);
				initPage(widgetAppMger,btmBar.tv_AppMger);
				widgetHome=null;widgetMsg=null;
				break;
			case R.id.imgBtnSetting:
				if(widgetSetting==null)widgetSetting=new WidgetSetting(ActivityProgram.this,null);
				initPage(widgetSetting,btmBar.tv_Setting);
				widgetHome=null;widgetAppMger=null;widgetMsg=null;
				break;
			}
		}
	};
	
// this is a test function
	public void getPlugin(){
		try {
			Context context=this.createPackageContext("com.leading.mobileplat.businesstake", Context.CONTEXT_INCLUDE_CODE|Context.CONTEXT_IGNORE_SECURITY);
			Class<?> clazz=context.getClassLoader().loadClass("com.leading.mobileplat.businesstake.PluginDescrible");
			Object obj=(Object)clazz.newInstance();
			String mm=(String) clazz.getMethod("getPluginDescrible").invoke(obj);
			tv.setText(mm);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@Override
	public void onDestroy(){
		this.unregisterReceiver(MessageReceiver);
		MainApplication.removeActivity(activityId);
		super.onDestroy();
	}
	/**
	 * 响应新消息
	 */
	private void ResponseNewMsg(){
		if(widgetMsg==null)btmBar.showNewMsgSite();
		else {
			widgetMsg=new WidgetMsg(ActivityProgram.this,null);
			initPage(widgetMsg,btmBar.tv_Msg);
		}
	}
	private BroadcastReceiver MessageReceiver =new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			ResponseNewMsg();
			Log.v("BroadcastReceiver","dd");
		}
	};
	
}
