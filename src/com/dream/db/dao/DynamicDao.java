package com.dream.db.dao;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import android.accounts.Account;
import android.content.Context;
import android.text.TextUtils;

import com.dream.db.DatabaseHelper;
import com.dream.db.OrmSqliteDao;
import com.dream.db.model.Dynamic;
import com.dream.db.model.Page;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;

public class DynamicDao implements OrmSqliteDao<Dynamic> {
	private Dao<Dynamic, Integer> dynamicDao = null;

	public DynamicDao(Context context) {
		try {
			dynamicDao = DatabaseHelper.getHelper(context).getDynamicDao();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	

	@Override
	public boolean save(Dynamic object) {
		try {
			dynamicDao.create(object);
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean saveOrUpdate(Dynamic object) {
		try {
			dynamicDao.createOrUpdate(object);
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public List<Dynamic> find() {
		try {
			//List<Article> result = dynamicDao.queryForAll();
			
			QueryBuilder<Dynamic, Integer> query = dynamicDao.queryBuilder();
			
			query.orderBy("atime", false);
			
			return query.query();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public boolean update(Dynamic object) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean delete(Dynamic object) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean deleteAll() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean executeSql(String sql) {
		try {
			dynamicDao.executeRaw(sql);
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public Dynamic findById(String id) {
		List<Dynamic> dynamics;
		try {
			dynamics = dynamicDao.queryForEq("ID", id);
			
			if (null != dynamics && dynamics.size() > 0) {
				return dynamics.get(0);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		
		return null;
	}


	@Override
	public List<Dynamic> find(Page page) {
		try {
			QueryBuilder<Dynamic, Integer> query = dynamicDao.queryBuilder();
			
			query.limit(page.getPageSize());
			query.offset(page.getPageSize() * page.getPageNo());
			
//			Where where = query.where();
//			 // the name field must be equal to "foo"
//			 where.eq(Account.NAME_FIELD_NAME, "foo");
//			 // and
//			 where.and();
//			 // the password field must be equal to "_secret"
//			 where.eq(Account.PASSWORD_FIELD_NAME, "_secret");
//			
//			
//			query.setWhere(where);
			
			String order = page.getOrder();
			if (!TextUtils.isEmpty(order)) {
				if (page.getSort().equalsIgnoreCase("desc")) {
					query.orderBy(order, false);	
				} else {
					query.orderBy(order, true);
				}
			}
			
			return dynamicDao.query(query.prepare());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}


	@Override
	public List<Dynamic> find(Page page, HashMap<String, Object> queryMap) {
		try {
			QueryBuilder<Dynamic, Integer> query = dynamicDao.queryBuilder();
			
			query.limit(page.getPageSize());
			query.offset(page.getPageSize() * page.getPageNo());
			
			Where where = query.where();
			
			Iterator<String> iter = queryMap.keySet().iterator();
			int i = 0;
			
			while (iter.hasNext()) {
				String key = iter.next();
				Object value = queryMap.get(key);
				
				if (i > 0) {
					where.and();
				}
				
				where.eq(key, value);
				
				i++;
			}
			
			query.setWhere(where);
			
			String order = page.getOrder();
			if (!TextUtils.isEmpty(order)) {
				if (page.getSort().equalsIgnoreCase("desc")) {
					query.orderBy(order, false);	
				} else {
					query.orderBy(order, true);
				}
			}
			
			return dynamicDao.query(query.prepare());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

}
