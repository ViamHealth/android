package com.viamhealth.android.provider.parsers;

import com.viamhealth.android.model.BaseModel;
import com.viamhealth.android.model.enums.BloodGroup;
import com.viamhealth.android.model.enums.Gender;
import com.viamhealth.android.model.enums.ReminderTime;
import com.viamhealth.android.model.enums.ReminderType;
import com.viamhealth.android.model.enums.RepeatMode;
import com.viamhealth.android.model.enums.RepeatWeekDay;
import com.viamhealth.android.model.reminder.Reminder;
import com.viamhealth.android.model.reminder.ReminderTimeData;
import com.viamhealth.android.model.users.BMIProfile;
import com.viamhealth.android.model.users.Profile;
import com.viamhealth.android.model.users.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by monj on 2/1/14.
 */
public class ReminderJasonParser extends JsonParser {

    @Override
    protected BaseModel parse(JSONObject jsonObject) throws JSONException {
        Reminder reminder = new Reminder();

        SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd");
        try {
            reminder.setId(jsonObject.getLong("id"));
            reminder.setUserId(jsonObject.getLong("user"));
            reminder.setName(jsonObject.getString("name"));
            reminder.setType(ReminderType.get(jsonObject.getInt("type")));
            reminder.setDetails(jsonObject.getString("details"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            reminder.setStartDate(formater.parse(jsonObject.getString("start_date")));
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            reminder.setEndDate(formater.parse(jsonObject.getString("end_date")));
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            ReminderTimeData morningData = new ReminderTimeData();
            morningData.setCount(jsonObject.getInt("morning_count"));
            reminder.putReminderTimeData(ReminderTime.Morning, morningData);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            ReminderTimeData noonData = new ReminderTimeData();
            noonData.setCount(jsonObject.getInt("afternoon_count"));
            reminder.putReminderTimeData(ReminderTime.Noon, noonData);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            ReminderTimeData eveningData = new ReminderTimeData();
            eveningData.setCount(jsonObject.getInt("eveing_count"));
            reminder.putReminderTimeData(ReminderTime.Evening, eveningData);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            ReminderTimeData nightData = new ReminderTimeData();
            nightData.setCount(jsonObject.getInt("night_count"));
            reminder.putReminderTimeData(ReminderTime.Night, nightData);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            reminder.setRepeatDay(jsonObject.getInt("repeat_day"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            reminder.setRepeatHour(jsonObject.getInt("repeat_hour"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            reminder.setRepeatMin(jsonObject.getInt("repeat_min"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            reminder.setRepeatEveryX(jsonObject.getInt("repeat_every_x"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            reminder.setRepeatICounter(jsonObject.getInt("repeat_i_counter"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            reminder.setRepeatMode(RepeatMode.get(jsonObject.getInt("repeat_mode")));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            reminder.setRepeatWeekDay(RepeatWeekDay.get(jsonObject.getInt("repeat_weekday")));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return reminder;
    }


    //need to write code for parsing reminderreading


    @Override
    public List<BaseModel> newList() {
        return new ArrayList<BaseModel>();
    }
}
