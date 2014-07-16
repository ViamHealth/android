package com.viamhealth.android.notification;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Parcel;

import com.viamhealth.android.Global_Application;
import com.viamhealth.android.model.enums.ReminderTime;
import com.viamhealth.android.model.enums.ReminderType;
import com.viamhealth.android.model.reminder.ReminderReading;
import com.viamhealth.android.model.users.User;
import com.viamhealth.android.services.NotifyMedicineThreeAfternoon;
import com.viamhealth.android.services.NotifyMedicineThreeMorning;
import com.viamhealth.android.services.NotifyMedicineThreeNight;
import com.viamhealth.android.services.NotifyMedicineTodayAfternoon;
import com.viamhealth.android.services.NotifyMedicineTodayMorning;
import com.viamhealth.android.services.NotifyMedicineTodayNight;
import com.viamhealth.android.services.NotifyMedicineTomorrowAfternoon;
import com.viamhealth.android.services.NotifyMedicineTomorrowMorning;
import com.viamhealth.android.services.NotifyMedicineTomorrowNight;
import com.viamhealth.android.services.NotifyMedicineTwoAfternoon;
import com.viamhealth.android.services.NotifyMedicineTwoMorning;
import com.viamhealth.android.services.NotifyMedicineTwoNight;
import com.viamhealth.android.services.NotifyService;
import com.viamhealth.android.services.NotifyServiceOne;
import com.viamhealth.android.services.NotifyServiceThree;
import com.viamhealth.android.services.NotifyServiceTwo;
import com.viamhealth.android.services.ServicesCommon;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by naren on 10/01/14.
 */
public class NotifyManager {
    private static NotifyManager ourInstance = new NotifyManager();

    public static NotifyManager getInstance() {
        return ourInstance;
    }


