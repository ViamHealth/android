package com.viamhealth.android.activities;

import android.accounts.Account;
import android.accounts.AccountAuthenticatorActivity;
import android.accounts.AccountAuthenticatorResponse;
import android.accounts.AccountManager;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.Session;
import com.facebook.SessionState;
import com.viamhealth.android.Global_Application;
import com.viamhealth.android.R;
import com.viamhealth.android.ViamHealthPrefs;
import com.viamhealth.android.activities.fragments.FBLoginFragment;
import com.viamhealth.android.auth.AccountGeneral;
import com.viamhealth.android.auth.AuthenticateTask;
import com.viamhealth.android.auth.ViamhealthAuthenticator;
import com.viamhealth.android.dao.db.DataBaseAdapter;
import com.viamhealth.android.dao.rest.endpoints.UserEP;
import com.viamhealth.android.provider.ScheduleContract;
import com.viamhealth.android.sync.SyncHelper;
import com.viamhealth.android.utils.Checker;
import com.viamhealth.android.utils.LogUtils;
import com.viamhealth.android.utils.Validator;

/**
 * Created by naren on 10/12/13.
 */
public class ViamhealthAccountAuthenticatorActivity extends BaseFragmentActivity implements View.OnClickListener, FBLoginFragment.OnSessionStateChangeListener {

    private static ProgressDialog dialog;

    private Button login_btn;

    private EditText user_name, user_password;
    private TextView sign_up, forgotPassword;
    private UserEP userEndPoint;

    private FBLoginFragment fbLoginFragment;
    private ViamHealthPrefs appPrefs;
    private Global_Application mApplication;

    private AccountManager mAccountManager;
    private String mAuthTokenType;

    public final static String ARG_ACCOUNT_TYPE = "ACCOUNT_TYPE";
    public final static String ARG_AUTH_TYPE = "AUTH_TYPE";
    public final static String ARG_ACCOUNT_NAME = "ACCOUNT_NAME";
    public final static String ARG_IS_ADDING_NEW_ACCOUNT = "IS_ADDING_ACCOUNT";

    public static final String KEY_ERROR_MESSAGE = "ERR_MSG";

    public final static String PARAM_USER_PASS = "USER_PASS";

    private final String TAG = LogUtils.makeLogTag(ViamhealthAccountAuthenticatorActivity.class);

    private AccountAuthenticatorResponse mAccountAuthenticatorResponse = null;
    private Bundle mResultBundle = null;

    /**
     * Set the result that is to be sent as the result of the request that caused this
     * Activity to be launched. If result is null or this method is never called then
     * the request will be canceled.
     * @param result this is returned as the result of the AbstractAccountAuthenticator request
     */
    public final void setAccountAuthenticatorResult(Bundle result) {
        mResultBundle = result;
    }

    /**
     * Sends the result or a Constants.ERROR_CODE_CANCELED error if a result isn't present.
     */
    @Override
    public void finish() {
        if (mAccountAuthenticatorResponse != null) {
            // send the result bundle back if set, otherwise send an error.
            if (mResultBundle != null) {
                mAccountAuthenticatorResponse.onResult(mResultBundle);
            } else {
                mAccountAuthenticatorResponse.onError(AccountManager.ERROR_CODE_CANCELED, "canceled");
            }
            mAccountAuthenticatorResponse = null;
        }
        super.finish();
    }

