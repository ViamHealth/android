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

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.MenuItem;
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
import com.viamhealth.android.model.goals.Goal;
import com.viamhealth.android.model.goals.GoalReadings;
import com.viamhealth.android.model.users.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddGoalActivity extends BaseFragmentActivity implements View.OnClickListener {

    Button btnSave;
    TextView btnCancel;

    AddGoalFragmentManager fm;
    ProgressDialog progressDialog;

    ActionBar actionBar;
    User user;
    MedicalConditions type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setContentView(R.layout.activity_add_goal);

        Intent intent = getIntent();
        Bundle goalsConfigured = intent.getBundleExtra("goals");

        user = intent.getParcelableExtra("user");
        type = (MedicalConditions)intent.getSerializableExtra("type");
        Bundle bundle = new Bundle();
        bundle.putParcelable("user", user);
        bundle.putBundle("goals", goalsConfigured);


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
                if(activeFragment.isValid()){
                    Goal goal = activeFragment.getGoal();
                    GoalReadings goalReading = activeFragment.getGoalReadings();
                    Intent intent = new Intent();
                    intent.putExtra("goal", goal);
                    intent.putExtra("reading", goalReading);
                    intent.putExtra("type", fm.getType());
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        });

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
