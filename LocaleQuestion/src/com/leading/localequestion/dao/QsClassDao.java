package com.leading.localequestion.dao;


import android.content.Context;

import com.leading.baselibrary.database.LzDAO;
import com.leading.localequestion.R;
import com.leading.localequestion.entity.QsClass;

public class QsClassDao extends LzDAO<QsClass>{

	public QsClassDao(Context context) {
		super(context,R.xml.db);
	}
	

}
