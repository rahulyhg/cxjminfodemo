package com.example.cxjmcj;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.andexert.expandablelayout.library.ExpandableLayoutListView;
import com.example.cxjmcj.InfoActivity.InfoMainActivity;
import com.example.cxjmcj.MainActivity3.MyAdapter.ViewHolder;
import com.example.cxjmcj.db.DBManager;
import com.example.cxjmcj.dto.Family;
import com.example.cxjmcj.dto.Personal;
import com.example.cxjmcj.dto.User;
import com.example.cxjmcj.server.dto.FamilyMemberDTO;
import com.example.cxjmcj.server.dto.MemberDTO;
import com.example.cxjmcj.server.dto.UserDetail;
import com.example.cxjmcj.utils.HttpManager;
import com.example.cxjmcj.utils.TextToMap;
import com.example.cxjmcj.utils.ToastUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import butterknife.ButterKnife;
import info.hoang8f.widget.FButton;

public class MainActivity3 extends Activity {
	ExpandableLayoutListView listview;
	// image_sjsc
	public static final int CBDJ = 101;
	MyAdapter adapter;
	static Map<String, String> oldMap;
	static int pos;
	private TextView title_logout, text_user, title_local, title_num;
	private List<UserDetail> list;
	Context activity;
	String sToken;
	DBManager db;
	// 当前listview位置
	static int itemIndex;
	private List<UserDetail> queryUserDetail;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main2);
		activity = this;

		db = new DBManager(this);
		ButterKnife.bind(MainActivity3.this);
		/** ------请求数据---------------- */
		getData();

		/** --------初始化布局----------------- */
		title_logout = (TextView) findViewById(R.id.title_logout);
		text_user = (TextView) findViewById(R.id.text_user);
		title_local = (TextView) findViewById(R.id.title_local);
		title_num = (TextView) findViewById(R.id.title_num);
		listview = (ExpandableLayoutListView) findViewById(R.id.listView);
		/** --------初始化数据----------------- */

		list = new ArrayList<UserDetail>();
		InputStream inputStream = getResources().openRawResource(R.raw.countrycode);
		oldMap = new TextToMap().TextToMap(inputStream);

		/** --------注销返回到登陆界面-------------- */
		title_logout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity3.this, LoginActivity.class);
				startActivity(intent);
				ToastUtil.showShort(getApplicationContext(), "注销成功！");
				finish();
			}
		});
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
	}

	private void getData() {
		getDataFromNet();
		/** ------向服务器请求数据---------------- */
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
				Log.e("AAA-msg", msg);
				Log.e("AAA-error", error.getMessage());
				System.out.println("===msg:" + msg);
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
		queryUserDetail = db.queryUserDetail();
		String account = list.get(1).getAccount();
		String query_usern = db.query_usern(this, account);
		if (query_usern.isEmpty()) {
			db.addUserDetail(list);
		}
		/** 设置适配器 */
		adapter = new MyAdapter(list);
		System.out.println("___________----------" + list.get(0).toString());
		listview.setAdapter(adapter);
		adapter.notifyDataSetChanged();
		ToastUtil.showShort(getApplicationContext(), "数据已经加载");

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
			
			LinearLayout top1;
			LinearLayout top;
			LinearLayout center;
			LinearLayout bottom;
		}

		private LayoutInflater mInflater; // 得到一个LayoutInfalter对象用来导入布局
		private List<UserDetail> list;
		private String cjarea2;

		public MyAdapter(List<UserDetail> list) {
			super();
			this.mInflater = LayoutInflater.from(getBaseContext());
			this.list = list;
		}

		@Override
		public int getCount() {

			return list.size();
		}

		@Override
		public Object getItem(int position) {
			return list.get(position);
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

				convertView.setTag(holder); // 绑定ViewHolder对象
			} else {
				holder = (ViewHolder) convertView.getTag(); // 取出ViewHolder对象
			}
			/** --------设置标题栏的数据-------------- */
			String city = list.get(0).getCity().toString();
			title_local.setText(city);
			title_num.setText("（共" + list.size() + "村）");
			final String account = list.get(0).getAccount();
			text_user.setText(account);

			/** 把乡镇代码转换形成乡镇 名称 */
			final String cjarea = list.get(position).getCjarea();
			for (String key : oldMap.keySet()) {
				if (key.equals(cjarea))
					holder.local.setText(oldMap.get(key));
			}

			int posi = position + 1;
			holder.text_num.setText(posi + "");
			holder.upload2.setVisibility(View.GONE);
			holder.upload.setVisibility(View.GONE);

			// 判断数据是否下载过,
			for (UserDetail userDetail : queryUserDetail) {
				if (userDetail.downloadflag.equals("1")) {
					cjarea2 = userDetail.getCjarea();
					if (cjarea.equals(cjarea2)) {
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
					}
				}
				// 判断是否已经上传
				if (userDetail.uploadflag.equals("1")) {
					if (cjarea.equals(cjarea2)) {
						holder.upload2.setVisibility(View.VISIBLE);
						holder.upload.setVisibility(View.GONE);
						holder.download.setVisibility(View.GONE);
					}
				}
			}

			// 50旋转 100录入 0上传
			final Handler handler = new Handler() {
				public void handleMessage(Message msg) {
					// 下载失败
					if (msg.what == 0) {
						((Activity) activity).runOnUiThread(new Runnable() {
							@Override
							public void run() {
								// TODO Auto-generated method stub
								http.isAlive = true;
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
							}

						});
					}

				};

			};

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
					if (http.isError)
						handler.sendEmptyMessage(0);
					else
						handler.sendEmptyMessage(4);

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
					if (http.isError)
						handler.sendEmptyMessage(2);
					else
						handler.sendEmptyMessage(3);
				}
			};

			holder.download.setOnClickListener(new View.OnClickListener() {
				@SuppressWarnings("deprecation")
				@Override
				public void onClick(View v) {
					if (holder.download.getText().toString().equals("下 载")) {
						// 下载
						new Thread(down_run).start();
					} else {
						// 录入
						Intent intent = new Intent(MainActivity3.this, InfoMainActivity.class);
						startActivityForResult(intent, CBDJ);
						pos = position;
					}
				}
			});

			holder.upload.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					new Thread(up_run).start();
				}
			});
			return convertView;
		}

	}

}
