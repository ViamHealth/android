package com.viamhealth.android.activities.oldones;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;

import com.viamhealth.android.activities.BaseActivity;
import com.viamhealth.android.activities.Downlaod;
import com.viamhealth.android.activities.Home;
import com.viamhealth.android.activities.UploadFile;
import com.viamhealth.android.adapters.FileDataAdapter;
import com.viamhealth.android.Global_Application;
import com.viamhealth.android.adapters.GoalDataAdapter;
import com.viamhealth.android.R;
import com.viamhealth.android.ViamHealthPrefs;
import com.viamhealth.android.dao.restclient.old.functionClass;
import com.viamhealth.android.model.FileData;
import android.app.Activity;
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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.viamhealth.android.ui.RefreshableListView;
import android.widget.TextView;
import android.widget.Toast;

public class File extends BaseActivity implements OnClickListener{
	private static ProgressDialog dialog;
	
	
	Display display;
	int height,width;
	int h40,w5,w15,w20,h10,w10,h1,w1,w9,h9,w8,h5,w35,w3,h3,w80,h20;
	
	TextView lbl_invite_user_file,lbl_files,heding_name,lbl_upload,lbl_share,lbl_delete,lbl_download,lbl_file_title,
			 lbl_label_title,lbl_date_title,goal_count,heding_name_food,lbl_invite_user_food;
	LinearLayout goal_list_layout,filename_lbl_layout,settiglayout_food,menu_invite_food,menu_invite_out_food,search_layout,file_mid_layout,
				download_layout_main,download_layout,lable_file_layout,
				date_file_layout;
	EditText edt_search;
	RefreshableListView listfile;
	ImageView back,person_icon;
	Typeface tf;
	
