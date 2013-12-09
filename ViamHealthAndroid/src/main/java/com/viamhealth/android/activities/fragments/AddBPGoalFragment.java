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
import com.viamhealth.android.model.goals.DiabetesGoal;
import com.viamhealth.android.model.goals.DiabetesGoalReading;
import com.viamhealth.android.model.goals.Goal;
import com.viamhealth.android.model.goals.GoalReadings;
import com.viamhealth.android.model.users.User;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by naren on 10/10/13.
 */
public class AddBPGoalFragment extends AddGoalFragment {

    BPGoal goal;

    EditText pSP, pDP, pPR, tSP, tDP, tPR;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_bp_goal, container, false);

        user = getArguments().getParcelable("user");
        Bundle bundle = getArguments().getBundle("goals");
        if(!bundle.isEmpty())
            goal = (BPGoal) bundle.getParcelable(MedicalConditions.BloodPressure.name());

        dialog = new ProgressDialog(getActivity());

        targetDate = (EditText) view.findViewById(R.id.add_goal_target_date);
        targetDate.setOnFocusChangeListener(mManager);

        pSP = (EditText) view.findViewById(R.id.add_goal_present_sp);
        pDP = (EditText) view.findViewById(R.id.add_goal_present_dp);
        pPR = (EditText) view.findViewById(R.id.add_goal_present_pr);
        tSP = (EditText) view.findViewById(R.id.add_goal_target_sp);
        tDP = (EditText) view.findViewById(R.id.add_goal_target_dp);
        tPR = (EditText) view.findViewById(R.id.add_goal_target_pr);

        if(user==null)
            return null;

        if(goal!=null){
            isGoalConfigured = true;
            tSP.setText(goal.getSystolicPressure());
            tDP.setText(goal.getDiastolicPressure());
            tPR.setText(goal.getPulseRate());
            targetDate.setText(formater.format(goal.getTargetDate()));
            ((LinearLayout)view.findViewById(R.id.section_present)).setVisibility(View.GONE);
        }else{
            if(user.getBmiProfile().getSystolicPressure()>0) pSP.setText(String.valueOf(user.getBmiProfile().getSystolicPressure()));
            if(user.getBmiProfile().getDiastolicPressure()>0) pDP.setText(String.valueOf(user.getBmiProfile().getDiastolicPressure()));
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
        BPGoal bpGoal = new BPGoal();
        setDefaultGoalAttributes(goal, bpGoal);
        bpGoal.setSystolicPressure(Integer.parseInt(tSP.getText().toString()));
        bpGoal.setDiastolicPressure(Integer.parseInt(tDP.getText().toString()));
        //goal.setPulseRate(Integer.parseInt(tPR.getText().toString()));
        try{
            bpGoal.setTargetDate(formater.parse(targetDate.getText().toString()));
        } catch(ParseException e){
            e.printStackTrace();
        }
        return bpGoal;
    }

    @Override
    public GoalReadings getGoalReadings() {
        if(!isGoalConfigured) {
            BPGoalReading readings = new BPGoalReading();
            readings.setSystolicPressure(Integer.parseInt(pSP.getText().toString()));
            readings.setDiastolicPressure(Integer.parseInt(pDP.getText().toString()));
            //readings.setPulseRate(Integer.parseInt(pPR.getText().toString()));
            readings.setReadingDate(new Date());
            return readings;
        }
        return null;
    }
}
