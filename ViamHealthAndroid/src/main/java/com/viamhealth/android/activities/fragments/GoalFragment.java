package com.viamhealth.android.activities.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.ConsoleMessage;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.viamhealth.android.Global_Application;
import com.viamhealth.android.R;
import com.viamhealth.android.activities.AddGoalActivity;
import com.viamhealth.android.activities.AddGoalValue;
import com.viamhealth.android.dao.rest.endpoints.GoalsEPHelper;
import com.viamhealth.android.dao.rest.endpoints.UserEP;
import com.viamhealth.android.model.enums.MedicalConditions;
import com.viamhealth.android.model.goals.Goal;
import com.viamhealth.android.model.goals.GoalReadings;
import com.viamhealth.android.model.users.User;
import com.viamhealth.android.utils.Checker;
import com.viamhealth.android.utils.JsonGraphDataBuilder;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by naren on 07/10/13.
 */
public class GoalFragment extends Fragment implements View.OnClickListener {

    Map<MedicalConditions, List<GoalReadings>> goalReadingsMap = new HashMap<MedicalConditions, List<GoalReadings>>();
    Map<MedicalConditions, Goal> goalsConfiguredMap = new LinkedHashMap<MedicalConditions, Goal>();
    Map<MedicalConditions, List<JsonGraphDataBuilder.JsonOutput.GraphSeries>> supportedSeries = new HashMap<MedicalConditions, List<JsonGraphDataBuilder.JsonOutput.GraphSeries>>();
    Map<Integer, GraphFragment> graphFragments = new HashMap<Integer, GraphFragment>();
    Map<MedicalConditions, OnGoalDataChangeListener> listenersSubscribed = new HashMap<MedicalConditions, OnGoalDataChangeListener>();

    GoalsEPHelper goalHelper = null;
    UserEP userEP = null;
    User selectedUser = null;

    View view;
    ProgressDialog dialog;
    ViewPager mPager;
    WebViewFragmentPagerAdapter mPagerAdapter;
    //WebView webView;
    //ImageButton addValue;

