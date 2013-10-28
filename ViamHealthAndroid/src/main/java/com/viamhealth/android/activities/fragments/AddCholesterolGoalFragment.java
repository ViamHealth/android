package com.viamhealth.android.activities.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.viamhealth.android.R;
import com.viamhealth.android.model.enums.MedicalConditions;
import com.viamhealth.android.model.goals.BPGoal;
import com.viamhealth.android.model.goals.BPGoalReading;
import com.viamhealth.android.model.goals.CholesterolGoal;
import com.viamhealth.android.model.goals.CholesterolGoalReading;
import com.viamhealth.android.model.goals.Goal;
import com.viamhealth.android.model.goals.GoalReadings;

import java.text.ParseException;
import java.util.Date;

/**
 * Created by naren on 10/10/13.
 */
public class AddCholesterolGoalFragment extends AddGoalFragment {

    CholesterolGoal goal;

    EditText pHDL, pLDL, pTG, tHDL, tLDL, tTG;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_cholesterol_goal, container, false);

        user = getArguments().getParcelable("user");
        Bundle bundle = getArguments().getBundle("goals");
        if(!bundle.isEmpty())
            goal = (CholesterolGoal) bundle.getParcelable(MedicalConditions.Cholesterol.name());

        dialog = new ProgressDialog(getActivity());

        targetDate = (EditText) view.findViewById(R.id.add_goal_target_date);
        targetDate.setOnFocusChangeListener(mManager);

        pHDL = (EditText) view.findViewById(R.id.add_goal_present_hdl);
        pLDL = (EditText) view.findViewById(R.id.add_goal_present_ldl);
        pTG = (EditText) view.findViewById(R.id.add_goal_present_try);
        tHDL = (EditText) view.findViewById(R.id.add_goal_target_hdl);
        tLDL = (EditText) view.findViewById(R.id.add_goal_target_ldl);
        tTG = (EditText) view.findViewById(R.id.add_goal_target_try);

        if(user==null)
            return null;

        if(goal!=null){
            isGoalConfigured = true;
            tHDL.setText(goal.getHdl());
            tLDL.setText(goal.getLdl());
            tTG.setText(goal.getTriglycerides());
            targetDate.setText(formater.format(goal.getTargetDate()));
            ((LinearLayout)view.findViewById(R.id.section_present)).setVisibility(View.GONE);
        }

        EditText date = (EditText) view.findViewById(R.id.add_goal_target_date);
        date.setOnFocusChangeListener(mManager);

        return view;
    }

    private boolean validation() {
        return true;
    }

    @Override
    public Goal getGoal() {
        CholesterolGoal goal = new CholesterolGoal();
        goal.setHdl(Integer.parseInt(tHDL.getText().toString()));
        goal.setLdl(Integer.parseInt(tLDL.getText().toString()));
        goal.setTriglycerides(Integer.parseInt(tTG.getText().toString()));
        try{
            goal.setTargetDate(formater.parse(targetDate.getText().toString()));
        } catch(ParseException e){
            e.printStackTrace();
        }
        return goal;
    }

    @Override
    public GoalReadings getGoalReadings() {
        if(!isGoalConfigured) {
            CholesterolGoalReading readings = new CholesterolGoalReading();
            readings.setHdl(Integer.parseInt(pHDL.getText().toString()));
            readings.setLdl(Integer.parseInt(pLDL.getText().toString()));
            readings.setTriglycerides(Integer.parseInt(pTG.getText().toString()));
            readings.setReadingDate(new Date());
            return readings;
        }
        return null;
    }
}
