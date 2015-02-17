package com.sdp.apps.eats.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.sdp.apps.eats.adapters.CustomDealArrayAdapter;
import com.sdp.apps.eats.Deal;
import com.sdp.apps.eats.R;
import com.sdp.apps.eats.activities.DetailActivity;

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
import java.util.List;

/**
 * Created by Simon on 11/01/2015.
 *
 * Fragment containing a list populated with the relevant deals. Eg. If set to display $5 deals
 * it will populate with the deals that are $5 or under
 */

public class DealsListFragment extends Fragment {
    private CustomDealArrayAdapter dealsAdapter;        //Adapter for each deal
    private int priceFilter;                          //The current cost filter.(Eg 5 dollars)

    DisplayImageOptions options;                        //Options for 3rd party image loader
    private SharedPreferences settings;                 //Shared Prefs

    //----------------------------------------------------------------------------------------------
    // Oncreate for this activity.
    //----------------------------------------------------------------------------------------------
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        //!!!------------{
        settings = PreferenceManager.getDefaultSharedPreferences(getActivity());
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.ic_stub)
                .showImageOnFail(R.drawable.ic_error)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .build();

        if (savedInstanceState != null){
            priceFilter = savedInstanceState.getInt("dealPrice");
            Log.v("EATS", "STATE Resumed" + priceFilter);
        }
        //!!!------------}
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.dealslistfragement, menu);
    }

    //!!!------------{

    @Override
    public void onResume() {
        super.onResume();

        settings = PreferenceManager.getDefaultSharedPreferences(getActivity());
        priceFilter = settings.getInt("priceFilter", priceFilter);

        updateDeals();
    }

    public void onStop()
    {
        super.onStop();
        SharedPreferences.Editor editor = this.settings.edit();
        editor.putFloat("priceFilter", this.priceFilter);
        editor.commit();
    }
    //!!!------------}

    @Override
    public void onStart() {
        super.onStart();
        updateDeals();
    }

    //----------------------------------------------------------------------------------------------
    // onOptionsItemSelected
    //----------------------------------------------------------------------------------------------
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();

        if(id== R.id.action_refresh){
            updateDeals();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //!!!------------{
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        savedInstanceState.putInt("dealPrice",priceFilter);
        Log.v("EATS", "STATE SAVED");
    }
    //!!!------------}

    //----------------------------------------------------------------------------------------------
    // Creates and returns view hierachy associated with the fragment
    //----------------------------------------------------------------------------------------------
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Intent intent =  getActivity().getIntent();

        if(intent != null && intent.hasExtra(Intent.EXTRA_TEXT)){
            priceFilter = Integer.parseInt(intent.getStringExtra(Intent.EXTRA_TEXT));

            SharedPreferences.Editor editor = this.settings.edit();
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
                        .putExtra("Deal", deal);
                startActivity(detailActivity);
            }
        });

        return rootView;
    }

    //----------------------------------------------------------------------------------------------
    // Update deals list helper method
    //----------------------------------------------------------------------------------------------
    private void updateDeals(){
        FetchDealsTask dealsTask =  new FetchDealsTask();
        dealsTask.execute();
    }

    //----------------------------------------------------------------------------------------------
    // Creates and returns view hierachy associated with the fragment
    //----------------------------------------------------------------------------------------------
    public class FetchDealsTask extends AsyncTask<Void, Void, Deal[]> {

        @Override
        protected Deal[] doInBackground(Void... params){
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
            try {
                Deal[] myDeals = getDealsDataFromJson(dealsJsonStr);
                /*
                for (Deal aDeal: myDeals){
                    Log.v("EATS", "TEST" + aDeal.getBusinessName() + aDeal.getPhotoURL());
                    if (aDeal.getPhotoURL() != null) {
                        try {
                            URL url = new URL(aDeal.getPhotoURL());
                            HttpGet httpRequest = null;
                            httpRequest = new HttpGet(url.toURI());

                            HttpClient httpclient = new DefaultHttpClient();
                            HttpResponse response = (HttpResponse) httpclient
                                    .execute(httpRequest);

                            HttpEntity entity = response.getEntity();
                            BufferedHttpEntity b_entity = new BufferedHttpEntity(entity);
                            InputStream input = b_entity.getContent();

                            Bitmap bitmap = BitmapFactory.decodeStream(input);

                            aDeal.setPhoto(bitmap);
                        } catch (MalformedURLException e){
                            aDeal.setPhoto(null);
                            e.printStackTrace();
                        } catch (URISyntaxException e) {
                            aDeal.setPhoto(null);
                            e.printStackTrace();
                        } catch (IOException e) {
                            aDeal.setPhoto(null);
                            e.printStackTrace();
                        }
                    }
                }*/
                return myDeals;
            }catch(JSONException e){
                e.printStackTrace();
                return null;
            }

        }

        @Override
        protected void onPostExecute(Deal[] result) {
            if(result != null){
                dealsAdapter.clear();
                for(Deal deal : result){
                    if(Double.parseDouble(deal.getPrice()) <= priceFilter && Double.parseDouble(deal.getPrice()) > (priceFilter-5)) {
                        dealsAdapter.add(deal);
                    }
                }
            }
        }

        //------------------------------------------------------------------------------------------
        // JSon String Parsing Helper Methods
        //------------------------------------------------------------------------------------------

        private Deal[] getDealsDataFromJson(String dealsJsonStr) throws JSONException {

            final String DUE_ROWS = "rows";

            JSONObject dealsJson = new JSONObject(dealsJsonStr);
            JSONArray dealsArray = dealsJson.getJSONArray("rows");

            Deal[] resultDeals = new Deal[dealsArray.length()];

            for (int i=0; i< dealsArray.length(); i++){
                String name;
                String description;
                double price;
                String photoURL;

                name = dealsArray.getJSONArray(i).getString(0);
                description = dealsArray.getJSONArray(i).getString(1);
                price = Double.parseDouble(dealsArray.getJSONArray(i).getString(2));
                photoURL = dealsArray.getJSONArray(i).getString(5).trim();

                resultDeals[i] = new Deal(name,description, price, photoURL);
            }
            return resultDeals;

        }

    }

}