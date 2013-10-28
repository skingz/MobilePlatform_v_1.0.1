package com.leading.localequestion.global;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import android.annotation.SuppressLint;

import com.leading.baselibrary.entity.ConfigureEntity;
import com.leading.baselibrary.global.MainApplication;
import com.leading.baselibrary.util.ConfigureUtil;
import com.leading.localequestion.entity.LocaleQuestion;
import com.leading.localequestion.entity.QsClass;
import com.leading.localequestion.entity.QuestionAffix;
import com.leading.localequestion.entity.SysBelong;

public class LQApplication extends MainApplication{

	@SuppressLint("SdCardPath")
	private static final String FILENAME="/data/data/com.leading.mobileplat/files/configure.json";
	
	private static Map<String,Object> map=new HashMap<String,Object>();
	
	public static final String LocaleQuestion="LocaleQuestion";
	
	public static final String QsClass="QsClass";
	
	public static final String QuestionAffix="QuestionAffix";
			
	public static final String SysBelong="SysBelong";
	
	public static String Imglocation="";
	
	public static ConfigureEntity getConfig(){
		if(config==null){
			File file = new File(FILENAME);
			config=ConfigureUtil.getConfigureUtil().getStride(ctx, file);
		}
		return config;
	}
	
	public static Object getMapValue(String key){
		return map.get(key);
	}
	
	public static void setMapValue(String key,Object o){
		map.put(key, o);
	}
	
	public static void removeMapValue(String key){
		map.remove(key);
	}
	
	public static LocaleQuestion getLocaleQuestion(){
		return (LocaleQuestion)getMapValue(LocaleQuestion);
	} 
	
	public static QsClass getQsClass(){
		return (QsClass)getMapValue(QsClass);
	} 
	
	public static QuestionAffix getQuestionAffix(){
		return (QuestionAffix)getMapValue(QuestionAffix);
	} 
	
	public static SysBelong getSysBelong(){
		return (SysBelong)getMapValue(SysBelong);
	} 
	
}
