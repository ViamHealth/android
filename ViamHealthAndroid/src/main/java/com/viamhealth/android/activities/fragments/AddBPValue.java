package com.viamhealth.android.activities.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.viamhealth.android.R;
import com.viamhealth.android.model.goals.BPGoalReading;
import com.viamhealth.android.model.goals.GoalReadings;
import com.viamhealth.android.model.goals.WeightGoalReadings;

/**
 * Created by naren on 18/10/13.
 */
public class AddBPValue extends AddValueBaseFragment {

    View view;
    EditText sp, dp, pr;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_bp_value, container, false);

        sp = (EditText) view.findViewById(R.id.input_systolic_pressure);
        dp = (EditText) view.findViewById(R.id.input_diastolic_pressure);
        pr = (EditText) view.findViewById(R.id.input_pulse_rate);

        return view;
    }

    @Override
    public GoalReadings getReadings() {
        BPGoalReading reading = new BPGoalReading();
        reading.setSystolicPressure(Integer.parseInt(sp.getText().toString()));
        reading.setDiastolicPressure(Integer.parseInt(dp.getText().toString()));
        reading.setPulseRate(Integer.parseInt(pr.getText().toString()));
        return reading;
    }
}
