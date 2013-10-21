package com.viamhealth.android.activities.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.viamhealth.android.R;
import com.viamhealth.android.model.goals.BPGoalReading;
import com.viamhealth.android.model.goals.CholesterolGoal;
import com.viamhealth.android.model.goals.CholesterolGoalReading;
import com.viamhealth.android.model.goals.GoalReadings;

/**
 * Created by naren on 18/10/13.
 */
public class AddCholesterolValue extends AddValueBaseFragment {

    View view;
    EditText hdl, ldl, tg;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_cholesterol_value, container, false);

        hdl = (EditText) view.findViewById(R.id.input_hdl);
        ldl = (EditText) view.findViewById(R.id.input_ldl);
        tg = (EditText) view.findViewById(R.id.input_triglycerides);

        return view;
    }

    @Override
    public GoalReadings getReadings() {
        CholesterolGoalReading reading = new CholesterolGoalReading();
        reading.setHdl(Integer.parseInt(hdl.getText().toString()));
        reading.setLdl(Integer.parseInt(ldl.getText().toString()));
        reading.setTriglycerides(Integer.parseInt(tg.getText().toString()));
        return reading;
    }
}
