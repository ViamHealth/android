package com.viamhealth.android.activities;

import java.util.Arrays;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.viamhealth.android.adapters.GoalDataAdapter;
import com.viamhealth.android.R;
import com.viamhealth.android.ViamHealthPrefs;

public class Settings extends Activity implements OnClickListener{
	Display display;
	int height,width,w15,w20,h40,w5;
	ViamHealthPrefs appPrefs;;
	
	TextView heading_name,lbl_invite_user_goal;
	LinearLayout menu_invite,menu_invite_out,setting_layout;
	Typeface tf;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
	 	setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		
	 	setContentView(R.layout.reminder);
	 	
	 	appPrefs=new ViamHealthPrefs(this.getParent());
	 	tf = Typeface.createFromAsset(this.getAssets(),"Roboto-Condensed.ttf");
		// get screen height and width
		ScreenDimension();
		
		w20=(int)((width*6.25)/100);
		w15=(int)((width*4.68)/100);
		w5=(int)((width*1.7)/100);
		
		h40=(int)((height*8.34)/100);
		
	 	heading_name = (TextView)findViewById(R.id.heding_name);
		heading_name.setTypeface(tf);
		
		menu_invite = (LinearLayout)findViewById(R.id.menu_invite);
		menu_invite.setPadding(w15, 0, w20, 0);
		menu_invite.setOnClickListener(this);
		
		menu_invite_out = (LinearLayout)findViewById(R.id.menu_invite_out);
		menu_invite_out.setPadding(w15, 0, w20, 0);
		menu_invite_out.setOnClickListener(this);
		  
		setting_layout = (LinearLayout)findViewById(R.id.settiglayout);
		setting_layout.setPadding(0, h40, w5, 0);
		
		lbl_invite_user_goal=(TextView)findViewById(R.id.lbl_invite_user_goal);
		lbl_invite_user_goal.setTypeface(tf);
		lbl_invite_user_goal.setOnClickListener(this);
		
		actionmenu();
		
	}
	public void ScreenDimension()
	{
		display = getWindowManager().getDefaultDisplay(); 
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		width = display.getWidth();
		height = display.getHeight();
		appPrefs.setSwidth(String.valueOf(width));
		appPrefs.setSheight(String.valueOf(height));
	}
	public void actionmenu(){
		// for generate menu
		 List<String> Goal_data;
		 Goal_data =Arrays.asList(appPrefs.getMenuList().toString().split("\\s*,\\s*"));
		 GoalDataAdapter adapter = new GoalDataAdapter(this,R.layout.listview_item_row, Goal_data);
		        
		ListView listView1 = (ListView)findViewById(R.id.listView1);
		listView1.setAdapter(adapter);
		
		listView1.setOnItemClickListener(new OnItemClickListener() {
		    	
		    	@Override
				public void onItemClick(AdapterView<?> arg0, View view,
						int arg2, long arg3) {
					// TODO Auto-generated method stub
					String value = ((TextView)view.findViewById(R.id.txtName)).getText().toString();
					((ImageView)view.findViewById(R.id.imgIcon)).setImageResource(R.drawable.tick);
					Log.e("TAG","Selected value is " + value);
				}
			
		       });
	}
	@Override
	public void onClick(View v) 
	{
		// TODO Auto-generated method stub
		
		if(v==lbl_invite_user_goal){
			Log.e("TAG","Selected value is " + "invite user is clicked");
			Animation anim = AnimationUtils.loadAnimation(Settings.this, R.anim.fade_out);
			setting_layout.startAnimation(anim);
			setting_layout.setVisibility(View.INVISIBLE);
			menu_invite.setVisibility(View.VISIBLE);
			menu_invite_out.setVisibility(View.INVISIBLE);
			Intent i = new Intent(this.getParent(),InviteUser.class);
			startActivity(i);
			
		}
		
		if(v==menu_invite){
			setting_layout.setVisibility(View.VISIBLE);
			menu_invite_out.setVisibility(View.VISIBLE);
			menu_invite.setVisibility(View.INVISIBLE);
			Animation anim = AnimationUtils.loadAnimation(Settings.this, R.anim.fade_in);
			setting_layout.startAnimation(anim);
			
			Log.e("TAG","Clicked");
		}
		if(v==menu_invite_out){
			Animation anim = AnimationUtils.loadAnimation(Settings.this, R.anim.fade_out);
			setting_layout.startAnimation(anim);
			setting_layout.setVisibility(View.INVISIBLE);
			menu_invite.setVisibility(View.VISIBLE);
			menu_invite_out.setVisibility(View.INVISIBLE);
			Log.e("TAG","Clicked");
		}  
		
		
	}
	@Override
    public void onBackPressed() 
        {
          
         
        }
}
