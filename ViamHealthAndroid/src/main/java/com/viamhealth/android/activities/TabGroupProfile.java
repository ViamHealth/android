package com.viamhealth.android.activities;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Window;

import com.viamhealth.android.activities.AddProfile;
import com.viamhealth.android.activities.TabGroupActivity;

public class TabGroupProfile extends TabGroupActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
	    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	    startChildActivity("ProfilePage", new Intent(this,AddProfile.class));
	}
	/*@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		Intent i = new Intent(TabGroupProfile.this,Home.class);
		startActivity(i);
	}*/
}
