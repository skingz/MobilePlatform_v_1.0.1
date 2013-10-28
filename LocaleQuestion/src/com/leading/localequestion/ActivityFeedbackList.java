package com.leading.localequestion;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.j256.ormlite.dao.GenericRawResults;
import com.leading.baselibrary.database.DataBaseManager;
import com.leading.baselibrary.ui.ActivityCommon;
import com.leading.localequestion.dao.LocaleQuestionDao;
import com.leading.localequestion.data.ConstantStore;
import com.leading.localequestion.data.MessageListAdapter;
import com.leading.localequestion.ui.PullDownView;
import com.leading.localequestion.ui.PullDownView.OnPullDownListener;

@SuppressLint("HandlerLeak")
public class ActivityFeedbackList extends ActivityCommon implements OnPullDownListener {

	private static final int WHAT_DID_LOAD_DATA = 0;
	private static final int WHAT_DID_REFRESH = 1;
	private static final int WHAT_DID_MORE = 2;
	private ListView mListView;
	private BaseAdapter mAdapter;
	private PullDownView mPullDownView;
	private List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
	private DataBaseManager mainDatabase;
	private LocaleQuestionDao lqDao;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_feedback_list);
		initTitle();		
		/*
		 * 1.使用PullDownView 2.设置OnPullDownListener 3.从mPullDownView里面获取ListView
		 */
		mPullDownView = (PullDownView) findViewById(R.id.pull_down_view);
		mPullDownView.setOnPullDownListener(this);
		mListView = mPullDownView.getListView();
		mAdapter = new MessageListAdapter(this, data);
		mListView.setAdapter(mAdapter);
		mPullDownView.enableAutoFetchMore(true, 1);
		initPage();
	}
	private void initTitle(){
		com.leading.baselibrary.ui.TitleBarLayout feedback_tbl=(com.leading.baselibrary.ui.TitleBarLayout)findViewById(R.id.feedback_tbl);
		String typeName = getIntent().getStringExtra("typeName");
		feedback_tbl.getLeftBtn().setVisibility(View.VISIBLE);
		feedback_tbl.getLeftBtn().setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				Intent intent=new Intent(ActivityFeedbackList.this, MainActivity.class);
				ActivityFeedbackList.this.startActivity(intent);
				ActivityFeedbackList.this.finish();
			}});
		if(typeName!=null)feedback_tbl.getTextView().setText(typeName);
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent intent=new Intent(this,MainActivity.class);
			startActivity(intent);
			ActivityFeedbackList.this.finish();
		}
		return false;
	}
	@Override
	protected void initPage() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				lqDao = new LocaleQuestionDao(ActivityFeedbackList.this);
				mainDatabase = new DataBaseManager(ActivityFeedbackList.this);
				Message msg = mUIHandler.obtainMessage(WHAT_DID_LOAD_DATA);
				currentPage=0;
				msg.sendToTarget();
			}
		}).start();
	}

	@Override
	public void onRefresh() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				Message msg = mUIHandler.obtainMessage(WHAT_DID_REFRESH);
				setData();
				msg.sendToTarget();
			}
		}).start();
	}

	@Override
	public void onMore() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				Message msg = mUIHandler.obtainMessage(WHAT_DID_MORE);
				setData();
				msg.sendToTarget();
			}
		}).start();
	}

	private Handler mUIHandler = new Handler() {

		@SuppressWarnings("unchecked")
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case WHAT_DID_LOAD_DATA: {
				if(data!=null)
					data.removeAll(data);
				setData();
				mAdapter.notifyDataSetChanged();
				// 诉它数据加载完毕;
				mPullDownView.notifyDidLoad();
				break;
			}
			case WHAT_DID_REFRESH: {
				mAdapter.notifyDataSetChanged();
				// 告诉它更新完毕
				mPullDownView.notifyDidRefresh();
				break;
			}

			case WHAT_DID_MORE: {
				mAdapter.notifyDataSetChanged();
				// 告诉它获取更多完毕
				mPullDownView.notifyDidMore();
				break;
			}
			}

		}

	};

	private int showCount=15;//每页显示15条
	private int currentPage=0;//当前页
	// 模拟数据
	private void setData() {
		int type = getIntent().getIntExtra("type", 0);
		Map<String,Object> map=null;
		switch (type) {
		case ConstantStore.LQ_TYPE_UNREAD:
			try {
				GenericRawResults<String[]> groupResults=mainDatabase.getMsgGroupDAO().getDao().
				queryRaw("select groupId from MsgGroup where sourceApkName='"+ConstantStore.LQ_NAME+"'");
				if(groupResults!=null){
					List<String[]> gourpList=groupResults.getResults();
					if(gourpList.size()>0){
						String[] arrayStrings=gourpList.get(0);
						int groupId=0;
						if(arrayStrings!=null)
							groupId=Integer.parseInt(arrayStrings[0]);
						StringBuilder msgSql=new StringBuilder();
						msgSql.append("select businessInstanceId,msgTitle,msgDetail,msgPubTime from MsgBean where group_Id=")
						.append(groupId).append(" and").append(" readState=")
						.append(ConstantStore.MSG_TYPE_UNREAD).append(" order by msgPubTime desc ").append(" Limit ")
						.append(showCount).append(" Offset ").append(showCount*currentPage);
						GenericRawResults<String[]> msgResults=mainDatabase.getMsgBeanDAO().getDao().queryRaw(msgSql.toString());
						if(msgResults!=null){
							List<String[]> msgList=msgResults.getResults();
							for(String[] strs:msgList){
								map=new HashMap<String, Object>();
								map.put("type", ConstantStore.LQ_TYPE_UNREAD);
								map.put("fsiid", strs[0]);
								map.put("txtHeading", strs[1]);
								map.put("txtSubHeading", strs[2]);
								map.put("txtTime", strs[3]);
								data.add(map);
							}
							if(msgList.size()>0)
								currentPage++;
						}
					}
				}
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			break;
		/*case ConstantStore.LQ_TYPE_DRAFT:
			setDateDraft(ConstantStore.LQ_TYPE_DRAFT, "ifDraft");
			break;
		case ConstantStore.LQ_TYPE_BESOLVED:
			setDateDraft(ConstantStore.LQ_TYPE_BESOLVED, "qsState");
			break;
		case ConstantStore.LQ_TYPE_NOTTOSOLVE:
			setDateDraft(ConstantStore.LQ_TYPE_NOTTOSOLVE, "qsState");
			break;
		case ConstantStore.LQ_TYPE_HASBEENRESOLVED:
			setDateDraft(ConstantStore.LQ_TYPE_HASBEENRESOLVED, "qsState");
			break;*/
		default:setDateDraft(type);break;
		}
//		int type = 0;
//		for (int i = 0; i < 10; i++) {
//			if (type > 4)
//				type = 0;
//			Map<String, Object> map = new HashMap<String, Object>();
//			map.put("type", type);
//			map.put("fsiid", "" + (i + 1));
//			map.put("txtHeading", "所属系统名称" + i);
//			map.put("txtSubHeading", "信息标题内容" + 1 + "..");
//			map.put("txtTime", "下午12：17 " + (i + 1) + "0" + i);
//			data.add(map);
//		}
	}
	
	public void setDateDraft(int type){
		StringBuilder lqSql=new StringBuilder();
		lqSql.append("select fsiid,sysBelong,title,createDate from LocaleQuestion where ")
		.append("qsState").append("=")
		.append(type).append(" order by createDate desc ").append(" Limit ")
		.append(showCount).append(" Offset ").append(showCount*currentPage);
		GenericRawResults<String[]> lqResults=null;
		try {
			lqResults = lqDao.getDao().queryRaw(lqSql.toString());
			if(lqResults!=null){
				List<String[]> lqList=lqResults.getResults();
				Map<String,Object> map=null;
				for(String[] strs:lqList){
					map=new HashMap<String, Object>();
					map.put("type", type);
					map.put("fsiid", strs[0]);
					map.put("txtHeading", strs[1]);
					map.put("txtSubHeading", strs[2]);
					map.put("txtTime", strs[3]);
					data.add(map);
				}
				if(lqList.size()>0)
					currentPage++;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Override
	public void onDestroy() {
		mPullDownView=null;mAdapter=null;mAdapter=null;
		data=null;mainDatabase=null;lqDao=null;
//		setContentView(R.layout.activity_null);
		super.onDestroy();
	}
	
	
}
