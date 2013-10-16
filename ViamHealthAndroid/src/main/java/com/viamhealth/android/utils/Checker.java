package com.viamhealth.android.utils;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by naren on 16/10/13.
 */
public class Checker {

    public static final boolean isInternetOn(Activity activity) {

        ConnectivityManager connec = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);

        if ((connec.getNetworkInfo(0).getState() == NetworkInfo.State.CONNECTED)
            || (connec.getNetworkInfo(0).getState() == NetworkInfo.State.CONNECTING)
            || (connec.getNetworkInfo(1).getState() == NetworkInfo.State.CONNECTING)
            || (connec.getNetworkInfo(1).getState() == NetworkInfo.State.CONNECTED)) {
                return true;
        } else if ((connec.getNetworkInfo(0).getState() == NetworkInfo.State.DISCONNECTED)
            || (connec.getNetworkInfo(1).getState() ==  NetworkInfo.State.DISCONNECTED)) {
                return false;
        }

        return false;
    }

}
