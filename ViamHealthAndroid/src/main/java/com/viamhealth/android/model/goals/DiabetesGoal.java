package com.viamhealth.android.model.goals;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by naren on 18/10/13.
 */
public class DiabetesGoal extends Goal {

    public static final Parcelable.Creator<DiabetesGoal> CREATOR = new Parcelable.Creator<DiabetesGoal>() {
        public DiabetesGoal createFromParcel(Parcel in) {
            return new DiabetesGoal(in);
        }

        public DiabetesGoal[] newArray(int size) {
            return new DiabetesGoal[size];
        }
    };
    private int fbs;
    private int rbs;

    public DiabetesGoal() {
        super();
    }

    public DiabetesGoal(Parcel in) {
        super(in);
        fbs = in.readInt();
        rbs = in.readInt();
        int readingsCount = in.readInt();
        Parcelable[] readArr = in.readParcelableArray(DiabetesGoalReading.class.getClassLoader());//new DiabetesGoalReading[readingsCount];
        if (readingsCount > 0) {
            //in.readTypedArray(readArr, DiabetesGoalReading.CREATOR);
            this.readings = new ArrayList<GoalReadings>(readingsCount);
            for (int i = 0; i < readingsCount; i++) {
                this.readings.add((DiabetesGoalReading) readArr[i]);
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
    public void setReadings(List<GoalReadings> readings) {
        this.readings.clear();
        for (GoalReadings reading : readings) {
            this.readings.add((DiabetesGoalReading) reading);
        }
    }

    public int getFbs() {
        return fbs;
    }

    public void setFbs(int fbs) {
        this.fbs = fbs;
    }

    public int getRbs() {
        return rbs;
    }

    public void setRbs(int rbs) {
        this.rbs = rbs;
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
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeInt(fbs);
        dest.writeInt(rbs);
        int readingsCount = this.readings == null ? 0 : this.readings.size();
        DiabetesGoalReading[] readArr = new DiabetesGoalReading[readingsCount];
        dest.writeInt(readingsCount);
        dest.writeParcelableArray(this.readings.toArray(readArr), flags);
    }

    @Override
    public int describeContents() {
        return super.describeContents();
    }

    @Override
    public JSONObject toJSON(GraphSeries series) {
        JSONObject object = parentJSON();

        try {
            object.put("targetFBS", fbs);
            object.put("targetRBS", rbs);
            object.put("healthyRange", healthyRange.toJSON(null));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return object;
    }

    @Override
    public String toString() {
        return "DiabetesGoal{" +
                "fbs=" + fbs +
                ", rbs=" + rbs +
                "} " + super.toString();
    }

    public class HealthyRange extends Goal.HealthyRange {

        private int minFBS;
        private int maxFBS;
        private int minRBS;
        private int maxRBS;

        public int getMinFBS() {
            return minFBS;
        }

        public void setMinFBS(int minFBS) {
            this.minFBS = minFBS;
        }

        public int getMaxFBS() {
            return maxFBS;
        }

        public void setMaxFBS(int maxFBS) {
            this.maxFBS = maxFBS;
        }

        public int getMinRBS() {
            return minRBS;
        }

        public void setMinRBS(int minRBS) {
            this.minRBS = minRBS;
        }

        public int getMaxRBS() {
            return maxRBS;
        }

        public void setMaxRBS(int maxRBS) {
            this.maxRBS = maxRBS;
        }

        @Override
        public String toString() {
            return "HealthyRange{" +
                    "minFBS=" + minFBS +
                    ", maxFBS=" + maxFBS +
                    ", minRBS=" + minRBS +
                    ", maxRBS=" + maxRBS +
                    "} " + super.toString();
        }

        @Override
        public JSONObject toJSON(GraphSeries series) {
            JSONObject healthyRangeJSON = new JSONObject();
            JSONObject fbs = new JSONObject();
            JSONObject rbs = new JSONObject();

            try {
                fbs.put("from", minFBS);
                fbs.put("to", maxFBS);
                rbs.put("from", minRBS);
                rbs.put("to", maxRBS);
                healthyRangeJSON.put("fbs", fbs);
                healthyRangeJSON.put("rbs", rbs);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return healthyRangeJSON;
        }

        @Override
        public int getMax() {
            return Math.max(maxFBS, maxRBS);
        }

        @Override
        public int getMin() {
            return Math.min(minFBS, minRBS);
        }
    }
}
