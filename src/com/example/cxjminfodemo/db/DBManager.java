package com.example.cxjminfodemo.db;

import java.util.ArrayList;
import java.util.List;

import com.example.cxjminfodemo.dto.Family;
import com.example.cxjminfodemo.dto.Personal;
import com.example.cxjminfodemo.dto.User;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


/*
家庭信息
序号	字段名称	描述	类型	长度	非空	备注
				AAB999	家庭编号	Varchar2	16		为空代表新登记家庭
getEdit_hzxm	AAB400	户主姓名	Varchar2	50	√	
				AAC058	户主证件类型	Varchar2	3	√	见代码表
getEdit_gmcfzh	AAE135	户主证件号码	Varchar2	20	√	
				AAB401	户籍编号	Varchar2	20		
getEdit_cjqtbxrs	BAB041	参保人数	number	3		
getEdit_lxdh	AAE005	联系电话	Varchar2	50		
getEdit_hkxxdz	AAE006	住址	Varchar2	100		
getEdit_djrq	AAB050	登记日期	Varchar2	10	√	格式：yyyymmdd

人员信息
序号	字段名称	描述	类型	长度	非空	备注
				AAC999	个人编号	Varchar2	16		为空代表新登记人员
getEdit_cbrxm	AAC003	姓名	Varchar2	50	√	
				AAC058	证件类型	Varchar2	3	√	见代码表
getEdit_gmcfzh	AAE135	公民身份号码	Varchar2	20	√	
getEdit_mz		AAC005	民族	Varchar2	3	√	见代码表
	
getEdit_xb		AAC004	性别	Varchar2	3	√	见代码表
getEdit_csrq	AAC006	出生日期	Varchar2	10	√	格式：yyyymmdd
getEdit_cbrylb	BAC067	参保人员类别	Varchar2	3	√	见代码表
getEdit_cbrq	AAC030	登记日期	Varchar2	10	√	格式：yyyymmdd
getEdit_yhzgx	AAC069	与户主关系	Varchar2	3		见代码表
	
				AAE005	联系电话	Varchar2	50		
getEdit_xxjzdz	AAE006	住址	Varchar2	100		
getEdit_hkxz	AAC009	户口性质	Varchar2	3		见代码表
getHZSFZ		HZSFZ	户主身份号码	Varchar2	20	√	*/

public class DBManager {
	private DBHelper helper;
	private SQLiteDatabase db;

	public DBManager(Context context) {
		helper = new DBHelper(context);
		// 因为getWritableDatabase内部调用了mContext.openOrCreateDatabase(mName, 0,
		// mFactory);
		// 所以要确保context已初始化,我们可以把实例化DBManager的步骤放在Activity的onCreate里
		db = helper.getWritableDatabase();
	}

	/**
	 * add persons
	 * 
	 * @param persons
	 */
	public void addUser(List<User> users) {
		db.beginTransaction(); // 开始事务
		try {
			for (User user : users) {
				db.execSQL("INSERT INTO user VALUES(null, ?, ?)",
						new Object[] { user.username, user.password });
			}
			db.setTransactionSuccessful(); // 设置事务成功完成
		} finally {
			db.endTransaction(); // 结束事务
		}
	}
	
	public void addPersonal(List<Personal> personals) {
		db.beginTransaction(); // 开始事务
		try {
			for (Personal personal : personals) {
				//14个字段
				db.execSQL("INSERT INTO user VALUES(null, ?,?,?,?,?,   ?,?,?,?,?  ,?,?,?,?)",
						new Object[] { null,personal.getEdit_cbrxm(), null,personal.getEdit_gmcfzh(),personal.getEdit_mz(),
								personal.getEdit_xb(),personal.getEdit_csrq(),personal.getEdit_cbrylb(),personal.getEdit_cbrq(),personal.getEdit_yhzgx(),
								null,personal.getEdit_xxjzdz(),personal.getEdit_hkxz(),personal.getHZSFZ()});
			}
			db.setTransactionSuccessful(); // 设置事务成功完成
		} finally {
			db.endTransaction(); // 结束事务
		}
	}
	
