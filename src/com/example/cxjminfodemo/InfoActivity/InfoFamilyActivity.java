/**
 *@filename InfoFamilyActivity.java
 *@Email tengzhenjiu@qq.com
 *
 */
package com.example.cxjminfodemo.InfoActivity;

import com.example.cxjminfodemo.MainActivity;
import com.example.cxjminfodemo.R;
import com.example.cxjminfodemo.dto.FamilyDTO;
import com.example.cxjminfodemo.utils.FamilyUtil;
import com.google.gson.Gson;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.EditText;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @Title InfoFamilyActivity
 * @author tengzj
 * @data 2016年8月23日 下午5:24:46
 */
public class InfoFamilyActivity extends Activity {

	/********** DECLARES *************/

	private EditText edit_jgszcwh;
	private EditText edit_hzxm;
	private EditText edit_hjbh;
	private EditText edit_lxdh;
	private EditText edit_dzyx;
	private EditText edit_yzbm;
	private EditText edit_cjqtbxrs;
	private EditText edit_hkxxdz;

	private FamilyDTO defaultFamily = new FamilyDTO("11111", "XXX", "张三", "123456", "123456");
	Gson gson = new Gson();

	private FamilyDTO tempFamily = new FamilyDTO();
	
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.info_family);
		ButterKnife.bind(this);
		initView();

		/*Handler mHandler = new Handler();
		Runnable r = new Runnable() {
			public void run() {
				// do something
				FamilyUtil.saveValue(getApplicationContext(), gson.toJson(defaultFamily));
				System.out.println("first" + gson.toJson(defaultFamily));
			}
		};
		mHandler.post(r);*/

	}

	/**
	 * 
	 */
	private void initView() {
		// TODO Auto-generated method stub
		edit_jgszcwh = (EditText) findViewById(R.id.edit_jgszcwh);
		edit_hzxm = (EditText) findViewById(R.id.edit_hzxm);
		edit_hjbh = (EditText) findViewById(R.id.edit_hjbh);
		edit_lxdh = (EditText) findViewById(R.id.edit_lxdh);
		edit_dzyx = (EditText) findViewById(R.id.edit_dzyx);
		edit_yzbm = (EditText) findViewById(R.id.edit_yzbm);
		edit_cjqtbxrs = (EditText) findViewById(R.id.edit_cjqtbxrs);
		edit_hkxxdz = (EditText) findViewById(R.id.edit_hkxxdz);
	}

	@OnClick(R.id.image_left)
	public void toInfoMainActivity() {
		Intent intent = new Intent(this, InfoMainActivity.class);
		startActivity(intent);
	}

	@OnClick(R.id.btn_save)
	public void toInfoMainActivity2() {
		getDataFromEdit();
		Handler mHandler = new Handler();
		Runnable r = new Runnable() {
			public void run() {
				// do something
				FamilyUtil.saveValue(getApplicationContext(), gson.toJson(tempFamily));
				System.out.println("familyAct save" + gson.toJson(tempFamily));
			}
		};
		mHandler.post(r);
		Intent intent = new Intent(this, InfoMainActivity.class);
		startActivity(intent);
	}
	
	private void getDataFromEdit()
	{
		Intent intent=getIntent();
		tempFamily.setEdit_gmcfzh(intent.getExtras().getString("gmsfzh"));
		tempFamily.setEdit_jgszcwh(edit_jgszcwh.getText().toString());
		tempFamily.setEdit_hjbh(edit_hjbh.getText().toString());
		tempFamily.setEdit_lxdh(edit_lxdh.getText().toString());
		tempFamily.setEdit_dzyx(edit_dzyx.getText().toString());
		tempFamily.setEdit_yzbm(edit_yzbm.getText().toString());
		tempFamily.setEdit_cjqtbxrs(edit_cjqtbxrs.getText().toString());
		tempFamily.setEdit_hkxxdz(edit_hkxxdz.getText().toString());
	}
}
