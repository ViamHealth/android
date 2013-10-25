package com.viamhealth.android.activities;

import java.util.Arrays;
import java.util.List;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import com.viamhealth.android.Global_Application;
import com.viamhealth.android.R;
import com.viamhealth.android.ViamHealthPrefs;

public class Downlaod extends Activity {
	public Button btndownload,btncancle;
	private DownloadManager downloadManager;
	private long downloadReference;
	Global_Application ga;
    ViamHealthPrefs appPrefs;
    String user_id;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.downlaod);
		
		ga=((Global_Application)getApplicationContext());
        appPrefs=new ViamHealthPrefs(getApplicationContext());
		user_id=getIntent().getStringExtra("user_id");
        Toast.makeText(getApplicationContext(),"user_id after receiving "+user_id,Toast.LENGTH_LONG).show();
		setview();
	}

	public void setview(){
		btndownload = (Button)findViewById(R.id.btnDownload);
		btncancle = (Button)findViewById(R.id.btnCancle);
		btndownload.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startdownload();
				finish();
			}
		});
		btncancle.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();  
			}
		});
	}       
public void startdownload(){
//	Bundle bundle = getIntent().getExtras();
	Log.e("TAG","Download uri : " + ga.getDownload());
	//List<String> lst = Arrays.asList(ga.getDownload().split("\\s*,\\s*"));
    List<String> lst = Arrays.asList(ga.getDownload().split(","));
	for(int i=0;i<lst.size();i++){
	   Log.e("TAG","url is : " + lst.get(i).toString()+"/?user="+user_id);
	   downloadManager = (DownloadManager)getSystemService(Context.DOWNLOAD_SERVICE);
	   //Uri Download_Uri = Uri.parse("http://api.viamhealth.com/healthfiles/download/69/");
       Uri Download_Uri = Uri.parse(lst.get(i).toString()+"/?user="+user_id);
       Toast.makeText(getApplicationContext(), "Download Uri = "+Download_Uri.toString(), Toast.LENGTH_SHORT).show();
        Toast.makeText(getApplicationContext(), "Toast = "+appPrefs.getToken().toString(), Toast.LENGTH_SHORT).show();

	   DownloadManager.Request request = new DownloadManager.Request(Download_Uri);
       request.addRequestHeader("Authorization","Token "+appPrefs.getToken().toString());

        Log.e("TAG","Authorization token is: " + appPrefs.getToken().toString());
	   //Restrict the types of networks over which this download may proceed.
	   request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
	   //Set whether this download may proceed over a roaming connection.
	   request.setAllowedOverRoaming(false);
	   //Set the title of this download, to be displayed in notifications (if enabled).
	   request.setTitle(lst.get(i).toString());
	   //Set a description of this download, to be displayed in notifications (if enabled)
	   request.setDescription("Downloading....");
	   //Set the local destination for the downloaded file to a path within the application's external files directory
	   request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,"logo" + i + ".png");
	   //Enqueue a new download and same the referenceId

        request.setNotificationVisibility(1);
	   downloadReference = downloadManager.enqueue(request);
	}
	}

}
