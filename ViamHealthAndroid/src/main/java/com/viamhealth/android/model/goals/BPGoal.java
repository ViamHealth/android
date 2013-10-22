package com.viamhealth.android.model.goals;

import android.os.Parcel;
import android.os.Parcelable;

import com.viamhealth.android.utils.JsonGraphDataBuilder;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by naren on 20/10/13.
 */
public class BPGoal extends Goal {

    int systolicPressure;
    int diastolicPressure;
    int pulseRate;

    public BPGoal() {

    }

    public BPGoal(Parcel in) {
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
            object.put("targetSP", systolicPressure);
            object.put("targetDP", diastolicPressure);
            object.put("targetPR", pulseRate);
            object.put("healthyRange", healthyRange.toJSON(null));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return object;
    }

    public static final Parcelable.Creator<BPGoal> CREATOR = new Parcelable.Creator<BPGoal>() {
        public BPGoal createFromParcel(Parcel in) {
            return new BPGoal(in);
        }

        public BPGoal[] newArray(int size) {
            return new BPGoal[size];
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
        return "BPGoal{" +
                "systolicPressure=" + systolicPressure +
                ", diastolicPressure=" + diastolicPressure +
                ", pulseRate=" + pulseRate +
                "} " + super.toString();
    }

    public class HealthyRange extends Goal.HealthyRange {
        int minSP;
        int maxSP;
        int minDP;
        int maxDP;
        int minPR;
        int maxPR;

        public int getMinSP() {
            return minSP;
        }

        public void setMinSP(int minSP) {
            this.minSP = minSP;
        }

        public int getMaxSP() {
            return maxSP;
        }

        public void setMaxSP(int maxSP) {
            this.maxSP = maxSP;
        }

        public int getMinDP() {
            return minDP;
        }

        public void setMinDP(int minDP) {
            this.minDP = minDP;
        }

        public int getMaxDP() {
            return maxDP;
        }

        public void setMaxDP(int maxDP) {
            this.maxDP = maxDP;
        }

        public int getMinPR() {
            return minPR;
        }

        public void setMinPR(int minPR) {
            this.minPR = minPR;
        }

        public int getMaxPR() {
            return maxPR;
        }

        public void setMaxPR(int maxPR) {
            this.maxPR = maxPR;
        }

        @Override
        public JSONObject toJSON(GraphSeries series) {
            JSONObject healthyRangeJSON = new JSONObject();
            JSONObject sp = new JSONObject();
            JSONObject dp = new JSONObject();
            JSONObject pr = new JSONObject();

            try {
                sp.put("from", minSP);
                sp.put("to", maxSP);
                dp.put("from", minDP);
                dp.put("to", maxDP);
                pr.put("from", minPR);
                pr.put("to", maxPR);
                healthyRangeJSON.put("sp", sp);
                healthyRangeJSON.put("dp", dp);
                healthyRangeJSON.put("pr", pr);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return healthyRangeJSON;
        }

        @Override
        public String toString() {
            return "HealthyRange{" +
                    "minSP=" + minSP +
                    ", maxSP=" + maxSP +
                    ", minDP=" + minDP +
                    ", maxDP=" + maxDP +
                    ", minPR=" + minPR +
                    ", maxPR=" + maxPR +
                    "} " + super.toString();
        }
    }
}