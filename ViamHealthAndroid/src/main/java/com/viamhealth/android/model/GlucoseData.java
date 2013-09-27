package com.viamhealth.android.model;

import java.util.ArrayList;

public class GlucoseData {
	String id,user,random,fasting,target_date,interval_num,interval_unit;
	ArrayList<String> readings = new ArrayList<String>();
	ArrayList<String> healthy_range = new ArrayList<String>();
	public GlucoseData(String id, String user, String random, String fasting,
			String target_date, String interval_num, String interval_unit,
			ArrayList<String> readings, ArrayList<String> healthy_range) {
		super();
		this.id = id;
		this.user = user;
		this.random = random;
		this.fasting = fasting;
		this.target_date = target_date;
		this.interval_num = interval_num;
		this.interval_unit = interval_unit;
		this.readings = readings;
		this.healthy_range = healthy_range;
	}
	
	public GlucoseData() {
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
	public String getRandom() {
		return random;
	}
	public void setRandom(String random) {
		this.random = random;
	}
	public String getFasting() {
		return fasting;
	}
	public void setFasting(String fasting) {
		this.fasting = fasting;
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
