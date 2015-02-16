package com.sdp.apps.eats.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.sdp.apps.eats.Deal;
import com.sdp.apps.eats.R;

import java.util.List;

/**
 * Created by Simon on 12/01/2015.
 */
public class CustomDealArrayAdapter extends ArrayAdapter<Deal> {
    DisplayImageOptions options;

    public CustomDealArrayAdapter(Context context, List<Deal> deals, DisplayImageOptions options) {
        super(context, 0, deals);
        this.options = options;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Deal deal = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_deals, parent, false);
        }

        TextView businessName = (TextView) convertView.findViewById(R.id.list_item_buisness_name);
        TextView description = (TextView) convertView.findViewById(R.id.list_item_small_deal_description);
        TextView price = (TextView) convertView.findViewById(R.id.list_item_price);
        ImageView photo = (ImageView) convertView.findViewById(R.id.list_item_image);


        // Populate the data into the template view using the data object
        businessName.setText(deal.getBusinessName());
        description.setText(deal.getDescription());
        price.setText("$" + deal.getPrice());

        ImageLoader.getInstance().displayImage(deal.getPhotoURL(), photo, options);

        /*if (deal.getPhoto() == null) {
            photo.setImageDrawable(convertView.getResources().getDrawable(R.drawable.ic_launcher));
        }else {
            photo.setImageBitmap(deal.getPhoto());
        }*/

        // Return the completed view to render on screen
        return convertView;
    }

}
