package com.sdp.apps.eats.activities;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.sdp.apps.eats.R;
import com.sdp.apps.eats.util.ObservableScrollable;
import com.sdp.apps.eats.util.OnScrollChangedCallback;
import com.sdp.apps.eats.util.SystemBarTintManager;


public class DetailActivity extends ActionBarActivity implements OnScrollChangedCallback {

    private Toolbar toolbar;
    private Drawable mActionBarBackgroundDrawable;
    private View mHeader;
    private int mLastDampedScroll;
    private int mInitialStatusBarColor;
    private int mFinalStatusBarColor;
    private SystemBarTintManager mStatusBarManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        toolbar.setNavigationIcon(R.drawable.ic_action_back);

        //int sdk = android.os.Build.VERSION.SDK_INT;

        toolbar.setBackgroundColor(getResources().getColor(R.color.ColorPrimary));

        /*if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
            //toolbar.setBackgroundDrawable(getResources().getDrawable(R.drawable.toolbar_trans_grad));
        } else {
            //toolbar.setBackground(getResources().getDrawable(R.drawable.toolbar_trans_grad));
        }*/

        mActionBarBackgroundDrawable = toolbar.getBackground();
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("");

        mStatusBarManager = new SystemBarTintManager(this);
        mStatusBarManager.setStatusBarTintEnabled(true);
        mInitialStatusBarColor = Color.BLACK;
        mFinalStatusBarColor = getResources().getColor(R.color.ColorPrimaryDark);

        mStatusBarManager.setTintColor(getResources().getColor(R.color.black));
        mHeader = findViewById(R.id.detail_fragment).findViewById(R.id.detail_view_image);

        ObservableScrollable scrollView = (ObservableScrollable) findViewById(R.id.scrollview);
        scrollView.setOnScrollChangedCallback(this);

        onScroll(-1, 0);
    }

    @Override
    public void onScroll(int l, int scrollPosition) {
        int headerHeight = mHeader.getHeight() - toolbar.getHeight();
        float ratio = 0;
        if (scrollPosition > 0 && headerHeight > 0)
            ratio = (float) Math.min(Math.max(scrollPosition, 0), headerHeight) / headerHeight;

        updateActionBarTransparency(ratio);
        //updateStatusBarColor(ratio);
        updateParallaxEffect(scrollPosition);
    }

    private void updateParallaxEffect(int scrollPosition) {
        float damping = 0.5f;
        int dampedScroll = (int) (scrollPosition * damping);
        int offset = mLastDampedScroll - dampedScroll;
        mHeader.offsetTopAndBottom(-offset);

        mLastDampedScroll = dampedScroll;
    }

    private void updateActionBarTransparency(float scrollRatio) {
        scrollRatio = (scrollRatio - 0.9f) * 10;
        scrollRatio = scrollRatio < 0 ? 0 : scrollRatio;
        int newAlpha = (int) (scrollRatio * 255);

        mActionBarBackgroundDrawable.setAlpha(newAlpha);

        int sdk = android.os.Build.VERSION.SDK_INT;
        if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
            toolbar.setBackgroundDrawable(mActionBarBackgroundDrawable);
        } else {
            toolbar.setBackground(mActionBarBackgroundDrawable);
        }

    }

    private void updateStatusBarColor(float scrollRatio) {
        int r = interpolate(Color.red(mInitialStatusBarColor), Color.red(mFinalStatusBarColor), 0);
        int g = interpolate(Color.green(mInitialStatusBarColor), Color.green(mFinalStatusBarColor), 0);
        int b = interpolate(Color.blue(mInitialStatusBarColor), Color.blue(mFinalStatusBarColor), 0);

        mStatusBarManager.setTintColor(Color.rgb(0, 0, 0));
    }

    private int interpolate(int from, int to, float param) {
        return (int) (from * param + to * (1 - param));
    }
}
