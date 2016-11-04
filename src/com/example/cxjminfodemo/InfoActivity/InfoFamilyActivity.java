/**
 *@filename InfoFamilyActivity.java
 *@Email tengzhenjiu@qq.com
 *
 */
package com.example.cxjminfodemo.InfoActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.example.cxjminfodemo.MainActivity;
import com.example.cxjminfodemo.R;
import com.example.cxjminfodemo.db.DBManager;
import com.example.cxjminfodemo.dto.Family;
import com.example.cxjminfodemo.utils.FamilyUtil;
import com.example.cxjminfodemo.utils.PersonalUtil;
import com.example.idcardscandemo.ACameraActivity;
import com.google.gson.Gson;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @Title InfoFamilyActivity
 * @author tengzj
 * @data 2016��8��23�� ����5:24:46
 */
public class InfoFamilyActivity extends Activity {

	/********** DECLARES *************/
	private Spinner edit_jhzzjlx;
	private EditText edit_hzxm;
	private EditText edit_lxdh;
	private EditText edit_dzyx;	private EditText edit_yzbm;
	private EditText edit_cjqtbxrs;
	private EditText edit_hkxxdz;
	private Calendar calendar;
	public static final int CAMERA = 1001;
	private String name = "";
	private String cardno = "";
	private String address = "";
	private DBManager mgr;
	String hasTemp;
	private Family defaultFamily = new Family("11111", "XXX", "����", "123456", "123456");
	Gson gson = new Gson();

	private Family tempFamily = new Family();

	@Bind(R.id.edit_djrq)
	TextView edit_djrq;

