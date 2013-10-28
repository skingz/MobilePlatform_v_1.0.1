package com.leading.baselibrary.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface.OnClickListener;

public class DialogAlertUtil {
	//private static OnScreenHint toast;
	/**
	 * 弹出只显示一定范围时间的消息提示框.
	 * 
	 * @author JianTao.tu
	 * @param context
	 *            上下文
	 * @param text
	 *            要显示的内容
	 */
	public static void showToast(Context context, String text) {
		OnScreenHint toast=OnScreenHint.makeText(context, text,4000L);
		toast.show();
	}
  
	/**
	 * 居中弹出窗口选择页面.
	 * 
	 * @author JianTao.tu
	 * @param context
	 *            上下文
	 * @param adress
	 *            string-array的XML集合
	 * @param click
	 *            弹出窗点击事件
	 * @param title
	 *            标题
	 */
	public void alertDialog(Context context, int adress, OnClickListener click,
			String title) {
		final String[] items = context.getResources().getStringArray(adress);
		new AlertDialog.Builder(context).setTitle(title).setItems(items, click)
				.show();
	}
}
