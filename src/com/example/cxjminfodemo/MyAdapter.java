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
 * @data 2016年8月24日 下午2:42:15
 */
public class MyAdapter extends BaseAdapter {

	ArrayList<HashMap<String, String>> listItem;
	
	/* 存放控件 的ViewHolder */
	public final class ViewHolder {
		public TextView gmsfzh;
		public TextView name;
		public TextView jf;
	}

	private LayoutInflater mInflater; // 得到一个LayoutInfalter对象用来导入布局

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
			/* 得到各个控件的对象 */
			holder.gmsfzh = (TextView) convertView.findViewById(R.id.text_gmsfzh);
			holder.name = (TextView) convertView.findViewById(R.id.text_name);

			convertView.setTag(holder); // 绑定ViewHolder对象
		} else {
			holder = (ViewHolder) convertView.getTag(); // 取出ViewHolder对象
		}

		/* 设置TextView显示的内容，即我们存放在动态数组中的数据 */
		holder.gmsfzh.setText(listItem.get(position).get("gmsfzh").toString());
		holder.name.setText(listItem.get(position).get("name").toString());
		return convertView;
	}
}
