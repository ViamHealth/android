package com.viamhealth.android.activities.fragments;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.InputType;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.viamhealth.android.Global_Application;
import com.viamhealth.android.R;
import com.viamhealth.android.ViamHealthPrefs;
import com.viamhealth.android.activities.AddBreakfast;
import com.viamhealth.android.activities.AddExercise;
import com.viamhealth.android.adapters.BreakfastAdapter;
import com.viamhealth.android.adapters.DinnerAdapter;
import com.viamhealth.android.adapters.ExerciseAdapter;
import com.viamhealth.android.adapters.JournalExerciseAdapter;
import com.viamhealth.android.adapters.JournalFoodAdapter;
import com.viamhealth.android.adapters.LunchAdapter;
import com.viamhealth.android.adapters.MedicalDataAdapter;
import com.viamhealth.android.adapters.SeparatedListAdapter;
import com.viamhealth.android.adapters.SnacksAdapter;
import com.viamhealth.android.adapters.TestDataAdapter;
import com.viamhealth.android.dao.restclient.old.functionClass;
import com.viamhealth.android.model.CategoryExercise;
import com.viamhealth.android.model.CategoryFood;
import com.viamhealth.android.model.users.User;
import com.viamhealth.android.ui.RefreshableListView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;


/**
 * Created by naren on 07/10/13.
 */
public class JournalFragment extends SherlockFragment implements View.OnClickListener,ExerciseListFragment.CommunicationActivity {

    Display display;
    int height,width;
    private Bundle savedInstanceState;
    int w15,w20,w10,w50,w5,h40,h10,h5,w2,h2,w110,h200,h20;

    TextView lblback,lbl_food_date,lbl_food_time,lblbrk,lbltotalbrkcal,lbllunch,
            lbllunchcal,lblsnack,lblsnakcal,lbldinner,lbldinnercal,lblExercise,lblexercisecal,lblitem1,lblitem2,lblitem3,lblitem4,lbltotcal,lblidealcal,lblcaldiff,lblcalmsg;
    LinearLayout settiglayout_food,back_food_layout,food_main_layout,food_mid_layout,
            btn_food_time_picker,btn_food_date_picker,food_header,layout1,layout2,layout3,layout4,breakfast,lunch,snacks,dinner,exercise;
    ImageView img_date,img_time,food_icon,addDinner,addExercise,addSnacks,addLunch,addBreakfast;

    FrameLayout lstViewBreakfast,lstViewLunch,lstViewSnacks,lstViewDinner,lstViewExercise;
    ImageView img_breakfast,img_lunch,img_dinner,img_snacks,img_exercise;
    String nexturl,frm;
    Typeface tf;
    Double breakfast_cal=0.0,lunch_cal=0.0,snacks_cal=0.0,dinner_cal=0.0,exercise_cal=0.0;
    ProgressDialog dialog1;
    String sub_url="diet-tracker/";
    boolean bolbrk,bollunch,bolsnaks,boldiner=false,bolexercise=false;
    int breakfast_height=0,lunch_height=0,snacks_height=0,dinner_height=0,exercise_height=0;

    public static CallExerciseListTask task1;
    public static CallListTask taskBreakfast;
    public static CallLunchListTask taskLunch;
    public static CallDinnerListTask taskDinner;
    public static CallSnaksListTask taskSnacks;


    ViamHealthPrefs appPrefs;
    functionClass obj;
    ArrayList<CategoryFood> lstResultBreakfast = new ArrayList<CategoryFood>();
    ArrayList<CategoryFood> lstResultLunch = new ArrayList<CategoryFood>();
    ArrayList<CategoryFood> lstResultSnacks = new ArrayList<CategoryFood>();
    ArrayList<CategoryFood> lstResultDinner = new ArrayList<CategoryFood>();
    ArrayList<CategoryExercise> lstResultExercise = new ArrayList<CategoryExercise>();

    DateFormat fmtDateAndTime=DateFormat.getDateTimeInstance();
    Calendar dateAndTime=Calendar.getInstance();
    int pYear,pMonth,pDay,monthval;


    Bundle args = new Bundle();
    User selected_user;
    String selected_date;

    double target_ideal_calories=0;

    ArrayList<String> date_lst = new ArrayList<String>();




    String selecteduserid="0";
    public HashMap<String, ArrayList<String>> lst = new HashMap<String, ArrayList<String>>();
    Global_Application ga;

