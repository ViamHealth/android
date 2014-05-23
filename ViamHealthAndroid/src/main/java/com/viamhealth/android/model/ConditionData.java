package com.viamhealth.android.model;

/**
 * Created by Kunal on 22/5/14.
 */
public class ConditionData {

    private String name;

    private boolean selected = false;

    public ConditionData(String name, boolean selected) {
        super();
        this.name = name;
        this.selected = selected;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

}