	public void addFamily(List<Family> familys) {
		db.beginTransaction(); // 开始事务
		try {
			//9个字段
			
			/*AAB999	家庭编号	Varchar2	16		为空代表新登记家庭
			AAB400	户主姓名	Varchar2	50	√	
			AAC058	户主证件类型	Varchar2	3	√	见代码表
			AAE135	户主证件号码	Varchar2	20	√	
			AAB401	户籍编号	Varchar2	20	
			
				
			BAB041	参保人数	number	3		
			AAE005	联系电话	Varchar2	50		
			AAE006	住址	Varchar2	100		
			AAB050	登记日期	Varchar2	10	√	格式：yyyymmdd*/
			
			for (Family family : familys) {
				db.execSQL("INSERT INTO user VALUES(null, ?,?,?,?,?   ,?,?,?,?)",
						new Object[] { null,family.getEdit_hzxm(), null,family.getEdit_gmcfzh(),null,
								family.getEdit_cjqtbxrs(),family.getEdit_lxdh(),family.getEdit_hkxxdz(),family.getEdit_djrq()});
			}
			db.setTransactionSuccessful(); // 设置事务成功完成
		} finally {
			db.endTransaction(); // 结束事务
		}
	}

	/**
	 * update person's age
	 * 
	 * @param person
	 */
	public void updatePassword(User user) {
		ContentValues cv = new ContentValues();
		cv.put("password", user.password);
		db.update("user", cv, "username = ?", new String[] { user.username });
	}

/*	*//**
	 * delete old person
	 * 
	 * @param person
	 *//*
	public void deleteOldUser(User user) {
		db.delete("user", "age >= ?", new String[] { String.valueOf(person.age) });
	}*/

	/*
	家庭信息
	序号	字段名称	描述	类型	长度	非空	备注
					AAB999	家庭编号	Varchar2	16		为空代表新登记家庭
	getEdit_hzxm	AAB400	户主姓名	Varchar2	50	√	
					AAC058	户主证件类型	Varchar2	3	√	见代码表
	getEdit_gmcfzh	AAE135	户主证件号码	Varchar2	20	√	
					AAB401	户籍编号	Varchar2	20		
	getEdit_cjqtbxrs	BAB041	参保人数	number	3		
	getEdit_lxdh	AAE005	联系电话	Varchar2	50		
	getEdit_hkxxdz	AAE006	住址	Varchar2	100		
	getEdit_djrq	AAB050	登记日期	Varchar2	10	√	格式：yyyymmdd

	人员信息
	序号	字段名称	描述	类型	长度	非空	备注
					AAC999	个人编号	Varchar2	16		为空代表新登记人员
	getEdit_cbrxm	AAC003	姓名	Varchar2	50	√	
					AAC058	证件类型	Varchar2	3	√	见代码表
	getEdit_gmcfzh	AAE135	公民身份号码	Varchar2	20	√	
	getEdit_mz		AAC005	民族	Varchar2	3	√	见代码表
		
	getEdit_xb		AAC004	性别	Varchar2	3	√	见代码表
	getEdit_csrq	AAC006	出生日期	Varchar2	10	√	格式：yyyymmdd
	getEdit_cbrylb	BAC067	参保人员类别	Varchar2	3	√	见代码表
	getEdit_cbrq	AAC030	登记日期	Varchar2	10	√	格式：yyyymmdd
	getEdit_yhzgx	AAC069	与户主关系	Varchar2	3		见代码表
		
					AAE005	联系电话	Varchar2	50		
	getEdit_xxjzdz	AAE006	住址	Varchar2	100		
	getEdit_hkxz	AAC009	户口性质	Varchar2	3		见代码表
	getHZSFZ		HZSFZ	户主身份号码	Varchar2	20	√	*/
	/**
	 * query all persons, return list
	 * 
	 * @return List<Person>
	 */
	public List<User> query() {
		ArrayList<User> persons = new ArrayList<User>();
		Cursor c = queryTheCursor();
		while (c.moveToNext()) {
			User user = new User();
			user._id = c.getInt(c.getColumnIndex("_id"));
			user.username = c.getString(c.getColumnIndex("username"));
			user.password = c.getString(c.getColumnIndex("password"));
			persons.add(user);
		}
		c.close();
		return persons;
	}

	/**
	 * query all persons, return cursor
	 * 
	 * @return Cursor
	 */
	public Cursor queryTheCursor() {
		Cursor c = db.rawQuery("SELECT * FROM user", null);
		return c;
	}

	/**
	 * close database
	 */
	public void closeDB() {
		db.close();
	}

}
