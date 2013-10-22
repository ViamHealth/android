package com.viamhealth.android.manager;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.TabHost;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by naren on 07/10/13.
 */
public class TabManager implements TabHost.OnTabChangeListener {

    private final FragmentActivity mActivity;
    private final TabHost mTabHost;
    private final int mContainerId;
    private int mHeaderContainerId;
    private TabInfo mTabHeader;
    private boolean isHeaderAdded = false;
    private final Map<String, TabInfo> mTabs = new HashMap<String, TabInfo>();
    TabInfo mLastTab;

    static final class TabInfo {
        private final String tag;
        private final Class clss;
        private final Bundle args;
        private Fragment fragment;

        TabInfo(String _tag, Class _class, Bundle _args) {
            tag = _tag;
            clss = _class;
            args = _args;
        }

    }

    static class DummyTabFactory implements TabHost.TabContentFactory {

        Context mContext;

        DummyTabFactory(Context context) {
            mContext = context;
        }

        @Override
        public View createTabContent(String tag) {
            View v = new View(mContext);
            v.setMinimumWidth(0);
            v.setMinimumHeight(0);
            return v;
        }
    }

    public TabManager(FragmentActivity activity, TabHost tabHost, int containerId) {
        mActivity = activity;
        mTabHost = tabHost;
        mContainerId = containerId;
        mTabHost.setOnTabChangedListener(this);
    }

    public void addHeader(int headerContainerId, Class clss, Bundle args) {
        TabInfo header = new TabInfo("header", clss, args);
        removeFragmentIfExists(header);
        mTabHeader = header;
        mHeaderContainerId = headerContainerId;
    }

    private boolean removeFragmentIfExists(TabInfo info) {
        info.fragment = mActivity.getSupportFragmentManager().findFragmentByTag(info.tag);
        if (info.fragment != null && !info.fragment.isDetached()) {
            FragmentTransaction ft = mActivity.getSupportFragmentManager().beginTransaction();
            ft.detach(info.fragment);
            ft.commit();
            return true;
        }

        return false;
    }

    public void addTab(TabHost.TabSpec tabSpec, Class clss, Bundle args) {
        tabSpec.setContent(new DummyTabFactory(mActivity));
        String tag = tabSpec.getTag();

        TabInfo info = new TabInfo(tag, clss, args);

        // Check to see if we already have a fragment for this tab,probably
        // from a previously saved state. If so, deactivate it, because our
        // initial state is that a tab isn't shown.
        removeFragmentIfExists(info);

        mTabs.put(tag, info);
        mTabHost.addTab(tabSpec);
    }

    @Override
    public void onTabChanged(String tabId) {
        TabInfo newTab = mTabs.get(tabId);
        if (mLastTab != newTab) {
            FragmentTransaction ft = mActivity.getSupportFragmentManager().beginTransaction();

            //add header if not present in the trasaction
            if(mTabHeader != null && !isHeaderAdded){
                if(mTabHeader.fragment == null){
                    mTabHeader.fragment = Fragment.instantiate(mActivity, mTabHeader.clss.getName(), mTabHeader.args);
                    ft.add(mHeaderContainerId, mTabHeader.fragment, mTabHeader.tag);
                    isHeaderAdded = true;
                }
            }

            if (mLastTab != null) {
                if (mLastTab.fragment != null) {
                    ft.detach(mLastTab.fragment);
                }
            }
            if (newTab != null) {
                if (newTab.fragment == null) {
                    newTab.fragment = Fragment.instantiate(mActivity,newTab.clss.getName(),newTab.args);
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

    public String getCurrentSelectedTab() {
        return mLastTab.tag;
    }

    public Fragment getCurrentSelectedTabFragment() {
        return mLastTab.fragment;
    }

}
