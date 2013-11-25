package com.viamhealth.android.activities.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.SherlockListFragment;
import com.actionbarsherlock.view.ActionMode;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.widget.ShareActionProvider;
import com.viamhealth.android.Global_Application;
import com.viamhealth.android.R;
import com.viamhealth.android.activities.AddReminder;
import com.viamhealth.android.adapters.ReminderDataAdapter;
import com.viamhealth.android.dao.rest.endpoints.ReminderEP;
import com.viamhealth.android.manager.BaseFragmentManager;
import com.viamhealth.android.model.FileData;
import com.viamhealth.android.model.enums.ReminderType;
import com.viamhealth.android.model.reminder.Reminder;
import com.viamhealth.android.model.reminder.ReminderReading;
import com.viamhealth.android.model.users.User;
import com.viamhealth.android.ui.helper.FileLoader;
import com.viamhealth.android.utils.Checker;
import com.viamhealth.android.utils.DateUtils;
import com.viamhealth.android.utils.UIUtility;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by naren on 24/11/13.
 */
public class ReminderPagerFragment extends SherlockListFragment implements ReminderFragmentNew.OnRefreshReminderReadingListener {

    Map<Date, List<ReminderReading>> mapReading = new HashMap<Date, List<ReminderReading>>();

    User user;
    Date currentDate;
    Boolean isFirstTime = false;
    List<ReminderReading> readings;

    View view;
    ReminderDataAdapter adapter;
    ReminderEP reminderEP;

    Global_Application ga;
    private ActionMode actionMode;
    private ShareActionProvider actionProvider = null;

    final int ADD_REMINDER_REQUEST = 125;
    final int EDIT_REMINDER_REQUEST = 126;

    private final String TAG = "ReminderPagerFragment";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        user = getArguments().getParcelable("user");
        currentDate = new Date(getArguments().getLong("currentDateInMs"));
        isFirstTime = getArguments().getBoolean("isFirstTime");
        readings = getArguments().getParcelableArrayList("readings");

        ga=((Global_Application)getSherlockActivity().getApplicationContext());

