package com.dream.view;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.dream.ImageShowActivity;
import com.dream.R;
import com.dream.db.OrmSqliteDao;
import com.dream.db.dao.DynamicDao;
import com.dream.db.model.Dynamic;
import com.dream.db.model.Page;
import com.dream.image.ImageLoader;
import com.dream.util.CommUtils;

public class ImageScollView extends ScrollView implements OnTouchListener {

	private Context mContext;
	
	/**
	 * 每页要加载的图片数量
	 */
	public static final int TEXT_HEIGHT = 36;

	/**
	 * 记录当前已加载到第几页
	 */
	private Page page = new Page();

	/**
	 * 每一列的宽度
	 */
	private int columnWidth;

	/**
	 * 当前第一列的高度
	 */
	private int firstColumnHeight;

	/**
	 * 当前第二列的高度
	 */
	private int secondColumnHeight;

	/**
	 * 是否已加载过一次layout，这里onLayout中的初始化只需加载一次
	 */
	private boolean loadOnce;

	/**
	 * 对图片进行管理的工具类
	 */
	private ImageLoader imageLoader;

	/**
	 * 第一列的布局
	 */
	private LinearLayout firstColumn;

	/**
	 * 第二列的布局
	 */
	private LinearLayout secondColumn;

	/**
	 * 记录所有正在下载或等待下载的任务。
	 */
	private static Set<LoadImageTask> taskCollection;

	/**
	 * 直接子布局。
	 */
	private static View scrollLayout;

	/**
	 * 布局的高度。
	 */
	private static int scrollViewHeight;

	/**
	 * 记录上垂直方向的滚动距离。
	 */
	private static int lastScrollY = -1;

	/**
	 * 记录所有界面上的图片，用以可以随时控制对图片的释放。
	 */
	private List<ImageView> imageViewList = new ArrayList<ImageView>();
	private ArrayList<String> imageUrlList = new ArrayList<String>();

	/**
	 * 刷新 ， 将之前的布局及文件都清除，重新获取数据 ， 暂时在发布之后的返回用到
	 */
	public void refresh() {
		imageUrlList.clear();
		imageViewList.clear();
		if (null != firstColumn && null != secondColumn) {
			firstColumn.removeAllViewsInLayout();
			secondColumn.removeAllViewsInLayout();
		}
		scrollViewHeight = getHeight();
		firstColumnHeight = 0;
		secondColumnHeight = 0;
		lastScrollY = -1;
		page = new Page();		
		
		loadMoreImages();
	}
	
	
	/**
	 * 在Handler中进行图片可见性检查的判断，以及加载更多图片的操作。
	 */
	private static Handler handler = new Handler() {

		public void handleMessage(android.os.Message msg) {
			ImageScollView myScrollView = (ImageScollView) msg.obj;
			int scrollY = myScrollView.getScrollY();
			// 如果当前的滚动位置和上次相同，表示已停止滚动
			if (scrollY == lastScrollY) {
				// 当滚动的最底部，并且当前没有正在下载的任务时，开始加载下一页的图片
				if (scrollViewHeight + scrollY >= scrollLayout.getHeight()
						&& taskCollection.isEmpty()) {
					myScrollView.loadMoreImages();
				}
				myScrollView.checkVisibility();
			} else {
				lastScrollY = scrollY;
				Message message = new Message();
				message.obj = myScrollView;
				// 5毫秒后再次对滚动位置进行判断
				handler.sendMessageDelayed(message, 5);
			}
		};

	};

