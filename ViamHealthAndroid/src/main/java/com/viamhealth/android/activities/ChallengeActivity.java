package com.viamhealth.android.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.viamhealth.android.Global_Application;
import com.viamhealth.android.R;
import com.viamhealth.android.model.ChallengeData;
import com.viamhealth.android.model.enums.SelectedCurrentTab;
import com.viamhealth.android.model.users.User;

/**
 * Created by Kunal on 18/6/14.
 */
public class ChallengeActivity extends BaseFragmentActivity {
    Global_Application ga;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.challenge_item);
        ga = ((Global_Application) getApplicationContext());

        Intent intent = getIntent();
        final ChallengeData item = (ChallengeData) intent.getParcelableExtra("taskData");
        TextView t = (TextView) findViewById(R.id.textView);
        Button button = (Button) findViewById(R.id.button);

        t.setText(item.getId());

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