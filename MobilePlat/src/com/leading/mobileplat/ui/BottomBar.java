package com.leading.mobileplat.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.leading.baselibrary.ui.BadgeView;
import com.leading.mobileplat.R;

public class BottomBar extends RelativeLayout {
	public TextView tv_Msg;
	public TextView tv_Home;
	public TextView tv_AppMger;
	public TextView tv_Setting;
	private BadgeView badgeView;
	private TextView tv_SelectView;

	public TextView tv_Current=null;
	public Context bContext;
	
	public BottomBar(Context context) {
		super(context);
	}
	public BottomBar(Context context, AttributeSet attrs) {
		super(context, attrs);bContext=context;
		LayoutInflater.from(context).inflate(R.layout.bottombar, this,true);
		tv_Msg=(TextView)this.findViewById(R.id.imgBtnMsg);
		tv_Home=(TextView)this.findViewById(R.id.imgBtnHome);
		tv_AppMger=(TextView)this.findViewById(R.id.imgBtnAppManger);
		tv_Setting=(TextView)this.findViewById(R.id.imgBtnSetting);
		badgeView=new BadgeView(context,tv_Msg);badgeView.setText("New");
		//badgeView.show();
		tv_SelectView=(TextView)this.findViewById(R.id.imgSelectedbg);//hideNewMsgSite();
	}
	public void setCurrentIcon(TextView tv){
		//添加滑动效果
		if(tv_Current==null){
			tv_Current=tv_Msg;
		}
		if(tv_Current!=null&&tv!=tv_Current){
			Animation animation=new TranslateAnimation(tv_Current.getLeft(),tv.getLeft(),0,0);
			animation.setFillAfter(true);
			animation.setDuration(100);
			tv_SelectView.startAnimation(animation);
		}
		tv_Current=tv;
	}
	public void showNewMsgSite(){
		badgeView.show();
	}
	public void hideNewMsgSite(){
		badgeView.hide();
	}
}
