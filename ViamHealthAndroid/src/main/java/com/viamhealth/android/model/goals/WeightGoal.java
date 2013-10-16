package com.viamhealth.android.model.goals;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by naren on 11/10/13.
 */
public class WeightGoal extends Goal implements Parcelable {
    double weight;

    public WeightGoal() {
        super();
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        if(weight!=null)
            this.weight = weight;
        else
            this.weight = 0.0;
    }

    @Override
    public String toString() {
        return "WeightGoal{" +
                "weight=" + weight +
                "} " + super.toString();
    }

    public WeightGoal(Parcel in) {
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

    public static final Parcelable.Creator<WeightGoal> CREATOR = new Parcelable.Creator<WeightGoal>() {
        public WeightGoal createFromParcel(Parcel in) {
            return new WeightGoal(in);
        }

        public WeightGoal[] newArray(int size) {
            return new WeightGoal[size];
        }
    };
    
}
