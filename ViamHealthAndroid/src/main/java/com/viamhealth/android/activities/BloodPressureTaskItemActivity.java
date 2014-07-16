package com.viamhealth.android.activities;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.viamhealth.android.Global_Application;
import com.viamhealth.android.R;
import com.viamhealth.android.ViamHealthPrefs;
import com.viamhealth.android.dao.rest.endpoints.TaskEP;
import com.viamhealth.android.model.tasks.BloodPressureTask;
import com.viamhealth.android.model.users.User;
import com.viamhealth.android.utils.Checker;


/**
 * Created by Kunal on 10/6/14.
 */
public class BloodPressureTaskItemActivity extends BaseFragmentActivity {

    ViamHealthPrefs appPrefs;
    Global_Application ga;


    ActionBar actionBar;

    User user;
    TextView message;
    EditText editsBP, editdBP, editPR;
    Button saveButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.task_list_bp_element_item);
        ga = ((Global_Application) getApplicationContext());

        Intent intent = getIntent();
        final BloodPressureTask item = (BloodPressureTask) intent.getParcelableExtra("taskData");

        message = (TextView) findViewById(R.id.task_message);
        editsBP = (EditText) findViewById(R.id.input_systolic_pressure);
        editdBP = (EditText) findViewById(R.id.input_diastolic_pressure);
        editPR = (EditText) findViewById(R.id.input_pulse_rate);
        saveButton = (Button) findViewById(R.id.btn_save);
        editsBP.requestFocus();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        message.setText(item.getMessage());


        saveButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (isValid()) {
                    if (Checker.isInternetOn(BloodPressureTaskItemActivity.this)) {
                        CallTakBPTask task = new CallTakBPTask();
                        task.execute(item.getId().toString(), editsBP.getText().toString(), editdBP.getText().toString(), editPR.getText().toString());

                    } else {
                        Toast.makeText(BloodPressureTaskItemActivity.this, "Network is not available....", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });

    }

    public boolean isValid() {
        boolean isValid = true;
        if (editsBP.getText().length() == 0) {
            editsBP.setError("Please fill the value for systolic pressure");
            isValid = false;
        }
        if (editdBP.getText().length() == 0) {
            editdBP.setError("Please fill the value for diastolic pressure");
            isValid = false;
        }
        if (editPR.getText().length() == 0) {
            editPR.setError("Please fill the value for pulse rate");
            isValid = false;
        }

        return isValid;
    }


    // async class for calling webservice and get responce message
    public class CallTakBPTask extends AsyncTask<String, Void, String> {
        protected FragmentActivity activity;
        protected ProgressDialog dialog;

        @Override
        protected void onPreExecute() {

            dialog = new ProgressDialog(BloodPressureTaskItemActivity.this);
            dialog.setCanceledOnTouchOutside(false);
            dialog.setMessage("Please Wait....");
            dialog.show();

        }

        protected void onPostExecute(String result) {
            Log.i("onPostExecute", "onPostExecute");
            dialog.dismiss();
            finish();

        }

        @Override
        protected String doInBackground(String... params) {
            String id = params[0];
            String sbp = params[1];
            String dbp = params[2];
            String pr = params[3];
            String u = ga.getLoggedInUser().getId().toString();
            TaskEP tep = new TaskEP(BloodPressureTaskItemActivity.this, ((Global_Application) getApplicationContext()));
            tep.addBPData(id, sbp, dbp, pr, u);
            return null;
        }

    }
}
