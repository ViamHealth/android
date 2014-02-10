package com.viamhealth.android.services;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.viamhealth.android.Global_Application;
import com.viamhealth.android.dao.rest.endpoints.ReminderEP;
import com.viamhealth.android.model.reminder.Reminder;
import com.viamhealth.android.model.users.User;

import java.util.ArrayList;

/**
 * Created by monj on 5/2/14.
 */
public class ReminderBackground extends IntentService {

    Global_Application ga;
    ReminderEP reminderEP;
    User user;
    Reminder rem;

    public ReminderBackground() {
        super("ReminderBackground");
    }

    @Override
    protected void onHandleIntent(Intent intent) {


        ga=(Global_Application)getApplicationContext();
        reminderEP=new ReminderEP(ReminderBackground.this, ga);
        ArrayList<Reminder> reminder=new ArrayList<Reminder>();
        reminder=intent.getParcelableArrayListExtra("reminder");
        user=intent.getParcelableExtra("user");
        Log.e("Background Reminder","before adding the reminders");
        for(int i=0;i<reminder.size();i++)
        {
            rem=reminderEP.add(user.getId(),reminder.get(i));
        }


    }
}
