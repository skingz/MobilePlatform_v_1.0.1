package com.leading.localequestion.dao;


import java.sql.SQLException;
import java.util.List;

import android.content.Context;

import com.leading.baselibrary.database.LzDAO;
import com.leading.localequestion.R;
import com.leading.localequestion.entity.LocaleQuestion;

public class LocaleQuestionDao extends LzDAO<LocaleQuestion>{

	private Context _context;
	public LocaleQuestionDao(Context context) {
		super(context,R.xml.db);
		this._context=context;
	}
	public void DeleteNullDraft(){
		try {
			List<LocaleQuestion> lqList=super.getDao().queryForAll();
			for(LocaleQuestion lq :lqList){
				if(lq.getQsState()==null){
					if(lq.getQuestionAffixs().size()>0){
						QuestionAffixDao qa=new QuestionAffixDao(this._context);
						qa.getDao().delete(lq.getQuestionAffixs());
					}
					super.getDao().delete(lq);
					}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
