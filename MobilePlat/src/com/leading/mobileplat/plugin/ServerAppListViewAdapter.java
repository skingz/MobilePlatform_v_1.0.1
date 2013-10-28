package com.leading.mobileplat.plugin;

import java.util.List;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.leading.baselibrary.database.bean.CacheServerAppBean;
import com.leading.baselibrary.util.StringUtils;
import com.leading.mobileplat.R;
import com.leading.mobileplat.mutual.DownLoadUtil;

public class ServerAppListViewAdapter extends BaseAdapter {
	private LayoutInflater mInflater=null;
	private List<CacheServerAppBean> mlistAppInfo = null;
	private Context context;
	public ServerAppListViewAdapter(Context context, List<CacheServerAppBean> apps){
		this.context=context;
		this.mInflater=LayoutInflater.from(context);
		this.mlistAppInfo=apps;
	}
	@Override
	public int getCount() {
		return mlistAppInfo.size();
	}

	@Override
	public Object getItem(int position) {
		return mlistAppInfo.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder=null;
		View view = null;
		if(convertView==null||convertView.getTag()==null){
			view = mInflater.inflate(R.layout.widget_appmger_server_list_item, null);
			holder = new ViewHolder(view);
			view.setTag(holder);
		}else {
			view = convertView;
			holder = (ViewHolder) convertView.getTag();
		}
		final CacheServerAppBean appInfo = (CacheServerAppBean) getItem(position);
		if(StringUtils.isNotNull(appInfo.getIconUrl())){
			/*Bitmap bt=ImgFactory.decodeBitmapFromFile(appInfo.getIconUrl(),50,50);
			BitmapDrawable  drawable =new BitmapDrawable(bt);
			holder.appIcon.setImageDrawable(drawable);*/
		}
		holder.tvAppName.setText(appInfo.getAppName_CN());
		holder.tvAppInfo.setText(appInfo.getDescrible());
		holder.tvAppVersion.setText("版本:"+appInfo.getEditionNo());
		holder.tvAppSize.setText(appInfo.getAppSize()+"k");
		holder.tvUpdateTime.setText(appInfo.getUpdateTime());
		holder.btnDowload.setTag(appInfo.getStrUrl());
		holder.btnPause.setTag(appInfo.getStrUrl());
		
		if(appInfo.getHasInstalled()){
			holder.tv_Remark.setText("已安装");
			holder.btnDowload.setVisibility(View.GONE);
		}else {
			holder.tv_Remark.setText("可下载");
			holder.btnDowload.setVisibility(View.VISIBLE);
			}
		if(appInfo.getNeedUpdate()){
			holder.tv_Remark.setText("可更新");
			holder.btnDowload.setVisibility(View.VISIBLE);
			}
		//holder.delIgmBtn.setImageResource(android.R.drawable.ic_menu_delete);
		holder.btnDowload.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View view) {
				Log.e("downLoad_Beging",Environment.getExternalStorageState()+ "------");
				View v=(View)view.getParent();
				ProgressBar progressBar=(ProgressBar)v.findViewById(R.id.progressBar1);
				TextView tv_downValue=(TextView)v.findViewById(R.id.tv_downValue);
				TextView tv_remark=(TextView)v.findViewById(R.id.txtViewRemark_server);
				TextView btnUrl=(TextView)v.findViewById(R.id.txtBtnInstall_server);
				TextView btnPause=(TextView)v.findViewById(R.id.txtBtnPause_server);
				String strUrl=(String)btnUrl.getTag();
				if(strUrl.equals("")){
					Toast.makeText(context, "下载地址解析错误!", Toast.LENGTH_SHORT).show();
					return;
				}
				if(progressBar!=null&&tv_downValue!=null){
					if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
						DownLoadUtil.getInstance(context).startDownload(strUrl, progressBar,tv_downValue,tv_remark,btnUrl,btnPause);
					} else {
						Toast.makeText(context, "sdcard不可用", Toast.LENGTH_SHORT).show();
					}
				}else Log.e("downLoad","error................................");
				
			}});
		holder.btnPause.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View view) {
				View v=(View)view.getParent();
				TextView btnUrl=(TextView)v.findViewById(R.id.txtBtnInstall_server);
				TextView btnPause=(TextView)v.findViewById(R.id.txtBtnPause_server);
				String strUrl=(String)btnUrl.getTag();
				btnUrl.setVisibility(View.VISIBLE);
				btnPause.setVisibility(View.GONE);
				DownLoadUtil dlh=DownLoadUtil.getInstance(context);
				dlh.puseDownload(strUrl);
			}});
		return view;
	}
	
	
	class ViewHolder {
		ImageView appIcon;
		TextView tvAppName;
		TextView tvAppVersion;
		TextView tvAppSize;
		TextView tvUpdateTime;
		TextView tvAppInfo;
		TextView btnDowload;
		TextView btnPause;
		TextView tv_downValue;
		TextView tv_Remark;
		ProgressBar progressBar;

		public ViewHolder(View view) {
			this.appIcon = (ImageView) view.findViewById(R.id.imgViewPluginIcon_server);
			this.tvAppName = (TextView) view.findViewById(R.id.txtViewPluginLable_server);
			this.tvAppVersion=(TextView) view.findViewById(R.id.txtViewPluginVersion_server);
			this.tvAppSize=(TextView)view.findViewById(R.id.txtViewPluginSize_server);
			this.tvUpdateTime=(TextView)view.findViewById(R.id.txtViewPluginUpdateTime_server);
			this.tvAppInfo=(TextView)view.findViewById(R.id.txtViewPluginInfo_server);
			this.btnDowload=(TextView)view.findViewById(R.id.txtBtnInstall_server);
			this.btnPause=(TextView)view.findViewById(R.id.txtBtnPause_server);
			this.tv_downValue=(TextView)view.findViewById(R.id.tv_downValue);
			this.progressBar=(ProgressBar)view.findViewById(R.id.progressBar1);
			this.tv_Remark=(TextView)view.findViewById(R.id.txtViewRemark_server);
		}
	}
	
}
