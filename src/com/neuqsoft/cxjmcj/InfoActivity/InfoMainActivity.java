/**
 *@filename InfoMainActivity.java
 *@Email tengzhenjiu@qq.com
 *
 */
package com.neuqsoft.cxjmcj.InfoActivity;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.neuqsoft.cxjmcj.R;
import com.neuqsoft.cxjmcj.adapter.MyAdapterMember;
import com.neuqsoft.cxjmcj.adapter.MyAdapterFamily;
import com.example.idcardscandemo.ACameraActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lapism.searchview.SearchView;
import com.neuqsoft.cxjmcj.WelcomeActivity;
import com.neuqsoft.cxjmcj.base.BaseActivity;
import com.neuqsoft.cxjmcj.db.DBManager;
import com.neuqsoft.cxjmcj.dto.Family;
import com.neuqsoft.cxjmcj.dto.Personal;
import com.neuqsoft.cxjmcj.utils.FamilyUtil;
import com.neuqsoft.cxjmcj.utils.IDCard;
import com.neuqsoft.cxjmcj.utils.LoadingDialog;
import com.roamer.slidelistview.SlideListView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
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
	/*
	 * private ImageView image_left; private EditText edit_num; private
	 * ScrollView ScrollView; private com.example.cxjminfodemo.NoScrollListView
	 * listView1;
	 * 
	 * image_left = (ImageView) findViewById(R.id.image_left); edit_num =
	 * (EditText) findViewById(R.id.edit_num); ScrollView = (ScrollView)
	 * findViewById(R.id.ScrollView); listView1 =
	 * (com.example.cxjminfodemo.NoScrollListView) findViewById(R.id.listView1);
	 * Please visit http://www.ryangmattison.com for updates
	 */
	private static final String tag = "InfoMainActivity";

	public static final int INFO_FAMILY = 101;
	public static final int INFO＿PERSONAL = 102;
	public static final int CAMERA = 1001;
	private static byte[] bytes;
	private static String extension;
	public static final String action = "idcard.scan";

	@Bind(R.id.image_left)
	ImageView image_left;

	String tempFamily;
	Family family = new Family();// 传回的数据
	static ArrayList<Family> listFamily = new ArrayList<Family>();

	// 存户主与成员的映射
	static HashMap<String, ArrayList<Personal>> list_family_personal = new HashMap<String, ArrayList<Personal>>();
	String res = null;// 查询身份证是否有效的返回信息
	static ArrayList<Personal> listItem = new ArrayList<Personal>();
	static ArrayList<Family> listItem2 = new ArrayList<Family>();
	private SlideListView lv;
	private SlideListView lv2;
	public static MyAdapterMember adapter;
	public static MyAdapterFamily adapter2;
	Gson gson = new Gson();

	private String name = "";

	private String cardno = "";

	private String sex = "";

	private String folk = "";

	private String birthday = "";

	private String address = "";

	public DBManager mgr;

	LoadingDialog loading;

	@Bind(R.id.line)
	LinearLayout line;

	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.info_main);

		((BaseActivity) (getParent()))._sonActivity = InfoMainActivity.this;
		this._sonActivity = InfoMainActivity.this;
		ButterKnife.bind(InfoMainActivity.this);
		loading = new LoadingDialog(this);
		mgr = new DBManager(this);

		initView();
		// /*为ListView设置Adapter来绑定数据*/
		// lv为户主列表
		listItem.clear();
		listItem2.clear();
		adapter = new MyAdapterMember(this, listItem);
		adapter2 = new MyAdapterFamily(this, listItem2);
		lv.setAdapter(adapter);
		lv2.setAdapter(adapter2);
		adapter.notifyDataSetChanged();
		adapter2.notifyDataSetChanged();
		/* 为动态数组添加数据 */
		setView();
	}

	private void setView() {
		setSearchView();
		mSearchView.setHint("身份证号码查询");
		mSearchView.setTextSize(15);
		mSearchView.setNavigationIconArrowHamburger();
		mSearchView.setOnMenuClickListener(new SearchView.OnMenuClickListener() {
			@Override
			public void onMenuClick() {
				Intent intent = new Intent(InfoMainActivity.this, ACameraActivity.class);
				startActivityForResult(intent, CAMERA);
			}
		});
		customSearchView();
		mSearchView.close(true);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		if (listItem2.size() != 0) {
			UpdateListView(listItem2.get(0).getEdit_gmcfzh());
		}
	}

	/**
	 * 
	 */
	private void initView() {
		// TODO Auto-generated method stub
		lv = (SlideListView) findViewById(R.id.listView);// 得到ListView对象的引用
		lv2 = (SlideListView) findViewById(R.id.listView2);// 得到ListView对象的引用
	}

	@OnClick(R.id.image_left)
	public void toMainActivity() {
		finish();
	}

	@OnClick(R.id.btn_add2)
	public void toInfoPersonalActivity() {
		if (listItem2.size() == 0) {
			Toast.makeText(getApplicationContext(), "请先添加户主信息", Toast.LENGTH_LONG).show();
		} else {
			Intent intent = new Intent(this, InfoPersonalActivity.class);
			intent.putExtra("HZSFZ", listItem2.get(0).getEdit_gmcfzh());
			startActivityForResult(intent, INFO＿PERSONAL);
		}

	}

	@OnClick(R.id.btn_add)
	public void toInfoFamilyActivity() {
		try {
			loading.show();
			res = IDCard.IDCardValidate(mSearchView.getTextInput());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		final Handler mHandler = new Handler() {
			public void handleMessage(Message msg) {
				if (msg.what == 0) {
					/* sendMessage方法更新UI的操作必须在handler的handleMessage回调中完成 */
					((Activity) InfoMainActivity.this).runOnUiThread(new Runnable() {
						@Override
						public void run() {
							// TODO Auto-generated method stub
							loading.dismiss();
						}
					});
				}
			}
		};
		Runnable r2 = new Runnable() {
			public void run() {
				Intent intent = new Intent(InfoMainActivity.this, InfoFamilyActivity.class);
				//新增状态 非编辑
				intent.putExtra("hasTemp", "0");
				intent.putExtra("gmsfzh", mSearchView.getTextInput());
				startActivityForResult(intent, INFO_FAMILY);
				mHandler.sendEmptyMessage(0);
			}
		};
		mHandler.post(r2);
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) { // resultCode为回传的标记，我在B中回传的是RESULT_OK
		case INFO＿PERSONAL:
			UpdateListView(listItem2.get(0).getEdit_gmcfzh());
			break;

		case INFO_FAMILY:
			if (resultCode == Activity.RESULT_OK) {
				Bundle f = data.getExtras(); // data为B中回传的Intent
				String str2 = f.getString("Family");// str即为回传的值
				Family tempFamily = gson.fromJson(str2, Family.class);
				// 刷新listview
				UpdateListView(tempFamily.getEdit_gmcfzh());
			}
			break;
		case CAMERA:
			if (resultCode == Activity.RESULT_OK) {
				String result = data.getStringExtra("result");
				try {
					// 解析xml
					Document doc;
					doc = DocumentHelper.parseText(result);
					// Document doc = reader.read(ffile); //读取一个xml的文件
					Element root = doc.getRootElement();
					Iterator it = root.elementIterator("data");
					// 遍历迭代器，获取根节点中的信息（书籍）
					while (it.hasNext()) {
						Element data1 = (Element) it.next();

						Iterator itt = data1.elementIterator("item");
						while (itt.hasNext()) {
							Element item = (Element) itt.next();
							name = item.elementTextTrim("name");
							cardno = item.elementTextTrim("cardno");
							sex = item.elementTextTrim("sex");
							folk = item.elementTextTrim("folk");
							birthday = item.elementTextTrim("birthday");
							address = item.elementTextTrim("address");
						}
					}
				} catch (DocumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				mSearchView.setTextInput(cardno);
				// 匹配身份证信息 并输出到户主信息栏
				listFamily = mgr.queryFamily();
				// System.out.println(listFamily.toString());
				Boolean hasTemp = false;
				for (Family tempFamily : listFamily) {
					if (tempFamily.getEdit_gmcfzh().equals(mSearchView.getTextInput())) {
						UpdateListView(tempFamily.getEdit_gmcfzh());
						hasTemp = true;
					}
				}
				if (!hasTemp) {
					// 拍照后不存在用户信息
					UpdateListView(mSearchView.getTextInput());
				}
			}
			break;
		default:
			break;
		}
	}

	public void UpdateListView(String temp) {
		listItem.clear();
		listItem2.clear();
		ArrayList<Personal> listPersonal = mgr.queryPersonal(temp);
		listItem.addAll(listPersonal);
		// 更新家庭信息参保人数的数据
		listFamily = mgr.queryFamily();
		for (Family tempFamily : listFamily) {
			if (tempFamily.getEdit_gmcfzh().equals(temp)) {
				Family thefamily = new Family();
				thefamily = tempFamily;
				mgr.updateFamily(thefamily);
				thefamily.setEdit_cjqtbxrs(listPersonal.size() + "");
				List<Family> the = new ArrayList<Family>();
				the.add(thefamily);
				mgr.addFamily(the);
				listItem2.add(thefamily);
			}
		}
		//头部的横线
		if(listItem.size()!=0)
			line.setVisibility(View.VISIBLE);
		else
			line.setVisibility(View.GONE);
		adapter2.notifyDataSetChanged();
		adapter.notifyDataSetChanged();
	}
}
