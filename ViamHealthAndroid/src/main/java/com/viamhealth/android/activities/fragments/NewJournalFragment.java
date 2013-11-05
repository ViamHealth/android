package com.viamhealth.android.activities.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.actionbarsherlock.app.SherlockFragment;

/**
 * Created by naren on 31/10/13.
 */
public class NewJournalFragment extends SherlockFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        int resourceId = 0;
        View view = inflater.inflate(resourceId, container, false);

        //fetch data here

        return view;
    }
}
