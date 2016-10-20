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
 * @data 2016��8��23�� ����4:52:12
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
	 * ��1�������û��������� ��2������ע�Ṧ�� ��һ�����Ӧһ���û��������� ��3����¼��ȫ�ԵĿ��ǣ�������MD5���ܴ���
	 * ��4��ʵ���û�ϵͳע�����ܣ���ҳע�����ܣ� ��5����¼���Զ�����ǰ�û���Ϣ���浽sqlite��
	 */

	private void initView() {
		edit_user = (EditText) findViewById(R.id.edit_user);
		edit_pw = (EditText) findViewById(R.id.edit_pw);
		image_left = (ImageView) findViewById(R.id.image_left);
		btn_login = (TextView) findViewById(R.id.btn_login);
		/*********** ���жϵ�ǰ�Ƿ��Ѿ���¼�����ѵ�¼��ִ��ע������ ***********/
		SharedPreferences spIsLongin = getSharedPreferences("IsLongin", MODE_PRIVATE);
		String strLoginFlag = spIsLongin.getString("IsLongin", "");
		// if (strLoginFlag.equals("1")) {
		// logOff();
		// spIsLongin.edit().putString("IsLongin", "0").commit();// ���ĵ�¼/ע����־λ
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
		/** ��½ */
		btn_login.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// �ж��û����Ƿ���������ݿ���
				if (!mgr.query_usern(getApplicationContext(), edit_user.getText().toString()).isEmpty()) {
					// ��MD5��������ܲ��ж������Ƿ���ȷ
					if (MD5Util.encode(edit_pw.getText().toString()).equals(MD5Util.encode("111111"))) {

						enterInfo();
						ToastUtil.showShort(getApplicationContext(), "��½�ɹ���");
						sp.edit().putString("LoginFlag", "1").commit();

					} else {
						ToastUtil.showShort(getApplicationContext(), "�������");
					}
				} else {
					ToastUtil.showShort(getApplicationContext(), "�û��������ڣ�");
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
	// ToastUtil.showShort(getApplicationContext(), "����ʧ��");
	//
	// }
	//
	// @Override
	// public void onSuccess(ResponseInfo<String> responseInfo) {
	// CjTask cjtaskInfo = gson.fromJson(responseInfo.result, CjTask.class);
	// ToastUtil.showShort(getApplicationContext(), "����ɹ�������");
	// System.out.println("�Ñ���Ϣ������������������������������������������������������������" + cjtaskInfo);
	//
	//
	// }
	// });
	//
	// }

	// 2016��10��19��14:36:23
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
				ToastUtil.showShort(getApplicationContext(), "����ʧ��");

			}
           
			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				String token = responseInfo.result;
				System.out.println("������Ϊ" + responseInfo.result);
				ToastUtil.showShort(getApplicationContext(), "����ɹ�");
                
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
		// Ӧ�õ����һ��Activity�ر�ʱӦ�ͷ�DB

	}

}