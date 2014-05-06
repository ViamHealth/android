package com.viamhealth.android.model.reminder;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import com.viamhealth.android.model.enums.ReminderTime;
import com.viamhealth.android.model.enums.ReminderType;
import com.viamhealth.android.model.enums.RepeatMode;
import com.viamhealth.android.model.enums.RepeatWeekDay;
import com.viamhealth.android.utils.DateUtils;
import com.viamhealth.android.utils.ParcelableUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by naren on 21/11/13.
 */
public class Reminder implements Parcelable {

    public static final Parcelable.Creator<Reminder> CREATOR = new Parcelable.Creator<Reminder>() {
        public Reminder createFromParcel(Parcel in) {
            return new Reminder(in);
        }

        public Reminder[] newArray(int size) {
            return new Reminder[size];
        }
    };
    Long id = 0L;
    Long userId = 0L;
    ReminderType type = ReminderType.Other;
    String name;
    String details;
    Map<Integer, ReminderTimeData> mapReminderTimeData = new HashMap<Integer, ReminderTimeData>();
    Date startDate;
    Date endDate;
    RepeatMode repeatMode = RepeatMode.None;
    Integer repeatDay = 0;
    Integer repeatHour = 0;
    Integer repeatMin = 0;
    Integer repeatEveryX = 0;
    Integer repeatICounter = 0;
    RepeatWeekDay repeatWeekDay = RepeatWeekDay.None;

    public Reminder() {
    }

    public Reminder(Parcel in) {
        id = in.readLong();
        userId = in.readLong();
        type = ReminderType.get(in.readInt());
        name = in.readString();
        details = in.readString();
        startDate = (Date) in.readValue(null);
        endDate = (Date) in.readValue(null);
        repeatMode = RepeatMode.get(in.readInt());
        repeatDay = in.readInt();
        repeatHour = in.readInt();
        repeatMin = in.readInt();
        repeatEveryX = in.readInt();
        repeatICounter = in.readInt();
        repeatWeekDay = RepeatWeekDay.get(in.readInt());
        mapReminderTimeData = ParcelableUtils.readMap(in, ReminderTimeData.class);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public ReminderType getType() {
        return type;
    }

    public void setType(ReminderType type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public RepeatMode getRepeatMode() {
        return repeatMode;
    }

    public void setRepeatMode(RepeatMode repeatMode) {
        this.repeatMode = repeatMode;
    }

    public Integer getRepeatDay() {
        return repeatDay;
    }

    public void setRepeatDay(Integer repeatDay) {
        this.repeatDay = repeatDay;
    }

    public Integer getRepeatHour() {
        return repeatHour;
    }

    public void setRepeatHour(Integer repeatHour) {
        this.repeatHour = repeatHour;
    }

    public Integer getRepeatMin() {
        return repeatMin;
    }

    public void setRepeatMin(Integer repeatMin) {
        this.repeatMin = repeatMin;
    }

    public Integer getRepeatEveryX() {
        return repeatEveryX;
    }

    public void setRepeatEveryX(Integer repeatEveryX) {
        this.repeatEveryX = repeatEveryX;
    }

    public Integer getRepeatICounter() {
        return repeatICounter;
    }

    public void setRepeatICounter(Integer repeatICounter) {
        this.repeatICounter = repeatICounter;
    }

    public RepeatWeekDay getRepeatWeekDay() {
        return repeatWeekDay;
    }

    public void setRepeatWeekDay(RepeatWeekDay repeatWeekDay) {
        this.repeatWeekDay = repeatWeekDay;
    }

    public void putReminderTimeData(ReminderTime time, ReminderTimeData data) {
        mapReminderTimeData.put(time.ordinal(), data);
    }

    public ReminderTimeData getReminderTimeData(ReminderTime time) {
        return mapReminderTimeData.get(time.ordinal());
    }

    public String getRepeatString(Context context) {

        if (startDate == null || repeatMode == RepeatMode.None || repeatEveryX < 1)
            return "";

        //once every <2> <week> from <stDate>, <x> times
        StringBuilder builder = new StringBuilder();
        builder = builder.append("once every ");

        if (repeatEveryX > 1)
            builder = builder.append(repeatEveryX).append(" ");

        builder = builder.append(context.getString(repeatMode.resId())).append(repeatEveryX > 1 ? "s " : " ")
                .append("from").append(" ")
                .append(DateUtils.getDisplayText(startDate)).append(", ")
                .append(repeatICounter).append(" times");

        return builder.toString();
    }

    @Override
    public String toString() {
        return "Reminder{" +
                "id=" + id +
                ", userId=" + userId +
                ", type=" + type +
                ", name='" + name + '\'' +
                ", details='" + details + '\'' +
                ", mapReminderTimeData=" + mapReminderTimeData +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", repeatMode=" + repeatMode +
                ", repeatDay=" + repeatDay +
                ", repeatHour=" + repeatHour +
                ", repeatMin=" + repeatMin +
                ", repeatEveryX=" + repeatEveryX +
                ", repeatICounter=" + repeatICounter +
                ", repeatWeekDay=" + repeatWeekDay +
                "} " + super.toString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeLong(userId);
        dest.writeInt(type.value());
        dest.writeString(name);
        dest.writeString(details);
        dest.writeValue(startDate);
        dest.writeValue(endDate);
        dest.writeInt(repeatMode.value());
        dest.writeInt(repeatDay);
        dest.writeInt(repeatHour);
        dest.writeInt(repeatMin);
        dest.writeInt(repeatEveryX);
        dest.writeInt(repeatICounter);
        dest.writeInt(repeatWeekDay.value());
        ParcelableUtils.writeMap(mapReminderTimeData, dest);
    }
}
