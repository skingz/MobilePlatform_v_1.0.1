package com.leading.localequestion;

import java.io.File;
import java.util.Date;
import java.util.UUID;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import com.leading.baselibrary.ui.ActivityCommon;
import com.leading.baselibrary.ui.BackgourdSwitch;
import com.leading.baselibrary.ui.Loading;
import com.leading.baselibrary.ui.SingleLineEditView;
import com.leading.baselibrary.ui.TitleBarLayout;
import com.leading.baselibrary.util.DateUtil;
import com.leading.baselibrary.util.FileBeanMakeUp;
import com.leading.baselibrary.util.ImgFactory;
import com.leading.baselibrary.util.StringUtils;
import com.leading.localequestion.dao.LocaleQuestionDao;
import com.leading.localequestion.dao.SysBelongDao;
import com.leading.localequestion.data.ConstantStore;
import com.leading.localequestion.entity.LocaleQuestion;
import com.leading.localequestion.global.LQApplication;
import com.leading.localequestion.net.FeedbackDraftboxChild;
import com.leading.localequestion.ui.QuestionMessageSubmitView;

@SuppressLint("HandlerLeak")
public class ActivityFeedbackDraftbox extends ActivityCommon {
	private ImageView feedbackXczpwjjImg;
	private ImageView feedbackXczpzxjImg;
	public static final int NONE = 0;
	public static final int PHOTOHRAPH = 1;// 拍照
	public static final int PHOTOZOOM = 2; // 缩放
	public static final int PHOTORESOULT = 3;// 结果
	public static final int IMGSAVE = 4;// 图片保存后
	private String imgUUID;
	private Button btnTitleLeft;
	private Button btnTitleRight;
	private ListView listView;
	private LocaleQuestion lq;
	private FeedbackDraftboxChild service;
	private LocaleQuestionDao lqDao;
	private boolean isFrist = true;
	private QuestionMessageSubmitView qmv;
	private Loading loading;// 加载效果对象

