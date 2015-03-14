package com.sdp.apps.eats.fragments;

/**
 * Created by Simon on 14/03/2015.
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.sdp.apps.eats.Deal;
import com.sdp.apps.eats.R;
import com.sdp.apps.eats.data.DealDbHelper;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Intent intent = getActivity().getIntent();
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

        if(intent != null && intent.hasExtra("deal_id")){
            long dealId = intent.getLongExtra("deal_id", -1);

            if(dealId != -1) {
                Deal deal = DealDbHelper.getHelper(getActivity()).getDealWithID(dealId);
                ImageView imageView = (ImageView) rootView.findViewById(R.id.detail_image);
                TextView descView = (TextView) rootView.findViewById(R.id.detail_desc);
                TextView stickyDescView = (TextView) rootView.findViewById(R.id.detail_sticky_desc);
                TextView voucherView = (TextView) rootView.findViewById(R.id.voucher_code);
                TextView aboutTitleView = (TextView) rootView.findViewById(R.id.about_desc);

                if(!ImageLoader.getInstance().isInited()) {
                    ImageLoaderConfiguration config = new ImageLoaderConfiguration.
                            Builder(getActivity()).build();
                    ImageLoader.getInstance().init(config);
                }
                ImageLoader.getInstance().displayImage(deal.getPhotoURL(), imageView);

                stickyDescView.setText("$" + deal.getPrice() + " from " + deal.getBusinessName());
                descView.setText(deal.getLongDesc());
                aboutTitleView.setText("About " + deal.getBusinessName());

                if (deal.getVoucherCode() != null && !deal.getVoucherCode().equals("")){
                    voucherView.setText("Voucher code: " + deal.getVoucherCode());
                }else{
                    voucherView.setVisibility(View.GONE);
                }
            }
        }
        return rootView;
    }
}