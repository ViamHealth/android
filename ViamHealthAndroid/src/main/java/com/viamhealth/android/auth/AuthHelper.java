package com.viamhealth.android.auth;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.facebook.Session;
import com.viamhealth.android.Global_Application;
import com.viamhealth.android.ViamHealthPrefs;
import com.viamhealth.android.utils.Checker;
import com.viamhealth.android.utils.LogUtils;

import java.io.IOException;

/**
 * Created by naren on 19/12/13.
 */
public class AuthHelper {

    private final Context mContext;
    private final AccountManager mAccountManager;

    private final Global_Application mApplication;
    private final ViamHealthPrefs mPrefs;
    private static final String TAG = LogUtils.makeLogTag(AuthHelper.class);

    public AuthHelper(Context context) {
        this.mContext = context;
        this.mAccountManager = AccountManager.get(context);
        mApplication = (Global_Application)context.getApplicationContext();
        mPrefs = new ViamHealthPrefs(context);
    }

    private AccountManagerCallback<Bundle> loginCallback = new AccountManagerCallback<Bundle>() {
        @Override
        public void run(AccountManagerFuture<Bundle> future) {
            LogUtils.LOGD(TAG, "callback of account manager");
            String msg = null;
            try {
                final Bundle bundle = future.getResult();
                LogUtils.LOGD(TAG, "bundle obtained in acct manager callback " + bundle.toString());
                if(mLoginObserver!=null)
                    mLoginObserver.onLoginComplete(bundle);
            } catch (OperationCanceledException e) {
                e.printStackTrace();
                msg = e.getMessage();
            } catch (IOException e) {
                e.printStackTrace();
                msg = e.getMessage();
            } catch (AuthenticatorException e) {
                e.printStackTrace();
                msg = e.getMessage();
            }
            if(!TextUtils.isEmpty(msg)){
                final String strMsg = msg;
                final Bundle bundle = new Bundle();
                bundle.putString("errMsg", strMsg);
                if(mLoginObserver!=null)
                    mLoginObserver.onLoginComplete(bundle);
            }
        }
    };

    public interface LoginLogoutObserver {
        public void onLoginComplete(Bundle bundle);
        public void onLogoutComplete();
    }

    private LoginLogoutObserver mLoginObserver;

    public void login(LoginLogoutObserver observer) {
        mLoginObserver = observer;
        LogUtils.LOGD(TAG, " login uid is " + Binder.getCallingUid());

        Account[] accounts = mAccountManager.getAccountsByType(AccountGeneral.ACCOUNT_TYPE);

        if(accounts==null || accounts.length==0){
            LogUtils.LOGD(TAG, " login Adding a new account ");
            mAccountManager.addAccount(AccountGeneral.ACCOUNT_TYPE, AccountGeneral.AUTHTOKEN_TYPE_FULL_ACCESS, null, null, (Activity)mContext, loginCallback, null);
        }else{
            //TODO just for temp fix
            String authToken = null;
            if(TextUtils.isEmpty(mPrefs.getToken())){
                authToken = mAccountManager.peekAuthToken(accounts[0], AccountGeneral.AUTHTOKEN_TYPE_FULL_ACCESS);
                LogUtils.LOGD(TAG, " login authtoken after peeking " + authToken);
                //mAccountManager.invalidateAuthToken(AccountGeneral.ACCOUNT_TYPE, authToken);
                mPrefs.setToken(authToken);
            }

            if(TextUtils.isEmpty(authToken)){
                AccountManagerFuture<Bundle> future = mAccountManager.getAuthToken(accounts[0], AccountGeneral.AUTHTOKEN_TYPE_FULL_ACCESS,null, (Activity)mContext, loginCallback, null);
                LogUtils.LOGD(TAG, " login getting AuthToken");
            }
        }
    }

    public void logout(LoginLogoutObserver observer) {
        mLoginObserver = observer;
        LogUtils.LOGD(TAG, "logout ");

        Account[] accounts = mAccountManager.getAccountsByType(AccountGeneral.ACCOUNT_TYPE);
        if(accounts!=null && accounts.length>0){
            String authToken = mAccountManager.peekAuthToken(accounts[0], AccountGeneral.AUTHTOKEN_TYPE_FULL_ACCESS);
            mAccountManager.invalidateAuthToken(AccountGeneral.ACCOUNT_TYPE, authToken);
            LogUtils.LOGD(TAG, "logout: invalidated from account manager successfully ");
        }

        //TODO:: All the bottom tasks should be removed
        if(mApplication.getLoggedInUser().getProfile()!=null && mApplication.getLoggedInUser().getProfile().getFbProfileId()!=null
                && !mApplication.getLoggedInUser().getProfile().getFbProfileId().isEmpty())
            callFacebookLogout();

        if(Checker.isInternetOn(mContext)){
            LogUtils.LOGD(TAG, "calling logout task ");
            LogoutTask task = new LogoutTask(mContext, new LogoutTask.LogoutCompleteListener() {
                @Override
                public void onLogoutComplete() {
                    mAccountManager.invalidateAuthToken(AccountGeneral.ACCOUNT_TYPE, mPrefs.getToken());
                    mApplication.setLoggedInUser(null);
                    mPrefs.setToken(null);
                    mLoginObserver.onLogoutComplete();
                }
            });
            task.execute();
        }
    }

    private void callFacebookLogout() {
        Session session = Session.getActiveSession();
        if (session != null) {
            if (!session.isClosed()) {
                session.closeAndClearTokenInformation();
            }
        } else {
            session = new Session(mContext);
            Session.setActiveSession(session);

            session.closeAndClearTokenInformation();
        }
        LogUtils.LOGD(TAG, "logout: invalidated from facebook successfully ");
    }

    public static String getToken(Context context) {
        AccountManager accountManager = AccountManager.get(context);

        Account[] accounts = accountManager.getAccountsByType(AccountGeneral.ACCOUNT_TYPE);
        String authToken = null;

        if(accounts!=null && accounts.length>0){
            authToken = accountManager.peekAuthToken(accounts[0], AccountGeneral.AUTHTOKEN_TYPE_FULL_ACCESS);
        }
        return authToken;
    }

    public static Account getAccount(Context context) {
        AccountManager accountManager = AccountManager.get(context);

        Account[] accounts = accountManager.getAccountsByType(AccountGeneral.ACCOUNT_TYPE);
        String authToken = null;

        if(accounts!=null && accounts.length>0){
            return accounts[0];
        }

        return null;
    }
}
