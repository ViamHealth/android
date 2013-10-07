package com.viamhealth.android.activities;

import com.viamhealth.android.Global_Application;
import com.viamhealth.android.dao.db.DataBaseAdapter;
import com.viamhealth.android.dao.rest.endpoints.UserEP;
import com.viamhealth.android.dao.restclient.old.functionClass;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.net.NetworkInfo;

import com.viamhealth.android.R;
import com.viamhealth.android.ViamHealthPrefs;
import com.viamhealth.android.utils.Validator;


public class Register extends BaseActivity implements OnClickListener
{
    private static ProgressDialog dialog;

    Button btnRegister;

    EditText user_name, password, confirm_password;
    TextView txtbtnCancel;

    UserEP obj;

    DataBaseAdapter dbAdapter;
    Typeface tf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.register_new);

        appPrefs = new ViamHealthPrefs(Register.this);

        obj=new UserEP(Register.this, (Global_Application)getApplicationContext());

        tf = Typeface.createFromAsset(this.getAssets(),"Roboto-Condensed.ttf");
        // get screen height and width
        ScreenDimension();

        user_name = (EditText)findViewById(R.id.user_name);
        password = (EditText)findViewById(R.id.user_password);
        confirm_password = (EditText)findViewById(R.id.confirm_user_password);
        password.setTypeface(tf);
        user_name.setTypeface(tf);
        confirm_password.setTypeface(tf);

        btnRegister=(Button)findViewById(R.id.btnRegister);
        btnRegister.setTypeface(tf);
        btnRegister.setOnClickListener(this);

        txtbtnCancel=(TextView)findViewById(R.id.txtbtnCancel);
        txtbtnCancel.setTypeface(tf);
        txtbtnCancel.setOnClickListener(this);
    }

    // onclick method of all clikable control
    @Override
    public void onClick(View v) {

        if(v==txtbtnCancel){
            finish();
        }
        if(v==btnRegister){
            if(validation()){
                if(isInternetOn()){
                    CallSignupTask task = new CallSignupTask();
                    task.applicationContext = Register.this;
                    task.execute();
                }else{
                    Toast.makeText(Register.this,"there is no network around here...",Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    // validation
    public boolean validation(){
        boolean val= true;
        if(user_name.getText().length()==0){
            user_name.setError(getString(R.string.register_user_name_not_present));
            val = false;
        } else if(!Validator.isEmailValid(user_name.getText().toString())){
            user_name.setError(getString(R.string.register_user_name_not_email));
            val = false;
        }
        if(password.getText().length()==0){
            password.setError(getString(R.string.register_user_password_not_present));
            val=false;
        }
        if(confirm_password.getText().length()==0){
            confirm_password.setError(getString(R.string.register_confirm_password_not_present));
            val = false;
        } else if(!password.getText().toString().equals(confirm_password.getText().toString())){
            confirm_password.setError(getString(R.string.register_passwords_mismatch));
            val=false;
        }
        return val;
    }
    // async class for calling webservice and get responce message
    public class CallSignupTask extends AsyncTask <String, Void,String>
    {
        protected Context applicationContext;

        @Override
        protected void onPreExecute()
        {
            //dialog = ProgressDialog.show(applicationContext, "Calling", "Please wait...", true);
            dialog = new ProgressDialog(Register.this);
            dialog.setMessage("we are creating your identity....");
            dialog.show();
            Log.i("onPreExecute", "onPreExecute");
        }

        protected void onPostExecute(String result)
        {
            Log.i("After Sign-Up", result);
            if(result.equals("0")){//just registered
                dialog.dismiss();
                appPrefs.setUsername(user_name.getText().toString().trim());
                Intent i = new Intent(Register.this,Home.class);
                startActivity(i);
                finish();
            }else{
                dialog.dismiss();
                user_name.setError("already registered!");
                //Toast.makeText(Register.this, "User with this user name already exist",Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            Log.i("doInBackground--Object", "doInBackground--Object");

            String result = obj.SignUp(user_name.getText().toString(), password.getText().toString());
            if(result.equals("0")) {
                result = obj.Login(user_name.getText().toString(), password.getText().toString());
            }
            return result;
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

}
