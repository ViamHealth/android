package com.viamhealth.android.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.viamhealth.android.auth.ViamhealthAuthenticator;

/**
 * Created by naren on 10/12/13.
 */
public class Authorization extends Service {

    @Override
    public IBinder onBind(Intent intent) {
        ViamhealthAuthenticator auth = new ViamhealthAuthenticator(this);
        return auth.getIBinder();
    }
}
