/**
 *@filename MyAdapter.java
 *@Email tengzhenjiu@qq.com
 *
 */
package com.neuqsoft.cxjmcj.adapter;

import java.util.ArrayList;

import com.example.idcardscandemo.ACameraActivity;
import com.google.gson.Gson;
import com.neuqsoft.cxjmcj.R;
import com.neuqsoft.cxjmcj.InfoActivity.InfoFamilyActivity;
import com.neuqsoft.cxjmcj.InfoActivity.InfoMainActivity;
import com.neuqsoft.cxjmcj.InfoActivity.InfoPersonalActivity;
import com.neuqsoft.cxjmcj.R.id;
import com.neuqsoft.cxjmcj.R.layout;
import com.neuqsoft.cxjmcj.db.DBManager;
import com.neuqsoft.cxjmcj.dto.Family;
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
import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * @Title MyAdapter
 * @author tengzj
 * @data 2016��8��24�� ����2:42:15
 */
public class MyAdapterFamily extends SlideBaseAdapter {
	public static final int INFO��PERSONAL = 102;
	public static final int INFO_FAMILY = 101;
	ArrayList<Family> listItem;
	DBManager db;
	Context context;

	/* ��ſؼ� ��ViewHolder */
	public final class ViewHolder {
		public TextView gmsfzh;
		public TextView name;
		public TextView jf;
		public TextView yjf;
		private ImageView icon2;
		private ImageView icon;
		TextView edit;
		TextView delete;
	}

	private LayoutInflater mInflater; // �õ�һ��LayoutInfalter�����������벼��

	public MyAdapterFamily(Context context, ArrayList<Family> listItem) {
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
		final ViewHolder holder;
		Log.v("BaseAdapterTest", "getView " + position + " " + convertView);

		if (convertView == null) {
			// convertView = mInflater.inflate(R.layout.list_item, null);
			convertView = createConvertView(position);
			holder = new ViewHolder();
			holder.icon = (ImageView) convertView.findViewById(R.id.text_icon);
			holder.icon2 = (ImageView) convertView.findViewById(R.id.text_icon2);
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
		holder.name.setText(listItem.get(position).getEdit_hzxm());
		holder.jf.setVisibility(View.INVISIBLE);
		holder.yjf.setVisibility(View.INVISIBLE);
		holder.icon2.setVisibility(View.INVISIBLE);
		holder.icon.setVisibility(View.VISIBLE);
		if (holder.edit != null) {
			holder.edit.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(context, InfoFamilyActivity.class);
					intent.putExtra("gmsfzh", listItem.get(position).getEdit_gmcfzh());
					String str = new Gson().toJson(listItem.get(position));
					intent.putExtra("Family", str);
					// ��݋��B
					intent.putExtra("hasTemp", "1");
					intent.putExtra("XZQH", listItem.get(position).getXzqh());
					((InfoMainActivity) context).startActivityForResult(intent, INFO_FAMILY);
				}
			});
		}

		if (holder.delete != null) {
			holder.delete.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE).setTitleText("ɾ����ͥ�����Ա")
							.setContentText(
									holder.name.getText().toString() + "\n" + holder.gmsfzh.getText().toString())
							.setConfirmText("ɾ ��").showCancelButton(true).setCancelText("ȡ ��")
							.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
								@Override
								public void onClick(SweetAlertDialog sDialog) {
									db.deleteFamily(listItem.get(position));
									listItem.remove(position);
									notifyDataSetChanged();
									sDialog.dismissWithAnimation();
									((InfoMainActivity) context).listItemMember.clear();
									((InfoMainActivity) context).adapterMember.notifyDataSetChanged();
									((InfoMainActivity) context).line.setVisibility(View.GONE);
								}
							}).setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
								@Override
								public void onClick(SweetAlertDialog sDialog) {
									sDialog.cancel();
								}
							}).show();
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
