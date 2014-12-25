package com.dream.adapter;

import java.util.List;
import java.util.Random;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.dream.R;
import com.dream.db.model.Article;
import com.dream.util.CommUtils;
import com.dream.util.ImgLoaderOptions;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ListWithThumAdapter extends BaseAdapter {
	protected ImageLoader imageLoader = ImageLoader.getInstance();
	
	private Activity activity;
	private List<Article> data;
	private static LayoutInflater inflater = null;
	
	DisplayImageOptions options;

	public ListWithThumAdapter(Activity a, List<Article> datalist) {
		activity = a;
		data = datalist;
		inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		options = ImgLoaderOptions.getListOptions();
	}

	public int getCount() {
		return data == null ? 0 : data.size();
	}

	public Article getItem(int position) {
		if (data != null && data.size() != 0) {
			return data.get(position);
		}
		return null;
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder mHolder;
		View view = convertView;
		if (view == null) {
			view = inflater.inflate(R.layout.list_thum_item, null);
			mHolder = new ViewHolder();
			mHolder.item_layout = (LinearLayout)view.findViewById(R.id.item_layout);
			mHolder.comment_layout = (RelativeLayout)view.findViewById(R.id.comment_layout);
			mHolder.item_title = (TextView)view.findViewById(R.id.item_title);
			mHolder.item_source = (TextView)view.findViewById(R.id.item_source);
			mHolder.list_item_local = (TextView)view.findViewById(R.id.list_item_local);
			mHolder.comment_count = (TextView)view.findViewById(R.id.comment_count);
			mHolder.publish_time = (TextView)view.findViewById(R.id.publish_time);
			mHolder.item_abstract = (TextView)view.findViewById(R.id.item_abstract);
			mHolder.alt_mark = (ImageView)view.findViewById(R.id.alt_mark);
			mHolder.right_image = (ImageView)view.findViewById(R.id.right_image);
			mHolder.item_image_layout = (LinearLayout)view.findViewById(R.id.item_image_layout);
			mHolder.item_image_0 = (ImageView)view.findViewById(R.id.item_image_0);
			mHolder.item_image_1 = (ImageView)view.findViewById(R.id.item_image_1);
			mHolder.item_image_2 = (ImageView)view.findViewById(R.id.item_image_2);
			mHolder.large_image = (ImageView)view.findViewById(R.id.large_image);
			mHolder.comment_content = (TextView)view.findViewById(R.id.comment_content);
			mHolder.right_padding_view = (View)view.findViewById(R.id.right_padding_view);
			
			view.setTag(mHolder);
		} else {
			mHolder = (ViewHolder) view.getTag();
		}
		//获取position对应的数据
		Article article = getItem(position);
		mHolder.item_title.setText(article.getTitle());
		mHolder.item_source.setText("栏目名称");
		//mHolder.comment_count.setText("评论" + "news.getCommentNum()");
		mHolder.publish_time.setText(article.getAtime());
		String imgUrls = article.getImgids();
		mHolder.comment_count.setVisibility(View.VISIBLE);
		mHolder.right_padding_view.setVisibility(View.VISIBLE);
		if(imgUrls.length() > 0){
			String[] imgArray = imgUrls.split(",");
			String firstImg = CommUtils.getRequestUri(activity)  + "/file/" + imgArray[0];
			
			if(imgArray.length == 1){
				mHolder.item_image_layout.setVisibility(View.GONE);
				//是否是大图
				boolean flag = new Random().nextBoolean();
				if(false){
					mHolder.large_image.setVisibility(View.VISIBLE);
					mHolder.right_image.setVisibility(View.GONE);
					imageLoader.displayImage(firstImg, mHolder.large_image, options);
					mHolder.comment_count.setVisibility(View.GONE);
					mHolder.right_padding_view.setVisibility(View.GONE);
				} else {
					mHolder.large_image.setVisibility(View.GONE);
					mHolder.right_image.setVisibility(View.VISIBLE);
					imageLoader.displayImage(firstImg, mHolder.right_image, options);
				}
			} else {
				mHolder.large_image.setVisibility(View.GONE);
				mHolder.right_image.setVisibility(View.GONE);
				mHolder.item_image_layout.setVisibility(View.VISIBLE);
				imageLoader.displayImage(firstImg, mHolder.item_image_0, options);
				String secondImg = CommUtils.getRequestUri(activity)  + "/file/" + imgArray[1];
				imageLoader.displayImage(secondImg, mHolder.item_image_1, options);
				if (imgArray.length > 2) {
					String thirdImg = CommUtils.getRequestUri(activity)  + "/file/" + imgArray[2];
					imageLoader.displayImage(thirdImg, mHolder.item_image_2, options);	
				}
			}
		}else{
			mHolder.large_image.setVisibility(View.GONE);
			mHolder.right_image.setVisibility(View.GONE);
			mHolder.item_image_layout.setVisibility(View.GONE);
		}
//		int markResID = getAltMarkResID(news.getMark(),news.getCollectStatus());
//		if(markResID != -1){
//			mHolder.alt_mark.setVisibility(View.VISIBLE);
//			mHolder.alt_mark.setImageResource(markResID);
//		}else{
			mHolder.alt_mark.setVisibility(View.GONE);
//		}
		//判断该新闻概述是否为空
		if (!TextUtils.isEmpty(article.getSummary())) {
			mHolder.item_abstract.setVisibility(View.VISIBLE);
			if (article.getSummary().length() > 30) {
				mHolder.item_abstract.setText(article.getSummary().substring(0, 30));
			} else {
				mHolder.item_abstract.setText(article.getSummary());	
			}
		} else {
			mHolder.item_abstract.setVisibility(View.GONE);
		}
		//判断该新闻是否是特殊标记的，推广等，为空就是新闻
//		if(!TextUtils.isEmpty(news.getLocal())){
//			mHolder.list_item_local.setVisibility(View.VISIBLE);
//			mHolder.list_item_local.setText(news.getLocal());
//		}else{
			mHolder.list_item_local.setVisibility(View.GONE);
//		}
		//判断评论字段是否为空，不为空显示对应布局
//		if(!TextUtils.isEmpty(news.getComment())){
//			//news.getLocal() != null && 
//			mHolder.comment_layout.setVisibility(View.VISIBLE);
//			mHolder.comment_content.setText(news.getComment());
//		}else{
			mHolder.comment_layout.setVisibility(View.GONE);
//		}

		return view;
	}
	
	static class ViewHolder {
		LinearLayout item_layout;
		//title
		TextView item_title;
		//图片源
		TextView item_source;
		//类似推广之类的标签
		TextView list_item_local;
		//评论数量
		TextView comment_count;
		//发布时间
		TextView publish_time;
		//新闻摘要
		TextView item_abstract;
		//右上方TAG标记图片
		ImageView alt_mark;
		//右边图片
		ImageView right_image;
		//3张图片布局
		LinearLayout item_image_layout; //3张图片时候的布局
		ImageView item_image_0;
		ImageView item_image_1;
		ImageView item_image_2;
		//大图的图片的话布局
		ImageView large_image;

		//评论布局
		RelativeLayout comment_layout;
		TextView comment_content;
		//paddingview
		View right_padding_view;
	}
}