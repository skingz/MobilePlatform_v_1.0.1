package com.leading.mobileplat.ui;

import java.util.List;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.leading.baselibrary.database.bean.MsgBean;
import com.leading.baselibrary.database.bean.MsgGroup;
import com.leading.mobileplat.R;


public class WidgetMsgExpAdapter  implements ExpandableListAdapter{
	 private LayoutInflater mInflater=null;
	 private List<MsgGroup> msgGroupList=null;
	 private Context context;
	 public WidgetMsgExpAdapter(Context _context,List<MsgGroup> _groupMsg){
		 this.mInflater=LayoutInflater.from(_context);
		 this.msgGroupList=_groupMsg;
		 this.context=_context;
	 }
	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return msgGroupList.get(groupPosition).getMsgList().get(childPosition);
	}
	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return msgGroupList.get(groupPosition).getMsgList().get(childPosition).getMsgId();
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return msgGroupList.get(groupPosition).getMsgList().size();
	}
	@Override
	public Object getGroup(int groupPosition) {
		return msgGroupList.get(groupPosition);
	}
	@Override
	public int getGroupCount() {
		return msgGroupList.size();
	}
	@Override
	public long getGroupId(int groupPosition) {
		return msgGroupList.get(groupPosition).getGroupId();
	}
	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,View convertView, ViewGroup parent) {
		View view = null;
		ViewGroupTitle holder=null;
		if(convertView==null||convertView.getTag()==null){
			view = mInflater.inflate(R.layout.widget_msg_subtitle, null);
			holder = new ViewGroupTitle(view);
			view.setTag(holder);
		}else {
			view = convertView;
			holder = (ViewGroupTitle) convertView.getTag();
		}
		//设置图标
		ImageView iv = (ImageView) view.findViewById(R.id.expanded);
		if (isExpanded) {
			iv.setImageDrawable(context.getResources().getDrawable(
					R.drawable.icon_add));
		} else {
			iv.setImageDrawable(context.getResources().getDrawable(
					R.drawable.icon_reduce));
		}
		holder.GroupName.setText(((MsgGroup)getGroup(groupPosition)).getGroupName());
       return view;
	}
	@Override
	public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
		
		ViewChildList holder=null;
		View view = null;
		if(convertView==null||convertView.getTag()==null){
			view = mInflater.inflate(R.layout.widget_msg_sublist, null);
			holder = new ViewChildList(view);
			view.setTag(holder);
		}else {
			view = convertView;
			holder = (ViewChildList) convertView.getTag();
		}
		MsgBean msgInfo=(MsgBean)getChild(groupPosition,childPosition);
		holder.MsgTitle.setText(msgInfo.getMsgTitle());
		String msgDetail=msgInfo.getMsgDetail();
		msgDetail=com.leading.baselibrary.util.StringUtils.getSubString( msgDetail,28,true);
		holder.MsgSubTitle.setText(msgDetail);
        return view;
	}
	@Override
	public boolean hasStableIds() {
		return false;
	}
	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}
	class ViewGroupTitle{
		TextView GroupName;
		public ViewGroupTitle(View view){this.GroupName=(TextView)view.findViewById(R.id.msgGroupTitle);}
	}
	class ViewChildList{
		TextView MsgTitle;
		TextView MsgSubTitle;
		TextView MsgTime;
		ImageView MsgTurnTo;
		public ViewChildList(View view){
			MsgTitle=(TextView)view.findViewById(R.id.txtViewMsgTitle);
			MsgSubTitle=(TextView)view.findViewById(R.id.txtViewMsgSubTitle);
			MsgTime=(TextView)view.findViewById(R.id.txtViewMsgTime);
			MsgTurnTo=(ImageView)view.findViewById(R.id.imgMsgTurnTo);
		}
	}
	@Override
	public boolean areAllItemsEnabled() {
		return false;
	}
	@Override
	public long getCombinedChildId(long groupId, long childId) {
		return 0;
	}
	@Override
	public long getCombinedGroupId(long groupId) {
		return 0;
	}
	@Override
	public boolean isEmpty() {
		return false;
	}
	@Override
	public void onGroupCollapsed(int groupPosition) {
		
	}
	@Override
	public void onGroupExpanded(int groupPosition) {
		
	}
	@Override
	public void registerDataSetObserver(DataSetObserver observer) {
		
	}
	@Override
	public void unregisterDataSetObserver(DataSetObserver observer) {
		
	}
	
}
