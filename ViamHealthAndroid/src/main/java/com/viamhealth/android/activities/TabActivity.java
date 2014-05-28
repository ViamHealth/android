package com.viamhealth.android.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Parcelable;

import android.support.v4.app.Fragment;
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
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.Window;

import com.viamhealth.android.Global_Application;
import com.viamhealth.android.R;
import com.viamhealth.android.activities.fragments.BabyGoalFragment;
import com.viamhealth.android.activities.fragments.FileFragment;


import com.viamhealth.android.activities.fragments.ReminderFragmentNew;
import com.viamhealth.android.activities.fragments.TaskScreenFragment;
import com.viamhealth.android.manager.TabManager;
import com.viamhealth.android.model.users.User;
import com.viamhealth.android.tasks.InviteUser;

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

    boolean headerIsVisible = true;
    Global_Application ga;
    private User user = null;
    private Parcelable[] pUsers=null;
    private final List<User> users = new ArrayList<User>();

    private static final float HEADER_TOP_MARGIN_DP = 58.0f;
    private static final int DELETE_PROFILE_FROM_LIST = 10001;
    private static final int ADD_PROFILE_FROM_LIST = 10002;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.requestWindowFeature(Window.FEATURE_PROGRESS);
        setContentView(R.layout.tab_main_activity);
        ga=((Global_Application)getApplicationContext());
        mTabHost = (TabHost)findViewById(R.id.tabHost);
        mTabHost.setup();
        mTabHost.setOnClickListener(this);
        mTabManager = new TabManager(this, mTabHost, R.id.realtabcontent, true);

        Global_Application ga=((Global_Application)getApplicationContext());
        Intent intent = getIntent();
        user = (User) intent.getParcelableExtra("user");
        User loggedInUser = ga.getLoggedInUser();
        pUsers = intent.getParcelableArrayExtra("users");

        users.clear();
        for(int i=0; i<pUsers.length; i++){
            users.add((User) pUsers[i]);
        }

        actionBar = getSupportActionBar();
        actionBar.setDisplayUseLogoEnabled(false);
        actionBar.setHomeButtonEnabled(false);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        //actionBar.setDisplayHomeAsUpEnabled(true);
        //actionBar.setLogo(R.drawable.ic_launcher);
        //actionBar.setTitle(user.getName());




        //TODO use Action Bar to create the Header

        /*** Action Bar Creation starts here ***/
        //actionBar = getSupportActionBar();
        //actionBar.setDisplayHomeAsUpEnabled(true);
        //actionBar.setTitle("");

        //actionBar.setHomeButtonEnabled(true);
        //actionBar.setLogo(R.drawable.ic_action_white_brand);

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

        SharedPreferences pref = getSharedPreferences("User"+user.getName()+user.getId(), Context.MODE_PRIVATE);
        // To comment

        Boolean isTab=getIntent().getBooleanExtra("isTab", false);
        Actions action = (Actions)getIntent().getSerializableExtra("action");

        Bundle bundle = new Bundle();
        bundle.putParcelable("user", user);
        bundle.putSerializable("action", action);
        bundle.putParcelableArray("users",pUsers);
        SharedPreferences.Editor edit1=pref.edit();
        if(isTab==true)
        {
            edit1.putBoolean("isTest",true); //MJ:set to true
            edit1.putBoolean("isGoal",true);
        }
        /*
        else
        {
            edit1.putBoolean("isTest",false); //MJ:set to true
            edit1.putBoolean("isGoal",false);
        }
        */

        edit1.commit();
        //To comment




        /* Create Tabs */
            /*mTabManager.addTab(//, getResources().getDrawable(R.drawable.tab_journal)
                    mTabHost.newTabSpec("files").setIndicator(getTabIndicator(R.string.tab_label_file, R.drawable.ic_action_files)),
                    //FileFragment.class, bundle);
                    TaskScreen.class, bundle);*/


        mTabManager.addTab(
                mTabHost.newTabSpec("task_screen").setIndicator(getTabIndicator(R.string.tab_task_screen)),
                TaskScreenFragment.class, bundle
        );

        mTabManager.addTab(//, getResources().getDrawable(R.drawable.tab_journal)
                mTabHost.newTabSpec("reminder").setIndicator(getTabIndicator(R.string.tab_label_reminder)),
                ReminderFragmentNew.class, bundle);

        if(user.getProfile().isMinor())
            mTabManager.addTab(//getString(R.string.tab_label_goal), getResources().getDrawable(R.drawable.tab_goal)
                mTabHost.newTabSpec("baby_growth").setIndicator(getTabIndicator(R.string.tab_label_kids)),
                    BabyGoalFragment.class, bundle);

        //NewReminders.class, bundle);
        mTabManager.addTab(//, getResources().getDrawable(R.drawable.tab_journal)
                mTabHost.newTabSpec("files").setIndicator(getTabIndicator(R.string.tab_label_file)),
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
            //mTabManager.selectTab(TabTypes.Files.name());
            FileFragment fragment = (FileFragment) mTabManager.getCurrentSelectedTabFragment();
            fragment.pickFile();
        } else if(action == Actions.SetGoal){
            mTabHost.setCurrentTabByTag("goals");

            //mTabManager.selectTab(TabTypes.Goals.name());
            //}

            //if (savedInstanceState != null) {
            //mTabManager.selectTab(savedInstanceState.getString("tab"));
            //mTabHost.setCurrentTabByTag(savedInstanceState.getString("tab"));
        } else if(user.getId() != loggedInUser.getId()){
            mTabHost.setCurrentTabByTag("reminder");
        }




    }

    @Override
    public void onResume() {
        super.onResume();


    }


    @Override
    public void onBackPressed() {
        if(mTabManager.getCurrentSelectedTab().equals("baby_growth")){
            Fragment webview =  getSupportFragmentManager().findFragmentByTag("baby_growth");
            if(((BabyGoalFragment)webview).mWebview.canGoBack()){
                ((BabyGoalFragment)webview).mWebview.goBack();
            }else {
                super.onBackPressed();
            }
        } else {
            super.onBackPressed();
        }
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

    protected View getTabIndicator(int labelId) {
        View tabIndicator = LayoutInflater.from(this).inflate(R.layout.tab_indicator_holo_no_image, mTabHost.getTabWidget(), false);
        TextView title = (TextView) tabIndicator.findViewById(android.R.id.title);
        title.setText(getString(labelId));

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
            ga.GA_eventButtonPress("tab_menu_logout");
            Intent returnIntent = new Intent(TabActivity.this, Home.class);
            returnIntent.putExtra("logout", true);
            returnIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(returnIntent);
            finish();
            return true;
        }

        if(item.getItemId() == android.R.id.home) {
            ga.GA_eventButtonPress("tab_home");
            finish();
            return true;
        }

        if(item.getItemId() == R.id.menu_toc) {
            ga.GA_eventButtonPress("tab_menu_terms_n_conditions");
            Intent returnIntent = new Intent(TabActivity.this, TermsActivity.class);
            startActivity(returnIntent);
            return true;
        }

        if(item.getItemId() == R.id.menu_profile_list) {
            ga.GA_eventButtonPress("tab_r2_profile_list");
            Intent returnIntent = new Intent(TabActivity.this, ProfileListActivity.class);
            startActivity(returnIntent);
            return true;
        }

        /*if(item.getItemId() == R.id.menu_edit){
            //GoalFragment fm1= (GoalFragment)getSupportFragmentManager().findFragmentByTag("goals");
           // if(fm1.isVisible())
            //{
                Intent editIntent = new Intent(TabActivity.this, EditGoals.class);
                startActivity(editIntent);
            //}
        }*/

        if(item.getItemId() == R.id.menu_invite) {
            ga.GA_eventButtonPress("tab_menu_invite");
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
        if(mTabManager.getCurrentSelectedTab()!=null)
        {
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
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADD_PROFILE_FROM_LIST || requestCode == DELETE_PROFILE_FROM_LIST) {
            user = (User) data.getParcelableExtra("user");
            pUsers = data.getParcelableArrayExtra("users");
            users.clear();
            for (int i = 0; i < pUsers.length; i++) {
                users.add((User) pUsers[i]);
            }

            Toast.makeText(TabActivity.this, "Selected User " + user.getName(), Toast.LENGTH_LONG).show();
            Intent intent = new Intent(TabActivity.this, TabActivity.class);
            intent.putExtra("user", user);
            Parcelable[] usrs = new Parcelable[users.size()];
            intent.putExtra("users", users.toArray(usrs));
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }

    }

    public enum Actions { UploadFiles, SetGoal; }





}
