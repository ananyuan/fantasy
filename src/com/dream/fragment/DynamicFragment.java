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
    protected ArrayList<Dynamic> dynamicItemList;
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
		
		dynamicItemList = new ArrayList<Dynamic>() ;
        gridAdapter = new PhotoAdapter(mContext, R.layout.item_grid_photo, dynamicItemList, false);
        
        gridView.setAdapter(gridAdapter);
        gridView.setOnScrollListener(this);
        gridView.setOnItemClickListener(this);
        
        new GetMoreDataTask().execute();
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
			
			return result;
		}


		@Override
		protected void onPostExecute(Integer result) {
			if (result > 0) { //取到数据了， 
				
				for (Dynamic dynamic: oldList) {
		        	String fullImageUri = CommUtils.getRequestUri(mContext) + "/file/" + dynamic.getImgId();
		        	
		        	dynamic.setImgId(fullImageUri);
		        	
		        	dynamicItemList.add(dynamic);
				}
				
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
        if (lastInScreen >= totalItemCount && totalItemCount > 0) {
            Log.d(TAG, "onScroll lastInScreen - so load more");
            new GetMoreDataTask().execute();
        }
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		
	}
	

}
