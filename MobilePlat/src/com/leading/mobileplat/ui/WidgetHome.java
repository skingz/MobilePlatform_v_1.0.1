package com.leading.mobileplat.ui;

import java.util.List;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.RelativeLayout;

import com.leading.baselibrary.ui.DialogAlertUtil;
import com.leading.mobileplat.R;
import com.leading.mobileplat.plugin.PluginBean;
import com.leading.mobileplat.plugin.PluginGridViewAdapter;
import com.leading.mobileplat.plugin.PluginManager;

public class WidgetHome  extends RelativeLayout{
	private GridView gridPlugins;
	private Context thisContext;
	private List<PluginBean> plugins;
	public WidgetHome(Context context, AttributeSet attrs) {
		super(context, attrs);
		thisContext=context;
		LayoutInflater.from(context).inflate(R.layout.widget_home, this,true);
		 gridPlugins = (GridView) this.findViewById(R.id.gridPlugins);
		 showPlugins();
	}
	public void showPlugins(){

		PluginManager pm = new PluginManager(thisContext);
		pm.findPlugins();
		plugins = pm.pluginList;
		PluginGridViewAdapter bpadapter=new PluginGridViewAdapter(thisContext,plugins);
		gridPlugins.setAdapter(bpadapter);
		gridPlugins.setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
			try{
				 Intent itt=new Intent();
				 itt.setAction(plugins.get(position).getApkName());
				 thisContext.startActivity(itt);
			} catch(ActivityNotFoundException ex){
				ex.printStackTrace();
				DialogAlertUtil.showToast(thisContext, "无法跳转到指定程序！请确认该程序是否正确安装!");
			}
			}});
	}
}
