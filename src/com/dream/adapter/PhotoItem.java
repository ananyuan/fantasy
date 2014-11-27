package com.dream.adapter;


public class PhotoItem {
	private String thumbnailUri;
	private String fullImageUri;

	public PhotoItem(String thumbnailUri, String fullImageUri) {
		this.thumbnailUri = thumbnailUri;
		this.fullImageUri = fullImageUri;
	}

	/**
	 * Getters and setters
	 */
	public String getThumbnailUri() {
		return thumbnailUri;
	}

	public void setThumbnailUri(String thumbnailUri) {
		this.thumbnailUri = thumbnailUri;
	}

	public String getFullImageUri() {
		return fullImageUri;
	}

	public void setFullImageUri(String fullImageUri) {
		this.fullImageUri = fullImageUri;
	}
}