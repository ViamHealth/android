package com.viamhealth.android.activities;


import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.Window;
import com.google.analytics.tracking.android.EasyTracker;
import com.viamhealth.android.Global_Application;
import com.viamhealth.android.R;
import com.viamhealth.android.activities.fragments.FileFragment;
import com.viamhealth.android.activities.fragments.GoalFragment;
import com.viamhealth.android.activities.fragments.JournalFragment;
import com.viamhealth.android.activities.fragments.PriorityScreenFragment;
import com.viamhealth.android.activities.fragments.ReminderFragmentNew;
import com.viamhealth.android.manager.TabManager;
import com.viamhealth.android.model.users.User;
import com.viamhealth.android.tasks.InviteUser;
import com.viamhealth.android.utils.Checker;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by naren on 07/10/13.
 */
public class TabActivity extends BaseFragmentActivity implements View.OnClickListener, ActionBar.OnNavigationListener {

    TabHost mTabHost;
    TabManager mTabManager;

    Animation animationMoveIn, animationMoveOut;
    FrameLayout tabContent;//, tabHeader;
    TabWidget tabs;

    ActionBar actionBar;
    User user;
    final List<User> users = new ArrayList<User>();

    boolean headerIsVisible = true;

    private static final float HEADER_TOP_MARGIN_DP = 58.0f;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.requestWindowFeature(Window.FEATURE_PROGRESS);
        setContentView(R.layout.tab_main_activity);

        mTabHost = (TabHost)findViewById(R.id.tabHost);
        mTabHost.setup();
        mTabHost.setOnClickListener(this);
        mTabManager = new TabManager(this, mTabHost, R.id.realtabcontent, true);

        Global_Application ga=((Global_Application)getApplicationContext());
        Intent intent = getIntent();
        user = (User) intent.getParcelableExtra("user");
        Parcelable[] pUsers = intent.getParcelableArrayExtra("users");
        for(int i=0; i<pUsers.length; i++){
            users.add((User) pUsers[i]);
        }

        Actions action = (Actions) intent.getSerializableExtra("action");

        Bundle bundle = new Bundle();
        bundle.putParcelable("user", user);
        bundle.putSerializable("action", action);
        //TODO use Action Bar to create the Header

        /*** Action Bar Creation starts here ***/
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("");
        actionBar.setHomeButtonEnabled(true);
        actionBar.setLogo(R.drawable.ic_action_white_brand);

        Context themedContext = actionBar.getThemedContext();
        //UsersMenuAdapter adapter = new UsersMenuAdapter(themedContext, users);
        List<String> strUserNames = new ArrayList<String>(users.size());
        //strUserNames.add(user.getName());
        int currentUserIndex = 0;
        for(int i=0; i<users.size(); i++){
            if(users.get(i).getId().equals(user.getId()))
                currentUserIndex = i;
            strUserNames.add(users.get(i).getName());
        }
        ArrayAdapter<String> list = new ArrayAdapter<String>(themedContext, com.actionbarsherlock.R.layout.sherlock_spinner_item, strUserNames);
        list.setDropDownViewResource(com.actionbarsherlock.R.layout.sherlock_spinner_dropdown_item);

        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
        actionBar.setListNavigationCallbacks(list, this);

        actionBar.setSelectedNavigationItem(currentUserIndex);
        /*** Action bar Creation Ends Here ***/

        /* Create the Tab Header */
        //mTabManager.addHeader(R.id.tabHeader, TabHeaderFragment.class, bundle);

        /* Create Tabs */


