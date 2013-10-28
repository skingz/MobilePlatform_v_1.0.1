package com.leading.localequestion.net;

import java.io.File;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.j256.ormlite.dao.CloseableIterator;
import com.j256.ormlite.dao.GenericRawResults;
import com.leading.baselibrary.database.DataBaseManager;
import com.leading.baselibrary.database.bean.MsgGroup;
import com.leading.baselibrary.ui.DialogAlertUtil;
import com.leading.baselibrary.ui.Loading;
import com.leading.baselibrary.util.StringUtils;
import com.leading.localequestion.ActivityFeedbackUnresolved;
import com.leading.localequestion.dao.LocaleQuestionDao;
import com.leading.localequestion.dao.QuestionAffixDao;
import com.leading.localequestion.dao.SysBelongDao;
import com.leading.localequestion.data.ConstantStore;
import com.leading.localequestion.entity.LocaleQuestion;
import com.leading.localequestion.entity.LocaleQuestionBo;
import com.leading.localequestion.entity.QuestionAffix;
import com.leading.localequestion.entity.QuestionAffixBo;
import com.leading.localequestion.entity.SysBelong;

/**
 * 问题页面子线程加载类.
 * 
 * @author Jiantao.tu
 * 
 */
@SuppressLint("HandlerLeak")
public class FeedbackUnresolvedChild {

	private LocaleQuestionDao lqDao;// 问题本地持久化对象
	public List<Map<String, Object>> listItem = null;// ListView里的所有信息
	private Loading loading;// 加载效果对象
	private Context context;// 当前运行activity
	private LocaleQuestion lq;// 问题对象
	private FeedbackNet feedBackNet;// 请求发送问题对象
	// private boolean isSuccess = true;// 是否连接服务器成功
	private SysBelongDao sysBelongDao;
	private QuestionAffixDao questionAffixDao;
	private String fsiid;
	private int type;
	private DataBaseManager mainDatabase;
	private Handler mainLoadActivity;
	public ChildLoadActivity childLoadActivity;
	public ChildSubmitActivity childSubmitActivity;
	// 声明进度条对话框
	private ProgressDialog dialog;

	public FeedbackUnresolvedChild(Context context, int type, String fsiid,
			List<Map<String, Object>> listItem, Loading loading, Handler handler) {
		this.context = context;
		this.type = type;
		this.fsiid = fsiid;
		this.loading = loading;
		this.listItem = listItem;
		this.mainLoadActivity = handler;
	}

	public FeedbackUnresolvedChild(Context context, LocaleQuestion lq,
			Handler handler, ProgressDialog dialog) {
		this.context = context;
		this.dialog = dialog;
		this.mainLoadActivity = handler;
		this.lq = lq;
	}

	/**
	 * 初始化加载ListView数据信息.
	 */
	public void load() {
		loading.openLoading();
		Message message = null;
		HandlerThread childThread = new HandlerThread("child_thread3");
		childThread.start();
		childLoadActivity = new ChildLoadActivity(
				childThread.getLooper());
		message = childLoadActivity.obtainMessage();
		message.sendToTarget();
	}

	public void showUploadDialog() {
		// 设置进度条风格，风格为圆形，旋转的
		dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		// 设置ProgressDialog 标题
		dialog.setTitle("请稍等正在努力为您提交。");
		// 设置ProgressDialog 提示信息
		dialog.setMessage("正在进行基本信息提交，请稍等....");
		// 设置ProgressDialog 标题图标
		// dialog.setIcon(null);
		// 设置ProgressDialog 的进度条是否不明确
		dialog.setIndeterminate(false);
		// 设置ProgressDialog 是否可以按退回按键取消
		dialog.setCancelable(true);
		// 设置ProgressDialog 的一个Button
		// 让ProgressDialog显示
		dialog.show();
		HandlerThread childThread = new HandlerThread("child_thread4");
		childThread.start();
		childSubmitActivity = new ChildSubmitActivity(
				childThread.getLooper());
		Message message = childSubmitActivity.obtainMessage();
		message.sendToTarget();
	}

