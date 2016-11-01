package com.example.cxjminfodemo.base.menu;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.view.GravityCompat;
import android.widget.Toast;

import com.example.cxjminfodemo.R;
import com.example.cxjminfodemo.base.BaseActivity;
import com.lapism.searchview.SearchFilter;
import com.lapism.searchview.SearchView;

import java.util.ArrayList;
import java.util.List;

public class FiltersActivity extends BaseActivity {

	@Override
	protected int getNavItem() {
		return NAV_ITEM_FILTERS;
	}

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		setTheme(R.style.AppThemeLight);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_toolbar);
		setTitle(null); // ""
		setToolbar();
		setViewPager();
	}

	@Override
	protected void onPostCreate(@Nullable Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		mActionBarDrawerToggle.setDrawerIndicatorEnabled(false);

		setSearchView();
		mSearchView.setNavigationIconArrowHamburger();
		mSearchView.setOnMenuClickListener(new SearchView.OnMenuClickListener() {
			@Override
			public void onMenuClick() {
				mDrawerLayout.openDrawer(GravityCompat.START); // finish();
			}
		});
		customSearchView();

		List<SearchFilter> filters = new ArrayList<>();
		filters.add(new SearchFilter("Filter1", true));
		filters.add(new SearchFilter("Filter2", false));
		filters.add(new SearchFilter("Filter3", true));

		mSearchView.setFilters(filters);
	}

	@Override
	protected void getData(String text, int position) {
		super.getData(text, position);

		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				String text = "Selected: ";
				List<Boolean> filtersState = mSearchView.getFiltersStates();
				int i = 0;
				for (Boolean filter : filtersState) {
					i++;
					if (filter)
						text += "Filter" + i;
				}
				if (text.equals("Selected: "))
					text += "nothing";
				Toast.makeText(FiltersActivity.this, text, Toast.LENGTH_SHORT).show();
			}
		}, 600);
	}

}