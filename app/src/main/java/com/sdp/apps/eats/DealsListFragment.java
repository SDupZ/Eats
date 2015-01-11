package com.sdp.apps.eats;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Simon on 11/01/2015.
 */
public class DealsListFragment extends Fragment {

    ArrayAdapter<String> dealsAdapter;

    public DealsListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.dealslistfragement, menu);
    }

    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();

        if(id== R.id.action_refresh){
            FetchDealsTask dealsTask =  new FetchDealsTask();
            dealsTask.execute();
            return true;
        }
        return super.onOptionsItemSelected(item);
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

    public class FetchDealsTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params){
            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String dealsJsonStr = null;

            try {
                URL url = new URL("https://www.googleapis.com/fusiontables/v2/query?sql=SELECT%20*%20FROM%20" +
                        "1ZX1GX4igiJoMv1eT7etzM8MNXnS_1g2io75ioobv&" +
                        "key=AIzaSyC7CYIsM_XEurrxSGqDsue30JCZ8Y4FwtE");

                // Create the request and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                dealsJsonStr = buffer.toString();

                try {
                    getDealsDataFromJson(dealsJsonStr);
                }catch(Exception e){
                    e.printStackTrace();
                }


            } catch (IOException e) {
                Log.e("PlaceholderFragment", "Error ", e);
                // If the code didn't successfully get the data, there's no point in attempting
                // to parse it.
                dealsJsonStr = null;
                return null;
            } finally{
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e("PlaceholderFragment", "Error closing stream", e);
                    }
                }
            }
            return null;
        }


        //------------------------------------------------------------------------------------------
        // JSon String Parsing Helper Methods
        //------------------------------------------------------------------------------------------

        private String[] getDealsDataFromJson(String dealsJsonStr) throws JSONException {

            final String DUE_ROWS = "rows";

            JSONObject dealsJson = new JSONObject(dealsJsonStr);
            JSONArray dealsArray = dealsJson.getJSONArray("rows");

            String[] resultStrs = new String[dealsArray.length()];
            for (int i=0; i< dealsArray.length(); i++){
                String name;
                String description;
                String price;

                name = dealsArray.getJSONArray(i).getString(0);
                description = dealsArray.getJSONArray(i).getString(1);
                price = dealsArray.getJSONArray(i).getString(2);

                resultStrs[i] = name + " " + description + " " + price + " ";
            }
            return null;

        }

    }

}