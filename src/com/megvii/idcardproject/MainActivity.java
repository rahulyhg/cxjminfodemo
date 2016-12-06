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
								GetUserTask();
								// ������
								queryUserDetail = db.queryUserDetail(account);
								if (queryUserDetail.size() > 0) {
									// ��ô����
									if (db.queryCode("AAC058").size() == 0)
										GetCode();
									if (!http.isError) {
										// �������������Ϣ
										GetXzqh();
										if (!http.isError)
											activity.runOnUiThread(new Runnable() {
												@Override
												public void run() {
													UpdateView(queryUserDetail);
													/** --------���ñ�����������-------------- */
													InitHeader();
												}
											});
									}
								} else {
									// ������
									activity.runOnUiThread(new Runnable() {
										@Override
										public void run() {
											serverError();
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
		title_num.setText("(��" + queryUserDetail.size() + "����)");
	}

	public void GetXzqh() throws UnsupportedEncodingException {
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
				return;
			}
		}
	}

	public void GetUserTask() {
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
		}
	}
}
