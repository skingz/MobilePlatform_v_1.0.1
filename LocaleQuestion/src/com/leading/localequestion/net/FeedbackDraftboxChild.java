package com.leading.localequestion.net;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.j256.ormlite.dao.CloseableIterator;
import com.leading.baselibrary.ui.ActivityCommon;
import com.leading.baselibrary.ui.Loading;
import com.leading.baselibrary.ui.SingleLineEditView;
import com.leading.baselibrary.util.SolveStyleQuestionUtil;
import com.leading.baselibrary.util.StringUtils;
import com.leading.localequestion.ActivityFeedbackList;
import com.leading.localequestion.R;
import com.leading.localequestion.dao.LocaleQuestionDao;
import com.leading.localequestion.dao.SysBelongDao;
import com.leading.localequestion.data.ConstantStore;
import com.leading.localequestion.data.FeedbackListAdapter;
import com.leading.localequestion.entity.LocaleQuestion;
import com.leading.localequestion.entity.QuestionAffix;
import com.leading.localequestion.entity.SysBelong;

/**
 * 问题页面子线程加载类.
 * 
 * @author Jiantao.tu
 * 
 */
@SuppressLint("HandlerLeak")
public class FeedbackDraftboxChild {

	private LocaleQuestionDao lqDao;// 问题本地持久化对象
	public List<Map<String, Object>> listItem = null;// ListView里的所有信息
	public ChildFindImages childFindImages;
	public ChildUpdateDialogMessage childUpdateDialogMessage;
	private ListView listView; // 需要操作的ListView控件
	private Loading loading;// 加载效果对象
	private ActivityCommon activity;// 当前运行activity
	private LocaleQuestion lq;// 问题对象
	private FeedbackNet feedBackNet;// 请求发送问题对象
	private boolean isSuccess = true;// 是否连接服务器成功
	private boolean flag=true;
	private SingleLineEditView sysBelong;
	public FeedbackDraftboxChild(ActivityCommon activity, ListView listView,
			LocaleQuestion lq,SingleLineEditView sysBelong) {
		this.activity = activity;
		this.listView = listView;
		this.lq = lq;
		this.sysBelong = sysBelong;
		feedBackNet = new FeedbackNet(activity);
		lqDao = new LocaleQuestionDao(activity);
	}

	/**
	 * 初始化加载ListView数据信息.
	 */
	public void loadListView(boolean flag) {
		this.flag=flag;
		loading = Loading.getLoading(activity, R.id.feedback_draftbox);
		loading.openLoading();
		HandlerThread childThread = new HandlerThread("child_thread");
		childThread.start();
		childFindImages = new ChildFindImages(childThread.getLooper());
		Message message = childFindImages.obtainMessage();
		message.sendToTarget();
	}

