package com.dream.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.dream.PublishedActivity;
import com.dream.R;
import com.dream.db.OrmSqliteDao;
import com.dream.db.dao.DynamicDao;
import com.dream.db.model.Dynamic;
import com.dream.db.model.Page;
import com.dream.util.CommUtils;
import com.dream.util.Constant;
import com.dream.util.PrefUtils;
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
		
		mTitleBarView.setBtnRight("更多");
		
		mTitleBarView.setBtnRightOnclickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
            	PopupMenu popupMenu = new PopupMenu(mContext, mTitleBarView.findViewById(R.id.title_btn_right));
            	
            	popupMenu.getMenuInflater().inflate(R.menu.dynamic, popupMenu.getMenu()); 
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() { 
                     
                    @Override 
                    public boolean onMenuItemClick(MenuItem item) { 
                    	if (item.getItemId() == R.id.dynamic_deploy) {  // 发布
                    		Intent intent = new Intent(mContext, PublishedActivity.class);
                    		Bundle bundle1 = new Bundle();
                    		bundle1.putBoolean("pubsort", true);
                			intent.putExtras(bundle1);
                			startActivityForResult(intent, Constant.REQUEST_CODE_PUBLISH);
                    	} else if (item.getItemId() == R.id.dynamic_upload) { //同步上传
                    		ConnectivityManager connManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
                    		NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

                    		if (mWifi.isConnected()) {
                    			new UploadTask().execute(); //上传数据    
                    		} else {
                    			new AlertDialog.Builder(mContext)
                    			.setTitle("不在WIFI")
                    			.setMessage("确认不在wifi的时候上传?")
                    			.setIcon(android.R.drawable.ic_dialog_alert)
                    			.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    			    public void onClick(DialogInterface dialog, int whichButton) {
                    			    	new UploadTask().execute(); 
                    			    }})
                    			.setNegativeButton(android.R.string.no, null).show();
                    		}
                    	} else if (item.getItemId() == R.id.dynamic_pull) {
                    		new RefreshDataTask().execute();
                    	}
                        
                        return false; 
                    } 
                }); 
                popupMenu.show(); 
            }
        });
	}

	
	/**
	 * 上传同步数据
	 *
	 */
	private class UploadTask extends AsyncTask<Integer, Integer, Integer> {
		
		@Override
		protected Integer doInBackground(Integer... params) {
			int result = -1;
			Page page = new Page();
			page.setPageSize(20);
			
			OrmSqliteDao<Dynamic> msgDao = new DynamicDao(mContext);  
			page.setOrder("atime");
			
			HashMap<String, Object> queryMap = new HashMap<String, Object>();
			queryMap.put("itemtype", Constant.DYNAMIC_ITEM_LOCAL); //查询本地的去上传
			
			List<Dynamic> needUploads = msgDao.find(page, queryMap);
			
			String url = CommUtils.getRequestUri(mContext) + "/dynamic/sync";
			for (Dynamic dynamic: needUploads) {
				//先上传一条文件， 如果成功，则上传记录， 如果失败，则 在catch中记录
				
				HashMap<String, Object> dataMap = new HashMap<String, Object>();
				dataMap.put("atime", dynamic.getAtime());
				dataMap.put("position", dynamic.getPosition());
				dataMap.put("id", dynamic.getId());
				
				
				CommUtils.postToServer(url, dataMap);
				
				CommUtils.uploadOneImg(dynamic, mContext);  //上传图片
				
				//上传完成之后， 修改其属性， 表示已经上传过了
				dynamic.setItemtype(Constant.DYNAMIC_ITEM_SERVER);
				msgDao.update(dynamic);
			}
			
			return result;
		}

		@Override
		protected void onPostExecute(Integer result) {
			super.onPostExecute(result);
		}
	}
	
	
	/**
	 * 刷新数据，从服务器上取
	 * 
	 */
	private class RefreshDataTask extends AsyncTask<Integer, Integer, Integer> {
		@Override
		protected Integer doInBackground(Integer... params) {
			int result = -1;
			List<Dynamic> newList = new ArrayList<Dynamic>();
			
			OrmSqliteDao<Dynamic> msgDao = new DynamicDao(mContext); 
			
			String lastTime = PrefUtils.getStr(mContext, CommUtils.LAST_KEY_DYNAMIC, "");
			Log.d(TAG, lastTime);
			//TODO query from server
			if (lastTime.length() == 0) {
				lastTime = "2014-01-01";
			}
			
			lastTime = lastTime.replace(" ", "%20");
			
			String requestUrl = CommUtils.getRequestUri(mContext) + "/" + "dynamic/list/" + lastTime;
			
			List<LinkedHashMap<String, Object>> list = CommUtils.getList(requestUrl);
			
			for (int i = 0; i < list.size(); i++) {
				Map<String, Object> map = list.get(i);
				
				ObjectMapper mapper = new ObjectMapper();
				Dynamic dynamic = mapper.convertValue(map, Dynamic.class);

				newList.add(dynamic);
				dynamic.setImgIds(dynamic.getId());
				
				msgDao.saveOrUpdate(dynamic);
				
				//按时间倒序取的数据，
				if (i==0) {
					PrefUtils.saveStr(mContext, CommUtils.LAST_KEY_DYNAMIC, dynamic.getAtime());
				}
				
				//下载图片
				
				
				//刷新
				
				
			}
			
			return result;
		}

		@Override
		protected void onPostExecute(Integer result) {
			Toast.makeText(mContext, "刷新完毕", Toast.LENGTH_SHORT);

			super.onPostExecute(result);
		}
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
