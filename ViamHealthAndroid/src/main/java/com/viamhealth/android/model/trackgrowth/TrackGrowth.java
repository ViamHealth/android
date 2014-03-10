package com.viamhealth.android.model.trackgrowth;

import com.viamhealth.android.model.BaseModel;

/**
 * Created by kunal on 10/3/14.
 */
public abstract class TrackGrowth extends BaseModel {

    private Float height = 0F;
    private Float weight = 0F;
    private Long age = 0L;

    public long getAge() {
        return age;
    }

    public void setAge(Long age) {
        this.age = age;
    }

    public Float getHeight() {
        return height;
    }

    public void setHeight(Float height) {
        this.height = height;
    }

    public Float getWeight() {
        return weight;
    }

    public void setWeight(Float weight) {
        this.weight = weight;
    }

}
