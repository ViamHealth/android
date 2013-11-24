package com.viamhealth.android.utils;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by naren on 21/11/13.
 */
public class DateUtils {

    public static Date getToday(Date date){
        Calendar c = Calendar.getInstance();
        if(date!=null)
            c.setTime(date);
        c.set(Calendar.HOUR_OF_DAY, 0); //anything 0 - 23
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        return c.getTime(); //the midnight, that's the first second of the day.
    }

    public static String getDisplayText(Date date){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        StringBuilder builder = new StringBuilder()
                .append(cal.get(Calendar.DATE)).append("-")
                .append(cal.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.US)).append("-")
                .append(cal.get(Calendar.YEAR));

        return builder.toString();
    }

    public static boolean hasElapsed(Date date) {
        Date now = new Date();
        if(now.getTime()<=date.getTime()){
            return false;
        }
        return true;
    }
}
