package com.viamhealth.android.model.enums;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by naren on 02/10/13.
 */
public enum Gender {
    Male(1, "MALE"),
    Female(2, "FEMALE");

    private final int value;
    private final String key;

    Gender(int value, String key){
        this.value = value;
        this.key = key;
    }

    public int value() {return value;}
    public String key() {return key;}

    // Lookup table
    private static final Map<Integer, Gender> valuelookup = new HashMap<Integer, Gender>();
    private static final Map<String, Gender> keylookup = new HashMap<String, Gender>();

    // Populate the lookup table on loading time
    static {
        for (Gender g : EnumSet.allOf(Gender.class)){
            valuelookup.put(g.value(), g);
            keylookup.put(g.key(), g);
        }
    }

    // This method can be used for reverse lookup purpose
    public static Gender get(int value) {
        return valuelookup.get(value);
    }

    public static Gender get(String key) {
        return keylookup.get(key);
    }

}
