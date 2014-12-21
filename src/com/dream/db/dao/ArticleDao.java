package com.dream.db.dao;

import java.sql.SQLException;
import java.util.List;

import android.content.Context;
import android.text.TextUtils;

import com.dream.db.DatabaseHelper;
import com.dream.db.OrmSqliteDao;
import com.dream.db.model.Article;
import com.dream.db.model.Page;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;


public class ArticleDao implements OrmSqliteDao<Article> {
	private Dao<Article, Integer> articleDao = null;

	public ArticleDao(Context context) {
		try {
			articleDao = DatabaseHelper.getHelper(context).getArticleDao();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	

	@Override
	public boolean save(Article object) {
		try {
			articleDao.create(object);
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean saveOrUpdate(Article object) {
		try {
			articleDao.createOrUpdate(object);
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public List<Article> find() {
		try {
			//List<Article> result = articleDao.queryForAll();
			
			QueryBuilder<Article, Integer> query = articleDao.queryBuilder();
			
			query.orderBy("atime", false);
			
			return query.query();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	

	@Override
	public List<Article> find(Page page) {
		try {
			QueryBuilder<Article, Integer> query = articleDao.queryBuilder();
			
			query.limit(page.getPageSize());
			query.offset(page.getPageSize() * page.getPageNo());
			
			String order = page.getOrder();
			if (!TextUtils.isEmpty(order)) {
				if (page.getSort().equalsIgnoreCase("desc")) {
					query.orderBy(order, false);	
				} else {
					query.orderBy(order, true);
				}
			}
			
			return articleDao.query(query.prepare());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}	
	

	@Override
	public boolean update(Article object) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean delete(Article object) {
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
			articleDao.executeRaw(sql);
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public Article findById(String id) {
		List<Article> articles;
		try {
			articles = articleDao.queryForEq("ID", id);
			
			if (null != articles && articles.size() > 0) {
				return articles.get(0);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		
		return null;
	}

}
