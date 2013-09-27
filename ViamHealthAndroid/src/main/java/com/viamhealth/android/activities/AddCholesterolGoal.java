package com.viamhealth.android.activities;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.viamhealth.android.Global_Application;
import com.viamhealth.android.R;
import com.viamhealth.android.ViamHealthPrefs;

import com.viamhealth.android.dao.restclient.functionClass;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class AddCholesterolGoal extends Activity implements OnClickListener{
	Display display;
	int width,height;
	int w10,w15,w20,w25,w5,w220,h10,h5,h1,h40,h20,h15;
	
	TextView lbl_hdl,lbl_hdl_number,lbl_ldl,lbl_ldl_number,lbl_triglyceriders,lbl_trigly_number,lbl_total,lbl_total_number,
			 lbl_add_goal,btn_goal_cancle,btn_goal_add;
	SeekBar seekbar_bar_hdl,seekbar_bar_ldl,seekbar_bar_triglycerides,seekbar_bar_total;
	EditText txt_target_date;
	LinearLayout middle_goal_layout;
	Spinner ddl_interval_unit,ddl_interval_num;
	ProgressDialog dialog;
	
	ViamHealthPrefs appPrefs;
	Global_Application ga;
	functionClass obj;
	
	 DateFormat fmtDateAndTime=DateFormat.getDateTimeInstance();
     Calendar dateAndTime=Calendar.getInstance();
     int pYear,pMonth,pDay;
	
	Typeface tf,digittf;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);  
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		
		setContentView(R.layout.add_cholesterol_goal);
		
		appPrefs = new ViamHealthPrefs(AddCholesterolGoal.this);
		ga=((Global_Application)getApplicationContext());
		obj=new functionClass(AddCholesterolGoal.this);
		
		
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
        w220=(int)((width*56.25)/100);
		
		h10=(int)((height*2.08)/100);
	    h5=(int)((height*1.042)/100);
		h1=(int)((height*0.21)/100);
		h40=(int)((height*8.34)/100);
		h20=(int)((height*4.17)/100);
		h15=(int)((height*3.13)/100);
		
		//casting control and manage padding and call onclick method
		middle_goal_layout=(LinearLayout)findViewById(R.id.middle_goal_layout);
		middle_goal_layout.setPadding(w20, h20, w20, h10);
		
		lbl_add_goal = (TextView)findViewById(R.id.lbl_add_goal);
		lbl_add_goal.setPadding(w15, h15, w15, h15);
		
		lbl_hdl = (TextView)findViewById(R.id.lbl_hdl);
		lbl_hdl.setTypeface(tf);
		lbl_hdl.setPadding(w10, h10, w10, h10);
		
		lbl_hdl_number = (TextView)findViewById(R.id.lbl_hdl_number);
		lbl_hdl_number.setTypeface(digittf);
		lbl_hdl_number.setPadding(w5, 0, 0, 0);
		
		lbl_ldl = (TextView)findViewById(R.id.lbl_ldl);
		lbl_ldl.setTypeface(tf);
		lbl_ldl.setPadding(w10, h10, w10, h10);
		
		lbl_ldl_number = (TextView)findViewById(R.id.lbl_ldl_number);
		lbl_ldl_number.setTypeface(digittf);
		//lbl_ldl_number.setPadding(w5, 0, 0, 0);
		
		lbl_triglyceriders = (TextView)findViewById(R.id.lbl_triglyceriders);
		lbl_triglyceriders.setTypeface(tf);
		lbl_triglyceriders.setPadding(w10, h10, w10, h10);
		
		lbl_trigly_number = (TextView)findViewById(R.id.lbl_trigly_number);
		lbl_trigly_number.setTypeface(digittf);
		lbl_trigly_number.setPadding(w5, 0, 0, 0);

		lbl_total = (TextView)findViewById(R.id.lbl_total);
		lbl_total.setTypeface(tf);
	//	lbl_total.setPadding(w5, 0, 0, 0);
		
		lbl_total_number = (TextView)findViewById(R.id.lbl_total_number);
		lbl_total_number.setTypeface(digittf);
		lbl_total_number.setPadding(w5, 0, 0, 0);
		
		txt_target_date = (EditText)findViewById(R.id.txt_target_date);
		txt_target_date.setTypeface(tf);
		txt_target_date.setOnClickListener(AddCholesterolGoal.this);
		
		seekbar_bar_hdl=(SeekBar)findViewById(R.id.seekbar_bar_hdl);
		seekbar_bar_hdl.setPadding(w15, 0, w15, 0);
		//seekbar_bar_systolic.getLayoutParams().width=w220;
		seekbar_bar_hdl.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				// TODO Auto-generated method stub
				if(seekBar.getProgress()==61){
					lbl_hdl_number.setText("60");
					lbl_hdl_number.setTextColor(Color.RED);
				}else{
					lbl_hdl_number.setTextColor(Color.BLACK);
					lbl_hdl_number.setText(seekBar.getProgress()+"");
				}
			}
		});
		
		seekbar_bar_ldl=(SeekBar)findViewById(R.id.seekbar_bar_ldl);
		seekbar_bar_ldl.setPadding(w15, 0, w15, 0);
		//seekbar_bar_diastolic.getLayoutParams().width=w220;
		seekbar_bar_ldl.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				// TODO Auto-generated method stub
				if(seekBar.getProgress()==191){
					lbl_ldl_number.setText("190");
					lbl_ldl_number.setTextColor(Color.RED);
				}else{
					lbl_ldl_number.setTextColor(Color.BLACK);
					lbl_ldl_number.setText(seekBar.getProgress()+"");
				}
			}
		});
		
		seekbar_bar_triglycerides=(SeekBar)findViewById(R.id.seekbar_bar_triglycerides);
		seekbar_bar_triglycerides.setPadding(w15, 0, w15, 0);
		//seekbar_bar_pulse.getLayoutParams().width=w220;
		seekbar_bar_triglycerides.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				// TODO Auto-generated method stub
				if(seekBar.getProgress()==501){
					lbl_trigly_number.setText("500");
					lbl_trigly_number.setTextColor(Color.RED);
				}else{
					lbl_trigly_number.setTextColor(Color.BLACK);
					lbl_trigly_number.setText(seekBar.getProgress()+"");
				}
			}
		});
		seekbar_bar_total=(SeekBar)findViewById(R.id.seekbar_bar_total);
		seekbar_bar_total.setPadding(w15, 0, w15, 0);
		//seekbar_bar_pulse.getLayoutParams().width=w220;
		seekbar_bar_total.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				// TODO Auto-generated method stub
				if(seekBar.getProgress()==251){
					lbl_total_number.setText("250");
					lbl_total_number.setTextColor(Color.RED);
				}else{
					lbl_total_number.setTextColor(Color.BLACK);
					lbl_total_number.setText(seekBar.getProgress()+"");
				}
			}
		});
		
		btn_goal_cancle = (TextView)findViewById(R.id.btn_goal_cancle);
		btn_goal_cancle.setPadding(w10, h10, w10, h10);
		btn_goal_cancle.setOnClickListener(this);
		
		btn_goal_add = (TextView)findViewById(R.id.btn_goal_add);
		btn_goal_add.setPadding(w10, h10, w10, h10);
		btn_goal_add.setOnClickListener(this);
		
		ddl_interval_num = (Spinner)findViewById(R.id.ddl_interval_num);
		ddl_interval_unit = (Spinner)findViewById(R.id.ddl_interval_unit);
		
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
		  // txt_target_date.setText(newDateStr);
	}

	 public void ScreenDimension()
		{
			display = getWindowManager().getDefaultDisplay(); 
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
			width = display.getWidth();
			height = display.getHeight();

		}
	 public boolean validation(){
			boolean valid=true;
			if(txt_target_date.getText().length()>0){
				if(ddl_interval_num.getSelectedItemId()!=0 || ddl_interval_unit.getSelectedItemId()!=0){
					txt_target_date.setError("Enter target date or interval number and unit");
					valid=false;
				}
			}else{
				if(ddl_interval_num.getSelectedItemId()==0){
					Toast.makeText(AddCholesterolGoal.this, "Enter interval number", Toast.LENGTH_SHORT);
					valid=false;
				}
				if(ddl_interval_unit.getSelectedItemId()==0){
					Toast.makeText(AddCholesterolGoal.this, "Enter interval unit", Toast.LENGTH_SHORT);
					valid=false;
				}
			}
			return valid;
		}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v==btn_goal_add){
			if(validation()){
				if(isInternetOn()){
					CallAddGoalTask task = new CallAddGoalTask();
					 task.applicationContext =this.getParent();
					 task.execute();
			}else{
				Toast.makeText(AddCholesterolGoal.this,"Network is not available....",Toast.LENGTH_SHORT).show();
			}
		}
		}
		if(v==txt_target_date){
			new DatePickerDialog(AddCholesterolGoal.this, d,pYear,
                    pMonth,
                    pDay).show();
		}
		if(v==btn_goal_cancle){
			finish();
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
		   txt_target_date.setText(newDateStr);
	    }
	  };
	
	// async class for calling webservice and get responce message
		public class CallAddGoalTask extends AsyncTask <String, Void,String>
		{
			protected Context applicationContext;

			@Override
			protected void onPreExecute()     
			{
				
				//dialog = ProgressDialog.show(applicationContext, "Calling", "Please wait...", true);
				dialog = new ProgressDialog(AddCholesterolGoal.this);
				dialog.setCanceledOnTouchOutside(false);
				dialog.setMessage("Please Wait....");
				dialog.show();
				Log.i("onPreExecute", "onPreExecute");
				
			}       
			
			protected void onPostExecute(String result)
			{
				
				Log.i("onPostExecute", "onPostExecute");
					dialog.dismiss();
					if(result.equals("0")){
						appPrefs.setReloadgraph("1");
						ga.setCalcelflg(true);
						finish();	
					}else{
						appPrefs.setReloadgraph("1");
						ga.setCalcelflg(true);
						finish();
						//Toast.makeText(AddCholesterolGoal.this, "No goal added...",Toast.LENGTH_SHORT).show();
					}
			}  
	   
			@Override
			protected String doInBackground(String... params) {
				// TODO Auto-generated method stub
				Log.i("doInBackground--Object", "doInBackground--Object");
				
			  return obj.addCholesterolGoal(lbl_hdl_number.getText().toString(),
					                  lbl_ldl_number.getText().toString(), 
					                  lbl_trigly_number.getText().toString(), 
					                  lbl_total_number.getText().toString(), 
					                  txt_target_date.getText().toString(), 
					                  ddl_interval_num.getSelectedItem().toString(), 
					                  ddl_interval_unit.getSelectedItem().toString());
				
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
	  @Override
	    public void onBackPressed() 
	        {
	          
	         
	        }
}



