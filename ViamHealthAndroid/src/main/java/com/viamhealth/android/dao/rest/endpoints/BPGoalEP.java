package com.viamhealth.android.dao.rest.endpoints;

import android.app.Application;
import android.content.Context;

import com.viamhealth.android.dao.restclient.core.RestClient;
import com.viamhealth.android.model.goals.BPGoal;
import com.viamhealth.android.model.goals.BPGoalReading;
import com.viamhealth.android.model.goals.DiabetesGoal;
import com.viamhealth.android.model.goals.DiabetesGoalReading;
import com.viamhealth.android.model.goals.Goal;
import com.viamhealth.android.model.goals.GoalReadings;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by naren on 20/10/13.
 */
public class BPGoalEP extends GoalsEP {

    public BPGoalEP(Context context, Application app) {
        super(context, app);
    }

    @Override
    protected String getReadingsURL() {
        return "blood-pressure-readings";
    }

    @Override
    protected String getGoalURL() {
        return "blood-pressure-goals";
    }

    @Override
    protected Goal newGoal() {
        return new BPGoal();
    }

    @Override
    protected GoalReadings newGoalReading() {
        return new BPGoalReading();
    }

    @Override
    protected Goal.HealthyRange newHealthyRange(Goal goal) {
        return ((BPGoal)goal).new HealthyRange();
    }

    @Override
    protected void addParams(RestClient client, GoalReadings readings) {
        client.AddParam("systolic_pressure", ((BPGoalReading) readings).getSystolicPressure());
        client.AddParam("diastolic_pressure", ((BPGoalReading) readings).getDiastolicPressure());
        client.AddParam("pulse_rate", ((BPGoalReading) readings).getPulseRate());
    }

    @Override
    protected void addParams(RestClient client, Goal goal) {
        client.AddParam("systolic_pressure", ((BPGoal) goal).getSystolicPressure());
        client.AddParam("diastolic_pressure", ((BPGoal) goal).getDiastolicPressure());
        client.AddParam("pulse_rate", ((BPGoal) goal).getPulseRate());
    }

    @Override
    protected void processParams(Goal.HealthyRange range, JSONObject jsonHRange) throws JSONException {
        JSONObject hSPR = jsonHRange.getJSONObject("systolic_pressure");
        ((BPGoal.HealthyRange) range).setMinSP(hSPR.getInt("min"));
        ((BPGoal.HealthyRange) range).setMaxSP(hSPR.getInt("max"));

        JSONObject hDPR = jsonHRange.getJSONObject("diastolic_pressure");
        ((BPGoal.HealthyRange) range).setMinDP(hDPR.getInt("min"));
        ((BPGoal.HealthyRange) range).setMaxDP(hDPR.getInt("max"));

        try {
            JSONObject hPRR = jsonHRange.getJSONObject("pulse_rate");
            ((BPGoal.HealthyRange) range).setMinPR(hPRR.getInt("min"));
            ((BPGoal.HealthyRange) range).setMaxPR(hPRR.getInt("max"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void processParams(GoalReadings reading, JSONObject jsonReading) throws JSONException {
        ((BPGoalReading) reading).setSystolicPressure(jsonReading.getInt("systolic_pressure"));
        ((BPGoalReading) reading).setDiastolicPressure(jsonReading.getInt("diastolic_pressure"));
        ((BPGoalReading) reading).setPulseRate(jsonReading.getInt("pulse_rate"));
    }

    @Override
    protected void processParams(Goal goal, JSONObject jsonGoal) throws JSONException {
        ((BPGoal) goal).setSystolicPressure(jsonGoal.getInt("systolic_pressure"));
        ((BPGoal) goal).setDiastolicPressure(jsonGoal.getInt("diastolic_pressure"));
        ((BPGoal) goal).setPulseRate(jsonGoal.getInt("pulse_rate"));
    }
}
