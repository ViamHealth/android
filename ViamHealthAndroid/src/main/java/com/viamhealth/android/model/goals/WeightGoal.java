package com.viamhealth.android.model.goals;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

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

    @Override
    public JSONObject toJSON(GraphSeries series) {
        JSONObject object = parentJSON();

        try {
            object.put("targetWeight", weight);
            object.put("healthyRange", healthyRange.toJSON(null));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return object;
    }


    public class HealthyRange extends Goal.HealthyRange {
        private double maxWeight;
        private double minWeight;

        public double getMaxWeight() {
            return maxWeight;
        }

        public void setMaxWeight(double maxWeight) {
            this.maxWeight = maxWeight;
        }

        public double getMinWeight() {
            return minWeight;
        }

        public void setMinWeight(double minWeight) {
            this.minWeight = minWeight;
        }

        @Override
        public JSONObject toJSON(GraphSeries series) {
            JSONObject healthyRangeJSON = new JSONObject();

            try {
                healthyRangeJSON.put("from", minWeight);
                healthyRangeJSON.put("to", maxWeight);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return healthyRangeJSON;
        }
    }
}