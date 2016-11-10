/**
 *@filename FamilyUtil.java
 *@Email tengzhenjiu@qq.com
 *
 */
package com.example.cxjmcj.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * @Title FamilyUtil
 * @author tengzj
 * @data 2016年8月24日 上午10:06:02
 */
public class FamilyUtil {
	public static String getValue(Context con) {
		SharedPreferences pref = con.getSharedPreferences("Family", 0);
		return pref.getString("Family", "");
	}

	/**
	 * 本地存储说明
	 * 
	 * @param key=family|personal
	 * @param value为DTO对象
	 */
	public static boolean saveValue(Context con, String value) {
		SharedPreferences pref = con.getSharedPreferences("Family", 0);
		return pref.edit().putString("Family", value).commit();
	}
}
