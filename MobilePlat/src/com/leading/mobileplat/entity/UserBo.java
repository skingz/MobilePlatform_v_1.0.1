package com.leading.mobileplat.entity;

import java.io.Serializable;

public class UserBo implements Serializable{

	private static final long serialVersionUID = 8035399495475527470L;

	private String UserId;
	
	private String UserName;
	
	private String DepNames;
	
	private String DepIds;

	private String XMPP_IP;
	
	private String XMPP_Port;
	
	private String error;
	
	
	public UserBo() {
		super();
	}

	public UserBo(String error) {
		super();
		this.error = error;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public String getUserId() {
		return UserId;
	}

	public void setUserId(String userId) {
		UserId = userId;
	}

	public String getUserName() {
		return UserName;
	}

	public void setUserName(String userName) {
		UserName = userName;
	}

	public String getDepNames() {
		return DepNames;
	}

	public void setDepNames(String depNames) {
		DepNames = depNames;
	}

	public String getDepIds() {
		return DepIds;
	}

	public void setDepIds(String depIds) {
		DepIds = depIds;
	}

	public String getXMPP_IP() {
		return XMPP_IP;
	}

	public void setXMPP_IP(String xMPP_IP) {
		XMPP_IP = xMPP_IP;
	}

	public String getXMPP_Port() {
		return XMPP_Port;
	}

	public void setXMPP_Port(String xMPP_Port) {
		XMPP_Port = xMPP_Port;
	}
	
	
}
