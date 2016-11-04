package com.example.cxjminfodemo.InfoActivity;

import java.io.File;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.example.cxjminfodemo.MainActivity;
import com.example.cxjminfodemo.R;
import com.example.cxjminfodemo.InfoActivity.PersonalActivity.InfoPersonalzxzfActivity;
import com.example.cxjminfodemo.db.DBManager;
import com.example.cxjminfodemo.dto.Family;
import com.example.cxjminfodemo.dto.Personal;
import com.example.cxjminfodemo.server.dto.UserDetail;
import com.example.cxjminfodemo.utils.CustomDialog;
import com.example.cxjminfodemo.utils.IDCard;
import com.example.cxjminfodemo.utils.PersonalUtil;
import com.example.idcardscandemo.ACameraActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemSelected;

/**
 * @Title InfoPersonalActivity
 * @author tengzj
 * @data 2016��8��23�� ����5:25:18
 */
public class InfoPersonalActivity extends Activity {

	private EditText edit_jtbh;
	private EditText edit_cbrxm;
	private EditText edit_gmcfzh;
	private EditText edit_lxdh;
	private TextView edit_csrq;
	private TextView edit_xxjzdz;
	private LinearLayout btn_save;
	private LinearLayout btn_xjzf;
	private LinearLayout btn_zxzf;

	private Spinner edit_zjlx;
	private Spinner edit_xb;
	private Spinner edit_yhzgx;
	private Spinner edit_cbrylb;
	private Spinner edit_hkxz;
	private Spinner edit_mz;
	private Calendar calendar;

	private String name = "";

	private String cardno = "";

	private String sex = "";

	private String folk = "";

	private String birthday = "";

	private String address = "";

	private String tag = "InfoPersonal";
	private DBManager mgr;

	Bundle bundle;

	String res = null;
	public static final int CAMERA = 1001;
	Personal tempPersonal;
	String HZSFZedit = "";
	Gson gson = new Gson();
	ArrayList<Personal> listPersonal = new ArrayList<Personal>();

	@Bind(R.id.edit_cbrq)
	TextView edit_cbrq;
	private TextView tv_xjzf;

