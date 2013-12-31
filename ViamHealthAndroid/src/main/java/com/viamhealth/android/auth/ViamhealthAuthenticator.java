package com.viamhealth.android.auth;

import android.accounts.AbstractAccountAuthenticator;
import android.accounts.Account;
import android.accounts.AccountAuthenticatorResponse;
import android.accounts.AccountManager;
import android.accounts.NetworkErrorException;
import android.app.Application;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SyncResult;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.viamhealth.android.Global_Application;
import com.viamhealth.android.ViamHealthPrefs;
import com.viamhealth.android.activities.ViamhealthAccountAuthenticatorActivity;
import com.viamhealth.android.dao.rest.endpoints.UserEP;
import com.viamhealth.android.provider.ScheduleContract;
import com.viamhealth.android.sync.SyncHelper;
import com.viamhealth.android.utils.LogUtils;

import java.io.IOException;

/**
 * Created by naren on 10/12/13.
 */
public class ViamhealthAuthenticator extends AbstractAccountAuthenticator {

    private Context mContext;
    private ViamHealthPrefs mPrefs;
    private Global_Application mApplication;

    private final String TAG = LogUtils.makeLogTag(ViamhealthAuthenticator.class);

    public ViamhealthAuthenticator(Context context, Application app) {
        super(context);

        mContext = context;
        mPrefs = new ViamHealthPrefs(context);
        mApplication = (Global_Application)app;
    }

    @Override
    public Bundle editProperties(AccountAuthenticatorResponse response, String accountType) {
        return null;
    }

    @Override
    public Bundle addAccount(AccountAuthenticatorResponse response, String accountType, String authTokenType, String[] requiredFeatures, Bundle options) throws NetworkErrorException {
        final Intent intent = new Intent(mContext, ViamhealthAccountAuthenticatorActivity.class);
        intent.putExtra(ViamhealthAccountAuthenticatorActivity.ARG_ACCOUNT_TYPE, accountType);
        intent.putExtra(ViamhealthAccountAuthenticatorActivity.ARG_AUTH_TYPE, authTokenType);
        intent.putExtra(ViamhealthAccountAuthenticatorActivity.ARG_IS_ADDING_NEW_ACCOUNT, true);
        intent.putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE, response);

        final Bundle bundle = new Bundle();
        bundle.putParcelable(AccountManager.KEY_INTENT, intent);
        return bundle;
    }

    @Override
    public Bundle confirmCredentials(AccountAuthenticatorResponse response, Account account, Bundle options) throws NetworkErrorException {
        return null;
    }

    public static void initSync(Account account, Context context) {
        ContentResolver.setIsSyncable(account, ScheduleContract.CONTENT_AUTHORITY, 1);
        ContentResolver.setSyncAutomatically(account, ScheduleContract.CONTENT_AUTHORITY, true);
        SyncHelper.requestSync(account);
    }

    @Override
    public Bundle getAuthToken(AccountAuthenticatorResponse response, Account account, String authTokenType, Bundle options) throws NetworkErrorException {
        Log.d(TAG, "> getAuthToken");

        // If the caller requested an authToken type we don't support, then
        // return an error
        if (!authTokenType.equals(AccountGeneral.AUTHTOKEN_TYPE_READ_ONLY) && !authTokenType.equals(AccountGeneral.AUTHTOKEN_TYPE_FULL_ACCESS)) {
            final Bundle result = new Bundle();
            result.putString(AccountManager.KEY_ERROR_MESSAGE, "invalid authTokenType");
            return result;
        }

        // Extract the username and password from the Account Manager, and ask
        // the server for an appropriate AuthToken.
        final AccountManager am = AccountManager.get(mContext);


        String authToken = am.peekAuthToken(account, authTokenType);

        Log.d(TAG, "> peekAuthToken returned - " + authToken);

        if(TextUtils.isEmpty(authToken)){
            authToken = mPrefs.getToken();
            Log.d(TAG, "> authToken from preferences returned - " + authToken);
        }

        // Lets give another try to authenticate the user
        if (TextUtils.isEmpty(authToken)) {
            final String password = am.getPassword(account);
            if (password != null) {
                try {
                    Log.d(TAG, "> re-authenticating with the existing password");
                    UserEP userEP = new UserEP(mContext, mApplication);
                    authToken = userEP.login(account.name, password, UserEP.LoginType.Email);
                    mApplication.setLoggedInUser(userEP.getLoggedInUser());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        // If we get an authToken - we return it
        if (!TextUtils.isEmpty(authToken)) {
            initSync(account, mContext);
            mPrefs.setToken(authToken);
            final Bundle result = new Bundle();
            result.putString(AccountManager.KEY_ACCOUNT_NAME, account.name);
            result.putString(AccountManager.KEY_ACCOUNT_TYPE, account.type);
            result.putString(AccountManager.KEY_AUTHTOKEN, authToken);
            return result;
        }

        // If we get here, then we couldn't access the user's password - so we
        // need to re-prompt them for their credentials. We do that by creating
        // an intent to display our AuthenticatorActivity.
        final Intent intent = new Intent(mContext, ViamhealthAccountAuthenticatorActivity.class);
        intent.putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE, response);
        intent.putExtra(ViamhealthAccountAuthenticatorActivity.ARG_ACCOUNT_TYPE, account.type);
        intent.putExtra(ViamhealthAccountAuthenticatorActivity.ARG_AUTH_TYPE, authTokenType);
        intent.putExtra(ViamhealthAccountAuthenticatorActivity.ARG_ACCOUNT_NAME, account.name);
        final Bundle bundle = new Bundle();
        bundle.putParcelable(AccountManager.KEY_INTENT, intent);
        return bundle;
    }


    @Override
    public String getAuthTokenLabel(String authTokenType) {
        if (AccountGeneral.AUTHTOKEN_TYPE_FULL_ACCESS.equals(authTokenType))
            return AccountGeneral.AUTHTOKEN_TYPE_FULL_ACCESS_LABEL;
        else if (AccountGeneral.AUTHTOKEN_TYPE_READ_ONLY.equals(authTokenType))
            return AccountGeneral.AUTHTOKEN_TYPE_READ_ONLY_LABEL;
        else
            return authTokenType + " (Label)";
    }

    @Override
    public Bundle updateCredentials(AccountAuthenticatorResponse response, Account account, String authTokenType, Bundle options) throws NetworkErrorException {
        return null;
    }

    @Override
    public Bundle hasFeatures(AccountAuthenticatorResponse response, Account account, String[] features) throws NetworkErrorException {
        final Bundle result = new Bundle();
        result.putBoolean(AccountManager.KEY_BOOLEAN_RESULT, false);
        return result;
    }

}
