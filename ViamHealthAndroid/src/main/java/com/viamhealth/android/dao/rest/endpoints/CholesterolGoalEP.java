package com.viamhealth.android.dao.rest.endpoints;

import android.app.Application;
import android.content.Context;

import com.viamhealth.android.dao.restclient.core.RestClient;
import com.viamhealth.android.model.goals.CholesterolGoal;
import com.viamhealth.android.model.goals.CholesterolGoalReading;
import com.viamhealth.android.model.goals.CholesterolGoal;
import com.viamhealth.android.model.goals.CholesterolGoalReading;
import com.viamhealth.android.model.goals.Goal;
import com.viamhealth.android.model.goals.GoalReadings;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by naren on 21/10/13.
 */
public class CholesterolGoalEP extends GoalsEP {

    public CholesterolGoalEP(Context context, Application app) {
        super(context, app);
    }

    @Override
    protected String getReadingsURL() {
        return "cholesterol-readings";
    }

    @Override
    protected String getGoalURL() {
        return "cholesterol-goals";
    }

    @Override
    protected Goal newGoal() {
        return new CholesterolGoal();
    }

    @Override
    protected GoalReadings newGoalReading() {
        return new CholesterolGoalReading();
    }

    @Override
    protected Goal.HealthyRange newHealthyRange(Goal goal) {
        return ((CholesterolGoal)goal).new HealthyRange();
    }

    @Override
    protected void addParams(RestClient client, GoalReadings readings) {
        client.AddParam("hdl", ((CholesterolGoalReading) readings).getHdl());
        client.AddParam("ldl", ((CholesterolGoalReading) readings).getLdl());
        client.AddParam("triglycerides", ((CholesterolGoalReading) readings).getTriglycerides());
    }

    @Override
    protected void addParams(RestClient client, Goal goal) {
        client.AddParam("hdl", ((CholesterolGoal) goal).getHdl());
        client.AddParam("ldl", ((CholesterolGoal) goal).getLdl());
        client.AddParam("triglycerides", ((CholesterolGoal) goal).getTriglycerides());
    }

    @Override
    protected void processParams(Goal.HealthyRange range, JSONObject jsonHRange) throws JSONException {
        JSONObject hHDLR = jsonHRange.getJSONObject("hdl");
        JSONObject hLDLR = jsonHRange.getJSONObject("ldl");
        JSONObject hTGR = jsonHRange.getJSONObject("triglycerides");
        ((CholesterolGoal.HealthyRange) range).setMinHDL(hHDLR.getInt("min"));
        ((CholesterolGoal.HealthyRange) range).setMaxHDL(hHDLR.getInt("max"));
        ((CholesterolGoal.HealthyRange) range).setMinLDL(hLDLR.getInt("min"));
        ((CholesterolGoal.HealthyRange) range).setMaxLDL(hLDLR.getInt("max"));
        ((CholesterolGoal.HealthyRange) range).setMinTG(hTGR.getInt("min"));
        ((CholesterolGoal.HealthyRange) range).setMaxTG(hTGR.getInt("max"));
    }

    @Override
    protected void processParams(GoalReadings reading, JSONObject jsonReading) throws JSONException {
        ((CholesterolGoalReading) reading).setHdl(jsonReading.getInt("hdl"));
        ((CholesterolGoalReading) reading).setLdl(jsonReading.getInt("ldl"));
        ((CholesterolGoalReading) reading).setTriglycerides(jsonReading.getInt("triglycerides"));
    }

    @Override
    protected void processParams(Goal goal, JSONObject jsonGoal) throws JSONException {
        ((CholesterolGoal) goal).setHdl(jsonGoal.getInt("hdl"));
        ((CholesterolGoal) goal).setLdl(jsonGoal.getInt("ldl"));
        ((CholesterolGoal) goal).setTriglycerides(jsonGoal.getInt("triglycerides"));
    }    
}
