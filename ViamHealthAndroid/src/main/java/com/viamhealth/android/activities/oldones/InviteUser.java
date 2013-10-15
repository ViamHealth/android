package com.viamhealth.android.activities.oldones;

import java.util.List;

import com.viamhealth.android.dao.restclient.old.functionClass;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.viamhealth.android.R;
import com.viamhealth.android.ViamHealthPrefs;
import com.viamhealth.android.model.users.User;

public class InviteUser extends Activity implements OnClickListener {
	
	private static ProgressDialog dialog;
	Display display;
	int width,height;
	int w15,h15,w20,w10,h20,w1,h1,h10;
	Typeface tf;
	
	TextView lbl_invite_user,btn_cancle,btn_invite;
	LinearLayout below_main_layout,below_invite_layout,middle_main_layout,inviteuser_layout;
	EditText txt_email_id,txt_first_name,txt_last_name;
	
	 List<User> lstFamily = null;
	
	functionClass obj;
	ViamHealthPrefs appPrefs;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);  
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		
        setContentView(R.layout.invite_user);
       
        tf = Typeface.createFromAsset(this.getAssets(),"Roboto-Condensed.ttf");
        obj=new functionClass(InviteUser.this);
        appPrefs=new ViamHealthPrefs(InviteUser.this);
        
        //for get screen height width
        ScreenDimension();
        
        //calculate dynamic padding
        w15=(int)((width*4.69)/100);
		w20=(int)((width*6.25)/100);
		w1=(int)((width*0.4)/100);
		w10=(int)((width*3.12)/100);
		
		h15=(int)((height*3.13)/100);
		h20=(int)((height*4.17)/100);
		h1=(int)((height*0.21)/100);
		h10=(int)((height*2.08)/100);
		
		//casting control and manage padding and call onclick method
		middle_main_layout = (LinearLayout)findViewById(R.id.middle_main_layout);
		middle_main_layout.setPadding(w20, h20, w20, h20);
		
		lbl_invite_user = (TextView)findViewById(R.id.lbl_invite_user);
		lbl_invite_user.setPadding(w15, h15, w15, h15);
		
		inviteuser_layout=(LinearLayout)findViewById(R.id.inviteuser_layout);
		inviteuser_layout.setPadding(0, h15, 0, h15);
		
		below_main_layout=(LinearLayout)findViewById(R.id.below_main_layout);
		below_invite_layout=(LinearLayout)findViewById(R.id.below_invite_layout);
	
		btn_cancle = (TextView)findViewById(R.id.btn_cancle);
		btn_cancle.setPadding(w10, h10, w10, h10);
		btn_cancle.setOnClickListener(this);
		
		btn_invite = (TextView)findViewById(R.id.btn_invite);
		btn_invite.setPadding(w10, h10, w10, h10);
		btn_invite.setOnClickListener(this);
		
		txt_email_id=(EditText)findViewById(R.id.txt_email_id);
		txt_email_id.setTypeface(tf);
		
		txt_first_name=(EditText)findViewById(R.id.txt_first_name);
		txt_first_name.setTag(tf);
		
		txt_last_name=(EditText)findViewById(R.id.txt_last_name);
		txt_last_name.setTag(tf);
		
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
		if(v==btn_cancle){
			finish();
		}
		if(v==btn_invite){
			if(isInternetOn()){
				if(validation()){
					CallInviteuserTask task = new CallInviteuserTask();
					 task.applicationContext = this.getParent();
					 task.execute();
				}
			}else{
				Toast.makeText(InviteUser.this,"Network is not available....",Toast.LENGTH_SHORT).show();
			}
		}
	}
	
	public boolean validation(){
		boolean valid=true;
		if(txt_email_id.getText().toString().length()==0){
			txt_email_id.setError("Enter email id");
			valid=false;
		}else if(txt_email_id.getText().toString().matches("^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$")==false){
				txt_email_id.setError("Incorrect email id");
				valid=false;
		}
		if(txt_first_name.getText().toString().length()==0){
			txt_first_name.setError("Enter first name");
			valid=false;
		}
		if(txt_last_name.getText().toString().length()==0){
			txt_last_name.setError("Enter last name");
			valid=false;
		}
		return valid;
	}
	// async class for calling webservice and get responce message
		public class CallInviteuserTask extends AsyncTask <String, Void,String>
		{
			protected Context applicationContext;

			@Override
			protected void onPreExecute()     
			{
				
				//dialog = ProgressDialog.show(applicationContext, "Calling", "Please wait...", true);
				dialog = new ProgressDialog(InviteUser.this);
				dialog.setCanceledOnTouchOutside(false);
				dialog.setMessage("Please Wait....");
				dialog.show();
				Log.i("onPreExecute", "onPreExecute");
				
			}       
			
			protected void onPostExecute(String result)
			{
				
				Log.i("onPostExecute", "onPostExecute");
				if(result.toString().equals("0")){
					//dialog.dismiss();
					 CallFamilyTask task = new CallFamilyTask();
					 task.applicationContext = InviteUser.this.getParent();
					 task.execute();
					finish();
				}else{
					dialog.dismiss();
					Toast.makeText(InviteUser.this,result.substring(2,result.length()-2),Toast.LENGTH_SHORT).show();
				}
			}  
	   
			@Override
			protected String doInBackground(String... params) {
				// TODO Auto-generated method stub
				Log.i("doInBackground--Object", "doInBackground--Object");
                //UserEP userendpoint = new UserEP(InviteUser.this, );
				return null;//userendpoint.inviteUser(null, null, null);
			}
			     
		}     
		// async class for calling webservice and get responce message
		public class CallFamilyTask extends AsyncTask <String, Void,String>
		{
			protected Context applicationContext;

			@Override
			protected void onPreExecute()     
			{
				
				//dialog = ProgressDialog.show(applicationContext, "Calling", "Please wait...", true);
				/*dialog = new ProgressDialog(InviteUser.this);
				dialog.setMessage("Please Wait....");
				dialog.show();*/
				Log.i("onPreExecute", "onPreExecute");
				
			}        
			 
			protected void onPostExecute(String result)
			{
				
				Log.i("onPostExecute", "onPostExecute");
			    dialog.dismiss();
			    finish();
			}  
	   
			@Override
			protected String doInBackground(String... params) {
				// TODO Auto-generated method stub
				Log.i("doInBackground--Object", "doInBackground--Object");
                //UserEP userEP = new UserEP(InviteUser.this, null);
				//lstFamily = userEP.GetFamilyMembers();
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
