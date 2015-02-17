package com.sdp.apps.eats.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.sdp.apps.eats.adapters.CustomMainViewAdapter;
import com.sdp.apps.eats.R;
import com.sdp.apps.eats.activities.DealListActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * Created by Simon on 21/01/2015.
 */
public class MainFragment extends Fragment  {
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.dealslistfragement, menu);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final int[] dealNumbers ={5,10,15};
        String[] dealItems= {
                "5",
                "10",
                "15",
        };

        List<String> currentDeals = new ArrayList<String>(Arrays.asList(dealItems));

        CustomMainViewAdapter menuAdapter =
            new CustomMainViewAdapter(
                    getActivity(),
                    currentDeals);

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        ListView view = (ListView) rootView.findViewById(R.id.listview_main);
        view.setAdapter(menuAdapter);
        view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent detailActivity = new Intent(getActivity(), DealListActivity.class)
                        .putExtra(Intent.EXTRA_TEXT, "" + dealNumbers[position]);
                startActivity(detailActivity);
            }
        });

        return rootView;
    }
}
