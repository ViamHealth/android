package com.viamhealth.android.activities.oldones;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.viamhealth.android.dao.db.DataBaseAdapter;

import android.os.Bundle;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.viamhealth.android.R;

public class AddReminder extends Activity implements OnClickListener{
	Display display;
	int height,width;
	Typeface tf;
	
	TextView lbl_add_reminder;
	EditText txt_reminder_name,reminder_when;
	LinearLayout middle_layout,time_layout,date_picker_layout,reminder_btn_layout,main_layout;
	Button btnCancle_reminder,btnSave_reminder;
	int w5,w10,h10,w135;
	
	int pYear,pMonth,pDay;
	Calendar dateAndTime=Calendar.getInstance();
	DataBaseAdapter dbObj;
	String tempdate;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
	 	setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

	 	setContentView(R.layout.add_reminder);
	 	
	 	tf = Typeface.createFromAsset(this.getAssets(),"Roboto-Condensed.ttf");
	 	dbObj = new DataBaseAdapter(AddReminder.this);
	 	// get screen diamnetion
	 	
	 	ScreenDimension();
	 	
	 	// calculate dynamic padding
	 	w5 = (int)((width*1.56)/100);
	 	w10 = (int)((width*3.13)/100);
	 	w135 = (int)((width*42.19)/100);
	 	
	 	h10 = (int)((height*2.083)/100);
	 	
	 	// cansting control and manage onclick
	 	
	 	lbl_add_reminder = (TextView)findViewById(R.id.lbl_add_reminder);
	 	lbl_add_reminder.setTypeface(tf);
        lbl_add_reminder.setPadding(w5, 0, 0, 0);
        
        main_layout=(LinearLayout)findViewById(R.id.main_layout);
        
        middle_layout=(LinearLayout)findViewById(R.id.middle_layout);
        middle_layout.setPadding(w10, h10, w10, h10);
        
        time_layout = (LinearLayout)findViewById(R.id.time_layout);
        time_layout.setPadding(0, h10, 0, h10);
        
        txt_reminder_name= (EditText)findViewById(R.id.txt_reminder_name);
        txt_reminder_name.setTypeface(tf);
        txt_reminder_name.setPadding(w5, 0, 0, 0);
	 	
        reminder_when=(EditText)findViewById(R.id.reminder_when);
        reminder_when.setTypeface(tf);
        reminder_when.setPadding(w5, 0, 0, 0);
        
        date_picker_layout=(LinearLayout)findViewById(R.id.date_picker_layout);
		date_picker_layout.setPadding(0, 0, w5, 0);
		date_picker_layout.setOnClickListener(this);
	 	
		pYear = dateAndTime.get(Calendar.YEAR);
	    pMonth = dateAndTime.get(Calendar.MONTH);
	    pDay = dateAndTime.get(Calendar.DAY_OF_MONTH);
	    
	    reminder_btn_layout=(LinearLayout)findViewById(R.id.reminder_btn_layout);
	    reminder_btn_layout.setPadding(w10, h10, w10, h10);
	    
	    btnSave_reminder = (Button)findViewById(R.id.btnSave_reminder);
	    btnSave_reminder.setTypeface(tf);
	    btnSave_reminder.getLayoutParams().width=w135;
	    btnSave_reminder.setOnClickListener(this);
	    
	    btnCancle_reminder = (Button)findViewById(R.id.btnCancle_reminder);
	    btnCancle_reminder.setTypeface(tf);
	    btnCancle_reminder.getLayoutParams().width=w135;
	    btnCancle_reminder.setOnClickListener(this);
	    
	    
	   
       // reminder_when.setText(strDate);
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
		if(v==date_picker_layout){
			new DatePickerDialog(AddReminder.this, d,pYear,
                    pMonth,
                    pDay).show();
		}
		if(v==btnSave_reminder){
			//save reminder
			if(validation()){
				dbObj.insertReminder(txt_reminder_name.getText().toString(), reminder_when.getText().toString());
				finish();
			}
		}
		if(v==btnCancle_reminder){
			finish();
		}
	}
	public boolean validation(){
		boolean valid=true;
		if(txt_reminder_name.getText().toString().length()==0){
			txt_reminder_name.setError("Enter reminder name");
			valid=false;
		}
		if(reminder_when.getText().toString().length()==0){
			reminder_when.setError("Enter Date");
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
	      updateDisplay(pYear,pMonth,pDay);
	    }
	  };
	  private void updateDisplay( int year, int monthOfYear,
              int dayOfMonth) {
	       /* reminder_when.setText(
	            new StringBuilder()
	                    // Month is 0 based so add 1
	                    .append(pYear).append("-")
	                    .append(pMonth + 1).append("-")
	                    .append(pDay).append(" "));*/
		  Date d = new Date(year, pMonth, pDay);
          SimpleDateFormat dateFormatter = new SimpleDateFormat(
                          "EEEE dd MMMM yy");
          String strDate = dateFormatter.format(d);
          reminder_when.setText(strDate);
	    }
}
