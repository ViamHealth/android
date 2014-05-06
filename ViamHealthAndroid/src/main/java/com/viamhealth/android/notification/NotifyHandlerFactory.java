package com.viamhealth.android.notification;

import android.content.Context;

/**
 * Created by naren on 11/01/14.
 */
public class NotifyHandlerFactory {

    public static NotificationHandler getHandler(NotificationType type, Context context) {
        switch (type) {
            case UserReminder:
                return new UserReminderNotifyHandler(context);

            default:
                return null;
        }
    }
}
