package com.sdp.apps.eats.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Simon on 17/02/2015.
 */
public class DealDbHelper extends SQLiteOpenHelper{

    private static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "deals.db";

    public DealDbHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        final String SQL_CREATE_DEAL_TABLE = "CREATE TABLE "+
                DealContract.DealEntry.TABLE_NAME           + " (" +
                DealContract.DealEntry._ID                  + "INTEGER PRIMARY KEY AUTOINCREMENT,"+
                DealContract.DealEntry.COLUMN_LOC_KEY       + "INTEGER NOT NULL, "+
                DealContract.DealEntry.COLUMN_BUSINESS_NAME + "TEXT NOT NULL,"+
                DealContract.DealEntry.COLUMN_SHORT_DESC    + "TEXT NOT NULL,"+
                DealContract.DealEntry.COLUMN_LONG_DESC     + "TEXT,"+
                DealContract.DealEntry.COLUMN_PRICE         + "REAL NOT NULL,"+
                DealContract.DealEntry.COLUMN_PHOTO_URI     + "TEXT,"+
                DealContract.DealEntry.COLUMN_VOUCHER_CODE  + "TEXT" +

                // Set up the location column as a foreign key to the location table
                " FOREIGN KEY (" + DealContract.DealEntry.COLUMN_LOC_KEY + ") REFERENCES" +
                DealContract.LocationEntry.TABLE_NAME + " (" + DealContract.LocationEntry._ID +"),";

        final String SQL_CREATE_LOCATION_TABLE = "CREATE TABLE " +
                DealContract.LocationEntry.TABLE_NAME + " (" +
                DealContract.LocationEntry._ID + " INTEGER PRIMARY KEY,"+
                DealContract.LocationEntry.COLUMN_LOCATION_SETTING + "TEXT UNIQUE NOT NULL"+
                DealContract.LocationEntry.COLUMN_CITY_NAME     + "TEXT NOT NULL,"+
                DealContract.LocationEntry.COLUMN_COORD_LAT     + "REAL NOT NULL,"+
                DealContract.LocationEntry.COLUMN_COORD_LONG    + "REAL NOT NULL,";

        db.execSQL(SQL_CREATE_DEAL_TABLE);
        db.execSQL(SQL_CREATE_LOCATION_TABLE);

    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL("DROP TABLE IF EXISTS " + DealContract.LocationEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DealContract.DealEntry.TABLE_NAME);
        onCreate(db);
    }
}
