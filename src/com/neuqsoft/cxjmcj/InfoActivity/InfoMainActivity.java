/**
 *@filename InfoMainActivity.java
 *@Email tengzhenjiu@qq.com
 *
 */
package com.neuqsoft.cxjmcj.InfoActivity;

import java.io.InputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.neuqsoft.cxjmcj.R;
import com.neuqsoft.cxjmcj.adapter.MyAdapterMember;
import com.neuqsoft.cxjmcj.adapter.MyAdapterFamily;
import com.dou361.dialogui.DialogUIUtils;
import com.dou361.dialogui.config.BuildBean;
import com.example.idcardscandemo.ACameraActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lapism.searchview.SearchView;
import com.neuqsoft.cxjmcj.WelcomeActivity;
import com.neuqsoft.cxjmcj.base.BaseActivity;
import com.neuqsoft.cxjmcj.db.DBManager;
import com.neuqsoft.cxjmcj.dto.Family;
import com.neuqsoft.cxjmcj.dto.Personal;
import com.neuqsoft.cxjmcj.dto.Xzqh;
import com.neuqsoft.cxjmcj.utils.FamilyUtil;
import com.neuqsoft.cxjmcj.utils.IDCard;
import com.roamer.slidelistview.SlideListView;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * @Title InfoMainActivity
 * @author tengzj
 * @data 2016��8��23�� ����5:22:33
 */
public class InfoMainActivity extends BaseActivity {

	/********** DECLARES *************/
	Activity activity;
	BuildBean build;
	private static final String tag = "InfoMainActivity";

	public static final int INFO_FAMILY = 101;
	public static final int INFO��PERSONAL = 102;
	public static final int CAMERA = 1001;
	private static byte[] bytes;
	private static String extension;
	public static final String action = "idcard.scan";

	@Bind(R.id.image_left)
	ImageView image_left;

	String tempFamily;
	Family family = new Family();// ���ص�����
	static ArrayList<Family> listFamily = new ArrayList<Family>();

	// �滧�����Ա��ӳ��
	static HashMap<String, ArrayList<Personal>> list_family_personal = new HashMap<String, ArrayList<Personal>>();
	String res = null;// ��ѯ���֤�Ƿ���Ч�ķ�����Ϣ
	public static ArrayList<Personal> listItemMember = new ArrayList<Personal>();
	static ArrayList<Family> listItemFamily = new ArrayList<Family>();
	private SlideListView lvMember;
	private SlideListView lvFamily;
	public static MyAdapterMember adapterMember;
	public static MyAdapterFamily adapterFamily;
	Gson gson = new Gson();
	Map<String, String> oldMap;

	private String name = "";

	private String cardno = "";

	private String sex = "";

	private String folk = "";

	private String birthday = "";

	private String address = "";

	public DBManager mgr;


	@Bind(R.id.line)
	public LinearLayout line;

	String XZQH;

