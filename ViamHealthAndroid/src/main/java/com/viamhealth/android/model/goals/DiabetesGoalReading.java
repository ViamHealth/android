package com.viamhealth.android.model.goals;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by naren on 18/10/13.
 */
public class DiabetesGoalReading extends GoalReadings {

    private int fbs;
    private int rbs;

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

    public DiabetesGoalReading() {
    }

    public DiabetesGoalReading(Parcel in) {
        super(in);
        fbs = in.readInt();
        rbs = in.readInt();
    }

    public static final Parcelable.Creator<DiabetesGoalReading> CREATOR = new Parcelable.Creator<DiabetesGoalReading>() {
        public DiabetesGoalReading createFromParcel(Parcel in) {
            return new DiabetesGoalReading(in);
        }

        public DiabetesGoalReading[] newArray(int size) {
            return new DiabetesGoalReading[size];
        }
    };

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
            if(series == GraphSeries.A)
                object.put("y", this.fbs);
            else
                object.put("y", this.rbs);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return object;
    }
}
