package com.dream.adapter;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.dream.R;
import com.dream.util.ImgLoaderOptions;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class SelectImgAdapter extends BaseAdapter {
	protected LayoutInflater mInflater;
	protected Context mContext;
	protected List<String> mDatas;
	protected final int mItemLayoutId;
	
	private HashSet<String> mSelectSet = new HashSet<String>();
	
	protected ImageLoader imageLoader = ImageLoader.getInstance();
	DisplayImageOptions options;
	
	/**
	 * 用户选择的图片，存储为图片的完整路径
	 */
	public static List<String> mSelectedImage = new LinkedList<String>();

	/**
	 * 文件夹路径
	 */
	private String mDirPath;

	public SelectImgAdapter(Context context, List<String> mDatas, int itemLayoutId,
			String dirPath)
	{
		this.mContext = context;
		this.mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.mDatas = mDatas;
		this.mItemLayoutId = itemLayoutId;
		this.mDirPath = dirPath;
		options = ImgLoaderOptions.getListOptions();
	}

	@Override
	public int getCount() {
		return mDatas.size();
	}

	@Override
	public Object getItem(int position) {
		return mDatas.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder mHolder;
		View view = convertView;
		if (view == null) {
			view = mInflater.inflate(mItemLayoutId, null);
			mHolder = new ViewHolder();
			mHolder.imageview = (ImageView)view.findViewById(R.id.id_item_image);
			mHolder.imageButton = (ImageButton)view.findViewById(R.id.id_item_select);
			
			view.setTag(mHolder);
		} else {
			mHolder = (ViewHolder) view.getTag();
		}
		
		final String item = (String)getItem(position);
		imageLoader.displayImage("file://" + mDirPath + "/" + item, mHolder.imageview, options);
		
		final ImageView mImageView = mHolder.imageview;
		final ImageButton mSelect = mHolder.imageButton;
		
		mImageView.setColorFilter(null);
		//设置ImageView的点击事件
		mImageView.setOnClickListener(new OnClickListener()
		{
			//选择，则将图片变暗，反之则反之
			@Override
			public void onClick(View v)
			{
				// 已经选择过该图片
				if (mSelectedImage.contains(mDirPath + "/" + item)) {
					mSelectedImage.remove(mDirPath + "/" + item);
					mSelect.setImageResource(R.drawable.pictures_select_icon_unselected);
					mImageView.setColorFilter(null);
					if (mSelectSet.contains(mDirPath + "/" + item)) {
						mSelectSet.remove(mDirPath + "/" + item);
					}
				} else {// 未选择该图片
					mSelectedImage.add(mDirPath + "/" + item);
					mSelect.setImageResource(R.drawable.pictures_select_icon_selected);
					mImageView.setColorFilter(Color.parseColor("#77000000"));
					
					mSelectSet.add(mDirPath + "/" + item);
				}

			}
		});
		
		/**
		 * 已经选择过的图片，显示出选择过的效果
		 */
		if (mSelectedImage.contains(mDirPath + "/" + item))
		{
			mSelect.setImageResource(R.drawable.pictures_select_icon_selected);
			mImageView.setColorFilter(Color.parseColor("#77000000"));
		}
		
		return view;
	}
	
	public HashSet<String> getSelectedImgs() {
		return mSelectSet;
	}
	
	
	static class ViewHolder {
		ImageView imageview;
		
		ImageButton imageButton;
	}

}


