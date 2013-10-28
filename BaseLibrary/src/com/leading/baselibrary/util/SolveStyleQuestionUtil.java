package com.leading.baselibrary.util;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListAdapter;
import android.widget.ListView;

/**
 * 解決样式问题工具类.
 * @author tjt
 *
 */
public class SolveStyleQuestionUtil {
	
	/**
	 * 只有用在实现了onMesasure事件的View(这个View是包含ListView的视图类)类才能生效比如LinearLayout
	 * 没有实现的比如RelativeLayout类如果是此类则会抛出异常
	 * @param view
	 */
	public void setListViewHeightBasedOnChildren(AbsListView view) {
        //获取ListView对应的Adapter
		ListAdapter listAdapter = view.getAdapter();
        if (listAdapter == null) {
               return;
        }
       
        int totalHeight = 0;
        for (int i = 0, len = listAdapter.getCount(); i < len; i++) {   //listAdapter.getCount()返回数据项的数目
               View listItem = listAdapter.getView(i, null, view);
               listItem.measure(0, 0);  //计算子项View 的宽高
               totalHeight += listItem.getMeasuredHeight();  //统计所有子项的总高度
        }
       
        ViewGroup.LayoutParams params = view.getLayoutParams();
        int dividerHeight=0;
        if(view instanceof ListView){
        	dividerHeight=((ListView)view).getDividerHeight();
        }
        params.height = totalHeight + (dividerHeight * (listAdapter.getCount() - 1));
        //listView.getDividerHeight()获取子项间分隔符占用的高度
        //params.height最后得到整个ListView完整显示需要的高度
        view.setLayoutParams(params);
	}
}
