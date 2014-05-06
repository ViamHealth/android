package com.viamhealth.android.model.enums;

import com.viamhealth.android.R;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by naren on 07/11/13.
 */
public enum Relation {
    None(0, R.string.select_relation),
    Spouse(1, R.string.spouse),
    Son(2, R.string.son),
    Daughter(3, R.string.daughter),
    Father(4, R.string.father),
    Mother(5, R.string.mother),
    Brother(6, R.string.brother),
    Sister(7, R.string.sister),
    FatherInLaw(8, R.string.father_in_law),
    MotherInLaw(9, R.string.mother_in_law),
    SonInLaw(10, R.string.son_in_law),
    DaughterInLaw(11, R.string.daughter_in_law),
    Cousin(12, R.string.cousin),
    Other(13, R.string.others);
    // Lookup table
    private static final Map<Integer, Relation> valuelookup = new HashMap<Integer, Relation>();

    // Populate the lookup table on loading time
    static {
        for (Relation bg : EnumSet.allOf(Relation.class)) {
            valuelookup.put(bg.value(), bg);
        }
    }

    private final int value;
    private final int resId;

    Relation(int value, int resId) {
        this.value = value;
        this.resId = resId;
    }

    // This method can be used for reverse lookup purpose
    public static Relation get(int value) {
        return valuelookup.get(value);
    }

    public int value() {
        return value;
    }

    public int resId() {
        return resId;
    }

}
