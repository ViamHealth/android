package com.viamhealth.android.activities;

import java.util.ArrayList;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.viamhealth.android.Global_Application;
import com.viamhealth.android.R;
import com.viamhealth.android.ViamHealthPrefs;

import com.viamhealth.android.dao.restclient.functionClass;

import com.viamhealth.android.model.FamilyData;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Home extends BaseActivity implements OnClickListener{
	Display display;
	int width,height;
	
	LinearLayout main_layout;
	LinearLayout[] layout11 = new LinearLayout[6];
	FrameLayout[] frm = new FrameLayout[6];
	
	ViamHealthPrefs appPrefs;
	Global_Application ga;
	int cnt=0,_count=0;
	int w80,w90,h90,w20,h5,w5,w12,h30;
	ArrayList<String> msgArray = new ArrayList<String>();
	ArrayList<FamilyData> lstFamily = new ArrayList<FamilyData>();
	ProgressDialog dialog;
	
	String selecteduserid;
	
	functionClass obj;
	private DisplayImageOptions options; 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);  
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		
		setContentView(R.layout.home);
		
		// for get screen diamention
		ScreenDimension();
		
		//calculate dynamic height width and padding
		w80=(int)((width*25)/100);
		w90=(int)((width*28.12)/100);
		w20=(int)((width*6.25)/100);
		w5=(int)((width*1.56)/100);
		w12=(int)((width*3.75)/100);
		
		h90=(int)((height*18.75)/100);
		h5=(int)((height*1.042)/100);
		h30=(int)((height*6.25)/100);
		
		appPrefs = new ViamHealthPrefs(Home.this);
		ga=((Global_Application)getApplicationContext());
		obj = new functionClass(this);
		
		msgArray.add("Update Weight...");
		msgArray.add("Update Daily Food...");
		msgArray.add("Vaccination due on...");
		msgArray.add("Update Height...");
		msgArray.add("Annual Health Checkup...");
		/*msgArray.add("take test 6");
		msgArray.add("take test 7");*/
		//for generate square
		main_layout = (LinearLayout)findViewById(R.id.main_layout);
		
		if(isInternetOn()){
		     CallFamilyTask task = new CallFamilyTask();
			 task.applicationContext = Home.this;
			 task.execute();
		
	}else{
		Toast.makeText(Home.this,"Network is not available....",Toast.LENGTH_SHORT).show();
	}
	}     
	public void ScreenDimension()
		{
			display = getWindowManager().getDefaultDisplay(); 
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
			width = display.getWidth();
			height = display.getHeight();

		}
	public void generateView(){
	
	  	Log.e("TAG","Generate view is call");
	  	  String[] str = appPrefs.getMenuList().split(",");
	    for(int i=0;i<6;i=i+2){
	    	
	    	LinearLayout layout1 = new LinearLayout(Home.this);
	    	int cnt=i;
	    	while(cnt<(i+2)){
	    		if(cnt<lstFamily.size()){
	    			layout11[cnt] = new LinearLayout(Home.this);
	    	    	layout11[cnt].setOrientation(LinearLayout.VERTICAL);
	    	    	layout11[cnt].setLayoutParams(new FrameLayout.LayoutParams(width/2,width/2));
	    	    	layout11[cnt].setPadding(2, 2, 2, 2);
			    		layout1.setOrientation(LinearLayout.HORIZONTAL);
			    		 FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(
			    				 FrameLayout.LayoutParams.FILL_PARENT, FrameLayout.LayoutParams.FILL_PARENT);
			    		 
			    		frm[cnt] = new FrameLayout(Home.this);
			    		frm[cnt].setLayoutParams(lp);
			    		frm[cnt].setId(cnt);
			    		frm[cnt].setOnClickListener(Home.this);
			    		final ImageView imgProfile = new ImageView(Home.this);
			    		//imgProfile.setImageResource(R.drawable.profilepic);
			    		frm[cnt].addView(imgProfile);
			    		
			    		options = new DisplayImageOptions.Builder()
						.build();
						
						imageLoader.displayImage(lstFamily.get(cnt).getProfile_picture_url(), imgProfile, options, new SimpleImageLoadingListener() {
							@Override
							public void onLoadingComplete(Bitmap loadedImage) {
								Animation anim = AnimationUtils.loadAnimation(Home.this, R.anim.fade_in);
								imgProfile.setAnimation(anim);
								anim.start();
								
								
							}
						});
			    		
			    		LinearLayout lay = new LinearLayout(Home.this);
			    		lay.setOrientation(LinearLayout.VERTICAL);
			    		lay.setGravity(Gravity.BOTTOM);
			    		
			    		 TextView txtName = new TextView(Home.this);
			    		      
			    		 
			    		txtName.setText(lstFamily.get(cnt).getFirst_name() + " " + lstFamily.get(cnt).getLast_name());
			    		txtName.setTag(lstFamily.get(cnt).getFirst_name() + " " + lstFamily.get(cnt).getLast_name());
			    		txtName.setPadding(w5, h5, w5, h5);
			    		txtName.setTextColor(Color.WHITE); 
			    		txtName.setBackgroundResource(R.color.textbg);
			    		txtName.setGravity(Gravity.CENTER_VERTICAL);
			    		
			    		/*final TextView txtdesc = new TextView(Home.this);
			    	 	txtdesc.setTextColor(Color.WHITE);
			    	 	txtdesc.setGravity(Gravity.BOTTOM);
			    	 	txtdesc.setPadding(w5, 0, w5, h5);
			    	 	txtdesc.setBackgroundResource(R.color.textbg);

			    	 	Timer _t = new Timer();
		
			    	     _t.scheduleAtFixedRate( new TimerTask() {
			    	             @Override
			    	             public void run() {
			    	                
		
			    	                 runOnUiThread(new Runnable() //run on ui thread
			    	                  {
			    	                   public void run() 
			    	                   { 
			    	                	   if(_count<msgArray.size()){
			    	                		   txtdesc.setText("  "+ msgArray.get(_count));
			    	                      	   _count++;
			    	                	   }
			    	                      if(_count==msgArray.size()){
			    	                    	  _count=0;
			    	                      }
			    	                  }
			    	                  });   
			    	             }
			    	         }, 1000, 1000 ); 
			    	 	*/
			    		lay.addView(txtName);
			    		/*lay.addView(txtdesc);
			    	 	txtdesc.getLayoutParams().width=width/2;
			    	 	txtdesc.setSingleLine();*/
			    		frm[cnt].addView(lay);
			    		layout11[cnt].setId(cnt);
			    		layout11[cnt].addView(frm[cnt]);
			    		layout1.addView(layout11[cnt]);
			    		   
	    		}else{
	    			layout11[cnt] = new LinearLayout(Home.this);
	    	    	layout11[cnt].setOrientation(LinearLayout.VERTICAL);
	    	    	layout11[cnt].setLayoutParams(new FrameLayout.LayoutParams(width/2,width/2));
	    	    	layout11[cnt].setPadding(2, 2, 2, 2);
	    			ImageView img1 = new ImageView(Home.this);
		    		img1.setImageResource(R.drawable.addprofile);
		    		layout11[cnt].addView(img1);
		    		layout11[cnt].setGravity(Gravity.CENTER_VERTICAL);
		    		layout11[cnt].setId(i);
		    		layout11[cnt].setOnClickListener(Home.this);
		    		layout1.addView(layout11[cnt]);
	    		}
	    		cnt++;
	    	}
	    	main_layout.addView(layout1);
	    /*	if(i>str.length){
	    		layout11[i] = new LinearLayout(Home.this);
		    	layout11[i].setOrientation(LinearLayout.VERTICAL);
		    	layout11[i].setLayoutParams(new FrameLayout.LayoutParams(width/2,width/2));
		    	layout11[i].setBackgroundColor(Color.rgb(206,49, 49));
	    		ImageView img = new ImageView(Home.this);
	    		img.setImageResource(R.drawable.plus);
	    		
	    		TextView txt = new TextView(Home.this);
	    		txt.setText("Add a ")
	    		layout11[i].addView(img);
	    		layout11[i].setGravity(Gravity.CENTER_VERTICAL);
	    		layout11[i].setId(i);
	    		layout11[i].setOnClickListener(Home.this);
	    		layout1.addView(layout11[i]);
	    		
	    		layout11[i+1] = new LinearLayout(Home.this);
		    	layout11[i+1].setOrientation(LinearLayout.VERTICAL);
		    	layout11[i+1].setLayoutParams(new FrameLayout.LayoutParams(width/2,width/2));
	    		ImageView img1 = new ImageView(Home.this);
	    		img1.setImageResource(R.drawable.plus);
	    		layout11[i+1].addView(img1);
	    		layout11[i+1].setGravity(Gravity.CENTER_VERTICAL);
	    		layout11[i+1].setId(i);
	    		layout11[i+1].setOnClickListener(Home.this);
	    		layout1.addView(layout11[i+1]);
	    	}*/
	    	}
	    	

	}
	@Override    
	public void onClick(View v) {
		// TODO Auto-generated method stub
	   for(int i=0;i<6;i++){
			Log.e("TAG","id is : " + v.getId());
			i=v.getId();
			if(v.getId()==i && v.getId()<lstFamily.size()){
				LinearLayout tr1lay=(LinearLayout)layout11[i];
				FrameLayout tr1frm=(FrameLayout)frm[i];
				LinearLayout tr1=(LinearLayout)tr1frm.getChildAt(1);
				TextView txt = (TextView)tr1.getChildAt(0);
				Log.e("TAG","profile name is : " + txt.getTag());
				appPrefs.setProfileName(txt.getTag().toString());
				appPrefs.setGoalDisable("0");
				selecteduserid=lstFamily.get(i).getId();
				
				if(isInternetOn()){
				     CalluserMeTask task = new CalluserMeTask();
					 task.applicationContext = Home.this;
					 task.execute();
				
				}else{
					Toast.makeText(Home.this,"Network is not available....",Toast.LENGTH_SHORT).show();
				}
			
				break;
			}else{
				Log.e("TAG","Invite user is call");
				appPrefs.setGoalDisable("0");
				ga.setInviteuser_flg(1);
				Intent intent = new Intent(Home.this,MainActivity.class);
				startActivity(intent);
				/*Intent intent = new Intent(Home.this,InviteUser.class);
				startActivity(intent);*/
				break;
			}  
		}
	}
	// async class for calling webservice and get responce message
		public class CalluserMeTask extends AsyncTask <String, Void,String>
		{
			protected Context applicationContext;

			@Override
			protected void onPreExecute()     
			{
				
				//dialog = ProgressDialog.show(applicationContext, "Calling", "Please wait...", true);
				dialog = new ProgressDialog(Home.this);
				dialog.setCanceledOnTouchOutside(false);
				dialog.setMessage("Please Wait....");
				dialog.show();
				Log.i("onPreExecute", "onPreExecute");
				
			}        
			 
			protected void onPostExecute(String result)
			{
				
				Log.i("onPostExecute", "onPostExecute");
				//generateView();
				dialog.dismiss();
				Intent intent = new Intent(Home.this,MainActivity.class);
                intent.putExtra("user_id",selecteduserid);
				startActivity(intent);
			}  
	   
			@Override
			protected String doInBackground(String... params) {
				// TODO Auto-generated method stub
				Log.i("doInBackground--Object", "doInBackground--Object");
				lstFamily = obj.GetUserProfile(selecteduserid);
				return null;
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
			dialog = new ProgressDialog(Home.this);
			dialog.setCanceledOnTouchOutside(false);
			dialog.setMessage("Please Wait....");
			dialog.show();
			Log.i("onPreExecute", "onPreExecute");
			
		}        
		 
		protected void onPostExecute(String result)
		{
			
			Log.i("onPostExecute", "onPostExecute");
			generateView();
			dialog.dismiss();
		}  
   
		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			Log.i("doInBackground--Object", "doInBackground--Object");
			lstFamily = obj.GetFamilyMember(appPrefs.getUsername().toString());
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
			moveTaskToBack(true);  
			System.exit(0);
			return;
         
        }
	
}
