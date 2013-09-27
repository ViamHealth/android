package com.viamhealth.android.activities;


import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Window;

import com.viamhealth.android.activities.Goal;
import com.viamhealth.android.activities.TabGroupActivity;


public class TabGroupGoal extends TabGroupActivity
	{    
	    @Override   
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
	        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	        startChildActivity("HomePage", new Intent(this,Goal.class));
	          
	    }
  
		@Override
		public void onBackPressed() {
			// TODO Auto-generated method stub
			//super.onBackPressed();
		}
	}
