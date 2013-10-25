package com.viamhealth.android.utils;

import android.content.Context;
import android.util.DisplayMetrics;

import java.util.Calendar;

/**
 * Created by naren on 23/10/13.
 */
public class UIUtility {

    public static int dpToPx(Context context, int dp) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int px = Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return px;
    }

    public static Calendar getDate(Calendar cal) {
        Calendar dayCal = Calendar.getInstance();
        dayCal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE), 0, 0, 0);
        dayCal.set(Calendar.MILLISECOND, 0);
        return dayCal;
    }
}
