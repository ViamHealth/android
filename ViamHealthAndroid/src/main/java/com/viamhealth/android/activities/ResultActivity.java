package com.viamhealth.android.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.viamhealth.android.R;

/**
 * This Activity is displayed when users click the notification itself. It provides
 * UI for snoozing and dismissing the notification.
 */
public class ResultActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        String message = getIntent().getStringExtra(CommonConstants.EXTRA_MESSAGE);
        TextView text = (TextView) findViewById(R.id.result_message);
        text.setText(message);
    }

    public void onSnoozeClick(View v) {
        Intent intent = new Intent(getApplicationContext(), PingService.class);
        intent.setAction(CommonConstants.ACTION_SNOOZE);
        startService(intent);
    }

    public void onDismissClick(View v) {
        Intent intent = new Intent(getApplicationContext(), PingService.class);
        intent.setAction(CommonConstants.ACTION_DISMISS);
        startService(intent);
    }
}

