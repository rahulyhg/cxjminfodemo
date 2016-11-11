/**
 *@filename MyAdapter.java
 *@Email tengzhenjiu@qq.com
 *
 */
package com.neuqsoft.cxjmcj;

import java.util.ArrayList;

import com.google.gson.Gson;
import com.neuqsoft.cxjmcj.InfoActivity.InfoMainActivity;
import com.neuqsoft.cxjmcj.InfoActivity.InfoPersonalActivity;
import com.neuqsoft.cxjmcj.db.DBManager;
import com.neuqsoft.cxjmcj.dto.Personal;
import com.roamer.slidelistview.SlideBaseAdapter;
import com.roamer.slidelistview.SlideListView.SlideMode;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @Title MyAdapter
 * @author tengzj
 * @data 2016��8��24�� ����2:42:15
 */
public class MyAdapter extends SlideBaseAdapter {
	public static final int INFO��PERSONAL = 102;
	ArrayList<Personal> listItem;
	DBManager db;
	Context context;

	/* ��ſؼ� ��ViewHolder */
	public final class ViewHolder {
		public TextView gmsfzh;
		public TextView name;
		public TextView jf;
		public TextView yjf;
		private ImageView icon;
		private ImageView icon2;
		TextView edit;
		TextView delete;
	}

	private LayoutInflater mInflater; // �õ�һ��LayoutInfalter�����������벼��
	

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
			holder.icon2 = (ImageView) convertView.findViewById(R.id.text_icon2);
			holder.icon = (ImageView) convertView.findViewById(R.id.text_icon);
			holder.gmsfzh = (TextView) convertView.findViewById(R.id.text_gmsfzh);
			holder.name = (TextView) convertView.findViewById(R.id.text_name);
			holder.jf = (TextView) convertView.findViewById(R.id.text_jf);
			holder.yjf = (TextView) convertView.findViewById(R.id.text_yjf);
			holder.edit = (TextView) convertView.findViewById(R.id.edit);
			holder.delete = (TextView) convertView.findViewById(R.id.delete);
			convertView.setTag(holder); // ��ViewHolder����
		} else {
			holder = (ViewHolder) convertView.getTag(); // ȡ��ViewHolder����
		}

		/* ����TextView��ʾ�����ݣ������Ǵ���ڶ�̬�����е����� */
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
		if (listItem.get(position).getEdit_gmcfzh().equals(listItem.get(position).getHZSFZ())) {
			holder.icon.setVisibility(View.VISIBLE);
			holder.icon2.setVisibility(View.INVISIBLE);
		}else {
			holder.icon.setVisibility(View.INVISIBLE);
			holder.icon2.setVisibility(View.VISIBLE);
		}

		if (holder.edit != null) {
			holder.edit.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(context, InfoPersonalActivity.class);
					intent.putExtra("personal", new Gson().toJson(listItem.get(position)));
					((InfoMainActivity) context).startActivityForResult(intent, INFO��PERSONAL);
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
