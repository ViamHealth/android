package com.viamhealth.android.activities;

import android.app.Activity;
import android.app.TabActivity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;   
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.TabHost.TabSpec;

import com.viamhealth.android.R;
import com.viamhealth.android.ViamHealthPrefs;

public class MainActivity extends TabActivity 
{
    /** Called when the activity is first created. */
	       
     Resources res;
     TabHost tabHost;
     TabSpec obj,obj1;
     Activity hehehe;
     Intent intent;
     ViamHealthPrefs appPrefs;

    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.tablayout);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        appPrefs = new ViamHealthPrefs(MainActivity.this);
        setTabs();     
    }    
                                                    
                          
    private void setTabs()    
	{    
		addTab("GOALS", R.drawable.tab_goal, TabGroupGoal.class);
		addTab("JOURNAL", R.drawable.tab_journal, TabGroupDiet.class);
		addTab("REMINDERS", R.drawable.tab_reminders, TabGroupReminder.class);
		addTab("FILES", R.drawable.tab_file, TabGroupFiles.class);
		
		//addTab("PROFILE", R.drawable.tab_profile, TabGroupProfile.class);
		//addTab("SETTING", R.drawable.tab_home, Home.class);   
		TabHost tabHost = getTabHost();  
		//tabHost.setCurrentTab(4);
		tabHost.getTabWidget().setDividerDrawable(null);
	}        
	     
	private void addTab(String labelId, int drawableId, Class<?> c)
	{
		TabHost tabHost = getTabHost();  
		Intent intent = new Intent(this, c);
		TabHost.TabSpec spec = tabHost.newTabSpec("tab" + labelId);	
		
		View tabIndicator = LayoutInflater.from(this).inflate(R.layout.tab_indicator, getTabWidget(), false);
		TextView title = (TextView) tabIndicator.findViewById(R.id.title);
		title.setText(labelId);
		ImageView icon = (ImageView) tabIndicator.findViewById(R.id.icon);
		icon.setImageResource(drawableId);
		
		spec.setIndicator(tabIndicator); 
		spec.setContent(intent);
		
		tabHost.addTab(spec);
		
	}
    
}   