    public static Calendar getLastThreeDays(int i) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        cal.set(Calendar.AM_PM, Calendar.AM);
        if (i > 0) {
            cal.add(Calendar.DATE, i);
        }
        return cal;
    }

    public boolean addReminders(User user, Context context, List<ReminderReading> readings, Date rrDate) {

        Global_Application ga = ((Global_Application) context.getApplicationContext());

        if (user.getId() != ga.getLoggedInUser().getId())
            return Boolean.FALSE;

        Calendar rrCal = Calendar.getInstance();
        rrCal.setTime(rrDate);

        Calendar rightNow = Calendar.getInstance();

        Calendar zeroDayAfter = getLastThreeDays(0);
        Calendar oneDayAfter = getLastThreeDays(1);
        Calendar twoDayAfter = getLastThreeDays(2);
        Calendar threeDayAfter = getLastThreeDays(3);

        if (rrCal.after(threeDayAfter) || rrCal.before(zeroDayAfter)) {
            return Boolean.FALSE;
        }

        Boolean setNotOther = Boolean.TRUE;
        Boolean setNotMedicineMorning = Boolean.TRUE;
        Boolean setNotMedicineNoon = Boolean.TRUE;
        Boolean setNotMedicineEvening = Boolean.TRUE;

        if (rrCal.equals(zeroDayAfter)) {
            if (rightNow.get(Calendar.AM_PM) == Calendar.PM) {
                setNotOther = Boolean.FALSE;
                setNotMedicineMorning = Boolean.FALSE;
                if (rightNow.get(Calendar.HOUR) >= ServicesCommon.MEDICINE_NOON_HOUR) {
                    setNotMedicineNoon = Boolean.FALSE;
                }
                if (rightNow.get(Calendar.HOUR) >= ServicesCommon.MEDICINE_EVENING_HOUR) {
                    setNotMedicineEvening = Boolean.FALSE;
                }
            } else {
                if (rightNow.get(Calendar.HOUR) >= ServicesCommon.OTHERS_HOUR) {
                    setNotOther = Boolean.FALSE;
                }
                if (rightNow.get(Calendar.HOUR) >= ServicesCommon.MEDICINE_MORNING_HOUR) {
                    setNotMedicineMorning = Boolean.FALSE;
                }
            }
        }


        Map<Calendar, List<ReminderReading>> mapTimeReadings = new HashMap<Calendar, List<ReminderReading>>();

        Calendar cal6 = Calendar.getInstance();
        cal6.setTime(rrDate);
        //cal6.add(Calendar.MINUTE, 10);
        cal6.set(Calendar.HOUR, ServicesCommon.OTHERS_HOUR);
        cal6.set(Calendar.MINUTE, 0);
        cal6.set(Calendar.SECOND, 0);
        cal6.set(Calendar.MILLISECOND, 0);
        cal6.set(Calendar.AM_PM, Calendar.AM);

        Calendar cal9 = Calendar.getInstance();
        cal9.setTime(rrDate);
        //cal9.add(Calendar.MINUTE, 1);
        cal9.set(Calendar.HOUR, ServicesCommon.MEDICINE_MORNING_HOUR);
        cal9.set(Calendar.MINUTE, 0);
        cal9.set(Calendar.SECOND, 0);
        cal9.set(Calendar.MILLISECOND, 0);
        cal9.set(Calendar.AM_PM, Calendar.AM);

        Calendar cal3 = Calendar.getInstance();
        cal3.setTime(rrDate);
        //cal3.add(Calendar.MINUTE, 7);
        cal3.set(Calendar.HOUR, ServicesCommon.MEDICINE_NOON_HOUR);
        cal3.set(Calendar.MINUTE, 0);
        cal3.set(Calendar.SECOND, 0);
        cal3.set(Calendar.MILLISECOND, 0);
        cal3.set(Calendar.AM_PM, Calendar.PM);

        Calendar cal92 = Calendar.getInstance();
        cal92.setTime(rrDate);
        //cal92.add(Calendar.MINUTE, 5);
        cal92.set(Calendar.HOUR, ServicesCommon.MEDICINE_EVENING_HOUR);
        cal92.set(Calendar.MINUTE, 0);
        cal92.set(Calendar.SECOND, 0);
        cal92.set(Calendar.MILLISECOND, 0);
        cal92.set(Calendar.AM_PM, Calendar.PM);

        int readingsCount = readings.size();
        for (int i = 0; i < readingsCount; i++) {
            ReminderReading reading = readings.get(i);

            if (reading.getReminder().getType() == ReminderType.Medicine) {

                if (setNotMedicineMorning
                        && reading.getReminder().getReminderTimeData(ReminderTime.Morning).getCount() > 0
                        && reading.getAction(ReminderTime.Morning).isCheck() == Boolean.FALSE) {
                    List<ReminderReading> rs9M = mapTimeReadings.get(cal9);
                    if (rs9M == null) rs9M = new ArrayList<ReminderReading>();
                    rs9M.add(reading);
                    mapTimeReadings.put(cal9, rs9M);
                }

                if (setNotMedicineNoon
                        && reading.getReminder().getReminderTimeData(ReminderTime.Noon).getCount() > 0
                        && reading.getAction(ReminderTime.Noon).isCheck() == Boolean.FALSE) {
                    List<ReminderReading> rs3P = mapTimeReadings.get(cal3);
                    if (rs3P == null) rs3P = new ArrayList<ReminderReading>();
                    rs3P.add(reading);
                    mapTimeReadings.put(cal3, rs3P);
                }
                if (setNotMedicineEvening
                        && reading.getReminder().getReminderTimeData(ReminderTime.Night).getCount() > 0
                        && reading.getAction(ReminderTime.Night).isCheck() == Boolean.FALSE) {
                    List<ReminderReading> rs9P = mapTimeReadings.get(cal92);
                    if (rs9P == null) rs9P = new ArrayList<ReminderReading>();
                    rs9P.add(reading);
                    mapTimeReadings.put(cal92, rs9P);
                }
            } else {
                if (setNotOther
                        && reading.isCompleteCheck() == Boolean.FALSE) {
                    List<ReminderReading> rs6P = mapTimeReadings.get(cal6);
                    if (rs6P == null) rs6P = new ArrayList<ReminderReading>();
                    rs6P.add(reading);
                    mapTimeReadings.put(cal6, rs6P);
                }
            }
        }

        Set<Calendar> keySet = mapTimeReadings.keySet();

        for (Iterator<Calendar> iter = keySet.iterator(); iter.hasNext(); ) {
            Calendar cal = iter.next();
            if (mapTimeReadings.get(cal) == null) {

                continue;
            }


            Intent myIntent = null;

            if (rrCal.equals(zeroDayAfter)) {
                if (cal == cal6)
                    myIntent = new Intent(context, NotifyService.class);
                else if (cal == cal9)
                    myIntent = new Intent(context, NotifyMedicineTodayMorning.class);
                else if (cal == cal3)
                    myIntent = new Intent(context, NotifyMedicineTodayAfternoon.class);
                else if (cal == cal92)
                    myIntent = new Intent(context, NotifyMedicineTodayNight.class);
                else
                    throw new IllegalArgumentException();
            } else if (rrCal.equals(oneDayAfter)) {
                if (cal == cal6)
                    myIntent = new Intent(context, NotifyServiceOne.class);
                else if (cal == cal9)
                    myIntent = new Intent(context, NotifyMedicineTomorrowMorning.class);
                else if (cal == cal3)
                    myIntent = new Intent(context, NotifyMedicineTomorrowAfternoon.class);
                else if (cal == cal92)
                    myIntent = new Intent(context, NotifyMedicineTomorrowNight.class);
                else
                    throw new IllegalArgumentException();
            } else if (rrCal.equals(twoDayAfter)) {
                if (cal == cal6)
                    myIntent = new Intent(context, NotifyServiceTwo.class);
                else if (cal == cal9)
                    myIntent = new Intent(context, NotifyMedicineTwoMorning.class);
                else if (cal == cal3)
                    myIntent = new Intent(context, NotifyMedicineTwoAfternoon.class);
                else if (cal == cal92)
                    myIntent = new Intent(context, NotifyMedicineTwoNight.class);
                else
                    throw new IllegalArgumentException();
            } else if (rrCal.equals(threeDayAfter)) {
                if (cal == cal6)
                    myIntent = new Intent(context, NotifyServiceThree.class);
                else if (cal == cal9)
                    myIntent = new Intent(context, NotifyMedicineThreeMorning.class);
                else if (cal == cal3)
                    myIntent = new Intent(context, NotifyMedicineThreeAfternoon.class);
                else if (cal == cal92)
                    myIntent = new Intent(context, NotifyMedicineThreeNight.class);
                else
                    throw new IllegalArgumentException();
            } else {
                throw new IllegalArgumentException();
            }


            Parcel parcel = ServicesCommon.getReminderNOParcel(mapTimeReadings.get(cal));
            Parcel parcel_user = ServicesCommon.getUserParcel(user);

            myIntent.putExtra(ServicesCommon.PARAM_TYPE, NotificationType.UserReminder.ordinal());
            myIntent.putExtra(ServicesCommon.PARAM_USER, parcel_user.marshall());
            myIntent.putExtra(ServicesCommon.PARAM_DATA, parcel.marshall());
            myIntent.setAction(ServicesCommon.ACTION_CREATE);

            //myIntent.putParcelableArrayListExtra(NotifyService.PARAM_DATA, (ArrayList) mapTimeReadings.get(cal));

            AlarmManager alarmManager = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);
            PendingIntent pendingIntent = PendingIntent.getService(context, 0, myIntent, 0);

            alarmManager.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);

        }

        return true;
    }

    /*private Boolean getSameCalDayCheck(Calendar cal1,Calendar cal2){
        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR);
    }*/
    private NotifyManager() {
    }
}
