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
    private Toolbar toolbar;
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

        // Creating The Toolbar and setting it as the Toolbar for the activity
        //UNCOMMENT TO INCLUDE TOOLBAR
        //toolbar = (Toolbar) findViewById(R.id.tool_bar);
        //setSupportActionBar(toolbar);

        // Creating The ViewPagerAdapter and Passing Ftoolbar.xmlragment Manager, Titles fot the Tabs and Number Of Tabs.
        adapter =  new ViewPagerAdapter(getSupportFragmentManager(),titles,numTabs);

        // Assigning ViewPager View and setting the adapter
        pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(adapter);

        // Assiging the Sliding Tab Layout View
        tabs = (SlidingTabLayout) findViewById(R.id.tabs);
        tabs.setDistributeEvenly(true); // To make the Tabs Fixed set this true, This makes the tabs Space Evenly in Available width

        // Setting Custom Color for the Scroll bar indicator of the Tab View
        tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.TabsScrollColor);
            }
        });

        // Setting the ViewPager For the SlidingTabsLayout
        tabs.setViewPager(pager);
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
