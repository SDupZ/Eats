package com.sdp.apps.eats;

/**
 * Created by Simon on 12/01/2015.
 */
public class Deal implements Comparable{
    private long id;
    private String businessName;
    private String shortDesc;
    private String longDesc;
    private double price;
    private String photoURL;
    private String voucherCode;
    private int locationKey;
    private String aboutPlace;
    private double lat;
    private double longi;
    private String address;
    private String phoneNumber;

    public Deal(String businessName,
                String shortDesc,
                String longDesc,
                double price,
                String photoURL,
                String voucherCode,
                int locationKey,
                String aboutPlace,
                double lat,
                double longi,
                String address,
                String phoneNumber){
        this.businessName   = businessName;
        this.shortDesc      = shortDesc;
        this.longDesc       = longDesc;
        this.price          = price;
        this.photoURL       = photoURL;
        this.voucherCode    = voucherCode;
        this.locationKey    = locationKey;
        this.aboutPlace     = aboutPlace;
        this.lat            = lat;
        this.longi          = longi;
        this.address        = address;
        this.phoneNumber    = phoneNumber;
    }

    public String getBusinessName() {
        return businessName;
    }

    public String getPrice() {
        if(price == (long) price)
            return String.format("%d",(long)price);
        else
            return String.format("%.2f",price);
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getPhotoURL() {
        return photoURL;
    }

    public void setPhotoURL(String photoURL){ this.photoURL = photoURL; }

    public String getShortDesc() {return shortDesc;}

    public String getLongDesc() {return longDesc;}

    public String getVoucherCode() {return voucherCode;}

    public int getLocationKey() {return locationKey;}

    public long getID(){ return id; }

    public void setID(long id){this.id = id;}

    public String getAboutPlace() {return aboutPlace;}

    public void setAboutPlace(String aboutPlace) {this.aboutPlace = aboutPlace;}

    public double getLat() {return lat;}

    public void setLat(double lat) {this.lat = lat;}

    public double getLongi() {return longi;}

    public void setLongi(double longi) {this.longi = longi;}

    public String getAddress() {return address;}

    public void setAddress(String address) {this.address = address;}

    public String getPhoneNumber() { return phoneNumber; }

    public void setPhoneNumber(String phoneNumber) {this.phoneNumber = phoneNumber;}

    @Override
    public int compareTo(Object arg0){
        Deal other = (Deal) arg0;

        if (this.getPrice().equals(other.getPrice())){
            return 0;
        }else if (Double.parseDouble(this.getPrice()) < Double.parseDouble(other.getPrice())){
            return -1;
        }else {
            return 1;
        }
    }

}
