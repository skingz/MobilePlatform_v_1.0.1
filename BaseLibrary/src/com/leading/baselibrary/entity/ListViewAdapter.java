package com.leading.baselibrary.entity;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.leading.baselibrary.R;
import com.leading.baselibrary.ui.SingleLineEditView;

public class ListViewAdapter extends BaseAdapter {
	private List<Map<String, Object>> items;
	private LayoutInflater inflater;
	
	public ListViewAdapter(Context context, List<Map<String, Object>> items) {
		this.items = items;
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);		
	}
	
	@Override
	public int getCount() {
		return items.size();
	}

	@Override
	public Object getItem(int position) {
		return items.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		if (view == null) {
			view = inflater.inflate(R.layout.list_item, null);
		}
		TextView textHidden=(TextView)view.findViewById(R.id.list_item_text_hidden);
		TextView text = (TextView) view.findViewById(R.id.list_item_text);
		textHidden.setText(items.get(position).get(SingleLineEditView.SELECTED_KEY).toString());
		text.setText(items.get(position).get(SingleLineEditView.SELECTED_NAME).toString());
		return view;
	}
	
	/**
	 * 添加列表
	 * @param item
	 */
	public void addItem(Map<String, Object> item) {
		items.add(item);
	}
}