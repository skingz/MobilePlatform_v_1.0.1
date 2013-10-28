package com.leading.localequestion.data;

import android.os.Environment;

public class ConstantStore {
	
	public  final static int CWJ_HEAP_SIZE = 6* 1024* 1024 ; 
	
	/**
	 * 成功
	 */
	public final static String SUCCESS="1";//成功
	
	/**
	 * 失败
	 */
	public final static String ERROR="0";//失败
	
	/**
	 * 时间格式
	 */
	public final static String TIME_PATTERN="yyyy-MM-dd HH:mm:ss.SSS";//时间格式
	
	public final static String TIME_PATTERN_ONE="yyyy-MM-dd HH:mm:ss:SSS";//时间格式
	
	/**
	 * 图片储存路径
	 */
	public final static String IMG_LOCATION=Environment.getExternalStorageDirectory()+"/lzProject/img";
	
	/**
	 * 消息状态未读
	 */
	public final static int MSG_TYPE_UNREAD=0;//消息状态
	
	/**
	 * 消息状态已读
	 */
	public final static int MSG_TYPE_HAVEREAD=1;//消息状态已读
	
	/**
	 * 问题状态未读
	 */
	public final static int LQ_TYPE_UNREAD=-1;//問題状态未读
	
	/**
	 * 本应用名称.
	 */
	public final static String LQ_NAME="com.leading.localequestion";


	/**
	 * 问题状态待解决
	 */
	public final static int LQ_TYPE_BESOLVED=0;//问题状态待解决
	
	/**
	 * 问题状态暂不解决
	 */
	public final static int LQ_TYPE_NOTTOSOLVE=2;//问题状态暂不解决
	
	/**
	 * 问题状态已解决
	 */
	public final static int LQ_TYPE_HASBEENRESOLVED=1;//问题状态已解决
	
	/**
	 * 问题状态草稿
	 */
	public final static int LQ_TYPE_DRAFT=-3;//问题状态草稿
	
	
	public final static Integer getGrade(String grade){
		if("壹级".equals(grade))
			return 1;
		else if("贰级".equals(grade))
			return 2;
		else if("叁级".equals(grade))
			return 3;
		return null;
	}
	
	public final static String getGrade(int grade){
		if(grade==1)
			return "壹级";
		else if(grade==2)
			return "贰级";
		else if(grade==3)
			return "叁级";
		return null;
	}
}
