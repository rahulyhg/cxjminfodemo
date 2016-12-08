/**
 *@filename InfoMainActivity.java
 *@Email tengzhenjiu@qq.com
 *
 */
package com.megvii.idcardproject.InfoActivity;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.dou361.dialogui.DialogUIUtils;
import com.dou361.dialogui.config.BuildBean;
import com.google.gson.Gson;
import com.lapism.searchview.SearchView;
import com.megvii.idcardlib.util.Util;
import com.megvii.idcardproject.R;
import com.megvii.idcardproject.adapter.MyAdapterFamily;
import com.megvii.idcardproject.adapter.MyAdapterMember;
import com.megvii.idcardproject.adapter.MyAdapterMember.ViewHolder;
import com.megvii.idcardproject.base.BaseActivity;
import com.megvii.idcardproject.db.DBManager;
import com.megvii.idcardproject.dto.Family;
import com.megvii.idcardproject.dto.Personal;
import com.megvii.idcardproject.dto.Xzqh;
import com.megvii.idcardproject.utils.IDCard;
import com.megvii.idcardquality.IDCardQualityLicenseManager;
import com.megvii.licensemanager.Manager;
import com.roamer.slidelistview.SlideListView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @Title InfoMainActivity
 * @author tengzj
 * @data 2016年8月23日 下午5:22:33
 */
public class InfoMainActivity extends BaseActivity {

	/********** DECLARES *************/
	Activity activity;
	private static final String tag = "InfoMainActivity";

	public static final int INFO_FAMILY = 101;
	public static final int INFO＿PERSONAL = 102;
	public static final int CAMERA = 1001;
	private static byte[] bytes;
	private static String extension;
	public static final String action = "idcard.scan";

	@Bind(R.id.image_left)
	ImageView image_left;

	@Bind(R.id.ScrollView)
	ScrollView mScrollView;

	String tempFamily;
	Family family = new Family();// 传回的数据
	static ArrayList<Family> listFamily = new ArrayList<Family>();

	// 存户主与成员的映射
	static HashMap<String, ArrayList<Personal>> list_family_personal = new HashMap<String, ArrayList<Personal>>();
	String res = null;// 查询身份证是否有效的返回信息
	public static ArrayList<Personal> listItemMember = new ArrayList<Personal>();
	static ArrayList<Family> listItemFamily = new ArrayList<Family>();
	private SlideListView lvMember;
	private static int ListMemberNum;
	private SlideListView lvFamily;
	public static MyAdapterMember adapterMember;
	public static MyAdapterFamily adapterFamily;
	Gson gson = new Gson();
	Map<String, String> oldMap;

	private String name = "";

	private String cardno = "";

	private String sex = "";

	private String folk = "";

	private String birthday = "";

	private String address = "";

	public DBManager mgr;

	@Bind(R.id.line)
	public LinearLayout line;

	static public String XZQH;

