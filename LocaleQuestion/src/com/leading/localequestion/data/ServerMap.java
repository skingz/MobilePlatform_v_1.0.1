package com.leading.localequestion.data;

/**
 * 接口方法功能地址.
 * @author tjt
 *
 */
public class ServerMap {
	
	/**
	 * 协议.
	 */
	public final static String PROTOCOL="http://";
	
	/**
	 * 提交问题POST.
	 */
	public final static String  QS_SUBMIT="/Service.svc/CommonRout/QsSubmit";
	
	/**
	 * 上传附件文件流POST.
	 */
	public final static String QS_AFFIX_UPLOAD="/Service.svc/StreamRout/QsAffixUpload";
	
	
	/**
	 * 获取问题信息POST.
	 */
	public final static String QS_GETINFO="/Service.svc/CommonRout/QsGetInfo/{questionId}";
	/**
	 * 获取问题信息POST.
	 */
	public final static String QS_GETPROJECTS="/Service.svc/CommonRout/QsGetProject/{UserID}";
	
	/**
	 * 下载附件get.
	 */
	public final static String QS_AFFIX_DOWNLOAD="/Service.svc/StreamRout/QsAffixDownload/{qAffixId}";
	
	/**
	 * 上传问题信息后进行分发数据
	 */
	public final static String QS_SEND_NEWMSG="/Service.svc/CommonRout/QsSendMsg/{QsId}/{SourceUserID}";
	
	/**
	 * 提交以解决问题.
	 */
	public final static String QS_RESOLVED="/Service.svc/CommonRout/QsResolved";
	
	/**
	 * 问题置成暂不解决状态。
	 */
	public final static String QS_TEMPNO_RESOLVED="/Service.svc/CommonRout/QsTempNoResolved";
}
