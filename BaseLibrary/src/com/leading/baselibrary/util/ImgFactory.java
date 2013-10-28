package com.leading.baselibrary.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;

import android.app.Activity;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;

/**
 * 图片工具类.
 * @author tjt
 *
 */
public class ImgFactory {
	
	/**
	 * Bitmap转换为InputStream.
	 * @param bm Bitmap
	 * @return
	 */
	public static InputStream getBitmapTurnInputStream(Bitmap bm) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
		InputStream sbs = new ByteArrayInputStream(baos.toByteArray());
		try {
			sbs.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return sbs;
	}
	
	
	public static String getUriTurnFilePath(Activity activity,Uri uri){
		String[] proj = { MediaStore.Images.Media.DATA };
		Cursor actualimagecursor = activity.managedQuery(uri, proj, null, null,
				null);
		int actual_image_column_index = actualimagecursor
				.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		actualimagecursor.moveToFirst();
		return actualimagecursor
				.getString(actual_image_column_index);
	}
	
	/**
	 * 把Bitmap写入到指定的文件路径上。
	 * @param bitmap 图片
	 * @param path 文件路径
	 * @return
	 */
	public static String storeBitMapToFile(Bitmap bitmap,String path) {
		if (bitmap == null) {
			return null;
		}
		int count = 15; // the number which will prevent the create segment
						// locked.
		File file = null;
		RandomAccessFile accessFile = null;
		do {
			file = new File(path);
			count--;
		} while (!file.exists() && count > 0);
		ByteArrayOutputStream steam = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.JPEG, 100, steam);
		byte[] buffer = steam.toByteArray();
		try {
			accessFile = new RandomAccessFile(file, "rw");
			accessFile.write(buffer);
		} catch (Exception e) {
			return null;
		}
		try {
			steam.close();
			accessFile.close();
			bitmap.recycle();
		} catch (IOException e) {
			// Note: do nothing.
		}
		return path;
	}
	/**
	 * 获取bitmap尺寸缩放解决OOM问题.
	 * 
	 * @throws IOException
	 */
	public static Bitmap convertBitmap(File file) {
		Bitmap bitmap = null;
		BitmapFactory.Options o = new BitmapFactory.Options();
		o.inJustDecodeBounds = true;
		try {
			FileInputStream fis = new FileInputStream(file.getAbsolutePath());
			BitmapFactory.decodeStream(fis, null, o);
			fis.close();
			final int REQUIRED_SIZE = 70;
			int width_tmp = o.outWidth, height_tmp = o.outHeight;
			int scale = 1;
			while (true) {
				if (width_tmp / 2 < REQUIRED_SIZE
						|| height_tmp / 2 < REQUIRED_SIZE)
					break;
				width_tmp /= 2;
				height_tmp /= 2;
				scale *= 2;
			}
			BitmapFactory.Options op = new BitmapFactory.Options();
			op.inSampleSize = scale;
			fis = new FileInputStream(file.getAbsolutePath());
			bitmap = BitmapFactory.decodeStream(fis, null, op);
			fis.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return bitmap;
	}
	/**
	 * 按比例缩放图片，防止大图片 内存溢出
	 * @param filePath
	 * @param bmpId
	 * @param reqWidth
	 * @param reqHeight
	 * @return
	 */
	 public static Bitmap decodeBitmapFromFile(String filePath,int reqWidth, int reqHeight) {
	        // First decode with inJustDecodeBounds=true to check dimensions
	        final BitmapFactory.Options options = new BitmapFactory.Options();
	        options.inJustDecodeBounds = true;//设置这个，只得到能解析的出来图片的大小，并不进行真正的解析
	        BitmapFactory.decodeFile(filePath, options);
	        // Calculate inSampleSize
	        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight); //跟指定大小比较，计算缩放比例
	        // Decode bitmap with inSampleSize set
	        options.inJustDecodeBounds = false; 
	        return BitmapFactory.decodeFile(filePath, options);
	    }
	 /**
	  * 缩放
	  * @param options
	  * @param reqWidth
	  * @param reqHeight
	  * @return
	  */
	 private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
		    // Raw height and width of image
		    final int height = options.outHeight;
		    final int width = options.outWidth;
		    int inSampleSize = 1;

		    if (height > reqHeight || width > reqWidth) {
		        if (width > height) {
		            inSampleSize = Math.round((float)height / (float)reqHeight);
		        } else {
		            inSampleSize = Math.round((float)width / (float)reqWidth);
		        }
		    }
		    return inSampleSize;
		    }

}
