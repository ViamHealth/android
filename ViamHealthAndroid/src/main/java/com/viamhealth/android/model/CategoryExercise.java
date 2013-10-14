package com.viamhealth.android.model;

/**
 * Created by Administrator on 10/12/13.
 */
public class CategoryExercise {

    String id,name,time,calories;

    public CategoryExercise(String name, String time,
                        String calories) {
        super();
        this.id = id;
        this.name = name;
        this.time = time;
        this.calories = calories;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCalories() {
        return calories;
    }

    public void setCalories(String calories) {
        this.calories = calories;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

}
