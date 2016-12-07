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
 * @data 2016��8��24�� ����2:42:15
 */
public class MyAdapterMainActivity extends BaseAdapter {

	/* ��ſؼ� ��ViewHolder */
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
	private LayoutInflater mInflater; // �õ�һ��LayoutInfalter�����������벼��
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
			/* �õ������ؼ��Ķ��� */
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

			convertView.setTag(holder); // ��ViewHolder����
		} else {
			holder = (ViewHolder) convertView.getTag(); // ȡ��ViewHolder����
		}

		/** ���������ת���γ����� ���� */
		holder.cjarea = queryUserDetail.get(position).getCjarea();
		final Xzqh xzqh = db.queryXzqh(holder.cjarea);
		if (xzqh.getName() != null && xzqh.getName() != "")
			holder.local.setText(xzqh.getName());
		else
			holder.local.setText("ĳ����");
		/** �ܽ�� */

		final Handler handler = new Handler() {
			public void handleMessage(Message msg) {
				// ����ʧ��
				if (msg.what == 0) {
					((Activity) activity).runOnUiThread(new Runnable() {
						@Override
						public void run() {
							// TODO Auto-generated method stub
							http.isAlive = true;
							new SweetAlertDialog(activity, SweetAlertDialog.ERROR_TYPE).setTitleText("����ʧ��...").show();
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
				// �ϴ�ʧ��
				if (msg.what == 2) {
					((Activity) activity).runOnUiThread(new Runnable() {
						@Override
						public void run() {
							// TODO Auto-generated method stub
							http.isAlive = true;
							dialog_up = new SweetAlertDialog(activity, SweetAlertDialog.ERROR_TYPE)
									.setTitleText("�ϴ�ʧ��...");
							dialog_up.show();
						}
					});
				}
				// �ϴ��ɹ�
				if (msg.what == 3) {
					/* sendMessage��������UI�Ĳ���������handler��handleMessage�ص������ */
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
											.setTitleText("�ϴ��ɹ�");
									dialog_up.show();
								}
							});
						}
					});
				}
				// ���سɹ� ������
				if (msg.what == 4) {
					/* sendMessage��������UI�Ĳ���������handler��handleMessage�ص������ */
					((Activity) activity).runOnUiThread(new Runnable() {
						@Override
						public void run() {
							// TODO Auto-generated method stub
							int memberSize = 0;
							int memberJf = 0;
							int memberylr = 0;
							int familyyl = 0;
							List<Family> familys = db.queryFamily(holder.cjarea);
							holder.num1.setText("�� " + familys.size() + " �� ��");
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
										//�շѲ���
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
											// MaxTime����newTime
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
							// �O�ý��~
							holder.money.setText(sum + "");
							holder.num2.setText(memberSize + " ��");
							holder.num3.setText(memberJf + "");
							// ��¼����Ա
							holder.ylrj.setText(familyyl + "");
							holder.ylrr.setText(memberylr + "");
							holder.upload.setVisibility(View.VISIBLE);
							holder.download.setText("¼ ��");
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

		// �ж������Ƿ����ع�,
		for (UserDetail userDetail : queryUserDetail) {
			cjarea2 = userDetail.getCjarea();
			if (holder.cjarea.equals(cjarea2)) {
				// �Ƿ������d
				if (userDetail.downloadflag.equals("1")) {
					// ��ʾ����
					handler.sendEmptyMessage(4);
				}
				// �ж��Ѿ��ϴ�
				if (userDetail.uploadflag.equals("1")) {
					// handler.sendEmptyMessage(3);
				}
			}
		}

		// 50��ת 100¼�� 0�ϴ�

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
				// ���سɹ�
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
					// �ϴ��ɹ�
					handler.sendEmptyMessage(3);
				// ȥdialog
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

				if (holder.download.getText().toString().equals("�� ��")) {
					// ���ؿ�
					((MainActivity) activity).runOnUiThread(new Runnable() {
						@Override
						public void run() {
							// TODO Auto-generated method stub
							DialogUIUtils.init(activity);
							build = DialogUIUtils.showLoadingHorizontal(activity, "��������...", false, false, true);
							build.show();
						}
					});

					// �����߳�
					new Thread(down_run).start();
				} else {
					// ¼��
					activity.runOnUiThread(new Runnable() {
						@Override
						public void run() {
							delay(800);
							// TODO Auto-generated method stub
							build = DialogUIUtils.showLoadingHorizontal(activity, "������...", false, false, true);
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
				new SweetAlertDialog(activity, SweetAlertDialog.WARNING_TYPE).setTitleText("ȷ���ϴ���")
						.setContentText(">�ϴ���ԱΪ�ѽɷ���Ա\n>���ϴ��ļ�ͥ��Ա�޷��༭��ɾ��\n").setConfirmText("�ϴ�").setCancelText("ȡ��").showCancelButton(true)
						.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
							@Override
							public void onClick(SweetAlertDialog sDialog) {
								sDialog.dismissWithAnimation();
								build = DialogUIUtils.showLoadingHorizontal(activity, "�ϴ���...", false, false, true);
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
