package com.leading.baselibrary.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.leading.baselibrary.global.MainApplication;

public class ActivityCommon extends Activity {
	public Long activityId = System.currentTimeMillis();
	protected OnScreenHint mOnScreenHint;
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		MainApplication.addActivity(activityId, this);
		mOnScreenHint = OnScreenHint.makeText(this, "tostTest",3000L);
	}
	
	 @Override
		protected void onPause() {
			super.onPause();
			mOnScreenHint.cancel();
		}
	@Override
	public void onDestroy() {
		MainApplication.removeActivity(activityId);
		super.onDestroy();
	}
	@Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        initPage();
    }
	/**
	 * 页面新实例或 重新启动堆栈中的实例时，页面需要初始化的信息
	 */
	protected void initPage(){
		
	}
	 /**
     * 显示S
     * @param s
     */
    public void showToast(CharSequence s){
    	mOnScreenHint.setText(s);
    	mOnScreenHint.show();
    }

}
