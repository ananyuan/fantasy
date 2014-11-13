package com.dream.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class PrefUtils {

	
	public static SharedPreferences getPref(Context context) {
		return context.getSharedPreferences(CommUtils.PREFS_NAME, Context.MODE_PRIVATE);
	}
	
	
	public static String getStr(Context context, String key, String defaultValue) {
		SharedPreferences sharePref = getPref(context);
		
		return sharePref.getString(key, defaultValue);
	}
	
	
	public static int getInt(Context context, String key) {
		SharedPreferences sharePref = getPref(context);
		
		return sharePref.getInt(key, 0);
	}
	
	
	public static boolean getBoolean(Context context, String key) {
		SharedPreferences sharePref = getPref(context);
		
		return sharePref.getBoolean(key, true);
	}
	
	public static void saveStr(Context context, String key, String value) {
		SharedPreferences sharePref = getPref(context);
		
		Editor editor = sharePref.edit();
		
		editor.putString(key, value);
		
		editor.commit();
	}
	
	public static void saveInt(Context context, String key, int value) {
		SharedPreferences sharePref = getPref(context);
		
		Editor editor = sharePref.edit();
		
		editor.putInt(key, value);
		
		editor.commit();
	}
	
	public static void saveBoolean(Context context, String key, boolean value) {
		SharedPreferences sharePref = getPref(context);
		
		Editor editor = sharePref.edit();
		
		editor.putBoolean(key, value);
		
		editor.commit();
	}
	
}
