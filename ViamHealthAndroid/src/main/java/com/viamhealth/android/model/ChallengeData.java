package com.viamhealth.android.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.viamhealth.android.model.cat.CatData;
import com.viamhealth.android.model.enums.CatAdapterType;
import com.viamhealth.android.model.enums.CatType;
import com.viamhealth.android.model.enums.TaskAdapterType;
import com.viamhealth.android.model.enums.TaskItemType;
import com.viamhealth.android.model.tasks.Task;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Kunal on 14/6/14.
 */
public class ChallengeData implements Task, Parcelable, CatData {
    private String id;
    private String title; //Heading - Title
    private Integer weight = 0;

    private final Integer taskType = TaskItemType.Challenge.value();
    final Integer adapterType = TaskAdapterType.Challenge.value();

    final Integer catAdapterType = CatAdapterType.Challenge.value();
    final Integer catType = CatType.Challlenge.value();

    private String labelChoice1;
    private Integer joinedCount= 2 ;
    private String message; // Explanatory Message
    private String bigMessage;
    private Integer numDays;
    private Integer dayNum = 0; //0 = not accepted
    private List<String> dayWiseValues; //Completed,1,0,number of steps,km of walk etc
    private String dayValueString; //Typ eof value . eg. Steps
    private Integer dayValueType;
    private Date acceptedDate;

    public ChallengeData() {
    }



    @Override
    public Integer getCatType() {
        return this.catType;
    }

    @Override
    public Integer getCatAdapterType() {
        return this.catAdapterType;
    }

    @Override
    public Date getStartDate(){
        DateTime dateTime = new DateTime(this.getAcceptedDate());
        dateTime = dateTime.plusDays(1);
        Date endD = dateTime.toDate();
        return endD;
    }
    @Override
    public Date getEndDate() {
        DateTime dateTime = new DateTime(this.getStartDate());
        dateTime = dateTime.plusDays(this.getNumDays());
        Date endD = dateTime.toDate();
        return endD;
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
        this.numDays = numDaysset;
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

    public String getDayValueString() {
        return dayValueString;
    }

    public void setDayValueString(String dayValueString) {
        this.dayValueString = dayValueString;
    }

    public Integer getDayValueType() {
        return dayValueType;
    }

    public void setDayValueType(Integer dayValueType) {
        this.dayValueType = dayValueType;
    }

    public Date getAcceptedDate() {
        return acceptedDate;
    }

    public void setAcceptedDate(Date acceptedDate) {
        this.acceptedDate = acceptedDate;
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
        dest.writeString(this.getBigMessage());
        dest.writeInt(this.getNumDays());
        dest.writeInt(this.getDayNum());
        dest.writeStringList(this.getDayWiseValues());
        if(this.getDayValueString() != null)
            dest.writeString(this.getDayValueString());
        dest.writeInt(this.getDayValueType());
        if(this.getAcceptedDate() != null )
            dest.writeLong(this.getAcceptedDate().getTime());
        else
            dest.writeLong(0);

    }

    public ChallengeData(Parcel in) {
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
        List<String> stringList = new ArrayList<String>();
        in.readStringList(stringList);
        setDayWiseValues(stringList);
        String dvs = in.readString();
        if ( dvs != null )
            setDayValueString(dvs);
        setDayValueType(in.readInt());
        Long l = in.readLong();
        if( l != 0 )
            setAcceptedDate(new Date(l));
    }

    public static final Parcelable.Creator<ChallengeData> CREATOR
            = new Parcelable.Creator<ChallengeData>() {
        public ChallengeData createFromParcel(Parcel in) {
            return new ChallengeData(in);
        }

        public ChallengeData[] newArray(int size) {
            return new ChallengeData[size];
        }
    };

    @Override
    public String toString() {
        return "ChallengeData{" +
                "id='" + getId() + '\'' +
                ", title='" + getTitle() + '\'' +
                ", labelChoice='" + getLabelChoice1() + '\'' +
                ", joinedCount='" + getJoinedCount() + '\'' +
                ", numDays=" + getNumDays() +
                "} ";
    }

}
