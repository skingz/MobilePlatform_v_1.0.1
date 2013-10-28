package com.leading.localequestion.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.leading.baselibrary.ui.SingleLineEditView;
import com.leading.localequestion.R;

public class QuestionMessageSubmitView extends LinearLayout{

	private SingleLineEditView uqm_bt;
	
	private SingleLineEditView uqm_ssxt;
	
	private SingleLineEditView uqm_wtfl;
	
	private SingleLineEditView uqm_fssj;
	
	private SingleLineEditView uqm_wtms;
	
	private SingleLineEditView uqm_yqjjsj;
	
	private SingleLineEditView uqm_wtdj;
	
	public QuestionMessageSubmitView(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater.from(context).inflate(R.layout.ui_question_message_submit, this, true);  
		uqm_ssxt=(SingleLineEditView)findViewById(R.id.uqm_ssxt_submit);
		uqm_wtfl=(SingleLineEditView)findViewById(R.id.uqm_wtfl_submit);
		uqm_fssj=(SingleLineEditView)findViewById(R.id.uqm_fssj_submit);
		uqm_wtms=(SingleLineEditView)findViewById(R.id.uqm_wtms_submit);
		uqm_yqjjsj=(SingleLineEditView)findViewById(R.id.uqm_yqjjsj_submit);
		uqm_wtdj=(SingleLineEditView)findViewById(R.id.uqm_wtdj_submit);
		uqm_bt=(SingleLineEditView)findViewById(R.id.uqm_bt_submit);
	}

	public SingleLineEditView getUqm_bt() {
		return uqm_bt;
	}

	public SingleLineEditView getUqm_ssxt() {
		return uqm_ssxt;
	}

	public SingleLineEditView getUqm_wtfl() {
		return uqm_wtfl;
	}

	public SingleLineEditView getUqm_fssj() {
		return uqm_fssj;
	}

	public SingleLineEditView getUqm_wtms() {
		return uqm_wtms;
	}

	public SingleLineEditView getUqm_yqjjsj() {
		return uqm_yqjjsj;
	}

	public SingleLineEditView getUqm_wtdj() {
		return uqm_wtdj;
	}

	public QuestionMessageSubmitView(Context context) {
		super(context,null);
		// TODO Auto-generated constructor stub
	}

}
