package com.leading.mobileplat.ui;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.leading.baselibrary.database.bean.CacheServerAppBean;
import com.leading.mobileplat.R;
import com.leading.mobileplat.mutual.ServiceHelper;
import com.leading.mobileplat.plugin.PluginBean;
import com.leading.mobileplat.plugin.PluginListViewAdapter;
import com.leading.mobileplat.plugin.PluginManager;
import com.leading.mobileplat.plugin.ServerAppListViewAdapter;

public class WidgetAppMger extends RelativeLayout  {

	private int offset=0; //图片偏移量
	private int currIndex=0;//当前tab页编号
	private int bmpW; //图片宽度
	private ImageView imgView;//
	private ViewPager viewPager;//叶片内容
	private List<View> listViews;
	private ProgressBar pBar;
	//
	private Context context;
	private List<PluginBean> plugins;
	private List<CacheServerAppBean> serverApp;
	
	private View local_View=null;
	private View server_View=null;
	public WidgetAppMger(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater.from(context).inflate(R.layout.widget_appmger, this,true);
		this.context=context;
		initViewPager();
		initImgLineSite();
	}
	private void initViewPager(){
		TextView tv_local=(TextView)this.findViewById(R.id.tabLocal);
		TextView tv_service=(TextView)this.findViewById(R.id.tabService);
		tv_local.setOnClickListener(new TabTextViewOnClickListener(0));
		tv_service.setOnClickListener(new TabTextViewOnClickListener(1));
		//
		pBar=(ProgressBar)this.findViewById(R.id.pgBarLoding);
		viewPager=(ViewPager)this.findViewById(R.id.vPager);
		imgView=(ImageView)this.findViewById(R.id.imageLine);
		local_View = LayoutInflater.from(context).inflate(R.layout.widget_appmger_local, null);
		server_View = LayoutInflater.from(context).inflate(R.layout.widget_appmger_server, null);
		
		
		////
		listViews=new ArrayList<View>();
		listViews.add(local_View);
		listViews.add(server_View);
		viewPager.setAdapter(new TabViewPagerAdapter(listViews));
		viewPager.setCurrentItem(0);
		viewPager.setOnPageChangeListener(new TabPageChangeListener());
		
		getAppListThread.start();
	}
	private void initImgLineSite(){
		bmpW=BitmapFactory.decodeResource(getResources(), R.drawable.green_line).getWidth();
		DisplayMetrics dm=new DisplayMetrics();
		WindowManager wm=(WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
		wm.getDefaultDisplay().getMetrics(dm);
		int screeW=dm.widthPixels;
		offset=(screeW/2-bmpW)/2;
		Matrix matrix=new Matrix();
		matrix.postTranslate(offset, 0);
		imgView.setImageMatrix(matrix);
	}
	
	private void initLocalAppListData(){
		PluginManager pm = new PluginManager(context);
		pm.findPlugins();
		plugins = pm.pluginList;
	}
	private void initLocalAppListUI(){
		if(plugins!=null&&plugins.size()>0){
			ListView lvPlginList=(ListView)local_View.findViewById(R.id.listAppManager_Local);
			PluginListViewAdapter plv=new PluginListViewAdapter(context,plugins);
			lvPlginList.setAdapter(plv);
		}
	}
	private void initServerAppListData(List<PluginBean> localList){
		ServiceHelper sh=new ServiceHelper(context);
		serverApp=sh.getAppList(localList);
	}
	private void initServerAppListUI(){
		if(serverApp!=null&&serverApp.size()>0){
			ListView lvAppList=(ListView)server_View.findViewById(R.id.listAppManager_server);
			ServerAppListViewAdapter plv=new ServerAppListViewAdapter(context,serverApp);
			lvAppList.setAdapter(plv);
		}
	}
	private class TabTextViewOnClickListener implements OnClickListener{
		private int index=0;
		public TabTextViewOnClickListener(int index){this.index=index;}
		@Override
		public void onClick(View arg0) {
			viewPager.setCurrentItem(index);
		}
	}
	private class TabPageChangeListener implements OnPageChangeListener{
		
		@Override
		public void onPageScrollStateChanged(int arg0) {}
		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {		}
		@Override
		public void onPageSelected(int armIndex) {
			int one=offset*2+bmpW;
			//int two=one*2;
			Animation animation=new TranslateAnimation(one*currIndex,one*armIndex,0,0);
			currIndex=armIndex;
			animation.setFillAfter(true);
			animation.setDuration(300);
			imgView.startAnimation(animation);
		}
		
	}
	private class TabViewPagerAdapter extends PagerAdapter{
		private List<View> mListView;
		public TabViewPagerAdapter(List<View> mListView){this.mListView=mListView;}
		@Override
		public int getCount() {
			return mListView.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0==arg1;
		}
		@Override
		public void destroyItem(ViewGroup container,int position,Object object){
			container.removeView(this.mListView.get(position));
		}
		@Override
		public Object instantiateItem(ViewGroup container,int position){
			container.addView(mListView.get(position),0);
			return mListView.get(position);
		}
	}
	
	Handler pBarHandler = new Handler(){   
		@Override  
		public void handleMessage(Message msg) {
			if(msg.arg1==1){
				initLocalAppListUI();
				initServerAppListUI();
				pBar.setVisibility(View.GONE);
				viewPager.setVisibility(View.VISIBLE);
			}
			super.handleMessage(msg);
		}   
	    };   
	private Thread getAppListThread=new Thread(){
		public void run(){
			initLocalAppListData();
			initServerAppListData(plugins);
			Message msg = pBarHandler.obtainMessage();
			msg.arg1=1;
			pBarHandler.sendMessage(msg);
		}
	};
}
