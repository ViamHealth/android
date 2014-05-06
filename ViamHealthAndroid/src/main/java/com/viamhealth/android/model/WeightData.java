package com.viamhealth.android.model;

import java.util.ArrayList;

public class WeightData {
    String id, user, weight, target_date, interval_num, interval_unit;
    ArrayList<String> readings = new ArrayList<String>();
    ArrayList<String> healthy_range = new ArrayList<String>();

    public WeightData(String id, String user, String weight,
                      String target_date, String interval_num, String interval_unit,
                      ArrayList<String> readings, ArrayList<String> healthy_range) {
        super();
        this.id = id;
        this.user = user;
        this.weight = weight;
        this.target_date = target_date;
        this.interval_num = interval_num;
        this.interval_unit = interval_unit;
        this.readings = readings;
        this.healthy_range = healthy_range;
    }

    public WeightData() {
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

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
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
