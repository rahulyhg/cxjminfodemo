package com.example.cxjminfodemo;

import android.app.Activity;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.cxjminfodemo.InfoActivity.InfoMainActivity;
import com.dd.CircularProgressButton;
import com.example.cxjminfodemo.MyAdapter.ViewHolder;
import com.example.cxjminfodemo.utils.TextToMap;
import com.roamer.slidelistview.SlideBaseAdapter;
import com.roamer.slidelistview.SlideListView.SlideMode;

import android.app.ActionBar;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
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
	ArrayList<ArrayList<String>> listItem = new ArrayList<ArrayList<String>>();
	ArrayList<String> item = new ArrayList<String>();
	ArrayList<String> item2 = new ArrayList<String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main2);
		ButterKnife.bind(MainActivity2.this);

		listview = (ListView) findViewById(R.id.listView);
		item.add("哈哈村");
		item.add("10");
		item.add("100");
		item.add("50");

		listItem.add(item);

		item.clear();

		item.add("圣达菲村");
		item.add("100");
		item.add("1000");
		item.add("500");

		listItem.add(item);

		item.clear();

		item.add("圣达菲村");
		item.add("100");
		item.add("1000");
		item.add("500");

		listItem.add(item);

		item.clear();

		item.add("圣达菲村");
		item.add("100");
		item.add("1000");
		item.add("500");

		listItem.add(item);

		item.clear();

		item.add("圣达菲村");
		item.add("100");
		item.add("1000");
		item.add("500");

		listItem.add(item);

		item.clear();

		item.add("圣达菲村");
		item.add("100");
		item.add("1000");
		item.add("500");

		listItem.add(item);

		item.clear();

		item.add("圣达菲村");
		item.add("100");
		item.add("1000");
		item.add("500");

		listItem.add(item);

		adapter = new MyAdapter(this, listItem);
		listview.setAdapter(adapter);

		adapter.notifyDataSetChanged();

		/*
		 * InputStream inputStream =
		 * getResources().openRawResource(R.raw.countrycode); Map<String,
		 * String> oldMap = new TextToMap().TextToMap(inputStream);
		 * 
		 * InputStream inputStream1 =
		 * getResources().openRawResource(R.raw.nation); Map<String, String>
		 * oldMap1 = new TextToMap().TextToMap(inputStream1);
		 */
	}

	public class MyAdapter extends BaseAdapter {

		ArrayList<ArrayList<String>> listItem;

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

		public MyAdapter(Context context, ArrayList<ArrayList<String>> listItem) {
			super();
			this.mInflater = LayoutInflater.from(context);
			this.listItem = listItem;
		}

		@Override
		public int getCount() {
			return listItem.size();
		}

		@Override
		public Object getItem(int position) {
			return listItem.get(position);
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
			holder.local.setText(listItem.get(position).get(0).toString());
			holder.num1.setText(listItem.get(position).get(1).toString());
			holder.num2.setText(listItem.get(position).get(2).toString());
			holder.num3.setText(listItem.get(position).get(3).toString());

			holder.download.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if (holder.download.getProgress() == 0) {
						holder.download.setProgress(50);
					} else if (holder.download.getProgress() == 100) {
						//holder.download.setProgress(0);
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