	/**
	 * 初始化ListView UI
	 */
	private Handler mainResultImages = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			BaseAdapter mAdapter = new FeedbackListAdapter(activity, listItem);
			listView.setAdapter(mAdapter);
			new SolveStyleQuestionUtil()
					.setListViewHeightBasedOnChildren(listView);
			if(flag){
				List<Map<String, Object>> items=(List<Map<String, Object>>)msg.obj;
				sysBelong.showDropDownOptions(items, "项目列表");
				if(StringUtils.isNotNull(lq.getSysBelong())&&StringUtils.isNotNull(lq.getSysBelongId())){
					sysBelong.setValue(lq.getSysBelongId());
					sysBelong.setShowValue(lq.getSysBelong());
				}
			}
			// 添加并且显示
			loading.closeLoading();
			super.handleMessage(msg);
		}
	};

	/**
	 * 查询SqlLite里的当前问题图片并初始化ListView.
	 * 
	 * @author tjt
	 * 
	 */
	public class ChildFindImages extends Handler {
		public ChildFindImages(Looper looper) {
			super(looper);
		}

		@Override
		public void handleMessage(Message msg) {
			// 生成动态数组，加入数据
			listItem = new ArrayList<Map<String, Object>>();
			CloseableIterator<QuestionAffix> list = lq.getQuestionAffixs()
					.closeableIterator();
			HashMap<String, Object> newMap = null;
			if (list != null) {
				while (list.hasNext()) {
					QuestionAffix qa = list.next();
					newMap = new HashMap<String, Object>();
					newMap.put("affixName", qa.getAffixName());
					newMap.put("affixPath", qa.getAffixPath());
					newMap.put("keyid", qa.getKeyid());
					listItem.add(newMap);
				}
			}
			Message message = mainResultImages.obtainMessage();
			if(flag)
				message.obj=addDialogDate();
			message.sendToTarget();
		}
	}
	
	public List<Map<String, Object>> addDialogDate() {
		SysBelongDao sbd = new SysBelongDao(activity);
		List<SysBelong> projectList = sbd.queryForAll();
		if(projectList==null||projectList.size()==0){
			sbd.UpdateProjectEnum();
			projectList = sbd.queryForAll();
		}
		List<Map<String, Object>> items = new ArrayList<Map<String, Object>>();
		for (SysBelong sb : projectList) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put(SingleLineEditView.SELECTED_NAME, sb.getName());
			map.put(SingleLineEditView.SELECTED_KEY, sb.getFsiid());
			items.add(map);
		}
		return items;
	}

	// 声明进度条对话框
	private ProgressDialog dialog;

	private final String MESSAGE = "正在进行基本信息提交，请稍等....";

	/**
	 * 初始化提交上传窗口.
	 */
	public void showUploadDialog() {
		// 创建ProgressDialog对象
		dialog = new ProgressDialog(activity);
		// 设置进度条风格，风格为圆形，旋转的
		dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		// 设置ProgressDialog 标题
		dialog.setTitle("请稍等正在努力为您提交。");
		// 设置ProgressDialog 提示信息
		dialog.setMessage(MESSAGE);
		// 设置ProgressDialog 标题图标
		// dialog.setIcon(null);
		// 设置ProgressDialog 的进度条是否不明确
		dialog.setIndeterminate(false);
		// 设置ProgressDialog 是否可以按退回按键取消
		dialog.setCancelable(true);
		// 设置ProgressDialog 的一个Button
		dialog.setButton("确定", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int i) {
				// 点击“确定按钮”取消对话框
				dialog.cancel();
			}
		});
		// 让ProgressDialog显示
		dialog.show();
		HandlerThread childThread = new HandlerThread("child_thread1");
		childThread.start();
		childUpdateDialogMessage = new ChildUpdateDialogMessage(
				childThread.getLooper());
		Message message = childUpdateDialogMessage.obtainMessage();
		message.sendToTarget();
	}

	/**
	 * 信息提交中页面UI效果处理
	 */
	private Handler mainUpdateDialogMessage = new Handler() {
		@Override
		public synchronized void handleMessage(Message msg) {
			Object[] o = (Object[]) msg.obj;
			if ((Boolean) o[0] == true) {
				activity.showToast("提交成功！");
				Intent intent=new Intent(activity,ActivityFeedbackList.class);
				intent.putExtra("type", ConstantStore.LQ_TYPE_DRAFT);
				intent.putExtra("typeName", "草稿箱");
				activity.startActivity(intent);
				activity.finish();
				dialog.cancel();// 关闭提示窗口
			} else {
				dialog.setIcon((Drawable) o[2]);// 设置标题图标
				dialog.setMessage(o[1].toString());// 设置提示信息
			}
			super.handleMessage(msg);
		}
	};

	/**
	 * 提交信息的处理线程类.
	 * 
	 * @author tjt
	 * 
	 */
	public class ChildUpdateDialogMessage extends Handler {
		public ChildUpdateDialogMessage(Looper looper) {
			super(looper);
		}

		@Override
		public void handleMessage(Message msg) {
			Object[] o = new Object[3];// 0是否提交完成,2图片,1分别储存显示消息
			o[0] = false;
			lq.setQsState(ConstantStore.LQ_TYPE_BESOLVED);
			if (listItem == null || listItem.size() == 0) {// 没有图片信息直接提交基本信息
				feedBackNet.qsSubmit(lq);// 提交基本信息
				o[2] = null;
				o[0] = true;
			} else {
				Drawable drawable = null;
				for (int i = 0; i < listItem.size() + 2; i++) {
					if (!isSuccess) {
						activity.showToast( "提交失败");
						o[0] = true;
						break;
					}
					if (i < listItem.size() + 1) {// 排除最后一次循环
						if (i == 0) {// 第一次循环进行基本信息提交
							feedBackNet.qsSubmit(lq);// 提交基本信息
							if (!feedBackNet.isSuccess())
								isSuccess = false;
							
							// 更改UI封装信息
							o[1] = "共有" + listItem.size() + "张图正在提交目前正在提交第"
									+ (i + 1) + "张";
							drawable = Drawable.createFromPath(listItem.get(i)
									.get("affixPath").toString());
							o[2] = drawable;
						} else {
							// 提交..
							if (i == listItem.size()) {// 最后一张图片不需要更改UI封装信息
								feedBackNet.qsAffixUpload(listItem.get(i - 1)
										.get("affixPath").toString());// 提交图片信息
							} else {
								feedBackNet.qsAffixUpload(listItem.get(i - 1)
										.get("affixPath").toString());// 提交图片信息
								o[1] = "共有" + listItem.size() + "张图正在提交目前正在提交第"
										+ (i + 1) + "张";
								drawable = Drawable.createFromPath(listItem
										.get(i).get("affixPath").toString());
								o[2] = drawable;
							}
							if (!feedBackNet.isSuccess())
								isSuccess = false;
						}
					} else {// 最后一次循环关闭提示窗口
						/*feedBackNet.qsSendNewMsg(lq.getFsiid(), lq.getUserId());
						if (!feedBackNet.isSuccess())
							DialogAlertUtil.showToast(activity, "提交失败");
						o[2] = null;
						o[0] = true;*/
					}
					// 生成动态数组，加入数据
					Message message = mainUpdateDialogMessage.obtainMessage();
					message.obj = o;
					message.sendToTarget();
				}
			}
			//前面信息提交成功后 发送转发通知
			if (isSuccess){
				feedBackNet.qsSendNewMsg(lq.getFsiid(), lq.getUserId());
				if (!feedBackNet.isSuccess())
					activity.showToast("提交失败");
				o[2] = null;
				o[0] = true;
				if (!feedBackNet.isSuccess())
					isSuccess = false;
			}
			// 如果提交不成功,将未解决状态置成草稿状态
			if (!isSuccess) {
				lq.setQsState(ConstantStore.LQ_TYPE_DRAFT);
			}
			lqDao.createOrUpdate(lq);
			
			isSuccess = true;
			Message message = mainUpdateDialogMessage.obtainMessage();
			message.obj = o;
			message.sendToTarget();
		}
	}

}
