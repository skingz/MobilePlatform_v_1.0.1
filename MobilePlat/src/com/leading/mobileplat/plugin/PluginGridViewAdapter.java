package com.leading.mobileplat.plugin;

import java.util.List;

import com.leading.mobileplat.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class PluginGridViewAdapter extends BaseAdapter {

	private List<PluginBean> mlistAppInfo = null;

	LayoutInflater infater = null;

	public PluginGridViewAdapter(Context context, List<PluginBean> apps) {
		infater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mlistAppInfo = apps;
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
	public View getView(int position, View convertview, ViewGroup arg2) {
		View view = null;
		ViewHolder holder = null;
		if (convertview == null || convertview.getTag() == null) {
			view = infater.inflate(R.layout.grid_item, null);
			holder = new ViewHolder(view);
			view.setTag(holder);
		} else {
			view = convertview;
			holder = (ViewHolder) convertview.getTag();
		}
		PluginBean appInfo = (PluginBean) getItem(position);
		holder.appIcon.setImageDrawable(appInfo.getIcon());
		holder.tvAppLabel.setText(appInfo.getApkLable());
		return view;
	}

	class ViewHolder {
		ImageView appIcon;
		TextView tvAppLabel;

		public ViewHolder(View view) {
			this.appIcon = (ImageView) view.findViewById(R.id.ItemImage);
			this.tvAppLabel = (TextView) view.findViewById(R.id.ItemText);
		}
	}

}