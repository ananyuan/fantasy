package com.dream.util;

public class ImageBean{
	/**
	 * 文件夹的第一张图片路径
	 */
	private String topImagePath = "";
	/**
	 * 文件夹名
	 */
	private String folderName; 
	/**
	 * 文件夹中的图片数
	 */
	private int imageCounts;
	
	public String getTopImagePath() {
		return topImagePath;
	}
	public void setTopImagePath(String topImagePath) {
		this.topImagePath = topImagePath;
	}
	public String getFolderName() {
		return folderName;
	}
	public void setFolderName(String folderName) {
		this.folderName = folderName;
	}
	public int getImageCounts() {
		return imageCounts;
	}
	public void setImageCounts(int imageCounts) {
		this.imageCounts = imageCounts;
	}
	
	/**
	 * /storage/emulated/0/QQBrowser/下载/a9ffcadfc67011a737c04e18335d8d48.jpg
	 * @return
	 */
	public String getFileDir() {
		String filePath = getTopImagePath();
		
		return filePath.substring(0, filePath.lastIndexOf("/"));
	}
	
}
