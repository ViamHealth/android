package com.viamhealth.android.activities;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Display;

import com.actionbarsherlock.app.SherlockActivity;
import com.google.analytics.tracking.android.EasyTracker;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.viamhealth.android.ViamHealthPrefs;
import com.viamhealth.android.utils.LogUtils;

/**
 * @author Sergey Tarasevich (nostra13[at]gmail[dot]com)
 */
public abstract class BaseActivity extends SherlockActivity {

    protected final String TAG = LogUtils.makeLogTag(getClass());

    Display display;
    int height, width;
    protected int w15, w20, h10, w10, w5, h40, h5, h8, h20;

    protected ImageLoader imageLoader = ImageLoader.getInstance();

    private boolean instanceStateSaved;
    protected ViamHealthPrefs appPrefs;

    @Override
    public void onSaveInstanceState(Bundle outState) {
        instanceStateSaved = true;
    }

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

    @Override
    protected void onDestroy() {
        if (!instanceStateSaved) {
            imageLoader.stop();
        }
        super.onDestroy();
    }

    protected void ScreenDimension() {
        display = getWindowManager().getDefaultDisplay();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        width = display.getWidth();
        height = display.getHeight();

        appPrefs.setSwidth(String.valueOf(width));
        appPrefs.setSheight(String.valueOf(height));

        //calculate dynamic padding
        w15 = (int) ((width * 4.68) / 100);
        w20 = (int) ((width * 6.25) / 100);
        w10 = (int) ((width * 3.13) / 100);
        w5 = (int) ((width * 1.56) / 100);

        h10 = (int) ((height * 2.08) / 100);
        h40 = (int) ((height * 8.33) / 100);
        h5 = (int) ((height * 1.042) / 100);
        h8 = (int) ((height * 1.67) / 100);
        h20 = (int) ((height * 4.17) / 100);
    }


}
