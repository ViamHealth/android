package com.viamhealth.android.activities;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.actionbarsherlock.view.ActionMode;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.facebook.Session;
import com.facebook.widget.ProfilePictureView;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.viamhealth.android.Global_Application;
import com.viamhealth.android.R;
import com.viamhealth.android.ViamHealthPrefs;


import com.viamhealth.android.dao.rest.endpoints.GCMEP;
import com.viamhealth.android.dao.rest.endpoints.UserEP;

import com.viamhealth.android.manager.ImageSelector;
import com.viamhealth.android.model.users.User;
import com.viamhealth.android.services.ServicesCommon;
import com.viamhealth.android.tasks.InviteUser;
import com.viamhealth.android.tasks.ShareUser;
import com.viamhealth.android.ui.helper.FileLoader;
import com.viamhealth.android.utils.Checker;
import com.viamhealth.android.utils.UIUtility;
import com.viamhealth.android.utils.Validator;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Parcelable;
import android.provider.Settings;
import android.text.InputType;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.analytics.tracking.android.EasyTracker;

public class Home extends BaseActivity implements OnClickListener{
	Display display;
	int width,height;
	
	LinearLayout main_layout, bottom_layout, core_layout;
	List<LinearLayout> tiles = new ArrayList<LinearLayout>();
	List<FrameLayout> frames = new ArrayList<FrameLayout>();

    RelativeLayout splashScreen;
    ScrollView scroller;
    ProgressBar bar;
    TextView logoutMessage;

	ViamHealthPrefs appPrefs;
	Global_Application ga;
	int cnt=0,_count=0, selectedViewPosition = 0;
	int w80,w90,h90,w20,h5,w5,w12,h30;
	ArrayList<String> msgArray = new ArrayList<String>();
	List<User> lstFamily = null;
	ProgressDialog dialog;

	UserEP userEndPoint;
	User user, selectedUser;
    private DisplayImageOptions options;

    boolean justRegistered = false;
    boolean isEditMode = false;

    private ActionMode actionMode;

    private final String TAG = "Home";

    private static final int LOGIN_ACTIVITY = 10000;


    //GCM vars
    //Move sender_id to config
    String SENDER_ID = "603460907161";
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    GoogleCloudMessaging gcm;
    String regid;
    public static final String PROPERTY_REG_ID = "registration_id";
    private static final String PROPERTY_APP_VERSION = "appVersion";
    //GCM Vars end

    @Override
    protected void onStart() {
        super.onStart();
        //EasyTracker.getInstance(this).activityStart(this);  // Add this method.
    }

