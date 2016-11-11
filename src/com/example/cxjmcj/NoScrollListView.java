/**
 *@filename NoScrollListView.java
 *@Email tengzhenjiu@qq.com
 *
 */
package com.example.cxjmcj;

import com.andexert.expandablelayout.library.ExpandableLayoutListView;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * 目的是为了显示listview无右侧的滑动条
 * 
 * @Title NoScrollListView
 * @author tengzj
 * @data 2016年8月23日 下午2:24:09
 */
public class NoScrollListView extends ExpandableLayoutListView {

	public NoScrollListView(Context context) {
		super(context);
	}

	public NoScrollListView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public NoScrollListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);
	}
}
