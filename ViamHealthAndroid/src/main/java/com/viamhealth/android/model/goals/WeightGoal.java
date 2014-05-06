package com.viamhealth.android.model.goals;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by naren on 11/10/13.
 */
public class WeightGoal extends Goal implements Parcelable {
    public static final Parcelable.Creator<WeightGoal> CREATOR = new Parcelable.Creator<WeightGoal>() {
        public WeightGoal createFromParcel(Parcel in) {
            return new WeightGoal(in);
        }

        public WeightGoal[] newArray(int size) {
            return new WeightGoal[size];
        }
    };

    //List<WeightGoalReadings> readings = new ArrayList<WeightGoalReadings>();
    double weight;

    public WeightGoal() {
        super();
    }

    public WeightGoal(Parcel in) {
        super(in);
        this.weight = in.readDouble();
        int readingsCount = in.readInt();
        //WeightGoalReadings[] readArr;// = new WeightGoalReadings[readingsCount];
        Parcelable[] readArr = in.readParcelableArray(WeightGoalReadings.class.getClassLoader());
        if (readingsCount > 0) {
            //readArr = in.readParcelableArray(WeightGoalReadings.class.getClassLoader());
            this.readings = new ArrayList<GoalReadings>(readingsCount);
            for (int i = 0; i < readingsCount; i++) {
                this.readings.add((WeightGoalReadings) readArr[i]);
            }
        } else {
            this.readings = new ArrayList<GoalReadings>();
        }
    }

    @Override
    public List<GoalReadings> getReadings() {
        return readings;
    }

    @Override
    public void setReadings(List<GoalReadings> rds) {
        this.readings.clear();
        for (GoalReadings reading : rds) {
            this.readings.add((WeightGoalReadings) reading);
        }
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
    public Goal.HealthyRange getHealthyRange() {
        return this.healthyRange;
    }

    @Override
    public void setHealthyRange(Goal.HealthyRange healthyRange) {
        this.healthyRange = (HealthyRange) healthyRange;
    }

    @Override
    public String toString() {
        return "WeightGoal{" +
                "weight=" + weight +
                "} " + super.toString();
    }

    @Override
    public int describeContents() {
        return super.describeContents();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeDouble(this.weight);
        int readingsCount = this.readings == null ? 0 : this.readings.size();
        WeightGoalReadings[] readArr = new WeightGoalReadings[readingsCount];
        dest.writeInt(readingsCount);
        dest.writeParcelableArray(this.readings.toArray(readArr), flags);
        //dest.writeTypedArray(this.readings.toArray(readArr), flags);
    }

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

        @Override
        public int getMax() {
            return new Double(maxWeight).intValue();
        }

        @Override
        public int getMin() {
            return new Double(minWeight).intValue();
        }
    }
}
