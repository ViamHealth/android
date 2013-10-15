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

/**
 * Created by naren on 10/10/13.
 */
public abstract class AddGoalFragment extends Fragment {

    protected AddGoalFragmentManager mManager = null;

    public void setEditTextFocusChangeListener(AddGoalFragmentManager fragmentManager) {
        mManager = fragmentManager;
    }

    public abstract void onSave();

    protected final boolean isInternetOn() {
        ConnectivityManager connec = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

        if ((connec.getNetworkInfo(0).getState() == NetworkInfo.State.CONNECTED)
                || (connec.getNetworkInfo(0).getState() == NetworkInfo.State.CONNECTING)
                || (connec.getNetworkInfo(1).getState() == NetworkInfo.State.CONNECTING)
                || (connec.getNetworkInfo(1).getState() == NetworkInfo.State.CONNECTED)) {
            return true;
        }

        else if ((connec.getNetworkInfo(0).getState() == NetworkInfo.State.DISCONNECTED)
                || (connec.getNetworkInfo(1).getState() ==  NetworkInfo.State.DISCONNECTED)) {
            return false;
        }

        return false;
    }

}
