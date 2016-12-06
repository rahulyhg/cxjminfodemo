package com.megvii.idcardproject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import com.dou361.dialogui.DialogUIUtils;
import com.dou361.dialogui.config.BuildBean;
import com.megvii.idcardproject.adapter.MyAdapterMainActivity;
import com.megvii.idcardproject.db.DBManager;
import com.megvii.idcardproject.dto.UserDetail;
import com.megvii.idcardproject.utils.HttpManager;
import com.megvii.idcardproject.utils.ToastUtil;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.TextView;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;

@SuppressLint("HandlerLeak")
public class MainActivity extends Activity {
	ListView listview;
	// image_sjsc
	public static final int CBDJ = 101;
	static MyAdapterMainActivity adapter;
	static int pos;
	private TextView title_logout, title_local, title_num;

	Activity activity;
	String sToken;
	DBManager db;
	// 当前listview位置
	static int itemIndex;
	static List<UserDetail> queryUserDetail = new ArrayList<UserDetail>();
	private String account;
	BuildBean build;
	HttpManager http;
	Thread downData;
	Dialog dialog;

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

					downData = new Thread(new Runnable() {
						@Override
						public void run() {
							// TODO Auto-generated method stub
							try {
								GetUserTask();
								// 有任务
								queryUserDetail = db.queryUserDetail(account);
								if (queryUserDetail.size() > 0) {
									// 获得代码表
									if (db.queryCode("AAC058").size() == 0)
										GetCode();
									if (!http.isError) {
										// 获得行政区划信息
										GetXzqh();
										if (!http.isError)
											activity.runOnUiThread(new Runnable() {
												@Override
												public void run() {
													UpdateView(queryUserDetail);
													/** --------设置标题栏的数据-------------- */
													InitHeader();
												}
											});
									}
								} else {
									// 无任务
									activity.runOnUiThread(new Runnable() {
										@Override
										public void run() {
											serverError();
										}
									});
								}
								// 等待加载完
								Thread.sleep(500);
							} catch (UnsupportedEncodingException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							build.dialog.dismiss();
						}
					});
					downData.start();
				}
			});
		} else {
			this.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					build = DialogUIUtils.showLoadingHorizontal(activity, "离线登录成功，加载中...", false, false, true);
					build.show();
					queryUserDetail = db.queryUserDetail(account);
					UpdateView(queryUserDetail);
					InitHeader();
					delay(1500);
				}
			});
		}
	}

	private void UpdateView(List<UserDetail> queryUserDetail) {
		/** 设置适配器 */
		adapter = new MyAdapterMainActivity(activity, queryUserDetail, sToken);
		listview.setAdapter(adapter);
		adapter.notifyDataSetChanged();
		// 加载完后隐藏加载框
	}

	private void InitView() {
		/** --------初始化布局----------------- */
		title_logout = (TextView) findViewById(R.id.title_logout);
		title_local = (TextView) findViewById(R.id.title_local);
		title_num = (TextView) findViewById(R.id.title_num);
		listview = (ListView) findViewById(R.id.listView);

		/** --------注销返回到登陆界面-------------- */
		title_logout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Logout("注销中...");
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
			build = DialogUIUtils.showLoadingHorizontal(activity, "加载中...", false, false, true);
			build.show();
			delay(1500);
			queryUserDetail = db.queryUserDetail(account);
			UpdateView(queryUserDetail);
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

	public void Logout(String info) {
		build = DialogUIUtils.showLoadingHorizontal(activity, info);
		build.show();
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

	@SuppressWarnings("deprecation")
	public void serverError() {
		dialog = new SweetAlertDialog(activity, SweetAlertDialog.ERROR_TYPE).setTitleText("在线登录失败")
				.setCancelText("注销重试").setConfirmText("离线登录")
				.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
					@Override
					public void onClick(SweetAlertDialog sDialog) {
						activity.runOnUiThread(new Runnable() {
							@Override
							public void run() {
								// TODO Auto-generated method stub
								build = DialogUIUtils.showLoadingHorizontal(activity, "离线登录成功，加载中...", false, false,
										true);
								build.show();
								if (db.queryCode("AAC058").size() == 0 || db.queryUserDetail(account).size() == 0) {
									Logout("无配置数据，注销中...");
								} else {
									queryUserDetail = db.queryUserDetail(account);
									UpdateView(queryUserDetail);
									InitHeader();
									delay(1500);
								}
							}
						});
						sDialog.dismissWithAnimation();
					}
				}).setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
					@Override
					public void onClick(SweetAlertDialog sDialog) {
						Logout("注销中...");
					}
				});
		dialog.show();
	}

	public void GetCode() throws UnsupportedEncodingException {
		UserDetail userDetail = queryUserDetail.get(0);
		http.getCode("AAC058", userDetail.getCjarea());
		while (http.isAlive) {
		}
		if (http.isError) {
			build.dialog.dismiss();
			activity.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					serverError();
				}
			});
		} else {
			http.getCode("AAC005", userDetail.getCjarea());
			while (http.isAlive) {
			}
			http.getCode("AAC004", userDetail.getCjarea());
			while (http.isAlive) {
			}
			http.getCode("BAC067", userDetail.getCjarea());
			while (http.isAlive) {
			}
			http.getCode("AAC069", userDetail.getCjarea());
			while (http.isAlive) {
			}
			http.getCode("AAC009", userDetail.getCjarea());
			while (http.isAlive) {
			}
		}
	}

	public void InitHeader() {
		String city = queryUserDetail.get(0).getCity().toString();
		title_local.setText(city);
		title_num.setText("(共" + queryUserDetail.size() + "地区)");
	}

	public void GetXzqh() throws UnsupportedEncodingException {
		for (UserDetail userDetail : queryUserDetail) {
			http.getXzqh(userDetail.getCjarea());
			while (http.isAlive) {
				// 等待行政区划下载完
			}
			if (http.isError) {
				build.dialog.dismiss();
				activity.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						serverError();
					}
				});
				return;
			}
		}
	}

	public void GetUserTask() {
		// 获得userTask信息
		http.getUserDetail(sToken);
		while (http.isAlive) {
			// 用户任务下载完
		}
		if (http.isError) {
			build.dialog.dismiss();
			activity.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					serverError();
				}
			});
		}
	}
}
