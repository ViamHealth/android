package com.viamhealth.android.activities.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.ListView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockListFragment;
import com.actionbarsherlock.view.ActionMode;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.widget.ShareActionProvider;
import com.viamhealth.android.Global_Application;
import com.viamhealth.android.R;
import com.viamhealth.android.ViamHealthPrefs;
import com.viamhealth.android.adapters.MultiSelectionAdapter;
import com.viamhealth.android.adapters.ReminderDataAdapter;
import com.viamhealth.android.dao.rest.endpoints.ReminderEP;
import com.viamhealth.android.dao.restclient.old.functionClass;
import com.viamhealth.android.model.FileData;
import com.viamhealth.android.model.reminder.Reminder;
import com.viamhealth.android.model.reminder.ReminderReading;
import com.viamhealth.android.model.users.User;
import com.viamhealth.android.utils.Checker;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * Created by naren on 24/11/13.
 */
public class ReminderListFragment extends SherlockListFragment  {

    private ReminderDataAdapter adapter;
    private ListView list;

    private ActionMode actionMode;
    private ShareActionProvider actionProvider = null;

    private Global_Application ga;
    private ViamHealthPrefs appPrefs;

    private ReminderEP reminderEP;

    private User user;

    private List<ReminderReading> readings;
    private Date currentDate;

    private final String TAG = "ReminderListFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_sherlock_list, null);

        user = getArguments().getParcelable("user");
        readings = getArguments().getParcelableArrayList("readings");
        currentDate = new Date(getArguments().getLong("currentDate"));

        Log.i(TAG, "onCreateListView - " + currentDate + "; readings - " + readings);


        appPrefs = new ViamHealthPrefs(getActivity());

        this.list = (ListView) v.findViewById(android.R.id.list);

        adapter = new ReminderDataAdapter(getSherlockActivity(), R.layout.row_reminder_reading, readings, currentDate);
        this.list.setAdapter(adapter);
        adapter.setOnSaveReminderAction(new ReminderDataAdapter.OnSaveReminderAction() {
            @Override
            public void OnSave(ReminderReading reading) {
                if(Checker.isInternetOn(getSherlockActivity())){
                    SaveAction task = new SaveAction();
                    task.reading = reading;
                    task.execute();
                }else{
                    Toast.makeText(getSherlockActivity(),"Network is not available....",Toast.LENGTH_SHORT).show();
                }
            }
        });
        reminderEP = new ReminderEP(getSherlockActivity(), ga);
        return v;
    }

    @Override
    public void onPause() {
        Log.i(TAG, "onPause - " + currentDate);
        super.onPause();
    }

    @Override
    public void onResume() {
        Log.i(TAG, "onResume - " + currentDate);
        super.onResume();
    }

    @Override
    public void onAttach(Activity activity) {
        Log.i(TAG, "onAttach - " + currentDate);
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        Log.i(TAG, "onDetach - " + currentDate);
        super.onDetach();
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
                    // all items - rename + delete
                    getSherlockActivity().getSupportMenuInflater().inflate(R.menu.action_mode_reminder, menu);
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
