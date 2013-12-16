package com.viamhealth.android.adapters;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.Context;
import android.content.SyncResult;
import android.os.Bundle;
import android.util.Log;

import com.viamhealth.android.Global_Application;
import com.viamhealth.android.auth.AccountGeneral;
import com.viamhealth.android.dao.rest.endpoints.GoalsEPHelper;
import com.viamhealth.android.model.users.User;

import java.util.List;

/**
 * Created by naren on 13/12/13.
 * Define a sync adapter for the app.
 *
 * <p>This class is instantiated in {@link SyncService}, which also binds SyncAdapter to the system.
 * SyncAdapter should only be initialized in SyncService, never anywhere else.
 *
 * <p>The system calls onPerformSync() via an RPC call through the IBinder object supplied by
 * SyncService.
 */
public class SyncAdapter extends AbstractThreadedSyncAdapter {

    private final AccountManager mAccountManager;
    private final Context mContext;
    private final Global_Application mApplication;


    private List<User> users;

    public SyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        this.mContext = context;
        this.mAccountManager = AccountManager.get(context);
        this.mApplication = (Global_Application) getContext().getApplicationContext();
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {
        Log.d("SyncAdapter", "onPerformSync for account[" + account.name + "]");

        // Get the auth token for the current account
        String authToken = mAccountManager.blockingGetAuthToken(account, AccountGeneral.AUTHTOKEN_TYPE_FULL_ACCESS, true);

        syncUsers();
        syncGoals();

    }

    private void syncUsers() {

    }

    private void syncGoalsForAllUsers() {

        GoalsEPHelper goalHelper = new GoalsEPHelper(mContext, mApplication);
        goalHelper.getAllGoalsConfigured()
    }
}
