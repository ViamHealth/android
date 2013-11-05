package com.viamhealth.android.activities.oldones;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.viamhealth.android.activities.AddReminder;
import com.viamhealth.android.dao.db.DataBaseAdapter;

import com.viamhealth.android.model.ReminderData;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout.LayoutParams;

import com.viamhealth.android.adapters.GoalDataAdapter;
import com.viamhealth.android.R;
import com.viamhealth.android.ViamHealthPrefs;

public class Reminder extends Activity implements OnClickListener{
	Display display;
	int height,width;
	ViamHealthPrefs appPrefs;;
	
	TextView heading_name,lbl_invite_user_goal,lbl_reminder_count;
	LinearLayout menu_invite,menu_invite_out,setting_layout,reminder_list_layout;
	TableLayout tb;
	Typeface tf;
	int w10,h10,w50,w15,w20,h40,w5,h50,h5;
	LinearLayout layout,layout111;
	ArrayList<ReminderData> lstData = new ArrayList<ReminderData>();
	DataBaseAdapter dbobj;
	int pYear,pMonth,pDay;
	Calendar dateAndTime=Calendar.getInstance();
	Date date1;
	SimpleDateFormat dateFormatter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
	 	setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		
	 	setContentView(R.layout.reminder);
	 	
	 	appPrefs=new ViamHealthPrefs(this.getParent());
	 	tf = Typeface.createFromAsset(this.getAssets(),"Roboto-Condensed.ttf");
	 	dbobj = new DataBaseAdapter(Reminder.this);
		lstData=dbobj.getReminder();
		// get screen height and width
		ScreenDimension();
		
		//calculate dynamic padding
		
		w20=(int)((width*6.25)/100);
		w50=(int)((width*15.63)/100);
		w10=(int)((width*3.13)/100);
		w15=(int)((width*4.68)/100);
		w5=(int)((width*1.7)/100);
		
		h40=(int)((height*8.34)/100);
		h50=(int)((height*10.42)/100);
		h5=(int)((height*1.042)/100);
		h10=(int)((height*2.083)/100);
		
	 	heading_name = (TextView)findViewById(R.id.heding_name);
	 	heading_name.setText(appPrefs.getProfileName());
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
		
		// for action menu
		actionmenu();
		
		// for get all reminders
		lbl_reminder_count=(TextView)findViewById(R.id.lbl_reminder_count);
		pYear = dateAndTime.get(Calendar.YEAR);
	    pMonth = dateAndTime.get(Calendar.MONTH);
	    pDay = dateAndTime.get(Calendar.DAY_OF_MONTH);
	    