	@SuppressLint("SdCardPath")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_feedback_draftbox);
		feedbackXczpwjjImg = (ImageView) findViewById(R.id.feedback_xczpwjj_img);
		feedbackXczpzxjImg = (ImageView) findViewById(R.id.feedback_xczpzxj_img);
		// TextView text = (TextView) findViewById(R.id.feedback_txt_ywwfkgxw);
		listView = (ListView) findViewById(R.id.feedback_lv_xczp);
		feedbackXczpwjjImg.setOnClickListener(new BatPhoto());
		feedbackXczpzxjImg.setOnClickListener(new BatPhoto());
		BackgourdSwitch.setButtonFocusChanged(feedbackXczpwjjImg);
		BackgourdSwitch.setButtonFocusChanged(feedbackXczpzxjImg);
		initTitleBar();
		BackgourdSwitch.setButtonFocusChanged(btnTitleLeft);
		BackgourdSwitch.setButtonFocusChanged(btnTitleRight);
		/**
		 * 
		 */
		qmv = (QuestionMessageSubmitView) findViewById(R.id.feedback_cgx_jbxx);
		initPage();
	}
	@Override
	protected void initPage(){
		lq = new LocaleQuestion();
		lqDao = new LocaleQuestionDao(this);
		initLQuestion();
		lq.setUserId(LQApplication.getConfig().getUserId());
		SingleLineEditView ssxt = qmv.getUqm_ssxt();
		service = new FeedbackDraftboxChild(this, listView, lq, ssxt);
	} 
	private void initTitleBar() {
		TitleBarLayout titleBar = (TitleBarLayout) findViewById(R.id.feedback_tbl);
		btnTitleLeft = titleBar.getLeftBtn();
		btnTitleLeft.setText("存草稿");
		/*
		 * UnitConversionUtil conversion=UnitConversionUtil.getInstance(this,
		 * 40f, UnitConversionUtil.DIPTOPX_TYPE);
		 * btnTitleLeft.setLayoutParams(new
		 * android.widget.RelativeLayout.LayoutParams(
		 * android.widget.RelativeLayout.LayoutParams.WRAP_CONTENT
		 * ,conversion.allConversion()));
		 */
		btnTitleRight = titleBar.getRightBtn();
		btnTitleRight.setText("提交");
		btnTitleLeft.setVisibility(View.VISIBLE);
		btnTitleRight.setVisibility(View.VISIBLE);
		TitleButtonListener titleButtonListener = new TitleButtonListener();
		btnTitleLeft.setOnClickListener(titleButtonListener);
		btnTitleRight.setOnClickListener(titleButtonListener);
	}

	private void initLQuestion() {

		String fsiid = getIntent().getStringExtra("fsiid");
		if (fsiid != null) {
			lq = lqDao.query("fsiid", fsiid);
			qmv.getUqm_bt().setValue(lq.getTitle());
			qmv.getUqm_ssxt().setValue(lq.getSysBelongId());
			qmv.getUqm_ssxt().setShowValue(lq.getSysBelong());
			qmv.getUqm_wtdj().setValue(String.valueOf(lq.getQsGrade()));
			qmv.getUqm_wtfl().setValue(lq.getQsClass());
			qmv.getUqm_fssj().setShowValue(
					DateUtil.getDateToString(lq.getHappenTime(), DateUtil.YMD));
			qmv.getUqm_fssj().setValue(
					DateUtil.getDateToString(lq.getHappenTime(),
							DateUtil.FULLFORMAT));
			qmv.getUqm_wtms().setValue(lq.getQsDiscrable());
			qmv.getUqm_yqjjsj().setShowValue(
					DateUtil.getDateToString(lq.getNeedFinishTime(),
							DateUtil.YMD));
			qmv.getUqm_yqjjsj().setValue(
					DateUtil.getDateToString(lq.getNeedFinishTime(),
							DateUtil.FULLFORMAT));
		} else {
			lqDao.DeleteNullDraft();
			lqDao.create(lq);
			lq = lqDao.query(lq.getKeyId());
			Date dtNow = new Date();
			qmv.getUqm_fssj()
					.setValue(
							DateUtil.getDateToString(dtNow,
									ConstantStore.TIME_PATTERN));
			qmv.getUqm_fssj().setShowValue(
					DateUtil.getDateToString(dtNow, DateUtil.YMD));
		}
	}

	/**
	 * 
	 */
	public boolean pottingSaveLocaleQuestion(boolean isDraftBox) {
		String bt = qmv.getUqm_bt().getValue();
		String ssxt = qmv.getUqm_ssxt().getValue();
		String wtdj = qmv.getUqm_wtdj().getValue();
		String wtfl = qmv.getUqm_wtfl().getValue();
		String fssj = qmv.getUqm_fssj().getValue();
		String wtms = qmv.getUqm_wtms().getValue();
		String yqjjsj = qmv.getUqm_yqjjsj().getValue();
		if (!isDraftBox) {
			if (!StringUtils.isNotNull(bt) || !StringUtils.isNotNull(ssxt)
					|| !StringUtils.isNotNull(wtdj)
					|| !StringUtils.isNotNull(wtfl)
					|| !StringUtils.isNotNull(fssj)
					|| !StringUtils.isNotNull(wtms)
					|| !StringUtils.isNotNull(yqjjsj)) {
				super.showToast("信息填写不完整，请全部填写！");
				return false;
			}
		}
		if (StringUtils.isNotNull(bt))
			lq.setTitle(bt);
		if (StringUtils.isNotNull(fssj))
			lq.setHappenTime(DateUtil
					.getStringToDate(fssj, DateUtil.FULLFORMAT));
		if (StringUtils.isNotNull(yqjjsj))
			lq.setNeedFinishTime(DateUtil.getStringToDate(yqjjsj,
					DateUtil.FULLFORMAT));
		if (StringUtils.isNotNull(wtms))
			lq.setQsDiscrable(wtms);
		if (StringUtils.isNotNull(wtfl))
			lq.setQsClass(wtfl);
		if (StringUtils.isNotNull(wtdj)) {
			int grade = Integer.parseInt(wtdj);
			lq.setQsGrade(grade);
		}
		if (StringUtils.isNotNull(ssxt)) {
			lq.setSysBelongId(ssxt);
			lq.setSysBelong(qmv.getUqm_ssxt().getShowValue());
		}
		return true;
	}

	class TitleButtonListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case com.leading.baselibrary.R.id.titlebar_left_button:
				pottingSaveLocaleQuestion(true);
				lq.setQsState(ConstantStore.LQ_TYPE_DRAFT);
				lqDao.createOrUpdate(lq);
				backToDarft();
				break;
			case com.leading.baselibrary.R.id.titlebar_right_button:
				boolean flag = pottingSaveLocaleQuestion(false);
				if (flag)
					service.showUploadDialog();
				break;
			}
		}

	}
	private void backToDarft(){
		Intent intent = new Intent(ActivityFeedbackDraftbox.this,ActivityFeedbackList.class);
		intent.putExtra("type", ConstantStore.LQ_TYPE_DRAFT);
		intent.putExtra("typeName", "草稿箱");
		ActivityFeedbackDraftbox.this.startActivity(intent);
		ActivityFeedbackDraftbox.this.finish();
	}
	/**
	 * 加载Activity时候在获取初始化调用ChildFindImages启动子线程.
	 */
	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		if (hasFocus && isFrist) {
			service.loadListView(true);
			isFrist = false;
		}
		super.onWindowFocusChanged(hasFocus);
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			backToDarft();
		}
		return false;
	}

	/**
	 * 照片拍照选择与处理.
	 * 
	 * @author tjt
	 * 
	 */
	private class BatPhoto implements OnClickListener {
		@Override
		public void onClick(View v) {
			LQApplication.Imglocation = ConstantStore.IMG_LOCATION;
			File file = new File(LQApplication.Imglocation);
			if (!file.exists())
				file.mkdirs();
			imgUUID = UUID.randomUUID().toString();
			LQApplication.Imglocation += "/" + imgUUID + ".jpg";
			if (v.getId() == R.id.feedback_xczpzxj_img) {
				String status = Environment.getExternalStorageState();
				if (!status.equals(Environment.MEDIA_MOUNTED)) {// 判断是否有SD卡
					ActivityFeedbackDraftbox.this.showToast("没有找到SD卡或者正在使用请关闭usb连接模式");
					return;
				}
				// 调用系统的拍照功能
				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				intent.putExtra(MediaStore.EXTRA_OUTPUT,
						Uri.fromFile(new File(LQApplication.Imglocation)));
				startActivityForResult(intent, PHOTOHRAPH);
			} else if (v.getId() == R.id.feedback_xczpwjj_img) {
				// 调用系统的相册
				Intent intent = new Intent(Intent.ACTION_PICK, null);
				intent.setDataAndType(
						MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
				// 调用剪切功能
				startActivityForResult(intent, PHOTOZOOM);
			}

		}
	}

	/**
	 * 处理选中或拍照后的照片
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == NONE)
			return;
		// 拍照
		if (requestCode == PHOTOHRAPH) {
			startPhotoZoom(LQApplication.Imglocation, false);
		}
		if (data == null)
			return;
		// 读取相册图片
		if (requestCode == PHOTOZOOM) {
			Uri uri = data.getData();
			String imgPath = ImgFactory.getUriTurnFilePath(this, uri);// URI对象转换为文件路径
			startPhotoZoom(imgPath, true);
			// startPhotoZoom(data.getData());
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	/**
	 * 跳转到照片上传页面.
	 * 
	 * @param bitmap
	 */
	public void startPhotoZoom(String path, boolean isZoom) {
		File file = new File(path);
		long kbCount = 0;
		if(file.exists())
			kbCount = file.length() / 1024;
		else{
			showToast("图片不存在您是否删除?");
			return;
		}
		if (kbCount > 1024) {
			showToast("您上传的图片大小不能大于1M！");
			return;
		}
		if (isZoom) {
			int endCount = file.getName().lastIndexOf(".");
			String extendName = file.getName().substring(endCount,
					file.getName().length());
			String fileName = UUID.randomUUID().toString() + extendName;
			byte[] bytes = FileBeanMakeUp.getFileByteByPath(path);
			FileBeanMakeUp.saveFileByBytes(bytes, ConstantStore.IMG_LOCATION,
					fileName);
			path = ConstantStore.IMG_LOCATION + "/" + fileName;
		}
		Intent intent = new Intent(ActivityFeedbackDraftbox.this,
				ActivityImgUpload.class);
		intent.putExtra("lq", lq);
		intent.putExtra("imagePath", path);
		intent.putExtra("kbCount", kbCount);
		startActivity(intent);
	}

	@Override
	protected void onRestart() {
		service.loadListView(false);
		super.onRestart();
	}

	@Override
	public void onDestroy() {
		if (service.childFindImages != null)
			service.childFindImages.getLooper().quit();
		if (service.childUpdateDialogMessage != null)
			service.childUpdateDialogMessage.getLooper().quit();
		listView = null;
		lq = null;
		service = null;
		lqDao = null;
		qmv = null;
		// setContentView(R.layout.activity_null);
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	menu.add(Menu.NONE, Menu.FIRST + 11, 5, R.string.Menu_UpdatePro).setIcon(android.R.drawable.ic_popup_sync);
        return true;
    }
    @Override
   	public boolean onOptionsItemSelected(MenuItem item) {
       	switch (item.getItemId()) {
       	case Menu.FIRST + 11:
       		
       		loading = Loading.getLoading(ActivityFeedbackDraftbox.this, R.id.feedback_draftbox);
       		loading.openLoading();
       		
       		syncProThread=new Thread(){
       	    	public void run(){
       	    		if (!this.isInterrupted()){
       		    		SysBelongDao sbd = new SysBelongDao(ActivityFeedbackDraftbox.this);
       		       		sbd.UpdateProjectEnum();
       		       		Message message = syncHandler.obtainMessage();
       		       		syncHandler.sendMessage(message);
       		       		this.interrupt();
       	    		}
       	    	}
       	    };
       		syncProThread.start();
       		break;
       	}
   		return false;
       }

    public Thread syncProThread=null;
    
    public Handler syncHandler=new Handler(){
    	public void handleMessage(Message msg) {
    		showToast("更新成功!");
    		loading.closeLoading();
    		syncProThread.stop();
    	}
    };
}
