package com.viamhealth.android.activities.fragments;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragment;
import com.viamhealth.android.R;
import com.viamhealth.android.manager.TabManager;

/**
 * Created by naren on 28/10/13.
 */
public class NewReminders extends SherlockFragment {

    ActionBar actionBar;
    TabHost mTabHost;
    TabManager mTabManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_reminder_base, container, false);

        actionBar = getSherlockActivity().getSupportActionBar();

        mTabHost = (TabHost)view.findViewById(R.id.tabHost);
        mTabHost.setup();
        mTabManager = new TabManager(getSherlockActivity(), mTabHost, R.id.realtabcontent, false);

        Bundle bundle = null;
        /* Create Tabs */
        mTabManager.addTab(//getString(R.string.tab_label_goal), getResources().getDrawable(R.drawable.tab_goal)
                mTabHost.newTabSpec("dashboard").setIndicator(getTabIndicator(R.string.tab_label_reminder, R.drawable.ic_action_goal)),
                ReminderDashboardFragment.class, bundle);
        mTabManager.addTab(//, getResources().getDrawable(R.drawable.tab_journal)
                mTabHost.newTabSpec("journal").setIndicator(getTabIndicator(R.string.tab_label_medicines, R.drawable.ic_action_log)),
                ReminderMedicineFragment.class, bundle);
        mTabManager.addTab(//, getResources().getDrawable(R.drawable.tab_journal)
                mTabHost.newTabSpec("reminder").setIndicator(getTabIndicator(R.string.tab_label_others, R.drawable.ic_action_reminders)),
                ReminderOthersFragment.class, bundle);


        return view;
    }


    protected View getTabIndicator(int labelId, int drawableId) {
        View tabIndicator = LayoutInflater.from(getSherlockActivity()).inflate(R.layout.tab_indicator, mTabHost.getTabWidget(), false);

        TextView title = (TextView) tabIndicator.findViewById(R.id.title);
        title.setText(getString(labelId));
        ImageView icon = (ImageView) tabIndicator.findViewById(R.id.icon);
        icon.setImageResource(drawableId);

        return tabIndicator;
    }
}
