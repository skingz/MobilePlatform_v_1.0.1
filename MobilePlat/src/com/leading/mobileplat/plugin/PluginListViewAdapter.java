package com.leading.mobileplat.plugin;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.leading.mobileplat.R;

public class PluginListViewAdapter extends BaseAdapter {
	private LayoutInflater mInflater=null;
	private List<PluginBean> mlistAppInfo = null;
	private Context context;
	public PluginListViewAdapter(Context context, List<PluginBean> apps){
		this.context=context;
		this.mInflater=LayoutInflater.from(context);
		this.mlistAppInfo=apps;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mlistAppInfo.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mlistAppInfo.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder=null;
		View view = null;
		if(convertView==null||convertView.getTag()==null){
			view = mInflater.inflate(R.layout.widget_appmger_local_list_item, null);
			holder = new ViewHolder(view);
			view.setTag(holder);
		}else {
			view = convertView;
			holder = (ViewHolder) convertView.getTag();
		}
		final PluginBean appInfo = (PluginBean) getItem(position);
		holder.appIcon.setImageDrawable(appInfo.getIcon());
		holder.tvAppLabel.setText(appInfo.getApkLable());
		holder.tvAppVersion.setText("版本:"+appInfo.getVersion());
		//holder.delIgmBtn.setImageResource(android.R.drawable.ic_menu_delete);
		holder.delIgmBtn.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				appInfo.uninstall(context);
			}});
		holder.updateBtn.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				
			}});
		return view;
	}
	
	class ViewHolder {
		ImageView appIcon;
		TextView tvAppLabel;
		TextView tvAppVersion;
		TextView tvNewVersion;
		TextView delIgmBtn;
		TextView updateBtn;

		public ViewHolder(View view) {
			this.appIcon = (ImageView) view.findViewById(R.id.imgViewPluginIcon);
			this.tvAppLabel = (TextView) view.findViewById(R.id.txtViewPluginLable);
			this.tvAppVersion=(TextView) view.findViewById(R.id.txtViewPluginVersion);
			this.delIgmBtn=(TextView)view.findViewById(R.id.txtBtnUnInstall);
			this.updateBtn=(TextView)view.findViewById(R.id.txtBtnUpdate);
		}
	}
}
