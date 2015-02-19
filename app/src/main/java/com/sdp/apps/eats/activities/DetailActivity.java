package com.sdp.apps.eats.activities;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.sdp.apps.eats.Deal;
import com.sdp.apps.eats.R;
import com.sdp.apps.eats.data.DealContract;
import com.sdp.apps.eats.data.DealDbHelper;


public class DetailActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_detail, menu);
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

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            Intent intent = getActivity().getIntent();
            View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

            if(intent != null && intent.hasExtra("deal_id")){
                Long id = intent.getLongExtra("deal_id", -1);
                if (id != -1) {
                    Cursor c = DealDbHelper.getHelper(getActivity()).getDealWithID(id);
                    c.moveToFirst();
                    String buisnessName =
                            c.getString(c.getColumnIndex(DealContract.DealEntry.COLUMN_BUSINESS_NAME));
                    String shortDesc    =
                            c.getString(c.getColumnIndex(DealContract.DealEntry.COLUMN_SHORT_DESC ));
                    String longDesc     =
                            c.getString(c.getColumnIndex(DealContract.DealEntry.COLUMN_LONG_DESC));
                    double price        =
                            c.getDouble(c.getColumnIndex(DealContract.DealEntry.COLUMN_PRICE));
                    String photoURL     =
                            c.getString(c.getColumnIndex(DealContract.DealEntry.COLUMN_PHOTO_URI));
                    String voucherCode  =
                            c.getString(c.getColumnIndex(DealContract.DealEntry.COLUMN_VOUCHER_CODE));
                    int locationKey     =
                            c.getInt(c.getColumnIndex(DealContract.DealEntry.COLUMN_LOC_KEY));

                    Deal deal = new Deal(
                            buisnessName,
                            shortDesc,
                            longDesc,
                            price,
                            photoURL,
                            voucherCode,
                            locationKey);
                    deal.setID(id);

                    ImageView imageView = (ImageView) rootView.findViewById(R.id.detail_image);
                    TextView nameView = (TextView) rootView.findViewById(R.id.detail_name);
                    TextView descView = (TextView) rootView.findViewById(R.id.detail_desc);
                    TextView priceView = (TextView) rootView.findViewById(R.id.detail_price);

                    ImageLoader.getInstance().displayImage(deal.getPhotoURL(), imageView);
                    nameView.setText(deal.getBusinessName());
                    descView.setText(deal.getLongDesc());
                    priceView.setText(deal.getPrice());
                }

                //ERROR CHECKING SHOUDL GO HERE

            }
            return rootView;
        }
    }
}
