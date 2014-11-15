package com.dream;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;

import com.dream.util.CommUtils;
import com.dream.util.PrefUtils;
import com.dream.view.TitleBarView;

public class ItemEditActivity extends Activity {
	
	private static final String TAG = "ItemEditActivity";
	
	private TitleBarView mTitleBarView;
	
	private EditText editText;
	
	private Context context;
	
	private String title;
	
	private String fromType;
	
	private String fieldKey;
	
	private String fieldValue = "";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = this;
		setContentView(R.layout.item_edit);
		getData();
		initView();
	}



	private void initView() {
		mTitleBarView=(TitleBarView) findViewById(R.id.title_bar);
		mTitleBarView.setCommonTitle(View.GONE, View.VISIBLE, View.GONE, View.VISIBLE);
		
		//中间的文字
		mTitleBarView.setTitleText(title);
		
		//右边的文字
		mTitleBarView.setBtnRight("确定");
		
		
		editText = (EditText) findViewById(R.id.input);
		
		editText.setText(fieldValue);
		
		//右边的点击事件
		mTitleBarView.setBtnRightOnclickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
            	if (fromType.equals(CommUtils.DATA_FROM_PREFS)) {
                	String newValue = editText.getText().toString();
                	
                	PrefUtils.saveStr(context, fieldKey, newValue);
                	
                	Toast.makeText(context, "保存成功！", Toast.LENGTH_SHORT).show();
                	
                	ItemEditActivity.this.finish();
            	}
            }
        });
		
	}

	private void getData() {
		title = getIntent().getStringExtra("title");
		fromType = getIntent().getStringExtra("from");
		fieldKey = getIntent().getStringExtra("field_key");
		
		if (fromType.equals(CommUtils.DATA_FROM_PREFS)) {
			fieldValue = PrefUtils.getStr(context, fieldKey, "");
		}
	}
}
