package com.viamhealth.android.activities;

import android.os.Bundle;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.view.Window;

import com.viamhealth.android.activities.Settings;
import com.viamhealth.android.activities.TabGroupActivity;

public class TabGroupSettings extends TabGroupActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
	    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	    startChildActivity("SettingsPage", new Intent(this,Settings.class));
	}
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		//super.onBackPressed();
	}
}
