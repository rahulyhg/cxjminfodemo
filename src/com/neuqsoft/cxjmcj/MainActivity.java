package com.neuqsoft.cxjmcj;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.dou361.dialogui.DialogUIUtils;
import com.dou361.dialogui.config.BuildBean;
import com.dou361.dialogui.listener.DialogUIListener;
import com.neuqsoft.cxjmcj.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.neuqsoft.cxjmcj.InfoActivity.InfoMainActivity;
import com.neuqsoft.cxjmcj.adapter.MyAdapterMainActivity;
import com.neuqsoft.cxjmcj.adapter.MyAdapterMainActivity.ViewHolder;
import com.neuqsoft.cxjmcj.db.DBManager;
import com.neuqsoft.cxjmcj.dto.Family;
import com.neuqsoft.cxjmcj.dto.Personal;
import com.neuqsoft.cxjmcj.dto.User;
import com.neuqsoft.cxjmcj.dto.UserDetail;
import com.neuqsoft.cxjmcj.dto.Xzqh;
import com.neuqsoft.cxjmcj.server.dto.FamilyMemberDTO;
import com.neuqsoft.cxjmcj.server.dto.MemberDTO;
import com.neuqsoft.cxjmcj.utils.HttpManager;
import com.neuqsoft.cxjmcj.utils.TextToMap;
import com.neuqsoft.cxjmcj.utils.ToastUtil;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;
import info.hoang8f.widget.FButton;

@SuppressLint("HandlerLeak")
public class MainActivity extends Activity {
	ListView listview;
	// image_sjsc
	public static final int CBDJ = 101;
	MyAdapterMainActivity adapter;
	static int pos;
	private TextView title_logout, text_user, title_local, title_num;

	Activity activity;
	String sToken;
	DBManager db;
	// 当前listview位置
	static int itemIndex;
	private List<UserDetail> queryUserDetail;
	private String account;
	BuildBean build;
	HttpManager http;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		activity = this;
		http = new HttpManager(activity);
		db = new DBManager(this);
		InitView();
		ButterKnife.bind(MainActivity.this);
		// 取值
		Intent intent = getIntent();
		account = intent.getStringExtra("userName");
		SharedPreferences tokenSp = getSharedPreferences("Token", MODE_PRIVATE);
		sToken = tokenSp.getString("token", "");
		int info = intent.getIntExtra("info", -1);
		loginFeedback(info);
	}

	public void loginFeedback(int info) {
		if (info == 0) {
			// 在线登陆
			this.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					build = DialogUIUtils.showLoadingHorizontal(activity, "在线登录成功，加载中...", false, false, true);
					build.show();
					// 获得代码表
					new Thread(new Runnable() {
						@Override
						public void run() {
							// TODO Auto-generated method stub
							try {
								// 获得代码表
								http.getCode("AAC058");
								http.getCode("AAC005");
								http.getCode("AAC004");
								http.getCode("BAC067");
								http.getCode("AAC069");
								http.getCode("AAC009");
								// 获得userTask信息
								http.getUserDetail(sToken);
								while (http.isAlive) {
								}
								if (http.isError) {
									build.dialog.dismiss();
									Dialog dialog;
									dialog = new SweetAlertDialog(activity, SweetAlertDialog.ERROR_TYPE)
											.setTitleText("服务器异常，请重试");
									dialog.show();
									finish();
								} else {
									// 获得行政区划信息
									List<UserDetail> userDetails = db.queryUserDetail(account);
									for (UserDetail userDetail : userDetails) {
										http.getXzqh(userDetail.getCjarea());
									}
								}
							} catch (UnsupportedEncodingException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							activity.runOnUiThread(new Runnable() {
								@Override
								public void run() {
									UpdateView();
									/** --------设置标题栏的数据-------------- */
									String city = queryUserDetail.get(0).getCity().toString();
									title_local.setText(city);
									title_num.setText("（共" + queryUserDetail.size() + "地区）");
									text_user.setText(account);
								}
							});
							try {
								Thread.sleep(500);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							build.dialog.dismiss();
						}
					}).start();
				}
			});
		} else {
			this.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					build = DialogUIUtils.showLoadingHorizontal(activity, "离线登录成功，加载中...", false, false, true);
					build.show();
					UpdateView();
					delay(1500);
				}
			});
		}
	}

	private void UpdateView() {
		queryUserDetail = db.queryUserDetail(account);
		/** 设置适配器 */
		adapter = new MyAdapterMainActivity(activity, queryUserDetail, sToken);
		listview.setAdapter(adapter);
		adapter.notifyDataSetChanged();
		// 加载完后隐藏加载框
	}

	private void InitView() {
		/** --------初始化布局----------------- */
		title_logout = (TextView) findViewById(R.id.title_logout);
		text_user = (TextView) findViewById(R.id.text_user);
		title_local = (TextView) findViewById(R.id.title_local);
		title_num = (TextView) findViewById(R.id.title_num);
		listview = (ListView) findViewById(R.id.listView);

		/** --------注销返回到登陆界面-------------- */
		title_logout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				build = DialogUIUtils.showLoadingHorizontal(activity, "注销中...");
				build.show();
				new Thread(new Runnable() {
					@Override
					public void run() {
						// TODO Auto-generated method stub
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						build.dialog.dismiss();
						activity.runOnUiThread(new Runnable() {
							@Override
							public void run() {
								// TODO Auto-generated method stub
								ToastUtil.showShort(getApplicationContext(), "注销成功！");
							}
						});
						Intent intent = new Intent(MainActivity.this, LoginActivity.class);
						startActivity(intent);
						finish();
					}
				}).start();
			}
		});
	}

	private void setSimulateClick(View view, float x, float y) {
		long downTime = SystemClock.uptimeMillis();
		final MotionEvent downEvent = MotionEvent.obtain(downTime, downTime, MotionEvent.ACTION_DOWN, x, y, 0);
		downTime += 1000;
		final MotionEvent upEvent = MotionEvent.obtain(downTime, downTime, MotionEvent.ACTION_UP, x, y, 0);
		view.onTouchEvent(downEvent);
		view.onTouchEvent(upEvent);
		downEvent.recycle();
		upEvent.recycle();
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) { // resultCode为回传的标记，我在B中回传的是RESULT_OK
		case CBDJ:
			adapter.notifyDataSetChanged();
		}
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
}
