package com.viamhealth.android.notification;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Parcel;

import com.viamhealth.android.model.enums.ReminderType;
import com.viamhealth.android.model.reminder.Reminder;
import com.viamhealth.android.model.reminder.ReminderReading;
import com.viamhealth.android.model.users.User;
import com.viamhealth.android.services.NotifyService;

import java.util.ArrayList;
import java.util.Calendar;
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

    public boolean addReminders(User user, Context context, List<ReminderReading> readings) {
        Map<Calendar, List<ReminderReading>> mapTimeReadings = new HashMap<Calendar, List<ReminderReading>>();
        int readingsCount1 = readings.size();

        for(int i=0; i<readingsCount1; i++){
            ReminderReading reading = readings.get(i);
            System.out.println(reading.toString());
        }
        Calendar cal6 = Calendar.getInstance();
        cal6.set(Calendar.HOUR, 8); cal6.set(Calendar.MINUTE, 0);
        cal6.set(Calendar.SECOND, 0); cal6.set(Calendar.MILLISECOND, 0);
        cal6.set(Calendar.AM_PM, Calendar.AM);


        int readingsCount = readings.size();
        for(int i=0; i<readingsCount; i++){
            ReminderReading reading = readings.get(i);

            if(reading.getReminder().getType()== ReminderType.Medicine){
                Calendar cal9 = Calendar.getInstance();
                cal9.set(Calendar.HOUR, 9); cal9.set(Calendar.MINUTE, 0);
                cal9.set(Calendar.SECOND, 0); cal9.set(Calendar.MILLISECOND, 0);
                cal9.set(Calendar.AM_PM, Calendar.AM);
                List<ReminderReading> rs9M = mapTimeReadings.get(cal9);
                if(rs9M==null) rs9M = new ArrayList<ReminderReading>();
                rs9M.add(reading);
                mapTimeReadings.put(cal9, rs9M);

                Calendar cal3 = Calendar.getInstance();
                cal3.set(Calendar.HOUR, 3); cal3.set(Calendar.MINUTE, 0);
                cal3.set(Calendar.SECOND, 0); cal3.set(Calendar.MILLISECOND, 0);
                cal3.set(Calendar.AM_PM, Calendar.PM);
                List<ReminderReading> rs3P = mapTimeReadings.get(cal3);
                if(rs3P==null) rs3P = new ArrayList<ReminderReading>();
                rs3P.add(reading);
                mapTimeReadings.put(cal3, rs3P);

                Calendar cal92 = Calendar.getInstance();
                cal92.set(Calendar.HOUR, 6); cal92.set(Calendar.MINUTE, 0);
                cal92.set(Calendar.SECOND, 0); cal92.set(Calendar.MILLISECOND, 0);
                cal92.set(Calendar.AM_PM, Calendar.PM);

                List<ReminderReading> rs9P = mapTimeReadings.get(cal92);
                if(rs9P==null) rs9P = new ArrayList<ReminderReading>();
                rs9P.add(reading);
                mapTimeReadings.put(cal92, rs9P);
            }else{

                List<ReminderReading> rs6P = mapTimeReadings.get(cal6);
                if(rs6P==null) rs6P = new ArrayList<ReminderReading>();
                rs6P.add(reading);
                mapTimeReadings.put(cal6, rs6P);
            }
        }

        Set<Calendar> keySet = mapTimeReadings.keySet();
        Calendar today = Calendar.getInstance();

        for (Iterator<Calendar> iter = keySet.iterator(); iter.hasNext(); ){
            Calendar cal = iter.next();

            if(cal.before(today)){
                //System.out.println(" Skipping old entries");
                //System.out.println(" skipping readings for this date time" + cal.toString());
                continue;
            }
            if(mapTimeReadings.get(cal) == null){
                //System.out.println(" No readings for this date time" + cal.toString());
                continue;
            } else {
                //System.out.println( " Readings for "+cal.getTime()+" size=" + mapTimeReadings.get(cal).size());
                //System.out.println(mapTimeReadings.get(cal).get(0).toString());
            }
            ReminderNO reminderNo = new ReminderNO(mapTimeReadings.get(cal));
            Parcel parcel = Parcel.obtain();
            reminderNo.writeToParcel(parcel, 0);
            parcel.setDataPosition(0);

            Parcel parcel_user = Parcel.obtain();
            user.writeToParcel(parcel_user, 0);
            parcel_user.setDataPosition(0);

            Intent myIntent = new Intent(context , NotifyService.class);
            myIntent.putExtra(NotifyService.PARAM_TYPE, NotificationType.UserReminder.ordinal());
            myIntent.putExtra(NotifyService.PARAM_USER, parcel_user.marshall());
            myIntent.putExtra(NotifyService.PARAM_DATA, parcel.marshall());

            //myIntent.putParcelableArrayListExtra(NotifyService.PARAM_DATA, (ArrayList) mapTimeReadings.get(cal));

            AlarmManager alarmManager = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);
            PendingIntent pendingIntent = PendingIntent.getService(context, 0, myIntent, 0);
            System.out.println(" Setting alarm for time " + cal.getTime());
            alarmManager.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);

        }

        return true;
    }

    private NotifyManager() {
    }
}
