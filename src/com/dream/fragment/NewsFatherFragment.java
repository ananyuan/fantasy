package com.dream.fragment;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.dream.R;
import com.dream.adapter.ListWithThumAdapter;
import com.dream.db.OrmSqliteDao;
import com.dream.db.dao.ArticleDao;
import com.dream.db.model.Article;
import com.dream.view.TitleBarView;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

public class NewsFatherFragment extends Fragment {

	private static final String TAG = "NewsFatherFragment";
	
	private Context mContext;
	private View mBaseView;
	private TitleBarView mTitleBarView;
	private PullToRefreshListView mPullRefreshListView;
	private ListWithThumAdapter listAdapter;
	private List<Article> dataList = new ArrayList<Article>();
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mContext=getActivity();
		mBaseView=inflater.inflate(R.layout.fragment_news_father, null);
		
		listAdapter = new ListWithThumAdapter(getActivity(), dataList);
		
		findView();
		init();
		return mBaseView;
	}
	


	private void findView(){
		mTitleBarView=(TitleBarView) mBaseView.findViewById(R.id.title_bar);
		
		mPullRefreshListView = (PullToRefreshListView) mBaseView.findViewById(R.id.pull_refresh_list);
		
		// Set a listener to be invoked when the list should be refreshed.
		mPullRefreshListView.setOnRefreshListener(new OnRefreshListener2<ListView>() {
//			@Override
//			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
//				String label = DateUtils.formatDateTime(mContext, System.currentTimeMillis(),
//						DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
//
//				// Update the LastUpdatedLabel
//				refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
//
//				// Do work to refresh the list here.
//				new GetDataTask().execute();
//			}
			
			@Override
			public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
				new RefreshDataTask().execute();
			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
				new GetMoreDataTask().execute();
			}
			
		});

		// Add an end-of-list listener
		mPullRefreshListView.setOnLastItemVisibleListener(new OnLastItemVisibleListener() {
			@Override
			public void onLastItemVisible() {
				//Toast.makeText(PullToRefreshListActivity.this, "End of List!", Toast.LENGTH_SHORT).show();
			}
		});
		
        // set mode to BOTH
		mPullRefreshListView.setMode(Mode.BOTH);
		
		
		ListView actualListView = mPullRefreshListView.getRefreshableView();
		
		actualListView.setAdapter(listAdapter);
		
		//初始化数据
		new GetMoreDataTask().execute();
	}
	
	private void init() {
		mTitleBarView.setCommonTitle(View.GONE, View.GONE, View.VISIBLE, View.VISIBLE);
		
		
		mTitleBarView.setTitleLeft(R.string.cnews);
		mTitleBarView.setTitleRight(R.string.call);
	}
	
	/**
	 * 加载更多
	 *
	 */
	private class GetMoreDataTask extends AsyncTask<Integer, Integer, Integer> {

		List<Article> oldList = new ArrayList<Article>();
		
		@Override
		protected Integer doInBackground(Integer... params) {
			int result = -1;
			
			OrmSqliteDao<Article> msgDao = new ArticleDao(mContext);  
			oldList = msgDao.find();
			
			if (oldList.size() > 0) {
				result = 1;
			}
			return result;
		}

		@Override
		protected void onPostExecute(Integer result) {
			if (result > 0) { //取到数据了， 
				dataList.addAll(oldList);
				
				listAdapter.notifyDataSetChanged();
			}
			// Call onRefreshComplete when the list has been refreshed.
			mPullRefreshListView.onRefreshComplete();

			super.onPostExecute(result);
		}
	}
	
	/**
	 * 刷新数据，从服务器上取
	 * 
	 */
	private class RefreshDataTask extends AsyncTask<Integer, Integer, Integer> {

		List<Article> newList = new ArrayList<Article>();
		
		@Override
		protected Integer doInBackground(Integer... params) {
			int result = -1;
			
			OrmSqliteDao<Article> msgDao = new ArticleDao(mContext);  
			//TODO query from server
			for (int i=0;i<3;i++) {
				Article article = new Article();
				article.setID(String.valueOf(i));
				article.setTITLE("这个是一条序号是" + i);
				article.setDATETIME("2014-11-12");
				
				newList.add(article);
				
				msgDao.saveOrUpdate(article);
			}
			
			if (newList.size() > 0) {
				result = 1;
			}
			return result;
		}

		@Override
		protected void onPostExecute(Integer result) {
			if (result > 0) {
				dataList.addAll(newList);

				listAdapter.notifyDataSetChanged();
			}
			
			// Call onRefreshComplete when the list has been refreshed.
			mPullRefreshListView.onRefreshComplete();

			super.onPostExecute(result);
		}
	}
}
