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
		/* ����table BookMark */
		String sql = "CREATE TABLE " + TABLE_CUSTOMD + " ("
				+ " id INTEGER PRIMARY KEY, " + " "
				+ "id_c int, " + " " 
				+ "customName text, " + " "
				+ "link text)";
		db.execSQL(sql);
	}
	
	// ����Custom����
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
	
	// ��ѯCustom��������
	public ArrayList<RulesCustomLocal> selectCustom() {
		CustomList = new ArrayList<RulesCustomLocal>();
		// ���ö�ȡ���ݵķ���
		SQLiteDatabase db = this.getWritableDatabase(); // �õ����ݿ�ʵ��
		String[] columns = {"id"};
		if (!db.isOpen()) {
			db = this.getWritableDatabase();
		}
		// ���ò�ѯ���ݱ�ķ������ײ��װ����ֱ�ӵ��ã�������Ҫ��ѯ�Ĳ����Ϳ�����
		Cursor cursor = db.query(TABLE_CUSTOMD, columns, null, null, null,
				null, null);
		
		// ���α������ƶ��ķ�ʽ�������ж�ȡ����
		if (cursor != null && cursor.getCount() > 0) {
			cursor.moveToFirst();
		}
		for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
			RulesCustomLocal rc = new RulesCustomLocal();
			rc.setId(cursor.getInt(0));
			CustomList.add(rc);
		}
		// �ر��α꣬���Źر����ݿ⣬�м�һ��Ҫ�أ���Ȼ����ᱨ��
		cursor.close();
		return CustomList;
	}

}
