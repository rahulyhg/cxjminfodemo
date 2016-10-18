/**
 *@filename LoginActivity.java
 *@Email tengzhenjiu@qq.com
 *
 */
package com.example.cxjminfodemo;

import java.util.ArrayList;
import java.util.List;

import com.example.cxjminfodemo.InfoActivity.InfoMainActivity;
import com.example.cxjminfodemo.db.DBManager;
import com.example.cxjminfodemo.dto.Family;
import com.example.cxjminfodemo.dto.Personal;
import com.example.cxjminfodemo.dto.User;
import com.example.cxjminfodemo.utils.MD5Util;
import com.example.cxjminfodemo.utils.ToastUtil;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.OnClick;

/**
 * @Title LoginActivity
 * @author tengzj
 * @data 2016年8月23日 下午4:52:12
 */
public class LoginActivity extends Activity {

	private DBManager mgr;
	private EditText edit_user;
	private EditText edit_pw;
	private String userName;
	private String passWord;
	private ArrayList<User> users;
	private String query_usern;
	private ImageView image_left;
	private TextView btn_login;
	private SharedPreferences sp;

	/********** INITIALIZES *************/

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		mgr = new DBManager(this);
		initView();
		initData();

		users = new ArrayList<User>();
		User user1 = new User("tttt", "123456");
		users.add(user1);
		mgr.addUser(users);

	}

	/*
	 * （1）输入用户名、密码 （2）无需注册功能 ，一个村对应一个用户名、密码 （3）登录安全性的考虑，密码用MD5加密传输
	 * （4）实现用户系统注销功能（首页注销功能） （5）登录后，自动将当前用户信息保存到sqlite中
	 */

	private void initView() {
		edit_user = (EditText) findViewById(R.id.edit_user);
		edit_pw = (EditText) findViewById(R.id.edit_pw);
		image_left = (ImageView) findViewById(R.id.image_left);
		btn_login = (TextView) findViewById(R.id.btn_login);
		/*********** 先判断当前是否已经登录，若已登录则执行注销操作 ***********/
		SharedPreferences spIsLongin = getSharedPreferences("IsLongin", MODE_PRIVATE);
		String strLoginFlag=spIsLongin.getString("IsLongin", "");
		if (strLoginFlag.equals("1")) {
			logOff();
			spIsLongin.edit().putString("IsLongin", "0").commit();// 更改登录/注销标志位
			return;
		}
		

	}

	private void logOff() {
		
		
	}

	private void initData() {
		userName = edit_user.getText().toString();
		passWord = edit_pw.getText().toString();
		sp = getSharedPreferences("LoginFlag", MODE_PRIVATE);
		/*mgr.query_usern(getApplicationContext(), edit_user.getText().toString());*/
		image_left.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent intent = new Intent(LoginActivity.this, MainActivity.class);
				startActivity(intent);
                  finish(); 
			}
		});
         /**登陆*/
		btn_login.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 判断用户名是否存在于数据库中
				 if (!mgr.query_usern(getApplicationContext(), edit_user.getText().toString()).isEmpty()) {
				// 用MD5给密码加密并判断密码是否正确
				if (MD5Util.encode(edit_pw.getText().toString()).equals(MD5Util.encode("111111"))) {
					enterInfo();
					ToastUtil.showShort(getApplicationContext(), "登陆成功！");
					sp.edit().putString("LoginFlag", "1").commit();
				     
				} else {
					ToastUtil.showShort(getApplicationContext(), "密码错误！");
				}
				} else {
				 ToastUtil.showShort(getApplicationContext(), "用户名不存在！");
				 }
			      
			}
		});
		
	}
    

	protected void enterInfo() {
		Intent intent = new Intent(this, InfoMainActivity.class);
		startActivity(intent);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// 应用的最后一个Activity关闭时应释放DB

	}

}