	@Bind(R.id.edit_gmcfzh)
	EditText edit_gmcfzh;

	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.info_family);
		ButterKnife.bind(InfoFamilyActivity.this);
		mgr = new DBManager(this);
		calendar = Calendar.getInstance();

		initView();
		/*
		 * Handler mHandler = new Handler(); Runnable r = new Runnable() {
		 * public void run() { // do something
		 * FamilyUtil.saveValue(getApplicationContext(),
		 * gson.toJson(defaultFamily)); System.out.println("first" +
		 * gson.toJson(defaultFamily)); } }; mHandler.post(r);
		 */
		setSampleFamily();

	}

	/**
	 * 
	 */
	private void setSampleFamily() {
		// TODO Auto-generated method stub
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras(); // ��ȡintent�����bundle����
		hasTemp = bundle.getString("hasTemp");
		if (hasTemp.equals("1")) {
			String str = bundle.getString("Family");
			tempFamily = gson.fromJson(str, Family.class);
			edit_hzxm.setText(tempFamily.getEdit_hzxm());
			edit_yzbm.setText(tempFamily.getEdit_yzbm());
			edit_gmcfzh.setText(tempFamily.getEdit_gmcfzh());
			edit_hkxxdz.setText(tempFamily.getEdit_hkxxdz());
		}
		if (hasTemp.equals("2")) {
			String str = bundle.getString("Family");
			tempFamily = gson.fromJson(str, Family.class);
			edit_hzxm.setText(tempFamily.getEdit_hzxm());
			edit_yzbm.setText(tempFamily.getEdit_yzbm());
			edit_hkxxdz.setText(tempFamily.getEdit_hkxxdz());
			edit_gmcfzh.setText(tempFamily.getEdit_gmcfzh());

			edit_jhzzjlx.setSelection(GetPos(tempFamily.edit_jhzzjlx));
			edit_cjqtbxrs.setText(tempFamily.edit_cjqtbxrs);
			edit_lxdh.setText(tempFamily.edit_lxdh);
			edit_djrq.setText(tempFamily.edit_djrq);
		}
	}

	private int GetPos(String edit_jhzzjlx2) {
		// TODO Auto-generated method stub
		int i = 0;
		switch (edit_jhzzjlx2) {
		case "�������֤�����ڲ���":
			i = 0;
			break;
		case "�й������ž�����֤":
			i = 1;
			break;
		case "�й�������װ���쾯��֤":
			i = 2;
			break;
		case "�����������/���֤��":
			i = 3;
			break;
		case "������������/���֤��":
			i = 4;
			break;
		case "̨�����������½ͨ��֤":
			i = 5;
			break;
		case "����˻���":
			i = 6;
			break;
		}
		return i;
	}

	/**
	 * 
	 */
	private void initView() {
		// TODO Auto-generated method stub

		/* ����֤������spiner */
		edit_jhzzjlx = (Spinner) findViewById(R.id.edit_jhzzjlx);
		ArrayList<String> data_list1 = new ArrayList<String>();
		data_list1.add("�������֤�����ڲ���");
		data_list1.add("�й������ž�����֤");
		data_list1.add("�й�������װ���쾯��֤");
		data_list1.add("�����������/���֤��");
		data_list1.add("������������/���֤��");
		data_list1.add("̨�����������½ͨ��֤");
		data_list1.add("����˻���");
		ArrayAdapter<String> arr_adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,
				data_list1);
		// ������ʽ
		arr_adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		edit_jhzzjlx.setAdapter(arr_adapter1);

		edit_hzxm = (EditText) findViewById(R.id.edit_hzxm);
		edit_lxdh = (EditText) findViewById(R.id.edit_lxdh);
		edit_dzyx = (EditText) findViewById(R.id.edit_dzyx);
		edit_yzbm = (EditText) findViewById(R.id.edit_yzbm);
		edit_cjqtbxrs = (EditText) findViewById(R.id.edit_cjqtbxrs);
		edit_hkxxdz = (EditText) findViewById(R.id.edit_hkxxdz);

		edit_djrq.setText(new StringBuilder().append(calendar.get(Calendar.YEAR)).append("-")
				.append((calendar.get(Calendar.MONTH) + 1) < 10 ? 0 + (calendar.get(Calendar.MONTH) + 1)
						: (calendar.get(Calendar.MONTH) + 1))
				.append("-").append((calendar.get(Calendar.DAY_OF_MONTH) < 10) ? 0 + calendar.get(Calendar.DAY_OF_MONTH)
						: calendar.get(Calendar.DAY_OF_MONTH)));
	}

	@OnClick(R.id.image_left)
	public void toInfoMainActivity() {
		intentMain();
		finish();
	}

	@Override
	public void onBackPressed() {
		intentMain();
		super.onBackPressed();
	}

	// intentΪA�����Ĵ���Bundle��intent����ȻҲ�����Լ������µ�Bundle
	private void intentMain() {
		if (!tempFamily.getEdit_gmcfzh().equals("")) {
			String str = gson.toJson(tempFamily);
			FamilyUtil.saveValue(getApplicationContext(), str);
			System.out.println("familyAct save" + gson.toJson(tempFamily));
			Intent intent = new Intent(InfoFamilyActivity.this, InfoMainActivity.class);
			intent.putExtra("Family", str);
			setResult(RESULT_OK, intent);
		}
	}

	@OnClick(R.id.edit_djrq)
	public void toDateDialog() {
		Toast.makeText(getApplicationContext(), "����", Toast.LENGTH_LONG).show();
		new DatePickerDialog(InfoFamilyActivity.this, new DatePickerDialog.OnDateSetListener() {
			@Override
			public void onDateSet(DatePicker view, int year, int month, int day) {
				// TODO Auto-generated method stub
				// ����EditText�ؼ����� С��10��0
				edit_djrq.setText(new StringBuilder().append(year).append("-")
						.append((month + 1) < 10 ? 0 + (month + 1) : (month + 1)).append("-")
						.append((day < 10) ? 0 + day : day));
			}
		}, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
	}

	@OnClick(R.id.btn_camera)
	public void toOCR() {
		Intent intent = new Intent(InfoFamilyActivity.this, ACameraActivity.class);
		startActivityForResult(intent, CAMERA);
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) { // resultCodeΪ�ش��ı�ǣ�����B�лش�����RESULT_OK
		case CAMERA:
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
						System.out.println(item.getStringValue());
						name = item.elementTextTrim("name");
						cardno = item.elementTextTrim("cardno");
						address = item.elementTextTrim("address");
					}
				}
			} catch (DocumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			edit_hzxm.setText(name);
			edit_gmcfzh.setText(cardno);
			edit_hkxxdz.setText(address);

			break;
		default:
			break;
		}
	}

	@OnClick(R.id.btn_save)
	public void toInfoMainActivity2() {
		if (edit_hzxm.getText().toString().isEmpty()) {
			Toast.makeText(getApplicationContext(), "������������Ϊ�գ�", Toast.LENGTH_SHORT).show();
		} else {
			// ��ɾ����
			if (hasTemp.equals("2"))
				mgr.deleteFamily(tempFamily);
			getDataFromEdit();

			Handler mHandler = new Handler();
			Runnable r = new Runnable() {
				public void run() {
					ArrayList<Family> familys = new ArrayList<Family>();
					Family family = new Family();
					family.setEdit_gmcfzh(tempFamily.edit_gmcfzh);
					family.setEdit_hzxm(tempFamily.edit_hzxm);
					family.setEdit_jhzzjlx(tempFamily.edit_jhzzjlx);
					family.setEdit_cjqtbxrs(tempFamily.edit_cjqtbxrs);
					family.setEdit_lxdh(tempFamily.edit_lxdh);
					family.setEdit_djrq(tempFamily.edit_djrq);
					family.setEdit_hkxxdz(tempFamily.edit_hkxxdz);
					familys.add(family);
					mgr.addFamily(familys);

					// do something
					// ͨ��OCR����ļ�ͥ��Ϣ
					String str = gson.toJson(tempFamily);
					FamilyUtil.saveValue(getApplicationContext(), str);
					System.out.println("familyAct save" + gson.toJson(tempFamily));
					Intent intent = new Intent(InfoFamilyActivity.this, InfoMainActivity.class);
					intent.putExtra("Family", str);
					setResult(RESULT_OK, intent); // intentΪA�����Ĵ���Bundle��intent����ȻҲ�����Լ������µ�Bundle
					finish();
				}
			};
			mHandler.post(r);
		}
	}

	private void getDataFromEdit() {
		Intent intent = getIntent();
		tempFamily.setEdit_gmcfzh(intent.getExtras().getString("gmcfzh"));
		// tempFamily.setEdit_jgszcwh(edit_jgszcwh.getText().toString());
		tempFamily.setEdit_hzxm(edit_hzxm.getText().toString());
		tempFamily.setEdit_lxdh(edit_lxdh.getText().toString());
		tempFamily.setEdit_dzyx(edit_dzyx.getText().toString());
		tempFamily.setEdit_yzbm(edit_yzbm.getText().toString());
		tempFamily.setEdit_cjqtbxrs(edit_cjqtbxrs.getText().toString());
		tempFamily.setEdit_hkxxdz(edit_hkxxdz.getText().toString());
		// spinner
		tempFamily.setEdit_jhzzjlx(edit_jhzzjlx.getSelectedItem().toString());
		tempFamily.setEdit_gmcfzh(edit_gmcfzh.getText().toString());
		tempFamily.setEdit_djrq(edit_djrq.getText().toString());

	}

}
