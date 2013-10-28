package com.leading.baselibrary.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class FileBeanMakeUp {

	public static byte[] getFileBytePacket(String filePath) {
		File file = new File(filePath);
		// 封装参数信息
		JSONObject jsonObject = new JSONObject();
		try {
			int pSite = file.getName().lastIndexOf(".");

			jsonObject.put("KeyID", file.getName().substring(0, pSite));
			jsonObject.put("ExtendName", file.getName().substring(pSite));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		byte[] bytes = getFileByteByPath(filePath);
		byte[] updata = addJsonToFile(jsonObject.toString(), bytes); // 参数与文件封装成单个数据包
		return updata;
	}

	public static Object[] saveFileByByte(String filePath, byte[] packetData) {
		boolean isSaved = true;
		int len = (int) packetData[0];
		byte[] strByte = new byte[len];
		byte[] fileByte = new byte[packetData.length - len - 1];
		System.arraycopy(packetData, 1, strByte, 0, len);
		System.arraycopy(packetData, 1 + len, fileByte, 0, fileByte.length);
		String strFileInfo = new String(strByte);
		Log.v("收到文件", strFileInfo + " 共:" + fileByte.length);
		String fileName = "";
		try {
			JSONObject obj = new JSONObject(strFileInfo);
			fileName = obj.getString("KeyID") + obj.getString("ExtendName");
		} catch (JSONException e) {
			isSaved = false;
			e.printStackTrace();
		}
		if (!fileName.equals(""))
			saveFileByBytes(fileByte, filePath, fileName);
		else
			isSaved = false;
		Object[] array = new Object[2];
		if (!filePath.endsWith("/"))
			filePath = filePath + "/";
		array[0] = isSaved;
		array[1] = filePath + fileName;
		return array;
	}

	public static void saveFileByBytes(byte[] fileBytes, String fileDirectory,
			String fileName) {
		RandomAccessFile accessFile = null;
		File file = null;
		try {
			File dir = new File(fileDirectory);
			if (!dir.exists() && dir.isDirectory())// 判断文件目录是否存在
				dir.mkdirs();
			if (!fileDirectory.endsWith("/"))
				fileDirectory = fileDirectory + "/";
			file = new File(fileDirectory + fileName);
			accessFile = new RandomAccessFile(file, "rw");
			accessFile.write(fileBytes);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (accessFile != null) {
				try {
					accessFile.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 将文件转成byte数组
	 * 
	 * @param filePath
	 * @return
	 */
	public static byte[] getFileByteByPath(String filePath) {
		byte[] bytes = null;
		InputStream is;
		File myfile = new File(filePath);
		try {
			is = new FileInputStream(filePath);
			bytes = new byte[(int) myfile.length()];
			int len = 0;
			int curLen = 0;
			while ((len = is.read(bytes)) != -1) {
				curLen += len;
				is.read(bytes);
			}
			is.close();
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return bytes;
	}

	/**
	 * 将字符串与 byte数组 统一包装成 byte数组（该数组第一位存储的是 字符串在数组中的长度）
	 * 
	 * @param json
	 * @param image
	 * @return
	 */
	static byte[] addJsonToFile(String json, byte[] image) {
		byte[] jsonb = json.getBytes();
		int length = image.length + jsonb.length;
		System.out.println(image.length + "    " + jsonb.length);
		byte[] bytes = new byte[length + 1];
		byte[] lengthb = InttoByteArray(jsonb.length, 1);
		System.arraycopy(lengthb, 0, bytes, 0, 1);
		System.arraycopy(jsonb, 0, bytes, 1, jsonb.length);
		System.arraycopy(image, 0, bytes, 1 + jsonb.length, image.length);
		return bytes;
	}

	static byte[] InttoByteArray(int iSource, int iArrayLen) {
		byte[] bLocalArr = new byte[iArrayLen];
		for (int i = 0; (i < 4) && (i < iArrayLen); i++) {
			bLocalArr[i] = (byte) (iSource >> 8 * i & 0xFF);
		}
		return bLocalArr;
	}

	static int BytestoInt(byte[] bRefArr) {
		int iOutcome = 0;
		byte bLoop;
		for (int i = 0; i < bRefArr.length; i++) {
			bLoop = bRefArr[i];
			iOutcome += (bLoop & 0xFF) << (8 * i);
		}
		return iOutcome;
	}

}
