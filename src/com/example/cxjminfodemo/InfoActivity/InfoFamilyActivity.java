/**
 *@filename InfoFamilyActivity.java
 *@Email tengzhenjiu@qq.com
 *
 */
package com.example.cxjminfodemo.InfoActivity;

import java.util.ArrayList;
import java.util.Calendar;

import com.example.cxjminfodemo.MainActivity;
import com.example.cxjminfodemo.R;
import com.example.cxjminfodemo.dto.Family;
import com.example.cxjminfodemo.utils.FamilyUtil;
import com.example.cxjminfodemo.utils.PersonalUtil;
import com.google.gson.Gson;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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
 * @data 2016年8月23日 下午5:24:46
 */
public class InfoFamilyActivity extends Activity {

	/********** DECLARES *************/

	private Spinner edit_jgszcwh;
	private EditText edit_hzxm;
	private EditText edit_hjbh;
	private EditText edit_lxdh;
	private EditText edit_dzyx;
	private EditText edit_yzbm;
	private EditText edit_cjqtbxrs;
	private EditText edit_hkxxdz;
	private Calendar calendar;

	private Family defaultFamily = new Family("11111", "XXX", "张三", "123456", "123456");
	Gson gson = new Gson();

	private Family tempFamily = new Family();

	@Bind(R.id.edit_djrq)
	TextView edit_djrq;

	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.info_family);
		ButterKnife.bind(InfoFamilyActivity.this);

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
		Bundle bundle = intent.getExtras(); // 获取intent里面的bundle对象
		String hasTemp = bundle.getString("hasTemp");
		if(hasTemp.equals("1"))
		{
			String str = bundle.getString("Family");
			Family tempFamily = gson.fromJson(str, Family.class);
			edit_hzxm.setText(tempFamily.getEdit_hzxm());
			edit_yzbm.setText(tempFamily.getEdit_yzbm());
			edit_hkxxdz.setText(tempFamily.getEdit_hkxxdz());
		}
	}

	/**
	 * 
	 */
	private void initView() {
		// TODO Auto-generated method stub
		
		//Spiner
		edit_jgszcwh = (Spinner) findViewById(R.id.edit_jgszcwh);
		ArrayList<String> data_list = new ArrayList<String>();
		data_list.add("八里庄村");
		data_list.add("古屯村");
		// 适配器
		ArrayAdapter<String> arr_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,
				data_list);
		// 设置样式
		arr_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// 加载适配器
		edit_jgszcwh.setAdapter(arr_adapter);

		edit_hzxm = (EditText) findViewById(R.id.edit_hzxm);
		edit_hjbh = (EditText) findViewById(R.id.edit_hjbh);
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
		Intent intent = new Intent(this, InfoMainActivity.class);
		startActivity(intent);
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

	@OnClick(R.id.btn_save)
	public void toInfoMainActivity2() {
		getDataFromEdit();
		Handler mHandler = new Handler();
		Runnable r = new Runnable() {
			public void run() {
				// do something
				String str = gson.toJson(tempFamily);
				FamilyUtil.saveValue(getApplicationContext(), str);
				System.out.println("familyAct save" + gson.toJson(tempFamily));

				Intent intent = new Intent(InfoFamilyActivity.this, InfoMainActivity.class);
				intent.putExtra("Family", str);
				setResult(RESULT_OK, intent); // intent为A传来的带有Bundle的intent，当然也可以自己定义新的Bundle
				finish();
			}
		};
		mHandler.post(r);
	}

	private void getDataFromEdit() {
		Intent intent = getIntent();
		tempFamily.setEdit_gmcfzh(intent.getExtras().getString("gmsfzh"));
		// tempFamily.setEdit_jgszcwh(edit_jgszcwh.getText().toString());
		tempFamily.setEdit_hzxm(edit_hzxm.getText().toString());
		tempFamily.setEdit_hjbh(edit_hjbh.getText().toString());
		tempFamily.setEdit_lxdh(edit_lxdh.getText().toString());
		tempFamily.setEdit_dzyx(edit_dzyx.getText().toString());
		tempFamily.setEdit_yzbm(edit_yzbm.getText().toString());
		tempFamily.setEdit_cjqtbxrs(edit_cjqtbxrs.getText().toString());
		tempFamily.setEdit_hkxxdz(edit_hkxxdz.getText().toString());
	}
}
