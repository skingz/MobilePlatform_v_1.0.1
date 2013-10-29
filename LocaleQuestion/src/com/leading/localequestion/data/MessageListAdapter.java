package com.leading.localequestion.data;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.leading.baselibrary.ui.DialogAlertUtil;
import com.leading.baselibrary.util.StringUtils;
import com.leading.localequestion.ActivityFeedbackDraftbox;
import com.leading.localequestion.ActivityFeedbackUnresolved;
import com.leading.localequestion.R;
import com.leading.localequestion.dao.LocaleQuestionDao;
import com.leading.localequestion.entity.LocaleQuestion;

public class MessageListAdapter extends BaseAdapter{
	private LayoutInflater mInflater;
	private List<Map<String, Object>> mData;// 存储的EditText值
	private Context context;
	private ViewHolder holder;
	private LocaleQuestionDao lqDao;
	private Map<Integer,Button> delete;
	public MessageListAdapter(Context context, List<Map<String, Object>> data) {
		mData = data;
		this.context=context;
		mInflater = LayoutInflater.from(context);
		lqDao=new LocaleQuestionDao(context);
		delete=new HashMap<Integer,Button>();
	}

	@Override
	public int getCount() {
		return mData.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		
		 	holder = new ViewHolder();
		    convertView = mInflater.inflate(
		       R.layout.message_item,null,true);
		    holder.txtHeading = (TextView) convertView.findViewById(R.id.msg_txt_heading);
		    holder.txtSubHeading=(TextView)convertView.findViewById((R.id.msg_txt_subheading));
		    holder.txtTime=(TextView)convertView.findViewById(R.id.msg_txt_time);
		    holder.txtHeading.setText(StringUtils.getSubString(String.valueOf(mData.get(position).get("txtHeading")),30,true));
		    String tmpTime=StringUtils.getSubString(String.valueOf(mData.get(position).get("txtTime")),16,false);
		    String subTitle=StringUtils.getSubString(String.valueOf(mData.get(position).get("txtSubHeading")),30,true);
		    holder.txtTime.setText("["+tmpTime+"]");
		    holder.txtSubHeading.setText(subTitle);
		    if(getCount()>0){
		    	delete.put(position,(Button)convertView.findViewById(R.id.msg_btn_delete));	
		    	Log.i("delete:",String.valueOf(delete.size()));
			    convertView.setOnLongClickListener(new OnLongClickListener() {
					@Override
					public boolean onLongClick(View v) {
						if(delete.get(position).getVisibility()==View.VISIBLE){
							delete.get(position).setVisibility(View.GONE);
							Log.i("position:",String.valueOf(position));
							delete.get(position).setAnimation(AnimationUtils.loadAnimation(context, R.anim.push_left_in_right));
						}else{
							delete.get(position).setVisibility(View.VISIBLE);
							Log.i("position:",String.valueOf(position));
							delete.get(position).setAnimation(AnimationUtils.loadAnimation(context, R.anim.push_left_in));
						}
						return 	false;
					}
				});
			    delete.get(position).setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						delete.get(position).setAnimation(AnimationUtils.loadAnimation(context, R.anim.push_left_in_right));
						delete.get(position).setVisibility(View.GONE);
						LocaleQuestion lq=lqDao.query("fsiid", mData.get(position).get("fsiid"));
						int result=lqDao.remove(lq);
						if(result>=0){
							DialogAlertUtil.showToast(context,"删除成功");
							mData.remove(position);
							MessageListAdapter.this.notifyDataSetChanged();
						}else
							DialogAlertUtil.showToast(context,"删除失败！");
					}
				});
		    }
		    convertView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					String type=StringUtils.nullToStr(mData.get(position).get("type"));
					String fsiid=StringUtils.nullToStr(mData.get(position).get("fsiid"));
					
						if(type.equals(String.valueOf( ConstantStore.LQ_TYPE_DRAFT))){
							Intent intent=new Intent(context, ActivityFeedbackDraftbox.class);
							intent.putExtra("fsiid", fsiid);
							context.startActivity(intent);
						}else{
							Intent intent=new Intent(context, ActivityFeedbackUnresolved.class);
							intent.putExtra("fsiid", fsiid);
							if(!"".equals(type))
								intent.putExtra("type", Integer.parseInt(type));
							context.startActivity(intent);
						}					
				}
			});
		holder=null;
		return convertView;
	}

	public class ViewHolder {
		public TextView txtHeading;
		public TextView txtSubHeading;
		public TextView txtTime;
	}
}

