package com.viamhealth.android.activities;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;

import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
    //TabManager mTabManager;
    ActionBarTabManager mTabManager;

    private final Map<TabTypes, SherlockFragment> mTabs = new HashMap<TabTypes, SherlockFragment>();
    private final Map<Integer, User> usersMap = new HashMap<Integer, User>();

    Animation animationMoveIn, animationMoveOut;
    FrameLayout tabContent;//, tabHeader;
    TabWidget tabs;

    ActionBar actionBar;
    User user;
    final List<User> users = new ArrayList<User>();

    boolean headerIsVisible = true;

    private User user = null;
    private final List<User> users = new ArrayList<User>();

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
                mTabHost.newTabSpec("journal").setIndicator(getTabIndicator(R.string.tab_label_journal, R.drawable.ic_action_log)),
                JournalFragment.class, bundle);
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
            //mTabHost.setCurrentTabByTag("files");
            mTabManager.selectTab(TabTypes.Files.name());
            FileFragment fragment = (FileFragment) mTabManager.getCurrentSelectedTabFragment();
            fragment.pickFile();
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
