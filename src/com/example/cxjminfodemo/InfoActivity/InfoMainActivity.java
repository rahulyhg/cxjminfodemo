/**
 *@filename InfoMainActivity.java
 *@Email tengzhenjiu@qq.com
 *
 */
package com.example.cxjminfodemo.InfoActivity;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.example.cxjminfodemo.MainActivity;
import com.example.cxjminfodemo.MyAdapter;
import com.example.cxjminfodemo.R;
import com.example.cxjminfodemo.dto.FamilyDTO;
import com.example.cxjminfodemo.dto.PersonalDTO;
import com.example.cxjminfodemo.utils.FamilyUtil;
import com.example.cxjminfodemo.utils.IDCard;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @Title InfoMainActivity
 * @author tengzj
 * @data 2016年8月23日 下午5:22:33
 */
public class InfoMainActivity extends Activity {

	/********** DECLARES *************/
	/*
	 * private ImageView image_left; private EditText edit_num; private
	 * ScrollView ScrollView; private com.example.cxjminfodemo.NoScrollListView
	 * listView1;
	 * 
	 * image_left = (ImageView) findViewById(R.id.image_left); edit_num =
	 * (EditText) findViewById(R.id.edit_num); ScrollView = (ScrollView)
	 * findViewById(R.id.ScrollView); listView1 =
	 * (com.example.cxjminfodemo.NoScrollListView) findViewById(R.id.listView1);
	 * Please visit http://www.ryangmattison.com for updates
	 */
	public static final int INFO_FAMILY = 101;
	public static final int INFO＿PERSONAL = 102;

	@Bind(R.id.image_left)
	ImageView image_left;

	@Bind(R.id.edit_num)
	EditText edit_num;

	@Bind(R.id.text_name)
	TextView text_name;

	@Bind(R.id.text_id)
	TextView text_id;

	String tempFamily;
	FamilyDTO family = new FamilyDTO();// 传回的数据
	static ArrayList<FamilyDTO> listFamily = new ArrayList<FamilyDTO>();

	// 存户主与成员的映射
	static HashMap<String, ArrayList<PersonalDTO>> list_family_personal = new HashMap<String, ArrayList<PersonalDTO>>();
	static CharSequence temp;// 监听前的文本
	String res = null;// 查询身份证是否有效的返回信息
	static ArrayList<HashMap<String, String>> listItem = new ArrayList<HashMap<String, String>>();
	ListView lv;
	MyAdapter adapter;
	Gson gson = new Gson();

	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.info_main);
		ButterKnife.bind(InfoMainActivity.this);
		initView();

		// /*为ListView设置Adapter来绑定数据*/
		adapter = new MyAdapter(this, listItem);

		lv.setAdapter(adapter);
		adapter.notifyDataSetChanged();

		/* 为动态数组添加数据 */

		Handler mHandler = new Handler();
		Runnable r = new Runnable() {
			public void run() {
				// do something
				tempFamily = FamilyUtil.getValue(getApplicationContext());
				System.out.println("getFamily" + tempFamily);
			}
		};
		mHandler.post(r);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}

	/**
	 * 
	 */
	private void initView() {
		// TODO Auto-generated method stub

		lv = (ListView) findViewById(R.id.listView);// 得到ListView对象的引用

		edit_num.setText(temp);
		// 家庭登记的edit监听事件
		edit_num.addTextChangedListener(new TextWatcher() {

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

				editStart = edit_num.getSelectionStart();
				editEnd = edit_num.getSelectionEnd();
				if (temp.length() > charMaxNum) {
					s.delete(editStart - 1, editEnd);
					int tempSelection = editStart;
					edit_num.setText(s);
					edit_num.setSelection(tempSelection);
					edit_num.setCursorVisible(false);
					edit_num.setFocusableInTouchMode(false);
					edit_num.clearFocus();
				}
			}
		});

	}

	@OnClick(R.id.image_left)
	public void toMainActivity() {
		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
	}

	@OnClick(R.id.btn_add)
	public void toInfoPersonalActivity() {
		if (text_id.getText().toString().equals("")) {
			Toast.makeText(getApplicationContext(), "请先添加户主信息", Toast.LENGTH_LONG).show();
		} else {
			Intent intent = new Intent(this, InfoPersonalActivity.class);
			intent.putExtra("gmsfzh", text_id.getText().toString());
			intent.putExtra("hzxm", text_name.getText().toString());
			startActivityForResult(intent, INFO＿PERSONAL);
		}

	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) { // resultCode为回传的标记，我在B中回传的是RESULT_OK
		case INFO＿PERSONAL:
			// 回退按钮没有data
			if (data != null) {
				Bundle p = data.getExtras(); // data为B中回传的Intent
				String str = p.getString("Personal");// str即为回传的值
				System.out.println("Personal" + str);
				ArrayList<PersonalDTO> listPersonal = gson.fromJson(str, new TypeToken<ArrayList<PersonalDTO>>() {
				}.getType());
				// 用于映射
				list_family_personal.put(text_id.getText().toString(), listPersonal);

				// 用于显示listview
				for (PersonalDTO tempPersonal : listPersonal) {
					HashMap<String, String> map = new HashMap<String, String>();
					map.put("name", tempPersonal.getEdit_cbrxm());
					map.put("gmsfzh", tempPersonal.getEdit_gmcfzh());
					listItem.add(map);
				}
				adapter.notifyDataSetChanged();
			}
			break;

		case INFO_FAMILY:
			Bundle f = data.getExtras(); // data为B中回传的Intent
			String str2 = f.getString("Family");// str即为回传的值
			FamilyDTO tempFamily = gson.fromJson(str2, FamilyDTO.class);
			listFamily.add(tempFamily);

			text_name.setText(tempFamily.getEdit_hzxm());
			text_id.setText(tempFamily.getEdit_gmcfzh());

			break;
		default:
			break;
		}
	}

	@OnClick(R.id.btn_search)
	public void toInfoFamilyActivity() {
		try {
			res = IDCard.IDCardValidate(edit_num.getText().toString());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Handler mHandler2 = new Handler();
		Runnable r2 = new Runnable() {
			public void run() {
				if (res == "") {
					// 匹配身份证信息 并输出到户主信息栏
					listFamily.add(family);
					System.out.println(listFamily.toString());

					for (FamilyDTO tempFamily : listFamily) {
						if (tempFamily.getEdit_gmcfzh().equals(temp.toString())) {
							Toast.makeText(getApplicationContext(), "有匹配的身份证信息", Toast.LENGTH_LONG).show();
							// 显示的信息 与 编辑框信息对比
							if (!text_id.getText().toString().equals(temp.toString())) {
								listItem.clear();
								adapter.notifyDataSetChanged();
							}

							text_name.setText(tempFamily.getEdit_hzxm());
							text_id.setText(tempFamily.getEdit_gmcfzh());
						}
					}
					// 为空就是未匹配到信息
					System.out.println("text_id:" + text_id.getText().toString());
					if (text_id.getText().toString().equals("")
							|| text_id.getText().toString().equals(temp.toString()) != true) {
						Toast.makeText(getApplicationContext(), "无匹配的身份证信息", Toast.LENGTH_LONG).show();
						Intent intent = new Intent(InfoMainActivity.this, InfoFamilyActivity.class);
						intent.putExtra("gmsfzh", temp.toString());
						startActivityForResult(intent, INFO_FAMILY);
					}
				} else
					Toast.makeText(getApplicationContext(), res, Toast.LENGTH_LONG).show();

			}
		};
		mHandler2.post(r2);
	}

}
