package com.viamhealth.android.activities.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.viamhealth.android.R;

/**
 * Created by naren on 07/10/13.
 */
public class JournalFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab_fragment_journal, container, false);
        return view;
    }
}
