package com.leading.baselibrary.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.leading.baselibrary.R;
import com.leading.baselibrary.global.MainApplication;
import com.leading.baselibrary.netutil.NetWorkCheck;

public class ActivityTemplat extends Activity {

	protected Button btnTitleLeft;
	protected Button btnTitleRight;
	protected TextView titleTextView;
	protected NetWorkCheck netWorkCheck;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_templat);
        TitleBarLayout tbl=(TitleBarLayout)this.findViewById(R.id.titlebar);
        btnTitleLeft=tbl.getLeftBtn();
        btnTitleRight=tbl.getRightBtn();
        titleTextView=tbl.getTextView();
        //默认添加返回
        btnTitleLeft.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				ActivityTemplat.this.finish();
			}});
        
        netWorkCheck=NetWorkCheck.getInstance(this);
    }
    public void addSelfContentView(int layoutResId){
    	RelativeLayout selfContent = (RelativeLayout) findViewById(R.id.selfContent);
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE); 
		View v = inflater.inflate(layoutResId, null);  
		selfContent.addView(v); 
	}
    public Button getTitleLeftBtn(){return btnTitleLeft;}
    public Button getTitleRightBtn(){return btnTitleRight;}
    public TextView getTitleTextView(){return titleTextView;}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
    	menu.add(Menu.NONE, Menu.FIRST + 1, 5, R.string.action_exit).setIcon(android.R.drawable.ic_menu_close_clear_cancel);
        return true;
    }
    @Override
    public void onDestroy(){
    	super.onDestroy();
    	//netWorkCheck.onDestroy();
    }
    @Override
	public boolean onOptionsItemSelected(MenuItem item) {
    	switch (item.getItemId()) {
    	case Menu.FIRST + 1:
//    		ActivityManager am=(ActivityManager)this.getSystemService(Context.ACTIVITY_SERVICE);
//    		am.restartPackage(ActivityTemplat.this.getApplicationInfo().packageName);
    		exitApp();
    		break;
    	}
		return false;
    }
    //退出应用
    public void exitApp(){
		if(MainApplication.getActivitys().size()>0){
			for(Activity activity : MainApplication.getActivitys().values()){
				if(activity!=null && !activity.isFinishing())
					activity.finish();
			}
			//android.os.Process.killProcess(android.os.Process.myPid());
		}else System.exit(0);
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
    
}
