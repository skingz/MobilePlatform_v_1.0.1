package com.leading.localequestion.entity;

import java.io.Serializable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName="SysBelong")
public class SysBelong implements Serializable{

	private static final long serialVersionUID = 8759411077678838744L;

	@DatabaseField(useGetSet=true,generatedId=true)
	private Long keyId;
	
	@DatabaseField(useGetSet=true)
	private String fsiid;
	
	@DatabaseField
	private String name;


	public Long getKeyId() {
		return keyId;
	}

	public void setKeyId(Long keyId) {
		this.keyId = keyId;
	}

	public String getFsiid() {
		return fsiid;
	}

	public void setFsiid(String fsiid) {
		this.fsiid = fsiid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
}
