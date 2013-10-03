package com.viamhealth.android.model;

import java.util.Date;

/**
 * Created by naren on 03/10/13.
 */
public abstract class BaseModel {

    protected Long id;
    protected Date created;
    protected Date updated;
    protected Long updatedBy;

    protected BaseModel() {
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

    public Long getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(Long updatedBy) {
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
}
