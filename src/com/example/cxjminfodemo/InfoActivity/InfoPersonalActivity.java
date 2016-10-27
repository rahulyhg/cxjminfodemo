/**
 *@filename InfoPersonalActivity.java
 *@Email tengzhenjiu@qq.com
 *
 */
package com.example.cxjminfodemo.InfoActivity;

import java.io.File;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;

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
import com.example.cxjminfodemo.utils.IDCard;
import com.example.cxjminfodemo.utils.PersonalUtil;
import com.example.idcardscandemo.ACameraActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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
	private LinearLayout btn_xjzf;
	private LinearLayout btn_zxzf;
	private LinearLayout btn_xyg;
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
	Gson gson = new Gson();
	ArrayList<Personal> listPersonal = new ArrayList<Personal>();

	@Bind(R.id.edit_cbrq)
	TextView edit_cbrq;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.info_personal);
		ButterKnife.bind(InfoPersonalActivity.this);
		mgr = new DBManager(this);
		Intent intent = getIntent();
		bundle = intent.getExtras(); // 获取intent里面的bundle对象
		tempPersonal = new Personal();
		initView();

		fixID();
	}

	/********** DECLARES *************/

	/**
	* 
	*/
	private void fixID() {
		// TODO Auto-generated method stub
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

		// Spiner2
		edit_cbrylb = (Spinner) findViewById(R.id.edit_cbrylb);
		ArrayList<String> data_list2 = new ArrayList<String>();

		data_list2.add("普通城乡居民");
		data_list2.add("重残城乡居民");
		data_list2.add("低保城乡居民");
		data_list2.add("五保供养城乡居民");
		data_list2.add("低收入家庭60岁以上老年人");
		data_list2.add("五保供养大学生");
		data_list2.add("重度残疾大学生");
		data_list2.add("低保大学生");
		data_list2.add("普通大学生");
		data_list2.add("五保供养中小学生");
		data_list2.add("重度残疾中小学生");
		data_list2.add("低保中小学生");
		data_list2.add("普通中小学生");

		ArrayAdapter<String> arr_adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,
				data_list2);
		arr_adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		edit_cbrylb.setAdapter(arr_adapter2);

		// Spiner3
		edit_hkxz = (Spinner) findViewById(R.id.edit_hkxz);
		ArrayList<String> data_list3 = new ArrayList<String>();
		data_list3.add("农业户口（农村）");
		data_list3.add("非农业户口（城镇）");
		data_list3.add("本地非农业户口（本地城镇）");
		data_list3.add("外地非农业户口（外地城镇）");
		data_list3.add("本地农业户口（本地农村）");
		data_list3.add("外地农业户口（外地农村）");
		data_list3.add("港澳台");
		data_list3.add("外籍");
		ArrayAdapter<String> arr_adapter3 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,
				data_list3);
		arr_adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		edit_hkxz.setAdapter(arr_adapter3);
		edit_hkxz.setSelection(1);

		// Spiner4
		edit_mz = (Spinner) findViewById(R.id.edit_mz);
		ArrayList<String> data_list4 = new ArrayList<String>();
		data_list4.add("汉族");
		data_list4.add("满族");
		data_list4.add("回族");
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
		data_list6.add("居民身份证（户口簿）");
		data_list6.add("中国人民解放军军官证");
		data_list6.add("中国人民武装警察警官证");
		data_list6.add("香港特区护照/身份证明");
		data_list6.add("澳门特区护照/身份证明");
		data_list6.add("台湾居民来往大陆通行证");
		data_list6.add("外国人护照");
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

	@OnItemSelected(R.id.edit_yhzgx)
	void onItemSelected(int position) {
		switch (position) {
		// 家属
		case 0:
			break;
		// 户主
		case 1:

			edit_cbrxm.setText(bundle.getString("name"));
			edit_gmcfzh.setText(bundle.getString("cardno"));
			edit_xxjzdz.setText(bundle.getString("address"));
			/* edit_xb.setText(bundle.getString("sex")); */
			String temp_folk = bundle.getString("folk");
			if (temp_folk.equals("汉"))
				edit_mz.setSelection(0);
			if (temp_folk.equals("满"))
				edit_mz.setSelection(1);
			if (temp_folk.equals("回"))
				edit_mz.setSelection(2);
			edit_csrq.setText(bundle.getString("birthday"));
			break;
		default:
			break;
		}
	}

	@OnClick(R.id.image_left)
	public void toinfoMainActivity() {
		Toast.makeText(getApplicationContext(), "未保存的数据将不会显示", Toast.LENGTH_LONG).show();
		finish();
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
		Toast.makeText(getApplicationContext(), "完成现金支付", Toast.LENGTH_SHORT).show();
		tempPersonal.setEdit_jf("1");
	}

	@OnClick(R.id.btn_zxzf)
	public void zxzf() {
		Toast.makeText(getApplicationContext(), "完成在线支付", Toast.LENGTH_SHORT).show();
		tempPersonal.setEdit_jf("1");
		Intent intent = new Intent(this, InfoPersonalzxzfActivity.class);
		startActivity(intent);
	}

	@OnClick(R.id.btn_save)
	public void toInfoMainActivity2() {
		getDataFromEdit();
		Handler mHandler = new Handler();
		Runnable r = new Runnable() {
			public void run() {
				ArrayList<Personal> personals = new ArrayList<Personal>();

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
				personal1.setHZSFZ(bundle.getString("HZSFZ"));
				personals.add(personal1);
				mgr.addPersonal(personals);

				// do something
				String str = gson.toJson(listPersonal);
				PersonalUtil.saveValue(getApplicationContext(), str);
				Intent intent = new Intent(InfoPersonalActivity.this, InfoMainActivity.class);
				intent.putExtra("Personal", str);
				setResult(RESULT_OK, intent); // intent为A传来的带有Bundle的intent，当然也可以自己定义新的Bundle
				Toast.makeText(getApplicationContext(), "已保存", Toast.LENGTH_SHORT).show();
			}
		};

		if (edit_cbrxm.getText().toString().isEmpty())
			Toast.makeText(getApplicationContext(), "参保人姓名不能为空", Toast.LENGTH_SHORT).show();
		else if (edit_gmcfzh.getText().toString().isEmpty())
			Toast.makeText(getApplicationContext(), "公民身份证号不能为空", Toast.LENGTH_SHORT).show();
		else if (res != "")
			Toast.makeText(getApplicationContext(), "公民身份证号不正确", Toast.LENGTH_SHORT).show();
		else
			mHandler.post(r);
	}

	@OnClick(R.id.btn_xyg)
	public void toNextActivity() {
		edit_cbrxm.setText("");
		edit_gmcfzh.setText("");
		/* edit_xb.setText(""); */
		edit_csrq.setText("");
		edit_yhzgx.setSelection(0);
		edit_xxjzdz.setText("");
		tempPersonal = new Personal();
		Toast.makeText(getApplicationContext(), "已经跳转到下一个", Toast.LENGTH_LONG).show();
	}

	@OnClick(R.id.btn_camera)
	public void toOCR() {
		Intent intent = new Intent(InfoPersonalActivity.this, ACameraActivity.class);
		startActivityForResult(intent, CAMERA);
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) { // resultCode为回传的标记，我在B中回传的是RESULT_OK
		case CAMERA:
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
		listPersonal.add(tempPersonal);
	}
}
