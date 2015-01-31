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
import android.widget.ListView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;

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
 */
public class DealsListFragment extends Fragment {

    CustomDealArrayAdapter dealsAdapter;


    DisplayImageOptions options;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.ic_stub)
                .showImageOnFail(R.drawable.ic_error)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .build();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.dealslistfragement, menu);
    }

    @Override
    public void onResume(){
        super.onResume();
        FetchDealsTask dealsTask =  new FetchDealsTask();
        dealsTask.execute();
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

        /*Deal[] dummyData= {
                new Deal("KEBAB KING", "My Cheap Kebab" , 10.0, null),
                new Deal("Pizza dudes", "Some cool pizza deal",5.0, null),
                new Deal("Sashimi", "Sushi Surprise",30.0, null),
                new Deal("Thai Turkey", "My Cheap Kebab",20.0, null),
                new Deal("Lucious Lemons", "My Cheap Kebab",4.0, null),
                new Deal("Edible Eats", "My Cheap Kebab",3.0, null)
         };*/

        List<Deal> currentDeals = new ArrayList<Deal>();

        dealsAdapter = new CustomDealArrayAdapter(getActivity(),currentDeals, options);

        View rootView = inflater.inflate(R.layout.fragment_list_deals, container, false);

        ListView view = (ListView) rootView.findViewById(R.id.listview_deals);

        view.setAdapter(dealsAdapter);
        view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Deal deal = dealsAdapter.getItem(position);
                Intent detailActivity = new Intent(getActivity(), DetailActivity.class)
                        .putExtra(Intent.EXTRA_TEXT, deal.getDescription());
                startActivity(detailActivity);
            }
        });

        return rootView;
    }

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
                    dealsAdapter.add(deal);
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