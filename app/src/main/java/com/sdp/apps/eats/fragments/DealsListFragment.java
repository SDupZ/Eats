package com.sdp.apps.eats.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.sdp.apps.eats.Deal;
import com.sdp.apps.eats.MyDeals;
import com.sdp.apps.eats.R;
import com.sdp.apps.eats.activities.DetailActivity;
import com.sdp.apps.eats.adapters.CustomDealArrayAdapter;
import com.sdp.apps.eats.data.ContentDownloader;
import com.sdp.apps.eats.data.DatabaseListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Simon on 11/01/2015.
 *
 * Fragment containing a list populated with the relevant deals. Eg. If set to display $5 deals
 * it will populate with the deals that are $5 or under
 */

public class DealsListFragment extends Fragment implements DatabaseListener, AdapterView.OnItemSelectedListener {

    public static final String LOG_TAG = "Eats Debug";

    private CustomDealArrayAdapter dealsAdapter;
    private ProgressDialog mDialog;

    int priceFilter;
    DisplayImageOptions options;                        //Options for 3rd party image loader

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

        SharedPreferences settings = getActivity().getPreferences(Activity.MODE_PRIVATE);
        priceFilter = settings.getInt("priceFilter", -1);
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

        updateAdapter();
        updateDatabase();
    }

    public void onStop()
    {
        super.onStop();
        SharedPreferences.Editor editor = getActivity().getPreferences(Activity.MODE_PRIVATE).edit();
        editor.putInt("priceFilter", priceFilter);
        editor.commit();
    }
    //----------------------------------------------------------------------------------------------
    // onOptionsItemSelected
    //----------------------------------------------------------------------------------------------
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();

        if(id== R.id.action_refresh){
            updateDatabase();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //----------------------------------------------------------------------------------------------
    // Creates and returns view hierachy associated with the fragment
    //----------------------------------------------------------------------------------------------
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Intent intent =  getActivity().getIntent();

        List<Deal> currentDeals = new ArrayList<Deal>();

        dealsAdapter = new CustomDealArrayAdapter(getActivity(),currentDeals, options);

        View rootView = inflater.inflate(R.layout.fragment_deal_list, container, false);

        ListView view = (ListView) rootView.findViewById(R.id.listview_deals);

        view.setAdapter(dealsAdapter);
        view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent detailActivity = new Intent(getActivity(), DetailActivity.class)
                        .putExtra("deal_position", position);
                startActivity(detailActivity);
            }
        });

        Spinner spinner = (Spinner) rootView.findViewById(R.id.pricefilter_spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.pricefilter_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        setSpinnerValue(spinner);
        spinner.setOnItemSelectedListener(this);

        return rootView;
    }

    private void setSpinnerValue(Spinner spinner){
        switch (priceFilter){
            case (5):
                spinner.setSelection(0);
                break;
            case (10):
                spinner.setSelection(1);
                break;
            case (15):
                spinner.setSelection(2);
                break;
        }
    }

    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        String item = parent.getItemAtPosition(pos).toString();

        switch (item){
            case ("$5"):
                this.priceFilter = 5;
                updateAdapter();
                break;
            case ("$10"):
                this.priceFilter = 10;
                updateAdapter();
                break;
            case ("$15"):
                this.priceFilter = 15;
                updateAdapter();
                break;
        }

        SharedPreferences.Editor editor = getActivity().getPreferences(Activity.MODE_PRIVATE).edit();
        editor.putInt("priceFilter", priceFilter);
        editor.commit();
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }

    public void databaseUpdated(){
        ((ProgressBar)getActivity().findViewById(R.id.deals_list_progress_bar))
                .setVisibility(View.GONE);
        updateAdapter();
    }

    //----------------------------------------------------------------------------------------------
    // Helper Methods
    //----------------------------------------------------------------------------------------------
    private void updateAdapter(){
        dealsAdapter.clear();
        List<Deal> allDeals = MyDeals.getDeals().getDealsList();
        List<Deal> viewableDeals = new ArrayList<Deal>();

        for (Deal deal:allDeals){
            double price = Double.parseDouble(deal.getPrice());
            if (price<= priceFilter && price > priceFilter - 5) {
                dealsAdapter.add(deal);
                viewableDeals.add(deal);
            }
        }
        MyDeals.getDeals().setViewableDeals(viewableDeals);
    }

    private void updateDatabase(){
        ContentDownloader cd = new ContentDownloader(getActivity());
        cd.addDatabaseListener(this);
        cd.updateDatabase();

    }
}