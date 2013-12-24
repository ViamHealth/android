package com.viamhealth.android.sync.restclient;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.app.Application;
import android.content.Context;
import android.net.Uri;

import com.viamhealth.android.Global_Application;
import com.viamhealth.android.ViamHealthPrefs;
import com.viamhealth.android.auth.AccountGeneral;
import com.viamhealth.android.dao.restclient.core.RestClient;
import com.viamhealth.android.utils.LogUtils;

import java.io.IOException;
import java.util.HashMap;

/**
 * Created by naren on 17/12/13.
 */
public abstract class BaseEndPoint {

    protected final String TAG = LogUtils.makeLogTag(getClass());

    protected Context context;
    //protected Global_Application ga;
    //protected ViamHealthPrefs appPrefs;

    protected final Uri BASE_URI = Uri.parse("http://api.viamhealth.com");

    public BaseEndPoint(Context context) {
        this.context = context;
        //this.ga = (Global_Application)context.getApplicationContext();
        //this.appPrefs=new ViamHealthPrefs(context);
    }

    protected RestClient getRestClient(Params params, String... pathSegments) {
        return getAuthenticatedRestClient(params, true, pathSegments);
    }

    protected RestClient getAuthenticatedRestClient(Params params, boolean needAuth, String... pathSegments) {
        Uri.Builder uriBuilder = BASE_URI.buildUpon();

        for(int i=0; i<pathSegments.length; i++)
            uriBuilder = uriBuilder.appendEncodedPath(pathSegments[i]);

        uriBuilder = uriBuilder.appendPath("");

        if(params!=null && !params.isEmpty()){
            for (String key : params.keySet()){
                uriBuilder.appendQueryParameter(key, params.get(key));
            }
        }

        RestClient client = new RestClient(uriBuilder.build().toString());

        if(needAuth){
            AccountManager manager = AccountManager.get(context);
            Account[] accounts = manager.getAccountsByType(AccountGeneral.ACCOUNT_TYPE);
            if(accounts != null && accounts.length>0){
                try {
                    String authToken = manager.blockingGetAuthToken(accounts[0], AccountGeneral.AUTHTOKEN_TYPE_FULL_ACCESS, true);
                    client.AddHeader("Authorization","Token " + authToken);
                } catch (OperationCanceledException e) {
                    e.printStackTrace();
                    LogUtils.LOGE(TAG, e.getMessage(), e.getCause());
                } catch (IOException e) {
                    e.printStackTrace();
                    LogUtils.LOGE(TAG, e.getMessage(), e.getCause());
                } catch (AuthenticatorException e) {
                    e.printStackTrace();
                    LogUtils.LOGE(TAG, e.getMessage(), e.getCause());
                }
            }
        }

        return client;
    }

    protected class Params extends HashMap<String, String> {

    }

    protected interface Paths {
        public static String GET_TOKEN = "api-token-auth";
        public static String USERS = "users";
        public static String LOGGED_IN_USER = "me";
    }
}
