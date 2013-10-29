package com.leading.localequestion;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TableRow;

import com.leading.baselibrary.ui.ActivityCommon;
import com.leading.baselibrary.ui.Loading;
import com.leading.baselibrary.ui.SingleLineEditView;
import com.leading.baselibrary.ui.TitleBarLayout;
import com.leading.baselibrary.util.DateUtil;
import com.leading.baselibrary.util.StringUtils;
import com.leading.localequestion.data.ConstantStore;
import com.leading.localequestion.data.FeedbackGridAdapter;
import com.leading.localequestion.entity.LocaleQuestion;
import com.leading.localequestion.global.LQApplication;
import com.leading.localequestion.net.FeedbackUnresolvedChild;
import com.leading.localequestion.ui.QuestionMessageReadView;

@SuppressLint("HandlerLeak")
public class ActivityFeedbackUnresolved extends ActivityCommon {
	
	private GridView gridView;
	private QuestionMessageReadView qmv;
	private boolean isFrist = true;
	private TitleBarLayout title;
	private FeedbackUnresolvedChild child;
	private SingleLineEditView fuJjfa;
	private SingleLineEditView fuWtzt;
	private SingleLineEditView fuBzsm;
	private EditText fuEdtFsgs;
	private CheckBox fuCkbSfcdgc;
	private LocaleQuestion lq;
	private List<Map<String, Object>> listItem;
	public static int executeChild;
	private ProgressDialog dialog;
	private Loading loading;
	private TableRow tr;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_feedback_unresolved);
		gridView = (GridView) findViewById(R.id.fu_lv_xczp);
		title = (TitleBarLayout) findViewById(R.id.fu_tbl);
		fuJjfa = (SingleLineEditView) findViewById(R.id.fu_jjfa);
		fuWtzt = (SingleLineEditView) findViewById(R.id.fu_wtzt);
		fuBzsm = (SingleLineEditView) findViewById(R.id.fu_bzsm);
		fuEdtFsgs = (EditText) findViewById(R.id.fu_edt_fsgs);
		fuCkbSfcdgc = (CheckBox) findViewById(R.id.fu_ckb_sfcdgc);
		tr = (TableRow) findViewById(R.id.fu_tr_fsgs);
		fuWtzt.setRadioOnclickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) tr
						.getLayoutParams();
				if (v.getTag().equals(
						String.valueOf(ConstantStore.LQ_TYPE_NOTTOSOLVE))) {
					lp.addRule(RelativeLayout.BELOW, R.id.fu_bzsm);
					tr.setLayoutParams(lp);
					fuBzsm.setVisibility(View.VISIBLE);
					fuJjfa.setVisibility(View.GONE);
					fuJjfa.setValue("");
				} else if (v.getTag().equals(
						String.valueOf(ConstantStore.LQ_TYPE_HASBEENRESOLVED))) {
					lp.addRule(RelativeLayout.BELOW, R.id.fu_jjfa);
					tr.setLayoutParams(lp);
					fuBzsm.setVisibility(View.GONE);
					fuJjfa.setVisibility(View.VISIBLE);
					fuBzsm.setValue("");
				}
			}
		});

	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			resultActivity();
			try {
				mAdapter.finalize();
			} catch (Throwable e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	public void resultActivity() {
		Intent intent = new Intent(this, ActivityFeedbackList.class);
		switch (type) {
		case ConstantStore.LQ_TYPE_UNREAD:
			intent.putExtra("type", ConstantStore.LQ_TYPE_UNREAD);
			intent.putExtra("typeName", "未读信息");
			startActivity(intent);
		case ConstantStore.LQ_TYPE_BESOLVED:
			intent.putExtra("type", ConstantStore.LQ_TYPE_BESOLVED);
			intent.putExtra("typeName", "待解决");
			startActivity(intent);
			break;
		case ConstantStore.LQ_TYPE_HASBEENRESOLVED:
			intent.putExtra("type", ConstantStore.LQ_TYPE_HASBEENRESOLVED);
			intent.putExtra("typeName", "已解决");
			startActivity(intent);
			break;
		case ConstantStore.LQ_TYPE_NOTTOSOLVE:
			intent.putExtra("type", ConstantStore.LQ_TYPE_NOTTOSOLVE);
			intent.putExtra("typeName", "暂不解决");
			startActivity(intent);
			break;
		}
		this.finish();
	}

	public LocaleQuestion setSubmitDate() {
		String fsiid = lq.getFsiid();
		lq = new LocaleQuestion();
		lq.setCreateDate(null);
		lq.setUserId(LQApplication.getConfig().getUserId());
		lq.setFsiid(fsiid);
		if (StringUtils.isNotNull(fuJjfa.getValue()))
			lq.setResolvent(fuJjfa.getValue());
		if (StringUtils.isNotNull(fuEdtFsgs.getText())) {
			try {
				lq.setManHour(Float.valueOf(fuEdtFsgs.getText().toString()));
			} catch (NumberFormatException e) {
				super.showToast("工时格式错误！");
				return null;
			}
		}
		if (StringUtils.isNotNull(fuBzsm.getValue()))
			lq.setRemark(fuBzsm.getValue());
		if (fuCkbSfcdgc.isChecked())
			lq.setIfSolvedRoot(true);
		lq.setQsState(Integer.parseInt(fuWtzt.getValue()));
		return lq;
	}

	public void titleLoad() {
		title.getRightBtn().setVisibility(View.VISIBLE);
		title.getRightBtn().setText("提交");
		title.getRightBtn().setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (Integer.parseInt(fuWtzt.getValue()) == ConstantStore.LQ_TYPE_NOTTOSOLVE
						&& fuCkbSfcdgc.isChecked()) {
					ActivityFeedbackUnresolved.this.showToast("您选择了彻底根除就不能选择暂不解决！");
					return;
				}
				dialog = new ProgressDialog(ActivityFeedbackUnresolved.this);
				LocaleQuestion lq=setSubmitDate();
				if(lq==null)
					return;
				child = new FeedbackUnresolvedChild(
						ActivityFeedbackUnresolved.this,lq,
						mainLoadActivity, dialog);

				child.showUploadDialog();
			}
		});
	}
	private FeedbackGridAdapter mAdapter=null;
	public void loadData() {
		mAdapter= new FeedbackGridAdapter(this, listItem);
		gridView.setAdapter(mAdapter);
		// new SolveStyleQuestionUtil()
		// .setListViewHeightBasedOnChildren(GridView);
		qmv = (QuestionMessageReadView) findViewById(R.id.fu_cgx_jbxx);
		qmv.getUqm_bt().setRightTextValue(lq.getTitle(), false);
		qmv.getUqm_ssxt().setRightTextValue(lq.getSysBelong(), false);
		qmv.getUqm_wtdj().setRightTextValue(
				ConstantStore.getGrade(lq.getQsGrade()), false);
		qmv.getUqm_wtfl().setRightTextValue(lq.getQsClass(), false);
		qmv.getUqm_fssj().setRightTextValue(
				DateUtil.getDateToString(lq.getHappenTime(), DateUtil.YMD),
				false);
		qmv.getUqm_wtms().setRightTextValue(lq.getQsDiscrable(), true);
		qmv.getUqm_yqjjsj().setRightTextValue(
				DateUtil.getDateToString(lq.getNeedFinishTime(), DateUtil.YMD),
				false);
		qmv.getUqm_yqjjsj().setValue(
				DateUtil.getDateToString(lq.getNeedFinishTime(), DateUtil.YMD));
		if (lq.getQsState() != ConstantStore.LQ_TYPE_BESOLVED) {
			if (lq.getQsState() == ConstantStore.LQ_TYPE_NOTTOSOLVE) {
				RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) tr
						.getLayoutParams();
				fuWtzt.setValue(String
						.valueOf(ConstantStore.LQ_TYPE_NOTTOSOLVE));
				lp.addRule(RelativeLayout.BELOW, R.id.fu_bzsm);
				tr.setLayoutParams(lp);
				fuBzsm.setVisibility(View.VISIBLE);
				fuBzsm.setRightTextValue(lq.getRemark(), true);
				fuJjfa.setVisibility(View.GONE);
				fuJjfa.setValue("");
				titleLoad();
			} else {
				fuWtzt.setValue(String
						.valueOf(ConstantStore.LQ_TYPE_HASBEENRESOLVED));
			}
			if (lq.getResolvent() != null)
				fuJjfa.setValue(lq.getResolvent());
			if (lq.getManHour() != null)
				fuEdtFsgs.setText(String.valueOf(lq.getManHour()));
			else fuEdtFsgs.setText("0");
			
			if (lq.isIfSolvedRoot() != null) {
				if (lq.isIfSolvedRoot())
					fuCkbSfcdgc.setChecked(true);
			}
			if (lq.getQsState() == ConstantStore.LQ_TYPE_HASBEENRESOLVED) {
				SingleLineEditView fuJjsj = (SingleLineEditView) findViewById(R.id.fu_jjsj);
				tr.setBackgroundResource(R.drawable.widget_kv_top);
				fuJjsj.setEnabled(false);
				fuJjsj.setValue(DateUtil.getDateToString(lq.getSolvedTime(),
						DateUtil.YMDHM));
				fuCkbSfcdgc.setEnabled(false);
				fuEdtFsgs.setFocusable(false);
				fuJjfa.setRightTextValue(lq.getResolvent(), true);
				fuWtzt.forbiddenWidget();
			}
		} else {
			titleLoad();
			//fuWtzt.setValue(String.valueOf(ConstantStore.LQ_TYPE_HASBEENRESOLVED));
		}

	}

	public Handler mainLoadActivity = new Handler() {

		public void handleMessage(Message msg) {
			if (executeChild == child.childTwo) {
				dialog.cancel();
				String[] results = (String[]) msg.obj;
				if (results[0] == ConstantStore.SUCCESS) {
					ActivityFeedbackUnresolved.this.showToast("提交成功！");
					resultActivity();
				} else {
					ActivityFeedbackUnresolved.this.showToast("提交失败："+ results[1]);
				}
			} else if (executeChild == child.childOne) {
				lq = (LocaleQuestion) msg.obj;
				// 添加并且显示
				if (lq != null)
					loadData();
				else{
					resultActivity();
				}
				loading.closeLoading();
			}
			super.handleMessage(msg);
		}
	};

	int type = 0;

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		if (hasFocus && isFrist) {
			String ifNotification = getIntent()
					.getStringExtra("ifNotification");
			type = getIntent()
					.getIntExtra("type", ConstantStore.LQ_TYPE_UNREAD);
			if (StringUtils.isNotNull(ifNotification))
				type = ConstantStore.LQ_TYPE_UNREAD;
			String fsiid = getIntent().getStringExtra("fsiid");
			listItem = new ArrayList<Map<String, Object>>();
			loading = Loading.getLoading(this, R.id.feedback_unresolved);
			child = new FeedbackUnresolvedChild(this, type, fsiid, listItem,
					loading, mainLoadActivity);
			child.load();
			isFrist = false;
		}
		super.onWindowFocusChanged(hasFocus);
	}

	@Override
	public void onDestroy() {
		if(child.childLoadActivity!=null)
			child.childLoadActivity.getLooper().quit();
		if(child.childSubmitActivity!=null)
			child.childSubmitActivity.getLooper().quit();
		qmv=null;gridView=null;title=null;child=null;
		lq=null;listItem=null;dialog=null;loading=null;
//		setContentView(R.layout.activity_null);
		super.onDestroy();
	}

}
