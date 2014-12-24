package com.dream.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.dream.PublishedActivity;
import com.dream.R;
import com.dream.util.Constant;
import com.dream.view.ImageScollView;
import com.dream.view.TitleBarView;

public class DynamicFragment extends Fragment {
	private static final String TAG = "DynamicFragment";
	
	
	private Context mContext;
	private View mBaseView;
	private TitleBarView mTitleBarView;
	private ImageScollView imgeView;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mContext = getActivity();
		mBaseView = inflater.inflate(R.layout.fragment_dynamic, null);
		initTitleView();
		
		imgeView = (ImageScollView) mBaseView.findViewById(R.id.image_scroll_view);
		
		
		return mBaseView;
	}
	
	private void initTitleView(){
		mTitleBarView=(TitleBarView) mBaseView.findViewById(R.id.title_bar);
		
		mTitleBarView.setCommonTitle(View.GONE, View.VISIBLE, View.GONE, View.VISIBLE);
		mTitleBarView.setTitleText(R.string.dynamic);
		
		mTitleBarView.setBtnRight("发布");
		
		mTitleBarView.setBtnRightOnclickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
        		Intent intent = new Intent(mContext, PublishedActivity.class);
        		Bundle bundle1 = new Bundle();
        		bundle1.putBoolean("pubsort", true);
    			intent.putExtras(bundle1);
    			startActivityForResult(intent, Constant.REQUEST_CODE_PUBLISH);
            }
        });
	}

	
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
			case Constant.REQUEST_CODE_PUBLISH:  
				if (null != data ) {
					String resultStr = data.getStringExtra(Constant.BACK_FROM_PUBLISH);
					if (resultStr.equals(Constant.BACK_FROM_PUBLISH)) {
						imgeView.refresh();  //发布的返回， 刷新页面
					}
				}
				
				
				break;
		}
	}
	
	
	/**
	 * 调用onCreate(), 目的是刷新数据, 
	 * 从另一activity界面返回到该activity界面时, 此方法自动调用
	 */
	@Override
	public void onResume() {
		super.onResume();
	}

}
