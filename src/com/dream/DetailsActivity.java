package com.dream;

import java.io.IOException;
import java.util.ArrayList;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.PopupMenu;
import android.widget.ProgressBar;

import com.dream.db.OrmSqliteDao;
import com.dream.db.dao.ArticleDao;
import com.dream.db.model.Article;
import com.dream.util.CommUtils;
import com.dream.util.Constant;
import com.dream.util.ImgLoaderOptions;
import com.dream.view.TitleBarView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

@SuppressLint("JavascriptInterface")
public class DetailsActivity extends Activity {
	
	private static final String TAG = "DetailsActivity";
	
	protected ImageLoader imageLoader = ImageLoader.getInstance();
	
	private Context context;
	
	private Article article;
	
	private ProgressBar progressBar;
	
	private TitleBarView mTitleBarView;
	
	WebView webView;
	
	DisplayImageOptions options;
	
	private IWXAPI api;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = this;
		setContentView(R.layout.article_details);
		getData();
		initView();
		initWebView();
		
		api = WXAPIFactory.createWXAPI(this, Constant.APP_ID_WE_CHAT, false);
		api.registerApp(Constant.APP_ID_WE_CHAT);
		
		options = ImgLoaderOptions.getListOptions();
	}


	private void initView() {
		mTitleBarView=(TitleBarView) findViewById(R.id.title_bar);
		
		progressBar = (ProgressBar) findViewById(R.id.ss_htmlprogessbar);
		
		progressBar.setVisibility(View.VISIBLE);
		
		mTitleBarView.setCommonTitle(View.VISIBLE, View.VISIBLE, View.GONE, View.VISIBLE);
		
		mTitleBarView.setTitleText(article.getChanname());
		
		mTitleBarView.setBtnLeft("返回");
		mTitleBarView.setBtnLeftOnclickListener(new OnClickListener() {
			  @Override
	          public void onClick(View v) {
				  finish();
	          }
		});	
		
		
		mTitleBarView.setBtnRight("分享");
		mTitleBarView.setBtnRightOnclickListener(new OnClickListener() {
			  @Override
	          public void onClick(View v) {

	            	PopupMenu popupMenu = new PopupMenu(context, mTitleBarView.findViewById(R.id.title_btn_right));
	            	
	            	popupMenu.getMenuInflater().inflate(R.menu.detailshare, popupMenu.getMenu()); 
	                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() { 
	                     
	                    @Override 
	                    public boolean onMenuItemClick(MenuItem item) { 
	                    	if (item.getItemId() == R.id.share_wechat_timeline || item.getItemId() == R.id.share_wechat_session) {  // 
	        					WXWebpageObject webpage = new WXWebpageObject();
	        					webpage.webpageUrl = CommUtils.getRequestUri(context) + article.getLocalurl();
	        					WXMediaMessage msg = new WXMediaMessage(webpage);
	        					msg.title = article.getTitle();
	        					msg.description = article.getSummary();
	        					try {
	        						Bitmap bmp;
	        						if (article.getImgids().length() > 0) {
	        							String[] imgArray = article.getImgids().split(",");
		        						String firstImg = CommUtils.getRequestUri(context)  + "/file/" + imgArray[0];
		        						
		        						bmp = imageLoader.loadImageSync(firstImg, options);
	        						} else { //默认
	        							bmp = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);	
	        						}
	        						
	        						Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, 150, 150, true);
	        						bmp.recycle();
	        						msg.setThumbImage(thumbBmp);
	        					} catch (Exception e) {
	        						Log.e(TAG, e.getLocalizedMessage());
	        					}
	        					SendMessageToWX.Req req = new SendMessageToWX.Req();
	        					req.transaction = String.valueOf(System.currentTimeMillis());
	        					req.message = msg;
	        					
	        					if (item.getItemId() == R.id.share_wechat_session) {
	        						req.scene = SendMessageToWX.Req.WXSceneSession; 	
	        					} else {
	        						req.scene = SendMessageToWX.Req.WXSceneTimeline;
	        					}
	        					
	        					api.sendReq(req);
	                    	} else if (item.getItemId() == R.id.share_common) { //
		          				  Intent intent=new Intent(Intent.ACTION_SEND);   
		        				  intent.setType("text/plain");   
		        				  
		        				  intent.putExtra(Intent.EXTRA_SUBJECT, article.getTitle());   
		        				  intent.putExtra(Intent.EXTRA_TEXT, CommUtils.getRequestUri(context) + article.getLocalurl()); 
		        				  intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);   
		        				  startActivity(Intent.createChooser(intent, "分享"));  
	                    	}
	                        
	                        return false; 
	                    } 
	                }); 
	                popupMenu.show(); 
	          }
		});	
	}
	
	private void initWebView() {
		webView = (WebView)findViewById(R.id.wb_details);
		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
		if (!TextUtils.isEmpty(article.getId())) {
			WebSettings settings = webView.getSettings();
			settings.setJavaScriptEnabled(true);//设置可以运行JS脚本
//			settings.setTextZoom(120);//Sets the text zoom of the page in percent. The default is 100.
			settings.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
//			settings.setUseWideViewPort(true); //打开页面时， 自适应屏幕 
//			settings.setLoadWithOverviewMode(true);//打开页面时， 自适应屏幕 
			settings.setSupportZoom(false);// 用于设置webview放大
			settings.setBuiltInZoomControls(false);
			webView.setBackgroundResource(R.color.transparent);
			// 添加js交互接口类，并起别名 imagelistner
			webView.addJavascriptInterface(new JavascriptInterfaceClass(getApplicationContext()),"imagelistner");
			webView.setWebChromeClient(new MyWebChromeClient());
			webView.setWebViewClient(new MyWebViewClient());
			new ArticleDetailTask().execute(article.getId());
		}
	}
	
	
	
	private void getData() {
		article = (Article)getIntent().getSerializableExtra("article");
	}
	
	
	private class ArticleDetailTask extends AsyncTask<String, String,String>{
		@Override
		protected String doInBackground(String... id) {
			String articleid = id[0];
			
			OrmSqliteDao<Article> msgDao = new ArticleDao(context); 
			
			//先查询本地有没有内容了
			article = msgDao.findById(articleid);
			
			if (!TextUtils.isEmpty(article.getContent())) {
				String content = article.getContent();
				//将html中的本地的图片路径修改
				
				//content = content.replaceAll("src=\"/file/", "src=\"/");
				
				return content;
			}
			
			//本地没有内容，就去服务端查询
			String url = CommUtils.getRequestUri(context) + "/" + "article/id/" + articleid;
			
			String data = CommUtils.request(url);
			
			ObjectMapper mapper = new ObjectMapper();
			try {
				article = mapper.readValue(data, Article.class);
				
				String content = article.getContent();
				//将html中的本地的图片路径修改
				
				//content = content.replaceAll("src=\"/file/", "src=\"/");
				
				article.setContent(content);
				
				msgDao.saveOrUpdate(article);
			} catch (JsonParseException e) {
				Log.d(TAG, e.getMessage());
			} catch (JsonMappingException e) {
				Log.d(TAG, e.getMessage());
			} catch (IOException e) {
				Log.d(TAG, e.getMessage());
			}
			
			if (null != article) {
				return	article.getContent();
			}
			
			return "获取数据出错";
		}

		@Override
		protected void onPostExecute(String data) {
			String extString = "<style>img { max-width: 100%}</style>";  //设置图片最大100%宽度
			
			extString += "<h2>"+article.getTitle()+"</h2>";  //添加标题 
			
			String time = article.getAtime();
			if (time.length() > 16) {
				time = time.substring(0, 16);
			}
			
			extString += "<p>"+time+"</p>";
			
			//时间
			
			data = extString + data;
			
			//webView.loadDataWithBaseURL("file://" + CommUtils.getImageDir() + "Cache/", data, "text/html", "utf-8", null);
			
			webView.loadDataWithBaseURL(CommUtils.getRequestUri(context), data, "text/html", "utf-8", null);
		}
	}

	// 注入js函数监听
	private void addImageClickListner() {
		// 这段js函数的功能就是，遍历所有的img几点，并添加onclick函数，在还是执行的时候调用本地接口传递url过去
		webView.loadUrl("javascript:(function(){"
				+ "var objs = document.getElementsByTagName(\"img\");"
				+ "var imgurl=''; " + "for(var i=0;i<objs.length;i++)  " + "{"
				+ "imgurl+=objs[i].src+','; objs[i].index = i;"
				+ "    objs[i].onclick=function()  " + "    {  "
				+ "        window.imagelistner.openImage(imgurl, this.index + 1); "
				+ "    }  " + "}" 
				+ "})()");
	}
	
	// js通信接口
	public class JavascriptInterfaceClass {

		private Context context;

		public JavascriptInterfaceClass(Context context) {
			this.context = context;
		}

		@JavascriptInterface
		public void openImage(String img, int pageNum) {

			//
			String[] imgs = img.split(",");
			ArrayList<String> imgsUrl = new ArrayList<String>();
			for (String s : imgs) {
				imgsUrl.add(s);
				Log.i("图片的URL>>>>>>>>>>>>>>>>>>>>>>>", s);
			}
			Intent intent = new Intent();
			intent.putStringArrayListExtra("infos", imgsUrl);
			intent.putExtra("currentItem", pageNum);
			intent.setClass(context, ImageShowActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(intent);
		}
	}

	// 监听
	private class MyWebViewClient extends WebViewClient {
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			return super.shouldOverrideUrlLoading(view, url);
		}

		@Override
		public void onPageFinished(WebView view, String url) {
			view.getSettings().setJavaScriptEnabled(true);
			super.onPageFinished(view, url);
			// html加载完成之后，添加监听图片的点击js函数
			addImageClickListner();
			progressBar.setVisibility(View.GONE);
			webView.setVisibility(View.VISIBLE);
		}

		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			view.getSettings().setJavaScriptEnabled(true);
			super.onPageStarted(view, url, favicon);
		}

		@Override
		public void onReceivedError(WebView view, int errorCode,
				String description, String failingUrl) {
			progressBar.setVisibility(View.GONE);
			super.onReceivedError(view, errorCode, description, failingUrl);
		}
	}
	
	private class MyWebChromeClient extends WebChromeClient {
		@Override
		public void onProgressChanged(WebView view, int newProgress) {
			// TODO Auto-generated method stub
			if(newProgress != 100){
				progressBar.setProgress(newProgress);
			}
			super.onProgressChanged(view, newProgress);
		}
	}



}
