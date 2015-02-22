package com.sdp.apps.eats.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.sdp.apps.eats.R;

import java.util.List;

/**
 * Created by Simon on 12/01/2015.
 */
public class CustomMainViewAdapter extends ArrayAdapter<Integer> {
    private static final String mainMenuString = "Deals Under $";

    //Circle colors and the corresponding base color.
    private static final int[][] imageAndColorResources = {
            {   R.drawable.circle_red       , R.color.color_eats_red    },
            {   R.drawable.circle_green     , R.color.color_eats_green  },
            {   R.drawable.circle_purple    , R.color.color_eats_purple }
    };

    public CustomMainViewAdapter(Context context, List<Integer> deals) {
        super(context, 0, deals);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position. Transffered as extra text.
        int priceFilter = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_main, parent, false);
        }

        TextView listItemImage      = (TextView) convertView.findViewById(R.id.listview_main_textimage);
        TextView listItemText       = (TextView) convertView.findViewById(R.id.listview_main_text);
        ImageView listItemRectangle =
                (ImageView) convertView.findViewById(R.id.listview_main_color_tag_rect);

        Typeface font = Typeface.createFromAsset(getContext().getAssets(), "vintage.ttf");
        listItemText.setTypeface(font);

        // Populate the data into the template view using the data object
        listItemText.setText(mainMenuString + priceFilter);
        listItemImage.setText("$" + priceFilter);
        listItemImage.setTypeface(font);

        listItemImage.setBackground(convertView.getResources().getDrawable(
                imageAndColorResources[position][0]));
        listItemRectangle.setBackgroundColor(convertView.getResources().getColor(
                imageAndColorResources[position][1]));

        return convertView;
    }
}
