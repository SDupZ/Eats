package com.sdp.apps.eats.fragments;

/**
 * Created by Simon on 14/03/2015.
 */

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.sdp.apps.eats.Deal;
import com.sdp.apps.eats.MyDeals;
import com.sdp.apps.eats.R;

import java.util.Locale;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Intent intent = getActivity().getIntent();

        final View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

        if(intent != null && intent.hasExtra("deal_id")){
            long dealId = intent.getLongExtra("deal_id", -1);

            if(dealId != -1) {
                final Deal deal = MyDeals.getDeals().getDealWithId(dealId, getActivity());
                if (deal !=null){
                    ImageView imageView = (ImageView) rootView.findViewById(R.id.detail_view_image);
                    ImageView mapView = (ImageView) rootView.findViewById(R.id.map_image);
                    TextView shortDescView = (TextView) rootView.findViewById(R.id.detail_view_short_desc);
                    TextView businessName = (TextView) rootView.findViewById(R.id.detail_view_business_name);
                    TextView price = (TextView) rootView.findViewById(R.id.detail_view_price);
                    TextView descView = (TextView) rootView.findViewById(R.id.detail_view_detail_desc);
                    TextView addressView  = (TextView) rootView.findViewById(R.id.location_address);
                    TextView phoneNumberView  = (TextView) rootView.findViewById(R.id.phone_number);
                    TextView voucherView  = (TextView) rootView.findViewById(R.id.voucher_code);

                    if(!ImageLoader.getInstance().isInited()) {
                        ImageLoaderConfiguration config = new ImageLoaderConfiguration.
                                Builder(getActivity()).build();
                        ImageLoader.getInstance().init(config);
                    }
                    ImageLoader.getInstance().displayImage(deal.getPhotoURL(), imageView);

                    DisplayMetrics displaymetrics = new DisplayMetrics();
                    getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);

                    float ht_px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 200, getResources().getDisplayMetrics());
                    int height = (int) ht_px;
                    int width = displaymetrics.widthPixels;

                    String mapUrl = "http://maps.google.com/maps/api/staticmap?center=" + deal.getLat()
                            + "," + deal.getLongi() + "&zoom=17&size="+
                            width + "x" + height + "&sensor=false&markers=color:blue" +
                            "&markers=color:blue%7Clabel:A%7C"+deal.getLat()+","
                    +deal.getLongi();
                    ImageLoader.getInstance().displayImage(mapUrl, mapView);
                    mapView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String uri = String.format(Locale.ENGLISH, "geo:%f,%f", deal.getLat(), deal.getLongi());
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                            try {
                                getActivity().startActivity(intent);
                            }catch (ActivityNotFoundException e){
                                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                builder.setTitle("Oops...")
                                        .setMessage("Could not find a suitable map application.")
                                        .setPositiveButton("Ok", null)
                                        .show();
                            }
                        }
                    });

                    Typeface font1 = Typeface.createFromAsset(getActivity().getAssets(), "Roboto-Regular.ttf");
                Typeface font2 = Typeface.createFromAsset(getActivity().getAssets(), "Roboto-Light.ttf");
                    Typeface font3 = Typeface.createFromAsset(getActivity().getAssets(), "Roboto-Bold.ttf");

                    businessName.setTypeface(font2);
                    shortDescView.setTypeface(font3);
                    descView.setTypeface(font2);
                    price.setTypeface(font3);
                    addressView.setTypeface(font2);
                    phoneNumberView.setTypeface(font2);
                    voucherView.setTypeface(font2);

                    shortDescView.setText(deal.getShortDesc());
                    price.setText("$" + deal.getPrice());
                    businessName.setText(deal.getBusinessName());
                    descView.setText(deal.getLongDesc());
                    addressView.setText(deal.getAddress());
                    phoneNumberView.setText(deal.getPhoneNumber());
                    voucherView.setText(deal.getVoucherCode());

                    if (deal.getPhoneNumber().equals(""))
                        rootView.findViewById(R.id.phone_number_view_group).setVisibility(View.GONE);
                    if (deal.getAddress().equals(""))
                        rootView.findViewById(R.id.address_view_group).setVisibility(View.GONE);
                    if (deal.getVoucherCode().equals(""))
                        rootView.findViewById(R.id.voucher_view_group).setVisibility(View.GONE);
                }
            }
        }
        return rootView;
    }
}