/**
 *@filename MyAdapter.java
 *@Email tengzhenjiu@qq.com
 *
 */
package com.example.cxjminfodemo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * @Title MyAdapter
 * @author tengzj
 * @data 2016��8��24�� ����2:42:15
 */
public class MyAdapter extends BaseAdapter {

	ArrayList<HashMap<String, String>> listItem;
	
	/* ��ſؼ� ��ViewHolder */
	public final class ViewHolder {
		public TextView gmsfzh;
		public TextView name;
		public TextView jf;
	}

	private LayoutInflater mInflater; // �õ�һ��LayoutInfalter�����������벼��

	public MyAdapter(Context context,ArrayList<HashMap<String, String>> listItem) {
		this.mInflater = LayoutInflater.from(context);
		this.listItem=listItem;
	}

	@Override
	public int getCount() {
		return listItem.size();
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		Log.v("BaseAdapterTest", "getView " + position + " " + convertView);

		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.list_item, null);
			holder = new ViewHolder();
			/* �õ������ؼ��Ķ��� */
			holder.gmsfzh = (TextView) convertView.findViewById(R.id.text_gmsfzh);
			holder.name = (TextView) convertView.findViewById(R.id.text_name);

			convertView.setTag(holder); // ��ViewHolder����
		} else {
			holder = (ViewHolder) convertView.getTag(); // ȡ��ViewHolder����
		}

		/* ����TextView��ʾ�����ݣ������Ǵ���ڶ�̬�����е����� */
		holder.gmsfzh.setText(listItem.get(position).get("gmsfzh").toString());
		holder.name.setText(listItem.get(position).get("name").toString());
		return convertView;
	}
}
