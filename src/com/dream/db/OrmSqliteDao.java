package com.dream.db;

import java.util.HashMap;
import java.util.List;

import com.dream.db.model.Page;

public interface OrmSqliteDao<T> {
	  public boolean save(T object);  
	    public boolean saveOrUpdate(T object);  
	    public List<T> find();  
	    public List<T> find(Page page);
	    public List<T> find(Page page, HashMap<String, Object> queryMap);
	    public boolean update(T object);  
	    public boolean delete(T object);  
	    public boolean deleteAll();  
	    public boolean executeSql(String sql);  
	    public T  findById(String id);  
}
