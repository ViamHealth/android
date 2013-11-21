package com.viamhealth.android.activities.fragments;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListAdapter;
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
import com.viamhealth.android.dao.rest.endpoints.ReminderEP;
import com.viamhealth.android.dao.restclient.old.functionClass;
import com.viamhealth.android.model.reminder.ReminderReading;
import com.viamhealth.android.model.users.User;
import com.viamhealth.android.ui.PagerContainer;
import com.viamhealth.android.utils.DateUtils;

import org.joda.time.DateTime;

import java.text.SimpleDateFormat;
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
public class ReminderFragmentNew extends SherlockFragment {

    View mView;
    PagerContainer mContainer;
    ViewPager mViewPager;

    ActionBar mActionBar;

    ViamHealthPrefs appPrefs;
    Global_Application ga;
    Typeface tf;

    ReminderEP reminderEP;

    Bundle savedInstanceState;
    User user;

    PagerAdapter mPagerAdapter;
    Map<Date, List<ReminderReading>> mapReadings = new HashMap<Date, List<ReminderReading>>();

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

        mPagerAdapter = new MyPagerAdapter();
        mViewPager.setAdapter(mPagerAdapter);

        //Necessary or the pager will only have one extra page to show
        // make this at least however many pages you can see
        mViewPager.setOffscreenPageLimit(mPagerAdapter.getCount());

        //A little space between pages
        mViewPager.setPageMargin(15);

        //If hardware acceleration is enabled, you should also remove
        // clipping on the pager for its children.
        mViewPager.setClipChildren(false);

        setHasOptionsMenu(true);

        getReadings(null);

        return mView;
    }

    private void getReadings(Date date) {
        List<Date> dates = new ArrayList<Date>();
        if(date==null){
            Date todayMidnight = DateUtils.getToday(new Date());
            final int milliSecInDay = 24 * 60 * 60 * 1000;
            dates.add(todayMidnight);
            dates.add(new Date(todayMidnight.getTime()-(1*milliSecInDay))); //-1 day
            dates.add(new Date(todayMidnight.getTime()-(2*milliSecInDay))); //-2 day
            dates.add(new Date(todayMidnight.getTime()-(3*milliSecInDay))); //-3 day
            dates.add(new Date(todayMidnight.getTime()+(1*milliSecInDay))); //+1 day
            dates.add(new Date(todayMidnight.getTime()+(2*milliSecInDay))); //+2 day
            dates.add(new Date(todayMidnight.getTime()+(3*milliSecInDay))); //+3 day
        }else{
            dates.add(date);
        }

        RetrieveReminderReading task = new RetrieveReminderReading();
        Date[] d = new Date[dates.size()];
        task.execute(dates.toArray(d));
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
        Toast.makeText(getSherlockActivity(), "Add New Reminder", Toast.LENGTH_LONG).show();
    }

    protected void addNewReminder(Date date) {
        if(date==null)
        Toast.makeText(getSherlockActivity(), "Add New Reminder for " + date.toString(), Toast.LENGTH_LONG).show();
    }

    private class MyListAdapter extends ArrayAdapter<ReminderReading> {

        final ReminderReading[] readings;
        final int layoutResourceId;
        final Activity activity;

        private MyListAdapter(Context context, ReminderReading[] objects) {
            super(context, R.layout.row_medical_list, objects);
            layoutResourceId = R.layout.row_medical_list;
            readings = objects;
            this.activity = (Activity) context;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View row = convertView;

            if(row == null){
                LayoutInflater inflater = activity.getLayoutInflater();
                row = inflater.inflate(layoutResourceId, parent, false);

            }

            TextView txtName = (TextView)row.findViewById(R.id.txt_name);
            txtName.setText(readings[position].getReminder().getName());

            return row;
        }
    }

    //Nothing special about this adapter, just throwing up colored views for demo
    private class MyPagerAdapter extends PagerAdapter {

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = LayoutInflater.from(getSherlockActivity()).inflate(R.layout.reminder_pager_content, container);
            ScrollView sView = (ScrollView)view.findViewById(R.id.scrollView);
            FrameLayout fL = (FrameLayout)view.findViewById(R.id.initial_layout);

            Date currentDate = getDateFromPosition(position);
            List<ReminderReading> readings = mapReadings.get(currentDate);
            if(mapReadings.keySet().size()==0 || readings==null || readings.isEmpty()){
                sView.setVisibility(View.GONE);
                fL.setVisibility(View.VISIBLE);
                TextView tView = (TextView) view.findViewById(R.id.textView);
                if(mapReadings.keySet().size()==0){
                    tView.setText(R.string.reminder_initial_string);
                }else{
                    tView.setText(R.string.reminder_no_data);
                }
            }else{
                sView.setVisibility(View.VISIBLE);
                fL.setVisibility(View.GONE);
            }

            Button btn = (Button) view.findViewById(R.id.add_rem);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addNewReminder();
                }
            });

            if(readings!=null && readings.size()>0){
                //construct the list view here
                ListView listView = (ListView) view.findViewById(R.id.listView);
                ReminderReading[] rds = new ReminderReading[readings.size()];
                MyListAdapter listAdapter = new MyListAdapter(getSherlockActivity(), readings.toArray(rds));
                listView.setAdapter(listAdapter);
            }

//            TextView view = new TextView(getSherlockActivity());
//            view.setText("Item "+position);
//            view.setGravity(Gravity.CENTER);
//            view.setBackgroundColor(Color.argb(255, position * 50, position * 10, position * 50));

            container.addView(view);
            return view;
        }

        private Date getDateFromPosition(int position){
            Date todayMidnight = DateUtils.getToday(new Date());
            final int milliSecInDay = 24 * 60 * 60 * 1000;
            int noOfDays = 3 - position;
            return new Date(todayMidnight.getTime()-(noOfDays*milliSecInDay));
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View)object);
        }

        @Override
        public int getCount() {
            int count = mapReadings.keySet().size();
            if(count==0)
                return 1;

            return count;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return (view == object);
        }
    }

    public class RetrieveReminderReading extends AsyncTask <Date, Void, String>
    {
        protected Context applicationContext;

        @Override
        protected void onPreExecute() {
        }

        protected void onPostExecute(String result) {
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

}