	public void requestLocaleQuestion(Long keyId) {
		LocaleQuestionBo lqb = feedBackNet.qsGetInfo(fsiid);
		if(lqb==null){
			DialogAlertUtil.showToast(context,"没有此条问题信息，请确认收到信息无误？");
			return;
		}
		saveUpdateLocaleQuestion(lqb, keyId);// 插入到本地数据库并把问题对象(lq)填充
		mainDatabase.setStateByBusinessInstanceId(fsiid);// 更新主程序消息状态为已读
	}

	public void saveUpdateLocaleQuestion(LocaleQuestionBo lqb, Long keyId) {
		sysBelongDao = new SysBelongDao(context);
		questionAffixDao = new QuestionAffixDao(context);
		SysBelong sysBelong = sysBelongDao.getByFsiid(lqb.getSysBelong());
		if (sysBelong == null
				|| (sysBelong != null && sysBelong.getName() == null)) {
			DialogAlertUtil.showToast(context,"所属系统初始化出错!！");
			((Activity) context).finish();
			return;
		}
		lq = new LocaleQuestion(lqb.getKeyId(), lqb.getQsTitle(),
				lqb.getSubmitPerson(), sysBelong.getName(),
				sysBelong.getFsiid(), lqb.getHappenTime(), lqb.getQsClass(),
				null, lqb.getQsGrade(), lqb.getQsDiscrable(),
				lqb.getNeedFinishTime(), lqb.getQsState(),
				lqb.isIfSolvedRoot(), lqb.getResolvent(), lqb.getManHour(),
				lqb.getRemark(), lqb.getSolvedTime(), null);
		if (keyId != null)
			lq.setKeyId(keyId);
		lqDao.createOrUpdate(lq);
		List<QuestionAffixBo> list = lqb.getAffixList();
		for (QuestionAffixBo qabo : list) {
			QuestionAffix qa = questionAffixDao.query("fsiid", qabo.getKeyId());
			if (qa == null) {
				qa = new QuestionAffix();
				qa.setAffixName(qabo.getAffixName());
				qa.setFsiid(qabo.getKeyId());
				qa.setLocaleQuestion(lq);
				questionAffixDao.createOrUpdate(qa);
			}
		}
	}

	/**
	 * 初始化显示数据
	 */

	public int childTwo = 2;

	public class ChildSubmitActivity extends Handler {
		public ChildSubmitActivity(Looper looper) {
			super(looper);
		}

		@Override
		public void handleMessage(Message msg) {
			feedBackNet = new FeedbackNet(context);
			ActivityFeedbackUnresolved.executeChild = childTwo;
			String[] results = new String[2];
			String result = null;
			if (lq.getQsState() == ConstantStore.LQ_TYPE_HASBEENRESOLVED)
				result = feedBackNet.qsResolved(lq);
			if (lq.getQsState() == ConstantStore.LQ_TYPE_NOTTOSOLVE)
				result = feedBackNet.qsTempNoResolved(lq);
			if (feedBackNet.isSuccess()) {
				lqDao = new LocaleQuestionDao(context);
				LocaleQuestion lqone = lqDao.query("fsiid", lq.getFsiid());
				lqone.setIfSolvedRoot(lq.isIfSolvedRoot());
				lqone.setManHour(lq.getManHour());
				lqone.setQsState(lq.getQsState());
				lqone.setResolvent(lq.getResolvent());
				lqone.setRemark(lq.getRemark());
				lqDao.createOrUpdate(lqone);
				results[0] = ConstantStore.SUCCESS;
			} else
				results[0] = ConstantStore.ERROR;
			results[1] = result;
			Message message = mainLoadActivity.obtainMessage();
			message.obj = results;
			message.sendToTarget();
			super.handleMessage(msg);
		}
	}

	public int childOne = 1;

