package com.viamhealth.android.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;

import com.viamhealth.android.Global_Application;
import com.viamhealth.android.R;
import com.viamhealth.android.activities.fragments.FileFragment;
import com.viamhealth.android.activities.fragments.GoalFragment;
import com.viamhealth.android.activities.fragments.JournalFragment;
import com.viamhealth.android.activities.fragments.ReminderFragment;
import com.viamhealth.android.activities.fragments.TabHeaderFragment;
import com.viamhealth.android.manager.TabManager;

/**
 * Created by naren on 07/10/13.
 */
public class TabActivity extends FragmentActivity implements View.OnClickListener {

    TabHost mTabHost;
    TabManager mTabManager;

    Animation animationMoveIn, animationMoveOut;
    FrameLayout tabContent, tabHeader;
    TabWidget tabs;

    boolean headerIsVisible = true;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.tab_main_activity);


        mTabHost = (TabHost)findViewById(R.id.tabHost);
        mTabHost.setup();
        mTabHost.setOnClickListener(this);
        mTabManager = new TabManager(this, mTabHost, R.id.realtabcontent);

        Global_Application ga=((Global_Application)getApplicationContext());
        Intent intent = getIntent();
        Bundle bundle = new Bundle();
        bundle.putParcelable("user", intent.getParcelableExtra("user"));

        /* Create the Tab Header */
        mTabManager.addHeader(R.id.tabHeader, TabHeaderFragment.class, bundle);

        /* Create Tabs */
        mTabManager.addTab(//getString(R.string.tab_label_goal), getResources().getDrawable(R.drawable.tab_goal)
                mTabHost.newTabSpec("goals").setIndicator(getTabIndicator(R.string.tab_label_goal, R.drawable.ic_action_goal)),
                GoalFragment.class, bundle);
        mTabManager.addTab(//, getResources().getDrawable(R.drawable.tab_journal)
                mTabHost.newTabSpec("journal").setIndicator(getTabIndicator(R.string.tab_label_journal, R.drawable.ic_action_log)),
                JournalFragment.class, bundle);
        mTabManager.addTab(//, getResources().getDrawable(R.drawable.tab_journal)
                mTabHost.newTabSpec("reminder").setIndicator(getTabIndicator(R.string.tab_label_reminder, R.drawable.ic_action_reminders)),
                ReminderFragment.class, bundle);
        mTabManager.addTab(//, getResources().getDrawable(R.drawable.tab_journal)
                mTabHost.newTabSpec("files").setIndicator(getTabIndicator(R.string.tab_label_file, R.drawable.ic_action_files)),
                FileFragment.class, bundle);


        animationMoveIn = new TranslateAnimation(0, 0, -29, 29);
        animationMoveIn.setDuration(2000);
        animationMoveIn.setRepeatCount(0);

        animationMoveOut = new TranslateAnimation(0, 0, 29, -29);
        animationMoveOut.setDuration(2000);
        animationMoveOut.setRepeatCount(0);

        tabContent = (FrameLayout) findViewById(R.id.realtabcontent);
        tabHeader = (FrameLayout) findViewById(R.id.tabHeader);
        tabs = (TabWidget) findViewById(android.R.id.tabs);

    }

    protected View getTabIndicator(int labelId, int drawableId) {
        View tabIndicator = LayoutInflater.from(this).inflate(R.layout.tab_indicator, mTabHost.getTabWidget(), false);
        TextView title = (TextView) tabIndicator.findViewById(R.id.title);
        title.setText(getString(labelId));
        ImageView icon = (ImageView) tabIndicator.findViewById(R.id.icon);
        icon.setImageResource(drawableId);

        return tabIndicator;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.tab_group_settings, menu);
        return true;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if(mTabManager.getCurrentSelectedTab().equals("goals")){
            if(newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE){
                tabHeader.setAlpha(0.4f);
                tabContent.setPadding(0,0,0,0);
                tabs.setVisibility(View.GONE);
                tabHeader.setAnimation(animationMoveOut);
                return;
            }
        }

        tabHeader.setAlpha(1);
        tabContent.setPadding(0, 58, 0, 0);
        tabs.setVisibility(View.VISIBLE);
        //tabHeader.setAnimation(animationMoveOut);
    }

    @Override
    public void onClick(View v) {
        if(mTabManager.getCurrentSelectedTab().equals("goals")){
            if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
                if(headerIsVisible)
                    tabHeader.setAnimation(animationMoveOut);
                else
                    tabHeader.setAnimation(animationMoveIn);
            }
        }
    }
}
