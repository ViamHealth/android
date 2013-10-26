package com.viamhealth.android.model.goals;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by naren on 11/10/13.
 */
public class WeightGoal extends Goal implements Parcelable {
    double weight;

    //List<WeightGoalReadings> readings = new ArrayList<WeightGoalReadings>();

    @Override
    public List<GoalReadings> getReadings() {
        return readings;
    }

    @Override
    public void setReadings(List<GoalReadings> readings) {
        this.readings.clear();
        for(GoalReadings reading : readings){
            this.readings.add((WeightGoalReadings) reading);
        }
    }

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

    public WeightGoal(Parcel in) {
        super(in);
        this.weight = in.readDouble();
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD

        //this.readings = in.readArr();

=======
>>>>>>> f2b4951d1989691d5b80875beac8e81665b29f25
=======
>>>>>>> f2b4951d1989691d5b80875beac8e81665b29f25
=======
>>>>>>> f2b4951d1989691d5b80875beac8e81665b29f25
        int readingsCount = in.readInt();
        WeightGoalReadings[] readArr = new WeightGoalReadings[readingsCount];
        if(readingsCount>0){
            in.readTypedArray(readArr, WeightGoalReadings.CREATOR);
            this.readings = new ArrayList<GoalReadings>(readingsCount);
            for(int i=0; i<readingsCount; i++){
                 this.readings.add(readArr[i]);
            }
        }else{
            this.readings = new ArrayList<GoalReadings>();
        }
    }

    @Override
    public int describeContents() {
        return super.describeContents();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeDouble(this.weight);
        int readingsCount = this.readings==null?0:this.readings.size();
        WeightGoalReadings[] readArr = new WeightGoalReadings[readingsCount];
        dest.writeInt(readingsCount);
        dest.writeTypedArray(this.readings.toArray(readArr), flags);
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