    /**
     * Retreives the AccountAuthenticatorResponse from either the intent of the icicle, if the
     * icicle is non-zero.
     * @param icicle the save instance data of this Activity, may be null
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.login_new);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        mAccountAuthenticatorResponse = getIntent().getParcelableExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE);

        if (mAccountAuthenticatorResponse != null) {
            mAccountAuthenticatorResponse.onRequestContinued();
        }

        mAccountManager = AccountManager.get(getBaseContext());
        String userName = getIntent().getStringExtra(ARG_ACCOUNT_NAME);
        mAuthTokenType = getIntent().getStringExtra(ARG_AUTH_TYPE);
        if (mAuthTokenType == null)
            mAuthTokenType = AccountGeneral.AUTHTOKEN_TYPE_FULL_ACCESS;

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

        appPrefs = new ViamHealthPrefs(ViamhealthAccountAuthenticatorActivity.this);
        mApplication = (Global_Application)getApplicationContext();

        userEndPoint=new UserEP(ViamhealthAccountAuthenticatorActivity.this, (Global_Application)getApplicationContext());

        Typeface tf = Typeface.createFromAsset(this.getAssets(),"Roboto-Condensed.ttf");

        ScreenDimension();

        user_name = (EditText)findViewById(R.id.user_name);
        user_password = (EditText)findViewById(R.id.user_password);
        user_password.setTypeface(tf);
        user_name.setTypeface(tf);
        if(userName!=null && !userName.isEmpty()){
            user_name.setText(userName);
        }

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
        if (state.isOpened()) {
            LogUtils.LOGI(TAG, "FB Logged in...");
            submit(UserEP.LoginType.FB, session.getAccessToken());
        } else if (state.isClosed()) {
            LogUtils.LOGI(TAG, "FB Logged out...");
        }
    }

    private void submit(UserEP.LoginType type, String token) {
        LogUtils.LOGI(getClass().getSimpleName(), " submit line 1 uid is " + Binder.getCallingUid());
        if(Checker.isInternetOn(ViamhealthAccountAuthenticatorActivity.this)){
            AuthenticateTask task =
                    new AuthenticateTask(getBaseContext(), (Global_Application)getApplicationContext(), new AuthenticateTask.AuthenticationCompleteListener() {
                        @Override
                        public void OnAuthenticated(Intent intent) {
                            LogUtils.LOGI(getClass().getSimpleName(), " onAuthenticated line 1 uid is " + Binder.getCallingUid());
                            finishLogin(intent);
                        }
                    });
            if(type== UserEP.LoginType.Email){
                String username = user_name.getText().toString();
                String password = user_password.getText().toString();
                task.execute(username, password, type);
            }else if(type== UserEP.LoginType.FB){
                task.execute(token, null, type);
            }
        }else{
            Toast.makeText(ViamhealthAccountAuthenticatorActivity.this, R.string.networkNotAvailable, Toast.LENGTH_SHORT).show();
        }
    }
    // onclick method of all clikable control
    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        if(v==login_btn){
            if(validation()){
                submit(UserEP.LoginType.Email, null);
            }
        }
        if(v==sign_up){
            //redirect registration activity
            Intent i = new Intent(ViamhealthAccountAuthenticatorActivity.this, Register.class);
            startActivity(i);
        }
        if(v==forgotPassword){
            Intent i = new Intent(ViamhealthAccountAuthenticatorActivity.this, ForgotPassword.class);
            i.putExtra("email", user_name.getText().toString());
            startActivity(i);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
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

    private void finishLogin(Intent intent){
        String accountName = intent.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
        String accountPassword = intent.getStringExtra(PARAM_USER_PASS);
        final Account account = new Account(accountName, intent.getStringExtra(AccountManager.KEY_ACCOUNT_TYPE));

        /** request Sync **/
        ViamhealthAuthenticator.initSync(account, ViamhealthAccountAuthenticatorActivity.this);

        if (getIntent().getBooleanExtra(ARG_IS_ADDING_NEW_ACCOUNT, false)) {
            LogUtils.LOGD(TAG, "> finishLogin > addAccountExplicitly");
            String authtoken = intent.getStringExtra(AccountManager.KEY_AUTHTOKEN);
            String authtokenType = mAuthTokenType;

            // Creating the account on the device and setting the auth token we got
            // (Not setting the auth token will cause another call to the server to authenticate the user)
            Bundle bundle = new Bundle();
            //bundle.putParcelable(AccountManager.KEY_USERDATA, intent.getParcelableExtra(AccountManager.KEY_USERDATA));
            mAccountManager.addAccountExplicitly(account, accountPassword, bundle);
            mAccountManager.setAuthToken(account, authtokenType, authtoken);
            appPrefs.setToken(authtoken);
        } else {
            LogUtils.LOGD(TAG, "> finishLogin > setPassword");
            mAccountManager.setPassword(account, accountPassword);
        }

        LogUtils.LOGD(TAG, "> finishLogin > with result as " + intent.getExtras());
        setAccountAuthenticatorResult(intent.getExtras());
        setResult(RESULT_OK, intent);
        finish();
    }
}
