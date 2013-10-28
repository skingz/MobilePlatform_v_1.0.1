package com.leading.baselibrary.database;

import java.sql.SQLException;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.leading.baselibrary.entity.DBEntity;
import com.leading.baselibrary.util.XmlReader;

public class OrmLiteSqlHelper extends OrmLiteSqliteOpenHelper {
	private static DBEntity dbEntity = null;
//	private Dao<UsersEntity, Integer> userDao = null; 
	static {
		dbEntity = new DBEntity();
	}
	public static String getDatabaseName(Context context,int configXMLResId) {
		if (dbEntity != null)
			dbEntity = new XmlReader().getDBEntity(context.getResources(),configXMLResId);
		return dbEntity.getDataBaseName();
	}
	
	public OrmLiteSqlHelper(Context context,int configXMLResId) {
		super(context, OrmLiteSqlHelper.getDatabaseName(context,configXMLResId), null, dbEntity
				.getDataBaseVersion());
	}

	/**
	 * 创建数据库
	 * @author JianTao.tu
	 * @param db
	 * @param connectionSource
	 */
	@Override
	public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
		try {
			for (String strClass : dbEntity.getDataBaseTable()) {
				Class<?> c = null;
				c = Class.forName(strClass);
				TableUtils.createTable(connectionSource, c);
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			Log.e(OrmLiteSqlHelper.class.getName(), "创建数据库失败", e);
			e.printStackTrace();
		}

	}

	/**
	 * 更新数据库
	 * @author JianTao.tu
	 * @param db
	 * @param connectionSource
	 * @param arg2
	 * @param arg3
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource,
			int arg2, int arg3) {
		try {
			for (String strClass : dbEntity.getDataBaseTable()) {
				Class<?> c = null;
				c = Class.forName(strClass);
				TableUtils.dropTable(connectionSource, c, true);
			}
			onCreate(db, connectionSource);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			Log.e(OrmLiteSqlHelper.class.getName(), "更新数据库失败", e);
			e.printStackTrace();
		}
	}
	
	@Override 
    public void close() 
    { 
        super.close(); 
    } 
 
//    public Dao<UsersEntity, Integer> getUserDataDao() throws SQLException 
//    { 
//        if (userDao == null) 
//        { 
//            userDao = getDao(UsersEntity.class); 
//        } 
//        return userDao; 
//    } 

}
