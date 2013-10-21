package com.viamhealth.android.activities.fragments;

import android.support.v4.app.Fragment;

import com.viamhealth.android.model.goals.GoalReadings;

/**
 * Created by naren on 18/10/13.
 */
public abstract class AddValueBaseFragment extends Fragment {

    abstract public GoalReadings getReadings();
}
