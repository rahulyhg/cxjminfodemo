/**
 *@filename MyAdapter.java
 *@Email tengzhenjiu@qq.com
 *
 */
package com.neuqsoft.cxjmcj.adapter;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import com.dou361.dialogui.DialogUIUtils;
import com.dou361.dialogui.config.BuildBean;
import com.google.gson.Gson;
import com.neuqsoft.cxjmcj.MainActivity;
import com.neuqsoft.cxjmcj.R;
import com.neuqsoft.cxjmcj.InfoActivity.InfoMainActivity;
import com.neuqsoft.cxjmcj.InfoActivity.InfoPersonalActivity;
import com.neuqsoft.cxjmcj.R.id;
import com.neuqsoft.cxjmcj.R.layout;
import com.neuqsoft.cxjmcj.db.DBManager;
import com.neuqsoft.cxjmcj.dto.Family;
import com.neuqsoft.cxjmcj.dto.Personal;
import com.neuqsoft.cxjmcj.dto.UserDetail;
import com.neuqsoft.cxjmcj.dto.Xzqh;
import com.neuqsoft.cxjmcj.utils.HttpManager;
import com.roamer.slidelistview.SlideBaseAdapter;
import com.roamer.slidelistview.SlideListView.SlideMode;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.pedant.SweetAlert.SweetAlertDialog;
import info.hoang8f.widget.FButton;

/**
 * @Title MyAdapter
 * @author tengzj
 * @data 2016年8月24日 下午2:42:15
 */
public class MyAdapterMainActivity extends BaseAdapter {

	/* 存放控件 的ViewHolder */
	public final class ViewHolder {
		public TextView num1;
		public TextView num2;
		public TextView num3;
		public TextView local;
		public FButton upload;
		FButton download;
		private ImageView upload2;
		public TextView text_num;

		private ImageView list;

		LinearLayout top1;
		LinearLayout top;
		LinearLayout center;
		ImageView bottom;

		public String cjarea = "";

	}

	public static final int CBDJ = 101;
	private LayoutInflater mInflater; // 得到一个LayoutInfalter对象用来导入布局
	private List<UserDetail> queryUserDetail;
	private String cjarea2;
	Activity activity;
	Dialog dialog_up;
	DBManager db;
	BuildBean build;
	String sToken;

	public MyAdapterMainActivity(Activity activity, List<UserDetail> queryUserDetail, String sToken) {
		super();
		this.mInflater = LayoutInflater.from(activity.getApplicationContext());
		this.queryUserDetail = queryUserDetail;
		this.activity = activity;
		db = new DBManager(activity);
		this.sToken = sToken;
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

		/** 把乡镇代码转换形成乡镇 名称 */
		holder.cjarea = queryUserDetail.get(position).getCjarea();
		Xzqh xzqh = db.queryXzqh(holder.cjarea);
		if (xzqh.getName() != null && xzqh.getName() != "")
			holder.local.setText(xzqh.getName());
		else
			holder.local.setText("某地区");

		final Handler handler = new Handler() {
			public void handleMessage(Message msg) {
				// 下载失败
				if (msg.what == 0) {
					((Activity) activity).runOnUiThread(new Runnable() {
						@Override
						public void run() {
							// TODO Auto-generated method stub
							http.isAlive = true;
							new SweetAlertDialog(activity, SweetAlertDialog.ERROR_TYPE).setTitleText("下载失败...").show();
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
							dialog_up = new SweetAlertDialog(activity, SweetAlertDialog.ERROR_TYPE)
									.setTitleText("上传失败...");
							dialog_up.show();
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
							activity.runOnUiThread(new Runnable() {

								@Override
								public void run() {
									// TODO Auto-generated method stub
									dialog_up = new SweetAlertDialog(activity, SweetAlertDialog.SUCCESS_TYPE)
											.setTitleText("上传成功");
									dialog_up.show();
								}

							});

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
							List<Family> familys = db.queryFamily(holder.cjarea);
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
							holder.list.setImageResource(R.drawable.list3);
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
			if (holder.cjarea.equals(cjarea2)) {
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
					// handler.sendEmptyMessage(3);
				}
			}
		}

		// 50旋转 100录入 0上传

		final Runnable down_run = new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				http.getJbxx(holder.cjarea);
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
					http.getCjxx(holder.cjarea, sToken, sToken);
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

				// 去dialog
				build.dialog.dismiss();
				new Thread(new Runnable() {
					@Override
					public void run() {
						// TODO Auto-generated method stub
						try {
							Thread.sleep(2000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						dialog_up.dismiss();
					}
				}).start();
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
					activity.runOnUiThread(new Runnable() {
						@Override
						public void run() {
							delay(800);
							// TODO Auto-generated method stub
							build = DialogUIUtils.showLoadingHorizontal(activity, "加载中...");
							build.show();
						}
					});
					Intent intent = new Intent(activity, InfoMainActivity.class);
					intent.putExtra("XZQH", holder.cjarea);
					((MainActivity) activity).startActivityForResult(intent, CBDJ);
				}
			}
		});

		holder.upload.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				new SweetAlertDialog(activity, SweetAlertDialog.WARNING_TYPE).setTitleText("警告")
						.setContentText("每个乡镇仅限上传一次\n上传成功后将无法录入\n").setConfirmText("上传").setCancelText("取消")
						.showCancelButton(true).setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
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
