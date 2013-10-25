package com.viamhealth.android.activities.fragments;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.viamhealth.android.R;
import com.viamhealth.android.model.goals.BPGoalReading;
import com.viamhealth.android.model.goals.DiabetesGoalReading;
import com.viamhealth.android.model.goals.GoalReadings;
import com.viamhealth.android.model.goals.WeightGoalReadings;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by naren on 18/10/13.
 */
public class AddWeightValue extends AddValueBaseFragment {

    View view;
    EditText weight;

    Map<Date, WeightGoalReadings> readingsMap = null;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        List<Parcelable> readings = Arrays.asList(getArguments().getParcelableArray("readings"));
        if(readings!=null){
            int count = readings.size();
            readingsMap = new HashMap<Date, WeightGoalReadings>(count);
            for(int i=0; i<count; i++){
                WeightGoalReadings reading = (WeightGoalReadings) readings.get(i);
                readingsMap.put(reading.getReadingDate(), reading);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_weight_value, container, false);

        weight = (EditText) view.findViewById(R.id.input_weight);

        return view;
    }

    @Override
    public GoalReadings getReadings(Date date) {
        WeightGoalReadings reading = new WeightGoalReadings();
        reading.setWeight(Double.parseDouble(weight.getText().toString()));
        return reading;
    }

    @Override
    public boolean doesExist(Date date) {
        return readingsMap.containsKey(date);
    }
}
