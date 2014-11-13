package com.dream.adapter;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dream.R;
import com.dream.db.model.Article;

public class ListWithThumAdapter extends BaseAdapter {

	private Activity activity;
	private List<Article> data;
	private static LayoutInflater inflater = null;
	//public ImageLoader imageLoader;

	public ListWithThumAdapter(Activity a, List<Article> datalist) {
		activity = a;
		data = datalist;
		inflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		//imageLoader = new ImageLoader(activity.getApplicationContext());
	}

	public int getCount() {
		return data == null ? 0 : data.size();
	}

	public Object getItem(int position) {
		return position;
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		View vi = convertView;
		if (convertView == null)
			vi = inflater.inflate(R.layout.list_thum_item, null);

		Article article = null;

		ImageView image = (ImageView) vi.findViewById(R.id.imgthumb);
		TextView tvartist = (TextView) vi.findViewById(R.id.tvartist);
		TextView tvtitle = (TextView) vi.findViewById(R.id.tvtitle);
		try {
			article = data.get(position);
			//imageLoader.DisplayImage(song.getString("thumb_url"), image);
			tvartist.setText(article.getAtime());
			tvtitle.setText(article.getTitle());
		} catch (Exception e) {
			e.printStackTrace();
		}

		return vi;
	}
}