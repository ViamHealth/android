package com.viamhealth.android.services;

import android.content.Context;
import android.content.Intent;
import android.os.Parcel;
import android.support.v4.app.NotificationCompat;

import com.viamhealth.android.Global_Application;
import com.viamhealth.android.dao.rest.endpoints.UserEP;
import com.viamhealth.android.model.reminder.ReminderReading;
import com.viamhealth.android.model.users.User;
import com.viamhealth.android.notification.NotificationHandler;
import com.viamhealth.android.notification.NotificationType;
import com.viamhealth.android.notification.NotifyHandlerFactory;
import com.viamhealth.android.notification.ReminderNO;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kunal on 6/2/14.
 */
public class ServicesCommon {
    public final static int REMIND_AGAIN_TIME = 90000;//millisec = 15 min
    public final static String PARAM_USER = "user";
    public final static String PARAM_TYPE = "type";
    public final static String PARAM_DATA = "data";
    public final static String PARAM_FAMILY_USERS = "family_users";
    public final static String ACTION_CREATE = "com.viamhealth.android.ACTION_CREATE";
    public final static String ACTION_ACTED = "com.viamhealth.android.ACTION_ACTED";
    public final static String ACTION_DISMISS = "com.viamhealth.android.ACTION_DISMISS";
    public final static String NOTIFICATION = "NOTIFICATION";
    public final static int OTHERS_HOUR = 9;
    public final static int MEDICINE_MORNING_HOUR = 8;
    public final static int MEDICINE_NOON_HOUR = 2;
    public final static int MEDICINE_EVENING_HOUR = 9;

    private ServicesCommon() {
    }

    public static NotificationCompat.Builder getNotification(Intent intent, Context mcontext, int NOTIFICATION) {

        User user = ServicesCommon.getUserFromIntent(intent);

        int typeOrdinal = intent.getIntExtra(ServicesCommon.PARAM_TYPE, -1);
        if (typeOrdinal >= 0) {
            NotificationType type = NotificationType.values()[typeOrdinal];

            List<ReminderReading> objects = ServicesCommon.getListReminderReadingFromIntent(intent);

            NotificationHandler handler = NotifyHandlerFactory.getHandler(type, mcontext);
            NotificationCompat.Builder notification = handler.getNotification(user, objects, NOTIFICATION);
            return notification;
        } else {
            throw new IllegalArgumentException();
        }

    }

    public static Parcel getUserParcel(User user) {
        Parcel parcel_user = Parcel.obtain();
        user.writeToParcel(parcel_user, 0);
        parcel_user.setDataPosition(0);
        return parcel_user;
    }

    public static Parcel getReminderNOParcel(List<ReminderReading> readings) {
        ReminderNO reminderNo = new ReminderNO(readings);
        Parcel parcel = Parcel.obtain();
        reminderNo.writeToParcel(parcel, 0);
        parcel.setDataPosition(0);
        return parcel;
    }

    public static User getUserFromIntent(Intent intent) {
        byte[] byteArrayExtraUser = intent.getByteArrayExtra(ServicesCommon.PARAM_USER);

        Parcel parcel_user = Parcel.obtain();
        parcel_user.unmarshall(byteArrayExtraUser, 0, byteArrayExtraUser.length);
        parcel_user.setDataPosition(0);
        User user = User.CREATOR.createFromParcel(parcel_user);
        return user;

    }

    public static List<ReminderReading> getListReminderReadingFromIntent(Intent intent) {
        byte[] byteArrayExtra = intent.getByteArrayExtra(ServicesCommon.PARAM_DATA);
        Parcel parcel = Parcel.obtain();
        parcel.unmarshall(byteArrayExtra, 0, byteArrayExtra.length);
        parcel.setDataPosition(0);
        ReminderNO objects_n = ReminderNO.CREATOR.createFromParcel(parcel);
        List<ReminderReading> objects = objects_n.getReadings();
        return objects;

    }

    public static List<User> getFamilyUsers(Context context) {

        List<User> lstFamily = new ArrayList<User>();
        Global_Application ga;
        ga = ((Global_Application) context.getApplicationContext());
        UserEP userEndPoint = new UserEP(context, ga);
        ;

        lstFamily.addAll(userEndPoint.GetFamilyMembers());
        return lstFamily;
    }

}
