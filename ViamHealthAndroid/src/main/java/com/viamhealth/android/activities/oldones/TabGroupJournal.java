package com.viamhealth.android.activities.oldones;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;

import android.graphics.Matrix;  
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Window;


public class TabGroupJournal extends TabGroupActivity
{
	 String path;
	 String Base64str=null,base64;
	 byte[] byteArray;  
	 Bitmap mBitmap,b1,rotatedBitmap;
	 Activity activity;  
	 String curentDateandTime;  
	 private static final int CAMERA_PIC_REQUEST = 1337; 
	 private String mpath = Environment.getExternalStorageDirectory() + "/Quote";      
	 @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
	        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	        //startChildActivity("CameraPage", new Intent(this,GoalActivity.class));
	          
	    }
	 
	 @Override
		protected void onActivityResult(int requestCode, int resultCode, Intent data)
		 {
		     super.onActivityResult(requestCode, resultCode, data);	
			    if (requestCode==CAMERA_PIC_REQUEST)
			    {                                
				    if (resultCode == RESULT_OK)   
				    {      
						Uri chosenImageUri = data.getData();   	         
							Bundle extras = data.getExtras();  
							mBitmap=(Bitmap) extras.get("data");  
							 ExifInterface exif;  
							/*try   
							{
								exif = new ExifInterface(chosenImageUri.getPath());
								int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
							 	if(orientation==6)
							 	{
							 		System.gc();   
							 		Matrix matrix = new Matrix();   
							 		matrix.postRotate(90);
							 		rotatedBitmap =Bitmap.createBitmap(mBitmap, 0, 0, mBitmap.getWidth(), mBitmap.getHeight(), matrix,true);
							 	}               
							 	else
							 	{
							 		System.gc(); 
							 		rotatedBitmap=mBitmap; 
							 	}	
							}  
							catch (IOException e) 
							{
								// TODO Auto-generated catch block
								e.printStackTrace();
							} */
							 	  
							rotatedBitmap=mBitmap; 
							 	System.gc(); 
							    /*((ImageGridActivity)activity).imageView1.setImageBitmap(rotatedBitmap);
							 	((ImageGridActivity)activity).imageView1.setAdjustViewBounds(true); */
								
								Boolean isSDPresent = android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
								if(isSDPresent)
								{
								  // yes SD-card is present
									createDirIfNotExists("/Quote");
									
									SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");   
									curentDateandTime = sdf.format(new Date());						     
							        File file = new File(mpath + "/"+curentDateandTime+".jpg");				                
							        FileOutputStream fos;
							        try    
							        {                                    
							        	System.gc(); 
							        	fos = new FileOutputStream(file);	                				          
							        	rotatedBitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);	
							        	fos.flush();
							            fos.close();               
							        }       
							        catch (FileNotFoundException e)          
							        {
							            Log.e("Panel", "FileNotFoundException", e);
							        }        
							        catch (IOException e)                                      
							        {
							            Log.e("Panel", "IOEception", e);    
							        }   
							        
							        
								}
								else
								{
								 // Sorry
								}


						 	
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
	 

	 public static boolean createDirIfNotExists(String path) {
		    boolean ret = true;

		    File file = new File(Environment.getExternalStorageDirectory(), path);
		    if (!file.exists()) {
		        if (!file.mkdirs()) {
		            Log.e("TravellerLog :: ", "Problem creating Image folder");
		            ret = false;
		        }
		    }
		    return ret;  
		}
		@Override
		public void onBackPressed() {
			// TODO Auto-generated method stub
			//super.onBackPressed();
		}
}
   