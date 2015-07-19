package com.sdp.apps.eats.activities;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.sdp.apps.eats.R;
import com.sdp.apps.eats.util.ObservableScrollable;
import com.sdp.apps.eats.util.OnScrollChangedCallback;
import com.sdp.apps.eats.util.SystemBarTintManager;


public class DetailActivity extends ActionBarActivity implements OnScrollChangedCallback {
    private static final int STATE_RETURNING = 0;
    private static final int STATE_ONSCREEN = 1;
    private static final int STATE_OFFSCREEN = 2;

    //Scroll buffer until quick return comes into effect
    private static final int SCROLL_BUFFER = 5;

    private Toolbar toolbar;
    private TextView toolbarTitle;
    private Drawable mActionBarBackgroundDrawable;
    private View mHeader;
    private int mInitialStatusBarColor;
    private int mFinalStatusBarColor;
    private SystemBarTintManager mStatusBarManager;
    private int statusBarHeight;

    private int state;
    private int previousScrollPosition;
    private int zeroPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        toolbar.setNavigationIcon(R.drawable.ic_action_back);
        toolbar.setBackgroundColor(getResources().getColor(R.color.ColorPrimary));

        mActionBarBackgroundDrawable = toolbar.getBackground();
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("");

        mStatusBarManager = new SystemBarTintManager(this);
        mStatusBarManager.setStatusBarTintEnabled(true);
        mInitialStatusBarColor = getResources().getColor(R.color.transparent);
        mFinalStatusBarColor = getResources().getColor(R.color.ColorPrimaryDark);
        mHeader = findViewById(R.id.detail_fragment).findViewById(R.id.detail_view_image);

        ObservableScrollable scrollView = (ObservableScrollable) findViewById(R.id.scrollview);
        scrollView.setOnScrollChangedCallback(this);
        onScroll(-1, 0);

        statusBarHeight = getStatusBarHeight();
        state = STATE_ONSCREEN;
        previousScrollPosition = 0;

        String title = ((TextView)findViewById(R.id.detail_fragment).findViewById(R.id.detail_view_short_desc)).getText().toString();
        toolbar.setTitle("");
        toolbarTitle = ((TextView)toolbar.findViewById(R.id.toolbar_title));
        toolbarTitle.setText(title);
        toolbarTitle.setAlpha(0);
    }

    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    @Override
    public void onScroll(int l, int scrollPosition) {
        int headerHeight;
        if (Build.VERSION.SDK_INT < 19){
            headerHeight = mHeader.getHeight() - toolbar.getHeight();
        }else{
            headerHeight = mHeader.getHeight() - (toolbar.getHeight() + statusBarHeight);
        }

        float ratio = 0;
        if (scrollPosition > 0 && headerHeight > 0)
            ratio = (float) Math.min(Math.max(scrollPosition, 0), headerHeight) / headerHeight;

        updateActionBarTransparency(ratio);
        updateStatusBarColor(ratio);
        updateParallaxEffect(scrollPosition);
        updateToolBarPosition(ratio, scrollPosition);

        previousScrollPosition = scrollPosition;
    }

    private void updateToolBarPosition(float scrollRatio, int scrollPosition) {
        Log.d("UNI EATS", "Scroll Ratio: " + scrollRatio + " ScrollPosition: " + scrollPosition + " " +
                " ToolbarHeight: " + toolbar.getHeight() + " Header Height: " + mHeader.getHeight() + " ActionBarHeight " +
                getSupportActionBar().getHeight() + " Status Bar Height: " + statusBarHeight + " STATE: " + state
                + " Zero Position: " + zeroPosition + " Toolbar Position: " + toolbar.getTranslationY());

        if (scrollRatio == 1.0){
            //Scrolling up
            if (scrollPosition < previousScrollPosition){
                Log.d("UNI EATS", "UP");
                if (state == STATE_OFFSCREEN){
                    zeroPosition = scrollPosition - toolbar.getHeight();
                    state = STATE_RETURNING;
                }
                if (zeroPosition - scrollPosition >= 0 ){
                    toolbar.setTranslationY(0);
                    state = STATE_ONSCREEN;
                }else{
                    toolbar.setTranslationY(zeroPosition - scrollPosition);
                }
            //Scrolling Down
            }else if (scrollPosition > previousScrollPosition){
                Log.d("UNI EATS", "DOWN");
                if (state == STATE_ONSCREEN) {
                    zeroPosition = scrollPosition;
                    state = STATE_RETURNING;
                }
                toolbar.setTranslationY(zeroPosition - scrollPosition);
                if (toolbar.getTranslationY() <= -toolbar.getHeight()){
                    state = STATE_OFFSCREEN;
                }
            }
        }else{
            toolbar.setTranslationY(0);
            state = STATE_ONSCREEN;
        }
    }

    private void updateParallaxEffect(int scrollPosition) {
        int parallaxRatio = 2;
        mHeader.setTranslationY(scrollPosition/parallaxRatio);
    }

    private void updateActionBarTransparency(float scrollRatio) {
        scrollRatio = (scrollRatio - 0.9f) * 10;
        scrollRatio = scrollRatio < 0 ? 0 : scrollRatio;
        int newAlpha = (int) (scrollRatio * 255);

        mActionBarBackgroundDrawable.setAlpha(newAlpha);
        if (toolbarTitle != null)
            toolbarTitle.setAlpha(newAlpha);


        int sdk = android.os.Build.VERSION.SDK_INT;
        if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
            toolbar.setBackgroundDrawable(mActionBarBackgroundDrawable);
        } else {
            toolbar.setBackground(mActionBarBackgroundDrawable);
        }

    }

    private void updateStatusBarColor(float scrollRatio) {
        scrollRatio = (scrollRatio - 0.9f) * 10;
        scrollRatio = scrollRatio < 0 ? 0 : scrollRatio;
        float tintRatio = scrollRatio;

        int r = interpolate(Color.red(mInitialStatusBarColor), Color.red(mFinalStatusBarColor), 1-tintRatio);
        int g = interpolate(Color.green(mInitialStatusBarColor), Color.green(mFinalStatusBarColor), 1-tintRatio);
        int b = interpolate(Color.blue(mInitialStatusBarColor), Color.blue(mFinalStatusBarColor), 1-tintRatio);
        mStatusBarManager.setTintAlpha(scrollRatio);
        mStatusBarManager.setTintColor(Color.rgb(r, g, b));
    }

    private int interpolate(int from, int to, float param) {
        return (int) (from * param + to * (1 - param));
    }
}
