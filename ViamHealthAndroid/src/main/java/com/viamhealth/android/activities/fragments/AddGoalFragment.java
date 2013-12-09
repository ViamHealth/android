package com.viamhealth.android.activities.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.viamhealth.android.manager.AddGoalFragmentManager;
import com.viamhealth.android.model.goals.Goal;
import com.viamhealth.android.model.goals.GoalReadings;
import com.viamhealth.android.model.users.User;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by naren on 10/10/13.
 */
public abstract class AddGoalFragment extends BaseFragment {

    protected AddGoalFragmentManager mManager = null;
    protected User user;
    protected boolean isGoalConfigured;
    protected View view = null;
    protected ProgressDialog dialog;
    protected EditText targetDate;
    protected List<EditText> mPresentEditText;
    protected List<EditText> mTargetEditText;

    protected SimpleDateFormat formater = new SimpleDateFormat("dd/MM/yyyy");

    public void setEditTextFocusChangeListener(AddGoalFragmentManager fragmentManager) {
        mManager = fragmentManager;
    }

    public abstract Goal getGoal();
    public abstract GoalReadings getGoalReadings();

    public boolean isValid(){
        return true;
    }

    public void onTargetDateChange () {

    }

    public void setDefaultGoalAttributes(Goal from, Goal to){
        if(from!=null && from.getId()!=null && from.getId()>0){
            to.setId(from.getId());
            to.setUserId(from.getUserId());
            to.setCreated(from.getCreated());
            to.setUpdated(from.getUpdated());
            to.setUpdatedBy(from.getUpdatedBy());
        }
    }

}