	@Bind(R.id.title_num)
	TextView title_num;

	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.info_main);
		activity = this;
		Intent intent = getIntent();
		XZQH = intent.getStringExtra("XZQH");
		getIntent().removeExtra("XZQH");
		((BaseActivity) (getParent()))._sonActivity = InfoMainActivity.this;
		this._sonActivity = InfoMainActivity.this;
		ButterKnife.bind(InfoMainActivity.this);
		mgr = new DBManager(this);

		initView();
		// /*ΪListView����Adapter��������*/
		// lvΪ�����б�
		listItemMember.clear();
		listItemFamily.clear();
		adapterMember = new MyAdapterMember(this, listItemMember);
		adapterFamily = new MyAdapterFamily(this, listItemFamily);
		lvMember.setAdapter(adapterMember);
		lvFamily.setAdapter(adapterFamily);
		adapterMember.notifyDataSetChanged();
		adapterFamily.notifyDataSetChanged();
		/* Ϊ��̬����������� */
		setView();
	}

	private void setView() {
		setSearchView();
		mSearchView.setHint("���֤�����ѯ");
		mSearchView.setTextSize(15);
		mSearchView.setNavigationIconArrowHamburger();
		mSearchView.setOnMenuClickListener(new SearchView.OnMenuClickListener() {
			@Override
			public void onMenuClick() {
				Intent intent = new Intent(InfoMainActivity.this, ACameraActivity.class);
				startActivityForResult(intent, CAMERA);
			}
		});
		customSearchView();
		mSearchView.close(true);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		if (listItemFamily.size() != 0) {
			UpdateListView(listItemFamily.get(0).getEdit_gmcfzh());
		}
	}

	/**
	 * 
	 */
	private void initView() {
		// TODO Auto-generated method stub
		lvMember = (SlideListView) findViewById(R.id.listView);// �õ�ListView���������
		lvFamily = (SlideListView) findViewById(R.id.listView2);// �õ�ListView���������
		// ����R�e�����^��
		Xzqh xzqh = mgr.queryXzqh(XZQH);
		if (xzqh.getName() != null && xzqh.getName() != "")
			title_num.setText("("+xzqh.getName()+")");
		else
			title_num.setText("ĳ����");
	}

	@OnClick(R.id.image_left)
	public void toMainActivity() {
		finish();
	}

	@OnClick(R.id.btn_add2)
	public void toInfoPersonalActivity() {
		if (listItemFamily.size() == 0) {
			Toast.makeText(getApplicationContext(), "������ӻ�����Ϣ", Toast.LENGTH_LONG).show();
		} else {
			Intent intent = new Intent(this, InfoPersonalActivity.class);
			intent.putExtra("HZSFZ", listItemFamily.get(0).getEdit_jtbh());
			startActivityForResult(intent, INFO��PERSONAL);
		}

	}

	@OnClick(R.id.btn_add)
	public void toInfoFamilyActivity() {
		try {
			res = IDCard.IDCardValidate(mSearchView.getTextInput());
			Intent intent = new Intent(InfoMainActivity.this, InfoFamilyActivity.class);
			// ����״̬ �Ǳ༭
			intent.putExtra("hasTemp", "0");
			intent.putExtra("gmsfzh", mSearchView.getTextInput());
			intent.putExtra("XZQH", XZQH);
			startActivityForResult(intent, INFO_FAMILY);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) { // resultCodeΪ�ش��ı�ǣ�����B�лش�����RESULT_OK
		case INFO��PERSONAL:
			UpdateListView(listItemFamily.get(0).getEdit_gmcfzh());
			break;

		case INFO_FAMILY:
			if (resultCode == Activity.RESULT_OK) {
				Bundle f = data.getExtras(); // dataΪB�лش���Intent
				String str2 = f.getString("Family");// str��Ϊ�ش���ֵ
				Family tempFamily = gson.fromJson(str2, Family.class);
				// ˢ��listview
				UpdateListView(tempFamily.getEdit_gmcfzh());
			}
			break;
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
							name = item.elementTextTrim("name");
							cardno = item.elementTextTrim("cardno");
							sex = item.elementTextTrim("sex");
							folk = item.elementTextTrim("folk");
							birthday = item.elementTextTrim("birthday");
							address = item.elementTextTrim("address");
						}
					}
				} catch (DocumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				mSearchView.setTextInput(cardno);
				// ƥ�����֤��Ϣ �������������Ϣ��
				listFamily = mgr.queryFamily();
				// System.out.println(listFamily.toString());
				Boolean hasTemp = false;
				for (Family tempFamily : listFamily) {
					if (tempFamily.getEdit_gmcfzh().equals(mSearchView.getTextInput())) {
						UpdateListView(tempFamily.getEdit_gmcfzh());
						hasTemp = true;
					}
				}
				if (!hasTemp) {
					// ���պ󲻴����û���Ϣ
					UpdateListView(mSearchView.getTextInput());
				}
			}
			break;
		default:
			break;
		}
	}

	public void UpdateListView(String temp) {
		build = DialogUIUtils.showLoadingHorizontal(activity, "������...");
		build.show();
		listItemMember.clear();
		listItemFamily.clear();
		ArrayList<Personal> listPersonal = mgr.queryPersonal(temp);
		listItemMember.addAll(listPersonal);
		// ���¼�ͥ��Ϣ�α�����������
		listFamily = mgr.queryFamily();
		for (Family tempFamily : listFamily) {
			if (tempFamily.getEdit_gmcfzh().equals(temp)) {
				Family thefamily = new Family();
				thefamily = tempFamily;
				mgr.updateFamily(thefamily);
				thefamily.setEdit_cjqtbxrs(listPersonal.size() + "");
				List<Family> the = new ArrayList<Family>();
				the.add(thefamily);
				mgr.addFamily(the);
				listItemFamily.add(thefamily);
			}
		}

		adapterFamily.notifyDataSetChanged();
		adapterMember.notifyDataSetChanged();
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				// ��¼�ȴ�4S
				try {
					Thread.sleep(800);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				build.dialog.dismiss();
			}
		}).start();

		// �жϸü�ͥ�Ƿ����ڸ���������
		if (listItemFamily.size() != 0) {
			if (!listItemFamily.get(0).xzqh.equals(XZQH)) {
				//��õ�ǰ������
				String country = "";
				Xzqh xzqh = mgr.queryXzqh(listItemFamily.get(0).getXzqh());
				if (xzqh.getName() != null && xzqh.getName() != "")
					country = xzqh.getName();
				new SweetAlertDialog(activity, SweetAlertDialog.WARNING_TYPE).setTitleText("�˼�ͥ�����ڸ�����")
						.setContentText(listItemFamily.get(0).getEdit_hzxm() + "\n" + listItemFamily.get(0).edit_gmcfzh
								+ "\n" + "����������" + country + "\n" + "�Ǽ����ڣ�" + listItemFamily.get(0).edit_djrq)
						.setConfirmText("��֪����").show();
				listItemMember.clear();
				listItemFamily.clear();
				adapterFamily.notifyDataSetChanged();
				adapterMember.notifyDataSetChanged();
			}
		}
		// ͷ���ĺ���
		if (listItemMember.size() != 0)
			line.setVisibility(View.VISIBLE);
		else
			line.setVisibility(View.GONE);
	}
}
