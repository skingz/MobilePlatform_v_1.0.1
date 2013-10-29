package com.leading.mobileplat.ui;

import java.util.List;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.LinearLayout;

import com.leading.baselibrary.database.DataBaseManager;
import com.leading.baselibrary.database.bean.MsgBean;
import com.leading.baselibrary.database.bean.MsgGroup;
import com.leading.baselibrary.global.MainApplication;
import com.leading.baselibrary.ui.DialogAlertUtil;
import com.leading.mobileplat.ActivityMessageDetail;
import com.leading.mobileplat.R;
import com.leading.xmpp_client.tools.Constants;

public class WidgetMsg extends LinearLayout  {

	Context context;
	LinearLayout listArea;
	private ExpandableListView msgExpandableList=null;
	private WidgetMsgExpAdapter exListAdapter=null;
	private String userId;
	public WidgetMsg(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater.from(context).inflate(R.layout.widget_msg, this,true);
		this.context=context;
		msgExpandableList=(ExpandableListView)this.findViewById(R.id.msgExpandableList);
		userId=MainApplication.getConfig().getUserId();
		DataBaseManager mgM=new DataBaseManager(this.context);
		 List<MsgGroup> msgObjList=mgM.getUnReadMsgGroup(userId);
		 exListAdapter=new WidgetMsgExpAdapter(context,msgObjList);
		 msgExpandableList.setAdapter(exListAdapter);
		 if(msgExpandableList.getCount()>0)msgExpandableList.expandGroup(0);
		 
		msgExpandableList.setOnChildClickListener(childClickListener);
	}
	/**
	 * 点击事件触发 弹出子系统的详细信息页面，并删除主程序中本条消息
	 */
	private OnChildClickListener childClickListener=new OnChildClickListener(){
		@Override
		public boolean onChildClick(ExpandableListView exListView, View view,
				int groupPosition, int childPosition, long id) {
			MsgBean mb=(MsgBean)exListAdapter.getChild(groupPosition, childPosition);
			MsgGroup mg=(MsgGroup)exListAdapter.getGroup(groupPosition);
			boolean IfStartActivity=true;
			
			if(mg.getSourceApkName()==null||mb.getRedrectUri()==null){
				Intent intent=new Intent(context,ActivityMessageDetail.class);
		            intent.putExtra(Constants.NOTIFICATION_TITLE, mb.getMsgTitle());
		            intent.putExtra(Constants.NOTIFICATION_MESSAGE, mb.getMsgDetail());
		            intent.putExtra(Constants.NOTIFICATION_URI, mb.getRedrectUri());
		            intent.putExtra(Constants.NOTIFICATION_VIEWAPP, mg.getSourceApkName());
		            intent.putExtra(Constants.NOTIFICATION_FSIID, mb.getBusinessInstanceId());
		            
		            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		            intent.setFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
		            intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
		            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		            context.startActivity(intent);
			}else{
				try{
					ComponentName toActivity = new ComponentName(mg.getSourceApkName(),mb.getRedrectUri());
					Intent intent=new Intent();
					intent.setComponent(toActivity);
					intent.putExtra("BusinessInstanceId", mb.getBusinessInstanceId());
					intent.putExtra("fsiid", mb.getBusinessInstanceId());
					intent.setAction(mb.getRedrectUri());
					context.startActivity(intent);
				}catch(Exception ex){
					ex.printStackTrace();
					IfStartActivity=false;
					DialogAlertUtil.showToast(context,"请确认该子应用已正确安装！");
				}
			}
			if(IfStartActivity){
				DataBaseManager mgM=new DataBaseManager(context);
				mgM.deleteMessage(mb.getMsgId());
				mgM.deleteAllReadMsgBean();
			}
			
			return false;
		}
	}; 
	 
}
