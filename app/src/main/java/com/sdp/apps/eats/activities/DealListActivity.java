package com.sdp.apps.eats.activities;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.sdp.apps.eats.R;
import com.sdp.apps.eats.data.ContentDownloader;
import com.sdp.apps.eats.data.DatabaseListener;
import com.sdp.apps.eats.fragments.DealsListFragment;
import com.sdp.apps.eats.util.SystemBarTintManager;

public class DealListActivity extends ActionBarActivity implements DatabaseListener{
    boolean updating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deal_list);

        if (Build.VERSION.SDK_INT < 19){
            findViewById(R.id.spacer).setVisibility(View.GONE);
        }else{
            findViewById(R.id.spacer).getLayoutParams().height = getStatusBarHeight();
        }

        SystemBarTintManager mStatusBarManager;
        mStatusBarManager = new SystemBarTintManager(this);
        mStatusBarManager.setStatusBarTintEnabled(true);
        mStatusBarManager.setTintColor(getResources().getColor(R.color.ColorPrimaryDark));
        updating = false;
     }

    // A method to find height of the status bar
    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public void databaseUpdated(boolean success){
        if (!success){
            Toast.makeText(
                    this,
                    getResources().getString(R.string.no_internet_toast),
                    Toast.LENGTH_SHORT
            ).show();
        }

        ((DealsListFragment)getSupportFragmentManager().findFragmentById(R.id.deal_list_fragment)).databaseUpdated(success);
        updating = false;
    }

    public synchronized void updateDatabase(){
        if (!updating) {
            updating = true;
            ContentDownloader cd = new ContentDownloader(this);
            cd.addDatabaseListener(this);
            cd.updateDatabase();
        }
    }
}
