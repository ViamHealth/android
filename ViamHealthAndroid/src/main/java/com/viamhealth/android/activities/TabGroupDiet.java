package com.viamhealth.android.activities;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

import com.viamhealth.android.activities.FoodDiary;
import com.viamhealth.android.activities.TabGroupActivity;

public class TabGroupDiet extends TabGroupActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		 setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	     startChildActivity("DietPage", new Intent(this,FoodDiary.class));
	}

	
	
}
