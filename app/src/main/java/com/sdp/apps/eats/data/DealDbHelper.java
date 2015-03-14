package com.sdp.apps.eats.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.sdp.apps.eats.Deal;

/**
 * Created by Simon on 17/02/2015.
 */
public class DealDbHelper extends SQLiteOpenHelper{

    private static DealDbHelper instance;
    private static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "deals.db";

    private final String SQL_CREATE_DEAL_TABLE = "CREATE TABLE "+
            DealContract.DealEntry.TABLE_NAME           + " (" +
            DealContract.DealEntry._ID                  + " INTEGER PRIMARY KEY AUTOINCREMENT, "+
            DealContract.DealEntry.COLUMN_LOC_KEY       + " INTEGER, "+
            DealContract.DealEntry.COLUMN_BUSINESS_NAME + " TEXT NOT NULL,"+
            DealContract.DealEntry.COLUMN_SHORT_DESC    + " TEXT NOT NULL,"+
            DealContract.DealEntry.COLUMN_LONG_DESC     + " TEXT,"+
            DealContract.DealEntry.COLUMN_PRICE         + " REAL NOT NULL,"+
            DealContract.DealEntry.COLUMN_PHOTO_URI     + " TEXT,"+
            DealContract.DealEntry.COLUMN_VOUCHER_CODE  + " TEXT," +

            // Set up the location column as a foreign key to the location table
            " FOREIGN KEY (" + DealContract.DealEntry.COLUMN_LOC_KEY + ") REFERENCES " +
            DealContract.LocationEntry.TABLE_NAME + " (" + DealContract.LocationEntry._ID +"));";

    private final String SQL_CREATE_LOCATION_TABLE = "CREATE TABLE " +
            DealContract.LocationEntry.TABLE_NAME + " (" +
            DealContract.LocationEntry._ID + " INTEGER PRIMARY KEY,"+
            DealContract.LocationEntry.COLUMN_LOCATION_SETTING + "TEXT UNIQUE NOT NULL"+
            DealContract.LocationEntry.COLUMN_CITY_NAME     + "TEXT NOT NULL,"+
            DealContract.LocationEntry.COLUMN_COORD_LAT     + "REAL NOT NULL,"+
            DealContract.LocationEntry.COLUMN_COORD_LONG    + "REAL NOT NULL,";

    private final String[] projection = {
            DealContract.DealEntry._ID,
            DealContract.DealEntry.COLUMN_LOC_KEY,
            DealContract.DealEntry.COLUMN_BUSINESS_NAME,
            DealContract.DealEntry.COLUMN_SHORT_DESC,
            DealContract.DealEntry.COLUMN_LONG_DESC,
            DealContract.DealEntry.COLUMN_PRICE,
            DealContract.DealEntry.COLUMN_PHOTO_URI,
            DealContract.DealEntry.COLUMN_VOUCHER_CODE,
    };

    //----------------------------------------------------------------------------------------------
    // Constructors and creators.
    //----------------------------------------------------------------------------------------------
    /**
     * This class uses the singleton design pattern as there is no need for another instance of this
     * class.
     * @param context
     * @return ContactsDatabaseHelper
     */
    public static synchronized DealDbHelper getHelper(Context context){
        if (instance == null){
            instance = new DealDbHelper(context);
        }
        return instance;
    }

