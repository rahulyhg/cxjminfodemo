package com.neuqsoft.cxjmcj.InfoActivity;

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

import com.neuqsoft.cxjmcj.R;
import com.example.idcardscandemo.ACameraActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.neuqsoft.cxjmcj.WelcomeActivity;

import com.neuqsoft.cxjmcj.db.DBManager;
import com.neuqsoft.cxjmcj.dto.Code;
import com.neuqsoft.cxjmcj.dto.Family;
import com.neuqsoft.cxjmcj.dto.Personal;
import com.neuqsoft.cxjmcj.dto.UserDetail;
import com.neuqsoft.cxjmcj.utils.FamilyUtil;
import com.neuqsoft.cxjmcj.utils.IDCard;
import com.neuqsoft.cxjmcj.utils.PersonalUtil;

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
import cn.pedant.SweetAlert.SweetAlertDialog;
import info.hoang8f.widget.FButton;

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
	String JTBHedit = "";// �Ƿ�Ϊ�༭״̬
	Gson gson = new Gson();
	ArrayList<Personal> listPersonal = new ArrayList<Personal>();
	Personal editPersonal;

	@Bind(R.id.edit_cbrq)
	TextView edit_cbrq;

	@Bind(R.id.btn_xyg)
	FButton btn_xyg;

	@Bind(R.id.img_xjzf)
	ImageView img_xjzf;
	@Bind(R.id.btn_xjzf)
	FButton btn_xjzf;
	Activity activity;

	@Bind(R.id.btn_save)
	FButton btn_save;

	Boolean tip_xjzf = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.info_personal);
		activity = this;
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
		if (JTBHedit != "") {
			edit_gmcfzh.setFocusable(false);
			edit_gmcfzh.setFocusableInTouchMode(false);
			try {
				res = IDCard.IDCardValidate(edit_gmcfzh.getText().toString());
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			edit_gmcfzh.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					new SweetAlertDialog(activity, SweetAlertDialog.WARNING_TYPE).setTitleText("�༭״̬�����֤���ɱ�")
							.setConfirmText("��֪����").show();
				}
			});
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
						Boolean hasPersonal = false;
						// ������B
						if (JTBHedit.equals("")) {
							for (Family tem : mgr.queryFamily(bundle.getString("XZQH"))) {
								for (Personal per : mgr.queryPersonal(tem.getEdit_jtbh())) {
									if (per.getEdit_gmcfzh().equals(temp.toString())) {
										// ���ݿ��Ѵ���
										new SweetAlertDialog(activity, SweetAlertDialog.WARNING_TYPE)
												.setTitleText("�Ѵ��ڸ���Ա")
												.setContentText(per.edit_cbrxm + "\n" + per.edit_gmcfzh + "\n"
														+ "�������֤���룺" + mgr.queryFamilyByJtbh(per.HZSFZ).getEdit_gmcfzh()
														+ "\n" + "�Ǽ����ڣ�" + per.edit_cbrq)
												.setConfirmText("��֪����").show();
										hasPersonal = true;
										revert();
										break;
									}
								}
							}
						}
						if (!hasPersonal) {
							edit_csrq.setText(IDCard.printDate());
							String xb = IDCard.printSex();
							if (xb.equals("��"))
								edit_xb.setSelection(0, true);
							if (xb.equals("Ů"))
								edit_xb.setSelection(1, true);
							if (xb.equals("δ˵���Ա�"))
								edit_xb.setSelection(2, true);
						}
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
		edit_cbrxm = (EditText) findViewById(R.id.edit_cbrxm);
		edit_gmcfzh = (EditText) findViewById(R.id.edit_gmcfzh);
		edit_csrq = (TextView) findViewById(R.id.edit_csrq);
		edit_xxjzdz = (TextView) findViewById(R.id.edit_xxjzdz);
		edit_lxdh = (EditText) findViewById(R.id.edit_lxdh);
		// Spiner1
		edit_yhzgx = (Spinner) findViewById(R.id.edit_yhzgx);

		// ����뻧����ϵ
		addsp("AAC069", edit_yhzgx);
		edit_yhzgx.setSelection(1, true);

		// ������
		// ArrayAdapter<String> arr_adapter = new ArrayAdapter<String>(this,
		// android.R.layout.simple_spinner_item,
		// data_list);
		// arr_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// edit_yhzgx.setAdapter(arr_adapter);
		// edit_yhzgx.setSelection(1, true);

		// Spiner2
		edit_cbrylb = (Spinner) findViewById(R.id.edit_cbrylb);

		addsp("BAC067", edit_cbrylb);

		// Spiner3
		edit_hkxz = (Spinner) findViewById(R.id.edit_hkxz);

		addsp("AAC009", edit_hkxz);

		// Spiner4
		edit_mz = (Spinner) findViewById(R.id.edit_mz);
		// �������
		addsp("AAC005", edit_mz);
		// Spiner5
		edit_xb = (Spinner) findViewById(R.id.edit_xb);
		// ����Ա�
		addsp("AAC004", edit_xb);

		// Spiner6
		edit_zjlx = (Spinner) findViewById(R.id.edit_zjlx);

		// ���֤������
		addsp("AAC058", edit_zjlx);
		// ����
		calendar = Calendar.getInstance();
		edit_cbrq.setText(new StringBuilder().append(calendar.get(Calendar.YEAR)).append("-")
				.append((calendar.get(Calendar.MONTH) + 1) < 10 ? "0" + (calendar.get(Calendar.MONTH) + 1)
						: (calendar.get(Calendar.MONTH) + 1))
				.append("-")
				.append((calendar.get(Calendar.DAY_OF_MONTH) < 10) ? "0" + calendar.get(Calendar.DAY_OF_MONTH)
						: calendar.get(Calendar.DAY_OF_MONTH))
				.append(" ")
				.append((calendar.get(Calendar.HOUR_OF_DAY) < 10) ? "0" + calendar.get(Calendar.HOUR_OF_DAY)
						: calendar.get(Calendar.HOUR_OF_DAY))
				.append(":")
				.append((calendar.get(Calendar.MINUTE) < 10) ? "0" + calendar.get(Calendar.MINUTE)
						: calendar.get(Calendar.MINUTE))
				.append(":").append((calendar.get(Calendar.SECOND) < 10) ? "0" + calendar.get(Calendar.SECOND)
						: calendar.get(Calendar.SECOND)));

	}

	// ���Spnner
	public void addsp(String AAA100, Spinner spinner) {
		ArrayList<String> data_list = new ArrayList<String>();
		List<Code> lx = mgr.queryCode(AAA100);
		for (int i = 0; i < lx.size(); i++) {
			Code code = lx.get(i);
			String aaa103 = code.getAAA103();
			// ��Ӿ������
			data_list.add(aaa103);
		}
		// ������
		ArrayAdapter<String> arr_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,
				data_list);
		arr_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(arr_adapter);

	}

	public void bjsp(String AAA100, String data, Spinner spinner) {
		List<Code> lx1 = mgr.queryCode(AAA100);
		for (int i = 0; i < lx1.size(); i++) {
			Code code = lx1.get(i);
			String aaa103 = code.getAAA103();
			if (aaa103.equals(data)) {
				spinner.setSelection(i, true);
			}
		}
	}
	/* Please visit http://www.ryangmattison.com for updates */

	private void whenEdit() {
		// ������Ϣ�༭״̬�� ���±�����
		// TODO Auto-generated method stub
		res = "";
		String per = bundle.getString("personal");
		if (per != null && per != "") {
			editPersonal = new Gson().fromJson(per, new TypeToken<Personal>() {
			}.getType());
			JTBHedit = editPersonal.HZSFZ;
			editPersonal = mgr.queryPersonalByGmsfzh(editPersonal.getEdit_gmcfzh());
			tempPersonal = editPersonal;
			setContent(editPersonal);
			if (editPersonal.getIsUpload().equals("1")) {
				new SweetAlertDialog(activity, SweetAlertDialog.WARNING_TYPE).setTitleText("�ü�ͥ��Ϣ���ϴ������ɱ���")
						.setConfirmText("��֪����").show();
				// ��ť�û�
				btn_save.setButtonColor(Color.rgb(204, 204, 204));
				btn_save.setShadowEnabled(false);
				btn_save.setClickable(false);
				btn_xjzf.setButtonColor(Color.rgb(204, 204, 204));
				btn_xjzf.setShadowEnabled(false);
				btn_xjzf.setClickable(false);
			}
		}
	}

	private void setContent(Personal personal) {

		// �뻧����ϵ
		String yhzgx = personal.edit_yhzgx;
		bjsp("AAC069", yhzgx, edit_yhzgx);
		// ֤������
		String zjlx = personal.edit_zjlx;
		bjsp("AAC058", zjlx, edit_zjlx);
		// �Ա�
		String xb = personal.edit_xb;
		bjsp("AAC004", xb, edit_xb);

		edit_cbrxm.setText(personal.getEdit_cbrxm());
		edit_gmcfzh.setText(personal.getEdit_gmcfzh());
		edit_xxjzdz.setText(personal.getEdit_xxjzdz());
		edit_lxdh.setText(personal.getEdit_lxdh());
		//����
		String temp_folk = personal.edit_mz;
		bjsp("AAC005", temp_folk, edit_mz);
		edit_cbrq.setText(personal.edit_cbrq);
		edit_csrq.setText(personal.edit_csrq);
		// ��������
		String hkxz = personal.edit_hkxz;
		bjsp("AAC009", hkxz, edit_hkxz);
		// ��Ա���
		String cbrylb = personal.edit_cbrylb;
		bjsp("BAC067", cbrylb, edit_cbrylb);

		if (personal.getEdit_jf().equals("1")) {
			btn_xjzf.setText("ȡ��֧��");
			img_xjzf.setImageResource(R.drawable.yjf);
		} else {
			btn_xjzf.setText("�ֽ�֧��");
			img_xjzf.setImageResource(R.drawable.djf);
		}
	}

	@OnItemSelected(R.id.edit_yhzgx)
	void onItemSelected(int position) {
		switch (position) {
		case 0:
			if (JTBHedit.equals("")) {
				// ����״̬
				Family family = mgr.queryFamilyByJtbh(bundle.getString("JTBH"));
				// �����֤������ѡ��
				edit_cbrxm.setText(family.getEdit_hzxm());
				edit_gmcfzh.setText(family.getEdit_gmcfzh());
				edit_xxjzdz.setText(family.getEdit_hkxxdz());
				edit_lxdh.setText(family.getEdit_lxdh());
			}
			break;
		default:
			if (JTBHedit.equals("")) {
				// ����״̬
				edit_cbrxm.setText("");
				edit_gmcfzh.setText("");
				edit_zjlx.setSelection(0, true);
				edit_mz.setSelection(0, true);
				edit_xb.setSelection(0, true);
				edit_csrq.setText("");
				img_xjzf.setImageResource(R.drawable.djf);
				btn_xjzf.setText("�ֽ�֧��");
			}
			break;
		}
	}

	@OnClick(R.id.image_left)
	public void toinfoMainActivity() {
		intentMain();
		finish();
	}

	@Override
	public void onBackPressed() {
		intentMain();
		super.onBackPressed();
	}

	private void intentMain() {
		if (!tempPersonal.getEdit_gmcfzh().equals("")) {
			Intent intent = new Intent(InfoPersonalActivity.this, InfoMainActivity.class);
			setResult(RESULT_OK, intent);
		}
	}

	@OnClick(R.id.edit_csrq)
	public void toDateDialog2() {
		new DatePickerDialog(InfoPersonalActivity.this, new DatePickerDialog.OnDateSetListener() {
			@Override
			public void onDateSet(DatePicker view, int year, int month, int day) {
				edit_csrq.setText(new StringBuilder().append(year).append("-")
						.append((month + 1) < 10 ? 0 + (month + 1) : (month + 1)).append("-")
						.append((day < 10) ? 0 + day : day));
				// TODO Auto-generated method stub
			}
		}, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();

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
		tip_xjzf = true;
		if (btn_xjzf.getText().toString().equals("�ֽ�֧��")) {
			tempPersonal.setEdit_jf("1");
			save();
		} else {
			tempPersonal.setEdit_jf("0");
			save();
		}
	}

	Runnable r = new Runnable() {
		public void run() {
			ArrayList<Personal> personals = new ArrayList<Personal>();
			getDataFromEdit();
			Personal personal1 = new Personal();
			personal1.setEdit_grbh(tempPersonal.edit_grbh);
			personal1.setLsh(tempPersonal.lsh);
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
			personal1.setIsEdit(tempPersonal.isEdit);
			personal1.setEdit_xxjzdz(tempPersonal.edit_xxjzdz);
			personal1.setEdit_lxdh(tempPersonal.edit_lxdh);
			personal1.setEdit_jf(tempPersonal.edit_jf);
			if (JTBHedit.equals("")) {
				// ����״̬
				personal1.setHZSFZ(bundle.getString("JTBH"));
				personal1.setIsEdit("1");
				personals.add(personal1);
				mgr.addPersonal(personals);
			} else {
				// �༭״̬
				if (tempPersonal.getIsUpload().equals("2")) {
					personal1.setIsEdit("1");
				}
				personal1.setId(tempPersonal.id);
				personal1.setHZSFZ(JTBHedit);
				mgr.updatePersonal(personal1);
			}
		}
	};

	private Handler mHandler;

	@OnClick(R.id.btn_save)
	public void save() {
		mHandler = new Handler();
		if (edit_cbrxm.getText().toString().isEmpty())
			Toast.makeText(getApplicationContext(), "�α�����������Ϊ��", Toast.LENGTH_SHORT).show();
		else if (edit_gmcfzh.getText().toString().isEmpty())
			Toast.makeText(getApplicationContext(), "֤�����벻��Ϊ��", Toast.LENGTH_SHORT).show();
		// �ж�֤�������Ƿ��Ǿ������֤�����ڲ���
		else if (edit_zjlx.getSelectedItem().equals("�������֤�����ڲ���")) {
			if (res != "")
				Toast.makeText(getApplicationContext(), "�������֤�Ų���ȷ", Toast.LENGTH_SHORT).show();
			else if (edit_gmcfzh.length() != 18) {
				Toast.makeText(getApplicationContext(), "�������֤�Ų���18λ��", Toast.LENGTH_SHORT).show();
			} else {
				success();
			}
		} else {
			success();
		}
	}

	private void success() {
		if(tempPersonal.getEdit_jf().equals("1"))
		{
			img_xjzf.setImageResource(R.drawable.yjf);
			btn_xjzf.setText("ȡ��֧��");
		}
		else
		{
			img_xjzf.setImageResource(R.drawable.djf);
			btn_xjzf.setText("�ֽ�֧��");
		}
		final SweetAlertDialog dialog = new SweetAlertDialog(activity, SweetAlertDialog.SUCCESS_TYPE)
				.setTitleText("����ɹ�");
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				dialog.show();
			}
		});
		mHandler.post(r);
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
				dialog.dismiss();
				if (JTBHedit != "") {
					// �༭״̬
					toinfoMainActivity();
				} else{
					// ����״̬
					revert();
				}
			}
		}).start();
	}

	@OnClick(R.id.btn_xyg)
	public void revert() {
		this.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				if (JTBHedit != "") {
					// �༭״̬
					setContent(editPersonal);
				} else {
					edit_cbrxm.setText("");
					edit_gmcfzh.setText("");
					/* edit_xb.setText(""); */
					edit_csrq.setText("");
					edit_yhzgx.setSelection(1, true);
					edit_xxjzdz.setText(tempPersonal.edit_xxjzdz);
					edit_zjlx.setSelection(0, true);
					edit_mz.setSelection(0, true);
					edit_xb.setSelection(1, true);
					edit_cbrylb.setSelection(0, true);
					edit_hkxz.setSelection(0, true);
					edit_lxdh.setText(tempPersonal.edit_lxdh);
				}
			}

		});
	}

	@OnClick(R.id.btn_camera)
	public void toOCR() {
		Intent intent = new Intent(InfoPersonalActivity.this, ACameraActivity.class);
		startActivityForResult(intent, CAMERA);
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) { // resultCodeΪ�ش��ı�ǣ�����B�лش�����RESULT_OK
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
			}
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
		tempPersonal.setEdit_xxjzdz(edit_xxjzdz.getText().toString());
		tempPersonal.setEdit_lxdh(edit_lxdh.getText().toString());
		tempPersonal.setEdit_jf(btn_xjzf.getText().toString().equals("ȡ��֧��") ? "1" : "0");
		listPersonal.add(tempPersonal);
	}
}
