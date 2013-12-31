package com.viamhealth.android.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.viamhealth.android.provider.ScheduleContract;

import java.util.Date;

/**
 * Created by naren on 03/10/13.
 */
public abstract class BaseModel implements Parcelable {

    protected Long id;
    protected Date created;
    protected Date updated;
    protected String updatedBy;
    protected Date pulledOn;
    protected Date pushedOn;
    protected ScheduleContract.SyncStatus syncStatus = ScheduleContract.SyncStatus.None;
    protected boolean isDeleted;

    protected BaseModel() {
    }


    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public ScheduleContract.SyncStatus getSyncStatus() {
        return syncStatus;
    }

    public Date getPulledOn() {
        return pulledOn;
    }

    public void setPulledOn(Date pulledOn) {
        this.pulledOn = pulledOn;
    }

    public Date getPushedOn() {
        return pushedOn;
    }

    public void setPushedOn(Date pushedOn) {
        this.pushedOn = pushedOn;
    }

    public void setSyncStatus(ScheduleContract.SyncStatus syncStatus) {
        this.syncStatus = syncStatus;
    }

    public void setSyncStatus(int syncStatus) {
        this.syncStatus = ScheduleContract.SyncStatus.values()[syncStatus];
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    @Override
    public String toString() {
        return "BaseModel{" +
                "id=" + id +
                ", created=" + created +
                ", updated=" + updated +
                ", updatedBy='" + updatedBy + '\'' +
                ", pulledOn=" + pulledOn +
                ", pushedOn=" + pushedOn +
                ", syncStatus=" + syncStatus +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if(id==null)
            id=0L;
        dest.writeLong(id);
        dest.writeValue(isDeleted);
        dest.writeValue(created);
        dest.writeValue(updated);
        dest.writeString(updatedBy);
        dest.writeInt(syncStatus.ordinal());
        dest.writeValue(pulledOn);
        dest.writeValue(pushedOn);
    }

    public BaseModel(Parcel in) {
        this.id = in.readLong();
        this.isDeleted = (Boolean) in.readValue(null);
        this.created = (Date) in.readValue(null);
        this.updated = (Date) in.readValue(null);
        this.updatedBy = in.readString();
        this.syncStatus = ScheduleContract.SyncStatus.values()[in.readInt()];
        this.pulledOn = (Date) in.readValue(null);
        this.pushedOn = (Date) in.readValue(null);
    }
}
