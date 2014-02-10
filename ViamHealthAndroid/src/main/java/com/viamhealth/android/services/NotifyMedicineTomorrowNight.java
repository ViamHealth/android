package com.viamhealth.android.services;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Parcel;
import android.support.v4.app.NotificationCompat;

import com.viamhealth.android.Global_Application;
import com.viamhealth.android.R;
import com.viamhealth.android.dao.rest.endpoints.ReminderEP;
import com.viamhealth.android.model.enums.ReminderTime;
import com.viamhealth.android.model.reminder.ReminderReading;
import com.viamhealth.android.model.users.User;
import com.viamhealth.android.notification.NotificationType;
import com.viamhealth.android.utils.LogUtils;

import java.util.List;

/**
 * Created by kunal on 6/2/14.
 */
public class NotifyMedicineTomorrowNight extends IntentService{
    public NotifyMedicineTomorrowNight(){
        super("com.viamhealth.android");
    }

    private NotificationManager mNM;
    Global_Application ga;

    private static int NOTIFICATION = 14;
    private final static Class selfClass = NotifyMedicineTomorrowNight.class;
    private static final String TAG = LogUtils.makeLogTag(selfClass);

    @Override
    protected void onHandleIntent(Intent intent) {
        mNM = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        ga = ((Global_Application)getApplicationContext());

        String action = intent.getAction();

        if(action.equals(ServicesCommon.ACTION_CREATE)) {
            buildNotification(intent);
        } else if(action.equals(ServicesCommon.ACTION_ACTED)) {
            mNM.cancel(NOTIFICATION);
            List<ReminderReading> objects = ServicesCommon.getListReminderReadingFromIntent(intent);
            for(ReminderReading rr : objects){
                if(rr.getAction(ReminderTime.Morning).isCheck() == Boolean.TRUE){
                    continue;
                }
                rr.getAction(ReminderTime.Morning).setCheck(Boolean.TRUE);
                ReminderEP rep = new ReminderEP(this,ga);
                rep.updateReading(rr);
            }
        }
        else if(action.equals(ServicesCommon.ACTION_DISMISS)) {
            mNM.cancel(NOTIFICATION);
            try {
                Thread.sleep(ServicesCommon.REMIND_AGAIN_TIME);
            } catch (InterruptedException e) {
            }
            buildNotification(intent);
        }
    }


    private void buildNotification(Intent intent){
        NotificationCompat.Builder notification = ServicesCommon.getNotification(intent, this, NOTIFICATION);
        notification.addAction(R.drawable.btn_check_holo_light,getString(R.string.notification_medicine_taken),getActedPendingIntent(intent));
        notification.addAction(R.drawable.ic_action_reminders,getString(R.string.notification_remind),getDismissPendingIntent(intent));
        Notification not = notification.build();
        mNM.notify(NOTIFICATION, not);
    }

    private PendingIntent getActedPendingIntent(Intent recievedIntent){
        User user = ServicesCommon.getUserFromIntent(recievedIntent);
        List<ReminderReading> objects = ServicesCommon.getListReminderReadingFromIntent(recievedIntent);
        Parcel parcel_user = ServicesCommon.getUserParcel(user);
        Parcel parcel = ServicesCommon.getReminderNOParcel(objects);

        Intent intent = new Intent(this, selfClass);
        intent.putExtra(ServicesCommon.PARAM_TYPE, NotificationType.UserReminder.ordinal());
        intent.putExtra(ServicesCommon.PARAM_USER, parcel_user.marshall());
        intent.putExtra(ServicesCommon.PARAM_DATA, parcel.marshall());
        intent.setAction(ServicesCommon.ACTION_ACTED);
        intent.putExtra(ServicesCommon.NOTIFICATION, NOTIFICATION);
        PendingIntent pi = PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        return pi;
    }

    private PendingIntent getDismissPendingIntent(Intent recievedIntent){
        User user = ServicesCommon.getUserFromIntent(recievedIntent);
        List<ReminderReading> objects = ServicesCommon.getListReminderReadingFromIntent(recievedIntent);
        Parcel parcel_user = ServicesCommon.getUserParcel(user);
        Parcel parcel = ServicesCommon.getReminderNOParcel(objects);

        Intent intent = new Intent(this, selfClass);
        intent.putExtra(ServicesCommon.PARAM_TYPE, NotificationType.UserReminder.ordinal());
        intent.putExtra(ServicesCommon.PARAM_USER, parcel_user.marshall());
        intent.putExtra(ServicesCommon.PARAM_DATA, parcel.marshall());
        intent.setAction(ServicesCommon.ACTION_DISMISS);
        intent.putExtra(ServicesCommon.NOTIFICATION, NOTIFICATION);
        PendingIntent pi = PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        return pi;
    }
}
