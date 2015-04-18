package com.sdp.apps.eats.activities;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.sdp.apps.eats.R;
import com.sdp.apps.eats.util.ObservableScrollable;
import com.sdp.apps.eats.util.OnScrollChangedCallback;


public class DetailActivity extends ActionBarActivity implements OnScrollChangedCallback {
    private Toolbar toolbar;
    private View mHeader;
    private int mLastDampedScroll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        toolbar.setNavigationIcon(R.drawable.ic_action_back);
        toolbar.setTitle("Title");
        int sdk = android.os.Build.VERSION.SDK_INT;
        if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
            toolbar.setBackgroundDrawable(getResources().getDrawable(R.drawable.toolbar_trans_grad));
        } else {
            toolbar.setBackground(getResources().getDrawable(R.drawable.toolbar_trans_grad));
        }
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mHeader = findViewById(R.id.detail_fragment).findViewById(R.id.detail_image);

        ObservableScrollable scrollView = (ObservableScrollable) findViewById(R.id.scrollview);
        scrollView.setOnScrollChangedCallback(this);

        onScroll(-1, 0);
    }

    @Override
    public void onScroll(int l, int scrollPosition) {
        updateParallaxEffect(scrollPosition);
    }

    private void updateParallaxEffect(int scrollPosition) {
        float damping = 0.5f;
        int dampedScroll = (int) (scrollPosition * damping);
        int offset = mLastDampedScroll - dampedScroll;
        mHeader.offsetTopAndBottom(-offset);

        mLastDampedScroll = dampedScroll;
    }
}
