package com.leading.mobileplat.ui;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

import com.leading.mobileplat.R;

public class SettingImgShow extends Activity implements OnClickListener {

	private Bitmap bitmap;

	private Button btnUpload;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setting_photo_show);
		ImageView image = (ImageView) findViewById(R.id.setting_img_show);
		btnUpload = (Button) findViewById(R.id.setting_btn_upload);
		Intent intent = getIntent();
		Bundle extras = intent.getExtras();
		if (null != extras) {
			bitmap = (Bitmap) extras.get("data");
		} else {
			Uri uri = intent.getData();
			if (uri != null) {
				bitmap = BitmapFactory.decodeFile(uri.getPath());
			}
		}
		image.setImageBitmap(bitmap);
		bitmap.recycle();
		btnUpload.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.setting_btn_upload) {
			// 获得图片的宽高
			int width = bitmap.getWidth();
			int height = bitmap.getHeight();
			// 设置想要的大小
			int newWidth = 60;
			int newHeight = 60;
			// 计算缩放比例
			float scaleWidth = ((float) newWidth) / width;
			float scaleHeight = ((float) newHeight) / height;
			// 取得想要缩放的matrix参数
			Matrix matrix = new Matrix();
			matrix.postScale(scaleWidth, scaleHeight);
			// 得到新的图片
			Bitmap newbm = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix,
			true);
			 try {
				FileOutputStream outStream = this.openFileOutput("head.png", Context.MODE_WORLD_READABLE);
				newbm.compress(Bitmap.CompressFormat.PNG, 75, outStream);
				Intent intent=new Intent(SettingImgShow.this,WidgetSettingUser.class);
				startActivity(intent);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			 newbm.recycle();
		}

	}

}