		 Date d = new Date(pYear, pMonth, pDay);
 dateFormatter = new SimpleDateFormat(
	                        "EEEE dd MMMM yy");
	     String tempdate = dateFormatter.format(d);
	     try {
			date1 = dateFormatter.parse(tempdate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		getReminders();
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
		    		heading_name.setText(appPrefs.getProfileName());
		    		for(int i=0;i<Goal_data.size();i++){
						if(value.toString().equals(appPrefs.getProfileName().toString())){
							((ImageView)view.findViewById(R.id.imgIcon)).setVisibility(View.VISIBLE);
						}else{
							((ImageView)view.findViewById(R.id.imgIcon)).setVisibility(View.INVISIBLE);
						}
				}
					Animation anim = AnimationUtils.loadAnimation(Reminder.this, R.anim.fade_out);
					setting_layout.startAnimation(anim);
					setting_layout.setVisibility(View.INVISIBLE);
					menu_invite.setVisibility(View.VISIBLE);
					menu_invite_out.setVisibility(View.INVISIBLE);
				}
			
		       });
	}
	// for generate reminder list
	public void getReminders(){
		lbl_reminder_count.setPadding(w10, 0, 0, 0);
		lbl_reminder_count.setText(" Reminders (" + lstData.size() +")");
	
		reminder_list_layout = (LinearLayout)findViewById(R.id.reminder_list_layout);
		reminder_list_layout.setPadding(w10, h10, w10, h10);
		reminder_list_layout.removeAllViews();
		for(int i=0;i<lstData.size();i++)
    	{
			 layout = new LinearLayout(Reminder.this);
			 layout.setOrientation(LinearLayout.VERTICAL);
			 layout.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
			 layout.setPadding(0, 0, 0, h10);
			 
			 	LinearLayout layout1 = new LinearLayout(Reminder.this);
			 	layout1.setOrientation(LinearLayout.VERTICAL);
			 	layout1.setBackgroundResource(R.drawable.r2);
			 	layout1.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
			 		
			 		LinearLayout first = new LinearLayout(Reminder.this);
			 		first.setOrientation(LinearLayout.HORIZONTAL);
			 		first.setPadding(w10, 0, w10, 0);
			 		
			 		TextView txtdetail = new TextView(Reminder.this);
			 		txtdetail.setText(lstData.get(i).getRemindername());
			 		txtdetail.setTextColor(Color.BLACK);
			 		txtdetail.setPadding(0, h10, 0, 0);
			 		txtdetail.setTextSize(16);
			 		txtdetail.setTypeface(null,Typeface.BOLD);
			 		first.addView(txtdetail);
			 		
			 		TextView txtstatus = new TextView(Reminder.this);
			 		txtstatus.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
			 		txtstatus.setGravity(Gravity.RIGHT);
			 		Date date2 = null ;
			 		try {
						date2 = dateFormatter.parse(lstData.get(i).getTime());
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			 		if(date2.after(date1)){
			 			txtstatus.setText("Upcomming");
			 		}else if(date2.before(date1)){
			 			txtstatus.setText("Previous");
			 		}else{
			 			txtstatus.setText("Today");
			 		}
			 		
			 		txtstatus.setTextColor(Color.BLACK);
			 		txtdetail.setPadding(0, h10, 0, 0);
			 		first.addView(txtstatus);
			 		
			 	layout1.addView(first);
				
				 	LinearLayout second = new LinearLayout(Reminder.this);
				 	second.setOrientation(LinearLayout.VERTICAL);
				 	second.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
				 	second.setGravity(Gravity.BOTTOM);
				 	second.setPadding(w10, h50, w10, 0);
			 		
			 		TextView txtwhen = new TextView(Reminder.this);
			 		txtwhen.setText("When");
			 		txtwhen.setTextColor(Color.BLACK);
			 		txtwhen.setPadding(0, h10, 0, 0);
			 		second.addView(txtwhen);
			 		
			 		TextView txtdate = new TextView(Reminder.this);
			 		txtdate.setText(lstData.get(i).getTime());
			 		txtdate.setTextColor(Color.BLACK);
			 		txtdetail.setPadding(0, h10, 0, 0);
			 		second.addView(txtdate);
			 		
			 	layout1.addView(second);
				layout.addView(layout1);
				  
				LinearLayout layout2 = new LinearLayout(Reminder.this);
				layout2.setOrientation(LinearLayout.HORIZONTAL);
				layout2.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
				
					LinearLayout layout11 = new LinearLayout(Reminder.this);
					layout11.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
					layout11.setGravity(Gravity.RIGHT);
					
					TextView txtModify = new TextView(Reminder.this);
					txtModify.setText("Modify");
					txtModify.setGravity(Gravity.CENTER);
					txtModify.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
					txtModify.setTextColor(Color.BLACK);
					txtModify.setTextSize(11);
					txtModify.setTypeface(tf);
					txtModify.setBackgroundResource(R.drawable.otherbg_toggle);
					txtModify.setOnClickListener(this);
					txtModify.setPadding(w15, h5, w15, h5);
					
					TextView txtDelete = new TextView(Reminder.this);
					txtDelete.setText("Delete");
					txtDelete.setId(2);
					txtDelete.setTag(lstData.get(i).getId());
					txtDelete.setGravity(Gravity.CENTER);
					txtDelete.setTextColor(Color.BLACK);
					txtDelete.setTextSize(11);       
					txtDelete.setTypeface(tf);
					txtDelete.setBackgroundResource(R.drawable.otherbg_toggle);
					txtDelete.setOnClickListener(this);
					txtDelete.setPadding(w15, h5, w15, h5);
					      
					layout111 = new LinearLayout(Reminder.this);
					layout111.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
					layout111.setGravity(Gravity.LEFT);
					
					TextView txtnew = new TextView(Reminder.this);
					txtnew.setText("+ New Reminder");
					txtnew.setTextColor(Color.rgb(47, 154, 206));
					txtnew.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
					
					txtnew.setPadding(w15, h5, w50, h5);
					txtnew.setTextSize(11);
					txtnew.setTypeface(tf);
					txtnew.setId(1);
					txtnew.setTag("new val " + i);
					txtnew.setBackgroundResource(R.drawable.newreminder_toggle);
					txtnew.setOnClickListener(Reminder.this);
					layout111.addView(txtnew);
				
				
				layout111.addView(txtDelete);	
				layout111.addView(txtModify);
				layout11.addView(layout111);	
				
				layout2.addView(layout11);
			 	layout.addView(layout2);
				reminder_list_layout.addView(layout);
			 
    	}
	}
	
	@Override
	public void onClick(View v) 
	{
		// TODO Auto-generated method stub
		
		if(v==lbl_invite_user_goal){
			Log.e("TAG","Selected value is " + "invite user is clicked");
			Animation anim = AnimationUtils.loadAnimation(Reminder.this, R.anim.fade_out);
			setting_layout.startAnimation(anim);
			setting_layout.setVisibility(View.INVISIBLE);
			menu_invite.setVisibility(View.VISIBLE);
			menu_invite_out.setVisibility(View.INVISIBLE);
			Intent i = new Intent(this.getParent(),InviteUser.class);
			startActivity(i);
			
		}
	
		if(v==menu_invite){
			actionmenu();
			setting_layout.setVisibility(View.VISIBLE);
			menu_invite_out.setVisibility(View.VISIBLE);
			menu_invite.setVisibility(View.INVISIBLE);
			Animation anim = AnimationUtils.loadAnimation(Reminder.this, R.anim.fade_in);
			setting_layout.startAnimation(anim);
			
			Log.e("TAG","Clicked");
		}
		if(v==menu_invite_out){
			Animation anim = AnimationUtils.loadAnimation(Reminder.this, R.anim.fade_out);
			setting_layout.startAnimation(anim);
			setting_layout.setVisibility(View.INVISIBLE);
			menu_invite.setVisibility(View.VISIBLE);
			menu_invite_out.setVisibility(View.INVISIBLE);
			Log.e("TAG","Clicked");
		}  
		if(v.getId()==1){
			TextView tr=(TextView)v;
			String tag=tr.getTag().toString();
			Log.e("TAG","tag name is " + tag);
			Intent i = new Intent(Reminder.this,AddReminder.class);
			startActivity(i);
		}
		if(v.getId()==2){
			TextView txt = (TextView)v;
			dbobj.manageReminder(txt.getTag().toString());
			lstData=dbobj.getReminder();
			getReminders();
		}
	}
	
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		heading_name.setText(appPrefs.getProfileName());
		lstData=dbobj.getReminder();
		getReminders();
	}
	@Override
    public void onBackPressed() 
        {
          
         
        }
}
