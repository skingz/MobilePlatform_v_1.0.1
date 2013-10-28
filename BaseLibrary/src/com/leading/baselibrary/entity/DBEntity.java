package com.leading.baselibrary.entity;

import java.io.Serializable;
import java.util.List;

/**
 * 数据库配置信息实体.
 * @author Jiantao.tu
 *
 */
public class DBEntity implements Serializable{
	
	private static final long serialVersionUID = -4739457039099683545L;
	
	private String dataBaseName; //数据库名称
	private int dataBaseVersion;//数据库版本
	private List<String> dataBaseTable;//数据库表名
	public String getDataBaseName() {
		return dataBaseName;
	}
	public void setDataBaseName(String dataBaseName) {
		this.dataBaseName = dataBaseName;
	}
	public int getDataBaseVersion() {
		return dataBaseVersion;
	}
	public void setDataBaseVersion(int dataBaseVersion) {
		this.dataBaseVersion = dataBaseVersion;
	}
	public List<String> getDataBaseTable() {
		return dataBaseTable;
	}
	public void setDataBaseTable(List<String> dataBaseTable) {
		this.dataBaseTable = dataBaseTable;
	}
	
}
