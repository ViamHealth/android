package com.viamhealth.android.utils;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by naren on 21/11/13.
 */
public class DateUtils {

    public static Date getToday(Date date){
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.HOUR_OF_DAY, 0); //anything 0 - 23
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        return c.getTime(); //the midnight, that's the first second of the day.
    }
}
