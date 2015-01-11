package com.sdp.apps.eats;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 	Main activity. First screen to be loaded when the app opens.
 *
 * 	Currently showing list of all deals. No distinction between price ranges. Also data
 * 	is dummmy. Need to transfer to google fusion.
 *
 * 2015/01/07
 * @author 	Simon du Preez
 * @version 0.1
 */


public class MainActivity extends ActionBarActivity {

    private static String DEBUG_TAG = "SDUPZ DEBUG";

    //----------------------------------------------------------------------------------------------
    // On Create for main activity.
    //----------------------------------------------------------------------------------------------
    /**
     * This is where the application loads. When the application is first started, the deals list
     * will need to be updated from the server.
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new DealsListFragment())
                    .commit();
        }
    }

    @Override
    protected void onStart(){
        super.onStart();
        Log.d(DEBUG_TAG, "onStart");
    }

    @Override
    protected void onResume(){
        super.onResume();
        Log.d(DEBUG_TAG, "onResume");
    }

    @Override
    protected void onPause(){
        super.onPause();
        Log.d(DEBUG_TAG, "onPause");
    }

    @Override
    protected void onStop(){
        super.onStop();
        Log.d(DEBUG_TAG, "onStop");
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        Log.d(DEBUG_TAG, "onDestroy");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    //----------------------------------------------------------------------------------------------
    // On Create for main activity.
    //----------------------------------------------------------------------------------------------
    /**
     * This is where the application loads. When the application is first started, the deals list
     * will need to be updated from the server.
     */

    public static class DealsListFragment extends Fragment {

        ArrayAdapter<String> dealsAdapter;

        public DealsListFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            String[] dummyData= {
                    "My Cheap Kebab",
                    "Some cool pizza deal",
                    "Sushi Surprise",
                    "Thai Turkey",
                    "Lucious Lemons",
                    "Edible Eats ",
                    "Cool Cucumbers",
                    "Food 0",
                    "Food 1",
                    "Food 2",
                    "Food 3",
            };

            List<String> currentDeals = new ArrayList<String>(Arrays.asList(dummyData));

            dealsAdapter = new ArrayAdapter<String>(getActivity(),
                    R.layout.list_item_deals, R.id.list_item_buisness_name, currentDeals);

            View rootView = inflater.inflate(R.layout.fragment_main, container, false);

            ListView view = (ListView) rootView.findViewById(R.id.listview_deals);
            view.setAdapter(dealsAdapter);
            view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String dealName = dealsAdapter.getItem(position);
                    Intent detailActivity = new Intent(getActivity(), DetailActivity.class)
                            .putExtra(Intent.EXTRA_TEXT, dealName);
                    startActivity(detailActivity);
                }
            });

            return rootView;
        }

    }
}
