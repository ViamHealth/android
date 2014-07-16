package com.viamhealth.android.model.reminder;

import android.os.Parcel;
import android.os.Parcelable;

import com.viamhealth.android.model.enums.ReminderTime;
import com.viamhealth.android.notification.NotificationObject;
import com.viamhealth.android.utils.ParcelableUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by naren on 21/11/13.
 */
public class ReminderReading implements NotificationObject, Parcelable {

    Long id;
    Long userId;
    Date readingDate;
    Reminder reminder;
    Map<Integer, Action> mapAction = new HashMap<Integer, Action>();
    boolean completeCheck = false;


    public void putAction(ReminderTime time, Action data) {
        mapAction.put(time.ordinal(), data);
    }

    public Action getAction(ReminderTime time) {
        return mapAction.get(time.ordinal());
    }

    public boolean isCompleteCheck() {
        return completeCheck;
    }

    public void setCompleteCheck(boolean completeCheck) {
        this.completeCheck = completeCheck;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Date getReadingDate() {
        return readingDate;
    }

    public void setReadingDate(Date readingDate) {
        this.readingDate = readingDate;
    }

    public Reminder getReminder() {
        return reminder;
    }

    public void setReminder(Reminder reminder) {
        this.reminder = reminder;
    }

    public static final Parcelable.Creator<ReminderReading> CREATOR = new Parcelable.Creator<ReminderReading>() {
        public ReminderReading createFromParcel(Parcel in) {
            return new ReminderReading(in);
        }

        public ReminderReading[] newArray(int size) {
            return new ReminderReading[size];
        }
    };

    public ReminderReading() {
    }

    public ReminderReading(Parcel in) {
        id = in.readLong();
        userId = in.readLong();
        readingDate = new Date(in.readLong());
        reminder = (Reminder) in.readParcelable(Reminder.class.getClassLoader());
        completeCheck = (Boolean) in.readValue(null);
        mapAction = ParcelableUtils.readMap(in, Action.class);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeLong(id);
        dest.writeLong(userId);
        dest.writeLong(readingDate.getTime());
        dest.writeParcelable(reminder, flags);
        dest.writeValue(completeCheck);
        ParcelableUtils.writeMap(mapAction, dest);
    }

    public String toString() {
        return "ReminderReading{" +
                "id=" + id +
                ", readingdate=" + readingDate.toString() +
                ", isCOmplete=" + String.valueOf(completeCheck) +
                ", reminder=" + reminder.getName();
    }

    /**
     * @return returns the id of the icon that needs
     * to be show in the notification bar, if nothing then return {@link NO_IMAGE}
     */
    @Override
    public int getIcon() {
        return reminder.getType().iconId();
    }

    /**
     * @return title string resource
     */
    @Override
    public String getTitle() {
        return reminder.getName();
    }
}
