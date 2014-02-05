package com.viamhealth.android.notification;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;

import com.viamhealth.android.R;
import com.viamhealth.android.activities.Home;
import com.viamhealth.android.activities.TabActivity;
import com.viamhealth.android.activities.fragments.ReminderFragmentNew;
import com.viamhealth.android.model.reminder.ReminderReading;
import com.viamhealth.android.model.users.User;

import java.util.List;

/**
 * Created by naren on 11/01/14.
 */
public class UserReminderNotifyHandler extends NotificationHandler {

    public UserReminderNotifyHandler(Context context) {
        super(context);
    }

    @Override
    public NotificationCompat.Builder getNotification(User user, List<ReminderReading> notifyObjects) {
        // In this sample, we'll use the same text for the ticker and the expanded notification
        CharSequence text = mContext.getText(R.string.local_service_started);

        // The PendingIntent to launch our activity if the user selects this notification
        PendingIntent contentIntent = PendingIntent.getActivity(mContext, 0,
                new Intent(mContext, Home.class), PendingIntent.FLAG_UPDATE_CURRENT);

        /** TODO:: if any special action to be taken then have it here **/

        int largeIconId = R.drawable.ic_navigation_logo;
        //largeIconId = largeIconId==NotificationObject.NO_IMAGE ?  :largeIconId;

        String contentTitle = "Nothing is set";
        String contentText = "";
        contentText = "You have " + notifyObjects.size() + " new reminders";
        if(notifyObjects.size()>1){
            contentTitle = notifyObjects.size() + " new reminders";
        }else if(notifyObjects.size()==1){
            contentTitle = notifyObjects.get(0).getTitle();
            largeIconId = notifyObjects.get(0).getIcon();
        }


        NotificationCompat.Builder notifyBuilder = new NotificationCompat.Builder(mContext)
                .setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), largeIconId))
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(contentTitle))
                .setDefaults(Notification.DEFAULT_ALL)
                .setContentTitle(contentText)
                .setSmallIcon(R.drawable.ic_action_reminders)
                .setContentText(contentTitle)
                .setContentIntent(contentIntent);

        return notifyBuilder;
    }
}
