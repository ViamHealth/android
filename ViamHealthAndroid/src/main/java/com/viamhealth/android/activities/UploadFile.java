package com.viamhealth.android.activities;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.viamhealth.android.Global_Application;
import com.viamhealth.android.adapters.GoalDataAdapter;
import com.viamhealth.android.R;
import com.viamhealth.android.ViamHealthPrefs;

import com.viamhealth.android.dao.rest.endpoints.UserEP;
import com.viamhealth.android.dao.restclient.old.functionClass;

import com.viamhealth.android.model.FileData;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class UploadFile extends BaseActivity implements OnClickListener{
	Display display;
	int height,width;
	int w15,w20,h40,w5,h20,w100;
	
	private static final int CAMERA_PIC_REQUEST = 1337;
	Bitmap mBitmap,b1;
	byte[] byteArray;
	String Base64str=null; 
	String path;
	String selecteduserid="0";
	
	ArrayList<FileData> lstResult = new ArrayList<FileData>();
	
	ImageView img_display,back,person_icon;
	TextView lbl_invite_user_food,heding_name_food;
	LinearLayout menu_invite_food,menu_invite_out_food,settiglayout_food;
	
	Global_Application ga;
	functionClass obj;
	private static ProgressDialog dialog;
	Button btn_upload,btn_cancle;
	static int serverResponseCode = 0;
	String upLoadServerUri = null;  
	String filename=null;
	static EditText file_desc;
	public static ViamHealthPrefs appPrefs;
	LinearLayout addphoto;
	
	Typeface tf;
	private DisplayImageOptions options;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.upload_file);
		ga=((Global_Application)getApplicationContext());
		obj = new functionClass(this.getParent());
		appPrefs = new ViamHealthPrefs(UploadFile.this);
		ScreenDimension();
		
		tf = Typeface.createFromAsset(this.getAssets(),"Roboto-Condensed.ttf");
		w15=(int)((width*4.68)/100);
		w20=(int)((width*6.25)/100);
		w5=(int)((width*1.7)/100);
		w100=(int)((width*31.25)/100);
		
		
		h40=(int)((height*8.34)/100);
		h20=(int)((height*4.17)/100);
		
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
  		
		img_display = (ImageView)findViewById(R.id.img_display);
		img_display.getLayoutParams().width = w100;
		img_display.getLayoutParams().height = w100;
		
		addphoto = (LinearLayout)findViewById(R.id.addphoto);
		addphoto.setOnClickListener(UploadFile.this);
		btn_upload = (Button)findViewById(R.id.btnUpload);
		btn_upload.setOnClickListener(this);
		
		btn_cancle=(Button)findViewById(R.id.btnCancle);
		btn_cancle.setOnClickListener(this);
		
		 person_icon = (ImageView)findViewById(R.id.person_icon);
	     person_icon.getLayoutParams().width = w20;
	     person_icon.getLayoutParams().height = h20;
	       
	     options = new DisplayImageOptions.Builder()
	 		.build();
	 		
	 		imageLoader.displayImage(appPrefs.getProfilepic(), person_icon, options, new SimpleImageLoadingListener() {
	 			@Override
	 			public void onLoadingComplete(Bitmap loadedImage) {
	 				Animation anim = AnimationUtils.loadAnimation(UploadFile.this, R.anim.fade_in);
	 				person_icon.setAnimation(anim);
	 				anim.start();
	 				
	 				
	 			}
	 		});
	 		
		file_desc = (EditText)findViewById(R.id.file_desc);
		
		Log.e("TAG","parent " + this.getParent());
		if(ga.getImg()==null){
	//	selectImage();
		}else{
			img_display.setImageBitmap(ga.getImg());
		}
		actionmenu();
		
	}
	public void ScreenDimension()
	{
		display = getWindowManager().getDefaultDisplay(); 
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		width = display.getWidth();
		height = display.getHeight();

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
		    		Animation anim = AnimationUtils.loadAnimation(UploadFile.this, R.anim.fade_out);
					settiglayout_food.startAnimation(anim);
					settiglayout_food.setVisibility(View.INVISIBLE);
					menu_invite_food.setVisibility(View.VISIBLE);
					menu_invite_out_food.setVisibility(View.INVISIBLE);
					
					selecteduserid = Integer.toString(position);
					
					CalluserMeTask task = new CalluserMeTask();
					task.applicationContext =UploadFile.this.getParent();
					task.execute();
				}
			
		       });
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v==btn_upload){
			Log.e("TAG","uri is : " + ga.getFileuri());
			if(ga.getFileuri()!=null){
				dialog = new ProgressDialog(this.getParent());
				dialog.setCanceledOnTouchOutside(false);
				dialog.setMessage("Please Wait....");
				dialog.show();
				uploadFile(ga.getFileuri().toString(), "http://api.viamhealth.com/healthfiles/");
			}
		}
		if(v==btn_cancle){
			finish();
		}
		if(v==addphoto){
				selectImage();
		}
		if(v==lbl_invite_user_food){
			Log.e("TAG","Selected value is " + "invite user is clicked");
			Animation anim = AnimationUtils.loadAnimation(UploadFile.this, R.anim.fade_out);
			settiglayout_food.startAnimation(anim);
			settiglayout_food.setVisibility(View.INVISIBLE);
			menu_invite_food.setVisibility(View.VISIBLE);
			menu_invite_out_food.setVisibility(View.INVISIBLE);
			Log.e("TAG","Clicked");
			Intent i = new Intent(UploadFile.this,InviteUser.class);
			startActivity(i);
		}
	
			if(v==menu_invite_food){
			actionmenu();
			settiglayout_food.setVisibility(View.VISIBLE);
			menu_invite_out_food.setVisibility(View.VISIBLE);
			menu_invite_food.setVisibility(View.INVISIBLE);
			Animation anim = AnimationUtils.loadAnimation(UploadFile.this, R.anim.fade_in);
			settiglayout_food.startAnimation(anim);
			
			Log.e("TAG","Clicked");
		}
		if(v==menu_invite_out_food){
			Animation anim = AnimationUtils.loadAnimation(UploadFile.this, R.anim.fade_out);
			settiglayout_food.startAnimation(anim);
			settiglayout_food.setVisibility(View.INVISIBLE);
			menu_invite_food.setVisibility(View.VISIBLE);
			menu_invite_out_food.setVisibility(View.INVISIBLE);
			Log.e("TAG","Clicked");
		}
		
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
	private void selectImage() {
		final CharSequence[] items = { "Take Photo", "Choose from Library",
				"Cancel" };

		AlertDialog.Builder builder = new AlertDialog.Builder(this.getParent());
		builder.setTitle("Add Photo!");
		builder.setItems(items, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int item) {
				if (items[item].equals("Take Photo")) {
					Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
					camera.putExtra("android.intent.extras.CAMERA_FACING", 1);
				    getParent().startActivityForResult(camera, CAMERA_PIC_REQUEST);  
					
				} else if (items[item].equals("Choose from Library")) {
					Intent photoPickerIntent = new Intent(Intent.ACTION_GET_CONTENT);
					photoPickerIntent.setType("image/*");
					getParent().startActivityForResult(photoPickerIntent, 1);
				
				} else if (items[item].equals("Cancel")) {
					dialog.dismiss();
				}
			}
		});
		builder.show();
	}
	
	 public static int uploadFile(String sourceFileUri,String upLoadServerUri) {
         
         
         final String fileName = sourceFileUri;
 
         HttpURLConnection conn = null;
         DataOutputStream dos = null; 
         String lineEnd = "\r\n";
         String twoHyphens = "--";
         String boundary = "*****";
         int bytesRead, bytesAvailable, bufferSize;
         byte[] buffer;
         int maxBufferSize = 1 * 1024 * 1024;
         File sourceFile = new File(sourceFileUri);
             
         if (!sourceFile.isFile()) {
              
              dialog.dismiss();
               
              Log.e("uploadFile", "Source File not exist :"
                                  +fileName);
               
            
               
              return 0;
           
         }
         else
         {   
              try {
                   
                    // open a URL connection to the Servlet
           	      
                  FileInputStream fileInputStream = new FileInputStream(sourceFile);
                  URL url = new URL(upLoadServerUri);
                   
                  // Open a HTTP  connection to  the URL
                  conn = (HttpURLConnection) url.openConnection();
                  conn.setDoInput(true); // Allow Inputs
                  conn.setDoOutput(true); // Allow Outputs
                  conn.setUseCaches(false); // Don't use a Cached Copy
                         
                  conn.setRequestMethod("POST");
                  conn.setRequestProperty("Authorization","Token "+appPrefs.getToken().toString());
                  conn.setRequestProperty("Connection", "Keep-Alive");
                  conn.setRequestProperty("ENCTYPE", "multipart/form-data");
                  conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                  conn.setRequestProperty("file", fileName);
                  conn.setRequestProperty("user", appPrefs.getUserid());    
                  conn.setRequestProperty("description", file_desc.getText().toString());
                  dos = new DataOutputStream(conn.getOutputStream());
         
                  dos.writeBytes(twoHyphens + boundary + lineEnd);
                  dos.writeBytes("Content-Disposition: form-data; name=file;filename="+ fileName + "" + lineEnd);
                   
                  dos.writeBytes(lineEnd);
         
                  // create a buffer of  maximum size
                  bytesAvailable = fileInputStream.available();
         
                  bufferSize = Math.min(bytesAvailable, maxBufferSize);
                  buffer = new byte[bufferSize];
         
                  // read file and write it into form...
                  bytesRead = fileInputStream.read(buffer, 0, bufferSize); 
                     
                  while (bytesRead > 0) {
                       
                    dos.write(buffer, 0, bufferSize);
                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);  
                     
                   }
         
                  // send multipart form data necesssary after file data...
                  dos.writeBytes(lineEnd);
                  dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
         
                  // Responses from the server (code and message)
                  serverResponseCode = conn.getResponseCode();
                  String serverResponseMessage = conn.getResponseMessage();
                    
                  Log.i("uploadFile", "HTTP Response is : "+ serverResponseMessage + ": " + serverResponseCode);
                   dialog.dismiss();
                  if(serverResponseCode == 200){
               	   Log.i("uploadFile", "HTTP Response is : "
                              + serverResponseMessage + ": " + "uploaded");
                       
                    }   
                   
                  //close the streams //
                  fileInputStream.close();
                  dos.flush();
                  dos.close();
                    
             } catch (MalformedURLException ex) {
                  
                 ex.printStackTrace();
                  
              
                  
                 Log.e("Upload file to server", "error: " + ex.getMessage(), ex); 
             } catch (Exception e) {
                  
                 dialog.dismiss(); 
                 e.printStackTrace();
                  
               
                 Log.e("Upload file to server Exception", "Exception : "
                                                  + e.getMessage(), e); 
             }

             return serverResponseCode;
              
          } // End else block
        }
		// async class for calling webservice and get responce message
		public class CalluserMeTask extends AsyncTask <String, Void,String>
		{
			protected Context applicationContext;

			@Override
			protected void onPreExecute()        
			{
				
				//dialog = ProgressDialog.show(applicationContext, "Calling", "Please wait...", true);
				dialog = new ProgressDialog(UploadFile.this.getParent());
				dialog.setMessage("Please Wait....");
				dialog.show();
				Log.i("onPreExecute", "onPreExecute");
				
			}        
			 
			protected void onPostExecute(String result)
			{
				
				Log.i("onPostExecute", "onPostExecute");
				//generateView();
				dialog.dismiss();
			/*	Intent intent = new Intent(Goal.this,MainActivity.class);
				startActivity(intent);*/
			}  
	   
			@Override
			protected String doInBackground(String... params) {
				// TODO Auto-generated method stub
				Log.i("doInBackground--Object", "doInBackground--Object");
                UserEP userEP = new UserEP(UploadFile.this, ga);
				userEP.getUserProfile(ga.getLoggedInUser().getId());
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
				
				
			
	public byte[] BitMapToString(Bitmap bitmap){
	       ByteArrayOutputStream baos=new  ByteArrayOutputStream();
	       bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
	       byte [] b=baos.toByteArray();
	      /* String temp=null;
	       try{
	       System.gc();
	       temp=Base64.encodeToString(b, Base64.DEFAULT);
	       }catch(Exception e){
	           e.printStackTrace();
	       }catch(OutOfMemoryError e){  
	           baos=new  ByteArrayOutputStream();
	           bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
	           b=baos.toByteArray();
	           temp=Base64.encodeToString(b, Base64.DEFAULT);
	           Log.e("EWN", "Out of memory error catched");
	       }*/
	       return b;
	 }
	
	  @Override
	    public void onBackPressed() 
	        {
	          
	         
	        }
	
}
