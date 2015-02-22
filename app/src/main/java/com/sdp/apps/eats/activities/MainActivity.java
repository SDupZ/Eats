package com.sdp.apps.eats.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.sdp.apps.eats.R;
import com.sdp.apps.eats.data.ContentDownloader;
import com.sdp.apps.eats.data.DealDbHelper;

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


public class MainActivity extends Activity {

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

        if(!ImageLoader.getInstance().isInited()) {
            ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this).build();
            ImageLoader.getInstance().init(config);
        }

        //Create the database here:
        DealDbHelper.getHelper(this);

        //Start downloading the data and save it to the database.
        ContentDownloader cd = new ContentDownloader(this);
        cd.updateDatabase();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
}
