package com.viamhealth.android.dao.rest.endpoints;

import android.app.Application;
import android.content.Context;

import com.viamhealth.android.model.enums.MedicalConditions;
import com.viamhealth.android.model.goals.Goal;
import com.viamhealth.android.model.goals.GoalReadings;
import com.viamhealth.android.model.goals.WeightGoal;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by naren on 16/10/13.
 */
public class GoalsEPHelper extends BaseEP {


    protected WeightGoalEP weight;

    public GoalsEPHelper(Context context, Application app) {
        super(context, app);
        weight = new WeightGoalEP(context, app);
    }

    public Map<MedicalConditions, Goal> getAllGoalsConfigured(Long userId) {
        Map<MedicalConditions, Goal> map = new HashMap<MedicalConditions, Goal>();

        map.put(MedicalConditions.Obese, weight.getGoalsForUser(userId));
        /* TODO get other goals too */

        return map;
    }

    public Map<MedicalConditions, List<GoalReadings>> getAllGoalReadings(Long userId) {
        Map<MedicalConditions, List<GoalReadings>> map = new HashMap<MedicalConditions, List<GoalReadings>>();

        map.put(MedicalConditions.Obese, weight.getGoalReadings(userId));

        return map;
    }

    public Goal createGoal(MedicalConditions mc, Goal goal, Long userId) {
        GoalsEP ep = getEndPoint(mc);
        return ep.createGoalForUser(userId, goal);
    }

    public Goal updateGoal(MedicalConditions mc, Goal goal, Long userId) {
        GoalsEP ep = getEndPoint(mc);
        //TODO need to implement update goal
        return null;
    }

    public GoalReadings saveGoalReadings(MedicalConditions mc, GoalReadings reading, Long userId) {
        GoalsEP ep = getEndPoint(mc);
        return ep.createGoalReadings(userId, reading);
    }

    private GoalsEP getEndPoint(MedicalConditions mc) {
        switch(mc){
            case Obese:
                return new WeightGoalEP(this.context, this.ga);

        }

        return null;
    }
}
