package com.viamhealth.android.model.goals;

import com.viamhealth.android.model.BaseModel;

import java.util.Date;

/**
 * Created by naren on 11/10/13.
 */
public class GoalReadings extends BaseModel {
    Long goalId;
    Date readingDate;
    String comments;

    public Long getGoalId() {
        return goalId;
    }

    public void setGoalId(Long goalId) {
        this.goalId = goalId;
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

    public GoalReadings() {
        super();
    }
}
