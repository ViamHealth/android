package com.viamhealth.android.activities.fragments;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.viamhealth.android.R;
import com.viamhealth.android.model.enums.MedicalConditions;
import com.viamhealth.android.model.goals.DiabetesGoal;
import com.viamhealth.android.model.goals.DiabetesGoalReading;
import com.viamhealth.android.model.goals.Goal;
import com.viamhealth.android.model.goals.GoalReadings;
import com.viamhealth.android.model.goals.WeightGoal;
import com.viamhealth.android.model.goals.WeightGoalReadings;
import com.viamhealth.android.model.users.User;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by naren on 10/10/13.
 */
public class AddDiabetesGoalFragment extends AddGoalFragment {

    User user;
    boolean isGoalConfigured;

    DiabetesGoal goal;

    View view = null;
    EditText targetDate, pFBS, pRBS, tFBS, tRBS;
    ProgressDialog dialog;

    SimpleDateFormat formater = new SimpleDateFormat("dd/MM/yyyy");

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_add_diabetes_goal, container, false);

        user = getArguments().getParcelable("user");
        //Commenting this losing the edit ability. But doing it, as both create and edit are not working
        Bundle bundle = getArguments().getBundle("goals");
        if(!bundle.isEmpty())
            goal = (DiabetesGoal) bundle.getParcelable(MedicalConditions.Diabetes.name());


        dialog = new ProgressDialog(getActivity());


        targetDate = (EditText) view.findViewById(R.id.add_goal_target_date);
        targetDate.setOnFocusChangeListener(mManager);

        pFBS = (EditText) view.findViewById(R.id.add_goal_present_fbs);
        pRBS = (EditText) view.findViewById(R.id.add_goal_present_rbs);
        tFBS = (EditText) view.findViewById(R.id.add_goal_target_fbs);
        tRBS = (EditText) view.findViewById(R.id.add_goal_target_rbs);

        if(user==null)
            return null;

        if(goal!=null){
            isGoalConfigured = true;
            if(user.getBmiProfile().getFastingSugar()>0) pFBS.setText(String.valueOf(user.getBmiProfile().getFastingSugar()));
            if(user.getBmiProfile().getRandomSugar()>0) pRBS.setText(String.valueOf(user.getBmiProfile().getRandomSugar()));
            tRBS.setText(String.valueOf(goal.getRbs()));
            tFBS.setText(String.valueOf(goal.getFbs()));
            targetDate.setText(formater.format(goal.getTargetDate()));
            //((LinearLayout)view.findViewById(R.id.section_present)).setVisibility(View.GONE);
        } else {
            //tRBS.setText(String.valueOf(140));
            //tFBS.setText(String.valueOf(100));
            if(user.getBmiProfile().getFastingSugar()>0) pFBS.setText(String.valueOf(user.getBmiProfile().getFastingSugar()));
            if(user.getBmiProfile().getRandomSugar()>0) pRBS.setText(String.valueOf(user.getBmiProfile().getRandomSugar()));
        }

        pFBS.requestFocus();
        return view;
    }

    private boolean validation() {
        return true;
    }

    @Override
    public Goal getGoal() {
        DiabetesGoal dbgoal = new DiabetesGoal();
        setDefaultGoalAttributes(goal, dbgoal);
        dbgoal.setFbs(Integer.parseInt(tFBS.getText().toString()));
        dbgoal.setRbs(Integer.parseInt(tRBS.getText().toString()));
        try{
            dbgoal.setTargetDate(formater.parse(targetDate.getText().toString()));
        } catch(ParseException e){
            e.printStackTrace();
        }
        return dbgoal;
    }

    @Override
    public GoalReadings getGoalReadings() {
        if(!isGoalConfigured) {
            DiabetesGoalReading readings = new DiabetesGoalReading();
            readings.setRbs(Integer.parseInt(pRBS.getText().toString()));
            readings.setFbs(Integer.parseInt(pFBS.getText().toString()));
            readings.setReadingDate(new Date());
            return readings;
        }
        return null;
    }

    @Override
    public boolean isValid() {
        boolean isValid=true;


        if(pFBS.getText().length()==0){
            pFBS.setError("Please fill the value for fasting blood sugar");
            isValid=false;
        }
        if(pRBS.getText().length()==0){
            pRBS.setError("Please fill the value for random blood sugar");
            isValid=false;
        }
        if(tFBS.getText().length()==0){
            tFBS.setError("Please fill the value for target fasting  blood sugar");
            isValid=false;
        }
        if(tRBS.getText().length()==0){
            tRBS.setError("Please fill the value for target random blood sugar");
            isValid=false;
        }

        if(targetDate.getText().length()==0){
            targetDate.setError("Please fill the value for target date");
            isValid=false;
        }

        return isValid;
    }
}
