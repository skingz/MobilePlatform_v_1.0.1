package com.leading.baselibrary.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.leading.baselibrary.R;

public class TitleBarLayout extends RelativeLayout {

	private Button leftBtn = null;
	private Button rightBtn = null;
	private TextView text = null;

	public TitleBarLayout(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		LayoutInflater.from(context).inflate(R.layout.titlebar, this, true);
		leftBtn = (Button) findViewById(R.id.titlebar_left_button);
		rightBtn = (Button) findViewById(R.id.titlebar_right_button);
		text = (TextView) findViewById(R.id.titlebar_title_text);
	}

	public TitleBarLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater.from(context).inflate(R.layout.titlebar, this, true);
		leftBtn = (Button) findViewById(R.id.titlebar_left_button);
		rightBtn = (Button) findViewById(R.id.titlebar_right_button);
		text = (TextView) findViewById(R.id.titlebar_title_text);

		TypedArray ta = context.obtainStyledAttributes(attrs,
				R.styleable.TitleBar);

		int length = ta.getIndexCount();
		for (int i = 0; i < length; i++) {
			int resId = ta.getIndex(i);
			switch (resId) {
			case R.styleable.TitleBar_LeftButton_Visible:
				boolean l = ta.getBoolean(resId, false);
				if (l) {
					leftBtn.setVisibility(View.VISIBLE);
				}
				break;
			case R.styleable.TitleBar_RightButton_Visible:
				boolean r = ta.getBoolean(resId, false);
				if (r) {
					rightBtn.setVisibility(View.VISIBLE);
				}
				break;
			case R.styleable.TitleBar_TextView_Visible:
				boolean t = ta.getBoolean(resId, false);
				if (t) {
					text.setVisibility(View.VISIBLE);
				}
				break;
			case R.styleable.TitleBar_RightButton_Name:
				String rName = ta.getString(resId);
				rightBtn.setText(rName);
				break;
			case R.styleable.TitleBar_TextView_Name:
				String tName = ta.getString(resId);
				text.setText(tName);
				break;
			default:
				break;
			}
		}
		ta.recycle();
	}

	public Button getLeftBtn() {
		return leftBtn;
	}

	public Button getRightBtn() {
		return rightBtn;
	}

	public TextView getTextView() {
		return text;
	}

}
