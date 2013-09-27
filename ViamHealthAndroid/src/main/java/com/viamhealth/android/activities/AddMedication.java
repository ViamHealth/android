package com.viamhealth.android.activities;

import java.util.ArrayList;
import java.util.Calendar;

import com.viamhealth.android.Global_Application;
import com.viamhealth.android.R;
import com.viamhealth.android.ViamHealthPrefs;
import com.viamhealth.android.dao.restclient.functionClass;

import com.nostra13.universalimageloader.core.DisplayImageOptions;

import com.viamhealth.android.model.MedicalData;
import com.viamhealth.android.model.MedicationData;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class AddMedication extends BaseActivity implements OnClickListener{
	Display display;
	int height,width;
	ProgressDialog dialog1;
	
	int w10,h10,w15,w20,h40,w5,h20;
	
	LinearLayout newval_main_layout,newval_mid_layout,mid_layout,ddl_repeate_mode_layout,menu_invite_addfood,menu_invite_out_addfood,
				 settiglayout_food,newval_submid_layout,newval_btn_layout;
	EditText txt_name,txt_detail,txt_morning,txt_afternoon,txt_evening,txt_night,txt_time,txt_min,txt_hour,txt_day,txt_week,txt_day_interval;
	Spinner ddl_repeate_mode;
	TextView btnSave,btnCancle;
	TextView lbl_invite_user_food,heding_Addfood_name;
	ImageView back,person_icon;
	ImageView imgMorningMinus,imgMorningPlus,imgNoonMinus,imgNoonPlus,imgNightMinus,imgNightPlus;
	int morning,night,noon;
	TextView morningval,nightval,noonval;
	
	ArrayList<MedicalData> lstData = new ArrayList<MedicalData>();
	MedicationData medicationdt = new MedicationData();
	ArrayList<String> lst = new ArrayList<String>();
	
	private DisplayImageOptions options;
	ViamHealthPrefs appPrefs;
	functionClass obj;
	Global_Application ga;
	Typeface tf;
	Calendar dateAndTime=Calendar.getInstance();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
	    super.onCreate(savedInstanceState);
	    //super.getSupportActionBar().setTitle("Sharat Khurana");
	    
		setContentView(R.layout.add_medication);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	        
		appPrefs = new ViamHealthPrefs(AddMedication.this);
		obj=new functionClass(AddMedication.this);
		ga=((Global_Application)getApplicationContext());
		   
		tf = Typeface.createFromAsset(this.getAssets(),"Roboto-Condensed.ttf");
		// get screen height and width
		ScreenDimension();
		
		  
		w10=(int)((width*3.13)/100);  
		w15=(int)((width*4.68)/100);
		w20=(int)((width*6.25)/100);
		w5=(int)((width*1.56)/100);  
		
		h10=(int)((height*2.09)/100);
		h40=(int)((height*8.33)/100);
		h20=(int)((height*4.17)/100);
	
		
		newval_main_layout = (LinearLayout)findViewById(R.id.newval_main_layout);
		
		newval_btn_layout = (LinearLayout)findViewById(R.id.newval_btn_layout);
		
		
		newval_mid_layout = (LinearLayout)findViewById(R.id.newval_mid_layout);
		newval_mid_layout.setPadding(w10, h10, w10, h10);
		
		
		mid_layout = (LinearLayout)findViewById(R.id.mid_layout);
		//mid_layout.setPadding(w10, 0, w10,0);
		
		btnSave = (TextView)findViewById(R.id.btnSave);
		btnSave.setOnClickListener(AddMedication.this);
		
		btnCancle = (TextView)findViewById(R.id.btnCancle);
		btnCancle.setOnClickListener(AddMedication.this);
		
		//ddl_repeate_mode_layout = (LinearLayout)findViewById(R.id.ddl_repeate_mode_layout);
		
		
		txt_name = (EditText)findViewById(R.id.txt_name);
		txt_name.setTypeface(tf);
		
		imgMorningMinus = (ImageView)findViewById(R.id.imgMorningMinus);
		imgMorningMinus.setOnClickListener(AddMedication.this);
		imgMorningPlus = (ImageView)findViewById(R.id.imgMorningPlus);
		imgMorningPlus.setOnClickListener(AddMedication.this);
		
		imgNoonMinus = (ImageView)findViewById(R.id.imgNoonMinus);
		imgNoonMinus.setOnClickListener(AddMedication.this);
		imgNoonPlus = (ImageView)findViewById(R.id.imgNoonPlus);
		imgNoonPlus.setOnClickListener(AddMedication.this);
		
		imgNightMinus = (ImageView)findViewById(R.id.imgNightMinus);
		imgNightMinus.setOnClickListener(AddMedication.this);
		imgNightPlus = (ImageView)findViewById(R.id.imgNightPlus);
		imgNightPlus.setOnClickListener(AddMedication.this);
		
		morningval = (TextView)findViewById(R.id.morningval);
		noonval = (TextView)findViewById(R.id.noonval);
		nightval = (TextView)findViewById(R.id.nightval);
		
		/*	txt_detail = (EditText)findViewById(R.id.txt_detail);
		txt_detail.setTypeface(tf);
		
		txt_time = (EditText)findViewById(R.id.txt_time);
		txt_time.setTypeface(tf);
		
		txt_morning = (EditText)findViewById(R.id.txt_morning);
		txt_morning.setTypeface(tf);*/
		
	/*	txt_afternoon = (EditText)findViewById(R.id.txt_afternoon);
		txt_afternoon.setTypeface(tf);
		
		txt_evening = (EditText)findViewById(R.id.txt_evening);
		txt_evening.setTypeface(tf);*/
		
	/*	txt_night = (EditText)findViewById(R.id.txt_night);
		txt_night.setTypeface(tf);
		
		txt_min = (EditText)findViewById(R.id.txt_min);
		txt_min.setTypeface(tf);
		
		txt_hour = (EditText)findViewById(R.id.txt_hour);
		txt_hour.setTypeface(tf);
		
		txt_day = (EditText)findViewById(R.id.txt_day);
		txt_day.setTypeface(tf);
		
		txt_week = (EditText)findViewById(R.id.txt_week);
		txt_week.setTypeface(tf);
		
		txt_day_interval = (EditText)findViewById(R.id.txt_day_interval);
		txt_day_interval.setTypeface(tf);
		*/
		//updateLabelTime();
		
		
	/*
		lst.add("--Select--");
		lst.add("NONE");
		lst.add("MONTHLY");
		lst.add("WEEKLY");
		lst.add("DAILY");
		lst.add("N_DAYS_INTERVAL");
		lst.add("X_WEEKDAY_MONTHLY");
		
		ddl_repeate_mode = new Spinner(this.getParent());
		ddl_repeate_mode_layout.addView(ddl_repeate_mode);
		ddl_repeate_mode.setBackgroundResource(R.drawable.spinner_bg);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getParent(),android.R.layout.simple_spinner_item, lst);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		ddl_repeate_mode.setAdapter(adapter);
		ddl_repeate_mode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				// TODO Auto-generated method stub
				if(position>1){
					txt_min.setVisibility(View.VISIBLE);
					txt_hour.setVisibility(View.VISIBLE);
					txt_day.setVisibility(View.VISIBLE);
					txt_day_interval.setVisibility(View.VISIBLE);
					txt_week.setVisibility(View.VISIBLE);
				}else{
					txt_min.setVisibility(View.GONE);
					txt_hour.setVisibility(View.GONE);
					txt_day.setVisibility(View.GONE);
					txt_day_interval.setVisibility(View.GONE);
					txt_week.setVisibility(View.GONE);
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		});*/

		
		
		
		// for update
		
		if(ga.getUpdate().equals("1")){
			
		}
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

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v==btnCancle){
			finish();
		}
		if(v==btnSave){
			if(validation()){
				if(isInternetOn()){
					if(ga.getUpdate().equals("1")){
						ga.setUpdate("0");
						 CallUpdateMedicalTask task = new CallUpdateMedicalTask();
						 task.applicationContext =AddMedication.this;
						 task.execute();
					}else{
						 CallMedicalTask task = new CallMedicalTask();
						 task.applicationContext =AddMedication.this;
						 task.execute();
					}
				}else{
					Toast.makeText(AddMedication.this,"Network is not available....",Toast.LENGTH_SHORT).show();
				}
			}
		}
		if(v==txt_time){
			 new TimePickerDialog(AddMedication.this, t,
                     dateAndTime.get(Calendar.HOUR_OF_DAY),
                     dateAndTime.get(Calendar.MINUTE),
                     true).show();
		}
		if(v==imgMorningMinus){
			
			morning = Integer.parseInt(morningval.getText().toString());
			if(morning!=0){
				morning--;
			}
			morningval.setText(morning+"");
		}
		if(v==imgMorningPlus){
			morning = Integer.parseInt(morningval.getText().toString());
			morning++;
			morningval.setText(morning+"");
		}
		
		if(v==imgNoonMinus){
			
			noon = Integer.parseInt(noonval.getText().toString());
			if(noon!=0){
				noon--;
			}
			noonval.setText(noon+"");
		}
		if(v==imgNoonPlus){
			noon = Integer.parseInt(noonval.getText().toString());
			noon++;
			noonval.setText(noon+"");
		}
		
		if(v==imgNightMinus){
			
			night = Integer.parseInt(nightval.getText().toString());
			if(night!=0){
				night--;
			}
			nightval.setText(night+"");
		}
		if(v==imgNightPlus){
			night = Integer.parseInt(nightval.getText().toString());
			night++;
			nightval.setText(night+"");
		}
	}
	 private void updateLabelTime() {
		    
		    txt_time.setText(dateAndTime.HOUR); //+":" +dateAndTime.MINUTE +":"+dateAndTime.SECOND);
		  }
	
	  TimePickerDialog.OnTimeSetListener t=new TimePickerDialog.OnTimeSetListener() {
	    public void onTimeSet(TimePicker view, int hourOfDay,
	                          int minute) {
	      dateAndTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
	      dateAndTime.set(Calendar.MINUTE, minute);
	      updateLabelTime();
	    }
	  };
	public boolean validation(){
		boolean valid = true;
		if(txt_name.getText().length()==0){
			txt_name.setError("Enter name");
			valid = false;
		}
		if(txt_detail.getText().length()==0){
			txt_detail.setError("Enter detail");
			valid = false;
		}
		if(txt_time.getText().length()==0){
			txt_time.setError("Enter time");
			valid=false;
		}
		return valid;
	}
	// async class for calling webservice and get responce message
	public class CallMedicalTask extends AsyncTask <String, Void,String>
	{
		protected Context applicationContext;

		@Override
		protected void onPreExecute()     
		{
			
			//dialog = ProgressDialog.show(applicationContext, "Calling", "Please wait...", true);
			dialog1 = new ProgressDialog(AddMedication.this);
			dialog1.setCanceledOnTouchOutside(false);
			dialog1.setMessage("Please Wait....");
			dialog1.show();
			Log.i("onPreExecute", "onPreExecute");
			
		}       
		
		protected void onPostExecute(String result)
		{
			
			Log.i("onPostExecute", "onPostExecute");
				dialog1.dismiss();
				//listfood.removeAllViews();
				Log.e("TAG","size : " + lstData.size());
				if(lstData.size()>0){
					finish();
				}else{
					//Toast.makeText(getParent(), "Try again lalter...",Toast.LENGTH_SHORT).show();
					finish();
				}
				 
		}  

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			Log.i("doInBackground--Object", "doInBackground--Object");
			//ga.lstResult=obj.manageGoal(appPrefs.getGoalname().toString(), type, goalvalue);
			lstData = obj.addMedication(txt_name.getText().toString(),
					 					txt_detail.getText().toString(), 
					 					txt_morning.getText().toString(),
					 					txt_afternoon.getText().toString(),
					 					txt_evening.getText().toString(),
					 					txt_night.getText().toString(), 
					 					txt_time.getText().toString(), 
					 					txt_hour.getText().toString(),
					 					txt_day.getText().toString(), 
					 					ddl_repeate_mode.getSelectedItem().toString(),
					 					txt_min.getText().toString(), 
					 					txt_week.getText().toString(), 
					 					txt_day_interval.getText().toString());
				return null;
		}
		   
	}     
	// async class for calling webservice and get responce message
		public class CallUpdateMedicalTask extends AsyncTask <String, Void,String>
		{
			protected Context applicationContext;

			@Override
			protected void onPreExecute()     
			{
				
				//dialog = ProgressDialog.show(applicationContext, "Calling", "Please wait...", true);
				dialog1 = new ProgressDialog(AddMedication.this);
				dialog1.setMessage("Please Wait....");
				dialog1.show();
				Log.i("onPreExecute", "onPreExecute");
				
			}       
			
			protected void onPostExecute(String result)
			{
				
				Log.i("onPostExecute", "onPostExecute");
					dialog1.dismiss();
					//listfood.removeAllViews();
					Log.e("TAG","size : " + lstData.size());
					if(lstData.size()>0){
						finish();
					}else{
						//Toast.makeText(getParent(), "Try again lalter...",Toast.LENGTH_SHORT).show();
						finish();
					}
					 
			}  

			@Override
			protected String doInBackground(String... params) {
				// TODO Auto-generated method stub
				Log.i("doInBackground--Object", "doInBackground--Object");
				//ga.lstResult=obj.manageGoal(appPrefs.getGoalname().toString(), type, goalvalue);
				lstData = obj.UpdateMedication(ga.getWatchupdate(),
											txt_name.getText().toString(),
						 					txt_detail.getText().toString(), 
						 					txt_morning.getText().toString(),
						 					txt_afternoon.getText().toString(),
						 					txt_evening.getText().toString(),
						 					txt_night.getText().toString(), 
						 					txt_time.getText().toString(), 
						 					txt_hour.getText().toString(),
						 					txt_day.getText().toString(), 
						 					ddl_repeate_mode.getSelectedItem().toString(),
						 					txt_min.getText().toString(), 
						 					txt_week.getText().toString(), 
						 					txt_day_interval.getText().toString());
					return null;
			}
			   
		}     
	// async class for calling webservice and get responce message
			public class CallgetdataTask extends AsyncTask <String, Void,String>
			{
				protected Context applicationContext;

				@Override
				protected void onPreExecute()     
				{
					
					//dialog = ProgressDialog.show(applicationContext, "Calling", "Please wait...", true);
					dialog1 = new ProgressDialog(AddMedication.this);
					dialog1.setCanceledOnTouchOutside(false);
					dialog1.setMessage("Please Wait....");
					dialog1.show();
					Log.i("onPreExecute", "onPreExecute");
					
				}       
				
				protected void onPostExecute(String result)
				{
					
					Log.i("onPostExecute", "onPostExecute");
						dialog1.dismiss();
						txt_name.setText(medicationdt.getName());
						txt_detail.setText(medicationdt.getDetails());
						txt_morning.setText(medicationdt.getMorning_count());
						txt_afternoon.setText(medicationdt.getAfternoon_count());
						txt_evening.setText(medicationdt.getEvening_count());
						txt_night.setText(medicationdt.getNight_count());
						txt_time.setText(medicationdt.getStart_timestamp());
						txt_min.setText(medicationdt.getRepeat_min());
						txt_hour.setText(medicationdt.getRepeat_hour());
						txt_day.setText(medicationdt.getRepeat_day());
						txt_week.setText(medicationdt.getRepeat_weekday());
						txt_day_interval.setText(medicationdt.getRepeat_day_interval());
						for(int i=0;i<lst.size();i++){
							if(medicationdt.getRepeat_mode().toString().equals(lst.get(i))){
								ddl_repeate_mode.setSelection(i);
								txt_min.setVisibility(View.VISIBLE);
								txt_hour.setVisibility(View.VISIBLE);
								txt_day.setVisibility(View.VISIBLE);
								txt_day_interval.setVisibility(View.VISIBLE);
								txt_week.setVisibility(View.VISIBLE);
							}
						}
				}  
		   
				@Override
				protected String doInBackground(String... params) {
					// TODO Auto-generated method stub
					Log.i("doInBackground--Object", "doInBackground--Object");
					
					medicationdt=obj.getMedicationByID(ga.getWatchupdate());
					return null;
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

	
}
