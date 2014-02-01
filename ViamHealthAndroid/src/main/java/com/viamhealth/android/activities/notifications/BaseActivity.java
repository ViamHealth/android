package com.viamhealth.android.activities.notifications;

import android.app.Activity;

import com.google.analytics.tracking.android.EasyTracker;

/**
 * Created by kunal on 30/1/14.
 */
public class BaseActivity extends Activity {
    @Override
    protected void onStart() {
        super.onStart();
        EasyTracker.getInstance(this).activityStart(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EasyTracker.getInstance(this).activityStop(this);
    }
}
