package com.viamhealth.android.activities;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

import com.viamhealth.android.R;

public class NewProfile extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_profile);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.new_profile, menu);
        return true;
    }
    
}
