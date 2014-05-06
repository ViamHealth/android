package com.viamhealth.android.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/**
 * Created by naren on 03/10/13.
 */
public abstract class BaseModel implements Parcelable {

    protected Long id;
    protected Date created;
    protected Date updated;
    protected String updatedBy;

    protected BaseModel() {
    }

    public BaseModel(Parcel in) {
        this.id = in.readLong();
        this.created = (Date) in.readValue(null);
        this.updated = (Date) in.readValue(null);
        this.updatedBy = in.readString();
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
                ", updatedBy=" + updatedBy +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (id == null)
            id = 0L;
        dest.writeLong(id);
        dest.writeValue(created);
        dest.writeValue(updated);
        dest.writeString(updatedBy);
    }
}
