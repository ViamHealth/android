package com.viamhealth.android.model.goals;

import com.viamhealth.android.model.BaseModel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by naren on 11/10/13.
 */
public class Goal extends BaseModel {

    Long userId;
    Date targetDate;
    List<GoalReadings> readings;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Date getTargetDate() {
        return targetDate;
    }

    public void setTargetDate(Date targetDate) {
        this.targetDate = targetDate;
    }

    public List<GoalReadings> getReadings() {
        return readings;
    }

    public void setReadings(List<GoalReadings> readings) {
        this.readings = readings;
    }

    @Override
    public String toString() {
        return "Goal{" +
                "userId=" + userId +
                ", targetDate=" + targetDate +
                ", readings=" + readings +
                "} " + super.toString();
    }

    public Goal() {
        super();
        readings = new ArrayList<GoalReadings>();
    }
}
