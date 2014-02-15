package com.viamhealth.android.activities;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;
import com.viamhealth.android.Global_Application;
import com.viamhealth.android.R;
import com.viamhealth.android.activities.fragments.FileFragment;
import com.viamhealth.android.activities.fragments.FileListFragment;
import com.viamhealth.android.activities.fragments.FileShowcaseActivity;
import com.viamhealth.android.adapters.CheckboxGoalListAdapter;
import com.viamhealth.android.model.enums.Gender;
import com.viamhealth.android.model.enums.ReminderType;
import com.viamhealth.android.model.reminder.Reminder;
import com.viamhealth.android.model.users.User;
import com.viamhealth.android.services.ReminderBackground;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by monj on 27/1/14.
 */
public class SelectFiles extends ListActivity {

    Boolean isWeight=false,isBp=false,isSugar=false,isCholesterol=false;
    //private static final String[] items={"Blood Test","Sugar Test","Thyroid Test","Cholesterol Test","Master Checkup"};
    private final ArrayList<String> items= new ArrayList<String>();
    private HashMap mymap = new HashMap();
    User selectedUser;
    private Boolean[] checkList;
    ArrayList<String> displayList;
    SharedPreferences userPref;
    ArrayList<Reminder> rem1 = new ArrayList<Reminder>();
    String reminderList=null;
    String fileUploadTest=null;
    Global_Application ga=null;
    //private static Boolean[] values = new Boolean();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.requestWindowFeature(Window.FEATURE_PROGRESS);
        setContentView(R.layout.select_files);
        Button btn = (Button)findViewById(R.id.btn_next);
        Button btn_skip=(Button)findViewById(R.id.btn_skip);
        selectedUser = getIntent().getParcelableExtra("user");
        userPref=getSharedPreferences("User" + selectedUser.getName()+selectedUser.getId(), Context.MODE_PRIVATE);
        mymap.put(20,items);
        displayList=(ArrayList<String>)mymap.get(20);
        ga=(Global_Application)getApplicationContext();

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent intent= new Intent(SelectFiles.this,TabActivity.class);

                ga.GA_eventButtonPress("wizard_select_tests_screen_next");
                SharedPreferences.Editor edit1=userPref.edit();

                edit1.putBoolean("isTest",true); //MJ:set to true
                edit1.commit();
                Intent intent1= new Intent(SelectFiles.this,TabActivity.class);
                intent1.putExtra("user",selectedUser);
                intent1.putExtra("users",getIntent().getParcelableArrayExtra("users"));
                intent1.putExtra("isTab", true);
                startActivity(intent1);
                for(int i=displayList.size()-1;i>=0;i--)
                {
                    if(checkList[i]==true)
                    {
                        if(fileUploadTest==null)
                        {
                            fileUploadTest="Upload test results for future reference ("  + displayList.get(i);
                        }
                        else
                        {
                            fileUploadTest=fileUploadTest+", "+displayList.get(i);
                        }
                    }
                    else
                    {
                        if(reminderList==null)
                        {
                            reminderList="Schedule the following ("+ displayList.get(i);
                        }
                        else
                        {
                            reminderList=reminderList+", "+displayList.get(i);
                        }
                    }
                }
                if(reminderList != null)
                    reminderList = reminderList + " )";
                if(fileUploadTest != null)
                    fileUploadTest = fileUploadTest + " )";

                if(fileUploadTest!=null)
                {
                    intent1= new Intent(SelectFiles.this,FileShowcaseActivity.class);
                    intent1.putExtra("user", selectedUser);
                    intent1.putExtra("testName",fileUploadTest);
                    startActivity(intent1);
                }

                if(reminderList!=null)
                {
                    Reminder reminder = new Reminder();
                    reminder.setName(reminderList);
                    reminder.setType(ReminderType.LabTests);
                    reminder.setUserId(selectedUser.getId());
                    Date dt = new Date();
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(dt);
                    cal.add(Calendar.DATE, 1);
                    dt=cal.getTime();
                    reminder.setStartDate(dt);
                    reminder.setEndDate(dt);
                    rem1.add(reminder);
                    Intent intentservice = new Intent(SelectFiles.this, ReminderBackground.class);
                    intentservice.putParcelableArrayListExtra("reminder",rem1);
                    intentservice.putExtra("user",selectedUser);
                    startService(intentservice);
                }
                finish();
            }
        });

        btn_skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    ga.GA_eventButtonPress("wizard_select_tests_screen_skip");
                    Intent intent = new Intent(SelectFiles.this, TabActivity.class);
                    intent.putExtra("user", selectedUser);
                    intent.putExtra("users",getIntent().getParcelableArrayExtra("users"));
                    intent.putExtra("isTab", true);
                    startActivity(intent);
                    finish();
            }
        });


        if(selectedUser.getProfile()!=null)
        {
            if(items!=null)
            {
                if(items.size()>0)
                {
                    items.clear();
                }
            }

            if(selectedUser.getProfile().getAge()>=18)
            {
                items.add("Blood Pressure");
                items.add("Cholesterol");
                items.add("Blood Sugar");
            }

            if(selectedUser.getProfile().getAge()>=20 && selectedUser.getProfile().getGender()== Gender.Female)
            {
                items.add("Pap Smear");
                items.add("Mammogram");
                items.add("Breast Exam");
            }

            if(selectedUser.getProfile().getAge()>=20 && selectedUser.getProfile().getGender()== Gender.Male)
            {
                items.add("Prostate Exam");
                //items.add("Rectal Exam");
                //items.add("Testicular Exam");
            }
        }
        if(items.size()==0)
        {
            Intent intent = new Intent(SelectFiles.this, TabActivity.class);
            intent.putExtra("user", selectedUser);
            intent.putExtra("users",getIntent().getParcelableArrayExtra("users"));
            intent.putExtra("isTab", true);
            startActivity(intent);
            finish();
        }
        else
        {
            ga.GA_eventGeneral("ui_action","launch_screen","wizard_select_tests_screen");
        }


        /*
        items.add("Blood Test");
        items.add("Sugar Test");
        items.add("Thyroid Test");
        items.add("Cholesterol Test");
        items.add("Master Checkup");
*/



        checkList= new Boolean[displayList.size()] ;
        for(int i=0;i<displayList.size();i++)
        {
            checkList[i]=false;
        }

        //CheckboxGoalListAdapter adapter = new CheckboxGoalListAdapter(getLayoutInflater());
        getListView().setAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_multiple_choice,
                displayList));
        getListView().setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);

        getListView().setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override

            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getApplicationContext(),"Checkbox "+i+" selected",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,final int position, long id) {
                checkList[position]=true;
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

