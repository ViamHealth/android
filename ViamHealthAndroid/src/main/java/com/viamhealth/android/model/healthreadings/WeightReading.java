package com.viamhealth.android.model.healthreadings;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Kunal on 19/6/14.
 */
public class WeightReading extends HealthReading implements Parcelable {
    double weight;

    public WeightReading() {
        super();
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        if (weight != null)
            this.weight = weight;
        else
            this.weight = 0.0;
    }

    @Override
    public String toString() {
        return "WeightReadings{" +
                "weight=" + weight +
                "} " + super.toString();
    }

    public WeightReading(Parcel in) {
        super(in);
        this.weight = in.readDouble();
    }

    @Override
    public int describeContents() {
        return super.describeContents();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeDouble(this.weight);
    }

    public static final Parcelable.Creator<WeightReading> CREATOR = new Parcelable.Creator<WeightReading>() {
        public WeightReading createFromParcel(Parcel in) {
            return new WeightReading(in);
        }

        public WeightReading[] newArray(int size) {
            return new WeightReading[size];
        }
    };

}