        mTabManager.addTab(//getString(R.string.tab_label_goal), getResources().getDrawable(R.drawable.tab_goal)
                mTabHost.newTabSpec("goals").setIndicator(getTabIndicator(R.string.tab_label_goal, R.drawable.ic_action_goal_white)),
                GoalFragment.class, bundle);
        mTabManager.addTab(//, getResources().getDrawable(R.drawable.tab_journal)
                mTabHost.newTabSpec("priority").setIndicator(getTabIndicator(R.string.tab_label_priority, R.drawable.ic_action_log)),
                PriorityScreenFragment.class, bundle);
        mTabManager.addTab(//, getResources().getDrawable(R.drawable.tab_journal)
                mTabHost.newTabSpec("reminder").setIndicator(getTabIndicator(R.string.tab_label_reminder, R.drawable.ic_action_reminders)),
                ReminderFragmentNew.class, bundle);
        //NewReminders.class, bundle);
        mTabManager.addTab(//, getResources().getDrawable(R.drawable.tab_journal)
                mTabHost.newTabSpec("files").setIndicator(getTabIndicator(R.string.tab_label_file, R.drawable.ic_action_files)),
                //FileFragment.class, bundle);
                FileFragment.class, bundle);


        animationMoveIn = new TranslateAnimation(0, 0, -29, 29);
        animationMoveIn.setDuration(2000);
        animationMoveIn.setRepeatCount(0);

        animationMoveOut = new TranslateAnimation(0, 0, 29, -29);
        animationMoveOut.setDuration(2000);
        animationMoveOut.setRepeatCount(0);

        tabContent = (FrameLayout) findViewById(R.id.realtabcontent);
        //tabHeader = (FrameLayout) findViewById(R.id.tabHeader);
        tabs = (TabWidget) findViewById(android.R.id.tabs);

        if(action == Actions.UploadFiles){
            mTabHost.setCurrentTabByTag("files");
            FileFragment fragment = (FileFragment) mTabManager.getCurrentSelectedTabFragment();
            fragment.pickFile();
        } else if(action == Actions.SetGoal){
            mTabHost.setCurrentTabByTag("goals");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public boolean onNavigationItemSelected(int itemPosition, long itemId) {
        //mSelected.setText("Selected: " + mLocations[itemPosition]);
        User usr = users.get(itemPosition);
        if(usr.getId().equals(user.getId()))
            return false;

        Toast.makeText(TabActivity.this, "Selected User " + usr.getName(), Toast.LENGTH_LONG).show();
        Intent intent = new Intent(TabActivity.this, TabActivity.class);
        intent.putExtra("user", usr);
        Parcelable[] usrs = new Parcelable[users.size()];
        intent.putExtra("users", users.toArray(usrs));
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        return true;
    }

    protected View getTabIndicator(int labelId, int drawableId) {
        View tabIndicator = LayoutInflater.from(this).inflate(R.layout.tab_indicator_holo, mTabHost.getTabWidget(), false);
        TextView title = (TextView) tabIndicator.findViewById(android.R.id.title);
        title.setText(getString(labelId));
        ImageView icon = (ImageView) tabIndicator.findViewById(android.R.id.icon);
        icon.setImageResource(drawableId);

        return tabIndicator;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getSupportMenuInflater().inflate(R.menu.tab_group_settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        boolean retVal = super.onOptionsItemSelected(item);
        if(item.getItemId()==R.id.menu_logout){
            Intent returnIntent = new Intent(TabActivity.this, Home.class);
            returnIntent.putExtra("logout", true);
            returnIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(returnIntent);
            return true;
        }

        if(item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }

        if(item.getItemId() == R.id.menu_invite) {
            InviteUser inviteUser = new InviteUser(TabActivity.this, (Global_Application)getApplicationContext());
            inviteUser.show();
        }
        return retVal;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if(mTabManager.getCurrentSelectedTab().equals("goals")){
            if(newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE){
                tabs.setVisibility(View.GONE);
                return;
            }

        }
        tabs.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View v) {
        if(mTabManager.getCurrentSelectedTab().equals("goals")){
            if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
                if(headerIsVisible){
                    //tabHeader.setAnimation(animationMoveOut);
                    headerIsVisible = false;
                }
                else{
                    //tabHeader.setAnimation(animationMoveIn);
                    headerIsVisible = true;
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    public enum Actions { UploadFiles, SetGoal; }
}
