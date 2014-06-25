package com.viamhealth.android.model.tasks;

import android.os.Parcel;
import android.os.Parcelable;

import com.viamhealth.android.model.enums.TaskAdapterType;
import com.viamhealth.android.model.enums.TaskItemType;

/**
 * Created by Kunal on 17/6/14.
 */
public class BloodPressureTask implements Parcelable, Task {
    String id;
    String message;
    String labelChoice1;
    String labelChoice2;
    Integer setChoice = 0;
    String feedbackMessageChoice1;
    String feedbackMessageChoice2;
    Integer weight = 0;
    final Integer taskType = TaskItemType.BloodPressure.value();
    final Integer adapterType = TaskAdapterType.BloodPressure.value();

    public BloodPressureTask() {
    }

    public Integer getAdapterType() {
        return adapterType;
    }

    public String getLabelChoice1() {
        return labelChoice1;
    }

    public void setLabelChoice1(String labelChoice1) {
        this.labelChoice1 = labelChoice1;
    }

    public String getLabelChoice2() {
        return labelChoice2;
    }

    public void setLabelChoice2(String labelChoice2) {
        this.labelChoice2 = labelChoice2;
    }

    public Integer getSetChoice() {
        return setChoice;
    }

    public void setSetChoice(Integer setChoice) {
        this.setChoice = setChoice;
    }

    public String getFeedbackMessageChoice1() {
        return feedbackMessageChoice1;
    }

    public void setFeedbackMessageChoice1(String feedbackMessageChoice1) {
        this.feedbackMessageChoice1 = feedbackMessageChoice1;
    }

    public String getFeedbackMessageChoice2() {
        return feedbackMessageChoice2;
    }

    public void setFeedbackMessageChoice2(String feedbackMessageChoice2) {
        this.feedbackMessageChoice2 = feedbackMessageChoice2;
    }

    public String getId() {

        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public Integer getTaskType() {
        return taskType;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int i) {
        dest.writeString(this.getId());
        dest.writeString(this.getMessage());
        dest.writeString(this.getLabelChoice1());
        dest.writeString(this.getLabelChoice2());
        dest.writeInt(this.getSetChoice());
        dest.writeString(this.getFeedbackMessageChoice1());
        dest.writeString(this.getFeedbackMessageChoice2());
        dest.writeInt(this.getWeight());
        //dest.writeInt(this.getTaskType());

    }

    public BloodPressureTask(Parcel in) {
        this.id = in.readString();
        this.message = in.readString();
        this.labelChoice1 = in.readString();
        this.labelChoice2 = in.readString();
        this.setChoice = in.readInt();
        this.feedbackMessageChoice1 = in.readString();
        this.feedbackMessageChoice2 = in.readString();
        this.weight = in.readInt();
        //this.taskType = in.readInt();
    }

    public static final Parcelable.Creator<BloodPressureTask> CREATOR
            = new Parcelable.Creator<BloodPressureTask>() {
        public BloodPressureTask createFromParcel(Parcel in) {
            return new BloodPressureTask(in);
        }

        public BloodPressureTask[] newArray(int size) {
            return new BloodPressureTask[size];
        }
    };
}
