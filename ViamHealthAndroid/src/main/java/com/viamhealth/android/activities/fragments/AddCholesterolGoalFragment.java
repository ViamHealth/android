package com.viamhealth.android.activities.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.viamhealth.android.R;
import com.viamhealth.android.model.enums.MedicalConditions;
import com.viamhealth.android.model.goals.BPGoal;
import com.viamhealth.android.model.goals.BPGoalReading;
import com.viamhealth.android.model.goals.CholesterolGoal;
import com.viamhealth.android.model.goals.CholesterolGoalReading;
import com.viamhealth.android.model.goals.Goal;
import com.viamhealth.android.model.goals.GoalReadings;
import com.viamhealth.android.model.users.BMIProfile;

import java.text.ParseException;
import java.util.Date;

/**
 * Created by naren on 10/10/13.
 */
public class AddCholesterolGoalFragment extends AddGoalFragment implements View.OnFocusChangeListener {

    CholesterolGoal goal;

    EditText pHDL, pLDL, pTG, tHDL, tLDL, tTG;
    TextView tTotal, pTotal;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_cholesterol_goal, container, false);

        user = getArguments().getParcelable("user");
        Bundle bundle = getArguments().getBundle("goals");
        if(bundle!=null && !bundle.isEmpty())
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
        pTotal = (TextView) view.findViewById(R.id.add_goal_present_total);
        tTotal = (TextView) view.findViewById(R.id.add_goal_target_total);


        if(user==null)
            return null;

        if(goal!=null){
            isGoalConfigured = true;
            tHDL.setText(String.valueOf(goal.getHdl()));
            tLDL.setText(String.valueOf(goal.getLdl()));
            tTG.setText(String.valueOf(goal.getTriglycerides()));
            if(goal.getTotal()>0){
                tTotal.setVisibility(View.VISIBLE);
                tTotal.setText(getString(R.string.total_cholesterol) + String.valueOf(goal.getTotal()));
            }else{
                tTotal.setVisibility(View.GONE);
            }
            targetDate.setText(formater.format(goal.getTargetDate()));
            ((LinearLayout)view.findViewById(R.id.section_present)).setVisibility(View.GONE);
        }else{
            BMIProfile profile = user.getBmiProfile();
            if(profile.getHdl()>0) pHDL.setText(String.valueOf(profile.getHdl()));
            if(profile.getLdl()>0) pLDL.setText(String.valueOf(profile.getLdl()));
            if(profile.getTriglycerides()>0) pTG.setText(String.valueOf(profile.getTriglycerides()));
            if(profile.getTotalCholesterol()>0) {
                tTotal.setVisibility(View.VISIBLE);
                tTotal.setText(getString(R.string.total_cholesterol) + String.valueOf(profile.getTotalCholesterol()));
            }else{
                tTotal.setVisibility(View.GONE);
            }
        }

        tHDL.setOnFocusChangeListener(this);
        tLDL.setOnFocusChangeListener(this);
        tTG.setOnFocusChangeListener(this);
        pHDL.setOnFocusChangeListener(this);
        pLDL.setOnFocusChangeListener(this);
        pTG.setOnFocusChangeListener(this);

        EditText date = (EditText) view.findViewById(R.id.add_goal_target_date);
        date.setOnFocusChangeListener(mManager);

        return view;
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if(hasFocus){ // if the editTexts got focus
            return;
        }

        int hdl, ldl, tg, total;
        if(v==tHDL || v==tLDL || v==tTG){//the target total needs to be updated
            try {
                hdl = Integer.parseInt(tHDL.getText().toString());
                ldl = Integer.parseInt(tLDL.getText().toString());
                tg = Integer.parseInt(tTG.getText().toString());
            } catch (NumberFormatException e) {
                e.printStackTrace();
                return;
            }
            total = hdl + ldl + tg/5;
            tTotal.setVisibility(View.VISIBLE);
            tTotal.setText(getString(R.string.total_cholesterol) + " " + String.valueOf(total));
        }

        if(v==pHDL || v==pLDL || v==pTG){//the target total needs to be updated
            try {
                hdl = Integer.parseInt(pHDL.getText().toString());
                ldl = Integer.parseInt(pLDL.getText().toString());
                tg = Integer.parseInt(pTG.getText().toString());
            } catch (NumberFormatException e) {
                e.printStackTrace();
                return;
            }
            total = hdl + ldl + tg/5;
            pTotal.setVisibility(View.VISIBLE);
            pTotal.setText(getString(R.string.total_cholesterol) + " " + String.valueOf(total));
        }
    }

    private boolean validation() {
        return true;
    }

    @Override
    public Goal getGoal() {
        CholesterolGoal cholesterolGoal = new CholesterolGoal();
        setDefaultGoalAttributes(goal, cholesterolGoal);
        cholesterolGoal.setHdl(Integer.parseInt(tHDL.getText().toString()));
        cholesterolGoal.setLdl(Integer.parseInt(tLDL.getText().toString()));
        cholesterolGoal.setTriglycerides(Integer.parseInt(tTG.getText().toString()));
        try{
            cholesterolGoal.setTargetDate(formater.parse(targetDate.getText().toString()));
        } catch(ParseException e){
            e.printStackTrace();
        }
        return cholesterolGoal;
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

    @Override
    public boolean isValid() {

        boolean isValid=true;

        if(pHDL.getText().length()==0){
            pHDL.setError("Please fill the value for present HDL");
            isValid=false;
        }

        if(pLDL.getText().length()==0){
            pLDL.setError("Please fill the value for present LDL");
            isValid=false;
        }

        if(pTG.getText().length()==0){
            pTG.setError("Please fill present triglycerides");
            isValid=false;
        }

        if(tHDL.getText().length()==0){
            tHDL.setError("Please fill the value for target HDL");
            isValid=false;
        }

        if(tLDL.getText().length()==0){
            tLDL.setError("Please fill target LDL");
            isValid=false;
        }

        if(tTG.getText().length()==0){
            tTG.setError("Please fill target triglycerides");
            isValid=false;
        }

        if(targetDate.getText().length()==0){
            targetDate.setError("Please fill target date");
            isValid=false;
        }


        return isValid;
    }
}
