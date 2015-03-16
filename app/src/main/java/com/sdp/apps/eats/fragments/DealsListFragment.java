package com.sdp.apps.eats.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.sdp.apps.eats.Deal;
import com.sdp.apps.eats.MyDeals;
import com.sdp.apps.eats.R;
import com.sdp.apps.eats.activities.DealListActivity;
import com.sdp.apps.eats.activities.DetailActivity;
import com.sdp.apps.eats.adapters.CustomDealArrayAdapter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Simon on 11/01/2015.
 *
 * Fragment containing a list populated with the relevant deals. Eg. If set to display $5 deals
 * it will populate with the deals that are $5 or under
 */

public class DealsListFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{

    private static final double changeRangePrice     =   3;
    private static final double priceRangeOverlap    =   0.25;

    //This should be either 0 or 1: 0=change range. 1=Rest of deals
    int priceFilter;

    private CustomDealArrayAdapter dealsAdapter;
    private ProgressDialog mDialog;

    DisplayImageOptions options;                        //Options for 3rd party image loader
    SwipeRefreshLayout swipeLayout;

    //----------------------------------------------------------------------------------------------
    // Lifecycle methods: oncreates, onresumes, onstops
    //----------------------------------------------------------------------------------------------

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .showImageOnLoading(getResources().getDrawable(R.drawable.ic_image_loading))
                .resetViewBeforeLoading(true)
                .build();
    }

    @Override
    public void onRefresh() {
        ((DealListActivity)getActivity()).updateDatabase();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.dealslistfragement, menu);
    }

    @Override
    public void onStart() {
        super.onStart();

        if(!ImageLoader.getInstance().isInited()) {
            ImageLoaderConfiguration config = new ImageLoaderConfiguration.
                    Builder(getActivity()).build();
            ImageLoader.getInstance().init(config);
        }

        if(MyDeals.getDeals().getDealsList().size() == 0){
            ((ProgressBar)getActivity().findViewById(R.id.deals_list_progress_bar))
                    .setVisibility(View.VISIBLE);
        }
        if (MyDeals.getDeals().getDealsList().size() == 0){
            ((DealListActivity)getActivity()).updateDatabase();
        }else{
            updateAdapter();
        }
    }

    //----------------------------------------------------------------------------------------------
    // onOptionsItemSelected
    //----------------------------------------------------------------------------------------------
    public boolean onOptionsItemSelected(MenuItem item){
        return super.onOptionsItemSelected(item);
    }

    //----------------------------------------------------------------------------------------------
    // Creates and returns view hierachy associated with the fragment
    //----------------------------------------------------------------------------------------------
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_deal_list, container, false);

        Intent intent =  getActivity().getIntent();

        swipeLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_refresh_view);
        swipeLayout.setOnRefreshListener(this);
        swipeLayout.setColorScheme(R.color.eats_blue,
                R.color.eats_green,
                R.color.eats_black);

        List<Deal> currentDeals = new ArrayList<Deal>();
        dealsAdapter = new CustomDealArrayAdapter(getActivity(),currentDeals, options);
        ListView view = (ListView) rootView.findViewById(R.id.listview_deals);

        view.setAdapter(dealsAdapter);
        view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent detailActivity = new Intent(getActivity(), DetailActivity.class)
                        .putExtra("deal_id", dealsAdapter.getItem(position).getID());
                startActivity(detailActivity);
            }
        });

        return rootView;
    }

    public void setPriceFilter(int priceFilter){
        this.priceFilter = priceFilter;
    }


    //----------------------------------------------------------------------------------------------
    // Helper Methods
    //----------------------------------------------------------------------------------------------
    public void updateAdapter(){
        dealsAdapter.clear();
        List<Deal> allDeals = MyDeals.getDeals().getDealsList();

        for (Deal deal:allDeals){
            double price = Double.parseDouble(deal.getPrice());
            if (priceFilter == 0 && price < (changeRangePrice + changeRangePrice * priceRangeOverlap)) {
                dealsAdapter.add(deal);
            }else if (priceFilter==1 && price > changeRangePrice){
                dealsAdapter.add(deal);
            }
        }
        dealsAdapter.sort(new Comparator<Deal>() {
            @Override
            public int compare(Deal lhs, Deal rhs) {
                return lhs.compareTo(rhs);
            }
        });
    }

    public void databaseUpdated(boolean success){
        View childView1 = this.getView();
        if(childView1 != null) {
            childView1.findViewById(R.id.deals_list_progress_bar).setVisibility(View.GONE);

            if (success) {
                childView1.findViewById(R.id.deals_list_swipe_down_text).setVisibility(View.GONE);
                updateAdapter();
            } else {
                childView1.findViewById(R.id.deals_list_swipe_down_text).setVisibility(View.VISIBLE);
            }
            ((SwipeRefreshLayout) childView1.findViewById(R.id.swipe_refresh_view)).setRefreshing(false);
        }
    }
}