package com.leading.localequestion.dao;


import android.content.Context;

import com.leading.baselibrary.database.LzDAO;
import com.leading.localequestion.R;
import com.leading.localequestion.entity.QuestionAffix;

public class QuestionAffixDao extends LzDAO<QuestionAffix>{

	public QuestionAffixDao(Context context) {
		super(context,R.xml.db);
		// TODO Auto-generated constructor stub
	}

}
