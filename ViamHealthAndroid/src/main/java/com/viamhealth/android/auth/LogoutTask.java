package com.viamhealth.android.auth;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.View;

import com.viamhealth.android.sync.restclient.AuthEndPoint;
import com.viamhealth.android.sync.restclient.UserEndPoint;
import com.viamhealth.android.utils.LogUtils;

/**
 * Created by naren on 19/12/13.
 */
public class LogoutTask extends AsyncTask<Void, Void, Boolean> {

    private final AuthEndPoint mAuthEP;
    private final static String TAG = LogUtils.makeLogTag(LogoutTask.class);

    public LogoutTask(Context context, LogoutCompleteListener listener) {
        this.mAuthEP = new AuthEndPoint(context);
        this.mListener = listener;
    }

    @Override
    protected void onPreExecute() {
    }

    public interface LogoutCompleteListener {
        public void onLogoutComplete();
    }

    private LogoutCompleteListener mListener;

    private void finishLogout(){
        if(mListener!=null){
            mListener.onLogoutComplete();
        }
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        finishLogout();
        LogUtils.LOGD(TAG, "loggedout successfully...");
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        return mAuthEP.logout();
    }
}
