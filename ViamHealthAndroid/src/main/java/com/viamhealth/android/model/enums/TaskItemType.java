package com.viamhealth.android.model.enums;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Kunal on 13/6/14.
 */
public enum TaskItemType {
    SimpleText(0, "0"),
    BloodPressure(1, "1"),
    Challenge(2, "0");

    private final int value;
    private final String key;

    TaskItemType(int value, String key){
        this.value = value;
        this.key = key;
    }

    public int value() {return value;}
    public String key() {return key;}

    // Lookup table
    private static final Map<Integer, TaskItemType> valuelookup = new HashMap<Integer, TaskItemType>();
    private static final Map<String, TaskItemType> keylookup = new HashMap<String, TaskItemType>();

    // Populate the lookup table on loading time
    static {
        for (TaskItemType g : EnumSet.allOf(TaskItemType.class)){
            valuelookup.put(g.value(), g);
            keylookup.put(g.key(), g);
        }
    }

    // This method can be used for reverse lookup purpose
    public static TaskItemType get(int value) {
        return valuelookup.get(value);
    }

    public static TaskItemType get(String key) {
        return keylookup.get(key);
    }

}
