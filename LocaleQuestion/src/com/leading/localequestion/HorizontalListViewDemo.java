package com.leading.localequestion;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.leading.baselibrary.ui.ActivityCommon;
import com.leading.localequestion.ui.HorizontialListView;

public class HorizontalListViewDemo extends ActivityCommon implements OnItemClickListener{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.listviewdemo);
		HorizontialListView listview = (HorizontialListView) findViewById(R.id.listview);
		listview.setAdapter(mAdapter);
		listview.setOnItemClickListener(this);

	}
	private static String[] dataObjects = new String[] {"全部","爱情","喜剧","爱情","喜剧","爱情","喜剧","爱情","喜剧","爱情","喜剧","爱情","喜剧","爱情","喜剧"};

	private BaseAdapter mAdapter = new BaseAdapter() {

		@Override
		public int getCount() {
			return dataObjects.length;

		}

		@Override
		public Object getItem(int position) {
			return null;

		}

		@Override
		public long getItemId(int position) {
			return 0;

		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			TextView title = new TextView(HorizontalListViewDemo.this);
			
			title.setPadding(8, 0, 8, 0);
			title.setText(dataObjects[position]);
			return title;

		}

	};
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		Toast.makeText(HorizontalListViewDemo.this, "点到我了"+arg2, Toast.LENGTH_LONG).show();
		
	}
	@Override
	public void onDestroy() {
//		setContentView(R.layout.activity_null);
		super.onDestroy();
	}
	

}
