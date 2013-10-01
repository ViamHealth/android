package com.viamhealth.android.activities;

import com.viamhealth.android.dao.db.DataBaseAdapter;
import com.viamhealth.android.dao.restclient.functionClass;
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



public class Register extends Activity implements OnClickListener
{
    private static ProgressDialog dialog;

    Button btnRegister;
    Display display;
    int width,height;
    int w10,h10,h15;
    int h40,h20,h30,pt7;

    TableLayout title_table;
    TableRow password_row, confirm_password_row, register_btn_row, cancel_btn_row;
    LinearLayout login_layout;
    EditText user_name, password, confirm_password;
    TextView txtbtnCancel;
    functionClass obj;
    ViamHealthPrefs appPrefs;
    DataBaseAdapter dbAdapter;
    Typeface tf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_register);

        appPrefs = new ViamHealthPrefs(Register.this);
        obj=new functionClass(Register.this);

        tf = Typeface.createFromAsset(this.getAssets(),"Roboto-Condensed.ttf");
        // get screen height and width
        ScreenDimension();

        //calculate dynamic padding
        w10=(int)((width*3.13)/100);
        h10=(int)((height*2.09)/100);
        h40=(int)((height*8.34)/100);
        h15=(int)((height*3.13)/100);
        h20=(int)((height*4.17)/100);
        h30=(int)((height*6.25)/100);
        pt7=(int)((height*2.19)/100);

        //casting control and manage padding and call onclick method
        title_table=(TableLayout)findViewById(R.id.title_table);
        password_row=(TableRow)findViewById(R.id.password_row);
        confirm_password_row=(TableRow)findViewById(R.id.confirm_password_row);
        register_btn_row=(TableRow)findViewById(R.id.register_btn_row);
        cancel_btn_row=(TableRow)findViewById(R.id.register_cancel_btn_row);
        user_name = (EditText)findViewById(R.id.user_name);
        password = (EditText)findViewById(R.id.user_password);
        confirm_password = (EditText)findViewById(R.id.confirm_user_password);
        login_layout = (LinearLayout)findViewById(R.id.loginwrapper);
        password.setTypeface(tf);
        user_name.setTypeface(tf);
        confirm_password.setTypeface(tf);

        title_table.setPadding(0, 0, 0, h40);
        password_row.setPadding(0, h15, 0, 0);
        confirm_password_row.setPadding(0, h15, 0, 0);
        register_btn_row.setPadding(0, h30, 0, 0);
        cancel_btn_row.setPadding(0, h20, 0, 0);
        login_layout.setPadding(0, pt7, 0, 0);

        btnRegister=(Button)findViewById(R.id.btnRegister);
        btnRegister.setTypeface(tf);
        btnRegister.setOnClickListener(this);

        txtbtnCancel=(TextView)findViewById(R.id.txtbtnCancel);
        txtbtnCancel.setTypeface(tf);
        txtbtnCancel.setOnClickListener(this);
    }

    // for get screen diamention
    public void ScreenDimension()
    {
        display = getWindowManager().getDefaultDisplay();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        width = display.getWidth();
        height = display.getHeight();
        appPrefs.setSwidth(String.valueOf(width));
        appPrefs.setSheight(String.valueOf(height));
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
                    Toast.makeText(Register.this,"Network is not available....",Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    // validation
    public boolean validation(){
        boolean val= true;
        if(user_name.getText().length()==0){
            user_name.setError("Enter email id");
            val = false;
        }else if(user_name.getText().toString().matches("^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$")==false){
            user_name.setError("Invalid email");
            val = false;
        }
        if(password.getText().length()==0){
            password.setError("Enter password");
            val=false;
        }
        if(confirm_password.getText().toString().length()==0){
            confirm_password.setError("Enter confirm password");
            val = false;
        }
        if(!password.getText().toString().equals(confirm_password.getText().toString())){
            confirm_password.setError("Password Mismatch");
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
            dialog.setMessage("Please Wait....");
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
                user_name.setError("User with this user name already exist");
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
