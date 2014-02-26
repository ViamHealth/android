package com.viamhealth.android.model.immunization;

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