	/**
	 * MyScrollView的构造函数。
	 * 
	 * @param context
	 * @param attrs
	 */
	public ImageScollView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		imageLoader = ImageLoader.getInstance();
		taskCollection = new HashSet<LoadImageTask>();
		setOnTouchListener(this);
	}

	/**
	 * 进行一些关键性的初始化操作，获取ScrollView的高度，以及得到第一列的宽度值。并在这里开始加载第一页的图片。
	 */
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		super.onLayout(changed, l, t, r, b);
		if (changed && !loadOnce) {
			scrollViewHeight = getHeight();
			scrollLayout = getChildAt(0);
			firstColumn = (LinearLayout) findViewById(R.id.first_column);
			secondColumn = (LinearLayout) findViewById(R.id.second_column);
			columnWidth = firstColumn.getWidth();
			loadOnce = true;
			loadMoreImages();
		}
	}

	/**
	 * 监听用户的触屏事件，如果用户手指离开屏幕则开始进行滚动检测。
	 */
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_UP) {
			Message message = new Message();
			message.obj = this;
			handler.sendMessageDelayed(message, 5);
		}
		return false;
	}

	/**
	 * 开始加载下一页的图片，每张图片都会开启一个异步线程去下载。
	 */
	public void loadMoreImages() {
		if (CommUtils.hasSDCard()) {
			new GetImageDataTask().execute(); //获取数据
		} else {
			Toast.makeText(getContext(), "未发现SD卡", Toast.LENGTH_SHORT).show();
		}
	}

	
	/**
	 * 加载更多
	 *
	 */
	private class GetImageDataTask extends AsyncTask<Integer, Integer, Integer> {

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
			if (result > 0) { //取到数据了
				for (Dynamic dynamic: oldList) {
		        	String fullImageUri = CommUtils.getRequestUri(mContext) + "/file/" + dynamic.getImgId();
		        	
		        	dynamic.setImgId(fullImageUri);
		        	
		        	LoadImageTask task = new LoadImageTask();
					taskCollection.add(task);
					task.execute(dynamic.getImgId(), dynamic.getPosition() + "~~" + dynamic.getAtime());
				}
			}
			page.setPageNo(page.getPageNo() + 1);
			
			super.onPostExecute(result);
		}
	}

	/**
	 * 遍历imageViewList中的每张图片，对图片的可见性进行检查，如果图片已经离开屏幕可见范围，则将图片替换成一张空图。
	 */
	public void checkVisibility() {
		for (int i = 0; i < imageViewList.size(); i++) {
			ImageView imageView = imageViewList.get(i);
			int borderTop = (Integer) imageView.getTag(R.string.border_top);
			int borderBottom = (Integer) imageView
					.getTag(R.string.border_bottom);
			if (borderBottom > getScrollY()
					&& borderTop < getScrollY() + scrollViewHeight) {
				String imageUrl = (String) imageView.getTag(R.string.image_url);
				Bitmap bitmap = imageLoader.getBitmapFromMemoryCache(imageUrl);
				if (bitmap != null) {
					imageView.setImageBitmap(bitmap);
				} else {
//					LoadImageTask task = new LoadImageTask(imageView);
					bitmap = imageLoader.loadImage(imageUrl, columnWidth);
					imageView.setImageBitmap(bitmap);
				}
			} else {
				imageView.setImageResource(R.drawable.pictures_no);
			}
		}
	}

	/**
	 * 异步下载图片的任务。
	 * 
	 * @author guolin
	 */
	class LoadImageTask extends AsyncTask<String, String, Bitmap> {

		/**
		 * 图片的URL地址
		 */
		private String mImageUrl;
		
		private String address = "";
		
		private String atime = "";

		/**
		 * 可重复使用的ImageView
		 */
		private ImageView mImageView;

		public LoadImageTask() {
		}

		/**
		 * 将可重复使用的ImageView传入
		 * 
		 * @param imageView
		 */
		public LoadImageTask(ImageView imageView) {
			mImageView = imageView;
		}

		@Override
		protected Bitmap doInBackground(String... params) {
			mImageUrl = params[0];
			String[] addressTime = params[1].split("~~");
			address = addressTime[0];
			atime = addressTime[1];
			Bitmap imageBitmap = imageLoader
					.getBitmapFromMemoryCache(mImageUrl);
			if (imageBitmap == null) {
				imageBitmap = imageLoader.loadImage(mImageUrl, columnWidth);
			}
			return imageBitmap;
		}

		@Override
		protected void onPostExecute(Bitmap bitmap) {
			if (bitmap != null) {
				double ratio = bitmap.getWidth() / (columnWidth * 1.0);
				int scaledHeight = (int) (bitmap.getHeight() / ratio);
				addImage(bitmap, columnWidth, scaledHeight);
			}
			taskCollection.remove(this);
		}

		/**
		 * 向ImageView中添加一张图片
		 * 
		 * @param bitmap
		 *            待添加的图片
		 * @param imageWidth
		 *            图片的宽度
		 * @param imageHeight
		 *            图片的高度
		 */
		private void addImage(Bitmap bitmap, int imageWidth, int imageHeight) {
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
					imageWidth, imageHeight);
			if (mImageView != null) {
				mImageView.setImageBitmap(bitmap);
			} else {
				ImageView imageView = new ImageView(getContext());
				imageView.setLayoutParams(params);
				imageView.setImageBitmap(bitmap);
				imageView.setScaleType(ScaleType.FIT_XY);
				imageView.setPadding(5, 5, 5, 5);
				imageView.setTag(R.string.image_url, mImageUrl);
				
		        //定义子View中两个元素的布局 
		        ViewGroup.LayoutParams vlp = new ViewGroup.LayoutParams( 
		                ViewGroup.LayoutParams.WRAP_CONTENT, 
		                ViewGroup.LayoutParams.WRAP_CONTENT); 
		        ViewGroup.LayoutParams vlp2 = new ViewGroup.LayoutParams( 
		                ViewGroup.LayoutParams.WRAP_CONTENT, 
		                ViewGroup.LayoutParams.WRAP_CONTENT); 
		         
		        TextView addressView = new TextView(mContext); 
		        TextView timeView = new TextView(mContext); 
		        addressView.setLayoutParams(vlp);//设置TextView的布局 
		        timeView.setLayoutParams(vlp2); 
		        addressView.setText(address); 
		        addressView.setHeight(TEXT_HEIGHT);
		        timeView.setText(atime);
		        timeView.setHeight(TEXT_HEIGHT);
				
		        LinearLayout layout = findColumnToAdd(imageView, imageHeight + TEXT_HEIGHT * 2);
		        layout.addView(imageView);
		        layout.addView(addressView);
		        layout.addView(timeView);
		        
				imageViewList.add(imageView);
				imageUrlList.add(mImageUrl);
				
				final int currentSize = imageUrlList.size();
				imageView.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						// imageView 添加单击事件
						Intent intent = new Intent();
						intent.putStringArrayListExtra("infos", imageUrlList);
						intent.putExtra("currentItem", currentSize);
						
						intent.setClass(mContext, ImageShowActivity.class);
						intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						mContext.startActivity(intent);
					}
				});
			}
		}

		/**
		 * 找到此时应该添加图片的一列。原则就是对各列的高度进行判断，当前高度最小的一列就是应该添加的一列。
		 * 
		 * @param imageView
		 * @param imageHeight
		 * @return 应该添加图片的一列
		 */
		private LinearLayout findColumnToAdd(ImageView imageView,
				int imageHeight) {
			if (firstColumnHeight <= secondColumnHeight) {
				imageView.setTag(R.string.border_top, firstColumnHeight);
				firstColumnHeight += imageHeight;
				imageView.setTag(R.string.border_bottom, firstColumnHeight);
				
				return firstColumn;
			} else {
				imageView.setTag(R.string.border_top, secondColumnHeight);
				secondColumnHeight += imageHeight;
				imageView.setTag(R.string.border_bottom, secondColumnHeight);
				
				return secondColumn;
			}
		}

	}

}