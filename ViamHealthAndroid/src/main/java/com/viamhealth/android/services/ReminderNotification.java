package com.viamhealth.android.services;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Intent;
import android.app.PendingIntent;
import android.support.v4.app.NotificationCompat;
import com.viamhealth.android.R;

import com.viamhealth.android.activities.ResultActivity;
import com.viamhealth.android.activities.TabActivity;
import com.viamhealth.android.model.enums.ReminderTime;
import com.viamhealth.android.model.reminder.Reminder;
import com.viamhealth.android.notifications.builders.reminders.Medicine;
import com.viamhealth.android.notifications.ReminderConstants;
import com.viamhealth.android.notifications.objects.reminders.Other;

import java.security.InvalidParameterException;
import java.util.Calendar;

/**
 * Created by kunal on 30/1/14.
 */
public class ReminderNotification extends IntentService {

    private NotificationManager mNotificationManager;
    NotificationCompat.Builder builder;
    int notificationId;
    private Other object;

    public ReminderNotification() {
        super("com.viamhealth.android");
    }

    @Override
    public void onDestroy (){
        super.onDestroy();
        System.out.println(" SERVICE DESTROYED !!!!!!!!!!!!!!!!!!!!!!!");
    }
    @Override
    protected void onHandleIntent(Intent intent) {

        mNotificationManager = (NotificationManager)
                getSystemService(NOTIFICATION_SERVICE);

        object = intent.getParcelableExtra("NOTIFICATION_OBJECT");
        String action = intent.getAction();
        notificationId = getNotificationId(action);
        System.out.println(" printing notification object");
        System.out.println(intent.getParcelableExtra("NOTIFICATION_OBJECT"));
        setReminderTime(action);

        if(action.equals(ReminderConstants.ACTION_MEDICINE_NOTIFY_MORNING) ||
                action.equals(ReminderConstants.ACTION_MEDICINE_NOTIFY_NOON) ||
                action.equals(ReminderConstants.ACTION_MEDICINE_NOTIFY_NIGHT)){
            builder = getNotificationBuilder(this, intent, rt);
            issueNotification(builder,notificationId);
        }

        else if (action.equals(ReminderConstants.ACTION_MEDICINE_REMIND_MORNING) ||
                    action.equals(ReminderConstants.ACTION_MEDICINE_REMIND_NOON) ||
                    action.equals(ReminderConstants.ACTION_MEDICINE_REMIND_NIGHT) ) {
            System.out.println("############REMIND AGAIN notifications");
            //Dismiss and reissue
            mNotificationManager.cancel(notificationId);
            try {
                Thread.sleep(com.viamhealth.android.notifications.objects.reminders.Medicine.snoozeRemindTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            builder = getNotificationBuilder(this, intent, rt);
            issueNotification(builder,notificationId);
        }


        else if (action.equals(ReminderConstants.ACTION_MEDICINE_TAKEN_MORNING) ||
                    action.equals(ReminderConstants.ACTION_MEDICINE_TAKEN_NOON) ||
                    action.equals(ReminderConstants.ACTION_MEDICINE_TAKEN_NIGHT) ) {
            System.out.println("############CANCEL notifications");
            //Dismiss action
            mNotificationManager.cancel(notificationId);
        }
    }

    public  NotificationCompat.Builder getNotificationBuilder(
            IntentService service,
            Intent intent,
            ReminderTime reminderTime)
    {
        System.out.println("Printing objectd-------------------------------");
        System.out.println(object.toString());
        Intent dismissIntent = new Intent(service.getApplicationContext(), ReminderNotification.class);
        dismissIntent.putExtra("NOTIFICATION_OBJECT", object);

        if(reminderTime == ReminderTime.Morning)
            dismissIntent.setAction(ReminderConstants.ACTION_MEDICINE_TAKEN_MORNING);
        else if (reminderTime == ReminderTime.Noon)
            dismissIntent.setAction(ReminderConstants.ACTION_MEDICINE_TAKEN_NOON);
        else if (reminderTime == ReminderTime.Night)
            dismissIntent.setAction(ReminderConstants.ACTION_MEDICINE_TAKEN_NIGHT);
        else
            throw new IllegalArgumentException();

        //dismissIntent.putExtra("NOTIFICATION_ID",object.getNotificationId());

        PendingIntent piDismiss = PendingIntent.getService(service.getApplicationContext(), 0, dismissIntent, 0);

        Intent snoozeIntent = new Intent(service.getApplicationContext(), ReminderNotification.class);
        snoozeIntent.putExtra("NOTIFICATION_OBJECT", object);
        if(reminderTime == ReminderTime.Morning)
            snoozeIntent.setAction(ReminderConstants.ACTION_MEDICINE_REMIND_MORNING);
        else if (reminderTime == ReminderTime.Noon)
            snoozeIntent.setAction(ReminderConstants.ACTION_MEDICINE_REMIND_NOON);
        else if (reminderTime == ReminderTime.Night)
            snoozeIntent.setAction(ReminderConstants.ACTION_MEDICINE_REMIND_NIGHT);
        else
            throw new IllegalArgumentException();

        //snoozeIntent.putExtra("NOTIFICATION_ID",object.getNotificationId());
        PendingIntent piSnooze = PendingIntent.getService(service, 0, snoozeIntent, 0);


        NotificationCompat.Builder builder = new NotificationCompat.Builder(service);
        builder.setSmallIcon(object.getSmallIcon());
        builder.setContentTitle(object.getContentTitle());
        builder.setContentText(object.getContentText());
        builder.setDefaults(Notification.DEFAULT_ALL);
        builder.setStyle(new NotificationCompat.BigTextStyle()
                .bigText(object.getBigText()));
        builder.addAction(object.getIconTakenAction(),
                object.getStringTakenAction(), piDismiss);
        builder.addAction(object.getIconRemindAction(),
                object.getStringRemindAction(), piSnooze);

        if(reminderTime == null ){
            //Launch immediately
            //For snooze/remind
        } else {
            //Set time
            Calendar cal = Calendar.getInstance();
            cal.setTime(object.getDate());
            if(reminderTime == ReminderTime.Morning){
                cal.add(Calendar.HOUR_OF_DAY, 7);
                cal.add(Calendar.MINUTE,30);
            }else if(reminderTime == ReminderTime.Noon){
                cal.add(Calendar.HOUR_OF_DAY, 14);
                cal.add(Calendar.MINUTE,1);
            } else if(reminderTime == ReminderTime.Night){
                cal.add(Calendar.HOUR_OF_DAY, 23);
                cal.add(Calendar.MINUTE,2);
            } else {
                cal.add(Calendar.HOUR_OF_DAY, 2);
                cal.add(Calendar.MINUTE,33);
            }
            builder.setWhen(cal.getTimeInMillis());
        }


        Intent resultIntent = new Intent(service,
                ResultActivity.class);
        //resultIntent.putExtra("EXTRA_MESSAGE", "extra message hia ye");
        resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//        resultIntent.putExtra("NOTIFICATION_OBJECT", object);
        //resultIntent.putExtra("NOTIFICATION_ID",1111);
        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        service,
                        0,
                        resultIntent,
                        //new Intent(),
                        PendingIntent.FLAG_UPDATE_CURRENT
                );

        builder.setContentIntent(resultPendingIntent);

        return builder;

    }

    private int getNotificationId(String action){
        if(action.equals(ReminderConstants.ACTION_MEDICINE_NOTIFY_MORNING)
                || action.equals(ReminderConstants.ACTION_MEDICINE_REMIND_MORNING)
                || action.equals(ReminderConstants.ACTION_MEDICINE_TAKEN_MORNING)){
            return ReminderConstants.NOTIFICATION_ID_MEDICINE_MORNING;
        }
        if(action.equals(ReminderConstants.ACTION_MEDICINE_NOTIFY_NOON)
                || action.equals(ReminderConstants.ACTION_MEDICINE_REMIND_NOON)
                || action.equals(ReminderConstants.ACTION_MEDICINE_TAKEN_NOON)){
            return ReminderConstants.NOTIFICATION_ID_MEDICINE_NOON;
        }
        if(action.equals(ReminderConstants.ACTION_MEDICINE_NOTIFY_NIGHT)
                || action.equals(ReminderConstants.ACTION_MEDICINE_REMIND_NIGHT)
                || action.equals(ReminderConstants.ACTION_MEDICINE_TAKEN_NIGHT)){
            return ReminderConstants.NOTIFICATION_ID_MEDICINE_NIGHT;
        }
        return 0;
    }

    private ReminderTime rt;

    private void setReminderTime(String action){
        if(action.equals(ReminderConstants.ACTION_MEDICINE_NOTIFY_MORNING)
                || action.equals(ReminderConstants.ACTION_MEDICINE_REMIND_MORNING)
                || action.equals(ReminderConstants.ACTION_MEDICINE_TAKEN_MORNING)){
            rt =  ReminderTime.Morning;
        }
        if(action.equals(ReminderConstants.ACTION_MEDICINE_NOTIFY_NOON)
                || action.equals(ReminderConstants.ACTION_MEDICINE_REMIND_NOON)
                || action.equals(ReminderConstants.ACTION_MEDICINE_TAKEN_NOON)){
            rt =  ReminderTime.Noon;
        }
        if(action.equals(ReminderConstants.ACTION_MEDICINE_NOTIFY_NIGHT)
                || action.equals(ReminderConstants.ACTION_MEDICINE_REMIND_NIGHT)
                || action.equals(ReminderConstants.ACTION_MEDICINE_TAKEN_NIGHT)){
            rt =  ReminderTime.Night;
        }
    }

    private void issueNotification(NotificationCompat.Builder builder, int notificationId){
        mNotificationManager = (NotificationManager)
                getSystemService(NOTIFICATION_SERVICE);
        mNotificationManager.notify(notificationId, builder.build());
    }
}
