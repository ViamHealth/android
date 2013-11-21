package com.viamhealth.android.model.enums;

import com.viamhealth.android.R;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by naren on 21/11/13.
 */
public enum ReminderType {
    Medicine(2, R.string.medicine),
    DrAppointments(4, R.string.drappointment),
    LabTests(3, R.string.labtest),
    Meals(5, R.string.meals),
    Activities(6, R.string.activities),
    Other(1, R.string.other);

    private int value;
    private int resId;

    ReminderType(int value, int resId){
        this.value = value;
        this.resId = resId;
    }

    public int resId() {return resId;}
    public int value() {return value;}

    private static final Map<Integer, ReminderType> valuelookup = new HashMap<Integer, ReminderType>();

    // Populate the lookup table on loading time
    static {
        for (ReminderType g : EnumSet.allOf(ReminderType.class)){
            valuelookup.put(g.value(), g);
        }
    }

    // This method can be used for reverse lookup purpose
    public static ReminderType get(int value) {
        return valuelookup.get(value);
    }

}
