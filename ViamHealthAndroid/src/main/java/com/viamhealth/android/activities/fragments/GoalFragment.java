package com.viamhealth.android.activities.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.JsonWriter;
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
import com.viamhealth.android.activities.AddWeight;
import com.viamhealth.android.dao.rest.endpoints.GoalsEPHelper;
import com.viamhealth.android.dao.rest.endpoints.UserEP;
import com.viamhealth.android.model.enums.MedicalConditions;
import com.viamhealth.android.model.goals.Goal;
import com.viamhealth.android.model.goals.GoalReadings;
import com.viamhealth.android.model.users.User;
import com.viamhealth.android.utils.Checker;
import com.viamhealth.android.utils.JsonGraphDataBuilder;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.util.ArrayList;
import java.util.Calendar;
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

    WebView webView;
    ImageButton addValue;

    protected final int ACTION_CONFIGURE_GOAL = 100;
    protected final int ACTION_ADD_GOAL_VALUE = 200;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.tab_fragment_goal, container, false);

        selectedUser = getArguments().getParcelable("user");

        webView = (WebView) view.findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.addJavascriptInterface(this, "goals");
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
                Log.i("GraphWebView", consoleMessage.message());
                return super.onConsoleMessage(consoleMessage);
            }
        });
        webView.loadUrl("file:///android_asset/weightgoal.html");


        addValue = (ImageButton) view.findViewById(R.id.addvalue);
        addValue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), AddWeight.class);
                startActivityForResult(i, ACTION_ADD_GOAL_VALUE);
            }
        });
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

    /**
     * target-date
     * goal-specific-targets
     * goal-readings
     * goal-specific-healthy-range
     *
     * @param index
     * @return
     */
    public String getDataForGraph(int index) {

        MedicalConditions mc = MedicalConditions.constructEnumByValue(index);
        List<GoalReadings> readings = goalReadingsMap.get(mc);
        Goal goal = goalsConfiguredMap.get(mc);

        JsonGraphDataBuilder builder = new JsonGraphDataBuilder();
        builder.write("goal", goal)
               .write("readings", readings);

        return builder.toString();

        /*Calendar targetDate = Calendar.getInstance();
        targetDate.set(2013, 10, 30);
        Calendar readDate1 = Calendar.getInstance();
        readDate1.set(2013, 9, 16);
        Calendar readDate2 = Calendar.getInstance();
        readDate2.set(2013, 9, 18);
        Calendar readDate3 = Calendar.getInstance();
        readDate3.set(2013, 9, 21);
        Calendar readDate4 = Calendar.getInstance();
        readDate4.set(2013, 9, 27);
        Calendar readDate5 = Calendar.getInstance();
        readDate5.set(2013, 9, 30);
        Calendar readDate6 = Calendar.getInstance();
        readDate6.set(2013, 10, 03);
        String json = new String("{" +
                "    \"goal\": {" +
                "        \"targetDate\": "+ targetDate.getTimeInMillis() +"," +
                "        \"targetWeight\": 60," +
                "        \"healthyRange\": {" +
                "            \"from\": 58," +
                "            \"to\": 72" +
                "        }" +
                "    }," +
                "    \"readings\": [{" +
                "                \"x\": "+readDate1.getTimeInMillis()+"," +
                "                \"y\": 83" +
                "            }," +
                "            {" +
                "                \"x\": "+readDate2.getTimeInMillis()+"," +
                "                \"y\": 81" +
                "            }," +
                "            {" +
                "                \"x\": "+readDate3.getTimeInMillis()+"," +
                "                \"y\": 79" +
                "            }," +
                "            {" +
                "                \"x\": "+readDate4.getTimeInMillis()+"," +
                "                \"y\": 75" +
                "            }," +
                "            {" +
                "                \"x\": "+readDate5.getTimeInMillis()+"," +
                "                \"y\": 74" +
                "            }," +
                "            {" +
                "                \"x\": "+readDate6.getTimeInMillis()+"," +
                "                \"y\": 74" +
                "            }" +
                "    ]" +
                "}");

        return json;*/
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
        if(resultCode==getActivity().RESULT_OK) {
            if(requestCode==ACTION_CONFIGURE_GOAL){
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
            webView.loadUrl( "javascript:window.location.reload( true )" );
            return null;
        }

        @Override
        protected void onPreExecute() {
            dialog.setMessage("saving the new value...");
            dialog.show();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            webView.loadUrl( "javascript:window.location.reload( true )" );
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
            webView.loadUrl( "javascript:window.location.reload( true )" );
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
            webView.loadUrl( "javascript:window.location.reload( true )" );
            TextView btnAddGoal = (TextView) view.findViewById(R.id.btn_add_goal);
            btnAddGoal.setOnClickListener(GoalFragment.this);
            dialog.dismiss();
        }
    }
}
