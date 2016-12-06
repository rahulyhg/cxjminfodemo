package com.megvii.idcardproject.base.menu;

import com.lapism.searchview.SearchAdapter;
import com.lapism.searchview.SearchView;
import com.megvii.idcardproject.R;
import com.megvii.idcardproject.base.BaseActivity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.GravityCompat;
import android.view.View;
import android.widget.Toast;


public class HistoryActivity extends BaseActivity {

    @Override
    protected int getNavItem() {
        return NAV_ITEM_HISTORY_TOGGLE;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setTheme(R.style.AppThemeLight);
        super.onCreate(savedInstanceState);
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

        Toast.makeText(HistoryActivity.this, "Fetching from entire database!", Toast.LENGTH_SHORT).show();
        if (mFab != null) {
            mFab.setOnClickListener(new View.OnClickListener() {
                int clickCount = 0;

                @Override
                public void onClick(View v) {
                    clickCount++;
                    if (clickCount == 4)
                        clickCount = 1;
                    SearchAdapter adapter = (SearchAdapter) mSearchView.getAdapter();
                    adapter.setDatabaseKey(clickCount);
                    Toast.makeText(HistoryActivity.this, "Fetching from version " + clickCount, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

}