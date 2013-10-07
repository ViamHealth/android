package com.viamhealth.android.activities;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;

import com.viamhealth.android.adapters.BreakfastAdapter;
import com.viamhealth.android.adapters.DinnerAdapter;
import com.viamhealth.android.Global_Application;
import com.viamhealth.android.adapters.GoalDataAdapter;
import com.viamhealth.android.adapters.LunchAdapter;
import com.viamhealth.android.R;
import com.viamhealth.android.adapters.SnacksAdapter;
import com.viamhealth.android.ViamHealthPrefs;
import com.viamhealth.android.dao.restclient.old.functionClass;

import com.viamhealth.android.model.CategoryFood;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.viamhealth.android.ui.RefreshableListView;

import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class FoodDiary extends BaseActivity implements OnClickListener {
	Display display;
	int height,width;
	int w15,w20,w10,w50,w5,h40,h10,h5,w2,h2,w110,h200,h20;
	
	TextView lblback,lbl_invite_user_food,heding_name_food,lbl_food_date,lbl_food_time,lblbrk,lbltotalbrkcal,lbllunch,
			 lbllunchcal,lblsnack,lblsnakcal,lbldinner,lbldinnercal,lblitem1,lblitem2,lblitem3,lblitem4;
	LinearLayout settiglayout_food,menu_invite_out_food,menu_invite_food,back_food_layout,food_main_layout,food_mid_layout,
				 btn_food_time_picker,btn_food_date_picker,food_header,layout1,layout2,layout3,layout4,breakfast,lunch,snacks,dinner;
	ImageView img_date,img_time,food_icon,addDinner,addSnacks,addLunch,addBreakfast,back,person_icon;
	RefreshableListView lstViewLunch,lstViewSnacks,lstViewDinner;
	RefreshableListView lstViewBreakfast;
	String nexturl,frm;
	Typeface tf;
	ProgressDialog dialog1;
	boolean bolbrk,bollunch,bolsnaks,boldiner=false;
	
	ViamHealthPrefs appPrefs;
	functionClass obj;
	ArrayList<CategoryFood> lstResultBreakfast = new ArrayList<CategoryFood>();
	ArrayList<CategoryFood> lstResultLunch = new ArrayList<CategoryFood>();
	ArrayList<CategoryFood> lstResultSnacks = new ArrayList<CategoryFood>();
	ArrayList<CategoryFood> lstResultDinner = new ArrayList<CategoryFood>();
	
	 DateFormat fmtDateAndTime=DateFormat.getDateTimeInstance();
     Calendar dateAndTime=Calendar.getInstance();
     int pYear,pMonth,pDay;
	String selecteduserid="0";
	public HashMap<String, ArrayList<String>> lst = new HashMap<String, ArrayList<String>>();
	Global_Application ga;
	
	private DisplayImageOptions options;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);  
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		
		setContentView(R.layout.food_diary);
		
		appPrefs = new ViamHealthPrefs(this.getParent());
		obj=new functionClass(this.getParent());
		ga=((Global_Application)getApplicationContext());
		
		tf = Typeface.createFromAsset(this.getAssets(),"Roboto-Condensed.ttf");
		//for get screen height width
        ScreenDimension();
        
        //calculate dynamic padding
        w10=(int)((width*3.13)/100);
        w15=(int)((width*4.68)/100);
        w20=(int)((width*6.25)/100);
        w50=(int)((width*15.63)/100);
        w5=(int)((width*1.56)/100);
        w2=(int)((width*0.60)/100);
        w110=(int)((width*31.25)/100);
      
        h40=(int)((height*8.34)/100);
        h10=(int)((height*2.083)/100);
        h5=(int)((height*1.042)/100);
        h2=(int)((height*0.38)/100);
        h200=(int)((height*41.67)/100);
    	h20=(int)((height*4.17)/100);
        
    	//casting control and manage padding and call onclick method
        back=(ImageView)findViewById(R.id.back);
      	back.setOnClickListener(FoodDiary.this);
        
      	lbl_invite_user_food=(TextView)findViewById(R.id.lbl_invite_user_food);
      	lbl_invite_user_food.setTypeface(tf);
      	lbl_invite_user_food.setOnClickListener(this);
      		
      		
      	menu_invite_food= (LinearLayout)findViewById(R.id.menu_invite_food);
      	menu_invite_food.setPadding(w15, 0, w20, 0);
      	menu_invite_food.setOnClickListener(this);
      		
      	heding_name_food=(TextView)findViewById(R.id.heding_name_food);
    	heding_name_food.setText(appPrefs.getProfileName());
    	heding_name_food.setTypeface(tf);
    		//heding_name_food.setPadding(0, 0, w50, 0);
    		
    	menu_invite_out_food = (LinearLayout)findViewById(R.id.menu_invite_out_food);
    	menu_invite_out_food.setOnClickListener(this);
    	menu_invite_out_food.setPadding(w15, 0, w20, 0);
    		
       settiglayout_food = (LinearLayout)findViewById(R.id.settiglayout_food);
       settiglayout_food.setPadding(0, h40, w5, 0);
    		
       person_icon = (ImageView)findViewById(R.id.person_icon);
       person_icon.getLayoutParams().width = w20;
       person_icon.getLayoutParams().height = h20;
       
       options = new DisplayImageOptions.Builder()
		.build();
		
		imageLoader.displayImage(appPrefs.getProfilepic(), person_icon, options, new SimpleImageLoadingListener() {
			@Override
			public void onLoadingComplete(Bitmap loadedImage) {
				Animation anim = AnimationUtils.loadAnimation(FoodDiary.this, R.anim.fade_in);
				person_icon.setAnimation(anim);
				anim.start();
				
				
			}
		});
       
        layout1 = (LinearLayout)findViewById(R.id.layout1);
        layout1.setPadding(0, 0, 0, h10);
        
        layout2 = (LinearLayout)findViewById(R.id.layout2);
        layout2.setPadding(0, 0, 0, h10);
        
        layout3 = (LinearLayout)findViewById(R.id.layout3);
        layout3.setPadding(0, 0, 0, h10);
        
        layout4 = (LinearLayout)findViewById(R.id.layout4);
        layout4.setPadding(0, 0, 0, h10);
        
        lblitem1 = (TextView)findViewById(R.id.lblitem1);
        lblitem1.getLayoutParams().width = w110;
        
        lblitem2 = (TextView)findViewById(R.id.lblitem2);
        lblitem2.getLayoutParams().width = w110;
        
        lblitem3 = (TextView)findViewById(R.id.lblitem3);
        lblitem3.getLayoutParams().width = w110;
        
        lblitem4 = (TextView)findViewById(R.id.lblitem4);
        lblitem4.getLayoutParams().width=w110;
        
        breakfast = (LinearLayout)findViewById(R.id.breakfast);
        breakfast.setOnClickListener(FoodDiary.this);
        
        lunch = (LinearLayout)findViewById(R.id.lunch);
        lunch.setOnClickListener(FoodDiary.this);
        
        snacks = (LinearLayout)findViewById(R.id.snacks);
        snacks.setOnClickListener(FoodDiary.this);
        
        dinner = (LinearLayout)findViewById(R.id.dinner);
        dinner.setOnClickListener(FoodDiary.this);
        
        lstViewBreakfast = (RefreshableListView)findViewById(R.id.lstViewBreakfast);
        lstViewBreakfast.getLayoutParams().height = h200;
        lstViewBreakfast.setOnTouchListener(new View.OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				int action = event.getAction();
	            switch (action) {
	            case MotionEvent.ACTION_DOWN:
	                // Disallow ScrollView to intercept touch events.
	                v.getParent().requestDisallowInterceptTouchEvent(true);
	                break;

	            case MotionEvent.ACTION_UP:
	                // Allow ScrollView to intercept touch events.
	                v.getParent().requestDisallowInterceptTouchEvent(false);
	                break;
	            }

	            // Handle ListView touch events.
	            v.onTouchEvent(event);
	            return true;
			}
		});
        
        lstViewLunch = (RefreshableListView)findViewById(R.id.lstViewLunch);
        lstViewLunch.getLayoutParams().height = h200;
        lstViewLunch.setOnTouchListener(new View.OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				int action = event.getAction();
	            switch (action) {
	            case MotionEvent.ACTION_DOWN:
	                // Disallow ScrollView to intercept touch events.
	                v.getParent().requestDisallowInterceptTouchEvent(true);
	                break;

	            case MotionEvent.ACTION_UP:
	                // Allow ScrollView to intercept touch events.
	                v.getParent().requestDisallowInterceptTouchEvent(false);
	                break;
	            }

	            // Handle ListView touch events.
	            v.onTouchEvent(event);
	            return true;
			}
		});
        
        lstViewSnacks = (RefreshableListView)findViewById(R.id.lstViewSnakes);
        lstViewSnacks.getLayoutParams().height = h200;
        lstViewSnacks.setOnTouchListener(new View.OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				int action = event.getAction();
	            switch (action) {
	            case MotionEvent.ACTION_DOWN:
	                // Disallow ScrollView to intercept touch events.
	                v.getParent().requestDisallowInterceptTouchEvent(true);
	                break;

	            case MotionEvent.ACTION_UP:
	                // Allow ScrollView to intercept touch events.
	                v.getParent().requestDisallowInterceptTouchEvent(false);
	                break;
	            }

	            // Handle ListView touch events.
	            v.onTouchEvent(event);
	            return true;
			}
		});
        
        lstViewDinner = (RefreshableListView)findViewById(R.id.lstViewDinner);
        lstViewDinner.getLayoutParams().height = h200;
        lstViewDinner.setOnTouchListener(new View.OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				int action = event.getAction();
	            switch (action) {
	            case MotionEvent.ACTION_DOWN:
	                // Disallow ScrollView to intercept touch events.
	                v.getParent().requestDisallowInterceptTouchEvent(true);
	                break;

	            case MotionEvent.ACTION_UP:
	                // Allow ScrollView to intercept touch events.
	                v.getParent().requestDisallowInterceptTouchEvent(false);
	                break;
	            }

	            // Handle ListView touch events.
	            v.onTouchEvent(event);
	            return true;
			}
		});
        
        
        lblbrk = (TextView)findViewById(R.id.lblbrk);
        lbltotalbrkcal = (TextView)findViewById(R.id.lbltotalbrkcal);
        lbllunch = (TextView)findViewById(R.id.lbllunch);
        lbllunchcal = (TextView)findViewById(R.id.lbllunchcal);
        lblsnack = (TextView)findViewById(R.id.lblsnack);
        lblsnakcal = (TextView)findViewById(R.id.lblsnakcal);
        lbldinner = (TextView)findViewById(R.id.lbldinner);
        lbldinnercal = (TextView)findViewById(R.id.lbldinnercal);
        
        addBreakfast = (ImageView)findViewById(R.id.addBreakfast);
        addBreakfast.setOnClickListener(FoodDiary.this);
        
        addLunch = (ImageView)findViewById(R.id.addLunch);
        addLunch.setOnClickListener(FoodDiary.this);
        
        addSnacks = (ImageView)findViewById(R.id.addSnacks);
        addSnacks.setOnClickListener(FoodDiary.this);
        
        addDinner = (ImageView)findViewById(R.id.addDinner);
        addDinner.setOnClickListener(FoodDiary.this);
        
    	
		food_main_layout = (LinearLayout)findViewById(R.id.food_main_layout);
		food_main_layout.setPadding(w10, h10, w10, h10);
	
		btn_food_time_picker = (LinearLayout)findViewById(R.id.btn_food_time_picker);
		btn_food_time_picker.setOnClickListener(this);
		
	
		img_time = (ImageView)findViewById(R.id.img_time);
		img_time.setPadding(w5, 0, w5, 0);
				
		btn_food_date_picker = (LinearLayout)findViewById(R.id.btn_food_date_picker);
		btn_food_date_picker.setOnClickListener(this);
		
		img_date = (ImageView)findViewById(R.id.img_date);
		img_date.setPadding(w5, 0, w5, 0);
		
		lbl_food_date = (TextView)findViewById(R.id.lbl_food_date);
		lbl_food_date.setPadding(w5, 0, 0, 0);
		lbl_food_date.setTypeface(tf);
		
		lbl_food_time = (TextView)findViewById(R.id.lbl_food_time);
	    lbl_food_time.setTypeface(tf);
		
		//food_icon = (ImageView)findViewById(R.id.food_icon);
	//	food_icon.setPadding(w5, h5, w5, h5);
		
		food_header=(LinearLayout)findViewById(R.id.food_header);
		food_header.setPadding(0, 0, 0, h10);
		// for action menu
		actionmenu();
	
		pYear = dateAndTime.get(Calendar.YEAR);
	    pMonth = dateAndTime.get(Calendar.MONTH);
	    pDay = dateAndTime.get(Calendar.DAY_OF_MONTH);	 
	    updateDisplay();
	   lstViewBreakfast.setOnRefreshListener(new RefreshableListView.OnRefreshListener() {
		
		@Override
		public void onRefresh() {
			// TODO Auto-generated method stub
			if(!ga.getNextbrekfast().toString().equals("null")){
				nexturl = ga.getNextbrekfast();
				frm="b";
				if(isInternetOn()){
					 CallBrkPullToRefreshTask task = new CallBrkPullToRefreshTask();
					 task.applicationContext =FoodDiary.this.getParent();
					 task.execute();
				}else{
					Toast.makeText(FoodDiary.this,"Network is not available....",Toast.LENGTH_SHORT).show();
				}
			}
		}
	   });
	   lstViewLunch.setOnRefreshListener(new RefreshableListView.OnRefreshListener() {
			
			@Override
			public void onRefresh() {
				// TODO Auto-generated method stub
				if(!ga.getNextlunch().toString().equals("null")){
					nexturl = ga.getNextlunch();
					frm="l";
					if(isInternetOn()){
						 CallBrkPullToRefreshTask task = new CallBrkPullToRefreshTask();
						 task.applicationContext =FoodDiary.this.getParent();
						 task.execute();
					}else{
						Toast.makeText(FoodDiary.this,"Network is not available....",Toast.LENGTH_SHORT).show();
					}
				}
			}
		   });
	   lstViewSnacks.setOnRefreshListener(new RefreshableListView.OnRefreshListener() {
			
			@Override
			public void onRefresh() {
				// TODO Auto-generated method stub
				if(!ga.getNextsnacks().toString().equals("null")){
					nexturl = ga.getNextsnacks();
					frm="s";
					if(isInternetOn()){
						 CallBrkPullToRefreshTask task = new CallBrkPullToRefreshTask();
						 task.applicationContext =FoodDiary.this.getParent();
						 task.execute();
					}else{
						Toast.makeText(FoodDiary.this,"Network is not available....",Toast.LENGTH_SHORT).show();
					}
				}
			}
		   });
	   lstViewDinner.setOnRefreshListener(new RefreshableListView.OnRefreshListener() {
			
			@Override
			public void onRefresh() {
				// TODO Auto-generated method stub
				if(!ga.getNextdinner().toString().equals("null")){
					nexturl = ga.getNextdinner();
					frm="d";
					if(isInternetOn()){
						 CallBrkPullToRefreshTask task = new CallBrkPullToRefreshTask();
						 task.applicationContext =FoodDiary.this.getParent();
						 task.execute();
					}else{
						Toast.makeText(FoodDiary.this,"Network is not available....",Toast.LENGTH_SHORT).show();
					}
				}
			}
		   });
	   lstViewBreakfast.setOnItemClickListener(new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View v, int arg2,
				long arg3) {
			// TODO Auto-generated method stub
			ImageView view = (ImageView)v.findViewById(R.id.delete);
			view.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
							FoodDiary.this.getParent());
			 
						// set title
						alertDialogBuilder.setTitle("Confirmation");
			 
						// set dialog message
						alertDialogBuilder
							.setMessage("Are you sure you want to delete this food?")
							.setCancelable(false)
							.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,int id) {
									// if this button is clicked, close
									// current activity
									dialog.cancel();
									if(isInternetOn()){
										 CallDeleteTask task = new CallDeleteTask();
										 task.applicationContext =FoodDiary.this.getParent();
										 task.execute();
									}else{
										Toast.makeText(FoodDiary.this,"Network is not available....",Toast.LENGTH_SHORT).show();
									}
								}
							  })
							.setNegativeButton("No",new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,int id) {
									// if this button is clicked, just close
									// the dialog box and do nothing
									dialog.cancel();
								}
							});
			 
							// create alert dialog
							AlertDialog alertDialog = alertDialogBuilder.create();
			 
							// show it
							alertDialog.show();
				}
			});
			
		}
	});
	   lstViewLunch.setOnItemClickListener(new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View v, int arg2,
				long arg3) {
			// TODO Auto-generated method stub
			ImageView view = (ImageView)v.findViewById(R.id.delete);
			view.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
							FoodDiary.this.getParent());
			 
						// set title
						alertDialogBuilder.setTitle("Confirmation");
			 
						// set dialog message
						alertDialogBuilder
							.setMessage("Are you sure you want to delete this food?")
							.setCancelable(false)
							.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,int id) {
									// if this button is clicked, close
									// current activity
									dialog.cancel();
									if(isInternetOn()){
										 CallDeleteTask task = new CallDeleteTask();
										 task.applicationContext =FoodDiary.this.getParent();
										 task.execute();
									}else{
										Toast.makeText(FoodDiary.this,"Network is not available....",Toast.LENGTH_SHORT).show();
									}
								}
							  })
							.setNegativeButton("No",new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,int id) {
									// if this button is clicked, just close
									// the dialog box and do nothing
									dialog.cancel();
								}
							});
			 
							// create alert dialog
							AlertDialog alertDialog = alertDialogBuilder.create();
			 
							// show it
							alertDialog.show();
				}
			});
			
		}
	});
	   lstViewSnacks.setOnItemClickListener(new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View v, int arg2,
				long arg3) {
			// TODO Auto-generated method stub
			ImageView view = (ImageView)v.findViewById(R.id.delete);
			view.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
							FoodDiary.this.getParent());
			 
						// set title
						alertDialogBuilder.setTitle("Confirmation");
			 
						// set dialog message
						alertDialogBuilder
							.setMessage("Are you sure you want to delete this food?")
							.setCancelable(false)
							.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,int id) {
									// if this button is clicked, close
									// current activity
									dialog.cancel();
									if(isInternetOn()){
										 CallDeleteTask task = new CallDeleteTask();
										 task.applicationContext =FoodDiary.this.getParent();
										 task.execute();
									}else{
										Toast.makeText(FoodDiary.this,"Network is not available....",Toast.LENGTH_SHORT).show();
									}
								}
							  })
							.setNegativeButton("No",new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,int id) {
									// if this button is clicked, just close
									// the dialog box and do nothing
									dialog.cancel();
								}
							});
			 
							// create alert dialog
							AlertDialog alertDialog = alertDialogBuilder.create();
			 
							// show it
							alertDialog.show();
				}
			});
			
		}
	});
	   lstViewDinner.setOnItemClickListener(new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View v, int arg2,
				long arg3) {
			// TODO Auto-generated method stub
			ImageView view = (ImageView)v.findViewById(R.id.delete);
			view.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
							FoodDiary.this.getParent());
			 
						// set title
						alertDialogBuilder.setTitle("Confirmation");
			 
						// set dialog message
						alertDialogBuilder
							.setMessage("Are you sure you want to delete this food?")
							.setCancelable(false)
							.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,int id) {
									// if this button is clicked, close
									// current activity
									dialog.cancel();
									if(isInternetOn()){
										 CallDeleteTask task = new CallDeleteTask();
										 task.applicationContext =FoodDiary.this.getParent();
										 task.execute();
									}else{
										Toast.makeText(FoodDiary.this,"Network is not available....",Toast.LENGTH_SHORT).show();
									}
								}
							  })
							.setNegativeButton("No",new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,int id) {
									// if this button is clicked, just close
									// the dialog box and do nothing
									dialog.cancel();
								}
							});
			 
							// create alert dialog
							AlertDialog alertDialog = alertDialogBuilder.create();
			 
							// show it
							alertDialog.show();
				}
			});
			
		}
	});
	
	}
	 public static Bitmap getBitmapFromURL(String src) {
	     try {
	         URL url = new URL(src);
	         HttpURLConnection connection = (HttpURLConnection) url.openConnection();
	         connection.setDoInput(true);
	         connection.connect();
	         InputStream input = connection.getInputStream();
	         Bitmap myBitmap = BitmapFactory.decodeStream(input);
	         return myBitmap;
	     } catch (IOException e) {
	         e.printStackTrace();
	         return null;
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
		    		heding_name_food.setText(appPrefs.getProfileName());
		    		for(int i=0;i<Goal_data.size();i++){
						if(value.toString().equals(appPrefs.getProfileName().toString())){
							Log.e("TAG","visible");
							((ImageView)view.findViewById(R.id.imgIcon)).setVisibility(View.VISIBLE);
						}else{
							Log.e("TAG","Invisible");
							((ImageView)view.findViewById(R.id.imgIcon)).setVisibility(View.INVISIBLE);
						}
				}
		    		Animation anim = AnimationUtils.loadAnimation(FoodDiary.this, R.anim.fade_out);
					settiglayout_food.startAnimation(anim);
					settiglayout_food.setVisibility(View.INVISIBLE);
					menu_invite_food.setVisibility(View.VISIBLE);
					menu_invite_out_food.setVisibility(View.INVISIBLE);
					
					selecteduserid = Integer.toString(position);
					
					CalluserMeTask task = new CalluserMeTask();
					task.applicationContext =FoodDiary.this.getParent();
					task.execute();
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
		if(v==back){
			Intent i=new Intent(FoodDiary.this, Home.class);
			startActivity(i);
			finish();	
		}
		
		if(v==breakfast){
			if(bolbrk){
				bolbrk=false;
				lstViewBreakfast.setVisibility(View.GONE);
				lstViewLunch.setVisibility(View.GONE);
				lstViewSnacks.setVisibility(View.GONE);
				lstViewDinner.setVisibility(View.GONE);
			}else{
				bolbrk=true;
				if(lstResultBreakfast.size()>0){
					lstViewBreakfast.setVisibility(View.VISIBLE);
					lstViewLunch.setVisibility(View.GONE);
					lstViewSnacks.setVisibility(View.GONE);
					lstViewDinner.setVisibility(View.GONE);
				}
			}
		}
		if(v==lunch){
			if(bollunch){
				bollunch=false;
				lstViewBreakfast.setVisibility(View.GONE);
				lstViewLunch.setVisibility(View.GONE);
				lstViewSnacks.setVisibility(View.GONE);
				lstViewDinner.setVisibility(View.GONE);
			}else{
				bollunch=true;
				if(lstResultLunch.size()>0){
					lstViewBreakfast.setVisibility(View.GONE);
					lstViewLunch.setVisibility(View.VISIBLE);
					lstViewSnacks.setVisibility(View.GONE);
					lstViewDinner.setVisibility(View.GONE);
				}
			}
		}
		if(v==snacks){
			if(bolsnaks){
				bolsnaks=false;
				lstViewBreakfast.setVisibility(View.GONE);
				lstViewLunch.setVisibility(View.GONE);
				lstViewSnacks.setVisibility(View.GONE);
				lstViewDinner.setVisibility(View.GONE);
			}else{
				bolsnaks=true;
				if(lstResultSnacks.size()>0){
					lstViewBreakfast.setVisibility(View.GONE);
					lstViewLunch.setVisibility(View.GONE);
					lstViewSnacks.setVisibility(View.VISIBLE);
					lstViewDinner.setVisibility(View.GONE);
				}
			}
		}
		if(v==dinner){
			
			if(boldiner){
				boldiner=false;
				lstViewBreakfast.setVisibility(View.GONE);
				lstViewLunch.setVisibility(View.GONE);
				lstViewSnacks.setVisibility(View.GONE);
				lstViewDinner.setVisibility(View.GONE);
			}else{
				boldiner=true;
				if(lstResultDinner.size()>0){
					lstViewBreakfast.setVisibility(View.GONE);
					lstViewLunch.setVisibility(View.GONE);
					lstViewSnacks.setVisibility(View.GONE);
					lstViewDinner.setVisibility(View.VISIBLE);
				}
			}
		}
		if(v==back_food_layout){
			finish();
		}
		if(v==btn_food_date_picker){
			new DatePickerDialog(getParent(), d,pYear,
                    pMonth,
                    pDay).show();
         }
		if(v==btn_food_time_picker){
			 new TimePickerDialog(getParent(), t,
                     dateAndTime.get(Calendar.HOUR_OF_DAY),
                     dateAndTime.get(Calendar.MINUTE),
                     true).show();
		}
		if(v==lbl_invite_user_food){
			Log.e("TAG","Selected value is " + "invite user is clicked");
			Animation anim = AnimationUtils.loadAnimation(FoodDiary.this, R.anim.fade_out);
			settiglayout_food.startAnimation(anim);
			settiglayout_food.setVisibility(View.INVISIBLE);
			menu_invite_food.setVisibility(View.VISIBLE);
			menu_invite_out_food.setVisibility(View.INVISIBLE);
			Log.e("TAG","Clicked");
			Intent i = new Intent(FoodDiary.this,InviteUser.class);
			startActivity(i);
		}
	
			if(v==menu_invite_food){
				actionmenu();
			settiglayout_food.setVisibility(View.VISIBLE);
			menu_invite_out_food.setVisibility(View.VISIBLE);
			menu_invite_food.setVisibility(View.INVISIBLE);
			Animation anim = AnimationUtils.loadAnimation(FoodDiary.this, R.anim.fade_in);
			settiglayout_food.startAnimation(anim);
			
			Log.e("TAG","Clicked");
		}
		if(v==menu_invite_out_food){
			Animation anim = AnimationUtils.loadAnimation(FoodDiary.this, R.anim.fade_out);
			settiglayout_food.startAnimation(anim);
			settiglayout_food.setVisibility(View.INVISIBLE);
			menu_invite_food.setVisibility(View.VISIBLE);
			menu_invite_out_food.setVisibility(View.INVISIBLE);
			Log.e("TAG","Clicked");
		}
		if(v==addBreakfast){
			ga.setFoodType("Breakfast");
			Intent Addfood = new Intent(getParent(),AddBreakfast.class);
			TabGroupActivity parentoption = (TabGroupActivity)getParent();
			parentoption.startChildActivity("Addfood",Addfood);
		
		}
		if(v==addLunch){
			ga.setFoodType("Lunch");
			Intent Addfood = new Intent(getParent(),AddBreakfast.class);
			TabGroupActivity parentoption = (TabGroupActivity)getParent();
			parentoption.startChildActivity("Addfood",Addfood);
		}
		if(v==addSnacks){
			ga.setFoodType("Snacks");
			Intent Addfood = new Intent(getParent(),AddBreakfast.class);
			TabGroupActivity parentoption = (TabGroupActivity)getParent();
			parentoption.startChildActivity("Addfood",Addfood);
		}
		if(v==addDinner){
			ga.setFoodType("Dinner");
			Intent Addfood = new Intent(getParent(),AddBreakfast.class);
			TabGroupActivity parentoption = (TabGroupActivity)getParent();
			parentoption.startChildActivity("Addfood",Addfood);
		}
			
	}
	DatePickerDialog.OnDateSetListener d=new DatePickerDialog.OnDateSetListener() {
	    public void onDateSet(DatePicker view, int year, int monthOfYear,
	                          int dayOfMonth) {
	      pMonth=monthOfYear;
	      pDay=dayOfMonth;
	      pYear=year;
	      updateDisplay();
	    }
	  };
	  private void updateDisplay() {
	        lbl_food_date.setText(
	            new StringBuilder()
	                    // Month is 0 based so add 1
	                    .append(pYear).append("-")
	                    .append(pMonth + 1).append("-")
	                    .append(pDay).append(" "));
	    }
	  private void updateLabelTime() {
		    
		    lbl_food_time.setText(dateAndTime.HOUR +":" +dateAndTime.MINUTE +":"+dateAndTime.SECOND);
		  }
	
	  TimePickerDialog.OnTimeSetListener t=new TimePickerDialog.OnTimeSetListener() {
	    public void onTimeSet(TimePicker view, int hourOfDay,
	                          int minute) {
	      dateAndTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
	      dateAndTime.set(Calendar.MINUTE, minute);
	      updateLabelTime();
	    }
	  };
	// async class for calling webservice and get responce message
			public class CallListTask extends AsyncTask <String, Void,String>
			{
				protected Context applicationContext;

				@Override
				protected void onPreExecute()     
				{
					
					//dialog = ProgressDialog.show(applicationContext, "Calling", "Please wait...", true);
					dialog1 = new ProgressDialog(getParent());
					dialog1.setCanceledOnTouchOutside(false);
					dialog1.setMessage("Please Wait....");
					dialog1.show();
					Log.i("onPreExecute", "onPreExecute");
					
				}       
				
				protected void onPostExecute(String result)
				{
					
					Log.i("onPostExecute", "onPostExecute");
						//dialog1.dismiss();
						Log.e("TAG","lst size : " + lstResultBreakfast.size());
						
						if(lstResultBreakfast.size()>0){
							lblbrk.setText("Breakfast ("+lstResultBreakfast.get(0).getCount()+")" );
							lbltotalbrkcal.setText(Global_Application.totalcal+"");
							BreakfastAdapter adapter = new BreakfastAdapter(FoodDiary.this,R.layout.breakfast_food_list, lstResultBreakfast);
						    lstViewBreakfast.setAdapter(adapter);
						    adapter.notifyDataSetChanged();
						    lstViewBreakfast.onRefreshComplete();
						    if(isInternetOn()){
								 CallLunchListTask task = new CallLunchListTask();
								 task.applicationContext =FoodDiary.this.getParent();
								 task.execute();
							}else{
								Toast.makeText(FoodDiary.this,"Network is not available....",Toast.LENGTH_SHORT).show();
							}
						}else{
							
							  if(isInternetOn()){
									 CallLunchListTask task = new CallLunchListTask();
									 task.applicationContext =FoodDiary.this.getParent();
									 task.execute();
								}else{
									Toast.makeText(FoodDiary.this,"Network is not available....",Toast.LENGTH_SHORT).show();
								}
						}
						 
				}  
		      
				@Override
				protected String doInBackground(String... params) {
					// TODO Auto-generated method stub
					Log.i("doInBackground--Object", "doInBackground--Object");
					//ga.lstResult=obj.manageGoal(appPrefs.getGoalname().toString(), type, goalvalue);
					Global_Application.totalcal=0;
					lstResultBreakfast = obj.FoodListing(Global_Application.url+"diet-tracker/?meal_type=BREAKFAST");
					return null;
				}
				   
			}     
			// async class for calling webservice and get responce message
						public class CallLunchListTask extends AsyncTask <String, Void,String>
						{
							protected Context applicationContext;

							@Override
							protected void onPreExecute()     
							{
								
								//dialog = ProgressDialog.show(applicationContext, "Calling", "Please wait...", true);
							/*	dialog1 = new ProgressDialog(getParent());
								dialog1.setMessage("Please Wait....");
								dialog1.show();*/
								Log.i("onPreExecute", "onPreExecute");
								
							}       
							
							protected void onPostExecute(String result)
							{
								
								Log.i("onPostExecute", "onPostExecute");
									//dialog1.dismiss();
									Log.e("TAG","lst size : " + lstResultLunch.size());
									if(lstResultLunch.size()>0){
										lbllunch.setText("Lunch ("+lstResultLunch.get(0).getCount()+")" );
										lbllunchcal.setText(Global_Application.totalcal+"");
									    LunchAdapter adapter = new LunchAdapter(FoodDiary.this,R.layout.lunch_food_list, lstResultLunch);
										lstViewLunch.setAdapter(adapter);
										adapter.notifyDataSetChanged();
										lstViewLunch.onRefreshComplete();
										  if(isInternetOn()){
												 CallSnaksListTask task = new CallSnaksListTask();
												 task.applicationContext =FoodDiary.this.getParent();
												 task.execute();
											}else{
												Toast.makeText(FoodDiary.this,"Network is not available....",Toast.LENGTH_SHORT).show();
											}
									}else{
										if(isInternetOn()){
											 CallSnaksListTask task = new CallSnaksListTask();
											 task.applicationContext =FoodDiary.this.getParent();
											 task.execute();
										}else{
											Toast.makeText(FoodDiary.this,"Network is not available....",Toast.LENGTH_SHORT).show();
										}
									}
									 
							}  
					      
							@Override
							protected String doInBackground(String... params) {
								// TODO Auto-generated method stub
								Log.i("doInBackground--Object", "doInBackground--Object");
								//ga.lstResult=obj.manageGoal(appPrefs.getGoalname().toString(), type, goalvalue);
								Global_Application.totalcal=0;
								lstResultLunch = obj.FoodListing(Global_Application.url+"diet-tracker/?meal_type=LUNCH");
								return null;
							}
							   
						}     
						// async class for calling webservice and get responce message
						public class CallSnaksListTask extends AsyncTask <String, Void,String>
						{
							protected Context applicationContext;

							@Override
							protected void onPreExecute()     
							{
								
								//dialog = ProgressDialog.show(applicationContext, "Calling", "Please wait...", true);
							/*	dialog1 = new ProgressDialog(getParent());
								dialog1.setMessage("Please Wait....");
								dialog1.show();*/
								Log.i("onPreExecute", "onPreExecute");
								
							}       
							
							protected void onPostExecute(String result)
							{
								
								Log.i("onPostExecute", "onPostExecute");
									dialog1.dismiss();
									Log.e("TAG","lst size : " + lstResultSnacks.size());
									if(lstResultSnacks.size()>0){
										lblsnack.setText("Snacks ("+lstResultSnacks.get(0).getCount()+")" );
										lblsnakcal.setText(Global_Application.totalcal+"");
										SnacksAdapter adapter = new SnacksAdapter(FoodDiary.this,R.layout.snacks_food_list, lstResultSnacks);
										lstViewSnacks.setAdapter(adapter);
										adapter.notifyDataSetChanged();
										lstViewSnacks.onRefreshComplete();
										  if(isInternetOn()){
												 CallDinnerListTask task = new CallDinnerListTask();
												 task.applicationContext =FoodDiary.this.getParent();
												 task.execute();
											}else{
												Toast.makeText(FoodDiary.this,"Network is not available....",Toast.LENGTH_SHORT).show();
											}
									}else{
										if(isInternetOn()){
											 CallDinnerListTask task = new CallDinnerListTask();
											 task.applicationContext =FoodDiary.this.getParent();
											 task.execute();
										}else{
											Toast.makeText(FoodDiary.this,"Network is not available....",Toast.LENGTH_SHORT).show();
										}
									}
									 
							}  
					      
							@Override
							protected String doInBackground(String... params) {
								// TODO Auto-generated method stub
								Log.i("doInBackground--Object", "doInBackground--Object");
								//ga.lstResult=obj.manageGoal(appPrefs.getGoalname().toString(), type, goalvalue);
								Global_Application.totalcal=0;
								lstResultSnacks = obj.FoodListing(Global_Application.url+"diet-tracker/?meal_type=SNACKS");
								return null;
							}
							   
						}  
						
						// async class for calling webservice and get responce message
						public class CallDinnerListTask extends AsyncTask <String, Void,String>
						{
							protected Context applicationContext;

							@Override
							protected void onPreExecute()     
							{
								
								//dialog = ProgressDialog.show(applicationContext, "Calling", "Please wait...", true);
								/*dialog1 = new ProgressDialog(getParent());
								dialog1.setMessage("Please Wait....");
								dialog1.show();*/
								Log.i("onPreExecute", "onPreExecute");
								
							}       
							
							protected void onPostExecute(String result)
							{
								
								Log.i("onPostExecute", "onPostExecute");
									dialog1.dismiss();
									Log.e("TAG","lst size : " + lstResultDinner.size());
									if(lstResultDinner.size()>0){
										lbldinner.setText("Dinner ("+lstResultDinner.get(0).getCount()+")" );
										lbldinnercal.setText(Global_Application.totalcal+"");
										DinnerAdapter adapter = new DinnerAdapter(FoodDiary.this,R.layout.dinner_food_list, lstResultDinner);
										lstViewDinner.setAdapter(adapter);
										adapter.notifyDataSetChanged();
										lstViewDinner.onRefreshComplete();
									}else{
										dialog1.dismiss();
									}
									 
							}  
					      
							@Override
							protected String doInBackground(String... params) {
								// TODO Auto-generated method stub
								Log.i("doInBackground--Object", "doInBackground--Object");
								//ga.lstResult=obj.manageGoal(appPrefs.getGoalname().toString(), type, goalvalue);
								Global_Application.totalcal=0;
								lstResultDinner = obj.FoodListing(Global_Application.url+"diet-tracker/?meal_type=DINNER");
								return null;
							}
							   
						} 
			// async class for calling webservice and get responce message
						public class CallBrkPullToRefreshTask extends AsyncTask <String, Void,String>
						{
							protected Context applicationContext;

							@Override
							protected void onPreExecute()     
							{
								
								//dialog = ProgressDialog.show(applicationContext, "Calling", "Please wait...", true);
								dialog1 = new ProgressDialog(getParent());
								dialog1.setCanceledOnTouchOutside(false);
								dialog1.setMessage("Please Wait....");
								dialog1.show();
								Log.i("onPreExecute", "onPreExecute");
								
							}       
							
							protected void onPostExecute(String result)
							{
								
								Log.i("onPostExecute", "onPostExecute");
									dialog1.dismiss();
									Log.e("TAG","lst size : " + lstResultBreakfast.size());
									if(lstResultBreakfast.size()>0 && frm.equals("b")){
										lblbrk.setText("Breakfast ("+lstResultBreakfast.get(0).getCount()+")");
										lbltotalbrkcal.setText(Global_Application.totalcal+"");
										BreakfastAdapter adapter = new BreakfastAdapter(FoodDiary.this,R.layout.breakfast_food_list, lstResultBreakfast);
									    lstViewBreakfast.setAdapter(adapter);
									    adapter.notifyDataSetChanged();
									    lstViewBreakfast.onRefreshComplete();
										
									}
									if(lstResultLunch.size()>0 && frm.equals("l")){
										lbllunch.setText("Lunch ("+lstResultLunch.get(0).getCount()+")");
										lbllunchcal.setText(Global_Application.totalcal+"");
										LunchAdapter adapter = new LunchAdapter(FoodDiary.this,R.layout.lunch_food_list, lstResultLunch);
									    lstViewLunch.setAdapter(adapter);
									    adapter.notifyDataSetChanged();
									    lstViewLunch.onRefreshComplete();
										
									}
									if(lstResultSnacks.size()>0 && frm.equals("l")){
										lblsnack.setText("Breakfast ("+lstResultSnacks.get(0).getCount()+")" );
										lblsnakcal.setText(Global_Application.totalcal+"");
										SnacksAdapter adapter = new SnacksAdapter(FoodDiary.this,R.layout.snacks_food_list, lstResultSnacks);
										lstViewSnacks.setAdapter(adapter);
										adapter.notifyDataSetChanged();
										lstViewSnacks.onRefreshComplete();
									}
									if(lstResultDinner.size()>0 && frm.equals("d")){
										lbldinner.setText("Breakfast ("+lstResultDinner.get(0).getCount()+")" );
										lbldinnercal.setText(Global_Application.totalcal+"");
										DinnerAdapter adapter = new DinnerAdapter(FoodDiary.this,R.layout.dinner_food_list, lstResultDinner);
										lstViewDinner.setAdapter(adapter);
										adapter.notifyDataSetChanged();
										lstViewDinner.onRefreshComplete();
									}
									 
							}  
					      
							@Override
							protected String doInBackground(String... params) {
								// TODO Auto-generated method stub
								Log.i("doInBackground--Object", "doInBackground--Object");
								//ga.lstResult=obj.manageGoal(appPrefs.getGoalname().toString(), type, goalvalue);
								if(frm.equals("b")){
									lstResultBreakfast.addAll(obj.FoodListing(nexturl));
								}else if(frm.equals("l")){
									lstResultLunch.addAll(obj.FoodListing(nexturl));
								}else if(frm.equals("s")){
									lstResultSnacks.addAll(obj.FoodListing(nexturl));
								}else if(frm.equals("d")){
									lstResultDinner.addAll(obj.FoodListing(nexturl));
								}
								
								
								return null;
							}
							   
						}     
	  @Override
		protected void onResume() {
			// TODO Auto-generated method stub
			super.onResume();
			if(isInternetOn()){
				 CallListTask task = new CallListTask();
				 task.applicationContext =this.getParent();
				 task.execute();
			}else{
				Toast.makeText(FoodDiary.this,"Network is not available....",Toast.LENGTH_SHORT).show();
			}
		}
	  
	  // async class for calling webservice and get responce message
		public class CallDeleteTask extends AsyncTask <String, Void,String>
		{
			protected Context applicationContext;

			@Override
			protected void onPreExecute()     
			{
				
				//dialog = ProgressDialog.show(applicationContext, "Calling", "Please wait...", true);
				dialog1 = new ProgressDialog(getParent());
				dialog1.setCanceledOnTouchOutside(false);
				dialog1.setMessage("Please Wait....");
				dialog1.show();
				Log.i("onPreExecute", "onPreExecute");
				
			}       
			
			protected void onPostExecute(String result)
			{
				dialog1.dismiss();
				Log.i("onPostExecute", "onPostExecute");
				if(isInternetOn()){
					 CallListTask task = new CallListTask();
					 task.applicationContext =FoodDiary.this.getParent();
					 task.execute();
				}else{
					Toast.makeText(FoodDiary.this,"Network is not available....",Toast.LENGTH_SHORT).show();
				}
					
			}  
	      
			@Override
			protected String doInBackground(String... params) {
				// TODO Auto-generated method stub
				Log.i("doInBackground--Object", "doInBackground--Object");
				
				//ga.lstResult=obj.manageGoal(appPrefs.getGoalname().toString(), type, goalvalue);
				return obj.DeleteFood(Global_Application.selectedfoodid);
			}
			   
		} 
		// async class for calling webservice and get responce message
		public class CalluserMeTask extends AsyncTask <String, Void,String>
		{
			protected Context applicationContext;

			@Override
			protected void onPreExecute()        
			{
				
				//dialog = ProgressDialog.show(applicationContext, "Calling", "Please wait...", true);
				dialog1 = new ProgressDialog(FoodDiary.this.getParent());
				dialog1.setCanceledOnTouchOutside(false);
				dialog1.setMessage("Please Wait....");
				dialog1.show();
				Log.i("onPreExecute", "onPreExecute");
				
			}        
			 
			protected void onPostExecute(String result)
			{
				
				Log.i("onPostExecute", "onPostExecute");
				//generateView();
				dialog1.dismiss();
			/*	Intent intent = new Intent(Goal.this,MainActivity.class);
				startActivity(intent);*/
			}  
	   
			@Override
			protected String doInBackground(String... params) {
				// TODO Auto-generated method stub
				Log.i("doInBackground--Object", "doInBackground--Object");
				//obj.GetUserProfile(ga.getLstfamilyglobal().get(Integer.parseInt(selecteduserid)).getId());
				return null;
			}
			   
		}     
		 @Override
		 public void onBackPressed() 
		 {
		          
		         
		 }
		 
	
		// function for check internet is available or not
					public final boolean isInternetOn() {

						  ConnectivityManager connec = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

						  if ((connec.getNetworkInfo(0).getState() == NetworkInfo.State.CONNECTED)
						    || (connec.getNetworkInfo(0).getState() == NetworkInfo.State.CONNECTING)
						    || (connec.getNetworkInfo(1).getState() == NetworkInfo.State.CONNECTING)
						    || (connec.getNetworkInfo(1).getState() == NetworkInfo.State.CONNECTED)) {
						   return true;
						  }

						  else if ((connec.getNetworkInfo(0).getState() == NetworkInfo.State.DISCONNECTED)
						    || (connec.getNetworkInfo(1).getState() ==  NetworkInfo.State.DISCONNECTED)) {
						   return false;
						  }

						  return false;
						 }

		
}
