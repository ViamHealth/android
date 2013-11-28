package com.viamhealth.android.dao.rest.endpoints;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.viamhealth.android.dao.restclient.core.RestClient;
import com.viamhealth.android.dao.restclient.old.RequestMethod;
import com.viamhealth.android.model.enums.ReminderTime;
import com.viamhealth.android.model.enums.ReminderType;
import com.viamhealth.android.model.enums.RepeatMode;
import com.viamhealth.android.model.enums.RepeatWeekDay;
import com.viamhealth.android.model.reminder.Action;
import com.viamhealth.android.model.reminder.Reminder;
import com.viamhealth.android.model.reminder.ReminderReading;
import com.viamhealth.android.model.reminder.ReminderTimeData;
import com.viamhealth.android.utils.DateUtils;

import org.apache.http.HttpStatus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by naren on 21/11/13.
 */
public class ReminderEP extends BaseEP {

    protected SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd");
    private static String TAG = "ReminderEP";

    public ReminderEP(Context context, Application app) {
        super(context, app);
    }

    protected RestClient addParams(RestClient client, Reminder reminder) {
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
        return client;
    }

    public Reminder add(Long userId, Reminder reminder){
        Params params = new Params();
        params.put("user", userId.toString());

        RestClient client = getRestClient("reminders", params);
        client = addParams(client, reminder);

        try {
            client.Execute(RequestMethod.POST);
        }catch (Exception e) {
            e.printStackTrace();
        }

        if(client.getResponseCode() != HttpStatus.SC_CREATED)
            return null;

        String responseString = client.getResponse();
        Log.i(TAG, client.toString());
        return processReminder(responseString);
    }

    public Reminder update(Reminder reminder){
        Params params = new Params();
        params.put("user", reminder.getUserId().toString());

        RestClient client = getRestClient("reminders/" + reminder.getId(), params);
        client = addParams(client, reminder);

        try {
            client.Execute(RequestMethod.PUT);
        }catch (Exception e) {
            e.printStackTrace();
        }

        if(client.getResponseCode() != HttpStatus.SC_OK)
            return null;

        String responseString = client.getResponse();
        Log.i(TAG, client.toString());
        return processReminder(responseString);
    }

    public Map<ReminderType, List<Reminder>> get(Long userId, ReminderType type){
        Params params = new Params();
        params.put("user", userId.toString());
        params.put("page_size", "100");

        if(type!=null)
            params.put("type", String.valueOf(type.value()));

        RestClient client = getRestClient("reminders", params);
        try {
            client.Execute(RequestMethod.GET);
        }catch (Exception e) {
            e.printStackTrace();
        }

        if(client.getResponseCode() != HttpStatus.SC_OK)
            return null;

        String responseString = client.getResponse();
        Log.i(TAG, client.toString());
        return processReminders(responseString);
    }

    public boolean delete(Long reminderId){
        Params params = new Params();

        RestClient client = getRestClient("reminders/" + reminderId, params);

        try {
            client.Execute(RequestMethod.DELETE);
        }catch (Exception e) {
            e.printStackTrace();
        }

        Log.i(TAG, client.toString());

        if(client.getResponseCode() != HttpStatus.SC_NO_CONTENT)
            return false;

        return true;
    }

    public Map<Date, List<ReminderReading>> getReadings(List<Date> dates, Long userId){
        Params params = new Params();
        params.put("user", userId.toString());

        if(dates!=null){
            int count = dates.size();
            StringBuilder builder = new StringBuilder();
            for(int i=0; i<count; i++){
                builder.append(formater.format(dates.get(i)));
                if(i!=count-1)
                    builder.append(",");
            }
            params.put("reading_date", builder.toString());
        }

        List<ReminderReading> readings = getReadings(params);
        int count = readings==null ? 0 : readings.size();
        Map<Date, List<ReminderReading>> mapResult = new HashMap<Date, List<ReminderReading>>();
        Date today = new Date();
        int dayInMilliSecs = 24 * 60 * 60 * 1000;
        for(int i=0; i<count; i++){
            ReminderReading r = readings.get(i);
            //TODO need to just do r.getReadingDate once the server sid ebug is fixed
            Date midnightDate = DateUtils.getToday(r.getReadingDate());
            List<ReminderReading> rs = mapResult.get(midnightDate);
            if(rs == null){
                rs = new ArrayList<ReminderReading>();
            }
            rs.add(r);
            mapResult.put(DateUtils.getToday(midnightDate), rs);
        }
        return mapResult;

    }

    protected boolean addCheckParam(RestClient client, Action action, String paramKey){
        boolean isCheck = true;
        if(action!=null){
            isCheck = action.isCheck();
            client.AddParam(paramKey, action.isCheck().toString());
        }else
            isCheck = false;
        return isCheck;
    }

