package com.example.cxjminfodemo.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "user.db";
	private static final int DATABASE_VERSION = 2;

	public DBHelper(Context context) {
		// CursorFactory����Ϊnull,ʹ��Ĭ��ֵ
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	// ���ݿ��һ�α�����ʱonCreate�ᱻ����
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE IF NOT EXISTS user"
				+ "(_id INTEGER PRIMARY KEY AUTOINCREMENT, username VARCHAR, password VARCHAR)");

		db.execSQL("CREATE TABLE IF NOT EXISTS personal"
				+ "(_id INTEGER PRIMARY KEY AUTOINCREMENT, edit_cbrxm VARCHAR, edit_gmcfzh VARCHAR,"
				+ " edit_mz VARCHAR, edit_xb VARCHAR, edit_csrq VARCHAR, edit_cbrq VARCHAR, "
				+ "edit_cbrylb VARCHAR, edit_jf VARCHAR)");

		db.execSQL("CREATE TABLE IF NOT EXISTS family"
				+ "(_id INTEGER PRIMARY KEY AUTOINCREMENT, edit_gmcfzh VARCHAR, edit_jgszcwh VARCHAR, "
				+ "edit_hzxm VARCHAR, edit_hjbh VARCHAR, edit_lxdh VARCHAR, edit_dzyx VARCHAR, "
				+ "edit_yzbm VARCHAR, edit_cjqtbxrs VARCHAR, edit_hkxxdz VARCHAR)");
	}

	// ���DATABASE_VERSIONֵ����Ϊ2,ϵͳ�����������ݿ�汾��ͬ,�������onUpgrade
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("CREATE TABLE IF NOT EXISTS personal"
				+ "(_id INTEGER PRIMARY KEY AUTOINCREMENT, edit_cbrxm VARCHAR, edit_gmcfzh VARCHAR,"
				+ " edit_mz VARCHAR, edit_xb VARCHAR, edit_csrq VARCHAR, edit_cbrq VARCHAR, "
				+ "edit_cbrylb VARCHAR, edit_jf VARCHAR)");

		db.execSQL("CREATE TABLE IF NOT EXISTS family"
				+ "(_id INTEGER PRIMARY KEY AUTOINCREMENT, edit_gmcfzh VARCHAR, edit_jgszcwh VARCHAR, "
				+ "edit_hzxm VARCHAR, edit_hjbh VARCHAR, edit_lxdh VARCHAR, edit_dzyx VARCHAR, "
				+ "edit_yzbm VARCHAR, edit_cjqtbxrs VARCHAR, edit_hkxxdz VARCHAR)");
	}
}
