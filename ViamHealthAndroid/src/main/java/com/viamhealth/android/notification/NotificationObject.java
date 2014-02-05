package com.viamhealth.android.notification;

import android.os.Parcelable;

/**
 * Created by naren on 09/01/14.
 */
public interface NotificationObject extends Parcelable{

    public static int NO_IMAGE = 0x0;

    /**
     *
     * @return returns the id of the icon that needs
     * to be show in the notification bar, if nothing then return {@link NO_IMAGE}
     */
    public int getIcon();

    /**
     *
     * @return title string resource
     */
    public String getTitle();


}
