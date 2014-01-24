package com.viamhealth.android.model.cards;

import android.os.Parcelable;

import com.viamhealth.android.model.BaseModel;
import com.viamhealth.android.model.enums.PriorityCardType;

/**
 * Created by kunal on 10/1/14.
 */
public abstract class PriorityCard  {

    private String id;
    private String message;

    private String user;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getMessage() {

        return message;
    }

    public abstract String getCustomData(String type);

    public abstract void setCustomData(String v, String value);

    public  abstract PriorityCardType getType();

}
