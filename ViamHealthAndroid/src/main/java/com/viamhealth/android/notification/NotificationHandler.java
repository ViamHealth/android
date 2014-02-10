package com.viamhealth.android.notification;


import android.content.Context;
import android.support.v4.app.NotificationCompat;

import com.viamhealth.android.Global_Application;
import com.viamhealth.android.dao.rest.endpoints.UserEP;
import com.viamhealth.android.model.reminder.ReminderReading;
import com.viamhealth.android.model.users.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by naren on 11/01/14.
 */
public abstract class NotificationHandler {

    protected final Context mContext;
    protected final NotificationCompat.Builder builder;


    public NotificationHandler(Context context) {
        mContext = context;
        builder = new NotificationCompat.Builder(context);

    }

    public abstract NotificationCompat.Builder getNotification(User user, List<ReminderReading> notifyObjects, int notification_id);


}
