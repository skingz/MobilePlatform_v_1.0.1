package com.leading.localequestion.entity;



public class SysBelongBo {

	private String ProjectName;
	private String ProjectId;


	public String getProjectId() {
		return ProjectId;
	}

	public void setProjectId(String projectId) {
		this.ProjectId = projectId;
	}

	public String getProjectName() {
		return ProjectName;
	}

	public void setProjectName(String projectName) {
		this.ProjectName = projectName;
	}

	public SysBelongBo(){super();}
	public SysBelongBo(String projectId,String projectName){
		this.ProjectId=projectId;
		this.ProjectName=projectName;
	}
	public SysBelong ToSysBelong(){
		SysBelong sb=new SysBelong();
		sb.setFsiid(this.ProjectId);
		sb.setName(this.ProjectName);
		return sb;
	}
	
}
