package com.viamhealth.android.model.enums;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by naren on 02/10/13.
 */
public enum BloodGroup {
    O_Positive(7, "O +ve"),
    O_Negative(8, "O -ve"),
    A_Positive(1, "A +ve"),
    A_Negative(2, "A -ve"),
    B_Positive(3, "B +ve"),
    B_Negative(4, "B -ve"),
    AB_Positive(5, "AB +ve"),
    AB_Negative(6, "AB -ve");

    private final int value;
    private final String viewStr;
    BloodGroup(int value, String viewStr){
        this.value = value;
        this.viewStr = viewStr;
    }

    public int value() {return value;}
    public String viewStr() {return viewStr;}

    // Lookup table
    private static final Map<Integer, BloodGroup> valuelookup = new HashMap<Integer, BloodGroup>();
    private static final Map<String, BloodGroup> strlookup = new HashMap<String, BloodGroup>();

    // Populate the lookup table on loading time
    static {
        for (BloodGroup bg : EnumSet.allOf(BloodGroup.class)){
            valuelookup.put(bg.value(), bg);
            strlookup.put(bg.viewStr(), bg);
        }
    }

    public static BloodGroup get(String viewStr){
        return strlookup.get(viewStr);
    }

    // This method can be used for reverse lookup purpose
    public static BloodGroup get(int value) {
        return valuelookup.get(value);
    }

}
