package com.viamhealth.android.model.goals;

import android.os.Parcel;
import android.os.Parcelable;

import com.viamhealth.android.model.BaseModel;
import com.viamhealth.android.utils.JsonGraphDataBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

/**
 * Created by naren on 11/10/13.
 */
public abstract class GoalReadings extends BaseModel implements Parcelable, JsonGraphDataBuilder.JsonOutput {

    long goalId;
    Date readingDate;
    String comments;

    private Boolean isToUpdate = false;

    public GoalReadings() {
        super();
    }

    public GoalReadings(Parcel in) {
        super(in);
        this.goalId = in.readLong();
        this.readingDate = new Date(in.readLong());
        this.comments = in.readString();
        this.isToUpdate = (Boolean) in.readValue(null);
    }

    public boolean isToUpdate() {
        return isToUpdate;
    }

    public void setIsToUpdate(boolean doUpdate) {
        this.isToUpdate = doUpdate;
    }

    public Long getGoalId() {
        return goalId;
    }

    public void setGoalId(Long goalId) {
        if (goalId != null)
            this.goalId = goalId;
        else
            this.goalId = 0L;
    }

    public Date getReadingDate() {
        return readingDate;
    }

    public void setReadingDate(Date readingDate) {
        this.readingDate = readingDate;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    @Override
    public String toString() {
        return "GoalReadings{" +
                "goalId=" + goalId +
                ", readingDate=" + readingDate +
                ", comments='" + comments + '\'' +
                "} " + super.toString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeLong(this.goalId);
        dest.writeLong(this.readingDate == null ? 0 : this.readingDate.getTime());
        dest.writeString(this.comments);
        dest.writeValue(isToUpdate);
    }

    protected JSONObject parentJSON() {
        JSONObject object = new JSONObject();
        try {
            object.put("x", readingDate.getTime());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return object;
    }

}
