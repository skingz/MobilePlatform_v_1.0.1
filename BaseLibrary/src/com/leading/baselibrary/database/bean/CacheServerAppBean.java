package com.leading.baselibrary.database.bean;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName="CacheServerAppBean")
public class CacheServerAppBean {
	@DatabaseField(generatedId=true)
	private int Id;
	@DatabaseField
	private String KeyId;
	@DatabaseField
	private String AppName_CN;
	@DatabaseField
	private String AppName_EN;
	@DatabaseField
	private String AppSize;
	@DatabaseField
	private String AppType;
	@DatabaseField
	private String Describle;
	@DatabaseField
	private String EditionNo;
	@DatabaseField
	private String EditionType;
	@DatabaseField
	private String IcoUrl;
	@DatabaseField
	private String StrUrl;
	@DatabaseField
	private String UpdateTime;
	
	private boolean HasInstalled=false;
	private boolean NeedUpdate=false;
	
	public int getId(){return this.Id;}
	public void setKeyId(String keyId){this.KeyId=keyId;}
	public void setAppName_CN(String appName_CN){this.AppName_CN=appName_CN;}
	public void setAppName_EN(String appName_EN){this.AppName_EN=appName_EN;}
	public void setAppSize(String appSize){this.AppSize=appSize;}
	public void setAppType(String appType){this.AppType=appType;}
	public void setDescrible(String describle){this.Describle=describle;}
	public void setEditionNo(String editionNo){this.EditionNo=editionNo;}
	public void setEditionType(String editionType){this.EditionType=editionType;}
	public void setIconUrl(String iconUrl){this.IcoUrl=iconUrl;}
	public void setStrUrl(String strUrl){this.StrUrl=strUrl;}
	public void setUpdateTime(String updateTime){this.UpdateTime=updateTime;}
	
	public void setHasInstalled(boolean hasInstalled){this.HasInstalled=hasInstalled;}
	public void setNeedUpdate(boolean needUpdate){this.NeedUpdate=needUpdate;}
	
	public String getKeyId(){return this.KeyId;}
	public String getAppName_CN(){return this.AppName_CN;}
	public String getAppName_EN(){return this.AppName_EN;}
	public String getAppSize(){return this.AppSize;}
	public String getAppType(){return this.AppType;}
	public String getEditionNo(){return this.EditionNo;}
	public String getDescrible(){return this.Describle;}
	public String getEditionType(){return this.EditionType;}
	public String getIconUrl(){return this.IcoUrl;}
	public String getStrUrl(){return this.StrUrl;}
	public String getUpdateTime(){return this.UpdateTime;}
	
	public boolean getHasInstalled(){return this.HasInstalled;}
	public boolean getNeedUpdate(){return this.NeedUpdate;}
}
