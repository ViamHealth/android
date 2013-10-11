package com.viamhealth.android.dao.rest.endpoints;

import android.app.Application;
import android.content.Context;

import com.viamhealth.android.Global_Application;
import com.viamhealth.android.dao.restclient.core.RestClient;
import com.viamhealth.android.dao.restclient.old.RequestMethod;
import com.viamhealth.android.model.goals.GoalReadings;
import com.viamhealth.android.model.goals.WeightGoal;
import com.viamhealth.android.model.goals.WeightGoalReadings;
import com.viamhealth.android.model.users.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by naren on 11/10/13.
 */
public class GoalsEP extends BaseEP {

    public GoalsEP(Context context, Application app) {
        super(context, app);
    }

    public WeightGoal getWeightGoalsForUser(Long userId) {
        String baseurlString = Global_Application.url+"weight-goals/";

        RestClient client = new RestClient(baseurlString);
        client.AddHeader("Authorization","Token "+appPrefs.getToken().toString());

        client.AddParam("user", userId);

        try {
            client.Execute(RequestMethod.GET);
        }catch (Exception e) {
            e.printStackTrace();
        }

        String responseString = client.getResponse();

        return processWeightGoalResponse(responseString);

    }

    public WeightGoal createWeightGoalForUser(Long userId, WeightGoal goal) {
        String baseurlString = Global_Application.url+"weight-goals/";

        RestClient client = new RestClient(baseurlString);
        client.AddHeader("Authorization","Token "+appPrefs.getToken().toString());

        client.AddParam("user", userId);
        client.AddParam("weight", goal.getWeight());
        SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd");
        client.AddParam("target_date", formater.format(goal.getTargetDate()));
        client.AddParam("weight_measure", "METRIC");

        try {
            client.Execute(RequestMethod.POST);
        }catch (Exception e) {
            e.printStackTrace();
        }

        String responseString = client.getResponse();

        return processWeightGoalResponse(responseString);

    }

    private List<GoalReadings> processWeightGoalReadings(JSONArray jsonReadings){
        List<GoalReadings> readings = new ArrayList<GoalReadings>();
        try{
            for(int i=0; i<jsonReadings.length(); i++){
                WeightGoalReadings reading = new WeightGoalReadings();
                JSONObject jsonReading = jsonReadings.getJSONObject(i);
                reading.setWeight(jsonReading.getDouble("weight"));
                reading.setComments(jsonReading.getString("comment"));
                reading.setGoalId(jsonReading.getLong("user_weight_goal"));
                SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd");
                reading.setReadingDate(formater.parse(jsonReading.getString("reading_date")));
                reading.setId(jsonReading.getLong("id"));
                readings.add(reading);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return readings;
    }

    private WeightGoal processWeightGoalResponse(String jsonResponse){
        WeightGoal goal = new WeightGoal();
        try{
            JSONArray results = new JSONArray(jsonResponse);
            JSONObject jsonGoal = results.getJSONObject(0);
            goal.setId(jsonGoal.getLong("id"));
            goal.setUserId(jsonGoal.getLong("user"));
            goal.setWeight(jsonGoal.getDouble("weight"));
            SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd");
            goal.setTargetDate(formater.parse(jsonGoal.getString("target_date")));
            goal.setReadings(processWeightGoalReadings(jsonGoal.getJSONArray("readings")));
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return goal;
    }
}
