package com.viamhealth.android.activities;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import com.viamhealth.android.dao.db.DataBaseAdapter;

import com.viamhealth.android.Global_Application;
import com.viamhealth.android.R;
import com.viamhealth.android.ViamHealthPrefs;

import com.viamhealth.android.dao.restclient.old.functionClass;
import com.viamhealth.android.model.GoalData;
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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class AddWeightGoal extends Activity implements OnClickListener{
	private static ProgressDialog dialog;
	
	Display display;
	int height,width;
	int w15,h15,w20,w10,h20,w1,h1,h10,w5,h5,w230;
	
    TextView lbl_add_goal,btn_goal_cancle,btn_goal_add;
	LinearLayout below_goal_main_layout,below_goal_layout,middle_goal_layout,goal_info_layout,curr_dia_layout,
				 goal_dia_layout,dia_measure_layout;
	Spinner ddl_interval_unit,ddl_interval_num,ddl_weight_measure;
	EditText txt_goal_weight,txt_target_date;
	
	ViamHealthPrefs appPrefs;
	functionClass obj;
	Global_Application ga;
	ArrayList<GoalData> lstResult = new ArrayList<GoalData>();
	Typeface tf;
	DataBaseAdapter dbobj;
	 DateFormat fmtDateAndTime=DateFormat.getDateTimeInstance();
     Calendar dateAndTime=Calendar.getInstance();
     int pYear,pMonth,pDay;
 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);  
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		
		setContentView(R.layout.add_weight_goal);
		
		appPrefs = new ViamHealthPrefs(AddWeightGoal.this);
		obj=new functionClass(AddWeightGoal.this);
		ga=((Global_Application)getApplicationContext());
		dbobj = new DataBaseAdapter(AddWeightGoal.this);
		
		tf = Typeface.createFromAsset(this.getAssets(),"Roboto-Condensed.ttf");
		//for get screen height width
        ScreenDimension();
        
        //calculate dynamic padding
        w15=(int)((width*4.69)/100);
		w20=(int)((width*6.25)/100);
		w1=(int)((width*0.4)/100);
		w10=(int)((width*3.12)/100);
		w5=(int)((width*1.6)/100);
		w230=(int)((width*71.88)/100);
		
		h15=(int)((height*3.13)/100);
		h20=(int)((height*4.17)/100);
		h1=(int)((height*0.21)/100);
		h10=(int)((height*2.08)/100);
		h5=(int)((height*1.042)/100);
		
		//casting control and manage padding and call onclick method
		middle_goal_layout=(LinearLayout)findViewById(R.id.middle_goal_layout);
		middle_goal_layout.setPadding(w20, h20, w20, h10);
		
		
		goal_dia_layout=(LinearLayout)findViewById(R.id.goal_dia_layout);
		
		lbl_add_goal = (TextView)findViewById(R.id.lbl_add_goal);
		lbl_add_goal.setPadding(w15, h15, w15, h15);
		
		below_goal_main_layout=(LinearLayout)findViewById(R.id.below_goal_main_layout);
		
		below_goal_layout=(LinearLayout)findViewById(R.id.below_goal_layout);


		btn_goal_cancle = (TextView)findViewById(R.id.btn_goal_cancle);
		btn_goal_cancle.setPadding(w10, h10, w10, h10);
		btn_goal_cancle.setOnClickListener(this);
		
		btn_goal_add = (TextView)findViewById(R.id.btn_goal_add);
		btn_goal_add.setPadding(w10, h10, w10, h10);
		btn_goal_add.setOnClickListener(this);
		
		txt_target_date = (EditText)findViewById(R.id.txt_target_date);
		txt_target_date.setTypeface(tf);
		txt_target_date.setOnClickListener(AddWeightGoal.this);
	
		txt_goal_weight=(EditText)findViewById(R.id.txt_goal_weight);
		txt_goal_weight.setTypeface(tf);
		
		ddl_weight_measure = (Spinner)findViewById(R.id.ddl_weight_measure);
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
		
		
		ddl_interval_num = (Spinner)findViewById(R.id.ddl_interval_num);
		ddl_interval_unit = (Spinner)findViewById(R.id.ddl_interval_unit);
		

		// for generate date
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
				Toast.makeText(AddWeightGoal.this,"Network is not available....",Toast.LENGTH_SHORT).show();
			}
		}
		}
		if(v==txt_target_date){
			new DatePickerDialog(AddWeightGoal.this, d,pYear,
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
	  private void updateDisplay(String dt) {
	        
	        
	    }
	public boolean validation(){
		boolean valid=true;
		if(txt_goal_weight.getText().toString().length()==0){
			txt_goal_weight.setError("Enter goal weight");
			valid=false;
		}
		if(txt_target_date.getText().length()>0){
			if(ddl_interval_num.getSelectedItemId()!=0 || ddl_interval_unit.getSelectedItemId()!=0){
				txt_target_date.setError("Enter target date or interval number and unit");
				valid=false;
			}
		}else{
			if(ddl_interval_num.getSelectedItemId()==0){
				Toast.makeText(AddWeightGoal.this, "Enter interval number", Toast.LENGTH_SHORT);
				valid=false;
			}
			if(ddl_interval_unit.getSelectedItemId()==0){
				Toast.makeText(AddWeightGoal.this, "Enter interval unit", Toast.LENGTH_SHORT);
				valid=false;
			}
		}
		return valid;
	}
	// async class for calling webservice and get responce message
			public class CallAddGoalTask extends AsyncTask <String, Void,String>
			{
				protected Context applicationContext;

				@Override
				protected void onPreExecute()     
				{
					
					//dialog = ProgressDialog.show(applicationContext, "Calling", "Please wait...", true);
					dialog = new ProgressDialog(AddWeightGoal.this);
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
							ga.setCalcelflg(true);
							//Toast.makeText(AddWeightGoal.this, "No goal added...",Toast.LENGTH_SHORT).show();
							finish();
						}
				}  
		   
				@Override
				protected String doInBackground(String... params) {
					// TODO Auto-generated method stub
					Log.i("doInBackground--Object", "doInBackground--Object");
				
					 return obj.addWeightGoal(txt_goal_weight.getText().toString(), ddl_weight_measure.getSelectedItem().toString(),
												  txt_target_date.getText().toString(), ddl_interval_num.getSelectedItem().toString(), 
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
