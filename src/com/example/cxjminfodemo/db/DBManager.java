package com.example.cxjminfodemo.db;

import java.util.ArrayList;
import java.util.List;

import com.example.cxjminfodemo.dto.User;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

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
	public void add(List<User> users) {
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
