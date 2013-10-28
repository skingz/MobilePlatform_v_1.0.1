package com.leading.baselibrary.database.bean;

import java.util.Date;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName="CacheRecord")
public class CacheRecord {
	@DatabaseField(generatedId=true)
	private int Id;
	@DatabaseField
	private String cacheName;
	@DatabaseField
	private Date updateTime;
	
	public int getId(){return this.Id;}
	public String getCacheName(){return this.cacheName;}
	public Date getUpdateTime(){return this.updateTime;}
	
	public void setCacheName(String cacheName){this.cacheName=cacheName;}
	public void setUpdateTimeNow(){this.updateTime=new Date();}
}
