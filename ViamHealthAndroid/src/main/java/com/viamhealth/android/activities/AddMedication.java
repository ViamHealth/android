package com.viamhealth.android.activities;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.support.v4.app.DialogFragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.FragmentManager;

import com.viamhealth.android.Global_Application;
import com.viamhealth.android.R;
import com.viamhealth.android.ViamHealthPrefs;
import com.viamhealth.android.dao.restclient.functionClass;

import com.nostra13.universalimageloader.core.DisplayImageOptions;

import com.viamhealth.android.model.MedicalData;
import com.viamhealth.android.model.MedicationData;

import android.content.Intent;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;

public class AddMedication extends FragmentActivity implements OnClickListener{
	Display display;
	int height,width;
	ProgressDialog dialog1;
	
	int w10,h10,w15,w20,h40,w5,h20;
	
	LinearLayout newval_main_layout,newval_mid_layout,mid_layout,ddl_repeate_mode_layout,menu_invite_addfood,menu_invite_out_addfood,
				 settiglayout_food,newval_submid_layout,newval_btn_layout;
	EditText txt_name,txt_detail,txt_morning,txt_afternoon,txt_evening,txt_night,txt_time,txt_min,txt_hour,txt_day,txt_week,txt_day_interval,txt_interval_val,txt_interval_type,txt_duration_val,txt_duration_type;
	Spinner ddl_repeate_mode;
    Spinner reminder_type,interval_type,duration_type;
	TextView btnSave,btnCancle,heading;
	TextView lbl_invite_user_food,heding_Addfood_name;
	ImageView back,person_icon;
	ImageView imgMorningMinus,imgMorningPlus,imgNoonMinus,imgNoonPlus,imgNightMinus,imgNightPlus;
	int morning,night,noon;
	TextView morningval,nightval,noonval;
    String interval_type_sel="1",duration_type_sel="1",end_date;
    int start_day,start_month,start_year,end_day,end_month,end_year;
	LinearLayout morning_layout,noon_layout,night_layout;
	ArrayList<MedicalData> lstData = new ArrayList<MedicalData>();
	MedicationData medicationdt = new MedicationData();
	ArrayList<String> lst = new ArrayList<String>();
	Boolean isMedicine=true;
	private DisplayImageOptions options;
	ViamHealthPrefs appPrefs;
	functionClass obj;
    String user_id;
	Global_Application ga;
	Typeface tf;
	Calendar dateAndTime=Calendar.getInstance();
    Intent int_edit=null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
	    super.onCreate(savedInstanceState);
	    //super.getSupportActionBar().setTitle("Sharat Khurana");
	    
		setContentView(R.layout.add_medication);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        user_id=getIntent().getStringExtra("user_id");
		appPrefs = new ViamHealthPrefs(AddMedication.this);
		obj=new functionClass(AddMedication.this);
		ga=((Global_Application)getApplicationContext());
        reminder_type = (Spinner)findViewById(R.id.reminder_type);
        reminder_type.setOnItemSelectedListener(new CustomOnItemSelectedListener());

        interval_type = (Spinner)findViewById(R.id.interval_type);
        interval_type.setOnItemSelectedListener(new IntervalOnItemSelectedListener());
        interval_type_sel="1";
        duration_type_sel="1";

        duration_type = (Spinner)findViewById(R.id.duration_type);
        duration_type.setOnItemSelectedListener(new DurationOnItemSelectedListener());

        morning_layout=(LinearLayout)findViewById(R.id.morning_layout);
        noon_layout=(LinearLayout)findViewById(R.id.noon_layout);
        night_layout=(LinearLayout)findViewById(R.id.night_layout);
        txt_interval_val=(EditText)findViewById(R.id.txt_interval_val);
        txt_duration_val=(EditText)findViewById(R.id.txt_duration_val);
		   
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

        //interval_val=
        Calendar c = Calendar.getInstance();
        start_year=c.get(Calendar.YEAR);
        start_day=c.get(Calendar.DATE);
        start_month=c.get(Calendar.MONTH);
        start_month++;

        //Toast.makeText(getApplicationContext(),"start_month oncreate()="+start_month,Toast.LENGTH_LONG).show();

