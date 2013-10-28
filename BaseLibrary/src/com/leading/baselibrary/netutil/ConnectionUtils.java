package com.leading.baselibrary.netutil;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.leading.baselibrary.ui.DialogAlertUtil;

/**
 * 判断网络是否可用,输入流转换等操作.
 * 
 * @author JianTao.tu
 * 
 */
public class ConnectionUtils {

	public static boolean isNet(Context context) {
		if (!isConnnected(context)) {
			DialogAlertUtil.showToast(context, "网络连接不可用！");
			return false;
		}
		int type = getConnectedType(context);
		if (type != -1) {
			switch (type) {
			case ConnectivityManager.TYPE_MOBILE:
				if (!isMobileConnected(context)) {
					DialogAlertUtil.showToast(context, "当前连接的移动网络不可用！");
					return false;
				}
				break;
			case ConnectivityManager.TYPE_WIFI:
				if (!isWifiConnected(context)) {
					DialogAlertUtil.showToast(context, "当前连接的WIFI网络不可用！");
					return false;
				}
				break;
			}
		} else {
			DialogAlertUtil.showToast(context, "所有网络均不可用！");
			return false;
		}
		return true;
	}

	/**
	 * 网络连接是否可用.
	 * 
	 * @author JianTao.tu
	 * 
	 * @param context
	 *            上下文
	 * @return (true[是]/false[否])
	 */
	public static boolean isConnnected(Context context) {
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (null != connectivityManager) {
			NetworkInfo networkInfo[] = connectivityManager.getAllNetworkInfo();

			if (null != networkInfo) {
				for (NetworkInfo info : networkInfo) {
					if (info.getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * 获取当前网络连接的类型信息
	 * 
	 * @param context
	 * @return
	 */
	public static int getConnectedType(Context context) {
		if (context != null) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mNetworkInfo = mConnectivityManager
					.getActiveNetworkInfo();
			if (mNetworkInfo != null && mNetworkInfo.isAvailable()) {
				return mNetworkInfo.getType();
			}
		}
		return -1;
	}

	/**
	 * 判断MOBILE网络是否可用
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isMobileConnected(Context context) {
		if (context != null) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mMobileNetworkInfo = mConnectivityManager
					.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
			if (mMobileNetworkInfo != null) {
				return mMobileNetworkInfo.isAvailable();
			}
		}
		return false;
	}

	/**
	 * 判断WIFI网络是否可用
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isWifiConnected(Context context) {
		if (context != null) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mWiFiNetworkInfo = mConnectivityManager
					.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
			if (mWiFiNetworkInfo != null) {
				return mWiFiNetworkInfo.isAvailable();
			}
		}
		return false;
	}

	/**
	 * 转换输入流为UTF-8格式.
	 * 
	 * @param instream
	 *            输入流对象
	 * @return 返回字符串
	 * @author JianTao.tu
	 * @throws IOException
	 */
	public static String stream2String(final InputStream instream)
			throws IOException {
		final StringBuilder sb = new StringBuilder();
		try {
			final BufferedReader reader = new BufferedReader(
					new InputStreamReader(instream, "UTF-8"));
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
		} finally {
			closeStream(instream);
		}
		return sb.toString();
	}

	/**
	 * 关闭流.
	 * 
	 * @param stream
	 *            流对象
	 * @author JianTao.tu
	 */
	public static void closeStream(Closeable stream) {
		if (stream != null) {
			try {
				stream.close();
			} catch (IOException e) {
				android.util.Log.e("IOUtils", "Could not close stream", e);
			}
		}
	}
}
