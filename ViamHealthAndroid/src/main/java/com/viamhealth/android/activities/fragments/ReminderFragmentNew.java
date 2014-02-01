package com.viamhealth.android.activities.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
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
import com.viamhealth.android.model.enums.NotificationTime;
import com.viamhealth.android.model.enums.ReminderTime;
import com.viamhealth.android.model.enums.ReminderType;
import com.viamhealth.android.model.reminder.Reminder;
import com.viamhealth.android.model.reminder.ReminderReading;
import com.viamhealth.android.model.reminder.ReminderTimeData;
import com.viamhealth.android.model.users.User;
import com.viamhealth.android.notifications.ReminderConstants;
import com.viamhealth.android.notifications.objects.reminders.Medicine;
import com.viamhealth.android.notifications.objects.reminders.Other;
import com.viamhealth.android.services.ReminderNotification;
import com.viamhealth.android.ui.PagerContainer;
import com.viamhealth.android.utils.Checker;
import com.viamhealth.android.utils.DateUtils;
import com.viamhealth.android.utils.TimeIgnoringComparator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by naren on 20/11/13.
 */
public class ReminderFragmentNew extends BaseFragment {

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

        setHasOptionsMenu(true);

        getReadings(null);

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

    public void setRemindersNotifications(List<ReminderReading> readings){

        if(readings == null)
            return;



        int readingsCount = readings.size();
        if(readingsCount == 0)
            return;

        Map<Date,  List<Reminder>> mapTimeRemindersM = new HashMap<Date,   List<Reminder>>();
        Map<Date,  List<Reminder>> mapTimeRemindersA = new HashMap<Date,   List<Reminder>>();
        Map<Date,  List<Reminder>> mapTimeRemindersN = new HashMap<Date,   List<Reminder>>();
        Map<Date,  List<Reminder>> mapTimeRemindersO = new HashMap<Date,   List<Reminder>>();

        Map<ReminderTime,  List<Reminder>> mapTimeList;

        for(int i=0; i<readingsCount; i++){
            ReminderReading reading = readings.get(i);

            Date readingDate  = reading.getReadingDate();
            TimeIgnoringComparator tic = new TimeIgnoringComparator();
            int comparedDates = tic.compare(readingDate,new Date());

            if( comparedDates < 0){
                //old entries
                continue;
            }

            Date now = new Date();
            if(reading.getReminder().getType() == ReminderType.Medicine){

                if(now.getHours() < 8 )
                    mapTimeRemindersM = populateNotificationMap(mapTimeRemindersM,readingDate,reading.getReminder());
                else if(now.getHours() < 14 )
                    mapTimeRemindersA = populateNotificationMap(mapTimeRemindersA,readingDate,reading.getReminder());
                else if(now.getHours() < 21 )
                    mapTimeRemindersN = populateNotificationMap(mapTimeRemindersN,readingDate,reading.getReminder());
            }else {
                if(now.getHours() < 23 )
                    mapTimeRemindersO = populateNotificationMap(mapTimeRemindersO,readingDate,reading.getReminder());
            }
        }

        for(Map.Entry<Date, List<Reminder>> entry: mapTimeRemindersM.entrySet())
        {
            setReminderNotification(entry.getKey(),entry.getValue(), ReminderType.Medicine, NotificationTime.Morning);
        }
        for(Map.Entry<Date, List<Reminder>> entry: mapTimeRemindersA.entrySet())
        {
            setReminderNotification(entry.getKey(),entry.getValue(), ReminderType.Medicine, NotificationTime.Noon);
        }
        for(Map.Entry<Date, List<Reminder>> entry: mapTimeRemindersN.entrySet())
        {
            setReminderNotification(entry.getKey(),entry.getValue(), ReminderType.Medicine, NotificationTime.Night);
        }
        for(Map.Entry<Date, List<Reminder>> entry: mapTimeRemindersO.entrySet())
        {
            setReminderNotification(entry.getKey(),entry.getValue(), ReminderType.Other, NotificationTime.EarlyMorning);
        }
    }
    public Map<Date,  List<Reminder>>
        populateNotificationMap(
            Map<Date,  List<Reminder>> map, Date date, Reminder reminder){
        List<Reminder> myList;
        if(!map.containsKey(date)){
            map.put(date, new ArrayList<Reminder>());
        }
        myList = map.get(date);
        myList.add(reminder);
        map.put(date, myList);
        return map;
    }

