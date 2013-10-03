package com.viamhealth.android.activities;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.util.Calendar;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.viamhealth.android.R;
import com.viamhealth.android.ViamHealthPrefs;

import com.viamhealth.android.activities.Home;
import com.viamhealth.android.dao.restclient.functionClass;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class AddProfile extends BaseActivity implements OnClickListener {
	private static ProgressDialog dialog;
	Display display;
	int height,width;
	Typeface tf;
	int w15,w20,h10,w10,w5,h40,h5,h8,h20;
	     
	TextView heading_name,heding_name_profile_add,lbl_invite_user_goal,Add_Profile,lbl_Profile,profile_heading;
	LinearLayout menu_invite,menu_invite_out,setting_layout,profile_txt_layout,
				 profile_heading_layout,profile_mid_layout,profile_submid_layout,last_name_layout,sex_layout,born_layout,
				 date_picker_layout,height_layout,weight_layout,profile_btn_layout,emailid_layout,phonenum_layout;
	Button btnSave,btnCancle;
	EditText first_name,last_name,sex_name,born_on,txt_height,txt_weight,txt_emailid,txt_phonenum;
	ImageView person_icon;
	
	ViamHealthPrefs appPrefs;
	functionClass obj;
	
	 DateFormat fmtDateAndTime=DateFormat.getDateTimeInstance();
     Calendar dateAndTime=Calendar.getInstance();
     int pYear,pMonth,pDay;
     private DisplayImageOptions options;

     private Activity parentActivity = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);  
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		
		setContentView(R.layout.add_profile);

        //Bundle extras = (Bundle)getIntent().getBundleExtra("parent");
        parentActivity = this.getParent();
        if(parentActivity==null){
            //if(extras!=null)
              //  parentActivity = (Home) extras.get("parentClass");
        }

		appPrefs=new ViamHealthPrefs(parentActivity);
		obj = new functionClass(parentActivity);
		
		tf = Typeface.createFromAsset(this.getAssets(),"Roboto-Condensed.ttf");
		//for get screen height width
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
        
        LinearLayout l1=(LinearLayout)findViewById(R.id.l1);
        l1.setPadding(w10, 0, w10, h5);
        
        LinearLayout l2=(LinearLayout)findViewById(R.id.l2);
        l2.setPadding(w10, h10, w10, h10);
        
		//casting control and manage padding and call onclick method
       
		
		profile_txt_layout=(LinearLayout)findViewById(R.id.profile_txt_layout);
		profile_txt_layout.setPadding(w10, h10, w10, 0);  
		
		Add_Profile=(TextView)findViewById(R.id.Add_profile);
		Add_Profile.setPressed(true);
		Add_Profile.setTypeface(tf,Typeface.BOLD);
		Add_Profile.setOnClickListener(this);
        
		person_icon = (ImageView)findViewById(R.id.person_icon);
	    person_icon.getLayoutParams().width = w20;
	    person_icon.getLayoutParams().height = h20;
	        
	    options = new DisplayImageOptions.Builder().build();
 		
 		imageLoader.displayImage(appPrefs.getProfilepic(), person_icon, options, new SimpleImageLoadingListener() {
 			@Override
 			public void onLoadingComplete(Bitmap loadedImage) {
 				Animation anim = AnimationUtils.loadAnimation(AddProfile.this, R.anim.fade_in);
 				person_icon.setAnimation(anim);
 				anim.start();
 			}
 		});
 		

		lbl_Profile = (TextView)findViewById(R.id.lbl_Profile);
		lbl_Profile.setTypeface(tf,Typeface.BOLD);
		
		profile_heading = (TextView)findViewById(R.id.profile_heading);
		profile_heading.setTypeface(tf,Typeface.BOLD);
		
		profile_heading_layout=(LinearLayout)findViewById(R.id.profile_heading_layout);
		profile_heading_layout.setPadding(w10, 0, w10, 0);
		
		profile_mid_layout=(LinearLayout)findViewById(R.id.profile_mid_layout);
		profile_mid_layout.setPadding(w10, 0, w10, 0);
		
		profile_submid_layout=(LinearLayout)findViewById(R.id.profile_submid_layout);
		profile_submid_layout.setPadding(w10, h8, w20, h20);
		
		last_name_layout=(LinearLayout)findViewById(R.id.last_name_layout);
		last_name_layout.setPadding(0, h8, 0, 0);
		
		sex_layout=(LinearLayout)findViewById(R.id.sex_layout);
		sex_layout.setPadding(0, h8, 0, 0);
		
		born_layout=(LinearLayout)findViewById(R.id.born_layout);
		born_layout.setPadding(0, h8, 0, 0);
		
		date_picker_layout=(LinearLayout)findViewById(R.id.date_picker_layout);
		date_picker_layout.setPadding(0, 0, w5, 0);
		date_picker_layout.setOnClickListener(this);
		
		height_layout=(LinearLayout)findViewById(R.id.height_layout);
		height_layout.setPadding(0, h8, 0, 0);
		
		weight_layout=(LinearLayout)findViewById(R.id.weight_layout);
		weight_layout.setPadding(0, h8, 0, 0);
		
		emailid_layout=(LinearLayout)findViewById(R.id.emailid_layout);
		emailid_layout.setPadding(0, h8, 0, 0);
		
		phonenum_layout=(LinearLayout)findViewById(R.id.phonenum_layout);
		phonenum_layout.setPadding(0, h8, 0, 0);
		
		profile_btn_layout = (LinearLayout)findViewById(R.id.profile_btn_layout);
		profile_btn_layout.setPadding(w10, h10, w10, h10);
		
		btnSave = (Button)findViewById(R.id.btnSave_profile);
		btnSave.setTypeface(tf);
		btnSave.setOnClickListener(this);
		
		btnCancle=(Button)findViewById(R.id.btnCancle_profile);
		btnCancle.setTypeface(tf);
		btnCancle.setOnClickListener(this);
		
		first_name = (EditText)findViewById(R.id.first_name);
		last_name=(EditText)findViewById(R.id.last_name);
		sex_name=(EditText)findViewById(R.id.sex_name);
		born_on=(EditText)findViewById(R.id.born_on);
		txt_height=(EditText)findViewById(R.id.txt_height);
		txt_weight=(EditText)findViewById(R.id.txt_weight);
		txt_emailid=(EditText)findViewById(R.id.txt_profile_emailid);
		txt_phonenum=(EditText)findViewById(R.id.txt_phonenum);
		
		// for generate menu
		 pYear = dateAndTime.get(Calendar.YEAR);
	     pMonth = dateAndTime.get(Calendar.MONTH);
	     pDay = dateAndTime.get(Calendar.DAY_OF_MONTH);
	    // updateDisplay();
	}
    public static Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
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
		if(v==btnSave){
			if(isInternetOn()){
				if(validation()){
				     CallAddProfileTask task = new CallAddProfileTask();
					 task.applicationContext = parentActivity;
					 task.execute();
				}
			}else{
				Toast.makeText(parentActivity,"Network is not available....",Toast.LENGTH_SHORT).show();
			}
		}
		if(v==btnCancle){
				finish();
		}
		if(v==date_picker_layout){
			new DatePickerDialog(parentActivity, d,pYear,
                    pMonth,
                    pDay).show();
		}
		if(v==lbl_invite_user_goal){
			Log.e("TAG","Selected value is " + "invite user is clicked");
			Animation anim = AnimationUtils.loadAnimation(AddProfile.this, R.anim.fade_out);
			setting_layout.startAnimation(anim);
			setting_layout.setVisibility(View.INVISIBLE);
			menu_invite.setVisibility(View.VISIBLE);
			menu_invite_out.setVisibility(View.INVISIBLE);
			Intent i = new Intent(parentActivity,InviteUser.class);
			startActivity(i);
		}
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
        born_on.setText(
        new StringBuilder()
            // Month is 0 based so add 1
            .append(pYear).append("-")
            .append(pMonth + 1).append("-")
            .append(pDay).append(" "));
    }

	public boolean validation(){
		boolean valid=true;
		if(first_name.getText().toString().length()==0){
			first_name.setError("Enter first name");
			valid=false;
		}
		if(last_name.getText().toString().length()==0){
			last_name.setError("Enter last name");
			valid=false;
		}
		if(sex_name.getText().toString().length()==0){
			sex_name.setError("Enter gender");
			valid=false;
		}
		if(born_on.getText().toString().length()==0){
			born_on.setError("Enter Birth date");
			valid=false;
		}
		if(txt_height.getText().toString().length()==0){
			txt_height.setError("Enter height");
			valid=false;
		}
		if(txt_weight.getText().toString().length()==0){
			txt_weight.setError("Enter weight");
			valid=false;
		}
		if(txt_emailid.getText().toString().length()==0){
			txt_emailid.setError("Enter Email id");
			valid=false;
		}else if(txt_emailid.getText().toString().matches("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+")==false){
			txt_emailid.setError("Incorrect email id");
			valid=false;
	    }
		if(txt_phonenum.getText().toString().length()==0){
			txt_phonenum.setError("Enter Phone number");
			valid=false;
		}
		return valid;
	}

	// async class for calling webservice and get responce message
		public class CallAddProfileTask extends AsyncTask <String, Void,String>
		{
			protected Context applicationContext;

			@Override
			protected void onPreExecute()     
			{
				
				//dialog = ProgressDialog.show(applicationContext, "Calling", "Please wait...", true);
				dialog = new ProgressDialog(AddProfile.this);
				dialog.setMessage("Please Wait....");
				dialog.show();
				Log.i("onPreExecute", "onPreExecute");
				
			}       
			
			protected void onPostExecute(String result)
			{
				
				Log.i("onPostExecute", "onPostExecute");
				if(result.toString().equals("0")){
					dialog.dismiss();
					finish();
				}else{
					dialog.dismiss();
					Toast.makeText(AddProfile.this, "Can't able to add new profile...",Toast.LENGTH_SHORT).show();
				}
			}  
	   
			@Override
			protected String doInBackground(String... params) {
				// TODO Auto-generated method stub
				Log.i("doInBackground--Object", "doInBackground--Object");
				String sex = sex_name.getText().toString();
				if(sex.equalsIgnoreCase("female")){
					sex="F";
				}else if(sex.equalsIgnoreCase("male")){
					sex="M";
				}
				return obj.addProfile(first_name.getText().toString(), last_name.getText().toString(), sex,
									  born_on.getText().toString(), txt_height.getText().toString(), txt_weight.getText().toString(),
									  txt_emailid.getText().toString(), txt_phonenum.getText().toString());
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
		protected void onResume() {
			// TODO Auto-generated method stub
			super.onResume();
			
		}
	@Override
	 public void onBackPressed() 
	 {
	          
	         
	 }

}
