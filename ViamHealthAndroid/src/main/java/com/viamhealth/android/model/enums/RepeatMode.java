package com.viamhealth.android.model.enums;

import com.viamhealth.android.R;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by naren on 21/11/13.
 */
public enum RepeatMode {
    None(0, R.string.none),
    Daily(1, R.string.daily),
    Weekly(2, R.string.weekly),
    Monthly(3, R.string.monthly),
    Yearly(4, R.string.yearly),
    Custom(5, R.string.custom);

    private int value;
    private int resId;

    RepeatMode(int value, int resId){
        this.value = value;
        this.resId = resId;
    }

    public int resId() {return resId;}
    public int value() {return value;}

    private static final Map<Integer, RepeatMode> valuelookup = new HashMap<Integer, RepeatMode>();

    // Populate the lookup table on loading time
    static {
        for (RepeatMode g : EnumSet.allOf(RepeatMode.class)){
            valuelookup.put(g.value(), g);
        }
    }

    // This method can be used for reverse lookup purpose
    public static RepeatMode get(int value) {
        return valuelookup.get(value);
    }

}
