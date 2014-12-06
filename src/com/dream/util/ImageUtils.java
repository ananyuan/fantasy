package com.dream.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.graphics.Bitmap;
import android.graphics.Matrix;

public class ImageUtils {

	/**
	 * 
	 * @param bitmap 图片对象
	 * @param width
	 * @param height
	 * @return 保存的新图片的 路径
	 */
	public static String CutPicture(Bitmap bitmap, int width, int height) {
		float scaleWidth = ((float) width) / bitmap.getWidth();
		float scaleHeight = ((float) height) / bitmap.getHeight();
		if (width == 0) {
			scaleWidth = scaleHeight;
		}
		
		if (height == 0) {
			scaleHeight = scaleWidth;
		}
		
		Matrix matrix = new Matrix();
		matrix.postScale(scaleWidth, scaleHeight);
		Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
		
		
		return saveBitmap(resizedBitmap);
	}
	
	/**
	 * 
	 * @param bitmap 图片对象
	 * @param width
	 * @return  保存的新图片的 路径
	 */
	public static String CutPictureByWidth(Bitmap bitmap, int width) {
		return CutPicture(bitmap, width, 0);
	}

	/**
	 * 
	 * @param bitmap 图片对象
	 * @param height
	 * @return 保存的新图片的 路径
	 */
	public static String CutPictureByHeight(Bitmap bitmap, int height) {
		return CutPicture(bitmap, 0, height);
	}
	
	/**
	 * 
	 * @param bitmap
	 * @return 保存的新图片的 路径
	 */
	public static String saveBitmap(Bitmap bitmap) {
		String filePath = CommUtils.getImageDir() + System.currentTimeMillis() + ".jpg";
		
		File file = new File(filePath);
		if (file.exists()) {
			file.delete();
		}
		try {
			file.createNewFile();
			FileOutputStream out = new FileOutputStream(file);
			bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
			out.flush();
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return filePath;
	}
}
