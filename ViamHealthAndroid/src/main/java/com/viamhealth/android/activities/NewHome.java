package com.viamhealth.android.activities;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

import com.viamhealth.android.R;


public class NewHome extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_home);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.new_home, menu);
        return true;
    }
    
}
