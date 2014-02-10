package com.viamhealth.android.notification;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Parcel;
import android.support.v4.app.NotificationCompat;

import com.viamhealth.android.R;
import com.viamhealth.android.activities.Home;
import com.viamhealth.android.activities.TabActivity;
import com.viamhealth.android.activities.fragments.ReminderFragmentNew;
import com.viamhealth.android.model.enums.ReminderType;
import com.viamhealth.android.model.reminder.ReminderReading;
import com.viamhealth.android.model.users.User;
import com.viamhealth.android.services.ServicesCommon;

import java.util.List;

/**
 * Created by naren on 11/01/14.
 */
public class UserReminderNotifyHandler extends NotificationHandler {

    public UserReminderNotifyHandler(Context context) {
        super(context);
    }

    @Override
    public NotificationCompat.Builder getNotification(User user, List<ReminderReading> notifyObjects, int notification_id) {
        // In this sample, we'll use the same text for the ticker and the expanded notification
        CharSequence text = mContext.getText(R.string.local_service_started);

        Intent nIntent = new Intent(mContext, Home.class);
        nIntent.putExtra(ServicesCommon.NOTIFICATION, notification_id);
        Parcel parcel_user = ServicesCommon.getUserParcel(user);
        nIntent.putExtra(ServicesCommon.PARAM_USER, parcel_user.marshall());
        Parcel parcel = ServicesCommon.getReminderNOParcel(notifyObjects);
        nIntent.putExtra(ServicesCommon.PARAM_DATA, parcel.marshall());

        nIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        // The PendingIntent to launch our activity if the user selects this notification
        PendingIntent contentIntent = PendingIntent.getActivity(mContext, 0,
                nIntent, PendingIntent.FLAG_UPDATE_CURRENT);


        /** TODO:: if any special action to be taken then have it here **/

        int largeIconId = R.drawable.ic_navigation_logo;
        //largeIconId = largeIconId==NotificationObject.NO_IMAGE ?  :largeIconId;

        String contentTitle = "";
        String contentText = "";

        //if(notifyObjects.size()>1){
            contentTitle = "";
            String medicines = "Medicine(s) -";
            String labTests = "[ Lab test(s) - ";
            String drApp = "[ Dr. Appointment(s) -";
            String Other = "[ Other - ";
            Boolean haveMedicine = Boolean.FALSE;
            Boolean haveLabTest = Boolean.FALSE;
            Boolean haveDrApp = Boolean.FALSE;
            Boolean haveOther = Boolean.FALSE;

            for(int i=0;i<notifyObjects.size();i++){
                if( notifyObjects.get(i).getReminder().getType() == ReminderType.Medicine){
                    haveMedicine = Boolean.TRUE;
                    medicines = medicines + notifyObjects.get(i).getReminder().getName()+", ";
                }
                else if( notifyObjects.get(i).getReminder().getType() == ReminderType.DrAppointments){
                    haveDrApp = Boolean.TRUE;
                    drApp = drApp +notifyObjects.get(i).getReminder().getName()+", ";
                }
                else if( notifyObjects.get(i).getReminder().getType() == ReminderType.LabTests){
                    haveLabTest = Boolean.TRUE;
                    labTests = labTests +notifyObjects.get(i).getReminder().getName()+", ";
                }
                else if( notifyObjects.get(i).getReminder().getType() == ReminderType.Other){
                    haveOther = Boolean.TRUE;
                    Other = Other +notifyObjects.get(i).getReminder().getName() +", ";
                }

            }
            if(haveMedicine == Boolean.TRUE){
                contentTitle = contentTitle + medicines;
                contentText = "Time to take your medicines";
            } else {
                contentText = "You have " + notifyObjects.size() + " new reminders";
            }

            if(haveDrApp == Boolean.TRUE)
                contentTitle = contentTitle + drApp + " ] ";
            if(haveLabTest == Boolean.TRUE)
                contentTitle = contentTitle + labTests + " ] ";
            if(haveOther == Boolean.TRUE)
                contentTitle = contentTitle + Other + " ] ";
        /*}else if(notifyObjects.size()==1){
            contentTitle = notifyObjects.get(0).getTitle();
            largeIconId = notifyObjects.get(0).getIcon();
        }*/


        NotificationCompat.Builder notifyBuilder = new NotificationCompat.Builder(mContext)
                //.setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), largeIconId))
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
