package com.viamhealth.android.sync;

import android.accounts.Account;
import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.content.SharedPreferences;
import android.content.SyncResult;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.RemoteException;
import android.preference.PreferenceManager;

import com.google.analytics.tracking.android.GAServiceManager;
import com.viamhealth.android.provider.ScheduleContract;
import com.viamhealth.android.provider.handlers.ReminderHandler;
import com.viamhealth.android.provider.handlers.UserHandler;
import com.viamhealth.android.sync.restclient.UserEndPoint;
import com.viamhealth.android.utils.Checker;
import com.viamhealth.android.utils.LogUtils;


import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * A helper class for dealing with sync and other remote persistence operations.
 * All operations occur on the thread they're called from, so it's best to wrap
 * calls in an {@link android.os.AsyncTask}, or better yet, a
 * {@link android.app.Service}.
 */
public class SyncHelper {
    private static final String TAG = LogUtils.makeLogTag(SyncHelper.class);

    public static final int FLAG_SYNC_PULL = 0x1;
    public static final int FLAG_SYNC_PUSH = 0x2;

    public static final String FILTER_USER_SYNC_FINISHED = "VH_USER_SYNC_FINISHED";

    //private static final int LOCAL_VERSION_CURRENT = 01;
    //private static final String LOCAL_MAPVERSION_CURRENT = "\"vlh7Ig\"";

    public enum SyncType {
        PUSH,
        PULL;
    }
    private Context mContext;

    public SyncHelper(Context context) {
        mContext = context;
    }

    public static void requestSync(Account account) {
        Bundle bundle = new Bundle();
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true); // Performing a sync no matter if it's off
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true); // Performing a sync no matter if it's off
        ContentResolver.requestSync(account, ScheduleContract.CONTENT_AUTHORITY, bundle);
    }

    public static void requestManualSync(Account mChosenAccount) {
        Bundle b = new Bundle();
        b.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        ContentResolver.requestSync(
                mChosenAccount,
                ScheduleContract.CONTENT_AUTHORITY, b);
    }


    /**
     * Loads all information from server (users, goals, journal, reminders, files, etc.)
     *
     * @param syncResult Optional {@link android.content.SyncResult} object to populate.
     * @throws java.io.IOException
     */
    public void performSync(SyncResult syncResult, int flags) throws IOException {

        // Bulk of sync work, performed by executing several fetches from
        // local and online sources.
        final ContentResolver resolver = mContext.getContentResolver();
        ArrayList<ContentProviderOperation> batch = new ArrayList<ContentProviderOperation>();

        LogUtils.LOGI(TAG, "Performing sync");

        if ((flags & FLAG_SYNC_PULL) != 0 && Checker.isInternetOn(mContext)) {
            final long startLocal = System.currentTimeMillis();

            /** If there are any pending data to be synced to the corresponding tables
             * then just remove those rows or mark them as sync_overriden
             */
            LogUtils.LOGI(TAG, "PULL Based Sync started...");
            LogUtils.LOGI(TAG, "Syncing users..");
            batch.addAll(new UserHandler(mContext).fetchAndParse(SyncType.PULL));
            LogUtils.LOGI(TAG, "Syncing reminders PULL..");
            batch.addAll(new ReminderHandler(mContext).fetchAndParse(SyncType.PULL));


            try {
                // Apply all queued up batch operations for local data.
                resolver.applyBatch(ScheduleContract.CONTENT_AUTHORITY, batch);
                LogUtils.LOGD(TAG, "Local sync took (PULL) " + (System.currentTimeMillis() - startLocal) + "ms");
            } catch (RemoteException e) {
                LogUtils.LOGI(TAG, "PULL Based Sync caused some error...");
                throw new RuntimeException("Problem applying batch operation", e);
            } catch (OperationApplicationException e) {
                LogUtils.LOGI(TAG, "PULL Based Sync caused some error...");
                throw new RuntimeException("Problem applying batch operation", e);
            }

            batch = new ArrayList<ContentProviderOperation>();
        }

        if ((flags & FLAG_SYNC_PUSH) != 0 && Checker.isInternetOn(mContext)) {
            final long startLocal = System.currentTimeMillis();

            /** If there are any pending data to be synced to the corresponding tables
             * then just remove those rows or mark them as sync_overriden
             */
            LogUtils.LOGI(TAG, "PUSH Based Sync started...");
            LogUtils.LOGI(TAG, "Syncing users..");
            batch.addAll(new UserHandler(mContext).push());
            LogUtils.LOGI(TAG, "Syncing reminders PUSH..");
            batch.addAll(new ReminderHandler(mContext).push());


            try {
                // Apply all queued up batch operations for local data.
                resolver.applyBatch(ScheduleContract.CONTENT_AUTHORITY, batch);
                LogUtils.LOGD(TAG, "Local sync took " + (System.currentTimeMillis() - startLocal) + "ms");
            } catch (RemoteException e) {
                throw new RuntimeException("Problem applying batch operation", e);
            } catch (OperationApplicationException e) {
                throw new RuntimeException("Problem applying batch operation", e);
            }

            batch = new ArrayList<ContentProviderOperation>();
        }

        mContext.sendBroadcast(new Intent(FILTER_USER_SYNC_FINISHED));

    }
}