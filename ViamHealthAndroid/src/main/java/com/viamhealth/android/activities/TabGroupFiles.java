package com.viamhealth.android.activities;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.Media;
import android.util.Base64;
import android.util.Log;
import android.view.Window;

import com.viamhealth.android.Global_Application;

public class TabGroupFiles extends TabGroupActivity {
	private static final int CAMERA_PIC_REQUEST = 1337;
	Bitmap mBitmap,b1;
	byte[] byteArray;
	String Base64str=null; 
	String path;
	Global_Application ga;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
	    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	    ga=((Global_Application)getApplicationContext());
	    startChildActivity("FilePage", new Intent(this,File.class));
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
									 startChildActivity("FilePage", new Intent(this,UploadFile.class));
								 	/*process_dialog = new ProgressDialog(this.getParent());
									process_dialog.setMessage("Please Wait....");
								    process_dialog.show();
								    new AddPhotoTask().execute();*/
							 

		             
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
							 	Log.e("TAG","chosenImageUri : " + ga.getPath());
							 	 startChildActivity("FilePage", new Intent(this,UploadFile.class));
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
		Cursor cursor = getContentResolver().query(Media.EXTERNAL_CONTENT_URI, new String[]{Media.DATA, Media.DATE_ADDED, MediaStore.Images.ImageColumns.ORIENTATION}, Media.DATE_ADDED, null, "date_added ASC");
		if(cursor != null && cursor.moveToFirst())
		{
		    do {
		       Uri uri = Uri.parse(cursor.getString(cursor.getColumnIndex(Media.DATA)));
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
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		//super.onBackPressed();
	}
}
