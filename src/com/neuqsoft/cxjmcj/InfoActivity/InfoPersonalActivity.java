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
 * @data 2016年8月23日 下午5:25:18
 */
public class InfoPersonalActivity extends Activity {

	private EditText edit_jtbh;
	private EditText edit_cbrxm;
	private EditText edit_gmcfzh;
	private EditText edit_lxdh;
	private TextView edit_csrq;
	private TextView edit_xxjzdz;
	private LinearLayout btn_save;

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
		bundle = intent.getExtras(); // 获取intent里面的bundle对象
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
						Boolean hasPersonal = false;
						// 新增狀態
						if (HZSFZedit.equals("")) {
							for (Personal tem : mgr.queryPersonal()) {
								if (tem.getEdit_gmcfzh().equals(temp.toString())) {
									// 数据库已存在
									new SweetAlertDialog(activity, SweetAlertDialog.WARNING_TYPE).setTitleText("已存在该人员")
											.setContentText(tem.edit_cbrxm + "\n" + tem.edit_gmcfzh + "\n" + "户主身份证号码："
													+ tem.HZSFZ + "\n" + "登记日期：" + tem.edit_cbrq)
											.setConfirmText("我知道了").show();
									hasPersonal = true;
									revert();
									break;
								}
							}
						}
						if (!hasPersonal) {
							edit_csrq.setText(IDCard.printDate());
							String xb = IDCard.printSex();
							if (xb.equals("男"))
								edit_xb.setSelection(0, true);
							if (xb.equals("女"))
								edit_xb.setSelection(1, true);
							if (xb.equals("未说明性别"))
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
		ArrayList<String> data_list = new ArrayList<String>();
		data_list.add("户主");
		data_list.add("夫妻");
		data_list.add("父母");
		data_list.add("子女");
		data_list.add("其他");
		// 适配器
		ArrayAdapter<String> arr_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,
				data_list);
		arr_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		edit_yhzgx.setAdapter(arr_adapter);
		edit_yhzgx.setSelection(1, true);

		// Spiner2
		edit_cbrylb = (Spinner) findViewById(R.id.edit_cbrylb);

		ArrayList<String> data_list2 = new ArrayList<String>();
		List<Code> jmlb = mgr.queryCode("BAC067");
		for (int i = 0; i < jmlb.size(); i++) {
			Code code = jmlb.get(i);
			String aaa103 = code.getAAA103();
			// 添加居民类别
			data_list2.add(aaa103);
		}

