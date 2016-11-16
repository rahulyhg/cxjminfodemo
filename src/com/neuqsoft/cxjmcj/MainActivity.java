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
import com.neuqsoft.cxjmcj.MainActivity.MyAdapter.ViewHolder;
import com.neuqsoft.cxjmcj.db.DBManager;
import com.neuqsoft.cxjmcj.dto.Family;
import com.neuqsoft.cxjmcj.dto.Personal;
import com.neuqsoft.cxjmcj.dto.User;
import com.neuqsoft.cxjmcj.server.dto.FamilyMemberDTO;
import com.neuqsoft.cxjmcj.server.dto.MemberDTO;
import com.neuqsoft.cxjmcj.server.dto.UserDetail;
import com.neuqsoft.cxjmcj.utils.HttpManager;
import com.neuqsoft.cxjmcj.utils.TextToMap;
import com.neuqsoft.cxjmcj.utils.ToastUtil;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
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
	MyAdapter adapter;
	static Map<String, String> oldMap;
	static int pos;
	private TextView title_logout, text_user, title_local, title_num;
	private List<UserDetail> list;
	Activity activity;
	String sToken;
	DBManager db;
	// 当前listview位置
	static int itemIndex;
	private List<UserDetail> queryUserDetail;
	private String userName;
	BuildBean build;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		activity = this;
		// 取值
		Intent intent = getIntent();
		userName = intent.getStringExtra("userName");
		int info = intent.getIntExtra("info", -1);
		if (info == 0) {
			this.runOnUiThread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					build = DialogUIUtils.showLoadingHorizontal(activity, "在线登录成功，加载中...");
					build.show();
					delay(3000);
				}
			});
		} else {
			this.runOnUiThread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					build = DialogUIUtils.showLoadingHorizontal(activity, "离线登录成功，加载中...");
					build.show();
					delay(3000);
				}
			});
		}

		db = new DBManager(this);
		ButterKnife.bind(MainActivity.this);
		/** ------请求数据---------------- */
		getDataFromNet();

		/** --------初始化布局----------------- */
		title_logout = (TextView) findViewById(R.id.title_logout);
		text_user = (TextView) findViewById(R.id.text_user);
		title_local = (TextView) findViewById(R.id.title_local);
		title_num = (TextView) findViewById(R.id.title_num);
		listview = (ListView) findViewById(R.id.listView);
		/** --------初始化数据----------------- */

		InputStream inputStream = getResources().openRawResource(R.raw.countrycode);
		oldMap = new TextToMap().TextToMap(inputStream);
		/** ------从本地请求数据---------------- */
		getDataFromlocal();
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

	private void getDataFromlocal() {
		queryUserDetail = db.queryUserDetail(userName);
		/** 设置适配器 */
		adapter = new MyAdapter(queryUserDetail);
		listview.setAdapter(adapter);
		adapter.notifyDataSetChanged();
		// 加载完后隐藏加载框
	}

	/**
	 * ----从SP中取出token值--- ---请求服务器 --
	 */
	private void getDataFromNet() {
		HttpUtils httpUtils = new HttpUtils();
		httpUtils.configCurrentHttpCacheExpiry(0);
		SharedPreferences tokenSp = getSharedPreferences("Token", MODE_PRIVATE);
		sToken = tokenSp.getString("token", "");
		RequestParams params1 = new RequestParams();
		params1.addHeader("token", sToken);
		params1.addHeader("Content-Type", "application/json;charset=utf-8");
		params1.addHeader("Accept", "*/*");
		params1.addHeader("client_id", "1");
		httpUtils.send(HttpMethod.GET, RcConstant.usertasksPath, params1, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException error, String msg) {
			}

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				String result = responseInfo.result;
				getInfoData(result);
			}
		});
	}

	/** 获取用户的详细信息并添加到list中--- */
	protected void getInfoData(String result) {
		Gson gson = new Gson();
		list = gson.fromJson(result, new TypeToken<List<UserDetail>>() {
		}.getType());

		/**
		 * 判断user表中是否有此条数据 没有就添加进去
		 */
		String account = list.get(1).getAccount();
		String query_usern = db.query_usern(this, account);
		if (query_usern.isEmpty()) {
			db.addUserDetail(list);
		}
		/** ------从本地请求数据---------------- */
		getDataFromlocal();

	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) { // resultCode为回传的标记，我在B中回传的是RESULT_OK
		case CBDJ:
			final ViewHolder holder = adapter.getViewHolder(pos);
			holder.upload.setVisibility(View.VISIBLE);
			((Activity) activity).runOnUiThread(new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					int memberSize = 0;
					int memberJf = 0;
					List<Family> familys = db.queryFamily();
					holder.num1.setText(familys.size() + "");
					for (Family family : familys) {
						List<Personal> personals = db.queryPersonal(family.getEdit_gmcfzh());
						memberSize = memberSize + personals.size();
						for (Personal personal : personals) {
							if (!personal.getEdit_jf().equals("0")) {
								memberJf = memberJf + 1;
							}
						}
					}
					holder.num2.setText(memberSize + "");
					holder.num3.setText(memberJf + "");
				}
			});
		}
	}

	public class MyAdapter extends BaseAdapter {

		/* 存放控件 的ViewHolder */
		public final class ViewHolder {
			public TextView num1;
			public TextView num2;
			public TextView num3;
			public TextView local;
			FButton upload;
			FButton download;
			private ImageView upload2;
			public TextView text_num;

			private ImageView list;

			LinearLayout top1;
			LinearLayout top;
			LinearLayout center;
			ImageView bottom;
		}

		private LayoutInflater mInflater; // 得到一个LayoutInfalter对象用来导入布局
		private List<UserDetail> queryUserDetail;
		private String cjarea2;

		public MyAdapter(List<UserDetail> queryUserDetail) {
			super();
			this.mInflater = LayoutInflater.from(getBaseContext());
			this.queryUserDetail = queryUserDetail;
		}

		@Override
		public int getCount() {

			return queryUserDetail.size();
		}

		@Override
		public Object getItem(int position) {
			return queryUserDetail.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		public ViewHolder getViewHolder(int position) {
			ViewHolder holder;
			int firstVisible = listview.getFirstVisiblePosition();
			int index = position + listview.getHeaderViewsCount();
			View temp = listview.getChildAt(index - firstVisible);
			holder = (ViewHolder) temp.getTag();
			return holder;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			final ViewHolder holder;
			final HttpManager http = new HttpManager(activity);
			Log.v("BaseAdapterTest", "getView " + position + " " + convertView);
			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.view_row, null);
				holder = new ViewHolder();
				/* 得到各个控件的对象 */
				holder.text_num = (TextView) convertView.findViewById(R.id.text_num);
				holder.num1 = (TextView) convertView.findViewById(R.id.num1);
				holder.num2 = (TextView) convertView.findViewById(R.id.num2);
				holder.num3 = (TextView) convertView.findViewById(R.id.num3);
				holder.local = (TextView) convertView.findViewById(R.id.local);

				holder.upload = (FButton) convertView.findViewById(R.id.buttom_up);
				holder.download = (FButton) convertView.findViewById(R.id.buttom_down);
				holder.upload2 = (ImageView) convertView.findViewById(R.id.buttom_up2);

				holder.top1 = (LinearLayout) convertView.findViewById(R.id.top1);
				holder.top = (LinearLayout) convertView.findViewById(R.id.top);
				holder.list = (ImageView) convertView.findViewById(R.id.list);

				holder.center = (LinearLayout) convertView.findViewById(R.id.center);
				holder.bottom = (ImageView) convertView.findViewById(R.id.bottom);

				convertView.setTag(holder); // 绑定ViewHolder对象
			} else {
				holder = (ViewHolder) convertView.getTag(); // 取出ViewHolder对象
			}
			/** --------设置标题栏的数据-------------- */
			String city = queryUserDetail.get(0).getCity().toString();
			title_local.setText(city);
			title_num.setText("（共" + queryUserDetail.size() + "村）");
			final String account = queryUserDetail.get(0).getAccount();
			text_user.setText(account);

			/** 把乡镇代码转换形成乡镇 名称 */
			final String cjarea = queryUserDetail.get(position).getCjarea();
			for (String key : oldMap.keySet()) {
				if (key.equals(cjarea))
					holder.local.setText(oldMap.get(key));
			}

			final Handler handler = new Handler() {
				public void handleMessage(Message msg) {
					// 下载失败
					if (msg.what == 0) {
						((Activity) activity).runOnUiThread(new Runnable() {
							@Override
							public void run() {
								// TODO Auto-generated method stub
								http.isAlive = true;
								new SweetAlertDialog(activity, SweetAlertDialog.ERROR_TYPE).setTitleText("下载失败...")
										.show();
							}
						});
					}
					if (msg.what == 1) {
						((Activity) activity).runOnUiThread(new Runnable() {
							@Override
							public void run() {
								// TODO Auto-generated method stub
								http.isAlive = true;
							}
						});
					}
					// 上传失败
					if (msg.what == 2) {
						((Activity) activity).runOnUiThread(new Runnable() {
							@Override
							public void run() {
								// TODO Auto-generated method stub
								http.isAlive = true;
								new SweetAlertDialog(activity, SweetAlertDialog.ERROR_TYPE).setTitleText("上传失败...")
										.show();
							}
						});
					}
					// 上传成功
					if (msg.what == 3) {
						/* sendMessage方法更新UI的操作必须在handler的handleMessage回调中完成 */
						((Activity) activity).runOnUiThread(new Runnable() {
							@Override
							public void run() {
								// TODO Auto-generated method stub
								http.isAlive = true;
								holder.upload2.setVisibility(View.VISIBLE);
								holder.download.setButtonColor(Color.rgb(204, 204, 204));
								holder.upload.setButtonColor(Color.rgb(204, 204, 204));

								holder.download.setShadowEnabled(false);
								holder.upload.setShadowEnabled(false);

								holder.download.setClickable(false);
								holder.upload.setClickable(false);

								holder.top1.setBackgroundResource(R.drawable.top31);
								holder.top.setBackgroundResource(R.drawable.top3);
								holder.list.setImageResource(R.drawable.list3);
								holder.list.setScaleType(ImageView.ScaleType.FIT_XY);

								holder.center.setBackgroundResource(R.drawable.center3);
								holder.bottom.setImageResource(R.drawable.bottom3);
							}
						});
					}
					// 下载成功 并更新
					if (msg.what == 4) {
						/* sendMessage方法更新UI的操作必须在handler的handleMessage回调中完成 */
						((Activity) activity).runOnUiThread(new Runnable() {
							@Override
							public void run() {
								// TODO Auto-generated method stub
								int memberSize = 0;
								int memberJf = 0;
								List<Family> familys = db.queryFamily();
								holder.num1.setText(familys.size() + "");
								for (Family family : familys) {
									List<Personal> personals = db.queryPersonal(family.getEdit_gmcfzh());
									memberSize = memberSize + personals.size();
									for (Personal personal : personals) {
										if (!personal.getEdit_jf().equals("0")) {
											memberJf = memberJf + 1;
										}
									}
								}
								holder.num2.setText(memberSize + "");
								holder.num3.setText(memberJf + "");
								holder.upload.setVisibility(View.VISIBLE);
								holder.download.setText("录 入");
								holder.download.setButtonColor(Color.rgb(237, 152, 17));

								holder.top1.setBackgroundResource(R.drawable.top21);
								holder.top.setBackgroundResource(R.drawable.top2);
								holder.list.setImageResource(R.drawable.list2);
								holder.list.setScaleType(ImageView.ScaleType.FIT_XY);

								holder.center.setBackgroundResource(R.drawable.center2);
								holder.bottom.setImageResource(R.drawable.bottom2);
							}
						});
					}

				};

			};

			int posi = position + 1;
			holder.text_num.setText(posi + "");
			holder.upload2.setVisibility(View.GONE);
			holder.upload.setVisibility(View.GONE);

			// 判断数据是否下载过,
			for (UserDetail userDetail : queryUserDetail) {
				cjarea2 = userDetail.getCjarea();
				if (cjarea.equals(cjarea2)) {
					// 是否已下載
					if (userDetail.downloadflag.equals("1")) {
						holder.upload.setVisibility(View.VISIBLE);
						holder.download.setText("录 入");
						holder.download.setButtonColor(Color.rgb(237, 152, 17));
						// 显示数据
						int memberSize = 0;
						int memberJf = 0;
						List<Family> familys = db.queryFamily();
						holder.num1.setText(familys.size() + "");
						for (Family family : familys) {
							List<Personal> personals = db.queryPersonal(family.getEdit_gmcfzh());
							memberSize = memberSize + personals.size();
							for (Personal personal : personals) {
								if (!personal.getEdit_jf().equals("0")) {
									memberJf = memberJf + 1;
								}
							}
						}
						holder.num2.setText(memberSize + "");
						holder.num3.setText(memberJf + "");
						handler.sendEmptyMessage(4);
					}
					// 判断已经上传
					if (userDetail.uploadflag.equals("1")) {
						handler.sendEmptyMessage(3);
					}
				}
			}

			// 50旋转 100录入 0上传

			final Runnable down_run = new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					http.getJbxx(cjarea);
					try {
						Thread.sleep(1500);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					while (http.isAlive) {
					}
					if (http.isError) {
						handler.sendEmptyMessage(0);
					} else
						// 下载成功
						handler.sendEmptyMessage(4);
					build.dialog.dismiss();
				}
			};

			final Runnable up_run = new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					try {
						http.getCjxx(cjarea, sToken, account);
					} catch (UnsupportedEncodingException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}

					try {
						Thread.sleep(1500);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					while (http.isAlive) {
					}
					if (http.isError) {
						handler.sendEmptyMessage(2);
					} else
						// 上传成功
						handler.sendEmptyMessage(3);
					build.dialog.dismiss();
				}
			};

			holder.download.setOnClickListener(new View.OnClickListener() {
				@SuppressWarnings("deprecation")
				@Override
				public void onClick(View v) {

					if (holder.download.getText().toString().equals("下 载")) {
						// 加载框
						((MainActivity) activity).runOnUiThread(new Runnable() {
							@Override
							public void run() {
								// TODO Auto-generated method stub
								DialogUIUtils.init(activity);
								build = DialogUIUtils.showLoadingHorizontal(activity, "正在下载...");
								build.show();
							}
						});

						// 下载线程
						new Thread(down_run).start();
					} else {
						// 录入
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								delay(800);
								// TODO Auto-generated method stub
								build = DialogUIUtils.showLoadingHorizontal(activity, "加载中...");
								build.show();
							}
						});
						Intent intent = new Intent(MainActivity.this, InfoMainActivity.class);
						intent.putExtra("XZQH", cjarea);
						startActivityForResult(intent, CBDJ);
						pos = position;
					}
				}
			});

			holder.upload.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					new SweetAlertDialog(activity, SweetAlertDialog.WARNING_TYPE).setTitleText("警告")
							.setContentText("每个乡镇仅限上传一次\n上传成功后将无法录入\n").setConfirmText("上传").setCancelText("取消")
							.showCancelButton(true)
							.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
								@Override
								public void onClick(SweetAlertDialog sDialog) {
									sDialog.dismissWithAnimation();
									build = DialogUIUtils.showLoadingHorizontal(activity, "上传中...");
									build.show();
									new Thread(up_run).start();
								}
							})

							.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
								@Override
								public void onClick(SweetAlertDialog sDialog) {
									sDialog.cancel();
								}
							}).show();
				}
			});
			return convertView;
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
