package com.dream.fragment;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;

import com.dream.PublishedActivity;
import com.dream.R;
import com.dream.adapter.PhotoAdapter;
import com.dream.adapter.PhotoItem;
import com.dream.db.OrmSqliteDao;
import com.dream.db.dao.DynamicDao;
import com.dream.db.model.Dynamic;
import com.dream.db.model.Page;
import com.dream.util.CommUtils;
import com.dream.view.TitleBarView;
import com.etsy.android.grid.StaggeredGridView;

public class DynamicFragment extends Fragment 
			implements AbsListView.OnItemClickListener, AbsListView.OnScrollListener {
	private static final String TAG = "DynamicFragment";
	
    protected PhotoAdapter gridAdapter;
    protected ArrayList<PhotoItem> photoItemList;
    protected StaggeredGridView gridView;
	
	private Context mContext;
	private View mBaseView;
	private TitleBarView mTitleBarView;
	
	private Page page;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		page = new Page();
		mContext=getActivity();
		mBaseView=inflater.inflate(R.layout.fragment_dynamic, null);
		findView();
		initTitleView();
		initPhotoGrid();
		
		return mBaseView;
	}
	
	private void initPhotoGrid() {
		
		gridView = (StaggeredGridView) mBaseView.findViewById(R.id.grid_view);
		
		photoItemList = new ArrayList<PhotoItem>() ;
        gridAdapter = new PhotoAdapter(mContext, R.layout.item_grid_photo, photoItemList, false);
        
        gridView.setAdapter(gridAdapter);
        
        gridView.setOnScrollListener(this);
        gridView.setOnItemClickListener(this);
        
        new GetMoreDataTask().execute();
        
        
//        for (int i=0;i<10;i++) {
//        	
//        	String fileId = "9MfFpM5mdCMv532wptwZUM";
//        	if (i%2 == 1 ) {
//        		fileId = "6KpkZ84DXNtz4vRqL77xW7";
//        	}
//        	
//        	String thumbnailUri = CommUtils.getRequestUri(mContext) + "/file/" + fileId;
//        	String fullImageUri = CommUtils.getRequestUri(mContext) + "/file/" + fileId;
//        	
//        	photoItemList.add(new PhotoItem(thumbnailUri, fullImageUri));
//        }
//        
//        gridAdapter.notifyDataSetChanged();
        
	}

	private void findView(){
		mTitleBarView=(TitleBarView) mBaseView.findViewById(R.id.title_bar);
	}
	
	private void initTitleView(){
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
				startActivity(intent);
            }
        });
	}


	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		
	}
	
	
	
	/**
	 * 加载更多
	 *
	 */
	private class GetMoreDataTask extends AsyncTask<Integer, Integer, Integer> {

		List<Dynamic> oldList = new ArrayList<Dynamic>();
		
		@Override
		protected Integer doInBackground(Integer... params) {
			int result = -1;
			
			OrmSqliteDao<Dynamic> msgDao = new DynamicDao(mContext);  
			page.setOrder("atime");
			oldList = msgDao.find(page);
			
			if (null != oldList && oldList.size() > 0) {
				result = 1;
			}
			result = 1;
			
			return result;
		}

		private List<PhotoItem> getTestData() {
			List<PhotoItem> xx = new ArrayList<PhotoItem>();

			String[] pics = "X8TAs5daAnRhMCjdxYheND,XGSpBp1vNVTBciLxo8Xqbx,7n1wQ492fHeKNiMbpvyejB,9MfFpM5mdCMv532wptwZUM,9niWUX6L7GxZY53oVwxbQQ,6KpkZ84DXNtz4vRqL77xW7,3N26LB3i4b4mxADWBeR2EZ,9iqXy4r2XDogr6g6HiGi9E,CbfXB2sKz1wXa1QZzjwQ1e,7215JAJD1HNv8s7Y1Z7ECa".split(","); 
			for (int i = 0; i < 10; i++) {

				String fileId = "9MfFpM5mdCMv532wptwZUM";
				if (i % 2 == 1) {
					fileId = "6KpkZ84DXNtz4vRqL77xW7";
				}
				
				fileId = pics[i];

				String thumbnailUri = CommUtils.getRequestUri(mContext)
						+ "/file/" + fileId;
				String fullImageUri = CommUtils.getRequestUri(mContext)
						+ "/file/" + fileId;

				xx.add(new PhotoItem(thumbnailUri, fullImageUri));
			}
			return xx;
		}

		@Override
		protected void onPostExecute(Integer result) {
			if (result > 0) { //取到数据了， 
				
				for (Dynamic dynamic: oldList) {
					
		        	String thumbnailUri = CommUtils.getRequestUri(mContext) + "/file/" + dynamic.getImgId();
		        	String fullImageUri = CommUtils.getRequestUri(mContext) + "/file/" + dynamic.getImgId();
		        	
		        	photoItemList.add(new PhotoItem(thumbnailUri, fullImageUri));
				}
				
				photoItemList.addAll(getTestData()); //TODO will del
				
				gridAdapter.notifyDataSetChanged();
			}

			page.setPageNo(page.getPageNo() + 1);
			
			super.onPostExecute(result);
		}
	}



	@Override
	public void onScroll(final AbsListView view, final int firstVisibleItem, final int visibleItemCount, final int totalItemCount) {
		//滚动加载
		
		int lastInScreen = firstVisibleItem + visibleItemCount;
        if (lastInScreen >= totalItemCount) {
            Log.d(TAG, "onScroll lastInScreen - so load more");
            new GetMoreDataTask().execute();
        }
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		
	}
	

}
