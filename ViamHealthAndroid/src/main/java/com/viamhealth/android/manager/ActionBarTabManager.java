package com.viamhealth.android.manager;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.TabHost;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.SherlockFragmentActivity;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by naren on 25/10/13.
 */
public class ActionBarTabManager implements ActionBar.TabListener {

    private final SherlockFragmentActivity mActivity;
    //private final ActionBar.Tab mTabHost;
    private final int mContainerId;
    private final Map<String, TabInfo> mTabs = new HashMap<String, TabInfo>();
    TabInfo mLastTab;

    static final class TabInfo {
        private final ActionBar.Tab tab;
        private final String tag;
        private final Class clss;
        private final Bundle args;
        private SherlockFragment fragment;

        TabInfo(ActionBar.Tab _tab, String _tag, Class _class, Bundle _args) {
            tab = _tab;
            clss = _class;
            args = _args;
            tag = _tag;
            tab.setTag(tag);
        }

    }

    public ActionBarTabManager(SherlockFragmentActivity activity, int containerId) {
        mActivity = activity;
        mContainerId = containerId;
    }

    private boolean removeFragmentIfExists(TabInfo info) {
        info.fragment = (SherlockFragment) mActivity.getSupportFragmentManager().findFragmentByTag(info.tag);
        if (info.fragment != null && !info.fragment.isDetached()) {
            FragmentTransaction ft = mActivity.getSupportFragmentManager().beginTransaction();
            ft.detach(info.fragment);
            ft.commit();
            return true;
        }

        return false;
    }

    public void addTab(ActionBar.Tab tab, String tag, Class fragmentClss, Bundle args) {

        TabInfo info = new TabInfo(tab, tag, fragmentClss, args);

        // Check to see if we already have a fragment for this tab,probably
        // from a previously saved state. If so, deactivate it, because our
        // initial state is that a tab isn't shown.
        removeFragmentIfExists(info);

        mTabs.put(info.tag, info);
        mActivity.getSupportActionBar().addTab(tab);
    }

    public void selectTab(String tag){
        mActivity.getSupportActionBar().selectTab(mTabs.get(tag).tab);
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        TabInfo newTab = mTabs.get(tab.getTag().toString());
        if (mLastTab != newTab) {
            FragmentTransaction ft = mActivity.getSupportFragmentManager().beginTransaction();

            if(mLastTab != null){
                if (mLastTab.fragment != null) {
                    ft.detach(mLastTab.fragment);
                }
            }

            if (newTab != null) {
                if (newTab.fragment == null) {
                    newTab.fragment = (SherlockFragment)SherlockFragment.instantiate(mActivity, newTab.clss.getName(), newTab.args);
                    ft.add(mContainerId, newTab.fragment, newTab.tag);
                } else {
                    ft.attach(newTab.fragment);
                }
            }

            mLastTab = newTab;
            ft.commit();
            mActivity.getSupportFragmentManager().executePendingTransactions();
        }
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

    }

    public String getCurrentSelectedTab() {
        return mLastTab.tag;
    }

    public Fragment getCurrentSelectedTabFragment() {
        return mLastTab.fragment;
    }

}
