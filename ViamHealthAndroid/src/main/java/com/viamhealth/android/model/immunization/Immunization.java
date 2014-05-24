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
        if(dob == null) return "";
        DateTime sDate = new DateTime(dob);
        DateTime today = new DateTime();
        sDate = sDate.plusDays(((int) this.recommendedAge));
        if(sDate.isBefore(today)){
            if(recommendedAge < 30 ){
                if(recommendedAge <= 1){
                    return "Birth";
                }
                else if(recommendedAge < 15) {
                    return String.valueOf(recommendedAge) + " Days";
                } else if(recommendedAge < 22){
                    return "1 Week";
                } else {
                    return "2 Weeks";
                }
            }else {
                return String.valueOf(recommendedAge/30) + " Months";
            }
        } else {
            return "";
        }
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
