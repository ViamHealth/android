package com.viamhealth.android.activities.fragments;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.actionbarsherlock.app.SherlockFragment;
import com.viamhealth.android.Global_Application;
import com.viamhealth.android.R;
import com.viamhealth.android.ViamHealthPrefs;
import com.viamhealth.android.model.users.User;

/**
 * Created by Kunal on 18/6/14.
 */
public class ChallengeAndTrackerFragment extends BaseFragment {

    private Global_Application ga;
    private ViamHealthPrefs appPrefs;

    private User selectedUser;

    private final String TAG = "CATFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab_fragment_task_screen, container, false);

        ga = ((Global_Application)getSherlockActivity().getApplicationContext());
        appPrefs = new ViamHealthPrefs(getSherlockActivity());
        selectedUser = getArguments().getParcelable("user");

        Bundle args = new Bundle();
        args.putParcelable("user", selectedUser);
        FragmentTransaction fm = getSherlockActivity().getSupportFragmentManager().beginTransaction();
        CatListFragment fragment = (CatListFragment) SherlockFragment.instantiate(getSherlockActivity(), CatListFragment.class.getName(), args);
        fm.add(R.id.taskList, fragment, "task-list");
        fm.commit();


        setHasOptionsMenu(true);

        return view;
    }
}
