package com.sdp.apps.eats;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ImageView;

/**
 * Created by Simon on 11/01/2015.
 */
public class DealAdapter extends CursorAdapter{

    public DealAdapter(Context context, Cursor c, int flags){
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item_deals, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
    }
}
