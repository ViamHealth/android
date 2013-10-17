package com.viamhealth.android.activities.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.viamhealth.android.R;
import com.viamhealth.android.model.goals.Goal;
import com.viamhealth.android.model.goals.GoalReadings;

/**
 * Created by naren on 10/10/13.
 */
public class AddBPGoalFragment extends AddGoalFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_bp_goal, container, false);

        EditText date = (EditText) view.findViewById(R.id.add_goal_target_date);
        date.setOnFocusChangeListener(mManager);

        return view;
    }

    @Override
    public Goal getGoal() {
        return null;
    }

    @Override
    public GoalReadings getGoalReadings() {
        return null;
    }
}
