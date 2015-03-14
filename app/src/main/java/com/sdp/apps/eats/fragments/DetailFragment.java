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
import com.sdp.apps.eats.Deal;
import com.sdp.apps.eats.MyDeals;
import com.sdp.apps.eats.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailFragment extends Fragment {

    /**
     * Factory method for this fragment class. Constructs a new fragment for the given page number.
     */
    public static  DetailFragment create(int dealPosition, int priceFilter) {
        DetailFragment fragment = new  DetailFragment();
        Bundle args = new Bundle();
        args.putInt("deal_position", dealPosition);
        args.putInt("price_filter", priceFilter);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Intent intent = getActivity().getIntent();
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

        if(intent != null && intent.hasExtra("deal_position") && intent.hasExtra("price_filter")){
            int position = getArguments().getInt("deal_position");
            int priceFilter = getArguments().getInt("price_filter");
            Deal deal;

            if (priceFilter == 0)
                deal = MyDeals.getDeals().getChangeRangeDeals().get(position);
            else
                deal = MyDeals.getDeals().getMealRangeDeals().get(position);

            if(deal != null) {
                ImageView imageView = (ImageView) rootView.findViewById(R.id.detail_image);
                TextView descView = (TextView) rootView.findViewById(R.id.detail_desc);
                TextView stickyDescView = (TextView) rootView.findViewById(R.id.detail_sticky_desc);
                TextView voucherView = (TextView) rootView.findViewById(R.id.voucher_code);
                TextView aboutTitleView = (TextView) rootView.findViewById(R.id.about_desc);

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