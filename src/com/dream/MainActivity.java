package com.dream;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.Toast;

import com.dream.fragment.ConstactFatherFragment;
import com.dream.fragment.DynamicFragment;
import com.dream.fragment.NewsFatherFragment;
import com.dream.fragment.SettingFragment;

public class MainActivity extends FragmentActivity {

	protected static final String TAG = "MainActivity";
	private Context context;
	private ImageButton mNews,mConstact,mDeynaimic,mSetting;
	private View currentButton;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		context = this;
		
		findView();
		init();
	}
	
	private void findView(){
		mNews=(ImageButton) findViewById(R.id.buttom_news);
		mConstact=(ImageButton) findViewById(R.id.buttom_constact);
		mDeynaimic=(ImageButton) findViewById(R.id.buttom_deynaimic);
		mSetting=(ImageButton) findViewById(R.id.buttom_setting);
	}
	
	private void init(){
		mNews.setOnClickListener(newsOnClickListener);
		mConstact.setOnClickListener(constactOnClickListener);
		mDeynaimic.setOnClickListener(deynaimicOnClickListener);
		mSetting.setOnClickListener(settingOnClickListener);
		
		mNews.performClick();
	}
	
	private OnClickListener newsOnClickListener=new OnClickListener() {
		@Override
		public void onClick(View v) {
			FragmentManager fm=getSupportFragmentManager();
			FragmentTransaction ft=fm.beginTransaction();
			NewsFatherFragment newsFatherFragment=new NewsFatherFragment();
			ft.replace(R.id.fl_content, newsFatherFragment,MainActivity.TAG);
			ft.commit();
			setButton(v);
			
		}
	};
	
	private OnClickListener constactOnClickListener=new OnClickListener() {
		@Override
		public void onClick(View v) {
			FragmentManager fm=getSupportFragmentManager();
			FragmentTransaction ft=fm.beginTransaction();
			ConstactFatherFragment constactFatherFragment=new ConstactFatherFragment();
			ft.replace(R.id.fl_content, constactFatherFragment,MainActivity.TAG);
			ft.commit();
			setButton(v);
			
		}
	};
	
	private OnClickListener deynaimicOnClickListener=new OnClickListener() {
		@Override
		public void onClick(View v) {
			FragmentManager fm=getSupportFragmentManager();
			FragmentTransaction ft=fm.beginTransaction();
			DynamicFragment dynamicFragment=new DynamicFragment();
			ft.replace(R.id.fl_content, dynamicFragment,MainActivity.TAG);
			ft.commit();
			setButton(v);
			
		}
	};
	
	private OnClickListener settingOnClickListener=new OnClickListener() {
		@Override
		public void onClick(View v) {
			FragmentManager fm=getSupportFragmentManager();
			FragmentTransaction ft=fm.beginTransaction();
			SettingFragment settingFragment=new SettingFragment();
			ft.replace(R.id.fl_content, settingFragment,MainActivity.TAG);
			ft.commit();
			setButton(v);
			
		}
	};
	
	private void setButton(View v){
		if(currentButton!=null&&currentButton.getId()!=v.getId()){
			currentButton.setEnabled(true);
		}
		v.setEnabled(false);
		currentButton=v;
	}

	
	
	private long mExitTime;
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if ((System.currentTimeMillis() - mExitTime) > 2000) {
				Toast.makeText(context, "在按一次退出", Toast.LENGTH_SHORT).show();
				mExitTime = System.currentTimeMillis();
			} else {
				finish();
			}
			return true;
		}
		//拦截MENU按钮点击事件，让他无任何操作
		if (keyCode == KeyEvent.KEYCODE_MENU) {
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
