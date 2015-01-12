package com.sdp.apps.eats;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Simon on 12/01/2015.
 */
public class CustomDealArrayAdapter extends ArrayAdapter<Deal> {
    public CustomDealArrayAdapter(Context context, List<Deal> deals) {
        super(context, 0, deals);
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


        // Populate the data into the template view using the data object
        businessName.setText(deal.getBusinessName());
        description.setText(deal.getDescription());
        price.setText("$" + deal.getPrice());

        // Return the completed view to render on screen
        return convertView;
    }

}
