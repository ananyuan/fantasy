package com.dream.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dream.ItemEditActivity;
import com.dream.R;
import com.dream.util.CommUtils;
import com.dream.view.TitleBarView;

public class SettingFragment extends Fragment {
	private Context mContext;
	private View mBaseView;
	private TitleBarView mTitleBarView;
	
	private RelativeLayout serverHost;
	
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
		
		serverHost = (RelativeLayout) mBaseView.findViewById(R.id.server_host);
		final TextView hostLableView = (TextView)mBaseView.findViewById(R.id.lable_server_host);
		
		
		serverHost.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
				Intent intent = new Intent(mContext, ItemEditActivity.class);

				intent.putExtra("title", hostLableView.getText());
				intent.putExtra("from", CommUtils.DATA_FROM_PREFS);
				intent.putExtra("field_key", CommUtils.HOST_KEY);

				startActivity(intent);
            }
        });
	}
	
	private void initTitleView(){
		mTitleBarView.setCommonTitle(View.GONE, View.VISIBLE, View.GONE, View.GONE);
		mTitleBarView.setTitleText(R.string.setting);
	}
}
