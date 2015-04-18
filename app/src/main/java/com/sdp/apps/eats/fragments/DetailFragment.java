package com.sdp.apps.eats.fragments;

/**
 * Created by Simon on 14/03/2015.
 */

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.DisplayMetrics;
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
                Deal deal = MyDeals.getDeals().getDealWithId(dealId, getActivity());
                if (deal !=null){
                    ImageView imageView = (ImageView) rootView.findViewById(R.id.detail_image);
                    ImageView mapView = (ImageView) rootView.findViewById(R.id.map_image);
                    TextView descView = (TextView) rootView.findViewById(R.id.detail_desc);
                    TextView stickyDescView = (TextView) rootView.findViewById(R.id.detail_sticky_desc);
                    TextView voucherView = (TextView) rootView.findViewById(R.id.voucher_code);
                    TextView aboutView  = (TextView) rootView.findViewById(R.id.about_desc);
                    TextView addressView  = (TextView) rootView.findViewById(R.id.location_address);

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

                    Typeface font1 = Typeface.createFromAsset(getActivity().getAssets(), "Ubuntu-B.ttf");
                    Typeface font2 = Typeface.createFromAsset(getActivity().getAssets(), "Roboto-Regular.ttf");

                    stickyDescView.setText("$" + deal.getPrice() + " from " + deal.getBusinessName());
                    stickyDescView.setTypeface(font1);

                    descView.setText(deal.getLongDesc());
                    descView.setTypeface(font2);

                    aboutView.setText(deal.getAboutPlace());
                    aboutView.setTypeface(font2);

                    addressView.setText(deal.getAddress());
                    addressView.setTypeface(font2);

                    if (deal.getVoucherCode() != null && !deal.getVoucherCode().equals("")){
                        voucherView.setText("Voucher code: " + deal.getVoucherCode());
                        voucherView.setTypeface(font2);
                    }else{
                        voucherView.setVisibility(View.GONE);
                    }
                }
            }
        }
        return rootView;
    }
}