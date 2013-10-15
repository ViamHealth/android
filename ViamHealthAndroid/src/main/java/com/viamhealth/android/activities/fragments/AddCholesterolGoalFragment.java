package com.viamhealth.android.activities.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.viamhealth.android.R;

/**
 * Created by naren on 10/10/13.
 */
public class AddCholesterolGoalFragment extends AddGoalFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_cholesterol_goal, container, false);

        EditText date = (EditText) view.findViewById(R.id.add_goal_target_date);
        date.setOnFocusChangeListener(mManager);

        return view;
    }

    @Override
    public void onSave() {

    }
}
