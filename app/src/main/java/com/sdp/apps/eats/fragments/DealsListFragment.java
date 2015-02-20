package com.sdp.apps.eats.fragments;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.sdp.apps.eats.Deal;
import com.sdp.apps.eats.R;
import com.sdp.apps.eats.activities.DetailActivity;
import com.sdp.apps.eats.adapters.CustomDealArrayAdapter;
import com.sdp.apps.eats.data.ContentDownloader;
import com.sdp.apps.eats.data.DatabaseListener;
import com.sdp.apps.eats.data.DealContract;
import com.sdp.apps.eats.data.DealDbHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Simon on 11/01/2015.
 *
 * Fragment containing a list populated with the relevant deals. Eg. If set to display $5 deals
 * it will populate with the deals that are $5 or under
 */

public class DealsListFragment extends Fragment implements DatabaseListener{

    private CustomDealArrayAdapter dealsAdapter;

    //The maximum price to list deals for. (-1 for all)
    private int priceFilter;

    DisplayImageOptions options;                        //Options for 3rd party image loader


    //----------------------------------------------------------------------------------------------
    // Lifecycle methods: oncreates, onresumes, onstops
    //----------------------------------------------------------------------------------------------

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        //!!!------------{
        options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .build();
        //!!!------------}
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.dealslistfragement, menu);
    }

    @Override
    public void onStart() {
        super.onResume();
        SharedPreferences settings = getActivity().getPreferences(Activity.MODE_PRIVATE);
        this.priceFilter = settings.getInt("priceFilter", -1);
        updateAdapter();
    }

    public void onStop()
    {
        super.onStop();
        SharedPreferences.Editor editor = getActivity().getPreferences(Activity.MODE_PRIVATE).edit();
        editor.putInt("priceFilter", this.priceFilter);
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

        if(intent != null && intent.hasExtra(Intent.EXTRA_TEXT)){
            priceFilter = Integer.parseInt(intent.getStringExtra(Intent.EXTRA_TEXT));

            SharedPreferences.Editor editor = getActivity().getPreferences(Activity.MODE_PRIVATE).edit();
            editor.putInt("priceFilter", this.priceFilter);
            editor.commit();
        }

        List<Deal> currentDeals = new ArrayList<Deal>();

        dealsAdapter = new CustomDealArrayAdapter(getActivity(),currentDeals, options);

        View rootView = inflater.inflate(R.layout.fragment_deal_list, container, false);

        ListView view = (ListView) rootView.findViewById(R.id.listview_deals);

        view.setAdapter(dealsAdapter);
        view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Deal deal = dealsAdapter.getItem(position);
                Intent detailActivity = new Intent(getActivity(), DetailActivity.class)
                        .putExtra("deal_id", deal.getID());
                startActivity(detailActivity);
            }
        });

        return rootView;
    }


    public void databaseUpdated(){
        updateAdapter();
        //Toast.makeText(this.getActivity().getApplicationContext(),
        //        "Content Updated.", Toast.LENGTH_SHORT).show();
    }

    //----------------------------------------------------------------------------------------------
    // Helper Methods
    //----------------------------------------------------------------------------------------------
    private void updateAdapter(){
        Cursor c = DealDbHelper.getHelper(getActivity()).getAllData();
        dealsAdapter.clear();
        if (c.moveToFirst()){
            do{
                double price        =
                        c.getDouble(c.getColumnIndex(DealContract.DealEntry.COLUMN_PRICE));
                if(price <= priceFilter) {
                    String businessName =
                            c.getString(c.getColumnIndex(DealContract.DealEntry.COLUMN_BUSINESS_NAME));
                    long id = c.getLong(c.getColumnIndex(DealContract.DealEntry._ID));
                    String shortDesc =
                            c.getString(c.getColumnIndex(DealContract.DealEntry.COLUMN_SHORT_DESC));
                    String longDesc =
                            c.getString(c.getColumnIndex(DealContract.DealEntry.COLUMN_LONG_DESC));

                    String photoURL =
                            c.getString(c.getColumnIndex(DealContract.DealEntry.COLUMN_PHOTO_URI));
                    String voucherCode =
                            c.getString(c.getColumnIndex(DealContract.DealEntry.COLUMN_VOUCHER_CODE));
                    int locationKey =
                            c.getInt(c.getColumnIndex(DealContract.DealEntry.COLUMN_LOC_KEY));

                    Deal deal = new Deal(
                            businessName,
                            shortDesc,
                            longDesc,
                            price,
                            photoURL,
                            voucherCode,
                            locationKey);
                    deal.setID(id);

                    dealsAdapter.add(deal);
                }
            }while(c.moveToNext());
        }
    }

    private void updateDatabase(){
        ContentDownloader cd = new ContentDownloader(getActivity());
        cd.addDatabaseListener(this);
        cd.updateDatabase();
    }
}