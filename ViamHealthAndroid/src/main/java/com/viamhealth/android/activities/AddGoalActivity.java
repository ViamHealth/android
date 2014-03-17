package com.viamhealth.android.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.MenuItem;
import com.viamhealth.android.Global_Application;
import com.viamhealth.android.R;
import com.viamhealth.android.activities.fragments.AddBPGoalFragment;
import com.viamhealth.android.activities.fragments.AddCholesterolGoalFragment;
import com.viamhealth.android.activities.fragments.AddDiabetesGoalFragment;
import com.viamhealth.android.activities.fragments.AddGoalFragment;
import com.viamhealth.android.activities.fragments.AddWeightGoalFragment;
import com.viamhealth.android.adapters.MedicalConditionsAdapter;
import com.viamhealth.android.manager.AddGoalFragmentManager;
import com.viamhealth.android.manager.OrFragmentManager;
import com.viamhealth.android.model.enums.MedicalConditions;
import com.viamhealth.android.model.enums.ReminderType;
import com.viamhealth.android.model.goals.Goal;
import com.viamhealth.android.model.goals.GoalReadings;
import com.viamhealth.android.model.reminder.Reminder;
import com.viamhealth.android.model.users.User;
import com.viamhealth.android.services.ReminderBackground;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddGoalActivity extends BaseFragmentActivity implements View.OnClickListener {

    Button btnSave,btnSkip,btnAddReminder;
    TextView btnCancel;

    AddGoalFragmentManager fm;
    ProgressDialog progressDialog;

    ActionBar actionBar;
    User user;
    MedicalConditions type;
    Boolean isButtonVisible;
    Global_Application ga;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setContentView(R.layout.activity_add_goal);

        Intent intent = getIntent();
        Bundle goalsConfigured = intent.getBundleExtra("goals");

        user = intent.getParcelableExtra("user");
        type = (MedicalConditions)intent.getSerializableExtra("type");
        isButtonVisible=intent.getBooleanExtra("isButtonVisible",false);
        Bundle bundle = new Bundle();
        bundle.putParcelable("user", user);
        bundle.putBundle("goals", goalsConfigured);
        ga=(Global_Application)getApplicationContext();

        fm = new AddGoalFragmentManager(this, R.id.add_goal_data_layout);
        fm.addFragment(MedicalConditions.Diabetes, AddDiabetesGoalFragment.class, bundle);
        fm.addFragment(MedicalConditions.Obese, AddWeightGoalFragment.class, bundle);
        fm.addFragment(MedicalConditions.BloodPressure, AddBPGoalFragment.class, bundle);
        fm.addFragment(MedicalConditions.Cholesterol, AddCholesterolGoalFragment.class, bundle);

        btnSave = (Button) findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddGoalFragment activeFragment = fm.getActiveGoalFragment();
                if(type==MedicalConditions.Diabetes)
                {
                    ga.GA_eventButtonPress("wizard_diabetes_goal_screen_save");
                }
                else if(type==MedicalConditions.Obese)
                {
                    ga.GA_eventButtonPress("wizard_weight_goal_screen_save");
                }
                else if(type==MedicalConditions.Cholesterol)
                {
                    ga.GA_eventButtonPress("wizard_cholesterol_goal_screen_save");
                }
                else if(type==MedicalConditions.BloodPressure)
                {
                    ga.GA_eventButtonPress("wizard_bloodPressure_goal_screen_save");
                }

                if(activeFragment.isValid()){
                    Goal goal = activeFragment.getGoal();
                    GoalReadings goalReading = activeFragment.getGoalReadings();
                    Intent intent = new Intent();
                    intent.putExtra("goal", goal);
                    ArrayList<GoalReadings> read= new ArrayList<GoalReadings>();
                    read.add(goalReading);
                    goal.setReadings(read);
                    intent.putExtra("reading", goalReading);
                    intent.putExtra("type", fm.getType());
                    setResult(RESULT_OK, intent);
                    finish();
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Looks like you left one or more fields empty",Toast.LENGTH_LONG).show();
                }
            }
        });

        btnSkip = (Button) findViewById(R.id.btnSkip);
        btnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(type==MedicalConditions.Diabetes)
                {
                    ga.GA_eventButtonPress("wizard_diabetes_goal_screen_skip");
                }
                else if(type==MedicalConditions.Obese)
                {
                    ga.GA_eventButtonPress("wizard_weight_goal_screen_skip");
                }
                else if(type==MedicalConditions.Cholesterol)
                {
                    ga.GA_eventButtonPress("wizard_cholesterol_goal_screen_skip");
                }
                else if(type==MedicalConditions.BloodPressure)
                {
                    ga.GA_eventButtonPress("wizard_bloodPressure_goal_screen_skip");
                }

                Intent intent = new Intent();
                intent.putExtra("type", fm.getType());
                setResult(RESULT_OK, intent);
                finish();
            }
        });


        btnAddReminder = (Button) findViewById(R.id.btnReminder);
        btnAddReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(type==MedicalConditions.Diabetes)
                {
                    ga.GA_eventButtonPress("wizard_diabetes_goal_screen_remind_later");
                }
                else if(type==MedicalConditions.Obese)
                {
                    ga.GA_eventButtonPress("wizard_weight_goal_screen_remind_later");
                }
                else if(type==MedicalConditions.Cholesterol)
                {
                    ga.GA_eventButtonPress("wizard_cholesterol_goal_screen_remind_later");
                }
                else if(type==MedicalConditions.BloodPressure)
                {
                    ga.GA_eventButtonPress("wizard_bloodPressure_goal_screen_remind_later");
                }

                ArrayList<Reminder> rem1= new ArrayList<Reminder>();
                Intent intentservice = new Intent(AddGoalActivity.this, ReminderBackground.class);
                Reminder reminder = new Reminder();
                String name ="";
                if(type==MedicalConditions.Obese)
                {
                    name="Weight";
                }
                else if(type==MedicalConditions.BloodPressure)
                {
                    name="Blood Pressure";
                }
                else if(type==MedicalConditions.Cholesterol)
                {
                    name="Cholesterol";
                }
                else if(type==MedicalConditions.Diabetes)
                {
                    name="Diabetes";
                }

                reminder.setName("Set "+name+" Goals");
                reminder.setType(ReminderType.DrAppointments);
                reminder.setUserId(user.getId());
                Date dt = new Date();
                Calendar cal = Calendar.getInstance();
                cal.setTime(dt);
                if(cal.get(Calendar.DAY_OF_WEEK)==Calendar.SATURDAY)
                {
                    cal.add(Calendar.DATE,1);
                }
                else
                {
                    cal.add(Calendar.DATE,2);
                }
                reminder.setStartDate(cal.getTime());
                reminder.setEndDate(cal.getTime());
                rem1.add(reminder);
                intentservice.putParcelableArrayListExtra("reminder", rem1);
                intentservice.putExtra("user",user);
                startService(intentservice);
                Intent intent = new Intent();
                intent.putExtra("type", fm.getType());
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        if(isButtonVisible==true)
        {
            btnSkip.setVisibility(View.VISIBLE);
            btnAddReminder.setVisibility(View.VISIBLE);
        }


        /*** Action Bar Creation starts here ***/
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        String title = "Set " + getString(type.key()) + " Goal";
        actionBar.setTitle(title);
        actionBar.setSubtitle(user.getName());
        actionBar.setHomeButtonEnabled(true);
        actionBar.setLogo(R.drawable.ic_action_white_brand);
        /*** Action bar Creation Ends Here ***/

        fm.changeFragment(type);

    }

    public void setReminder()
    {

    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()== android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        if(v==btnCancel){
            Intent intent = new Intent();
            setResult(RESULT_CANCELED, intent);
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(com.actionbarsherlock.view.Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }
}
