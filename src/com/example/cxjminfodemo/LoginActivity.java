package com.example.cxjminfodemo;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.entity.StringEntity;

import com.example.cxjminfodemo.InfoActivity.InfoMainActivity;
import com.example.cxjminfodemo.db.DBManager;
import com.example.cxjminfodemo.dto.User;
import com.example.cxjminfodemo.server.dto.CjUser;
import com.example.cxjminfodemo.server.dto.UserDetail;
import com.example.cxjminfodemo.utils.LoadingDialog;
import com.example.cxjminfodemo.utils.MD5Util;
import com.example.cxjminfodemo.utils.ToastUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

import android.app.Activity;
import android.content.Context;
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
@SuppressWarnings({ "deprecation" })
public class LoginActivity extends Activity {

	private DBManager mgr;
	private EditText edit_user;
	private EditText edit_pw;
	private ArrayList<User> users;
	private String query_usern;
	private ImageView image_left;
	private TextView btn_login;
	private SharedPreferences sp;
	private Gson gson;
	private HttpUtils utils;
	private String userName;
	private String passWord;

	private SharedPreferences tokenSp;

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
		utils = new HttpUtils(1000);
		gson = new Gson();
		mgr = new DBManager(this);
		users = new ArrayList<User>();
		User user1 = new User("tttt", "123456");
		users.add(user1);
		// mgr.addUser(users);
		initView();
		initData();

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

	}

	private void initData() {
		userName = edit_user.getText().toString().trim();
		passWord = edit_pw.getText().toString().trim();

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

				/**
				 * 用户登录POST请求服务器，验证用户名和密码是否正确 登陆成功返回token 时间：2016年10月20日09:45:42
				 */
				RequestParams params = new RequestParams();
				params.addHeader("Content-Type", "application/json");
				params.addHeader("Accept", "text/plain");
				params.addHeader("client_id", "1");
				CjUser userDTO = new CjUser();
				userDTO.setAccount(edit_user.getText().toString().trim());
				userDTO.setName("");
				userDTO.setArea("");
				userDTO.setPwd(edit_pw.getText().toString().trim());

				String jsonStr = gson.toJson(userDTO);

				params.setBodyEntity(new StringEntity(jsonStr, "utf-8"));

				utils.send(HttpMethod.POST, RcConstant.loginPath, params, new RequestCallBack<String>() {
					// 请求失败调用次方法

					@Override
					public void onFailure(HttpException error, String msg) {
						int exceptionCode = error.getExceptionCode();
						if (exceptionCode == 0) {
							ToastUtil.showShort(getApplicationContext(), "请检查网络连接是否正常！");
						} else if (exceptionCode == 406) {
							ToastUtil.showShort(getApplicationContext(), "用户名或密码错误！");

						}
					}

					// 请求成功调用此方法
					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {

						/** 获取服务器返回的Token，并储存到SP中 */
						String token = responseInfo.result;
						tokenSp = getSharedPreferences("Token", MODE_PRIVATE);
						tokenSp.edit().putString("token", token).commit();
						System.out.println("输出结果为" + token);

						/** --------进入选择页面-------- */
						enterInfo();
						ToastUtil.showShort(getApplicationContext(), "登陆成功！");
					}
				});

			}
		});

	}

	/**
	 * 获取用户的详细个人信息 时间：2016年10月20日14:18:03
	 *
	 */

	// 2016年10月19日14:36:23

	protected void enterInfo() {

		Intent intent = new Intent(this, MainActivity2.class);
		startActivity(intent);
		finish();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// 应用的最后一个Activity关闭时应释放DB

	}

}