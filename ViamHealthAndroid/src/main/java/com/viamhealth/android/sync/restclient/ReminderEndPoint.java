package com.viamhealth.android.sync.restclient;

import android.content.Context;

import com.viamhealth.android.Global_Application;
import com.viamhealth.android.dao.restclient.core.RestClient;
import com.viamhealth.android.dao.restclient.old.RequestMethod;
import com.viamhealth.android.model.BaseModel;
import com.viamhealth.android.model.enums.ReminderTime;
import com.viamhealth.android.model.enums.RepeatWeekDay;
import com.viamhealth.android.model.reminder.Reminder;
import com.viamhealth.android.model.users.User;
import com.viamhealth.android.provider.parsers.JsonParser;
import com.viamhealth.android.provider.parsers.ReminderJasonParser;
import com.viamhealth.android.provider.parsers.UserJsonParser;
import com.viamhealth.android.utils.LogUtils;

import org.apache.http.HttpStatus;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by monj on 2/1/14.
 */
public class ReminderEndPoint extends BaseEndPoint {

private final Global_Application mApplication;
private final ReminderJasonParser parser = new ReminderJasonParser();

        public ReminderEndPoint(Context context) {
            super(context);
            mApplication = (Global_Application)context.getApplicationContext();
        }



        @Override
        protected List<BaseModel> newList() {
            return new ArrayList<BaseModel>();
        }

        @Override
        protected Params getQueryParams() {
            if(mApplication.getLoggedInUser()!=null)
            {
                Params params = new Params();
                params.put("user",mApplication.getLoggedInUser().getId().toString());
                params.put("page_size", "100");
                return params;
            }
            return null;
        }


        @Override
        protected String[] getPathSegments() {
            return new String[]{Paths.REMINDER_READINGS};
        }

        @Override
        protected JsonParser getJsonParser() {
            return new ReminderJasonParser();
        }

        @Override
        protected void addParams(RestClient client, BaseModel model) {

            Reminder reminder= (Reminder)model;

            SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd");

            client.AddParam("type", reminder.getType().value());
            client.AddParam("name", reminder.getName());
            client.AddParam("details", reminder.getDetails());
            if(reminder.getReminderTimeData(ReminderTime.Morning)!=null)
                client.AddParam("morning_count", reminder.getReminderTimeData(ReminderTime.Morning).getCount());
            if(reminder.getReminderTimeData(ReminderTime.Noon)!=null)
                client.AddParam("afternoon_count", reminder.getReminderTimeData(ReminderTime.Noon).getCount());
            if(reminder.getReminderTimeData(ReminderTime.Evening)!=null)
                client.AddParam("evening_count", reminder.getReminderTimeData(ReminderTime.Evening).getCount());
            if(reminder.getReminderTimeData(ReminderTime.Night)!=null)
                client.AddParam("night_count", reminder.getReminderTimeData(ReminderTime.Night).getCount());

            client.AddParam("start_date", formater.format(reminder.getStartDate()));

            if(reminder.getEndDate()!=null)
                client.AddParam("end_date", formater.format(reminder.getEndDate()));

            client.AddParam("repeat_mode", reminder.getRepeatMode().value());
            client.AddParam("repeat_day", reminder.getRepeatDay());
            client.AddParam("repeat_hour", reminder.getRepeatHour());
            client.AddParam("repeat_min", reminder.getRepeatMin());

            if(reminder.getRepeatWeekDay() != RepeatWeekDay.None)
                client.AddParam("repeat_weekday", reminder.getRepeatWeekDay().value());

            client.AddParam("repeat_every_x", reminder.getRepeatEveryX());
            client.AddParam("repeat_i_counter", reminder.getRepeatICounter());
        }

        @Override
        protected List<BaseModel> saveData(BaseModel model, RequestMethod method) throws IOException {
            return super.saveData(model, method);
        }
}


