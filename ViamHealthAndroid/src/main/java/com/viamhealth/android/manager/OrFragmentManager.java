package com.viamhealth.android.manager;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.AdapterView;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by naren on 10/10/13.
 */
public abstract class OrFragmentManager extends BaseFragmentManager implements AdapterView.OnItemSelectedListener {

    protected FragmentActivity mActivity;
    protected int mContainerId;
    protected Map<Object, FragmentInfo> mMap = new HashMap<Object, FragmentInfo>();
    protected FragmentInfo mLastShownFragment;

    public OrFragmentManager(FragmentActivity activity, int containerId) {
        mActivity = activity;
        mContainerId = containerId;
    }

    public void addFragment(Enum key, Class fragmentClass, Bundle args) {
        FragmentInfo info = new FragmentInfo(key, fragmentClass, args);
        mMap.put(key, info);
    }

    public void changeFragment(Enum key) {
        FragmentInfo newFragment = mMap.get(key);
        if (mLastShownFragment != newFragment) {
            FragmentTransaction ft = mActivity.getSupportFragmentManager().beginTransaction();

            if (mLastShownFragment != null) {
                if (mLastShownFragment.fragment != null) {
                    ft.detach(mLastShownFragment.fragment);
                }
            }
            if (newFragment != null) {
                if (newFragment.fragment == null) {
                    newFragment.fragment = Fragment.instantiate(mActivity, newFragment.clss.getName(), newFragment.args);
                    initFragment(newFragment.fragment);
                    ft.add(mContainerId, newFragment.fragment);
                } else {
                    ft.attach(newFragment.fragment);
                }
            }
            mLastShownFragment = newFragment;
            ft.commit();
            mActivity.getSupportFragmentManager().executePendingTransactions();
        }
    }

    protected abstract void initFragment(Fragment fragment);

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        try {
            FragmentSpinnerElement item = (FragmentSpinnerElement) parent.getItemAtPosition(position);
            Enum key = item.getEnum();
            changeFragment(key);
        } catch (ClassCastException e) {
            throw new ClassCastException("Objects in ArrayAdapter for the Spinner needs to implement OrFragmentManager.FragmentElement");
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public interface FragmentSpinnerElement {

        public Enum getEnum();
    }

    protected static final class FragmentInfo {
        public final Enum key;
        public final Class clss;
        public final Bundle args;
        public Fragment fragment;

        protected FragmentInfo(Enum key, Class clss, Bundle args) {
            this.key = key;
            this.clss = clss;
            this.args = args;
        }
    }

}
