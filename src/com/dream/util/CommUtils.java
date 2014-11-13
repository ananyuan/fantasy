package com.dream.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import android.content.Context;
import android.util.Log;

public class CommUtils {

	public static final String PREFS_NAME = "com.dream";
	
	public static final String LAST_KEY_ARTICLE = "article_last";
	
	/** perf 中保存的服务器的地址 key */
	public static final String HOST_KEY = "host_address";
	
	/** perf 中保存的服务器的地址 value */
	public static final String HOST_KEY_VALUE = "172.16.0.114:8083";
	
	
	public static String getRequestUri(Context context) {
		String uri = "http://" + PrefUtils.getStr(context, HOST_KEY, HOST_KEY_VALUE);
		
		return uri;
	}
	
	/**
	 * 
	 * @param url 要获取数据的url
	 * @return List的数据
	 */
	public static List<LinkedHashMap<String, Object>> getList(String url) {
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
}
