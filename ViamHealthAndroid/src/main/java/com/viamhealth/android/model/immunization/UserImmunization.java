package com.viamhealth.android.model.immunization;

import com.viamhealth.android.model.BaseModel;

/**
 * Created by kunal on 26/2/14.
 */
public class UserImmunization extends BaseModel {
    private Long immunization = 0L;
    private Long userId = 0L;
    private boolean completed;

    public Long getImmunization() {
        return immunization;
    }

    public void setImmunization(Long immunization) {
        this.immunization = immunization;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    @Override
    public String toString() {
        return "UserImmunization{" +
                "immunization='" + immunization + '\'' +
                ", userId='" + userId + '\'' +
                ", completed='" + completed + '\'' +
                "} " + super.toString();
    }
}
