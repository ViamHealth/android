package com.viamhealth.android.model.goals;

import android.os.Parcel;
import android.os.Parcelable;

import com.viamhealth.android.utils.JsonGraphDataBuilder;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by naren on 20/10/13.
 */
public class CholesterolGoal extends Goal {

    int hdl;
    int ldl;
    int triglycerides;
    int total;

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
    public Goal.HealthyRange getHealthyRange() {
        return this.healthyRange;
    }

    @Override
    public void setHealthyRange(Goal.HealthyRange healthyRange) {
        this.healthyRange = (HealthyRange) healthyRange;
    }

    public CholesterolGoal() {
    }

    public CholesterolGoal(Parcel in) {
        super(in);
        hdl = in.readInt();
        ldl = in.readInt();
        triglycerides = in.readInt();
        total = in.readInt();
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

    public static final Parcelable.Creator<CholesterolGoal> CREATOR = new Parcelable.Creator<CholesterolGoal>() {
        public CholesterolGoal createFromParcel(Parcel in) {
            return new CholesterolGoal(in);
        }

        public CholesterolGoal[] newArray(int size) {
            return new CholesterolGoal[size];
        }
    };

    @Override
    public String toString() {
        return "CholesterolGoal{" +
                "hdl=" + hdl +
                ", ldl=" + ldl +
                ", triglycerides=" + triglycerides +
                ", total=" + total +
                "} " + super.toString();
    }

    @Override
    public JSONObject toJSON(GraphSeries series) {
        JSONObject object = parentJSON();

        try {
            object.put("targetHDL", hdl);
            object.put("targetLDL", ldl);
            object.put("targetTri", triglycerides);
            object.put("targetTotal", total);
            object.put("healthyRange", healthyRange.toJSON(null));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return object;
    }

    public class HealthyRange extends Goal.HealthyRange {
        int minHDL;
        int maxHDL;
        int minLDL;
        int maxLDL;
        int minTG;
        int maxTG;
        int minTotal;
        int maxTotal;

        public HealthyRange() {
        }

        public int getMinHDL() {
            return minHDL;
        }

        public void setMinHDL(int minHDL) {
            this.minHDL = minHDL;
        }

        public int getMaxHDL() {
            return maxHDL;
        }

        public void setMaxHDL(int maxHDL) {
            this.maxHDL = maxHDL;
        }

        public int getMinLDL() {
            return minLDL;
        }

        public void setMinLDL(int minLDL) {
            this.minLDL = minLDL;
        }

        public int getMaxLDL() {
            return maxLDL;
        }

        public void setMaxLDL(int maxLDL) {
            this.maxLDL = maxLDL;
        }

        public int getMinTG() {
            return minTG;
        }

        public void setMinTG(int minTG) {
            this.minTG = minTG;
        }

        public int getMaxTG() {
            return maxTG;
        }

        public void setMaxTG(int maxTG) {
            this.maxTG = maxTG;
        }

        public int getMinTotal() {
            return minTotal;
        }

        public void setMinTotal(int minTotal) {
            this.minTotal = minTotal;
        }

        public int getMaxTotal() {
            return maxTotal;
        }

        public void setMaxTotal(int maxTotal) {
            this.maxTotal = maxTotal;
        }


        @Override
        public String toString() {
            return "HealthyRange{" +
                    "minHDL=" + minHDL +
                    ", maxHDL=" + maxHDL +
                    ", minLDL=" + minLDL +
                    ", maxLDL=" + maxLDL +
                    ", minTG=" + minTG +
                    ", maxTG=" + maxTG +
                    ", minTotal=" + minTotal +
                    ", maxTotal=" + maxTotal +
                    "} " + super.toString();
        }

        @Override
        public JSONObject toJSON(GraphSeries series) {
            JSONObject healthyRangeJSON = new JSONObject();
            JSONObject hdl = new JSONObject();
            JSONObject ldl = new JSONObject();
            JSONObject tg = new JSONObject();
            JSONObject total = new JSONObject();

            try {
                hdl.put("from", minHDL);
                hdl.put("tp", maxHDL);
                ldl.put("from", minLDL);
                ldl.put("to", maxLDL);
                tg.put("from", minTG);
                tg.put("to", maxTG);
                total.put("from", minTotal);
                total.put("to", maxTotal);
                healthyRangeJSON.put("hdl", hdl);
                healthyRangeJSON.put("ldl", ldl);
                healthyRangeJSON.put("tg", tg);
                healthyRangeJSON.put("total", total);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return healthyRangeJSON;
        }
    }
}
