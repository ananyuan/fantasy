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
    
    @DatabaseField
    private int itemtype = 0;

    /**
     * 记录类型
     * 0 从服务端下载到的数据 ， 不需要上传到服务端
     * 1 本地数据还未上传
     * 2 已经上传到服务端， 不需要从服务端获取
     * 
     * @return
     */
	public int getItemtype() {
		return itemtype;
	}


	public void setItemtype(int itemtype) {
		this.itemtype = itemtype;
	}


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