    @Override
    protected void onStop() {
        super.onStop();
        //EasyTracker.getInstance(this).activityStop(this);  // Add this method.
    }

    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);  
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		
		setContentView(R.layout.home);

        appPrefs = new ViamHealthPrefs(Home.this);
        ga=((Global_Application)getApplicationContext());
        userEndPoint = new UserEP(this, ga);
        user = ga.getLoggedInUser();

        //for generate square
        scroller = (ScrollView)findViewById(R.id.scroller);
        splashScreen = (RelativeLayout) findViewById(R.id.splash);
        bar = (ProgressBar) splashScreen.findViewById(R.id.progressBar);
        logoutMessage = (TextView) splashScreen.findViewById(R.id.logoutMsg);
        logoutMessage.setVisibility(View.GONE);

        scroller.setVisibility(View.GONE);
        splashScreen.setVisibility(View.VISIBLE);

        if(getIntent().getIntExtra(ServicesCommon.NOTIFICATION,0) != 0 ){
            NotificationManager mNM = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
            mNM.cancel(getIntent().getIntExtra(ServicesCommon.NOTIFICATION,0));
            ga.GA_eventGeneral("notification",Integer.toString(getIntent().getIntExtra(ServicesCommon.NOTIFICATION,0)),"LAUNCH");

        }

        if(appPrefs.getToken()==null || appPrefs.getToken().isEmpty()){
            Intent loginIntent = new Intent(Home.this, Login.class);
            startActivity(loginIntent);
        }else{
            if(getIntent().getBooleanExtra("logout", false)) {
                logout();
                return;
            }

            main_layout = (LinearLayout)findViewById(R.id.main_layout);

            justRegistered = getIntent().getBooleanExtra("justRegistered", false);
            lstFamily = getIntent().getParcelableArrayListExtra("family");

            ScreenDimension();

            next();
        }
    }

    private void next() {
        next(false);
    }


    private void postLogin(){

        main_layout = (LinearLayout)findViewById(R.id.main_layout);
        justRegistered = getIntent().getBooleanExtra("justRegistered", false);
        lstFamily = getIntent().getParcelableArrayListExtra("family");
        ScreenDimension();
        next();
    }
    private void next(boolean moveToTabActivity) {
        //if the only logged-in user has not yet created the profile than
        //force for profile creation
        User user = ga.getLoggedInUser();
        boolean getFamilyData = false;
        if(user==null || lstFamily==null || lstFamily.size()==0){
            getFamilyData = true;
        }

        /*if(lstFamily!=null && lstFamily.size()>0){
            generateView();
            splashScreen.setVisibility(View.GONE);
            scroller.setVisibility(View.VISIBLE);
        }*/

        if(!getFamilyData && !user.isProfileCreated()){
            // logged In User's profile not yet completed then show this
            Intent addProfileIntent = new Intent(Home.this, NewProfile.class);
            addProfileIntent.putExtra("user", user);
            startActivityForResult(addProfileIntent, 0);
        } else if(getFamilyData) {//fetch the data
            lstFamily = new ArrayList<User>();
            if(Checker.isInternetOn(Home.this)){
                GetFamilyListTask task = new GetFamilyListTask();
                task.applicationContext = Home.this;
                task.execute();
                gcmRegister();
            }else{
                Toast.makeText(Home.this,R.string.networkNotAvailable,Toast.LENGTH_SHORT).show();
            }
        } else if(moveToTabActivity){//take the user to the goals screen for the loggedInUser\
            //splashScreen.setVisibility(View.GONE);
            //scroller.setVisibility(View.VISIBLE);

            //Monjyoti:commented
            Intent intent = new Intent(Home.this, TabActivity.class);
            //Intent intent = new Intent(Home.this, SelectGoals.class);
            //MJ:condition if first time,goalfragment else tabactivity
            //Intent intent = new Intent(Home.this, GoalFragment.class);
            intent.putExtra("user", user);
            Parcelable[] users = new Parcelable[lstFamily.size()];
            intent.putExtra("users", lstFamily.toArray(users));
            startActivity(intent);
            finish();
        }
    }

    private void logout() {
        splashScreen.setVisibility(View.VISIBLE);
        scroller.setVisibility(View.GONE);

        logoutMessage.setVisibility(View.VISIBLE);

        if(ga.getLoggedInUser().getProfile()!=null && ga.getLoggedInUser().getProfile().getFbProfileId()!=null
                && !ga.getLoggedInUser().getProfile().getFbProfileId().isEmpty())
            callFacebookLogout();

        if(Checker.isInternetOn(Home.this)){
            LogoutTask task = new LogoutTask();
            task.execute();
        }
    }

    public void callFacebookLogout() {
        Session session = Session.getActiveSession();
        if (session != null) {
            if (!session.isClosed()) {
                session.closeAndClearTokenInformation();
            }
        } else {
            session = new Session(Home.this);
            Session.setActiveSession(session);

            session.closeAndClearTokenInformation();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getSupportMenuInflater().inflate(R.menu.home_menu_settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        boolean retVal = super.onOptionsItemSelected(item);
        if(item.getItemId()==R.id.menu_logout){
            logout();
            return false;
        }

        /*if(item.getItemId()==R.id.menu_edit){
            startActionMode(new HomeActionModeCallback());
            return false;
        }*/

        if(item.getItemId() == R.id.menu_invite) {
            InviteUser inviteUser = new InviteUser(Home.this, ga);
            inviteUser.show();
        }

        /*if(item.getItemId() == R.id.menu_refresh) {
            if(Checker.isInternetOn(Home.this)){
                GetFamilyListTask task = new GetFamilyListTask();
                task.applicationContext = Home.this;
                task.execute();
            }else{
                Toast.makeText(Home.this,R.string.networkNotAvailable,Toast.LENGTH_SHORT).show();
            }
        }*/

        if(item.getItemId()==R.id.menu_change_password){
            View dialogView = LayoutInflater.from(Home.this).inflate(R.layout.dialog_change_password, null);
            final EditText old = (EditText) dialogView.findViewById(R.id.old);
            final EditText newP = (EditText) dialogView.findViewById(R.id.newP);
            final EditText newPAgain = (EditText) dialogView.findViewById(R.id.new_again);

            AlertDialog.Builder builder = new AlertDialog.Builder(Home.this, R.style.AlertDialogGreenTheme);
            builder.setTitle(R.string.changePasswordTitle);
            builder.setView(dialogView);
            builder.setPositiveButton(R.string.change, new DialogInterface.OnClickListener() {
                private boolean isValid(){
                    String oStr = old.getText().toString();
                    String nStr = newP.getText().toString();
                    String naStr = newP.getText().toString();

                    if(oStr==null || oStr.isEmpty()){
                        old.setError(getString(R.string.oldPasswordMandatory));
                        return false;
                    }

                    if(nStr==null || nStr.isEmpty()){
                        newP.setError(getString(R.string.newPasswordMandatory));
                        return false;
                    }

                    if(naStr==null || naStr.isEmpty()){
                        newPAgain.setError(getString(R.string.confirmNewPasswordMandatory));
                        return false;
                    }

                    if(!nStr.equals(naStr)){
                        newP.setError(getString(R.string.newPasswordsDoNotMatch));
                        return false;
                    }

                    return true;
                }

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if(Checker.isInternetOn(Home.this)){
                        if(isValid()){
                            ga.GA_eventButtonPress("home_change_password");
                            ChangePasswordTask task = new ChangePasswordTask();
                            task.execute(old.getText().toString(), newP.getText().toString());
                        }
                    }else{
                        Toast.makeText(Home.this, R.string.networkNotAvailable, Toast.LENGTH_LONG).show();
                    }
                }
            });
            builder.show();
            return false;
        }

        return retVal;
    }

    @Override
    protected void onResume() {
        super.onResume();
	}

	public void ScreenDimension(){
        display = getWindowManager().getDefaultDisplay();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        width = display.getWidth();
        height = display.getHeight();
    }


	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
        Log.e("TAG", "id is : " + v.getId());
        int index = v.getId();
        this.selectedViewPosition = index;
        LinearLayout tr1lay=(LinearLayout)tiles.get(index);
        Boolean shouldCreateProfile = index<lstFamily.size() && lstFamily.get(index).isProfileCreated() ?
                                        false : true;

        if(lstFamily.size() > index) {
            selectedUser = lstFamily.get(index);
        }


        if(isEditMode){
            if(actionMode!=null){
                actionMode.setTitle(selectedUser.getName() + " selected");
            }
        }else{
            if(shouldCreateProfile){
                appPrefs.setBtnprofile_hide("1");
                Long userId = null;
                Boolean isLoggedInUser = false;

                Intent addProfileIntent = new Intent(Home.this, NewProfile.class);
                if(lstFamily.size() > index)
                {
                    addProfileIntent.putExtra("user", selectedUser);
                }
                startActivityForResult(addProfileIntent, index);
            }else{
                Intent intent = new Intent(Home.this, TabActivity.class);
                //Intent intent = new Intent(Home.this, SelectGoals.class);
                //MJ:condition if first time then goalfragment,otherwise tabactivity
                //Intent intent = new Intent(Home.this, GoalFragment.class);
                intent.putExtra("user", selectedUser);
                Parcelable[] users = new Parcelable[lstFamily.size()];
                intent.putExtra("users", lstFamily.toArray(users));
                startActivity(intent);
            }
        }
	}


    @Override
    public void onBackPressed() {
        if(actionMode!=null){
            actionMode.finish();
        }

        super.onBackPressed();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        this.selectedViewPosition = requestCode;
        if(requestCode == LOGIN_ACTIVITY){
            if(resultCode == Activity.RESULT_OK){
                postLogin();
            } else{
                finish();
            }
        }
        if(resultCode==RESULT_OK){
            if(requestCode < 100) {
                if(isEditMode && actionMode!=null)
                    actionMode.finish();

                user = (User) data.getParcelableExtra("user");
                if(Checker.isInternetOn(Home.this)){
                    Log.e("HOME", "Code broken due to move of profile list from home. In onActivityResult");
                    /*CallAddProfileTask task = new CallAddProfileTask();
                    task.applicationContext = Home.this;
                    task.execute();*/

                }else{

                }
            }else{//it is from tabactivity

            }
        }
    }


    // async class for calling webservice and get responce message
    public class GetFamilyListTask extends AsyncTask <String, Void,String>
    {
        protected Context applicationContext;

        @Override
        protected void onPreExecute() {
            //dialog = ProgressDialog.show(applicationContext, "Calling", "Please wait...", true);
            dialog = new ProgressDialog(Home.this, R.style.StyledProgressDialog);
            dialog.setCanceledOnTouchOutside(false);
            dialog.setMessage("loading your family");
            //dialog.show();
            Log.i("onPreExecute", "onPreExecute");

        }

        protected void onPostExecute(String result) {
            if(lstFamily.isEmpty()){
                logout();
                return;
            }
            //splashScreen.setVisibility(View.GONE);
            //scroller.setVisibility(View.VISIBLE);

            next(true);
            //dialog.dismiss();
        }

        @Override
        protected String doInBackground(String... params) {
            lstFamily.clear();
            if(ga.getLoggedInUser()==null){
                userEndPoint.getLoggedInUser();
            }
            lstFamily.addAll(userEndPoint.GetFamilyMembers());
            return null;
        }

    }

    public class LogoutTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            splashScreen.setVisibility(View.VISIBLE);
            scroller.setVisibility(View.GONE);
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            ga.setLoggedInUser(null);
            appPrefs.setToken(null);
            Intent i = new Intent(Home.this, Login.class);
            startActivity(i);
            finish();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            return userEndPoint.Logout();
        }
    }


    public class ChangePasswordTask extends AsyncTask<String, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(Home.this, R.style.StyledProgressDialog);
            dialog.setCanceledOnTouchOutside(false);
            dialog.setMessage("changing your password...");
            dialog.show();
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            dialog.dismiss();
            String msg;
            if(aBoolean){
                msg = "Changed your password successfully.";
            }else{
                msg = "Sorry! Couldn't change your password. Please try after some time.";
            }
            Toast.makeText(Home.this, msg, Toast.LENGTH_LONG).show();
        }

        @Override
        protected Boolean doInBackground(String... params) {
            return userEndPoint.ChangePassword(params[0], params[1]);
        }
    }

    public class ImproperArgumentsPassedException extends Exception {
        public ImproperArgumentsPassedException(String detailMessage) {
            super(detailMessage);
        }
    }



    // GCM Starts
    private void gcmRegister(){
        // Check device for Play Services APK. If check succeeds, proceed with GCM registration.
        if (checkPlayServices()) {
            gcm = GoogleCloudMessaging.getInstance(this);
            regid = getRegistrationId(getApplicationContext());

            //if (regid.isEmpty()) {
                registerInBackground();
            //}
        } else {
            Log.i("GCM", "No valid Google Play Services APK found.");
        }
    }

    /**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */
    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i("GCM", "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }

    /**
     * Gets the current registration ID for application on GCM service, if there is one.
     * <p>
     * If result is empty, the app needs to register.
     *
     * @return registration ID, or empty string if there is no existing
     *         registration ID.
     */
    private String getRegistrationId(Context context) {
        final SharedPreferences prefs = getGcmPreferences(context);
        String registrationId = prefs.getString(PROPERTY_REG_ID, "");
        if (registrationId.isEmpty()) {
            Log.i("GCM", "Registration not found.");
            return "";
        }
        // Check if app was updated; if so, it must clear the registration ID
        // since the existing regID is not guaranteed to work with the new
        // app version.
        int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION, Integer.MIN_VALUE);
        int currentVersion = getAppVersion(context);
        if (registeredVersion != currentVersion) {
            Log.i("GCM", "App version changed.");
            return "";
        }
        return registrationId;
    }

    /**
     * @return Application's version code from the {@code PackageManager}.
     */
    private static int getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            // should never happen
            throw new RuntimeException("Could not get package name: " + e);
        }
    }

    /**
     * @return Application's {@code SharedPreferences}.
     */
    private SharedPreferences getGcmPreferences(Context context) {
        // This sample app persists the registration ID in shared preferences, but
        // how you store the regID in your app is up to you.
        return getSharedPreferences(Home.class.getSimpleName(),
                Context.MODE_PRIVATE);
    }

    /**
     * Stores the registration ID and the app versionCode in the application's
     * {@code SharedPreferences}.
     *
     * @param context application's context.
     * @param regId registration ID
     */
    private void storeRegistrationId(Context context, String regId) {
        final SharedPreferences prefs = getGcmPreferences(context);
        int appVersion = getAppVersion(context);
        Log.i("GCM", "Saving regId on app version " + appVersion);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PROPERTY_REG_ID, regId);
        editor.putInt(PROPERTY_APP_VERSION, appVersion);
        editor.commit();
    }

    /**
     * Registers the application with GCM servers asynchronously.
     * <p>
     * Stores the registration ID and the app versionCode in the application's
     * shared preferences.
     */
    private void registerInBackground() {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String msg = "";
                try {
                    if (gcm == null) {
                        gcm = GoogleCloudMessaging.getInstance(getApplicationContext());
                    }
                    regid = gcm.register(SENDER_ID);
                    msg = "Device registered, registration ID=" + regid;
                    storeRegistrationId(getApplicationContext(), regid);

                    String deviceID = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);

                    GCMEP gcmep = new GCMEP(getApplicationContext(),ga);
                    gcmep.register(deviceID, regid);

                } catch (IOException ex) {
                    msg = "Error :" + ex.getMessage();
                }
                return msg;
            }

            @Override
            protected void onPostExecute(String msg) {
                //mDisplay.append(msg + "\n");
            }
        }.execute(null, null, null);
    }

    // GCM ENDS

}
