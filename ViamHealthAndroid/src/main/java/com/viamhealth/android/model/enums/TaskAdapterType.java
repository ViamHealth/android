package com.viamhealth.android.model.enums;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Kunal on 17/6/14.
 */
public enum TaskAdapterType {
    SimpleText(0, "0"),
    BloodPressure(1, "1"),
    Challenge(2, "2");

    private final int value;
    private final String key;

    TaskAdapterType(int value, String key) {
        this.value = value;
        this.key = key;
    }

    public int value() {
        return value;
    }

    public String key() {
        return key;
    }

    // Lookup table
    private static final Map<Integer, TaskAdapterType> valuelookup = new HashMap<Integer, TaskAdapterType>();
    private static final Map<String, TaskAdapterType> keylookup = new HashMap<String, TaskAdapterType>();

    // Populate the lookup table on loading time
    static {
        for (TaskAdapterType g : EnumSet.allOf(TaskAdapterType.class)) {
            valuelookup.put(g.value(), g);
            keylookup.put(g.key(), g);
        }
    }

    // This method can be used for reverse lookup purpose
    public static TaskAdapterType get(int value) {
        return valuelookup.get(value);
    }

    public static TaskAdapterType get(String key) {
        return keylookup.get(key);
    }

}
