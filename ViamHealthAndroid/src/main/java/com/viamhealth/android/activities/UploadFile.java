package com.viamhealth.android.activities;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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
import com.viamhealth.android.model.users.User;

import android.database.Cursor;
import android.graphics.Matrix;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
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
import android.util.Base64;
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
import android.widget.Toast;

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
	Button btn_upload;
    TextView btn_cancle;
	static int serverResponseCode = 0;
	String upLoadServerUri = null;  
	String filename=null;
	static EditText file_desc;
	public static ViamHealthPrefs appPrefs;
	LinearLayout addphoto;
	
	Typeface tf;
	private DisplayImageOptions options;

    User user;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.upload_file);
		ga=((Global_Application)getApplicationContext());
		obj = new functionClass(UploadFile.this);
		appPrefs = new ViamHealthPrefs(UploadFile.this);
		ScreenDimension();

        user = (User) getIntent().getParcelableExtra("user");

		tf = Typeface.createFromAsset(this.getAssets(),"Roboto-Condensed.ttf");
		w15=(int)((width*4.68)/100);
		w20=(int)((width*6.25)/100);
		w5=(int)((width*1.7)/100);
		w100=(int)((width*31.25)/100);
		
		
		h40=(int)((height*8.34)/100);
		h20=(int)((height*4.17)/100);
		

		img_display = (ImageView)findViewById(R.id.img_display);
        img_display.setOnClickListener(this);

		btn_upload = (Button)findViewById(R.id.btnUpload);
		btn_upload.setOnClickListener(this);
		
		btn_cancle=(TextView)findViewById(R.id.btnCancle);
		btn_cancle.setOnClickListener(this);
		
		file_desc = (EditText)findViewById(R.id.file_desc);
		
		Log.e("TAG","parent " + UploadFile.this);
		if(ga.getImg()==null){
	//	selectImage();
		}else{
			img_display.setImageBitmap(ga.getImg());
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
		if(v==btn_upload){
			Log.e("TAG","uri is : " + ga.getFileuri());
			//if(ga.getFileuri()!=null){
				//dialog = new ProgressDialog(UploadFile.this);
				//dialog.setCanceledOnTouchOutside(false);
				//dialog.setMessage("Please Wait....");
				//dialog.show();
                Toast.makeText(getApplicationContext()," before calling uploadfile",Toast.LENGTH_LONG).show();
                UploadFiletoServer task1=new UploadFiletoServer();
                task1.execute();
				//uploadDatatoServer(ga.getFileByte(),ga.getFileuri(), "http://api.viamhealth.com/healthfiles/");
			//}
		}
		if(v==btn_cancle){
			finish();
		}
		if(v==img_display){
		    selectImage();
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

		AlertDialog.Builder builder = new AlertDialog.Builder(UploadFile.this);
		builder.setTitle("Add Photo!");
		builder.setItems(items, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int item) {
				if (items[item].equals("Take Photo")) {
					Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
					camera.putExtra("android.intent.extras.CAMERA_FACING", 1);
				    startActivityForResult(camera, CAMERA_PIC_REQUEST);
					
				} else if (items[item].equals("Choose from Library")) {
					Intent photoPickerIntent = new Intent(Intent.ACTION_GET_CONTENT);
					//photoPickerIntent.setType("image/*");
                    //photoPickerIntent.setType("*/*");
                    photoPickerIntent.setType("*/*");
                    //photoPickerIntent.addCategory(Intent.CATEGORY_OPENABLE);
					startActivityForResult(photoPickerIntent, 1);
				
				} else if (items[item].equals("Cancel")) {
					dialog.dismiss();
				}
			}
		});
		builder.show();
	}

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("TAG","On  ac result is called");
        if (requestCode==1)
        {
            if (resultCode == RESULT_OK)
            {

                Uri chosenImageUri = data.getData();
                try
                {
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inSampleSize=4;
                    mBitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(chosenImageUri),null,options);
                    options.inPurgeable = true;
                    System.runFinalization();
                    Runtime.getRuntime().gc();
                    System.gc();

                    String chosenstring=chosenImageUri+"";
                    Log.e("TAG","choosen String : " + chosenstring);
                    if(chosenstring.contains("content://"))
                    {
                        path=getRealPathFromURI(chosenImageUri);
                    }
                    else if (chosenstring.contains("file:///"))
                    {
                        String[] splitval=chosenstring.split("//");
                        path=splitval[1];
                    }
                    ga.setFileuri(path);
                    System.gc();
                    b1=getResizedBitmap(mBitmap,300,300);
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    b1.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                    //img_display.setImageBitmap(b1);
                    ga.setImg(b1);
                    byteArray = stream.toByteArray();
                    Base64str = Base64.encodeToString(byteArray, Base64.NO_WRAP);
                    Log.e("TAG","FROM FILE : "  + Base64str);
									/* if(mBitmap!=null){
									    	mBitmap.recycle();
									    	mBitmap=null;
										}*/

                }
                catch (FileNotFoundException e)
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }


        if (requestCode==CAMERA_PIC_REQUEST)
        {
            if (resultCode == RESULT_OK)
            {
                Log.e("TAG","Path is : " + path);
                Bundle extras = data.getExtras();
                mBitmap=(Bitmap) extras.get("data");

                System.gc();
                b1=getResizedBitmap(mBitmap,300,300);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                b1.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byteArray = stream.toByteArray();
                Base64str = Base64.encodeToString(byteArray, Base64.DEFAULT);
                Log.e("TAG","Camera FILE : "  + Base64str);
                ga.setImg(b1);
                Uri chosenImageUri = data.getData();
                getRealPathFromURI(chosenImageUri);
							 	/*

			    		 		ga.setFileuri(getRealPathFromURI(chosenImageUri));
			    		 		String chosenstring=chosenImageUri+"";
			    		 		if(chosenstring.contains("content://"))
							 	{
							 		path=getRealPathFromURI(chosenImageUri);
				        	 	}
							 	else if (chosenstring.contains("file:///"))
							 	{
							 		String[] splitval=chosenstring.split("//");
							 		path=splitval[1];
							 	}      */
							    /*if(mBitmap!=null){
							    	mBitmap.recycle();
							    	mBitmap=null;
								}  */
							 /*	process_dialog = new ProgressDialog(this.getParent());
								process_dialog.setMessage("Please Wait....");
							    process_dialog.show();
							    new AddPhotoTask().execute();*/

            }
        }

    }
    public Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth)
    {





        int width = bm.getWidth();

        int height = bm.getHeight();


        float srcWidth = bm.getWidth();
        float srcHeight = bm.getHeight();



        if((srcWidth>=newWidth || srcHeight>=newHeight)==false)
        {
            return bm;
        }


        float resizeWidth = srcWidth;
        float resizeHeight = srcHeight;

        float aspect = resizeWidth / resizeHeight;

        if (resizeWidth > newWidth)
        {
            resizeWidth = newWidth;
            resizeHeight= (newWidth * srcHeight)/srcWidth;

            //  resizeHeight = resizeWidth / aspect;
        }
        if (resizeHeight > newHeight)
        {
            //  aspect = resizeWidth / resizeHeight;
            resizeHeight = newHeight;

            resizeWidth=(newHeight * srcWidth)/srcHeight;

            // resizeWidth = resizeHeight * aspect;
        }

        Matrix matrix = new Matrix();

// resize the bit map

        matrix.postScale(resizeWidth / width, resizeHeight / height);

// recreate the new Bitmap

        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);
        return resizedBitmap;


    }
    public String getRealPathFromURI(Uri contentUri)
    {
        Cursor cursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new String[]{MediaStore.Images.Media.DATA, MediaStore.Images.Media.DATE_ADDED, MediaStore.Images.ImageColumns.ORIENTATION}, MediaStore.Images.Media.DATE_ADDED, null, "date_added ASC");
        if(cursor != null && cursor.moveToFirst())
        {
            do {
                Uri uri = Uri.parse(cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA)));
                ga.setFileuri(uri.toString());
            }while(cursor.moveToNext());
            cursor.close();
        }
        return null;
    }


    public String BitMapToString(Bitmap bitmap){
        ByteArrayOutputStream baos=new  ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
        byte [] b=baos.toByteArray();
        String temp=null;
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
        }
        return temp;
    }

    public byte[] BitMapToByteArray(Bitmap bitmap)
    {
        ByteArrayOutputStream baos=new  ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
        byte [] b=baos.toByteArray();
        return b;
    }


    public int uploadDatatoServer(byte []file,String sourceFileUri,String upLoadServerUri)
    {

        String fileName=sourceFileUri;
        HttpURLConnection conn = null;
        DataOutputStream dos = null;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024;

        try {

            // open a URL connection to the Servlet
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
            //conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
            conn.setRequestProperty("Content-Type", "docx;boundary=" + boundary);
            conn.setRequestProperty("file", fileName);
            //conn.setRequestProperty("user", user.getId().toString());
            conn.setRequestProperty("user",ga.getLoggedInUser().getId().toString());
            conn.setRequestProperty("description", "description");
            dos = new DataOutputStream(conn.getOutputStream());
            Log.e("upload MJ","before Uploading file to server");
            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition: form-data; name=file;filename="+ fileName + "" + lineEnd);
            bufferSize=dos.size();

            dos.writeBytes(lineEnd);

            dos.write(file,0,bufferSize);

            dos.writeBytes(lineEnd);
            dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

            // Responses from the server (code and message)
            Log.e("upload MJ","user id="+ga.getLoggedInUser().getId().toString());
            Log.e("upload MJ","before Uploading file to server");
            serverResponseCode = conn.getResponseCode();
            String serverResponseMessage = conn.getResponseMessage();
            Log.e("upload MJ","after Uploading file to server");

            Log.i("uploadFile", "HTTP Response code is : "+ serverResponseMessage + ": " + serverResponseCode);
            Log.i("uploadFile", "HTTP Response is : "+ serverResponseMessage + ": " + serverResponseMessage);
            dialog.dismiss();
            if(serverResponseCode == 200){
                Log.i("uploadFile", "HTTP Response is : "
                        + serverResponseMessage + ": " + "uploaded");

            }

            //close the streams //
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



    }



	 public int uploadFile(String sourceFileUri,String upLoadServerUri) {
         
         
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
                  conn.setRequestProperty("user", user.getId().toString());
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


    public class UploadFiletoServer extends AsyncTask <String, Void,String>
    {
        protected Context applicationContext;

        @Override
        protected void onPreExecute()
        {

            //dialog = ProgressDialog.show(applicationContext, "Calling", "Please wait...", true);
            dialog = new ProgressDialog(UploadFile.this);
            dialog.setMessage("Please Wait....");
            dialog.show();
            Log.i("onPreExecute", "onPreExecute");

        }

        protected void onPostExecute(String result)
        {

            Log.i("onPostExecute", "onPostExecute");
            //generateView();
            dialog.dismiss();
            Toast.makeText(getApplicationContext()," after uploading",Toast.LENGTH_LONG).show();
			/*	Intent intent = new Intent(GoalActivity.this,MainActivity.class);
				startActivity(intent);*/
        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            uploadDatatoServer(ga.getFileByte(),ga.getFileuri(),"http://api.viamhealth.com/healthfiles/");
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
				dialog = new ProgressDialog(UploadFile.this);
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
				
				
			

	  @Override
	    public void onBackPressed() 
	        {
	          
	         
	        }
	
}
