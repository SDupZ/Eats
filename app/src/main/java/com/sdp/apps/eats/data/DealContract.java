package com.sdp.apps.eats.data;

import android.provider.BaseColumns;

/**
 * Created by Simon on 17/02/2015.
 */
public class DealContract {

    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    public DealContract(){}

    /*
        Inner class that defines the table contents of the location table
     */
    public static final class LocationEntry implements BaseColumns {
        public static final String TABLE_NAME               =   "location";
        public static final String COLUMN_LOCATION_SETTING  =   "location_setting";
        public static final String COLUMN_CITY_NAME         =   "city_name";
        public static final String COLUMN_COORD_LAT         =   "coord_lat";
        public static final String COLUMN_COORD_LONG        =   "coord_long";
    }

    /* Inner class that defines the table contents of the deal table */
    public static final class DealEntry implements BaseColumns {

        public static final String TABLE_NAME               =   "deal";

        //public static final String _ID                      =   "deal_id";
        public static final String COLUMN_LOC_KEY           =   "location_id";
        public static final String COLUMN_BUSINESS_NAME     =   "business_name";
        public static final String COLUMN_SHORT_DESC        =   "short_description";
        public static final String COLUMN_LONG_DESC         =   "long_description";
        public static final String COLUMN_PRICE             =   "price";
        public static final String COLUMN_PHOTO_URI         =   "photo_uri";
        public static final String COLUMN_VOUCHER_CODE      =   "voucher_code";
        public static final String COLUMN_ABOUT_PLACE       =   "about_place";
        public static final String COLUMN_LAT               =   "lat";
        public static final String COLUMN_LONGI             =   "long";
        public static final String COLUMN_ADDRESS           =   "address";
        public static final String COLUMN_PHONE_NUMBER      =   "phone_number";
    }

}
