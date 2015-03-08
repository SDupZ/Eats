package com.sdp.apps.eats.activities;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.widget.Toast;

import com.sdp.apps.eats.R;
import com.sdp.apps.eats.data.ContentDownloader;
import com.sdp.apps.eats.data.DatabaseListener;
import com.sdp.apps.eats.fragments.DealsListFragment;

/**
 * Created by Simon on 21/01/2015.
 */
public class DealListActivity extends FragmentActivity implements ActionBar.TabListener,
        DatabaseListener{
    ViewPager mPager;
    DealsListFragment childFrag1;
    DealsListFragment childFrag2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deal_list);

        childFrag1 = new DealsListFragment();
        childFrag1.setPriceFilter(0);
        childFrag2 = new DealsListFragment();
        childFrag2.setPriceFilter(1);

        PagerAdapter adapter = new FragmentStatePagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                switch (position){
                    case 0:
                        return childFrag1;
                    case 1:
                        return childFrag2;
                }
                return null;
            }

            @Override
            public int getCount() {
                return 2;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                switch (position) {
                    case 0:
                        return getString(R.string.change_range_title);
                    case 1:
                        return getString(R.string.meal_range_title);
                }
                return null;
            }
        };

        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setAdapter(adapter);
        mPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener(){
            @Override
            public void onPageSelected(int position){
                getActionBar().setSelectedNavigationItem(position);
            }
        });
        mPager.setPageMargin(getResources().getDimensionPixelSize(R.dimen.page_margin));

        getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        for (int position = 0; position < adapter.getCount(); position++) {
            getActionBar().addTab(getActionBar().newTab()
                    .setText(adapter.getPageTitle(position))
                    .setTabListener(this));
        }

        getActionBar().setDisplayShowHomeEnabled(false);
        getActionBar().setDisplayShowTitleEnabled(false);
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        mPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    public void databaseUpdated(boolean success){
        if (!success){
            Toast.makeText(
                    this,
                    getResources().getString(R.string.no_internet_toast),
                    Toast.LENGTH_SHORT
            ).show();
        }
        if(childFrag1 != null) {
            childFrag1.databaseUpdated(success);
        }
        if (childFrag2 != null) {
            childFrag2.databaseUpdated(success);
        }
    }

    public void updateDatabase(){
        ContentDownloader cd = new ContentDownloader(this);
        cd.addDatabaseListener(this);
        cd.updateDatabase();
    }
}
