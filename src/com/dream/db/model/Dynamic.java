package com.dream.db.model;

import java.io.Serializable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "DYNAMIC")
public class Dynamic implements Serializable {

	private static final long serialVersionUID = -6415911291514672159L;

	public Dynamic() {
    }
	
	
    @DatabaseField(id = true)
    private String id;
    
    
    @DatabaseField
    private String position = "";
    
	@DatabaseField
    private String atime = "";
    
    
    @DatabaseField
    private String geopoint = "";

    
    
    @DatabaseField
    private String imgIds = "";

    
    @DatabaseField
    private String memo = "";


	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public String getPosition() {
		return position;
	}


	public void setPosition(String position) {
		this.position = position;
	}


	public String getAtime() {
		return atime;
	}


	public void setAtime(String atime) {
		this.atime = atime;
	}


	public String getGeopoint() {
		return geopoint;
	}


	public void setGeopoint(String geopoint) {
		this.geopoint = geopoint;
	}


	public static long getSerialversionuid() {
		return serialVersionUID;
	}


	public String getImgId() {
		return imgIds;
	}


	public void setImgId(String imgId) {
		this.imgIds = imgId;
	}
    
	public String getMemo() {
		return memo;
	}


	public void setMemo(String memo) {
		this.memo = memo;
	}
}
