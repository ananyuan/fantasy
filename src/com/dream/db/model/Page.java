package com.dream.db.model;

/**
 * 分页对象
 *
 */
public class Page {
    private int pageNo = 0; //页码，默认是第一页
    private int pageSize = 5; //每页显示的记录数，默认是15
    private int totalRecord; //总记录数
    private int totalPage; //总页数
    
    private String order; //排序 字段
    
    private String sort = "DESC";  //默认倒序
    
	public int getPageNo() {
		return pageNo;
	}
	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	public int getTotalRecord() {
		return totalRecord;
	}
	public void setTotalRecord(int totalRecord) {
		this.totalRecord = totalRecord;
	}
	public int getTotalPage() {
		return totalPage;
	}
	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}
	public String getOrder() {
		return order;
	}
	public void setOrder(String order) {
		this.order = order;
	}
	public String getSort() {
		return sort;
	}
	public void setSort(String sort) {
		this.sort = sort;
	}
	
	
	
}
