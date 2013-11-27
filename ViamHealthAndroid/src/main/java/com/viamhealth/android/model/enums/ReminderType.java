package com.viamhealth.android.model.enums;

import com.viamhealth.android.R;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by naren on 21/11/13.
 */
public enum ReminderType {
    Medicine(2, R.string.medicine, R.string.hint_reminder_medicine_name),
    DrAppointments(4, R.string.drappointment, R.string.hint_reminder_drappointment_name),
    LabTests(3, R.string.labtest, R.string.hint_reminder_labTests_name),
//    Meals(5, R.string.meals, R.string.hint_reminder_meals_name),
//    Activities(6, R.string.activities, R.string.hint_reminder_activities_name),
    Other(1, R.string.other, R.string.hint_reminder_other_name);

    private int value;
    private int resId;
    private int nameHintResId;

    ReminderType(int value, int resId, int hintId){
        this.value = value;
        this.resId = resId;
        this.nameHintResId = hintId;
    }

    public int hintResId() {return nameHintResId;}
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
