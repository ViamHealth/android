package com.viamhealth.android.activities.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import com.viamhealth.android.R;

/**
 * Created by naren on 10/10/13.
 */
public class AddDiabetesGoalFragment extends AddGoalFragment {

    View view = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_add_diabetes_goal, container, false);

        EditText date = (EditText) view.findViewById(R.id.add_goal_target_date);
        date.setOnFocusChangeListener(mManager);

        SeekBar pfbs = (SeekBar) view.findViewById(R.id.seekBar_present_fbs);
        pfbs.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                setValueInTextView(R.id.txt_add_present_fbs, seekBar.getProgress());
            }
        });
        pfbs.setMax(300);
        pfbs.setProgress(100);
        setValueInTextView(R.id.txt_add_present_fbs, 100);

        SeekBar prbs = (SeekBar) view.findViewById(R.id.seekBar_present_rbs);
        prbs.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                setValueInTextView(R.id.txt_add_present_rbs, seekBar.getProgress());
            }
        });
        prbs.setMax(300);
        prbs.setProgress(140);
        setValueInTextView(R.id.txt_add_present_rbs, 140);

        SeekBar tfbs = (SeekBar) view.findViewById(R.id.seekBar_target_fbs);
        tfbs.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                setValueInTextView(R.id.txt_add_target_fbs, seekBar.getProgress());
            }
        });
        tfbs.setMax(300);
        tfbs.setProgress(100);
        setValueInTextView(R.id.txt_add_target_fbs, 100);

        SeekBar trbs = (SeekBar) view.findViewById(R.id.seekBar_target_rbs);
        trbs.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                setValueInTextView(R.id.txt_add_target_rbs, seekBar.getProgress());
            }
        });
        trbs.setMax(300);
        trbs.setProgress(140);
        setValueInTextView(R.id.txt_add_target_rbs, 140);

        return view;
    }

    private void setValueInTextView(int resId, Integer value) {
        TextView txt = (TextView) view.findViewById(resId);
        txt.setText(value.toString());
    }

    @Override
    public void onSave() {

    }
}
