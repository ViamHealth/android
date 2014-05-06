package com.viamhealth.android.model;

import java.util.ArrayList;

public class BPData {
    String id, user, systolic, diastolic, pulserate, target_date, interval_num, interval_unit;
    ArrayList<String> readings = new ArrayList<String>();
    ArrayList<String> healthy_range = new ArrayList<String>();

    public BPData(String id, String user, String systolic, String diastolic,
                  String pulserate, String target_date, String interval_num,
                  String interval_unit, ArrayList<String> readings,
                  ArrayList<String> healthy_range) {
        super();
        this.id = id;
        this.user = user;
        this.systolic = systolic;
        this.diastolic = diastolic;
        this.pulserate = pulserate;
        this.target_date = target_date;
        this.interval_num = interval_num;
        this.interval_unit = interval_unit;
        this.readings = readings;
        this.healthy_range = healthy_range;
    }

    public BPData() {
        super();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getSystolic() {
        return systolic;
    }

    public void setSystolic(String systolic) {
        this.systolic = systolic;
    }

    public String getDiastolic() {
        return diastolic;
    }

    public void setDiastolic(String diastolic) {
        this.diastolic = diastolic;
    }

    public String getPulserate() {
        return pulserate;
    }

    public void setPulserate(String pulserate) {
        this.pulserate = pulserate;
    }

    public String getTarget_date() {
        return target_date;
    }

    public void setTarget_date(String target_date) {
        this.target_date = target_date;
    }

    public String getInterval_num() {
        return interval_num;
    }

    public void setInterval_num(String interval_num) {
        this.interval_num = interval_num;
    }

    public String getInterval_unit() {
        return interval_unit;
    }

    public void setInterval_unit(String interval_unit) {
        this.interval_unit = interval_unit;
    }

    public ArrayList<String> getReadings() {
        return readings;
    }

    public void setReadings(ArrayList<String> readings) {
        this.readings = readings;
    }

    public ArrayList<String> getHealthy_range() {
        return healthy_range;
    }

    public void setHealthy_range(ArrayList<String> healthy_range) {
        this.healthy_range = healthy_range;
    }


}
