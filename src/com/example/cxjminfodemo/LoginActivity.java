/**
 *@filename LoginActivity.java
 *@Email tengzhenjiu@qq.com
 *
 */
package com.example.cxjminfodemo;

import java.util.ArrayList;
import java.util.List;

import com.example.cxjminfodemo.InfoActivity.InfoMainActivity;
import com.example.cxjminfodemo.db.DBHelper;
import com.example.cxjminfodemo.db.DBManager;
import com.example.cxjminfodemo.dto.Family;
import com.example.cxjminfodemo.dto.Personal;
import com.example.cxjminfodemo.dto.User;
import com.example.cxjminfodemo.utils.LoadingDialog;
import com.example.cxjminfodemo.utils.MD5Util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @Title LoginActivity
 * @author tengzj
 * @data 2016年8月23日 下午4:52:12
 */
public class LoginActivity extends Activity {

	/*	*//********** DECLARES *************/
	/*
	 * private ImageView image_left; private EditText edit_user; private
	 * EditText edit_pw; private TextView btn_login; private ImageView
	 * imageView1;
	 *//********** INITIALIZES *************//*
											 * image_left = (ImageView)
											 * findViewById(R.id.image_left);
											 * edit_user = (EditText)
											 * findViewById(R.id.edit_user);
											 * edit_pw = (EditText)
											 * findViewById(R.id.edit_pw);
											 * btn_login = (TextView)
											 * findViewById(R.id.btn_login);
											 * imageView1 = (ImageView)
											 * findViewById(R.id.imageView1);
											 * Please visit
											 * http://www.ryangmattison.com for
											 * updates
											 */
	private DBManager mgr;

	@Bind(R.id.image_left)
	ImageView image_left;

	private EditText edit_user;

	private EditText edit_pw;

	private String userName;

	private String passWord;

	private SQLiteDatabase rdb;

	private LoadingDialog dialog;

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
        dialog = new LoadingDialog(this);
		ButterKnife.bind(LoginActivity.this);
		mgr = new DBManager(this);

		initView();
		initData();

		/*ArrayList<User> users = new ArrayList<User>();
		User user1 = new User("tengzhenjiu", "123456");
		users.add(user1);
		mgr.addUser(users);
		List<User> persons = mgr.queryUser();
		Log.i("user", persons.get(0).username);*/

	/*	ArrayList<Family> familys = new ArrayList<Family>();
		Family family = new Family();
		family.setEdit_gmcfzh("330702199402180816");
		family.setEdit_hzxm("滕真久");
		family.setEdit_jhzzjlx("1");
		family.setEdit_cjqtbxrs("1");
		family.setEdit_lxdh("1");
		family.setEdit_djrq("2016");
		familys.add(family);

		Family family1 = new Family();
		family1.setEdit_gmcfzh("130322199204061011");
		family1.setEdit_hzxm("吴林鸿");
		family1.setEdit_jhzzjlx("1");
		family1.setEdit_cjqtbxrs("1");
		family1.setEdit_lxdh("1");
		family1.setEdit_djrq("2016");
		familys.add(family1);
		mgr.addFamily(familys);
*/
		ArrayList<Personal> personals = new ArrayList<Personal>();
		int i = 0;

		// Integer.parseInt(a);
		while (i < 10) {
			Personal personal = new Personal();
			personal.setEdit_cbrxm(String.valueOf(i));
			personal.setEdit_gmcfzh(String.valueOf(i));
			i++;

			personal.setEdit_zjlx("1");
			personal.setEdit_mz("1");
			personal.setEdit_xb("男");
			personal.setEdit_cbrq("2016");
			personal.setEdit_cbrylb("1");
			personal.setEdit_csrq("2016");
			personal.setHZSFZ("330702199402180816");
			personals.add(personal);
		}

		i = 0;
		// Integer.parseInt(a);
		while (i < 10) {
			Personal personal1 = new Personal();
			personal1.setEdit_cbrxm(String.valueOf(i));
			personal1.setEdit_gmcfzh(String.valueOf(i));
			i++;

			personal1.setEdit_zjlx("1");
			personal1.setEdit_mz("1");
			personal1.setEdit_xb("男");
			personal1.setEdit_cbrq("2016");
			personal1.setEdit_cbrylb("1");
			personal1.setEdit_csrq("2016");
			personal1.setHZSFZ("130322199204061011");
			personals.add(personal1);
		}
		mgr.addPersonal(personals);

		Personal personal2 = new Personal();
		personal2.setId(5);
		personal2.setEdit_cbrxm(String.valueOf(100));
		personal2.setEdit_gmcfzh(String.valueOf(100));
		personal2.setEdit_zjlx("1");
		personal2.setEdit_mz("1");
		personal2.setEdit_xb("女");
		personal2.setEdit_cbrq("2016");
		personal2.setEdit_cbrylb("1");
		personal2.setEdit_csrq("2016");
		personal2.setHZSFZ("130322199204061011");
		personals.add(personal2);
		mgr.updatePersonal(personal2);

		Personal personal3 = new Personal();
		personal3.setId(1);
		personal3.setEdit_cbrxm(String.valueOf(1));
		personal3.setEdit_gmcfzh(String.valueOf(1));
		personal3.setEdit_zjlx("1");
		personal3.setEdit_mz("1");
		personal3.setEdit_xb("男");
		personal3.setEdit_cbrq("2016");
		personal3.setEdit_cbrylb("1");
		personal3.setEdit_csrq("2016");
		personal3.setHZSFZ("130322199204061011");
		personals.add(personal3);
		mgr.deletePersonal(personal3);
	}

	/*
	 * （1）输入用户名、密码 （2）无需注册功能 ，一个村对应一个用户名、密码 （3）登录安全性的考虑，密码用MD5加密传输
	 * （4）实现用户系统注销功能（首页注销功能） （5）登录后，自动将当前用户信息保存到sqlite中
	 */

	private void initView() {
		edit_user = (EditText) findViewById(R.id.edit_user);
		edit_pw = (EditText) findViewById(R.id.edit_pw);
		userName = edit_user.getText().toString().trim();
		passWord = edit_pw.getText().toString().trim();
	}

	private void initData() {
		DBHelper dbHelper = new DBHelper(this);
		rdb = dbHelper.getReadableDatabase();

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// 应用的最后一个Activity关闭时应释放DB

	}

	@OnClick(R.id.image_left)
	public void ToMainActivity() {
		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
	}

	@OnClick(R.id.btn_login)
	public void toInfoMainActivity() {
		dialog.show();
		
		/* select * form user where username= */
		/*
		 * rdb.execSQL("select * form user where username=userName");
		 * 
		 * // 判断密码是否为空
		 * 
		 * if (!userName.equals("") && !passWord.equals("")) {
		 * 
		 * // 判断密码是否正确 if (MD5Util.encode(passWord).equals()) { enterInfo(); }
		 * else { Toast.makeText(getApplicationContext(), "密码错误！", 0).show(); }
		 * } else
		 * 
		 * { Toast.makeText(getApplicationContext(), "用户名或密码不能为空！", 0).show(); }
		 */

		enterInfo();
	}

	private void enterInfo() {
		Intent intent = new Intent(this, InfoMainActivity.class);
		startActivity(intent);

	}
}