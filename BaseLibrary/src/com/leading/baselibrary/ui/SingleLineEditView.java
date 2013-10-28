package com.leading.baselibrary.ui;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.text.InputType;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.leading.baselibrary.R;
import com.leading.baselibrary.entity.ListViewAdapter;
import com.leading.baselibrary.util.DateUtil;
import com.leading.baselibrary.util.UnitConversionUtil;

public class SingleLineEditView extends LinearLayout {

	public final static String SELECTED_NAME = "name";// 弹出纵向滑动窗口内容
	public final static String SELECTED_KEY = "key";// 弹出纵向滑动窗口键值
	private int rightTextSize = 14;// 右边字体默认为12SP
	private final int TOP = 1;// 顶部带圆角 白色背景 灰色边框 无下边框 长方体样式
	private final int BUTTOM = 2;// 底部圆角 白色背景 灰色边框 长方体样式
	private final int CENTER = 3;// 不带圆角 白色背景 灰色边框 无下边框 长方体背景样式
	private final int SINGLE = 4;// 上下都带圆角 白色背景 灰色边框 长方体样式
	private final int DATETIME_TYPE = 1;// 右边显示控件类型为时间类型代表此行是弹出时间控件进行选择的功能
	private final int RADIO_TYPE = 2;// 右边显示控件类型为单选（一个扩展多个）按钮
	private final int TEXT_TYPE = 3;
	private final int EDIT_TYPE = 4;
	private final int EDIT_LINES_TYPE = 5;
	private final int SELECTED_LIST_TYPE = 6;
	private String leftTextValue = "标题";// 左边控件为默认显示值为标题
	private float singleHeight = 35f;// 默认左边控件的长度为35dip
	private float rightWidth = 185f;// 默认右边控件的长度为185dip
	private TextView textView;// 左边默认控件为TextView文本类型
	private EditText editText;// 右边默认控件类型是EditText编辑框类型
	private LinearLayout linearLayout;// 顶级控件
	private boolean singleTop = false;// 单行内容位置是否置顶
	private int edtLine = 6;// 右边编辑框行数
	private TypedArray ta;// XML配置参数获取
	private Context context;// 上下文对象
	private ViewGroup layout;// 当前行里的控件的父控件，用于添加和移除控件
	private UnitConversionUtil conversion;// PX-DIP单位转换工具类
	private RadioGroup radioGroup;// 右边单选按钮（RadioButton）控件的组
	private List<RadioButton> listRadio = new ArrayList<RadioButton>();// 单选按钮（RadioButton）的储存变量，用来操作单选控件
	boolean hasMeasured = false;
	private int mYear;// 为时间控件记录当前选择的年已备下次选择时间控件初始化值为上一次所设置的值
	private int mMonth;// 同上
	private int mDay;// 同上
	private ListViewAdapter adapter;// 弹出纵向滑动窗口的数据源
	private TextView rightText;// 右边TextView（文本）控件

