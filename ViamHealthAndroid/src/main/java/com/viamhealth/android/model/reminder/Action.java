package com.viamhealth.android.model.reminder;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by naren on 24/11/13.
 */

public class Action implements Parcelable {

    public static final Parcelable.Creator<Action> CREATOR = new Parcelable.Creator<Action>() {
        public Action createFromParcel(Parcel in) {
            return new Action(in);
        }

        public Action[] newArray(int size) {
            return new Action[size];
        }
    };
    Boolean check;

    public Action() {
    }

    public Action(Parcel in) {
        check = (Boolean) in.readValue(null);
    }

    public Boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(check);
    }

    @Override
    public String toString() {
        return "Action{" +
                "check=" + check +
                "} " + super.toString();
    }
}
