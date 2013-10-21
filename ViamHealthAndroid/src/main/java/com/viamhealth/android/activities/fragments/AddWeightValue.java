package com.viamhealth.android.activities.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.viamhealth.android.R;
import com.viamhealth.android.model.goals.GoalReadings;
import com.viamhealth.android.model.goals.WeightGoalReadings;

/**
 * Created by naren on 18/10/13.
 */
public class AddWeightValue extends AddValueBaseFragment {

    View view;
    EditText weight;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_weight_value, container, false);

        weight = (EditText) view.findViewById(R.id.input_weight);

        return view;
    }

    @Override
    public GoalReadings getReadings() {
        WeightGoalReadings reading = new WeightGoalReadings();
        reading.setWeight(Double.parseDouble(weight.getText().toString()));
        return reading;
    }
}
