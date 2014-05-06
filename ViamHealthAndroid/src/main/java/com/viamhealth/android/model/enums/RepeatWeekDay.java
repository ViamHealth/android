package com.viamhealth.android.model.enums;

import com.viamhealth.android.R;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by naren on 21/11/13.
 */
public enum RepeatWeekDay {
    None(0, R.string.none),
    Sunday(1, R.string.sunday),
    Monday(2, R.string.monday),
    Tuesday(3, R.string.tuesday),
    Wednesday(4, R.string.wednesday),
    Thursday(5, R.string.thursday),
    Friday(6, R.string.friday),
    Saturday(7, R.string.saturday);
    private static final Map<Integer, RepeatWeekDay> valuelookup = new HashMap<Integer, RepeatWeekDay>();

    // Populate the lookup table on loading time
    static {
        for (RepeatWeekDay g : EnumSet.allOf(RepeatWeekDay.class)) {
            valuelookup.put(g.value(), g);
        }
    }

    private int value;
    private int resId;

    RepeatWeekDay(int value, int resId) {
        this.value = value;
        this.resId = resId;
    }

    // This method can be used for reverse lookup purpose
    public static RepeatWeekDay get(int value) {
        return valuelookup.get(value);
    }

    public int resId() {
        return resId;
    }

    public int value() {
        return value;
    }
}
