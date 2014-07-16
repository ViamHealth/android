package com.viamhealth.android.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.viamhealth.android.Global_Application;
import com.viamhealth.android.R;
import com.viamhealth.android.adapters.ChallengeDayValuesAdapter;
import com.viamhealth.android.model.ChallengeData;
import com.viamhealth.android.model.enums.SelectedCurrentTab;
import com.viamhealth.android.model.users.User;

/**
 * Created by Kunal on 18/6/14.
 */
public class ChallengeActivity extends BaseFragmentActivity {
    Global_Application ga;
    User user;
    ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.challenge_item);
        ga = ((Global_Application) getApplicationContext());

        actionBar = getSupportActionBar();
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);

        Intent intent = getIntent();

        final ChallengeData item = (ChallengeData) intent.getParcelableExtra("taskData");
        TextView t = (TextView) findViewById(R.id.textView);
        Button button = (Button) findViewById(R.id.button);
        final ListView listView = (ListView) findViewById(android.R.id.list);
        ChallengeDayValuesAdapter adapter = new ChallengeDayValuesAdapter(ChallengeActivity.this, item.getDayWiseValues(), item);
        listView.setAdapter(adapter);

        t.setText(item.getTitle());
        listView.post(new Runnable() {
            public void run() {
                listView.setSelection(listView.getCount() - 1);
            }
        });

        button.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent i = new Intent(ChallengeActivity.this, TabActivity.class);
                i.putExtra("user", ga.getLoggedInUser());
                i.putExtra("selectedCurrentTab", SelectedCurrentTab.ChallengesAndTrack);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
                finish();
            }
        });

    }
}
