package com.sdp.apps.eats.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.sdp.apps.eats.R;
import com.sdp.apps.eats.data.DealDbHelper;

public class SplashActivity extends Activity {

    private final int SPLASH_DISPLAY_LENGTH = 1500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(!ImageLoader.getInstance().isInited()) {
            ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this).build();
            ImageLoader.getInstance().init(config);
        }

        TextView splashLogo = (TextView) findViewById(R.id.splash_eats_logo);
        Typeface font = Typeface.createFromAsset(getAssets(), "Ubuntu-M.ttf");
        splashLogo.setTypeface(font);

        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                findViewById(R.id.splash_progress_bar).setVisibility(View.INVISIBLE);
                Intent mainIntent = new Intent(SplashActivity.this, DealListActivity.class);
                SplashActivity.this.startActivity(mainIntent);
                SplashActivity.this.finish();
            }
        }, SPLASH_DISPLAY_LENGTH);

        //Create the database here:
        DealDbHelper.getHelper(this);
    }
}
