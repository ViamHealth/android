package com.viamhealth.android.model.goals;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by naren on 20/10/13.
 */
public class CholesterolGoalReading extends GoalReadings {

    public static final Parcelable.Creator<CholesterolGoalReading> CREATOR = new Parcelable.Creator<CholesterolGoalReading>() {
        public CholesterolGoalReading createFromParcel(Parcel in) {
            return new CholesterolGoalReading(in);
        }

        public CholesterolGoalReading[] newArray(int size) {
            return new CholesterolGoalReading[size];
        }
    };
    int hdl;
    int ldl;
    int triglycerides;
    int total;

    public CholesterolGoalReading() {
    }

    public CholesterolGoalReading(Parcel in) {
        super(in);
        hdl = in.readInt();
        ldl = in.readInt();
        triglycerides = in.readInt();
        total = in.readInt();
    }

    public int getHdl() {
        return hdl;
    }

    public void setHdl(int hdl) {
        this.hdl = hdl;
    }

    public int getLdl() {
        return ldl;
    }

    public void setLdl(int ldl) {
        this.ldl = ldl;
    }

    public int getTriglycerides() {
        return triglycerides;
    }

    public void setTriglycerides(int triglycerides) {
        this.triglycerides = triglycerides;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeInt(hdl);
        dest.writeInt(ldl);
        dest.writeInt(triglycerides);
        dest.writeInt(total);
    }

    @Override
    public int describeContents() {
        return super.describeContents();
    }

    @Override
    public String toString() {
        return "CholesterolGoalReading{" +
                "hdl=" + hdl +
                ", ldl=" + ldl +
                ", triglycerides=" + triglycerides +
                "} " + super.toString();
    }

    @Override
    public JSONObject toJSON(GraphSeries series) {
        JSONObject object = parentJSON();

        try {
            if (series == GraphSeries.A)
                object.put("y", this.hdl);
            else if (series == GraphSeries.B)
                object.put("y", this.ldl);
            else if (series == GraphSeries.C)
                object.put("y", this.triglycerides);
            else
                object.put("y", this.total);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return object;
    }

    @Override
    public int getMax() {
        return Math.max(Math.max(Math.max(hdl, ldl), triglycerides), total);
    }

    @Override
    public int getMin() {
        return Math.min(Math.min(Math.min(hdl, ldl), triglycerides), total);
    }
}
