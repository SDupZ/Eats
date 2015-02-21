package com.sdp.apps.eats.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.sdp.apps.eats.Deal;
import com.sdp.apps.eats.MyDeals;
import com.sdp.apps.eats.R;


public class DetailActivity extends ActionBarActivity {

    private int numDeals;
    private ViewPager   mPager;
    private PagerAdapter mPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_activity_slide);

        numDeals = MyDeals.getDeals().getDealsList().size();

        // Instantiate a ViewPager and a PagerAdapter.
        mPager = (ViewPager) findViewById(R.id.pager);
        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);
        //mPager.setPageTransformer(true, new ZoomOutPageTransformer());

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
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        /**
         * Factory method for this fragment class. Constructs a new fragment for the given page number.
         */
        public static PlaceholderFragment create(int dealPosition) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt("deal_position", dealPosition);
            fragment.setArguments(args);
            return fragment;
        }


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            Intent intent = getActivity().getIntent();
            View rootView = inflater.inflate(R.layout.fragment_detail, container, false);


            if(intent != null && intent.hasExtra("deal_position")){
                int position = getArguments().getInt("deal_position");
                Deal deal = MyDeals.getDeals().getDealAtPosition(position);

                if(deal != null) {
                    ImageView imageView = (ImageView) rootView.findViewById(R.id.detail_image);
                    TextView nameView = (TextView) rootView.findViewById(R.id.detail_name);
                    TextView descView = (TextView) rootView.findViewById(R.id.detail_desc);
                    TextView priceView = (TextView) rootView.findViewById(R.id.detail_price);

                    ImageLoader.getInstance().displayImage(deal.getPhotoURL(), imageView);
                    nameView.setText(deal.getBusinessName());
                    descView.setText(deal.getLongDesc());
                    priceView.setText("$" + deal.getPrice());

                    double price = Double.parseDouble(deal.getPrice());
                    if (price <= 5) {
                        priceView.setTextColor(getResources().getColor(R.color.color_eats_red));
                    } else if (price <= 10) {
                        priceView.setTextColor(getResources().getColor(R.color.color_eats_green));
                    } else {
                        priceView.setTextColor(getResources().getColor(R.color.color_eats_purple));
                    }
                }
            }
            return rootView;
        }
    }

    /**
     * A simple pager adapter that represents 5 ScreenSlidePageFragment objects, in
     * sequence.
     */
    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return PlaceholderFragment.create(position);
        }

        @Override
        public int getCount() {
            return numDeals;
        }
    }

}
