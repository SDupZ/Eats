package com.sdp.apps.eats.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;

import com.sdp.apps.eats.MyDeals;
import com.sdp.apps.eats.R;
import com.sdp.apps.eats.ZoomOutPageTransformer;
import com.sdp.apps.eats.fragments.DetailFragment;


public class DetailActivity extends FragmentActivity {
    private int numDeals;
    private int priceFilter;
    private ViewPager   mPager;
    private PagerAdapter mPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        priceFilter = getIntent().getIntExtra("price_filter", 1);
        if (priceFilter == 0)
            numDeals = MyDeals.getDeals().getChangeRangeDeals().size();
        else
            numDeals = MyDeals.getDeals().getMealRangeDeals().size();

        // Instantiate a ViewPager and a PagerAdapter.
        mPager = (ViewPager) findViewById(R.id.pager);
        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager(), priceFilter);
        mPager.setAdapter(mPagerAdapter);

        mPager.setPageTransformer(true, new ZoomOutPageTransformer());

        Intent intent = getIntent();
        int position = intent.getIntExtra("deal_position", -1);
        mPager.setCurrentItem(position);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    /**
     * A simple pager adapter that represents 5 ScreenSlidePageFragment objects, in
     * sequence.
     */
    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        int pricefilter;
        public ScreenSlidePagerAdapter(FragmentManager fm, int pricefilter) {
            super(fm);
            this.pricefilter = pricefilter;
        }

        @Override
        public Fragment getItem(int position) {
            return DetailFragment.create(position, pricefilter);
        }

        @Override
        public int getCount() {
            return numDeals;
        }


    }

}
