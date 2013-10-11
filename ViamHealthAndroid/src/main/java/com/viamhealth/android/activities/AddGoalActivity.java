package com.viamhealth.android.activities;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.app.Activity;
import android.support.v4.app.FragmentActivity;
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

import java.util.HashMap;
import java.util.List;

public class AddGoalActivity extends BaseFragmentActivity implements View.OnClickListener {

    Button btnSave;
    TextView btnCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setContentView(R.layout.activity_add_goal);

        Intent intent = getIntent();
        Bundle bundle = new Bundle();
        bundle.putParcelable("user", intent.getParcelableExtra("user"));

        final AddGoalFragmentManager fm = new AddGoalFragmentManager(this, R.id.add_goal_data_layout);
        fm.addFragment(MedicalConditions.Diabetes, AddDiabetesGoalFragment.class, bundle);
        fm.addFragment(MedicalConditions.Obese, AddWeightGoalFragment.class, bundle);
        fm.addFragment(MedicalConditions.BloodPressure, AddBPGoalFragment.class, bundle);
        fm.addFragment(MedicalConditions.Cholesterol, AddCholesterolGoalFragment.class, bundle);

        Spinner mcSelector = (Spinner) findViewById(R.id.add_goal_type_selector);
        ArrayAdapter<MedicalConditions> adapter = new ArrayAdapter<MedicalConditions>(this, android.R.layout.select_dialog_item, MedicalConditions.values());
        mcSelector.setAdapter(adapter);
        mcSelector.setOnItemSelectedListener(fm);

        btnSave = (Button) findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fm.OnSave();
            }
        });

        btnCancel = (TextView) findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    @Override
    public void onClick(View v) {
        if(v==btnCancel){

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.add_goal, menu);
        return true;
    }
    
}
