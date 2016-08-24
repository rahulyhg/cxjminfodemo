/**
 *@filename InfoPersonalActivity.java
 *@Email tengzhenjiu@qq.com
 *
 */
package com.example.cxjminfodemo.InfoActivity;

import java.text.ParseException;
import java.util.ArrayList;

import com.example.cxjminfodemo.MainActivity;
import com.example.cxjminfodemo.R;
import com.example.cxjminfodemo.dto.FamilyDTO;
import com.example.cxjminfodemo.dto.PersonalDTO;
import com.example.cxjminfodemo.utils.FamilyUtil;
import com.example.cxjminfodemo.utils.IDCard;
import com.google.gson.Gson;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @Title InfoPersonalActivity
 * @author tengzj
 * @data 2016年8月23日 下午5:25:18
 */
public class InfoPersonalActivity extends Activity {

	private EditText edit_jtbh;
	private EditText edit_cbrxm;
	private EditText edit_gmcfzh;
	private EditText edit_mz;
	private EditText edit_xb;
	private EditText edit_csrq;
	private EditText edit_cbrq;
	private EditText edit_cbrylb;
	private LinearLayout btn_save;
	private LinearLayout btn_xjzf;
	private LinearLayout btn_zxzf;
	private LinearLayout btn_xyg;

	Gson gson = new Gson();
	ArrayList<PersonalDTO> listPersonal = new ArrayList<PersonalDTO>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.info_personal);
		ButterKnife.bind(InfoPersonalActivity.this);
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
					String res = null;
					try {
						res = IDCard.IDCardValidate(edit_gmcfzh.getText().toString());
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					if (res == "") {
						edit_csrq.setText(IDCard.printDate());
						edit_xb.setText(IDCard.printSex());
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
		edit_mz = (EditText) findViewById(R.id.edit_mz);
		edit_xb = (EditText) findViewById(R.id.edit_xb);
		edit_csrq = (EditText) findViewById(R.id.edit_csrq);
		edit_cbrq = (EditText) findViewById(R.id.edit_cbrq);
		edit_cbrylb = (EditText) findViewById(R.id.edit_cbrylb);
	}
	/* Please visit http://www.ryangmattison.com for updates */

	@OnClick(R.id.image_left)
	public void toinfoMainActivity() {
		finish();
	}

	@OnClick(R.id.btn_save)
	public void toInfoMainActivity2() {
		getDataFromEdit();
		Handler mHandler = new Handler();
		Runnable r = new Runnable() {
			public void run() {
				// do something
				String str = gson.toJson(listPersonal);
				FamilyUtil.saveValue(getApplicationContext(), str);
				Intent intent = new Intent(InfoPersonalActivity.this, InfoMainActivity.class);
				intent.putExtra("Personal", str);
				setResult(RESULT_OK, intent); // intent为A传来的带有Bundle的intent，当然也可以自己定义新的Bundle
				finish();
			}
		};
		mHandler.post(r);
	}

	@OnClick(R.id.btn_xyg)
	public void toNextActivity() {
		getDataFromEdit();
		edit_cbrxm.setText("");
		edit_gmcfzh.setText("");
		edit_mz.setText("");
		edit_xb.setText("");
		edit_csrq.setText("");
		edit_cbrq.setText("");
		edit_cbrylb.setText("");
		Toast.makeText(getApplicationContext(), "已经跳转到下一个", Toast.LENGTH_LONG).show();
	}

	private void getDataFromEdit() {
		// TODO Auto-generated method stub
		PersonalDTO tempPersonal = new PersonalDTO();
		tempPersonal.setEdit_cbrxm(edit_cbrxm.getText().toString());
		tempPersonal.setEdit_mz(edit_mz.getText().toString());
		tempPersonal.setEdit_gmcfzh(edit_gmcfzh.getText().toString());
		tempPersonal.setEdit_xb(edit_xb.getText().toString());
		tempPersonal.setEdit_csrq(edit_csrq.getText().toString());
		tempPersonal.setEdit_cbrq(edit_cbrq.getText().toString());
		tempPersonal.setEdit_cbrylb(edit_cbrylb.getText().toString());
		listPersonal.add(tempPersonal);
	}
}
