package com.viamhealth.android.activities;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Window;

import com.viamhealth.android.Global_Application;
import com.viamhealth.android.R;
import com.viamhealth.android.ViamHealthPrefs;
import com.viamhealth.android.dao.db.DataBaseAdapter;
import com.viamhealth.android.dao.restclient.old.functionClass;

/**
 * Created by naren on 31/10/13.
 */
public class AddReminder extends Activity {

    ViamHealthPrefs appPrefs;
    Global_Application ga;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setContentView(R.layout.activity_add_reminder);

        appPrefs = new ViamHealthPrefs(AddReminder.this);
        ga=((Global_Application)getApplicationContext());

    }
}
