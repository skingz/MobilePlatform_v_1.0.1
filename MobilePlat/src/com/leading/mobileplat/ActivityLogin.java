package com.leading.mobileplat;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.leading.baselibrary.entity.ConfigureEntity;
import com.leading.baselibrary.global.MainApplication;
import com.leading.baselibrary.ui.BackgourdSwitch;
import com.leading.baselibrary.ui.DialogAlertUtil;
import com.leading.baselibrary.ui.Loading;
import com.leading.baselibrary.util.ConfigureUtil;
import com.leading.mobileplat.entity.UserBo;
import com.leading.mobileplat.mutual.ServiceHelper;
import com.leading.xmpp_client.server.ServiceManager;

/**
 * 登录页面.
 * 
 * @author Jiantao.tu
 * 
 */
@SuppressLint("HandlerLeak")
@TargetApi(Build.VERSION_CODES.GINGERBREAD)
public class ActivityLogin extends Activity {
	public static final Long activityId = 0625L;
	// 用户名.
	private EditText edtUsername;
	// 用户密码.
	private EditText edtPassword;
	// 服务器地址.
	private EditText editServerAddress;
	// 是否自动登录.
	private CheckBox chkAutoLogin;
	// 是否记住密码.
	private CheckBox chkRemenberPwd;
	// 用户配置信息实体.
	private ConfigureEntity configEntity;
	// 登录按钮.
	private Button btnLogin;
	// 配置文件工具处理对象.
	private ConfigureUtil config;
	// 初始化Loading加载图标
	private Loading loading = Loading.getLoading(ActivityLogin.this,
			R.id.activity_login);

	// 子线程Handler负责处理与服务器交互的登录功能
	private ChildHandler childHandler = null;

