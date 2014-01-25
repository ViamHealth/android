package com.viamhealth.android.activities.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.viamhealth.android.Global_Application;
import com.viamhealth.android.R;
import com.viamhealth.android.ViamHealthPrefs;
import com.viamhealth.android.activities.AddReminder;
import com.viamhealth.android.dao.rest.endpoints.ReminderEP;
import com.viamhealth.android.model.enums.ReminderTime;
import com.viamhealth.android.model.enums.ReminderType;
import com.viamhealth.android.model.reminder.Reminder;
import com.viamhealth.android.model.reminder.ReminderReading;
import com.viamhealth.android.model.reminder.ReminderTimeData;
import com.viamhealth.android.model.users.User;
import com.viamhealth.android.ui.PagerContainer;
import com.viamhealth.android.utils.Checker;
import com.viamhealth.android.utils.DateUtils;
import com.viamhealth.android.provider.ScheduleContract;
import com.viamhealth.android.utils.LogUtils;

import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by naren on 20/11/13.
 */
public class ReminderFragmentNew extends BaseFragment implements LoaderManager.LoaderCallbacks<Cursor>{

    View mView;
    PagerContainer mContainer;
    ViewPager mViewPager;

    ActionBar mActionBar;

    ProgressDialog mDialog;

    ViamHealthPrefs appPrefs;
    Global_Application ga;
    Typeface tf;

    ReminderEP reminderEP;

    Bundle savedInstanceState;
    User user;

    PagerAdapter mPagerAdapter;
    Map<Date, List<ReminderReading>> mapReadings = new HashMap<Date, List<ReminderReading>>();

    final int ADD_REMINDER_REQUEST = 125;
    final int EDIT_REMINDER_REQUEST = 126;

