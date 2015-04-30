package com.sdp.apps.eats.data;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;

import com.sdp.apps.eats.Deal;
import com.sdp.apps.eats.MyDeals;

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

/**
 * Created by Simon on 18/02/2015.
 */
public class ContentDownloader extends AsyncTask<Void, Void, Deal[]>{

    private Context context;
    private ArrayList<DatabaseListener> listeners;

    public ContentDownloader(Context context){
        this.context = context;
        this.listeners = new ArrayList<DatabaseListener>();
    }

    public void updateDatabase(){
        if (isConnected()) {
            this.execute();
        } else {
            notifyListeners(false);
        }
    }

    public void addDatabaseListener(DatabaseListener databaseListener){
        this.listeners.add(databaseListener);
    }

    //true for successful, false for non sucessful
    private void notifyListeners(boolean success){
        for (DatabaseListener listener: this.listeners){
            listener.databaseUpdated(success);
        }
    }

    @Override
    protected Deal[] doInBackground(Void... params){
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
            if(myDeals != null){
                DealDbHelper db = DealDbHelper.getHelper(context);
                db.deleteAllData();
                for(int i = 0; i <myDeals.length; i++){
                    long id = db.insertData(myDeals[i]);
                    myDeals[i].setID(id);
                }
            }
            return myDeals;
        }catch(JSONException e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(Deal[] result) {
        if(result != null){
            MyDeals.getDeals().updateDealsList(Arrays.asList(result));
            notifyListeners(true);
        }
    }

    //------------------------------------------------------------------------------------------
    // JSon String Parsing Helper Methods
    //------------------------------------------------------------------------------------------

    private Deal[] getDealsDataFromJson(String dealsJsonStr) throws JSONException {
        JSONObject dealsJson = new JSONObject(dealsJsonStr);
        JSONArray columnIndexArray = dealsJson.getJSONArray("columns");

        ArrayList<String> columnNamesArrayList = new ArrayList<String>();

        for (int i=0; i< columnIndexArray.length(); i++){
            columnNamesArrayList.add(columnIndexArray.getString(i));
        }

        JSONArray dealsArray = dealsJson.getJSONArray("rows");

        Deal[] resultDeals = new Deal[dealsArray.length()];

        for (int i=0; i< dealsArray.length(); i++){
            String businessName;    String shortDesc;   String longDesc     =   null;
            double price;           String photoURI;    String voucherCode  =   null;
            int locationKey= -1;    String aboutPlace;  String address;
            double lat;             double longi;       String phoneNumber;

            businessName = dealsArray.getJSONArray(i).getString(columnNamesArrayList.indexOf(
                    DealContract.DealEntry.COLUMN_BUSINESS_NAME));
            shortDesc = dealsArray.getJSONArray(i).getString(columnNamesArrayList.indexOf(
                    DealContract.DealEntry.COLUMN_SHORT_DESC));
            longDesc = dealsArray.getJSONArray(i).getString(columnNamesArrayList.indexOf(
                    DealContract.DealEntry.COLUMN_LONG_DESC));
            price = Double.parseDouble(dealsArray.getJSONArray(i).getString(columnNamesArrayList.indexOf(
                    DealContract.DealEntry.COLUMN_PRICE)));
            photoURI = dealsArray.getJSONArray(i).getString(columnNamesArrayList.indexOf(
                    DealContract.DealEntry.COLUMN_PHOTO_URI)).trim();
            voucherCode = dealsArray.getJSONArray(i).getString(columnNamesArrayList.indexOf(
                    DealContract.DealEntry.COLUMN_VOUCHER_CODE));
            aboutPlace = dealsArray.getJSONArray(i).getString(columnNamesArrayList.indexOf(
                    DealContract.DealEntry.COLUMN_ABOUT_PLACE));
            phoneNumber = dealsArray.getJSONArray(i).getString(columnNamesArrayList.indexOf(
                    DealContract.DealEntry.COLUMN_PHONE_NUMBER));
            try {
                lat = Double.parseDouble((dealsArray.getJSONArray(i).getString(columnNamesArrayList.indexOf(
                        DealContract.DealEntry.COLUMN_LAT))));
                longi = Double.parseDouble((dealsArray.getJSONArray(i).getString(columnNamesArrayList.indexOf(
                        DealContract.DealEntry.COLUMN_LONGI))));
            }catch(NumberFormatException e){
                lat = -36.8505778;
                longi = 174.7666621;
            }
            address = dealsArray.getJSONArray(i).getString(columnNamesArrayList.indexOf(
                    DealContract.DealEntry.COLUMN_ADDRESS));

            Deal deal = new Deal(businessName,shortDesc,longDesc,price,photoURI,voucherCode,
                    locationKey, aboutPlace, lat, longi, address, phoneNumber);

            resultDeals[i] = deal;
        }
        return resultDeals;
    }

    private boolean isConnected(){
        ConnectivityManager cm =
                (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }
}
