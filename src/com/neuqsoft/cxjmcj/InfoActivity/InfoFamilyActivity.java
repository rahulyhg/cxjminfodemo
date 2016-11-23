/**
 *@filename InfoFamilyActivity.java
 *@Email tengzhenjiu@qq.com
 *
 */
package com.neuqsoft.cxjmcj.InfoActivity;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.neuqsoft.cxjmcj.R;
import com.dou361.dialogui.DialogUIUtils;
import com.example.idcardscandemo.ACameraActivity;
import com.google.gson.Gson;
import com.neuqsoft.cxjmcj.WelcomeActivity;
import com.neuqsoft.cxjmcj.db.DBManager;
import com.neuqsoft.cxjmcj.dto.Family;
import com.neuqsoft.cxjmcj.dto.Personal;
import com.neuqsoft.cxjmcj.utils.FamilyUtil;
import com.neuqsoft.cxjmcj.utils.IDCard;
import com.neuqsoft.cxjmcj.utils.PersonalUtil;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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

/**
 * @Title InfoFamilyActivity
 * @author tengzj
 * @data 2016年8月23日 下午5:24:46
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
	private Family defaultFamily = new Family("11111", "XXX", "张三", "123456", "123456");
	Gson gson = new Gson();

	private Family tempFamily = new Family();
	String res = null;
	@Bind(R.id.edit_djrq)
	TextView edit_djrq;

	@Bind(R.id.edit_gmcfzh)
	EditText edit_gmcfzh;

	Activity activity;

	Bundle bundle;

	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.info_family);
		Intent intent = getIntent();
		bundle = intent.getExtras(); // 获取intent里面的bundle对象
		ButterKnife.bind(InfoFamilyActivity.this);
		mgr = new DBManager(this);
		calendar = Calendar.getInstance();
		activity = this;
		initView();
		/*
		 * Handler mHandler = new Handler(); Runnable r = new Runnable() {
		 * public void run() { // do something
		 * FamilyUtil.saveValue(getApplicationContext(),
		 * gson.toJson(defaultFamily)); System.out.println("first" +
		 * gson.toJson(defaultFamily)); } }; mHandler.post(r);
		 */
		setSampleFamily();
		try {
			fixID();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void fixID() throws ParseException {
		// TODO Auto-generated method stub
		if (hasTemp.equals("1")) {
			// 編輯狀態下不可編輯
			edit_gmcfzh.setFocusable(false);
			edit_gmcfzh.setFocusableInTouchMode(false);
			res = IDCard.IDCardValidate(edit_gmcfzh.getText().toString());
			edit_gmcfzh.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					new SweetAlertDialog(activity, SweetAlertDialog.WARNING_TYPE).setTitleText("编辑状态下身份证不可变")
							.setConfirmText("我知道了").show();
				}
			});
		}
		edit_gmcfzh.addTextChangedListener(new TextWatcher() {

			CharSequence temp;// 监听前的文本
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
						// 新增狀態
						if (hasTemp.equals("0")) {
							Boolean hasFamily = false;
							for (Family tem : mgr.queryFamily()) {
								if (tem.getEdit_gmcfzh().equals(temp.toString())) {
									// 数据库已存在
									new SweetAlertDialog(activity, SweetAlertDialog.WARNING_TYPE)
											.setTitleText("已存在该家庭").setContentText(tem.getEdit_hzxm() + "\n"
													+ tem.edit_gmcfzh + "\n" + "登记日期：" + tem.edit_djrq)
											.setConfirmText("我知道了").show();
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
		case "居民身份证（户口簿）":
			i = 0;
			break;
		case "中国人民解放军军官证":
			i = 1;
			break;
		case "中国人民武装警察警官证":
			i = 2;
			break;
		case "香港特区护照/身份证明":
			i = 3;
			break;
		case "澳门特区护照/身份证明":
			i = 4;
			break;
		case "台湾居民来往大陆通行证":
			i = 5;
			break;
		case "外国人护照":
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

		/* 户主证件类型spiner */
		edit_jhzzjlx = (Spinner) findViewById(R.id.edit_jhzzjlx);
		ArrayList<String> data_list1 = new ArrayList<String>();
		data_list1.add("居民身份证（户口簿）");
		data_list1.add("中国人民解放军军官证");
		data_list1.add("中国人民武装警察警官证");
		data_list1.add("香港特区护照/身份证明");
		data_list1.add("澳门特区护照/身份证明");
		data_list1.add("台湾居民来往大陆通行证");
		data_list1.add("外国人护照");
		ArrayAdapter<String> arr_adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,
				data_list1);
		// 设置样式
		arr_adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		edit_jhzzjlx.setAdapter(arr_adapter1);

		edit_hzxm = (EditText) findViewById(R.id.edit_hzxm);
		edit_lxdh = (EditText) findViewById(R.id.edit_lxdh);
		edit_dzyx = (EditText) findViewById(R.id.edit_dzyx);
		edit_yzbm = (EditText) findViewById(R.id.edit_yzbm);
		edit_cjqtbxrs = (EditText) findViewById(R.id.edit_cjqtbxrs);
		edit_hkxxdz = (EditText) findViewById(R.id.edit_hkxxdz);
		edit_jtxxdz = (EditText) findViewById(R.id.edit_jtxxdz);

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

	// intent为A传来的带有Bundle的intent，当然也可以自己定义新的Bundle
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
		Toast.makeText(getApplicationContext(), "日期", Toast.LENGTH_LONG).show();
		new DatePickerDialog(InfoFamilyActivity.this, new DatePickerDialog.OnDateSetListener() {
			@Override
			public void onDateSet(DatePicker view, int year, int month, int day) {
				// TODO Auto-generated method stub
				// 更新EditText控件日期 小于10加0
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
		switch (requestCode) { // resultCode为回传的标记，我在B中回传的是RESULT_OK
		case CAMERA:
			if (data == null)
				return;
			else if (resultCode == Activity.RESULT_OK) {
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
			// 更新数据
			if (hasTemp.equals("1")) {
				// 编辑状态
				mgr.updateFamily(tempFamily);
				family.setEdit_jtbh(tempFamily.edit_jtbh);
			} else
				family.setEdit_jtbh(tempFamily.edit_gmcfzh);
			family.setEdit_gmcfzh(tempFamily.edit_gmcfzh);
			family.setEdit_hzxm(tempFamily.edit_hzxm);
			family.setEdit_jhzzjlx(tempFamily.edit_jhzzjlx);
			family.setEdit_cjqtbxrs(tempFamily.edit_cjqtbxrs);
			family.setEdit_lxdh(tempFamily.edit_lxdh);
			family.setEdit_djrq(tempFamily.edit_djrq);
			family.setEdit_hkxxdz(tempFamily.edit_hkxxdz);
			family.setXzqh(bundle.getString("XZQH"));
			familys.add(family);
			mgr.addFamily(familys);

			// do something
			// 通过OCR输出的家庭信息
			String str = gson.toJson(tempFamily);
			FamilyUtil.saveValue(getApplicationContext(), str);
			System.out.println("familyAct save" + gson.toJson(tempFamily));
			Intent intent = new Intent(InfoFamilyActivity.this, InfoMainActivity.class);
			intent.putExtra("Family", str);
			setResult(RESULT_OK, intent); // intent为A传来的带有Bundle的intent，当然也可以自己定义新的Bundle
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					new SweetAlertDialog(activity, SweetAlertDialog.SUCCESS_TYPE).setTitleText("保存成功").show();
				}
			});
		}
	};

	@OnClick(R.id.btn_save)
	public void toInfoMainActivity2() {
		Handler mHandler = new Handler();
		if (edit_hkxxdz.getText().toString().isEmpty())
			Toast.makeText(getApplicationContext(), "户口地址不能为空！", Toast.LENGTH_SHORT).show();
		else if (edit_hzxm.getText().toString().isEmpty()) {
			Toast.makeText(getApplicationContext(), "户主姓名不能为空！", Toast.LENGTH_SHORT).show();
		} else if (edit_gmcfzh.getText().toString().isEmpty())
			Toast.makeText(getApplicationContext(), "公民身份证号不能为空！", Toast.LENGTH_SHORT).show();
		// 判断证件类型是否是居民身份证（户口簿）
		else if (edit_jhzzjlx.getSelectedItem().equals("居民身份证（户口簿）")) {
			if (res != "")
				Toast.makeText(getApplicationContext(), "公民身份证号不正确", Toast.LENGTH_SHORT).show();
			else if (edit_gmcfzh.length() != 18) {
				Toast.makeText(getApplicationContext(), "公民身份证号不是18位！", Toast.LENGTH_SHORT).show();
			}
		} else {
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
	}

	@OnClick(R.id.btn_revert)
	public void revert() {
		edit_hzxm.setText("");
		edit_lxdh.setText("");
		edit_dzyx.setText("");
		edit_yzbm.setText("");
		edit_cjqtbxrs.setText("");
		edit_hkxxdz.setText("");
		// spinner
		edit_jhzzjlx.setSelection(0, true);
		edit_gmcfzh.setText("");
		edit_djrq.setText("");
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
