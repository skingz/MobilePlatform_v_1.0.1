package com.leading.localequestion.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * 客户端问题表.
 * @author tjt
 *
 */
@DatabaseTable(tableName="LocaleQuestion")
public class LocaleQuestion implements Serializable{

	private static final long serialVersionUID = -8837534630417090656L;

	@DatabaseField(useGetSet=true,generatedId=true)
	private Long keyId;
	
	@DatabaseField(useGetSet=true)
	private String fsiid=UUID.randomUUID().toString();
	
	@DatabaseField
	private String userId;
	
	/**
	 * 标题
	 */
	@DatabaseField
	private String title;
	
	@DatabaseField
	private String sysBelong;//所属系统
	
	@DatabaseField
	private String sysBelongId;//所属系统Id
	
	@DatabaseField
	private Date happenTime;//发生时间
	
	@DatabaseField
	private String qsClass;//问题分类
	
	@DatabaseField
	private String qsClassId;//问题分类Id
	
	@DatabaseField
	private Integer qsGrade;//1(壹级)2(贰级)3(叁级) 问题等级
	
	@DatabaseField
	private String qsDiscrable;//问题描述
	
	@DatabaseField
	private Date needFinishTime;//要求完成时间
	
	@DatabaseField
	private Integer qsState;//解决状态
	
	@DatabaseField
	private Boolean ifSolvedRoot;//是否彻底解决
	
	@DatabaseField
	private String resolvent;//解决方案说明
	
	@DatabaseField
	private Float manHour;//工时
	
	@DatabaseField
	private String remark;//备注说明
	
	@DatabaseField
	private Date solvedTime;//解决时间
	
	@DatabaseField
	private Date createDate=new Date();//创建时间
	
	@ForeignCollectionField
	private ForeignCollection<QuestionAffix> questionAffixs;
	public LocaleQuestion() {
		super();
	}

	public LocaleQuestion(String fsiid,String title, String userId, String sysBelong,
			String sysBelongId, Date happenTime, String qsClass,
			String qsClassId, Integer qsGrade, String qsDiscrable,
			Date needFinishTime, Integer qsState, Boolean ifSolvedRoot,
			String resolvent, Float manHour, String remark, Date solvedTime,
			ForeignCollection<QuestionAffix> questionAffixs) {
		super();
		this.fsiid = fsiid;
		this.title=title;
		this.userId = userId;
		this.sysBelong = sysBelong;
		this.sysBelongId = sysBelongId;
		this.happenTime = happenTime;
		this.qsClass = qsClass;
		this.qsClassId = qsClassId;
		this.qsGrade = qsGrade;
		this.qsDiscrable = qsDiscrable;
		this.needFinishTime = needFinishTime;
		this.qsState = qsState;
		this.ifSolvedRoot = ifSolvedRoot;
		this.resolvent = resolvent;
		this.manHour = manHour;
		this.remark = remark;
		this.solvedTime = solvedTime;
		this.questionAffixs = questionAffixs;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getFsiid() {
		return fsiid;
	}

	public void setFsiid(String fsiid) {
		this.fsiid = fsiid;
	}

	public ForeignCollection<QuestionAffix> getQuestionAffixs() {
		return questionAffixs;
	}

	public void setQuestionAffixs(ForeignCollection<QuestionAffix> questionAffixs) {
		this.questionAffixs = questionAffixs;
	}


	public Long getKeyId() {
		return keyId;
	}

	public void setKeyId(Long keyId) {
		this.keyId = keyId;
	}

	public String getSysBelong() {
		return sysBelong;
	}

	public void setSysBelong(String sysBelong) {
		this.sysBelong = sysBelong;
	}

	public String getSysBelongId() {
		return sysBelongId;
	}

	public void setSysBelongId(String sysBelongId) {
		this.sysBelongId = sysBelongId;
	}

	public Date getHappenTime() {
		return happenTime;
	}

	public void setHappenTime(Date happenTime) {
		this.happenTime = happenTime;
	}

	public String getQsClass() {
		return qsClass;
	}

	public void setQsClass(String qsClass) {
		this.qsClass = qsClass;
	}

	public String getQsClassId() {
		return qsClassId;
	}

	public void setQsClassId(String qsClassId) {
		this.qsClassId = qsClassId;
	}

	public Integer getQsGrade() {
		return qsGrade;
	}

	public void setQsGrade(Integer qsGrade) {
		this.qsGrade = qsGrade;
	}

	public String getQsDiscrable() {
		return qsDiscrable;
	}

	public void setQsDiscrable(String qsDiscrable) {
		this.qsDiscrable = qsDiscrable;
	}

	public Date getNeedFinishTime() {
		return needFinishTime;
	}

	public void setNeedFinishTime(Date needFinishTime) {
		this.needFinishTime = needFinishTime;
	}

	public Integer getQsState() {
		return qsState;
	}

	public void setQsState(Integer qsState) {
		this.qsState = qsState;
	}

	public Boolean isIfSolvedRoot() {
		return ifSolvedRoot;
	}

	public void setIfSolvedRoot(Boolean ifSolvedRoot) {
		this.ifSolvedRoot = ifSolvedRoot;
	}

	public String getResolvent() {
		return resolvent;
	}

	public void setResolvent(String resolvent) {
		this.resolvent = resolvent;
	}

	public Float getManHour() {
		return manHour;
	}

	public void setManHour(Float manHour) {
		this.manHour = manHour;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Date getSolvedTime() {
		return solvedTime;
	}

	public void setSolvedTime(Date solvedTime) {
		this.solvedTime = solvedTime;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	
	
}
