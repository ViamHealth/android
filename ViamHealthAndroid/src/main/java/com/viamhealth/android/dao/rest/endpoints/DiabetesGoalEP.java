package com.viamhealth.android.dao.rest.endpoints;

import android.app.Application;
import android.content.Context;

import com.viamhealth.android.dao.restclient.core.RestClient;
import com.viamhealth.android.model.goals.DiabetesGoal;
import com.viamhealth.android.model.goals.DiabetesGoalReading;
import com.viamhealth.android.model.goals.Goal;
import com.viamhealth.android.model.goals.GoalReadings;
import com.viamhealth.android.model.goals.WeightGoalReadings;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by naren on 18/10/13.
 */
public class DiabetesGoalEP extends GoalsEP{

    public DiabetesGoalEP(Context context, Application app) {
        super(context, app);
    }

    @Override
    protected String getReadingsURL() {
        return "glucose-readings";
    }

    @Override
    protected String getGoalURL() {
        return "glucose-goals";
    }

    @Override
    protected Goal newGoal() {
        return new DiabetesGoal();
    }

    @Override
    protected GoalReadings newGoalReading() {
        return new DiabetesGoalReading();
    }

    @Override
    protected Goal.HealthyRange newHealthyRange(Goal goal) {
        return ((DiabetesGoal)goal).new HealthyRange();
    }

    @Override
    protected void addParams(RestClient client, GoalReadings readings) {
        int fasting = ((DiabetesGoalReading)readings).getFbs();
        int random = ((DiabetesGoalReading)readings).getRbs();

        if(fasting>0)
            client.AddParam("fasting", fasting);

        if(random>0)
            client.AddParam("random", random);
    }

    @Override
    protected void addParams(RestClient client, Goal goal) {
        client.AddParam("fasting", ((DiabetesGoal)goal).getFbs());
        client.AddParam("random", ((DiabetesGoal)goal).getRbs());
    }

    @Override
    protected void processParams(Goal.HealthyRange range, JSONObject jsonHRange) throws JSONException {
        JSONObject hFastingRange = jsonHRange.getJSONObject("fasting");
        ((DiabetesGoal.HealthyRange) range).setMaxFBS(hFastingRange.getInt("max"));
        ((DiabetesGoal.HealthyRange) range).setMinFBS(hFastingRange.getInt("min"));
        JSONObject hRandomRange = jsonHRange.getJSONObject("random");
        ((DiabetesGoal.HealthyRange) range).setMaxRBS(hRandomRange.getInt("max"));
        ((DiabetesGoal.HealthyRange) range).setMinRBS(hRandomRange.getInt("min"));
    }

    @Override
    protected void processParams(GoalReadings reading, JSONObject jsonReading) throws JSONException {
        ((DiabetesGoalReading)reading).setFbs(jsonReading.getInt("fasting"));
        ((DiabetesGoalReading)reading).setRbs(jsonReading.getInt("random"));
    }

    @Override
    protected void processParams(Goal goal, JSONObject jsonGoal) throws JSONException {
        ((DiabetesGoal)goal).setFbs(jsonGoal.getInt("fasting"));
        ((DiabetesGoal)goal).setRbs(jsonGoal.getInt("random"));
    }

    @Override
    public boolean updateReading(GoalReadings reading) {
        return ((DiabetesGoalReading) reading).isToUpdate();
    }
}
