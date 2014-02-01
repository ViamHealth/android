package com.viamhealth.android.notifications.builders.reminders;

import android.app.IntentService;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.viamhealth.android.activities.TabActivity;
import com.viamhealth.android.activities.fragments.ReminderFragmentNew;
import com.viamhealth.android.model.enums.ReminderTime;
import com.viamhealth.android.notifications.ReminderConstants;
import com.viamhealth.android.services.ReminderNotification;

import java.util.Calendar;

/**
 * Created by kunal on 30/1/14.
 */
public class Medicine {

    /*public static NotificationCompat.Builder getNotificationBuilder(
            IntentService service,
            Intent intent,
            ReminderTime reminderTime)
    {
        com.viamhealth.android.notifications.objects.reminders.Medicine
                object = intent.getParcelableExtra("NOTIFICATION_OBJECT");
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
                cal.add(Calendar.HOUR_OF_DAY, 23);
                cal.add(Calendar.MINUTE,33);
            }
            builder.setWhen(cal.getTimeInMillis());
        }


        Intent resultIntent = new Intent(service,
                TabActivity.class);
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

    }*/
}
