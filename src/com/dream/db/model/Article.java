package com.dream.db.model;

import java.io.Serializable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "ARTICLE")
public class Article implements Serializable {


	private static final long serialVersionUID = -2744215917402309530L;



	public Article() {
    }
	
	
    @DatabaseField(id = true)
    private String id;
    
    
    @DatabaseField
    private String title;
    
	@DatabaseField
    private String content;
    
    
    @DatabaseField
    private String chanId;
    
    
    @DatabaseField
    private String atime;
    
    
    
    @DatabaseField
    private String summary;


    @DatabaseField
    private String localurl;
    
    
    private String imgUrls = "";
    
    
    public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public String getTitle() {
		return title;
	}


	public void setTitle(String title) {
		this.title = title;
	}


	public String getContent() {
		return content;
	}


	public void setContent(String content) {
		this.content = content;
	}


	public String getChanId() {
		return chanId;
	}


	public void setChanId(String chanId) {
		this.chanId = chanId;
	}


	public String getAtime() {
		return atime;
	}


	public void setAtime(String atime) {
		this.atime = atime;
	}


	public String getSummary() {
		return summary;
	}


	public void setSummary(String summary) {
		this.summary = summary;
	}


	public String getLocalurl() {
		return localurl;
	}


	public void setLocalurl(String localurl) {
		this.localurl = localurl;
	}


	public String getImgUrls() {
		return imgUrls;
	}


	public void setImgUrls(String imgUrls) {
		this.imgUrls = imgUrls;
	}
}
