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
import java.util.List;


/**
 * Created by Simon on 21/01/2015.
 */
public class MainFragment extends Fragment  {

    //This is a Integer as the filter will use a Integer.
    private static final List<Integer> priceOptionsMainMenu = new ArrayList<Integer>(){{
            add(new Integer(5));
            add(new Integer(10));
            add(new Integer(15));}};

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
        CustomMainViewAdapter menuAdapter = new CustomMainViewAdapter(getActivity(),priceOptionsMainMenu);

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        ListView view = (ListView) rootView.findViewById(R.id.listview_main);
        view.setAdapter(menuAdapter);
        view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent detailActivity = new Intent(getActivity(), DealListActivity.class)
                        .putExtra(Intent.EXTRA_TEXT, "" + priceOptionsMainMenu.get(position));
                startActivity(detailActivity);
            }
        });

        return rootView;
    }
}
