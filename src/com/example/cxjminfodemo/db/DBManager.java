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
��ͥ��Ϣ
���	�ֶ�����	����	����	����	�ǿ�	��ע
				AAB999	��ͥ���	Varchar2	16		Ϊ�մ����µǼǼ�ͥ
getEdit_hzxm	AAB400	��������	Varchar2	50	��	
				AAC058	����֤������	Varchar2	3	��	�������
getEdit_gmcfzh	AAE135	����֤������	Varchar2	20	��	
				AAB401	�������	Varchar2	20		
getEdit_cjqtbxrs	BAB041	�α�����	number	3		
getEdit_lxdh	AAE005	��ϵ�绰	Varchar2	50		
getEdit_hkxxdz	AAE006	סַ	Varchar2	100		
getEdit_djrq	AAB050	�Ǽ�����	Varchar2	10	��	��ʽ��yyyymmdd

��Ա��Ϣ
���	�ֶ�����	����	����	����	�ǿ�	��ע
				AAC999	���˱��	Varchar2	16		Ϊ�մ����µǼ���Ա
getEdit_cbrxm	AAC003	����	Varchar2	50	��	
				AAC058	֤������	Varchar2	3	��	�������
getEdit_gmcfzh	AAE135	������ݺ���	Varchar2	20	��	
getEdit_mz		AAC005	����	Varchar2	3	��	�������
	
getEdit_xb		AAC004	�Ա�	Varchar2	3	��	�������
getEdit_csrq	AAC006	��������	Varchar2	10	��	��ʽ��yyyymmdd
getEdit_cbrylb	BAC067	�α���Ա���	Varchar2	3	��	�������
getEdit_cbrq	AAC030	�Ǽ�����	Varchar2	10	��	��ʽ��yyyymmdd
getEdit_yhzgx	AAC069	�뻧����ϵ	Varchar2	3		�������
	
				AAE005	��ϵ�绰	Varchar2	50		
getEdit_xxjzdz	AAE006	סַ	Varchar2	100		
getEdit_hkxz	AAC009	��������	Varchar2	3		�������
getHZSFZ		HZSFZ	������ݺ���	Varchar2	20	��	*/

public class DBManager {
	private DBHelper helper;
	private SQLiteDatabase db;

	public DBManager(Context context) {
		helper = new DBHelper(context);
		// ��ΪgetWritableDatabase�ڲ�������mContext.openOrCreateDatabase(mName, 0,
		// mFactory);
		// ����Ҫȷ��context�ѳ�ʼ��,���ǿ��԰�ʵ����DBManager�Ĳ������Activity��onCreate��
		db = helper.getWritableDatabase();
	}

	/**
	 * add persons
	 * 
	 * @param persons
	 */
	public void addUser(List<User> users) {
		db.beginTransaction(); // ��ʼ����
		try {
			for (User user : users) {
				db.execSQL("INSERT INTO user VALUES(null, ?, ?)",
						new Object[] { user.username, user.password });
			}
			db.setTransactionSuccessful(); // ��������ɹ����
		} finally {
			db.endTransaction(); // ��������
		}
	}
	
	public void addPersonal(List<Personal> personals) {
		db.beginTransaction(); // ��ʼ����
		try {
			for (Personal personal : personals) {
				//14���ֶ�
				db.execSQL("INSERT INTO user VALUES(null, ?,?,?,?,?,   ?,?,?,?,?  ,?,?,?,?)",
						new Object[] { null,personal.getEdit_cbrxm(), null,personal.getEdit_gmcfzh(),personal.getEdit_mz(),
								personal.getEdit_xb(),personal.getEdit_csrq(),personal.getEdit_cbrylb(),personal.getEdit_cbrq(),personal.getEdit_yhzgx(),
								null,personal.getEdit_xxjzdz(),personal.getEdit_hkxz(),personal.getHZSFZ()});
			}
			db.setTransactionSuccessful(); // ��������ɹ����
		} finally {
			db.endTransaction(); // ��������
		}
	}
	
	public void addFamily(List<Family> familys) {
		db.beginTransaction(); // ��ʼ����
		try {
			//9���ֶ�
			
			/*AAB999	��ͥ���	Varchar2	16		Ϊ�մ����µǼǼ�ͥ
			AAB400	��������	Varchar2	50	��	
			AAC058	����֤������	Varchar2	3	��	�������
			AAE135	����֤������	Varchar2	20	��	
			AAB401	�������	Varchar2	20	
			
				
			BAB041	�α�����	number	3		
			AAE005	��ϵ�绰	Varchar2	50		
			AAE006	סַ	Varchar2	100		
			AAB050	�Ǽ�����	Varchar2	10	��	��ʽ��yyyymmdd*/
			
			for (Family family : familys) {
				db.execSQL("INSERT INTO user VALUES(null, ?,?,?,?,?   ,?,?,?,?)",
						new Object[] { null,family.getEdit_hzxm(), null,family.getEdit_gmcfzh(),null,
								family.getEdit_cjqtbxrs(),family.getEdit_lxdh(),family.getEdit_hkxxdz(),family.getEdit_djrq()});
			}
			db.setTransactionSuccessful(); // ��������ɹ����
		} finally {
			db.endTransaction(); // ��������
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
	��ͥ��Ϣ
	���	�ֶ�����	����	����	����	�ǿ�	��ע
					AAB999	��ͥ���	Varchar2	16		Ϊ�մ����µǼǼ�ͥ
	getEdit_hzxm	AAB400	��������	Varchar2	50	��	
					AAC058	����֤������	Varchar2	3	��	�������
	getEdit_gmcfzh	AAE135	����֤������	Varchar2	20	��	
					AAB401	�������	Varchar2	20		
	getEdit_cjqtbxrs	BAB041	�α�����	number	3		
	getEdit_lxdh	AAE005	��ϵ�绰	Varchar2	50		
	getEdit_hkxxdz	AAE006	סַ	Varchar2	100		
	getEdit_djrq	AAB050	�Ǽ�����	Varchar2	10	��	��ʽ��yyyymmdd

	��Ա��Ϣ
	���	�ֶ�����	����	����	����	�ǿ�	��ע
					AAC999	���˱��	Varchar2	16		Ϊ�մ����µǼ���Ա
	getEdit_cbrxm	AAC003	����	Varchar2	50	��	
					AAC058	֤������	Varchar2	3	��	�������
	getEdit_gmcfzh	AAE135	������ݺ���	Varchar2	20	��	
	getEdit_mz		AAC005	����	Varchar2	3	��	�������
		
	getEdit_xb		AAC004	�Ա�	Varchar2	3	��	�������
	getEdit_csrq	AAC006	��������	Varchar2	10	��	��ʽ��yyyymmdd
	getEdit_cbrylb	BAC067	�α���Ա���	Varchar2	3	��	�������
	getEdit_cbrq	AAC030	�Ǽ�����	Varchar2	10	��	��ʽ��yyyymmdd
	getEdit_yhzgx	AAC069	�뻧����ϵ	Varchar2	3		�������
		
					AAE005	��ϵ�绰	Varchar2	50		
	getEdit_xxjzdz	AAE006	סַ	Varchar2	100		
	getEdit_hkxz	AAC009	��������	Varchar2	3		�������
	getHZSFZ		HZSFZ	������ݺ���	Varchar2	20	��	*/
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
