package com.viamhealth.android.activities.fragments;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.facebook.FacebookRequestError;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphObject;
import com.facebook.widget.LoginButton;
import com.viamhealth.android.Global_Application;
import com.viamhealth.android.R;
import com.viamhealth.android.ViamHealthPrefs;
import com.viamhealth.android.activities.Home;
import com.viamhealth.android.activities.Register;
import com.viamhealth.android.dao.rest.endpoints.UserEP;
import com.viamhealth.android.model.users.FBUser;
import com.viamhealth.android.model.users.User;
import com.viamhealth.android.utils.Checker;

import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

/**
 * Created by naren on 05/10/13.
 */
public class FBLoginFragment extends BaseFragment {

    private static final String TAG = "FBLoginFragment";

    private UiLifecycleHelper uiHelper;
    private OnSessionStateChangeListener scListener;

    private ViamHealthPrefs appPrefs;

    boolean isPaused = false;

    UserEP userEndPoint;

    public static List<String> fbPermissions = Arrays.asList("user_birthday", "user_hometown", "user_location", "email",
            "user_relationships", "user_friends", "user_work_history",
            "friends_about_me", "friends_birthday", "friends_hometown", "friends_location",
            "friends_work_history");

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fb_login_fragment, container, false);

        appPrefs = new ViamHealthPrefs(getSherlockActivity());
        LoginButton login = (LoginButton) view.findViewById(R.id.authButton);
        login.setFragment(this);
        login.setReadPermissions(fbPermissions);

        userEndPoint = new UserEP(getActivity(), (Global_Application)getActivity().getApplicationContext());

        return view;
    }

    private void onSessionStateChange(Session session, SessionState state, Exception exception) {
        scListener.onSessionStateChange(session, state, exception);
    }

    private Session.StatusCallback callback = new Session.StatusCallback() {
        @Override
        public void call(Session session, SessionState state, Exception exception) {
            onSessionStateChange(session, state, exception);
        }
    };

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try{
            scListener = (OnSessionStateChangeListener) activity;
        }catch(ClassCastException e){
            throw new ClassCastException(activity.toString() + " must implement OnSessionStateChangeListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        uiHelper = new UiLifecycleHelper(getActivity(), callback);
        uiHelper.onCreate(savedInstanceState);
        isPaused = false;
    }

    @Override
    public void onResume() {
        super.onResume();

        // For scenarios where the main activity is launched and user
        // session is not null, the session state change notification
        // may not be triggered. Trigger it if it's open/closed.
        Session session = Session.getActiveSession();
        if (session != null &&
                (session.isOpened() || session.isClosed()) ) {
            //if(isPaused)
                //onSessionStateChange(session, session.getState(), null);
        }

        uiHelper.onResume();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        uiHelper.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onPause() {
        super.onPause();
        isPaused = true;
        uiHelper.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        uiHelper.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        uiHelper.onSaveInstanceState(outState);
    }

    public interface OnSessionStateChangeListener {
        public void onSessionStateChange(Session session, SessionState state, Exception exception);
    }

//    public class FBAuthenticateTask extends AsyncTask<Void, Void, Boolean> {
//        protected Context applicationContext;
//        protected String fbToken;
//        protected User user;
//        protected ProgressDialog dialog;
//
//        @Override
//        protected void onPreExecute() {
//            //dialog = ProgressDialog.show(applicationContext, "Calling", "Please wait...", true);
//            dialog = new ProgressDialog(getActivity(), R.style.StyledProgressDialog);
//            dialog.setMessage(getString(R.string.loginMessage));
//            dialog.show();
//        }
//
//        protected void onPostExecute(Boolean hasFailed) {
//            if(hasFailed){
//                dialog.dismiss();
//                Toast.makeText(getActivity(), R.string.loginFailureMessage, Toast.LENGTH_SHORT).show();
//            }else{
//                dialog.dismiss();
//                Intent i = new Intent(getActivity(), Home.class);
//                i.putExtra("justRegistered", true);
//                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(i);
//                getActivity().finish();
//            }
//        }
//
//        @Override
//        protected Boolean doInBackground(Void... params) {
//            // TODO Auto-generated method stub
//            Boolean hasFailed = true;
//            user = userEndPoint.AuthenticateThroughFB(fbToken);
//            if(user!=null)
//                hasFailed = false;
//
//            return hasFailed;
//        }
//
//    }

}