	@Bind(R.id.btn_xyg)
	LinearLayout btn_xyg;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.info_personal);
		ButterKnife.bind(InfoPersonalActivity.this);
		mgr = new DBManager(this);
		Intent intent = getIntent();
		bundle = intent.getExtras(); // ��ȡintent�����bundle����
		tempPersonal = new Personal();
		initView();
		whenEdit();
		fixID();
	}

	/********** DECLARES *************/

	/**
	* 
	*/
	private void fixID() {
		// TODO Auto-generated method stub
		if (HZSFZedit != "") {
			Toast.makeText(getApplicationContext(), "�༭״̬�����֤���벻�ɱ�", Toast.LENGTH_SHORT).show();
			edit_gmcfzh.setFocusable(false);
			edit_gmcfzh.setFocusableInTouchMode(false);
			btn_xyg.setVisibility(View.GONE);
		}
		edit_gmcfzh.addTextChangedListener(new TextWatcher() {

			CharSequence temp;// ����ǰ���ı�
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

				editStart = edit_gmcfzh.getSelectionStart();
				editEnd = edit_gmcfzh.getSelectionEnd();
				if (temp.length() == charMaxNum) {

					try {
						res = IDCard.IDCardValidate(edit_gmcfzh.getText().toString());
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					if (res == "") {
						edit_csrq.setText(IDCard.printDate());
						/* edit_xb.setText(IDCard.printSex()); */
					} else
						Toast.makeText(getApplicationContext(), res, Toast.LENGTH_LONG).show();
				}

				if (temp.length() > charMaxNum) {
					s.delete(editStart - 1, editEnd);
					int tempSelection = editStart;
					edit_gmcfzh.setText(s);
					edit_gmcfzh.setSelection(tempSelection);
				}
			}
		});
	}

	/********** INITIALIZES *************/

	public void initView() {
		tv_xjzf = (TextView) findViewById(R.id.tv_xjzf);
		edit_cbrxm = (EditText) findViewById(R.id.edit_cbrxm);
		edit_gmcfzh = (EditText) findViewById(R.id.edit_gmcfzh);
		edit_csrq = (TextView) findViewById(R.id.edit_csrq);
		edit_xxjzdz = (TextView) findViewById(R.id.edit_xxjzdz);
		edit_lxdh = (EditText) findViewById(R.id.edit_lxdh);
		// Spiner1
		edit_yhzgx = (Spinner) findViewById(R.id.edit_yhzgx);
		ArrayList<String> data_list = new ArrayList<String>();
		data_list.add("����");
		data_list.add("����");
		data_list.add("��ĸ");
		data_list.add("��Ů");
		data_list.add("����");
		// ������
		ArrayAdapter<String> arr_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,
				data_list);
		arr_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		edit_yhzgx.setAdapter(arr_adapter);
		edit_yhzgx.setSelection(1, true);

		// Spiner2
		edit_cbrylb = (Spinner) findViewById(R.id.edit_cbrylb);

		ArrayList<String> data_list2 = new ArrayList<String>();

		data_list2.add("��ͨ�������");
		data_list2.add("�زг������");
		data_list2.add("�ͱ��������");
		data_list2.add("�屣�����������");
		data_list2.add("�������ͥ60������������");
		data_list2.add("�屣������ѧ��");
		data_list2.add("�ضȲм���ѧ��");
		data_list2.add("�ͱ���ѧ��");
		data_list2.add("��ͨ��ѧ��");
		data_list2.add("�屣������Сѧ��");
		data_list2.add("�ضȲм���Сѧ��");
		data_list2.add("�ͱ���Сѧ��");
		data_list2.add("��ͨ��Сѧ��");

		ArrayAdapter<String> arr_adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,
				data_list2);
		arr_adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		edit_cbrylb.setAdapter(arr_adapter2);

		// Spiner3
		edit_hkxz = (Spinner) findViewById(R.id.edit_hkxz);
		ArrayList<String> data_list3 = new ArrayList<String>();
		data_list3.add("ũҵ���ڣ�ũ�壩");
		data_list3.add("��ũҵ���ڣ�����");
		data_list3.add("���ط�ũҵ���ڣ����س���");
		data_list3.add("��ط�ũҵ���ڣ���س���");
		data_list3.add("����ũҵ���ڣ�����ũ�壩");
		data_list3.add("���ũҵ���ڣ����ũ�壩");
		data_list3.add("�۰�̨");
		data_list3.add("�⼮");
		ArrayAdapter<String> arr_adapter3 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,
				data_list3);
		arr_adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		edit_hkxz.setAdapter(arr_adapter3);
		edit_hkxz.setSelection(1, true);

		// Spiner4
		edit_mz = (Spinner) findViewById(R.id.edit_mz);
		ArrayList<String> data_list4 = new ArrayList<String>();
		data_list4.add("����");
		data_list4.add("����");
		data_list4.add("����");
		ArrayAdapter<String> arr_adapter4 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,
				data_list4);
		arr_adapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		edit_mz.setAdapter(arr_adapter4);
		// Spiner5
		edit_xb = (Spinner) findViewById(R.id.edit_xb);
		ArrayList<String> data_list5 = new ArrayList<String>();
		data_list5.add("��");
		data_list5.add("Ů");
		data_list5.add("δ˵���Ա�");
		ArrayAdapter<String> arr_adapter5 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,
				data_list5);
		arr_adapter5.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		edit_xb.setAdapter(arr_adapter5);
		// Spiner6
		edit_zjlx = (Spinner) findViewById(R.id.edit_zjlx);
		ArrayList<String> data_list6 = new ArrayList<String>();
		data_list6.add("�������֤�����ڲ���");
		data_list6.add("�й������ž�����֤");
		data_list6.add("�й�������װ���쾯��֤");
		data_list6.add("�����������/���֤��");
		data_list6.add("������������/���֤��");
		data_list6.add("̨�����������½ͨ��֤");
		data_list6.add("����˻���");
		ArrayAdapter<String> arr_adapter6 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,
				data_list6);
		arr_adapter6.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		edit_zjlx.setAdapter(arr_adapter6);

		// ����
		calendar = Calendar.getInstance();
		edit_cbrq.setText(new StringBuilder().append(calendar.get(Calendar.YEAR)).append("-")
				.append((calendar.get(Calendar.MONTH) + 1) < 10 ? 0 + (calendar.get(Calendar.MONTH) + 1)
						: (calendar.get(Calendar.MONTH) + 1))
				.append("-").append((calendar.get(Calendar.DAY_OF_MONTH) < 10) ? 0 + calendar.get(Calendar.DAY_OF_MONTH)
						: calendar.get(Calendar.DAY_OF_MONTH)));

	}
	/* Please visit http://www.ryangmattison.com for updates */

	private void whenEdit() {
		// ������Ϣ�༭״̬�� ���±�����
		// TODO Auto-generated method stub
		res = "";
		String per = bundle.getString("personal");
		if (per != null && per != "") {
			Personal personal = new Gson().fromJson(per, new TypeToken<Personal>() {
			}.getType());
			HZSFZedit = personal.HZSFZ;
			String yhzgx = personal.edit_yhzgx;
			if (yhzgx.equals("����"))
				edit_yhzgx.setSelection(0, true);
			if (yhzgx.equals("����"))
				edit_yhzgx.setSelection(1, true);
			if (yhzgx.equals("��ĸ"))
				edit_yhzgx.setSelection(2, true);
			if (yhzgx.equals("��Ů"))
				edit_yhzgx.setSelection(3, true);
			if (yhzgx.equals("����"))
				edit_yhzgx.setSelection(4, true);

			String zjlx = personal.edit_zjlx;
			if (zjlx.equals("�������֤�����ڲ���"))
				edit_zjlx.setSelection(0, true);
			if (zjlx.equals("�й������ž�����֤"))
				edit_zjlx.setSelection(1, true);
			if (zjlx.equals("�й�������װ���쾯��֤"))
				edit_zjlx.setSelection(2, true);
			if (zjlx.equals("�����������/���֤��"))
				edit_zjlx.setSelection(3, true);
			if (zjlx.equals("������������/���֤��"))
				edit_zjlx.setSelection(4, true);
			if (zjlx.equals("̨�����������½ͨ��֤"))
				edit_zjlx.setSelection(5, true);
			if (zjlx.equals("����˻���"))
				edit_zjlx.setSelection(6, true);

			String xb = personal.edit_xb;
			if (xb.equals("��"))
				edit_xb.setSelection(0, true);
			if (xb.equals("Ů"))
				edit_xb.setSelection(1, true);
			if (xb.equals("δ˵���Ա�"))
				edit_xb.setSelection(2, true);

			edit_cbrxm.setText(personal.getEdit_cbrxm());
			edit_gmcfzh.setText(personal.getEdit_gmcfzh());
			edit_xxjzdz.setText(personal.getEdit_xxjzdz());
			edit_lxdh.setText(personal.getEdit_lxdh());
			String temp_folk = personal.getEdit_mz();
			if (temp_folk.equals("��"))
				edit_mz.setSelection(0, true);
			if (temp_folk.equals("��"))
				edit_mz.setSelection(1, true);
			if (temp_folk.equals("��"))
				edit_mz.setSelection(2, true);
			edit_csrq.setText(personal.edit_cbrq);

			String hkxz = personal.getEdit_hkxz();
			if (hkxz.equals("ũҵ���ڣ�ũ�壩"))
				edit_hkxz.setSelection(0, true);
			if (hkxz.equals("��ũҵ���ڣ�����"))
				edit_hkxz.setSelection(1, true);
			if (hkxz.equals("���ط�ũҵ���ڣ����س���"))
				edit_hkxz.setSelection(2, true);
			if (hkxz.equals("��ط�ũҵ���ڣ���س���"))
				edit_hkxz.setSelection(3, true);
			if (hkxz.equals("����ũҵ���ڣ�����ũ�壩"))
				edit_hkxz.setSelection(4, true);
			if (hkxz.equals("���ũҵ���ڣ����ũ�壩"))
				edit_hkxz.setSelection(5, true);
			if (hkxz.equals("�۰�̨"))
				edit_hkxz.setSelection(6, true);
			if (hkxz.equals("�⼮"))
				edit_hkxz.setSelection(7, true);

			String cbrylb = personal.getEdit_cbrylb();
			if (cbrylb.equals("��ͨ�������"))
				edit_cbrylb.setSelection(0, true);
			if (cbrylb.equals("�زг������"))
				edit_cbrylb.setSelection(1, true);
			if (cbrylb.equals("�ͱ��������"))
				edit_cbrylb.setSelection(2, true);
			if (cbrylb.equals("�屣�����������"))
				edit_cbrylb.setSelection(3, true);
			if (cbrylb.equals("�������ͥ60������������"))
				edit_cbrylb.setSelection(4, true);
			if (cbrylb.equals("�屣������ѧ��"))
				edit_cbrylb.setSelection(5, true);
			if (cbrylb.equals("�ضȲм���ѧ��"))
				edit_cbrylb.setSelection(6, true);
			if (cbrylb.equals("�ͱ���ѧ��"))
				edit_cbrylb.setSelection(7, true);

			if (cbrylb.equals("��ͨ��ѧ��"))
				edit_cbrylb.setSelection(8, true);
			if (cbrylb.equals("�屣������Сѧ��"))
				edit_cbrylb.setSelection(9, true);
			if (cbrylb.equals("�ضȲм���Сѧ��"))
				edit_cbrylb.setSelection(10, true);

			if (cbrylb.equals("�ͱ���Сѧ��"))
				edit_cbrylb.setSelection(11, true);
			if (cbrylb.equals("��ͨ��Сѧ��"))
				edit_cbrylb.setSelection(12, true);

		}

	}

	@OnItemSelected(R.id.edit_yhzgx)
	void onItemSelected(int position) {
		switch (position) {
		case 0:
			for (Family family : mgr.queryFamily()) {
				if (family.getEdit_gmcfzh().equals(bundle.getString("HZSFZ"))) {
					edit_cbrxm.setText(family.getEdit_hzxm());
					edit_gmcfzh.setText(family.getEdit_gmcfzh());
					edit_xxjzdz.setText(family.getEdit_hkxxdz());
					/* edit_xb.setText(bundle.getString("sex")); */
				}
			}
			break;
		default:
			/*
			 * edit_cbrxm.setText(""); edit_gmcfzh.setText("");
			 * edit_xb.setText(""); edit_csrq.setText("");
			 * edit_xxjzdz.setText(""); tv_xjzf.setText("�ֽ�֧��");
			 * tv_xjzf.setTextColor(Color.BLACK);
			 */
			break;
		}
	}

	@OnClick(R.id.image_left)
	public void toinfoMainActivity() {
		Toast.makeText(getApplicationContext(), "δ��������ݽ�������ʾ", Toast.LENGTH_LONG).show();
		finish();
	}

	@OnClick(R.id.edit_cbrq)
	public void toDateDialog() {
		new DatePickerDialog(InfoPersonalActivity.this, new DatePickerDialog.OnDateSetListener() {
			@Override
			public void onDateSet(DatePicker view, int year, int month, int day) {
				// TODO Auto-generated method stub
				// ����EditText�ؼ����� С��10��0
				edit_cbrq.setText(new StringBuilder().append(year).append("-")
						.append((month + 1) < 10 ? 0 + (month + 1) : (month + 1)).append("-")
						.append((day < 10) ? 0 + day : day));
			}
		}, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
	}

	@OnClick(R.id.btn_xjzf)
	public void xjzf() {
		CustomDialog.Builder builder = new CustomDialog.Builder(this);
		builder.setMessage("ȷ������ֽ�֧������");
		builder.setTitle("��ʾ");
		builder.setPositiveButton("ȷ��", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				Handler mHandler = new Handler();
				dialog.dismiss();
				Toast.makeText(getApplicationContext(), "����ֽ�֧��", Toast.LENGTH_SHORT).show();
				tempPersonal.setEdit_jf("1");
				tv_xjzf.setText("֧�����");
				tv_xjzf.setTextColor(Color.BLUE);

				if (edit_cbrxm.getText().toString().isEmpty())
					Toast.makeText(getApplicationContext(), "�α�����������Ϊ��", Toast.LENGTH_SHORT).show();
				else if (edit_gmcfzh.getText().toString().isEmpty())
					Toast.makeText(getApplicationContext(), "�������֤�Ų���Ϊ��", Toast.LENGTH_SHORT).show();
				else if (res != "")
					Toast.makeText(getApplicationContext(), "�������֤�Ų���ȷ", Toast.LENGTH_SHORT).show();
				else
					mHandler.post(r);
			}
		});
		builder.setNegativeButton("ȡ��", new android.content.DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});

		builder.create().show();
	}

	@OnClick(R.id.btn_zxzf)
	public void zxzf() {
		Toast.makeText(getApplicationContext(), "�������֧��", Toast.LENGTH_SHORT).show();
		tempPersonal.setEdit_jf("1");
		Intent intent = new Intent(this, InfoPersonalzxzfActivity.class);
		startActivity(intent);
	}

	Runnable r = new Runnable() {
		public void run() {
			ArrayList<Personal> personals = new ArrayList<Personal>();
			getDataFromEdit();
			Personal personal1 = new Personal();
			personal1.setEdit_yhzgx(tempPersonal.edit_yhzgx);
			personal1.setEdit_cbrxm(tempPersonal.edit_cbrxm);
			personal1.setEdit_zjlx(tempPersonal.edit_zjlx);
			personal1.setEdit_gmcfzh(tempPersonal.edit_gmcfzh);

			personal1.setEdit_mz(tempPersonal.edit_mz);
			personal1.setEdit_xb(tempPersonal.edit_xb);
			personal1.setEdit_cbrq(tempPersonal.edit_cbrq);
			personal1.setEdit_cbrylb(tempPersonal.edit_cbrylb);
			personal1.setEdit_csrq(tempPersonal.edit_csrq);

			personal1.setEdit_hkxz(tempPersonal.edit_hkxz);

			personal1.setEdit_xxjzdz(tempPersonal.edit_xxjzdz);
			personal1.setEdit_lxdh(tempPersonal.edit_lxdh);
			personal1.setEdit_jf(tempPersonal.edit_jf);
			if (bundle.getString("HZSFZ") != null && bundle.getString("HZSFZ") != "") {
				// ����״̬
				personal1.setHZSFZ(bundle.getString("HZSFZ"));
				Boolean hasPersonal = false;
				for (Personal tem : mgr.queryPersonal()) {
					if (tem.getEdit_gmcfzh().equals(tempPersonal.edit_gmcfzh)) {
						// ���ݿ��Ѵ���
						Toast.makeText(getApplicationContext(), "�òα����Ѵ��ڣ���ɾ�������", Toast.LENGTH_SHORT).show();
						hasPersonal = true;
						break;
					}
				}
				if (!hasPersonal) {
					// �����ڲα���
					personals.add(personal1);
					mgr.addPersonal(personals);
					Toast.makeText(getApplicationContext(), "�ѱ���", Toast.LENGTH_SHORT).show();
				}
			} else {
				// �༭״̬
				personal1.setHZSFZ(tempPersonal.HZSFZ);
				personals.add(personal1);
				mgr.addPersonal(personals);
				Toast.makeText(getApplicationContext(), "�ѱ���", Toast.LENGTH_SHORT).show();
			}
		}
	};

	@OnClick(R.id.btn_save)
	public void toInfoMainActivity2() {
		Handler mHandler = new Handler();
		if (edit_cbrxm.getText().toString().isEmpty())
			Toast.makeText(getApplicationContext(), "�α�����������Ϊ��", Toast.LENGTH_SHORT).show();
		else if (edit_gmcfzh.getText().toString().isEmpty())
			Toast.makeText(getApplicationContext(), "�������֤�Ų���Ϊ��", Toast.LENGTH_SHORT).show();
		else if (res != "")
			Toast.makeText(getApplicationContext(), "�������֤�Ų���ȷ", Toast.LENGTH_SHORT).show();
		else
			mHandler.post(r);
	}

	@OnClick(R.id.btn_xyg)
	public void toNextActivity() {
		edit_cbrxm.setText("");
		edit_gmcfzh.setText("");
		/* edit_xb.setText(""); */
		edit_csrq.setText("");
		edit_yhzgx.setSelection(1, true);
		edit_xxjzdz.setText("");
		tv_xjzf.setText("�ֽ�֧��");
		tv_xjzf.setTextColor(Color.BLACK);
		Toast.makeText(getApplicationContext(), "�Ѿ���ת����һ��", Toast.LENGTH_LONG).show();
	}

	@OnClick(R.id.btn_camera)
	public void toOCR() {
		Intent intent = new Intent(InfoPersonalActivity.this, ACameraActivity.class);
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
						sex = item.elementTextTrim("sex");
						folk = item.elementTextTrim("folk");
						birthday = item.elementTextTrim("birthday");
						address = item.elementTextTrim("address");

						Log.i(tag, cardno);
					}
				}
			} catch (DocumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			edit_cbrxm.setText(name);
			edit_gmcfzh.setText(cardno);
			edit_xxjzdz.setText(address);
			/* edit_xb.setText(sex); */
			if (folk.equals("��"))
				edit_mz.setSelection(0);
			if (folk.equals("��"))
				edit_mz.setSelection(1);
			if (folk.equals("��"))
				edit_mz.setSelection(2);
			edit_csrq.setText(birthday);

			break;
		default:
			break;
		}
	}

	private void getDataFromEdit() {
		// TODO Auto-generated method stub
		tempPersonal.setEdit_yhzgx(edit_yhzgx.getSelectedItem().toString());
		tempPersonal.setEdit_cbrxm(edit_cbrxm.getText().toString());

		tempPersonal.setEdit_zjlx(edit_zjlx.getSelectedItem().toString());

		tempPersonal.setEdit_gmcfzh(edit_gmcfzh.getText().toString());
		/* tempPersonal.setEdit_xb(edit_xb.getText().toString()); */
		tempPersonal.setEdit_mz(edit_mz.getSelectedItem().toString());
		tempPersonal.setEdit_xb(edit_xb.getSelectedItem().toString());

		tempPersonal.setEdit_csrq(edit_csrq.getText().toString());
		tempPersonal.setEdit_cbrq(edit_cbrq.getText().toString());

		tempPersonal.setEdit_cbrylb(edit_cbrylb.getSelectedItem().toString());
		tempPersonal.setEdit_hkxz(edit_hkxz.getSelectedItem().toString());
		tempPersonal.setHZSFZ(HZSFZedit);
		tempPersonal.setEdit_xxjzdz(edit_xxjzdz.getText().toString());
		tempPersonal.setEdit_lxdh(edit_lxdh.getText().toString());
		listPersonal.add(tempPersonal);
	}
}
