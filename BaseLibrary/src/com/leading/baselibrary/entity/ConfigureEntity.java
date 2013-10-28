package com.leading.baselibrary.entity;

import java.io.Serializable;
import java.util.Map;

/**
 * 用户配置信息实体.
 * @author Jiantao.tu
 *
 */
public class ConfigureEntity implements Serializable{
	
	private static final long serialVersionUID = -2157799297591174298L;

	/**
	 * 服务器IP.
	 */
	private String serverAddress;
	
	/**
	 * 消息条数.
	 */
	private Integer messagNumber;
	
	/**
	 * 消息显示天数.
	 */
	private Integer messageDaysNumber;
	
	/**
	 * 用户ID。
	 */
	private String userId;
	
	/**
	 * 用户名.
	 */
	private String username;
	
	/**
	 * 用户密码.
	 */
	private String password;
	
	/**
	 * 用户真实姓名.
	 */
	private String fullName;
	
	/**
	 * 用户版本键值对.
	 */
	private Map<String,String>  versionMaps;
	
	/**
	 * 是否自动登录.
	 */
	private boolean autoLogin=false;
	
	/**
	 * 是否记住密码.
	 */
	private boolean memoryPassword=false;
	
	
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getServerAddress() {
		return serverAddress;
	}

	public void setServerAddress(String serverAddress) {
		this.serverAddress = serverAddress;
	}

	public Integer getMessagNumber() {
		return messagNumber;
	}

	public void setMessagNumber(Integer messagNumber) {
		this.messagNumber = messagNumber;
	}

	public Integer getMessageDaysNumber() {
		return messageDaysNumber;
	}

	public void setMessageDaysNumber(Integer messageDaysNumber) {
		this.messageDaysNumber = messageDaysNumber;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public Map<String, String> getVersionMaps() {
		return versionMaps;
	}

	public void setVersionMaps(Map<String, String> versionMaps) {
		this.versionMaps = versionMaps;
	}
	public boolean getAutoLogin() {
		return autoLogin;
	}

	public boolean getMemoryPassword() {
		return memoryPassword;
	}

	public void setAutoLogin(boolean autoLogin) {
		this.autoLogin = autoLogin;
	}

	public void setMemoryPassword(boolean memoryPassword) {
		this.memoryPassword = memoryPassword;
	}
	
}