    //Private constructor to ensure a new instance cannot be made by anything other than this class.
    private DealDbHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL(SQL_CREATE_DEAL_TABLE);
        //db.execSQL(SQL_CREATE_LOCATION_TABLE);
    }

    //Used when changing the database. Adding columns etc.
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL("DROP TABLE IF EXISTS " + DealContract.LocationEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DealContract.DealEntry.TABLE_NAME);
        onCreate(db);
    }

    //----------------------------------------------------------------------------------------------
    // Methods for interacting with the database.
    //----------------------------------------------------------------------------------------------
    /**
     * This method gets a cursor which contains all the information in the database.
     * @return Cursor
     */
    public Cursor getAllData(){
        SQLiteDatabase db = this.getReadableDatabase();

        String sortOrder = DealContract.DealEntry._ID + " DESC";

        Cursor c = db.query(DealContract.DealEntry.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                sortOrder);

        return c;
    }

    public Deal getDealWithID(long id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("select * from " +
                DealContract.DealEntry.TABLE_NAME + " where " +
                DealContract.DealEntry._ID + "='" + id + "'" , null);

        c.moveToFirst();
        String businessName;    String shortDesc;   String longDesc     =   null;
        double price;           String photoURI;    String voucherCode  =   null;
        int locationKey     =   -1;

        businessName    = c.getString(c.getColumnIndex(DealContract.DealEntry.COLUMN_BUSINESS_NAME));
        shortDesc       = c.getString(c.getColumnIndex(DealContract.DealEntry.COLUMN_SHORT_DESC));
        longDesc        = c.getString(c.getColumnIndex(DealContract.DealEntry.COLUMN_LONG_DESC));
        price           = c.getDouble(c.getColumnIndex(DealContract.DealEntry.COLUMN_PRICE));
        photoURI        = c.getString(c.getColumnIndex(DealContract.DealEntry.COLUMN_PHOTO_URI));
        voucherCode     = c.getString(c.getColumnIndex(DealContract.DealEntry.COLUMN_VOUCHER_CODE));

        Deal deal = new Deal(businessName,shortDesc,longDesc,price,photoURI,voucherCode,
                locationKey);

        return deal;
    }

    /**
     * Returns the id of the row just inserted.
     *
     * @return Long
     * @param deal
     */
    public long insertData (Deal deal){
        ContentValues contentValues = getContentValues(deal);
        SQLiteDatabase db = this.getWritableDatabase();

        long newRowId;
        newRowId = db.insert(DealContract.DealEntry.TABLE_NAME, null, contentValues);
        db.close();

        return newRowId;
    }

    public void updateData(Deal deal){
        ContentValues contentValues = getContentValues(deal);
        SQLiteDatabase db = this.getWritableDatabase();
        db.update(DealContract.DealEntry.TABLE_NAME, contentValues,
                DealContract.DealEntry._ID +"="+ deal.getID(), null);
        db.close();
    }

    public void deleteContact(Deal deal){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(DealContract.DealEntry.TABLE_NAME,  DealContract.DealEntry._ID +"="+
                deal.getID(), null);
        db.close();
    }

    public void deleteAllData(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM  " + DealContract.DealEntry.TABLE_NAME);
        db.close();
    }

    //----------------------------------------------------------------------------------------------
    // Helper Method - GetContentValues
    //----------------------------------------------------------------------------------------------
    /**
     * Helper method to get the contentvalues for a deal so that it can then
     * be saved into the database.
     * @param deal
     * @return ContentValues
     */
    private ContentValues getContentValues(Deal deal){

        ContentValues contentValues = new ContentValues();
        contentValues.put(DealContract.DealEntry.COLUMN_LOC_KEY,        deal.getLocationKey());
        contentValues.put(DealContract.DealEntry.COLUMN_BUSINESS_NAME, deal.getBusinessName());
        contentValues.put(DealContract.DealEntry.COLUMN_SHORT_DESC ,    deal.getShortDesc());
        contentValues.put(DealContract.DealEntry.COLUMN_LONG_DESC ,     deal.getLongDesc());
        contentValues.put(DealContract.DealEntry.COLUMN_PRICE ,         deal.getPrice());
        contentValues.put(DealContract.DealEntry.COLUMN_PHOTO_URI ,     deal.getPhotoURL());
        contentValues.put(DealContract.DealEntry.COLUMN_VOUCHER_CODE ,  deal.getVoucherCode());
        return contentValues;
    }
}
