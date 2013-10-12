package com.viamhealth.android.activities.oldones;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

public class TabGroupDiet extends TabGroupActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		 setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	     startChildActivity("DietPage", new Intent(this,FoodDiary.class));
	}

	
	
}
