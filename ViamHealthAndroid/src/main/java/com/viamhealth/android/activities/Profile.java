package com.viamhealth.android.activities;

import java.util.Arrays;
import java.util.List;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.viamhealth.android.Global_Application;
import com.viamhealth.android.adapters.GoalDataAdapter;
import com.viamhealth.android.R;
import com.viamhealth.android.ViamHealthPrefs;

public class Profile extends Activity implements OnClickListener {
	Display display;
	int height,width;
	Typeface tf;
	int w15,w20,h10,w10,w5,h40,h5,h20;
	
	TextView heding_name_profile,lbl_invite_user_profile;
	LinearLayout menu_invite_profile,menu_invite_out_profile,settiglayout_profile,addprofile_layout,addgoal_layout,addhelth_layout,notification_layout,profile_layout;
	TextView btn_add_profile,btn_add_goal,btn_add_health;
	ViamHealthPrefs appPrefs;
	Global_Application ga;
	LinearLayout layoutNotification[] = new LinearLayout[6];
	ScrollView main_scroll;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);  
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		
		appPrefs = new ViamHealthPrefs(Profile.this);
		ga=((Global_Application)getApplicationContext());
		appPrefs.setGoalDisable("1");
		setContentView(R.layout.profile);
		
		tf = Typeface.createFromAsset(this.getAssets(),"Roboto-Condensed.ttf");
		//for get screen height width
        ScreenDimension();  
        
        //calculate dynamic padding
        w15=(int)((width*4.68)/100);
        w20=(int)((width*6.25)/100);
        w10=(int)((width*3.13)/100);
        w5=(int)((width*1.56)/100);
        
        h10=(int)((height*2.083)/100);
        h20=(int)((height*4.17)/100);
        h40=(int)((height*8.33)/100);
        
		//casting control and manage padding and call onclick method
        heding_name_profile=(TextView)findViewById(R.id.heding_name_profile);
        heding_name_profile.setText(appPrefs.getProfileName());
        heding_name_profile.setTypeface(tf);
        
        menu_invite_profile= (LinearLayout)findViewById(R.id.menu_invite_profile);
        menu_invite_profile.setPadding(w15, 0, w20, 0);
        menu_invite_profile.setOnClickListener(this);
        
        menu_invite_out_profile = (LinearLayout)findViewById(R.id.menu_invite_out_profile);
		menu_invite_out_profile.setOnClickListener(this);
		
		settiglayout_profile = (LinearLayout)findViewById(R.id.settiglayout_profile);
		settiglayout_profile.setPadding(0, h40, w5, 0);
		   
		 GradientDrawable gd = new GradientDrawable(
	                GradientDrawable.Orientation.TOP_BOTTOM,
	                new int[] {Color.rgb(172, 225, 245),Color.rgb(172, 225, 245)});
	        gd.setCornerRadius(10f);
	        
	    profile_layout = (LinearLayout)findViewById(R.id.profile_layout);
	    
		addprofile_layout=(LinearLayout)findViewById(R.id.addprofile_layout);
		addprofile_layout.setPadding(w10, h20, w10, 0);
		  
		btn_add_profile=(TextView)findViewById(R.id.btn_add_profile);
		btn_add_profile.setTypeface(tf);
		btn_add_profile.setPadding(w10, h5, 0, h5);
		btn_add_profile.setBackgroundDrawable(gd);
		btn_add_profile.setCompoundDrawablesWithIntrinsicBounds(R.drawable.addicon, 0, 0, 0);
		btn_add_profile.setOnClickListener(this);
		
		addgoal_layout=(LinearLayout)findViewById(R.id.addgoal_layout);
		addgoal_layout.setPadding(w10, h10, w10, 0);
		
		btn_add_goal=(TextView)findViewById(R.id.btn_add_goal);
		btn_add_goal.setTypeface(tf);
		btn_add_goal.setPadding(w10, h5, 0, h5);
		btn_add_goal.setBackgroundDrawable(gd);
		btn_add_goal.setCompoundDrawablesWithIntrinsicBounds(R.drawable.addicon, 0, 0, 0);
		btn_add_goal.setOnClickListener(this);
		
		addhelth_layout=(LinearLayout)findViewById(R.id.addhelth_layout);
		addhelth_layout.setPadding(w10, h10, w10, 0);
		
		btn_add_health=(TextView)findViewById(R.id.btn_add_health);
		btn_add_health.setTypeface(tf);
		btn_add_health.setPadding(w10, h5, 0, h5);
		btn_add_health.setBackgroundDrawable(gd);
		btn_add_health.setCompoundDrawablesWithIntrinsicBounds(R.drawable.addicon, 0, 0, 0);
		btn_add_health.setOnClickListener(this);
		
		lbl_invite_user_profile=(TextView)findViewById(R.id.lbl_invite_user_profile);
		lbl_invite_user_profile.setTypeface(tf);
		lbl_invite_user_profile.setOnClickListener(this);
		
		if(!appPrefs.getBtngoal_hide().toString().equals("0")){
			btn_add_goal.setVisibility(View.GONE);
		}
		if(!appPrefs.getBtnhealth_hide().toString().equals("0")){
			btn_add_health.setVisibility(View.GONE);
		}
		if(!appPrefs.getBtnprofile_hide().toString().equals("0")){
			btn_add_profile.setVisibility(View.GONE);
		}
		
		main_scroll= (ScrollView)findViewById(R.id.main_scroll);
		main_scroll.setPadding(0, h10, 0, 0);
		// for generate menu
	   actionmenu();
	   
	   // notification display
	   notification_layout = (LinearLayout)findViewById(R.id.notification_layout);
	   notification_layout.setPadding(w10, 0, w10, w10);
	   getNotification();
	   
	   if(ga.getInviteuser_flg()==1){
		   ga.setInviteuser_flg(0);
		   Intent intent = new Intent(Profile.this,InviteUser.class);
			startActivity(intent);
	   }
	}   
	public void getNotification(){
		 GradientDrawable gd = new GradientDrawable(
	                GradientDrawable.Orientation.TOP_BOTTOM,
	                new int[] {Color.rgb(172, 225, 245),Color.rgb(172, 225, 245)});
	        gd.setCornerRadius(10f);
		for(int i=0;i<3;i++){
			Log.e("TAG","Add Notification");
			LinearLayout temp = new LinearLayout(Profile.this);
			if(i>0){
				temp.setPadding(0, h10, 0, 0);
			}
			layoutNotification[i] = new LinearLayout(Profile.this);
			layoutNotification[i].setId(i);
			layoutNotification[i].setTag("notification");
			layoutNotification[i].setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
			layoutNotification[i].setOrientation(LinearLayout.VERTICAL);
			layoutNotification[i].setBackgroundDrawable(gd);
			layoutNotification[i].setOnClickListener(this);
			   
			if(i==0){
				TextView txttitle = new TextView(Profile.this);
				txttitle.setCompoundDrawablesWithIntrinsicBounds(R.drawable.exclamation, 0, 0, 0);
				txttitle.setText("  Diabetes");
				txttitle.setTextColor(Color.BLACK);
				txttitle.setGravity(Gravity.LEFT);
				txttitle.setTextSize(18);
				txttitle.setPadding(w15, 0, 0, 0);
				txttitle.setTypeface(null, Typeface.BOLD);
				layoutNotification[i].addView(txttitle);
				
				
				TextView txtdesc = new TextView(Profile.this);
				txtdesc.setText("Need to check yout fasting blood sugar(FBS) and Random Bolld Sugar(RBS)");
				txtdesc.setTextColor(Color.BLACK);
				txtdesc.setGravity(Gravity.LEFT);
				txtdesc.setPadding(w15, h10, 0, h10);
				layoutNotification[i].addView(txtdesc);
				
				temp.addView(layoutNotification[i]);
				notification_layout.addView(temp);
			}else if(i==1){
				TextView txttitle = new TextView(Profile.this);
				txttitle.setCompoundDrawablesWithIntrinsicBounds(R.drawable.right, 0, 0, 0);
				txttitle.setText("  Blood Pressure");
				txttitle.setTextColor(Color.BLACK);
				txttitle.setGravity(Gravity.LEFT);
				txttitle.setTextSize(18);
				txttitle.setPadding(w15, 0, 0, 0);
				txttitle.setTypeface(null, Typeface.BOLD);
				layoutNotification[i].addView(txttitle);
				
				
				TextView txtdesc = new TextView(Profile.this);
				txtdesc.setText("Blood Pressure is normal. Greate going buddy");
				txtdesc.setTextColor(Color.BLACK);
				txtdesc.setGravity(Gravity.LEFT);
				txtdesc.setPadding(w15, h10, 0, h10);
				layoutNotification[i].addView(txtdesc);
				
				temp.addView(layoutNotification[i]);
				notification_layout.addView(temp);
			}else if(i==2){
				TextView txttitle = new TextView(Profile.this);
				txttitle.setCompoundDrawablesWithIntrinsicBounds(R.drawable.exclamation, 0, 0, 0);
				txttitle.setText("  Reminder for Health Checkup");
				txttitle.setTextColor(Color.BLACK);
				txttitle.setGravity(Gravity.LEFT);
				txttitle.setTextSize(18);
				txttitle.setPadding(w15, 0, 0, 0);
				txttitle.setTypeface(null, Typeface.BOLD);
				layoutNotification[i].addView(txttitle);
				
				
				TextView txtdesc = new TextView(Profile.this);
				txtdesc.setText("Please take and appointment for your Trigleceride Test Tomorrow");
				txtdesc.setTextColor(Color.BLACK);
				txtdesc.setGravity(Gravity.LEFT);
				txtdesc.setPadding(w15, h10, 0, h10);
				layoutNotification[i].addView(txtdesc);
				
				temp.addView(layoutNotification[i]);
				notification_layout.addView(temp);
			}
		}
	}
	public void actionmenu(){
		// for generate menu
		 final List<String> Goal_data;
		 Goal_data =Arrays.asList(appPrefs.getMenuList().toString().split("\\s*,\\s*"));
		 final GoalDataAdapter adapter = new GoalDataAdapter(this,R.layout.listview_item_row, Goal_data);
		        
		final ListView listView1 = (ListView)findViewById(R.id.listView1);
		listView1.setAdapter(adapter);
	
		listView1.setOnItemClickListener(new OnItemClickListener() {
		    	
		    	@Override
				public void onItemClick(AdapterView<?> arg0, View view,
						int position, long arg3) {
					// TODO Auto-generated method stub
		    		String value = ((TextView)view.findViewById(R.id.txtName)).getText().toString();
		    		/*((ImageView)view.findViewById(R.id.imgIcon)).setImageResource(R.drawable.tick);
					Log.e("TAG","Selected value is " + value);*/
		    	    
		    		Log.e("TAG","Selected value is " + value);
		    		appPrefs.setProfileName(value);
		    		 heding_name_profile.setText(appPrefs.getProfileName());
		    		for(int i=0;i<Goal_data.size();i++){
						if(value.toString().equals(appPrefs.getProfileName().toString())){
							((ImageView)view.findViewById(R.id.imgIcon)).setVisibility(View.VISIBLE);
						}else{
							((ImageView)view.findViewById(R.id.imgIcon)).setVisibility(View.INVISIBLE);
						}
				}
		    		Animation anim = AnimationUtils.loadAnimation(Profile.this, R.anim.fade_out);
					settiglayout_profile.startAnimation(anim);
					settiglayout_profile.setVisibility(View.INVISIBLE);
					menu_invite_profile.setVisibility(View.VISIBLE);
					menu_invite_out_profile.setVisibility(View.INVISIBLE);
				}
			
		       });
	}
	
	 public void ScreenDimension()
		{
			display = getWindowManager().getDefaultDisplay(); 
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
			width = display.getWidth();
			height = display.getHeight();

		}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
		if(v==btn_add_profile){
			appPrefs.setBtnprofile_hide("1");
			Intent AddProfile = new Intent(getParent(), com.viamhealth.android.activities.AddProfile.class);
			TabGroupActivity parentoption = (TabGroupActivity)getParent();
			parentoption.startChildActivity("AddProfile",AddProfile);
		}
		if(v==btn_add_goal){
			appPrefs.setBtngoal_hide("1");
		     final TabHost tabHost = (TabHost) getParent().getParent().findViewById(android.R.id.tabhost);
	         tabHost.setOnTabChangedListener(new  TabHost.OnTabChangeListener() {
				
				@Override
				public void onTabChanged(String tabId) {
					// TODO Auto-generated method stub
					 final int pos = tabHost.getCurrentTab();
	                    final View tabView = tabHost.getTabWidget().getChildTabViewAt(pos);
	                    final int[] locationOnScreen= new int[2];
	                    tabView.getLocationOnScreen(locationOnScreen);
	                   
				}
			});

	              
	       tabHost.setCurrentTab(0);
		   Intent i = new Intent(Profile.this,AddWeightGoal.class);
	       startActivity(i);
		}
		if(v==btn_add_health){
			appPrefs.setBtnhealth_hide("1");
			 final TabHost tabHost = (TabHost) getParent().getParent().findViewById(android.R.id.tabhost);
	         tabHost.setOnTabChangedListener(new  TabHost.OnTabChangeListener() {
				
				@Override
				public void onTabChanged(String tabId) {
					// TODO Auto-generated method stub
					 final int pos = tabHost.getCurrentTab();
	                    final View tabView = tabHost.getTabWidget().getChildTabViewAt(pos);
	                    final int[] locationOnScreen= new int[2];
	                    tabView.getLocationOnScreen(locationOnScreen);
	                   
				}
			});

	              
	       tabHost.setCurrentTab(2);
		 /*  Intent i = new Intent(Profile.this,MainActivity.class);
	       startActivity(i);*/
		}
		
		if(v==lbl_invite_user_profile){
			Log.e("TAG","Selected value is " + "invite user is clicked");
			Animation anim = AnimationUtils.loadAnimation(Profile.this, R.anim.fade_out);
			settiglayout_profile.startAnimation(anim);
			settiglayout_profile.setVisibility(View.INVISIBLE);
			menu_invite_profile.setVisibility(View.VISIBLE);
			menu_invite_out_profile.setVisibility(View.INVISIBLE);
			Log.e("TAG","Clicked");
			Intent i = new Intent(Profile.this,InviteUser.class);
			startActivity(i);
		}
		if(v==menu_invite_profile){
			actionmenu();
			settiglayout_profile.setVisibility(View.VISIBLE);
			menu_invite_out_profile.setVisibility(View.VISIBLE);
			menu_invite_profile.setVisibility(View.INVISIBLE);
			Animation anim = AnimationUtils.loadAnimation(Profile.this, R.anim.fade_in);
			settiglayout_profile.startAnimation(anim);
			
			Log.e("TAG","Clicked");
		}
		if(v==menu_invite_out_profile){
			Animation anim = AnimationUtils.loadAnimation(Profile.this, R.anim.fade_out);
			settiglayout_profile.startAnimation(anim);
			settiglayout_profile.setVisibility(View.INVISIBLE);
			menu_invite_profile.setVisibility(View.VISIBLE);
			menu_invite_out_profile.setVisibility(View.INVISIBLE);
			Log.e("TAG","Clicked");
		}
		if(v.getId()==0 || v.getId()==1){
				Log.e("TAG","Layout Clicked");
				 final TabHost tabHost = (TabHost) getParent().getParent().findViewById(android.R.id.tabhost);
		         tabHost.setOnTabChangedListener(new  TabHost.OnTabChangeListener() {
					
					@Override     
					public void onTabChanged(String tabId) {
						// TODO Auto-generated method stub
						 final int pos = tabHost.getCurrentTab();
		                    final View tabView = tabHost.getTabWidget().getChildTabViewAt(pos);
		                    final int[] locationOnScreen= new int[2];
		                    tabView.getLocationOnScreen(locationOnScreen);
		                   
					}
				});
		         tabHost.setCurrentTab(0);
		}
		if(v.getId()==2){
			Log.e("TAG","Layout Clicked");
			 final TabHost tabHost = (TabHost) getParent().getParent().findViewById(android.R.id.tabhost);
	         tabHost.setOnTabChangedListener(new  TabHost.OnTabChangeListener() {
				
				@Override     
				public void onTabChanged(String tabId) {
					// TODO Auto-generated method stub
					 final int pos = tabHost.getCurrentTab();
	                    final View tabView = tabHost.getTabWidget().getChildTabViewAt(pos);
	                    final int[] locationOnScreen= new int[2];
	                    tabView.getLocationOnScreen(locationOnScreen);
	                   
				}
			});
	         tabHost.setCurrentTab(3);
		}
	}
	
	
	 @Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		//bindMenu();
		if(!appPrefs.getBtngoal_hide().toString().equals("0")){
			btn_add_goal.setVisibility(View.GONE);
		}
		if(!appPrefs.getBtnhealth_hide().toString().equals("0")){
			btn_add_health.setVisibility(View.GONE);
		}
		if(!appPrefs.getBtnprofile_hide().toString().equals("0")){
			btn_add_profile.setVisibility(View.GONE);
		}
		if(!appPrefs.getBtngoal_hide().toString().equals("0") && !appPrefs.getBtnhealth_hide().toString().equals("0") && !appPrefs.getBtnprofile_hide().toString().equals("0")){
			profile_layout.setVisibility(View.GONE);
		}
		 heding_name_profile.setText(appPrefs.getProfileName());
	}
	     

	@Override
	 public void onBackPressed() 
	 {
	          
	         
	 }
}
