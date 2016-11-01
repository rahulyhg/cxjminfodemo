/**
 *@filename InfoMainActivity.java
 *@Email tengzhenjiu@qq.com
 *
 */
package com.example.cxjminfodemo.InfoActivity;

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

import com.example.cxjminfodemo.MainActivity;
import com.example.cxjminfodemo.MyAdapter;
import com.example.cxjminfodemo.R;
import com.example.cxjminfodemo.base.BaseActivity;
import com.example.cxjminfodemo.db.DBManager;
import com.example.cxjminfodemo.dto.Family;
import com.example.cxjminfodemo.dto.Personal;
import com.example.cxjminfodemo.utils.FamilyUtil;
import com.example.cxjminfodemo.utils.IDCard;
import com.example.cxjminfodemo.utils.LoadingDialog;
import com.example.idcardscandemo.ACameraActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lapism.searchview.SearchView;
import com.roamer.slidelistview.SlideListView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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

	@Bind(R.id.edit_num)
	EditText edit_num;

	@Bind(R.id.text_name)
	TextView text_name;

	@Bind(R.id.text_id)
	TextView text_id;

	String tempFamily;
	Family family = new Family();// 传回的数据
	static Family thefamily;// 传出的数据
	static ArrayList<Family> listFamily = new ArrayList<Family>();

	// 存户主与成员的映射
	static HashMap<String, ArrayList<Personal>> list_family_personal = new HashMap<String, ArrayList<Personal>>();
	static CharSequence temp;// 监听前的文本
	String res = null;// 查询身份证是否有效的返回信息
	static ArrayList<Personal> listItem = new ArrayList<Personal>();
	private SlideListView lv;
	MyAdapter adapter;
	Gson gson = new Gson();

	private String name = "";

	private String cardno = "";

	private String sex = "";

	private String folk = "";

	private String birthday = "";

	private String address = "";

	private DBManager mgr;

	LoadingDialog loading;

	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.info_main);
		ButterKnife.bind(InfoMainActivity.this);
		loading = new LoadingDialog(this);
		mgr = new DBManager(this);

		initView();
		edit_num.setText("330702199402180816");
		// /*为ListView设置Adapter来绑定数据*/
		listItem.clear();
		adapter = new MyAdapter(this, listItem);

		lv.setAdapter(adapter);
		adapter.notifyDataSetChanged();

		/* 为动态数组添加数据 */

		setView();
	}

	private void setView() {
		setSearchView();
		mSearchView.setNavigationIconArrowHamburger();
		mSearchView.setTextInput(R.string.search);
		mSearchView.setOnMenuClickListener(new SearchView.OnMenuClickListener() {
			@Override
			public void onMenuClick() {
				finish();
			}
		});
		customSearchView();
		mSearchView.open(false);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		if (thefamily != null) {
			text_name.setText(thefamily.getEdit_hzxm());
			text_id.setText(thefamily.getEdit_gmcfzh());
		}
	}

	/**
	 * 
	 */
	private void initView() {
		// TODO Auto-generated method stub

		lv = (SlideListView) findViewById(R.id.listView);// 得到ListView对象的引用

		edit_num.setText(temp);
		// 家庭登记的edit监听事件
		edit_num.addTextChangedListener(new TextWatcher() {

			int editStart;// 光标开始位置
			int editEnd;// 光标结束位置
			final int charMaxNum = 18;

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				temp = s;
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {

				editStart = edit_num.getSelectionStart();
				editEnd = edit_num.getSelectionEnd();
				if (temp.length() > charMaxNum) {
					s.delete(editStart - 1, editEnd);
					int tempSelection = editStart;
					edit_num.setText(s);
					edit_num.setSelection(tempSelection);
					edit_num.setCursorVisible(false);
					edit_num.setFocusableInTouchMode(false);
					edit_num.clearFocus();
				}
			}
		});

	}

	@OnClick(R.id.image_left)
	public void toMainActivity() {
		finish();
	}

	@OnClick(R.id.text)
	public void toInfoFamilyActivity2() {
		thefamily = null;
		Intent intent = new Intent(InfoMainActivity.this, InfoFamilyActivity.class);
		listFamily = mgr.queryFamily();
		for (Family tempFamily : listFamily) {
			if (tempFamily.getEdit_gmcfzh().equals(temp.toString())) {
				thefamily = new Family();
				thefamily = tempFamily;
			}
		}

		if (thefamily != null) {
			intent.putExtra("gmsfzh", temp.toString());
			String str = gson.toJson(thefamily);
			intent.putExtra("Family", str);
			intent.putExtra("hasTemp", "2");
			startActivityForResult(intent, INFO_FAMILY);
		}

	}

	@OnClick(R.id.btn_add2)
	public void toInfoPersonalActivity() {
		if (text_id.getText().toString().equals("")) {
			Toast.makeText(getApplicationContext(), "请先添加户主信息", Toast.LENGTH_LONG).show();
		} else {
			Intent intent = new Intent(this, InfoPersonalActivity.class);
			intent.putExtra("name", name);
			intent.putExtra("cardno", cardno);
			intent.putExtra("sex", sex);
			intent.putExtra("folk", folk);
			intent.putExtra("birthday", birthday);
			intent.putExtra("address", address);
			intent.putExtra("HZSFZ", text_id.getText().toString());
			startActivityForResult(intent, INFO＿PERSONAL);
		}

	}

	@OnClick(R.id.btn_add)
	public void toInfoFamilyActivity() {
		try {
			loading.show();
			res = IDCard.IDCardValidate(edit_num.getText().toString());
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
				if (res == "") {
					// 匹配身份证信息 并输出到户主信息栏
					listFamily = mgr.queryFamily();

					// System.out.println(listFamily.toString());
					for (Family tempFamily : listFamily) {
						if (tempFamily.getEdit_gmcfzh().equals(temp.toString())) {
							Toast.makeText(getApplicationContext(), "有匹配的身份证信息", Toast.LENGTH_LONG).show();
							// 显示的信息 与 编辑框信息对比
							if (!text_id.getText().toString().equals(temp.toString())) {
								listItem.clear();
								adapter.notifyDataSetChanged();
							}

							text_name.setText(tempFamily.getEdit_hzxm());
							text_id.setText(tempFamily.getEdit_gmcfzh());
							UpdateListView();

						}
					}
					// 为空就是未匹配到信息
					System.out.println("text_id:" + text_id.getText().toString());
					if (text_id.getText().toString().equals("")
							|| text_id.getText().toString().equals(temp.toString()) != true) {
						Toast.makeText(getApplicationContext(), "无匹配的身份证信息", Toast.LENGTH_LONG).show();
						Intent intent = new Intent(InfoMainActivity.this, InfoFamilyActivity.class);
						intent.putExtra("gmsfzh", temp.toString());
						if (thefamily != null) {
							String str = gson.toJson(thefamily);
							intent.putExtra("Family", str);
							intent.putExtra("hasTemp", "1");
						} else
							intent.putExtra("hasTemp", "0");
						startActivityForResult(intent, INFO_FAMILY);
					}

				} else
					Toast.makeText(getApplicationContext(), res, Toast.LENGTH_LONG).show();
				mHandler.sendEmptyMessage(0);
			}
		};
		mHandler.post(r2);
	}

	@OnClick(R.id.btn_camera)
	public void toOCR() {
		Intent intent = new Intent(InfoMainActivity.this, ACameraActivity.class);
		startActivityForResult(intent, CAMERA);
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) { // resultCode为回传的标记，我在B中回传的是RESULT_OK
		case INFO＿PERSONAL:
			// 回退按钮没有data
			if (data != null) {
				// Bundle p = data.getExtras(); // data为B中回传的Intent
				// String str = p.getString("Personal");// str即为回传的值
				// System.out.println("Personal" + str);
				// ArrayList<Personal> listPersonal = gson.fromJson(str, new
				// TypeToken<ArrayList<Personal>>() {
				// }.getType());
				// // 用于映射
				// list_family_personal.put(text_id.getText().toString(),
				// listPersonal);

				// 用于显示listview
				UpdateListView();
			}
			break;

		case INFO_FAMILY:
			Bundle f = data.getExtras(); // data为B中回传的Intent
			String str2 = f.getString("Family");// str即为回传的值
			Family tempFamily = gson.fromJson(str2, Family.class);

			text_name.setText(tempFamily.getEdit_hzxm());
			text_id.setText(tempFamily.getEdit_gmcfzh());

			// 刷新listview
			UpdateListView();
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
				edit_num.setText(cardno);
				thefamily = new Family();
				thefamily.setEdit_hzxm(name);
				thefamily.setEdit_gmcfzh(cardno);
				thefamily.setEdit_hkxxdz(address);
			}
			break;
		default:
			break;
		}
	}

	private void UpdateListView() {
		listItem.clear();
		ArrayList<Personal> listPersonal = mgr.queryPersonal(text_id.getText().toString());

		listItem.addAll(listPersonal);
		// 更新家庭信息参保人数的数据
		listFamily = mgr.queryFamily();
		for (Family tempFamily : listFamily) {
			if (tempFamily.getEdit_gmcfzh().equals(temp.toString())) {
				thefamily = new Family();
				thefamily = tempFamily;
				mgr.deleteFamily(thefamily);
				thefamily.setEdit_cjqtbxrs(listPersonal.size() + "");
				List<Family> the = new ArrayList<Family>();
				the.add(thefamily);
				mgr.addFamily(the);
			}
		}
		adapter.notifyDataSetChanged();
	}
}
