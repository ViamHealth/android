package com.viamhealth.android.model;

import java.util.ArrayList;

public class CholesterolData {
    String id, user, hdl, ldl, triglycerides, total_cholesterol, target_date, interval_num, interval_unit;
    ArrayList<String> readings = new ArrayList<String>();
    ArrayList<String> healthy_range = new ArrayList<String>();


    public CholesterolData(String id, String user, String hdl, String ldl,
                           String triglycerides, String total_cholesterol, String target_date,
                           String interval_num, String interval_unit,
                           ArrayList<String> readings, ArrayList<String> healthy_range) {
        super();
        this.id = id;
        this.user = user;
        this.hdl = hdl;
        this.ldl = ldl;
        this.triglycerides = triglycerides;
        this.total_cholesterol = total_cholesterol;
        this.target_date = target_date;
        this.interval_num = interval_num;
        this.interval_unit = interval_unit;
        this.readings = readings;
        this.healthy_range = healthy_range;
    }

    public CholesterolData() {
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

    public String getHdl() {
        return hdl;
    }

    public void setHdl(String hdl) {
        this.hdl = hdl;
    }

    public String getLdl() {
        return ldl;
    }

    public void setLdl(String ldl) {
        this.ldl = ldl;
    }

    public String getTriglycerides() {
        return triglycerides;
    }

    public void setTriglycerides(String triglycerides) {
        this.triglycerides = triglycerides;
    }

    public String getTotal_cholesterol() {
        return total_cholesterol;
    }

    public void setTotal_cholesterol(String total_cholesterol) {
        this.total_cholesterol = total_cholesterol;
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
