package com.viamhealth.android.activities.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.viamhealth.android.Global_Application;
import com.viamhealth.android.R;
import com.viamhealth.android.dao.rest.endpoints.GoalsEP;
import com.viamhealth.android.dao.rest.endpoints.UserEP;
import com.viamhealth.android.model.goals.WeightGoal;
import com.viamhealth.android.model.users.User;

import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Created by naren on 10/10/13.
 */
public class AddWeightGoalFragment extends AddGoalFragment implements View.OnFocusChangeListener {

    EditText pHeight, pWeight, tWeight, targetDate;
    User user = null;

    boolean needHeightInput = false;
    UserEP userEndPoint;
    GoalsEP goalsEndPoint;

    ProgressDialog dialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_add_weight_goal, container, false);

        user = getArguments().getParcelable("user");
        dialog = new ProgressDialog(getActivity());

        targetDate = (EditText) view.findViewById(R.id.add_goal_target_date);
        targetDate.setOnFocusChangeListener(mManager);

        pHeight = (EditText) view.findViewById(R.id.add_goal_height_input);
        pWeight = (EditText) view.findViewById(R.id.add_goal_weight_input);
        tWeight = (EditText) view.findViewById(R.id.add_goal_target_weight);

        if(user==null)
            return null;

        if(user.getBmiProfile().getHeight()==0)
            needHeightInput = true;
        else{
            pHeight.setText(user.getBmiProfile().getHeight());
            pHeight.setOnFocusChangeListener(this);
            pWeight.setText(user.getBmiProfile().getWeight().toString());
            pWeight.setOnFocusChangeListener(this);
            tWeight.setText((getIdealTargetWeight(user.getBmiProfile().getHeight(), user.getBmiProfile().getWeight())).toString());
        }

        return view;
    }

    public void onFocusChange(View v, boolean hasFocus) {
        if(hasFocus)
            return;

        if(pHeight.getText().length()==0 || pWeight.getText().length()==0)
            return;

        int height = Integer.parseInt(pHeight.getText().toString());
        double weight = Double.parseDouble(pWeight.getText().toString());
        tWeight.setText((getIdealTargetWeight(height, weight)).toString());
    }

    private Double getIdealTargetWeight(Integer height, Double weight) {
        double heightInM = height/100;
        double idealBMI = 22.5;
        double idealWeight = idealBMI * heightInM * heightInM;
        return idealWeight;
    }

    @Override
    public void onSave() {
        if(needHeightInput){
            user.getBmiProfile().setHeight(Integer.parseInt(pHeight.getText().toString()));
            user.getBmiProfile().setWeight(Double.parseDouble(pWeight.getText().toString()));
        }

        WeightGoal goal = new WeightGoal();
        goal.setWeight(Double.parseDouble(tWeight.getText().toString()));
        try{
            SimpleDateFormat formater = new SimpleDateFormat("dd/MM/yyyy");
            goal.setTargetDate(formater.parse(targetDate.getText().toString()));
        } catch(ParseException e){
            e.printStackTrace();
        }
        updateUserBMIProfile(goal);
    }

    private boolean validation() {
        return true;
    }

    private void updateUserBMIProfile(WeightGoal goal) {
        if(validation()){
            if(isInternetOn()){
                userEndPoint=new UserEP(getActivity(), (Global_Application)getActivity().getApplicationContext());
                goalsEndPoint = new GoalsEP(getActivity(), (Global_Application)getActivity().getApplicationContext());
                UserBMISaveTask task = new UserBMISaveTask();
                task.activity = getActivity();
                task.execute(goal);
            }
        }
    }

    public class UserBMISaveTask extends AsyncTask<WeightGoal, Integer, Integer> {

        protected FragmentActivity activity;

        @Override
        protected Integer doInBackground(WeightGoal... params) {
            int count = params.length;
            long totalSize = 0;
            user.setBmiProfile(userEndPoint.updateBMIProfile(user.getId(), user.getBmiProfile()));
            for (int i = 0; i < count; i++) {
                WeightGoal goal = params[i];
                goal = goalsEndPoint.createWeightGoalForUser(user.getId(), goal);
            }
            return 1;
        }

        @Override
        protected void onPreExecute() {
            dialog.setMessage("updating the system...");
            dialog.show();
            Log.i("onPreExecute", "onPreExecute");
        }

        @Override
        protected void onPostExecute(Integer integer) {
            dialog.dismiss();
            activity.finish();
        }
    }
}
