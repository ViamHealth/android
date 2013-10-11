package com.viamhealth.android.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.viamhealth.android.R;
import com.viamhealth.android.dao.restclient.old.functionClass;

/**
 * Created by Administrator on 10/3/13.
 */
public class DeleteMedication extends BaseActivity {

    ProgressDialog dialog;
    functionClass obj;
    Intent getIntent;
    String reminderid="";

    public void onCreate(Bundle savedInstanceState)
    {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        getIntent=getIntent();

        setContentView(R.layout.delete_medication);
        obj=new functionClass(DeleteMedication.this);
        reminderid=getIntent.getStringExtra("id");

        Button button_yes = (Button) findViewById(R.id.btn_yes);
        Button button_no = (Button) findViewById(R.id.btn_no);
        button_yes.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                DeleteReminder d1= new DeleteReminder();
                d1.applicationContext=DeleteMedication.this;
                d1.execute();
                finish();
            }
        });

        button_no.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });


    }


    public class DeleteReminder extends AsyncTask<String, Void,String>
    {
        protected Context applicationContext;

        @Override
        protected void onPreExecute()
        {

            //dialog = ProgressDialog.show(applicationContext, "Calling", "Please wait...", true);
            dialog = new ProgressDialog(DeleteMedication.this);
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
            String responseString=null;
            responseString=obj.DeleteMedication("http://api.viamhealth.com/reminders/"+reminderid+"/"+"?user="+getIntent.getStringExtra("user_id"));
            return null;
        }

    }
}
