package com.leading.baselibrary.util;

import android.content.Context;

/**
 * 单位 转换工具类.
 * @author tjt
 *
 */
public class UnitConversionUtil {
	
	private Context context;
	
	private float unitValue;
	
	private int type=-1;
	/**
	 * PX类型.
	 */
	public final static int DIPTOPX_TYPE=1;
	
	/**
	 * DIP类型.
	 */
	public final static int PXTODIP_TYPE=2;
	
	private static UnitConversionUtil unitConversionUtil=null;
	
	private UnitConversionUtil(){}
	
	public static UnitConversionUtil getInstance(Context context){
		if(unitConversionUtil==null)
			unitConversionUtil=new UnitConversionUtil();
		if(unitConversionUtil.context!=context)
			unitConversionUtil.context=context;
		return unitConversionUtil;
		
	}
	
	/**
	 * 
	 * @param context 当前上下文.
	 * @param unitValue 转换的值.
	 * @param type 转换类型 UnitConversionUtil里有常量.
	 * @return
	 */
	public static UnitConversionUtil getInstance(Context context,float unitValue,int type){
		if(unitConversionUtil==null){
			unitConversionUtil=new UnitConversionUtil();
		}
		if(unitConversionUtil.type!=type)
			unitConversionUtil.type=type;
		if(unitConversionUtil.unitValue!=unitValue)
			unitConversionUtil.unitValue=unitValue;
		if(unitConversionUtil.context!=context)
			unitConversionUtil.context=context;
		return unitConversionUtil;
	}
	/**
	 * dip转换px
	 * @param dipValue
	 * @return
	 */
	public int dipTopx(float dipValue){
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int)(dipValue * scale + 0.5f);
	}
	/**
	 * px转换wdip
	 * @param pxValue
	 * @return
	 */
	public int pxTodip(float pxValue){
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int)(unitValue / scale + 0.5f);
	}
	
	/**
	 * getInstance(Context context,float unitValue,int type)此方法实例化的对象直接调用可返回转换的值.
	 * @param dipValue
	 * @return
	 * @throws Exception 
	 */
	public int allConversion(){
		if(type!=-1){
			switch(type){
			case UnitConversionUtil.PXTODIP_TYPE:
				return pxTodip(unitValue);
			case UnitConversionUtil.DIPTOPX_TYPE:
				return dipTopx(unitValue);
			}
		}else{
			try {
				throw new Exception("除了用 getInstance(Context context,float unitValue,int type) 方法实例化的对象不能调用此方法");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return -1;
		
	}

}
