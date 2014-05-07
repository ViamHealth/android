package com.viamhealth.android.sync;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SyncResult;
import android.os.Bundle;

import com.viamhealth.android.BuildConfig;
import com.viamhealth.android.auth.AccountGeneral;
import com.viamhealth.android.utils.LogUtils;

import java.io.IOException;
import java.util.regex.Pattern;

/**
 * Created by naren on 17/12/13.
 */
public class SyncAdapter extends AbstractThreadedSyncAdapter {

    private static final String TAG = LogUtils.makeLogTag(SyncAdapter.class);

    private static final Pattern sSanitizeAccountNamePattern = Pattern.compile("(.).*?(.?)@");

    private final Context mContext;
    private final AccountManager mAccountManager;
    private final SyncHelper mSyncHelper;

    public SyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        mContext = context;
        mAccountManager = AccountManager.get(context);
        mSyncHelper = new SyncHelper(context);

        //noinspection ConstantConditions,PointlessBooleanExpression
        if (!BuildConfig.DEBUG) {
            Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
                @Override
                public void uncaughtException(Thread thread, Throwable throwable) {
                    LogUtils.LOGE(TAG, "Uncaught sync exception, suppressing UI in release build.",
                            throwable);
                }
            });
        }
    }


    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {
        final boolean uploadOnly = extras.getBoolean(ContentResolver.SYNC_EXTRAS_UPLOAD, false);
        final boolean manualSync = extras.getBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, false);
        final boolean initialize = extras.getBoolean(ContentResolver.SYNC_EXTRAS_INITIALIZE, false);

        final String logSanitizedAccountName = sSanitizeAccountNamePattern.matcher(account.name).replaceAll("$1...$2@");

        try {
            // Get the auth token for the current account
            String authToken = mAccountManager.blockingGetAuthToken(account, AccountGeneral.AUTHTOKEN_TYPE_FULL_ACCESS, true);
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

        LogUtils.LOGI(TAG, "Beginning sync for account " + logSanitizedAccountName + "," +
                " uploadOnly=" + uploadOnly +
                " manualSync=" + manualSync +
                " initialize=" + initialize);

        try {
            mSyncHelper.performSync(syncResult, SyncHelper.FLAG_SYNC_PULL | SyncHelper.FLAG_SYNC_PUSH);
        } catch (IOException e) {
            e.printStackTrace();
            LogUtils.LOGE(TAG, e.getMessage(), e.getCause());
        }
    }
}
