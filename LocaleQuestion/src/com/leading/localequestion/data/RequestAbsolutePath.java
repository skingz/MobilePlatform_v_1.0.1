package com.leading.localequestion.data;

import com.leading.localequestion.global.LQApplication;

public class RequestAbsolutePath {
	
	private String beginPath=ServerMap.PROTOCOL+LQApplication.getConfig().getServerAddress();
	
	/**
	 * 提交问题.
	 * @return
	 */
	public String getQsSubmitPath(){
		return beginPath+ServerMap.QS_SUBMIT;
	}
	
	/**
	 * 上传附件文件流.
	 * @return
	 */
	public String getQsAffixUploadPath(){
		return beginPath+ServerMap.QS_AFFIX_UPLOAD;
	}
	
	/**
	 * 获取问题信息.
	 * @return
	 */
	public String getQsGetInfoPath(String questionId){
		return beginPath+ServerMap.QS_GETINFO.replace("{questionId}", questionId);
	}
	
	/**
	 * 获取附件.
	 * @param keyId
	 * @return
	 */
	public String getAffixDownLoad(String keyId){
		return beginPath+ServerMap.QS_AFFIX_DOWNLOAD.replace("{qAffixId}", keyId);
	}
	
	public String getQsSendNewMsg(String qsId,String sourceUserID){
		return beginPath+ServerMap.QS_SEND_NEWMSG.replace("{QsId}", qsId).replace("{SourceUserID}", sourceUserID);
	}
	public String getProjects(String UserId){
		return beginPath+ServerMap.QS_GETPROJECTS.replace("{UserID}", UserId);
	}
	
	public String getQsResolvedPath(){
		return beginPath+ServerMap.QS_RESOLVED;
	}
	
	public String getQsTempNoResolvedPath(){
		return beginPath+ServerMap.QS_TEMPNO_RESOLVED;
	}
}
