package com.leading.mobileplat.ui;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.leading.baselibrary.entity.ConfigureEntity;
import com.leading.baselibrary.global.MainApplication;
import com.leading.baselibrary.util.ConfigureUtil;
import com.leading.mobileplat.ActivityAbout;
import com.leading.mobileplat.ActivityLogin;
import com.leading.mobileplat.R;
import com.leading.xmpp_client.server.ServiceManager;
import com.leading.xmpp_client.tools.Constants;


public class WidgetSetting extends RelativeLayout implements OnClickListener{

	/**
	 * 消息显示条数数据存取值数组
	 */
	private static final int[] messageArray={50,100,200,500};
	
	/**
	 * 消息提醒时长数据存取值数组
	 */
	private static final int[] dayArray={3,7,15,60,0};
	
	/**
	 * 消息显示条数索引--配合messageArray使用
	 */
	private int messageIndex;
	
	/**
	 * 消息提醒时常--配合dayArray使用
	 */
	private int dayIndex;
	
	/**
	 * 服务器输入文本框
	 */
	private EditText etServerIp;
	
	/**
	 * 第一行显示用户信息的RelativeLayout
	 */
	private RelativeLayout relativeUser;

	/**
	 * 显示用户名的RelativeLayout
	 */
	private RelativeLayout relativeUsername;

	/**
	 * 显示原有密码的RelativeLayout
	 */
	private RelativeLayout relativePassword;
	
	/**
	 * 显示新密码的RelativeLayout
	 */
	private RelativeLayout relativeSettingNewPassword;

	/**
	 * 显示确认密码的RelativeLayout
	 */
	private RelativeLayout relativeAffirmPassword;

	
	/**
	 * 显示消息条数的RelativeLayout
	 */
	private RelativeLayout relativeSettingShownumber;
	
	private TextView tvSettingShowNumebr;
	
	private RelativeLayout relativeSettingCheckUpdate;
	private RelativeLayout relativeSettingAbout;
	private RelativeLayout relativeSettingFeedback;
	
	private CheckBox cbNoticeSound;
	private CheckBox cbNoticeVibrate;
	private CheckBox cbNoticeBackSend;
	private Button btn_Logout;
	
