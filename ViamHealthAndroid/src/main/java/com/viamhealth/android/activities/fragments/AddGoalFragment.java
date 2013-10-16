package com.viamhealth.android.activities.fragments;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.viamhealth.android.manager.AddGoalFragmentManager;
import com.viamhealth.android.model.goals.Goal;
import com.viamhealth.android.model.goals.GoalReadings;

/**
 * Created by naren on 10/10/13.
 */
public abstract class AddGoalFragment extends Fragment {

    protected AddGoalFragmentManager mManager = null;

    public void setEditTextFocusChangeListener(AddGoalFragmentManager fragmentManager) {
        mManager = fragmentManager;
    }

    public abstract Goal getGoal();
    public abstract GoalReadings getGoalReadings();

}
