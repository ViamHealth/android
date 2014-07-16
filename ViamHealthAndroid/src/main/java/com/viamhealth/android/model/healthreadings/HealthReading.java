package com.viamhealth.android.model.healthreadings;

import android.os.Parcel;
import android.os.Parcelable;

import com.viamhealth.android.model.BaseModel;

import java.util.Date;

/**
 * Created by Kunal on 19/6/14.
 */
public class HealthReading extends BaseModel implements Parcelable {
    Date readingDate;

    public Date getReadingDate() {
        return readingDate;
    }

    public void setReadingDate(Date readingDate) {
        this.readingDate = readingDate;
    }

    @Override
    public String toString() {
        return "HealthReadings{" +
                ", readingDate=" + readingDate +
                "} " + super.toString();
    }

    public HealthReading() {
        super();
    }

    public HealthReading(Parcel in) {
        super(in);
        this.readingDate = new Date(in.readLong());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeLong(this.readingDate == null ? 0 : this.readingDate.getTime());
    }
}
