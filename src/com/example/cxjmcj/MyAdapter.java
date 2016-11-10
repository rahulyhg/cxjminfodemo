/**
 *@filename MyAdapter.java
 *@Email tengzhenjiu@qq.com
 *
 */
package com.example.cxjmcj;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.cxjmcj.InfoActivity.InfoMainActivity;
import com.example.cxjmcj.InfoActivity.InfoPersonalActivity;
import com.example.cxjmcj.db.DBManager;
import com.example.cxjmcj.dto.Personal;
import com.google.gson.Gson;
import com.roamer.slidelistview.SlideBaseAdapter;
import com.roamer.slidelistview.SlideListView.SlideMode;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @Title MyAdapter
 * @author tengzj
 * @data 2016年8月24日 下午2:42:15
 */
public class MyAdapter extends SlideBaseAdapter {
	public static final int INFO＿PERSONAL = 102;
	ArrayList<Personal> listItem;
	DBManager db;
	Context context;

	/* 存放控件 的ViewHolder */
	public final class ViewHolder {
		public TextView gmsfzh;
		public TextView name;
		public TextView jf;
		public TextView yjf;
		TextView edit;
		TextView delete;
	}

	private LayoutInflater mInflater; // 得到一个LayoutInfalter对象用来导入布局

	public MyAdapter(Context context, ArrayList<Personal> listItem) {
		super(context);
		this.context = context;
		this.mInflater = LayoutInflater.from(context);
		this.listItem = listItem;
		this.db = new DBManager(context);
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
	public SlideMode getSlideModeInPosition(int position) {
		return super.getSlideModeInPosition(position);
	}

	@Override
	public int getItemViewType(int position) {
		if (position % 2 == 0) {
			return 0;
		}
		return 1;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		Log.v("BaseAdapterTest", "getView " + position + " " + convertView);

		if (convertView == null) {
			// convertView = mInflater.inflate(R.layout.list_item, null);
			convertView = createConvertView(position);
			holder = new ViewHolder();
			/* 得到各个控件的对象 */
			holder.gmsfzh = (TextView) convertView.findViewById(R.id.text_gmsfzh);
			holder.name = (TextView) convertView.findViewById(R.id.text_name);
			holder.jf = (TextView) convertView.findViewById(R.id.text_jf);
			holder.yjf = (TextView) convertView.findViewById(R.id.text_yjf);
			holder.edit = (TextView) convertView.findViewById(R.id.edit);
			holder.delete = (TextView) convertView.findViewById(R.id.delete);
			convertView.setTag(holder); // 绑定ViewHolder对象
		} else {
			holder = (ViewHolder) convertView.getTag(); // 取出ViewHolder对象
		}

		/* 设置TextView显示的内容，即我们存放在动态数组中的数据 */
		holder.gmsfzh.setText(listItem.get(position).getEdit_gmcfzh());
		holder.name.setText(listItem.get(position).getEdit_cbrxm());

		if (listItem.get(position).getEdit_jf().equals("1")) {
			holder.jf.setVisibility(View.INVISIBLE);
			holder.yjf.setVisibility(View.VISIBLE);
		}
		else
		{
			holder.jf.setVisibility(View.VISIBLE);
			holder.yjf.setVisibility(View.INVISIBLE);
		}

		if (holder.edit != null) {
			holder.edit.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(context, InfoPersonalActivity.class);
					intent.putExtra("personal", new Gson().toJson(listItem.get(position)));
					((InfoMainActivity) context).startActivityForResult(intent, INFO＿PERSONAL);
				}
			});
		}

		if (holder.delete != null) {
			holder.delete.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {				
					db.deletePersonal(listItem.get(position));
					listItem.remove(position);
					notifyDataSetChanged();
					Toast.makeText(mContext, "Click delete:" + position, Toast.LENGTH_SHORT).show();
				}
			});
		}
		return convertView;
	}

	@Override
	public int getFrontViewId(int position) {
		return R.layout.row_front_view;
	}

	@Override
	public int getRightBackViewId(int position) {
		return R.layout.row_right_back_view;
	}

	@Override
	public int getLeftBackViewId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}
}
