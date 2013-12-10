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
import android.graphics.Bitmap;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
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
import com.viamhealth.android.utils.UIUtility;
import com.viamhealth.android.utils.Validator;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;


public class Login extends BaseFragmentActivity implements OnClickListener, FBLoginFragment.OnSessionStateChangeListener {
	private static ProgressDialog dialog;
	
	Button login_btn;
	
	EditText user_name, user_password;
	TextView sign_up, forgotPassword;
	UserEP userEndPoint;

	DataBaseAdapter dbAdapter;
    FBLoginFragment fbLoginFragment;
    ViamHealthPrefs appPrefs;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);  
		setContentView(R.layout.login_new);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

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

		appPrefs = new ViamHealthPrefs(Login.this);

        dbAdapter=new DataBaseAdapter(getApplicationContext());
        dbAdapter.createDatabase();
        dbAdapter.insertDefaulValues();

        userEndPoint=new UserEP(Login.this, (Global_Application)getApplicationContext());

        Typeface tf = Typeface.createFromAsset(this.getAssets(),"Roboto-Condensed.ttf");

        ScreenDimension();

        user_name = (EditText)findViewById(R.id.user_name);
        user_password = (EditText)findViewById(R.id.user_password);
        user_password.setTypeface(tf);
        user_name.setTypeface(tf);

        login_btn=(Button)findViewById(R.id.login_btn);
        login_btn.setOnClickListener(this);
        login_btn.setTypeface(tf);

        sign_up=(TextView)findViewById(R.id.sign_up);
        sign_up.setOnClickListener(this);
        sign_up.setTypeface(tf);

        forgotPassword = (TextView)findViewById(R.id.forgotPassword);
        forgotPassword.setOnClickListener(this);
        forgotPassword.setTypeface(tf);
	}

    @Override
    public void onSessionStateChange(Session session, SessionState state, Exception exception) {
        if(state.isOpened()){
            //Logged in through facebook
            //String fbToken = session.getAccessToken();
            //Toast.makeText(Login.this, "FB Access Token is - " + fbToken, Toast.LENGTH_LONG).show();
            //getProfileDataFromFB(session);
        }
    }

    private static final int MAX_SIZE = 1024;

    private Drawable createLargeDrawable(int resId) throws IOException {

        InputStream is = getResources().openRawResource(resId);
        BitmapRegionDecoder brd = BitmapRegionDecoder.newInstance(is, true);

        try {
            if (brd.getWidth() <= MAX_SIZE && brd.getHeight() <= MAX_SIZE) {
                return new BitmapDrawable(getResources(), is);
            }

            int rowCount = (int) Math.ceil((float) brd.getHeight() / (float) MAX_SIZE);
            int colCount = (int) Math.ceil((float) brd.getWidth() / (float) MAX_SIZE);

            BitmapDrawable[] drawables = new BitmapDrawable[rowCount * colCount];

            for (int i = 0; i < rowCount; i++) {

                int top = MAX_SIZE * i;
                int bottom = i == rowCount - 1 ? brd.getHeight() : top + MAX_SIZE;

                for (int j = 0; j < colCount; j++) {

                    int left = MAX_SIZE * j;
                    int right = j == colCount - 1 ? brd.getWidth() : left + MAX_SIZE;

                    Bitmap b = brd.decodeRegion(new Rect(left, top, right, bottom), null);
                    BitmapDrawable bd = new BitmapDrawable(getResources(), b);
                    bd.setGravity(Gravity.TOP | Gravity.LEFT);
                    drawables[i * colCount + j] = bd;
                }
            }

            LayerDrawable ld = new LayerDrawable(drawables);
            for (int i = 0; i < rowCount; i++) {
                for (int j = 0; j < colCount; j++) {
                    ld.setLayerInset(i * colCount + j, MAX_SIZE * j, MAX_SIZE * i, 0, 0);
                }
            }

            return ld;
        }
        finally {
            brd.recycle();
            return null;
        }
    }

    // onclick method of all clikable control
	@Override    
	public void onClick(View v) 
	{
		// TODO Auto-generated method stub
		if(v==login_btn){
			if(Checker.isInternetOn(Login.this)){
				if(validation()){
					CallLoginTask task = new CallLoginTask();
					task.applicationContext = Login.this;
                    task.username = user_name.getText().toString();
                    task.password = user_password.getText().toString();
					task.execute();
				}
			}else{
				Toast.makeText(Login.this,R.string.networkNotAvailable,Toast.LENGTH_SHORT).show();
			}
		}
		if(v==sign_up){
			//redirect registration activity
			Intent i = new Intent(Login.this, Register.class);
			startActivity(i);
		}
        if(v==forgotPassword){
            Intent i = new Intent(Login.this, ForgotPassword.class);
            i.putExtra("email", user_name.getText().toString());
            startActivity(i);
        }
	}

	// function for validation
	public boolean validation(){
		boolean valid=true;
		if(user_name.getText().length()==0){
			user_name.setError(getString(R.string.login_user_name_not_present));
			valid=false;
		} else if(!Validator.isEmailValid(user_name.getText().toString())){
            user_name.setError(getString(R.string.login_user_name_not_email));
            valid=false;
        }
		if(user_password.getText().length()==0){
			user_password.setError(getString(R.string.login_user_password_not_present));
			valid=false;
		}

        return valid;
	}

	// async class for calling webservice and get responce message
	public class CallLoginTask extends AsyncTask <String, Void,String>
	{
		protected Context applicationContext;
        protected String username, password;

		@Override
		protected void onPreExecute() {
			//dialog = ProgressDialog.show(applicationContext, "Calling", "Please wait...", true);
			dialog = new ProgressDialog(Login.this, R.style.StyledProgressDialog);
			dialog.setMessage(getString(R.string.loginMessage));
			dialog.show();
		}
		 
		protected void onPostExecute(String response) {
			if(!response.equals("0")){
                dialog.dismiss();
                Toast.makeText(Login.this, R.string.loginFailureMessage, Toast.LENGTH_SHORT).show();
			}else{
                dialog.dismiss();
                //appPrefs.setUsername(user_name.getText().toString().trim());
                Intent i=new Intent(Login.this, Home.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                finish();
			}
		}  

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			userEndPoint.Login(username, password);
            return "0";
		}
		   
	}     
}
