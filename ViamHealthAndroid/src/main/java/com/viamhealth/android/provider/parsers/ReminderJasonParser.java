package com.viamhealth.android.provider.parsers;

import com.viamhealth.android.model.BaseModel;
import com.viamhealth.android.model.enums.BloodGroup;
import com.viamhealth.android.model.enums.Gender;
import com.viamhealth.android.model.enums.ReminderTime;
import com.viamhealth.android.model.enums.ReminderType;
import com.viamhealth.android.model.enums.RepeatMode;
import com.viamhealth.android.model.enums.RepeatWeekDay;
import com.viamhealth.android.model.reminder.Action;
import com.viamhealth.android.model.reminder.Reminder;
import com.viamhealth.android.model.reminder.ReminderReading;
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

/**s
 * Created by monj on 2/1/14.
 */
public class ReminderJasonParser extends JsonParser {

    protected SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    protected BaseModel parse(JSONObject jsonObject) throws JSONException {
        ReminderReading reading = null;
        try {
            reading = new ReminderReading();

            reading.setId(jsonObject.getLong("id"));
            reading.setUserId(jsonObject.getLong("user"));
            reading.setCompleteCheck(jsonObject.getBoolean("complete_check"));
            reading.setReminder(processReminder(jsonObject.getJSONObject("reminder")));
            try {
                reading.setReadingDate(formater.parse(jsonObject.getString("reading_date")));
            } catch (ParseException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            Action morningAction = new Action();
            morningAction.setCheck(jsonObject.getBoolean("morning_check"));
            reading.putAction(ReminderTime.Morning, morningAction);

            Action noonAction = new Action();
            noonAction.setCheck(jsonObject.getBoolean("afternoon_check"));
            reading.putAction(ReminderTime.Noon, noonAction);

            Action eveningCheck = new Action();
            eveningCheck.setCheck(jsonObject.getBoolean("evening_check"));
            reading.putAction(ReminderTime.Evening, eveningCheck);

            Action nightCheck = new Action();
            nightCheck.setCheck(jsonObject.getBoolean("night_check"));
            reading.putAction(ReminderTime.Night, nightCheck);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return reading;
    }

    protected Reminder processReminder(JSONObject jsonReminder) {
        Reminder reminder = new Reminder();

        try {
            reminder.setId(jsonReminder.getLong("id"));
            reminder.setUserId(jsonReminder.getLong("user"));
            reminder.setName(jsonReminder.getString("name"));
            reminder.setType(ReminderType.get(jsonReminder.getInt("type")));
            reminder.setDetails(jsonReminder.getString("details"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            reminder.setStartDate(formater.parse(jsonReminder.getString("start_date")));
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            reminder.setEndDate(formater.parse(jsonReminder.getString("end_date")));
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            ReminderTimeData morningData = new ReminderTimeData();
            morningData.setCount(jsonReminder.getInt("morning_count"));
            reminder.putReminderTimeData(ReminderTime.Morning, morningData);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            ReminderTimeData noonData = new ReminderTimeData();
            noonData.setCount(jsonReminder.getInt("afternoon_count"));
            reminder.putReminderTimeData(ReminderTime.Noon, noonData);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            ReminderTimeData eveningData = new ReminderTimeData();
            eveningData.setCount(jsonReminder.getInt("eveing_count"));
            reminder.putReminderTimeData(ReminderTime.Evening, eveningData);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            ReminderTimeData nightData = new ReminderTimeData();
            nightData.setCount(jsonReminder.getInt("night_count"));
            reminder.putReminderTimeData(ReminderTime.Night, nightData);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            reminder.setRepeatDay(jsonReminder.getInt("repeat_day"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            reminder.setRepeatHour(jsonReminder.getInt("repeat_hour"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            reminder.setRepeatMin(jsonReminder.getInt("repeat_min"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            reminder.setRepeatEveryX(jsonReminder.getInt("repeat_every_x"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            reminder.setRepeatICounter(jsonReminder.getInt("repeat_i_counter"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            reminder.setRepeatMode(RepeatMode.get(jsonReminder.getInt("repeat_mode")));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            reminder.setRepeatWeekDay(RepeatWeekDay.get(jsonReminder.getInt("repeat_weekday")));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return reminder;
    }
    

    /*
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
*/

    //need to write code for parsing reminderreading

    @Override
    protected String getJsonArray(String jsonArray)
    {
        JSONObject response = null;
        JSONArray array=null;
        try {
            response = new JSONObject(jsonArray);
            array= response.getJSONArray("results");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if(array!=null)
        {
            return array.toString();
        }
        return jsonArray;
    }

    @Override
    public List<BaseModel> newList() {
        return new ArrayList<BaseModel>();
    }
}