	@Bind(R.id.title_num)
	TextView title_num;

	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.info_main);
		activity = this;

		if (Build.VERSION.SDK_INT >= 23) {
			int checkLocationPermission = ContextCompat.checkSelfPermission(InfoMainActivity.this,
					Manifest.permission_group.STORAGE);
			if (checkLocationPermission != PackageManager.PERMISSION_GRANTED) {
				String[] mPermissionList = new String[] { Manifest.permission.READ_EXTERNAL_STORAGE,
						Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA };
				ActivityCompat.requestPermissions(this, mPermissionList, 10);
			}
		}
		Intent intent = getIntent();
		XZQH = intent.getStringExtra("XZQH");
		getIntent().removeExtra("XZQH");
		((BaseActivity) (getParent()))._sonActivity = InfoMainActivity.this;
		this._sonActivity = InfoMainActivity.this;
		ButterKnife.bind(InfoMainActivity.this);
		mgr = new DBManager(this);

		initView();
		// /*为ListView设置Adapter来绑定数据*/
		// lv为户主列表
		listItemMember.clear();
		listItemFamily.clear();
		adapterMember = new MyAdapterMember(this, listItemMember);
		adapterFamily = new MyAdapterFamily(this, listItemFamily);
		lvMember.setAdapter(adapterMember);
		lvFamily.setAdapter(adapterFamily);
		adapterMember.notifyDataSetChanged();
		adapterFamily.notifyDataSetChanged();
		/* 为动态数组添加数据 */
		setView();
	}

	private void setView() {
		setSearchView();
		mSearchView.setHint("请输入证件号码或姓名");
		mSearchView.setTextSize(15);
		mSearchView.setNavigationIconArrowHamburger();
		mSearchView.setOnMenuClickListener(new SearchView.OnMenuClickListener() {
			@Override
			public void onMenuClick() {
				final String uuid = Util.getUUIDString(activity);
				new Thread(new Runnable() {
					@Override
					public void run() {
						Manager manager = new Manager(activity);
						IDCardQualityLicenseManager idCardLicenseManager = new IDCardQualityLicenseManager(activity);
						manager.registerLicenseManager(idCardLicenseManager);
						manager.takeLicenseFromNetwork(uuid);
						if (idCardLicenseManager.checkCachedLicense() > 0)
							mHandler2.sendEmptyMessage(1);
						else
							mHandler2.sendEmptyMessage(2);
					}
				}).start();
			}
		});
		customSearchView();
		mSearchView.close(true);
	}

	@SuppressLint("HandlerLeak")
	Handler mHandler2 = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (msg.what == 1) {
				Intent intent = new Intent(InfoMainActivity.this, com.megvii.idcardlib.IDCardScanActivity.class);
				intent.putExtra("side", 0);
				intent.putExtra("isvertical", false);
				startActivityForResult(intent, CAMERA);
			} else if (msg.what == 2) {
				Toast.makeText(getApplicationContext(), "联网授权失败，请点击按钮重新授权", Toast.LENGTH_SHORT).show();
			}
		}
	};

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		if (listItemFamily.size() != 0) {
			UpdateListView(listItemFamily.get(0).getEdit_gmcfzh(), 0);
		}
	}

	/**
	 * 
	 */
	private void initView() {
		// TODO Auto-generated method stub
		lvMember = (SlideListView) findViewById(R.id.listView);// 得到ListView对象的引用
		lvFamily = (SlideListView) findViewById(R.id.listView2);// 得到ListView对象的引用
		// 用於識別行政區劃
		Xzqh xzqh = mgr.queryXzqh(XZQH);
		if (xzqh.getName() != null && xzqh.getName() != "")
			title_num.setText("(" + xzqh.getName() + ")");
		else
			title_num.setText("某地区");
	}

	@OnClick(R.id.image_left)
	public void toMainActivity() {
		finish();
	}

	@OnClick(R.id.btn_add2)
	public void toInfoPersonalActivity() {
		if (listItemFamily.size() == 0) {
			Toast.makeText(getApplicationContext(), "请先添加户主信息", Toast.LENGTH_LONG).show();
		} else {
			Intent intent = new Intent(this, InfoPersonalActivity.class);
			intent.putExtra("JTBH", listItemFamily.get(0).getEdit_jtbh());
			intent.putExtra("XZQH", XZQH);
			startActivityForResult(intent, INFO＿PERSONAL);
		}
	}

	@OnClick(R.id.btn_add)
	public void toInfoFamilyActivity() {
		try {
			res = IDCard.IDCardValidate(mSearchView.getTextInput());
			Intent intent = new Intent(InfoMainActivity.this, InfoFamilyActivity.class);
			// 新增状态 非编辑
			intent.putExtra("hasTemp", "0");
			intent.putExtra("gmsfzh", mSearchView.getTextInput());
			intent.putExtra("XZQH", XZQH);
			startActivityForResult(intent, INFO_FAMILY);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) { // resultCode为回传的标记，我在B中回传的是RESULT_OK
		case INFO＿PERSONAL:
			if (resultCode == Activity.RESULT_OK) {
				UpdateListView(listItemFamily.get(0).getEdit_gmcfzh(), 0);
			}
			break;

		case INFO_FAMILY:
			if (resultCode == Activity.RESULT_OK) {
				Bundle f = data.getExtras(); // data为B中回传的Intent
				String str2 = f.getString("Family");// str即为回传的值
				Family tempFamily = gson.fromJson(str2, Family.class);
				// 刷新listview
				UpdateListView(tempFamily.getEdit_gmcfzh(), 0);
			}
			break;
		case CAMERA:
			if (resultCode == Activity.RESULT_OK) {
				String result = data.getStringExtra("result");
				JSONTokener jsonTokener = new JSONTokener(result);
				try {
					JSONObject jsonObject = (JSONObject) jsonTokener.nextValue();
					name = jsonObject.getString("name");
					cardno = jsonObject.getString("id_card_number");
					address = jsonObject.getString("address");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				mSearchView.setTextInput(cardno);
				mSearchView.addFocus();
				setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
				setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
			}
			break;
		default:
			break;
		}
	}

	public void UpdateListView(final String temp, int isMember) {
		final BuildBean build = DialogUIUtils.showLoadingHorizontal(activity, "加载中...");
		build.show();
		listItemMember.clear();
		listItemFamily.clear();
		// 历史数据
		if (isMember == -1) {
			listFamily = mgr.queryFamily();
			for (Family tempFamily : listFamily) {
				if (tempFamily.getEdit_gmcfzh().equals(temp)) {
					isMember = 0;
					break;
				} else
					isMember = 1;
			}
		}

		if (isMember == 0) {
			// 更新家庭信息参保人数的数据
			listFamily = mgr.queryFamily();
			for (Family tempFamily : listFamily) {
				if (tempFamily.getEdit_gmcfzh().equals(temp)) {
					listItemFamily.add(tempFamily);
					// 获得人员信息
					ArrayList<Personal> listPersonal = mgr.queryPersonal(tempFamily.getEdit_jtbh());
					listItemMember.addAll(listPersonal);
				}
			}
		}

		if (isMember == 1) {
			Personal listPersonal = mgr.queryPersonalByGmsfzh(temp);
			Family thefamily = new Family();
			thefamily = mgr.queryFamilyByJtbh(listPersonal.getHZSFZ());
			listItemFamily.add(thefamily);
			// 获得人员信息
			ArrayList<Personal> listPersonals = mgr.queryPersonal(thefamily.getEdit_jtbh());
			listItemMember.addAll(listPersonals);
		}

		adapterFamily.notifyDataSetChanged();
		adapterMember.notifyDataSetChanged();
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				// 登录等待4S
				while (build.dialog.isShowing()) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					build.dialog.dismiss();
				}
			}
		}).start();

		// 判断该家庭是否属于该行政区划
		// if (listItemFamily.size() != 0) {
		// if (!listItemFamily.get(0).xzqh.equals(XZQH)) {
		// // 获得当前地区名
		// String country = "";
		// Xzqh xzqh = mgr.queryXzqh(listItemFamily.get(0).getXzqh());
		// if (xzqh.getName() != null && xzqh.getName() != "")
		// country = xzqh.getName();
		// new SweetAlertDialog(activity,
		// SweetAlertDialog.WARNING_TYPE).setTitleText("此家庭不属于该区域")
		// .setContentText(listItemFamily.get(0).getEdit_hzxm() + "\n" +
		// listItemFamily.get(0).edit_gmcfzh
		// + "\n" + "所属地区：" + country + "\n" + "登记日期：" +
		// listItemFamily.get(0).edit_djrq)
		// .setConfirmText("我知道了").show();
		// listItemMember.clear();
		// listItemFamily.clear();
		// adapterFamily.notifyDataSetChanged();
		// adapterMember.notifyDataSetChanged();
		// }
		// }
		// 头部的横线
		if (listItemMember.size() != 0) {
			line.setVisibility(View.VISIBLE);
			ScorllingSituation(temp, isMember);
		} else
			line.setVisibility(View.GONE);
	}

	// 新增和搜索状态时自动滑动到指定位置
	public void ScorllingSituation(final String temp, int isMember) {
		View listItem = adapterMember.getView(0, null, lvMember);
		listItem.measure(0, 0);
		final int itemHeight = listItem.getMeasuredHeight();
		if (listItemMember.size() == ListMemberNum + 1) {
			mScrollView.postDelayed(new Runnable() {
				@Override
				public void run() {
					// 新增状态
					mScrollView.smoothScrollTo(0, listItemMember.size() * itemHeight);
				}
			}, 500);
		}
		if (isMember == 1) {
			// 搜索状态
			mScrollView.postDelayed(new Runnable() {

				@Override
				public void run() {
					// 新增状态
					Personal listPersonal = mgr.queryPersonalByGmsfzh(temp);
					int memberPos = 0;
					for (Personal per : listItemMember) {
						if (per.getId().equals(listPersonal.getId()))
							break;
						else
							memberPos = memberPos + 1;
					}
					View listItem = lvMember.getChildAt(memberPos);
					final ViewHolder viewHolder = (ViewHolder) listItem.getTag();
					mScrollView.smoothScrollTo(0, (memberPos - 2) * itemHeight);
					viewHolder.front.setBackgroundColor(Color.RED);
					// 1500毫秒后恢复原来状况
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
							activity.runOnUiThread(new Runnable() {
								@Override
								public void run() {
									// TODO Auto-generated method stub
									viewHolder.front.setBackgroundColor(Color.parseColor("#99F1F1F1"));
								}
							});
						}
					}).start();
				}
			}, 500);
		}
		ListMemberNum = listItemMember.size();
	}
}