    final int ACTION_CONFIGURE_GOAL = 100;
    final int ACTION_ADD_GOAL_VALUE = 200;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.tab_fragment_goal, container, false);

        selectedUser = getArguments().getParcelable("user");

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

        /** build the supported series map **/

        return view;
    }

    /**
     * target-date
     * goal-specific-targets
     * goal-readings
     * goal-specific-healthy-range
     *
     * @param index
     * @return
     */
    public String getDataForGraph(MedicalConditions mc) {

        List<GoalReadings> readings = goalReadingsMap.get(mc);
        Goal goal = goalsConfiguredMap.get(mc);

        JsonGraphDataBuilder builder = new JsonGraphDataBuilder();
        builder.write("goal", goal, null)
               .write("seriesA", readings, JsonGraphDataBuilder.JsonOutput.GraphSeries.A);

        if(mc!=MedicalConditions.Obese)
            builder.write("seriesB", readings, JsonGraphDataBuilder.JsonOutput.GraphSeries.B);

        if(mc==MedicalConditions.Cholesterol){
            builder.write("seriesC", readings, JsonGraphDataBuilder.JsonOutput.GraphSeries.C);
            builder.write("seriesD", readings, JsonGraphDataBuilder.JsonOutput.GraphSeries.D);
        }
        return builder.toString();

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
        i.putExtra("goals", getBundleFromMap(goalsConfiguredMap));
        startActivityForResult(i, ACTION_CONFIGURE_GOAL);
    }

    private Bundle getBundleFromMap(Map<MedicalConditions, Goal> map) {
        if(map==null)
            return new Bundle();

        Set<MedicalConditions> keySet = map.keySet();
        Bundle bundle = new Bundle();
        for(MedicalConditions key: keySet) {
            bundle.putParcelable(key.name(), map.get(key));
        }
        return bundle;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode==getActivity().RESULT_OK) {
            if(requestCode==ACTION_CONFIGURE_GOAL){
                //save the goal and goalReadings
                Goal goal = data.getParcelableExtra("goal");
                GoalReadings readings = data.getParcelableExtra("goalReading§");
                MedicalConditions selectedCondition = (MedicalConditions)data.getSerializableExtra("type");
                if(goal!=null){
                    if(Checker.isInternetOn(getActivity())) {
                        CreateGoal task = new CreateGoal();
                        task.type = selectedCondition;
                        task.reading = readings;
                        task.execute(goal);
                    } else {
                        Toast.makeText(getActivity(), "Internet is not on..", Toast.LENGTH_LONG).show();
                    }
                }
            }

            if(requestCode == ACTION_ADD_GOAL_VALUE) {
                GoalReadings reading = data.getParcelableExtra("reading");
                MedicalConditions selectedCondition = (MedicalConditions) data.getSerializableExtra("type");
                if(reading!=null){
                    if(Checker.isInternetOn(getActivity())) {
                        SaveGoalReading task = new SaveGoalReading();
                        task.type = selectedCondition;
                        task.execute(reading);
                    }else{
                        Toast.makeText(getActivity(), "Internet is not on..", Toast.LENGTH_LONG).show();
                    }
                }
            }
        }
    }

    public void setOnGoalDataChangeListener(MedicalConditions mc, OnGoalDataChangeListener listener) {
        listenersSubscribed.put(mc, listener);
    }

    private String getReadingJSON(MedicalConditions mc, GoalReadings newReadings) {
        JsonGraphDataBuilder builder = new JsonGraphDataBuilder();
        builder.write("seriesA", newReadings, JsonGraphDataBuilder.JsonOutput.GraphSeries.A);

        if(mc!=MedicalConditions.Obese)
            builder.write("seriesB", newReadings, JsonGraphDataBuilder.JsonOutput.GraphSeries.B);

        if(mc==MedicalConditions.Cholesterol){
            builder.write("seriesC", newReadings, JsonGraphDataBuilder.JsonOutput.GraphSeries.C);
            builder.write("seriesD", newReadings, JsonGraphDataBuilder.JsonOutput.GraphSeries.D);
        }
        return builder.toString();
    }

    private void onGoalReadingAdded(MedicalConditions mc, GoalReadings newReading) {
        OnGoalDataChangeListener listener = listenersSubscribed.get(mc);

        if(listener != null){
            listener.onAdd(getReadingJSON(mc, newReading));
        }
        else
            mPagerAdapter.notifyDataSetChanged();

    }

    private void onGoalDataChanged(MedicalConditions mc){
        OnGoalDataChangeListener listener = listenersSubscribed.get(mc);

        if(listener != null){
            listener.onChange(getDataForGraph(mc));
        }
        else
            mPagerAdapter.notifyDataSetChanged();
    }

    public interface OnGoalDataChangeListener {
        public void onChange(String json);
        public void onAdd(String json);
        //public void onUpdate(String json);
    }
    /**
     * A simple pager adapter that represents 5 ScreenSlidePageFragment objects, in
     * sequence.
     */
    private class WebViewFragmentPagerAdapter extends FragmentPagerAdapter {


        public WebViewFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            GraphFragment fragment = graphFragments.get(position);
            if(fragment == null) {
                Bundle args = new Bundle();
                final MedicalConditions mc = (MedicalConditions)goalsConfiguredMap.keySet().toArray()[position];
                args.putSerializable("type", mc);
                args.putString("json", getDataForGraph(mc));
                fragment = (GraphFragment)Fragment.instantiate(getActivity(), GraphFragment.class.getName(), args);
                setOnGoalDataChangeListener(mc, fragment);
                fragment.setOnClickAddValueListener(new GraphFragment.OnClickAddValueListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(getActivity(), AddGoalValue.class);
                        i.putExtra("type", mc);
                        startActivityForResult(i, ACTION_ADD_GOAL_VALUE);
                    }
                });
            }
            return fragment;
        }

        @Override
        public int getCount() {
            return goalsConfiguredMap.keySet().size();
        }
    }

    public class SaveGoalReading extends AsyncTask<GoalReadings, Void, GoalReadings> {

        MedicalConditions type;

        @Override
        protected GoalReadings doInBackground(GoalReadings... readings) {
            List<GoalReadings> rds = goalReadingsMap.get(type);
            GoalReadings reading = null;
            if(rds==null)
                rds = new ArrayList<GoalReadings>();
            for(int i=0; i<readings.length; i++){
                reading = goalHelper.saveGoalReadings(type, readings[i], selectedUser.getId());
                rds.add(reading);
            }
            goalReadingsMap.put(type, rds);

            return reading;
        }

        @Override
        protected void onPreExecute() {
            dialog.setMessage("saving the new value...");
            dialog.show();
        }

        @Override
        protected void onPostExecute(GoalReadings reading) {
            //TODO need to fix this - onGoalReadingAdded(type, reading);
            //for temporary fix
            onGoalDataChanged(type);
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
            onGoalDataChanged(type);
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

            mPager = (ViewPager) view.findViewById(R.id.pager);
            mPagerAdapter = new WebViewFragmentPagerAdapter(getChildFragmentManager());
            mPager.setAdapter(mPagerAdapter);

            TextView btnAddGoal = (TextView) view.findViewById(R.id.btn_add_goal);
            btnAddGoal.setOnClickListener(GoalFragment.this);
            dialog.dismiss();
        }
    }


}