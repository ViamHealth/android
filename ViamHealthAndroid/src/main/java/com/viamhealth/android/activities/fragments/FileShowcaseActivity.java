package com.viamhealth.android.activities.fragments;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Window;
import com.viamhealth.android.R;
import com.viamhealth.android.activities.BaseFragmentActivity;
import com.viamhealth.android.model.users.User;

/**
 * Created by monj on 31/1/14.
 */
public class FileShowcaseActivity extends BaseFragmentActivity {

    User selectedUser;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.requestWindowFeature(Window.FEATURE_PROGRESS);
        setContentView(R.layout.file_fragment_new);
        selectedUser=getIntent().getParcelableExtra("user");
        Bundle args = new Bundle();
        args.putParcelable("user", selectedUser);
        TextView tv=(TextView)findViewById(R.id.textView1);
        tv.setText(tv.getText().toString()+getIntent().getStringExtra("testName"));
        //args.putString("testName",getIntent().getStringExtra("testName"));
        Button skip= (Button)findViewById(R.id.btn_skip);
        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Button remind= (Button)findViewById(R.id.btn_remind);
        remind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setReminder();
                finish();
            }
        });

        Button next= (Button)findViewById(R.id.btn_next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        FragmentTransaction fm = FileShowcaseActivity.this.getSupportFragmentManager().beginTransaction();
        FileFragment fragment = (FileFragment)SherlockFragment.instantiate(FileShowcaseActivity.this, FileFragment.class.getName(), args);
        fm.add(R.id.realfilecontent, fragment, "file-list");
        fm.commit();
    }


    void setReminder()
    {

    }
}
