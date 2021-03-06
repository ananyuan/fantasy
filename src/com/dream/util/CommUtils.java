package com.dream.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONException;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

import com.dream.db.model.Dynamic;

public class CommUtils {

	public static final String PREFS_NAME = "com.dream";
	
	public static final String DATA_FROM_PREFS = "prefs";
	
	public static final String LAST_KEY_ARTICLE = "article_last";
	
	public static final String LAST_KEY_MAIL = "mail_last";
	
	public static final String LAST_KEY_DYNAMIC = "dynamic_last";
	
	/** perf 中保存的服务器的地址 key */
	public static final String HOST_KEY = "host_address";
	
	/** perf 中保存的服务器的地址 value */
	public static final String HOST_KEY_VALUE = "182.92.131.36:8083";
	
	public static final String FORMAT_DATETIME = "yyyy-MM-dd HH:mm:ss";
	
	public static String getRequestUri(Context context) {
		String uri = "http://" + PrefUtils.getStr(context, HOST_KEY, HOST_KEY_VALUE);
		
		return uri;
	}
	
	/**
	 * 
	 * @return 当前时间
	 */
    public static String getDatetime() {
        Calendar calendar = Calendar.getInstance();
        
        SimpleDateFormat sdf = new SimpleDateFormat(FORMAT_DATETIME);
        return sdf.format(calendar.getTime());
    }
	
    
    /**
     * 
     * @return 保存图片的地址
     */
    public static String getImageDir() {
    	String fileDir = Environment.getExternalStorageDirectory() + "/fantasy/";
    	
    	
		File dirFile = new File(fileDir);
		if (!dirFile.exists()) {
			dirFile.mkdirs();
		}
		
		return fileDir;
    }
    
	/**
	 * 获取图片的本地存储路径。
	 * 
	 * @param imageUrl
	 *            图片的URL地址。
	 * @return 图片的本地存储路径。
	 */
	public static String getImagePath(String imageUrl) {
		int lastSlashIndex = imageUrl.lastIndexOf("/");
		String imageName = imageUrl.substring(lastSlashIndex + 1);
		String imagePath = CommUtils.getImageDir() + imageName;
		return imagePath;
	}
    
	/**
	 * 
	 * @param original 原始串
	 * @return md5串
	 */
	public static String getMd5(String original) {
		StringBuffer sb = new StringBuffer();
		MessageDigest md;
		try {
			md = MessageDigest.getInstance("MD5", "UTF-8");

			md.update(original.getBytes());
			byte[] digest = md.digest();

			for (byte b : digest) {
				sb.append(String.format("%02x", b & 0xff));
			}
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchProviderException e) {
			e.printStackTrace();
		}

		return sb.toString();
	}
    
