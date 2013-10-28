package com.leading.mobileplat.ui;

import java.lang.ref.WeakReference;

import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.leading.baselibrary.ui.ActivityTemplat;
import com.leading.baselibrary.ui.DialogAlertUtil;
import com.leading.baselibrary.ui.Loading;
import com.leading.mobileplat.R;
import com.leading.mobileplat.mutual.ServiceHelper;

public class WidgetSettingFeedback extends ActivityTemplat{

	private static Handler mainHanler;
	
	private EditText edtFeedbackMassage;
	
	private EditText edtFeedbackContact;
	
	/**
	 * 是否登录成功标识.
	 */
	private final static String isLoginOk = "1";
	
	
	private static WeakReference<WidgetSettingFeedback> mainActivity;
	
	private Loading loading=null;
	@Override
	protected  void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		addSelfContentView(R.layout.widget_setting_feedback);
		Button button=(Button)findViewById(R.id.btn_feedback);
		edtFeedbackMassage=(EditText)findViewById(R.id.edt_feedback_massage);
		edtFeedbackContact=(EditText)findViewById(R.id.edt_feedback_contact);
		loading=Loading.getLoading(WidgetSettingFeedback.this, R.id.activity_feedback);
		mainActivity=new WeakReference<WidgetSettingFeedback>(this);
		super.btnTitleLeft.setVisibility(View.VISIBLE);
		super.titleTextView.setText("意见反馈");
		button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				loading.openLoading();
				HandlerThread handlerThread=new HandlerThread("child_Thread");
				handlerThread.start();
				ChildHandler childHandler=new 
						ChildHandler(handlerThread.getLooper());
				Message message=childHandler.obtainMessage();
				message.sendToTarget();
			}
		});
	}
	
	public static class MainRunnable implements Runnable{
		String result;
		public MainRunnable(String result){
			this.result=result;
		}
		@Override
		public void run() {
			WidgetSettingFeedback wsFeedback=mainActivity.get();
			wsFeedback.loading.closeLoading();
			if("error".equals(result))
				DialogAlertUtil.showToast(wsFeedback, "服务器地址解析错误！");
			else if (!isLoginOk.equals(result))
				DialogAlertUtil.showToast(wsFeedback, "提交失败,请稍后再试！");
			else
				DialogAlertUtil.showToast(wsFeedback, "谢谢您的反馈,如有必要我们可能会联系您！");
		
		}
	}
	public static class ChildHandler extends Handler{
		public ChildHandler(Looper looper){
			super(looper);
		}
		@Override
		public void handleMessage(Message msg) {
			WidgetSettingFeedback wsFeedback=mainActivity.get();
			ServiceHelper serviceHelper=new ServiceHelper(wsFeedback);
			String result=serviceHelper.feedBack(wsFeedback.edtFeedbackMassage.getText().toString(), 
					wsFeedback.edtFeedbackContact.getText().toString());
			mainHanler=new Handler();
			mainHanler.post(new WidgetSettingFeedback.MainRunnable(result));
		}
		
	}

}
