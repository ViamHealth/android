package com.viamhealth.android.model.goals;

import android.os.Parcel;
import android.os.Parcelable;

import com.viamhealth.android.model.BaseModel;
import com.viamhealth.android.utils.JsonGraphDataBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by naren on 11/10/13.
 */
public abstract class Goal extends BaseModel implements Parcelable, JsonGraphDataBuilder.JsonOutput {

    //List<GoalReadings> readings;
    protected HealthyRange healthyRange;
    protected List<GoalReadings> readings = new ArrayList<GoalReadings>();
    long userId;
    Date targetDate;

    public Goal() {
        super();
    }

    public Goal(Parcel in) {
        super(in);
        this.userId = in.readLong();
        this.targetDate = new Date(in.readLong());
    }

    public abstract HealthyRange getHealthyRange();

    public abstract void setHealthyRange(HealthyRange healthyRange);

    public abstract List<GoalReadings> getReadings();

    public abstract void setReadings(List<GoalReadings> readings);

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        if (userId != null)
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

    public Date getStartDate() {
        if (readings == null)
            return null;

        int count = readings.size();
        Date date = new Date();
        for (int i = 0; i < count; i++) {
            Date rd = readings.get(i).getReadingDate();
            date.setTime(Math.min(rd.getTime(), date.getTime()));
        }
        return date;
    }

    public Date getPresentDate() {
        if (readings == null)
            return null;

        int count = readings.size();
        Date date = new Date();
        for (int i = 0; i < count; i++) {
            Date rd = readings.get(i).getReadingDate();
            date.setTime(Math.max(rd.getTime(), date.getTime()));
        }
        return date;
    }

    @Override
    public String toString() {
        return "Goal{" +
                "userId=" + userId +
                ", targetDate=" + targetDate +
                "} " + super.toString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeLong(this.userId);
        dest.writeLong(this.targetDate == null ? 0 : this.targetDate.getTime());
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

    protected Integer round(Integer num, boolean upper) {
        if (num == null)
            throw new NullPointerException("num cannot be null");
        int multiplier_factor = upper ? 1 : -1;
        Integer round = (num / 5 + multiplier_factor) * 5;
        return round;
    }

    @Override
    public int getMax() {
        int max = healthyRange.getMax();

        int rCount = readings.size();
        for (int i = 0; i < rCount; i++) {
            max = Math.max(readings.get(i).getMax(), max);
            max = round(max, true);
        }
        return max;
    }

    @Override
    public int getMin() {
        int min = healthyRange.getMin();

        int rCount = readings.size();
        for (int i = 0; i < rCount; i++) {
            min = Math.min(readings.get(i).getMin(), min);
            min = round(min, false);
        }
        return min;
    }

    public abstract class HealthyRange implements JsonGraphDataBuilder.JsonOutput {

    }
}
