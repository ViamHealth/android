package com.viamhealth.android.model.enums;

import com.viamhealth.android.R;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by naren on 26/10/13.
 */
public enum JournalType {
    Breakfast(1, R.string.medical_conidtion_obese, "weightgoal.html"),
    Lunch(2, R.string.medical_conidtion_bp, "bpgoal.html"),
    Snacks(3, R.string.medical_conidtion_diabetes, "sugargoal.html"),
    Dinner(4, R.string.medical_conidtion_cholesterol, "cholesterol.html"),
    Exercise(5, R.string.medical_conidtion_cholesterol, "cholesterol.html");;

    private final int value;
    private final int key;
    private final String assetName;

    JournalType(int value, int key, String assetName){
        this.value = value;
        this.key = key;
        this.assetName = assetName;
    }


    public int value() {return value;}
    public int key() {return key;}
    public String assetName() {return assetName;}

    // Lookup table
    private static final Map<Integer, JournalType> valuelookup = new HashMap<Integer, JournalType>();
    private static final Map<Integer, JournalType> keylookup = new HashMap<Integer, JournalType>();

    // Populate the lookup table on loading time
    static {
        for (JournalType mc : EnumSet.allOf(JournalType.class)){
            valuelookup.put(mc.value(), mc);
            keylookup.put(mc.key(), mc);
        }
    }

    // This method can be used for reverse lookup purpose
    public static JournalType constructEnumByValue(int value) {
        return valuelookup.get(value);
    }

    public static JournalType constructEnumByKey(int key) {
        return keylookup.get(key);
    }

}
