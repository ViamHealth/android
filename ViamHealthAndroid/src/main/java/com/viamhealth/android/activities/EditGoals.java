package com.viamhealth.android.activities;

import android.app.Activity;
import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import com.viamhealth.android.R;
import com.viamhealth.android.adapters.CheckboxGoalListAdapter;

/**
 * Created by monj on 27/1/14.
 */
public class EditGoals extends Activity{

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.requestWindowFeature(Window.FEATURE_PROGRESS);
        setContentView(R.layout.edit_delete_goals);
        Button edit=(Button)findViewById(R.id.btnEdit);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Button delete=(Button)findViewById(R.id.btnDelete);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
}



//when at least one checkbox is ticked,enable the next button
//disable it when none is clicked
//when a checkbox is clicked,enable that flag for that textbox
//and for the first clicked checkbox,start the intent,with the result
//code for that particular activity,once returned,check for that
//particular result code and if the next intent is checked,launch
//the next activity and so on.

