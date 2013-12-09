package com.viamhealth.android.model.reminder;

import android.os.Parcel;
import android.os.Parcelable;

/**
* Created by naren on 22/11/13.
*/
public class ReminderTimeData implements Parcelable {
    Integer count;

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public ReminderTimeData() {}

    public ReminderTimeData(Parcel in) {
        count = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(count);
    }

    public static final Parcelable.Creator<ReminderTimeData> CREATOR = new Parcelable.Creator<ReminderTimeData>() {
        public ReminderTimeData createFromParcel(Parcel in) {
            return new ReminderTimeData(in);
        }

        public ReminderTimeData[] newArray(int size) {
            return new ReminderTimeData[size];
        }
    };

    @Override
    public String toString() {
        return "ReminderTimeData{" +
                "count=" + count +
                "} " + super.toString();
    }
}
