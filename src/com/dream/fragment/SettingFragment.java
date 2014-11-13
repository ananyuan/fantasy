package com.dream.fragment;

import com.dream.R;
import com.dream.view.TitleBarView;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class SettingFragment extends Fragment {
	private Context mContext;
	private View mBaseView;
	private TitleBarView mTitleBarView;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mContext=getActivity();
		mBaseView=inflater.inflate(R.layout.fragment_setting, null);
		findView();
		initTitleView();
		return mBaseView;
	}
	
	private void findView(){
		mTitleBarView=(TitleBarView) mBaseView.findViewById(R.id.title_bar);
	}
	
	private void initTitleView(){
		mTitleBarView.setCommonTitle(View.GONE, View.VISIBLE, View.GONE, View.GONE);
		mTitleBarView.setTitleText(R.string.setting);
	}
}
