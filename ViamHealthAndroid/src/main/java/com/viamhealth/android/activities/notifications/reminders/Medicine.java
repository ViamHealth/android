package com.viamhealth.android.activities.notifications.reminders;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.viamhealth.android.R;
import com.viamhealth.android.notifications.ReminderConstants;
import com.viamhealth.android.services.ReminderNotification;

/**
 * Created by kunal on 30/1/14.
 */
public class Medicine extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notification_reminder_medicine);
        System.out.println("activity callled ....................");
        //String message = getIntent().getStringExtra(CommonConstants.EXTRA_MESSAGE);
        //TextView text = (TextView) findViewById(R.id.result_message);
        //text.setText(message);
        /*Button b = (Button) findViewById(R.id.notification_remind_button);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println(" on click calllledddddddd");
                onRemindClick(view);
            }
        });

       Button c = (Button) findViewById(R.id.notification_taken_button);
        c.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println(" on click calllledddddddd");
                onDismissClick(view);
            }
        });*/

    }
/*
    public void onRemindClick(View v) {
        System.out.println("lkwjdfkljslkfjlksjflksdjflksjflksjflksdfjdslk");
        Intent intent = new Intent(getApplicationContext(), ReminderNotification.class);
        intent.setAction(ReminderConstants.ACTION_REMIND);
        startService(intent);
    }

    public void onDismissClick(View v) {
        System.out.println("kjashdkjahdkjahdskjahdkjhakjsdhajkdhkjasdkjasd");
        Intent intent = new Intent(getApplicationContext(), ReminderNotification.class);
        intent.setAction(ReminderConstants.ACTION_TAKEN);
        startService(intent);
    }
    */
}
