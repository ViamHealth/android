package com.viamhealth.android.model.enums;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Kunal on 18/6/14.
 */
public enum CatType {
    //AskToEnter(0,"0"),
    HealthStats(1, "1"),
    Challlenge(2, "2");

    private final int value;
    private final String key;

    CatType(int value, String key) {
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
    private static final Map<Integer, CatType> valuelookup = new HashMap<Integer, CatType>();
    private static final Map<String, CatType> keylookup = new HashMap<String, CatType>();

    // Populate the lookup table on loading time
    static {
        for (CatType g : EnumSet.allOf(CatType.class)) {
            valuelookup.put(g.value(), g);
            keylookup.put(g.key(), g);
        }
    }

    // This method can be used for reverse lookup purpose
    public static CatType get(int value) {
        return valuelookup.get(value);
    }

    public static CatType get(String key) {
        return keylookup.get(key);
    }
}
