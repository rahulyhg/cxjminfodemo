package com.neuqsoft.cxjmcj.base;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.neuqsoft.cxjmcj.R;
import com.lapism.searchview.SearchAdapter;
import com.lapism.searchview.SearchHistoryTable;
import com.lapism.searchview.SearchItem;
import com.lapism.searchview.SearchView;
import com.neuqsoft.cxjmcj.InfoActivity.InfoMainActivity;
import com.neuqsoft.cxjmcj.base.menu.FiltersActivity;
import com.neuqsoft.cxjmcj.base.menu.HistoryActivity;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseActivity extends AppCompatActivity {

	@SuppressWarnings("WeakerAccess")
	protected static final int NAV_ITEM_INVALID = -1;
	protected static final int NAV_ITEM_TOOLBAR = 0;
	protected static final int NAV_ITEM_MENU_ITEM = 1;
	protected static final int NAV_ITEM_HISTORY_TOGGLE = 2;
	protected static final int NAV_ITEM_FILTERS = 3;

	private static final String EXTRA_KEY_VERSION = "version";
	private static final String EXTRA_KEY_THEME = "theme";
	private static final String EXTRA_KEY_VERSION_MARGINS = "version_margins";
	private static final String EXTRA_KEY_TEXT = "text";

	public static InfoMainActivity _sonActivity;

	// TODO
	protected SearchView mSearchView = null;
	protected DrawerLayout mDrawerLayout = null;
	protected FloatingActionButton mFab = null;
	protected ActionBarDrawerToggle mActionBarDrawerToggle = null;
	private Toolbar mToolbar = null;

	private SearchHistoryTable mHistoryDatabase;

	// ---------------------------------------------------------------------------------------------
	protected int getNavItem() {
		return NAV_ITEM_INVALID;
	}

	@Override
	protected void onPostCreate(@Nullable Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);

		setFab();
		setDrawer();
		// setNavigationView();
		if (mActionBarDrawerToggle != null) {
			mActionBarDrawerToggle.syncState();
		}
	}

	@Override
	public void onBackPressed() {
		if (mDrawerLayout != null && mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
			mDrawerLayout.closeDrawer(GravityCompat.START);
		} else {
			super.onBackPressed(); // finish
		}
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		if (mActionBarDrawerToggle != null) {
			mActionBarDrawerToggle.onConfigurationChanged(newConfig);
		}
	}

	@SuppressWarnings("SimplifiableIfStatement")
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (mActionBarDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/*
	 * @Override public boolean onPrepareOptionsMenu(Menu menu) { //
	 * menu.setVisible(!mDrawerLayout.isDrawerOpen(GravityCompat.START)); return
	 * super.onPrepareOptionsMenu(menu); }
	 */

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == SearchView.SPEECH_REQUEST_CODE && resultCode == RESULT_OK) {
			List<String> results = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
			if (results != null && results.size() > 0) {
				String searchWrd = results.get(0);
				if (!TextUtils.isEmpty(searchWrd)) {
					if (mSearchView != null) {
						mSearchView.setQuery(searchWrd);
					}
				}
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	// ---------------------------------------------------------------------------------------------
	protected void setToolbar() {
		mToolbar = (Toolbar) findViewById(R.id.toolbar);
		if (mToolbar != null) {
			mToolbar.setNavigationContentDescription(getResources().getString(R.string.app_name));
			setSupportActionBar(mToolbar);
		}
	}

	protected void setViewPager() {
		FragmentAdapter adapter = new FragmentAdapter(getSupportFragmentManager());
		adapter.addFragment(new SearchFragment(), getString(R.string.installed));
		adapter.addFragment(new SearchFragment(), getString(R.string.all));
	}

	// ---------------------------------------------------------------------------------------------
	private void setFab() {
		if (mFab != null) {
			mFab.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					mHistoryDatabase.clearDatabase();
					Snackbar.make(v, "Search history deleted.", Snackbar.LENGTH_LONG).setAction("Action", null).show();
				}
			});
		}
	}

	private void setDrawer() {

		if (mDrawerLayout != null && mToolbar != null) {

			mActionBarDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.open,
					R.string.close) {
				@Override
				public void onDrawerOpened(View drawerView) {
					super.onDrawerOpened(drawerView);
					if (mSearchView != null && mSearchView.isSearchOpen()) {
						mSearchView.close(true);
					}
					if (mFab != null) {
						mFab.hide();
					}
				}

				@Override
				public void onDrawerClosed(View drawerView) {
					super.onDrawerClosed(drawerView);
					if (mFab != null) {
						mFab.show();
					}
				}
			};

			mActionBarDrawerToggle.syncState();
		}
	}

	protected void setSearchView() {
		mHistoryDatabase = new SearchHistoryTable(this);

		mSearchView = (SearchView) findViewById(R.id.searchView);
		if (mSearchView != null) {
			mSearchView.setHint(R.string.search);
			mSearchView.setVoiceText("Set permission on Android 6+ !");
			mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
				@Override
				public boolean onQueryTextSubmit(String query) {
					getData(query, 0);
					// mSearchView.close(false);
					return true;
				}

				@Override
				public boolean onQueryTextChange(String newText) {
					return false;
				}
			});
			mSearchView.setOnOpenCloseListener(new SearchView.OnOpenCloseListener() {
				@Override
				public void onOpen() {
					if (mFab != null) {
						mFab.hide();
					}
				}

				@Override
				public void onClose() {
					if (mFab != null) {
						mFab.show();
					}
				}
			});
			mSearchView.setOnVoiceClickListener(new SearchView.OnVoiceClickListener() {
				@Override
				public void onVoiceClick() {
				}
			});

			if (mSearchView.getAdapter() == null) {
				List<SearchItem> suggestionsList = new ArrayList<>();

				SearchAdapter searchAdapter = new SearchAdapter(this, suggestionsList, _sonActivity.XZQH);
				searchAdapter.addOnItemClickListener(new SearchAdapter.OnItemClickListener() {
					@SuppressWarnings("deprecation")
					@Override
					public void onItemClick(View view, int position) {
						TextView textView = (TextView) view.findViewById(R.id.textView_item_text);
						ImageView imageView = (ImageView) view.findViewById(R.id.imageView_item_icon_left);
						int isMember = -1;
						String query = textView.getText().toString();
						getData(query, position);
						mSearchView.setTextInput(query);
						String[] querys = query.split("\\t");
						mSearchView.close(false);
						if (imageView.getDrawable().getCurrent().getConstantState()
								.equals(getResources().getDrawable(R.drawable.yezhu).getConstantState())) {
							isMember = 0;
						}
						if (imageView.getDrawable().getCurrent().getConstantState()
								.equals(getResources().getDrawable(R.drawable.member).getConstantState())) {
							isMember = 1;
						}
						_sonActivity.UpdateListView(querys[0], isMember);
					}
				});
				mSearchView.setAdapter(searchAdapter);
			}
		}
	}

	protected void customSearchView() {
		Bundle extras = getIntent().getExtras();
		if (extras != null && mSearchView != null) {
			mSearchView.setVersion(extras.getInt(EXTRA_KEY_VERSION));
			mSearchView.setVersionMargins(extras.getInt(EXTRA_KEY_VERSION_MARGINS));
			mSearchView.setTheme(extras.getInt(EXTRA_KEY_THEME), true);
			mSearchView.setTextInput(extras.getString(EXTRA_KEY_TEXT));
		}
	}

	// ---------------------------------------------------------------------------------------------
	@CallSuper
	protected void getData(String text, int position) {
		mHistoryDatabase.addItem(new SearchItem(text));
		// Toast.makeText(getApplicationContext(), text + ", position: " +
		// position, Toast.LENGTH_SHORT).show();
	}
}