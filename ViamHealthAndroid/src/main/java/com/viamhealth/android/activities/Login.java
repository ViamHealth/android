package com.viamhealth.android.activities;

import com.viamhealth.android.dao.db.DataBaseAdapter;
import com.viamhealth.android.dao.restclient.functionClass;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.net.NetworkInfo;

import com.viamhealth.android.R;
import com.viamhealth.android.ViamHealthPrefs;
   
      
   
public class Login extends Activity implements OnClickListener 
{
	private static ProgressDialog dialog;
	
	Button login_btn;
	Display display;
	int width,height;  
	int w10,h10,h15;
	int h40,h20,h30,pt7;
	
	TableLayout title_table;
	TableRow password_row,login_btn_row,sign_up_row;
	LinearLayout copyright_layout,login_layout;
	EditText user_name,user_password;
	TextView sign_up;
	functionClass obj;
	ViamHealthPrefs apPrefs;
	DataBaseAdapter dbAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);  
		setContentView(R.layout.login);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		 
		apPrefs = new ViamHealthPrefs(Login.this);
		Log.e("TAG","Token is " + apPrefs.getToken());
		if(!apPrefs.getToken().toString().equals("null") || apPrefs.getToken().toString() ==null){
			Intent i=new Intent(Login.this, Home.class);
			startActivity(i);
		}else{
			dbAdapter=new DataBaseAdapter(getApplicationContext());
			dbAdapter.createDatabase();
			dbAdapter.insertDefaulValues();	
			
			obj=new functionClass(Login.this);
			
			Typeface tf = Typeface.createFromAsset(this.getAssets(),"Roboto-Condensed.ttf");
		       
			ScreenDimension();
		    
			w10=(int)((width*3.13)/100);
			h10=(int)((height*2.09)/100);
			h40=(int)((height*8.34)/100);
			h15=(int)((height*3.13)/100);
			h20=(int)((height*4.17)/100);
			h30=(int)((height*6.25)/100);
			pt7=(int)((height*2.19)/100);  
			
			title_table=(TableLayout)findViewById(R.id.title_table);
			password_row=(TableRow)findViewById(R.id.password_row);
			login_btn_row=(TableRow)findViewById(R.id.login_btn_row);
			sign_up_row=(TableRow)findViewById(R.id.sign_up_row);
			login_layout = (LinearLayout)findViewById(R.id.loginwrapper);
			user_name = (EditText)findViewById(R.id.user_name);
			user_password = (EditText)findViewById(R.id.user_password);
			user_password.setTypeface(tf);
			user_name.setTypeface(tf);
			    
			title_table.setPadding(0, 0, 0, h40);
			password_row.setPadding(0, h15, 0, 0);
			login_btn_row.setPadding(0, h20, 0, 0);       
			sign_up_row.setPadding(0, h15, 0, 0);
			login_layout.setPadding(0, pt7, 0, 0);
			user_name.setTypeface(tf);
			user_password.setTypeface(tf);
			
			login_btn=(Button)findViewById(R.id.login_btn);
			login_btn.setOnClickListener(this);
			login_btn.setTypeface(tf);
			
			sign_up=(TextView)findViewById(R.id.sign_up);
			sign_up.setOnClickListener(this);
			sign_up.setTypeface(tf);	
		}
	} 
	
	// for get screen diamention
	public void ScreenDimension()
	{
		display = getWindowManager().getDefaultDisplay(); 
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		width = display.getWidth();
		height = display.getHeight();
		apPrefs.setSwidth(String.valueOf(width));
		apPrefs.setSheight(String.valueOf(height));
	}
	
	// onclick method of all clikable control
	@Override    
	public void onClick(View v) 
	{
		// TODO Auto-generated method stub
		if(v==login_btn)
		{
			if(isInternetOn()){
				if(validation()){
					 CallLoginTask task = new CallLoginTask();
					 task.applicationContext = Login.this;
					 task.execute();
				}
			}else{
				Toast.makeText(Login.this,"Network is not available....",Toast.LENGTH_SHORT).show();
			}
		}
		if(v==sign_up){
			//redirect registration activity
			Intent i = new Intent(Login.this,SignUp.class);
			startActivity(i);
		}
		
	}
	// function for validation
	public boolean validation(){
		boolean valid=true;
		if(user_name.getText().length()==0){
			user_name.setError("Enter user name");
			valid=false;
		}
		if(user_password.getText().length()==0){
			user_password.setError("Enter password");
			valid=false;
		}
		return valid;
	}
	// async class for calling webservice and get responce message
	public class CallLoginTask extends AsyncTask <String, Void,String>
	{
		protected Context applicationContext;

		@Override
		protected void onPreExecute()     
		{
			
			//dialog = ProgressDialog.show(applicationContext, "Calling", "Please wait...", true);
			dialog = new ProgressDialog(Login.this);
			dialog.setMessage("Please Wait....");
			dialog.show();
			Log.i("onPreExecute", "onPreExecute");
			
		}        
		 
		protected void onPostExecute(String result)
		{
			
			Log.i("onPostExecute", "onPostExecute");
			if(result.toString().equals("0")){
				dialog.dismiss();
				apPrefs.setUsername(user_name.getText().toString().trim());
				Intent i=new Intent(Login.this, Home.class);
				startActivity(i);
			}else{
				dialog.dismiss();    
				Toast.makeText(Login.this, "Incorrect user name and password",Toast.LENGTH_SHORT).show();
			}
		}  
   
		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			Log.i("doInBackground--Object", "doInBackground--Object");
			return obj.Login(user_name.getText().toString(), user_password.getText().toString());
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
