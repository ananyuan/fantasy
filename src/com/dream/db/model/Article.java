package com.dream.db.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "ARTICLE")
public class Article {

	public Article() {
    }
	
	
    @DatabaseField(id = true)
    private String ID;
    
    
    @DatabaseField
    private String TITLE;
    
    
    @DatabaseField
    private String DATETIME;
    
    
    @DatabaseField
    private String CONTENT;
    
    
    @DatabaseField
    private String CHANNEL;
    
    
    
    @DatabaseField
    private String IMGID;  //逗号分隔



	public String getID() {
		return ID;
	}



	public void setID(String iD) {
		ID = iD;
	}



	public String getTITLE() {
		return TITLE;
	}



	public void setTITLE(String tITLE) {
		TITLE = tITLE;
	}



	public String getDATETIME() {
		return DATETIME;
	}



	public void setDATETIME(String dATETIME) {
		DATETIME = dATETIME;
	}



	public String getCONTENT() {
		return CONTENT;
	}



	public void setCONTENT(String cONTENT) {
		CONTENT = cONTENT;
	}



	public String getCHANNEL() {
		return CHANNEL;
	}



	public void setCHANNEL(String cHANNEL) {
		CHANNEL = cHANNEL;
	}



	public String getIMGID() {
		return IMGID;
	}



	public void setIMGID(String iMGID) {
		IMGID = iMGID;
	}
    
    
    
}
