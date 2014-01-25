package com.viamhealth.android.notification;

import android.app.Notification;
import android.content.Context;

import com.viamhealth.android.model.users.User;

import java.util.List;

/**
 * Created by naren on 11/01/14.
 */
public abstract class NotificationHandler {

    protected final Context mContext;
    protected final Notification.Builder builder;

    public NotificationHandler(Context context) {
        mContext = context;
        builder = new Notification.Builder(context);
    }

    public abstract Notification getNotification(User user, List<NotificationObject> notifyObjects);
}
