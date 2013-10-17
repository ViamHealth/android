package com.viamhealth.android.activities;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.viamhealth.android.Global_Application;
import com.viamhealth.android.adapters.GoalDataAdapter;
import com.viamhealth.android.R;
import com.viamhealth.android.ViamHealthPrefs;

import com.viamhealth.android.dao.db.DataBaseAdapter;

import com.viamhealth.android.dao.restclient.functionClass;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
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
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class AddWeightValue extends BaseActivity implements OnClickListener {
	private static ProgressDialog dialog;
	
	Display display;
	int height,width;
	int w10,h10,w5,h5,h1,w220,w15,w20,h40,w25,h20;
	   
 
	LinearLayout header_layout,newval_main_layout,newval_mid_layout,newval_bottom_layout,newval_txt_layout,newval_btn_layout,
				menu_invite_out_newval,setting_layout,menu_invite_newval,back_layout,btn_time_picker,btn_date_picker,mid_layout,
				blood_presure_layout,weight_goal_layout,settiglayout_food,menu_invite_food,menu_invite_out_food,weigth_measure_layout,
				newval_submid_layout;
	ImageView newval_icon,back,person_icon;
	TextView lbl_newval_date,lbl_newval_time,heding_name_newval,
			lbl_invite_user_goal,lblback,goal_name,goal_desc,lbl_add_val,lbl_add_val_unit,lbl_invite_user_food,heding_name_food;
	TextView btnSave,btnCancle;
	EditText txt_goal_weight;
	Spinner ddl_weight_measure;
	
	String value;
	Typeface tf,digittf;
	
	ViamHealthPrefs appPrefs;
	functionClass obj;
	Global_Application ga;
	
	DateFormat fmtDateAndTime=DateFormat.getDateTimeInstance();
    Calendar dateAndTime=Calendar.getInstance();
    int pYear,pMonth,pDay;
	
    DataBaseAdapter dbObj;
    private DisplayImageOptions options;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);  
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		
		setContentView(R.layout.add_weight_value);
		
		appPrefs = new ViamHealthPrefs(AddWeightValue.this);
		ga=((Global_Application)getApplicationContext());
		obj=new functionClass(AddWeightValue.this);
		dbObj=new DataBaseAdapter(AddWeightValue.this);
		
		
		tf = Typeface.createFromAsset(this.getAssets(),"Roboto-Condensed.ttf");
		digittf = Typeface.createFromAsset(this.getAssets(),"digital-7 (mono).ttf");
		//for get screen height width
        ScreenDimension();
        
        //calculate dynamic padding
        w10=(int)((width*3.13)/100);
        w15=(int)((width*4.68)/100);
        w20=(int)((width*6.25)/100);
        w25=(int)((width*7.85)/100);
        w5=(int)((width*1.56)/100);
        w220=(int)((width*68.75)/100);
		
		h10=(int)((height*2.08)/100);
	    h5=(int)((height*1.042)/100);
		h1=(int)((height*0.21)/100);
		h40=(int)((height*8.34)/100);
		h20=(int)((height*4.17)/100);
		
		//casting control and manage padding and call onclick method
		/*back=(ImageView)findViewById(R.id.back);
    	back.setOnClickListener(AddWeightValue.this);
		
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
 				Animation anim = AnimationUtils.loadAnimation(AddWeightValue.this, R.anim.fade_in);
 				person_icon.setAnimation(anim);
 				anim.start();
 				
 				
 			}
 		});
 		

		
		header_layout=(LinearLayout)findViewById(R.id.header_layout);
		header_layout.setPadding(0, h5, 0, h5);*/
		
		btnSave = (TextView)findViewById(R.id.btnSave);
		btnSave.setTypeface(tf);
		btnSave.setOnClickListener(this);
		
		btnCancle=(TextView)findViewById(R.id.btnCancle);
		btnCancle.setTypeface(tf);
		btnCancle.setOnClickListener(this);
		
		newval_main_layout = (LinearLayout)findViewById(R.id.newval_main_layout);
		newval_main_layout.setPadding(w10, h10, w10, 0);
		
		newval_mid_layout = (LinearLayout)findViewById(R.id.newval_mid_layout);
		newval_mid_layout.setPadding(w10, 0, w10, h10);
	
		/*newval_btn_layout = (LinearLayout)findViewById(R.id.newval_btn_layout);
		newval_btn_layout.setPadding(w10, h10, w10, h10);
		*/
		btn_time_picker = (LinearLayout)findViewById(R.id.btn_time_picker);
		btn_time_picker.setPadding(w5, 0, w5, 0);
		btn_time_picker.setOnClickListener(this);
		
		btn_date_picker = (LinearLayout)findViewById(R.id.btn_date_picker);
		btn_date_picker.setPadding(w5, 0, 0, 0);
		btn_date_picker.setOnClickListener(this);
		
		/*newval_icon = (ImageView)findViewById(R.id.newval_icon);
		newval_icon.setPadding(w5, h5, w5, h5);
		
	*/
		  
		lbl_newval_date = (TextView)findViewById(R.id.lbl_newval_date);
		lbl_newval_date.setTypeface(tf);
		
		lbl_newval_time = (TextView)findViewById(R.id.lbl_newval_time);
		lbl_newval_time.setTypeface(tf);
		
		/*goal_name=(TextView)findViewById(R.id.goal_name);
		goal_name.setTypeface(tf,Typeface.BOLD);*/
		
		newval_submid_layout = (LinearLayout)findViewById(R.id.newval_submid_layout);
		newval_submid_layout.setPadding(0, h10, 0, 0);
		
		txt_goal_weight = (EditText)findViewById(R.id.txt_goal_weight);
		txt_goal_weight.setTypeface(tf);
		
		weigth_measure_layout= (LinearLayout)findViewById(R.id.weigth_measure_layout);
		
		ArrayList<String> lst = new ArrayList<String>();
		lst.add("METRIC");
		lst.add("STANDARD");
		
	    ddl_weight_measure = new Spinner(AddWeightValue.this);
	    ddl_weight_measure.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				 ((TextView) parent.getChildAt(0)).setTextColor(Color.BLACK);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		});
	    
		weigth_measure_layout.addView(ddl_weight_measure);
		ddl_weight_measure.setBackgroundResource(R.drawable.spinner_bg);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(AddWeightValue.this,android.R.layout.simple_spinner_item, lst);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		ddl_weight_measure.setAdapter(adapter);

		
    	//actionmenu();
	

		pYear = dateAndTime.get(Calendar.YEAR);
	    pMonth = dateAndTime.get(Calendar.MONTH);
	    pDay = dateAndTime.get(Calendar.DAY_OF_MONTH);	    
	    String s=(pMonth + 1)+"-"+pDay+"-"+pYear;
	    SimpleDateFormat curFormater = new SimpleDateFormat("MM-dd-yyyy"); 
        Date dateObj=null;
	   try
	   {
	    dateObj = curFormater.parse(s);
	    
	   }
	   catch (ParseException e) 
	   {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	   } 
	   
	   SimpleDateFormat postFormater = new SimpleDateFormat("yyyy-MM-dd"); 
	   String newDateStr = postFormater.format(dateObj);
	   lbl_newval_date.setText(newDateStr);
	   
	   
	   if(ga.isWeightupdate()){
		   // get already added record
	   }
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
		    		Animation anim = AnimationUtils.loadAnimation(AddWeightValue.this, R.anim.fade_out);
					settiglayout_food.startAnimation(anim);
					settiglayout_food.setVisibility(View.INVISIBLE);
					menu_invite_food.setVisibility(View.VISIBLE);
					menu_invite_out_food.setVisibility(View.INVISIBLE);
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
			Intent i=new Intent(AddWeightValue.this, Home.class);
			startActivity(i);
			finish();
		}
		if(v==btnCancle){
			finish();
		}
		if(v==btnSave){
			if(othervalidation()){
				if(isInternetOn()){
					 CallAddReadingTask task = new CallAddReadingTask();
					 task.applicationContext =AddWeightValue.this;
					 task.execute();
				}else{
					Toast.makeText(AddWeightValue.this,"Network is not available....",Toast.LENGTH_SHORT).show();
				}
			}
		}
			
		//}
		if(v==lbl_invite_user_food){
			Log.e("TAG","Selected value is " + "invite user is clicked");
			Animation anim = AnimationUtils.loadAnimation(AddWeightValue.this, R.anim.fade_out);
			settiglayout_food.startAnimation(anim);
			settiglayout_food.setVisibility(View.INVISIBLE);
			menu_invite_food.setVisibility(View.VISIBLE);
			menu_invite_out_food.setVisibility(View.INVISIBLE);
			Log.e("TAG","Clicked");
			Intent i = new Intent(AddWeightValue.this,InviteUser.class);
			startActivity(i);
		}
		if(v==btn_date_picker){
			//appPrefs.setDateAdded("0");
			new DatePickerDialog(AddWeightValue.this, d,pYear,
                    pMonth,
                    pDay).show();
         }
		if(v==btn_time_picker){
			 new TimePickerDialog(AddWeightValue.this, t,
                     dateAndTime.get(Calendar.HOUR_OF_DAY),
                     dateAndTime.get(Calendar.MINUTE),
                     true).show();
		}
		
		if(v==menu_invite_food){
			actionmenu();
		settiglayout_food.setVisibility(View.VISIBLE);
		menu_invite_out_food.setVisibility(View.VISIBLE);
		menu_invite_food.setVisibility(View.INVISIBLE);
		Animation anim = AnimationUtils.loadAnimation(AddWeightValue.this, R.anim.fade_in);
		settiglayout_food.startAnimation(anim);
		
		Log.e("TAG","Clicked");
	}
	if(v==menu_invite_out_food){
		Animation anim = AnimationUtils.loadAnimation(AddWeightValue.this, R.anim.fade_out);
		settiglayout_food.startAnimation(anim);
		settiglayout_food.setVisibility(View.INVISIBLE);
		menu_invite_food.setVisibility(View.VISIBLE);
		menu_invite_out_food.setVisibility(View.INVISIBLE);
		Log.e("TAG","Clicked");
	}
		
		}
	
	
	public boolean othervalidation(){
		boolean valid=true;
		if(txt_goal_weight.getText().length()==0){
			txt_goal_weight.setError("Enter weight value");
			valid=false;
		}
		return valid;
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
		  String s=(pMonth + 1)+"-"+pDay+"-"+pYear;
	        SimpleDateFormat curFormater = new SimpleDateFormat("MM-dd-yyyy"); 
	         Date dateObj=null;
		   try
		   {
		    dateObj = curFormater.parse(s);
		    
		   }
		   catch (ParseException e) 
		   {
		    // TODO Auto-generated catch block
		    e.printStackTrace();
		   } 
		   
		   SimpleDateFormat postFormater = new SimpleDateFormat("yyyy-MM-dd"); 
		   String newDateStr = postFormater.format(dateObj);
		   lbl_newval_date.setText(newDateStr);
	    }
	  private void updateLabelTime() {
		    
		    lbl_newval_time.setText(dateAndTime.HOUR +":" +dateAndTime.MINUTE +":"+dateAndTime.SECOND);
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
		public class CallAddReadingTask extends AsyncTask <String, Void,String>
		{
			protected Context applicationContext;

			@Override
			protected void onPreExecute()     
			{
				
				//dialog = ProgressDialog.show(applicationContext, "Calling", "Please wait...", true);
				dialog = new ProgressDialog(AddWeightValue.this);
				dialog.setCanceledOnTouchOutside(false);
				dialog.setMessage("Please Wait....");
				dialog.show();
				Log.i("onPreExecute", "onPreExecute");
				
			}       
			
			protected void onPostExecute(String result)
			{     
				
				Log.i("onPostExecute", "onPostExecute");
				if(result.toString().equals("0")){
					dialog.dismiss();
					appPrefs.setReloadgraph("1");
					ga.setCalcelflg(true);
					finish();
				}else{
					dialog.dismiss();
					ga.setCalcelflg(true);
					//Toast.makeText(AddNewValue.this, "Value not added successfully..",Toast.LENGTH_SHORT).show();
					finish();
				}
			}  
	   
			@Override
			protected String doInBackground(String... params) {
				// TODO Auto-generated method stub
				Log.i("doInBackground--Object", "doInBackground--Object");
				
				return obj.addWeightReading(txt_goal_weight.getText().toString(), 
										    ddl_weight_measure.getSelectedItem().toString(), 
										    lbl_newval_date.getText().toString(), ga.getWeightid().toString());
			}
			   
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
		public interface OnFragmentClickListener {
		    public void onFragmentClick(int action, Object object);
		}
		
		 @Override
			protected void onResume() {
				// TODO Auto-generated method stub
				super.onResume();
				
			}
		 @Override
		 public void onBackPressed() 
		 {
		          
		         
		 }
		
}