package com.leading.baselibrary.ui;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.PopupWindow;

import com.leading.baselibrary.R;

/**
 * 加载等待动画.
 * @author Jiantao.tu
 *
 */
public class Loading{
	/**
	 * 弹出窗体对象.
	 */
	private PopupWindow popupWindow;
	
	/**
	 * 加载图片空间.
	 */
	private ImageView imgView;
	
	/**
	 * 动画放映对象.
	 */
	private AnimationDrawable ad;
	
	private static Context context;
	
	private static int id;
	
	private Loading(){}
	
	private static Loading loading;
	
	public static Loading getLoading(Context context,int id){
		Loading.context=context;
		Loading.id=id;
		if(loading==null)return new Loading();
		else return null;
	}
	
	public void openLoading(){
		initPopuptWindow(context,id);
	}
	
	public void closeLoading(){
		close();
	}
	
	/**
	 * 创建PopupWindow
	 */
	private void initPopuptWindow(Context context,int id) {
//		View popupWindow_view = a.getLayoutInflater().inflate( // 获取自定义布局文件dialog.xml的视图
//				R.layout.loading, null, false);
		View popupWindow_view=LayoutInflater.from(context).inflate(R.layout.loading, null, false);  
		// 使用下面的构造方法
		popupWindow = new PopupWindow(popupWindow_view,
				LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT, true);
		// 这里注意 必须要有一个背景 ，有了背景后
		// 当你点击对话框外部的时候或者按了返回键的时候对话框就会消失，当然前提是使用的构造函数中Focusable为true
		//popupWindow.setBackgroundDrawable(new BitmapDrawable());
		popupWindow.showAtLocation(((Activity)context).findViewById(id),Gravity.CENTER, 0, 0);
		imgView=(ImageView)popupWindow_view.findViewById(R.id.img_loading);
		imgView.setBackgroundResource(R.anim.spinner_small);
        ad=(AnimationDrawable) imgView.getBackground();
        /**
         * AnimationDrawable要实现动画的自启动直接写在onCreate，onStart，
         * onResume里面，单纯的 .start()一句启动是无效的，必须有事件启动，写在比如事件监听当中 。
         */
        imgView.post(new Runnable() {
			@Override
			public void run() {
	                ad.start(); 
			}
		});
		popupWindow_view.setFocusableInTouchMode(true); //设置view能够接听事件
		popupWindow_view.setOnKeyListener(new OnKeyListener(){
			/**
			 * 如按返回键则关闭加载窗口.
			 */
			public boolean onKey(View arg0, int arg1, KeyEvent arg2) {
				if (arg1 == KeyEvent.KEYCODE_BACK){
	     			if(popupWindow != null) {
	     				close();
	     			} 
	     		}
	     		return false; 
			}
		 });
	}
	
	
	/**
	 * 关闭窗口.
	 * @author JianTao.tu
	 */
	private void close(){
		if(popupWindow != null) {
			ad.stop();
			popupWindow.dismiss();
		} 
	}
}