	private SharedPreferences sharedPrefs;
	private Context context;
	public WidgetSetting(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater.from(context).inflate(R.layout.widget_setting, this,true);
		this.context=context;
		sharedPrefs = context.getSharedPreferences(Constants.SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
		
		relativeUser = (RelativeLayout) findViewById(R.id.relative_setting_user);

		tvSettingShowNumebr=(TextView)findViewById(R.id.textview_setting_show_numebr);
		btn_Logout=(Button)findViewById(R.id.btn_Logout);

		relativeSettingShownumber=(RelativeLayout)findViewById(R.id.relative_setting_shownumber);
		relativeSettingCheckUpdate=(RelativeLayout)findViewById(R.id.setting_check_update);
		relativeSettingFeedback=(RelativeLayout)findViewById(R.id.relative_setting_feedback);
		relativeSettingAbout=(RelativeLayout)findViewById(R.id.setting_about);
		
		cbNoticeSound=(CheckBox)findViewById(R.id.relative_setting_message_notice_Sound_checkBox);
		cbNoticeVibrate=(CheckBox)findViewById(R.id.relative_setting_message_notice_Vibrate_checkBox);
		cbNoticeBackSend=(CheckBox)findViewById(R.id.relative_setting_message_notice_BackSend_checkBox);
		boolean cbNoticeSoundValue=sharedPrefs.getBoolean(Constants.SETTINGS_SOUND_ENABLED, true);
		boolean cbNoticeVibrateValue=sharedPrefs.getBoolean(Constants.SETTINGS_VIBRATE_ENABLED, true);
		boolean cbNoticeBackSendValue=sharedPrefs.getBoolean(Constants.SETTINGS_HOLD_SERVICE_ENABLED, false);
		cbNoticeSound.setChecked(cbNoticeSoundValue);
		cbNoticeVibrate.setChecked(cbNoticeVibrateValue);
		cbNoticeBackSend.setChecked(cbNoticeBackSendValue);
		int tvNoticeShowNum=sharedPrefs.getInt(Constants.SETTINGS_HISTORYLY_MESSAGE_NUMBER, 200);
		tvSettingShowNumebr.setText(String.valueOf(tvNoticeShowNum));
		
		relativeSettingShownumber.setOnClickListener(this);
		relativeUser.setOnClickListener(this);
		relativeSettingFeedback.setOnClickListener(this);
		relativeSettingCheckUpdate.setOnClickListener(this);
		relativeSettingAbout.setOnClickListener(this);
		btn_Logout.setOnClickListener(this);
		
		cbNoticeSound.setOnCheckedChangeListener(new OnCheckedChangeListener(){
			@Override
			public void onCheckedChanged(CompoundButton cbtn, boolean cbValue) {
				Editor editor = sharedPrefs.edit();
				editor.putBoolean(Constants.SETTINGS_SOUND_ENABLED, cbValue);	
				editor.commit();
			}
		});
		cbNoticeVibrate.setOnCheckedChangeListener(new OnCheckedChangeListener(){
			@Override
			public void onCheckedChanged(CompoundButton cbtn, boolean cbValue) {
				Editor editor = sharedPrefs.edit();
				editor.putBoolean(Constants.SETTINGS_VIBRATE_ENABLED, cbValue);	
				editor.commit();			
			}
		});
		cbNoticeBackSend.setOnCheckedChangeListener(new OnCheckedChangeListener(){
			@Override
			public void onCheckedChanged(CompoundButton cbtn, boolean cbValue) {
				Editor editor = sharedPrefs.edit();
				editor.putBoolean(Constants.SETTINGS_HOLD_SERVICE_ENABLED, cbValue);	
				editor.commit();			
			}
		});
	}

	@Override
	public void onClick(View v) {
	  switch(v.getId()){
		case R.id.relative_setting_user: 
			Intent intent=new Intent(getContext(), WidgetSettingUser.class);
			getContext().startActivity(intent); 
			break;
			
		case R.id.relative_setting_shownumber:
			  final String[] items = getResources().getStringArray(R.array.show_num);  
              new AlertDialog.Builder(getContext()).setTitle("请点击选择消息条数:").setItems(items, new DialogInterface.OnClickListener() {  
                          public void onClick(DialogInterface dialog,int which) {
                        	  messageIndex=which;
                        	  tvSettingShowNumebr.setText(items[which]);
                        	  Editor editor = sharedPrefs.edit();
              					editor.putInt(Constants.SETTINGS_HISTORYLY_MESSAGE_NUMBER,Integer.parseInt(items[which]));	
              					editor.commit();
                          }  
                      }).show();  break;
		case R.id.setting_about: 
			Intent intent_3=new Intent(getContext(),ActivityAbout.class);
			getContext().startActivity(intent_3);
			break;
		case R.id.relative_setting_feedback:
			Intent intent_2=new Intent(getContext(),WidgetSettingFeedback.class);
			getContext().startActivity(intent_2);
			break;
		case R.id.setting_check_update:
			Toast.makeText(getContext(), "当前已是最新版本!", 0).show();
			break;
		case R.id.btn_Logout:userLogout();break;			
		default:break;
		}

	}
	
	public void validateIp(){
		Pattern patterm=Pattern.compile("^((0|(?:[1-9]\\d{0,1})|(?:1\\d{2})|(?:2[0-4]\\d)|(?:25[0-5]))\\.){3}((?:[1-9]\\d{0,1})|(?:1\\d{2})|(?:2[0-4]\\d)|(?:25[0-5]))$");
		String[] arrayStr=etServerIp.getText().toString().trim().split("\\:");
		Matcher matcher=patterm.matcher(arrayStr[0]);
		if(!matcher.matches())alert("IP格式錯誤！");
		else if(arrayStr.length>2){
			alert("输入的IP地址格式错误！");
			return;
		}else if(arrayStr.length>1){
			try {
				if(Integer.parseInt(arrayStr[0])>65535)
					alert("端口号错误格式必须为65535以下数字！");
					return;
			} catch (NumberFormatException e) {
					alert("端口号错误格式必须为65535以下数字！");
					return;
			}
		}
		
	}
	//登陆界面 用户名密码置空，消息推送服务 停滞
	private void userLogout(){
		ConfigureEntity ce=com.leading.baselibrary.global.MainApplication.getConfig();
		ce.setUsername("");
		ce.setPassword("");
		MainApplication.setConfig(ce);
		ConfigureUtil.getConfigureUtil().save(context, ce);
		
		ServiceManager ServerInstance=ServiceManager.getXMPPServerInstance(getContext(),null);
		ServerInstance.stopService();
		
		if(MainApplication.getActivitys().size()>0){
			for(Activity activity : MainApplication.getActivitys().values()){
				if(activity!=null && !activity.isFinishing())
					activity.finish();
			}
		}
		
		Intent it=new Intent(getContext(),ActivityLogin.class);
		getContext().startActivity(it);
		
		
	}
	public void alert(String message){
		Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
	}

}
