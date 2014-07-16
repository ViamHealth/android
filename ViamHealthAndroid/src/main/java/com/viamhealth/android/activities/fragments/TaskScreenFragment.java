package com.viamhealth.android.activities.fragments;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragment;
import com.viamhealth.android.Global_Application;
import com.viamhealth.android.R;
import com.viamhealth.android.ViamHealthPrefs;
import com.viamhealth.android.model.users.User;

/**
 * Created by Kunal on 14/5/14.
 */
public class TaskScreenFragment extends BaseFragment {

    private Global_Application ga;
    private ViamHealthPrefs appPrefs;

    private User selectedUser;

    private ActionBar actionBar;

    private final String TAG = "TaskScreenFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab_fragment_task_screen, container, false);

        ga = ((Global_Application) getSherlockActivity().getApplicationContext());
        appPrefs = new ViamHealthPrefs(getSherlockActivity());
        selectedUser = getArguments().getParcelable("user");

        Bundle args = new Bundle();
        args.putParcelable("user", selectedUser);
        FragmentTransaction fm = getSherlockActivity().getSupportFragmentManager().beginTransaction();
        TaskListFragment fragment = (TaskListFragment) SherlockFragment.instantiate(getSherlockActivity(), TaskListFragment.class.getName(), args);
        fm.add(R.id.taskList, fragment, "task-list");
        fm.commit();


        actionBar = getSherlockActivity().getSupportActionBar();

        setHasOptionsMenu(true);

        return view;
    }
}
