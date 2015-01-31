package com.sdp.apps.eats;

import android.graphics.Bitmap;

/**
 * Created by Simon on 12/01/2015.
 */
public class Deal {
    private String dealName;        //Not currently used.
    private String businessName;
    private String description;
    private double price;
    private Bitmap photo;
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

    public void setBusinessName(String name) {
        this.businessName = name;
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

    public Bitmap getPhoto() {
        return photo;
    }

    public void setPhoto(Bitmap photo){ this.photo = photo; }


}
