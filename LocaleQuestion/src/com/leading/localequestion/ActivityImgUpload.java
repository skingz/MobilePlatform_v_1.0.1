package com.leading.localequestion;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.leading.baselibrary.ui.ActivityCommon;
import com.leading.baselibrary.ui.Loading;
import com.leading.baselibrary.util.StringUtils;
import com.leading.localequestion.dao.QuestionAffixDao;
import com.leading.localequestion.entity.LocaleQuestion;
import com.leading.localequestion.entity.QuestionAffix;

@SuppressLint("HandlerLeak")
public class ActivityImgUpload extends ActivityCommon implements OnClickListener {
	private ImageView image;
	private Bitmap bitmap;
	private Button btnUpload;
	private Button btnWaste;
	private Loading loading;
	private ChildHandler childHandler = null;
	private QuestionAffixDao qaDao;
	private String imagePath;
	private EditText editText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_photo_upload);
		image = (ImageView) findViewById(R.id.setting_img_show);
		btnUpload = (Button) findViewById(R.id.setting_btn_upload);
		btnWaste = (Button) findViewById(R.id.setting_btn_waste);
		editText = (EditText) findViewById(R.id.edt_feedback_contact);
		
		btnUpload.setOnClickListener(this);
		btnWaste.setOnClickListener(this);
		
		initPage();
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			result();
		}
		return false;
	}

	@Override
	protected void initPage(){
		Intent intent = getIntent();
		imagePath = intent.getExtras().getString("imagePath");
		try {
			InputStream input = new FileInputStream(imagePath);
			bitmap = BitmapFactory.decodeStream(input);
			if(input!=null)input.close();
			image.setImageBitmap(bitmap);
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally{
			//bitmap.recycle();
		}

	}
	public void result(){
		File file=new File(imagePath);
		if(file.exists())
			file.delete();
		finish();
	}
	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.setting_btn_upload) {
			bitmap.recycle();
			loading = Loading.getLoading(this, R.id.photo_upload);
			loading.openLoading();
			HandlerThread childThread = new HandlerThread("child_thread");
			childThread.start();
			childHandler = new ChildHandler(childThread.getLooper());
			Message message = childHandler.obtainMessage();
			message.sendToTarget();
		}
		if (v.getId() == R.id.setting_btn_waste) {
			result();
		}
	}

	private Handler mainHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			loading.closeLoading();
			ActivityImgUpload.this.showToast("保存成功!");
			finish();
			super.handleMessage(msg);
		}
	};

	/**
	 * 子线程处理写入sd卡并进行数据库储存.
	 * 
	 * @author Jiantao.tu
	 * 
	 */
	class ChildHandler extends Handler {
		public ChildHandler(Looper looper) {
			super(looper);
		}

		public void handleMessage(Message msg) {
			Long kbCount=getIntent().getLongExtra("kbCount", 0);
			QuestionAffix qa = new QuestionAffix();
			File file = new File(imagePath);
			if(StringUtils.isNotNull(file.getName())){
				qa.setFsiid(file.getName().split("\\.")[0]);
			}
			String content = editText.getText().toString();
			qa.setAffixName(content);
			qa.setAffixPath(imagePath);
			if(kbCount!=null && kbCount>0L)
				qa.setAffixSize(kbCount.intValue());
			qa.setLocaleQuestion((LocaleQuestion) getIntent()
					.getSerializableExtra("lq"));
			qaDao = new QuestionAffixDao(ActivityImgUpload.this);
			qaDao.create(qa);
			Message message = mainHandler.obtainMessage();
			message.sendToTarget();
		}
	}

	@Override
	public void onDestroy() {
//		setContentView(R.layout.activity_null);
		super.onDestroy();
	}
	

}