        heading=(TextView)findViewById(R.id.heading);
        int_edit=getIntent();
        if(int_edit.getBooleanExtra("iseditMed",false)==true)
        {
            reminder_type.setVisibility(View.GONE);
            ga.setUpdate("1");
            txt_name.setText((int_edit.getStringExtra("name")).toString());
            morningval.setText((int_edit.getStringExtra("morning")));
            isMedicine=true;
            noonval.setText((int_edit.getStringExtra("noon")).toString());
            nightval.setText((int_edit.getStringExtra("night")).toString());
            heading.setText("Edit Medication");
            morning_layout.setVisibility(View.VISIBLE);
            noon_layout.setVisibility(View.VISIBLE);
            night_layout.setVisibility(View.VISIBLE);
            btnSave.setText("Edit");
            Toast.makeText(getApplicationContext(),"start date="+int_edit.getStringExtra("start_date"),Toast.LENGTH_LONG).show();

        }
        else if(int_edit.getBooleanExtra("iseditOthers",false)==true)
        {
            btnSave.setText("Edit");
            reminder_type.setVisibility(View.GONE);
            ga.setUpdate("1");
            isMedicine=false;
            heading.setText("Edit Others");
            morning_layout.setVisibility(View.GONE);
            noon_layout.setVisibility(View.GONE);
            night_layout.setVisibility(View.GONE);
        }
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

    protected void onResume()
    {
        super.onResume();
        //isMedicine=true;
    }




