package com.viamhealth.android.model;

public class GoalData {
    String id, gn, gd, gv, dates, val, dates1;

    public GoalData(String id, String gn, String gd, String gv, String dates, String val, String dates1) {
        super();
        this.id = id;
        this.gn = gn;
        this.gd = gd;
        this.gv = gv;
        this.dates = dates;
        this.val = val;
        this.dates1 = dates1;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGn() {
        return gn;
    }

    public void setGn(String gn) {
        this.gn = gn;
    }

    public String getGd() {
        return gd;
    }

    public void setGd(String gd) {
        this.gd = gd;
    }

    public String getGv() {
        return gv;
    }

    public void setGv(String gv) {
        this.gv = gv;
    }

    public String getDates() {
        return dates;
    }

    public void setDates(String dates) {
        this.dates = dates;
    }

    public String getVal() {
        return val;
    }

    public void setVal(String val) {
        this.val = val;
    }

    public String getDates1() {
        return dates1;
    }

    public void setDates1(String dates1) {
        this.dates1 = dates1;
    }

}
