package com.zhihui.quicksearch.sqlite;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.zhihui.quicksearch.bean.RulesCustomLocal;

public class SearchSqlite extends SQLiteOpenHelper {
	private final static String DATABASE_NAME = "ZhiSearch.db";
	private final static int DATABASE_VERSION = 1;
	private final static String TABLE_CUSTOMD = "CUSTOMD";
	private static ArrayList<RulesCustomLocal> CustomList = null;
	
	private static SearchSqlite instance = null;
	public static SearchSqlite getInstance(Context context){
		if (instance == null) {
			instance = new SearchSqlite(context);
		}
		return instance;
	}
	
	public SearchSqlite(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		createCustom(db);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
		// TODO Auto-generated method stub
		String sqlBookMark = "DROP TABLE IF EXISTS " + TABLE_CUSTOMD;
		db.execSQL(sqlBookMark);
		onCreate(db);
	}
	
	/**
	 * create table Custom
	 */
	public void createCustom(SQLiteDatabase db) {
		/* 建立table BookMark */
		String sql = "CREATE TABLE " + TABLE_CUSTOMD + " ("
				+ " id INTEGER PRIMARY KEY, " + " "
				+ "id_c int, " + " " 
				+ "customName text, " + " "
				+ "link text)";
		db.execSQL(sql);
	}
	
	// 增加Custom数据
	public long insertCustom(RulesCustomLocal rc) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues cv = new ContentValues();
		cv.put("id_c", rc.getId_c());
		cv.put("customName", rc.getCustomName());
		cv.put("link", rc.getLink());
		if (!db.isOpen()) {
			db = this.getWritableDatabase();
		}
		long row = db.insert(TABLE_CUSTOMD, null, cv);
		System.out.println("low" + row);
		return row;
	}
	
	// 查询Custom所有数据
	public ArrayList<RulesCustomLocal> selectCustom() {
		CustomList = new ArrayList<RulesCustomLocal>();
		// 调用读取数据的方法
		SQLiteDatabase db = this.getWritableDatabase(); // 得到数据库实例
		String[] columns = {"id"};
		if (!db.isOpen()) {
			db = this.getWritableDatabase();
		}
		// 调用查询数据表的方法，底层封装好了直接调用，传入你要查询的参数就可以了
		Cursor cursor = db.query(TABLE_CUSTOMD, columns, null, null, null,
				null, null);
		
		// 以游标向下移动的方式，来逐行读取数据
		if (cursor != null && cursor.getCount() > 0) {
			cursor.moveToFirst();
		}
		for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
			RulesCustomLocal rc = new RulesCustomLocal();
			rc.setId(cursor.getInt(0));
			CustomList.add(rc);
		}
		// 关闭游标，接着关闭数据库，切忌一定要关，不然程序会报错。
		cursor.close();
		return CustomList;
	}

}
