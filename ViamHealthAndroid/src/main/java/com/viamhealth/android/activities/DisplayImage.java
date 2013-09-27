package com.viamhealth.android.activities;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import com.viamhealth.android.Global_Application;
import com.viamhealth.android.R;
import com.viamhealth.android.ViamHealthPrefs;

import com.viamhealth.android.dao.restclient.functionClass;

import com.viamhealth.android.model.FileData;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class DisplayImage extends Activity implements OnClickListener{
	private static final int CAMERA_PIC_REQUEST = 1337;
	Bitmap mBitmap,b1;
	byte[] byteArray;
	String Base64str=null; 
	String path;
	ArrayList<FileData> lstResult = new ArrayList<FileData>();
	ImageView img_display;
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
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.display_image);
		ga=((Global_Application)getApplicationContext());
		obj = new functionClass(this.getParent());
		appPrefs = new ViamHealthPrefs(DisplayImage.this);
		img_display = (ImageView)findViewById(R.id.img_display);
		addphoto = (LinearLayout)findViewById(R.id.addphoto);
		addphoto.setOnClickListener(DisplayImage.this);
		btn_upload = (Button)findViewById(R.id.btnUpload);
		btn_upload.setOnClickListener(this);
		
		btn_cancle=(Button)findViewById(R.id.btnCancle);
		btn_cancle.setOnClickListener(this);
		
		file_desc = (EditText)findViewById(R.id.file_desc);
		
		Log.e("TAG","parent " + this.getParent());
		if(ga.getImg()==null){
	//	selectImage();
		}else{
			img_display.setImageBitmap(ga.getImg());
		}
		
		
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
				public class CallFileTask extends AsyncTask <String, Void,String>
				{
					protected Context applicationContext;

					@Override
					protected void onPreExecute()     
					{
						
						//dialog = ProgressDialog.show(applicationContext, "Calling", "Please wait...", true);
						dialog = new ProgressDialog(getParent());
						dialog.setMessage("Please Wait....");
						dialog.show();
						Log.i("onPreExecute", "onPreExecute");
						
					}       
					
					protected void onPostExecute(String result)
					{
						
						Log.i("onPostExecute", "onPostExecute");
							dialog.dismiss();
							if(lstResult.size()>0){
								finish();
								
							}else{
								Toast.makeText(getParent(), "No goals found...",Toast.LENGTH_SHORT).show();
							}
					}  
			   
					@Override
					protected String doInBackground(String... params) {
						// TODO Auto-generated method stub
						Log.i("doInBackground--Object", "doInBackground--Object");
						Log.e("TAG","Image : " + BitMapToString(ga.getImg()).toString());
						//lstResult=obj.UploadFile(BitMapToString(ga.getImg()).toString(), "hello one");
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
