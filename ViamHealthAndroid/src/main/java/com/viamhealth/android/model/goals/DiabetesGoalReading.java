package com.viamhealth.android.model.goals;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by naren on 18/10/13.
 */
public class DiabetesGoalReading extends GoalReadings {

    public static final Parcelable.Creator<DiabetesGoalReading> CREATOR = new Parcelable.Creator<DiabetesGoalReading>() {
        public DiabetesGoalReading createFromParcel(Parcel in) {
            return new DiabetesGoalReading(in);
        }

        public DiabetesGoalReading[] newArray(int size) {
            return new DiabetesGoalReading[size];
        }
    };
    private int fbs;
    private int rbs;

    public DiabetesGoalReading() {
    }

    public DiabetesGoalReading(Parcel in) {
        super(in);
        fbs = in.readInt();
        rbs = in.readInt();
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
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeInt(fbs);
        dest.writeInt(rbs);
    }

    @Override
    public int describeContents() {
        return super.describeContents();
    }

    @Override
    public String toString() {
        return "DiabetesGoalReading{" +
                "fbs=" + fbs +
                ", rbs=" + rbs +
                "} " + super.toString();
    }

    @Override
    public JSONObject toJSON(GraphSeries series) {
        JSONObject object = parentJSON();

        try {
            if (series == GraphSeries.A && fbs > 0)
                object.put("y", this.fbs);
            else if (series == GraphSeries.B && rbs > 0)
                object.put("y", this.rbs);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return object;
    }

    @Override
    public int getMax() {
        return Math.max(fbs, rbs);
    }

    @Override
    public int getMin() {
        return Math.min(fbs, rbs);
    }
}
