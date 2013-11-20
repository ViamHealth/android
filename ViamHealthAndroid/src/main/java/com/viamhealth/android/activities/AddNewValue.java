package com.viamhealth.android.activities;

import java.text.DateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import com.viamhealth.android.dao.db.DataBaseAdapter;

import com.viamhealth.android.dao.restclient.old.functionClass;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
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
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.viamhealth.android.Global_Application;
import com.viamhealth.android.adapters.GoalDataAdapter;
import com.viamhealth.android.R;
import com.viamhealth.android.ViamHealthPrefs;

public class AddNewValue extends Activity implements OnClickListener {
	private static ProgressDialog dialog;
	
	Display display;
	int height,width;
	int w10,h10,w5,h5,h1,w220,w15,w20,h40,w25;
	
 
	LinearLayout header_layout,newval_main_layout,newval_mid_layout,newval_bottom_layout,newval_txt_layout,newval_btn_layout,
				menu_invite_out_newval,setting_layout,menu_invite_newval,back_layout,btn_time_picker,btn_date_picker,
				blood_presure_layout,weight_goal_layout,settiglayout_food,menu_invite_food,menu_invite_out_food;
	ImageView newval_icon,back;
	TextView lbl_systolic,lbl_diastolic,lbl_systolic_number,lbl_diastolic_number,lbl_pulse,lbl_pulse_number,lbl_newval_date,lbl_newval_time,heding_name_newval,
			lbl_invite_user_goal,lblback,goal_name,goal_desc,lbl_add_val,lbl_add_val_unit,lbl_invite_user_food,heding_name_food;
	SeekBar seekbar_bar_systolic,seekbar_bar_diastolic,seekbar_bar_pulse;
	Button btnSave,btnCancle;
	EditText txt_Add_val;
	
	String value;
	Typeface tf,digittf;
	
	ViamHealthPrefs appPrefs;
	functionClass obj;
	Global_Application ga;
	
	DateFormat fmtDateAndTime=DateFormat.getDateTimeInstance();
    Calendar dateAndTime=Calendar.getInstance();
    int pYear,pMonth,pDay;
	
