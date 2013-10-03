package com.viamhealth.android.activities;

import android.os.Bundle;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.view.Window;

import com.viamhealth.android.activities.TabGroupActivity;
import com.viamhealth.android.activities.Watch;

public class TabGroupReminder extends TabGroupActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
	    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        String userid=getIntent().getStringExtra("user_id");
        Intent intent_watch=new Intent(this,Watch.class);
        intent_watch.putExtra("user_id",userid);
	    startChildActivity("ReminderPage",intent_watch);
	}
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		//super.onBackPressed();
	}
}
