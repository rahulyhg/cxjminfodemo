/**
 *@filename FamilyUtil.java
 *@Email tengzhenjiu@qq.com
 *
 */
package com.megvii.idcardproject.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * @Title FamilyUtil
 * @author tengzj
 * @data 2016��8��24�� ����10:06:02
 */
public class PersonalUtil {
	public static String getValue(Context con) {
		SharedPreferences pref = con.getSharedPreferences("Personal", 0);
		return pref.getString("Personal", "");
	}

	/**
	 * ���ش洢˵��
	 * 
	 * @param key=family|personal
	 * @param valueΪDTO����
	 */
	public static boolean saveValue(Context con, String value) {
		SharedPreferences pref = con.getSharedPreferences("Personal", 0);
		return pref.edit().putString("Personal", value).commit();
	}
}