    DataBaseAdapter dbObj;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);  
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		
		setContentView(R.layout.add_new_value);
		
		appPrefs = new ViamHealthPrefs(this.getParent());
		ga=((Global_Application)getApplicationContext());
		obj=new functionClass(this.getParent());
		dbObj=new DataBaseAdapter(AddNewValue.this);
		
		
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
		
		//casting control and manage padding and call onclick method
		  back=(ImageView)findViewById(R.id.back);
    		
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
  		
		blood_presure_layout=(LinearLayout)findViewById(R.id.blood_presure_layout);
	//	weight_goal_layout=(LinearLayout)findViewById(R.id.weight_goal_layout);
		
		header_layout=(LinearLayout)findViewById(R.id.header_layout);
		header_layout.setPadding(0, h5, 0, h5);
		
		btnSave = (Button)findViewById(R.id.btnSave);
		btnSave.setTypeface(tf);
		btnSave.setOnClickListener(this);
		
		btnCancle=(Button)findViewById(R.id.btnCancle);
		btnCancle.setTypeface(tf);
		btnCancle.setOnClickListener(this);
		
		newval_main_layout = (LinearLayout)findViewById(R.id.newval_main_layout);
		newval_main_layout.setPadding(w10, h10, w10, h10);
		
		newval_mid_layout = (LinearLayout)findViewById(R.id.newval_mid_layout);
		newval_mid_layout.setPadding(w10, h10, w10, h10);
		

    /*	newval_txt_layout = (LinearLayout)findViewById(R.id.newval_txt_layout);
		newval_txt_layout.setPadding(w10, 0, w10, h10);*/
		
		newval_btn_layout = (LinearLayout)findViewById(R.id.newval_btn_layout);
		newval_btn_layout.setPadding(w10, h10, w10, h10);
		
		btn_time_picker = (LinearLayout)findViewById(R.id.btn_time_picker);
		btn_time_picker.setPadding(w5, 0, w5, 0);
		btn_time_picker.setOnClickListener(this);
		
		btn_date_picker = (LinearLayout)findViewById(R.id.btn_date_picker);
		btn_date_picker.setPadding(w5, 0, 0, 0);
		btn_date_picker.setOnClickListener(this);
		
		newval_icon = (ImageView)findViewById(R.id.newval_icon);
		newval_icon.setPadding(w5, h5, w5, h5);
		
		lbl_systolic = (TextView)findViewById(R.id.lbl_systolic);
		lbl_systolic.setTypeface(tf);
		lbl_systolic.setPadding(w10, h10, w10, h10);
		
		lbl_systolic_number = (TextView)findViewById(R.id.lbl_systolic_number);
		lbl_systolic_number.setTypeface(digittf);
		lbl_systolic_number.setPadding(w5, 0, 0, 0);
		
		lbl_diastolic = (TextView)findViewById(R.id.lbl_diastolic);
		lbl_diastolic.setTypeface(tf);
		lbl_diastolic.setPadding(w10, h10, w10, h10);
		
		lbl_diastolic_number = (TextView)findViewById(R.id.lbl_diastolic_number);
		lbl_diastolic_number.setTypeface(digittf);
		lbl_diastolic_number.setPadding(w5, 0, 0, 0);
		
		lbl_pulse = (TextView)findViewById(R.id.lbl_pulse);
		lbl_pulse.setTypeface(tf);
		lbl_pulse.setPadding(w10, h10, w10, h10);
		
		lbl_pulse_number = (TextView)findViewById(R.id.lbl_pulse_number);
		lbl_pulse_number.setTypeface(digittf);
		lbl_pulse_number.setPadding(w5, 0, 0, 0);
		
		seekbar_bar_systolic=(SeekBar)findViewById(R.id.seekbar_bar_systolic);
		seekbar_bar_systolic.setPadding(w15, 0, w15, 0);
		seekbar_bar_systolic.getLayoutParams().width=w220;
		seekbar_bar_systolic.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			
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
				lbl_systolic_number.setText(seekBar.getProgress()+"");
			}
		});
		
		seekbar_bar_diastolic=(SeekBar)findViewById(R.id.seekbar_bar_diastolic);
		seekbar_bar_diastolic.setPadding(w15, 0, w15, 0);
		seekbar_bar_diastolic.getLayoutParams().width=w220;
		seekbar_bar_diastolic.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			
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
				lbl_diastolic_number.setText(seekBar.getProgress()+"");
			}
		});
		
		seekbar_bar_pulse=(SeekBar)findViewById(R.id.seekbar_bar_pulse);
		seekbar_bar_pulse.setPadding(w15, 0, w15, 0);
		seekbar_bar_pulse.getLayoutParams().width=w220;
		seekbar_bar_pulse.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			
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
				lbl_pulse_number.setText(seekBar.getProgress()+"");
			}
		});
		
		  
		lbl_newval_date = (TextView)findViewById(R.id.lbl_newval_date);
		lbl_newval_date.setTypeface(tf);
		
		lbl_newval_time = (TextView)findViewById(R.id.lbl_newval_time);
		lbl_newval_time.setTypeface(tf);
		
		goal_name=(TextView)findViewById(R.id.goal_name);
		goal_name.setTypeface(tf,Typeface.BOLD);
		goal_name.setText(appPrefs.getGoalname().toString());
		
		goal_desc=(TextView)findViewById(R.id.goal_desc);
		goal_desc.setTypeface(tf);
		goal_desc.setText(appPrefs.getGoaldesc().toString());
		
		/*lbl_add_val=(TextView)findViewById(R.id.lbl_add_val);
		lbl_add_val.setTypeface(tf);
		lbl_add_val.setPadding(w10, 0, 0, 0);
		*/
	/*	txt_Add_val=(EditText)findViewById(R.id.txt_Add_val);
		txt_Add_val.setTypeface(tf);
		txt_Add_val.setPadding(w5, 0, 0, 0);*/
		
	/*	lbl_add_val_unit=(TextView)findViewById(R.id.lbl_add_val_unit);
		lbl_add_val_unit.setTypeface(tf);
		lbl_add_val_unit.setPadding(w10, 0, w10, 0);*/
		actionmenu();
		if(ga.getAddvalType().equals("Weight")){
			weight_goal_layout.setVisibility(View.VISIBLE);
		}
		
		

		pYear = dateAndTime.get(Calendar.YEAR);
	    pMonth = dateAndTime.get(Calendar.MONTH);
	    pDay = dateAndTime.get(Calendar.DAY_OF_MONTH);	    
	    updateDisplay();
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
		    		Animation anim = AnimationUtils.loadAnimation(AddNewValue.this, R.anim.fade_out);
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
		if(v==back_layout){
			/*Intent i=new Intent(AddNewValue.this, MainActivity.class);
			startActivity(i);*/
			finish();
		}
		if(v==btnCancle){
			finish();
		}
		if(v==btnSave){
			if(isInternetOn()){
				if(!appPrefs.getGoalname().toString().equals("Blood Presure")){
					if(othervalidation()){
						value=txt_Add_val.getText().toString();
						Log.e("TAG","val is " + value);
						CallLoginTask task = new CallLoginTask();
						task.applicationContext = this.getParent();
						task.execute();
					}
				}else{
					Log.e("TAG","Bp is true");
					if(bpvalidation()){
						value= lbl_pulse_number.getText().toString();//lbl_systolic_number.getText().toString() + "," + lbl_diastolic_number.getText().toString() + "," +
						Log.e("TAG","val is " + value);
						CallLoginTask task = new CallLoginTask();
						task.applicationContext = this.getParent();
						task.execute();
					}
					
				}
					
			}else{
				Toast.makeText(AddNewValue.this,"Network is not available....",Toast.LENGTH_SHORT).show();
			}
			}
			
		//}
		if(v==lbl_invite_user_food){
			Log.e("TAG","Selected value is " + "invite user is clicked");
			Animation anim = AnimationUtils.loadAnimation(AddNewValue.this, R.anim.fade_out);
			settiglayout_food.startAnimation(anim);
			settiglayout_food.setVisibility(View.INVISIBLE);
			menu_invite_food.setVisibility(View.VISIBLE);
			menu_invite_out_food.setVisibility(View.INVISIBLE);
			Log.e("TAG","Clicked");
			//Intent i = new Intent(AddNewValue.this,InviteUser.class);
			//startActivity(i);
		}
		if(v==btn_date_picker){
			//appPrefs.setDateAdded("0");
			new DatePickerDialog(getParent(), d,pYear,
                    pMonth,
                    pDay).show();
         }
		if(v==btn_time_picker){
			 new TimePickerDialog(getParent(), t,
                     dateAndTime.get(Calendar.HOUR_OF_DAY),
                     dateAndTime.get(Calendar.MINUTE),
                     true).show();
		}
		
		if(v==menu_invite_food){
			actionmenu();
		settiglayout_food.setVisibility(View.VISIBLE);
		menu_invite_out_food.setVisibility(View.VISIBLE);
		menu_invite_food.setVisibility(View.INVISIBLE);
		Animation anim = AnimationUtils.loadAnimation(AddNewValue.this, R.anim.fade_in);
		settiglayout_food.startAnimation(anim);
		
		Log.e("TAG","Clicked");
	}
	if(v==menu_invite_out_food){
		Animation anim = AnimationUtils.loadAnimation(AddNewValue.this, R.anim.fade_out);
		settiglayout_food.startAnimation(anim);
		settiglayout_food.setVisibility(View.INVISIBLE);
		menu_invite_food.setVisibility(View.VISIBLE);
		menu_invite_out_food.setVisibility(View.INVISIBLE);
		Log.e("TAG","Clicked");
	}
		
		}
	
	public boolean bpvalidation(){
		boolean valid=true;
		if(lbl_systolic_number.getText().toString().equals("0")){
			Toast.makeText(AddNewValue.this, "Please select systolic value", Toast.LENGTH_SHORT);
			valid=false;
		}
		if(lbl_diastolic_number.getText().toString().equals("0")){
			Toast.makeText(AddNewValue.this, "Please select diastolic value", Toast.LENGTH_SHORT);
			valid=false;
		}
		if(lbl_pulse_number.getText().toString().equals("0")){
			Toast.makeText(AddNewValue.this, "Please select pulse value", Toast.LENGTH_SHORT);
			valid=false;
		}
		return valid;
	}
	
	public boolean othervalidation(){
		boolean valid=true;
		if(txt_Add_val.getText().length()==0){
			txt_Add_val.setError("Enter new value");
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
	        lbl_newval_date.setText(
	            new StringBuilder()
	                    // Month is 0 based so add 1
	                    .append(pYear).append("-")
	                    .append(pMonth + 1).append("-")
	                    .append(pDay).append(" "));
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
		public class CallLoginTask extends AsyncTask <String, Void,String>
		{
			protected Context applicationContext;

			@Override
			protected void onPreExecute()     
			{
				
				//dialog = ProgressDialog.show(applicationContext, "Calling", "Please wait...", true);
				dialog = new ProgressDialog(getParent());
				dialog.setCanceledOnTouchOutside(false);
				dialog.setMessage("Please Wait....");
				dialog.show();
				Log.i("onPreExecute", "onPreExecute");
				
			}       
			
			protected void onPostExecute(String result)
			{
				
				Log.i("onPostExecute", "onPostExecute");
				if(!result.toString().equals("0")){
					dialog.dismiss();
					appPrefs.setReloadgraph("1");
					finish();
				}else{
					dialog.dismiss();
					//Toast.makeText(AddNewValue.this, "Value not added successfully..",Toast.LENGTH_SHORT).show();
					finish();
				}
			}  
	   
			@Override
			protected String doInBackground(String... params) {
				// TODO Auto-generated method stub
				Log.i("doInBackground--Object", "doInBackground--Object");
				
				return dbObj.AddNewValue(","+value.toString(), appPrefs.getGoalid().toString(),lbl_newval_date.getText().toString()+",");// obj.addGoalValue(goal_name.getText().toString(), lbl_newval_date.getText().toString(), value);
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
