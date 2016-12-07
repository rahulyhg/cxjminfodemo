/**
 *@filename InfoFamilyActivity.java
 *@Email tengzhenjiu@qq.com
 *
 */
package com.megvii.idcardproject.InfoActivity;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import org.codehaus.jackson.JsonNode;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.megvii.idcardlib.util.Util;
import com.megvii.idcardproject.R;
import com.megvii.idcardproject.db.DBManager;
import com.megvii.idcardproject.dto.Code;
import com.megvii.idcardproject.dto.Family;
import com.megvii.idcardproject.utils.FamilyUtil;
import com.megvii.idcardproject.utils.IDCard;
import com.megvii.idcardquality.IDCardQualityLicenseManager;
import com.megvii.licensemanager.Manager;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;
import info.hoang8f.widget.FButton;

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
	private EditText edit_dzyx;
	private EditText edit_yzbm;
	private EditText edit_cjqtbxrs;
	private EditText edit_hkxxdz;
	private EditText edit_jtxxdz;
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
	String res = null;
	@Bind(R.id.edit_djrq)
	TextView edit_djrq;

	@Bind(R.id.edit_gmcfzh)
	EditText edit_gmcfzh;

	@Bind(R.id.btn_save)
	FButton btn_save;

	Activity activity;

	Bundle bundle;

	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.info_family);
		Intent intent = getIntent();
		bundle = intent.getExtras(); // ��ȡintent�����bundle����
		ButterKnife.bind(InfoFamilyActivity.this);
		mgr = new DBManager(this);
		calendar = Calendar.getInstance();
		activity = this;
		initView();
		setSampleFamily();
		try {
			fixID();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}

	private void fixID() throws ParseException {
		// TODO Auto-generated method stub
		if (hasTemp.equals("1")) {
			// ��݋��B�²��ɾ�݋
			edit_gmcfzh.setFocusable(false);
			edit_gmcfzh.setFocusableInTouchMode(false);
			res = IDCard.IDCardValidate(edit_gmcfzh.getText().toString());
			edit_gmcfzh.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					new SweetAlertDialog(activity, SweetAlertDialog.WARNING_TYPE).setTitleText("�༭״̬������֤���ɱ�")
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
						// ������B
						if (hasTemp.equals("0")) {
							Boolean hasFamily = false;
							for (Family tem : mgr.queryFamily(bundle.getString("XZQH"))) {
								if (tem.getEdit_gmcfzh().equals(temp.toString())) {
									// ���ݿ��Ѵ���
									new SweetAlertDialog(activity, SweetAlertDialog.WARNING_TYPE)
											.setTitleText("�Ѵ��ڸü�ͥ").setContentText(tem.getEdit_hzxm() + "\n"
													+ tem.edit_gmcfzh + "\n" + "�Ǽ����ڣ�" + tem.edit_djrq)
											.setConfirmText("��֪����").show();
									hasFamily = true;
									revert();
									break;
								}
							}
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

	/**
	 * 
	 */
	private void setSampleFamily() {
		// TODO Auto-generated method stub
		hasTemp = bundle.getString("hasTemp");
		if (hasTemp.equals("1")) {
			String str = bundle.getString("Family");
			tempFamily = gson.fromJson(str, Family.class);
			edit_hzxm.setText(tempFamily.getEdit_hzxm());
			edit_yzbm.setText(tempFamily.getEdit_yzbm());
			edit_hkxxdz.setText(tempFamily.getEdit_hkxxdz());
			edit_gmcfzh.setText(tempFamily.getEdit_gmcfzh());
			// �༭״̬Spnnier
			for (int i = 0; i < hkxz.size(); i++) {
				Code code = hkxz.get(i);
				String aaa103 = code.getAAA103();
				if (aaa103.equals(tempFamily.edit_jhzzjlx)) {
					edit_jhzzjlx.setSelection(i);
				}
			}
			edit_cjqtbxrs.setText(tempFamily.edit_cjqtbxrs);
			edit_lxdh.setText(tempFamily.edit_lxdh);
			edit_djrq.setText(tempFamily.edit_djrq);
			edit_jtxxdz.setText(tempFamily.edit_jtxxdz);
			if (tempFamily.getIsUpload().equals("1")) {
				// ���ϴ�
				new SweetAlertDialog(activity, SweetAlertDialog.WARNING_TYPE).setTitleText("�ü�ͥ��Ϣ���ϴ������ɱ���")
						.setConfirmText("��֪����").show();
				// ��ť�û�
				btn_save.setButtonColor(Color.rgb(204, 204, 204));
				btn_save.setShadowEnabled(false);
				btn_save.setClickable(false);
			}
		}
	}

	/**
	 * 
	 */
	private void initView() {
		// TODO Auto-generated method stub

		/* ����֤������spiner */
		edit_jhzzjlx = (Spinner) findViewById(R.id.edit_jhzzjlx);
		ArrayList<String> data_list1 = new ArrayList<String>();
		hkxz = mgr.queryCode("AAC058");

		for (int i = 0; i < hkxz.size(); i++) {
			Code code = hkxz.get(i);
			String aaa103 = code.getAAA103();
			// ����֤������
			data_list1.add(aaa103);
		}

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
		edit_jtxxdz = (EditText) findViewById(R.id.edit_jtxxdz);
		calendar = Calendar.getInstance();
		edit_djrq.setText(new StringBuilder().append(calendar.get(Calendar.YEAR)).append("-")
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

	@OnClick(R.id.image_left)
	public void toInfoMainActivity() {
		intentMain();
		finish();
	}

	@Override
	public void onBackPressed() {
		intentMain();
		super.onBackPressed();
		finish();
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

	@SuppressLint("HandlerLeak")
	Handler mHandler2 = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (msg.what == 1) {
				Intent intent = new Intent(InfoFamilyActivity.this, com.megvii.idcardlib.IDCardScanActivity.class);
				intent.putExtra("side", 0);
				intent.putExtra("isvertical", false);
				startActivityForResult(intent, CAMERA);
				setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
			} else if (msg.what == 2) {
				Toast.makeText(getApplicationContext(), "������Ȩʧ�ܣ�������ť������Ȩ", Toast.LENGTH_SHORT).show();
			}
		}
	};

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		switch (requestCode) { // resultCodeΪ�ش��ı�ǣ�����B�лش�����RESULT_OK
		case CAMERA:
			if (data == null)
				return;
			else if (resultCode == Activity.RESULT_OK) {
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
				edit_hzxm.setText(name);
				edit_gmcfzh.setText(cardno);
				edit_hkxxdz.setText(address);
			} else if (resultCode == Activity.RESULT_CANCELED) {
				String result = data.getStringExtra("result");
				Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
			}
		default:
			break;
		}
	}

	Runnable r = new Runnable() {
		public void run() {
			ArrayList<Family> familys = new ArrayList<Family>();
			Family family = new Family();
			getDataFromEdit();
			// ��������
			if (hasTemp.equals("1")) {
				// �༭״̬
				family.setEdit_jtbh(tempFamily.edit_jtbh);
				family.setId(tempFamily.id);
			} else {
				family.setEdit_jtbh(UUID.randomUUID().toString().replace("-", ""));
			}
			tempFamily.setIsEdit("1");
			family.setLsh(tempFamily.lsh);
			family.setEdit_gmcfzh(tempFamily.edit_gmcfzh);
			family.setEdit_hzxm(tempFamily.edit_hzxm);
			family.setEdit_jhzzjlx(tempFamily.edit_jhzzjlx);
			family.setEdit_cjqtbxrs(tempFamily.edit_cjqtbxrs);
			family.setEdit_lxdh(tempFamily.edit_lxdh);
			family.setEdit_djrq(tempFamily.edit_djrq);
			family.setEdit_hkxxdz(tempFamily.edit_hkxxdz);
			family.setEdit_jtxxdz(tempFamily.edit_jtxxdz);
			family.setXzqh(bundle.getString("XZQH"));
			family.setIsEdit(tempFamily.getIsEdit());
			family.setIsUpload(tempFamily.getIsUpload());
			familys.add(family);
			if (hasTemp.equals("1"))
				mgr.updateFamily(family);
			else
				mgr.addFamily(familys);

			// do something
			// ͨ��OCR����ļ�ͥ��Ϣ
			String str = gson.toJson(tempFamily);
			FamilyUtil.saveValue(getApplicationContext(), str);
			System.out.println("familyAct save" + gson.toJson(tempFamily));
			Intent intent = new Intent(InfoFamilyActivity.this, InfoMainActivity.class);
			intent.putExtra("Family", str);
			setResult(RESULT_OK, intent); // intentΪA�����Ĵ���Bundle��intent����ȻҲ�����Լ������µ�Bundle
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					new SweetAlertDialog(activity, SweetAlertDialog.SUCCESS_TYPE).setTitleText("����ɹ�").show();
				}
			});
		}
	};
	private Handler mHandler;
	private List<Code> hkxz;

	@OnClick(R.id.btn_save)
	public void toInfoMainActivity2() {
		mHandler = new Handler();
		if (edit_hkxxdz.getText().toString().isEmpty()) {
			Toast.makeText(getApplicationContext(), "���ڵ�ַ����Ϊ�գ�", Toast.LENGTH_SHORT).show();
		} else if (edit_hzxm.getText().toString().isEmpty()) {
			Toast.makeText(getApplicationContext(), "������������Ϊ�գ�", Toast.LENGTH_SHORT).show();
		} else if (edit_gmcfzh.getText().toString().isEmpty()) {
			Toast.makeText(getApplicationContext(), "֤�����벻��Ϊ�գ�", Toast.LENGTH_SHORT).show();
			// �ж�֤�������Ƿ��Ǿ�������֤�����ڲ���
		} else if (edit_jhzzjlx.getSelectedItem().equals("��������֤�����ڲ���")) {
			if (res != "") {
				Toast.makeText(getApplicationContext(), "��������֤�Ų���ȷ", Toast.LENGTH_SHORT).show();
			} else if (edit_gmcfzh.length() != 18) {
				Toast.makeText(getApplicationContext(), "��������֤�Ų���18λ��", Toast.LENGTH_SHORT).show();
			} else {
				success();
			}
		} else {
			success();
		}
	}

	private void success() {
		// TODO Auto-generated method stub
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
				finish();
			}
		}).start();
	}

	@OnClick(R.id.btn_revert)
	public void revert() {
		if (hasTemp.equals("1")) {
			// �༭״̬
			setSampleFamily();
		} else {
			edit_hzxm.setText("");
			edit_lxdh.setText("");
			edit_dzyx.setText("");
			edit_yzbm.setText("");
			edit_cjqtbxrs.setText("");
			edit_hkxxdz.setText("");
			edit_jtxxdz.setText("");
			// spinner
			edit_jhzzjlx.setSelection(0, true);
			edit_gmcfzh.setText("");
			// edit_djrq.setText("");
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
		tempFamily.setEdit_jtxxdz(edit_jtxxdz.getText().toString());
		// spinner
		tempFamily.setEdit_jhzzjlx(edit_jhzzjlx.getSelectedItem().toString());
		tempFamily.setEdit_gmcfzh(edit_gmcfzh.getText().toString());
		tempFamily.setEdit_djrq(edit_djrq.getText().toString());
	}
}