        reminderEP = new ReminderEP(getActivity(), ga);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.reminder_pager_content, container, false);

        view = updateViewWithData(view);

        return view;
    }

    @Override
    public void OnRefresh(List<ReminderReading> readings) {
        this.readings = readings;
        isFirstTime = false;
        updateViewWithData(view);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode==Activity.RESULT_OK){
            if(requestCode==ADD_REMINDER_REQUEST){
                Reminder reminder = data.getParcelableExtra("reminder");

            }
        }
    }

    protected View updateViewWithData(View view) {
        TextView dateView = (TextView)view.findViewById(R.id.dateTextView);
        //ScrollView sView = (ScrollView)view.findViewById(R.id.scrollView);
        ListView list = (ListView) view.findViewById(android.R.id.list);
        FrameLayout fL = (FrameLayout)view.findViewById(R.id.initial_layout);

        dateView.setVisibility(View.VISIBLE);

        dateView.setText(DateUtils.getDisplayText(currentDate));

        if(isFirstTime || readings==null || readings.isEmpty()){
            list.setVisibility(View.GONE);
            fL.setVisibility(View.VISIBLE);
            TextView tView = (TextView) view.findViewById(R.id.textView);
            if(isFirstTime){
                tView.setText(R.string.reminder_initial_string);
                dateView.setVisibility(View.GONE);
            }else{
                tView.setText(R.string.reminder_no_data);
            }
        }else{
            list.setVisibility(View.VISIBLE);
            fL.setVisibility(View.GONE);
        }

        Button btn = (Button) view.findViewById(R.id.add_rem);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OnAdd(null);
            }
        });

        if(readings!=null && readings.size()>0){
            //construct the list view here

            adapter = new ReminderDataAdapter(getSherlockActivity(), R.layout.row_reminder_reading, readings, currentDate);
            list.setAdapter(adapter);
            adapter.setOnSaveReminderAction(new ReminderDataAdapter.OnSaveReminderAction() {
                @Override
                public void OnSave(ReminderReading reading) {
                    if(Checker.isInternetOn(getSherlockActivity())){
                        SaveAction task = new SaveAction();
                        task.reading = reading;
                        task.execute();
                    }else{
                        Toast.makeText(getSherlockActivity(), "Network is not available....", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id)
                {
                    if (actionMode != null) {
                        // if already in action mode - do nothing
                        return false;
                    }
                    // set checked selected item and enter multi selection mode
                    adapter.toggleChecked(position);
                    getSherlockActivity().startActionMode(new ReminderListActionMode());
                    actionMode.invalidate();
                    return true;
                    }
            });

            list.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                @Override
                public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                    if (actionMode != null) {
                        actionMode.invalidate();
                        // if action mode, toggle checked state of item
                        adapter.toggleChecked(position);
                    }
                    //Nothing to be done for single click
                }
            });
        }

        return view;
    }

    @Override
    public void onResume() {
        Log.i(TAG, "onResume - " + currentDate);
        super.onResume();
    }

    @Override
    public void onPause() {
        Log.i(TAG, "onPause - " + currentDate);
        super.onPause();
        if (this.actionMode != null) {
            this.actionMode.finish();
        }
    }

    // all our ActionMode stuff here :)
    private final class ReminderListActionMode implements ActionMode.Callback {

        // " selected" string resource to update ActionBar text
        private String selected = getActivity().getString(R.string.selected);

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            adapter.enterMultiMode();
            // save global action mode
            actionMode = mode;
            return true;
        }


        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            // remove previous items
            menu.clear();
            final int checked = adapter.getCheckedItemCount();
            // update title with number of checked items
            mode.setTitle(checked + " " + this.selected);
            switch (checked) {
                case 0:
                    // if nothing checked - exit action mode
                    mode.finish();
                    return true;
                case 1:
                    // only edit
                    menu.add(Menu.NONE, R.id.action_mode_edit, Menu.NONE, "Edit")
                            .setIcon(R.drawable.ic_action_edit_white)
                            .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
                    return true;
                default:
                    getSherlockActivity().getSupportMenuInflater().inflate(R.menu.action_mode_reminder, menu);
                    return true;
            }
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            Set<Integer> checked = adapter.getCheckedItems();
            int checkedCount = checked.size();
            final Reminder[] reminders = new Reminder[checkedCount];
            int i = 0;
            for (Integer ci : checked) {
                ReminderReading reading = (ReminderReading) adapter.getItem(ci.intValue());
                reminders[i++] = reading.getReminder();
            }

            switch (item.getItemId()) {
                case R.id.action_mode_edit:
                    Toast.makeText(getActivity(), "Edit", Toast.LENGTH_LONG).show();
                    ReminderReading reading = adapter.getFirstCheckedItem();
                    OnEdit(reading.getReminder());
                    return true;

                case R.id.action_mode_delete:
                    if(adapter.getCheckedItemCount()>0){
                        OnDelete(reminders);
                    }else{
                        Toast.makeText(getSherlockActivity(), "Please select atlest one reminder..", Toast.LENGTH_SHORT).show();
                    }

                    Toast.makeText(getActivity(), "Delete", Toast.LENGTH_LONG).show();
                    return true;

                default:
                    return false;
            }
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            adapter.exitMultiMode();
            // don't forget to remove it, because we are assuming that if it's not null we are in ActionMode
            actionMode = null;
        }

    }

    public interface OnRemiderDataChangeListener {
        public void OnAdd(Date date);
        public void OnEdit(Reminder reminder);
        public void OnDelete(Reminder[] reminders);
    }

    protected OnRemiderDataChangeListener listener;

    public void setOnRemiderDataChangeListener(OnRemiderDataChangeListener listener){
        this.listener = listener;
    }

    protected void OnEdit(Reminder reminder){
        if(listener!=null) listener.OnEdit(reminder);
    }

    protected void OnDelete(Reminder[] reminders){
        if(listener!=null) listener.OnDelete(reminders);
    }

    protected void OnAdd(Date date){
        if(listener!=null) listener.OnAdd(date);
    }

    public class SaveAction extends AsyncTask<Void, Void, Void> {
        protected ReminderReading reading;

        @Override
        protected void onPostExecute(Void aVoid) {
            adapter.notifyDataSetChanged();
        }

        @Override
        protected Void doInBackground(Void... params) {
            reading = reminderEP.updateReading(reading);
            return null;
        }

    }

}
