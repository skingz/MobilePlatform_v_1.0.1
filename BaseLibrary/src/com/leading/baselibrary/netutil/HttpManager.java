package com.leading.baselibrary.netutil;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.params.ConnPerRouteBean;
import org.apache.http.conn.params.ConnRouteParams;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;

import android.content.Context;
import android.net.Proxy;
import android.text.TextUtils;

/**
 * HTTP连接池管理类.
 * @author JianTao
 *
 */
public class HttpManager {
	/**
	 * 设置字节大小
	 */
	//private static final int DEFAULT_SOCKET_BUFFER_SIZE = 8192;

	/**
	 * 
	 * 最大连接数
	 */

	public final static int MAX_TOTAL_CONNECTIONS = 800;

	/**
	 * 
	 * 获取连接的最大等待时间
	 */

	public final static int WAIT_TIMEOUT = 60000;

	/**
	 * 
	 * 每个路由最大连接数
	 */

	public final static int MAX_ROUTE_CONNECTIONS = 400;

	/**
	 * 
	 * 连接超时时间
	 */

	public final static int CONNECT_TIMEOUT = 30000;

	/**
	 * 
	 * 读取超时时间
	 */

	public final static int READ_TIMEOUT = 30000;

	private static DefaultHttpClient sHttpClient;
	static {
		final HttpParams httpParams = new BasicHttpParams();
		httpParams.setParameter("charset", HTTP.UTF_8);
		// 设置获取连接的最大等待时间
		ConnManagerParams.setTimeout(httpParams, WAIT_TIMEOUT);
		// 设置每个路由最大连接数
		ConnManagerParams.setMaxConnectionsPerRoute(httpParams,
				new ConnPerRouteBean(MAX_ROUTE_CONNECTIONS));
		// 设置最大连接数
		ConnManagerParams.setMaxTotalConnections(httpParams,
				MAX_TOTAL_CONNECTIONS);
		// 设置读取超时时间
		HttpConnectionParams.setSoTimeout(httpParams, READ_TIMEOUT);
		// 设置连接超时时间
		HttpConnectionParams.setConnectionTimeout(httpParams, CONNECT_TIMEOUT);
		// 设置字节大小
		//HttpConnectionParams.setSocketBufferSize(httpParams,DEFAULT_SOCKET_BUFFER_SIZE);

		HttpConnectionParams.setTcpNoDelay(httpParams, true);
		// 设置HTTP版本
		HttpProtocolParams.setVersion(httpParams, HttpVersion.HTTP_1_1);
		// 设置HTTP获取的字符编码格式
		HttpProtocolParams.setContentCharset(httpParams, "UTF-8");
		HttpConnectionParams.setStaleCheckingEnabled(httpParams, false);

		// 是否重定向
		HttpClientParams.setRedirecting(httpParams, false);
		// 用户代理信息
		HttpProtocolParams.setUserAgent(httpParams, "Android client");

		/**
		 * 
		 ClientConnectionManager 使用 ThreadSafeClientConnManager 用来支持http
		 * 的Scheme，使用下面方法注册
		 */
		SchemeRegistry schemeRegistry = new SchemeRegistry();
		schemeRegistry.register(new Scheme("http", PlainSocketFactory
				.getSocketFactory(), 80));
		try {
			KeyStore trustStore = KeyStore.getInstance(KeyStore
					.getDefaultType());
			trustStore.load(null, null);
			SSLSocketFactory sf = new MySSLSocketFactory(trustStore);
			sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
			// 设置HTTPS
			schemeRegistry.register(new Scheme("https", sf, 443));
		} catch (Exception ex) {
			// do nothing, just keep not crash
		}

		ClientConnectionManager manager = new ThreadSafeClientConnManager(
				httpParams, schemeRegistry);
		// -----------------------------------------
		sHttpClient = new DefaultHttpClient(manager, httpParams);
	}

	private HttpManager() {
	}

	public static HttpResponse execute(HttpHead head) throws IOException {
		return sHttpClient.execute(head);
	}

	public static HttpResponse execute(HttpHost host, HttpGet get)
			throws IOException {
		return sHttpClient.execute(host, get);
	}

	public static HttpResponse execute(Context context, HttpUriRequest request)
			throws IOException {
		if (isWapNetwork()) {
			setWapProxy();
			return sHttpClient.execute(request);
		}

		final HttpHost host = (HttpHost) sHttpClient.getParams().getParameter(
				ConnRouteParams.DEFAULT_PROXY);
		if (host != null) {
			sHttpClient.getParams().removeParameter(
					ConnRouteParams.DEFAULT_PROXY);
		}

		return sHttpClient.execute(request);
	}

	private static boolean isWapNetwork() {
		final String proxyHost = android.net.Proxy.getDefaultHost();
		return !TextUtils.isEmpty(proxyHost);
	}

	private static void setWapProxy() {
		final HttpHost host = (HttpHost) sHttpClient.getParams().getParameter(
				ConnRouteParams.DEFAULT_PROXY);
		if (host == null) {
			final String host1 = Proxy.getDefaultHost();
			int port = Proxy.getDefaultPort();
			HttpHost httpHost = new HttpHost(host1, port);
			sHttpClient.getParams().setParameter(ConnRouteParams.DEFAULT_PROXY,
					httpHost);
		}
	}

	private static class MySSLSocketFactory extends SSLSocketFactory {
		SSLContext sslContext = SSLContext.getInstance("TLS");

		public MySSLSocketFactory(KeyStore truststore)
				throws NoSuchAlgorithmException, KeyManagementException,
				KeyStoreException, UnrecoverableKeyException {
			super(truststore);

			TrustManager tm = new X509TrustManager() {
				@Override
				public void checkClientTrusted(X509Certificate[] chain,
						String authType) throws CertificateException {
				}

				@Override
				public void checkServerTrusted(X509Certificate[] chain,
						String authType) throws CertificateException {
				}

				@Override
				public X509Certificate[] getAcceptedIssuers() {
					return null;
				}
			};

			sslContext.init(null, new TrustManager[] { tm }, null);
		}

		@Override
		public Socket createSocket(Socket socket, String host, int port,
				boolean autoClose) throws IOException, UnknownHostException {
			return sslContext.getSocketFactory().createSocket(socket, host,
					port, autoClose);
		}

		@Override
		public Socket createSocket() throws IOException {
			return sslContext.getSocketFactory().createSocket();
		}
	}
}
