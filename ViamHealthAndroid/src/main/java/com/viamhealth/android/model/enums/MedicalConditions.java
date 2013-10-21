package com.viamhealth.android.model.enums;

import com.viamhealth.android.R;
import com.viamhealth.android.manager.OrFragmentManager;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by naren on 10/10/13.
 */
public enum MedicalConditions implements OrFragmentManager.FragmentSpinnerElement {
    Obese(1, R.string.medical_conidtion_obese, "weightgoal.html"),
    BloodPressure(2, R.string.medical_conidtion_bp, "bpgoal.html"),
    Diabetes(3, R.string.medical_conidtion_diabetes, "sugargoal.html"),
    Cholesterol(4, R.string.medical_conidtion_cholesterol, "cholesterol.html");

    private final int value;
    private final int key;
    private final String assetName;

    MedicalConditions(int value, int key, String assetName){
        this.value = value;
        this.key = key;
        this.assetName = assetName;
    }


    public int value() {return value;}
    public int key() {return key;}
    public String assetName() {return assetName;}

    // Lookup table
    private static final Map<Integer, MedicalConditions> valuelookup = new HashMap<Integer, MedicalConditions>();
    private static final Map<Integer, MedicalConditions> keylookup = new HashMap<Integer, MedicalConditions>();

    // Populate the lookup table on loading time
    static {
        for (MedicalConditions mc : EnumSet.allOf(MedicalConditions.class)){
            valuelookup.put(mc.value(), mc);
            keylookup.put(mc.key(), mc);
        }
    }

    // This method can be used for reverse lookup purpose
    public static MedicalConditions constructEnumByValue(int value) {
        return valuelookup.get(value);
    }

    public static MedicalConditions constructEnumByKey(int key) {
        return keylookup.get(key);
    }

    @Override
    public Enum getEnum() {
        return this;
    }
}
