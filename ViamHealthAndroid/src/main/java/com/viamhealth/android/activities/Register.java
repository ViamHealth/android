package com.viamhealth.android.activities;

import com.facebook.FacebookRequestError;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.model.GraphObject;
import com.viamhealth.android.Global_Application;
import com.viamhealth.android.activities.fragments.FBLoginFragment;
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
import com.viamhealth.android.model.users.FBUser;
import com.viamhealth.android.model.users.User;
import com.viamhealth.android.utils.Checker;
import com.viamhealth.android.utils.Validator;

import org.json.JSONObject;


public class Register extends BaseFragmentActivity implements OnClickListener, FBLoginFragment.OnSessionStateChangeListener {
    private static ProgressDialog dialog;

    Button btnRegister;

    EditText user_name, password, confirm_password;
    TextView txtbtnCancel;

    UserEP obj;

    DataBaseAdapter dbAdapter;
    Typeface tf;

    ViamHealthPrefs appPrefs;
    User user;

    FBLoginFragment fbLoginFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.register_new);


        if (savedInstanceState == null) {
            // Add the fragment on initial activity setup
            fbLoginFragment = new FBLoginFragment();
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.fbLoginFragment, fbLoginFragment)
                    .commit();
        } else {
            // Or set the fragment from restored state info
            fbLoginFragment = (FBLoginFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.fbLoginFragment);
        }

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

    @Override
    public void onSessionStateChange(Session session, SessionState state, Exception exception) {
        if(state.isOpened()){
            //SignedUP through facebook
            //String fbToken = session.getAccessToken();
            //Toast.makeText(Register.this, "FB Access Token is - " + fbToken, Toast.LENGTH_LONG).show();
            //getProfileDataFromFB(session);
        }
    }

    // onclick method of all clikable control
    @Override
    public void onClick(View v) {

        if(v==txtbtnCancel){
            finish();
        }
        if(v==btnRegister){
            if(validation()){
                if(Checker.isInternetOn(Register.this)){
                    CallSignupTask task = new CallSignupTask();
                    task.applicationContext = Register.this;
                    task.username = user_name.getText().toString();
                    task.password = password.getText().toString();
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
        protected String username, password;

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
                Intent i = new Intent(Register.this,Home.class);
                i.putExtra("justRegistered", true);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
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

            String result = "1";

            User createdUser = obj.SignUp(username, password);
            if(createdUser!=null) {
                result = obj.Login(username, password);
            }
            return result;
        }
    }
}
