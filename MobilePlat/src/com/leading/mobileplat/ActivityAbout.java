package com.leading.mobileplat;

import java.util.Hashtable;
import java.util.List;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.leading.baselibrary.database.bean.CacheServerAppBean;
import com.leading.baselibrary.ui.ActivityTemplat;
import com.leading.baselibrary.ui.TitleBarLayout;
import com.leading.baselibrary.util.StringUtils;
import com.leading.mobileplat.mutual.ServiceHelper;

public class ActivityAbout extends ActivityTemplat {
	public static final Long activityId=0620L;
	private static final int QR_WIDTH=250;
	private static final int QR_HEIGHT=250;
	private ImageView imgView_QR;
	private String strAppUri;
	private TextView strAppDescriable;
	private TextView strDownMsg;
	private ProgressBar bp;
	private CacheServerAppBean thisApp;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);
		TitleBarLayout tbl=(TitleBarLayout)this.findViewById(R.id.about_tbl);
		tbl.getLeftBtn().setVisibility(View.VISIBLE);
		tbl.getLeftBtn().setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				ActivityAbout.this.finish();			
			}
		});
		imgView_QR=(ImageView)this.findViewById(R.id.imgView_QR);
		strAppDescriable=(TextView)this.findViewById(R.id.tv_thisAppDescriable);
		strDownMsg=(TextView)this.findViewById(R.id.tv_downUriMsg);
		bp=(ProgressBar)this.findViewById(R.id.pgBar_Loading);
		initPage();
	}
	@Override
	protected void initPage(){
		getAppThread.start();
	}
	private void updateUI(){
		if(StringUtils.isNotNull(strAppUri)){
			TextView tv_msg=(TextView)this.findViewById(R.id.tv_downUrititle);
			tv_msg.setText("下载连接:");
			strDownMsg.setText(Html.fromHtml("<a href='"+strAppUri+"'>"+strAppUri+"</a>"));
			strDownMsg.setMovementMethod(LinkMovementMethod.getInstance());
			createImage();

			strAppDescriable.setText(thisApp.getDescrible());
			strDownMsg.setVisibility(View.VISIBLE);
			bp.setVisibility(View.GONE);
		} else {
	    	 strDownMsg.setTextColor(Color.RED);
	    	 strDownMsg.setText("下载地址获取错误!");
	     }
	}
	Handler pBarHandler = new Handler(){   
		@Override  
		public void handleMessage(Message msg) {
			if(msg.arg1==1){
				updateUI();
			}
			super.handleMessage(msg);
		}   
	    };   
	private Thread getAppThread=new Thread(){
		public void run(){
			getDate();
			Message msg = pBarHandler.obtainMessage();
			msg.arg1=1;
			pBarHandler.sendMessage(msg);
		}
	};
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {return true;}
	
	private void getDate(){
		ServiceHelper sh=new ServiceHelper(ActivityAbout.this);
		 List<CacheServerAppBean> serverApp=sh.getAppList(null);
		 for(CacheServerAppBean capp:serverApp){
			 if(capp.getAppName_EN().equals(ActivityAbout.this.getPackageName())){
				 thisApp=capp;
				 strAppUri=capp.getStrUrl();
			 }
		 }
	}
	// 生成QR图
	private void createImage() {
		
			try {
	            // 需要引入core包
				QRCodeWriter writer = new QRCodeWriter();
	            // 把输入的文本转为二维码
	            BitMatrix martix = writer.encode(strAppUri, BarcodeFormat.QR_CODE,QR_WIDTH, QR_HEIGHT);

	            System.out.println("w:" + martix.getWidth() + "h:"+ martix.getHeight());

	            Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();
	            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
	            BitMatrix bitMatrix = new QRCodeWriter().encode(strAppUri,BarcodeFormat.QR_CODE, QR_WIDTH, QR_HEIGHT, hints);
	            int[] pixels = new int[QR_WIDTH * QR_HEIGHT];
	            for (int y = 0; y < QR_HEIGHT; y++) {
	                for (int x = 0; x < QR_WIDTH; x++) {
	                    if (bitMatrix.get(x, y)) {
	                        pixels[y * QR_WIDTH + x] = 0xff000000;
	                    } else {
	                        pixels[y * QR_WIDTH + x] = 0xffffffff;
	                    }

	                }
	            }

	            Bitmap bitmap = Bitmap.createBitmap(QR_WIDTH, QR_HEIGHT,Bitmap.Config.ARGB_8888);

	            bitmap.setPixels(pixels, 0, QR_WIDTH, 0, 0, QR_WIDTH, QR_HEIGHT);
	            imgView_QR.setImageBitmap(bitmap);

	        } catch (WriterException e) {
	            e.printStackTrace();
	        }
	    
	 }
}
