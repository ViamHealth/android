package com.viamhealth.android.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.ActionBarSherlock;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.ActionProvider;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.SubMenu;
import com.facebook.widget.ProfilePictureView;
import com.viamhealth.android.Global_Application;
import com.viamhealth.android.R;
import com.viamhealth.android.activities.fragments.FileFragment;
import com.viamhealth.android.activities.fragments.GoalFragment;
import com.viamhealth.android.activities.fragments.JournalFragment;
import com.viamhealth.android.activities.fragments.ReminderFragment;
import com.viamhealth.android.activities.fragments.TabHeaderFragment;
import com.viamhealth.android.manager.ActionBarTabManager;
import com.viamhealth.android.manager.TabManager;
import com.viamhealth.android.model.users.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by naren on 07/10/13.
 */
public class TabActivity extends SherlockFragmentActivity implements View.OnClickListener, MenuItem.OnMenuItemClickListener {

    TabHost mTabHost;
    //TabManager mTabManager;
    ActionBarTabManager mTabManager;

    private final Map<TabTypes, SherlockFragment> mTabs = new HashMap<TabTypes, SherlockFragment>();
    private final Map<Integer, User> usersMap = new HashMap<Integer, User>();

    Animation animationMoveIn, animationMoveOut;
    LinearLayout tabContent;// tabHeader;
    TabWidget tabs;

    ActionBar actionBar;

    boolean headerIsVisible = true;

    private User user = null;
    private final List<User> users = new ArrayList<User>();

