package com.leading.localequestion.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.j256.ormlite.dao.CloseableIterator;

public class LocaleQuestionBo {
	private String KeyId;
	private String QsTitle;
	private String SysBelong;//所属系统Id
	private Date HappenTime;//发生时间
	private String QsClass;//问题分类Id 
	private Integer QsGrade;//1(壹级)2(贰级)3(叁级) 问题等级
	private String QsDiscrable;//问题描述
	private Date NeedFinishTime;//要求完成时间
	private Integer QsState;//解决状态 0未解决，1已解决，2暂不解决
	private Boolean IfSolvedRoot;//是否彻底解决
	private String Resolvent;
	private Float ManHour;//工时
	private String Remark;//备注说明
	private Date SolvedTime;//解决时间
	private List<QuestionAffixBo> AffixList;
	public String SubmitPerson;
	public LocaleQuestionBo(String KeyId,String QsTitle, String SysBelong, Date HappenTime,
			String QsClass, Integer QsGrade, String QsDiscrable,
			Date NeedFinishTime, Integer QsState, Boolean IfSolvedRoot,
			String Resolvent, Float ManHour, String Remark, Date SolvedTime,String SubmitPerson,
			List<QuestionAffixBo> AffixList) {
		super();
		this.KeyId = KeyId;
		this.QsTitle=QsTitle;
		this.SysBelong = SysBelong;
		this.HappenTime = HappenTime;
		this.QsClass = QsClass;
		this.QsGrade = QsGrade;
		this.QsDiscrable = QsDiscrable;
		this.NeedFinishTime = NeedFinishTime;
		this.QsState = QsState;
		this.IfSolvedRoot = IfSolvedRoot;
		this.Resolvent = Resolvent;
		this.ManHour = ManHour;
		this.Remark = Remark;
		this.SolvedTime = SolvedTime;
		this.SubmitPerson=SubmitPerson;
		this.AffixList = AffixList;
	}
	
	public LocaleQuestionBo() {
		super();
	}

	public LocaleQuestionBo getLocaleQuestionToBo(LocaleQuestion lq){
		CloseableIterator<QuestionAffix> list = lq.getQuestionAffixs().closeableIterator();
		List<QuestionAffixBo> qbList=new ArrayList<QuestionAffixBo>();
		while(list.hasNext()){
			QuestionAffix qa=list.next();
			QuestionAffixBo qb=new QuestionAffixBo();
			qb.setAffixName(qa.getAffixName());
			qb.setKeyId(qa.getFsiid());
			qbList.add(qb);
		}
		LocaleQuestionBo localq=new LocaleQuestionBo(lq.getFsiid(),lq.getTitle(),lq.getSysBelongId(),lq.getHappenTime()
				,lq.getQsClass(),lq.getQsGrade(),lq.getQsDiscrable(),lq.getNeedFinishTime()
				,lq.getQsState(),lq.isIfSolvedRoot(),lq.getResolvent(),lq.getManHour(),lq.getRemark(),lq.getSolvedTime(),
				lq.getUserId(),qbList);
		return localq;
	}
	
	

	public String getQsTitle() {
		return QsTitle;
	}

	public void setQsTitle(String qsTitle) {
		QsTitle = qsTitle;
	}

	public String getKeyId() {
		return KeyId;
	}

	public void setKeyId(String keyId) {
		KeyId = keyId;
	}

	public String getSysBelong() {
		return SysBelong;
	}

	public void setSysBelong(String sysBelong) {
		SysBelong = sysBelong;
	}

	public Date getHappenTime() {
		return HappenTime;
	}

	public void setHappenTime(Date happenTime) {
		HappenTime = happenTime;
	}

	public String getQsClass() {
		return QsClass;
	}

	public void setQsClass(String qsClass) {
		QsClass = qsClass;
	}

	public Integer getQsGrade() {
		return QsGrade;
	}

	public void setQsGrade(Integer qsGrade) {
		QsGrade = qsGrade;
	}

	public String getQsDiscrable() {
		return QsDiscrable;
	}

	public void setQsDiscrable(String qsDiscrable) {
		QsDiscrable = qsDiscrable;
	}

	public Date getNeedFinishTime() {
		return NeedFinishTime;
	}

	public void setNeedFinishTime(Date needFinishTime) {
		NeedFinishTime = needFinishTime;
	}

	public Integer getQsState() {
		return QsState;
	}

	public void setQsState(Integer qsState) {
		QsState = qsState;
	}

	public Boolean isIfSolvedRoot() {
		return IfSolvedRoot;
	}

	public void setIfSolvedRoot(Boolean ifSolvedRoot) {
		IfSolvedRoot = ifSolvedRoot;
	}

	public String getResolvent() {
		return Resolvent;
	}

	public void setResolvent(String resolvent) {
		Resolvent = resolvent;
	}

	public Float getManHour() {
		return ManHour;
	}

	public void setManHour(Float manHour) {
		ManHour = manHour;
	}

	public String getRemark() {
		return Remark;
	}

	public void setRemark(String remark) {
		Remark = remark;
	}

	public Date getSolvedTime() {
		return SolvedTime;
	}

	public void setSolvedTime(Date solvedTime) {
		SolvedTime = solvedTime;
	}

	public List<QuestionAffixBo> getAffixList() {
		return AffixList;
	}

	public void setAffixList(List<QuestionAffixBo> affixList) {
		AffixList = affixList;
	}

	public String getSubmitPerson() {
		return SubmitPerson;
	}

	public void setSubmitPerson(String submitPerson) {
		SubmitPerson = submitPerson;
	}
}
