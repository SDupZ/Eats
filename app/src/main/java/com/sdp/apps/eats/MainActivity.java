package com.sdp.apps.eats;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

/**
 * 	Main activity. First screen to be loaded when the app opens.
 *
 * 	Currently showing list of all deals. No distinction between price ranges. Also data
 * 	is dummmy. Need to transfer to google fusion.
 *
 * 2015/01/07
 * @author 	Simon du Preez
 * @version 0.1
 */


public class MainActivity extends ActionBarActivity {

    private static String DEBUG_TAG = "SDUPZ DEBUG";

    //----------------------------------------------------------------------------------------------
    // On Create for main activity.
    //----------------------------------------------------------------------------------------------
    /**
     * This is where the application loads. When the application is first started, the deals list
     * will need to be updated from the server.
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new MainFragment())
                    .commit();
        }
    }

    @Override
    protected void onStart(){
        super.onStart();
        Log.d(DEBUG_TAG, "onStart");
    }

    @Override
    protected void onResume(){
        super.onResume();
        Log.d(DEBUG_TAG, "onResume");
    }

    @Override
    protected void onPause(){
        super.onPause();
        Log.d(DEBUG_TAG, "onPause");
    }

    @Override
    protected void onStop(){
        super.onStop();
        Log.d(DEBUG_TAG, "onStop");
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        Log.d(DEBUG_TAG, "onDestroy");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
}
