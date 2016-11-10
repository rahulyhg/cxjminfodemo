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
 * @data 2016��8��24�� ����10:06:02
 */
public class FamilyUtil {
	public static String getValue(Context con) {
		SharedPreferences pref = con.getSharedPreferences("Family", 0);
		return pref.getString("Family", "");
	}

	/**
	 * ���ش洢˵��
	 * 
	 * @param key=family|personal
	 * @param valueΪDTO����
	 */
	public static boolean saveValue(Context con, String value) {
		SharedPreferences pref = con.getSharedPreferences("Family", 0);
		return pref.edit().putString("Family", value).commit();
	}
}
