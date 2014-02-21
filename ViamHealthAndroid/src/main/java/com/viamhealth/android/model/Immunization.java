package com.viamhealth.android.model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by kunal on 21/2/14.
 */
public class Immunization {
    private long id;
    private String title;
    private Long recommendedAge;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getRecommendedAge() {
        return recommendedAge;
    }

    public void setRecommendedAge(Long recommendedAge) {
        this.recommendedAge = recommendedAge;
    }

}
