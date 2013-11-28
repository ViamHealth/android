package com.viamhealth.android.model.enums;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by naren on 11/10/13.
 */
public enum LifeStyle {

    Sedentary(1, "Sedentary"),
    LightlyActive(2, "Lightly Active"),
    ModeratelyActive(3, "Moderately Active"),
    VeryActive(4, "Very Active"),
    ExtremelyActive(5, "Extremenly Active");

    private final int value;
    private final String key;

    LifeStyle(int value, String key){
        this.value = value;
        this.key = key;
    }

    public int value() {return value;}
    public String key() {return key;}

    // Lookup table
    private static final Map<Integer, LifeStyle> valuelookup = new HashMap<Integer, LifeStyle>();
    private static final Map<String, LifeStyle> keylookup = new HashMap<String, LifeStyle>();

    // Populate the lookup table on loading time
    static {
        for (LifeStyle bmi : EnumSet.allOf(LifeStyle.class)){
            valuelookup.put(bmi.value(), bmi);
            keylookup.put(bmi.key(), bmi);
        }
    }

    // This method can be used for reverse lookup purpose
    public static LifeStyle get(int value) {
        return valuelookup.get(value);
    }

    public static LifeStyle get(String key) {
        return keylookup.get(key);
    }
}
