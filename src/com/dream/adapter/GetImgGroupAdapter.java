package com.dream.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Point;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;
import com.dream.R;
import com.dream.util.ImageBean;
import com.dream.util.ImgLoaderOptions;
import com.dream.view.GroupImageView;
import com.dream.view.GroupImageView.OnMeasureListener;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class GetImgGroupAdapter extends BaseAdapter{
	private List<ImageBean> list;
	private Point mPoint = new Point(0, 0);//用来封装ImageView的宽和高的对象
	private GridView mGridView;
	protected LayoutInflater mInflater;
	
	protected ImageLoader imageLoader = ImageLoader.getInstance();
	DisplayImageOptions options;
	
	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}


	@Override
	public long getItemId(int position) {
		return position;
	}
	
	public GetImgGroupAdapter(Context context, List<ImageBean> list, GridView mGridView){
		options = ImgLoaderOptions.getListOptions();
		
		this.list = list;
		this.mGridView = mGridView;
		mInflater = LayoutInflater.from(context);
	}
	

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder viewHolder;
		ImageBean mImageBean = list.get(position);
		String path = mImageBean.getTopImagePath();
		if(convertView == null){
			viewHolder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.image_grid_group_item, null);
			viewHolder.mImageView = (GroupImageView) convertView.findViewById(R.id.group_image);
			viewHolder.mTextViewTitle = (TextView) convertView.findViewById(R.id.group_title);
			viewHolder.mTextViewCounts = (TextView) convertView.findViewById(R.id.group_count);
			
			//用来监听ImageView的宽和高
			viewHolder.mImageView.setOnMeasureListener(new OnMeasureListener() {
				
				@Override
				public void onMeasureSize(int width, int height) {
					mPoint.set(width, height);
				}
			});
			
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder) convertView.getTag();
			viewHolder.mImageView.setImageResource(R.drawable.pictures_no);
		}
		
		viewHolder.mTextViewTitle.setText(mImageBean.getFolderName());
		viewHolder.mTextViewCounts.setText(Integer.toString(mImageBean.getImageCounts()));
		//给ImageView设置路径Tag,这是异步加载图片的小技巧
		viewHolder.mImageView.setTag(path);
		
		String imageUri = "file://" + path;
		imageLoader.displayImage(imageUri, viewHolder.mImageView, options);
		
		return convertView;
	}
	
	
	
	public static class ViewHolder{
		public GroupImageView mImageView;
		public TextView mTextViewTitle;
		public TextView mTextViewCounts;
	}

}
