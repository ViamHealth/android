package com.viamhealth.android.activities;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.viamhealth.android.R;
import com.viamhealth.android.adapters.CheckboxGoalListAdapter;
import com.viamhealth.android.model.enums.MedicalConditions;
import com.viamhealth.android.model.goals.Goal;
import com.viamhealth.android.model.goals.GoalReadings;
import com.viamhealth.android.model.goals.WeightGoal;
import com.viamhealth.android.utils.Checker;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by monj on 27/1/14.
 */
public class EditGoals extends Activity{

    final int ACTION_CONFIGURE_GOAL = 100;
    RadioGroup radGrp=null;
    MedicalConditions type=MedicalConditions.None;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.requestWindowFeature(Window.FEATURE_PROGRESS);
        setContentView(R.layout.edit_delete_goals);
        Bundle b = getIntent().getBundleExtra("goals");
        RadioButton rad= (RadioButton)findViewById(R.id.radioWeight);
        if(b.containsKey("Obese"))
        {
            rad= (RadioButton)findViewById(R.id.radioWeight);
            rad.setVisibility(View.VISIBLE);
        }
        else if(b.containsKey("BloodPressure"))
        {
            rad= (RadioButton)findViewById(R.id.radioBp);
            rad.setVisibility(View.VISIBLE);
        }
        else if(b.containsKey("Diabetes"))
        {
            rad= (RadioButton)findViewById(R.id.radioDiabetes);
            rad.setVisibility(View.VISIBLE);
        }
        else if(b.containsKey("Cholesterol"))
        {
            rad= (RadioButton)findViewById(R.id.radioCholesterol);
            rad.setVisibility(View.VISIBLE);
        }

        radGrp=(RadioGroup)findViewById(R.id.radioGoals);

        Button edit=(Button)findViewById(R.id.btnEdit);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int selectedId = radGrp.getCheckedRadioButtonId();

                // find the radiobutton by returned id
                RadioButton selectedRad = (RadioButton) findViewById(selectedId);
                if(selectedRad.getText().toString().equalsIgnoreCase("Weight"))
                {
                    type=MedicalConditions.Obese;
                }
                else if(selectedRad.getText().toString().equalsIgnoreCase("Blood Pressure"))
                {
                    type=MedicalConditions.BloodPressure;
                }
                else if(selectedRad.getText().toString().equalsIgnoreCase("Diabetes"))
                {
                    type=MedicalConditions.Diabetes;
                }
                else
                {
                    type=MedicalConditions.Cholesterol;
                }

                editGoal(type);
            }
        });

        Button delete=(Button)findViewById(R.id.btnDelete);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }


    private void editGoal(MedicalConditions type){
        Intent i = new Intent(EditGoals.this, AddGoalActivity.class);
        i.putExtra("user", getIntent().getParcelableExtra("user"));
        i.putExtra("goals", getIntent().getBundleExtra("goals"));
        i.putExtra("type", type);
        startActivityForResult(i, ACTION_CONFIGURE_GOAL);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Intent intent = new Intent();
        intent.putExtra("type", type);
        Goal goal=data.getParcelableExtra("goal");
        //goal.setId(Long.valueOf(1));
        intent.putExtra("goal", goal);
        intent.putExtra("reading",data.getParcelableExtra("reading"));
        setResult(RESULT_OK, intent);
        finish();
    }

}




//when at least one checkbox is ticked,enable the next button
//disable it when none is clicked
//when a checkbox is clicked,enable that flag for that textbox
//and for the first clicked checkbox,start the intent,with the result
//code for that particular activity,once returned,check for that
//particular result code and if the next intent is checked,launch
//the next activity and so on.

