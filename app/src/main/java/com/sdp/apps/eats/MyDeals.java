package com.sdp.apps.eats;

import android.content.Context;

import com.sdp.apps.eats.data.DealDbHelper;

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
    }

    public void updateDealsList(List<Deal> myDeals){
        this.myDeals = myDeals;
    }

    /**
     * Getter method to retrieve all contacts.
     * @return
     */
    public List<Deal> getDealsList(){
        return myDeals;
    }

    public Deal getDealWithId(long id, Context context){
        for (Deal deal: myDeals){
            if (deal.getID() == id){
                return deal;
            }
        }
        return DealDbHelper.getHelper(context).getDealWithID(id);
    }
}
