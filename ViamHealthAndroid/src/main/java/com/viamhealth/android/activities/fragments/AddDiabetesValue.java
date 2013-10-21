package com.viamhealth.android.activities.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;

import com.viamhealth.android.R;
import com.viamhealth.android.model.goals.DiabetesGoalReading;
import com.viamhealth.android.model.goals.GoalReadings;
import com.viamhealth.android.model.goals.WeightGoalReadings;

/**
 * Created by naren on 18/10/13.
 */
public class AddDiabetesValue extends AddValueBaseFragment {

    View view;
    EditText sugar;
    Switch type;

    boolean isFasting;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_diabetes_value, container, false);

        sugar = (EditText) view.findViewById(R.id.input_sugar);
        type = (Switch) view.findViewById(R.id.input_sugar_type);
        type.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    isFasting = true;
                }else{
                    isFasting = false;
                }
            }
        });
        type.setChecked(true);

        return view;
    }

    @Override
    public GoalReadings getReadings() {
        DiabetesGoalReading reading = new DiabetesGoalReading();

        if(isFasting)
            reading.setFbs(Integer.parseInt(sugar.getText().toString()));
        else
            reading.setRbs(Integer.parseInt(sugar.getText().toString()));
        return reading;
    }
}
