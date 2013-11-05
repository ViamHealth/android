package com.viamhealth.android.model.enums;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by naren on 31/10/13.
 */
public enum FoodType {
    None(0, "NONE"),
    BreakFast(1, "BREAKFAST"),
    Lunch(2, "LUNCH"),
    Snacks(3, "SNACKS"),
    Dinner(4, "DINNER");

    private final int value;
    private final String key;

    FoodType(int value, String key){
        this.value = value;
        this.key = key;
    }

    public int value() {return value;}
    public String key() {return key;}

    // Lookup table
    private static final Map<Integer, FoodType> valuelookup = new HashMap<Integer, FoodType>();
    private static final Map<String, FoodType> keylookup = new HashMap<String, FoodType>();

    // Populate the lookup table on loading time
    static {
        for (FoodType g : EnumSet.allOf(FoodType.class)){
            valuelookup.put(g.value(), g);
            keylookup.put(g.key(), g);
        }
    }

    // This method can be used for reverse lookup purpose
    public static FoodType get(int value) {
        return valuelookup.get(value);
    }

    public static FoodType get(String key) {
        return keylookup.get(key);
    }

}
