package com.neuqsoft.cxjmcj;

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
import com.neuqsoft.cxjmcj.utils.LoadingDialog;
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
 * @data 2016��8��23�� ����4:52:12
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

		activity = this;
		utils = new HttpUtils(3000);
		gson = new Gson();
		mgr = new DBManager(this);
		// mgr.addUser(users);
		initView();
		initData();
	}

	/*
	 * ��1�������û��������� ��2������ע�Ṧ�� ��һ�����Ӧһ���û��������� ��3����¼��ȫ�ԵĿ��ǣ�������MD5���ܴ���
	 * ��4��ʵ���û�ϵͳע�����ܣ���ҳע�����ܣ� ��5����¼���Զ�����ǰ�û���Ϣ���浽sqlite��
	 */

	private void initView() {
		edit_user = (EditText) findViewById(R.id.edit_user);
		edit_pw = (EditText) findViewById(R.id.edit_pw);
		btn_login = (TextView) findViewById(R.id.btn_login);

	}

	private void initData() {
		sp = getSharedPreferences("LoginFlag", MODE_PRIVATE);
		/** ��½ */
		btn_login.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				userName = edit_user.getText().toString().trim();
				passWord = edit_pw.getText().toString().trim();
				// ��ʾ���ؿ�
				activity.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						// TODO Auto-generated method stub
						build = DialogUIUtils.showLoadingHorizontal(activity, "��½��...");
						build.show();
					}

				});

				/** �����½ */
				loginfromnet();
				//4S���¼ʧ��
				new Thread(new Runnable() {
					@Override
					public void run() {
						// TODO Auto-generated method stub
						// ��¼�ȴ�4S
						try {
							Thread.sleep(4000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						build.dialog.dismiss();
						//�������UI�߳��� ��Ȼ����
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								// TODO Auto-generated method stub
								Toast.makeText(getApplicationContext(), "��¼ʧ�ܣ�������", Toast.LENGTH_SHORT).show();
							}

						});			
					}

				}).start();
			}
		});

	}

	protected void loginfromnet() {
		// TODO Auto-generated method stub
		/**
		 * �û���¼POST�������������֤�û����������Ƿ���ȷ ��½�ɹ�����token ʱ�䣺2016��10��20��09:45:42
		 */
		RequestParams params = new RequestParams();
		params.addHeader("Content-Type", "application/json");
		params.addHeader("Accept", "text/plain");
		params.addHeader("client_id", "1");
		CjUser userDTO = new CjUser();
		userDTO.setAccount(userName);
		userDTO.setName("");
		userDTO.setArea("");
		userDTO.setPwd(passWord);

		String jsonStr = gson.toJson(userDTO);

		params.setBodyEntity(new StringEntity(jsonStr, "utf-8"));
		utils.send(HttpMethod.POST, RcConstant.loginPath, params, new RequestCallBack<String>() {
			// ����ʧ�ܵ��ôη���

			@Override
			public void onFailure(HttpException error, String msg) {
				int exceptionCode = error.getExceptionCode();
				if (exceptionCode == 0) {
					loginfromlocal();
				} else if (exceptionCode == 406) {
					ToastUtil.showShort(getApplicationContext(), "�û������������");
				}
			}

			// ����ɹ����ô˷���
			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {

				/** ��ȡ���������ص�Token�������浽SP�� */
				String token = responseInfo.result;
				tokenSp = getSharedPreferences("Token", MODE_PRIVATE);
				tokenSp.edit().putString("token", token).commit();
				System.out.println("������Ϊ" + token);

				/** --------����ѡ��ҳ��-------- */
				enterInfo("���ߵ�½");
				insertUser();
			}

		});

	}

	/**
	 * ���ص�¼ ʱ�䣺2016��11��8��16:27:45
	 */
	protected void loginfromlocal() {
		List<User> queryUser = mgr.queryUser();
		for (User user : queryUser) {
			if (user.username.equals(userName) && user.password.equals(passWord)) {
				enterInfo("���ߵ�½");
				// } else if (!user.username.equals(userName) ||
				// !user.password.equals(passWord)) {
				// ToastUtil.showShort(this, "�û������������");
			}
		}
	}

	/** �ѵ�½��Ϣ�浽���ݿ� */
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
	 * ��ȡ�û�����ϸ������Ϣ ʱ�䣺2016��10��20��14:18:03
	 *
	 */

	// 2016��10��19��14:36:23

	protected void enterInfo(String info) {
		// LoadingDialog ld = new LoadingDialog(this);
		// ld.show();
		Intent intent = new Intent(this, MainActivity.class);
		intent.putExtra("userName", userName);
		intent.putExtra("info", info);
		startActivity(intent);
		finish();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// Ӧ�õ����һ��Activity�ر�ʱӦ�ͷ�DB

	}

}