package com.example.cxjminfodemo;

import android.app.Activity;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.example.cxjminfodemo.InfoActivity.InfoMainActivity;
import com.dd.CircularProgressButton;
import com.example.cxjminfodemo.MyAdapter.ViewHolder;
import com.example.cxjminfodemo.server.dto.UserDetail;
import com.example.cxjminfodemo.utils.TextToMap;
import com.example.cxjminfodemo.utils.ToastUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.roamer.slidelistview.SlideBaseAdapter;
import com.roamer.slidelistview.SlideListView.SlideMode;

import android.app.ActionBar;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import android.os.Build;

public class MainActivity2 extends Activity {
	ListView listview;
	// image_sjsc

	MyAdapter adapter;
	private Map<String, String> oldMap;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main2);
		ButterKnife.bind(MainActivity2.this);
		/** ------向服务器请求数据-------------- */
		getDataFromNet();
		listview = (ListView) findViewById(R.id.listView);

		InputStream inputStream = getResources().openRawResource(R.raw.countrycode);
		oldMap = new TextToMap().TextToMap(inputStream);

		/*
		 * InputStream inputStream1 =
		 * getResources().openRawResource(R.raw.nation); Map<String, String>
		 * oldMap1 = new TextToMap().TextToMap(inputStream1);
		 */

	}

	/**
	 * ----从SP中取出token值--- ---请求服务器 --
	 */
	private void getDataFromNet() {

		HttpUtils httpUtils = new HttpUtils();
		SharedPreferences tokenSp = getSharedPreferences("Token", MODE_PRIVATE);
		String sToken = tokenSp.getString("token", "");
		System.out.println("-----------------------" + sToken);
		RequestParams params1 = new RequestParams();
		params1.addHeader("token", sToken);
		params1.addHeader("Content-Type", "application/json;charset=utf-8");
		params1.addHeader("Accept", "*/*");
		params1.addHeader("client_id", "1");

		httpUtils.send(HttpMethod.GET, RcConstant.usertasksPath, params1, new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException error, String msg) {
				Log.e("AAA-msg", msg);
				Log.e("AAA-error", error.getMessage());
				System.out.println("===msg:" + msg);

			}

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				String result = responseInfo.result;
				getInfoData(result);

			}
		});
	}

	/** 获取用户的详细信息并储存到list中--- */
	protected void getInfoData(String result) {
		Gson gson = new Gson();
		List<UserDetail> list = new ArrayList<UserDetail>();
		list = gson.fromJson(result, new TypeToken<List<UserDetail>>() {
		}.getType());
		adapter = new MyAdapter(list);
		listview.setAdapter(adapter);
		adapter.notifyDataSetChanged();
		ToastUtil.showShort(getApplicationContext(), "数据已经加载");
	}

	/**
	 * 
	 * LstView适配器
	 * 
	 */
	public class MyAdapter extends BaseAdapter {

		/* 存放控件 的ViewHolder */
		public final class ViewHolder {
			public TextView num1;
			public TextView num2;
			public TextView num3;
			public TextView local;
			CircularProgressButton upload;
			CircularProgressButton download;
		}

		private LayoutInflater mInflater; // 得到一个LayoutInfalter对象用来导入布局
		private List<UserDetail> list;

		public MyAdapter(List<UserDetail> list) {
			super();
			this.mInflater = LayoutInflater.from(getBaseContext());
			this.list = list;
		}

		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub

			final ViewHolder holder;
			Log.v("BaseAdapterTest", "getView " + position + " " + convertView);

			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.listview_item, null);
				holder = new ViewHolder();
				/* 得到各个控件的对象 */
				holder.num1 = (TextView) convertView.findViewById(R.id.num1);
				holder.num2 = (TextView) convertView.findViewById(R.id.num2);
				holder.num3 = (TextView) convertView.findViewById(R.id.num3);
				holder.local = (TextView) convertView.findViewById(R.id.local);
				holder.upload = (CircularProgressButton) convertView.findViewById(R.id.buttom_up);
				holder.download = (CircularProgressButton) convertView.findViewById(R.id.buttom_down);
				holder.download.setIndeterminateProgressMode(true);
				holder.upload.setIndeterminateProgressMode(true);
				convertView.setTag(holder); // 绑定ViewHolder对象
			} else {
				holder = (ViewHolder) convertView.getTag(); // 取出ViewHolder对象
			}
			
			/**把乡镇代码转换形成乡镇*/

			String cjarea = list.get(position).getCjarea();

			Set<String> set = oldMap.keySet();
			for (String str : set) {
				System.out.println("_____________sdd__________" + oldMap.get(set) + "");
			}

			list.get(position).getCjarea();
			holder.local.setText(list.get(position).getCjarea().toString());
			// holder.num1.setText(listItem.get(position).get(1).toString());
			// holder.num2.setText(listItem.get(position).get(2).toString());
			// holder.num3.setText(listItem.get(position).get(3).toString());

			holder.download.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if (holder.download.getProgress() == 0) {
						holder.download.setProgress(50);
					} else if (holder.download.getProgress() == 100) {
						// holder.download.setProgress(0);
						Intent intent = new Intent(MainActivity2.this, InfoMainActivity.class);
						startActivity(intent);
					} else {
						holder.download.setProgress(100);
					}

				}
			});

			holder.upload.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if (holder.upload.getProgress() == 0) {
						holder.upload.setProgress(50);
					} else if (holder.upload.getProgress() == 100) {
						holder.upload.setProgress(0);
					} else {
						holder.upload.setProgress(100);
					}
				}
			});
			/*
			 * holder.gmsfzh.setOnClickListener(new View.OnClickListener() {
			 * 
			 * @Override public void onClick(View v) {
			 * 
			 * } });
			 * 
			 * holder.name.setOnClickListener(new View.OnClickListener() {
			 * 
			 * @Override public void onClick(View v) { } });
			 */
			return convertView;
		}

	}

}
