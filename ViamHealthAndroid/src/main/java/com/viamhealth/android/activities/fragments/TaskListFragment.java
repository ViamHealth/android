package com.viamhealth.android.activities.fragments;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.actionbarsherlock.view.ActionMode;
import com.actionbarsherlock.widget.ShareActionProvider;
import com.viamhealth.android.Global_Application;
import com.viamhealth.android.R;
import com.viamhealth.android.ViamHealthPrefs;
import com.viamhealth.android.adapters.task.ATaskListAdapter;
import com.viamhealth.android.dao.rest.endpoints.TaskEP;
import com.viamhealth.android.dao.restclient.old.functionClass;
import com.viamhealth.android.model.tasks.Task;
import com.viamhealth.android.model.users.User;
import com.viamhealth.android.utils.Checker;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Kunal on 14/5/14.
 */
public class TaskListFragment extends BaseListFragment {

    private ATaskListAdapter adapter = null;
    private ListView list;
    private LinearLayout zeroItemsMessageContainer;
    private final List<Task> tasks = new ArrayList<Task>();

    private functionClass obj;
    private Global_Application ga;

    // if ActoinMode is null - assume we are in normal mode
    private ActionMode actionMode;
    private ShareActionProvider actionProvider = null;

    private ViamHealthPrefs appPrefs;

    private User selectedUser;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.task_list, null);

        selectedUser = getArguments().getParcelable("user");

        obj = new functionClass(getSherlockActivity());
        ga = ((Global_Application) getSherlockActivity().getApplicationContext());
        appPrefs = new ViamHealthPrefs(getActivity());

        this.list = (ListView) v.findViewById(android.R.id.list);
        this.zeroItemsMessageContainer = (LinearLayout) v.findViewById(R.id.zero_items_in_list);

        if (Checker.isInternetOn(getSherlockActivity())) {
            CallTaskListNavigationTask task = new CallTaskListNavigationTask();
            task.activity = getSherlockActivity();
            task.execute();
        } else {
            Toast.makeText(getSherlockActivity(), "Network is not available....", Toast.LENGTH_SHORT).show();
        }

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
        if (tasks.size() == 0) {
            this.zeroItemsMessageContainer.setVisibility(View.VISIBLE);
            this.list.setVisibility(View.GONE);
            Toast.makeText(getSherlockActivity(), "No task found...", Toast.LENGTH_SHORT).show();
            return;
        }
        this.zeroItemsMessageContainer.setVisibility(View.GONE);
        this.list.setVisibility(View.VISIBLE);

        if (this.adapter == null) {
            this.adapter = new ATaskListAdapter(getSherlockActivity());
            this.adapter.setListData(tasks);
        } else
            this.adapter.notifyDataSetChanged();

        this.list.setAdapter(adapter);
        /*this.list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                TextView message = (TextView)  view.findViewById(R.id.task_message);
                if(message.getCurrentTextColor() == Color.parseColor("#828282") ){
                    message.setTextColor(getResources().getColor(R.color.row_textcolor_selector));
                    Button choice1 = (Button) view.findViewById(R.id.task_choice_1);
                    Button choice2 = (Button) view.findViewById(R.id.task_choice_2);
                    choice2.setVisibility(View.VISIBLE);
                    choice1.setVisibility(View.VISIBLE);
                    choice1.setEnabled(true);
                    choice2.setEnabled(true);
                    choice2.setTextColor(Color.parseColor("#ffffff"));
                    choice1.setTextColor(Color.parseColor("#ffffff"));
                    //adapter.notifyDataSetChanged();
                }
            }
        });*/

    }


    // async class for calling webservice and get responce message
    public class CallTaskListNavigationTask extends AsyncTask<String, Void, String> {
        protected FragmentActivity activity;
        protected ProgressDialog dialog;

        @Override
        protected void onPreExecute() {

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
            TaskEP tep = new TaskEP(getSherlockActivity(), ga);
            List<Task> ts = tep.list(selectedUser.getId());

            Collections.sort(ts, new Comparator<Task>() {
                @Override
                public int compare(Task taskData, Task taskData2) {
                    int r = 0;
                    //if (taskData.getSetChoice() != 0 && taskData2.getSetChoice() != 0)
                    if (taskData.getWeight() > taskData2.getWeight()) {
                        r = -1;
                    } else {
                        r = 1;
                    }
                    /*else if (taskData.getSetChoice() != 0) {
                        r = 1;
                    } else if (taskData2.getSetChoice() != 0) {
                        r = -1;
                    } else {*/
                    if (taskData.getWeight() > taskData2.getWeight()) {
                        r = -1;
                    } else {
                        r = 1;
                    }
                    //}
                    return r;
                }
            });
            tasks.addAll(ts);

            return null;
        }

    }


}