	public SingleLineEditView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		linearLayout = (LinearLayout) inflater.inflate(
				R.layout.ui_single_line_edit, this, true);
		textView = (TextView) findViewById(R.id.singleline_edit_text);
		editText = (EditText) findViewById(R.id.singleline_edit_edit);
		editText.setBackgroundDrawable(null);
		ta = context.obtainStyledAttributes(attrs,
				R.styleable.SingleLineEditProperties, 0, 0);
		layout = (ViewGroup) editText.getParent();
		conversion = UnitConversionUtil.getInstance(context, 10f,
				UnitConversionUtil.DIPTOPX_TYPE);
		initStyle();// 初始化样式
		ta.recycle();

	}

	void initStyle() {
		singleTop = ta.getBoolean(
				R.styleable.SingleLineEditProperties_single_top, singleTop);
		rightWidth = ta.getDimension(
				R.styleable.SingleLineEditProperties_right_width, rightWidth);
		int widgetType = ta.getInteger(
				R.styleable.SingleLineEditProperties_widget_type, EDIT_TYPE);
		
		/**
		 * 单行样式.
		 * ....................................Single-Style..................
		 * .......................................
		 */
		singleStyle();
		/**
		 * 左边默认文本样式.
		 * ....................................TextView-Style..................
		 * ................................
		 */
		leftTextDefaultStyle();
		if (EDIT_TYPE == widgetType) {
			rightEditSingleStyle();
		} else if (RADIO_TYPE == widgetType) {
			rightRadioStyle();
		} else if (TEXT_TYPE == widgetType) {

		} else if (EDIT_LINES_TYPE == widgetType) {
			rightEditLinesStyle();
		} else if (DATETIME_TYPE == widgetType) {
			rightDateTimeStyle();
		} else if (SELECTED_LIST_TYPE == widgetType) {

		}
		

	}

	public void setRightTextValue(String value,boolean isEditLines) {
		removeRightWidget();
		rightText = new TextView(context);
		LinearLayout.LayoutParams lpet = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		rightText.setText(value);
		rightText.setLayoutParams(lpet);
		if(isEditLines){
			layout.setPadding(0, 0, 0, 0);
			textView.setPadding(conversion.allConversion(), 0, 0, 0);
			linearLayout.setPadding(0, 0, 0, UnitConversionUtil.getInstance(context, 10f, UnitConversionUtil.DIPTOPX_TYPE).allConversion());
			rightText.setPadding(0,0, 0, 0);
		}
		layout.addView(rightText);
	}

	private EditText datetimeType;

	public void removeRightWidget() {
		layout.removeView(editText);
		layout.removeView(radioGroup);
		layout.removeView(rightText);
		layout.removeView(edtTextLines);
		layout.removeView(datetimeType);
		editText = null;
		radioGroup = null;
		rightText = null;
		edtTextLines = null;
		datetimeType = null;
	}

	/**
	 * 时间类型样式.
	 */
	public void rightDateTimeStyle() {
		removeRightWidget();
		datetimeType = new EditText(context);
		datetimeType.setInputType(InputType.TYPE_NULL);// TYPE_NULL不弹出软键盘
		LinearLayout.LayoutParams lpet = new LinearLayout.LayoutParams(
				(int) rightWidth, LinearLayout.LayoutParams.WRAP_CONTENT);
		datetimeType.setTextSize(TypedValue.COMPLEX_UNIT_SP, rightTextSize);
		datetimeType.setLayoutParams(lpet);
		layout.setPadding(0, 0, 0,0);
		datetimeType.setBackgroundDrawable(null);
		final Calendar c = Calendar.getInstance();
		mYear = c.get(Calendar.YEAR);
		mMonth = c.get(Calendar.MONTH);
		mDay = c.get(Calendar.DAY_OF_MONTH);
		datetimeType.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus)
					showDate();
			}
		});
		datetimeType.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showDate();
			}
		});
		layout.addView(datetimeType);
	}

	public void rightRadioStyle() {
		removeRightWidget();
		String values = ta
				.getString(R.styleable.SingleLineEditProperties_right_value);
		String[] texts = values.substring(0, values.length()).split("\\,");
		editText = null;
		rightText = null;
		int count = texts.length;
		radioGroup = new RadioGroup(context);
		LayoutParams lprg = new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		radioGroup.setLayoutParams(lprg);
		radioGroup.setOrientation(HORIZONTAL);
		for (int i = 0; i < count; i++) {
			RadioButton cb = new RadioButton(context);
			TextView radioTv = new TextView(context);
			LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT);
			// lp.leftMargin=30;
			cb.setButtonDrawable(R.drawable.checkbox_style);
			cb.setBackgroundResource(0);
			cb.setLayoutParams(lp);
			String[] valueTag = texts[i].split(":");
			radioTv.setText(valueTag[0]);
			cb.setTag(valueTag[1]);
			radioTv.setPadding(0, 0, 0, 3);
			radioGroup.addView(radioTv);
			radioGroup.addView(cb);
			listRadio.add(cb);
		}
