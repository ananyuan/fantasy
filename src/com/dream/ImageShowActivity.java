package com.dream;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.widget.ImageView;
import android.widget.TextView;
import com.dream.R;
import com.dream.adapter.ImagePagerAdapter;
import com.ortiz.touch.ExtendedViewPager;

public class ImageShowActivity extends Activity {
	/** 图片展示 */
	private ExtendedViewPager image_pager;
	private TextView page_number;
	/** 图片下载按钮 */
	private ImageView download;
	/** 图片列表 */
	private ArrayList<String> imgsUrl;
	/** PagerAdapter */
	private ImagePagerAdapter mAdapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.details_imageshow);
		initView();
		initData();
		initViewPager();
	}

	private void initData() {
		imgsUrl = getIntent().getStringArrayListExtra("infos");
		page_number.setText("1" + "/" + imgsUrl.size());
	}

	private void initView() {
		image_pager = (ExtendedViewPager) findViewById(R.id.image_pager);
		page_number = (TextView) findViewById(R.id.page_number);
		download = (ImageView) findViewById(R.id.download);
		image_pager.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int arg0) {
				page_number.setText((arg0 + 1) + "/" + imgsUrl.size());
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub
				
			}
		});
	}

	private void initViewPager() {
		if (imgsUrl != null && imgsUrl.size() != 0) {
			mAdapter = new ImagePagerAdapter(getApplicationContext(), imgsUrl);
			image_pager.setAdapter(mAdapter);
		}
	}
}
