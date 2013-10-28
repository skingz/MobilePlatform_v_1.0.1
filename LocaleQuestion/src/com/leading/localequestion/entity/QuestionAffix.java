package com.leading.localequestion.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName="QuestionAffix")
public class QuestionAffix implements Serializable{
	
	private static final long serialVersionUID = -7702535809525634625L;

	@DatabaseField(useGetSet=true,generatedId=true)
	private Long keyid;
	
	@DatabaseField(useGetSet=true)
	private String fsiid=UUID.randomUUID().toString();
	
	@DatabaseField(foreign = true, foreignAutoRefresh = true,foreignAutoCreate=true) 
	private LocaleQuestion localeQuestion; // 略去get set
	
	@DatabaseField
	private String affixName;//附件说明
	
	@DatabaseField
	private String affixPath;//附件路径
	
	
	/**
	 * 图片大小.
	 */
	@DatabaseField
	private Integer affixSize;
	
	public String getFsiid() {
		return fsiid;
	}

	public void setFsiid(String fsiid) {
		this.fsiid = fsiid;
	}

	public void setKeyid(Long keyid) {
		this.keyid = keyid;
	}

	public LocaleQuestion getLocaleQuestion() {
		return localeQuestion;
	}

	public void setLocaleQuestion(LocaleQuestion localeQuestion) {
		this.localeQuestion = localeQuestion;
	}

	/**
	 * 创建时间.
	 */
	@DatabaseField(dataType=DataType.DATE)
	private Date createDate=new Date();

	
	public Long getKeyid() {
		return keyid;
	}

	public String getAffixName() {
		return affixName;
	}

	public void setAffixName(String affixName) {
		this.affixName = affixName;
	}

	public String getAffixPath() {
		return affixPath;
	}

	public void setAffixPath(String affixPath) {
		this.affixPath = affixPath;
	}

	public Integer getAffixSize() {
		return affixSize;
	}

	public void setAffixSize(Integer affixSize) {
		this.affixSize = affixSize;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

}
