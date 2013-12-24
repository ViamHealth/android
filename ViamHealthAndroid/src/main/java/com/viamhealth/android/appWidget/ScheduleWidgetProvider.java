package com.viamhealth.android.appWidget;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import com.viamhealth.android.auth.AccountGeneral;
import com.viamhealth.android.utils.LogUtils;

/**
 * Created by naren on 17/12/13.
 */
public class ScheduleWidgetProvider extends AppWidgetProvider {

    private static final String TAG = LogUtils.makeLogTag(ScheduleWidgetProvider.class);

    private static final String REFRESH_ACTION = "com.viamhealth.android.appwidget.action.REFRESH";
    private static final String EXTRA_PERFORM_SYNC = "com.viamhealth.android.appwidget.extra.PERFORM_SYNC";

    public static Intent getRefreshBroadcastIntent(Context context, boolean performSync) {
        return new Intent(REFRESH_ACTION)
                .setComponent(new ComponentName(context, ScheduleWidgetProvider.class))
                .putExtra(EXTRA_PERFORM_SYNC, performSync);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        final String action = intent.getAction();

//        if (REFRESH_ACTION.equals(action)) {
//            final boolean shouldSync = widgetIntent.getBooleanExtra(EXTRA_PERFORM_SYNC, false);
//            AccountManager manager = AccountManager.get(context);
//            Account[] accounts = manager.getAccountsByType(AccountGeneral.ACCOUNT_TYPE);
//
//            Account chosenAccount = (accounts!=null && accounts.length>0) ? accounts[0] : null;
//            if(shouldSync && chosenAccount!=null){
//                //do manual sync
//            }
//
//        }
        super.onReceive(context, intent);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }
}
