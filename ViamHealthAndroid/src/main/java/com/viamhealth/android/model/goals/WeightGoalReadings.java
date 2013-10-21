package com.viamhealth.android.model.goals;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

/**
 * Created by naren on 11/10/13.
 */
public class WeightGoalReadings extends GoalReadings implements Parcelable {

    double weight;
    double height;

    public WeightGoalReadings() {
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

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    @Override
    public String toString() {
        return "WeightGoalReadings{" +
                "weight=" + weight +
                "} " + super.toString();
    }

    public WeightGoalReadings(Parcel in) {
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

    public static final Parcelable.Creator<WeightGoalReadings> CREATOR = new Parcelable.Creator<WeightGoalReadings>() {
        public WeightGoalReadings createFromParcel(Parcel in) {
            return new WeightGoalReadings(in);
        }

        public WeightGoalReadings[] newArray(int size) {
            return new WeightGoalReadings[size];
        }
    };

    @Override
    public JSONObject toJSON(GraphSeries series) {
        JSONObject object = parentJSON();

        try {
            object.put("y", weight);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return object;
    }
}
