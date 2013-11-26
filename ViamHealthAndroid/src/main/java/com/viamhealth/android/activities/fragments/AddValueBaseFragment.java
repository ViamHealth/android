package com.viamhealth.android.activities.fragments;

import android.support.v4.app.Fragment;

import com.actionbarsherlock.app.SherlockFragment;
import com.viamhealth.android.model.goals.GoalReadings;

import java.util.Date;

/**
 * Created by naren on 18/10/13.
 */
public abstract class AddValueBaseFragment extends BaseFragment {

    abstract public GoalReadings getReadings(Date date);
    abstract public boolean doesExist(Date date);

}
