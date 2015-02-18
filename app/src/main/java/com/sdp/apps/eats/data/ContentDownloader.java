package com.sdp.apps.eats.data;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.sdp.apps.eats.Deal;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Simon on 18/02/2015.
 */
public class ContentDownloader extends AsyncTask<Void, Void, Deal[]>{

    private Context context;

    public ContentDownloader(Context context){this.context = context;}

    public void updateDatabase(){
        ContentDownloader updateDealsTask =  new ContentDownloader(this.context);
        updateDealsTask.execute();
    }

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
            DealDbHelper db = DealDbHelper.getHelper(context);
            db.deleteAllData();
            for(Deal deal : result){
                db.insertData(deal);
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
            String businessName;
            String shortDesc;
            String longDesc     =   null;
            double price;
            String photoURI;
            String voucherCode  =   null;
            int locationKey     =   -1;

            businessName = dealsArray.getJSONArray(i).getString(0);
            shortDesc = dealsArray.getJSONArray(i).getString(1);
            price = Double.parseDouble(dealsArray.getJSONArray(i).getString(2));
            photoURI = dealsArray.getJSONArray(i).getString(5).trim();

            Deal deal = new Deal(
                    businessName,
                    shortDesc,
                    longDesc,
                    price,
                    photoURI,
                    voucherCode,
                    locationKey);

            resultDeals[i] = deal;
        }
        return resultDeals;

    }
}
