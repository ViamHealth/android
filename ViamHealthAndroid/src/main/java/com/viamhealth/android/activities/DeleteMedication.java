package com.viamhealth.android.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
<<<<<<< .merge_file_a07116
import android.support.v4.app.FragmentTransaction;
=======
>>>>>>> .merge_file_a09128
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;

<<<<<<< .merge_file_a07116
import com.actionbarsherlock.app.SherlockFragment;
import com.viamhealth.android.R;
import com.viamhealth.android.activities.fragments.MedicineListFragment;
import com.viamhealth.android.activities.fragments.OthersListFragment;
import com.viamhealth.android.dao.restclient.old.functionClass;
=======
import com.viamhealth.android.R;
import com.viamhealth.android.dao.restclient.functionClass;
>>>>>>> .merge_file_a09128

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
<<<<<<< .merge_file_a07116

=======
                finish();
>>>>>>> .merge_file_a09128
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
<<<<<<< .merge_file_a07116
            //dialog = new ProgressDialog(DeleteMedication.this);
            //dialog.setCanceledOnTouchOutside(false);
           // dialog.setMessage("Please Wait....");
            //dialog.show();
=======
            dialog = new ProgressDialog(DeleteMedication.this);
            dialog.setCanceledOnTouchOutside(false);
            dialog.setMessage("Please Wait....");
            dialog.show();
>>>>>>> .merge_file_a09128
            Log.i("onPreExecute", "onPreExecute");

        }

        protected void onPostExecute(String result)
        {

            Log.i("onPostExecute", "onPostExecute");
<<<<<<< .merge_file_a07116
            Intent returnIntent = new Intent();
            returnIntent.putExtra("id", reminderid);
            setResult(RESULT_OK, returnIntent);
            finish();

            //generateView();
            //dialog.dismiss();
			/*	Intent intent = new Intent(GoalActivity.this,MainActivity.class);
=======
            //generateView();
            dialog.dismiss();
			/*	Intent intent = new Intent(Goal.this,MainActivity.class);
>>>>>>> .merge_file_a09128
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
