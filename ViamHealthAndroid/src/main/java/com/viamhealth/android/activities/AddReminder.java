package com.viamhealth.android.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.MenuItem;
import com.viamhealth.android.Global_Application;
import com.viamhealth.android.R;
import com.viamhealth.android.ViamHealthPrefs;
import com.viamhealth.android.activities.fragments.DatePickerFragment;
import com.viamhealth.android.model.enums.ReminderTime;
import com.viamhealth.android.model.enums.ReminderType;
import com.viamhealth.android.model.enums.RepeatMode;
import com.viamhealth.android.model.reminder.Reminder;
import com.viamhealth.android.model.reminder.ReminderTimeData;
import com.viamhealth.android.model.users.User;
import com.viamhealth.android.utils.UIUtility;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by naren on 31/10/13.
 */

public class AddReminder extends BaseFragmentActivity {

    ViamHealthPrefs appPrefs;
    Global_Application ga;

    ImageButton repeatBtn;
    TextView repeatTextView;
    EditText etName, etMorningCount, etNoonCount, etNightCount, etNotes;
    LinearLayout medicineLayout;
    Button btnSave, btnEnd;

    ReminderType type;

    ActionBar actionBar;
    Reminder reminder;
    User user;
    Date forDate;

    SimpleDateFormat formater = new SimpleDateFormat("dd/MM/yyyy");

    boolean isEditMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setContentView(R.layout.activity_add_reminder);

        appPrefs = new ViamHealthPrefs(AddReminder.this);
        ga=((Global_Application)getApplicationContext());

        type = (ReminderType) getIntent().getSerializableExtra("type");
        reminder = (Reminder) getIntent().getParcelableExtra("reminder");
        user = (User) getIntent().getParcelableExtra("user");
        forDate = (Date) getIntent().getSerializableExtra("date");


        if(reminder==null) {
            reminder = new Reminder();
            reminder.setStartDate(new Date());
        }

        if(reminder.getId()>0) isEditMode = true;

