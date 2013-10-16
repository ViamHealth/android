package com.viamhealth.android.activities.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.viamhealth.android.Global_Application;
import com.viamhealth.android.R;
import com.viamhealth.android.activities.AddGoalActivity;
import com.viamhealth.android.dao.rest.endpoints.GoalsEP;
import com.viamhealth.android.dao.rest.endpoints.GoalsEPHelper;
import com.viamhealth.android.dao.rest.endpoints.UserEP;
import com.viamhealth.android.model.enums.MedicalConditions;
import com.viamhealth.android.model.goals.Goal;
import com.viamhealth.android.model.goals.GoalReadings;
import com.viamhealth.android.model.goals.WeightGoal;
import com.viamhealth.android.model.goals.WeightGoalReadings;
import com.viamhealth.android.model.users.User;
import com.viamhealth.android.utils.Checker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by naren on 07/10/13.
 */
public class GoalFragment extends Fragment implements View.OnClickListener {

    View view;
    Map<MedicalConditions, List<GoalReadings>> goalReadingsMap = new HashMap<MedicalConditions, List<GoalReadings>>();
    Map<MedicalConditions, Goal> goalsConfiguredMap = new HashMap<MedicalConditions, Goal>();

    GoalsEPHelper goalHelper = null;
    UserEP userEP = null;
    User selectedUser = null;

    ProgressDialog dialog;

    protected static int ACTION_CONFIGURE_GOAL = 100;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.tab_fragment_goal, container, false);

        selectedUser = getArguments().getParcelable("user");

        WebView webView = (WebView) view.findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl("file:///android_asset/weightgoal.html");

        goalHelper = new GoalsEPHelper(getActivity(), (Global_Application)getActivity().getApplicationContext());
        userEP = new UserEP(getActivity(), (Global_Application)getActivity().getApplicationContext());

        dialog = new ProgressDialog(getActivity());
        dialog.setCanceledOnTouchOutside(false);

        if(Checker.isInternetOn(getActivity())){
            GetALLGoals task = new GetALLGoals();
            task.execute();
        } else {
            Toast.makeText(getActivity(), "Internet is not on..", Toast.LENGTH_LONG);
        }
        return view;
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if(newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            ((LinearLayout) view.findViewById(R.id.title_bar)).setVisibility(View.GONE);
        } else {
            ((LinearLayout) view.findViewById(R.id.title_bar)).setVisibility(View.VISIBLE);
        }
    }


    @Override
    public void onClick(View v) {
        Intent i = new Intent(getActivity(), AddGoalActivity.class);
        i.putExtra("user", selectedUser);
        HashMap map = (HashMap) goalsConfiguredMap;
        i.putExtra("goals", map);
        startActivityForResult(i, ACTION_CONFIGURE_GOAL);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==ACTION_CONFIGURE_GOAL){
            if(resultCode==getActivity().RESULT_OK){
                //save the goal and goalReadings
                Goal goal = data.getParcelableExtra("goal");
                GoalReadings readings = data.getParcelableExtra("reading");
                MedicalConditions selectedCondition = (MedicalConditions)data.getSerializableExtra("type");
                if(goal!=null){
                    if(Checker.isInternetOn(getActivity())) {
                        CreateGoal task = new CreateGoal();
                        task.type = selectedCondition;
                        task.reading = readings;
                        task.execute(goal);
                    } else {
                        Toast.makeText(getActivity(), "Internet is not on..", Toast.LENGTH_LONG);
                    }
                }
            }
        }
    }

    public class SaveGoalReading extends AsyncTask<GoalReadings, Void, Void> {

        MedicalConditions type;

        @Override
        protected Void doInBackground(GoalReadings... readings) {
            List<GoalReadings> rds = goalReadingsMap.get(type);
            if(rds==null)
                rds = new ArrayList<GoalReadings>();
            for(int i=0; i<readings.length; i++){
                rds.add(goalHelper.saveGoalReadings(type, readings[i], selectedUser.getId()));
            }
            goalReadingsMap.put(type, rds);
            return null;
        }

        @Override
        protected void onPreExecute() {
            dialog.setMessage("saving the new value...");
            dialog.show();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            dialog.dismiss();
        }
    }

    public class CreateGoal extends AsyncTask<Goal, Void, Void> {

        MedicalConditions type;
        GoalReadings reading;

        @Override
        protected Void doInBackground(Goal... goals) {
            for(int i=0; i<goals.length; i++){
                goalsConfiguredMap.put(type, goalHelper.createGoal(type, goals[i], selectedUser.getId()));
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            dialog.setMessage("saving the new goal...");
            dialog.show();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            dialog.dismiss();
            if(reading!=null){
                SaveGoalReading task = new SaveGoalReading();
                task.type = type;
                task.execute(reading);
            }
        }
    }

    public class GetALLGoals extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            goalsConfiguredMap = goalHelper.getAllGoalsConfigured(selectedUser.getId());
            goalReadingsMap = goalHelper.getAllGoalReadings(selectedUser.getId());
            if(selectedUser.getBmiProfile().isEmpty())
                selectedUser.setBmiProfile(userEP.getUserBMIProfile(selectedUser.getId()));
            return null;
        }

        @Override
        protected void onPreExecute() {
            dialog.setMessage("getting all your goals...");
            dialog.show();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            TextView btnAddGoal = (TextView) view.findViewById(R.id.btn_add_goal);
            btnAddGoal.setOnClickListener(GoalFragment.this);
            dialog.dismiss();
        }
    }
}
