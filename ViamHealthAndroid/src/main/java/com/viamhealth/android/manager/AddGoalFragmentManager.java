package com.viamhealth.android.manager;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.SeekBar;

import com.viamhealth.android.activities.fragments.AddGoalFragment;
import com.viamhealth.android.activities.fragments.DatePickerFragment;
import com.viamhealth.android.listeners.OnFragmentEditTextListener;

/**
 * Created by naren on 10/10/13.
 */
public class AddGoalFragmentManager extends OrFragmentManager implements EditText.OnFocusChangeListener {

    @Override
    public void addFragment(Enum key, Class fragmentClass, Bundle args) {
        super.addFragment(key, fragmentClass, args);
    }

    public AddGoalFragmentManager(FragmentActivity activity, int containerId) {
        super(activity, containerId);
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        EditText text = (EditText) v;
        int inputType = text.getInputType();
        if(hasFocus){//when focused in
            if(inputType==(InputType.TYPE_CLASS_DATETIME|InputType.TYPE_DATETIME_VARIATION_DATE)){//if the editText is a dateTime filed then showTheDatePicker
                DialogFragment newFragment = new DatePickerFragment(text);
                newFragment.show(mActivity.getSupportFragmentManager(), "datePicker");
            }
        }else{//when focused out

        }
    }

    public void OnSave() {
        AddGoalFragment fragment = (AddGoalFragment) this.mLastShownFragment.fragment;
        fragment.onSave();
    }

    @Override
    protected void initFragment(Fragment fragment) {
        AddGoalFragment addGoalFragment = (AddGoalFragment) fragment;
        addGoalFragment.setEditTextFocusChangeListener(this);
    }

}
