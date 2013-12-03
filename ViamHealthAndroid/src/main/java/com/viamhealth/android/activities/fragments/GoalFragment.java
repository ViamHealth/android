package com.viamhealth.android.activities.fragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.viamhealth.android.Global_Application;
import com.viamhealth.android.R;
import com.viamhealth.android.ViamHealthPrefs;
import com.viamhealth.android.activities.AddGoalActivity;
import com.viamhealth.android.activities.AddGoalValue;
import com.viamhealth.android.activities.TabActivity;
import com.viamhealth.android.dao.rest.endpoints.GoalsEPHelper;
import com.viamhealth.android.dao.rest.endpoints.UserEP;
import com.viamhealth.android.model.enums.MedicalConditions;
import com.viamhealth.android.model.goals.Goal;
import com.viamhealth.android.model.goals.GoalReadings;
import com.viamhealth.android.model.goals.WeightGoal;
import com.viamhealth.android.model.users.User;
import com.viamhealth.android.ui.viewpagerindicator.CirclePageIndicator;
import com.viamhealth.android.utils.BMRCalculator;
import com.viamhealth.android.utils.Checker;
import com.viamhealth.android.utils.JsonGraphDataBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

//import com.viewpagerindicator.CirclePageIndicator;
/**
 * Created by naren on 07/10/13.
 */
public class GoalFragment extends BaseFragment implements View.OnClickListener {

    //Map<MedicalConditions, List<GoalReadings>> goalReadingsMap = new HashMap<MedicalConditions, List<GoalReadings>>();
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

    FrameLayout initial_layout;
    RelativeLayout final_layout;

    ViamHealthPrefs appPrefs;
    Global_Application ga=null;

    ActionBar actionBar;

    //WebView webView;
    //ImageButton addValue;

    final int ACTION_CONFIGURE_GOAL = 100;
    final int ACTION_ADD_GOAL_VALUE = 200;

