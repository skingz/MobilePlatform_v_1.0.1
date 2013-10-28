package com.leading.baselibrary.database;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.util.Log;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.UpdateBuilder;

public abstract class LzDAO<T> {

	private Class<T> entityClass;

	private OrmLiteSqlHelper db;

	private Dao<T, Long> dao;

	
	@SuppressWarnings("unchecked")
	public LzDAO(Context context,int configXMLResId) {
		Type genType = getClass().getGenericSuperclass();
		Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
		entityClass = (Class<T>) params[0];
		db = new OrmLiteSqlHelper(context,configXMLResId);
		try {
			dao = db.getDao(entityClass);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public Dao<T, Long> getDao(){
		return dao;
	}

	public int count() {
		try {
			return (int) dao.countOf();
		} catch (SQLException e) {
			Log.e("DbHelper", "count", e);
		}
		return 0;
	}

	public int create(T po) {
		try {
			return dao.create(po);
		} catch (SQLException e) {
			Log.e("DbHelper", "create", e);
		}
		return -1;
	}

	public int createOrUpdate(T po) {
		try {
			return dao.createOrUpdate(po).getNumLinesChanged();
		} catch (SQLException e) {
			Log.e("DbHelper", "createOrUpdate", e);
		}
		return -1;
	}

	public int remove(T po) {
		try {
			return dao.delete(po);
		} catch (SQLException e) {
			Log.e("DbHelper", "remove", e);
		}
		return -1;
	}

	public int remove(Collection<T> po) {
		try {
			return dao.delete(po);
		} catch (SQLException e) {
			Log.e("DbHelper", "remove", e);
		}
		return -1;
	}

	/**
	 * 根据特定条件更新特定字段
	 * 
	 * @param c
	 * @param values
	 * @param columnName
	 *            where字段
	 * @param value
	 *            where值
	 * @return
	 */
	public int update(HashMap<String, Object> values, String columnName,
			Object value) {
		try {
			UpdateBuilder<T, Long> updateBuilder = dao.updateBuilder();
			updateBuilder.where().eq(columnName, value);
			for (String key : values.keySet()) {
				updateBuilder.updateColumnValue(key, values.get(key));
			}
			return updateBuilder.update();
		} catch (SQLException e) {
			Log.e("DbHelper", "update", e);
		}
		return -1;
	}

	public int update(T po) {
		try {
			return dao.update(po);
		} catch (SQLException e) {
			Log.e("DbHelper", "update", e);
		}
		return -1;
	}

	public List<T> queryForAll() {
		try {
			return dao.queryForAll();
		} catch (SQLException e) {
			Log.e("DbHelper", "queryForAll", e);
		}
		return new ArrayList<T>();
	}

	public List<T> queryForAllOrderby(String orderFieldName) {
		return queryForAllOrderby(orderFieldName, false);
	}

	public List<T> queryForAllOrderby(String orderFieldName, boolean asc) {
		try {
			QueryBuilder<T, Long> query = dao.queryBuilder();
			query.orderBy(orderFieldName, asc);
			return dao.query(query.prepare());
		} catch (SQLException e) {
			Log.e("DbHelper", "queryForAllOrderby", e);
		}
		return null;
	}

	public List<T> queryForAllOrderby(String fieldName, Object value,
			String orderFieldName, boolean asc) {
		try {
			QueryBuilder<T, Long> query = dao.queryBuilder();
			query.orderBy(orderFieldName, asc);
			query.where().eq(fieldName, value);
			return dao.query(query.prepare());
		} catch (SQLException e) {
			Log.e("DbHelper", "queryForAllOrderby", e);
		}
		return null;
	}

	public List<T> queryForAll(String fieldName, Object value) {
		try {
			return dao.queryForEq(fieldName, value);
		} catch (SQLException e) {
			Log.e("DbHelper", "queryForAll", e);
		}
		return null;
	}

	/**  */
	public T query(String fieldName, Object value) {
		try {
			List<T> result = dao.queryForEq(fieldName, value);
			if (result != null && result.size() > 0)
				return result.get(0);
		} catch (SQLException e) {
			Log.e("DbHelper", "count", e);
		}
		return null;
	}

	public T query(long id) {
		try {
			return dao.queryForId(id);
		} catch (SQLException e) {
			Log.e("DbHelper", "query", e);
		}
		return null;
	}
	
	/**
	 * 关闭数据库连接.
	 * @author JianTao.tu
	 */
	public void dbClose(){
		db.close();
	}
}
