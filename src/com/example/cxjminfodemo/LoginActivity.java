/**
 *@filename LoginActivity.java
 *@Email tengzhenjiu@qq.com
 *
 */
package com.example.cxjminfodemo;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.entity.StringEntity;

import com.example.cxjminfodemo.InfoActivity.InfoMainActivity;
import com.example.cxjminfodemo.db.DBManager;
import com.example.cxjminfodemo.dto.Family;
import com.example.cxjminfodemo.dto.Personal;
import com.example.cxjminfodemo.dto.User;
import com.example.cxjminfodemo.server.ao.Ao;
import com.example.cxjminfodemo.server.dto.CjTask;
import com.example.cxjminfodemo.server.dto.CjUser;
import com.example.cxjminfodemo.server.dto.FamilyDTO;
import com.example.cxjminfodemo.server.dto.MemberDTO;
import com.example.cxjminfodemo.utils.MD5Util;
import com.example.cxjminfodemo.utils.ToastUtil;
import com.example.idcardscandemo.utils.HttpUtil;
import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Base64;
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
	private Gson gson;
	private HttpUtils utils;

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
		utils = new HttpUtils();
		gson = new Gson();
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
		String strLoginFlag = spIsLongin.getString("IsLongin", "");
		// if (strLoginFlag.equals("1")) {
		// logOff();
		// spIsLongin.edit().putString("IsLongin", "0").commit();// 更改登录/注销标志位
		// return;
		// }
		//

	}

	private void logOff() {

	}

	private void initData() {
		userName = edit_user.getText().toString();
		passWord = edit_pw.getText().toString();
		sp = getSharedPreferences("LoginFlag", MODE_PRIVATE);

		image_left.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent intent = new Intent(LoginActivity.this, MainActivity.class);
				startActivity(intent);
				finish();
			}
		});
		/** 登陆 */
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

		// getUserData();
		postUserData();

	}

	// private void getUserData() {
	//
	// utils.send(HttpMethod.GET, RcConstant.usertasksPath, new
	// RequestCallBack<String>() {
	//
	// @Override
	// public void onFailure(HttpException error, String msg) {
	// ToastUtil.showShort(getApplicationContext(), "请求失败");
	//
	// }
	//
	// @Override
	// public void onSuccess(ResponseInfo<String> responseInfo) {
	// CjTask cjtaskInfo = gson.fromJson(responseInfo.result, CjTask.class);
	// ToastUtil.showShort(getApplicationContext(), "请求成功。。。");
	// System.out.println("用戶信息——————————————————————————————" + cjtaskInfo);
	//
	//
	// }
	// });
	//
	// }

	// 2016年10月19日14:36:23
	private void postUserData() {

		RequestParams params = new RequestParams();
		params.addHeader("Content-Type", "application/json");
		params.addHeader("Accept", "text/plain");
		params.addHeader("client_id", "1");
		CjUser userDTO = new CjUser();
		userDTO.setName("1");
		userDTO.setArea("1");
		userDTO.setPwd("1");
		userDTO.setAccount("1");

		String jsonStr = gson.toJson(userDTO);

		try {
			params.setBodyEntity(new StringEntity(jsonStr, "utf-8"));
		} catch (UnsupportedEncodingException e) {

		}

		utils.send(HttpMethod.POST, RcConstant.loginPath, params, new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException error, String msg) {
				ToastUtil.showShort(getApplicationContext(), "请求失败");

			}
           
			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				String token = responseInfo.result;
				System.out.println("输出结果为" + responseInfo.result);
				ToastUtil.showShort(getApplicationContext(), "请求成功");
                
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