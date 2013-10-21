package com.viamhealth.android.dao.rest.endpoints;

import android.app.Application;
import android.content.Context;

import com.viamhealth.android.model.enums.MedicalConditions;
import com.viamhealth.android.model.goals.BPGoal;
import com.viamhealth.android.model.goals.CholesterolGoal;
import com.viamhealth.android.model.goals.DiabetesGoal;
import com.viamhealth.android.model.goals.Goal;
import com.viamhealth.android.model.goals.GoalReadings;
import com.viamhealth.android.model.goals.WeightGoal;

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

    public GoalsEPHelper(Context context, Application app) {
        super(context, app);
        weight = new WeightGoalEP(context, app);
        sugar = new DiabetesGoalEP(context, app);
        bp = new BPGoalEP(context, app);
        cholesterol = new CholesterolGoalEP(context, app);
    }

    public Map<MedicalConditions, Goal> getAllGoalsConfigured(Long userId) {
        Map<MedicalConditions, Goal> map = new LinkedHashMap<MedicalConditions, Goal>();

        WeightGoal weightGoal = (WeightGoal)weight.getGoalsForUser(userId);
        if(weightGoal != null)
            map.put(MedicalConditions.Obese, weightGoal);

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
