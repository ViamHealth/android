package com.viamhealth.android.model.tasks;

import android.os.Parcel;
import android.os.Parcelable;

import com.viamhealth.android.model.enums.TaskAdapterType;
import com.viamhealth.android.model.enums.TaskItemType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kunal on 14/6/14.
 */
public class ChallengeTask implements Task, Parcelable {
    private String id;
    private String title; //Heading - Title
    private Integer weight = 0;
    private final Integer taskType = TaskItemType.Challenge.value();
    final Integer adapterType = TaskAdapterType.Challenge.value();

    private String labelChoice1;
    private Integer joinedCount= 2 ;
    private String message; // Explanatory Message
    private String bigMessage;
    private Integer numDays;
    private Integer dayNum = 0; //0 = not accepted
    private List<String> dayWiseValues; //Completed,1,0,number of steps,km of walk etc

    public ChallengeTask() {
    }

    public Integer getAdapterType() {
        return adapterType;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public String getLabelChoice1() {
        return labelChoice1;
    }

    public void setLabelChoice1(String labelChoice1) {
        this.labelChoice1 = labelChoice1;
    }

    public Integer getJoinedCount() {
        return joinedCount;
    }

    public void setJoinedCount(Integer joinedCount) {
        this.joinedCount = joinedCount;
    }

    public String getBigMessage() {
        return bigMessage;
    }

    public void setBigMessage(String bigMessage) {
        this.bigMessage = bigMessage;
    }

    public Integer getNumDays() {
        return numDays;
    }

    public void setNumDays(Integer numDaysset) {
        this.numDays = numDays;
    }

    public Integer getDayNum() {
        return dayNum;
    }

    public void setDayNum(Integer dayNum) {
        this.dayNum = dayNum;
    }

    public List<String> getDayWiseValues() {
        return dayWiseValues;
    }

    public void setDayWiseValues(List<String> dayWiseValues) {
        this.dayWiseValues = dayWiseValues;
    }



    @Override
    public int describeContents() {
        return 0;
    }


    @Override
    public void writeToParcel(Parcel dest, int i) {
        dest.writeString(this.getId());
        dest.writeString(this.getTitle());
        dest.writeString(this.getLabelChoice1());
        dest.writeInt(this.getWeight());
        //dest.writeInt(this.getTaskType());
        dest.writeInt(this.getJoinedCount());
        dest.writeString(this.getMessage());
        dest.writeString(getBigMessage());
        dest.writeInt(getNumDays());
        dest.writeInt(getDayNum());
        dest.writeStringList(getDayWiseValues());

    }

    public ChallengeTask(Parcel in) {
        setId(in.readString());
        setTitle(in.readString());
        setLabelChoice1(in.readString());
        setWeight(in.readInt());
        //this.taskType = in.readInt();
        setJoinedCount(in.readInt());
        setMessage(in.readString());
        setBigMessage(in.readString());
        setNumDays(in.readInt());
        setDayNum(in.readInt());
        setDayWiseValues(in.readArrayList(String.class.getClassLoader()));

    }

    public static final Parcelable.Creator<ChallengeTask> CREATOR
            = new Parcelable.Creator<ChallengeTask>() {
        public ChallengeTask createFromParcel(Parcel in) {
            return new ChallengeTask(in);
        }

        public ChallengeTask[] newArray(int size) {
            return new ChallengeTask[size];
        }
    };
}
