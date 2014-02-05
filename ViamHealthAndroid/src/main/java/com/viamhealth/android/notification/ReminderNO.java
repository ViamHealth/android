package com.viamhealth.android.notification;

import android.os.Parcel;
import android.os.Parcelable;

import com.viamhealth.android.model.reminder.ReminderReading;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kunal on 5/2/14.
 */
public class ReminderNO implements Parcelable, NotificationObject {
    private List<ReminderReading> readings;

    public ReminderNO(List<ReminderReading> readings) {
        this.readings = readings;
    }

    public List<ReminderReading> getReadings() {
        return readings;
    }

    public void setReadings(List<ReminderReading> readings) {
        this.readings = readings;
    }

    public static final Parcelable.Creator<ReminderNO> CREATOR = new Parcelable.Creator<ReminderNO>() {
        public ReminderNO createFromParcel(Parcel in) {
            return new ReminderNO(in);
        }

        public ReminderNO[] newArray(int size) {
            return new ReminderNO[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeList(readings);

    }

    public ReminderNO(Parcel in) {
        readings = in.readArrayList(ReminderReading.class.getClassLoader());
    }

    /**
     * @return returns the id of the icon that needs
     * to be show in the notification bar, if nothing then return {@link NO_IMAGE}
     */
    @Override
    public int getIcon() {
        return 0;
    }

    /**
     * @return title string resource
     */
    @Override
    public String getTitle() {
        return null;
    }
}
