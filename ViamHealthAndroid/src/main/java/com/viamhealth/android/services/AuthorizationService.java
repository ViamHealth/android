package com.viamhealth.android.services;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.viamhealth.android.Global_Application;
import com.viamhealth.android.auth.ViamhealthAuthenticator;

/**
 * Created by naren on 10/12/13.
 */
public class AuthorizationService extends Service {

    @Override
    public IBinder onBind(Intent intent) {
        ViamhealthAuthenticator auth = new ViamhealthAuthenticator(this, (Global_Application)getApplicationContext());
        Log.i(getClass().getSimpleName(), " uid is " + Binder.getCallingUid());
        return auth.getIBinder();
    }
}
