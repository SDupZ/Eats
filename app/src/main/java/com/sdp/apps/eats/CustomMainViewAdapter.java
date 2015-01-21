package com.sdp.apps.eats;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Simon on 12/01/2015.
 */
public class CustomMainViewAdapter extends ArrayAdapter<String> {
    private static final String mainMenuString = "Deals Under $";

    public CustomMainViewAdapter(Context context, List<String> deals) {
        super(context, 0, deals);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        String cost = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_main, parent, false);
        }

        TextView mainImage       = (TextView) convertView.findViewById(R.id.listview_main_textimage);
        TextView mainText      = (TextView) convertView.findViewById(R.id.listview_main_text);
        ImageView mainRectangle = (ImageView) convertView.findViewById(R.id.listview_main_color_tag_rect);

        // Populate the data into the template view using the data object
        mainText.setText(mainMenuString + cost);
        mainImage.setText("$" + cost);
        mainImage.setBackground(convertView.getResources().getDrawable(getIconForMainMenu(cost)));
        //getColorForMainMenu(cost)
        mainRectangle.setBackgroundColor(convertView.getResources().getColor(getColorForMainMenu(cost)));
        // Return the completed view to render on screen
        return convertView;
    }

    private int getIconForMainMenu(String cost){
        switch (cost){
            case "5":
                return R.drawable.circle_5;
            case "10":
                return R.drawable.circle_10;
            case "15":
                return R.drawable.circle_15;
            default:
                return R.drawable.circle_5;
        }
    }

    private int getColorForMainMenu(String cost){
        switch (cost){
            case "5":
                return R.color.color_5;
            case "10":
                return R.color.color_10;
            case "15":
                return R.color.color_15;
            default:
                return R.color.white;
        }
    }

}
