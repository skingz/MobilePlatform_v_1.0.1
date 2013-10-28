package com.leading.mobileplat.mutual;

/**
 * function 所有与服务器交互的Uri地址片段，方便拼IP
 * @author sjz
 *
 */
public class ServiceMap {
	
	/**
	 * 协议.
	 */
	public final static String PROTOCOL="http://";
	/**
	 * 登陆地址.
	 */
	public final static String Segment_LoginUri="/Service.svc/CommonRout/Login";
	/**
	 * 反馈地址.
	 */
	public final static String Segment_FeedbackUri="/Service.svc/CommonRout/FeedBack";
	/**
	 * 修改密码地址.
	 */
	public final static String Segment_ModifyPWDUri="/Service.svc/CommonRout/ModifyPWD";
	/**
	 * 上传图片地址.
	 */
	public final static String Segment_UploadImageUri="/Service.svc/StreamRout/UploadImage";
	/**
	 * 得到APP列表的地址
	 */
	public final static String Segment_GetAppList="/Service.svc/CommonRout/GetAppList";
	
	//test
	public final static String Segment_UploadAffix="/Service.svc/StreamRout/QsAffixUpload";
	public final static String Segment_DownloadAffic="/Service.svc/StreamRout/QsAffixDownload";
}
