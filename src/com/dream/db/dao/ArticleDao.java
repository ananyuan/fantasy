package com.dream.db.dao;

import java.sql.SQLException;
import java.util.List;

import android.content.Context;

import com.dream.db.DatabaseHelper;
import com.dream.db.OrmSqliteDao;
import com.dream.db.model.Article;
import com.j256.ormlite.dao.Dao;


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
			List<Article> result = articleDao.queryForAll();
			return result;
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