    View view;
    ViewPager mPager1;
    Intent addFoodIntent;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.item_pager_journal, container, false);



        User user = getArguments().getParcelable("user");

        fromOldCode();
        this.savedInstanceState=savedInstanceState;
        task1 = new CallExerciseListTask();
        taskBreakfast=new CallListTask();
        taskBreakfast.activity=getSherlockActivity();
        taskLunch=new CallLunchListTask();
        taskLunch.activity=getSherlockActivity();
        taskDinner=new CallDinnerListTask();
        taskDinner.activity=getSherlockActivity();
        taskSnacks=new CallSnaksListTask();
        taskSnacks.activity=getSherlockActivity();
        //task1.activity =getSherlockActivity();
        return view;

    }



    public static SherlockFragment getInstance()
    {
        return JournalFragment.getInstance();
    }


    private void fromOldCode() {

        appPrefs = new ViamHealthPrefs(getSherlockActivity());
        obj=new functionClass(getSherlockActivity());
        ga=((Global_Application)getSherlockActivity().getApplicationContext());
        args.putParcelable("user", selected_user);

        tf = Typeface.createFromAsset(getSherlockActivity().getAssets(), "Roboto-Condensed.ttf");
        //for get screen height width

        pYear = dateAndTime.get(Calendar.YEAR);
        pMonth = dateAndTime.get(Calendar.MONTH);
        pDay = dateAndTime.get(Calendar.DAY_OF_MONTH);
        monthval=pMonth+1;
        //updateDisplay();
        ga.selected_date=""+pYear+"-"+monthval+"-"+pDay;
        if(isInternetOn()){
            CallListTask task = new CallListTask();
            task.activity = getSherlockActivity();
            task.execute();
        }else{
            Toast.makeText(getSherlockActivity(),"Network is not available....",Toast.LENGTH_SHORT).show();
        }


        ScreenDimension();

        //calculate dynamic padding
        w10=(int)((width*3.13)/100);
        w15=(int)((width*4.68)/100);
        w20=(int)((width*6.25)/100);
        w50=(int)((width*15.63)/100);
        w5=(int)((width*1.56)/100);
        w2=(int)((width*0.60)/100);
        w110=(int)((width*31.25)/100);

        h40=(int)((height*8.34)/100);
        h10=(int)((height*2.083)/100);
        h5=(int)((height*1.042)/100);
        h2=(int)((height*0.38)/100);
        h200=(int)((height*41.67)/100);
        h20=(int)((height*4.17)/100);
        //addFoodIntent=new Intent(getSherlockActivity(),AddBreakfast.class);
/*

        layout1 = (LinearLayout)view.findViewById(R.id.layout1);
        layout1.setPadding(0, 0, 0, h10);

        layout2 = (LinearLayout)view.findViewById(R.id.layout2);
        layout2.setPadding(0, 0, 0, h10);

        layout3 = (LinearLayout)view.findViewById(R.id.layout3);
        layout3.setPadding(0, 0, 0, h10);

        layout4 = (LinearLayout)view.findViewById(R.id.layout4);
        layout4.setPadding(0, 0, 0, h10);

        lblitem1 = (TextView)view.findViewById(R.id.lblitem1);
        lblitem1.getLayoutParams().width = w110;

        lblitem2 = (TextView)view.findViewById(R.id.lblitem2);
        lblitem2.getLayoutParams().width = w110;

        lblitem3 = (TextView)view.findViewById(R.id.lblitem3);
        lblitem3.getLayoutParams().width = w110;

        lblitem4 = (TextView)view.findViewById(R.id.lblitem4);
        lblitem4.getLayoutParams().width=w110;

        lbltotcal = (TextView)view.findViewById(R.id.lbl_total_calories);
        lblidealcal=(TextView)view.findViewById(R.id.lbl_ideal_calories);
        lblcaldiff=(TextView)view.findViewById(R.id.lbl_cal_diff);
        lblcalmsg=(TextView)view.findViewById(R.id.lbl_calorie_message);

        breakfast = (LinearLayout)view.findViewById(R.id.breakfast);
        breakfast.setOnClickListener(this);

        lunch = (LinearLayout)view.findViewById(R.id.lunch);
        lunch.setOnClickListener(this);

        snacks = (LinearLayout)view.findViewById(R.id.snacks);
        snacks.setOnClickListener(this);

        dinner = (LinearLayout)view.findViewById(R.id.dinner);
        dinner.setOnClickListener(this);

        exercise = (LinearLayout)view.findViewById(R.id.exercise);
        exercise.setOnClickListener(this);

        img_breakfast=(ImageView)view.findViewById(R.id.img_breakfast);
        img_lunch=(ImageView)view.findViewById(R.id.img_lunch);
        img_snacks=(ImageView)view.findViewById(R.id.img_snacks);
        img_dinner=(ImageView)view.findViewById(R.id.img_dinner);
        img_exercise=(ImageView)view.findViewById(R.id.img_exercise);

        lstViewBreakfast = (FrameLayout)view.findViewById(R.id.lstViewBreakfast);
        lstViewBreakfast.getLayoutParams().height =LinearLayout.LayoutParams.WRAP_CONTENT;
        lstViewBreakfast.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        // Disallow ScrollView to intercept touch events.
                        v.getParent().requestDisallowInterceptTouchEvent(true);
                        break;

                    case MotionEvent.ACTION_UP:
                        // Allow ScrollView to intercept touch events.
                        v.getParent().requestDisallowInterceptTouchEvent(false);
                        break;
                }

                // Handle ListView touch events.
                v.onTouchEvent(event);
                return true;
            }
        });

        lstViewLunch = (FrameLayout)view.findViewById(R.id.lstViewLunch);
        //lstViewLunch.getLayoutParams().height = h200;
        lstViewLunch.getLayoutParams().height =LinearLayout.LayoutParams.WRAP_CONTENT;
        lstViewLunch.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        // Disallow ScrollView to intercept touch events.
                        v.getParent().requestDisallowInterceptTouchEvent(true);
                        break;

                    case MotionEvent.ACTION_UP:
                        // Allow ScrollView to intercept touch events.
                        v.getParent().requestDisallowInterceptTouchEvent(false);
                        break;
                }

                // Handle ListView touch events.
                v.onTouchEvent(event);
                return true;
            }
        });

        lstViewSnacks = (FrameLayout)view.findViewById(R.id.lstViewSnakes);
        //lstViewSnacks.getLayoutParams().height = h200;
        lstViewSnacks.getLayoutParams().height =LinearLayout.LayoutParams.WRAP_CONTENT;
        lstViewSnacks.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        // Disallow ScrollView to intercept touch events.
                        v.getParent().requestDisallowInterceptTouchEvent(true);
                        break;

                    case MotionEvent.ACTION_UP:
                        // Allow ScrollView to intercept touch events.
                        v.getParent().requestDisallowInterceptTouchEvent(false);
                        break;
                }

                // Handle ListView touch events.
                v.onTouchEvent(event);
                return true;
            }
        });

        lstViewDinner = (FrameLayout)view.findViewById(R.id.lstViewDinner);
        //lstViewDinner.getLayoutParams().height = h200;
        lstViewDinner.getLayoutParams().height =LinearLayout.LayoutParams.WRAP_CONTENT;
        lstViewDinner.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        // Disallow ScrollView to intercept touch events.
                        v.getParent().requestDisallowInterceptTouchEvent(true);
                        break;

                    case MotionEvent.ACTION_UP:
                        // Allow ScrollView to intercept touch events.
                        v.getParent().requestDisallowInterceptTouchEvent(false);
                        break;
                }

                // Handle ListView touch events.
                v.onTouchEvent(event);
                return true;
            }
        });

        lstViewExercise = (FrameLayout)view.findViewById(R.id.lstViewExercise);
        //lstViewExercise.getLayoutParams().height = h200;
        lstViewExercise.getLayoutParams().height =LinearLayout.LayoutParams.WRAP_CONTENT;
        lstViewExercise.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        // Disallow ScrollView to intercept touch events.
                        v.getParent().requestDisallowInterceptTouchEvent(true);
                        break;

                    case MotionEvent.ACTION_UP:
                        // Allow ScrollView to intercept touch events.
                        v.getParent().requestDisallowInterceptTouchEvent(false);
                        break;
                }

                // Handle ListView touch events.
                v.onTouchEvent(event);
                return true;
            }
        });



        lblbrk = (TextView)view.findViewById(R.id.lblbrk);
        lbltotalbrkcal = (TextView)view.findViewById(R.id.lbltotalbrkcal);
        lbllunch = (TextView)view.findViewById(R.id.lbllunch);
        lbllunchcal = (TextView)view.findViewById(R.id.lbllunchcal);
        lblsnack = (TextView)view.findViewById(R.id.lblsnack);
        lblsnakcal = (TextView)view.findViewById(R.id.lblsnakcal);
        lbldinner = (TextView)view.findViewById(R.id.lbldinner);
        lbldinnercal = (TextView)view.findViewById(R.id.lbldinnercal);

        lblExercise = (TextView)view.findViewById(R.id.lblexercise);
        lblexercisecal=(TextView)view.findViewById(R.id.lblexercisecal);

        addBreakfast = (ImageView)view.findViewById(R.id.addBreakfast);
        addBreakfast.setOnClickListener(this);

        addLunch = (ImageView)view.findViewById(R.id.addLunch);
        addLunch.setOnClickListener(this);

        addSnacks = (ImageView)view.findViewById(R.id.addSnacks);
        addSnacks.setOnClickListener(this);

        addDinner = (ImageView)view.findViewById(R.id.addDinner);
        addDinner.setOnClickListener(this);

        addExercise = (ImageView)view.findViewById(R.id.addExercise);
        addExercise.setOnClickListener(this);




        food_main_layout = (LinearLayout)view.findViewById(R.id.food_main_layout);
        food_main_layout.setPadding(w10, h10, w10, h10);

        btn_food_time_picker = (LinearLayout)view.findViewById(R.id.btn_food_time_picker);
        btn_food_time_picker.setOnClickListener(this);


        img_time = (ImageView)view.findViewById(R.id.img_time);
        img_time.setPadding(w5, 0, w5, 0);

        btn_food_date_picker = (LinearLayout)view.findViewById(R.id.btn_food_date_picker);
        btn_food_date_picker.setOnClickListener(this);

        img_date = (ImageView)view.findViewById(R.id.img_date);
        img_date.setPadding(w5, 0, w5, 0);

        lbl_food_date = (TextView)view.findViewById(R.id.lbl_food_date);
        lbl_food_date.setPadding(w5, 0, 0, 0);
        lbl_food_date.setTypeface(tf);



        lbl_food_time = (TextView)view.findViewById(R.id.lbl_food_time);
        lbl_food_time.setTypeface(tf);

        //food_icon = (ImageView)view.findViewById(R.id.food_icon);
        //	food_icon.setPadding(w5, h5, w5, h5);

        food_header=(LinearLayout)view.findViewById(R.id.food_header);
        food_header.setPadding(0, 0, 0, h10);

*/



    }

    public static Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void ScreenDimension()
    {
        display = getSherlockActivity().getWindowManager().getDefaultDisplay();
        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        width = display.getWidth();
        height = display.getHeight();

    }

    public void updateData()
    {
        if(isInternetOn()){
            /*if(getSherlockActivity()!=null)
            {
            //CallListTask task = new CallListTask();
            task.activity = getSherlockActivity();
            task.execute();
            }*/
        }else{
            //Toast.makeText(getSherlockActivity(),"Network is not available....",Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        if(v==breakfast){
            if(bolbrk){
                bolbrk=false;
                lstViewBreakfast.setVisibility(View.GONE);
                lstViewLunch.setVisibility(View.GONE);
                lstViewSnacks.setVisibility(View.GONE);
                lstViewDinner.setVisibility(View.GONE);
                lstViewExercise.setVisibility(View.GONE);
                img_breakfast.setImageDrawable(getResources().getDrawable(R.drawable.picker_bg_1));
            }else{
                bolbrk=true;
                if(lstResultBreakfast.size()>0){
                    lstViewBreakfast.setVisibility(View.VISIBLE);
                    lstViewLunch.setVisibility(View.GONE);
                    lstViewSnacks.setVisibility(View.GONE);
                    lstViewDinner.setVisibility(View.GONE);
                    lstViewExercise.setVisibility(View.GONE);
                    img_breakfast.setImageDrawable(getResources().getDrawable(R.drawable.picker_bg_2));
                }
            }
        }
        if(v==lunch){
            if(bollunch){
                bollunch=false;
                lstViewBreakfast.setVisibility(View.GONE);
                lstViewLunch.setVisibility(View.GONE);
                lstViewSnacks.setVisibility(View.GONE);
                lstViewDinner.setVisibility(View.GONE);
                lstViewExercise.setVisibility(View.GONE);
                img_lunch.setImageDrawable(getResources().getDrawable(R.drawable.picker_bg_1));
            }else{
                bollunch=true;
                if(lstResultLunch.size()>0){
                    lstViewBreakfast.setVisibility(View.GONE);
                    lstViewLunch.setVisibility(View.VISIBLE);
                    lstViewSnacks.setVisibility(View.GONE);
                    lstViewDinner.setVisibility(View.GONE);
                    lstViewExercise.setVisibility(View.GONE);
                    img_lunch.setImageDrawable(getResources().getDrawable(R.drawable.picker_bg_2));
                }
            }
        }
        if(v==snacks){
            if(bolsnaks){
                bolsnaks=false;
                lstViewBreakfast.setVisibility(View.GONE);
                lstViewLunch.setVisibility(View.GONE);
                lstViewSnacks.setVisibility(View.GONE);
                lstViewDinner.setVisibility(View.GONE);
                lstViewExercise.setVisibility(View.GONE);
                img_snacks.setImageDrawable(getResources().getDrawable(R.drawable.picker_bg_1));
            }else{
                bolsnaks=true;
                if(lstResultSnacks.size()>0){
                    lstViewBreakfast.setVisibility(View.GONE);
                    lstViewLunch.setVisibility(View.GONE);
                    lstViewSnacks.setVisibility(View.VISIBLE);
                    lstViewDinner.setVisibility(View.GONE);
                    lstViewExercise.setVisibility(View.GONE);
                    img_snacks.setImageDrawable(getResources().getDrawable(R.drawable.picker_bg_2));
                }
            }
        }
        if(v==dinner){

            if(boldiner){
                boldiner=false;
                lstViewBreakfast.setVisibility(View.GONE);
                lstViewLunch.setVisibility(View.GONE);
                lstViewSnacks.setVisibility(View.GONE);
                lstViewDinner.setVisibility(View.GONE);
                lstViewExercise.setVisibility(View.GONE);
                img_dinner.setImageDrawable(getResources().getDrawable(R.drawable.picker_bg_1));
            }else{
                boldiner=true;
                if(lstResultDinner.size()>0){
                    lstViewBreakfast.setVisibility(View.GONE);
                    lstViewLunch.setVisibility(View.GONE);
                    lstViewSnacks.setVisibility(View.GONE);
                    lstViewDinner.setVisibility(View.VISIBLE);
                    lstViewExercise.setVisibility(View.GONE);
                    img_dinner.setImageDrawable(getResources().getDrawable(R.drawable.picker_bg_2));
                }
            }
        }

        if(v==exercise){

            if(bolexercise){
                bolexercise=false;
                lstViewBreakfast.setVisibility(View.GONE);
                lstViewLunch.setVisibility(View.GONE);
                lstViewSnacks.setVisibility(View.GONE);
                lstViewDinner.setVisibility(View.GONE);
                lstViewExercise.setVisibility(View.GONE);
                img_exercise.setImageDrawable(getResources().getDrawable(R.drawable.picker_bg_1));

            }else{
                bolexercise=true;
                if(lstResultExercise.size()>0){
                    lstViewBreakfast.setVisibility(View.GONE);
                    lstViewLunch.setVisibility(View.GONE);
                    lstViewSnacks.setVisibility(View.GONE);
                    lstViewDinner.setVisibility(View.GONE);
                    lstViewExercise.setVisibility(View.VISIBLE);
                    img_exercise.setImageDrawable(getResources().getDrawable(R.drawable.picker_bg_2));
                }
            }
        }


        if(v==back_food_layout){
            getSherlockActivity().finish();
        }
        if(v==btn_food_date_picker){
            new DatePickerDialog(getSherlockActivity(), d,pYear,
                    pMonth,
                    pDay).show();
        }
        if(v==btn_food_time_picker){
            new TimePickerDialog(getSherlockActivity(), t,
                    dateAndTime.get(Calendar.HOUR_OF_DAY),
                    dateAndTime.get(Calendar.MINUTE),
                    true).show();
        }

        if(v==addBreakfast){
            ga.setFoodType("Breakfast");
            Intent addfood = new Intent(getSherlockActivity(),AddBreakfast.class);
            User user = getArguments().getParcelable("user");
            addfood.putExtra("user", user);
            startActivity(addfood);


        }
        if(v==addLunch){
            ga.setFoodType("Lunch");
            Intent addfood = new Intent(getSherlockActivity(),AddBreakfast.class);
            User user = getArguments().getParcelable("user");
            addfood.putExtra("user", user);
            startActivity(addfood);
        }
        if(v==addSnacks){
            ga.setFoodType("Snacks");
            Intent addfood = new Intent(getSherlockActivity(),AddBreakfast.class);
            User user = getArguments().getParcelable("user");
            addfood.putExtra("user", user);
            startActivity(addfood);
        }
        if(v==addDinner){
            ga.setFoodType("Dinner");
            Intent addfood = new Intent(getSherlockActivity(),AddBreakfast.class);
            User user = getArguments().getParcelable("user");
            addfood.putExtra("user", user);
            startActivity(addfood);
        }

        if(v==addExercise){
            ga.setFoodType("Exercise");
            Intent addExercise = new Intent(getSherlockActivity(), AddExercise.class);
            User user = getArguments().getParcelable("user");
            addExercise.putExtra("user", user);
            startActivity(addExercise);

        }

    }
    DatePickerDialog.OnDateSetListener d=new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            pMonth=monthOfYear;
            pDay=dayOfMonth;
            pYear=year;
            monthval=pMonth+1;
            updateDisplay();
            ga.selected_date=""+pYear+"-"+monthval+"-"+pDay;
            /*
            if(isInternetOn()){

                CallListTask task = new CallListTask();
                task.activity = getSherlockActivity();
                task.execute();
            }else{
                Toast.makeText(getSherlockActivity(),"Network is not available....",Toast.LENGTH_SHORT).show();
            }
            */

        }
    };
    private void updateDisplay() {
        lbl_food_date.setText(
                new StringBuilder()
                        // Month is 0 based so add 1
                        .append(pYear).append("-")
                        .append(pMonth + 1).append("-")
                        .append(pDay).append(" "));
    }
    private void updateLabelTime() {

        lbl_food_time.setText(dateAndTime.HOUR +":" +dateAndTime.MINUTE +":"+dateAndTime.SECOND);
    }

    TimePickerDialog.OnTimeSetListener t=new TimePickerDialog.OnTimeSetListener() {
        public void onTimeSet(TimePicker view, int hourOfDay,
                              int minute) {
            dateAndTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
            dateAndTime.set(Calendar.MINUTE, minute);
            updateLabelTime();
        }
    };


    public class CallExerciseListTask extends AsyncTask <String, Void,String>
    {
        protected FragmentActivity activity;

        @Override
        protected void onPreExecute()
        {

            //dialog = ProgressDialog.show(applicationContext, "Calling", "Please wait...", true);

            Log.i("onPreExecute", "onPreExecute");
            dialog1.dismiss();

        }

        protected void onPostExecute(String result)
        {

            Log.i("onPostExecute", "onPostExecute");
            //fillExerciseDetails();


            ArrayList<String> date_lst = new ArrayList<String>();
            Date now = new Date();


            Calendar cal = Calendar.getInstance();
            cal.setTime(now);
            cal.add(Calendar.DAY_OF_YEAR,1);
            SimpleDateFormat fmt= new SimpleDateFormat("yyyy-MM-dd");
            date_lst.add(fmt.format(cal.getTime()));
            cal.add(Calendar.DAY_OF_YEAR,1);
            date_lst.add(fmt.format(cal.getTime()));
            cal.add(Calendar.DAY_OF_YEAR,1);
            date_lst.add(fmt.format(cal.getTime()));
            cal.add(Calendar.DAY_OF_YEAR,1);
            date_lst.add(fmt.format(cal.getTime()));
            cal.add(Calendar.DAY_OF_YEAR,1);
            date_lst.add(fmt.format(cal.getTime()));

            mPager1=(ViewPager)view.findViewById(R.id.pager1);

            mPager1.setAdapter(new JournalPagerAdapter(date_lst));


        }
        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            Log.i("doInBackground--Object", "doInBackground--Object");
            //ga.lstResult=obj.manageGoal(appPrefs.getGoalname().toString(), type, goalvalue);
            User user = getArguments().getParcelable("user");
            lstResultExercise = obj.getExercise(user.getId().toString());
            return null;
        }

    }

    void fillTrackingDetails()
    {
        Global_Application.total_ideal_calories=breakfast_cal+lunch_cal+snacks_cal+dinner_cal-exercise_cal;
        double target_ideal_calories = appPrefs.getTargetCaloriesPerDay();
        lbltotcal.setText("Total Calories Consumed"+" "+Global_Application.total_ideal_calories+" Calories");
        lblidealcal.setText("Total Target Calories"+" "+target_ideal_calories+" Calories");

        if(Global_Application.total_ideal_calories > target_ideal_calories)
        {
            lblcaldiff.setText("You crossed the calorie limit by "+(Global_Application.total_ideal_calories-target_ideal_calories));
            lblcaldiff.setTextColor(Color.RED);
            lblcalmsg.setText("You need to improve your Calorie Consumption");
            lblcalmsg.setTextColor(Color.RED);
        }
        else
        {
            lblcaldiff.setText("You are under the daily Limit by "+(target_ideal_calories-Global_Application.total_ideal_calories));
            lblcaldiff.setTextColor(Color.WHITE);
            lblcalmsg.setText("Good Job! You are On track :)");
            lblcalmsg.setTextColor(0xFF37AA4F);
        }

        Global_Application.total_ideal_calories=0;
    }


    // async class for calling webservice and get responce message
    public class CallListTask extends AsyncTask <String, Void,String>
    {
        protected FragmentActivity activity;

        @Override
        protected void onPreExecute()
        {

            //dialog = ProgressDialog.show(activity, "Calling", "Please wait...", true);
            if(dialog1!=null)
            {
                dialog1.dismiss();
                dialog1=null;
            }

            dialog1 = new ProgressDialog(activity);
            dialog1.setCanceledOnTouchOutside(false);
            dialog1.setMessage("Please Wait....");
            dialog1.show();

            Log.i("onPreExecute", "onPreExecute");

        }

        protected void onPostExecute(String result)
        {
            //fillBreakfastDetails();
            if(isInternetOn()){
                if(getSherlockActivity()!=null)
                {
                    CallLunchListTask task = new CallLunchListTask();
                    task.activity =getSherlockActivity();
                    task.execute();
                }
            }
        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            Log.i("doInBackground--Object", "doInBackground--Object");
            //ga.lstResult=obj.manageGoal(appPrefs.getGoalname().toString(), type, goalvalue);
            Global_Application.totalcal=0;

            User user = getArguments().getParcelable("user");
            lstResultBreakfast = obj.FoodListing(Global_Application.url+"diet-tracker/?meal_type=BREAKFAST",ga.selected_date,user.getId().toString());
            return null;
        }

    }



    public void fillLunchDetails()
    {
        Double total_calories=0.0;
        int i=0;
        lunch_cal=0.0;
        for(i=0;i<lstResultLunch.size();i++)
        {
            total_calories+=Double.parseDouble(lstResultLunch.get(i).getCalories())*Double.parseDouble(lstResultLunch.get(i).getMultiplier());
        }

        if(lstResultLunch.size()>0){
            lbllunch.setText("Lunch ("+lstResultLunch.get(0).getCount()+")" );
            lbllunchcal.setText(total_calories+"");
            lunch_cal=total_calories;
            ga.lstResultLunch=lstResultLunch;
            try{
                SherlockFragmentActivity f1=getSherlockActivity();
                if(f1!=null)
                {
                    android.support.v4.app.FragmentManager man1=f1.getSupportFragmentManager();
                    FragmentTransaction fm = man1.beginTransaction();
                    LunchListFragment fragment = (LunchListFragment)SherlockFragment.instantiate(getSherlockActivity(), LunchListFragment.class.getName(), args);
                    fm.replace(R.id.lstViewLunch, fragment, "Lunch");
                    fm.commit();
                    man1.executePendingTransactions();
                    setHasOptionsMenu(true);
                }
            }
            catch(Exception e)
            {

            }

            if(isInternetOn()){
                if(getSherlockActivity()!=null)
                {
                    CallSnaksListTask task = new CallSnaksListTask();
                    task.activity =getSherlockActivity();
                    task.execute();
                }
            }else{
                //Toast.makeText(getSherlockActivity(),"Network is not available....",Toast.LENGTH_SHORT).show();
            }
        }else{
            lbllunch.setText("Lunch (0)");
            lbllunchcal.setText(Global_Application.totalcal+"");
            lstViewLunch.setVisibility(View.GONE);
            try{
                img_lunch.setImageDrawable(getResources().getDrawable(R.drawable.picker_bg_1));
            }
            catch(Exception e)
            {

            }

        }
        if(isInternetOn()){
            if(getSherlockActivity()!=null)
            {
                CallSnaksListTask task = new CallSnaksListTask();
                task.activity =getSherlockActivity();
                task.execute();
            }
        }else{
            //Toast.makeText(getSherlockActivity(),"Network is not available....",Toast.LENGTH_SHORT).show();
        }
        if(getSherlockActivity()!=null)
        {
            taskLunch=null;
            taskLunch=new CallLunchListTask();
            taskLunch.activity=getSherlockActivity();
        }
    }




    public void fillSnacksDetails()
    {
        Double total_calories=0.0;
        int i=0;
        snacks_cal=0.0;
        for(i=0;i<lstResultSnacks.size();i++)
        {
            total_calories+=Double.parseDouble(lstResultSnacks.get(i).getCalories())*Double.parseDouble(lstResultSnacks.get(i).getMultiplier());
        }

        if(lstResultSnacks.size()>0){
            lblsnack.setText("Snacks ("+lstResultSnacks.get(0).getCount()+")" );
            lblsnakcal.setText(total_calories+"");
            snacks_cal=total_calories;
            ga.lstResultSnacks=lstResultSnacks;
            try{
                SherlockFragmentActivity f1=getSherlockActivity();
                if(f1!=null)
                {
                    android.support.v4.app.FragmentManager man1=f1.getSupportFragmentManager();
                    FragmentTransaction fm = man1.beginTransaction();
                    SnacksListFragment fragment = (SnacksListFragment)SherlockFragment.instantiate(getSherlockActivity(), SnacksListFragment.class.getName(), args);
                    fm.replace(R.id.lstViewSnakes, fragment, "Lunch");
                    fm.commit();
                    man1.executePendingTransactions();
                }


                setHasOptionsMenu(true);
            }
            catch(Exception e)
            {

            }

            if(isInternetOn()){
                SherlockFragmentActivity f1=getSherlockActivity();
                if(f1!=null)
                {

                    CallDinnerListTask task = new CallDinnerListTask();
                    task.activity =getSherlockActivity();
                    task.execute();
                }
            }else{
                //Toast.makeText(getSherlockActivity(),"Network is not available....",Toast.LENGTH_SHORT).show();
            }
        }else{
            lblsnack.setText("Snacks (0)");
            lblsnakcal.setText(Global_Application.totalcal+"");
            lstViewSnacks.setVisibility(View.GONE);

            try{
                img_snacks.setImageDrawable(getResources().getDrawable(R.drawable.picker_bg_1));
            }
            catch(Exception e)
            {

            }

        }
        if(isInternetOn()){
            SherlockFragmentActivity f1=getSherlockActivity();
            if(f1!=null)
            {

                CallDinnerListTask task = new CallDinnerListTask();
                task.activity =getSherlockActivity();
                task.execute();
            }
        }else{
            //Toast.makeText(getSherlockActivity(),"Network is not available....",Toast.LENGTH_SHORT).show();
        }
        if(getSherlockActivity()!=null)
        {
            taskSnacks=null;
            taskSnacks=new CallSnaksListTask();
            taskSnacks.activity=getSherlockActivity();}
    }

    public void fillDinnerDetails()
    {
        Double total_calories=0.0;
        int i=0;
        dinner_cal=0.0;
        for(i=0;i<lstResultDinner.size();i++)
        {
            total_calories+=Double.parseDouble(lstResultDinner.get(i).getCalories())*Double.parseDouble(lstResultDinner.get(i).getMultiplier());
        }


        if(lstResultDinner.size()>0){
            lbldinner.setText("Dinner ("+lstResultDinner.get(0).getCount()+")" );
            lbldinnercal.setText(total_calories+"");
            dinner_cal=total_calories;

            ga.lstResultDinner=lstResultDinner;
            try{
                SherlockFragmentActivity f1=getSherlockActivity();
                if(f1!=null)
                {
                    android.support.v4.app.FragmentManager man1=f1.getSupportFragmentManager();

                    FragmentTransaction fm = man1.beginTransaction();
                    DinnerListFragment fragment = (DinnerListFragment)SherlockFragment.instantiate(getSherlockActivity(), DinnerListFragment.class.getName(), args);
                    fm.replace(R.id.lstViewDinner, fragment, "Lunch");
                    fm.commit();
                    man1.executePendingTransactions();

                    setHasOptionsMenu(true);
                }
            }
            catch(Exception e)
            {

            }


        }else{
            lbldinner.setText("Dinner (0)");
            lbldinnercal.setText(Global_Application.totalcal+"");
            lstViewDinner.setVisibility(View.GONE);

            try{
                img_dinner.setImageDrawable(getResources().getDrawable(R.drawable.picker_bg_1));
            }
            catch(Exception e)
            {

            }


            dialog1.dismiss();
        }

        if(isInternetOn()){
            if(getSherlockActivity()!=null)
            {
                CallExerciseListTask task = new CallExerciseListTask();
                task.activity =getSherlockActivity();
                task.execute();
            }
        }else{
            //Toast.makeText(getSherlockActivity(),"Network is not available....",Toast.LENGTH_SHORT).show();
        }
        if(getSherlockActivity()!=null)
        {
            taskDinner=null;
            taskDinner=new CallDinnerListTask();
            taskDinner.activity=getSherlockActivity();
        }

    }

    public void fillExerciseDetails()
    {
        if(dialog1!=null)
        {
            dialog1.dismiss();
        }

        Log.e("TAG", "lst size : " + lstResultExercise.size());
        double total_calories=0.0;
        int i=0;

        for(i=0;i<lstResultExercise.size();i++)
        {
            total_calories+=Double.parseDouble(lstResultExercise.get(i).getCalories());
        }

        if(lstResultExercise.size()>0){
            lblExercise.setText("Exercise ("+lstResultExercise.size()+")" );
            lblexercisecal.setText(String.valueOf(total_calories));
            exercise_cal=total_calories;
            //ExerciseAdapter adapter = new ExerciseAdapter(getSherlockActivity(),R.layout.exercise_list, lstResultExercise);
            try{
                ga.lstResultExercise=lstResultExercise;
                SherlockFragmentActivity f1=getSherlockActivity();
                if(f1!=null)
                {
                    android.support.v4.app.FragmentManager man1=f1.getSupportFragmentManager();
                    FragmentTransaction fm = man1.beginTransaction();
                    ExerciseListFragment fragment = (ExerciseListFragment)SherlockFragment.instantiate(getSherlockActivity(), ExerciseListFragment.class.getName(), args);
                    fm.replace(R.id.lstViewExercise, fragment, "Lunch");
                    fm.commit();
                    man1.executePendingTransactions();
                    setHasOptionsMenu(true);
                }
            }
            catch(Exception e)
            {

            }
        }
        else
        {
            lblExercise.setText("Exercise (0)");
            lblexercisecal.setText("0");
            lstViewExercise.setVisibility(View.GONE);
            try{
                img_exercise.setImageDrawable(getResources().getDrawable(R.drawable.picker_bg_1));
            }
            catch(Exception e)
            {

            }
        }




        //fillTrackingDetails();
        task1=null;
        task1= new CallExerciseListTask();
    }


    public void fillBreakfastDetails()
    {
        Double total_calories=0.0;
        breakfast_cal=0.0;

        for(int i=0;i<lstResultBreakfast.size();i++)
        {
            total_calories+=Double.parseDouble(lstResultBreakfast.get(i).getCalories())*Double.parseDouble(lstResultBreakfast.get(i).getMultiplier());
        }

        Toast.makeText(getSherlockActivity(),"Call List entered",Toast.LENGTH_SHORT).show();

        if(lstResultBreakfast.size()>0){
            lblbrk.setText("Breakfast ("+lstResultBreakfast.get(0).getCount()+")" );
            lbltotalbrkcal.setText(total_calories+"");
            breakfast_cal=total_calories;

            ga.lstResultBreakfast=lstResultBreakfast;
            try{
                SherlockFragmentActivity f1=getSherlockActivity();
                if(f1!=null)
                {
                    android.support.v4.app.FragmentManager man1=f1.getSupportFragmentManager();
                    FragmentTransaction fm = man1.beginTransaction();
                    BreakfastListFragment fragment = (BreakfastListFragment)SherlockFragment.instantiate(getSherlockActivity(), BreakfastListFragment.class.getName(), args);
                    fm.replace(R.id.lstViewBreakfast, fragment, "Breakfast");
                    fm.commit();
                    man1.executePendingTransactions();



                    setHasOptionsMenu(true);
                }
            }
            catch(Exception e)
            {

            }

            if(isInternetOn()){
                if(getSherlockActivity()!=null)
                {
                    CallLunchListTask task = new CallLunchListTask();
                    task.activity =getSherlockActivity();
                    task.execute();
                }
            }else{
                //Toast.makeText(getSherlockActivity(),"Network is not available....",Toast.LENGTH_SHORT).show();
            }
        }else{

            lblbrk.setText("Breakfast (0)");
            lbltotalbrkcal.setText(Global_Application.totalcal+"");
            lstViewBreakfast.setVisibility(View.GONE);
            try{
                img_breakfast.setImageDrawable(getResources().getDrawable(R.drawable.picker_bg_1));
            }
            catch(Exception e)
            {

            }

        }
        if(isInternetOn()){
            if(getSherlockActivity()!=null)
            {
                CallLunchListTask task = new CallLunchListTask();
                task.activity =getSherlockActivity();
                task.execute();
            }
        }else{
            Toast.makeText(getSherlockActivity(),"Network is not available....",Toast.LENGTH_SHORT).show();
        }
        if(getSherlockActivity()!=null)
        {
            taskBreakfast=null;
            taskBreakfast=new CallListTask();
            taskBreakfast.activity=getSherlockActivity();
        }
    }
    // async class for calling webservice and get responce message
    public class CallLunchListTask extends AsyncTask <String, Void,String>
    {
        protected FragmentActivity activity;

        @Override
        protected void onPreExecute()
        {

            //dialog = ProgressDialog.show(activity, "Calling", "Please wait...", true);
							/*	dialog1 = new ProgressDialog(getParent());
								dialog1.setMessage("Please Wait....");
								dialog1.show();*/
            Log.i("onPreExecute", "onPreExecute");

        }

        protected void onPostExecute(String result)
        {

            Log.i("onPostExecute", "onPostExecute");
            //dialog1.dismiss();
            Log.e("TAG","lst size : " + lstResultLunch.size());
            if(isInternetOn()){
                if(getSherlockActivity()!=null)
                {
                    CallSnaksListTask task = new CallSnaksListTask();
                    task.activity =getSherlockActivity();
                    task.execute();
                }
            }

        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            Log.i("doInBackground--Object", "doInBackground--Object");
            //ga.lstResult=obj.manageGoal(appPrefs.getGoalname().toString(), type, goalvalue);
            Global_Application.totalcal=0;
            User user = getArguments().getParcelable("user");
            lstResultLunch = obj.FoodListing(Global_Application.url+"diet-tracker/?meal_type=LUNCH",ga.selected_date,user.getId().toString());
            return null;
        }

    }
    // async class for calling webservice and get responce message
    public class CallSnaksListTask extends AsyncTask <String, Void,String>
    {
        protected FragmentActivity activity;

        @Override
        protected void onPreExecute()
        {

            Log.i("onPreExecute", "onPreExecute");

        }

        protected void onPostExecute(String result)
        {

            Log.i("onPostExecute", "onPostExecute");
            //dialog1.dismiss();
            Log.e("TAG","lst size : " + lstResultSnacks.size());
            if(isInternetOn()){
                if(getSherlockActivity()!=null)
                {
                    CallDinnerListTask task = new CallDinnerListTask();
                    task.activity =getSherlockActivity();
                    task.execute();
                }
            }



        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            Log.i("doInBackground--Object", "doInBackground--Object");
            //ga.lstResult=obj.manageGoal(appPrefs.getGoalname().toString(), type, goalvalue);
            Global_Application.totalcal=0;
            User user = getArguments().getParcelable("user");
            lstResultSnacks = obj.FoodListing(Global_Application.url+"diet-tracker/?meal_type=SNACKS",ga.selected_date,user.getId().toString());
            return null;
        }

    }

    // async class for calling webservice and get responce message
    public class CallDinnerListTask extends AsyncTask <String, Void,String>
    {
        protected FragmentActivity activity;

        @Override
        protected void onPreExecute()
        {

            //dialog = ProgressDialog.show(activity, "Calling", "Please wait...", true);
								/*dialog1 = new ProgressDialog(getParent());
								dialog1.setMessage("Please Wait....");
								dialog1.show();*/
            Log.i("onPreExecute", "onPreExecute");

        }

        protected void onPostExecute(String result)
        {

            Log.i("onPostExecute", "onPostExecute");
            //dialog1.dismiss();
            Log.e("TAG","lst size : " + lstResultDinner.size());
            if(isInternetOn()){
                if(getSherlockActivity()!=null)
                {
                    CallExerciseListTask task = new CallExerciseListTask();
                    task.activity =getSherlockActivity();
                    task.execute();
                }
            }


        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            Log.i("doInBackground--Object", "doInBackground--Object");
            //ga.lstResult=obj.manageGoal(appPrefs.getGoalname().toString(), type, goalvalue);
            Global_Application.totalcal=0;
            User user = getArguments().getParcelable("user");
            lstResultDinner = obj.FoodListing(Global_Application.url+"diet-tracker/?meal_type=DINNER",ga.selected_date,user.getId().toString());
            return null;
        }

    }
    // async class for calling webservice and get responce message
    public class CallBrkPullToRefreshTask extends AsyncTask <String, Void,String>
    {
        protected FragmentActivity activity;

        @Override
        protected void onPreExecute()
        {

            //dialog = ProgressDialog.show(applicationFragmentActivity, "Calling", "Please wait...", true);
            //dialog1 = new ProgressDialog(activity);
            //dialog1.setCanceledOnTouchOutside(false);
            //dialog1.setMessage("Please Wait....");
            //dialog1.show();
            Log.i("onPreExecute", "onPreExecute");

        }

        protected void onPostExecute(String result)
        {

            Log.i("onPostExecute", "onPostExecute");
            //dialog1.dismiss();
            Log.e("TAG","lst size : " + lstResultBreakfast.size());


        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            Log.i("doInBackground--Object", "doInBackground--Object");
            //ga.lstResult=obj.manageGoal(appPrefs.getGoalname().toString(), type, goalvalue);
            /*
            if(frm.equals("b")){
                lstResultBreakfast.addAll(obj.FoodListing(nexturl));
            }else if(frm.equals("l")){
                lstResultLunch.addAll(obj.FoodListing(nexturl));
            }else if(frm.equals("s")){
                lstResultSnacks.addAll(obj.FoodListing(nexturl));
            }else if(frm.equals("d")){
                lstResultDinner.addAll(obj.FoodListing(nexturl));
            }


            if(isInternetOn()){
                CallListTask task = new CallListTask();
                task.activity = getSherlockActivity();
                task.execute();
            }else{
                Toast.makeText(getSherlockActivity(),"Network is not available....",Toast.LENGTH_SHORT).show();
            }
*/
            return null;
        }

    }
    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();


        /*
        lstViewBreakfast.setVisibility(View.GONE);
        lstViewLunch.setVisibility(View.GONE);
        lstViewSnacks.setVisibility(View.GONE);
        lstViewDinner.setVisibility(View.GONE);
        lstViewExercise.setVisibility(View.GONE);
*/

    }


    public void startAddFoodActivity(String addFood)
    {
        ga.setFoodType(addFood);
        Intent addfood = new Intent(getSherlockActivity(),AddBreakfast.class);
        User user = getArguments().getParcelable("user");
        addfood.putExtra("user", user);
        startActivity(addfood);


    }

    public void startAddExerciseActivity()
    {
        ga.setFoodType("Exercise");
        Intent addExercise = new Intent(getSherlockActivity(),AddExercise.class);
        User user = getArguments().getParcelable("user");
        addExercise.putExtra("user", user);
        startActivity(addExercise);


    }


    // async class for calling webservice and get responce message

    public class CallEditTask extends AsyncTask <String, Void,String>
    {
        protected FragmentActivity activity;

        @Override
        protected void onPreExecute()
        {

            //dialog = ProgressDialog.show(activity, "Calling", "Please wait...", true);
            dialog1 = new ProgressDialog(activity);
            dialog1.setCanceledOnTouchOutside(false);
            dialog1.setMessage("Please Wait....");
            dialog1.show();
            Log.i("onPreExecute", "onPreExecute");

        }

        protected void onPostExecute(String result)
        {
            dialog1.dismiss();
            Log.i("onPostExecute", "onPostExecute");
            if(isInternetOn()){
                CallListTask task = new CallListTask();
                task.activity = activity;
                task.execute();
            }else{
                Toast.makeText(activity,"Network is not available....",Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            Log.i("doInBackground--Object", "doInBackground--Object");

            //ga.lstResult=obj.manageGoal(appPrefs.getGoalname().toString(), type, goalvalue);
            //Toast.makeText(getSherlockActivity(),"user id="+Global_Application.selectedfoodid,Toast.LENGTH_LONG ).show();
            User user = getArguments().getParcelable("user");
            return obj.EditFood(Global_Application.selectedfoodid,Global_Application.food_item,Global_Application.food_quantity,Global_Application.meal_type,user.getId().toString());
        }

    }


    public class CallEditExercise extends AsyncTask <String, Void,String>
    {
        protected FragmentActivity activity;

        @Override
        protected void onPreExecute()
        {

            //dialog = ProgressDialog.show(activity, "Calling", "Please wait...", true);
            dialog1 = new ProgressDialog(activity);
            dialog1.setCanceledOnTouchOutside(false);
            dialog1.setMessage("Please Wait....");
            dialog1.show();
            Toast.makeText(getSherlockActivity(),"Global_Application.exercise_value="+Global_Application.exercise_value,Toast.LENGTH_LONG).show();
            Log.i("onPreExecute", "onPreExecute");

        }

        protected void onPostExecute(String result)
        {
            dialog1.dismiss();
            Log.i("onPostExecute", "onPostExecute");
            if(isInternetOn()){
                CallListTask task = new CallListTask();
                task.activity = activity;
                task.execute();
            }else{
                Toast.makeText(activity,"Network is not available....",Toast.LENGTH_SHORT).show();
            }

        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            Log.i("doInBackground--Object", "doInBackground--Object");

            //ga.lstResult=obj.manageGoal(appPrefs.getGoalname().toString(), type, goalvalue);
            //Toast.makeText(getSherlockActivity(),"user id="+Global_Application.selectedfoodid,Toast.LENGTH_LONG ).show();
            User user = getArguments().getParcelable("user");
            if(user.getBmiProfile().getWeight()==null)
            {
                Global_Application.weight="0";
            }
            else
            {
                Global_Application.weight=user.getBmiProfile().getWeight().toString();
            }

            return obj.EditExercise(Global_Application.selectedexerciseid,Global_Application.weight,Global_Application.user_calories,Global_Application.time_spent,user.getId().toString(),Global_Application.exercise_value);
        }

    }




    private class JournalPagerAdapter extends PagerAdapter implements View.OnClickListener {
        private LayoutInflater inflater;
        private SherlockFragmentActivity sherlockact;
        ArrayList<String> date_lst = new ArrayList<String>();

        private
        JournalPagerAdapter(ArrayList<String> date_list) {
            //serlockact=sherlock;
            date_lst=date_list;
            inflater = getLayoutInflater(savedInstanceState);
        }



        @Override
        public void destroyItem(View container, int position, Object object)
        {
            //	Toast.makeText(ImagePagerActivity.this,"hello" +position+"", Toast.LENGTH_LONG).show();
            ((ViewPager) container).removeView((View) object);

        }

        @Override
        public void finishUpdate(View container) {
        }


        @Override
        public int getCount() {
            return 5;
        }

        @Override
        synchronized public Object instantiateItem(final View localView, final int position)
        {
            View view = inflater.inflate(R.layout.tab_fragment_journal, null);
            //TextView todayDate=(TextView)imageLayout.findViewById(R.id.todayDate);
            ViewPager v1=(ViewPager)localView;
            ((ViewPager) localView).setOffscreenPageLimit(2);
            v1.setPageMargin(60);
            ((ViewPager) localView).addView(view, 0);


            layout1 = (LinearLayout)view.findViewById(R.id.layout1);
            layout1.setPadding(0, 0, 0, h10);

            layout2 = (LinearLayout)view.findViewById(R.id.layout2);
            layout2.setPadding(0, 0, 0, h10);

            layout3 = (LinearLayout)view.findViewById(R.id.layout3);
            layout3.setPadding(0, 0, 0, h10);

            layout4 = (LinearLayout)view.findViewById(R.id.layout4);
            layout4.setPadding(0, 0, 0, h10);

            lblitem1 = (TextView)view.findViewById(R.id.lblitem1);
            lblitem1.getLayoutParams().width = w110;

            lblitem2 = (TextView)view.findViewById(R.id.lblitem2);
            lblitem2.getLayoutParams().width = w110;

            lblitem3 = (TextView)view.findViewById(R.id.lblitem3);
            lblitem3.getLayoutParams().width = w110;

            lblitem4 = (TextView)view.findViewById(R.id.lblitem4);
            lblitem4.getLayoutParams().width=w110;

            lbltotcal = (TextView)view.findViewById(R.id.lbl_total_calories);
            lblidealcal=(TextView)view.findViewById(R.id.lbl_ideal_calories);
            lblcaldiff=(TextView)view.findViewById(R.id.lbl_cal_diff);
            lblcalmsg=(TextView)view.findViewById(R.id.lbl_calorie_message);

            breakfast = (LinearLayout)view.findViewById(R.id.breakfast);
            breakfast.setOnClickListener(JournalFragment.this);

            lunch = (LinearLayout)view.findViewById(R.id.lunch);
            lunch.setOnClickListener(JournalFragment.this);

            snacks = (LinearLayout)view.findViewById(R.id.snacks);
            snacks.setOnClickListener(JournalFragment.this);

            dinner = (LinearLayout)view.findViewById(R.id.dinner);
            dinner.setOnClickListener(JournalFragment.this);

            exercise = (LinearLayout)view.findViewById(R.id.exercise);
            exercise.setOnClickListener(JournalFragment.this);

            img_breakfast=(ImageView)view.findViewById(R.id.img_breakfast);
            img_lunch=(ImageView)view.findViewById(R.id.img_lunch);
            img_snacks=(ImageView)view.findViewById(R.id.img_snacks);
            img_dinner=(ImageView)view.findViewById(R.id.img_dinner);
            img_exercise=(ImageView)view.findViewById(R.id.img_exercise);

            lstViewBreakfast = (FrameLayout)view.findViewById(R.id.lstViewBreakfast);
            lstViewBreakfast.getLayoutParams().height =LinearLayout.LayoutParams.WRAP_CONTENT;
            lstViewBreakfast.setOnTouchListener(new View.OnTouchListener() {

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    // TODO Auto-generated method stub
                    int action = event.getAction();
                    switch (action) {
                        case MotionEvent.ACTION_DOWN:
                            // Disallow ScrollView to intercept touch events.
                            v.getParent().requestDisallowInterceptTouchEvent(true);
                            break;

                        case MotionEvent.ACTION_UP:
                            // Allow ScrollView to intercept touch events.
                            v.getParent().requestDisallowInterceptTouchEvent(false);
                            break;
                    }

                    // Handle ListView touch events.
                    v.onTouchEvent(event);
                    return true;
                }
            });

            lstViewLunch = (FrameLayout)view.findViewById(R.id.lstViewLunch);
            //lstViewLunch.getLayoutParams().height = h200;
            lstViewLunch.getLayoutParams().height =LinearLayout.LayoutParams.WRAP_CONTENT;
            lstViewLunch.setOnTouchListener(new View.OnTouchListener() {

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    // TODO Auto-generated method stub
                    int action = event.getAction();
                    switch (action) {
                        case MotionEvent.ACTION_DOWN:
                            // Disallow ScrollView to intercept touch events.
                            v.getParent().requestDisallowInterceptTouchEvent(true);
                            break;

                        case MotionEvent.ACTION_UP:
                            // Allow ScrollView to intercept touch events.
                            v.getParent().requestDisallowInterceptTouchEvent(false);
                            break;
                    }

                    // Handle ListView touch events.
                    v.onTouchEvent(event);
                    return true;
                }
            });

            lstViewSnacks = (FrameLayout)view.findViewById(R.id.lstViewSnakes);
            //lstViewSnacks.getLayoutParams().height = h200;
            lstViewSnacks.getLayoutParams().height =LinearLayout.LayoutParams.WRAP_CONTENT;
            lstViewSnacks.setOnTouchListener(new View.OnTouchListener() {

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    // TODO Auto-generated method stub
                    int action = event.getAction();
                    switch (action) {
                        case MotionEvent.ACTION_DOWN:
                            // Disallow ScrollView to intercept touch events.
                            v.getParent().requestDisallowInterceptTouchEvent(true);
                            break;

                        case MotionEvent.ACTION_UP:
                            // Allow ScrollView to intercept touch events.
                            v.getParent().requestDisallowInterceptTouchEvent(false);
                            break;
                    }

                    // Handle ListView touch events.
                    v.onTouchEvent(event);
                    return true;
                }
            });

            lstViewDinner = (FrameLayout)view.findViewById(R.id.lstViewDinner);
            //lstViewDinner.getLayoutParams().height = h200;
            lstViewDinner.getLayoutParams().height =LinearLayout.LayoutParams.WRAP_CONTENT;
            lstViewDinner.setOnTouchListener(new View.OnTouchListener() {

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    // TODO Auto-generated method stub
                    int action = event.getAction();
                    switch (action) {
                        case MotionEvent.ACTION_DOWN:
                            // Disallow ScrollView to intercept touch events.
                            v.getParent().requestDisallowInterceptTouchEvent(true);
                            break;

                        case MotionEvent.ACTION_UP:
                            // Allow ScrollView to intercept touch events.
                            v.getParent().requestDisallowInterceptTouchEvent(false);
                            break;
                    }

                    // Handle ListView touch events.
                    v.onTouchEvent(event);
                    return true;
                }
            });

            lstViewExercise = (FrameLayout)view.findViewById(R.id.lstViewExercise);
            //lstViewExercise.getLayoutParams().height = h200;
            lstViewExercise.getLayoutParams().height =LinearLayout.LayoutParams.WRAP_CONTENT;
            lstViewExercise.setOnTouchListener(new View.OnTouchListener() {

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    // TODO Auto-generated method stub
                    int action = event.getAction();
                    switch (action) {
                        case MotionEvent.ACTION_DOWN:
                            // Disallow ScrollView to intercept touch events.
                            v.getParent().requestDisallowInterceptTouchEvent(true);
                            break;

                        case MotionEvent.ACTION_UP:
                            // Allow ScrollView to intercept touch events.
                            v.getParent().requestDisallowInterceptTouchEvent(false);
                            break;
                    }

                    // Handle ListView touch events.
                    v.onTouchEvent(event);
                    return true;
                }
            });



            lblbrk = (TextView)view.findViewById(R.id.lblbrk);
            lbltotalbrkcal = (TextView)view.findViewById(R.id.lbltotalbrkcal);
            lbllunch = (TextView)view.findViewById(R.id.lbllunch);
            lbllunchcal = (TextView)view.findViewById(R.id.lbllunchcal);
            lblsnack = (TextView)view.findViewById(R.id.lblsnack);
            lblsnakcal = (TextView)view.findViewById(R.id.lblsnakcal);
            lbldinner = (TextView)view.findViewById(R.id.lbldinner);
            lbldinnercal = (TextView)view.findViewById(R.id.lbldinnercal);

            lblExercise = (TextView)view.findViewById(R.id.lblexercise);
            lblexercisecal=(TextView)view.findViewById(R.id.lblexercisecal);

            addBreakfast = (ImageView)view.findViewById(R.id.addBreakfast);
            //addBreakfast.setOnClickListener(JournalPagerAdapter.this);

            addBreakfast.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Intent int1=new Intent(getParentFragment().getActivity(),AddBreakfast.class);
                    startAddFoodActivity("Breakfast");
                }
            });


            addLunch = (ImageView)view.findViewById(R.id.addLunch);
            addLunch.setOnClickListener(JournalPagerAdapter.this);

            addLunch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Intent int1=new Intent(getParentFragment().getActivity(),AddBreakfast.class);
                    startAddFoodActivity("Lunch");
                }
            });


            addSnacks = (ImageView)view.findViewById(R.id.addSnacks);
            addSnacks.setOnClickListener(JournalPagerAdapter.this);

            addSnacks.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Intent int1=new Intent(getParentFragment().getActivity(),AddBreakfast.class);
                    startAddFoodActivity("Snacks");
                }
            });

            addDinner = (ImageView)view.findViewById(R.id.addDinner);
            addDinner.setOnClickListener(JournalPagerAdapter.this);

            addDinner.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Intent int1=new Intent(getParentFragment().getActivity(),AddBreakfast.class);
                    startAddFoodActivity("Dinner");
                }
            });

            addExercise = (ImageView)view.findViewById(R.id.addExercise);
            addExercise.setOnClickListener(JournalPagerAdapter.this);

            addExercise.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Intent int1=new Intent(getParentFragment().getActivity(),AddBreakfast.class);
                    startAddExerciseActivity();
                }
            });




            food_main_layout = (LinearLayout)view.findViewById(R.id.food_main_layout);
            food_main_layout.setPadding(w10, h10, w10, h10);

            btn_food_time_picker = (LinearLayout)view.findViewById(R.id.btn_food_time_picker);
            btn_food_time_picker.setOnClickListener(JournalFragment.this);


            img_time = (ImageView)view.findViewById(R.id.img_time);
            img_time.setPadding(w5, 0, w5, 0);

            btn_food_date_picker = (LinearLayout)view.findViewById(R.id.btn_food_date_picker);
            btn_food_date_picker.setOnClickListener(JournalFragment.this);

            img_date = (ImageView)view.findViewById(R.id.img_date);
            img_date.setPadding(w5, 0, w5, 0);

            lbl_food_date = (TextView)view.findViewById(R.id.lbl_food_date);
            lbl_food_date.setPadding(w5, 0, 0, 0);
            lbl_food_date.setTypeface(tf);



            lbl_food_time = (TextView)view.findViewById(R.id.lbl_food_time);
            lbl_food_time.setTypeface(tf);

            //food_icon = (ImageView)view.findViewById(R.id.food_icon);
            //	food_icon.setPadding(w5, h5, w5, h5);

            food_header=(LinearLayout)view.findViewById(R.id.food_header);
            food_header.setPadding(0, 0, 0, h10);

            lbl_food_date.setText(date_lst.get(position));

            Global_Application.total_ideal_calories=0;


            // lblbrk.setText("Breakfast (2)" );

            bolbrk=false;
            bollunch=false;
            boldiner=false;
            bolsnaks=false;
            bolexercise=false;



            fillBreakfastDetails();
            fillLunchDetails();
            fillSnacksDetails();
            fillDinnerDetails();
            fillExerciseDetails();

            return view;
        }



        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view.equals(object);
        }

        @Override
        public void restoreState(Parcelable state, ClassLoader loader) {
        }

        @Override
        public Parcelable saveState() {
            return null;
        }

        @Override
        public void startUpdate(View container) {
        }

        public float getPageWidth(int position)
        {
            return 0.8f;
        }


        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            if(v==breakfast){
                if(bolbrk){
                    bolbrk=false;
                    lstViewBreakfast.setVisibility(View.GONE);
                    lstViewLunch.setVisibility(View.GONE);
                    lstViewSnacks.setVisibility(View.GONE);
                    lstViewDinner.setVisibility(View.GONE);
                    lstViewExercise.setVisibility(View.GONE);
                    img_breakfast.setImageDrawable(getResources().getDrawable(R.drawable.picker_bg_1));
                }else{
                    bolbrk=true;
                    if(lstResultBreakfast.size()>0){
                        lstViewBreakfast.setVisibility(View.VISIBLE);
                        lstViewLunch.setVisibility(View.GONE);
                        lstViewSnacks.setVisibility(View.GONE);
                        lstViewDinner.setVisibility(View.GONE);
                        lstViewExercise.setVisibility(View.GONE);
                        img_breakfast.setImageDrawable(getResources().getDrawable(R.drawable.picker_bg_2));
                    }
                }
            }
            if(v==lunch){
                if(bollunch){
                    bollunch=false;
                    lstViewBreakfast.setVisibility(View.GONE);
                    lstViewLunch.setVisibility(View.GONE);
                    lstViewSnacks.setVisibility(View.GONE);
                    lstViewDinner.setVisibility(View.GONE);
                    lstViewExercise.setVisibility(View.GONE);
                    img_lunch.setImageDrawable(getResources().getDrawable(R.drawable.picker_bg_1));
                }else{
                    bollunch=true;
                    if(lstResultLunch.size()>0){
                        lstViewBreakfast.setVisibility(View.GONE);
                        lstViewLunch.setVisibility(View.VISIBLE);
                        lstViewSnacks.setVisibility(View.GONE);
                        lstViewDinner.setVisibility(View.GONE);
                        lstViewExercise.setVisibility(View.GONE);
                        img_lunch.setImageDrawable(getResources().getDrawable(R.drawable.picker_bg_2));
                    }
                }
            }
            if(v==snacks){
                if(bolsnaks){
                    bolsnaks=false;
                    lstViewBreakfast.setVisibility(View.GONE);
                    lstViewLunch.setVisibility(View.GONE);
                    lstViewSnacks.setVisibility(View.GONE);
                    lstViewDinner.setVisibility(View.GONE);
                    lstViewExercise.setVisibility(View.GONE);
                    img_snacks.setImageDrawable(getResources().getDrawable(R.drawable.picker_bg_1));
                }else{
                    bolsnaks=true;
                    if(lstResultSnacks.size()>0){
                        lstViewBreakfast.setVisibility(View.GONE);
                        lstViewLunch.setVisibility(View.GONE);
                        lstViewSnacks.setVisibility(View.VISIBLE);
                        lstViewDinner.setVisibility(View.GONE);
                        lstViewExercise.setVisibility(View.GONE);
                        img_snacks.setImageDrawable(getResources().getDrawable(R.drawable.picker_bg_2));
                    }
                }
            }
            if(v==dinner){

                if(boldiner){
                    boldiner=false;
                    lstViewBreakfast.setVisibility(View.GONE);
                    lstViewLunch.setVisibility(View.GONE);
                    lstViewSnacks.setVisibility(View.GONE);
                    lstViewDinner.setVisibility(View.GONE);
                    lstViewExercise.setVisibility(View.GONE);
                    img_dinner.setImageDrawable(getResources().getDrawable(R.drawable.picker_bg_1));
                }else{
                    boldiner=true;
                    if(lstResultDinner.size()>0){
                        lstViewBreakfast.setVisibility(View.GONE);
                        lstViewLunch.setVisibility(View.GONE);
                        lstViewSnacks.setVisibility(View.GONE);
                        lstViewDinner.setVisibility(View.VISIBLE);
                        lstViewExercise.setVisibility(View.GONE);
                        img_dinner.setImageDrawable(getResources().getDrawable(R.drawable.picker_bg_2));
                    }
                }
            }

            if(v==exercise){

                if(bolexercise){
                    bolexercise=false;
                    lstViewBreakfast.setVisibility(View.GONE);
                    lstViewLunch.setVisibility(View.GONE);
                    lstViewSnacks.setVisibility(View.GONE);
                    lstViewDinner.setVisibility(View.GONE);
                    lstViewExercise.setVisibility(View.GONE);
                    img_exercise.setImageDrawable(getResources().getDrawable(R.drawable.picker_bg_1));

                }else{
                    bolexercise=true;
                    if(lstResultExercise.size()>0){
                        lstViewBreakfast.setVisibility(View.GONE);
                        lstViewLunch.setVisibility(View.GONE);
                        lstViewSnacks.setVisibility(View.GONE);
                        lstViewDinner.setVisibility(View.GONE);
                        lstViewExercise.setVisibility(View.VISIBLE);
                        img_exercise.setImageDrawable(getResources().getDrawable(R.drawable.picker_bg_2));
                    }
                }
            }


            if(v==back_food_layout){
                getSherlockActivity().finish();
            }
            if(v==btn_food_date_picker){
                new DatePickerDialog(getSherlockActivity(), d,pYear,
                        pMonth,
                        pDay).show();
            }
            if(v==btn_food_time_picker){
                new TimePickerDialog(getSherlockActivity(), t,
                        dateAndTime.get(Calendar.HOUR_OF_DAY),
                        dateAndTime.get(Calendar.MINUTE),
                        true).show();
            }

            if(v==addBreakfast){
                ga.setFoodType("Breakfast");
                Intent addfood = new Intent(getSherlockActivity(),AddBreakfast.class);
                addfood.putExtra("user", selected_user);
                startActivity(addfood);
            }
            if(v==addLunch){
                ga.setFoodType("Lunch");
                Intent addfood = new Intent(getSherlockActivity(),AddBreakfast.class);
                addfood.putExtra("user", selected_user);
                startActivity(addfood);
            }
            if(v==addSnacks){
                ga.setFoodType("Snacks");
                Intent addfood = new Intent(getSherlockActivity(),AddBreakfast.class);
                addfood.putExtra("user", selected_user);
                startActivity(addfood);
            }
            if(v==addDinner){
                ga.setFoodType("Dinner");
                Intent addfood = new Intent(getSherlockActivity(),AddBreakfast.class);
                addfood.putExtra("user", selected_user);
                startActivity(addfood);
            }

            if(v==addExercise){
                ga.setFoodType("Exercise");
                Intent addExercise = new Intent(getSherlockActivity(), AddExercise.class);
                addExercise.putExtra("user", selected_user);
                startActivity(addExercise);

            }

        }

    }


    public class CallDeleteTask extends AsyncTask <String, Void,String>
    {
        protected FragmentActivity activity;

        @Override
        protected void onPreExecute()
        {

            //dialog = ProgressDialog.show(activity, "Calling", "Please wait...", true);
            //dialog1 = new ProgressDialog(activity);
            //dialog1.setCanceledOnTouchOutside(false);
            //dialog1.setMessage("Please Wait....");
            //dialog1.show();
            Log.i("onPreExecute", "onPreExecute");
            Log.i("onPreExecute", "before calling delete task");

        }

        protected void onPostExecute(String result)
        {
            // dialog1.dismiss();
            Log.i("onPostExecute", "onPostExecute");
            if(isInternetOn()){
                CallListTask task = new CallListTask();
                task.activity = activity;
                task.execute();
            }else{
                Toast.makeText(activity,"Network is not available....",Toast.LENGTH_SHORT).show();
            }
            //onResume();

        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            Log.i("doInBackground--Object", "doInBackground--Object");
            User user = getArguments().getParcelable("user");
            return obj.DeleteFood(sub_url,Global_Application.selectedfoodid,user.getId().toString());
        }

    }
    // async class for calling webservice and get responce message
    public class CalluserMeTask extends AsyncTask <String, Void,String>
    {
        protected FragmentActivity activity;

        @Override
        protected void onPreExecute()
        {

            //dialog = ProgressDialog.show(activity, "Calling", "Please wait...", true);
            dialog1 = new ProgressDialog(activity.getParent());
            dialog1.setCanceledOnTouchOutside(false);
            dialog1.setMessage("Please Wait....");
            dialog1.show();
            Log.i("onPreExecute", "onPreExecute");

        }

        protected void onPostExecute(String result)
        {

            Log.i("onPostExecute", "onPostExecute");
            //generateView();
            dialog1.dismiss();
			/*	Intent intent = new Intent(GoalActivity.this,MainActivity.class);
				startActivity(intent);*/
        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            Log.i("doInBackground--Object", "doInBackground--Object");
            //obj.GetUserProfile(ga.getLstfamilyglobal().get(Integer.parseInt(selecteduserid)).getId());
            return null;
        }

    }

    // function for check internet is available or not
    public final boolean isInternetOn() {

        SherlockFragmentActivity act=getSherlockActivity();
        if(act!=null)
        {
            ConnectivityManager connec = (ConnectivityManager) getSherlockActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

            if ((connec.getNetworkInfo(0).getState() == NetworkInfo.State.CONNECTED)
                    || (connec.getNetworkInfo(0).getState() == NetworkInfo.State.CONNECTING)
                    || (connec.getNetworkInfo(1).getState() == NetworkInfo.State.CONNECTING)
                    || (connec.getNetworkInfo(1).getState() == NetworkInfo.State.CONNECTED)) {
                return true;
            }

            else if ((connec.getNetworkInfo(0).getState() == NetworkInfo.State.DISCONNECTED)
                    || (connec.getNetworkInfo(1).getState() ==  NetworkInfo.State.DISCONNECTED)) {
                return false;
            }
        }

        return false;
    }



}
