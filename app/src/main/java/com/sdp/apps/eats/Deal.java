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

    public Deal(String businessName,
                String shortDesc,
                String longDesc,
                double price,
                String photoURL,
                String voucherCode,
                int locationKey){
        this.businessName   = businessName;
        this.shortDesc      = shortDesc;
        this.longDesc       = longDesc;
        this.price          = price;
        this.photoURL       = photoURL;
        this.voucherCode    = voucherCode;
        this.locationKey    = locationKey;
    }

    public String getBusinessName() {
        return businessName;
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

    public String getShortDesc() {return shortDesc;}

    public String getLongDesc() {return longDesc;}

    public String getVoucherCode() {return voucherCode;}

    public int getLocationKey() {return locationKey;}

    public long getID(){ return id; }

    public void setID(long id){this.id = id;}

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