//		radioGroup.setPadding(conversion.allConversion(),
//				conversion.allConversion(), 0, conversion.allConversion());
		layout.addView(radioGroup);
	}

	public void setRadioOnclickListener(OnClickListener onclick) {
		for (RadioButton radio : listRadio) {
			radio.setOnClickListener(onclick);
		}
	}

	/**
	 * 左边默认文本样式.
	 */
	public void leftTextDefaultStyle() {
		textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
		LinearLayout.LayoutParams lptx = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		String leftValue = ta
				.getString(R.styleable.SingleLineEditProperties_left_textvalue);
		if (leftValue != null && !"".equals(leftValue.trim()))
			leftTextValue = leftValue;
		textView.setText(leftTextValue);
		textView.setLayoutParams(lptx);
		textView.requestLayout();
	}

	/**
	 * 设置单行样式.
	 */
	public void singleStyle() {
		int backgroundType = ta.getInteger(
				R.styleable.SingleLineEditProperties_backgroud_direction,
				SINGLE);
		if (TOP == backgroundType)
			linearLayout.setBackgroundResource(R.drawable.widget_kv_top);
		else if (BUTTOM == backgroundType)
			linearLayout.setBackgroundResource(R.drawable.widget_kv_bottom);
		else if (CENTER == backgroundType)
			linearLayout.setBackgroundResource(R.drawable.widget_kv_center);
		else if (SINGLE == backgroundType)
			linearLayout.setBackgroundResource(R.drawable.widget_kv_single);
		singleHeight = ta.getDimension(
				R.styleable.SingleLineEditProperties_single_height,
				singleHeight);
//		ViewTreeObserver vto = linearLayout.getViewTreeObserver();
//		vto.addOnPreDrawListener(new OnPreDrawListener() {
//			@Override
//			public boolean onPreDraw() {
//				if (hasMeasured == false) {
//					hasMeasured = true;
//				}
//				return true;
//			}
//		});
	}

	private ScrollView scroll;

	public void rightEditLinesStyle() {
		removeRightWidget();
		edtLine = ta.getInteger(R.styleable.SingleLineEditProperties_edt_line,
				edtLine);
		layout.setPadding(0, 0, 0, 0);
		textView.setPadding(conversion.allConversion(), 0, 0, 0);
		linearLayout.setPadding(0, 0, 0, UnitConversionUtil.getInstance(context, 20f, UnitConversionUtil.DIPTOPX_TYPE).allConversion());
		edtTextLines = new EditText(context);
		edtTextLines.setPadding(0,0, 0, 0);
		edtTextLines.setBackgroundDrawable(null);
		LinearLayout.LayoutParams lpet = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		edtTextLines.setTextSize(TypedValue.COMPLEX_UNIT_SP, rightTextSize);
		edtTextLines.setSingleLine(false);
		edtTextLines.setGravity(Gravity.TOP);
		edtTextLines.setFocusable(true);
		edtTextLines.setMinLines(1);
		edtTextLines.setMaxLines(edtLine);
		edtTextLines.setLayoutParams(lpet);
//		edtTextLines.setHorizontalScrollBarEnabled(false);
//		edtTextLines.setVerticalFadingEdgeEnabled(true);
//		edtTextLines.setVerticalScrollBarEnabled(true);
//		edtTextLines.setFocusableInTouchMode(true);
//		scroll = new ScrollView(context);
//		scroll.setLayoutParams(lpedt);
//		scroll.addView(edtTextLines);
//		layout.addView(scroll);
//
//		edtTextLines.setOnTouchListener(onTouchListener);
		layout.addView(edtTextLines);
	}

	float xdown = 0, ydown = 0; // 在按下屏幕的X、Y坐标
	float movex, movey; // 在移动的XX、Y坐标
	boolean flag = false;// true为往上滑动，false为往下滑动
	OnTouchListener onTouchListener = new OnTouchListener() {
		@Override
		public boolean onTouch(View v, MotionEvent me) {
			if (me.getAction() == MotionEvent.ACTION_DOWN) {
				xdown = me.getX();
				ydown = me.getY();
			}
			if (me.getAction() == MotionEvent.ACTION_UP) {
				v.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
					}
				});
			}
			if (me.getAction() == MotionEvent.ACTION_MOVE) {
				movex = me.getX();
				movey = me.getY();
				float xChange = Math.abs(movex - xdown);
				float yChange = Math.abs(movey - ydown);
				if (yChange > xChange) {// 上或下滑动
					if (ydown > movey) {// 往上滑动
						System.out.println("向上滑动");
						flag = true;
						scrollUpDownSliding();
					} else {// 往下滑动
						System.out.println("向下滑动");
						flag = false;
						scrollUpDownSliding();
					}
				}
			}
			return false;
		}
	};

	void scrollUpDownSliding() {
		int height = scroll.getHeight();
		int lineHeight = edtTextLines.getLineHeight();
		int lineCount = edtTextLines.getLineCount();// 总行数
		double viewCount = Math.floor(height / lineHeight);// 可见区域最大显示多少行
		if (lineCount > viewCount) {// 总行数大于可见区域显示的行数时则滚动
			if (flag)
				scroll.scrollBy(0, 8);
			else
				scroll.scrollBy(0, -8);
		}
	}

	@Override
	protected void finalize() throws Throwable {
		// TODO Auto-generated method stub
		super.finalize();
	}

	public void rightEditSingleStyle() {
		if (editText != null) {
			editText.setInputType(InputType.TYPE_CLASS_TEXT);
			LinearLayout.LayoutParams lpet = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
			editText.setTextSize(TypedValue.COMPLEX_UNIT_SP, rightTextSize);
			editText.setLayoutParams(lpet);
		}
	}

	public class MyDatePickerDialog implements
			DatePickerDialog.OnDateSetListener {
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			mYear = year;
			mMonth = monthOfYear;
			mDay = dayOfMonth;
			String ymd=new StringBuilder()
			.append(mYear)
			.append("-")
			.append((mMonth + 1) < 10 ? "0" + (mMonth + 1)
					: (mMonth + 1)).append("-")
			.append((mDay < 10) ? "0" + mDay : mDay).toString();
			
			datetimeType.setText(ymd);
			datetimeType.setTag(ymd+" "+DateUtil.getDateToString(new java.util.Date(), DateUtil.HMS));
		}
	}

	public void showDate() {
		MyDatePickerDialog mDatePicker = new MyDatePickerDialog();
		DatePickerDialog datePickerDialog = new DatePickerDialog(context,
				mDatePicker, mYear, mMonth, mDay);
		datePickerDialog.show();
	}

	public void showDropDownOptions(final List<Map<String, Object>> items,
			final String title) {
		removeRightWidget();
		rightText = new TextView(context);
		rightText.setText("请点击选择...");
		layout.addView(rightText);
		BackgourdSwitch.setButtonFocusChanged(linearLayout);
		linearLayout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				adapter = new ListViewAdapter(context, items);
				AlertDialog.Builder sinChosDia = new AlertDialog.Builder(
						context);
				sinChosDia.setTitle(title);
				sinChosDia.setSingleChoiceItems(adapter, 0,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();
								Map<String, Object> map = items.get(which);
								rightText.setText(map.get(SELECTED_NAME)
										.toString());
								rightText.setTag(map.get(SELECTED_KEY)
										.toString());
								int left=UnitConversionUtil.getInstance(context,10f,UnitConversionUtil.DIPTOPX_TYPE).allConversion();
								rightText.setPadding(0, 0, 0, 20);

								Log.i("px", String.valueOf(conversion
										.allConversion()));
								textView.setPadding(left, 0, 0, 20);
							}
						});
				sinChosDia.create().show();

			}
		});
	}

	public void settingRightTextPosition(int height, int width) {
		if (rightText != null)
			rightText.setPadding(width - rightText.getMeasuredWidth() / 2, 0,
					0, 0);
	}

	public void settingRadioGroupPosition(int height) {
		if (radioGroup != null)
			radioGroup.setPadding(8,
					(height - radioGroup.getMeasuredHeight()) / 2, 0, 0);
	}

	private EditText edtTextLines;

	public String getValue() {
		if (editText != null)
			return editText.getText().toString();
		else if (listRadio.size() > 0) {
			for (RadioButton rb : listRadio) {
				if (radioGroup.getCheckedRadioButtonId() == rb.getId()) {
					return rb.getTag().toString();
				}
			}
		} else if (rightText != null && rightText.getTag() != null)
			return rightText.getTag().toString();
		else if (edtTextLines != null && edtTextLines.getText() != null)
			return edtTextLines.getText().toString();
		else if (datetimeType != null && datetimeType.getTag() != null)
			return datetimeType.getTag().toString();
		return null;
	}

	public String getShowValue() {
		if (editText != null)
			return editText.getText().toString();
		else if (listRadio.size() > 0) {
			for (RadioButton rb : listRadio) {
				if (radioGroup.getCheckedRadioButtonId() == rb.getId()) {
					return rb.getText().toString();
				}
			}
		} else if (rightText != null && rightText.getTag() != null)
			return rightText.getText().toString();
		else if (edtTextLines != null && edtTextLines.getTag() != null)
			return edtTextLines.getText().toString();
		else if (datetimeType != null && datetimeType.getTag() != null)
			return datetimeType.getText().toString();
		return null;
	}

	public void setValue(String value) {
		if (editText != null)
			editText.setText(value);
		else if (listRadio.size() > 0) {
			for (RadioButton rb : listRadio) {
				if (rb.getTag().equals(value)) {
					rb.setTag(value);
					rb.setChecked(true);
				}
			}
		} else if (rightText != null)
			rightText.setTag(value);
		else if (edtTextLines != null)
			edtTextLines.setText(value);
		else if (datetimeType != null)
			datetimeType.setTag(value);
	}

	public void setShowValue(String value) {
		if (editText != null)
			editText.setText(value);
		else if (listRadio.size() > 0) {
			for (RadioButton rb : listRadio) {
				if (radioGroup.getCheckedRadioButtonId() == rb.getId()) {
					rb.setText(value);
				}
			}
		} else if (rightText != null)
			rightText.setText(value);
		else if (edtTextLines != null)
			edtTextLines.setText(value);
		else if (datetimeType != null)
			datetimeType.setText(value);
	}

	public void forbiddenWidget() {
		if (editText != null)
			editText.setEnabled(false);
		if (edtTextLines != null)
			edtTextLines.setEnabled(false);
		if (datetimeType != null)
			datetimeType.setEnabled(false);
		for (RadioButton radio : listRadio) {
			radio.setEnabled(false);
		}
	}

	public SingleLineEditView(Context context) {
		super(context, null);
	}

	public float getRightWidth() {
		return rightWidth;
	}

	public float getSingleHeight() {
		return singleHeight;
	}

	public TextView getTextView() {
		return textView;
	}

	public EditText getEditText() {
		return editText;
	}

	public LinearLayout getLinearLayout() {
		return linearLayout;
	}
}
