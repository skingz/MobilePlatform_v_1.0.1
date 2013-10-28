package com.leading.baselibrary.netutil;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.leading.baselibrary.util.FileBeanMakeUp;
import com.leading.baselibrary.util.StringUtils;

/**
 * 通过HTTP (get/post)访问Server服务器.
 * 
 * @author JianTao.tu
 * 
 */
public class NetUtil {

	private static Context context;

	/**
	 * 网络可用状态下，通过get方式向server端发送请求，并返回响应数据.
	 * 
	 * @param strUrl
	 *            请求地址
	 * @param context
	 *            上下文
	 * @return 响应的JSON数据
	 */
	public static String getResponseForGet(String strUrl, Context context) {
		NetUtil.context = context;
		HttpGet httpRequest = new HttpGet(strUrl);
		return getRespose(httpRequest);
	}

	/**
	 * 网络可用状态下，通过post方式向server端发送请求，并返回响应数据.
	 * 
	 * @param market_uri
	 *            请求网址
	 * @param nameValuePairs
	 *            请求参数
	 * @param context
	 *            上下文
	 * @return 响应的JSON数据
	 */
	public static String getResponseForPost(String market_uri,
			List<NameValuePair> nameValuePairs, Context context) {
		if (null == market_uri || "" == market_uri) {
			return null;
		}
		NetUtil.context = context;
		HttpPost request = new HttpPost(market_uri);
		try {
			if (nameValuePairs != null)
				request.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			return getRespose(request);
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		return null;
	}

	/**
	 * 网络可用状态下，通过post方式向server端发送请求，并返回响应数据.
	 * 
	 * @param market_uri
	 *            请求网址
	 * @param JSON字符串
	 *            请求参数
	 * @param context
	 *            上下文
	 * @return 响应的JSON数据
	 */
	public static String getResponseForPost(String market_uri, String str,
			Context context) {
		if (null == market_uri || "" == market_uri) {
			return null;
		}
		NetUtil.context = context;
		HttpPost request = new HttpPost(market_uri);
		if (str != null) {
			try {
				StringEntity entity = new StringEntity(str, HTTP.UTF_8);
				request.setEntity(entity);
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		request.addHeader("Accept", "application/json");
		request.addHeader("Content-Type", "application/json");
		request.addHeader("charset", HTTP.UTF_8);
		return getRespose(request);

	}

	/**
	 * 响应客户端请求.
	 * 
	 * @param request
	 *            客户端请求get/post
	 * @return 响应数据
	 */
	public static String getRespose(HttpUriRequest request) {
		try {
			if (!ConnectionUtils.isNet(context))
				return null;
			HttpResponse httpResponse = HttpManager.execute(context, request);
			int statusCode = httpResponse.getStatusLine().getStatusCode();
			Log.v("HttpUrl", request.getURI().toString());
			Log.v("HttpResponseCode", "" + statusCode);
			if (HttpStatus.SC_OK == statusCode) {
				String result=EntityUtils.toString(httpResponse.getEntity());
				if(StringUtils.isNotNull(result))
					return result;
				else
					return "null";
			} else{
				Toast.makeText(NetUtil.context,"服务器异常请与技术人员联系！",Toast.LENGTH_SHORT).show();
			}
		} catch (ClientProtocolException e) {
//			e.printStackTrace();
		} catch (IOException e) {
//			e.printStackTrace();
			Toast.makeText(NetUtil.context,"服务器地址解析错误！",Toast.LENGTH_SHORT).show();
		}
		return null;
	}

	/**
	 * 文件名 +文件扩展名+文件流 byte 上传
	 * 
	 * @param market_uri
	 * @param filePath
	 * @param context
	 * @return
	 */
	public static String getResposeForPostStream(String market_uri,
			String filePath, Context context) {
		if (null == market_uri || "" == market_uri)
			return null;
		File file = new File(filePath);
		if (!file.exists()) {
			Log.v("getResposeForPostStream", "文件不存在:" + filePath);
			return null;
		}
		NetUtil.context = context;
		byte[] updata = FileBeanMakeUp.getFileBytePacket(filePath); // 参数与文件封装成单个数据包
		HttpPost httpPost = new HttpPost(market_uri);
		// 单个文件流上传
		InputStream input = new ByteArrayInputStream(updata);
		InputStreamEntity reqEntity;
		reqEntity = new InputStreamEntity(input, -1);
		reqEntity.setContentType("binary/octet-stream");
		reqEntity.setChunked(true);
		httpPost.setEntity(reqEntity);
		return getRespose(httpPost);
	}

	/**
	 * 下载文件 用get方法(参数拼到Url上)
	 * 
	 * @param market_uri
	 * @param str
	 * @param context
	 * @return
	 */
	public static byte[] getStreamForGet(String market_uri, Context context) {
		if(!ConnectionUtils.isNet(context))
			return null;
		if (null == market_uri || "" == market_uri) {
			return null;
		}
		NetUtil.context = context;
		HttpGet request = new HttpGet(market_uri);

		try {
			HttpResponse httpResponse = HttpManager.execute(context, request);

			int statusCode = httpResponse.getStatusLine().getStatusCode();
			Log.v("HttpUrl", request.getURI().toString());
			Log.v("HttpResponseCode", "" + statusCode);
			if (HttpStatus.SC_OK == statusCode) {
				return EntityUtils.toByteArray(httpResponse.getEntity());
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;

	}
}
