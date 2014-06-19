package com.viamhealth.android.model.healthreadings;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Kunal on 19/6/14.
 */
public class BloodPressureReading extends HealthReading implements Parcelable {
    int systolicPressure;
    int diastolicPressure;
    int pulseRate;

    public BloodPressureReading() {
    }

    public BloodPressureReading(Parcel in) {
        super(in);
        systolicPressure = in.readInt();
        diastolicPressure = in.readInt();
        pulseRate = in.readInt();
    }

    public Integer getSystolicPressure() {
        return systolicPressure;
    }

    public void setSystolicPressure(int systolicPressure) {
        this.systolicPressure = systolicPressure;
    }

    public Integer getDiastolicPressure() {
        return diastolicPressure;
    }

    public void setDiastolicPressure(int diastolicPressure) {
        this.diastolicPressure = diastolicPressure;
    }

    public Integer getPulseRate() {
        return pulseRate;
    }

    public void setPulseRate(int pulseRate) {
        this.pulseRate = pulseRate;
    }

    public static final Parcelable.Creator<BloodPressureReading> CREATOR = new Parcelable.Creator<BloodPressureReading>() {
        public BloodPressureReading createFromParcel(Parcel in) {
            return new BloodPressureReading(in);
        }

        public BloodPressureReading[] newArray(int size) {
            return new BloodPressureReading[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeInt(systolicPressure);
        dest.writeInt(diastolicPressure);
        dest.writeInt(pulseRate);
    }

    @Override
    public int describeContents() {
        return super.describeContents();
    }

    @Override
    public String toString() {
        return "BloodPressureReading{" +
                "systolicPressure=" + systolicPressure +
                ", diastolicPressure=" + diastolicPressure +
                ", pulseRate=" + pulseRate +
                "} " + super.toString();
    }


}