    private static final float HEADER_TOP_MARGIN_DP = 58.0f;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.tab_main_activity_new);
        //setTheme(R.style.Theme_Greentheme);

        //mTabHost = (TabHost)findViewById(R.id.tabHost);
        //mTabHost.setup();
        //mTabHost.setOnClickListener(this);
        mTabManager = new ActionBarTabManager(this, R.id.realtabcontent);

        Global_Application ga=((Global_Application)getApplicationContext());
        Intent intent = getIntent();

        Actions action = (Actions) intent.getSerializableExtra("action");

        user = (User) intent.getParcelableExtra("user");
        Parcelable[] usrs = intent.getParcelableArrayExtra("users");
        for(int i=0; i<usrs.length; i++){
            User usr = (User)usrs[i];
            users.add(usr);
        }

        Bundle bundle = new Bundle();
        bundle.putParcelable("user", user);
        bundle.putSerializable("action", action);

        actionBar = getSupportActionBar();
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setLogo(R.drawable.ic_launcher);
        actionBar.setTitle(user.getName());


        getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        getActionBarTab(TabTypes.Goals, bundle);
        getActionBarTab(TabTypes.Journal, bundle);
        getActionBarTab(TabTypes.Reminder, bundle);
        getActionBarTab(TabTypes.Files, bundle);


        //TODO use Action Bar to create the Header

        /* Create the Tab Header */
        //mTabManager.addHeader(R.id.tabHeader, TabHeaderFragment.class, bundle);

        /* Create Tabs */
        /*mTabManager.addTab(//getString(R.string.tab_label_goal), getResources().getDrawable(R.drawable.tab_goal)
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
        */

        animationMoveIn = new TranslateAnimation(0, 0, -29, 29);
        animationMoveIn.setDuration(2000);
        animationMoveIn.setRepeatCount(0);

        animationMoveOut = new TranslateAnimation(0, 0, 29, -29);
        animationMoveOut.setDuration(2000);
        animationMoveOut.setRepeatCount(0);

        tabContent = (LinearLayout) findViewById(R.id.realtabcontent);
        //tabHeader = (FrameLayout) findViewById(R.id.tabHeader);
        //tabs = (TabWidget) findViewById(android.R.id.tabs);

        if(action == Actions.UploadFiles){
            //mTabHost.setCurrentTabByTag("files");
            mTabManager.selectTab(TabTypes.Files.name());
            FileFragment fragment = (FileFragment) mTabManager.getCurrentSelectedTabFragment();
            fragment.uploadImage();
        } else if(action == Actions.SetGoal){
            //mTabHost.setCurrentTabByTag("goals");
            mTabManager.selectTab(TabTypes.Goals.name());
        }

        if (savedInstanceState != null) {
            mTabManager.selectTab(savedInstanceState.getString("tab"));
            //mTabHost.setCurrentTabByTag(savedInstanceState.getString("tab"));
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //outState.putString("tab", mTabHost.getCurrentTabTag());
        outState.putString("tab", mTabManager.getCurrentSelectedTab());
    }

    protected void getActionBarTab(TabTypes type, Bundle args) {
        ActionBar.Tab tab = getSupportActionBar().newTab();
        tab.setCustomView(getTabIndicator(type.res(), type.icon()));
        tab.setTabListener(mTabManager);
        tab.setTag(type.name());
        mTabManager.addTab(tab, type.name(), type.clss(), args);
    }

    protected View getTabIndicator(int labelId, int drawableId) {
        View tabIndicator = LayoutInflater.from(this).inflate(R.layout.tab_indicator, null);
        //View tabIndicator = LayoutInflater.from(this).inflate(R.layout.tab_indicator, mTabHost.getTabWidget(), false);
        TextView title = (TextView) tabIndicator.findViewById(R.id.title);
        title.setText(getString(labelId));
        ImageView icon = (ImageView) tabIndicator.findViewById(R.id.icon);
        icon.setImageResource(drawableId);

        return tabIndicator;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        //getSupportMenuInflater().inflate(R.menu.menu_tab_activity_main, menu);

        LayoutInflater layoutInflater = LayoutInflater.from(TabActivity.this);
        View view = layoutInflater.inflate(R.layout.menu_users, null);
        ProfilePictureView picView = (ProfilePictureView) view.findViewById(R.id.header_user_icon);
        if(user.getProfile()!=null)
            picView.setProfileId(user.getProfile().getFbProfileId());
        MenuItem item = menu.add(user.getName())
                .setActionView(view);



        int usrsCount = users.size();
        int i=0;
        for(; i< usrsCount; i++){
            User usr = users.get(i);
            usersMap.put(usr.getId().intValue(), usr);
            if(usr.getId()==user.getId()){
                continue;
            }else{
                SubMenu subMenu = menu.addSubMenu(usr.getName());
                subMenu.add(0, usr.getId().intValue(), i, usr.getName())
                        .setOnMenuItemClickListener(TabActivity.this);
            }

        }

        SubMenu subMenu = menu.addSubMenu("Logout");
        subMenu.add(0, 0, i, "Logout")
                .setOnMenuItemClickListener(TabActivity.this);

        /*menu.add(user.getName())
                .setActionProvider(new UsersActionProvider(TabActivity.this))
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);

        menu.add(user.getName())
                .setActionProvider(new UsersActionProvider(TabActivity.this))
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);*/

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // If this callback does not handle the item click, onPerformDefaultAction
        // of the ActionProvider is invoked. Hence, the provider encapsulates the
        // complete functionality of the menu item.
        Toast.makeText(this, "Handling in onOptionsItemSelected avoided",
                Toast.LENGTH_SHORT).show();
        finish();
        return false;
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        int userId = menuItem.getItemId();
        if(userId==0){
            //Logout
            Intent returnIntent = new Intent(TabActivity.this, Home.class);
            returnIntent.putExtra("logout", true);
            returnIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(returnIntent);
            return true;
        }
        User curUser = usersMap.get(userId);
        Intent intent = new Intent(TabActivity.this, TabActivity.class);
        intent.putExtra("user", user);
        Parcelable[] usersList = new Parcelable[users.size()];
        intent.putExtra("users", users.toArray(usersList));
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        return true;
    }

    /*    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.tab_group_settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.menu_settings){
            Intent returnIntent = new Intent(TabActivity.this, Home.class);
            returnIntent.putExtra("logout", true);
            returnIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(returnIntent);
        }
        return super.onOptionsItemSelected(item);
    }*/

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if(mTabManager.getCurrentSelectedTab().equals("goals")){
            if(newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE){
                //tabHeader.setAlpha(0.4f);

                FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) tabContent.getLayoutParams();
                params.setMargins(0,0,0,0);
                tabContent.setLayoutParams(params);

                //tabs.setVisibility(View.GONE);
                //tabHeader.setAnimation(animationMoveOut);
                return;
            }

        }

        //tabHeader.setAlpha(1);

        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) tabContent.getLayoutParams();
        // Get the screen's density scale
        final float scale = getResources().getDisplayMetrics().density;
        // Convert the dps to pixels, based on density scale
        int margin = (int) (HEADER_TOP_MARGIN_DP * scale + 0.5f);
        params.setMargins(0, margin, 0, 0);
        tabContent.setLayoutParams(params);

        //tabs.setVisibility(View.VISIBLE);
        //tabHeader.setAnimation(animationMoveOut);
    }

    @Override
    public void onClick(View v) {
        if(mTabManager.getCurrentSelectedTab().equals("goals")){
            if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
/*                if(headerIsVisible){
                    //tabHeader.setAnimation(animationMoveOut);
                    //headerIsVisible = false;
                }
                else{
                    //tabHeader.setAnimation(animationMoveIn);
                    //headerIsVisible = true;
                }*/
            }
        }
    }

    public enum Actions { UploadFiles, SetGoal; }

    public enum TabTypes {
        Goals(1, R.string.tab_label_goal, R.drawable.ic_action_goal, GoalFragment.class),
        Journal(2, R.string.tab_label_journal, R.drawable.ic_action_log, JournalFragment.class),
        Reminder(3, R.string.tab_label_reminder, R.drawable.ic_action_reminders, ReminderFragment.class),
        Files(4, R.string.tab_label_file, R.drawable.ic_action_files, FileFragment.class);

        private final int value;
        private final int resId;
        private final int iconId;
        private final Class className;

        TabTypes(int value, int resId, int iconId, Class className){
            this.value = value;
            this.resId = resId;
            this.iconId = iconId;
            this.className = className;
        }


        public int value() {return value;}
        public int res() {return resId;}
        public int icon() {return iconId;}
        public Class clss() {return className;}

        // Lookup table
        private static final Map<Integer, TabTypes> valuelookup = new HashMap<Integer, TabTypes>();
        private static final Map<Integer, TabTypes> keylookup = new HashMap<Integer, TabTypes>();

        // Populate the lookup table on loading time
        static {
            for (TabTypes mc : EnumSet.allOf(TabTypes.class)){
                valuelookup.put(mc.value(), mc);
                keylookup.put(mc.res(), mc);
            }
        }

        // This method can be used for reverse lookup purpose
        public static TabTypes constructEnumByValue(int value) {
            return valuelookup.get(value);
        }

        public static TabTypes constructEnumByKey(int key) {
            return keylookup.get(key);
        }

    }


    public class UsersActionProvider extends ActionProvider implements MenuItem.OnMenuItemClickListener {

        /** Context for accessing resources. */
        private final Context mContext;
        private ProfilePictureView picView;
        private final Map<Integer, User> usersMap = new HashMap<Integer, User>();

        public UsersActionProvider(Context context) {
            super(context);
            this.mContext = context;
        }


        @Override
        public View onCreateActionView() {
            // Inflate the action view to be shown on the action bar.
            LayoutInflater layoutInflater = LayoutInflater.from(mContext);
            View view = layoutInflater.inflate(R.layout.menu_users, null);
            picView = (ProfilePictureView) view.findViewById(R.id.header_user_icon);
            if(user.getProfile()!=null)
                picView.setProfileId(user.getProfile().getFbProfileId());
            return view;
        }

        @Override
        public boolean hasSubMenu() {
            return true;
        }

        @Override
        public boolean onPerformDefaultAction() {
            return super.onPerformDefaultAction();
        }

        @Override
        public void onPrepareSubMenu(SubMenu subMenu) {
            subMenu.clear();


        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            int userId = menuItem.getItemId();
            if(userId==0){
                //Logout
                Intent returnIntent = new Intent(mContext, Home.class);
                returnIntent.putExtra("logout", true);
                returnIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(returnIntent);
                return true;
            }
            User curUser = usersMap.get(userId);
            Intent intent = new Intent(mContext, TabActivity.class);
            intent.putExtra("user", user);
            Parcelable[] usersList = new Parcelable[users.size()];
            intent.putExtra("users", users.toArray(usersList));
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            return true;
        }

    }
}
