package com.viamhealth.android.activities.fragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.viamhealth.android.Global_Application;
import com.viamhealth.android.R;
import com.viamhealth.android.dao.rest.endpoints.GoalsEP;
import com.viamhealth.android.dao.rest.endpoints.UserEP;
import com.viamhealth.android.dao.rest.endpoints.WeightGoalEP;
import com.viamhealth.android.model.enums.MedicalConditions;
import com.viamhealth.android.model.goals.Goal;
import com.viamhealth.android.model.goals.GoalReadings;
import com.viamhealth.android.model.goals.WeightGoal;
import com.viamhealth.android.model.goals.WeightGoalReadings;
import com.viamhealth.android.model.users.User;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Weeks;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by naren on 10/10/13.
 */
public class AddWeightGoalFragment extends AddGoalFragment implements View.OnFocusChangeListener {

    EditText pHeight, pWeight, tWeight, targetDate;
    User user = null;

    boolean needHeightInput = false, isGoalConfigured = false;
    UserEP userEndPoint;
    WeightGoalEP goalsEndPoint;
    WeightGoal goal;

    ProgressDialog dialog;
    SimpleDateFormat formater = new SimpleDateFormat("dd/MM/yyyy");

    final Double idealWeightPerWeek = 0.5;

    ImageView warningImage;
    TextView warningText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_add_weight_goal, container, false);

        user = getArguments().getParcelable("user");
        Bundle bundle = getArguments().getBundle("goals");
        if(!bundle.isEmpty())
            goal = (WeightGoal) bundle.getParcelable(MedicalConditions.Obese.name());

        dialog = new ProgressDialog(getActivity());

        targetDate = (EditText) view.findViewById(R.id.add_goal_target_date);
        targetDate.setOnFocusChangeListener(mManager);


        pHeight = (EditText) view.findViewById(R.id.add_goal_height_input);
        pWeight = (EditText) view.findViewById(R.id.add_goal_weight_input);
        tWeight = (EditText) view.findViewById(R.id.add_goal_target_weight);
        warningImage = (ImageView) view.findViewById(R.id.imgWarning);
        warningText = (TextView) view.findViewById(R.id.tvWarning);
        warningImage.setVisibility(View.GONE);

        if(user==null)
            return null;

        if(goal!=null){
            isGoalConfigured = true;
            tWeight.setText(goal.getWeight().toString());
            targetDate.setText(formater.format(goal.getTargetDate()));

            pHeight.setText(String.valueOf(user.getBmiProfile().getHeight()));
            pWeight.setVisibility(View.GONE);
            pHeight.setEnabled(false);
        } else {//if goal is not yet configured
            if(user.getBmiProfile().getHeight()>0){
                pHeight.setText(user.getBmiProfile().getHeight().toString());
                pHeight.setEnabled(false);
                pWeight.setText(user.getBmiProfile().getWeight().toString());
                pWeight.setEnabled(false);
                DecimalFormat d=new DecimalFormat("0.0");
                tWeight.setText(d.format(getIdealTargetWeight(user.getBmiProfile().getHeight(), user.getBmiProfile().getWeight())));
                targetDate.setText(formater.format(getIdealTargetDate()));
                if(getIdealTargetWeight(user.getBmiProfile().getHeight(), user.getBmiProfile().getWeight())>=user.getBmiProfile().getWeight())
                {
                    warningText.setText("");
                }

            }
        }

        tWeight.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus && targetDate.getText().length()==0){
                    targetDate.setText(formater.format(getIdealTargetDate()));
                }
            }
        });

        pHeight.requestFocus();
        return view;
    }


    @Override
    public void onTargetDateChange() {
        /*double targetWeight = Double.parseDouble(tWeight.getText().toString());
        double presentWeight = user.getBmiProfile().getWeight();
        if(presentWeight-targetWeight<=1){
            warningImage.setVisibility(View.GONE);
            warningText.setVisibility(View.GONE);
            return;
        }*/
        /*double weightDiffPerWeek = getWeightDiffPerWeek();
        if(weightDiffPerWeek > 1.0){
            warningImage.setVisibility(View.VISIBLE);
            warningText.setText(String.format(getString(R.string.weightGoalUnhealthyWarning), weightDiffPerWeek));
        }else if(weightDiffPerWeek > 0.5){
            warningImage.setVisibility(View.VISIBLE);
            warningText.setText(String.format(getString(R.string.weightGoalAggressiveWarning), weightDiffPerWeek));
        }else{
            warningImage.setVisibility(View.GONE);
            warningText.setText(String.format(getString(R.string.weightGoalIdealTargetYourDate), weightDiffPerWeek));
        }*/
    }

    public void onFocusChange(View v, boolean hasFocus) {
        if(hasFocus)
            return;

        if(pHeight.getText().length()==0 || pWeight.getText().length()==0)
            return;

        int height = Integer.parseInt(pHeight.getText().toString());
        double weight = Double.parseDouble(pWeight.getText().toString());
        DecimalFormat d=new DecimalFormat("0.0");
        tWeight.setText(d.format((getIdealTargetWeight(height, weight))));
    }

    private Double getIdealTargetWeight(Integer height, Double weight) {
        double heightInM = height.doubleValue()/100;
        double idealBMI = 22.5;
        double idealWeight = idealBMI * heightInM * heightInM;
        return idealWeight;
    }

    private Date getIdealTargetDate() {
        DateTime start = new DateTime();
        double targetWeight = Double.parseDouble(tWeight.getText().toString());
        double presentWeight = user.getBmiProfile().getWeight();
        double weightDiff = Math.abs(targetWeight - presentWeight);

        Double idealTWD = Math.ceil(weightDiff / idealWeightPerWeek);
        int idealTargetWeeks = idealTWD.intValue();

        DateTime idealTD = start.plusWeeks(idealTargetWeeks);

        return new Date(idealTD.getMillis());
    }

    private double getWeightDiffPerWeek() {
        DateTime start = new DateTime();
        try {
            DateTime end = new DateTime(formater.parse(targetDate.getText().toString()).getTime());
            int weeksForTarget = Weeks.weeksBetween(start, end).getWeeks();
            weeksForTarget = Math.abs(weeksForTarget);

            double targetWeight = Double.parseDouble(tWeight.getText().toString());
            double presentWeight = user.getBmiProfile().getWeight();
            double weightDiff = Math.abs(targetWeight - presentWeight);

            return weightDiff/weeksForTarget;
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
    }


    @Override
    public boolean isValid() {
        boolean isValid=true;

        if(pHeight.getText().length()==0){
            pHeight.setError("Please fill the value for present height");
            isValid=false;
        }
        if(pWeight.getText().length()==0){
            tWeight.setError("Please fill the value for present weight");
            isValid=false;
        }
        if(tWeight.getText().length()==0){
            tWeight.setError("Please fill the value for target weight");
            isValid=false;
        }
        if(targetDate.getText().length()==0){
            targetDate.setError("Please fill the value for target date");
            isValid=false;
        }

        return isValid;
    }



    @Override
    public Goal getGoal() {
        WeightGoal weightGoal = new WeightGoal();
        setDefaultGoalAttributes(goal, weightGoal);
        weightGoal.setWeight(Double.parseDouble(tWeight.getText().toString()));
        try{
            weightGoal.setTargetDate(formater.parse(targetDate.getText().toString()));
        } catch(ParseException e){
            e.printStackTrace();
        }
        return weightGoal;
    }

    @Override
    public GoalReadings getGoalReadings() {
        if(!isGoalConfigured) {
            WeightGoalReadings readings = new WeightGoalReadings();
            readings.setHeight(Integer.parseInt(pHeight.getText().toString()));
            readings.setWeight(Double.parseDouble(pWeight.getText().toString()));
            readings.setReadingDate(new Date());
            return readings;
        }

        return null;
    }
}
