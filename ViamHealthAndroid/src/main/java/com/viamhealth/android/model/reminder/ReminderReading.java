package com.viamhealth.android.model.reminder;

import android.os.Parcel;
import android.os.Parcelable;

import com.viamhealth.android.model.enums.ReminderTime;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by naren on 21/11/13.
 */
public class ReminderReading implements Parcelable {

    Long id;
    Long userId;
    Date readingDate;
    Reminder reminder;
    Map<ReminderTime, Action> mapAction = new HashMap<ReminderTime, Action>();
    boolean completeCheck = false;


    public void putAction(ReminderTime time, Action data) {
        mapAction.put(time, data);
    }

    public Action getAction(ReminderTime time) {
        return mapAction.get(time);
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

    public class Action implements Parcelable {
        Boolean check;

        public boolean isCheck() {
            return check;
        }

        public void setCheck(boolean check) {
            this.check = check;
        }

        public Action() {}
        public Action(Parcel in) {
            check = (Boolean) in.readValue(null);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeValue(check);
        }

        @Override
        public String toString() {
            return "Action{" +
                    "check=" + check +
                    "} " + super.toString();
        }
    }

    public static final Parcelable.Creator<ReminderReading> CREATOR = new Parcelable.Creator<ReminderReading>() {
        public ReminderReading createFromParcel(Parcel in) {
            return new ReminderReading(in);
        }

        public ReminderReading[] newArray(int size) {
            return new ReminderReading[size];
        }
    };

    public ReminderReading() {}
    public ReminderReading(Parcel in) {
        id = in.readLong();
        userId = in.readLong();
        readingDate = new Date(in.readLong());
        reminder = (Reminder) in.readParcelable(null);
        completeCheck = (Boolean) in.readValue(null);
        mapAction = in.readHashMap(null);
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
        dest.writeMap(mapAction);
    }
}
