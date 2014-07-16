package com.viamhealth.android.activities.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Window;
import com.viamhealth.android.Global_Application;
import com.viamhealth.android.R;
import com.viamhealth.android.activities.BaseFragmentActivity;
import com.viamhealth.android.model.enums.ReminderType;
import com.viamhealth.android.model.reminder.Reminder;
import com.viamhealth.android.model.users.User;
import com.viamhealth.android.services.ReminderBackground;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by monj on 31/1/14.
 */
public class FileShowcaseActivity extends BaseFragmentActivity {

    User selectedUser;
    ArrayList<Reminder> rem1 = new ArrayList<Reminder>();
    Global_Application ga;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.requestWindowFeature(Window.FEATURE_PROGRESS);
        setContentView(R.layout.file_fragment_new);
        selectedUser = getIntent().getParcelableExtra("user");
        Bundle args = new Bundle();
        args.putParcelable("user", selectedUser);
        TextView tv = (TextView) findViewById(R.id.textView1);
        tv.setText(tv.getText().toString() + getIntent().getStringExtra("testName"));
        //args.putString("testName",getIntent().getStringExtra("testName"));
        Button skip = (Button) findViewById(R.id.btn_skip);
        ga = (Global_Application) getApplicationContext();
        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ga.GA_eventButtonPress("wizard_upload_file_skip");
                finish();
            }
        });

        Button remind = (Button) findViewById(R.id.btn_remind);
        remind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ga.GA_eventButtonPress("wizard_upload_file_remind");
                setReminder();
                finish();
            }
        });

        Button next = (Button) findViewById(R.id.btn_next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ga.GA_eventButtonPress("wizard_upload_file_next");
                finish();
            }
        });

        FragmentTransaction fm = FileShowcaseActivity.this.getSupportFragmentManager().beginTransaction();
        FileFragment fragment = (FileFragment) SherlockFragment.instantiate(FileShowcaseActivity.this, FileFragment.class.getName(), args);
        fm.add(R.id.realfilecontent, fragment, "file-list");
        fm.commit();
        ga.GA_eventGeneral("ui_action", "launch_screen", "wizard_upload_file_next");
    }


    void setReminder() {
        Intent intentservice = new Intent(FileShowcaseActivity.this, ReminderBackground.class);
        Reminder reminder = new Reminder();
        reminder.setName(getIntent().getStringExtra("testName"));
        reminder.setType(ReminderType.LabTests);
        reminder.setUserId(selectedUser.getId());
        Date dt = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(dt);
        if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
            cal.add(Calendar.DATE, 1);
        } else {
            cal.add(Calendar.DATE, 2);
        }
        reminder.setStartDate(cal.getTime());
        reminder.setEndDate(cal.getTime());
        rem1.add(reminder);
        intentservice.putParcelableArrayListExtra("reminder", rem1);
        intentservice.putExtra("user", selectedUser);
        startService(intentservice);
        finish();
    }
}