    int currentDayPosition = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.tab_fragment_reminder_new, container, false);
        this.savedInstanceState = savedInstanceState;
        user = getArguments().getParcelable("user");

        appPrefs = new ViamHealthPrefs(getSherlockActivity());
        ga = ((Global_Application)getSherlockActivity().getApplicationContext());
        reminderEP = new ReminderEP(getSherlockActivity(), ga);

        tf = Typeface.createFromAsset(getSherlockActivity().getAssets(), "Roboto-Condensed.ttf");

        mActionBar = getSherlockActivity().getSupportActionBar();
        mContainer = (PagerContainer) mView.findViewById(R.id.container);
        mViewPager = mContainer.getViewPager();

        mDialog = new ProgressDialog(getSherlockActivity());
        mDialog.setCanceledOnTouchOutside(false);

        Uri uri =ScheduleContract.Reminders.buildReminderUri();
        Cursor c= getSherlockActivity().getContentResolver().query(uri,null, ScheduleContract.ReminderForeignKeyColumn.USER_ID+" = '"+user.getId()+"'",null,null);
        Log.e("Reminder db","number of rows = "+c.getCount());
        Toast.makeText(getSherlockActivity(), "number of reminder rows and user id = "+ c.getCount()+" "+user.getId() , Toast.LENGTH_SHORT).show();

        c.moveToFirst();

        for(;!c.isAfterLast();c.moveToNext())
        {
            Log.e("Reminder db table reminders ","number of rows,rem name,morning count,afternoon count,evening count,isdeleted,sync status,user id "+c.getCount()+ " " + c.getString(c.getColumnIndex(ScheduleContract.Reminders.NAME))+ " "+c.getInt(c.getColumnIndex(ScheduleContract.Reminders.MORNING_COUNT))+" "+ c.getInt(c.getColumnIndex(ScheduleContract.Reminders.AFTERNOON_COUNT))+ " "+ c.getInt(c.getColumnIndex(ScheduleContract.Reminders.NIGHT_COUNT))+" "+ c.getString(c.getColumnIndex(ScheduleContract.Reminders.IS_DELETED))+ " " + c.getString(c.getColumnIndex(ScheduleContract.Reminders.SYNC_STATUS)) + " " + c.getString(c.getColumnIndex(ScheduleContract.ReminderForeignKeyColumn.USER_ID)));
        }



        setHasOptionsMenu(true);

        getReadings(null);
        getLoaderManager().initLoader(0, null,ReminderFragmentNew.this);

        return mView;
    }

    private void getReadings(Date date) {
        List<Date> dates = new ArrayList<Date>();
        if(date==null){
            Date todayMidnight = DateUtils.getToday(new Date());
            final int milliSecInDay = 24 * 60 * 60 * 1000;
            dates.add(new Date(todayMidnight.getTime()-(3*milliSecInDay))); //-3 day
            dates.add(new Date(todayMidnight.getTime()-(2*milliSecInDay))); //-2 day
            dates.add(new Date(todayMidnight.getTime()-(1*milliSecInDay))); //-1 day
            dates.add(todayMidnight);
            dates.add(new Date(todayMidnight.getTime()+(1*milliSecInDay))); //+1 day
            dates.add(new Date(todayMidnight.getTime()+(2*milliSecInDay))); //+2 day
            dates.add(new Date(todayMidnight.getTime()+(3*milliSecInDay))); //+3 day
        }else{
            dates.add(date);
        }

        for(Date d : dates){
            mapReadings.put(d, null);
        }

        if(Checker.isInternetOn(getActivity())){
            RetrieveReminderReading task = new RetrieveReminderReading();
            Date[] d = new Date[dates.size()];
            task.execute(dates.toArray(d));
        }else{
            Toast.makeText(getSherlockActivity(), "Network is not available....", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.add(Menu.NONE, R.drawable.ic_action_reminders, 10, "New Reminder")
                .setIcon(R.drawable.ic_action_reminders)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.drawable.ic_action_reminders){
            addNewReminder();
            return false;
        }
        return super.onOptionsItemSelected(item);
    }

    protected void addNewReminder() {
        addNewReminder(null);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode==Activity.RESULT_OK){
            if(requestCode==ADD_REMINDER_REQUEST || requestCode == EDIT_REMINDER_REQUEST){
                Reminder reminder = data.getParcelableExtra("reminder");
                Boolean isEnd = data.getBooleanExtra("end", false);
                if(Checker.isInternetOn(getActivity())){
                    if(isEnd){
                        EndReminder task = new EndReminder();
                        task.execute(reminder);
                    }else{
                        SaveReminder task = new SaveReminder();
                        task.reminder = reminder;
                        task.execute();
                    }
                }else{
                    Toast.makeText(getSherlockActivity(), "Network is not available....", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    protected void addNewReminder(final Date date) {
        final ReminderType[] types = ReminderType.values();
        final CharSequence[] items = new CharSequence[types.length];
        for(int i=0; i<types.length; i++){
            items[i] = getString(types[i].resId());
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getSherlockActivity());
        builder.setTitle("Choose a reminder type...");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ReminderType type = types[which];
                Intent addReminderIntent = new Intent(getActivity(), AddReminder.class);
                addReminderIntent.putExtra("type", type);
                addReminderIntent.putExtra("date", date);
                addReminderIntent.putExtra("user", user);
                startActivityForResult(addReminderIntent, ADD_REMINDER_REQUEST);
                //Toast.makeText(getSherlockActivity(), "Add New Reminder for " + date.toString(), Toast.LENGTH_LONG).show();
            }
        });

        builder.show();
    }

    public interface OnRefreshReminderReadingListener {
        public void OnRefresh(List<ReminderReading> readings);
    }

    protected Map<Date, OnRefreshReminderReadingListener> listenerMap = new HashMap<Date, OnRefreshReminderReadingListener>();

    public void setOnRefreshReminderReadingListener(Date key, OnRefreshReminderReadingListener listener) {
        this.listenerMap.put(key, listener);
    }

    protected void OnRefreshReminderReading(Map<Date, List<ReminderReading>> mReadings) {
        if(mPagerAdapter==null){
            mPagerAdapter = new ReminderPagerAdapter(getChildFragmentManager());
        }
        mViewPager.setAdapter(mPagerAdapter);

        //Necessary or the pager will only have one extra page to show
        //make this at least however many pages you can see
        mViewPager.setOffscreenPageLimit(mPagerAdapter.getCount());
        //A little space between pages
        mViewPager.setPageMargin(20);

        //If hardware acceleration is enabled, you should also remove
        //clipping on the pager for its children.
        mViewPager.setClipChildren(false);
        mViewPager.setCurrentItem(currentDayPosition, true);


        if(listenerMap!=null){
            for(Date date : mReadings.keySet()){
                if(listenerMap.get(date)==null) continue;
                listenerMap.get(date).OnRefresh(mReadings.get(date));
            }
        }
    }

    private class ReminderPagerAdapter extends FragmentPagerAdapter {

        private SparseArray<ReminderPagerFragment> reminderPages = new SparseArray<ReminderPagerFragment>();

        private ReminderPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            ReminderPagerFragment fragment = reminderPages.get(position);
            Bundle args = new Bundle();
            args.putParcelable("user", user);
            Date date = getDateFromPosition(position);
            Date today = DateUtils.getToday(new Date());
            if(date.equals(today))
                currentDayPosition = position;
            args.putLong("currentDateInMs", date.getTime());
            args.putBoolean("isFirstTime", mapReadings.keySet().size() == 0 ? true : false);
            args.putParcelableArrayList("readings", (ArrayList<ReminderReading>) mapReadings.get(date));
            fragment = (ReminderPagerFragment)Fragment.instantiate(getActivity(), ReminderPagerFragment.class.getName(), args);
            fragment.setOnRemiderDataChangeListener(new ReminderPagerFragment.OnRemiderDataChangeListener() {
                @Override
                public void OnEdit(Reminder reminder) {
                    Intent addReminderIntent = new Intent(getActivity(), AddReminder.class);
                    addReminderIntent.putExtra("type", reminder.getType());
                    addReminderIntent.putExtra("date", reminder.getStartDate());
                    addReminderIntent.putExtra("user", user);
                    addReminderIntent.putExtra("reminder", reminder);
                    startActivityForResult(addReminderIntent, EDIT_REMINDER_REQUEST);
                }

                @Override
                public void OnDelete(Reminder[] reminder) {
                    if(Checker.isInternetOn(getActivity())){
                        DeleteReminder task = new DeleteReminder();
                        task.execute(reminder);
                    }else{
                        Toast.makeText(getSherlockActivity(), "Network is not available....", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void OnAdd(Date date) {
                    addNewReminder(date);
                }
            });
            setOnRefreshReminderReadingListener(date, fragment);
            return fragment;
        }

        @Override
        public void notifyDataSetChanged() {
            reminderPages.clear();
            super.notifyDataSetChanged();
        }

        private Date getDateFromPosition(int position){
            Date todayMidnight = DateUtils.getToday(new Date());
            final int milliSecInDay = 24 * 60 * 60 * 1000;
            int dataAvailableForPastDays = getDataAvailableForPastDays(todayMidnight, milliSecInDay);
            int noOfDays = dataAvailableForPastDays - position;
            return new Date(todayMidnight.getTime()-(noOfDays*milliSecInDay));
        }

        private int getDataAvailableForPastDays(Date today, int dayInMS) {
            if(mapReadings.containsKey(new Date(today.getTime()-(3*dayInMS)))) return 3;
            if(mapReadings.containsKey(new Date(today.getTime()-(2*dayInMS)))) return 2;
            if(mapReadings.containsKey(new Date(today.getTime()-(1*dayInMS)))) return 1;
            return 0;
        }

        @Override
        public int getCount() {
            int count = mapReadings.keySet().size();
            if(count==0)
                return 1;
            return count;
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        CursorLoader cursorLoader = new CursorLoader(getSherlockActivity(),ScheduleContract.Reminders.CONTENT_REMINDER_URI,null, null, null, null);
        return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        //Toast.makeText(getSherlockActivity(),"cursor count"+data.getColumnCount(),Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        LogUtils.LOGD(" ", "onLoadReset: " + loader.getId());
    }

    public class RetrieveReminderReading extends AsyncTask <Date, Void, String> {
        protected Context applicationContext;
        protected ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            if(!mDialog.isShowing()){
                mDialog.setMessage("getting reminders...");
                mDialog.show();
            }
        }

        protected void onPostExecute(String result) {
            mDialog.dismiss();
            OnRefreshReminderReading(mapReadings);
            mPagerAdapter.notifyDataSetChanged();
        }

        @Override
        protected String doInBackground(Date... params) {
            Map<Date, List<ReminderReading>> mapResult = reminderEP.getReadings(Arrays.asList(params), user.getId());
            updateMap(mapResult);
            return null;
        }

        protected void updateMap(Map<Date, List<ReminderReading>> mapResult){
            for(Date date : mapResult.keySet()){
                mapReadings.put(date, mapResult.get(date));
            }
        }

    }

    public class SaveReminder extends AsyncTask<Void, Void, Void> {

        protected Reminder reminder;

        @Override
        protected void onPreExecute() {
            mDialog.setMessage("saving reminder...");
            mDialog.show();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            getReadings(null);
        }

        @Override
        protected Void doInBackground(Void... params) {
            if(reminder.getId()==null || reminder.getId()==0)
                reminder = reminderEP.add(user.getId(), reminder);
            else
                reminder = reminderEP.update(reminder);
            return null;
        }
    }

    public class EndReminder extends AsyncTask<Reminder, Void, Void> {

        protected Activity activity;

        @Override
        protected void onPreExecute() {
            mDialog.setMessage("saving reminder...");
            mDialog.show();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            getReadings(null);
        }

        @Override
        protected Void doInBackground(Reminder... params) {
            for(Reminder reminder : params ){
                reminderEP.endReminder(reminder);
            }
            return null;
        }
    }

    public class DeleteReminder extends AsyncTask<Reminder, Void, Void> {

        protected Activity activity;

        @Override
        protected void onPreExecute() {
            mDialog.setMessage("deleting reminder...");
            mDialog.show();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            getReadings(null);
        }

        @Override
        protected Void doInBackground(Reminder... params) {
            for(Reminder reminder : params ){
                reminderEP.delete(reminder.getId());
            }
            return null;
        }
    }
}
