package com.viamhealth.android.model.enums;

import com.viamhealth.android.R;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by naren on 21/11/13.
 */
public enum ReminderType {
    Medicine(2, R.string.medicine, R.string.hint_reminder_medicine_name, R.drawable.selector_icon_medicine),
    DrAppointments(4, R.string.drappointment, R.string.hint_reminder_drappointment_name, R.drawable.selector_icon_dr_appointments),
    LabTests(3, R.string.labtest, R.string.hint_reminder_labTests_name, R.drawable.selector_icon_lab_test),
//    Meals(5, R.string.meals, R.string.hint_reminder_meals_name),
//    Activities(6, R.string.activities, R.string.hint_reminder_activities_name),
    Other(1, R.string.other, R.string.hint_reminder_other_name, R.drawable.selector_icon_lab_test);

    private int value;
    private int resId;
    private int nameHintResId;
    private int iconId;

    ReminderType(int value, int resId, int hintId, int iconId){
        this.value = value;
        this.resId = resId;
        this.nameHintResId = hintId;
        this.iconId = iconId;
    }

    public int hintResId() {return nameHintResId;}
    public int resId() {return resId;}
    public int value() {return value;}
    public Integer iconId() {return iconId>0?iconId:null;}

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