    public void setReminderNotification(
            Date date, List<Reminder> reminders, ReminderType type, NotificationTime nt)
    {

        Intent mServiceIntent = new Intent(getSherlockActivity().getApplicationContext(), ReminderNotification.class);
        if(type == ReminderType.Medicine){
            if(nt==NotificationTime.Morning){
                mServiceIntent.setAction(ReminderConstants.ACTION_MEDICINE_NOTIFY_MORNING);
            }else if(nt==NotificationTime.Noon){
                mServiceIntent.setAction(ReminderConstants.ACTION_MEDICINE_NOTIFY_NOON);
            }else if(nt==NotificationTime.Night){
                mServiceIntent.setAction(ReminderConstants.ACTION_MEDICINE_NOTIFY_NIGHT);
            }else {
                //return;
                throw new IllegalArgumentException();
            }
            Medicine medicineNotificationObject = new Medicine(date, reminders,nt);
            mServiceIntent.putExtra("NOTIFICATION_OBJECT", medicineNotificationObject);
        } else {
                mServiceIntent.setAction(ReminderConstants.ACTION_MEDICINE_NOTIFY_EARLY_MORNING);
            //Non medicine reminder type
            return;
            //Medicine medicineNotificationObject = new Other(reminders,nt);
        }
        System.out.println("starting service-------------------");
        getSherlockActivity().startService(mServiceIntent);
    }
    /*public void setReminderNotification(ReminderReading reading){

        Intent mServiceIntent = new Intent(getSherlockActivity().getApplicationContext(), ReminderNotification.class);

        //mServiceIntent.putExtra("NOTIFICATION_ID",reading.getId().intValue());
        //mServiceIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        if(reading.getReminder().getType() == ReminderType.Medicine){
            mServiceIntent.setAction(ReminderConstants.ACTION_MEDICINE_NOTIFY_MORNING);
            Medicine medicineNotificationObject = new Medicine(reading);
            mServiceIntent.putExtra("NOTIFICATION_OBJECT", medicineNotificationObject);
        } else {
            return;
        }

        getSherlockActivity().startService(mServiceIntent);

    }*/