        repeatBtn = (ImageButton) findViewById(R.id.repeatBtn);
        if(isEditMode){
            repeatBtn.setVisibility(View.INVISIBLE);
        }
/*
        repeatBtn.setOnClickListener(new OnRepeatBtnClickListener(AddReminder.this){
            @Override
            public void onClick(View v) {
                super.onClick(v);
            }
        });
*/
        repeatBtn.setOnClickListener(new OnRepeatBtnClickListener(AddReminder.this));
            repeatTextView = (TextView) findViewById(R.id.repeatTextView);
        etName = (EditText) findViewById(R.id.reminder_name);
        etMorningCount = (EditText) findViewById(R.id.etMorningCount);
        etNoonCount = (EditText) findViewById(R.id.etNoonCount);
        etNightCount = (EditText) findViewById(R.id.etNightCount);
        etNotes = (EditText) findViewById(R.id.etComment);
        medicineLayout = (LinearLayout) findViewById(R.id.medicine_layout);
        btnSave = (Button) findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ga.GA_eventButtonPress("reminder_save");
                updateModelFromView();
                if(isValid()){
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("reminder", reminder);
                    setResult(Activity.RESULT_OK, returnIntent);
                    finish();
                }
            }
        });

        btnEnd = (Button) findViewById(R.id.btnEnd);
        if(isEditMode){
            btnEnd.setVisibility(View.VISIBLE);
        }else{
            btnEnd.setVisibility(View.GONE);
        }
        btnEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ga.GA_eventButtonPress("reminder_end");
                Intent returnIntent = new Intent();
                returnIntent.putExtra("end", true);
                returnIntent.putExtra("reminder", reminder);
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
        });

        updateViewFromModel();
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Add " + getString(type.resId()));
        actionBar.setSubtitle(user.getName());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void updateViewFromModel() {
        if(reminder.getRepeatString(AddReminder.this).isEmpty()){
            repeatTextView.setVisibility(View.GONE);
        }else{
            repeatTextView.setVisibility(View.VISIBLE);
            repeatTextView.setText(reminder.getRepeatString(AddReminder.this));
        }
        etName.setHint(type.hintResId());
        etName.setText(reminder.getName());
        etNotes.setText(reminder.getDetails());

        if(type!=ReminderType.Medicine){
            medicineLayout.setVisibility(View.GONE);
            return;
        }

        ReminderTimeData dataM = reminder.getReminderTimeData(ReminderTime.Morning);
        if(dataM!=null)
            etMorningCount.setText(dataM.getCount().toString());

        ReminderTimeData dataNoon = reminder.getReminderTimeData(ReminderTime.Noon);
        if(dataNoon!=null)
            etNoonCount.setText(dataNoon.getCount().toString());

        ReminderTimeData dataN = reminder.getReminderTimeData(ReminderTime.Night);
        if(dataN!=null)
            etNightCount.setText(dataN.getCount().toString());

    }

    private boolean isValid() {
        if(etName.getText().toString().isEmpty()){
            etName.setError("mandatory field");
            return false;
        }

        if(type==ReminderType.Medicine){
            if(etMorningCount.getText().toString().isEmpty() && etNoonCount.getText().toString().isEmpty()
                    && etNightCount.getText().toString().isEmpty()){
                Toast.makeText(AddReminder.this, "set dosage", Toast.LENGTH_LONG).show();
                return false;
            }
        }

        return true;
    }

    private void updateModelFromView() {
        reminder.setName(etName.getText().toString());
        reminder.setDetails(etNotes.getText().toString());
        reminder.setType(type);

        //Date today = new Date();
        //reminder.setStartDate(today);
        if(type!=ReminderType.Medicine){
            return;
        }

        ReminderTimeData data1 = new ReminderTimeData();
        try {
            data1.setCount(Integer.parseInt(etMorningCount.getText().toString()));
        } catch (NumberFormatException e) {
            data1.setCount(0);
        } finally {
            reminder.putReminderTimeData(ReminderTime.Morning, data1);
        }

        ReminderTimeData data2 = new ReminderTimeData();
        try {
            data2.setCount(Integer.parseInt(etNoonCount.getText().toString()));
        } catch (NumberFormatException e) {
            data2.setCount(0);
        } finally {
            reminder.putReminderTimeData(ReminderTime.Noon, data2);
        }

        ReminderTimeData data3 = new ReminderTimeData();
        try {
            data3.setCount(Integer.parseInt(etNightCount.getText().toString()));
        } catch (NumberFormatException e) {
            data3.setCount(0);
        } finally {
            reminder.putReminderTimeData(ReminderTime.Night, data3);
        }
    }

    public class OnRepeatBtnClickListener implements View.OnClickListener{

        final Context mContext;
        EditText etXDays, etDuration;
        Spinner frequeSpinner, frequeSpinner1;

        /*--date--*/

//        EditText etStartDate;
        static final int DATE_DIALG_ID = 1;
        private Button pickDate;
        private DatePicker dpResult;

        public int mYear;
        private int mMonth;
        private int mDay;

        int minYear;
        int minMonth;
//        int minDay;

        int maxYear = 1954;
        int maxMonth;
        int maxDay;

        // these are the minimum dates to set Datepicker..
        private int year;
        private int month;
        private int day;

        public String dateOutput = null;

/*--date--*/


        ArrayAdapter<String> adapter1, adapter;

        DialogFragment newFragment;
/*
        private boolean show(View v){
            if(newFragment!=null )
                return true;
            EditText text = (EditText) v;
            int inputType = text.getInputType();
            if(inputType==(InputType.TYPE_CLASS_DATETIME|InputType.TYPE_DATETIME_VARIATION_DATE)){//if the editText is a dateTime filed then showTheDatePicker
                newFragment = new DatePickerFragment(text, null);
                newFragment.show(getSupportFragmentManager(), "datePicker");
                return true;
            }
            return false;
        }


        @Override
        public boolean onTouch(View v, MotionEvent event) {
             return show(v);
        }
*/
//        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            //if(hasFocus)
                //show(v);
        }

        public OnRepeatBtnClickListener(Context context) {
            mContext = context;
        }

        @Override
        public void onClick(View v) {
            final View dialogView = LayoutInflater.from(AddReminder.this).inflate(R.layout.dialog_repeat, null);
//          etStartDate = (EditText) dialogView.findViewById(R.id.etStartDate);

            final Calendar c = Calendar.getInstance();
            mYear = (c.get(Calendar.YEAR) - 10);
            mMonth = c.get(Calendar.MONTH);
//            mMonth = mMonth + 1;
            mDay = c.get(Calendar.DAY_OF_MONTH);
//            mDay = mDay + 1;
            setCurrentDateOnView();

            pickDate = (Button) dialogView.findViewById(R.id.tvDateButton);
            etDuration = (EditText) dialogView.findViewById(R.id.etDuration);
            frequeSpinner = (Spinner) dialogView.findViewById(R.id.frequencySpinner);
            frequeSpinner1 = (Spinner) dialogView.findViewById(R.id.frequencySpinner1);
            etXDays = (EditText) dialogView.findViewById(R.id.etXDays);

            //etStartDate.setOnFocusChangeListener(OnRepeatBtnClickListener.this);

            /*
            etStartDate.setOnTouchListener(OnRepeatBtnClickListener.this);
            */

  /*          pickDate.setOnClickListener(new View.OnClickListener() {
//                @Override
                public void onClick(View view) {
                    onCreateDialog(DATE_DIALG_ID);
                }
            });
            */

            pickDate.setOnClickListener(new View.OnClickListener() {
//                @Override
                public void onClick(View view) {
                    onCreateDialog(DATE_DIALG_ID);
                }
            });

/*            pickDate.setOnClickListener(new View.OnClickListener() {
//                @Override
                public void onClick(View view) {
                    showDialog(DATE_DIALG_ID);
                }
            });
*/
            etXDays.setVisibility(View.GONE);

            final RepeatMode[] modes = RepeatMode.values();
            String[] items = getSpinnerElements(modes, 1, modes.length);
            adapter = new ArrayAdapter<String>(mContext, R.layout.custom_spinner_item, items);
            frequeSpinner.setAdapter(adapter);
            frequeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    RepeatMode mode = modes[position+1];
                    if(mode==RepeatMode.Custom){
                        //frequeSpinner.setVisibility(View.GONE);
                        etXDays.setVisibility(View.VISIBLE);
                        frequeSpinner1.setVisibility(View.VISIBLE);
                        frequeSpinner.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            String[] items1 = getSpinnerElements(modes, 1, modes.length-1);
            adapter1 = new ArrayAdapter<String>(mContext, R.layout.custom_spinner_item, items1);
            frequeSpinner1.setAdapter(adapter1);

            updateModelFromView();
            updateRepeatDataFromReminder();
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(mContext);
            dialogBuilder.setView(dialogView);
            dialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    accumulateRepeatInformation(dialogView);
                    dialog.dismiss();
                    updateViewFromModel();
                }
            });
            dialogBuilder.show();
        }

        public void setCurrentDateOnView()
        {
            dpResult = (DatePicker) findViewById(R.id.dpResult);
            final Calendar c = Calendar.getInstance();
            year  = (c.get(Calendar.YEAR));
            month = (c.get(Calendar.MONTH));
//            month = month + 1;
            day   = (c.get(Calendar.DAY_OF_MONTH));
//            day = day + 1;

            minYear = year;
            minMonth = month;
            mDay = day;

            maxMonth = month;
            maxDay = day;

//            dpResult.init(year, month, day, null);
        }