    TabActivity.Actions action = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        action = (TabActivity.Actions) getArguments().getSerializable("action");
        selectedUser = getArguments().getParcelable("user");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.tab_fragment_goal, container, false);

        goalHelper = new GoalsEPHelper(getSherlockActivity(), (Global_Application)getSherlockActivity().getApplicationContext());
        userEP = new UserEP(getSherlockActivity(), (Global_Application)getSherlockActivity().getApplicationContext());
        appPrefs = new ViamHealthPrefs(getSherlockActivity());

        actionBar = getSherlockActivity().getSupportActionBar();

        dialog = new ProgressDialog(getSherlockActivity());
        dialog.setCanceledOnTouchOutside(false);

        final_layout = (RelativeLayout) view.findViewById(R.id.final_layout);
        final_layout.setVisibility(View.GONE);

        initial_layout = (FrameLayout) view.findViewById(R.id.initial_layout);
        initial_layout.setVisibility(View.GONE);

        mPager = (ViewPager) final_layout.findViewById(R.id.pager);
        mPagerAdapter = new WebViewFragmentPagerAdapter(getChildFragmentManager());
        mPager.setAdapter(mPagerAdapter);
        ga=((Global_Application)getSherlockActivity().getApplicationContext());

        CirclePageIndicator circlePageIndicator = (CirclePageIndicator) final_layout.findViewById(R.id.titles);
        circlePageIndicator.setViewPager(mPager);

        if(action == TabActivity.Actions.SetGoal){
            addNewGoal();
            action = null;
        }else{
            if(Checker.isInternetOn(getSherlockActivity())){
                GetALLGoals task = new GetALLGoals();
                task.execute();
            } else {
                Toast.makeText(getSherlockActivity(), "Internet is not on..", Toast.LENGTH_LONG);
            }
        }

        setHasOptionsMenu(true);

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.add(Menu.NONE, R.drawable.ic_action_goal, 10, "New Goal")
                .setIcon(R.drawable.ic_action_goal)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.drawable.ic_action_goal){
            addNewGoal();
            return false;
        }
        return super.onOptionsItemSelected(item);
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

        if(!goalsConfiguredMap.containsKey(mc))
            return null;

        Goal goal = goalsConfiguredMap.get(mc);
        List<GoalReadings> readings = goal.getReadings();


        JsonGraphDataBuilder builder = new JsonGraphDataBuilder();

        builder.write("goal", goal, null)
               .write("seriesA", readings, JsonGraphDataBuilder.JsonOutput.GraphSeries.A);

        if(mc!=MedicalConditions.Obese)
            builder.write("seriesB", readings, JsonGraphDataBuilder.JsonOutput.GraphSeries.B);

        if(mc==MedicalConditions.Cholesterol){
            builder.write("seriesC", readings, JsonGraphDataBuilder.JsonOutput.GraphSeries.C);
            //builder.write("seriesD", readings, JsonGraphDataBuilder.JsonOutput.GraphSeries.D);
        }

        builder.writeYAxisExtras(goal);

        return builder.toString();

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    public void addNewGoal() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getSherlockActivity());
        builder.setTitle("Set Goals for...");
        String[] mcs = getMedicalConditions(getBundleFromMap(goalsConfiguredMap));
        if(mcs==null || mcs.length==0){
            builder.setMessage("There are no more goals to configure. Lets wait for some to be completed!");
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
        }else{
            final String[] items = Arrays.copyOf(mcs, mcs.length);
            builder.setItems(items, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int item) {
                    MedicalConditions selectedMC = MedicalConditions.None;
                    if (items[item].equals(getString(MedicalConditions.Diabetes.key()))) {
                        selectedMC = MedicalConditions.Diabetes;
                    } else if (items[item].equals(getString(MedicalConditions.Cholesterol.key()))) {
                        selectedMC = MedicalConditions.Cholesterol;
                    } else if (items[item].equals(getString(MedicalConditions.BloodPressure.key()))) {
                        selectedMC = MedicalConditions.BloodPressure;
                    } else if (items[item].equals(getString(MedicalConditions.Obese.key()))) {
                        selectedMC = MedicalConditions.Obese;
                    }

                    Intent i = new Intent(getSherlockActivity(), AddGoalActivity.class);
                    i.putExtra("user", selectedUser);
                    i.putExtra("goals", getBundleFromMap(goalsConfiguredMap));
                    i.putExtra("type", selectedMC);
                    startActivityForResult(i, ACTION_CONFIGURE_GOAL);
                }
            });
        }
        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                //finish();
            }
        });
        builder.show();
    }

    private String[] getMedicalConditions(Bundle goalsConfigued) {
        MedicalConditions[] mcs = MedicalConditions.values();
        String[] items = new String[mcs.length];
        int actualSize = 0;
        for (int i=0; i<mcs.length; i++){
            if(mcs[i] == MedicalConditions.None || goalsConfigued.containsKey(mcs[i].name()))
                continue;
            items[actualSize++] = getString(mcs[i].key());
        }

        return Arrays.copyOf(items, actualSize);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btnAddGoal){
            addNewGoal();
        }
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
        if(resultCode==getSherlockActivity().RESULT_OK) {
            if(requestCode==ACTION_CONFIGURE_GOAL){
                //save the goal and goalReadings
                Goal goal = data.getParcelableExtra("goal");
                GoalReadings readings = data.getParcelableExtra("reading");
                MedicalConditions selectedCondition = (MedicalConditions)data.getSerializableExtra("type");
                if(goal!=null){
                    if(Checker.isInternetOn(getSherlockActivity())) {
                        SaveGoal task = new SaveGoal();
                        task.type = selectedCondition;
                        task.reading = readings;
                        task.execute(goal);
                    } else {
                        Toast.makeText(getSherlockActivity(), "Internet is not on..", Toast.LENGTH_LONG).show();
                    }
                }
            }

            if(requestCode == ACTION_ADD_GOAL_VALUE) {
                GoalReadings reading = data.getParcelableExtra("reading");
                MedicalConditions selectedCondition = (MedicalConditions) data.getSerializableExtra("type");
                if(reading!=null){
                    if(Checker.isInternetOn(getSherlockActivity())) {
                        SaveGoalReading task = new SaveGoalReading();
                        task.type = selectedCondition;
                        task.execute(reading);
                    }else{
                        Toast.makeText(getSherlockActivity(), "Internet is not on..", Toast.LENGTH_LONG).show();
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
        if(mc==null)
            mPagerAdapter.notifyDataSetChanged();

        OnGoalDataChangeListener listener = listenersSubscribed.get(mc);

        if(listener != null){
            listener.onChange(getDataForGraph(mc), goalsConfiguredMap.get(mc));
        }
        else
            mPagerAdapter.notifyDataSetChanged();
    }

    public interface OnGoalDataChangeListener {
        public void onChange(String json, Goal goal);
        public void onAdd(String json);
        //public void onUpdate(String json);
    }

    private void editGoal(Goal goal, MedicalConditions type){
        Intent i = new Intent(getSherlockActivity(), AddGoalActivity.class);
        i.putExtra("user", selectedUser);
        i.putExtra("goals", getBundleFromMap(goalsConfiguredMap));
        i.putExtra("type", type);
        startActivityForResult(i, ACTION_CONFIGURE_GOAL);
    }

    private void deleteGoal(Goal goal, MedicalConditions type){
        AlertDialog.Builder buildr = new AlertDialog.Builder(getSherlockActivity());
        buildr.setTitle("Delete " + getString(type.key()) + " Goal");
        buildr.setMessage("Are you sure?");
        buildr.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        buildr.show();
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
                final MedicalConditions mc = (MedicalConditions) goalsConfiguredMap.keySet().toArray()[position];
                Goal goal = goalsConfiguredMap.get(mc);
                args.putSerializable("type", mc);
                args.putString("json", getDataForGraph(mc));
                args.putParcelable("goal", goal);
                fragment = (GraphFragment)Fragment.instantiate(getActivity(), GraphFragment.class.getName(), args);
                setOnGoalDataChangeListener(mc, fragment);
                fragment.setOnGoalModifyListener(new GraphFragment.OnGoalModifyListener() {
                    @Override
                    public void OnEdit(Goal goal, MedicalConditions type) {
                        editGoal(goal, type);
                    }

                    @Override
                    public void OnDelete(Goal goal, MedicalConditions type) {
                        deleteGoal(goal, type);
                    }
                });
                fragment.setOnClickAddValueListener(new GraphFragment.OnClickAddValueListener() {
                    @Override
                    public void onClick(MedicalConditions medicalCondition) {
                        Intent i = new Intent(getSherlockActivity(), AddGoalValue.class);
                        i.putExtra("type", medicalCondition);
                        i.putExtra("user", selectedUser);
                        List<GoalReadings> grs = goalsConfiguredMap.get(medicalCondition).getReadings();
                        Parcelable[] readings = new Parcelable[grs.size()];
                        readings = grs.toArray(readings);
                        i.putExtra("readings", readings);
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

    private void updateTargetCalories(User user, WeightGoal goal) {
        if(user==null || goal==null)
            return;

        int calories = BMRCalculator.getCaloriesToBeReducedPerDay(user.getBmiProfile().getWeight()-goal.getWeight(), goal.getTargetDate());
        int totalCalories = BMRCalculator.calculateBMR(user.getBmiProfile().getWeight(), user.getBmiProfile().getHeight(),
                user.getProfile().getAge(), user.getProfile().getGender());

        int targetCaloriesPerDay = totalCalories - calories;
        appPrefs.setTargetCaloriesPerDay(targetCaloriesPerDay);
    }

    public class SaveGoalReading extends AsyncTask<GoalReadings, Void, GoalReadings> {

        MedicalConditions type;

        @Override
        protected GoalReadings doInBackground(GoalReadings... readings) {
            Goal goal = goalsConfiguredMap.get(type);
            List<GoalReadings> rds = goal.getReadings();
            GoalReadings reading = null;
            if(rds==null)
                rds = new ArrayList<GoalReadings>();
            for(int i=0; i<readings.length; i++){
                reading = goalHelper.saveGoalReadings(type, readings[i], selectedUser.getId());
                if(readings[i].isToUpdate()){
                    rds.remove(i);
                }
                rds.add(reading);
            }
            //goal.setReadings(rds);
            goalsConfiguredMap.put(type, goal);
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

//    public class DeleteGoal extends AsyncTask<Integer, Void, Void> {
//        @Override
//        protected Void doInBackground(Integer... params) {
//            //goalHelper.
//        }
//    }
    public class SaveGoal extends AsyncTask<Goal, Void, Void> {

        MedicalConditions type;
        GoalReadings reading;
        boolean isUpdate = false;

        @Override
        protected Void doInBackground(Goal... goals) {
            //TODO support multiple goals saving if required later
            Goal goal = goals[0];
            if(goal.getId()>0){
                isUpdate = true;
            }else{
                isUpdate = false;
            }
            goalsConfiguredMap.put(type, goalHelper.saveGoal(type, goal, selectedUser.getId()));
            if(type==MedicalConditions.Obese)
                updateTargetCalories(selectedUser, (WeightGoal)goalsConfiguredMap.get(MedicalConditions.Obese));
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
            if(reading!=null && !isUpdate){
                SaveGoalReading task = new SaveGoalReading();
                task.type = type;
                task.execute(reading);
            }
            if(goalsConfiguredMap==null || goalsConfiguredMap.isEmpty() || goalsConfiguredMap.values().isEmpty()) {
                final_layout.setVisibility(View.GONE);
                initial_layout.setVisibility(View.VISIBLE);
            }else{
                initial_layout.setVisibility(View.GONE);
                final_layout.setVisibility(View.VISIBLE);
            }

            onGoalDataChanged(type);
        }
    }

    public class GetALLGoals extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            goalsConfiguredMap = goalHelper.getAllGoalsConfigured(selectedUser.getId());
            ga.goalsConfiguredMap=goalsConfiguredMap;
            //goalReadingsMap = goalHelper.getAllGoalReadings(selectedUser.getId());
            updateTargetCalories(selectedUser, (WeightGoal)goalsConfiguredMap.get(MedicalConditions.Obese));
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

            Toast.makeText(getSherlockActivity(), "Calories Per Day - " + appPrefs.getTargetCaloriesPerDay(), Toast.LENGTH_LONG).show();

            onGoalDataChanged(null);

            //TextView btnAddGoal = (TextView) final_layout.findViewById(R.id.btn_add_goal);
            //btnAddGoal.setOnClickListener(GoalFragment.this);

            Button btnAddG = (Button) initial_layout.findViewById(R.id.btnAddGoal);
            btnAddG.setOnClickListener(GoalFragment.this);

            if(goalsConfiguredMap==null || goalsConfiguredMap.isEmpty() || goalsConfiguredMap.values().isEmpty()) {
                final_layout.setVisibility(View.GONE);
                initial_layout.setVisibility(View.VISIBLE);
            }else{
                initial_layout.setVisibility(View.GONE);
                final_layout.setVisibility(View.VISIBLE);
            }

            dialog.dismiss();
        }


    }


}
