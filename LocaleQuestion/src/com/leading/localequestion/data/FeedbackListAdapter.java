package com.leading.localequestion.data;

import java.io.File;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
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
import com.leading.localequestion.dao.QuestionAffixDao;

public class FeedbackListAdapter extends BaseAdapter {
	private LayoutInflater mInflater;
	private List<Map<String, Object>> mData;// 存储的EditText值
	private Context context;
	private QuestionAffixDao qaDao;
	public FeedbackListAdapter(Context context, List<Map<String, Object>> data) {
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

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.feedback_xczp_item, null);
			holder.imageView = (ImageView) convertView
					.findViewById(R.id.feedback_xczp_img);
			holder.affixName = (TextView) convertView
					.findViewById((R.id.feedback_xczp_txt));
			holder.deleteImg = (ImageView) convertView
					.findViewById(R.id.feedback_delete_img);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		Bitmap bt=ImgFactory.decodeBitmapFromFile(mData.get(position).get("affixPath").toString(),50,50);
		BitmapDrawable  drawable =new BitmapDrawable(bt);
		holder.imageView.setImageDrawable(drawable);
		holder.affixName.setText((String) mData.get(position).get("affixName"));
		holder.deleteImg.setTag((Long) mData.get(position).get("keyid"));
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
		holder.deleteImg.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				qaDao = new QuestionAffixDao(context);
				try {
					qaDao.getDao().deleteById((Long) v.getTag());
					File file = new File(mData.get(position).get("affixPath")
							.toString());
					mData.remove(position);
					if (file.isFile())
						file.delete();
					FeedbackListAdapter.this.notifyDataSetChanged();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		return convertView;
	}


	public final class ViewHolder {
		public ImageView imageView;
		public TextView affixName;
		public ImageView deleteImg;
	}
}
