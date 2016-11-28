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
	static MyAdapterMainActivity adapter;
	static int pos;
	private TextView title_logout, text_user, title_local, title_num;

	Activity activity;
	String sToken;
	DBManager db;
	// ��ǰlistviewλ��
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
		// ȡֵ
		Intent intent = getIntent();
		account = intent.getStringExtra("userName");
		SharedPreferences tokenSp = getSharedPreferences("Token", MODE_PRIVATE);
		sToken = tokenSp.getString("token", "");
		int info = intent.getIntExtra("info", -1);
		loginFeedback(info);
	}

	public void loginFeedback(int info) {
		if (info == 0) {
			// ���ߵ�½
			this.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					build = DialogUIUtils.showLoadingHorizontal(activity, "���ߵ�¼�ɹ���������...", false, false, true);
					build.show();

					downData = new Thread(new Runnable() {
						@Override
						public void run() {
							// TODO Auto-generated method stub
							try {
								// ��ô����
								if (db.queryCode("AAC058").size() == 0)
									GetCode();
								if (!http.isError) {
									// ���userTask��Ϣ
									http.getUserDetail(sToken);
									while (http.isAlive) {
										// �û�����������
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
										// �������������Ϣ
										GetXzqh();
									}
									activity.runOnUiThread(new Runnable() {
										@Override
										public void run() {
											if (queryUserDetail.size() < 1) {
												// ������
												serverError();
											} else {
												UpdateView(queryUserDetail);
												/** --------���ñ�����������-------------- */
												InitHeader();
											}
										}
									});
								}
								// �ȴ�������
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
					build = DialogUIUtils.showLoadingHorizontal(activity, "���ߵ�¼�ɹ���������...", false, false, true);
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
		/** ���������� */
		adapter = new MyAdapterMainActivity(activity, queryUserDetail, sToken);
		listview.setAdapter(adapter);
		adapter.notifyDataSetChanged();
		// ����������ؼ��ؿ�
	}

	private void InitView() {
		/** --------��ʼ������----------------- */
		title_logout = (TextView) findViewById(R.id.title_logout);
		text_user = (TextView) findViewById(R.id.text_user);
		title_local = (TextView) findViewById(R.id.title_local);
		title_num = (TextView) findViewById(R.id.title_num);
		listview = (ListView) findViewById(R.id.listView);

		/** --------ע�����ص���½����-------------- */
		title_logout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Logout("ע����...");
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
		switch (requestCode) { // resultCodeΪ�ش��ı�ǣ�����B�лش�����RESULT_OK
		case CBDJ:
			build = DialogUIUtils.showLoadingHorizontal(activity, "������...", false, false, true);
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
						ToastUtil.showShort(getApplicationContext(), "ע���ɹ���");
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
		dialog = new SweetAlertDialog(activity, SweetAlertDialog.ERROR_TYPE).setTitleText("���ߵ�¼ʧ��")
				.setCancelText("ע������").setConfirmText("���ߵ�¼")
				.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
					@Override
					public void onClick(SweetAlertDialog sDialog) {
						activity.runOnUiThread(new Runnable() {
							@Override
							public void run() {
								// TODO Auto-generated method stub
								build = DialogUIUtils.showLoadingHorizontal(activity, "���ߵ�¼�ɹ���������...", false, false,
										true);
								build.show();
								if (db.queryCode("AAC058").size() == 0 || db.queryUserDetail(account).size() == 0) {
									Logout("���������ݣ�ע����...");
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
						Logout("ע����...");
					}
				});
		dialog.show();
	}

	public void GetCode() throws UnsupportedEncodingException {
		http.getCode("AAC058");
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
			http.getCode("AAC005");
			while (http.isAlive) {
			}
			http.getCode("AAC004");
			while (http.isAlive) {
			}
			http.getCode("BAC067");
			while (http.isAlive) {
			}
			http.getCode("AAC069");
			while (http.isAlive) {
			}
			http.getCode("AAC009");
			while (http.isAlive) {
			}
		}
	}

	public void InitHeader() {
		String city = queryUserDetail.get(0).getCity().toString();
		title_local.setText(city);
		title_num.setText("(��" + queryUserDetail.size() + "����)");
		text_user.setText(account);
	}

	public void GetXzqh() throws UnsupportedEncodingException {
		queryUserDetail = db.queryUserDetail(account);
		for (UserDetail userDetail : queryUserDetail) {
			http.getXzqh(userDetail.getCjarea());
			while (http.isAlive) {
				// �ȴ���������������
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
}
