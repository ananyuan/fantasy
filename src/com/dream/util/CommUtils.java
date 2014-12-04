package com.dream.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.util.EntityUtils;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONException;

import android.content.Context;
import android.util.Log;

import com.dream.db.model.Dynamic;

public class CommUtils {

	public static final String PREFS_NAME = "com.dream";
	
	public static final String DATA_FROM_PREFS = "prefs";
	
	public static final String LAST_KEY_ARTICLE = "article_last";
	
	/** perf 中保存的服务器的地址 key */
	public static final String HOST_KEY = "host_address";
	
	/** perf 中保存的服务器的地址 value */
	public static final String HOST_KEY_VALUE = "10.198.1.48:8083";
	
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
	 * @param saveObj 上传文件到服务器端
	 * @param context 上下文对象
	 */
	public static String uploadOneImg(Dynamic saveObj, Context context) {
		try {
			return uploadOneFile(saveObj.getImgId(), context);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
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
	public static String uploadOneFile(String localFilePath, Context context)
			throws ClientProtocolException, IOException, JSONException {
		HttpClient httpclient = new DefaultHttpClient();
		httpclient.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);

		String upLoadServerUri = getRequestUri(context) + "/file/upload";
		HttpPost httppost = new HttpPost(upLoadServerUri);
		File file = new File(localFilePath);

		MultipartEntity mpEntity = new MultipartEntity(); // 文件传输
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
}