    protected void OnRefreshReminderReading(Map<Date, List<ReminderReading>> mReadings) {
        
        for (Map.Entry<Date, List<ReminderReading>> entry : mReadings.entrySet()){
            //setRemindersNotifications(entry.getValue());
        }

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
                setRNotification(date, mReadings.get(date));
            }
        }
        for(Date date : mReadings.keySet()){
            setRNotification(date, mReadings.get(date));
        }
    }

    private void setRNotification(Date date, List<ReminderReading> readings){

        if(readings == null)
            return;
        List<ReminderReading> remindersMedicineMorning = new ArrayList<ReminderReading>();
        List<ReminderReading> remindersMedicineNoon = new ArrayList<ReminderReading>();
        List<ReminderReading> remindersMedicineNight = new ArrayList<ReminderReading>();
        List<ReminderReading> remindersLabTest = new ArrayList<ReminderReading>();
        List<Reminder> remindersDrA = new ArrayList<Reminder>();
        List<ReminderReading> remindersOther = new ArrayList<ReminderReading>();
        Date now = new Date();

        //@TODO NOTIFICATIONTIME SETTINGS
        for(ReminderReading reading : readings){
            TimeIgnoringComparator tic = new TimeIgnoringComparator();
            if( tic.compare(reading.getReadingDate(),new Date()) < 0){
                continue;
            } else if( tic.compare(reading.getReadingDate(), new Date()) == 0){
                if(reading.getReminder().getType() == ReminderType.Medicine){
                    if(now.getHours() < 8 &&
                            reading.getAction(ReminderTime.Morning).isCheck() == Boolean.FALSE &&
                            reading.getReminder().getReminderTimeData(ReminderTime.Morning).getCount() > 0){
                        remindersMedicineMorning.add(reading);
                    }
                    else if(now.getHours() < 14 &&
                            reading.getAction(ReminderTime.Noon).isCheck() == Boolean.FALSE &&
                            reading.getReminder().getReminderTimeData(ReminderTime.Noon).getCount() > 0){
                        remindersMedicineNoon.add(reading);
                    }
                    else if(now.getHours() < 20 &&
                            reading.getAction(ReminderTime.Night).isCheck() == Boolean.FALSE &&
                            reading.getReminder().getReminderTimeData(ReminderTime.Noon).getCount() > 0){
                        remindersMedicineNight.add(reading);
                    }
                } else if(reading.getReminder().getType() == ReminderType.DrAppointments){
                    if( reading.isCompleteCheck() == Boolean.FALSE &&
                            now.getHours() < 10 ){
                            remindersOther.add(reading);
                    } else if(reading.getReminder().getType() == ReminderType.LabTests){
                        if( reading.isCompleteCheck() == Boolean.FALSE &&
                                now.getHours() < 10){
                            remindersOther.add(reading);
                        }
                    }
                    else if(reading.getReminder().getType() == ReminderType.Other){
                        if( reading.isCompleteCheck() == Boolean.FALSE &&
                                now.getHours() < 10){
                            remindersOther.add(reading);
                        }
                    }
                }
            } else {
                    if(reading.getReminder().getType() == ReminderType.Medicine){
                        if( reading.getAction(ReminderTime.Morning).isCheck() == Boolean.FALSE &&
                                reading.getReminder().getReminderTimeData(ReminderTime.Morning).getCount() > 0)
                            remindersMedicineMorning.add(reading);
                        if( reading.getAction(ReminderTime.Noon).isCheck() == Boolean.FALSE &&
                                reading.getReminder().getReminderTimeData(ReminderTime.Noon).getCount() > 0)
                            remindersMedicineNoon.add(reading);
                        if( reading.getAction(ReminderTime.Night).isCheck() == Boolean.FALSE &&
                                reading.getReminder().getReminderTimeData(ReminderTime.Night).getCount() > 0)
                            remindersMedicineNight.add(reading);
                    } else if(reading.getReminder().getType() == ReminderType.DrAppointments){
                        if( reading.isCompleteCheck() == Boolean.FALSE){
                            remindersOther.add(reading);
                        }
                    } else if(reading.getReminder().getType() == ReminderType.LabTests){
                        if( reading.isCompleteCheck() == Boolean.FALSE){
                            remindersOther.add(reading);
                        }
                    }
                    else if(reading.getReminder().getType() == ReminderType.Other){
                        if( reading.isCompleteCheck() == Boolean.FALSE){
                            remindersOther.add(reading);
                        }
                    }
            }
            //sendNotification(date, remindersMedicineMorning, NotificationTime.Morning, ReminderType.Medicine);
            //sendNotification(date, remindersMedicineNoon, NotificationTime.Noon, ReminderType.Medicine);
            //sendNotification(date, remindersMedicineNight, NotificationTime.Night, ReminderType.Medicine);
            sendNotification(date, remindersOther, NotificationTime.Other, ReminderType.Other);
        }
    }

    private void sendNotification(Date date, List<ReminderReading> reminders, NotificationTime nt, ReminderType rt){
        Intent mServiceIntent = new Intent(getSherlockActivity().getApplicationContext(), ReminderNotification.class);


        if(rt == ReminderType.Medicine){
            Medicine medicineNotificationObject =  null;
            if(nt == NotificationTime.Morning){
                mServiceIntent.setAction(ReminderConstants.ACTION_MEDICINE_NOTIFY_EARLY_MORNING);
                 //medicineNotificationObject = new Medicine(date, reminders,NotificationTime.Morning);
            } else if(nt == NotificationTime.Noon){
                mServiceIntent.setAction(ReminderConstants.ACTION_MEDICINE_NOTIFY_NOON);
                 //medicineNotificationObject = new Medicine(date, reminders,NotificationTime.Noon);
            } else if(nt == NotificationTime.Night){
                mServiceIntent.setAction(ReminderConstants.ACTION_MEDICINE_NOTIFY_NIGHT);
                 //medicineNotificationObject = new Medicine(date, reminders,NotificationTime.Night);
            } else {
                throw new IllegalArgumentException();
            }
            mServiceIntent.putExtra("NOTIFICATION_OBJECT", medicineNotificationObject);
        } else if(rt == ReminderType.Other){
            Other otherNotificationObject;
            mServiceIntent.setAction(ReminderConstants.ACTION_MEDICINE_NOTIFY_OTHER);
            otherNotificationObject = new Other(date, reminders,NotificationTime.Other);
            mServiceIntent.putExtra("NOTIFICATION_OBJECT", otherNotificationObject);
        } else {
            throw new IllegalArgumentException();
        }



        System.out.println("starting service-------------------");
        getSherlockActivity().startService(mServiceIntent);

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
