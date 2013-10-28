package com.leading.localequestion.entity;

import java.io.Serializable;
import java.util.UUID;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName="QsClass")
public class QsClass implements Serializable{

	private static final long serialVersionUID = 5845468925388124440L;

	@DatabaseField(useGetSet=true)
	private Long keyId;
	
	@DatabaseField(useGetSet=true)
	private String fsiid=UUID.randomUUID().toString();
	
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
