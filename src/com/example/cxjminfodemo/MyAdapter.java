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

import com.roamer.slidelistview.SlideBaseAdapter;
import com.roamer.slidelistview.SlideListView.SlideMode;

import android.content.Context;
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
 * @data 2016��8��24�� ����2:42:15
 */
public class MyAdapter extends SlideBaseAdapter {

	ArrayList<HashMap<String, String>> listItem;

	/* ��ſؼ� ��ViewHolder */
	public final class ViewHolder {
		public TextView gmsfzh;
		public TextView name;
		public TextView jf;
		Button edit;
		Button delete;
	}

	private LayoutInflater mInflater; // �õ�һ��LayoutInfalter�����������벼��

	public MyAdapter(Context context, ArrayList<HashMap<String, String>> listItem) {
		super(context);
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
	public SlideMode getSlideModeInPosition(int position) {
		if (position == 1) {
			return SlideMode.LEFT;
		}
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
			/* �õ������ؼ��Ķ��� */
			holder.gmsfzh = (TextView) convertView.findViewById(R.id.text_gmsfzh);
			holder.name = (TextView) convertView.findViewById(R.id.text_name);
			holder.jf = (TextView) convertView.findViewById(R.id.text_jf);
			convertView.setTag(holder); // ��ViewHolder����
		} else {
			holder = (ViewHolder) convertView.getTag(); // ȡ��ViewHolder����
		}

		/* ����TextView��ʾ�����ݣ������Ǵ���ڶ�̬�����е����� */
		holder.gmsfzh.setText(listItem.get(position).get("gmsfzh").toString());
		holder.name.setText(listItem.get(position).get("name").toString());
		holder.gmsfzh.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Toast.makeText(mContext, "Click title:" + position, Toast.LENGTH_SHORT).show();
			}
		});

		holder.name.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Toast.makeText(mContext, "Click title:" + position, Toast.LENGTH_SHORT).show();
			}
		});

		if (listItem.get(position).get("jf").toString().equals("1")) {
			holder.jf.setText("�ѽɷ�");
			holder.jf.setTextColor(Color.BLACK);
		}

		if (holder.edit != null) {
			holder.edit.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Toast.makeText(mContext, "Click edit:" + position, Toast.LENGTH_SHORT).show();
				}
			});
		}

		if (holder.delete != null) {
			holder.delete.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
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
