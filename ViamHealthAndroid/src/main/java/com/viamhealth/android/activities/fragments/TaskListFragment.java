package com.viamhealth.android.activities.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.actionbarsherlock.view.ActionMode;
import com.actionbarsherlock.widget.ShareActionProvider;
import com.viamhealth.android.Global_Application;
import com.viamhealth.android.R;
import com.viamhealth.android.ViamHealthPrefs;
import com.viamhealth.android.adapters.FileDataAdapter;
import com.viamhealth.android.adapters.MultiSelectionAdapter;
import com.viamhealth.android.adapters.TaskListAdapter;
import com.viamhealth.android.dao.rest.endpoints.TaskEP;
import com.viamhealth.android.dao.restclient.old.functionClass;
import com.viamhealth.android.model.FileData;
import com.viamhealth.android.model.TaskData;
import com.viamhealth.android.model.users.User;
import com.viamhealth.android.ui.helper.FileLoader;
import com.viamhealth.android.utils.Checker;
import com.viamhealth.android.utils.UIUtility;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Kunal on 14/5/14.
 */
public class TaskListFragment extends BaseListFragment{

    private TaskListAdapter adapter = null;
    private ListView list;
    private final List<TaskData> tasks = new ArrayList<TaskData>();

    private functionClass obj;
    private Global_Application ga;

    // if ActoinMode is null - assume we are in normal mode
    private ActionMode actionMode;
    private ShareActionProvider actionProvider = null;

    private ViamHealthPrefs appPrefs;

    private User selectedUser;
    final private Map<String, Uri> mapSelectedUris = new HashMap<String, Uri>();

    private static final int LIBRARY_FILE_VIEW = 1000;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_sherlock_list, null);

        selectedUser = getArguments().getParcelable("user");

        obj=new functionClass(getSherlockActivity());
        ga=((Global_Application)getSherlockActivity().getApplicationContext());
        appPrefs = new ViamHealthPrefs(getActivity());

        this.list = (ListView) v.findViewById(android.R.id.list);

        if(Checker.isInternetOn(getSherlockActivity())){
            CallTaskListNavigationTask task = new CallTaskListNavigationTask();
            task.activity = getSherlockActivity();
            task.execute();
        }else{
            Toast.makeText(getSherlockActivity(), "Network is not available....", Toast.LENGTH_SHORT).show();
        }

        //Toast.makeText(getSherlockActivity(), "Got here", Toast.LENGTH_SHORT).show();
        return v;
    }

    @Override
    public void onPause() {
        super.onPause();
        if (this.actionMode != null) {
            this.actionMode.finish();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void initListView() {
        if(tasks.size()==0){
            Toast.makeText(getSherlockActivity(), "No task found...",Toast.LENGTH_SHORT).show();
            return;
        }
        //goal_count.setText("("+files.size()+")");
        if(this.adapter==null)
            this.adapter = new TaskListAdapter(getSherlockActivity(), ga, tasks, selectedUser);
        else
            this.adapter.notifyDataSetChanged();

        this.list.setAdapter(adapter);
        this.list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                view.setBackgroundColor(Color.parseColor("#ffffff"));
                Toast.makeText(getSherlockActivity(),
                        "Click ListItem Number " + position, Toast.LENGTH_LONG)
                        .show();
            }
        });

    }


    // async class for calling webservice and get responce message
    public class CallTaskListNavigationTask extends AsyncTask<String, Void,String>
    {
        protected FragmentActivity activity;
        protected ProgressDialog dialog;

        @Override
        protected void onPreExecute()
        {

            //dialog = ProgressDialog.show(activity, "Calling", "Please wait...", true);
            dialog = new ProgressDialog(getSherlockActivity());
            dialog.setCanceledOnTouchOutside(false);
            dialog.setMessage("Please Wait....");
            dialog.show();
            tasks.clear();
            Log.i("onPreExecute", "onPreExecute");

        }

        protected void onPostExecute(String result) {
            Log.i("onPostExecute", "onPostExecute");
            dialog.dismiss();
            Log.e("TAG", "Task list size : " + tasks.size());
            initListView();
        }

        @Override
        protected String doInBackground(String... params) {
            TaskEP tep = new TaskEP(getSherlockActivity(),ga);
            List<TaskData> ts = tep.list(selectedUser.getId());
            tasks.addAll(ts);


            return null;
        }

    }


}
