package com.sdp.apps.eats.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.sdp.apps.eats.R;
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

    private final int SPLASH_DISPLAY_LENGTH = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(!ImageLoader.getInstance().isInited()) {
            ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this).build();
            ImageLoader.getInstance().init(config);
        }

        ImageView splashLogo = (ImageView) findViewById(R.id.splash_eats_logo);
        splashLogo.getDrawable().setColorFilter(getResources().getColor(R.color.color_splash_logo_filter)
                , PorterDuff.Mode.MULTIPLY);

        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                findViewById(R.id.splash_progress_bar).setVisibility(View.INVISIBLE);
                Intent mainIntent = new Intent(MainActivity.this, DealListActivity.class);
                MainActivity.this.startActivity(mainIntent);
                MainActivity.this.finish();
            }
        }, SPLASH_DISPLAY_LENGTH);

        //Create the database here:
        DealDbHelper.getHelper(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
}
