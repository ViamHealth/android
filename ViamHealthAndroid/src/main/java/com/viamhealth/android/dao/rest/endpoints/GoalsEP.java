package com.viamhealth.android.dao.rest.endpoints;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.viamhealth.android.Global_Application;
import com.viamhealth.android.dao.restclient.core.RestClient;
import com.viamhealth.android.dao.restclient.old.RequestMethod;
import com.viamhealth.android.model.enums.MedicalConditions;
import com.viamhealth.android.model.goals.Goal;
import com.viamhealth.android.model.goals.GoalReadings;
import com.viamhealth.android.model.users.User;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by naren on 11/10/13.
 */
public abstract class GoalsEP extends BaseEP {
    protected static final String TAG = "GOALSEP";

    protected SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd");

    public GoalsEP(Context context, Application app) {
        super(context, app);
    }

    private RestClient getRestClient(String url, Long userId) {
        String baseurlString = Global_Application.url+ url + "/?user=" + userId;

        RestClient client = new RestClient(baseurlString);
        client.AddHeader("Authorization","Token "+appPrefs.getToken().toString());

        return client;
    }

    public Goal getGoalsForUser(Long userId) {

        RestClient client = getRestClient(getGoalURL(), userId);

        try {
            client.Execute(RequestMethod.GET);
        }catch (Exception e) {
            e.printStackTrace();
        }

        String responseString = client.getResponse();
        Log.i(TAG, client.toString());

        Goal goal = null;
        if(client.getResponseCode()==HttpStatus.SC_OK)
            goal = processGoalResponse(responseString);

        return goal;
    }

    public List<GoalReadings> getGoalReadings(Long userId) {
        RestClient client = getRestClient(getReadingsURL(), userId);

        try {
            client.Execute(RequestMethod.GET);
        }catch (Exception e) {
            e.printStackTrace();
        }

        String responseString = client.getResponse();
        Log.i(TAG, client.toString());

        List<GoalReadings> readings = null;
        if(client.getResponseCode()==HttpStatus.SC_OK)
            readings = processGoalReadings(responseString);

        return readings;
    }

    public GoalReadings getGoalReadings(Long userId, Date readingdate) {
        RestClient client = getRestClient(getReadingsURL() + "/" + formater.format(readingdate), userId);

        try {
            client.Execute(RequestMethod.GET);
        }catch (Exception e) {
            e.printStackTrace();
        }

        if(client.getResponseCode() == HttpStatus.SC_NOT_FOUND){
            return null;
        }

        String responseString = client.getResponse();
        Log.i(TAG, client.toString());

        GoalReadings reading = null;
        if(client.getResponseCode()==HttpStatus.SC_OK)
            reading = processGoalReading(responseString);

        return reading;

    }

    abstract protected String getReadingsURL();
    abstract protected String getGoalURL();
    abstract protected Goal newGoal();
    abstract protected GoalReadings newGoalReading();
    abstract protected Goal.HealthyRange newHealthyRange(Goal goal);
    abstract protected void addParams(final RestClient client, final GoalReadings readings);
    abstract protected void addParams(final RestClient client, final Goal goal);
    abstract protected void processParams(final Goal.HealthyRange range, final JSONObject jsonHRange) throws JSONException;
    abstract protected void processParams(final GoalReadings reading, final JSONObject jsonReading) throws JSONException;
    abstract protected void processParams(final Goal goal, final JSONObject jsonGoal) throws JSONException;

    public GoalReadings updateGoalReadings(Long userId, GoalReadings readings) {
        RestClient client = getRestClient(getReadingsURL() + "/" + formater.format(readings.getReadingDate()), userId);

        client.AddParam("reading_date", formater.format(readings.getReadingDate()));

        addParams(client, readings);

        try {
            client.Execute(RequestMethod.PUT);
        }catch (Exception e) {
            e.printStackTrace();
        }

        String responseString = client.getResponse();
        Log.i(TAG, client.toString());
        return processGoalReading(responseString);

    }

    public GoalReadings createGoalReadings(Long userId, GoalReadings readings) {

        RestClient client = getRestClient(getReadingsURL(), userId);
        client.AddParam("reading_date", formater.format(readings.getReadingDate()));

        addParams(client, readings);

        try {
            client.Execute(RequestMethod.POST);
        }catch (Exception e) {
            e.printStackTrace();
        }

        //this is a hack for time being
        if(client.getResponseCode()== HttpStatus.SC_CREATED)
            return readings;

        return null;

        /*String responseString = client.getResponse();
        Log.i(TAG, client.toString());
        return processGoalReading(responseString);*/
    }

    public Goal createGoalForUser(Long userId, Goal goal) {

        RestClient client = getRestClient(getGoalURL(), userId);
        client.AddParam("target_date", formater.format(goal.getTargetDate()));

        addParams(client, goal);

        try {
            client.Execute(RequestMethod.POST);
        }catch (Exception e) {
            e.printStackTrace();
        }

        String responseString = client.getResponse();
        Log.i(TAG, client.toString());
        return processGoalResponse(responseString);

    }

    private List<GoalReadings> processGoalReadings(String jsonResponse) {
        try {
            JSONObject response = new JSONObject(jsonResponse);
            JSONArray results = response.getJSONArray("results");
            return processGoalReadings(results);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    private GoalReadings processGoalReading(String jsonResponse) {
        try {
            JSONObject response = new JSONObject(jsonResponse);
            return processGoalReading(response);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    private GoalReadings processGoalReading(JSONObject jsonReading) {
        GoalReadings reading = null;

        try {
            reading = newGoalReading();
            //JSONObject jsonReading = jsonReadings.getJSONObject(i);
            reading.setComments(jsonReading.getString("comment"));
            reading.setReadingDate(formater.parse(jsonReading.getString("reading_date")));

            processParams(reading, jsonReading);

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return reading;
    }

    private List<GoalReadings> processGoalReadings(JSONArray jsonReadings){
        List<GoalReadings> readings = new ArrayList<GoalReadings>();
        try{
            for(int i=0; i<jsonReadings.length(); i++){
                GoalReadings reading = processGoalReading(jsonReadings.getJSONObject(i));
                if(reading!=null)
                readings.add(reading);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return readings;
    }

    private Goal processGoalResponse(JSONObject jsonGoal){
        Goal goal = newGoal();
        try{
            goal.setId(jsonGoal.getLong("id"));
            goal.setUserId(jsonGoal.getLong("user"));
            goal.setTargetDate(formater.parse(jsonGoal.getString("target_date")));
            goal.setHealthyRange(processGoalHealthyRange(goal, jsonGoal.getJSONObject("healthy_range")));
            processParams(goal, jsonGoal);

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return goal;
    }

    private Goal.HealthyRange processGoalHealthyRange(Goal goal, JSONObject jsonHealthy) {
        Goal.HealthyRange hRange = newHealthyRange(goal);

        try {
            processParams(hRange, jsonHealthy);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return hRange;
    }

    private Goal processGoalResponse(String jsonResponse){
        Goal goal = null;
        try{
            JSONObject response = new JSONObject(jsonResponse);
            if(response.getInt("count")>0){
                JSONArray results = response.getJSONArray("results");
                JSONObject jsonGoal = results.getJSONObject(0);
                goal = processGoalResponse(jsonGoal);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return goal;
    }
}
