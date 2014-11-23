package com.dream.fragment;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.dream.DetailsActivity;
import com.dream.R;
import com.dream.adapter.ListWithThumAdapter;
import com.dream.db.OrmSqliteDao;
import com.dream.db.dao.ArticleDao;
import com.dream.db.model.Article;
import com.dream.db.model.Page;
import com.dream.util.CommUtils;
import com.dream.util.PrefUtils;
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
	private Page page;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mContext=getActivity();
		mBaseView=inflater.inflate(R.layout.fragment_news_father, null);
		
		listAdapter = new ListWithThumAdapter(getActivity(), dataList);
		
		page = new Page();
		
		findView();
		init();
		return mBaseView;
	}
	


	private void findView(){
		mTitleBarView=(TitleBarView) mBaseView.findViewById(R.id.title_bar);
		
		mPullRefreshListView = (PullToRefreshListView) mBaseView.findViewById(R.id.pull_refresh_list);
		
		// Set a listener to be invoked when the list should be refreshed.
		mPullRefreshListView.setOnRefreshListener(new OnRefreshListener2<ListView>() {
			
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
		
		
		mPullRefreshListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(mContext, DetailsActivity.class);
				
				intent.putExtra("article", listAdapter.getItem(position - 1));
				
				startActivity(intent);
			}
		});
		
        // set mode to BOTH
		mPullRefreshListView.setMode(Mode.BOTH);
		
		
		ListView actualListView = mPullRefreshListView.getRefreshableView();
		
		actualListView.setAdapter(listAdapter);
		
		//初始化数据 , 从本地查询
		new GetMoreDataTask().execute();
		
		//刷新，从服务器上查询
		new RefreshDataTask().execute();
	}
	
	private void init() {
		mTitleBarView.setCommonTitle(View.GONE, View.VISIBLE, View.GONE, View.GONE);
		
		mTitleBarView.setTitleText("柳暗花明");
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
			oldList = msgDao.find(page);
			
			if (null != oldList && oldList.size() > 0) {
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

			page.setPageNo(page.getPageNo() + 1);
			
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
			
			String lastTime = PrefUtils.getStr(mContext, CommUtils.LAST_KEY_ARTICLE, "");
			Log.d(TAG, lastTime);
			//TODO query from server
			if (lastTime.length() == 0) {
				lastTime = "2014-01-01";
			}
			
			lastTime = lastTime.replace(" ", "%20");
			
			String requestUrl = CommUtils.getRequestUri(mContext) + "/" + "article/list/" + lastTime;
			
			List<LinkedHashMap<String, Object>> list = CommUtils.getList(requestUrl);
			
			for (int i = 0; i < list.size(); i++) {
				Map<String, Object> map = list.get(i);
				
				ObjectMapper mapper = new ObjectMapper();
				Article article = mapper.convertValue(map, Article.class);

				newList.add(article);
				
				msgDao.saveOrUpdate(article);
				
				//按时间倒序取的数据，
				if (i==0) {
					PrefUtils.saveStr(mContext, CommUtils.LAST_KEY_ARTICLE, article.getAtime());
				}
			}
			
			if (newList.size() > 0) {
				result = 1;
			}
			return result;
		}

		@Override
		protected void onPostExecute(Integer result) {
			if (result > 0) {
				//刷新的数据，需要放在最上面
				int count = newList.size();
				
				for (int i=0;i<count;i++) {
					dataList.add(i, newList.get(i));
				}

				listAdapter.notifyDataSetChanged();
			}
			
			// Call onRefreshComplete when the list has been refreshed.
			mPullRefreshListView.onRefreshComplete();

			super.onPostExecute(result);
		}
	}
}
