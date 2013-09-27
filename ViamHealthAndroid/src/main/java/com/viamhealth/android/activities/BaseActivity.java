package com.viamhealth.android.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * @author Sergey Tarasevich (nostra13[at]gmail[dot]com)
 */
public abstract class BaseActivity extends Activity {

    protected ImageLoader imageLoader = ImageLoader.getInstance();

    private boolean instanceStateSaved;

    @Override
    public void onSaveInstanceState(Bundle outState) {
        instanceStateSaved = true;
    }

    @Override
    protected void onDestroy() {
        if (!instanceStateSaved) {
            imageLoader.stop();
        }
        super.onDestroy();
    }
}
