package com.dream.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.dream.R;
import com.dream.util.ImgLoaderOptions;
import com.etsy.android.grid.util.DynamicHeightImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class PhotoAdapter extends ArrayAdapter<PhotoItem> {
	private static final String TAG = "PhotoAdaptor";
	
	protected ImageLoader imageLoader = ImageLoader.getInstance();
	DisplayImageOptions options;
	
	private Context context;
	private int resourceId;
	private final Random mRandom;
	private final ArrayList<Integer> mBackgroundColors;

	private static final SparseArray<Double> sPositionHeightRatios = new SparseArray<Double>();

	public PhotoAdapter(Context context, int resourceId, List<PhotoItem> items,
			boolean useList) {
		super(context, resourceId, items);
		mRandom = new Random();
		this.context = context;
		this.resourceId = resourceId;
		mBackgroundColors = new ArrayList<Integer>();
		mBackgroundColors.add(R.color.orange);
		mBackgroundColors.add(R.color.lightgreen);
		mBackgroundColors.add(R.color.blue);
		mBackgroundColors.add(R.color.yellow);
		mBackgroundColors.add(R.color.gray);
		
		options = ImgLoaderOptions.getListOptions();
	}

	private class ViewHolder {
		DynamicHeightImageView photoImageView;
	}

	/**
	 * Populate the view holder with data.
	 * 
	 * @param position
	 * @param convertView
	 * @param parent
	 * @return
	 */
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder holder = null;
		PhotoItem photoItem = getItem(position);
		View viewToUse = null;

		LayoutInflater mInflater = (LayoutInflater) context
				.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		if (convertView == null) {
			holder = new ViewHolder();
			viewToUse = mInflater.inflate(resourceId, null);
			holder.photoImageView = (DynamicHeightImageView) viewToUse.findViewById(R.id.imageView);
			
			viewToUse.setTag(holder);
		} else {
			viewToUse = convertView;
			holder = (ViewHolder) viewToUse.getTag();
		}

		double positionHeight = getPositionRatio(position);
		int backgroundIndex = position >= mBackgroundColors.size() ? position
				% mBackgroundColors.size() : position;
		Log.d(TAG, "getView position:" + position + " h:" + positionHeight);

		// Set the thumbnail

		//holder.photoImageView.setImageURI(photoItem.getThumbnailUri());
		
		imageLoader.displayImage(photoItem.getThumbnailUri().toString(), holder.photoImageView, options);
		
		holder.photoImageView.setHeightRatio(positionHeight);

		viewToUse.setBackgroundResource(mBackgroundColors.get(backgroundIndex));

		return viewToUse;
	}

	private double getPositionRatio(final int position) {
		double ratio = sPositionHeightRatios.get(position, 0.0);

		if (ratio == 0) {
			ratio = getRandomHeightRatio();
			sPositionHeightRatios.append(position, ratio);
			Log.d(TAG, "getPositionRatio:" + position + " ratio:" + ratio);
		}
		return ratio;
	}

	private double getRandomHeightRatio() {
		return (mRandom.nextDouble() / 2.0) + 1.0; // height will be 1.0 - 1.5
													// the width
	}
}

