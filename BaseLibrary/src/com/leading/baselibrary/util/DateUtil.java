package com.leading.baselibrary.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {

	public final static String YMD = "yyyy-MM-dd";
	public final static String YMDHM = "yyyy-MM-dd HH:mm";
	public final static String HMS="HH:mm:ss.SSS";
	public final static String FULLFORMAT="yyyy-MM-dd HH:mm:ss.SSS";

	public static String getDateToString(Date date, String format) {
		if (date == null)
			return null;
		else {
			SimpleDateFormat formatter = new SimpleDateFormat(format);
			String dateString = formatter.format(date);
			return dateString;
		}
	}

	public static Date getStringToDate(String date, String format) {
		SimpleDateFormat formatter = new SimpleDateFormat(format);
		try {
			return formatter.parse(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
