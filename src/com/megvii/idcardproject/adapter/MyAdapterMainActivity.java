/**
 *@filename MyAdapter.java
 *@Email tengzhenjiu@qq.com
 *
 */
package com.megvii.idcardproject.adapter;

import java.io.IOException;
import java.util.List;

import com.dou361.dialogui.DialogUIUtils;
import com.dou361.dialogui.config.BuildBean;
import com.megvii.idcardproject.MainActivity;
import com.megvii.idcardproject.R;
import com.megvii.idcardproject.InfoActivity.InfoMainActivity;
import com.megvii.idcardproject.db.DBManager;
import com.megvii.idcardproject.dto.Family;
import com.megvii.idcardproject.dto.Personal;
import com.megvii.idcardproject.dto.UserDetail;
import com.megvii.idcardproject.dto.Xzqh;
import com.megvii.idcardproject.utils.HttpManager;
import com.megvii.idcardproject.utils.Sfcl;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
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
		public TextView money;
		public TextView ylrj;
		public TextView ylrr;
		public FButton upload;
		FButton download;
		public TextView text_num;
		private ImageView list;
		LinearLayout background;

		LinearLayout blank;

		LinearLayout time;
		TextView time2;

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
			holder.money = (TextView) convertView.findViewById(R.id.money);
			holder.text_num = (TextView) convertView.findViewById(R.id.text_num);
			holder.num1 = (TextView) convertView.findViewById(R.id.num1);
			holder.num2 = (TextView) convertView.findViewById(R.id.num2);
			holder.num3 = (TextView) convertView.findViewById(R.id.num3);
			holder.local = (TextView) convertView.findViewById(R.id.local);
			holder.ylrj = (TextView) convertView.findViewById(R.id.ylrj);
			holder.ylrr = (TextView) convertView.findViewById(R.id.ylrr);

			holder.upload = (FButton) convertView.findViewById(R.id.buttom_up);
			holder.download = (FButton) convertView.findViewById(R.id.buttom_down);

			holder.background = (LinearLayout) convertView.findViewById(R.id.id_background);
			holder.list = (ImageView) convertView.findViewById(R.id.list);

			holder.blank = (LinearLayout) convertView.findViewById(R.id.blank);
			holder.time = (LinearLayout) convertView.findViewById(R.id.time);
			holder.time2 = (TextView) convertView.findViewById(R.id.time2);

			convertView.setTag(holder); // 绑定ViewHolder对象
		} else {
			holder = (ViewHolder) convertView.getTag(); // 取出ViewHolder对象
		}

		/** 把乡镇代码转换形成乡镇 名称 */
		holder.cjarea = queryUserDetail.get(position).getCjarea();
		final Xzqh xzqh = db.queryXzqh(holder.cjarea);
		if (xzqh.getName() != null && xzqh.getName() != "")
			holder.local.setText(xzqh.getName());
		else
			holder.local.setText("某地区");
		/** 总金额 */

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
							int memberylr = 0;
							int familyyl = 0;
							List<Family> familys = db.queryFamily(holder.cjarea);
							holder.num1.setText("共 " + familys.size() + " 家 ，");
							String MaxTime = "0000-00-00 00:00:00";
							int sum = 0;
							for (Family family : familys) {
								if (family.isEdit.equals("1")) {
									familyyl = familyyl + 1;
								}
								List<Personal> personals = db.queryPersonal(family.getEdit_jtbh());
								memberSize = memberSize + personals.size();
								for (Personal personal : personals) {
									if (personal.getEdit_jf().equals("1")) {
										memberJf = memberJf + 1;

										String lb = personal.getEdit_cbrylb();
										//收费策略
										String sfcl = queryUserDetail.get(position).sfcl;
										sum = Sfcl.Calculate(sum, lb, sfcl);
									}
									if (personal.getIsEdit().equals("1")) {
										memberylr = memberylr + 1;
									}
								}
								if (db.queryTime(family.getEdit_jtbh()) != null) {
									String newTime = db.queryTime(family.getEdit_jtbh());
									String newTime2 = db.queryTime2(family.getEdit_gmcfzh());
									if (newTime.compareTo(newTime2) > 0) {
										if (MaxTime.compareTo(newTime) > 0) {
											// MaxTime晚于newTime
										} else {
											MaxTime = newTime;
										}
									} else {
										if (MaxTime.compareTo(newTime2) > 0) {

										} else {
											MaxTime = newTime2;
										}
									}
								}
							}
							// 設置金額
							holder.money.setText(sum + "");
							holder.num2.setText(memberSize + " 人");
							holder.num3.setText(memberJf + "");
							// 已录入人员
							holder.ylrj.setText(familyyl + "");
							holder.ylrr.setText(memberylr + "");
							holder.upload.setVisibility(View.VISIBLE);
							holder.download.setText("录 入");
							holder.download.setButtonColor(Color.rgb(237, 152, 17));

							holder.background.setBackgroundResource(R.drawable.item_background2);
							holder.list.setImageResource(R.drawable.list3);
							holder.list.setScaleType(ImageView.ScaleType.FIT_XY);
							holder.blank.setVisibility(View.VISIBLE);
							holder.time.setVisibility(View.VISIBLE);
							if (MaxTime.equals("0000-00-00 00:00:00")) {
								holder.time2.setText("");
							} else
								holder.time2.setText(MaxTime);

						}
					});
				}

			};

		};

		int posi = position + 1;
		holder.text_num.setText(posi + "");
		holder.upload.setVisibility(View.GONE);

		// 判断数据是否下载过,
		for (UserDetail userDetail : queryUserDetail) {
			cjarea2 = userDetail.getCjarea();
			if (holder.cjarea.equals(cjarea2)) {
				// 是否已下載
				if (userDetail.downloadflag.equals("1")) {
					// 显示数据
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
					db.update_df(activity.getApplicationContext(), holder.cjarea, "downloadflag", "0");
				} else
				// 下载成功
				{
					handler.sendEmptyMessage(4);
					db.update_df(activity.getApplicationContext(), holder.cjarea, "downloadflag", "1");
				}
				build.dialog.dismiss();
			}
		};

		final Runnable up_run = new Runnable() {
			@Override
			public void run() {
				try {
					http.getCjxx(holder.cjarea, sToken, queryUserDetail.get(0).getAccount());
				} catch (IOException e1) {
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
			@SuppressLint("SimpleDateFormat")
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
							build = DialogUIUtils.showLoadingHorizontal(activity, "正在下载...", false, false, true);
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
							build = DialogUIUtils.showLoadingHorizontal(activity, "加载中...", false, false, true);
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
				new SweetAlertDialog(activity, SweetAlertDialog.WARNING_TYPE).setTitleText("确认上传？")
						.setContentText(">上传人员为已缴费人员\n>已上传的家庭人员无法编辑或删除\n").setConfirmText("上传").setCancelText("取消").showCancelButton(true)
						.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
							@Override
							public void onClick(SweetAlertDialog sDialog) {
								sDialog.dismissWithAnimation();
								build = DialogUIUtils.showLoadingHorizontal(activity, "上传中...", false, false, true);
								build.show();
								new Thread(up_run).start();
							}
						}).setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
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
