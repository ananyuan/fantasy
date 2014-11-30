package com.dream;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.GridView;
import android.widget.Toast;

import com.dream.adapter.SelectImgAdapter;
import com.dream.view.TitleBarView;

public class ShowSelectImageActivity extends Activity {

	private File mImgDir;
	/**
	 * 所有的图片
	 */
	private List<String> mImgs = new ArrayList<String>();
	
	private GridView mGirdView;
	private SelectImgAdapter mAdapter;
	
	private TitleBarView mTitleBarView;
	
	private Context context;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		this.context = this;
		
		setContentView(R.layout.activity_select_image);

		mGirdView = (GridView) findViewById(R.id.selectImg_gridView);
		
		initTitleView();
		
		if(!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
			Toast.makeText(this, "暂无外部存储", Toast.LENGTH_SHORT).show();
			return;
		}
		
		Bundle bundle = getIntent().getExtras();
		String fileDir = bundle.getString("fileDir");
		
		mImgDir = new File(fileDir);
		
		String[] imgs = mImgDir.list();
		for (String fileName: imgs) {
			String extName = fileName.substring(fileName.lastIndexOf("."), fileName.length() - 1);
			if (".png.jpg.gif.jpeg".indexOf(extName.toLowerCase()) > 0) {
				mImgs.add(fileName);
			}
		}
		
		//mImgs = Arrays.asList(mImgDir.list());
		
		
		mAdapter = new SelectImgAdapter(getApplicationContext(), mImgs,
				R.layout.select_image_grid_item, mImgDir.getAbsolutePath());
		mGirdView.setAdapter(mAdapter);
	}
	
	
	private void initTitleView(){
		mTitleBarView=(TitleBarView) findViewById(R.id.title_bar);
		
		mTitleBarView.setCommonTitle(View.GONE, View.VISIBLE, View.GONE, View.VISIBLE);
		mTitleBarView.setTitleText("照片选择");
		
		mTitleBarView.setBtnRight("确定");
		
		mTitleBarView.setBtnRightOnclickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
            	
            	HashSet<String> rtnimgs = mAdapter.getSelectedImgs();
            	
            	Toast.makeText(context, rtnimgs.toString(), Toast.LENGTH_SHORT).show();
            }
        });
	}
}
