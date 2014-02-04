package com.viamhealth.android.model;

/**
 * Created by monj on 28/1/14.
 */
public class CheckboxGoalData {
    private String name;

    private boolean selected;

    public CheckboxGoalData(String name, boolean selected) {
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
