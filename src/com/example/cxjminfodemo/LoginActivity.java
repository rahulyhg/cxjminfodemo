/**
 *@filename LoginActivity.java
 *@Email tengzhenjiu@qq.com
 *
 */
package com.example.cxjminfodemo;

import com.example.cxjminfodemo.InfoActivity.InfoMainActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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

	@Bind(R.id.image_left)
	ImageView image_left;

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
		ButterKnife.bind(LoginActivity.this);
	}

	@OnClick(R.id.image_left)
	public void ToMainActivity() {
		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
	}

	@OnClick(R.id.btn_login)
	public void toInfoMainActivity() {
		Intent intent = new Intent(this, InfoMainActivity.class);
		startActivity(intent);
	}
}