package com.viamhealth.android.model.goals;

import android.os.Parcel;
import android.os.Parcelable;

import com.viamhealth.android.model.BaseModel;
import com.viamhealth.android.utils.JsonGraphDataBuilder;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by naren on 11/10/13.
 */
public abstract class Goal extends BaseModel implements Parcelable, JsonGraphDataBuilder.JsonOutput {

    long userId;
    Date targetDate;
    //List<GoalReadings> readings;
    protected HealthyRange healthyRange;

    public HealthyRange getHealthyRange() {
        return healthyRange;
    }

    public void setHealthyRange(HealthyRange healthyRange) {
        this.healthyRange = healthyRange;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        if(userId != null)
            this.userId = userId;
        else
            this.userId = 0L;
    }

    public Date getTargetDate() {
        return targetDate;
    }

    public void setTargetDate(Date targetDate) {
        this.targetDate = targetDate;
    }

    @Override
    public String toString() {
        return "Goal{" +
                "userId=" + userId +
                ", targetDate=" + targetDate +
                "} " + super.toString();
    }

    public Goal() {
        super();
    }

    public Goal(Parcel in) {
        super(in);
        this.userId = in.readLong();
        this.targetDate = new Date(in.readLong());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeLong(this.userId);
        dest.writeLong(this.targetDate==null?0:this.targetDate.getTime());
    }

    public JSONObject parentJSON() {
        JSONObject object = new JSONObject();
        try {
            object.put("targetDate", targetDate.getTime());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return object;
    }

    public abstract class HealthyRange implements JsonGraphDataBuilder.JsonOutput {

    }
}
