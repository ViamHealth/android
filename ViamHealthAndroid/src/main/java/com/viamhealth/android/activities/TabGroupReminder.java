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
	    startChildActivity("ReminderPage", new Intent(this,Watch.class));
	}
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		//super.onBackPressed();
	}
}
