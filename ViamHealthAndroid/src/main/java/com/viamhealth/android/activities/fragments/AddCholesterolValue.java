package com.viamhealth.android.activities.fragments;

import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.viamhealth.android.R;
import com.viamhealth.android.model.goals.BPGoalReading;
import com.viamhealth.android.model.goals.CholesterolGoal;
import com.viamhealth.android.model.goals.CholesterolGoalReading;
import com.viamhealth.android.model.goals.DiabetesGoalReading;
import com.viamhealth.android.model.goals.GoalReadings;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by naren on 18/10/13.
 */
public class AddCholesterolValue extends AddValueBaseFragment {

    View view;
    EditText hdl, ldl, tg;

    Map<Date, CholesterolGoalReading> readingsMap = null;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        List<Parcelable> readings = Arrays.asList(getArguments().getParcelableArray("readings"));
        if(readings!=null){
            int count = readings.size();
            readingsMap = new HashMap<Date, CholesterolGoalReading>(count);
            for(int i=0; i<count; i++){
                CholesterolGoalReading reading = (CholesterolGoalReading) readings.get(i);
                readingsMap.put(reading.getReadingDate(), reading);
            }
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_cholesterol_value, container, false);

        hdl = (EditText) view.findViewById(R.id.input_hdl);
        ldl = (EditText) view.findViewById(R.id.input_ldl);
        tg = (EditText) view.findViewById(R.id.input_triglycerides);

        return view;
    }

    @Override
    public GoalReadings getReadings(Date date) {
        CholesterolGoalReading reading = new CholesterolGoalReading();
        reading.setHdl(Integer.parseInt(hdl.getText().toString()));
        reading.setLdl(Integer.parseInt(ldl.getText().toString()));
        reading.setTriglycerides(Integer.parseInt(tg.getText().toString()));
        return reading;
    }

    @Override
    public boolean doesExist(Date date) {
        return readingsMap.containsKey(date);
    }
}
