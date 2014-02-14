package com.viamhealth.android.activities.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;
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
import com.viamhealth.android.activities.SelectFiles;
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
import java.util.Collections;
import java.util.Comparator;
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
    MedicalConditions lastSelected=MedicalConditions.None;

    SharedPreferences userPref=null;
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
    String[] items=null;

    ActionBar actionBar;

    //WebView webView;
    //ImageButton addValue;

    final int ACTION_CONFIGURE_GOAL = 100;
    final int ACTION_ADD_GOAL_VALUE = 200;

    TabActivity.Actions action = null;
    Boolean isWeight=false,isBp=false,isSugar=false,isCholesterol=false;
    MedicalConditions selectedMC = MedicalConditions.None;
    Boolean isWeightActivity=false;
    Boolean isBpActivity=false;
    Boolean isSugarActivity=false;
    Boolean isCholesterolActivity=false;

    int CODE_WEIGHT=101;
    int CODE_BP=102;
    int CODE_SUGAR=103;
    int CODE_CHOLESTEROL=104;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        action = (TabActivity.Actions) getArguments().getSerializable("action");
        selectedUser = getArguments().getParcelable("user");
        userPref=getSherlockActivity().getSharedPreferences("User"+selectedUser.getId(), Context.MODE_PRIVATE);
        if((userPref.getBoolean("isGoal",false)==true) && (userPref.getBoolean("isTest",false)==false))
        {
            Intent inFileTest = new Intent(getSherlockActivity(), SelectFiles.class);
            inFileTest.putExtra("user", selectedUser);
            inFileTest.putExtra("users",getArguments().getParcelableArray("users"));
            startActivity(inFileTest);
            getActivity().finish();
        }
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
        menu.add(Menu.NONE, R.drawable.addicon, 10, "New Goal")
                .setIcon(R.drawable.addicon)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.drawable.addicon){
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
     * @param
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
            builder.write("seriesD", readings, JsonGraphDataBuilder.JsonOutput.GraphSeries.D);
        }

        builder.writeYAxisExtras(goal);

        return builder.toString();

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }


    public void setGoalFlag(String[] items,int position)
    {
        if(position < items.length)
        {
            if(items[position].equalsIgnoreCase("weight"))
            {
                isWeight = true ? (isWeight == false) : false;
            }
            else if(items[position].equalsIgnoreCase("blood pressure"))
            {
                isBp = true ? (isBp == false) : false;
            }
            else if(items[position].equalsIgnoreCase("diabetes"))
            {
                isSugar = true ? (isSugar == false) : false;
            }
            else if(items[position].equalsIgnoreCase("cholesterol"))
            {
                isCholesterol = true ? (isCholesterol == false) : false;
            }
        }
    }

    public void addNewGoalFirstTime() {
        items = getMedicalConditions(getBundleFromMap(goalsConfiguredMap));
        if(items!=null && items.length>0)
        {
            final Dialog dialog = new Dialog(getSherlockActivity(),R.style.Greentheme);
            dialog.setContentView(R.layout.select_goals);
            dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
            ListView lv= (ListView)dialog.findViewById(R.id.mylist);
            lv.setAdapter(new ArrayAdapter<String>(getSherlockActivity(), android.R.layout.simple_list_item_multiple_choice, items));
            lv.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);


            dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                @Override
                public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
                    //Toast.makeText(getSherlockActivity(),"key pressed in fragment and key pressed is"+i,Toast.LENGTH_LONG).show();
                    if(keyEvent.getAction()==KeyEvent.ACTION_DOWN)

                        if(i==KeyEvent.KEYCODE_BACK){
                            getActivity().finish();
                        }
                    return false;
                }
            });


            Button btnSkip = (Button)dialog.findViewById(R.id.btn_skip);
            btnSkip.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ga.GA_eventButtonPress("wizard_select_goals_skip");
                    Intent inFileTest = new Intent(getSherlockActivity(), SelectFiles.class);
                    inFileTest.putExtra("user", selectedUser);
                    inFileTest.putExtra("users",getArguments().getParcelableArray("users"));
                    startActivity(inFileTest);
                    getActivity().finish();
                }
            });

            Button btnSave=(Button)dialog.findViewById(R.id.btn_next);

            btnSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ga.GA_eventButtonPress("wizard_select_goals_save");
                        SharedPreferences.Editor edit = userPref.edit();
                        edit.putBoolean("isGoal",true);
                        edit.commit();
                        Intent in = new Intent(getSherlockActivity(), AddGoalActivity.class);
                        in.putExtra("user", selectedUser);
                        in.putExtra("isButtonVisible",true);
                        in.putExtra("goals", getBundleFromMap(goalsConfiguredMap));
                        Intent inFileTest = new Intent(getSherlockActivity(), SelectFiles.class);
                        inFileTest.putExtra("user", selectedUser);
                        inFileTest.putExtra("users",getArguments().getParcelableArray("users"));
                        startActivity(inFileTest);

                        if(isCholesterol==true)
                        {
                            lastSelected= MedicalConditions.Cholesterol;
                        }
                        else if(isSugar == true)
                        {
                            lastSelected= MedicalConditions.Diabetes;
                        }
                        else if(isBp==true)
                        {
                            lastSelected=MedicalConditions.BloodPressure;
                        }
                        else if(isWeight == true)
                        {
                            lastSelected=MedicalConditions.Obese;
                        }
                        else
                        {
                           getActivity().finish();
                        }



                        if(isCholesterol==true)
                        {
                            isCholesterolActivity=true;
                            in.putExtra("type", MedicalConditions.Cholesterol);
                            startActivityForResult(in, ACTION_CONFIGURE_GOAL);
                        }
                        if(isSugar==true)
                        {
                            isSugarActivity=true;
                            in.putExtra("type", MedicalConditions.Diabetes);
                            startActivityForResult(in, ACTION_CONFIGURE_GOAL);
                        }
                        if(isBp==true)
                        {
                            isBpActivity=true;
                            in.putExtra("type", MedicalConditions.BloodPressure);
                            startActivityForResult(in, ACTION_CONFIGURE_GOAL);
                        }
                        if(isWeight== true)
                        {
                            isWeightActivity=true;
                            in.putExtra("type", MedicalConditions.Obese);
                            startActivityForResult(in, ACTION_CONFIGURE_GOAL);
                        }

                }
            });

            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                    setGoalFlag(items,position);
                }
            });
            dialog.show();
            ga.GA_eventGeneral("ui_action","launch_screen","wizard_select_goals_screen");
        }
        else
        {
            Intent inFileTest = new Intent(getSherlockActivity(), SelectFiles.class);
            inFileTest.putExtra("user", selectedUser);
            inFileTest.putExtra("users", getArguments().getParcelableArray("users"));
            startActivity(inFileTest);
            getActivity().finish();
        }
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

                if(selectedCondition==lastSelected)
                {
                    //Intent intent = new Intent(getSherlockActivity(), TabActivity.class);
                    //intent.putExtra("user", selectedUser);
                    //intent.putExtra("users",getArguments().getParcelableArray("users"));
                    //intent.putExtra("isTab", true);
                    Intent intent=getActivity().getIntent();
                    intent.putExtra("isTab", true);
                    getActivity().finish();
                    startActivity(intent);
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
                if(reading == null)
                    continue;
                if(readings[i].isToUpdate()){
                    rds.remove(i);
                }
                rds.add(reading);
            }
            Collections.sort(rds, new Comparator<GoalReadings>() {
                @Override
                public int compare(GoalReadings p1, GoalReadings p2) {
                    return p1.getReadingDate().compareTo(p2.getReadingDate());
                }

            });
            //goal.setReadings(rds);
            goalsConfiguredMap.put(type, goal);
            return reading;
        }

        @Override
        protected void onPreExecute() {
            //dialog.setMessage("saving the new value...");
            //dialog.show();
        }

        @Override
        protected void onPostExecute(GoalReadings reading) {
            //TODO need to fix this - onGoalReadingAdded(type, reading);
            //for temporary fix
            onGoalDataChanged(type);
            //dialog.dismiss();
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
            //dialog.setMessage("saving the new goal...");
            //dialog.show();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            //dialog.dismiss();
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
            //dialog.setMessage("getting all your goals...");
            //dialog.show();
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            //Toast.makeText(getSherlockActivity(), "Calories Per Day - " + appPrefs.getTargetCaloriesPerDay(), Toast.LENGTH_LONG).show();

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

            //dialog.dismiss();
            //if MJ:condition
            //addNewGoal();
           if(userPref.getBoolean("isGoal",false)==false)
           {
               addNewGoalFirstTime();
           }
        }


    }





}
