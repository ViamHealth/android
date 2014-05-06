package com.viamhealth.android.activities.fragments;

import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import com.viamhealth.android.R;
import com.viamhealth.android.model.goals.DiabetesGoalReading;
import com.viamhealth.android.model.goals.GoalReadings;
import com.viamhealth.android.utils.UIUtility;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by naren on 18/10/13.
 */
public class AddDiabetesValue extends AddValueBaseFragment {

    View view;
    EditText fasting, random;
    CheckBox cbfasting, cbrandom;

    boolean isFasting, isRandom, isUpdate = false;

    Map<Date, DiabetesGoalReading> readingsMap = null;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        List<Parcelable> readings = Arrays.asList(getArguments().getParcelableArray("readings"));
        if (readings != null) {
            int count = readings.size();
            readingsMap = new HashMap<Date, DiabetesGoalReading>(count);
            for (int i = 0; i < count; i++) {
                DiabetesGoalReading reading = (DiabetesGoalReading) readings.get(i);
                readingsMap.put(reading.getReadingDate(), reading);
            }
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_diabetes_value, container, false);


        fasting = (EditText) view.findViewById(R.id.input_fasting);
        fasting.setVisibility(View.GONE);
        random = (EditText) view.findViewById(R.id.input_random);
        //random.setVisibility(View.GONE);
        isRandom = true;

        cbfasting = (CheckBox) view.findViewById(R.id.cbFasting);
        cbrandom = (CheckBox) view.findViewById(R.id.cbRandom);

        cbfasting.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isFasting = isChecked;
                if (isFasting) fasting.setVisibility(View.VISIBLE);
                else fasting.setVisibility(View.GONE);
            }
        });
        cbrandom.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isRandom = isChecked;
                if (isRandom) random.setVisibility(View.VISIBLE);
                else random.setVisibility(View.GONE);
            }
        });

        return view;
    }

    @Override
    public GoalReadings getReadings(Date date) {
        DiabetesGoalReading reading = new DiabetesGoalReading();
        DiabetesGoalReading origReading = null;

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal = UIUtility.getDate(cal);

        if (readingsMap != null)
            origReading = readingsMap.get(cal.getTime());

        if (isFasting)
            reading.setFbs(Integer.parseInt(fasting.getText().toString()));
        if (isRandom)
            reading.setRbs(Integer.parseInt(random.getText().toString()));

        if (origReading != null) {
            if (reading.getFbs() == 0) reading.setFbs(origReading.getFbs());
            if (reading.getRbs() == 0) reading.setRbs(origReading.getRbs());
        }

        reading.setIsToUpdate(isUpdate);

        return reading;
    }

    @Override
    public boolean doesExist(Date date) {
        return readingsMap.containsKey(date);
        /*
        if(!readingsMap.containsKey(date)){
            isUpdate = false;
            return false;
        }

        DiabetesGoalReading reading = readingsMap.get(date);

        if(reading.getRbs()>0 && reading.getFbs()>0){
            //isUpdate = false;
            return true;
        }*/


//        if(reading.getRbs()==0 && reading.getFbs()==0)
//            isUpdate = false;
//
//        if(reading.getRbs()==0 || reading.getFbs()==0)
//            isUpdate = true;

        //return false;
    }
}
