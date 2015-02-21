package com.sdp.apps.eats;

import java.util.ArrayList;
import java.util.List;

/**
 * Singlton class to store the list of deals for the session. Enables all activities to have access
 * to the deal list without having to access the database. The GUI and lists can be updated quickley
 * while the database gets updated in the background.
 *
 * Created by Simon on 20/02/2015.
 */
public class MyDeals {

    private List<Deal> myDeals;
    private int priceFilter;

    private static MyDeals instance;

    //-----------------------------------------------------------------------------------------------
    // Constructors and Getting the Instance
    //----------------------------------------------------------------------------------------------
    /**
     * 	This method is invoked by any class wanting to get an instance of this class.
     * 	Only one instance of this class exists.
     * 	@return MyContacts
     */
    public static synchronized MyDeals getDeals(){
        if (instance == null){
            instance = new MyDeals();
        }
        return instance;
    }

    //Private constructor to ensure a new instance cannot be made by anything other than this class.
    private MyDeals(){
        myDeals = new ArrayList<Deal>();
        this.priceFilter = -1;
    }

    public int getPriceFilter(){return priceFilter;}

    public void setPriceFilter(int priceFilter){this.priceFilter = priceFilter;}

    public void updateDealsList(List<Deal> myDeals){
        this.myDeals = myDeals;
    }

    /**
     * Getter method to retrieve all contacts.
     * @return
     */
    public List<Deal> getDealsList(){
        List<Deal> tempList = new ArrayList<Deal>();

        for (Deal deal: myDeals){
            if (priceFilter == -1 || Double.parseDouble(deal.getPrice()) <= priceFilter){
                tempList.add(deal);
            }
        }
        return tempList;
    }

    public Deal getDealAtPosition(int position){

        if(position == -1){return null;}

        List<Deal> tempList = new ArrayList<Deal>();

        for (Deal deal: myDeals){
            if (priceFilter == -1 || Double.parseDouble(deal.getPrice()) <= priceFilter){
                tempList.add(deal);
            }
        }

        return tempList.get(position);
    }
}
