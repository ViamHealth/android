package com.viamhealth.android.model;

public class ReminderData {
    String id, remindername, time;

    public ReminderData(String id, String remindername, String time) {
        super();
        this.id = id;
        this.remindername = remindername;
        this.time = time;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRemindername() {
        return remindername;
    }

    public void setRemindername(String remindername) {
        this.remindername = remindername;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

}
