package com.leading.baselibrary.database.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName="MsgGroup")
public class MsgGroup implements Serializable{
	
	private static final long serialVersionUID = 6324132134410379107L;
	@DatabaseField(generatedId=true)
	private int groupId;
	@DatabaseField(canBeNull=false)
	private String groupName;
	@DatabaseField(canBeNull=false)
	private String sourceApkName;
	@ForeignCollectionField
	private ForeignCollection<MsgBean> msgCollection;
	
	private List<MsgBean> msgList=new ArrayList<MsgBean>();
	
	public MsgGroup(){}
	public int getGroupId(){return this.groupId;}
	public String getGroupName(){return this.groupName;}
	public void setGroupName(String groupName){this.groupName=groupName;}
	
	public String getSourceApkName(){return this.sourceApkName;}
	public void setSourceApkName(String sourceApkName){this.sourceApkName=sourceApkName;}
	
	public ForeignCollection<MsgBean> getMsgCollection(){return this.msgCollection;}
	public void setMsgCollection(ForeignCollection<MsgBean> msgCollection){this.msgCollection=msgCollection;}
	
	public List<MsgBean> getMsgList(){return this.msgList;}
	public void setMsgList(List<MsgBean> msgList){this.msgList=msgList;}
	public void addMsg(MsgBean msgBean){this.msgList.add(msgBean);}
}
