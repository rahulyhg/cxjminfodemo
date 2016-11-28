package com.neuqsoft.cxjmcj;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.entity.StringEntity;

import com.dou361.dialogui.DialogUIUtils;
import com.dou361.dialogui.config.BuildBean;
import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.neuqsoft.cxjmcj.db.DBManager;
import com.neuqsoft.cxjmcj.dto.User;
import com.neuqsoft.cxjmcj.server.dto.CjUser;
import com.neuqsoft.cxjmcj.utils.ToastUtil;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @Title LoginActivity
 * @author tengzj
 * @data 2016年8月23日 下午4:52:12
 */
public class LoginActivity extends Activity {

	private DBManager mgr;
	private EditText edit_user;
	private EditText edit_pw;
	private ArrayList<User> users;
	private String query_usern;
	private TextView btn_login;
	private SharedPreferences sp;
	private Gson gson;
	private HttpUtils utils;
	private String userName;
	private String passWord;

	private SharedPreferences tokenSp;
	Activity activity;
	Context context;
	BuildBean build;

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
		context = getApplication();
		setContentView(R.layout.activity_login);
		DialogUIUtils.init(context);
		Intent intent = getIntent();
		activity = this;
		utils = new HttpUtils(3000);
		gson = new Gson();
		mgr = new DBManager(this);
		initView();
		initData();
	}

	/*
	 * （1）输入用户名、密码 （2）无需注册功能 ，一个地区对应一个用户名、密码 （3）登录安全性的考虑，密码用MD5加密传输
	 * （4）实现用户系统注销功能（首页注销功能） （5）登录后，自动将当前用户信息保存到sqlite中
	 */

	private void initView() {
		edit_user = (EditText) findViewById(R.id.edit_user);
		edit_pw = (EditText) findViewById(R.id.edit_pw);
		btn_login = (TextView) findViewById(R.id.btn_login);

	}

	private void initData() {
		sp = getSharedPreferences("LoginFlag", MODE_PRIVATE);
		/** 登陆 */
		btn_login.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				userName = edit_user.getText().toString().trim();
				passWord = edit_pw.getText().toString().trim();
				/** 网络登陆 */
				showloading();
				try {
					loginfromnet();
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		});

	}

	protected void loginfromnet() throws UnsupportedEncodingException {
		// TODO Auto-generated method stub
		/**
		 * 用户登录POST请求服务器，验证用户名和密码是否正确 登陆成功返回token 时间：2016年10月20日09:45:42
		 */
		RequestParams params = new RequestParams();
		params.addHeader("Content-Type", "application/json");
		params.addHeader("Accept", "application/json");
		params.addHeader("client_id", "1");
		CjUser userDTO = new CjUser();
		userDTO.setAccount(userName);
		userDTO.setName("");
		userDTO.setArea("");
		userDTO.setPwd(passWord);

		String jsonStr = gson.toJson(userDTO);

		params.setBodyEntity(new StringEntity(jsonStr, "utf-8"));
		utils.send(HttpMethod.POST, RcConstant.loginPath, params, new RequestCallBack<String>() {
			// 请求失败调用次方法

			@Override
			public void onFailure(HttpException error, String msg) {
				int exceptionCode = error.getExceptionCode();
				if (exceptionCode == 0) {
					// showloading();
					loginfromlocal();
				} else if (exceptionCode == 500) {
					waitToast("用户名或密码错误，请重新输入！");
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
				// showloading();
				enterInfo(0);
				insertUser();
			}
		});

	}

	protected void loginfromlocal() {
		int quer = mgr.Quer(passWord, userName);
		if (quer == 1) {
			enterInfo(1);
		} else if (quer == -1) {

			waitToast("密码错误，请重新输入！");

		} else if (quer == 0) {
			// 4S后登录失败
			waitToast("登录失败，请检查网络！");

		}
	}

	private void waitToast(final String msg) {
		// TODO Auto-generated method stub
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				// 登录等待2S
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				// 必须加在UI线程中 不然报错
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						// TODO Auto-generated method stub
						Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
					}

				});
			}

		}).start();
	}

	/**
	 * 本地登录 时间：2016年11月8日16:27:45
	 */

	protected void showloading() {
		activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				build = DialogUIUtils.showLoadingHorizontal(activity, "登录中...");
				build.show();
				delay(2000);
			}
		});
	}

	/** 把登陆信息存到数据库 */
	private void insertUser() {
		users = new ArrayList<User>();
		User user1 = new User(userName, passWord);
		users.add(user1);
		String query_user = mgr.query_user(getApplicationContext(), userName);
		if (query_user.isEmpty()) {
			mgr.addUser(users);
		}

	}

	/**
	 * 获取用户的详细个人信息 时间：2016年10月20日14:18:03
	 *
	 */

	// 2016年10月19日14:36:23

	protected void enterInfo(final int info) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					Thread.sleep(1500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				build.dialog.dismiss();
				Intent intent = new Intent(activity, MainActivity.class);
				intent.putExtra("userName", userName);
				intent.putExtra("info", info);
				startActivity(intent);
				finish();
			}
		}).start();
	}

	protected void delay(final int time) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					Thread.sleep(time);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				build.dialog.dismiss();
			}
		}).start();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// 应用的最后一个Activity关闭时应释放DB

	}

}