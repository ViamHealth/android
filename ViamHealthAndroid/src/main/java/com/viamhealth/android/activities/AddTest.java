package com.viamhealth.android.activities;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.viamhealth.android.Global_Application;
import com.viamhealth.android.R;
import com.viamhealth.android.ViamHealthPrefs;

import com.viamhealth.android.dao.restclient.functionClass;
import com.viamhealth.android.model.MedicalData;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class AddTest extends BaseActivity implements OnClickListener{
	Display display;
	int height,width;
	ProgressDialog dialog1;
	
	int w10,h10,w15,w20,h40,w5,h20;
	
	LinearLayout newval_main_layout,newval_mid_layout,mid_layout,ddl_repeate_mode_layout,menu_invite_addfood,menu_invite_out_addfood,
				 settiglayout_food;
	EditText txt_name,txt_date,txt_comment;//,txt_min,txt_hour,txt_day,txt_week,txt_day_interval;
	Spinner ddl_repeate_mode;
	TextView btnSave,btnCancle;
	TextView lbl_invite_user_food,heding_Addfood_name;
	ImageView back,person_icon;
	
	ArrayList<MedicalData> lstData = new ArrayList<MedicalData>();
	MedicalData mediData = new MedicalData();
	ArrayList<String> lst = new ArrayList<String>();
	
	private DisplayImageOptions options;
	ViamHealthPrefs appPrefs;
	functionClass obj;
	Global_Application ga;
	Typeface tf;
	 DateFormat fmtDateAndTime=DateFormat.getDateTimeInstance();
     Calendar dateAndTime=Calendar.getInstance();
     int pYear,pMonth,pDay;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
	    super.onCreate(savedInstanceState);
	    //super.getSupportActionBar().setTitle("Sharat Khurana");
	    
		setContentView(R.layout.add_test);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	        
		appPrefs = new ViamHealthPrefs(AddTest.this);
		obj=new functionClass(AddTest.this);
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
		
		newval_mid_layout = (LinearLayout)findViewById(R.id.newval_mid_layout);
		newval_mid_layout.setPadding(w10, 0, w10, 0);
		
		mid_layout = (LinearLayout)findViewById(R.id.mid_layout);
		
		btnSave = (TextView)findViewById(R.id.btnSave);
		btnSave.setOnClickListener(AddTest.this);
		
		btnCancle = (TextView)findViewById(R.id.btnCancle);
		btnCancle.setOnClickListener(AddTest.this);
		
		
		txt_name = (EditText)findViewById(R.id.txt_name);
		txt_name.setTypeface(tf);
		
		txt_date = (EditText)findViewById(R.id.txt_date);
		txt_date.setOnClickListener(AddTest.this);
		txt_date.setTypeface(tf);
		
		txt_comment = (EditText)findViewById(R.id.txt_comment);
		txt_comment.setTypeface(tf);

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
		   txt_date.setText(newDateStr);
		// for update
		if(ga.getUpdate().equals("1")){
			if(isInternetOn()){
				     CallgetdataTask task = new CallgetdataTask();
					 task.applicationContext =AddTest.this;
					 task.execute();
				 
			}else{
				Toast.makeText(AddTest.this,"Network is not available....",Toast.LENGTH_SHORT).show();
			}
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
			/*if(validation()){
				if(isInternetOn()){
					if(ga.getUpdate().equals("1")){
						ga.setUpdate("0");
						 CallUpdateMedicalTask task = new CallUpdateMedicalTask();
						 task.applicationContext =this.getParent();
						 task.execute();
					}else{
						 CallMedicalTask task = new CallMedicalTask();
						 task.applicationContext =this.getParent();
						 task.execute();
					 }
				}else{
					Toast.makeText(AddTest.this,"Network is not available....",Toast.LENGTH_SHORT).show();
				}
			}*/
			finish();
		}
		if(v==txt_date){
			new DatePickerDialog(AddTest.this, d,pYear,
                    pMonth,
                    pDay).show();
		}
	}
	DatePickerDialog.OnDateSetListener d=new DatePickerDialog.OnDateSetListener() {
	    public void onDateSet(DatePicker view, int year, int monthOfYear,
	                          int dayOfMonth) {
	      pMonth=monthOfYear;
	      pDay=dayOfMonth;
	      pYear=year;
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
		   txt_date.setText(newDateStr);
	    }
	  };
	/* private void updateLabelTime() {
		    
		 txt_date.setText(dateAndTime.HOUR); //+":" +dateAndTime.MINUTE +":"+dateAndTime.SECOND);
		  }
	
	  TimePickerDialog.OnTimeSetListener t=new TimePickerDialog.OnTimeSetListener() {
	    public void onTimeSet(TimePicker view, int hourOfDay,
	                          int minute) {
	      dateAndTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
	      dateAndTime.set(Calendar.MINUTE, minute);
	      updateLabelTime();
	    }
	  };*/
	public boolean validation(){
		boolean valid = true;
		/*if(txt_name.getText().length()==0){
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
		}*/
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
			dialog1 = new ProgressDialog(AddTest.this);
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
		/*	lstData = obj.addMedical(txt_name.getText().toString(), 
									 txt_detail.getText().toString(), 
									 txt_time.getText().toString(), 
									 txt_hour.getText().toString(), 
									 txt_day.getText().toString(), 
									 ddl_repeate_mode.getSelectedItem().toString(), 
									 txt_min.getText().toString(), 
									 txt_week.getText().toString(),
									 txt_day_interval.getText().toString());	*/
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
					dialog1 = new ProgressDialog(AddTest.this);
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
					/*lstData = obj.UpdateMedical(ga.getWatchupdate(),
												txt_name.getText().toString(),
							 					txt_detail.getText().toString(), 
							 					txt_time.getText().toString(), 
							 					txt_hour.getText().toString(),
							 					txt_day.getText().toString(), 
							 					ddl_repeate_mode.getSelectedItem().toString(),
							 					txt_min.getText().toString(), 
							 					txt_week.getText().toString(), 
							 					txt_day_interval.getText().toString());*/
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
								dialog1 = new ProgressDialog(AddTest.this);
								dialog1.setMessage("Please Wait....");
								dialog1.show();
								Log.i("onPreExecute", "onPreExecute");
								
							}       
							
							protected void onPostExecute(String result)
							{
								
								/*Log.i("onPostExecute", "onPostExecute");
									dialog1.dismiss();
									txt_name.setText(mediData.getName());
									txt_detail.setText(mediData.getDetail());
									txt_time.setText(mediData.getStart_timestamp());
									txt_min.setText(mediData.getRepeat_min());
									txt_hour.setText(mediData.getRepeat_hour());
									txt_day.setText(mediData.getRepeat_day());
									txt_week.setText(mediData.getRepeat_weekday());
									txt_day_interval.setText(mediData.getRepeat_day_interval());
									for(int i=0;i<lst.size();i++){
										if(mediData.getRepeat_mode().toString().equals(lst.get(i))){
											ddl_repeate_mode.setSelection(i);
											txt_min.setVisibility(View.VISIBLE);
											txt_hour.setVisibility(View.VISIBLE);
											txt_day.setVisibility(View.VISIBLE);
											txt_day_interval.setVisibility(View.VISIBLE);
											txt_week.setVisibility(View.VISIBLE);
										}
									}*/
							}  
					   
							@Override
							protected String doInBackground(String... params) {
								// TODO Auto-generated method stub
								Log.i("doInBackground--Object", "doInBackground--Object");
								
								mediData=obj.getMedicalByID(ga.getWatchupdate());
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
