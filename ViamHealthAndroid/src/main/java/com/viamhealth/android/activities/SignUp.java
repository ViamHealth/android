package com.viamhealth.android.activities;

import com.viamhealth.android.dao.restclient.functionClass;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.viamhealth.android.R;
import com.viamhealth.android.ViamHealthPrefs;

public class SignUp extends Activity implements OnClickListener{
	private static ProgressDialog dialog;
	
	Display display;
	int width,height;  
	
	ViamHealthPrefs appPrefs;
	functionClass obj;
	Typeface tf;
	int w15,w20,w5,w10,h10,h5,h8,h20,h40;
	
	Button btn_signUp,btn_cancel;
	LinearLayout signup_txt_layout,signup_mid_layout,signup_submid_layout,password_layout,confirmpassword_layout,signup_btn_layout;
	TextView lbl_register;
	TableLayout title_table;
	EditText email_id,password,confirmpassword;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.sign_up);
		
		appPrefs = new ViamHealthPrefs(SignUp.this);
		obj=new functionClass(SignUp.this);
		
		tf = Typeface.createFromAsset(this.getAssets(),"Roboto-Condensed.ttf");
		// get screen height and width
		ScreenDimension();
		
	   //calculate dynamic padding
        w15=(int)((width*4.68)/100);
        w20=(int)((width*6.25)/100);
        w10=(int)((width*3.13)/100);
        w5=(int)((width*1.56)/100);
        
        h10=(int)((height*2.08)/100);
        h40=(int)((height*8.33)/100);
        h5=(int)((height*1.042)/100);
        h8=(int)((height*1.67)/100);
        h20=(int)((height*4.17)/100);
        
		//casting control and manage padding and call onclick method
		 
		
		
		signup_mid_layout=(LinearLayout)findViewById(R.id.signup_mid_layout);
		signup_mid_layout.setPadding(w10, 0, w10, 0); 
		
		signup_submid_layout=(LinearLayout)findViewById(R.id.signup_submid_layout);
		signup_submid_layout.setPadding(w10, h8, w10, h20);
		
		password_layout=(LinearLayout)findViewById(R.id.password_layout);
		password_layout.setPadding(0, h10, 0, 0);
		
		confirmpassword_layout=(LinearLayout)findViewById(R.id.confirmpassword_layout);
		confirmpassword_layout.setPadding(0, h10, 0, 0);
		
		email_id = (EditText)findViewById(R.id.email_id);
		email_id.setTypeface(tf);
		
		password = (EditText)findViewById(R.id.password);
		password.setTypeface(tf);
		
		confirmpassword = (EditText)findViewById(R.id.confirmpassword);
		confirmpassword.setTypeface(tf);
		
		title_table= (TableLayout)findViewById(R.id.title_table);
		title_table.setPadding(0, 0, 0, h20);
		
		signup_btn_layout=  (LinearLayout)findViewById(R.id.signup_btn_layout);
		signup_btn_layout.setPadding(w5, 0, w5, h20);
		
		btn_signUp=(Button)findViewById(R.id.btnSignup);
		btn_signUp.setTypeface(tf);
		btn_signUp.getLayoutParams().width = (width/2)-w20;
		btn_signUp.setOnClickListener(this);
		
		btn_cancel=(Button)findViewById(R.id.btnCancle);
		btn_cancel.setTypeface(tf);
		btn_cancel.getLayoutParams().width = (width/2)-w20;
		btn_cancel.setOnClickListener(this);
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
		if(v==btn_cancel){
			finish();
		}
		if(v==btn_signUp){
			if(validation()){
					if(isInternetOn()){
							 CallSignupTask task = new CallSignupTask();
							 task.applicationContext = SignUp.this;
							 task.execute();
						
					}else{
						Toast.makeText(SignUp.this,"Network is not available....",Toast.LENGTH_SHORT).show();
					}
				}
			}
		
	}
	// validation
	public boolean validation(){
		boolean val= true;
		if(email_id.getText().length()==0){
			email_id.setError("Enter email id");
			val = false;
		}else if(email_id.getText().toString().matches("^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$")==false){
			email_id.setError("Invalid email");
			val = false;
		}
		if(password.getText().length()==0){
			password.setError("Enter password");
			val=false;
		}
		if(confirmpassword.getText().toString().length()==0){
			confirmpassword.setError("Enter confirm password");
			val = false;
		}
		if(password.getText().toString().equals(confirmpassword.getText().toString())){
			confirmpassword.setError("Password Mismatch");
			val=false;
		}
		return val;
	}
	// async class for calling webservice and get responce message
		public class CallSignupTask extends AsyncTask <String, Void,String>
		{
			protected Context applicationContext;

			@Override
			protected void onPreExecute()     
			{
				
				//dialog = ProgressDialog.show(applicationContext, "Calling", "Please wait...", true);
				dialog = new ProgressDialog(SignUp.this);
				dialog.setMessage("Please Wait....");
				dialog.show();
				Log.i("onPreExecute", "onPreExecute");
				
			}       
			
			protected void onPostExecute(String result)
			{
				
				Log.i("onPostExecute", "onPostExecute");
				if(result.toString().equals("0")){
					dialog.dismiss();
				    Intent i = new Intent(SignUp.this,Login.class);
					startActivity(i);
					finish();
				}else{
					dialog.dismiss();
					email_id.setError("User with this user name already exist");
					//Toast.makeText(SignUp.this, "User with this user name already exist",Toast.LENGTH_SHORT).show();
				}
			}     
	   
			@Override   
			protected String doInBackground(String... params) {
				// TODO Auto-generated method stub
				Log.i("doInBackground--Object", "doInBackground--Object");
				
				return obj.SignUp(email_id.getText().toString(), password.getText().toString());
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
