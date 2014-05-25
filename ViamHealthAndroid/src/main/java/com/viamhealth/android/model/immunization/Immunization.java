package com.viamhealth.android.model.immunization;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by kunal on 21/2/14.
 */
public class Immunization {
    private long id;
    private String label;
    private long recommendedAge;
    private UserImmunization userImmunization;

    public UserImmunization getUserImmunization() {
        return userImmunization;
    }

    public void setUserImmunization(UserImmunization userImmunization) {
        this.userImmunization = userImmunization;
    }

    public long getRecommendedAge() {
        return recommendedAge;
    }

    public void setRecommendedAge(long recommendedAge) {
        this.recommendedAge = recommendedAge;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String scheduleDate(Date dob){
        if(dob == null) return "";
        DateTime sDate = new DateTime(dob);
        DateTime today = new DateTime();
        sDate = sDate.plusDays(((int) this.recommendedAge));
        if(sDate.isAfter(today)){
            DateTimeFormatter formatter = DateTimeFormat.forPattern("dd-MMM-yy").withLocale(Locale.US);
            return formatter.print(sDate);
        } else {
            return "";
        }
    }

    public String scheduleTimeFrame(Date dob){
        String rstring="";
        if(dob == null) return "";
        DateTime sDate = new DateTime(dob);
        DateTime today = new DateTime();
        sDate = sDate.plusDays(((int) this.recommendedAge));

        //if(sDate.isBefore(today)){
            if(recommendedAge < 24 ) {
                if (recommendedAge < 1) {
                    rstring = "At Birth";
                } else {
                    rstring = "Age: " + String.valueOf(recommendedAge) + " Weeks";
                }
            } else {
                rstring = "Age: " + String.valueOf(Math.round(((3*1.0)/13)*recommendedAge)) + " Months";
            }
            /*}else if(recommendedAge < 52){
                rstring=  "Age: " + String.valueOf(recommendedAge/30) + " Month(s)";
            }else {
                rstring = "Age: " + String.valueOf(recommendedAge/(30*12)) + " Year(s)";
            }*/
        //} else {
        //    return "";
        //}
        //if(!sDate.isBefore(today)) System.out.println("KUNAL " + rstring );
        return rstring;
    }

    public int getListItemType(Date dob) {
        int rInt = 0;
        if(dob == null) return rInt;
        DateTime sDate = new DateTime(dob);
        DateTime today = new DateTime();
        sDate = sDate.plusDays(((int) this.recommendedAge));
        if(sDate.isBefore(today)) rInt = 1;
        else rInt = 2;
        return rInt;
    }

    @Override
    public String toString() {
        if(userImmunization != null)
            return "Immunization{" +
                "id='" + id + '\'' +
                ", label='" + label + '\'' +
                ", recommenedAge='" + recommendedAge + '\'' +
                ", userImmunization=" + userImmunization.toString() + '\''+
                "} ";
        else
            return "Immunization{" +
                    "id='" + id + '\'' +
                    ", label='" + label + '\'' +
                    ", recommenedAge='" + recommendedAge + '\'' +
                    ", userImmunization='null'" +
                    "} ";
    }


}
