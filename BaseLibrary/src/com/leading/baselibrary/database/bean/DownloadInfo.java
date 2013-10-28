package com.leading.baselibrary.database.bean;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName="DownloadInfo")
public class DownloadInfo {
	@DatabaseField(generatedId=true)
	private int id;
	@DatabaseField
	private int threadId;// 下载器id
	@DatabaseField
	private int startPos;// 开始点
	@DatabaseField
	private int endPos;// 结束点
	@DatabaseField
	private int compeleteSize;// 完成度
	@DatabaseField
	private String url;// 下载器网络标识
	
	
	public int getId(){return this.id;}
	public int getThreadId(){return this.threadId;}
	public int getStartPos(){return this.startPos;}
	public int getEndPos(){return this.endPos;}
	public int getCompeleteSize(){return this.compeleteSize;}
	public String getDownPath(){return this.url;}
	
	public void setDownPath(String url){this.url=url;}
	public void setThreadId(int threadId){this.threadId=threadId;}
	public void setStartPos(int startPos){this.startPos=startPos;}
	public void setEndPos(int endPos){this.endPos=endPos;}
	public void setCompeleteSize(int compSize){this.compeleteSize=compSize;}
	
	
}
