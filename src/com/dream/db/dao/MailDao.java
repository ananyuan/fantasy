package com.dream.db.dao;

import java.sql.SQLException;
import java.util.List;

import android.content.Context;
import android.text.TextUtils;

import com.dream.db.DatabaseHelper;
import com.dream.db.OrmSqliteDao;
import com.dream.db.model.MailBean;
import com.dream.db.model.Page;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;

public class MailDao implements OrmSqliteDao<MailBean> {

	private Dao<MailBean, Integer> mailDao = null;

	public MailDao(Context context) {
		try {
			mailDao = DatabaseHelper.getHelper(context).getMailDao();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}	
	
	@Override
	public boolean save(MailBean object) {
		try {
			mailDao.create(object);
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean saveOrUpdate(MailBean object) {
		try {
			mailDao.createOrUpdate(object);
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public List<MailBean> find() {
		try {
			QueryBuilder<MailBean, Integer> query = mailDao.queryBuilder();
			
			query.orderBy("sendTime", false);
			
			return query.query();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<MailBean> find(Page page) {
		try {
			QueryBuilder<MailBean, Integer> query = mailDao.queryBuilder();
			
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
			
			return mailDao.query(query.prepare());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public boolean update(MailBean object) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean delete(MailBean object) {
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
			mailDao.executeRaw(sql);
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public MailBean findById(String id) {
		List<MailBean> mails;
		try {
			mails = mailDao.queryForEq("ID", id);
			
			if (null != mails && mails.size() > 0) {
				return mails.get(0);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		
		return null;
	}

}
