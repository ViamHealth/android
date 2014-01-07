package com.viamhealth.android.auth;

import android.accounts.AccountManager;
import android.app.Application;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Toast;

import com.viamhealth.android.Global_Application;
import com.viamhealth.android.R;
import com.viamhealth.android.ViamHealthPrefs;
import com.viamhealth.android.activities.Home;
import com.viamhealth.android.activities.ViamhealthAccountAuthenticatorActivity;
import com.viamhealth.android.dao.rest.endpoints.UserEP;
import com.viamhealth.android.model.users.User;

/**
 * Created by naren on 10/12/13.
 */
public class AuthenticateTask extends AsyncTask<Object, Void, Intent> {

    Context mContext;
    ProgressDialog mDialog;
    UserEP mUserEP;
    Global_Application mApp;
    ViamHealthPrefs mPrefs;
    AuthenticationCompleteListener mListener;

    public interface AuthenticationCompleteListener {
        public void OnAuthenticated(Intent intent);
    }

    private void finishLogin(Intent intent){
        if(mListener!=null){
            mListener.OnAuthenticated(intent);
        }
    }

    public AuthenticateTask(Context context, Application app, AuthenticationCompleteListener listener) {
        mContext = context;
        mDialog = new ProgressDialog(context, R.style.StyledProgressDialog);
        mUserEP = new UserEP(context, app);
        mApp = (Global_Application)app;
        mPrefs = new ViamHealthPrefs(context);
        mListener = listener;
    }

    @Override
    protected void onPreExecute() {
        //mDialog.show();
    }

    @Override
    protected void onPostExecute(Intent intent) {
        //mDialog.dismiss();
        if (intent.hasExtra(ViamhealthAccountAuthenticatorActivity.KEY_ERROR_MESSAGE)) {
            Toast.makeText(mContext, intent.getStringExtra(ViamhealthAccountAuthenticatorActivity.KEY_ERROR_MESSAGE), Toast.LENGTH_SHORT).show();
        } else {
            User user = (User) intent.getParcelableExtra(AccountManager.KEY_USERDATA);
            mApp.setLoggedInUser(user);
            finishLogin(intent);
        }

    }

    @Override
    protected Intent doInBackground(Object... params) {
        String authtoken = null;
        Bundle data = new Bundle();
        String username = (String) params[0];
        String password = (String) params[1];
        UserEP.LoginType type = (UserEP.LoginType) params[2];

        try {
        /* login */
            String token = mUserEP.login(username, password, type);

        /* set the token in the prefs so that it can be globally accessed */
            mPrefs.setToken(token);

            User user = mUserEP.getLoggedInUser();

            if(type== UserEP.LoginType.Email || type== UserEP.LoginType.FB)
                data.putString(AccountManager.KEY_ACCOUNT_NAME, user.getEmail());
            else if(type == UserEP.LoginType.Mobile)
                data.putString(AccountManager.KEY_ACCOUNT_NAME, user.getMobile());

            data.putString(AccountManager.KEY_AUTHTOKEN, token);
            data.putString(ViamhealthAccountAuthenticatorActivity.PARAM_USER_PASS, password);
            data.putString(AccountManager.KEY_ACCOUNT_TYPE, AccountGeneral.ACCOUNT_TYPE);

            data.putParcelable(AccountManager.KEY_USERDATA, user);
        } catch (Exception e) {
            e.printStackTrace();
            data.putString(ViamhealthAccountAuthenticatorActivity.KEY_ERROR_MESSAGE, e.getMessage());
        }

        final Intent res = new Intent(mContext, Home.class);
        //data.putParcelable(AccountManager.KEY_INTENT, res);
        res.putExtras(data);
        return res;
    }
}