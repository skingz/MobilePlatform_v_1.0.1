package com.leading.localequestion.data;

import java.io.File;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.leading.baselibrary.util.ImgFactory;
import com.leading.localequestion.ActivityImgShow;
import com.leading.localequestion.R;

public class FeedbackGridAdapter extends BaseAdapter {
	private LayoutInflater mInflater;
	private List<Map<String, Object>> mData;// 存储的EditText值
	private Context context;
	private Bitmap[] bitmaps;
	public FeedbackGridAdapter(Context context, List<Map<String, Object>> data) {
		mData = data;
		this.context = context;
		mInflater = LayoutInflater.from(context);
		// init();
	}

	@Override
	public int getCount() {
		return mData.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	private ViewHolder holder;

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.feedback_xczp_row, null);
			holder.imageView = (ImageView) convertView
					.findViewById(R.id.feedback_xczp_img);
			holder.affixName = (TextView) convertView
					.findViewById((R.id.feedback_xczp_txt));
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		Bitmap bitmap = ImgFactory.convertBitmap(new File(mData.get(position)
				.get("affixPath").toString()));
		if(position==0){
			bitmaps=new Bitmap[getCount()];
		}
		bitmaps[position]=bitmap;
		holder.imageView.setImageBitmap(bitmap);
		holder.affixName.setText((String) mData.get(position).get("affixName"));
		// BackgourdSwitch.setButtonFocusChanged(holder.imageView);
		holder.imageView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context, ActivityImgShow.class);
				intent.putExtra("affixPath",
						(String) mData.get(position).get("affixPath"));
				intent.putExtra("affixName",
						(String) mData.get(position).get("affixName"));
				context.startActivity(intent);
			}
		});

		return convertView;
	}

	public final class ViewHolder {
		public ImageView imageView;
		public TextView affixName;
	}

	@Override
	public void finalize() throws Throwable {
		holder = null;
		for(Bitmap bitmap:bitmaps){
			bitmap.recycle();
			bitmap=null;
		}
		bitmaps=null;
		super.finalize();
	}
}
