package com.viamhealth.android.activities.fragments;

import android.os.Bundle;

import com.actionbarsherlock.app.SherlockFragment;
import com.google.analytics.tracking.android.Fields;
import com.google.analytics.tracking.android.GoogleAnalytics;
import com.google.analytics.tracking.android.MapBuilder;
import com.google.analytics.tracking.android.Tracker;
import com.viamhealth.android.R;

/**
 * Created by naren on 25/11/13.
 */
public class BaseFragment extends SherlockFragment {

    private Tracker mTracker;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTracker = GoogleAnalytics.getInstance(getSherlockActivity()).getTracker(getString(R.string.ga_trackingId));
    }

    public String getScreenName() {
        return getClass().getSimpleName();
    }

    @Override
    public void onResume() {
        super.onResume();
        mTracker.set(Fields.SCREEN_NAME, getScreenName());
        mTracker.send(MapBuilder.createAppView().build());
    }
}