		ArrayAdapter<String> arr_adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,
				data_list2);
		arr_adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		edit_cbrylb.setAdapter(arr_adapter2);

		// Spiner3
		edit_hkxz = (Spinner) findViewById(R.id.edit_hkxz);
		ArrayList<String> data_list3 = new ArrayList<String>();
		List<Code> hkxz = mgr.queryCode("AAC009");
		for (int i = 0; i < hkxz.size(); i++) {
			Code code = hkxz.get(i);
			String aaa103 = code.getAAA103();
			// 添加户口性质
			data_list3.add(aaa103);
		}

		ArrayAdapter<String> arr_adapter3 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,
				data_list3);
		arr_adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		edit_hkxz.setAdapter(arr_adapter3);
		edit_hkxz.setSelection(1, true);

		// Spiner4
		edit_mz = (Spinner) findViewById(R.id.edit_mz);
		ArrayList<String> data_list4 = new ArrayList<String>();
		// 查询数据库
		List<Code> nation = mgr.queryCode("AAC005");
		for (int i = 0; i < nation.size(); i++) {
			Code code = nation.get(i);
			String aaa103 = code.getAAA103();
			// 添加民族
			data_list4.add(aaa103);
		}
		ArrayAdapter<String> arr_adapter4 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,
				data_list4);
		arr_adapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		edit_mz.setAdapter(arr_adapter4);
		// Spiner5
		edit_xb = (Spinner) findViewById(R.id.edit_xb);
		ArrayList<String> data_list5 = new ArrayList<String>();
		data_list5.add("男");
		data_list5.add("女");
		data_list5.add("未说明性别");
		ArrayAdapter<String> arr_adapter5 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,
				data_list5);
		arr_adapter5.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		edit_xb.setAdapter(arr_adapter5);
		// Spiner6
		edit_zjlx = (Spinner) findViewById(R.id.edit_zjlx);
		ArrayList<String> data_list6 = new ArrayList<String>();
		// 查询数据库
		List<Code> zjlx = mgr.queryCode("AAC058");
		for (int i = 0; i < zjlx.size(); i++) {
			Code code = zjlx.get(i);
			String aaa103 = code.getAAA103();
			// 添加民族
			data_list6.add(aaa103);
		}

		ArrayAdapter<String> arr_adapter6 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,
				data_list6);
		arr_adapter6.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		edit_zjlx.setAdapter(arr_adapter6);

		// 日期
		calendar = Calendar.getInstance();
		edit_cbrq.setText(new StringBuilder().append(calendar.get(Calendar.YEAR)).append("-")
				.append((calendar.get(Calendar.MONTH) + 1) < 10 ? 0 + (calendar.get(Calendar.MONTH) + 1)
						: (calendar.get(Calendar.MONTH) + 1))
				.append("-").append((calendar.get(Calendar.DAY_OF_MONTH) < 10) ? 0 + calendar.get(Calendar.DAY_OF_MONTH)
						: calendar.get(Calendar.DAY_OF_MONTH)));

	}
	/* Please visit http://www.ryangmattison.com for updates */

	private void whenEdit() {
		// 个人信息编辑状态下 更新表单数据
		// TODO Auto-generated method stub
		res = "";
		String per = bundle.getString("personal");
		if (per != null && per != "") {
			editPersonal = new Gson().fromJson(per, new TypeToken<Personal>() {
			}.getType());
			HZSFZedit = editPersonal.HZSFZ;
			setContent(editPersonal);
		}
	}

	private void setContent(Personal personal) {
		String yhzgx = personal.edit_yhzgx;
		if (yhzgx.equals("户主"))
			edit_yhzgx.setSelection(0, true);
		if (yhzgx.equals("夫妻"))
			edit_yhzgx.setSelection(1, true);
		if (yhzgx.equals("父母"))
			edit_yhzgx.setSelection(2, true);
		if (yhzgx.equals("子女"))
			edit_yhzgx.setSelection(3, true);
		if (yhzgx.equals("其他"))
			edit_yhzgx.setSelection(4, true);

		String zjlx = personal.edit_zjlx;
		if (zjlx.equals("居民身份证（户口簿）"))
			edit_zjlx.setSelection(0, true);
		if (zjlx.equals("中国人民解放军军官证"))
			edit_zjlx.setSelection(1, true);
		if (zjlx.equals("中国人民武装警察警官证"))
			edit_zjlx.setSelection(2, true);
		if (zjlx.equals("香港特区护照/身份证明"))
			edit_zjlx.setSelection(3, true);
		if (zjlx.equals("澳门特区护照/身份证明"))
			edit_zjlx.setSelection(4, true);
		if (zjlx.equals("台湾居民来往大陆通行证"))
			edit_zjlx.setSelection(5, true);
		if (zjlx.equals("外国人护照"))
			edit_zjlx.setSelection(6, true);

		String xb = personal.edit_xb;
		if (xb.equals("男"))
			edit_xb.setSelection(0, true);
		if (xb.equals("女"))
			edit_xb.setSelection(1, true);
		if (xb.equals("未说明性别"))
			edit_xb.setSelection(2, true);

		edit_cbrxm.setText(personal.getEdit_cbrxm());
		edit_gmcfzh.setText(personal.getEdit_gmcfzh());
		edit_xxjzdz.setText(personal.getEdit_xxjzdz());
		edit_lxdh.setText(personal.getEdit_lxdh());
		String temp_folk = personal.getEdit_mz();
		if (temp_folk.equals("汉"))
			edit_mz.setSelection(0, true);
		if (temp_folk.equals("满"))
			edit_mz.setSelection(1, true);
		if (temp_folk.equals("回"))
			edit_mz.setSelection(2, true);
		edit_csrq.setText(personal.edit_csrq);

		String hkxz = personal.getEdit_hkxz();
		if (hkxz.equals("农业户口（农地区）"))
			edit_hkxz.setSelection(0, true);
		if (hkxz.equals("非农业户口（城镇）"))
			edit_hkxz.setSelection(1, true);
		if (hkxz.equals("本地非农业户口（本地城镇）"))
			edit_hkxz.setSelection(2, true);
		if (hkxz.equals("外地非农业户口（外地城镇）"))
			edit_hkxz.setSelection(3, true);
		if (hkxz.equals("本地农业户口（本地农地区）"))
			edit_hkxz.setSelection(4, true);
		if (hkxz.equals("外地农业户口（外地农地区）"))
			edit_hkxz.setSelection(5, true);
		if (hkxz.equals("港澳台"))
			edit_hkxz.setSelection(6, true);
		if (hkxz.equals("外籍"))
			edit_hkxz.setSelection(7, true);

		String cbrylb = personal.getEdit_cbrylb();
		if (cbrylb.equals("普通城乡居民"))
			edit_cbrylb.setSelection(0, true);
		if (cbrylb.equals("重残城乡居民"))
			edit_cbrylb.setSelection(1, true);
		if (cbrylb.equals("低保城乡居民"))
			edit_cbrylb.setSelection(2, true);
		if (cbrylb.equals("五保供养城乡居民"))
			edit_cbrylb.setSelection(3, true);
		if (cbrylb.equals("低收入家庭60岁以上老年人"))
			edit_cbrylb.setSelection(4, true);
		if (cbrylb.equals("五保供养大学生"))
			edit_cbrylb.setSelection(5, true);
		if (cbrylb.equals("重度残疾大学生"))
			edit_cbrylb.setSelection(6, true);
		if (cbrylb.equals("低保大学生"))
			edit_cbrylb.setSelection(7, true);

		if (cbrylb.equals("普通大学生"))
			edit_cbrylb.setSelection(8, true);
		if (cbrylb.equals("五保供养中小学生"))
			edit_cbrylb.setSelection(9, true);
		if (cbrylb.equals("重度残疾中小学生"))
			edit_cbrylb.setSelection(10, true);

		if (cbrylb.equals("低保中小学生"))
			edit_cbrylb.setSelection(11, true);
		if (cbrylb.equals("普通中小学生"))
			edit_cbrylb.setSelection(12, true);

		if (personal.getEdit_jf().equals("1")) {
			btn_xjzf.setText("取消支付");
			img_xjzf.setImageResource(R.drawable.yjf);
		} else {
			btn_xjzf.setText("现金支付");
			img_xjzf.setImageResource(R.drawable.djf);
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
					edit_lxdh.setText(family.getEdit_lxdh());
					/* edit_xb.setText(bundle.getString("sex")); */
				}
			}
			break;
		default:
			/*
			 * edit_cbrxm.setText(""); edit_gmcfzh.setText("");
			 * edit_xb.setText(""); edit_csrq.setText("");
			 * edit_xxjzdz.setText(""); tv_xjzf.setText("现金支付");
			 * tv_xjzf.setTextColor(Color.BLACK);
			 */
			break;
		}
	}

	@OnClick(R.id.image_left)
	public void toinfoMainActivity() {
		finish();
	}

	@OnClick(R.id.edit_csrq)
	public void toDateDialog2() {
		new DatePickerDialog(InfoPersonalActivity.this, new DatePickerDialog.OnDateSetListener() {
			@Override
			public void onDateSet(DatePicker view, int year, int month, int day) {
				// TODO Auto-generated method stub
				// 更新EditText控件日期 小于10加0
				edit_csrq.setText(new StringBuilder().append(year).append("-")
						.append((month + 1) < 10 ? 0 + (month + 1) : (month + 1)).append("-")
						.append((day < 10) ? 0 + day : day));
			}
		}, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();

	}

	@OnClick(R.id.edit_cbrq)
	public void toDateDialog() {
		new DatePickerDialog(InfoPersonalActivity.this, new DatePickerDialog.OnDateSetListener() {
			@Override
			public void onDateSet(DatePicker view, int year, int month, int day) {
				// TODO Auto-generated method stub
				// 更新EditText控件日期 小于10加0
				edit_cbrq.setText(new StringBuilder().append(year).append("-")
						.append((month + 1) < 10 ? 0 + (month + 1) : (month + 1)).append("-")
						.append((day < 10) ? 0 + day : day));
			}
		}, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
	}

	@OnClick(R.id.btn_xjzf)
	public void xjzf() {
		tip_xjzf = true;
		if (btn_xjzf.getText().toString().equals("现金支付")) {
			tempPersonal.setEdit_jf("1");
			save();
			img_xjzf.setImageResource(R.drawable.yjf);
			btn_xjzf.setText("取消支付");
		} else {
			tempPersonal.setEdit_jf("0");
			save();
			img_xjzf.setImageResource(R.drawable.djf);
			btn_xjzf.setText("现金支付");
		}

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
			personal1.setIsEdit(tempPersonal.isEdit);
			personal1.setEdit_xxjzdz(tempPersonal.edit_xxjzdz);
			personal1.setEdit_lxdh(tempPersonal.edit_lxdh);
			personal1.setEdit_jf(tempPersonal.edit_jf);

			if (bundle.getString("HZSFZ") != null && bundle.getString("HZSFZ") != "") {
				// 新增状态
				personal1.setHZSFZ(bundle.getString("HZSFZ"));
				personals.add(personal1);
				mgr.addPersonal(personals);
			} else {
				// 编辑状态
				personal1.setHZSFZ(tempPersonal.HZSFZ);
				personals.add(personal1);
				mgr.addPersonal(personals);
			}
		}
	};

	@OnClick(R.id.btn_save)
	public void save() {
		tempPersonal.setIsEdit("1");
		Handler mHandler = new Handler();
		if (edit_cbrxm.getText().toString().isEmpty())
			Toast.makeText(getApplicationContext(), "参保人姓名不能为空", Toast.LENGTH_SHORT).show();
		else if (edit_gmcfzh.getText().toString().isEmpty())
			Toast.makeText(getApplicationContext(), "证件号码不能为空", Toast.LENGTH_SHORT).show();
		//判断证件类型是否是居民身份证（户口簿）
		 else if (edit_zjlx.getSelectedItem().equals("居民身份证（户口簿）")) {
			if (res != "")
				Toast.makeText(getApplicationContext(), "公民身份证号不正确", Toast.LENGTH_SHORT).show();
			else if (edit_gmcfzh.length() != 18) {
				Toast.makeText(getApplicationContext(), "公民身份证号不是18位！", Toast.LENGTH_SHORT).show();
			}
		} else {
			final SweetAlertDialog dialog = new SweetAlertDialog(activity, SweetAlertDialog.SUCCESS_TYPE)
					.setTitleText("保存成功");
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

					if (HZSFZedit != "") {
						// 编辑状态
					} else if (!tip_xjzf)
						// 新增状态
						revert();
				}
			}).start();
		}

	}

	@OnClick(R.id.btn_xyg)
	public void revert() {
		this.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				if (HZSFZedit != "") {
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
					edit_hkxz.setSelection(1, true);
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
		switch (requestCode) { // resultCode为回传的标记，我在B中回传的是RESULT_OK
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

				if (folk.equals("汉"))
					edit_mz.setSelection(0);
				if (folk.equals("满"))
					edit_mz.setSelection(1);
				if (folk.equals("回"))
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
		tempPersonal.setHZSFZ(HZSFZedit);
		tempPersonal.setEdit_xxjzdz(edit_xxjzdz.getText().toString());
		tempPersonal.setEdit_lxdh(edit_lxdh.getText().toString());
		tempPersonal.setEdit_jf(btn_xjzf.getText().toString().equals("取消支付") ? "1" : "0");
		listPersonal.add(tempPersonal);
	}
}
