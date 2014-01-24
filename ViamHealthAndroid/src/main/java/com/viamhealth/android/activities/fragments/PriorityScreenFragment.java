package com.viamhealth.android.activities.fragments;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragment;
import com.viamhealth.android.Global_Application;
import com.viamhealth.android.R;
import com.viamhealth.android.ViamHealthPrefs;
import com.viamhealth.android.dao.restclient.old.functionClass;
import com.viamhealth.android.model.users.User;


public class PriorityScreenFragment extends BaseFragment {

    private User selectedUser;
    private ActionBar actionBar;
    private final String TAG = "PriorityScreenFragment";
    private functionClass obj;

    private Global_Application ga;
    private ViamHealthPrefs appPrefs;
    Bundle savedInstanceState;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.priority_fragment, container, false);
        this.savedInstanceState = savedInstanceState;

        selectedUser = getArguments().getParcelable("user");

        obj=new functionClass(getSherlockActivity());

        ga=((Global_Application)getSherlockActivity().getApplicationContext());
        appPrefs = new ViamHealthPrefs(getActivity());

        //EP or Sync
        actionBar = getSherlockActivity().getSupportActionBar();

        Bundle args = new Bundle();
        args.putParcelable("user", selectedUser);

        FragmentTransaction fm = getSherlockActivity().getSupportFragmentManager().beginTransaction();
        PriorityListFragment fragment = (PriorityListFragment) SherlockFragment.instantiate(getSherlockActivity(),PriorityListFragment.class.getName(), args );

        fm.add(R.id.priorityScreenList,fragment, "priority-screen-list");

        fm.commit();

        return v;

    }


}