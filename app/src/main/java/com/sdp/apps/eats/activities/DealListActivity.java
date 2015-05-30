package com.sdp.apps.eats.activities;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.sdp.apps.eats.R;
import com.sdp.apps.eats.data.ContentDownloader;
import com.sdp.apps.eats.data.DatabaseListener;
import com.sdp.apps.eats.tabs.SlidingTabLayout;
import com.sdp.apps.eats.tabs.ViewPagerAdapter;
import com.sdp.apps.eats.util.SystemBarTintManager;

public class DealListActivity extends ActionBarActivity implements DatabaseListener{
    ViewPager pager;
    ViewPagerAdapter adapter;
    SlidingTabLayout tabs;
    CharSequence titles[]={"Uni Eats", "Dollar Deals"};
    int numTabs = 1;
    boolean updating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deal_list);

        SystemBarTintManager mStatusBarManager;
        mStatusBarManager = new SystemBarTintManager(this);
        mStatusBarManager.setStatusBarTintEnabled(true);
        mStatusBarManager.setTintColor(getResources().getColor(R.color.ColorPrimaryDark));
        updating = false;

        adapter =  new ViewPagerAdapter(getSupportFragmentManager(),titles,numTabs);
        pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(adapter);

        tabs = (SlidingTabLayout) findViewById(R.id.tabs);
        tabs.setDistributeEvenly(true);

        tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.TabsScrollColor);
            }
        });

        tabs.setViewPager(pager);

        // Set the padding to match the Status Bar height
        tabs.setPadding(0, getStatusBarHeight(), 0, 0);
     }

    // A method to find height of the status bar
    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }


    public void databaseUpdated(boolean success){
        if (!success){
            Toast.makeText(
                    this,
                    getResources().getString(R.string.no_internet_toast),
                    Toast.LENGTH_SHORT
            ).show();
        }
        adapter.databaseUpdated(success);
        updating = false;
    }

    public synchronized void updateDatabase(){
        if (!updating) {
            updating = true;
            ContentDownloader cd = new ContentDownloader(this);
            cd.addDatabaseListener(this);
            cd.updateDatabase();
        }
    }
}
