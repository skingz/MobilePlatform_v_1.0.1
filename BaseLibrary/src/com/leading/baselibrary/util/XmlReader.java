package com.leading.baselibrary.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParserException;

import android.content.res.Resources;
import android.content.res.XmlResourceParser;

import com.leading.baselibrary.entity.DBEntity;

public class XmlReader {

	private final String DATABSE = "database";
	private final String DATABSE_NAME = "name";
	private final String DATABSE_VERSION = "version";
	private final String TABLE = "table";
	private final String TABLE_NAME = "name";

	public DBEntity getDBEntity(Resources resources,int url) {
		XmlResourceParser xpp = null;
		DBEntity db = null;
		List<String> list = null;
		xpp = resources.getXml(url);
		try {
			int type = xpp.getEventType();
			while (type != XmlResourceParser.END_DOCUMENT) {// 如果没走到最后一个节点就继续循环
				switch (type) {
				case XmlResourceParser.START_DOCUMENT:// 节点开始
					db = new DBEntity();
					list = new ArrayList<String>();
					break;
				case XmlResourceParser.START_TAG:// 开始的某个节点
					if (this.DATABSE.equals(xpp.getName())) {// 节点名称进行比较
						for (int i = 0; i < xpp.getAttributeCount(); i++) {
							if (this.DATABSE_NAME.equals(xpp
									.getAttributeName(i)))
								db.setDataBaseName(xpp.getAttributeValue(i));
							else if (this.DATABSE_VERSION.equals(xpp
									.getAttributeName(i)))
								db.setDataBaseVersion(Integer.parseInt(xpp
										.getAttributeValue(i)));
						}
					} else if (this.TABLE.equals(xpp.getName())) {
						for (int i = 0; i < xpp.getAttributeCount(); i++) {
							if (this.TABLE_NAME.equals(xpp.getAttributeName(i)))
								list.add(xpp.getAttributeValue(i));
						}
					}
					break;
				case XmlResourceParser.END_TAG:// 结束某个节点
					// sb.append("End tag:" + xpp.getName());
					break;
				case XmlResourceParser.TEXT://输出接点之间的文本
					// if (xpp.getText().endsWith("")) {
					// sb.append("\t Start text: null");
					// } else {
					// sb.append("\t Start text:" + xpp.getText());
					// }
					break;
				default:
					break;
				}
				xpp.next();
				type = xpp.getEventType();
			}
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		db.setDataBaseTable(list);
		return db;
	}
}
