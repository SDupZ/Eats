package com.sdp.apps.eats.fragments;

/**
 * Created by Simon on 14/03/2015.
 */

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
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
                    TextView descView = (TextView) rootView.findViewById(R.id.detail_desc);
                    TextView stickyDescView = (TextView) rootView.findViewById(R.id.detail_sticky_desc);
                    TextView voucherView = (TextView) rootView.findViewById(R.id.voucher_code);
                    TextView aboutTitleView = (TextView) rootView.findViewById(R.id.about_desc_label);
                    TextView aboutView  = (TextView) rootView.findViewById(R.id.about_desc);
                    TextView addressView  = (TextView) rootView.findViewById(R.id.location_address);

                    if(!ImageLoader.getInstance().isInited()) {
                        ImageLoaderConfiguration config = new ImageLoaderConfiguration.
                                Builder(getActivity()).build();
                        ImageLoader.getInstance().init(config);
                    }
                    ImageLoader.getInstance().displayImage(deal.getPhotoURL(), imageView);

                    Typeface font1 = Typeface.createFromAsset(getActivity().getAssets(), "Ubuntu-B.ttf");
                    Typeface font2 = Typeface.createFromAsset(getActivity().getAssets(), "Ubuntu-R.ttf");

                    stickyDescView.setText("$" + deal.getPrice() + " from " + deal.getBusinessName());
                    stickyDescView.setTypeface(font1);

                    descView.setText(deal.getLongDesc());
                    descView.setTypeface(font2);

                    aboutTitleView.setText("About " + deal.getBusinessName());
                    aboutTitleView.setTypeface(font2);

                    aboutView.setText(deal.getAboutPlace());
                    aboutView.setTypeface(font2);

                    addressView.setText(deal.getAddress());
                    addressView.setTypeface(font2);

                    MapFragment mapFragment = (MapFragment) getFragmentManager()
                            .findFragmentById(R.id.map);

                    GoogleMap map = mapFragment.getMap();
                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(
                            new LatLng(deal.getLat(), deal.getLongi()), 15
                    ));

                    ImageView transparentImageView = (ImageView) rootView.findViewById(R.id.transparent_image);

                    transparentImageView.setOnTouchListener(new View.OnTouchListener() {

                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            ScrollView mainScrollView = (ScrollView) rootView.findViewById(R.id.detail_scrollview);
                            int action = event.getAction();
                            switch (action) {
                                case MotionEvent.ACTION_DOWN:
                                    // Disallow ScrollView to intercept touch events.
                                    mainScrollView.requestDisallowInterceptTouchEvent(true);
                                    // Disable touch on transparent view
                                    return false;

                                case MotionEvent.ACTION_UP:
                                    // Allow ScrollView to intercept touch events.
                                    mainScrollView.requestDisallowInterceptTouchEvent(false);
                                    return true;

                                case MotionEvent.ACTION_MOVE:
                                    mainScrollView.requestDisallowInterceptTouchEvent(true);
                                    return false;

                                default:
                                    return true;
                            }
                        }
                    });

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