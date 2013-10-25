package com.viamhealth.android.activities.fragments;

import android.os.Bundle;
import android.os.Parcelable;
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
public class AddBPValue extends AddValueBaseFragment {

    View view;
    EditText sp, dp, pr;

    Map<Date, BPGoalReading> readingsMap = null;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        List<Parcelable> readings = Arrays.asList(getArguments().getParcelableArray("readings"));
        if(readings!=null){
            int count = readings.size();
            readingsMap = new HashMap<Date, BPGoalReading>(count);
            for(int i=0; i<count; i++){
                BPGoalReading reading = (BPGoalReading) readings.get(i);
                readingsMap.put(reading.getReadingDate(), reading);
            }
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_bp_value, container, false);

        sp = (EditText) view.findViewById(R.id.input_systolic_pressure);
        dp = (EditText) view.findViewById(R.id.input_diastolic_pressure);
        pr = (EditText) view.findViewById(R.id.input_pulse_rate);

        return view;
    }


    @Override
    public GoalReadings getReadings(Date date) {
        BPGoalReading reading = new BPGoalReading();
        reading.setSystolicPressure(Integer.parseInt(sp.getText().toString()));
        reading.setDiastolicPressure(Integer.parseInt(dp.getText().toString()));
        reading.setPulseRate(Integer.parseInt(pr.getText().toString()));
        return reading;
    }

    @Override
    public boolean doesExist(Date date) {
        return readingsMap.containsKey(date);
    }
}