	/**
	 * 判断手机是否有SD卡。
	 * 
	 * @return 有SD卡返回true，没有返回false。
	 */
    public static boolean hasSDCard() {
		return Environment.MEDIA_MOUNTED.equals(Environment
				.getExternalStorageState());
	}
    
    
	public static String request(String url) {
		InputStream inputStream = null;
        String result = "";
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpResponse httpResponse = httpclient.execute(new HttpGet(url));
            inputStream = httpResponse.getEntity().getContent();
            if(inputStream != null) {
            	result = convertInputStreamToString(inputStream);
            } else {
                result = "Did not work!";
            }
        } catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
        }
        
        return result;
	}
	
	/**
	 * 
	 * @param url 要获取数据的url
	 * @return List的数据
	 */
	public static List<LinkedHashMap<String, Object>> getList(String url) {
        String result = request(url);
        
		if (result.length() > 10) { //取到值了才去解析
			return getObject(result);	
		}
        return new ArrayList<LinkedHashMap<String, Object>>();
	}
	
	/**
	 * 
	 * @param inputStream InputStream
	 * @return 转成的字符串
	 * @throws IOException
	 */
	private static String convertInputStreamToString(InputStream inputStream)
			throws IOException {
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
		String line = "";
		String result = "";
		while ((line = bufferedReader.readLine()) != null) {
			result += line;
		}
		
		inputStream.close();
		return result;

	}
	
	/**
	 * 
	 * @param jsonStr json 的串
	 * @return json 转成的list
	 */
	private static List<LinkedHashMap<String, Object>> getObject(String jsonStr) {
		ObjectMapper objectMapper = new ObjectMapper();
		
		try {
			List<LinkedHashMap<String, Object>> list = objectMapper.readValue(jsonStr, List.class);
			
			return list;
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}

	/**
	 * 
	 * @param url
	 *            请求的地址
	 * @param dataMap
	 *            数据
	 */
	public static void postToServer(String url, HashMap<String, Object> dataMap) {
		DefaultHttpClient httpclient = new DefaultHttpClient();

		HttpParams params = httpclient.getParams();
		HttpConnectionParams.setConnectionTimeout(params, 10000);
		HttpConnectionParams.setSoTimeout(params, 10000);

		HttpPost httpost = new HttpPost(url);

		try {
			ObjectMapper mapper = new ObjectMapper();  
			String  json = mapper.writeValueAsString(dataMap);
			
			StringEntity stringEntity = new StringEntity(json, HTTP.UTF_8);
			stringEntity.setContentType("application/json");
			
			httpost.setEntity(stringEntity);

			
/*			List<NameValuePair> nvps = new ArrayList<NameValuePair>();
			
			Iterator<String> iter = dataMap.keySet().iterator();
	
			while (iter.hasNext()) {
				String key = iter.next();
				nvps.add(new BasicNameValuePair(key, (String) dataMap.get(key)));
			}
					
			UrlEncodedFormEntity urlEncodeEntiry = new UrlEncodedFormEntity(nvps, HTTP.UTF_8);
			httpost.setEntity(urlEncodeEntiry);
			urlEncodeEntiry.setContentType("application/json");*/
			
			HttpResponse response = httpclient.execute(httpost);

			int responseCode = response.getStatusLine().getStatusCode();

			if (responseCode == HttpURLConnection.HTTP_OK) {

			}
		} catch (ClientProtocolException e) {
			Log.e("ClientProtocolException", e.getLocalizedMessage());
		} catch (UnsupportedEncodingException e) {
			Log.e("UnsupportedEncodingException", e.getLocalizedMessage());
		} catch (IOException e) {
			Log.e("IOException", e.getLocalizedMessage());
		}
	}
	
	
	/**
	 * 
	 * @param saveObj 上传文件到服务器端
	 * @param context 上下文对象
	 */
	public static String uploadOneImg(Dynamic saveObj, Context context) {
		return uploadImage(getImageDir() + saveObj.getImgIds(), context, saveObj.getId());
	}
	
	/**
	 * 
	 * @param imgPath 图片路径
	 * @param context 上下文
	 * @return 上传文件返回的文件ID
	 */
	public static String uploadImage(String imgPath, Context context, String fileId) {
		InputStream is = null;
		try {
			File imgFile = new File(imgPath);
			is = new FileInputStream(imgFile);
	        Bitmap mBitmap = BitmapFactory.decodeStream(is); 
	        if (mBitmap.getWidth() > Constant.CUT_IMG_WIDTH) {
		        //将图缩小，返回小图的路径
	        	imgPath = ImageUtils.CutPictureByWidth(mBitmap, Constant.CUT_IMG_WIDTH);
	        }

	        return uploadOneFile(imgPath, context, fileId);
		} catch (Exception e) {
			Log.e("uploadImage Exception", e.getLocalizedMessage());
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
				Log.e("uploadImage Exception", e.getLocalizedMessage());
			}
		}
		
		return "";
	}
	
	
	/**
	 * 
	 * @param localFilePath 本地文件
	 * @param context 上下文
	 * @return 文件上传完之后 的  ID
	 * @throws ClientProtocolException
	 * @throws IOException
	 * @throws JSONException
	 */
	public static String uploadOneFile(String localFilePath, Context context, String fileId)
			throws ClientProtocolException, IOException, JSONException {
		HttpClient httpclient = new DefaultHttpClient();
		httpclient.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);

		String upLoadServerUri = getRequestUri(context) + "/file/upload";
		
		if (null != fileId && fileId.length() > 0) { //如果给定了fileId 则用这个ID上传
			upLoadServerUri += "/" + fileId;
		}
		
		HttpPost httppost = new HttpPost(upLoadServerUri);
		File file = new File(localFilePath);
	    
		MultipartEntity mpEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE); // 文件传输
		ContentBody cbFile = new FileBody(file);
		mpEntity.addPart("userfile", cbFile); // <input type="file" name="userfile" /> 对应的

		httppost.setEntity(mpEntity);
		System.out.println("executing request " + httppost.getRequestLine());
		


		HttpResponse response = httpclient.execute(httppost);
		HttpEntity resEntity = response.getEntity();

		String restr = "";
		if (resEntity != null) {
			restr = EntityUtils.toString(resEntity, "utf-8");
		}
		if (resEntity != null) {
			resEntity.consumeContent();
		}

		httpclient.getConnectionManager().shutdown();
		return restr;
	}
	
	/**
	 * 
	 * @param sourcePath
	 * @param fileId
	 */
	public static void copyImgFile(String sourcePath, String fileId) {
		String newPath = getImageDir() + fileId;
		try {
			int bytesum = 0;
			int byteread = 0;
			File oldfile = new File(sourcePath);
			if (oldfile.exists()) {
				InputStream inStream = new FileInputStream(sourcePath);
				FileOutputStream fs = new FileOutputStream(newPath);
				byte[] buffer = new byte[1444];
				int length;
				while ((byteread = inStream.read(buffer)) != -1) {
					bytesum += byteread;
					System.out.println(bytesum);
					fs.write(buffer, 0, byteread);
				}
				inStream.close();
			}
		} catch (Exception e) {
			System.out.println("复制单个文件操作出错");
			e.printStackTrace();

		}
	}
	
}
