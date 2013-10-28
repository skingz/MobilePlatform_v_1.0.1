package com.leading.localequestion;

import java.sql.SQLException;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.leading.baselibrary.database.DataBaseManager;
import com.leading.baselibrary.ui.ActivityCommon;
import com.leading.baselibrary.ui.TitleBarLayout;
import com.leading.localequestion.dao.LocaleQuestionDao;
import com.leading.localequestion.data.ConstantStore;
import com.leading.localequestion.global.LQApplication;

public class MainActivity extends ActivityCommon implements OnClickListener{

	private TitleBarLayout tbl;
	private RelativeLayout rlWdxx;//未读信息
	private RelativeLayout rlCgx;//草稿箱
	private RelativeLayout rlDjj;//待解决
	private RelativeLayout rlYjj;//已解决
	private RelativeLayout rlZbjj;//暂不解决
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.i("main测试我的ID", super.activityId.toString());
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		tbl=(TitleBarLayout)findViewById(R.id.main_tbl);
/*		Bitmap bitmap=getResIcon(this.getResources(), R.drawable.icon_qs_unread);
		ImageView imgView=(ImageView)findViewById(R.id.home_img_looks);
		bitmap=generatorContactCountIcon(bitmap,12);
		Drawable drawable = new BitmapDrawable(bitmap); 
		imgView.setBackgroundDrawable(drawable);*/
		rlWdxx=(RelativeLayout)findViewById(R.id.main_rl_wdxx);
		rlCgx=(RelativeLayout)findViewById(R.id.main_rl_cgx);
		rlDjj=(RelativeLayout)findViewById(R.id.main_rl_djj);
		rlYjj=(RelativeLayout)findViewById(R.id.main_rl_yjj);
		rlZbjj=(RelativeLayout)findViewById(R.id.main_rl_zbjj);
		rlWdxx.setOnClickListener(this);
		rlCgx.setOnClickListener(this);
		rlDjj.setOnClickListener(this);
		rlYjj.setOnClickListener(this);
		rlZbjj.setOnClickListener(this);
		tblLoad();
		initPage();
	}
	
	@Override
	public void onClick(View v) {
		Intent intent=new Intent(this, ActivityFeedbackList.class);
		switch (v.getId()) {
			case R.id.main_rl_wdxx:
				intent.putExtra("type", ConstantStore.LQ_TYPE_UNREAD);
				intent.putExtra("typeName", "未读信息");
				break;
			case R.id.main_rl_cgx:
				intent.putExtra("type", ConstantStore.LQ_TYPE_DRAFT);
				intent.putExtra("typeName", "草稿箱");
				break;
			case R.id.main_rl_djj:
				intent.putExtra("type", ConstantStore.LQ_TYPE_BESOLVED);
				intent.putExtra("typeName", "待解决");
				break;
			case R.id.main_rl_yjj:
				intent.putExtra("type", ConstantStore.LQ_TYPE_HASBEENRESOLVED);
				intent.putExtra("typeName", "已解决");
				break;
			case R.id.main_rl_zbjj:
				intent.putExtra("type", ConstantStore.LQ_TYPE_NOTTOSOLVE);
				intent.putExtra("typeName", "暂不解决");
				break;
		}
		this.startActivity(intent);
		
	}

	/**
	 * 初始化 首页上各个状态 的数值
	 */
	@Override
	protected void initPage(){
		TextView tv_UnRead=(TextView)this.findViewById(R.id.tv_Unread);
		TextView tv_Draft=(TextView)this.findViewById(R.id.tv_Draft);
		TextView tv_BeReslove=(TextView)this.findViewById(R.id.tv_BeReslove);
		TextView tv_Resloved=(TextView)this.findViewById(R.id.tv_Resloved);
		TextView tv_NotToReslove=(TextView)this.findViewById(R.id.tv_NotToReslove);
		LocaleQuestionDao ld=new LocaleQuestionDao(this);
		try {

			int tmp=ld.getDao().queryForEq("qsState", ConstantStore.LQ_TYPE_DRAFT).size();
			if(tmp>0) tv_Draft.setText(tv_Draft.getText().toString().replaceFirst("[0-9]+",String.valueOf(tmp) ));
			tmp=ld.getDao().queryForEq("qsState", ConstantStore.LQ_TYPE_BESOLVED).size();
			if(tmp>0) tv_BeReslove.setText(tv_BeReslove.getText().toString().replaceFirst("[0-9]+",String.valueOf(tmp) ));
			tmp=ld.getDao().queryForEq("qsState", ConstantStore.LQ_TYPE_HASBEENRESOLVED).size();
			if(tmp>0) tv_Resloved.setText(tv_Resloved.getText().toString().replaceFirst("[0-9]+",String.valueOf(tmp) ));
			tmp=ld.getDao().queryForEq("qsState", ConstantStore.LQ_TYPE_NOTTOSOLVE).size();
			if(tmp>0) tv_NotToReslove.setText(tv_NotToReslove.getText().toString().replaceFirst("[0-9]+",String.valueOf(tmp)));
			DataBaseManager mainDatabase=new DataBaseManager(this);
			tmp=mainDatabase.getUnReadCount(ConstantStore.LQ_NAME,LQApplication.getConfig().getUserId());
			if(tmp>0) tv_UnRead.setText(tv_UnRead.getText().toString().replaceFirst("[0-9]+",String.valueOf(tmp) ));
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void tblLoad(){
		Button rightButton=tbl.getRightBtn();
		Button leftButton=tbl.getLeftBtn();
		rightButton.setText("创建");
		leftButton.setVisibility(View.VISIBLE);
		rightButton.setVisibility(View.VISIBLE);
		rightButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent=new Intent(MainActivity.this, ActivityFeedbackDraftbox.class);
				MainActivity.this.startActivity(intent);
				MainActivity.this.finish();
			}
		});
		leftButton.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				BackToMainApp();
			}});
	}
	private void BackToMainApp(){
		/*String apkName=MainActivity.this.getString(R.string.MainPlatAPK);
		String pageUri=MainActivity.this.getString(R.string.MainPlatMainActivty);
		ComponentName toActivity = new ComponentName(apkName,pageUri);
		Intent intent=new Intent();
		intent.setComponent(toActivity);
		intent.setAction(pageUri);
		startActivity(intent);*/
		
		if(LQApplication.getActivitys().size()>0){
			for(Activity activity : LQApplication.getActivitys().values()){
				if(activity!=null && !activity.isFinishing())
					activity.finish();
			}
		}
	}
	/**
     * 根据id获取一个图片
     * @param res
     * @param resId
     * @return
     */
    private Bitmap getResIcon(Resources res,int resId){
    	Drawable icon=res.getDrawable(resId);
    	if(icon instanceof BitmapDrawable){
    		BitmapDrawable bd=(BitmapDrawable)icon;
    		return bd.getBitmap();
    	}else{
    		return null;
    	}
    }
	/**
     * 在给定的图片的右上角加上联系人数量。数量用红色表示
     * @param icon 给定的图片
     * @return 带联系人数量的图片
     */
    private Bitmap generatorContactCountIcon(Bitmap icon,int num){
    	//初始化画布
    	int iconSize=(int)getResources().getDimension(android.R.dimen.app_icon_size);
    	Log.d("e", "the icon size is "+iconSize);
    	Bitmap contactIcon=Bitmap.createBitmap(iconSize, iconSize, Config.ARGB_8888);
    	Canvas canvas=new Canvas(contactIcon);
    	
    	//拷贝图片
    	Paint iconPaint=new Paint();
    	iconPaint.setDither(true);//防抖动
    	iconPaint.setFilterBitmap(true);//用来对Bitmap进行滤波处理，这样，当你选择Drawable时，会有抗锯齿的效果
    	Rect src=new Rect(0, 0, icon.getWidth(), icon.getHeight());
    	Rect dst=new Rect(0, 0, iconSize, iconSize);
    	canvas.drawBitmap(icon, src, dst, iconPaint);
    	
    	//启用抗锯齿和使用设备的文本字距
    	Paint countPaint=new Paint(Paint.ANTI_ALIAS_FLAG|Paint.DEV_KERN_TEXT_FLAG);
    	countPaint.setColor(Color.RED);
    	countPaint.setTextSize(20f);
    	countPaint.setTypeface(Typeface.DEFAULT_BOLD);
    	canvas.drawText(String.valueOf(num), iconSize-23, 20, countPaint);
    	return contactIcon;
    }
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			BackToMainApp();
		}
		return false;
	}



	@Override
	public void onDestroy() {
//		setContentView(R.layout.activity_null);
		super.onDestroy();
	}
	
	

}
