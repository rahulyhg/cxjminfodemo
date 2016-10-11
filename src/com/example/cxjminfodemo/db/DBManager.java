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
getEdit_jhzzjlx	AAC058	户主证件类型	Varchar2	3	√	见代码表
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
getEdit_zjlx	AAC058	证件类型	Varchar2	3	√	见代码表
getEdit_gmcfzh	AAE135	公民身份号码	Varchar2	20	√	
getEdit_mz		AAC005	民族	Varchar2	3	√	见代码表
	
getEdit_xb		AAC004	性别	Varchar2	3	√	见代码表
getEdit_csrq	AAC006	出生日期	Varchar2	10	√	格式：yyyymmdd
getEdit_cbrylb	BAC067	参保人员类别	Varchar2	3	√	见代码表
getEdit_cbrq	AAC030	登记日期	Varchar2	10	√	格式：yyyymmdd
getEdit_yhzgx	AAC069	与户主关系	Varchar2	3		见代码表
	
getEdit_lxdh	AAE005	联系电话	Varchar2	50		
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
						new Object[] { null,personal.getEdit_cbrxm(), personal.getEdit_zjlx(),personal.getEdit_gmcfzh(),personal.getEdit_mz(),
								personal.getEdit_xb(),personal.getEdit_csrq(),personal.getEdit_cbrylb(),personal.getEdit_cbrq(),personal.getEdit_yhzgx(),
								personal.getEdit_lxdh(),personal.getEdit_xxjzdz(),personal.getEdit_hkxz(),personal.getHZSFZ()});
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
			for (Family family : familys) {
				db.execSQL("INSERT INTO user VALUES(null, ?,?,?,?,?   ,?,?,?,?)",
						new Object[] { null,family.getEdit_hzxm(), family.getEdit_jhzzjlx(),family.getEdit_gmcfzh(),null,
								family.getEdit_cjqtbxrs(),family.getEdit_lxdh(),family.getEdit_hkxxdz(),family.getEdit_djrq()});
			}
			db.setTransactionSuccessful(); // 设置事务成功完成
		} finally {
			db.endTransaction(); // 结束事务
		}
	}

	/**
	 * update family
	 * 
	 * @param family
	 */

	
	public void updateFamily(Family family) {
		ContentValues cv = new ContentValues();
		cv.put("AAB400", family.getEdit_hzxm());
		cv.put("AAE135", family.getEdit_gmcfzh());
		cv.put("AAC058", family.getEdit_jhzzjlx());
		cv.put("BAB041", family.getEdit_cjqtbxrs());
		cv.put("AAE005", family.getEdit_lxdh());
		cv.put("AAE006", family.getEdit_hkxxdz());
		cv.put("AAB050", family.getEdit_djrq());
		
		cv.put("ISEDIT", family.getIsEdit());
		cv.put("ISUPLOAD", family.getIsUpload());
		
		db.update("family", cv, "_id = ?", new String[] { String.valueOf(family.getId())});
	}
	
	/**
	 * update personal
	 * 	
	 * @param personal
	 */
	public void updatePersonal(Personal personal) {
		ContentValues cv = new ContentValues();
		cv.put("AAC003", personal.getEdit_cbrxm());
		cv.put("AAE135", personal.getEdit_gmcfzh());
		cv.put("AAC005", personal.getEdit_mz());
		cv.put("AAC004", personal.getEdit_xb());
		cv.put("AAC006", personal.getEdit_csrq());
		cv.put("BAC067", personal.getEdit_cbrylb());
		
		cv.put("AAC030", personal.getEdit_cbrq());
		cv.put("AAC069", personal.getEdit_yhzgx());
		cv.put("AAE006", personal.getEdit_xxjzdz());
		cv.put("AAC009", personal.getEdit_hkxz());
		cv.put("HZSFZ", personal.getHZSFZ());
		
		cv.put("AAC058", personal.getEdit_zjlx());
		cv.put("AAE005", personal.getEdit_lxdh());
		
		cv.put("ISEDIT", personal.getIsEdit());
		cv.put("ISUPLOAD", personal.getIsUpload());
		
		db.update("personal", cv, "_id = ?", new String[] {  String.valueOf(personal.getId())});
	}

	/**
	 * delete old family
	 * 
	 * @param family
	 */
	public void deleteFamily(Family family) {
		db.delete("family", "_id = ?", new String[] { String.valueOf(family.id) });
	}
	
	/**
	 * delete old personal
	 * 
	 * @param personal
	 */
	public void deletePersonal(Personal personal) {
		db.delete("personal", "_id = ?", new String[] { String.valueOf(personal.id) });
	}

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
	public List<User> queryUser() {
		ArrayList<User> users = new ArrayList<User>();
		Cursor c = queryTheCursor();
		while (c.moveToNext()) {
			User user = new User();
			user._id = c.getInt(c.getColumnIndex("_id"));
			user.username = c.getString(c.getColumnIndex("username"));
			user.password = c.getString(c.getColumnIndex("password"));
			users.add(user);
		}
		c.close();
		return users;
	}
	
	
	public List<Family> queryFamily() {
		ArrayList<Family> familys = new ArrayList<Family>();
		Cursor c = queryTheCursor();
		while (c.moveToNext()) {
			Family family = new Family();
			family.id = c.getInt(c.getColumnIndex("_id"));
			family.edit_hzxm = c.getString(c.getColumnIndex("AAB400"));
			family.edit_gmcfzh = c.getString(c.getColumnIndex("AAE135"));
			family.edit_jhzzjlx = c.getString(c.getColumnIndex("AAC058"));
			family.edit_cjqtbxrs = c.getString(c.getColumnIndex("BAB041"));
			family.edit_lxdh = c.getString(c.getColumnIndex("AAE005"));
			family.edit_hkxxdz= c.getString(c.getColumnIndex("AAE006"));
			family.edit_djrq= c.getString(c.getColumnIndex("AAB050"));
			familys.add(family);
		}
		c.close();
		return familys;
	}
	
	/*人员信息
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
	public List<Personal> queryPersonal() {
		ArrayList<Personal> personals = new ArrayList<Personal>();
		Cursor c = queryTheCursor();
		while (c.moveToNext()) {
			Personal personal = new Personal();
			personal.id = c.getInt(c.getColumnIndex("_id"));
			personal.edit_cbrxm = c.getString(c.getColumnIndex("AAC003"));
			personal.edit_gmcfzh = c.getString(c.getColumnIndex("AAE135"));
			personal.edit_mz = c.getString(c.getColumnIndex("AAC005"));
			personal.edit_xb = c.getString(c.getColumnIndex("AAC004"));
			personal.edit_csrq = c.getString(c.getColumnIndex("AAC006"));
			personal.edit_cbrylb = c.getString(c.getColumnIndex("BAC067"));
			personal.edit_cbrq = c.getString(c.getColumnIndex("AAC030"));
			personal.edit_yhzgx = c.getString(c.getColumnIndex("AAC069"));
			personal.edit_xxjzdz = c.getString(c.getColumnIndex("AAE006"));
			personal.edit_hkxz = c.getString(c.getColumnIndex("AAC009"));
			personal.HZSFZ = c.getString(c.getColumnIndex("HZSFZ"));
			
			personal.edit_zjlx = c.getString(c.getColumnIndex("AAC058"));
			personal.edit_lxdh = c.getString(c.getColumnIndex("AAE005"));
			personals.add(personal);
		}
		c.close();
		return personals;
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
