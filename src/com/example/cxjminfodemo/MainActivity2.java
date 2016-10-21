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
	public static final int CBDJ = 101;
	MyAdapter adapter;
	ArrayList<ArrayList<String>> listItem = new ArrayList<ArrayList<String>>();
	ArrayList<String> item = new ArrayList<String>();
	ArrayList<String> item2 = new ArrayList<String>();
	static int pos;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main2);
		ButterKnife.bind(MainActivity2.this);

		listview = (ListView) findViewById(R.id.listView);
		item.add("������");
		item.add("10");
		item.add("100");
		item.add("50");

		listItem.add(item);

		item.clear();

		item.add("ʥ��ƴ�");
		item.add("100");
		item.add("1000");
		item.add("500");

		listItem.add(item);

		item.clear();

		item.add("ʥ��ƴ�");
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

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) { // resultCodeΪ�ش��ı�ǣ�����B�лش�����RESULT_OK
		case CBDJ:
			adapter.getViewHolder(pos).upload.setVisibility(View.VISIBLE);
			;
		}
	}

	public class MyAdapter extends BaseAdapter {

		ArrayList<ArrayList<String>> listItem;

		/* ��ſؼ� ��ViewHolder */
		public final class ViewHolder {
			public TextView num1;
			public TextView num2;
			public TextView num3;
			public TextView local;
			CircularProgressButton upload;
			CircularProgressButton download;
			private ImageView download2;
			private ImageView upload2;
		}

		private LayoutInflater mInflater; // �õ�һ��LayoutInfalter�����������벼��

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

		public ViewHolder getViewHolder(int position) {
			ViewHolder holder;
			int firstVisible = listview.getFirstVisiblePosition();
			int index = position + listview.getHeaderViewsCount();
			View temp = listview.getChildAt(index - firstVisible);
			holder = (ViewHolder) temp.getTag();
			return holder;

		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub

			final ViewHolder holder;
			Log.v("BaseAdapterTest", "getView " + position + " " + convertView);

			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.listview_item, null);
				holder = new ViewHolder();
				/* �õ������ؼ��Ķ��� */
				holder.num1 = (TextView) convertView.findViewById(R.id.num1);
				holder.num2 = (TextView) convertView.findViewById(R.id.num2);
				holder.num3 = (TextView) convertView.findViewById(R.id.num3);
				holder.local = (TextView) convertView.findViewById(R.id.local);

				holder.upload = (CircularProgressButton) convertView.findViewById(R.id.buttom_up);
				holder.download = (CircularProgressButton) convertView.findViewById(R.id.buttom_down);
				holder.download2 = (ImageView) convertView.findViewById(R.id.buttom_down2);
				holder.upload2 = (ImageView) convertView.findViewById(R.id.buttom_up2);
				holder.download.setIndeterminateProgressMode(true);
				holder.upload.setIndeterminateProgressMode(true);
				convertView.setTag(holder); // ��ViewHolder����
			} else {
				holder = (ViewHolder) convertView.getTag(); // ȡ��ViewHolder����
			}
			holder.local.setText(listItem.get(position).get(0).toString());
			holder.num1.setText(listItem.get(position).get(1).toString());
			holder.num2.setText(listItem.get(position).get(2).toString());
			holder.num3.setText(listItem.get(position).get(3).toString());

			holder.upload2.setVisibility(View.INVISIBLE);
			holder.upload.setVisibility(View.INVISIBLE);

			holder.download.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if (holder.download.getProgress() == 0) {
						holder.download.setProgress(50);
						holder.download2.setVisibility(View.INVISIBLE);
					} else if (holder.download.getProgress() == 100) {
						// holder.download.setProgress(0);
						Intent intent = new Intent(MainActivity2.this, InfoMainActivity.class);
						startActivityForResult(intent, CBDJ);
						pos = position;
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
						holder.upload2.setVisibility(View.VISIBLE);
						holder.download.setVisibility(View.INVISIBLE);
						holder.upload.setVisibility(View.INVISIBLE);
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

		public void updateView(View view, int itemIndex) {
			if (view == null) {
				return;
			}
			// ��view��ȡ��holder
			ViewHolder holder = (ViewHolder) view.getTag();
			holder.upload.setVisibility(View.VISIBLE);
		}

	}

}
