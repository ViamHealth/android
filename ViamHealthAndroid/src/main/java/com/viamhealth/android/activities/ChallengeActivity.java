package com.viamhealth.android.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;

import com.viamhealth.android.Global_Application;
import com.viamhealth.android.R;
import com.viamhealth.android.model.tasks.ChallengeTask;
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
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.challenge_item);
        ga = ((Global_Application) getApplicationContext());

        Intent intent = getIntent();
        final ChallengeTask item = (ChallengeTask) intent.getParcelableExtra("taskData");
        TextView t = (TextView) findViewById(R.id.textView);

        t.setText(item.getId());

    }
}
