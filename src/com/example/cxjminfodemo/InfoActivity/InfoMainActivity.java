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
 * @data 2016��8��23�� ����5:22:33
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
	public static final int INFO��PERSONAL = 102;
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
	Family family = new Family();// ���ص�����
	static Family thefamily;// ����������
	static ArrayList<Family> listFamily = new ArrayList<Family>();

	// �滧�����Ա��ӳ��
	static HashMap<String, ArrayList<Personal>> list_family_personal = new HashMap<String, ArrayList<Personal>>();
	static CharSequence temp;// ����ǰ���ı�
	String res = null;// ��ѯ���֤�Ƿ���Ч�ķ�����Ϣ
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
		// /*ΪListView����Adapter��������*/
		listItem.clear();
		adapter = new MyAdapter(this, listItem);

		lv.setAdapter(adapter);
		adapter.notifyDataSetChanged();

		/* Ϊ��̬����������� */

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

		lv = (SlideListView) findViewById(R.id.listView);// �õ�ListView���������

		edit_num.setText(temp);
		// ��ͥ�Ǽǵ�edit�����¼�
		edit_num.addTextChangedListener(new TextWatcher() {

			int editStart;// ��꿪ʼλ��
			int editEnd;// ������λ��
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
			Toast.makeText(getApplicationContext(), "������ӻ�����Ϣ", Toast.LENGTH_LONG).show();
		} else {
			Intent intent = new Intent(this, InfoPersonalActivity.class);
			intent.putExtra("name", name);
			intent.putExtra("cardno", cardno);
			intent.putExtra("sex", sex);
			intent.putExtra("folk", folk);
			intent.putExtra("birthday", birthday);
			intent.putExtra("address", address);
			intent.putExtra("HZSFZ", text_id.getText().toString());
			startActivityForResult(intent, INFO��PERSONAL);
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
					/* sendMessage��������UI�Ĳ���������handler��handleMessage�ص������ */
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
					// ƥ�����֤��Ϣ �������������Ϣ��
					listFamily = mgr.queryFamily();

					// System.out.println(listFamily.toString());
					for (Family tempFamily : listFamily) {
						if (tempFamily.getEdit_gmcfzh().equals(temp.toString())) {
							Toast.makeText(getApplicationContext(), "��ƥ������֤��Ϣ", Toast.LENGTH_LONG).show();
							// ��ʾ����Ϣ �� �༭����Ϣ�Ա�
							if (!text_id.getText().toString().equals(temp.toString())) {
								listItem.clear();
								adapter.notifyDataSetChanged();
							}

							text_name.setText(tempFamily.getEdit_hzxm());
							text_id.setText(tempFamily.getEdit_gmcfzh());
							UpdateListView();

						}
					}
					// Ϊ�վ���δƥ�䵽��Ϣ
					System.out.println("text_id:" + text_id.getText().toString());
					if (text_id.getText().toString().equals("")
							|| text_id.getText().toString().equals(temp.toString()) != true) {
						Toast.makeText(getApplicationContext(), "��ƥ������֤��Ϣ", Toast.LENGTH_LONG).show();
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
		switch (requestCode) { // resultCodeΪ�ش��ı�ǣ�����B�лش�����RESULT_OK
		case INFO��PERSONAL:
			// ���˰�ťû��data
			if (data != null) {
				// Bundle p = data.getExtras(); // dataΪB�лش���Intent
				// String str = p.getString("Personal");// str��Ϊ�ش���ֵ
				// System.out.println("Personal" + str);
				// ArrayList<Personal> listPersonal = gson.fromJson(str, new
				// TypeToken<ArrayList<Personal>>() {
				// }.getType());
				// // ����ӳ��
				// list_family_personal.put(text_id.getText().toString(),
				// listPersonal);

				// ������ʾlistview
				UpdateListView();
			}
			break;

		case INFO_FAMILY:
			Bundle f = data.getExtras(); // dataΪB�лش���Intent
			String str2 = f.getString("Family");// str��Ϊ�ش���ֵ
			Family tempFamily = gson.fromJson(str2, Family.class);

			text_name.setText(tempFamily.getEdit_hzxm());
			text_id.setText(tempFamily.getEdit_gmcfzh());

			// ˢ��listview
			UpdateListView();
			break;
		case CAMERA:
			if (resultCode == Activity.RESULT_OK) {
				String result = data.getStringExtra("result");
				try {
					// ����xml
					Document doc;
					doc = DocumentHelper.parseText(result);
					// Document doc = reader.read(ffile); //��ȡһ��xml���ļ�
					Element root = doc.getRootElement();
					Iterator it = root.elementIterator("data");
					// ��������������ȡ���ڵ��е���Ϣ���鼮��
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
		// ���¼�ͥ��Ϣ�α�����������
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