	/**
	 * 主线程Handler负责与ChildHanlder子线程交互由子线程通知进行调用 接受子线程的消息进行相应处理.
	 */
	private Handler mainHandler = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		MainApplication.addActivity(activityId, this);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_login);
		btnLogin = (Button) findViewById(R.id.login_btn);
		// 设置图片按钮按下时的效果
		BackgourdSwitch.setButtonFocusChanged(btnLogin);
		initialize();// 初始化控件
		mainHandler = new Handler() {
			public void handleMessage(Message msg) {
				UserBo user = (UserBo) msg.obj;
				loading.closeLoading();
				btnLogin.setEnabled(true);
				if (user != null && "null".equals(user.getError())) {
					DialogAlertUtil.showToast(ActivityLogin.this,
							"用户名或者密码有误,请重新登录！");
				} else if (user != null) {
					// 把配置对象保存到全局Application里方便程序随时读取
					MainApplication.setConfig(configEntity);
					Intent it = new Intent(ActivityLogin.this,ActivityProgram.class);
					startActivityForResult(it, 0);
					settingConfig(true, user.getUserName(), user.getUserId());
					startXMPP(user);
					ActivityLogin.this.finish();
				}
				super.handleMessage(msg);
			}
		};
		btnLogin.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				btnLogin.setEnabled(false);
				if (edtUsername.getText().toString().trim().isEmpty()) {
					DialogAlertUtil.showToast(ActivityLogin.this, "用户名不能为空！");
				} else if (editServerAddress.getText().toString().trim()
						.isEmpty()) {
					DialogAlertUtil.showToast(ActivityLogin.this, "服务器地址不能为空！");
				} else {
					settingConfig(false, null, null);
					loginBegin();
				}
				btnLogin.setEnabled(true);
			}
		});

	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	private void loginBegin() {
		loading.openLoading();
		HandlerThread childThread = new HandlerThread("child_thread");
		childThread.start();
		childHandler = new ChildHandler(childThread.getLooper());
		Message message = childHandler.obtainMessage();
		message.sendToTarget();
	}

	/**
	 * 登录页面初始化.
	 * 
	 * @author JianTao.tu
	 */
	public void initialize() {
		edtUsername = (EditText) findViewById(R.id.login_edt_username);
		edtPassword = (EditText) findViewById(R.id.login_edt_password);
		editServerAddress = (EditText) findViewById(R.id.login_edit_serverAddress);
		chkAutoLogin = (CheckBox) findViewById(R.id.login_chk_AutoLogin);
		chkRemenberPwd = (CheckBox) findViewById(R.id.login_chk_RemenberPwd);
		config = ConfigureUtil.getConfigureUtil();
		configEntity = config.get(ActivityLogin.this);

		if (configEntity.getMemoryPassword())
			edtPassword.setText(configEntity.getPassword());
		if (configEntity.getAutoLogin())
			edtUsername.setText(configEntity.getUsername());

		chkAutoLogin.setChecked(configEntity.getAutoLogin());
		chkRemenberPwd.setChecked(configEntity.getMemoryPassword());
		editServerAddress.setText(configEntity.getServerAddress());
		// 如果有缓存 尝试自动登录 (需要延时执行)
		if (configEntity.getAutoLogin()
				&& configEntity.getServerAddress() != "") {
			// loginBegin();
		}
		editServerAddress.setText("119.255.48.198:81");
	}

	/**
	 * 子线程处理与服务器通信进行登录.
	 * 
	 * @author Jiantao.tu
	 * 
	 */
	class ChildHandler extends Handler {
		public ChildHandler() {
		}

		public ChildHandler(Looper looper) {
			super(looper);
		}

		public void handleMessage(Message msg) {
			ServiceHelper serviceHelper = new ServiceHelper(ActivityLogin.this);
			UserBo user = serviceHelper.login(configEntity.getServerAddress(),
					edtUsername.getText().toString(), edtPassword.getText()
							.toString());
			Message message = mainHandler.obtainMessage();
			message.obj = user;
			message.sendToTarget();
		}

	}

	/**
	 * 释放子线程的Looper.
	 */
	public void onDestroy() {
		super.onDestroy();
		MainApplication.removeActivity(activityId);
		if (childHandler != null) {
			childHandler.getLooper().quit();
		}
	}

	/**
	 * 设置配置信息.
	 * 
	 * @author JianTao.tu
	 * @param flag
	 *            是否登录成功
	 */
	public void settingConfig(boolean flag, String fullName, String userId) {
		configEntity.setServerAddress(editServerAddress.getText().toString()
				.trim());
		configEntity.setAutoLogin(chkAutoLogin.isChecked());
		configEntity.setMemoryPassword(chkRemenberPwd.isChecked());
		if (flag) {
			configEntity.setUsername(edtUsername.getText().toString().trim());
			configEntity.setPassword(edtPassword.getText().toString());
			configEntity.setFullName(fullName);
			configEntity.setUserId(userId);
			config.save(this, configEntity);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		// getMenuInflater().inflate(R.menu.login, menu);
		menu.add(Menu.NONE, Menu.FIRST + 1, 5, R.string.action_exit_all)
				.setIcon(android.R.drawable.ic_menu_close_clear_cancel);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case Menu.FIRST + 1:
			this.finish();
			break;
		}
		return false;
	}

	/**
	 * 启动消息服务
	 */
	private void startXMPP(UserBo user) {
		// String serverAdress=configEntity.getServerAddress();
		// String serverIP=serverAdress.split(":")[0];
		ServiceManager serviceManager = ServiceManager.getXMPPServerInstance(
				this, "com.leading.mobileplat.ActivityMessageDetail");
		serviceManager.setNotificationIcon(R.drawable.ic_launcher);
		// serviceManager.setUserInfo(edtUsername.getText().toString(),serverIP);
		serviceManager.setUserInfo(MainApplication.getConfig().getUserId(),
				edtPassword.getText().toString(), user.getXMPP_IP(),
				user.getXMPP_Port());
		serviceManager.stopService();
		serviceManager.startService();
	}


}