    public ReminderReading updateReading(ReminderReading reading) {
        Params params = new Params();
        params.put("user", reading.getUserId().toString());

        RestClient client = getRestClient("reminderreadings/" + reading.getId(), params);

        client.AddParam("reading_date", formater.format(reading.getReadingDate()));
        Boolean completeCheck = addCheckParam(client, reading.getAction(ReminderTime.Morning), "morning_check");

        boolean isCheck = addCheckParam(client, reading.getAction(ReminderTime.Noon), "afternoon_check");
        completeCheck = !isCheck ? false : completeCheck;

        isCheck = addCheckParam(client, reading.getAction(ReminderTime.Evening), "evening_check");
        completeCheck = !isCheck ? false : completeCheck;

        isCheck = addCheckParam(client, reading.getAction(ReminderTime.Night), "night_check");
        completeCheck = !isCheck ? false : completeCheck;

        //TODO complete check should consider only those times when there is atleast more than 1 dosage
        client.AddParam("complete_check", completeCheck.toString());

        try {
            client.Execute(RequestMethod.PUT);
        }catch (Exception e) {
            e.printStackTrace();
        }

        if(client.getResponseCode() != HttpStatus.SC_OK)
            return null;

        String responseString = client.getResponse();
        Log.i(TAG, client.toString());
        return processReminderReading(responseString);
    }

    protected List<ReminderReading> getReadings(Params params){
        params.put("page_size", "100");
        RestClient client = getRestClient("reminderreadings", params);
        try {
            client.Execute(RequestMethod.GET);
        }catch (Exception e) {
            e.printStackTrace();
        }

        if(client.getResponseCode() != HttpStatus.SC_OK)
            return null;

        String responseString = client.getResponse();
        Log.i(TAG, client.toString());

        return processReminderReadings(responseString);
    }

    public List<ReminderReading> getReadings(Date date, Long userId){
        Params params = new Params();
        params.put("user", userId.toString());

        if(date!=null)
            params.put("reading_date", formater.format(date));

        return getReadings(params);
    }

    protected List<ReminderReading> processReminderReadings(String responseString) {
        try {
            JSONObject response = new JSONObject(responseString);
            JSONArray array = response.getJSONArray("results");
            List<ReminderReading> readings = new ArrayList<ReminderReading>();
            for(int i=0; i<array.length(); i++){
                ReminderReading reading = processReminderReading(array.getJSONObject(i));
                if(reading==null) continue;
                readings.add(reading);
            }
            return readings;
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    protected Reminder processReminder(String responseString) {
        try {
            JSONObject response = new JSONObject(responseString);
            return processReminder(response);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    protected Map<ReminderType, List<Reminder>> processReminders(String responseString) {
        try{
            JSONObject response = new JSONObject(responseString);
            JSONArray array = response.getJSONArray("results");
            Map<ReminderType, List<Reminder>> mapReminders = new HashMap<ReminderType, List<Reminder>>();
            for(int i=0; i<array.length(); i++){
                Reminder reminder = processReminder(array.getJSONObject(i));
                if(reminder==null) continue;

                List<Reminder> reminders = mapReminders.get(reminder.getType());
                if(reminders==null){
                    reminders = new ArrayList<Reminder>();
                }
                reminders.add(reminder);

                mapReminders.put(reminder.getType(), reminders);
            }
            return mapReminders;
        }catch (JSONException e){
            e.printStackTrace();
        }

        return null;
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

    protected ReminderReading processReminderReading(String responseString) {
        try {
            JSONObject response = new JSONObject(responseString);
            return processReminderReading(response);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    protected ReminderReading processReminderReading(JSONObject jsonReminder) {
        ReminderReading reading = null;
        try {
            reading = new ReminderReading();

            reading.setId(jsonReminder.getLong("id"));
            reading.setUserId(jsonReminder.getLong("user"));
            reading.setCompleteCheck(jsonReminder.getBoolean("complete_check"));
            reading.setReminder(processReminder(jsonReminder.getJSONObject("reminder")));
            try {
                reading.setReadingDate(formater.parse(jsonReminder.getString("reading_date")));
            } catch (ParseException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            Action morningAction = new Action();
            morningAction.setCheck(jsonReminder.getBoolean("morning_check"));
            reading.putAction(ReminderTime.Morning, morningAction);

            Action noonAction = new Action();
            noonAction.setCheck(jsonReminder.getBoolean("afternoon_check"));
            reading.putAction(ReminderTime.Noon, noonAction);

            Action eveningCheck = new Action();
            eveningCheck.setCheck(jsonReminder.getBoolean("evening_check"));
            reading.putAction(ReminderTime.Evening, eveningCheck);

            Action nightCheck = new Action();
            nightCheck.setCheck(jsonReminder.getBoolean("night_check"));
            reading.putAction(ReminderTime.Night, nightCheck);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return reading;
    }
}
