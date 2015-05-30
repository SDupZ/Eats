package com.sdp.apps.eats.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.sdp.apps.eats.Deal;
import com.sdp.apps.eats.R;
import com.sdp.apps.eats.activities.DetailActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Simon on 3/04/2015.
 */
public class DealsAdapter extends RecyclerView.Adapter<DealsAdapter.ViewHolder> {
    private List<Deal> mDataset;
    private int lastPosition = -1;
    private Context context;
    DisplayImageOptions options;

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private Deal deal;

        private TextView businessNameTextView;
        private TextView shortDescTextView;
        private TextView priceTextView;
        private ImageView photoImageView;

        public RelativeLayout container;

        public ViewHolder(View v) {
            super(v);
            businessNameTextView = (TextView) v.findViewById(R.id.list_item_buisness_name);
            shortDescTextView = (TextView) v.findViewById(R.id.list_item_small_deal_description);
            priceTextView = (TextView) v.findViewById(R.id.list_item_price);
            photoImageView = (ImageView) v.findViewById(R.id.list_item_image);
            container = (RelativeLayout) v.findViewById(R.id.list_item_container);


            Typeface font = Typeface.createFromAsset(v.getContext().getAssets(), "Roboto-Bold.ttf");
            Typeface font2 = Typeface.createFromAsset(v.getContext().getAssets(), "Roboto-Light.ttf");
            Typeface font3 = Typeface.createFromAsset(v.getContext().getAssets(), "Roboto-Regular.ttf");
            businessNameTextView.setTypeface(font);
            shortDescTextView.setTypeface(font2);
            priceTextView.setTypeface(font3);

            v.setOnClickListener(this);
        }

        public void bindDeal(Deal deal, DisplayImageOptions options){
            this.deal = deal;
            String buisnessNameText  = deal.getBusinessName();

            businessNameTextView.setText(buisnessNameText);
            shortDescTextView.setText(deal.getShortDesc());
            priceTextView.setText("$" + deal.getPrice());

            ImageLoader.getInstance().displayImage(deal.getPhotoURL(), photoImageView, options);
        }

        @Override
        public void onClick(View v) {
            if (deal != null) {
                Intent detailActivity = new Intent(v.getContext(),DetailActivity.class)
                        .putExtra("deal_id", deal.getID());
                v.getContext().startActivity(detailActivity);

            }
        }
    }

    public DealsAdapter(Context context, DisplayImageOptions options) {
        mDataset = new ArrayList<Deal>();
        this.context = context;
        this.options = options;
    }

    @Override
    public DealsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_deal, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Deal currentDeal = mDataset.get(position);
        holder.bindDeal(currentDeal, this.options);
        //setAnimation(holder.container, position);
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public void add(Deal deal) {
        mDataset.add(deal);
        this.notifyDataSetChanged();
    }

    public void replaceAll(List<Deal> newDealList){
        this.mDataset = newDealList;
        this.notifyDataSetChanged();
    }


    private void setAnimation(View viewToAnimate, int position){
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition){
            Animation animation = AnimationUtils.loadAnimation(context, R.anim.float_in_up);
            if (position <= 4) {
                animation.setStartOffset((long) ((position / 4.0f) * 100));
            }
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }
}