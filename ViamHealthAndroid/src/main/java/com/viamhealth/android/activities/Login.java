package com.viamhealth.android.activities;

import com.viamhealth.android.Global_Application;
import com.viamhealth.android.dao.db.DataBaseAdapter;
import com.viamhealth.android.dao.rest.endpoints.UserEP;
import com.viamhealth.android.dao.restclient.old.functionClass;
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
import com.viamhealth.android.utils.Validator;


public class Login extends BaseActivity implements OnClickListener
{
	private static ProgressDialog dialog;
	
	Button login_btn;
	
	EditText user_name, user_password;
	TextView sign_up;
	UserEP userEndPoint;

	DataBaseAdapter dbAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);  
		setContentView(R.layout.login_new);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		 
		appPrefs = new ViamHealthPrefs(Login.this);
		Log.e("TAG","Token is " + appPrefs.getToken());
		if(!appPrefs.getToken().toString().equals("null") || appPrefs.getToken().toString() ==null){
			Intent i=new Intent(Login.this, Home.class);
			startActivity(i);
		}else{
			dbAdapter=new DataBaseAdapter(getApplicationContext());
			dbAdapter.createDatabase();
			dbAdapter.insertDefaulValues();

            userEndPoint=new UserEP(Login.this, (Global_Application)getApplicationContext());
			
			Typeface tf = Typeface.createFromAsset(this.getAssets(),"Roboto-Condensed.ttf");
		       
			ScreenDimension();
		    
			user_name = (EditText)findViewById(R.id.user_name);
			user_password = (EditText)findViewById(R.id.user_password);
			user_password.setTypeface(tf);
			user_name.setTypeface(tf);
			    
			login_btn=(Button)findViewById(R.id.login_btn);
			login_btn.setOnClickListener(this);
			login_btn.setTypeface(tf);

			sign_up=(TextView)findViewById(R.id.sign_up);
			sign_up.setOnClickListener(this);
			sign_up.setTypeface(tf);	
		}
	} 
	
	// onclick method of all clikable control
	@Override    
	public void onClick(View v) 
	{
		// TODO Auto-generated method stub
		if(v==login_btn){
			if(isInternetOn()){
				if(validation()){
					 CallLoginTask task = new CallLoginTask();
					 task.applicationContext = Login.this;
					 task.execute();
				}
			}else{
				Toast.makeText(Login.this,"there is no network around here...",Toast.LENGTH_SHORT).show();
			}
		}
		if(v==sign_up){
			//redirect registration activity
			Intent i = new Intent(Login.this, Register.class);
			startActivity(i);
		}
		
	}

	// function for validation
	public boolean validation(){
		boolean valid=true;
		if(user_name.getText().length()==0){
			user_name.setError(getString(R.string.login_user_name_not_present));
			valid=false;
		} else if(!Validator.isEmailValid(user_name.getText().toString())){
            user_name.setError(getString(R.string.login_user_name_not_email));
            valid=false;
        }
		if(user_password.getText().length()==0){
			user_password.setError(getString(R.string.login_user_password_not_present));
			valid=false;
		}

        return valid;
	}

	// async class for calling webservice and get responce message
	public class CallLoginTask extends AsyncTask <String, Void,String>
	{
		protected Context applicationContext;

		@Override
		protected void onPreExecute() {
			//dialog = ProgressDialog.show(applicationContext, "Calling", "Please wait...", true);
			dialog = new ProgressDialog(Login.this);
			dialog.setMessage("we are validating your identity....");
			dialog.show();
			Log.i("onPreExecute", "onPreExecute");
		}
		 
		protected void onPostExecute(String response) {
			Log.i("onPostExecute", "onPostExecute");
			if(!response.equals("0")){
                dialog.dismiss();
                Toast.makeText(Login.this, "wrong credentials were given",Toast.LENGTH_SHORT).show();
			}else{
                dialog.dismiss();
                //appPrefs.setUsername(user_name.getText().toString().trim());
                Intent i=new Intent(Login.this, Home.class);
                startActivity(i);
			}
		}  

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			Log.i("doInBackground--Object", "doInBackground--Object");
			return userEndPoint.Login(user_name.getText().toString(), user_password.getText().toString());
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
