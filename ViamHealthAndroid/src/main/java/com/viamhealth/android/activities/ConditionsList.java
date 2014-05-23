package com.viamhealth.android.activities;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;

import com.viamhealth.android.Global_Application;
import com.viamhealth.android.R;
import com.viamhealth.android.adapters.ConditionListAdapter;
import com.viamhealth.android.dao.rest.endpoints.ConditionsEP;
import com.viamhealth.android.dao.rest.endpoints.TaskEP;
import com.viamhealth.android.model.ConditionData;
import com.viamhealth.android.model.TaskData;
import com.viamhealth.android.model.reminder.Reminder;
import com.viamhealth.android.model.users.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by shawn on 22/5/14.
 */
public class ConditionsList extends ListActivity {

    private final ArrayList<String> items = new ArrayList<String>();
    User selectedUser;
    SharedPreferences userPref;
    Global_Application ga = null;


    List<User> users = new ArrayList<User>();
    private Parcelable[] pUsers = null;
    private static final int ADD_PROFILE_FROM_LIST = 10002;

    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        this.requestWindowFeature(Window.FEATURE_PROGRESS);
        setContentView(R.layout.r2_select_conditions);
        Button btn = (Button) findViewById(R.id.btn_save);
        final EditText txt = (EditText) findViewById(R.id.editText);

        selectedUser = getIntent().getParcelableExtra("user");
        pUsers = getIntent().getParcelableArrayExtra("users");
        users.clear();
        for (int i = 0; i < pUsers.length; i++) {
            users.add((User) pUsers[i]);
        }

        userPref = getSharedPreferences("User" + selectedUser.getName() + selectedUser.getId(), Context.MODE_PRIVATE);

        ga = (Global_Application) getApplicationContext();

        final ConditionListAdapter adapter = new ConditionListAdapter(
                getLayoutInflater());

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StringBuffer str = new StringBuffer();
                String fstr = "";
                String text = txt.getText().toString().trim();
                if (text.isEmpty()) {
                } else {
                    str.append(text);
                }
                for (int i = 0; i < adapter.dataList.size(); i++) {
                    ConditionData cd = (ConditionData) adapter.dataList.get(i);
                    if (cd.isSelected()) {
                        if (str.length() > 0)
                            str.append(",");
                        str.append(cd.getName());

                    }
                }
                if (str.length() > 0) {
                    fstr = str.toString();
                    PostConditionsNavigationTask task = new PostConditionsNavigationTask();
                    task.execute(fstr.toLowerCase());
                }

            }
        });


        getListView().setAdapter(adapter);
    }


    // async class for calling webservice and get responce message
    public class PostConditionsNavigationTask extends AsyncTask<String, Void, String> {
        protected FragmentActivity activity;
        protected ProgressDialog dialog;

        @Override
        protected void onPreExecute() {

            //dialog = ProgressDialog.show(activity, "Calling", "Please wait...", true);
            dialog = new ProgressDialog(ConditionsList.this);
            dialog.setCanceledOnTouchOutside(false);
            dialog.setMessage("Please Wait....");
            dialog.show();

        }

        protected void onPostExecute(String result) {
            Log.i("onPostExecute", "onPostExecute");
            dialog.dismiss();

            Intent intent = new Intent(ConditionsList.this, TabActivity.class);
            intent.putExtra("user", selectedUser);
            Parcelable[] kusers = new Parcelable[users.size()];
            intent.putExtra("users", users.toArray(kusers));
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivityForResult(intent, ADD_PROFILE_FROM_LIST);
            finish();

        }

        @Override
        protected String doInBackground(String... params) {
            ConditionsEP tep = new ConditionsEP(ConditionsList.this, ga);
            tep.postConditions(params[0], selectedUser);
            return null;
        }

    }
}
