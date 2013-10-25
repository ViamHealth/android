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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setContentView(R.layout.activity_add_goal);

        Intent intent = getIntent();
        Bundle goalsConfigured = intent.getBundleExtra("goals");

        Bundle bundle = new Bundle();
        bundle.putParcelable("user", intent.getParcelableExtra("user"));
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
                Goal goal = fm.getGoal();
                GoalReadings goalReading = fm.getGoalReadings();
                Intent intent = new Intent();
                intent.putExtra("goal", goal);
                intent.putExtra("reading", goalReading);
                intent.putExtra("type", fm.getType());
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        btnCancel = (TextView) findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(AddGoalActivity.this);
        builder.setTitle("Set Goals for...");
        String[] mcs = getMedicalConditions(goalsConfigured);
        final String[] items = Arrays.copyOf(mcs, mcs.length);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (items[item].equals(getString(MedicalConditions.Diabetes.key()))) {
                    fm.changeFragment(MedicalConditions.Diabetes);
                } else if (items[item].equals(getString(MedicalConditions.Cholesterol.key()))) {
                    fm.changeFragment(MedicalConditions.Cholesterol);
                } else if (items[item].equals(getString(MedicalConditions.BloodPressure.key()))) {
                    fm.changeFragment(MedicalConditions.BloodPressure);
                } else if (items[item].equals(getString(MedicalConditions.Obese.key()))) {
                    fm.changeFragment(MedicalConditions.Obese);
                } else {
                    Intent intent = new Intent();
                    setResult(RESULT_CANCELED, intent);
                    finish();
                }
            }
        });
        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                finish();
            }
        });
        builder.show();
    }

    private String[] getMedicalConditions(Bundle goalsConfigued) {
        MedicalConditions[] mcs = MedicalConditions.values();
        String[] items = new String[mcs.length];
        for (int i=0; i<mcs.length; i++){
            if(goalsConfigued.containsKey(mcs[i].name()))
                continue;
            items[i] = getString(mcs[i].key());
        }

        return items;
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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.add_goal, menu);
        return true;
    }

}
