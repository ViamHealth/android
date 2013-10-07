package com.viamhealth.android.model;

/**
 * Created by Administrator on 10/6/13.
 */
public class ReminderReadings {
    String id,type,morning_check,afternoon_check,evening_check,night_check,complete_check;

    public ReminderReadings() {
        super();
    }

    public ReminderReadings(String id,String type,String morning_check,String afternoon_check,String evening_check,String night_check,String complete_check)
    {
        super();
        this.id=id;
        this.type=type;
        this.morning_check=morning_check;
        this.afternoon_check=afternoon_check;
        this.evening_check=evening_check;
        this.night_check=night_check;
        this.complete_check=complete_check;
    }

    public void setId(String id)
    {

    }

    public String getId()
    {
        return this.id;
    }

    public void setType()
    {

    }

    public String getType()
    {
        return this.type;
    }

    public void setMorningCheck(String morning_check)
    {
        this.morning_check=morning_check;
    }

    public String getMorningCheck()
    {
        return this.morning_check;
    }

    public void setAfternoonCheck(String afternoon_check)
    {
        this.afternoon_check=afternoon_check;
    }

    public String getAfternoonCheck()
    {
        return this.afternoon_check;
    }

    public void setEveningCheck(String evening_check)
    {
        this.evening_check=evening_check;
    }

    public String getEveningCheck()
    {
        return this.evening_check;
    }

    public void setNightCheck(String night_check)
    {
        this.night_check=night_check;
    }

    public String getNightCheck()
    {
        return this.night_check;
    }

    public void setCompleteCheck(String complete_check)
    {
        this.complete_check=complete_check;
    }

    public String getCompleteCheck()
    {
        return this.complete_check;
    }

}

