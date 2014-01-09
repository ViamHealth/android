package com.viamhealth.android.activities;

import android.app.LoaderManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.Loader;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.viamhealth.android.Global_Application;
import com.viamhealth.android.R;
import com.viamhealth.android.ViamHealthPrefs;
import com.viamhealth.android.auth.AuthHelper;
import com.viamhealth.android.dao.rest.endpoints.UserEP;
import com.viamhealth.android.model.users.User;
import com.viamhealth.android.provider.ScheduleContract;
import com.viamhealth.android.provider.handlers.ReminderHandler;
import com.viamhealth.android.provider.handlers.UserHandler;
import com.viamhealth.android.sync.SyncHelper;
import com.viamhealth.android.utils.LogUtils;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by naren on 19/12/13.
 *
 * Home Screen is the place where all authentication, initial sync and load the users data
 *
 * The intent for this activity expects the following optional parameters
 * @param boolean logout (optional)
 *
 */
public class SplashActivity extends BaseActivity implements LoaderManager.LoaderCallbacks<Cursor> {


    ViamHealthPrefs appPrefs;
    Global_Application ga;
    UserEP userEndPoint;
    User user;
    AuthHelper mAuthHelper;

    /** Fields expected in intent **/
    public static final String KEY_SHOULD_LOGOUT = "logout";

    /** ALl UI Elements **/
    ProgressBar bar;
    TextView logoutMessage;

    /** extra data **/
    private ArrayList<User> familyMembers = new ArrayList<User>();

    private static final int USERS_LOADER = 100;
    private static boolean hasLoginBeenCalled = false;

    private Date userSyncStartTime = null;
    //private Date userSyncEndTime = null;

    private BroadcastReceiver mBroadCastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            LogUtils.LOGD(TAG, "onBroadCastReceive: " + intent);
            loadFamilyData();
        }
    };

    private void loadFamilyData(){
        LogUtils.LOGD(TAG, "on load family data ");
        Loader<Cursor> loader = getLoaderManager().getLoader(USERS_LOADER);
        if(loader==null)
            getLoaderManager().initLoader(USERS_LOADER, null, SplashActivity.this);
        else
            getLoaderManager().restartLoader(USERS_LOADER, null, SplashActivity.this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        LogUtils.LOGD(TAG, "creating the cursor.. " + id);
        switch (id) {
            case USERS_LOADER:
                return new CursorLoader(SplashActivity.this,
                        ScheduleContract.Users.CONTENT_USER_URI,
                        null,
                        null,
                        null,
                        null);
            default:
                return null;
        }
    }

    private ArrayList<User> getFamilyFromCursor(Cursor cursor) {
        if(cursor!=null && cursor.moveToFirst()){
            familyMembers.clear();
            while(!cursor.isAfterLast()){
                User u = new UserHandler(SplashActivity.this).parseCursor(cursor);
                if(u.isLoggedInUser())
                    ga.setLoggedInUser(u);
                familyMembers.add(u);
                if(!cursor.moveToNext())
                    break;
            }
        }
        return familyMembers;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        LogUtils.LOGD(TAG, "onLoadFinished: " + loader.getId());
        if(cursor!=null){
            switch (loader.getId()){
                case USERS_LOADER:
                    getFamilyFromCursor(cursor);
                    moveToHomeScreen();
                    break;

                default:
                    return;
            }
        }
    }

    private void moveToHomeScreen() {
        if(familyMembers==null || familyMembers.isEmpty()) return;

        LogUtils.LOGD(TAG, "moveToHomeScreen: " + familyMembers);
        Intent homeScreen = new Intent(SplashActivity.this, Home.class);
        homeScreen.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        homeScreen.putParcelableArrayListExtra(Home.KEY_FAMILY_LIST, familyMembers);
        startActivity(homeScreen);
        finish();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        LogUtils.LOGD(TAG, "onLoadReset: " + loader.getId());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setContentView(R.layout.activity_splash);

        appPrefs = new ViamHealthPrefs(SplashActivity.this);
        ga=((Global_Application)getApplicationContext());
        userEndPoint = new UserEP(this, ga);
        //user = ga.getLoggedInUser();
        mAuthHelper = new AuthHelper(this);

        bar = (ProgressBar) findViewById(R.id.progressBar);
        logoutMessage = (TextView) findViewById(R.id.logoutMsg);
        logoutMessage.setVisibility(View.GONE);

        LogUtils.LOGD(TAG, "oncreate " + getIntent().toString());

        if(getIntent().getBooleanExtra(KEY_SHOULD_LOGOUT, false)) {
            logoutMessage.setVisibility(View.VISIBLE);
            logoutMessage.setText(R.string.loggingOutMsg);
            mAuthHelper.logout(llobserver);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    private AuthHelper.LoginLogoutObserver llobserver = new AuthHelper.LoginLogoutObserver() {
        @Override
        public void onLoginComplete(final Bundle bundle) {
            LogUtils.LOGD(TAG, "on LoginComplete ");
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    String errMsg = bundle.getString("errMsg");
                    if (errMsg == null) {
                        postLogin();
                    } else {
                        Toast.makeText(SplashActivity.this, errMsg, Toast.LENGTH_LONG).show();
                    }
                }
            });
        }

        @Override
        public void onLogoutComplete() {
            Intent intent = new Intent(SplashActivity.this, SplashActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }
    };

    private void postLogin(){
        //hasLoginBeenCalled = true;
        logoutMessage.setVisibility(View.VISIBLE);
        logoutMessage.setText(R.string.syncingMessage);
        loadFamilyData();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        LogUtils.LOGD(TAG, "on ActivityResult req:" + requestCode + "; result:" + resultCode + "; data : " + data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onResume() {
        LogUtils.LOGD(TAG, "on Resume ");
        registerReceiver(mBroadCastReceiver, new IntentFilter(SyncHelper.FILTER_USER_SYNC_FINISHED));
        if(!getIntent().getBooleanExtra(KEY_SHOULD_LOGOUT, false) && !hasLoginBeenCalled){//call login as its not been called yet
            userSyncStartTime = new Date();
            mAuthHelper.login(llobserver);
            hasLoginBeenCalled = true;
        }else if(hasLoginBeenCalled){//login has been done the activity is just resuming
            postLogin();
            //check if the data has been synched in between period, if so just proceed further
            LogUtils.LOGD(TAG, "resume>> login happened some time back, checking if sync completed during then");
            UserHandler uh = new UserHandler(SplashActivity.this);
            ReminderHandler rm = new ReminderHandler(SplashActivity.this);
            if(uh.hasSynched(userSyncStartTime, new Date()))
                loadFamilyData();
            else
                userSyncStartTime = new Date();
        }
        super.onResume();
    }

    @Override
    protected void onPause() {
        LogUtils.LOGD(TAG, "on Pause ");
        unregisterReceiver(mBroadCastReceiver);
        super.onPause();
    }
}