	ViamHealthPrefs appPrefs;
	functionClass obj;
	Activity myactivity;
	String delid;
	Global_Application ga;
	String selecteduserid="0";
	ArrayList<FileData> lstResult = new ArrayList<FileData>();
	private DisplayImageOptions options;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.file);
		
		appPrefs = new ViamHealthPrefs(this.getParent());
		obj=new functionClass(this.getParent());
		  ga=((Global_Application)getApplicationContext());
		tf = Typeface.createFromAsset(this.getAssets(),"Roboto-Condensed.ttf");
	
		// get screen height and width
		ScreenDimension();
		
		// calculate padding dynamically accroding to different screen
		w5=(int)((width*1.7)/100);
		w20=(int)((width*6.25)/100);
		w15=(int)((width*4.68)/100);
		w10=(int)((width*3.13)/100);
		w35=(int)((width*7.30)/100);
		w1=(int)((width*0.32)/100);
		w9=(int)((width*2.81)/100);
		w8=(int)((width*2.50)/100);
		w3=(int)((width*0.94)/100);
		w80=(int)((width*25)/100);
		
		h40=(int)((height*8.34)/100);
		h10=(int)((height*2.083)/100);
		h1=(int)((height*0.21)/100);
		h9=(int)((height*1.9)/100);
		h5=(int)((height*1.042)/100);
		h3=(int)((height*0.63)/100);
		h20=(int)((height*4.17)/100);


		// contol casting, set typeface of textview and edittext and manage onclicck of button
		back=(ImageView)findViewById(R.id.back);
     	back.setOnClickListener(File.this);
		
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
		
		goal_count=(TextView)findViewById(R.id.goal_count);
		goal_count.setTypeface(tf);
		
		goal_list_layout=(LinearLayout)findViewById(R.id.goal_list_layout);
		goal_list_layout.setPadding(w10, 0, w10,h10);
		
		filename_lbl_layout=(LinearLayout)findViewById(R.id.filename_lbl_layout);
		filename_lbl_layout.setPadding(w20, 0,0,0);
		
		lbl_files=(TextView)findViewById(R.id.lbl_files);
		lbl_files.setTypeface(tf);
		
		
		lbl_file_title=(TextView)findViewById(R.id.lbl_file_title);
		lbl_file_title.setPadding(w5, h5, w5, h5);
		lbl_file_title.setTypeface(tf);
	
		person_icon = (ImageView)findViewById(R.id.person_icon);
        person_icon.getLayoutParams().width = w20;
        person_icon.getLayoutParams().height = h20;
        
        options = new DisplayImageOptions.Builder()
 		.build();
 		
 		imageLoader.displayImage(appPrefs.getProfilepic(), person_icon, options, new SimpleImageLoadingListener() {
 			@Override
 			public void onLoadingComplete(Bitmap loadedImage) {
 				Animation anim = AnimationUtils.loadAnimation(File.this, R.anim.fade_in);
 				person_icon.setAnimation(anim);
 				anim.start();
 				
 				
 			}
 		});
		edt_search=(EditText)findViewById(R.id.edt_search);
		edt_search.setTypeface(tf);
		edt_search.setPadding(w5, 0, 0, 0);
		edt_search.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				if(isInternetOn()){
					 CallFileSearchTask task = new CallFileSearchTask();
					 task.applicationContext =File.this.getParent();
					 task.execute();
				}else{
					Toast.makeText(File.this,"Network is not available....",Toast.LENGTH_SHORT).show();
				}
			}
		});
		
		lbl_upload = (TextView)findViewById(R.id.lbl_upload);
		lbl_upload.setCompoundDrawablesWithIntrinsicBounds(R.drawable.file_upload, 0, 0, 0);
		lbl_upload.setPadding(w8, h9, w8, h9);
		lbl_upload.setOnClickListener(this);
		lbl_upload.setTypeface(tf);
		
		lbl_share = (TextView)findViewById(R.id.lbl_share);
		lbl_share.setCompoundDrawablesWithIntrinsicBounds(R.drawable.file_share, 0, 0, 0);
		lbl_share.setPadding(w8, h9, w8, h9);
		
		lbl_delete = (TextView)findViewById(R.id.lbl_delete);
		lbl_delete.setCompoundDrawablesWithIntrinsicBounds(R.drawable.file_delete, 0, 0, 0);
		lbl_delete.setPadding(w8, h9, w8, h9);
		lbl_delete.setOnClickListener(this);
		lbl_delete.setTypeface(tf);
		
		lbl_download = (TextView)findViewById(R.id.lbl_download);
		lbl_download.setCompoundDrawablesWithIntrinsicBounds(R.drawable.file_download, 0, 0, 0);
		lbl_download.setPadding(w8, h9, w8, h9);
		lbl_download.setOnClickListener(this);
		lbl_download.setTypeface(tf);
		  
		search_layout = (LinearLayout)findViewById(R.id.search_layout);
		search_layout.setPadding(w10, h10, w10, h10);
		
		file_mid_layout = (LinearLayout)findViewById(R.id.file_mid_layout);
	
		
		listfile = (RefreshableListView)findViewById(R.id.lstfile);
		//listfile.setPadding(w3, h3, w3, h3);
		 listfile.setOnRefreshListener(new RefreshableListView.OnRefreshListener() {
				
				@Override
				public void onRefresh() {
					// TODO Auto-generated method stub
					if(!ga.getNextfile().toString().equals("null")){
						
						if(isInternetOn()){
							 CallFileNevigationTask task = new CallFileNevigationTask();
							 task.applicationContext =File.this.getParent();
							 task.execute();
						}else{
							Toast.makeText(File.this,"Network is not available....",Toast.LENGTH_SHORT).show();
						}
					}
				}
			   });
		// for generate menu

		 actionmenu();
		/*
		if(isInternetOn()){
			CallFileTask task = new CallFileTask();
			 task.applicationContext =this.getParent();
			 task.execute();
			 appPrefs.setReloadgraph("0");
		}else{
			Toast.makeText(File.this,"Network is not available....",Toast.LENGTH_SHORT).show();
		}     */
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
		    		Animation anim = AnimationUtils.loadAnimation(File.this, R.anim.fade_out);
					settiglayout_food.startAnimation(anim);
					settiglayout_food.setVisibility(View.INVISIBLE);
					menu_invite_food.setVisibility(View.VISIBLE);
					menu_invite_out_food.setVisibility(View.INVISIBLE);
					
					selecteduserid = Integer.toString(position);
					
					CalluserMeTask task = new CalluserMeTask();
					task.applicationContext =File.this.getParent();
					task.execute();
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
		if(v==back){
			Intent i=new Intent(File.this, Home.class);
			startActivity(i);
			finish();
		}
		
		if(v==lbl_upload){
			// reditect uplod file screen
			//uploadImage();
			Intent DisplayImage = new Intent(getParent(),UploadFile.class);
			TabGroupActivity parentoption = (TabGroupActivity)getParent();
			parentoption.startChildActivity("DisplayImage",DisplayImage);
		}
		if(v==lbl_delete){
			boolean val=false;
			for(int i=0;i<lstResult.size();i++){
				if(lstResult.get(i).isChecked()){
					delid=lstResult.get(i).getId() + "," + delid;
					val=true;
				}
			}

			if(val==true){
				Log.e("TAG",delid.toString().substring(0, delid.length()-5) + " th cb is checked");
				if(isInternetOn()){
					 DeleteFileTask task = new DeleteFileTask();
					 task.applicationContext =this.getParent();
					 task.execute();
					 //appPrefs.setReloadgraph("0");
				}else{
					Toast.makeText(File.this,"Network is not available....",Toast.LENGTH_SHORT).show();
				}     
			}else{
				Toast.makeText(File.this, "Please select atlest one file..", Toast.LENGTH_SHORT).show();
			}
		}
		if(v==lbl_download){
			String url = null;
			boolean val=false;
			for(int i=0;i<lstResult.size();i++){
				if(lstResult.get(i).isChecked()){
					url=lstResult.get(i).getDownload_url() + "," + url;
					val=true;
				}
			}
		
			if(val==true){
				Log.e("TAG",url.toString().substring(0, url.length()-5) + " urls");
				ga.setDownload(url.toString().substring(0, url.length()-5));
				Intent i = new Intent(File.this,Downlaod.class);
				startActivity(i);
			}else{
				Toast.makeText(File.this, "Please select atlest one file..", Toast.LENGTH_SHORT).show();
			}
		}
		if(v==lbl_invite_user_food){
			Log.e("TAG","Selected value is " + "invite user is clicked");
			Animation anim = AnimationUtils.loadAnimation(File.this, R.anim.fade_out);
			settiglayout_food.startAnimation(anim);
			settiglayout_food.setVisibility(View.INVISIBLE);
			menu_invite_food.setVisibility(View.VISIBLE);
			menu_invite_out_food.setVisibility(View.INVISIBLE);
			Log.e("TAG","Clicked");
			Intent i = new Intent(File.this,InviteUser.class);
			startActivity(i);
		}
	
			if(v==menu_invite_food){
			actionmenu();
			settiglayout_food.setVisibility(View.VISIBLE);
			menu_invite_out_food.setVisibility(View.VISIBLE);
			menu_invite_food.setVisibility(View.INVISIBLE);
			Animation anim = AnimationUtils.loadAnimation(File.this, R.anim.fade_in);
			settiglayout_food.startAnimation(anim);
			
			Log.e("TAG","Clicked");
		}
		if(v==menu_invite_out_food){
			Animation anim = AnimationUtils.loadAnimation(File.this, R.anim.fade_out);
			settiglayout_food.startAnimation(anim);
			settiglayout_food.setVisibility(View.INVISIBLE);
			menu_invite_food.setVisibility(View.VISIBLE);
			menu_invite_out_food.setVisibility(View.INVISIBLE);
			Log.e("TAG","Clicked");
		}
		
	}
	
	
	
	// async class for calling webservice and get responce message
			public class CallFileTask extends AsyncTask <String, Void,String>
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
						dialog.dismiss();
						if(lstResult.size()>0){
							 goal_count.setText("("+lstResult.size()+")");
							 FileDataAdapter adapter = new FileDataAdapter(File.this.getParent(),R.layout.filelist, lstResult);
						     listfile.setAdapter(adapter);	
						     adapter.notifyDataSetChanged();
						     listfile.onRefreshComplete();
						}else{
							Toast.makeText(getParent(), "No goals found...",Toast.LENGTH_SHORT).show();
						}
				}  
		   
				@Override
				protected String doInBackground(String... params) {
					// TODO Auto-generated method stub
					Log.i("doInBackground--Object", "doInBackground--Object");
					lstResult.clear();
					lstResult=obj.getFile(Global_Application.url+"healthfiles/?user="+appPrefs.getUserid());
					return null;
				}
				   
			}     
			// async class for calling webservice and get responce message
						public class CallFileSearchTask extends AsyncTask <String, Void,String>
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
									dialog.dismiss();
									if(lstResult.size()>0){
										 goal_count.setText("("+lstResult.size()+")");
										 FileDataAdapter adapter = new FileDataAdapter(File.this.getParent(),R.layout.filelist, lstResult);
									     listfile.setAdapter(adapter);	
									     adapter.notifyDataSetChanged();
									     listfile.onRefreshComplete();
									}else{
										Toast.makeText(getParent(), "No goals found...",Toast.LENGTH_SHORT).show();
									}
							}  
					   
							@Override
							protected String doInBackground(String... params) {
								// TODO Auto-generated method stub
								Log.i("doInBackground--Object", "doInBackground--Object");
								lstResult=obj.getFile(Global_Application.url+"healthfiles/?search="+edt_search.getText().toString());
								return null;
							}
							   
						}     
						
			// async class for calling webservice and get responce message
						public class CallFileNevigationTask extends AsyncTask <String, Void,String>
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
									dialog.dismiss();
									Log.e("TAG","File list size : " + lstResult.size());
									if(lstResult.size()>0){
										 goal_count.setText("("+lstResult.size()+")");
										 FileDataAdapter adapter = new FileDataAdapter(File.this.getParent(),R.layout.filelist, lstResult);
									     listfile.setAdapter(adapter);	
									     adapter.notifyDataSetChanged();
									     listfile.onRefreshComplete();
									}else{
										Toast.makeText(getParent(), "No goals found...",Toast.LENGTH_SHORT).show();
									}
							}  
					   
							@Override
							protected String doInBackground(String... params) {
								// TODO Auto-generated method stub
								Log.i("doInBackground--Object", "doInBackground--Object");
								lstResult.addAll(obj.getFile(ga.getNextfile()));
								
								return null;
							}
							   
						}     
			// async class for calling webservice and get responce message
					public class DeleteFileTask extends AsyncTask <String, Void,String>
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
								dialog.dismiss();
								if(isInternetOn()){
									CallFileTask task = new CallFileTask();
									 task.applicationContext =File.this.getParent();
									 task.execute();
									 appPrefs.setReloadgraph("0");
								}else{
									Toast.makeText(File.this,"Network is not available....",Toast.LENGTH_SHORT).show();
								}     
						}  
				   
						@Override
						protected String doInBackground(String... params) {
							// TODO Auto-generated method stub
							Log.i("doInBackground--Object", "doInBackground--Object");
							String[] temp = delid.toString().substring(0, delid.length()-5).split(",");
							for(int i=0;i<temp.length;i++){
								obj.FileDelete(Global_Application.url + "healthfiles/"+temp[i]+"/");
							}
							return null;
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
							dialog = new ProgressDialog(File.this.getParent());
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
						/*	Intent intent = new Intent(GoalActivity.this,MainActivity.class);
							startActivity(intent);*/
						}  
				   
						@Override
						protected String doInBackground(String... params) {
							// TODO Auto-generated method stub
							Log.i("doInBackground--Object", "doInBackground--Object");
							//obj.GetUserProfile(ga.getLstfamilyglobal().get(Integer.parseInt(selecteduserid)).getId());
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
				protected void onResume() {
					// TODO Auto-generated method stub
					super.onResume();
					
				}

		@Override
		    public void onBackPressed() 
		        {
		          
		         
		        }
		
}