//       @Override
        protected Dialog onCreateDialog(int id)
        {
            DatePickerDialog _date = null;
            switch (DATE_DIALG_ID)
            {
                case DATE_DIALG_ID:
                {
                    if(Build.VERSION.SDK_INT >= 11)
                    {
                        _date = new DatePickerDialog(AddReminder.this , R.style.Theme_Viamhealth , datePickListener , year , month , day)
                        {
                            public void onDateChanged (DatePicker view, int ChangeYear, int ChangeMonthOfYear, int ChangeDayOfMonth)
                            {
                                if(ChangeYear > year || ChangeYear < maxYear)
                                {
                                    view.updateDate(year , month , day);
                                }
                                if(ChangeYear == year && ChangeDayOfMonth > month)
                                {
                                    view.updateDate(year , month , day);
                                }
                                if(ChangeYear == year && ChangeMonthOfYear == month && ChangeDayOfMonth > day)
                                {
                                    view.updateDate(year , month , day);
                                }

                                dateOutput = String.format("Date Selected: %02d/%02d/%04d",
                                        ChangeDayOfMonth , ChangeDayOfMonth + 1, ChangeYear);
                            }
                        };
                    }
                    else
                    {
                        _date = new DatePickerDialog(AddReminder.this ,  datePickListener , year , month , day)
                        {
                            public void onDateChanged (DatePicker view, int ChangeYear, int ChangeMonthOfYear, int ChangeDayOfMonth)
                            {
                                if(ChangeYear > year || ChangeYear < maxYear)
                                {
                                    view.updateDate(year , month , day);
                                }
                                if(ChangeYear == year && ChangeDayOfMonth > month)
                                {
                                    view.updateDate(year , month , day);
                                }
                                if(ChangeYear == year && ChangeMonthOfYear == month && ChangeDayOfMonth > day)
                                {
                                    view.updateDate(year , month , day);
                                }
                                dateOutput = String.format("Date Selected: %02d/%02d/%04d",
                                        ChangeDayOfMonth , ChangeDayOfMonth + 1, ChangeYear);
                            }
                        };
                    }
                }
            }
            return _date;
        }

        private DatePickerDialog.OnDateSetListener datePickListener = new DatePickerDialog.OnDateSetListener()
        {
            @Override
            public void onDateSet(DatePicker datePicker, int selectedYear, int selectedMonth, int selectedDay)
            {
                year = selectedYear;
                month = selectedMonth;
                day = selectedDay;

                pickDate.setText(new StringBuilder().append(day).append("/").append(month).append("/").append(year).append(" "));
                dpResult.init(year, month, day, null);
            }
        };



        protected void updateRepeatDataFromReminder() {
            Date startDate = forDate==null?reminder.getStartDate():forDate;

            if(startDate!=null) {
                pickDate.setText(formater.format(startDate));
//                etStartDate.setText(formater.format(startDate));
            }

            if(reminder.getRepeatICounter()!=null && reminder.getRepeatICounter()>0)
                etDuration.setText(reminder.getRepeatICounter().toString());

            int position = 0;
            final RepeatMode[] modes = RepeatMode.values();
            if(reminder.getRepeatEveryX()!=null && reminder.getRepeatEveryX()>1){
                position = reminder.getRepeatMode().ordinal() - 1;
                etXDays.setVisibility(View.VISIBLE);
                frequeSpinner1.setVisibility(View.VISIBLE);
                frequeSpinner.setVisibility(View.GONE);
                if(position>=0 && position<adapter1.getCount())
                    frequeSpinner1.setSelection(position);
            }else{
                etXDays.setVisibility(View.GONE);
                frequeSpinner1.setVisibility(View.GONE);
                frequeSpinner.setVisibility(View.VISIBLE);
                position = reminder.getRepeatMode().ordinal() - 1;
                if(position>=0 && position<adapter.getCount())
                    frequeSpinner.setSelection(position);
            }

            if(reminder.getRepeatEveryX()!=null && reminder.getRepeatEveryX()>0)
                etXDays.setText(reminder.getRepeatEveryX().toString());

        }

        protected void accumulateRepeatInformation(View view) {
            String startDate = pickDate.getText().toString();
//            String startDate = etStartDate.getText().toString();
            try {
                reminder.setStartDate(formater.parse(startDate));
            } catch (ParseException e) {
                reminder.setStartDate(new Date());
            }

            if(etDuration.getText().toString().trim().length()==0){
             reminder.setRepeatICounter(1);
            }
            else{
                reminder.setRepeatICounter(Integer.parseInt(etDuration.getText().toString()));
            }

            final RepeatMode[] modes = RepeatMode.values();
            int selectedPosition = frequeSpinner.getSelectedItemPosition();
            RepeatMode selectedRepeatMode = (RepeatMode) modes[selectedPosition+1];
            if(selectedRepeatMode!=RepeatMode.Custom && selectedRepeatMode!=RepeatMode.None){
                reminder.setRepeatMode(selectedRepeatMode);
                try {
                    reminder.setRepeatEveryX(Integer.parseInt(etXDays.getText().toString()));
                } catch (NumberFormatException e) {
                    reminder.setRepeatEveryX(1);
                }
            }
        }

        protected String[] getSpinnerElements(RepeatMode[] modes, int from, int to){
            String[] items = new String[to-from];
            for(int i=from, j=0; i<to; i++, j++){
                items[j] = mContext.getString(modes[i].resId());
            }
            return items;
        }

        protected class FrequencySpinnerAdapter extends ArrayAdapter<RepeatMode> {

            final int resourceId;
            final Activity activity;

            public FrequencySpinnerAdapter(Context context, RepeatMode[] objects) {
                super(context, android.R.layout.simple_spinner_item, objects);
                resourceId = android.R.layout.simple_spinner_item;
                activity = (Activity) context;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View row = convertView;

                if(row == null){
                    LayoutInflater inflater = activity.getLayoutInflater();
                    row = inflater.inflate(resourceId, parent, false);
                }

                TextView txtName = (TextView)row.findViewById(android.R.id.text1);
                txtName.setText(activity.getString(getItem(position).resId()));

                row.setMinimumHeight(UIUtility.dpToPx(AddReminder.this, 36));
                return row;
            }

        }
    }
}

/**
 * Created by Abhilash chikara (Date Changes) on 25/06/14.
 */
