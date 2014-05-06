package com.viamhealth.android.activities;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.viamhealth.android.R;

/**
 * Created by monj on 27/1/14.
 */
public class SelectGoals extends ListActivity {

    private static final String[] items = {"Weight", "Blood Pressure", "Diabetes", "Cholesterol"};
    Boolean isWeight = false, isBp = false, isSugar = false, isCholesterol = false;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.requestWindowFeature(Window.FEATURE_PROGRESS);
        setContentView(R.layout.select_goals);

        //CheckboxGoalListAdapter adapter = new CheckboxGoalListAdapter(getLayoutInflater());
        getListView().setAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_multiple_choice,
                items));
        getListView().setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);

        getListView().setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getApplicationContext(), "Checkbox " + i + " selected", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                //final CheckBox cb = (CheckBox) view.findViewById(R.id.checkBox1);
                //Toast.makeText(getApplicationContext(),"checkbox "+position + " ticked",Toast.LENGTH_LONG).show();
                if (position == 0) {
                    isWeight = true ? (isWeight == false) : false;
                    Toast.makeText(getApplicationContext(), "isWeight " + isWeight, Toast.LENGTH_LONG).show();
                } else if (position == 1) {
                    isBp = true ? (isBp == false) : false;
                    Toast.makeText(getApplicationContext(), "isBp " + isBp, Toast.LENGTH_LONG).show();
                } else if (position == 2) {
                    isSugar = true ? (isSugar == false) : false;
                    Toast.makeText(getApplicationContext(), "isSugar " + isSugar, Toast.LENGTH_LONG).show();
                } else if (position == 3) {
                    isCholesterol = true ? (isWeight == false) : false;
                    Toast.makeText(getApplicationContext(), "isCholesterol " + isCholesterol, Toast.LENGTH_LONG).show();
                }
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

