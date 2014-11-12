package com.dream.db;

import java.util.List;

public interface OrmSqliteDao<T> {
	  public boolean save(T object);  
	    public boolean saveOrUpdate(T object);  
	    public List<T> find();  
	    public boolean update(T object);  
	    public boolean delete(T object);  
	    public boolean deleteAll();  
	    public boolean executeSql(String sql);  
	    public T  findById(String id);  
}
