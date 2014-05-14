package com.viamhealth.android.activities.fragments;

import android.os.Bundle;

import com.actionbarsherlock.app.SherlockListFragment;
import com.google.analytics.tracking.android.Fields;
import com.google.analytics.tracking.android.GoogleAnalytics;
import com.google.analytics.tracking.android.MapBuilder;
import com.google.analytics.tracking.android.Tracker;
import com.viamhealth.android.R;

/**
 * Created by naren on 25/11/13.
 */
public class BaseListFragment extends SherlockListFragment {

    private Tracker mTracker;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTracker = GoogleAnalytics.getInstance(getSherlockActivity()).getTracker(getString(R.string.ga_trackingId));
    }

    @Override
    public void onResume() {
        super.onResume();
        mTracker.set(Fields.SCREEN_NAME, getClass().getSimpleName());
        mTracker.send(MapBuilder.createAppView().build());
    }

}
