package com.viamhealth.android.activities;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

import com.viamhealth.android.dao.db.DataBaseAdapter;

import com.viamhealth.android.R;
import com.viamhealth.android.ViamHealthPrefs;

import com.viamhealth.android.dao.restclient.old.functionClass;
import com.viamhealth.android.model.GoalData;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class AddGoal extends Activity implements OnClickListener{
	private static ProgressDialog dialog;
	
	Display display;
	int height,width;
	int w15,h15,w20,w10,h20,w1,h1,h10,w5,h5,w230;
	
    TextView lbl_add_goal,btn_goal_cancle,btn_goal_add;
	LinearLayout below_goal_main_layout,below_goal_layout,middle_goal_layout,goal_info_layout,curr_dia_layout,
				 goal_dia_layout,dia_measure_layout;
	Spinner ddl_goal_name,ddl_goal_info,ddl_goal_time;
	EditText txt_curr_diabetes,txt_goal_diabetes;
	
	ViamHealthPrefs appPrefs;
	functionClass obj;
	ArrayList<GoalData> lstResult = new ArrayList<GoalData>();
	Typeface tf;
	DataBaseAdapter dbobj;
	int pYear,pMonth,pDay;
    Calendar dateAndTime=Calendar.getInstance();
	   
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);  
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		
		setContentView(R.layout.add_goal);
		
		appPrefs = new ViamHealthPrefs(AddGoal.this);
		obj=new functionClass(AddGoal.this);
		dbobj = new DataBaseAdapter(AddGoal.this);
		
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
		
		goal_info_layout=(LinearLayout)findViewById(R.id.goal_info_layout);
		goal_info_layout.setPadding(0, h5, 0, h5);
		
		curr_dia_layout=(LinearLayout)findViewById(R.id.curr_dia_layout);
		curr_dia_layout.setPadding(0, h10, 0, 0);
		
		goal_dia_layout=(LinearLayout)findViewById(R.id.goal_dia_layout);
		goal_dia_layout.setPadding(0, h10, 0, 0);
		
		dia_measure_layout=(LinearLayout)findViewById(R.id.dia_measure_layout);
		dia_measure_layout.setPadding(0, h10, 0, 0);
		
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
		
		ddl_goal_name = (Spinner)findViewById(R.id.ddl_goal_name);
		ddl_goal_name.getLayoutParams().width=w230;
		ddl_goal_name.setPadding(w10, 0, 0, 0);
	
		ddl_goal_info = (Spinner)findViewById(R.id.ddl_goal_info);
		ddl_goal_info.getLayoutParams().width=w230;
		ddl_goal_info.setPadding(w10, 0, 0, 0);
		
		ddl_goal_time = (Spinner)findViewById(R.id.ddl_goal_time);
		ddl_goal_time.getLayoutParams().width=w230;
		ddl_goal_time.setPadding(w10, 0, 0, 0);
		
		txt_curr_diabetes=(EditText)findViewById(R.id.txt_curr_diabetes);
		txt_curr_diabetes.setTypeface(tf);
		    
		txt_goal_diabetes=(EditText)findViewById(R.id.txt_goal_diabetes);
		txt_goal_diabetes.setTypeface(tf);
		
		if(appPrefs.getEdt().toString().equals("1")){
			appPrefs.getEdt().toString().equals("0");
			ddl_goal_name.setEnabled(false);
			ddl_goal_info.setEnabled(false);
			txt_curr_diabetes.setText(appPrefs.getGoalval());
			//txt_goal_diabetes.setEnabled(false);
			btn_goal_add.setText("Update");
			ddl_goal_time.setEnabled(false);
		}
		
		//get current date
		pYear = dateAndTime.get(Calendar.YEAR);
	    pMonth = dateAndTime.get(Calendar.MONTH);
	    pDay = dateAndTime.get(Calendar.DAY_OF_MONTH);
	    
	    if(appPrefs.getEdt().toString().equals("1")){
	    	lbl_add_goal.setText("Edit GoalActivity");
	    }else{
	    	lbl_add_goal.setText("Add GoalActivity");
	    }
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
			if(appPrefs.getEdt().toString().equals("1")){
				if(isInternetOn()){
					if(txt_curr_diabetes.getText().length()!=0){
						CallManageGoalTask task = new CallManageGoalTask();
						 task.applicationContext =this.getParent();
						 task.execute();
					}else{
						txt_curr_diabetes.setError("Enter current value");
					}
				}else{
					Toast.makeText(AddGoal.this,"Network is not available....",Toast.LENGTH_SHORT).show();
				}     
			}else{
				if(isInternetOn()){
					if(validation()){
						 CallAddGoalTask task = new CallAddGoalTask();
						 task.applicationContext =this.getParent();
						 task.execute();
					}
				}else{
					Toast.makeText(AddGoal.this,"Network is not available....",Toast.LENGTH_SHORT).show();
				}     
			}
		}
		if(v==btn_goal_cancle){
			finish();
		}
	}
	public boolean validation(){
		boolean valid=true;
		if(txt_curr_diabetes.getText().toString().length()==0){
			txt_curr_diabetes.setError("Enter current value");
			valid=false;
		}
		if(txt_goal_diabetes.getText().toString().length()==0){
			txt_goal_diabetes.setError("Enter goal value");
			valid=false;
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
					dialog = new ProgressDialog(AddGoal.this);
					dialog.setCanceledOnTouchOutside(false);
					dialog.setMessage("Please Wait....");
					dialog.show();
					Log.i("onPreExecute", "onPreExecute");
					
				}       
				
				protected void onPostExecute(String result)
				{
					
					Log.i("onPostExecute", "onPostExecute");
						dialog.dismiss();
						if(lstResult.size()>0){
							appPrefs.setReloadgraph("1");
							finish();	
						}else{
							Toast.makeText(AddGoal.this, "No goals found...",Toast.LENGTH_SHORT).show();
						}
				}  
		   
				@Override
				protected String doInBackground(String... params) {
					// TODO Auto-generated method stub
					Log.i("doInBackground--Object", "doInBackground--Object");
					/*lstResult=obj.addGoal(ddl_goal_name.getSelectedItem().toString(), ddl_goal_info.getSelectedItem().toString(),
										  txt_curr_diabetes.getText().toString(), txt_goal_diabetes.getText().toString(),
										  ddl_goal_time.getSelectedItem().toString());*/
					int range=0;
					String less="0";
					if(Integer.parseInt(txt_goal_diabetes.getText().toString()) >Integer.parseInt(txt_curr_diabetes.getText().toString())){
						less=txt_curr_diabetes.getText().toString();
						range =  (Integer.parseInt(txt_goal_diabetes.getText().toString()) - Integer.parseInt(txt_curr_diabetes.getText().toString()))/4;
					}else{
						less=txt_goal_diabetes.getText().toString();
						range =  (Integer.parseInt(txt_curr_diabetes.getText().toString()) - Integer.parseInt(txt_goal_diabetes.getText().toString()))/4;
					}
					String val="";
					Log.e("TAG","Range is : "  +range);
					int temp=Integer.parseInt(less);
					ArrayList<String> lst = new ArrayList<String>();
					ArrayList<String> lst1 = new ArrayList<String>();
					for(int i=0;i<5;i++){
						temp +=range;
						//val+=temp+",";
						lst.add(Integer.toString(temp));
					}
					lst1.add(lst.get(1));
					lst1.add(lst.get(2));
					lst1.add(lst.get(3));
					Collections.shuffle(lst1);
					val=lst.get(0) +","+ lst1.get(0)+"," + lst1.get(1)+"," + lst1.get(2) +"," +  lst.get(4);
					int timestamp = (Integer.parseInt(ddl_goal_time.getSelectedItem().toString().substring(0,1))*7)/4;
					String dates="";
					String dt =  new StringBuilder().append(pYear).append("-").append(pMonth + 1).append("-").append(pDay).append(" ").toString();  // Start date
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					Calendar c = Calendar.getInstance();
					for (int i = 0; i <5; i++) {
						try {
							c.setTime(sdf.parse(dt));
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						c.add(Calendar.DATE, timestamp);  // number of days to add
						dt = sdf.format(c.getTime());
						dates+=dt+",";
					}
					Log.e("TAG","Dates : " + dates  + " Value is : " + val);
					lstResult = dbobj.insertGoal(ddl_goal_name.getSelectedItem().toString(),
												 ddl_goal_info.getSelectedItem().toString(),
												 txt_curr_diabetes.getText().toString(), dates, val);
					return null;
				}
				   
			}     
			// async class for calling webservice and get responce message
			public class CallManageGoalTask extends AsyncTask <String, Void,String>
			{
				protected Context applicationContext;

				@Override
				protected void onPreExecute()     
				{
					
					//dialog = ProgressDialog.show(applicationContext, "Calling", "Please wait...", true);
					dialog = new ProgressDialog(AddGoal.this);
					dialog.setCanceledOnTouchOutside(false);
					dialog.setMessage("Please Wait....");
					dialog.show();
					Log.i("onPreExecute", "onPreExecute");
					
				}       
				
				protected void onPostExecute(String result)
				{
					
					Log.i("onPostExecute", "onPostExecute");
						dialog.dismiss();
						if(lstResult.size()>0){
							appPrefs.setReloadgraph("1");
							appPrefs.setEdt("0");
							finish(); 	
						}else{
							Toast.makeText(AddGoal.this, "No goals found...",Toast.LENGTH_SHORT).show();
						}
				}  
		   
				@Override
				protected String doInBackground(String... params) {
					// TODO Auto-generated method stub
					Log.i("doInBackground--Object", "doInBackground--Object");
					int range=0;
					String less="0";
					if(Integer.parseInt(txt_goal_diabetes.getText().toString()) >Integer.parseInt(txt_curr_diabetes.getText().toString())){
						less=txt_curr_diabetes.getText().toString();
						range =  (Integer.parseInt(txt_goal_diabetes.getText().toString()) - Integer.parseInt(txt_curr_diabetes.getText().toString()))/4;
					}else{
						less=txt_goal_diabetes.getText().toString();
						range =  (Integer.parseInt(txt_curr_diabetes.getText().toString()) - Integer.parseInt(txt_goal_diabetes.getText().toString()))/4;
					}
					String val="";
					Log.e("TAG","Range is : "  +range);
					int temp=Integer.parseInt(less);
					ArrayList<String> lst = new ArrayList<String>();
					ArrayList<String> lst1 = new ArrayList<String>();
					for(int i=0;i<5;i++){
						temp +=range;
						//val+=temp+",";
						lst.add(Integer.toString(temp));
					}
					lst1.add(lst.get(1));
					lst1.add(lst.get(2));
					lst1.add(lst.get(3));
					Collections.shuffle(lst1);
					val=lst.get(0) +","+ lst1.get(0)+"," + lst1.get(1)+"," + lst1.get(2) +"," +  lst.get(4);
					dbobj.managegoal(appPrefs.getGoalid().toString(),1,val.toString());
					lstResult = dbobj.getGoal();
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
		  @Override
		    public void onBackPressed() 
		        {
		          
		         
		        }
}
