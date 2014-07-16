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
import com.viamhealth.android.adapters.cat.CatListAdapter;
import com.viamhealth.android.dao.rest.endpoints.CatEP;
import com.viamhealth.android.dao.restclient.old.functionClass;
import com.viamhealth.android.model.cat.CatData;
import com.viamhealth.android.model.users.User;
import com.viamhealth.android.utils.Checker;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Kunal on 18/6/14.
 */
public class CatListFragment extends BaseListFragment {
    private CatListAdapter adapter = null;
    private ListView list;
    private LinearLayout zeroItemsMessageContainer;
    private LinearLayout addLatestContainer;
    private final List<CatData> tasks = new ArrayList<CatData>();

    private functionClass obj;
    private Global_Application ga;

    // if ActoinMode is null - assume we are in normal mode
    private ActionMode actionMode;
    private ShareActionProvider actionProvider = null;

    private ViamHealthPrefs appPrefs;

    private User selectedUser;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.cat_task_list, null);

        selectedUser = getArguments().getParcelable("user");

        obj = new functionClass(getSherlockActivity());
        ga = ((Global_Application) getSherlockActivity().getApplicationContext());
        appPrefs = new ViamHealthPrefs(getActivity());

        this.list = (ListView) v.findViewById(android.R.id.list);
        this.zeroItemsMessageContainer = (LinearLayout) v.findViewById(R.id.zero_items_in_list);
        this.addLatestContainer = (LinearLayout) v.findViewById(R.id.add_latest_health_stats_container);

        if (Checker.isInternetOn(getSherlockActivity())) {
            CallCATListNavigationTask task = new CallCATListNavigationTask();
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
            this.addLatestContainer.setVisibility(View.GONE);
            this.list.setVisibility(View.GONE);
            Toast.makeText(getSherlockActivity(), "No task found...", Toast.LENGTH_SHORT).show();
            return;
        }
        this.zeroItemsMessageContainer.setVisibility(View.GONE);
        this.addLatestContainer.setVisibility(View.VISIBLE);
        this.list.setVisibility(View.VISIBLE);

        if (this.adapter == null) {
            this.adapter = new CatListAdapter(getSherlockActivity());
            this.adapter.setListData(tasks);
        } else
            this.adapter.notifyDataSetChanged();

        this.list.setAdapter(adapter);
        this.list.post(new Runnable() {
            public void run() {
                list.setSelection(list.getCount() - 1);
            }
        });


    }


    // async class for calling webservice and get responce message
    public class CallCATListNavigationTask extends AsyncTask<String, Void, String> {
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
            CatEP tep = new CatEP(getSherlockActivity(), ga);
            List<CatData> ts = tep.list(selectedUser.getId());

            Collections.sort(ts, new Comparator<CatData>() {
                @Override
                public int compare(CatData t1, CatData t2) {
                    int r = 0;
                    if (t1.getStartDate().getTime() > t2.getStartDate().getTime()) {
                        r = 1;
                    } else {
                        r = -1;
                    }
                    return r;
                }
            });
            tasks.addAll(ts);
            return null;
        }

    }

}
