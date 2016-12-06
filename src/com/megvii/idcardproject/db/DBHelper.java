package com.megvii.idcardproject.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/*
家庭信息
序号	字段名称	描述	类型	长度	非空	备注
	AAB999	家庭编号	Varchar2	16		为空代表新登记家庭
	AAB400	户主姓名	Varchar2	50	√	
	AAC058	户主证件类型	Varchar2	3	√	见代码表
	AAE135	户主证件号码	Varchar2	20	√	
	AAB401	户籍编号	Varchar2	20		
	BAB041	参保人数	number	3		
	AAE005	联系电话	Varchar2	50		
	AAE006	住址	Varchar2	100		
	AAB050	登记日期	Varchar2	10	√	格式：yyyymmdd

人员信息
序号	字段名称	描述	类型	长度	非空	备注
	AAC999	个人编号	Varchar2	16		为空代表新登记人员
	AAC003	姓名	Varchar2	50	√	
	AAC058	证件类型	Varchar2	3	√	见代码表
	AAE135	公民身份号码	Varchar2	20	√	
	AAC005	民族	Varchar2	3	√	见代码表
	AAC004	性别	Varchar2	3	√	见代码表
	AAC006	出生日期	Varchar2	10	√	格式：yyyymmdd
	BAC067	参保人员类别	Varchar2	3	√	见代码表
	AAC030	登记日期	Varchar2	10	√	格式：yyyymmdd
	AAC069	与户主关系	Varchar2	3		见代码表
	AAE005	联系电话	Varchar2	50		
	AAE006	住址	Varchar2	100		
	AAC009	户口性质	Varchar2	3		见代码表
	HZSFZ	户主身份号码	Varchar2	20	√	*/

public class DBHelper extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "test.db";
	private static final int DATABASE_VERSION = 17;

	public DBHelper(Context context) {
		// CursorFactory设置为null,使用默认值
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	// 数据库第一次被创建时onCreate会被调用
	@Override
	public void onCreate(SQLiteDatabase db) {
		CreatTable(db);
	
	}

	// 如果DATABASE_VERSION值被改为2,系统发现现有数据库版本不同,即会调用onUpgrade
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		CreatTable(db);
	}
	
	void CreatTable(SQLiteDatabase db)
	{
		db.execSQL("CREATE TABLE IF NOT EXISTS user"
				+ "(_id INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ "username VARCHAR NOT NULL,"
				+ "password VARCHAR)");
		db.execSQL("CREATE TABLE IF NOT EXISTS userdetail"
				+ "(taskid VARCHAR PRIMARY KEY ,"
				+ "account VARCHAR NOT NULL,"
				+ "city VARCHAR,"
				+ "cjarea VARCHAR,"
				+ "downloadflag VARCHAR,"
				+ "sfcl VARCHAR,"
				+ "taskdesc VARCHAR,"
				+ "taskstatus VARCHAR,"
				+ "uploadflag VARCHAR,"
				+ "validcfcburl VARCHAR)");

		db.execSQL("CREATE TABLE IF NOT EXISTS family"
				+ "(_id INTEGER PRIMARY KEY AUTOINCREMENT, "
				+ "LSH VARCHAR, "
				+ "AAB999 VARCHAR, "
				+ "AAB400 VARCHAR  NOT NULL,"
				+ "AAC058 VARCHAR  NOT NULL, "
				+ "AAE135 VARCHAR  NOT NULL, "
				+ "AAB401 VARCHAR, "
				+ "BAB041 VARCHAR, "
				+ "AAE005 VARCHAR, "
				+ "AAC010 VARCHAR, "
				+ "AAE006 VARCHAR,"
				+ "AAB050 VARCHAR  NOT NULL,"
				+ "ISEDIT VARCHAR NOT　NULL,"
				+ "ISUPLOAD VARCHAR NOT NULL,"
				+ "XZQH VARCHAR　NOT NULL)");

		db.execSQL("CREATE TABLE IF NOT EXISTS personal"
				+ "(_id INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ "LSH VARCHAR, "
				+ "AAC999 VARCHAR, "
				+ "AAC003 VARCHAR  NOT NULL, "
				+ "AAC058 VARCHAR  NOT NULL, "
				+ "AAE135 VARCHAR  NOT NULL, "
				+ "AAC005 VARCHAR  NOT NULL, "
				+ "AAC004 VARCHAR  NOT NULL, "
				+ "AAC006 VARCHAR  NOT NULL, "
				+ "BAC067 VARCHAR  NOT NULL, "
				+ "AAC030 VARCHAR  NOT NULL,"
				+ "AAC069 VARCHAR,"
				+ "AAE005 VARCHAR,"
				+ "AAE006 VARCHAR,"
				+ "AAC009 VARCHAR,"
				+ "JFBZ VARCHAR,"
				+ "HZSFZ VARCHAR  NOT NULL,"
				+ "ISEDIT VARCHAR NOT　NULL,"
				+ "ISUPLOAD VARCHAR NOT NULL)");
		
		db.execSQL("CREATE TABLE IF NOT EXISTS code"
				+ "(_id INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ "AAA100 VARCHAR NOT NULL,"
				+ "AAA101 VARCHAR,"
				+ "AAA103 VARCHAR,"
				+ "AAA102 VARCHAR)");
		
		db.execSQL("CREATE TABLE IF NOT EXISTS xzqh"
				+ "(_id INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ "COUNTRY VARCHAR NOT NULL,"
				+ "TOWN VARCHAR ,"
				+ "COUNTY VARCHAR,"
				+ "CITY VARCHAR,"
				+ "PROVINCE VARCHAR,"
				+ "NAME VARCHAR,"
				+ "SFCL VARCHAR,"
				+ "CJZT VARCHAR)");
		
		db.execSQL("CREATE TABLE IF NOT EXISTS url"
				+ "(_id INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ "COUNTRYCODE VARCHAR NOT NULL,"
				+ "NAME VARCHAR ,"
				+ "URL VARCHAR)");
	}
}
