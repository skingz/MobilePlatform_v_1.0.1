package com.leading.baselibrary.database.bean;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/*
 * 基本消息类
 * @author skingz 
 */
@DatabaseTable(tableName="MsgBean")
public class MsgBean {
	public static final String MSG_GROUP_ID_FIELDNAME="group_Id";
	@DatabaseField(generatedId=true)
	private int msgId;
	@DatabaseField
	private String msgTitle;
	@DatabaseField
	private String msgDetail;
	@DatabaseField
	private String msgPubTime;
	@DatabaseField
	private Boolean readState;
	@DatabaseField
	private String businessInstanceId;
	@DatabaseField
	private String redrectUri;
	@DatabaseField(foreign=true,foreignAutoRefresh=true,foreignAutoCreate=true,columnName=MSG_GROUP_ID_FIELDNAME)
	private MsgGroup msgGroup;
	@DatabaseField
	private String userId;
	
	public MsgBean(){}
	public int getMsgId(){return this.msgId;}
	public MsgGroup getMsgGroup(){return this.msgGroup;} 
	public void setMsgGroup(MsgGroup msgGroup){this.msgGroup=msgGroup;}
	
	public String getMsgTitle(){return this.msgTitle;}
	public void setMsgTitle(String childTitle){this.msgTitle=childTitle;}
	
	public String getMsgDetail(){return this.msgDetail; }
	public void setMsgDetail(String childDetail){this.msgDetail=childDetail;}
	
	public String getMsgPubTime(){return this.msgPubTime;}
	public void setMsgPubTime(String msgPubTime){this.msgPubTime=msgPubTime;}
	
	public Boolean getReadState(){return this.readState;}
	public void setReadState(Boolean readState){this.readState=readState;}
	
	public String getBusinessInstanceId(){return this.businessInstanceId;}
	public void setBusinessInstanceid(String businessInstanceId){this.businessInstanceId=businessInstanceId;}
	
	public String getRedrectUri(){return this.redrectUri;}
	public void setRedrectUri(String redrectUri){this.redrectUri=redrectUri;}
	
	public String getUserId(){return this.userId;}
	public void setUserId(String userId){this.userId=userId;}
}
