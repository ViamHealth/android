package com.viamhealth.android.activities;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.viamhealth.android.R;
import com.viamhealth.android.activities.fragments.AddBPValue;
import com.viamhealth.android.activities.fragments.AddCholesterolValue;
import com.viamhealth.android.activities.fragments.AddDiabetesValue;
import com.viamhealth.android.activities.fragments.AddValueBaseFragment;
import com.viamhealth.android.activities.fragments.AddWeightValue;
import com.viamhealth.android.activities.oldones.AddCholesterolGoal;
import com.viamhealth.android.model.enums.MedicalConditions;
import com.viamhealth.android.model.goals.GoalReadings;
import com.viamhealth.android.model.goals.WeightGoalReadings;
import com.viamhealth.android.utils.UIUtility;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AddGoalValue extends BaseFragmentActivity {

    TextView date, time;
    Calendar cal = Calendar.getInstance();

    AddValueBaseFragment fragment;
    MedicalConditions type;

    boolean shouldUpdate = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_goal_value);

        date = (TextView) findViewById(R.id.lbl_date);
        time = (TextView) findViewById(R.id.lbl_time);

        //set current date and time
        setDate();setTime();

        LinearLayout timePicker = (LinearLayout) findViewById(R.id.btn_time_picker);
        timePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                new TimePickerDialog(AddGoalValue.this, t,
                        c.get(Calendar.HOUR_OF_DAY),
                        c.get(Calendar.MINUTE),
                        true).show();
            }
        });

        LinearLayout datePicker = (LinearLayout) findViewById(R.id.btn_date_picker);
        datePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                new DatePickerDialog(AddGoalValue.this, d,
                        c.get(Calendar.YEAR),
                        c.get(Calendar.MONTH),
                        c.get(Calendar.DATE)).show();
            }
        });

        //final EditText weight = (EditText) findViewById(R.id.input_weight);
        Button btnSave = (Button) findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirm();
            }
        });

        TextView btnCancel = (TextView) findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });

        Intent intent = getIntent();
        type = (MedicalConditions) intent.getSerializableExtra("type");

        Bundle bundle = new Bundle();
        bundle.putParcelableArray("readings", intent.getParcelableArrayExtra("readings"));

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        fragment = (AddValueBaseFragment) getFragment(type, bundle);
        ft.add(R.id.container, fragment);
        ft.commit();
        getSupportFragmentManager().executePendingTransactions();
    }

    private Fragment getFragment(MedicalConditions mc, Bundle bundle) {
        Class fragmentClass = null;
        switch(mc){
            case Obese: fragmentClass = AddWeightValue.class; break;
            case BloodPressure: fragmentClass = AddBPValue.class; break;
            case Diabetes: fragmentClass = AddDiabetesValue.class; break;
            case Cholesterol: fragmentClass = AddCholesterolValue.class; break;
        }
        return Fragment.instantiate(this, fragmentClass.getName(), bundle);
    }

    private void save() {
        if(isValid()){
            Intent intent = new Intent();
            GoalReadings reading = fragment.getReadings(cal.getTime());
            reading.setReadingDate(cal.getTime());
            reading.setIsToUpdate(shouldUpdate);
            intent.putExtra("reading", reading);
            intent.putExtra("type", type);
            setResult(RESULT_OK, intent);
            finish();
        }
    }

    private void confirm() {

        Calendar dayCal = UIUtility.getDate(cal);

        if(fragment.doesExist(dayCal.getTime())){
            AlertDialog.Builder builder = new AlertDialog.Builder(AddGoalValue.this);
            builder.setTitle("Are you sure?");
            builder.setMessage("value for the date exists, change it?")
                    .setCancelable(false)
                    .setPositiveButton("Sure..", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            shouldUpdate = true;
                            save();
                        }
                    })
                    .setNegativeButton("Nope..", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            shouldUpdate = false;
                            save();
                        }
                    });

            AlertDialog dialog = builder.create();
            dialog.show();

        }
    }

    private boolean isValid() {
        return true;
    }

    private void setDate() {
        date.setText(new StringBuilder()
                .append(cal.get(Calendar.YEAR)).append("-")
                .append(cal.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.US)).append("-")
                .append(cal.get(Calendar.DATE)).append(" "));
    }

    private void setTime() {
        time.setText(new StringBuilder()
                .append(cal.getDisplayName(Calendar.HOUR, Calendar.LONG, Locale.US)).append(":")
                .append(cal.getDisplayName(Calendar.MINUTE, Calendar.LONG, Locale.US)).append(":")
                .append(cal.getDisplayName(Calendar.AM_PM, Calendar.SHORT, Locale.US)));
    }

    TimePickerDialog.OnTimeSetListener t=new TimePickerDialog.OnTimeSetListener() {
        public void onTimeSet(TimePicker view, int hourOfDay,
                              int minute) {
            cal.set(Calendar.HOUR_OF_DAY, hourOfDay);
            cal.set(Calendar.MINUTE, minute);
            setTime();
        }
    };


    DatePickerDialog.OnDateSetListener d=new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            cal.set(year, monthOfYear, dayOfMonth);

            Calendar dayCal = UIUtility.getDate(cal);

            if(fragment.doesExist(dayCal.getTime()))
                Toast.makeText(AddGoalValue.this, "value exists for this date..", Toast.LENGTH_SHORT).show();
            else
                setDate();
        }
    };
}
