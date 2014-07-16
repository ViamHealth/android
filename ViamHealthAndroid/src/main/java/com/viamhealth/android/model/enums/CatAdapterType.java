package com.viamhealth.android.model.enums;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Kunal on 18/6/14.
 */
public enum CatAdapterType {
    HealthStats(0, "0"),
    Challenge(1, "1");

    private final int value;
    private final String key;

    CatAdapterType(int value, String key) {
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
    private static final Map<Integer, CatAdapterType> valuelookup = new HashMap<Integer, CatAdapterType>();
    private static final Map<String, CatAdapterType> keylookup = new HashMap<String, CatAdapterType>();

    // Populate the lookup table on loading time
    static {
        for (CatAdapterType g : EnumSet.allOf(CatAdapterType.class)) {
            valuelookup.put(g.value(), g);
            keylookup.put(g.key(), g);
        }
    }

    // This method can be used for reverse lookup purpose
    public static CatAdapterType get(int value) {
        return valuelookup.get(value);
    }

    public static CatAdapterType get(String key) {
        return keylookup.get(key);
    }

}