	/**
	 * 初始化显示数据线程
	 * 
	 * @author tjt
	 * 
	 */
	public class ChildLoadActivity extends Handler {
		public ChildLoadActivity(Looper looper) {
			super(looper);
		}

		@Override
		public void handleMessage(Message msg) {
			ActivityFeedbackUnresolved.executeChild = childOne;
			if (StringUtils.isNotNull(fsiid)) {
				feedBackNet = new FeedbackNet(context);
				mainDatabase = new DataBaseManager(context);
				lqDao = new LocaleQuestionDao(context);
				lq = lqDao.query("fsiid", fsiid);
				if (lq == null) {// 如果本地没数据就从服务器上取
					requestLocaleQuestion(null);
				} else {
					if (type != ConstantStore.LQ_TYPE_DRAFT) {
						boolean flag = false;
						if (type == ConstantStore.LQ_TYPE_UNREAD)// 如果是未读就直接访问服务器获取数据进行更新插入
							requestLocaleQuestion(lq.getKeyId());
						else {
							MsgGroup group = mainDatabase.getMsgGroupDAO()
									.query("groupName", ConstantStore.LQ_NAME);
							if (group != null) {
								int groupId = group.getGroupId();
								try {
									GenericRawResults<String[]> results = mainDatabase
											.getMsgBeanDAO()
											.getDao()
											.queryRaw(
													"select businessInstanceId from MsgBean where readState="
															+ ConstantStore.LQ_TYPE_UNREAD
															+ " and group_Id="
															+ groupId);
									List<String[]> resultArray = results
											.getResults();
									for (String[] strings : resultArray) {// 如果当前消息存在于未读消息内就访问服务器更新插入
										if (fsiid.equals(strings[0])) {
											flag = true;
											break;
										}
									}
									results.close();
								} catch (SQLException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
							if (flag)
								requestLocaleQuestion(lq.getKeyId());
						}
					}
				}
			} else {
				Log.e("FeedbackUnresolvedChild_ChildLoadActivity", "fsiid不能为空!");
			}
			if (lq != null) {
				// 生成动态数组，加入数据
				CloseableIterator<QuestionAffix> list = null;
				try {
					list = lq.getQuestionAffixs().closeableIterator();
				} catch (Exception e) {
					lq = lqDao.query(lq.getKeyId());
					list = lq.getQuestionAffixs().closeableIterator();
					e.printStackTrace();
				}
				HashMap<String, Object> newMap = null;
				if (list != null) {
					while (list.hasNext()) {
						QuestionAffix qa = list.next();
						newMap = new HashMap<String, Object>();
						newMap.put("affixName", qa.getAffixName());
						String affixPath = null;
						if (StringUtils.isNotNull(qa.getAffixPath())) {// 如果图片地址不为空
							File file = new File(qa.getAffixPath());
							if (!file.exists())// 查看缓存里面是否存在图片不存在则从服务器获取
								affixPath = getImgPathStorager(qa);
							else
								affixPath = "";
						} else
							affixPath = getImgPathStorager(qa);
						if (affixPath != null) {
							newMap.put("affixPath", qa.getAffixPath());
							newMap.put("keyid", qa.getKeyid());
							listItem.add(newMap);
						}
					}
				}
			} //else
//				Log.e("FeedbackUnresolvedChild_ChildLoadActivity",
//						"服务器获取失败，可能服务端没开起服务或者IP地址有误！");
			Message message = mainLoadActivity.obtainMessage();
			message.obj = lq;
			message.sendToTarget();
		}
	}

	public String getImgPathStorager(QuestionAffix qa) {
		String path = feedBackNet.qsAffixDownLoad(qa.getFsiid());
		if (path == null) {
			DialogAlertUtil.showToast(context,"服务器名为：【" + qa.getAffixName()+ "】的图片不存在！");
		} else {
			qa.setAffixPath(path);
			questionAffixDao.createOrUpdate(qa);
		}
		return path;
	}

}