    public  class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {


        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year,month,day;
            if(int_edit.getBooleanExtra("iseditMed",false)==false)
            {
                year = c.get(Calendar.YEAR);
                month = c.get(Calendar.MONTH);
                day = c.get(Calendar.DAY_OF_MONTH);
                start_month=month+1;
                //Toast.makeText(AddMedication.this,"start_month onCreateDialog ="+month,Toast.LENGTH_SHORT).show();
            }
            else
            {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date d1=new Date();
                try {
                    d1=sdf.parse(int_edit.getStringExtra("start_date"));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Calendar c1= Calendar.getInstance();
                c1.setTime(d1);

                year = c1.get(Calendar.YEAR);
                month = c1.get(Calendar.MONTH);
                day = c1.get(Calendar.DAY_OF_MONTH);
                start_month=month;
               // Toast.makeText(AddMedication.this,"start_month onCreateDialog ="+month,Toast.LENGTH_SHORT).show();
            }
            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the date chosen by the user

            start_day=day;
            start_year=year;
           // Toast.makeText(AddMedication.this,"start_month On DataSet ="+start_month,Toast.LENGTH_SHORT).show();

        }


    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public class CustomOnItemSelectedListener implements OnItemSelectedListener {

        public void onItemSelected(AdapterView<?> parent, View view, int pos,long id) {
            if(pos==1 && int_edit.getBooleanExtra("iseditOthers",false)==false) //Rest all
            {
               morning_layout.setVisibility(View.GONE);
               noon_layout.setVisibility(View.GONE);
               night_layout.setVisibility(View.GONE);
               isMedicine=false;
            }
            else
            {
                isMedicine=true;
                morning_layout.setVisibility(View.VISIBLE);
                noon_layout.setVisibility(View.VISIBLE);
                night_layout.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> arg0) {
            // TODO Auto-generated method stub
        }

    }

    public class IntervalOnItemSelectedListener implements OnItemSelectedListener {

        public void onItemSelected(AdapterView<?> parent, View view, int pos,long id) {


            if(pos==3) //Rest all
            {
                interval_type_sel="4";
            }
            else if(pos==2)
            {
                interval_type_sel="3";
            }
            else if(pos==1)
            {
                interval_type_sel="2";
            }
            else
            {
                interval_type_sel="1";
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> arg0) {
            // TODO Auto-generated method stub
        }
    }

        public class DurationOnItemSelectedListener implements OnItemSelectedListener {

            public void onItemSelected(AdapterView<?> parent, View view, int pos,long id) {

                if(pos==3) //Rest all
                {
                    duration_type_sel="4";
                }
                else if(pos==2)
                {
                    duration_type_sel="3";
                }
                else if(pos==1)
                {
                    duration_type_sel="2";
                }
                else
                {
                    duration_type_sel="1";
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
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
			
			morning = (int)Float.parseFloat(morningval.getText().toString());
			if(morning!=0){
				morning--;
			}
			morningval.setText(morning+"");
		}
		if(v==imgMorningPlus){
			morning = (int)Float.parseFloat(morningval.getText().toString());
			morning++;
			morningval.setText(morning+"");
		}
		
		if(v==imgNoonMinus){
			
			noon = (int)Float.parseFloat(noonval.getText().toString());
			if(noon!=0){
				noon--;
			}
			noonval.setText(noon+"");
		}
		if(v==imgNoonPlus){
			noon = (int)Float.parseFloat(noonval.getText().toString());
			noon++;
			noonval.setText(noon+"");
		}
		
		if(v==imgNightMinus){
			
			night = (int)Float.parseFloat(nightval.getText().toString());
			if(night!=0){
				night--;
			}
			nightval.setText(night+"");
		}
		if(v==imgNightPlus){
			night = (int)Float.parseFloat(nightval.getText().toString());
			night++;
			nightval.setText(night+"");
		}
	}
	 private void updateLabelTime() {
		    
		    //txt_time.setText(dateAndTime.HOUR); //+":" +dateAndTime.MINUTE +":"+dateAndTime.SECOND);
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
        /*
		if(txt_detail.getText().length()==0){
			txt_detail.setError("Enter detail");
			valid = false;
		}
		if(txt_time.getText().length()==0){
			txt_time.setError("Enter time");
			valid=false;
		}
		*/
		return valid;
	}

    public String getEndDate(String value)
    {
        String end_date="";

       // YYYY[-MM[-DD]]

        String dt = ""+start_year+"-"+start_month+"-"+start_day+"]]";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(sdf.parse(dt));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if(duration_type_sel.equalsIgnoreCase("4"))
        {
            c.add(Calendar.YEAR,Integer.parseInt(txt_duration_val.getText().toString()));
        }

        else if(duration_type_sel.equalsIgnoreCase("3"))
        {
            c.add(Calendar.MONTH, Integer.parseInt(txt_duration_val.getText().toString()));
        }
        else if(duration_type_sel.equalsIgnoreCase("2"))
        {
            c.add(Calendar.WEEK_OF_YEAR, Integer.parseInt(txt_duration_val.getText().toString()));
        }
        else if(duration_type_sel.equalsIgnoreCase("1"))
        {
            c.add(Calendar.DATE, Integer.parseInt(txt_duration_val.getText().toString()));

        }


          // number of days to add, can also use Calendar.DAY_OF_MONTH in place of Calendar.DATE
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
        Date resultdate = new Date(c.getTimeInMillis());
        end_date = sdf1.format(resultdate);
        //Toast.makeText(getApplicationContext(),"end date="+end_date,Toast.LENGTH_LONG).show();


        return end_date;

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
            end_date=getEndDate(txt_duration_val.getText().toString());
			
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
                Toast.makeText(getApplicationContext(),"start date="+""+start_year+"-"+start_month+"-"+start_day,Toast.LENGTH_LONG).show();
                Toast.makeText(getApplicationContext(),"end date and isMedicine= "+end_date+""+ isMedicine,Toast.LENGTH_LONG).show();
				 
		}  

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			Log.i("doInBackground--Object", "doInBackground--Object");
			//ga.lstResult=obj.manageGoal(appPrefs.getGoalname().toString(), type, goalvalue);
            String type;
            if(isMedicine==true)
            {
                type="2";
            }
            else
            {
                type="1";
            }
			lstData = obj.addMedication(txt_name.getText().toString(),
                                        user_id,
                                        type,
					 					"null",
                                        morningval.getText().toString(),
					 					noonval.getText().toString(),
					 					"0",
                                        nightval.getText().toString(),
					 					"0",
					 					"0", //txt_hour.getText().toString()
					 					"0",//txt_day.getText().toString()
                                        interval_type_sel,//ddl_repeate_mode.getSelectedItem().toString()
					 					"0", //ddl_repeate_mode.getSelectedItem().toString()
                                        "1",//txt_week.getText().toString()
                                        "0",
                                        txt_interval_val.getText().toString(),
                                        ""+start_year+"-"+start_month+"-"+start_day,
                                        end_date);//txt_day_interval.getText().toString()

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
                String type;

                if(isMedicine==true)
                {
                    type="2";
                }
                else
                {
                    type="1";
                }
                    lstData = obj.UpdateMedication(int_edit.getStringExtra("id"),
                                                user_id,
                                                type,
                                                txt_name.getText().toString(),
                                                "null", //MJ:hardcoded
                                                morningval.getText().toString(),
                                                noonval.getText().toString(),
                                                "0",
                                                nightval.getText().toString(),
                                                "0",
                                                "0",//txt_hour.getText().toString()
                                                "0",// txt_day.getText().toString()
                                                "0",//ddl_repeate_mode.getSelectedItem().toString()
                                                "0",// txt_min.getText().toString()
                                                "1", //txt_week.getText().toString()
                                                "0");//txt_day_interval.getText().toString()

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
