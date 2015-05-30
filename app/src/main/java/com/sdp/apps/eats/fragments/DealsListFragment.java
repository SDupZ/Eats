package com.sdp.apps.eats.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.sdp.apps.eats.Deal;
import com.sdp.apps.eats.MyDeals;
import com.sdp.apps.eats.R;
import com.sdp.apps.eats.activities.DealListActivity;
import com.sdp.apps.eats.adapters.DealsAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Simon on 11/01/2015.
 *
 * Fragment containing a list populated with the relevant deals. Eg. If set to display $5 deals
 * it will populate with the deals that are $5 or under
 */

public class DealsListFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{
    public static final String SCROLL_STATE_TAG = "LAYOUT_STATE_PARCELABLE";

    private RecyclerView mRecyclerView;
    private DealsAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private static final double changeRangePrice     =   3;
    private static final double priceRangeOverlap    =   0.25;

    //This should be either 0 or 1: 0=change range. 1=Rest of deals
    int priceFilter;

    private ProgressDialog mDialog;

    DisplayImageOptions options;                        //Options for 3rd party image loader
    SwipeRefreshLayout swipeLayout;

    private static Parcelable state;

    //----------------------------------------------------------------------------------------------
    // Lifecycle methods: oncreates, onresumes, onstops
    //----------------------------------------------------------------------------------------------

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        options = new DisplayImageOptions.Builder()
                .cacheOnDisk(true)
                .showImageOnLoading(getResources().getDrawable(R.drawable.ic_image_loading))
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .imageScaleType(ImageScaleType.EXACTLY)
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
        Log.d("UNI EATS", "Start method called on fragment: " + priceFilter);
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
    // Creates and returns view hierachy associated with the fragment
    //----------------------------------------------------------------------------------------------
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_deal_list, container, false);

        swipeLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipe_refresh_view);
        swipeLayout.setOnRefreshListener(this);
        swipeLayout.setColorScheme(R.color.eats_blue,
                R.color.eats_green,
                R.color.eats_black);

        mRecyclerView = (RecyclerView) v.findViewById(R.id.deals_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this.getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        mAdapter = new DealsAdapter(getActivity(), this.options);
        mRecyclerView.setAdapter(mAdapter);

        mLayoutManager.onSaveInstanceState();
        swipeLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipe_refresh_view);
        swipeLayout.setOnRefreshListener(this);
        return v;
    }

    public void setPriceFilter(int priceFilter){
        this.priceFilter = priceFilter;
    }


    //----------------------------------------------------------------------------------------------
    // Helper Methods
    //----------------------------------------------------------------------------------------------
    public void updateAdapter(){
        List<Deal> allDeals = MyDeals.getDeals().getDealsList();
        List<Deal> showingDeals = new ArrayList<Deal>();
        for (Deal deal:allDeals){
            double price = Double.parseDouble(deal.getPrice());
            if (priceFilter == 0 && price < (changeRangePrice + changeRangePrice * priceRangeOverlap)) {
                showingDeals.add(deal);
            }else if (priceFilter==1 && price > changeRangePrice){
                showingDeals.add(deal);
            }
            Collections.sort(showingDeals);
        }
        mAdapter.replaceAll(showingDeals);
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

    //----------------------------------------------------------------------------------------------
    // StateRestoration
    //----------------------------------------------------------------------------------------------

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        this.state = mRecyclerView.getLayoutManager().onSaveInstanceState();
        savedInstanceState.putParcelable(SCROLL_STATE_TAG, state);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);

        if(savedInstanceState != null){
            this.state = savedInstanceState.getParcelable(SCROLL_STATE_TAG);
        }
        mRecyclerView.getLayoutManager().onRestoreInstanceState(state);
    }
}