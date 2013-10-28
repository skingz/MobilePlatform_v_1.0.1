package com.leading.mobileplat.ui;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.leading.baselibrary.global.MainApplication;
import com.leading.baselibrary.ui.ActivityTemplat;
import com.leading.baselibrary.ui.DialogAlertUtil;
import com.leading.mobileplat.R;
import com.leading.mobileplat.mutual.ServiceHelper;

public class WidgetSettingUser extends ActivityTemplat implements
		OnClickListener {

	public static final int NONE = 0;
	public static final int PHOTOHRAPH = 1;// 拍照
	public static final int PHOTOZOOM = 2; // 缩放
	public static final int PHOTORESOULT = 3;// 结果
	public static String ImageName;


	private TextView tvUserName;
	private RelativeLayout rlHeadArea;
	private RelativeLayout rlChangePwdArea;
	private RelativeLayout rlChangePwdBtns;
	
	private PopupWindow popupWindow;

	private ImageButton imgbtnHead;

	private Button btnBeatimg;

	private Button btnLocalImg;
	//private ImageView imgArrow;

	private EditText editOldPassword;
	private EditText editNewPassword;
	private EditText editNewAffirmPassword;
	
	private Button btnEditPassword;
	private Button btnSavePwd;
	private Button btnCancelPwd;
	private Button btnSetHeadPic;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addSelfContentView(R.layout.widget_setting_user);
		//imgArrow=(ImageView)findViewById(R.id.setting_img_arrowhead);
		tvUserName = (TextView) findViewById(R.id.setting_tv_UserName);
		
		rlHeadArea=(RelativeLayout) findViewById(R.id.setting_relative_User_Area);
		rlChangePwdArea = (RelativeLayout) findViewById(R.id.relative_setting_password);
		rlChangePwdBtns = (RelativeLayout) findViewById(R.id.setting_modif_Pwd_btn);
		
		btnEditPassword = (Button) findViewById(R.id.setting_btn_editpassword);
		btnSavePwd=(Button)findViewById(R.id.setting_btn_savePassword);
		btnCancelPwd=(Button)findViewById(R.id.setting_btn_canclePassword);
		btnSetHeadPic=(Button)findViewById(R.id.setting_btn_sendHeadPic);
		
		editOldPassword=(EditText)findViewById(R.id.editText_setting_password_old);
		editNewPassword=(EditText)findViewById(R.id.editText_setting_new_password);
		editNewAffirmPassword=(EditText)findViewById(R.id.editText_setting_affirm_password);
		
		imgbtnHead = (ImageButton) findViewById(R.id.show_imgbtn_head);
	
		try {
			FileInputStream inputStream = this.openFileInput("head.png");
			Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
			imgbtnHead.setImageBitmap(bitmap);
			bitmap.recycle();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Spannable span = new SpannableString(MainApplication.getConfig().getFullName());

		span.setSpan(new ForegroundColorSpan(Color.BLUE), 0, span.length(),
				Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

		tvUserName.setText(span);

		rlHeadArea.setOnClickListener(this);
		btnSetHeadPic.setOnClickListener(this);
		btnEditPassword.setOnClickListener(this);
		imgbtnHead.setOnClickListener(this);
		btnSavePwd.setOnClickListener(this);
		btnCancelPwd.setOnClickListener(this);
		
		super.btnTitleLeft.setVisibility(View.VISIBLE);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.setting_relative_User_Area:// 第一行的点击事件
			relativeSettingBtn();
			break;
		case R.id.setting_btn_editpassword:// 修改密码按钮
			btnEditpasswordVisiable(true);
			break;
		case R.id.setting_btn_sendHeadPic: //修改头像
			break;
		case R.id.show_imgbtn_head:// 用户头像按钮
			initPopuptWindow();
			popupWindow.showAtLocation(findViewById(R.id.widget_setting_user),Gravity.BOTTOM, 0, 0);
			break;

		case R.id.setting_btn_canclePassword://
			btnEditpasswordVisiable(false);
			break;
		case R.id.setting_btn_savePassword:
			passwordValidate();
			break;
		default:
			break;
		}

	}

	/**
	 * 验证密码.
	 * @author JianTao.tu
	 */
	private void passwordValidate() {
		String passwordText = editOldPassword.getText().toString();
		String affirmPasswordText = editNewAffirmPassword.getText().toString();
		String newPasswordText = editNewPassword.getText().toString();
		if ("".equals(passwordText.trim())) {
			DialogAlertUtil.showToast(this.getApplicationContext(), "原密码需要填写!");
			return;
		}else if ("".equals(newPasswordText.trim())) {
			DialogAlertUtil.showToast(this.getApplicationContext(), "新密码需要填写!");
			return;
		}else if ("".equals(affirmPasswordText.trim())) {
			DialogAlertUtil.showToast(this.getApplicationContext(), "重复密码需要填写!");
			return;
		}else if (!newPasswordText.equals(affirmPasswordText)) {
			DialogAlertUtil.showToast(this.getApplicationContext(), "两次密码不一致!");
			return;
		}
		else {	
			ServiceHelper serviceHelper = new ServiceHelper(WidgetSettingUser.this);
			String re=serviceHelper.ModifyPwd(passwordText, affirmPasswordText);
			if(re.equals("1")){
				Toast.makeText(WidgetSettingUser.this, "修改成功！下次登陆时请用新密码!", Toast.LENGTH_SHORT).show();
				WidgetSettingUser.this.finish();
			}else{
				Toast.makeText(WidgetSettingUser.this, "修改失败!", Toast.LENGTH_SHORT).show();
			}
		}
		 
	}

	/**
	 * 展示隐藏的修改按钮与注销.
	 * 
	 * @author JianTao.tu
	 */
	private void relativeSettingBtn() {
		switch(rlChangePwdBtns.getVisibility()){
			case View.VISIBLE: rlChangePwdBtns.setVisibility(View.GONE);break;
			case View.GONE:rlChangePwdBtns.setVisibility(View.VISIBLE);break;
		}
		rlChangePwdArea.setVisibility(View.GONE);
	}

	/**
	 * 展示密码修改下拉页面.
	 * 
	 * @author JianTao.tu
	 */
	private void btnEditpasswordVisiable(boolean isShow) {
		if(isShow){
			rlChangePwdBtns.setVisibility(View.GONE);
			rlChangePwdArea.setVisibility(View.VISIBLE);
		}
		else {
			rlChangePwdBtns.setVisibility(View.VISIBLE);
			rlChangePwdArea.setVisibility(View.GONE);
		}
	}

	/**
	 * 创建PopupWindow
	 */
	protected void initPopuptWindow() {
		// TODO Auto-generated method stub
		View popupWindow_view = getLayoutInflater().inflate( // 获取自定义布局文件dialog.xml的视图
				R.layout.setting_popup_window_alert, null, false);
		// 使用下面的构造方法
		popupWindow = new PopupWindow(popupWindow_view,
				LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT, true);
		btnBeatimg = (Button) popupWindow_view
				.findViewById(R.id.setting_btn_beatimg);
		btnLocalImg = (Button) popupWindow_view
				.findViewById(R.id.setting_btn_local_img);
		btnBeatimg.setOnClickListener(new BatPhoto());
		btnLocalImg.setOnClickListener(new BatPhoto());
		// 这里注意 必须要有一个背景 ，有了背景后
		// 当你点击对话框外部的时候或者按了返回键的时候对话框就会消失，当然前提是使用的构造函数中Focusable为true
		popupWindow.setBackgroundDrawable(new BitmapDrawable());

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if (resultCode == NONE)
			return;
		// 拍照
		if (requestCode == PHOTOHRAPH) {
			// 设置文件保存路径这里放在跟目录下
			File picture = new File(Environment.getExternalStorageDirectory()
					+ ImageName);
			startPhotoZoom(Uri.fromFile(picture));
		}

		if (data == null)
			return;

		// 读取相册缩放图片
		if (requestCode == PHOTOZOOM) {
			startPhotoZoom(data.getData());
		}
		// 处理结果
		if (requestCode == PHOTORESOULT) {
			Bundle extras = data.getExtras();
			Intent intent = new Intent(WidgetSettingUser.this,
					SettingImgShow.class);
			if (extras != null) {
				intent.putExtras(extras);
			}
			startActivity(intent);
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	public static String getStringToday() {
		Date currentTime = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
		String dateString = formatter.format(currentTime);
		return dateString;
	}

	private class BatPhoto implements OnClickListener {
		@Override
		public void onClick(View v) {
			if (v.getId() == R.id.setting_btn_beatimg) {
				String status = Environment.getExternalStorageState();
				if (!status.equals(Environment.MEDIA_MOUNTED)) {// 判断是否有SD卡
					DialogAlertUtil.showToast(WidgetSettingUser.this,
							"没有找到SD卡或者正在使用请关闭usb连接模式");
					return;
				}
				ImageName = "/" + getStringToday() + ".jpg";
				// 调用系统的拍照功能
				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(
						Environment.getExternalStorageDirectory(), ImageName)));
				startActivityForResult(intent, PHOTOHRAPH);
			} else if (v.getId() == R.id.setting_btn_local_img) {
				// 调用系统的相册
				Intent intent = new Intent(Intent.ACTION_PICK, null);
				intent.setDataAndType(
						MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
				// 调用剪切功能
				startActivityForResult(intent, PHOTOZOOM);
			}

		}

	}

	public void startPhotoZoom(Uri uri) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		intent.putExtra("crop", "true");
		intent.putExtra("return-data", true);
		startActivityForResult(intent, PHOTORESOULT);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		return super.onCreateOptionsMenu(menu);
	}

}
