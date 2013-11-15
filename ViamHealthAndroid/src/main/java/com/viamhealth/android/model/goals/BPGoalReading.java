package com.viamhealth.android.model.goals;

import android.os.Parcel;
import android.os.Parcelable;

import com.viamhealth.android.utils.JsonGraphDataBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by naren on 20/10/13.
 */
public class BPGoalReading extends GoalReadings {

    int systolicPressure;
    int diastolicPressure;
    int pulseRate;

    public BPGoalReading() {
    }

    public BPGoalReading(Parcel in) {
        super(in);
        systolicPressure = in.readInt();
        diastolicPressure = in.readInt();
        pulseRate = in.readInt();
    }

    public int getSystolicPressure() {
        return systolicPressure;
    }

    public void setSystolicPressure(int systolicPressure) {
        this.systolicPressure = systolicPressure;
    }

    public int getDiastolicPressure() {
        return diastolicPressure;
    }

    public void setDiastolicPressure(int diastolicPressure) {
        this.diastolicPressure = diastolicPressure;
    }

    public int getPulseRate() {
        return pulseRate;
    }

    public void setPulseRate(int pulseRate) {
        this.pulseRate = pulseRate;
    }

    @Override
    public JSONObject toJSON(GraphSeries series) {
        JSONObject object = parentJSON();

        try {
            if(series == GraphSeries.A)
                object.put("y", this.systolicPressure);
            else if(series == GraphSeries.B)
                object.put("y", this.diastolicPressure);
            else
                object.put("y", this.pulseRate);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return object;
    }

    @Override
    public int getMax() {
        return Math.max(systolicPressure, diastolicPressure);
    }

    @Override
    public int getMin() {
        return Math.min(systolicPressure, diastolicPressure);
    }

    public static final Parcelable.Creator<BPGoalReading> CREATOR = new Parcelable.Creator<BPGoalReading>() {
        public BPGoalReading createFromParcel(Parcel in) {
            return new BPGoalReading(in);
        }

        public BPGoalReading[] newArray(int size) {
            return new BPGoalReading[size];
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
        return "BPGoalReading{" +
                "systolicPressure=" + systolicPressure +
                ", diastolicPressure=" + diastolicPressure +
                ", pulseRate=" + pulseRate +
                "} " + super.toString();
    }
}
