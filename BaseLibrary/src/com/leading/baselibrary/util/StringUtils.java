package com.leading.baselibrary.util;

public class StringUtils {
	/**
	 * @deprecated Use {@link #isNotNull(Object)} instead
	 */
	public final static boolean isEmplayOrNull(Object o){
		return isNotNull(o);
	}

	/**
	 * @deprecated Use {@link #isNotNull(Object)} instead
	 */
	public final static boolean isEmplay(Object o){
		return isNotNull(o);
	}

	public final static boolean isNotNull(Object o){
		if(o==null)
			return false;
		if(o instanceof String){
			String str=o.toString();
			if(str!=null && !"".equals(str.trim()))
				return true;
			else
				return false;
		}else
			return true;
	}
	
	public final static String nullToStr(Object o){
		if(o==null)
			return "";
		return o.toString();
	}
	/**
	 * 截取前site位字符串，如果有超出的家省略号
	 * @param str
	 * @param site
	 * @param ifEllipsis 是否有省略号
	 * @return 返回处理后的字符串
	 */
	public final static String getSubString(String str,int site,boolean ifEllipsis){
		if(str==null)return "";
		else if(str.length()<site)return str;
		else if(ifEllipsis)return str.substring(0,site)+"...";
		else return str.substring(0,site);
	}
}
