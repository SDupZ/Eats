package com.sdp.apps.eats;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Simon on 12/01/2015.
 */
public class Deal implements Parcelable{
    private String businessName;
    private String description;
    private double price;
    private String photoURL;

    public Deal(String businessName, String description, double price, String photoURL){
        this.businessName = businessName;
        this.description = description;
        this.price = price;
        this.photoURL = photoURL;
    }

    public String getBusinessName() {
        return businessName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrice() {
        if(price == (long) price)
            return String.format("%d",(long)price);
        else
            return String.format("%s",price);
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getPhotoURL() {
        return photoURL;
    }

    public void setPhotoURL(String photoURL){ this.photoURL = photoURL; }

    //----------------------Parcelable Methods----------------------------

    public int describeContents(){return 0;}

    public void writeToParcel(Parcel out, int flags){
        out.writeString(businessName);
        out.writeString(description);
        out.writeDouble(price);
        out.writeString(photoURL);
    }

    // this is used to regenerate your object. All Parcelables must have a CREATOR that implements these two methods
    public static final Parcelable.Creator<Deal> CREATOR = new Parcelable.Creator<Deal>() {

        public Deal createFromParcel(Parcel in) {
            return new Deal(in);
        }

        public Deal[] newArray(int size) {
            return new Deal[size];
        }
    };

    private Deal(Parcel in) {
        businessName    = in.readString();
        description     = in.readString();
        price           = in.readDouble();
        photoURL        = in.readString();
    }
}
