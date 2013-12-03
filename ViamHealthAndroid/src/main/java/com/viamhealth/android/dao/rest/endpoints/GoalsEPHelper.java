package com.viamhealth.android.dao.rest.endpoints;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.viamhealth.android.Global_Application;
import com.viamhealth.android.dao.restclient.core.RestClient;
import com.viamhealth.android.dao.restclient.old.RequestMethod;
import com.viamhealth.android.model.enums.MedicalConditions;
import com.viamhealth.android.model.goals.BPGoal;
import com.viamhealth.android.model.goals.CholesterolGoal;
import com.viamhealth.android.model.goals.DiabetesGoal;
import com.viamhealth.android.model.goals.Goal;
import com.viamhealth.android.model.goals.GoalReadings;
import com.viamhealth.android.model.goals.WeightGoal;

import org.apache.http.HttpStatus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by naren on 16/10/13.
 */
public class GoalsEPHelper extends BaseEP {


    protected WeightGoalEP weight;
    protected DiabetesGoalEP sugar;
    protected BPGoalEP bp;
    protected CholesterolGoalEP cholesterol;

    final static String TAG = "GoalsEPHelper";

    public GoalsEPHelper(Context context, Application app) {
        super(context, app);
        weight = new WeightGoalEP(context, app);
        sugar = new DiabetesGoalEP(context, app);
        bp = new BPGoalEP(context, app);
        cholesterol = new CholesterolGoalEP(context, app);
    }

    private RestClient getRestClient(String url, Long userId) {
        String baseurlString = Global_Application.url+ url + "/?user=" + userId;

        RestClient client = new RestClient(baseurlString);
        client.AddHeader("Authorization","Token "+appPrefs.getToken().toString());

        return client;
    }

    public Map<MedicalConditions, Goal> getAllGoalsConfiguredNew(Long userId) {
        RestClient client = getRestClient("goals", userId);

        try {
            client.Execute(RequestMethod.GET);
        }catch (Exception e) {
            e.printStackTrace();
        }

        String responseString = client.getResponse();
        Log.i(TAG, client.toString());

        Map<MedicalConditions, Goal> mapGoals = null;
        //Map<MedicalConditions, List<GoalReadings>> mapReadings = null;
        if(client.getResponseCode()== HttpStatus.SC_OK)
            mapGoals = processGoalsResponse(responseString);

        return mapGoals;
    }

    private Map<MedicalConditions, Goal> processGoalsResponse(String jsonResponse){
        Map<MedicalConditions, Goal> map = new HashMap<MedicalConditions, Goal>();

        try {
            JSONObject object = new JSONObject(jsonResponse);

            if(object.has(weight.getGoalURL())){
                WeightGoal goal = (WeightGoal) weight.processGoalResponse(object.getJSONObject(weight.getGoalURL()));
                map.put(MedicalConditions.Obese, goal);
            }

            if(object.has(sugar.getGoalURL())){
                DiabetesGoal goal = (DiabetesGoal) sugar.processGoalResponse(object.getJSONObject(sugar.getGoalURL()));
                map.put(MedicalConditions.Diabetes, goal);
            }

            if(object.has(bp.getGoalURL())){
                BPGoal goal = (BPGoal) bp.processGoalResponse(object.getJSONObject(bp.getGoalURL()));
                map.put(MedicalConditions.BloodPressure, goal);
            }

            if(object.has(cholesterol.getGoalURL())){
                CholesterolGoal goal = (CholesterolGoal) cholesterol.processGoalResponse(object.getJSONObject(cholesterol.getGoalURL()));
                map.put(MedicalConditions.Cholesterol, goal);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return map;
    }

    public Map<MedicalConditions, Goal> getAllGoalsConfigured(Long userId) {
        Map<MedicalConditions, Goal> map = new LinkedHashMap<MedicalConditions, Goal>();

        WeightGoal weightGoal = (WeightGoal)weight.getGoalsForUser(userId);
        if(weightGoal != null){
            map.put(MedicalConditions.Obese, weightGoal);
        }

        DiabetesGoal dGoal = (DiabetesGoal) sugar.getGoalsForUser(userId);
        if(dGoal != null)
            map.put(MedicalConditions.Diabetes, dGoal);

        BPGoal bpGoal = (BPGoal) bp.getGoalsForUser(userId);
        if(bpGoal != null)
            map.put(MedicalConditions.BloodPressure, bpGoal);

        CholesterolGoal cGoal = (CholesterolGoal) cholesterol.getGoalsForUser(userId);
        if(cGoal != null)
            map.put(MedicalConditions.Cholesterol, cGoal);

        /* TODO get other goals too */

        return map;
    }

    public Map<MedicalConditions, List<GoalReadings>> getAllGoalReadings(Long userId) {
        Map<MedicalConditions, List<GoalReadings>> map = new HashMap<MedicalConditions, List<GoalReadings>>();

        map.put(MedicalConditions.Obese, weight.getGoalReadings(userId));
        map.put(MedicalConditions.Diabetes, sugar.getGoalReadings(userId));
        map.put(MedicalConditions.BloodPressure, bp.getGoalReadings(userId));
        map.put(MedicalConditions.Cholesterol, cholesterol.getGoalReadings(userId));

        return map;
    }

    public Goal saveGoal(MedicalConditions mc, Goal goal, Long userId) {
        GoalsEP ep = getEndPoint(mc);
        return ep.saveGoalForUser(userId, goal);
    }

    public Goal updateGoal(MedicalConditions mc, Goal goal, Long userId) {
        GoalsEP ep = getEndPoint(mc);
        //TODO need to implement update goal
        return null;
    }

    public GoalReadings saveGoalReadings(MedicalConditions mc, GoalReadings reading, Long userId) {
        GoalsEP ep = getEndPoint(mc);
        if(reading.isToUpdate())
            reading = ep.updateGoalReadings(userId, reading);
        else
            reading = ep.createGoalReadings(userId, reading);

        return reading;
    }

    private GoalsEP getEndPoint(MedicalConditions mc) {
        switch(mc){
            case Obese:
                return weight;

            case Diabetes:
                return sugar;

            case Cholesterol:
                return cholesterol;

            case BloodPressure:
                return bp;

        }

        return null;
    }
}
