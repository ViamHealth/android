package com.viamhealth.android.activities;

import android.app.ProgressDialog;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.viamhealth.android.Global_Application;
import com.viamhealth.android.R;
import com.viamhealth.android.dao.rest.endpoints.UserEP;
import com.viamhealth.android.utils.Checker;
import com.viamhealth.android.utils.Validator;

/**
 * Created by naren on 27/11/13.
 */
public class ForgotPassword extends BaseFragmentActivity {

    EditText etEmail;
    Button btnMailMe;
    Global_Application ga;
    UserEP userEndPoint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.forgot_password);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        ga=((Global_Application)getApplicationContext());
        userEndPoint=new UserEP(ForgotPassword.this, (Global_Application)getApplicationContext());

        Typeface tf = Typeface.createFromAsset(this.getAssets(),"Roboto-Condensed.ttf");

        String strEmail = getIntent().getStringExtra("email");

        etEmail = (EditText) findViewById(R.id.user_email);
        etEmail.setText(strEmail);
        etEmail.setTypeface(tf);

        btnMailMe = (Button) findViewById(R.id.btn_mail_me);
        btnMailMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Checker.isInternetOn(ForgotPassword.this)){
                    ga.GA_eventButtonPress("forgot_password_mail_me");
                    if(isValid()){
                        ForgotPasswordTask task = new ForgotPasswordTask();
                        task.execute(etEmail.getText().toString());
                    }
                }else{
                    Toast.makeText(ForgotPassword.this, R.string.networkNotAvailable, Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private boolean isValid(){
        String email = etEmail.getText().toString();
        if(email==null || email.isEmpty()){
            etEmail.setError(getString(R.string.email_registered_with_us_is_mandatory));
            return false;
        }

        if(!Validator.isEmailValid(email)){
            etEmail.setError(getString(R.string.login_user_name_not_email));
            return false;
        }

        return true;
    }

    public class ForgotPasswordTask extends AsyncTask<String, Void, Boolean> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(ForgotPassword.this, R.style.StyledProgressDialog);
            dialog.setMessage(getString(R.string.forgotPasswordTaskMessage));
            dialog.show();
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            dialog.dismiss();
            int msgResId;
            if(aBoolean){
                msgResId = R.string.forgotPasswordTaskSuccess;
            }else{
                msgResId = R.string.forgotPasswordTaskFailure;
            }
            Toast.makeText(ForgotPassword.this, msgResId, Toast.LENGTH_LONG).show();
        }

        @Override
        protected Boolean doInBackground(String... params) {
            String email = params[0];
            return userEndPoint.ForgotPassword(email);
        }
    }


}
