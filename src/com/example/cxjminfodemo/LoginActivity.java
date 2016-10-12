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
import com.example.cxjminfodemo.dto.User;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
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
		
		initView();
		initData();
		ButterKnife.bind(LoginActivity.this);
		mgr = new DBManager(this);

		ArrayList<User> users = new ArrayList<User>();
		User user1 = new User("tengzhenjiu", "123456");
		users.add(user1);
		mgr.addUser(users);
		List<User> persons = mgr.queryUser();
		Log.i("user", persons.get(0).username);
		
		//130322199204061011
		ArrayList<Family> familys = new ArrayList<Family>();
		int i=0;
		while(i<10)
		{
			Family family = new Family();
			family.setEdit_gmcfzh("330702199402180816");
			family.setEdit_cjqtbxrs("1");
			family.setEdit_lxdh("1");
			familys.add(family);
		}
		
		
		mgr.addFamily(familys);
		
		/*getEdit_hzxm	AAB400	户主姓名	Varchar2	50	√	
					AAC058	户主证件类型	Varchar2	3	√	见代码表
	getEdit_gmcfzh	AAE135	户主证件号码	Varchar2	20	√	
					AAB401	户籍编号	Varchar2	20		
	getEdit_cjqtbxrs	BAB041	参保人数	number	3		
	getEdit_lxdh	AAE005	联系电话	Varchar2	50		
	getEdit_hkxxdz	AAE006	住址	Varchar2	100		
	getEdit_djrq	AAB050	登记日期	Varchar2	10	√	格式：yyyymmdd*/
	}

	private void initView() {
		edit_user = (EditText) findViewById(R.id.edit_user);
		edit_pw = (EditText) findViewById(R.id.edit_pw);
	}

	private void initData() {
		
		
	}


	@Override
	protected void onDestroy() {
		super.onDestroy();
		// 应用的最后一个Activity关闭时应释放DB
		mgr.closeDB();
	}

	@OnClick(R.id.image_left)
	public void ToMainActivity() {
		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
	}

	@OnClick(R.id.btn_login)
	public void toInfoMainActivity() {
		String userName = edit_user.getText().toString().trim();
		String passWord = edit_pw.getText().toString().trim();
		
		Intent intent = new Intent(this, InfoMainActivity.class);
		startActivity(intent);
		
	}
}