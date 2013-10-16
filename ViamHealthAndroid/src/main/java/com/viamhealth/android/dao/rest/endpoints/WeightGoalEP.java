package com.viamhealth.android.dao.rest.endpoints;

import android.app.Application;
import android.content.Context;

import com.viamhealth.android.dao.restclient.core.RestClient;
import com.viamhealth.android.model.goals.Goal;
import com.viamhealth.android.model.goals.GoalReadings;
import com.viamhealth.android.model.goals.WeightGoal;
import com.viamhealth.android.model.goals.WeightGoalReadings;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by naren on 16/10/13.
 */
public class WeightGoalEP extends GoalsEP {

    public WeightGoalEP(Context context, Application app) {
        super(context, app);
    }

    @Override
    protected String getReadingsURL() {
        return "weight-readings";
    }

    @Override
    protected String getGoalURL() {
        return "weight-goal";
    }

    @Override
    protected Goal newGoal() {
        return new WeightGoal();
    }

    @Override
    protected GoalReadings newGoalReading() {
        return new WeightGoalReadings();
    }

    @Override
    protected void addParams(final RestClient client, final GoalReadings readings) {
        client.AddParam("weight", ((WeightGoalReadings)readings).getWeight());
    }

    @Override
    protected void addParams(final RestClient client, final Goal goal) {
        client.AddParam("weight", ((WeightGoal)goal).getWeight());
    }

    @Override
    protected void processParams(final GoalReadings reading, final JSONObject jsonReading) throws JSONException {
        ((WeightGoalReadings)reading).setWeight(jsonReading.getDouble("weight"));
    }

    @Override
    protected void processParams(final Goal goal, final JSONObject jsonGoal) throws JSONException {
        ((WeightGoal)goal).setWeight(jsonGoal.getDouble("weight"));
    }
}
