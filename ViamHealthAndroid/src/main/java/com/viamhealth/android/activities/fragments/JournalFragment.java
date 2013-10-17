package com.viamhealth.android.activities.fragments;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.viamhealth.android.Global_Application;
import com.viamhealth.android.R;
import com.viamhealth.android.ViamHealthPrefs;
import com.viamhealth.android.activities.AddBreakfast;
import com.viamhealth.android.activities.AddExercise;
import com.viamhealth.android.adapters.BreakfastAdapter;
import com.viamhealth.android.adapters.DinnerAdapter;
import com.viamhealth.android.adapters.LunchAdapter;
import com.viamhealth.android.adapters.SnacksAdapter;
import com.viamhealth.android.dao.restclient.old.functionClass;
import com.viamhealth.android.model.CategoryFood;
import com.viamhealth.android.model.users.User;
import com.viamhealth.android.ui.RefreshableListView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

/**
 * Created by naren on 07/10/13.
 */
public class JournalFragment extends Fragment implements View.OnClickListener{

    Display display;
    int height,width;
    int w15,w20,w10,w50,w5,h40,h10,h5,w2,h2,w110,h200,h20;

    TextView lblback,lbl_food_date,lbl_food_time,lblbrk,lbltotalbrkcal,lbllunch,
            lbllunchcal,lblsnack,lblsnakcal,lbldinner,lbldinnercal,lblitem1,lblitem2,lblitem3,lblitem4;
    LinearLayout settiglayout_food,back_food_layout,food_main_layout,food_mid_layout,
            btn_food_time_picker,btn_food_date_picker,food_header,layout1,layout2,layout3,layout4,breakfast,lunch,snacks,dinner;
    ImageView img_date,img_time,food_icon,addDinner,addExercise,addSnacks,addLunch,addBreakfast;
    RefreshableListView lstViewLunch,lstViewSnacks,lstViewDinner;
    RefreshableListView lstViewBreakfast;
    String nexturl,frm;
    Typeface tf;
    ProgressDialog dialog1;
    boolean bolbrk,bollunch,bolsnaks,boldiner=false;

    ViamHealthPrefs appPrefs;
    functionClass obj;
    ArrayList<CategoryFood> lstResultBreakfast = new ArrayList<CategoryFood>();
    ArrayList<CategoryFood> lstResultLunch = new ArrayList<CategoryFood>();
    ArrayList<CategoryFood> lstResultSnacks = new ArrayList<CategoryFood>();
    ArrayList<CategoryFood> lstResultDinner = new ArrayList<CategoryFood>();

    DateFormat fmtDateAndTime=DateFormat.getDateTimeInstance();
    Calendar dateAndTime=Calendar.getInstance();
    int pYear,pMonth,pDay;
    String selecteduserid="0";
    public HashMap<String, ArrayList<String>> lst = new HashMap<String, ArrayList<String>>();
    Global_Application ga;

    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.tab_fragment_journal, container, false);
        fromOldCode();
        return view;
    }

    private void fromOldCode() {

        appPrefs = new ViamHealthPrefs(getActivity());
        obj=new functionClass(getActivity());
        ga=((Global_Application)getActivity().getApplicationContext());

        tf = Typeface.createFromAsset(getActivity().getAssets(), "Roboto-Condensed.ttf");
        //for get screen height width
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

        breakfast = (LinearLayout)view.findViewById(R.id.breakfast);
        breakfast.setOnClickListener(this);

        lunch = (LinearLayout)view.findViewById(R.id.lunch);
        lunch.setOnClickListener(this);

        snacks = (LinearLayout)view.findViewById(R.id.snacks);
        snacks.setOnClickListener(this);

        dinner = (LinearLayout)view.findViewById(R.id.dinner);
        dinner.setOnClickListener(this);

        lstViewBreakfast = (RefreshableListView)view.findViewById(R.id.lstViewBreakfast);
        lstViewBreakfast.getLayoutParams().height = h200;
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

        lstViewLunch = (RefreshableListView)view.findViewById(R.id.lstViewLunch);
        lstViewLunch.getLayoutParams().height = h200;
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

        lstViewSnacks = (RefreshableListView)view.findViewById(R.id.lstViewSnakes);
        lstViewSnacks.getLayoutParams().height = h200;
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

        lstViewDinner = (RefreshableListView)view.findViewById(R.id.lstViewDinner);
        lstViewDinner.getLayoutParams().height = h200;
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


        lblbrk = (TextView)view.findViewById(R.id.lblbrk);
        lbltotalbrkcal = (TextView)view.findViewById(R.id.lbltotalbrkcal);
        lbllunch = (TextView)view.findViewById(R.id.lbllunch);
        lbllunchcal = (TextView)view.findViewById(R.id.lbllunchcal);
        lblsnack = (TextView)view.findViewById(R.id.lblsnack);
        lblsnakcal = (TextView)view.findViewById(R.id.lblsnakcal);
        lbldinner = (TextView)view.findViewById(R.id.lbldinner);
        lbldinnercal = (TextView)view.findViewById(R.id.lbldinnercal);

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


        pYear = dateAndTime.get(Calendar.YEAR);
        pMonth = dateAndTime.get(Calendar.MONTH);
        pDay = dateAndTime.get(Calendar.DAY_OF_MONTH);
        updateDisplay();
        lstViewBreakfast.setOnRefreshListener(new RefreshableListView.OnRefreshListener() {

            @Override
            public void onRefresh() {
                // TODO Auto-generated method stub
                if(!ga.getNextbrekfast().toString().equals("null")){
                    nexturl = ga.getNextbrekfast();
                    frm="b";
                    if(isInternetOn()){
                        CallBrkPullToRefreshTask task = new CallBrkPullToRefreshTask();
                        task.activity =getActivity();
                        task.execute();
                    }else{
                        Toast.makeText(getActivity(), "Network is not available....", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        lstViewLunch.setOnRefreshListener(new RefreshableListView.OnRefreshListener() {

            @Override
            public void onRefresh() {
                // TODO Auto-generated method stub
                if(!ga.getNextlunch().toString().equals("null")){
                    nexturl = ga.getNextlunch();
                    frm="l";
                    if(isInternetOn()){
                        CallBrkPullToRefreshTask task = new CallBrkPullToRefreshTask();
                        task.activity =getActivity();
                        task.execute();
                    }else{
                        Toast.makeText(getActivity(),"Network is not available....",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        lstViewSnacks.setOnRefreshListener(new RefreshableListView.OnRefreshListener() {

            @Override
            public void onRefresh() {
                // TODO Auto-generated method stub
                if(!ga.getNextsnacks().toString().equals("null")){
                    nexturl = ga.getNextsnacks();
                    frm="s";
                    if(isInternetOn()){
                        CallBrkPullToRefreshTask task = new CallBrkPullToRefreshTask();
                        task.activity =getActivity();
                        task.execute();
                    }else{
                        Toast.makeText(getActivity(),"Network is not available....",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        lstViewDinner.setOnRefreshListener(new RefreshableListView.OnRefreshListener() {

            @Override
            public void onRefresh() {
                // TODO Auto-generated method stub
                if(!ga.getNextdinner().toString().equals("null")){
                    nexturl = ga.getNextdinner();
                    frm="d";
                    if(isInternetOn()){
                        CallBrkPullToRefreshTask task = new CallBrkPullToRefreshTask();
                        task.activity =getActivity();
                        task.execute();
                    }else{
                        Toast.makeText(getActivity(),"Network is not available....",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        lstViewBreakfast.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View v, int arg2,
                                    long arg3) {
                // TODO Auto-generated method stub
                ImageView view = (ImageView)v.findViewById(R.id.delete);
                view.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        // TODO Auto-generated method stub
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                                getActivity());

                        // set title
                        alertDialogBuilder.setTitle("Confirmation");

                        // set dialog message
                        alertDialogBuilder
                                .setMessage("Are you sure you want to delete this food?")
                                .setCancelable(false)
                                .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        // if this button is clicked, close
                                        // current activity
                                        dialog.cancel();
                                        if(isInternetOn()){
                                            CallDeleteTask task = new CallDeleteTask();
                                            task.activity =getActivity();
                                            task.execute();
                                        }else{
                                            Toast.makeText(getActivity(),"Network is not available....",Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                })
                                .setNegativeButton("No",new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        // if this button is clicked, just close
                                        // the dialog box and do nothing
                                        dialog.cancel();
                                    }
                                });

                        // create alert dialog
                        AlertDialog alertDialog = alertDialogBuilder.create();

                        // show it
                        alertDialog.show();
                    }
                });

            }
        });
        lstViewLunch.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View v, int arg2,
                                    long arg3) {
                // TODO Auto-generated method stub
                ImageView view = (ImageView)v.findViewById(R.id.delete);
                view.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        // TODO Auto-generated method stub
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                                getActivity());

                        // set title
                        alertDialogBuilder.setTitle("Confirmation");

                        // set dialog message
                        alertDialogBuilder
                                .setMessage("Are you sure you want to delete this food?")
                                .setCancelable(false)
                                .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        // if this button is clicked, close
                                        // current activity
                                        dialog.cancel();
                                        if(isInternetOn()){
                                            CallDeleteTask task = new CallDeleteTask();
                                            task.activity =getActivity();
                                            task.execute();
                                        }else{
                                            Toast.makeText(getActivity(),"Network is not available....",Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                })
                                .setNegativeButton("No",new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        // if this button is clicked, just close
                                        // the dialog box and do nothing
                                        dialog.cancel();
                                    }
                                });

                        // create alert dialog
                        AlertDialog alertDialog = alertDialogBuilder.create();

                        // show it
                        alertDialog.show();
                    }
                });

            }
        });
        lstViewSnacks.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View v, int arg2,
                                    long arg3) {
                // TODO Auto-generated method stub
                ImageView view = (ImageView)v.findViewById(R.id.delete);
                view.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        // TODO Auto-generated method stub
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                                getActivity());

                        // set title
                        alertDialogBuilder.setTitle("Confirmation");

                        // set dialog message
                        alertDialogBuilder
                                .setMessage("Are you sure you want to delete this food?")
                                .setCancelable(false)
                                .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        // if this button is clicked, close
                                        // current activity
                                        dialog.cancel();
                                        if(isInternetOn()){
                                            CallDeleteTask task = new CallDeleteTask();
                                            task.activity =getActivity();
                                            task.execute();
                                        }else{
                                            Toast.makeText(getActivity(),"Network is not available....",Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                })
                                .setNegativeButton("No",new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        // if this button is clicked, just close
                                        // the dialog box and do nothing
                                        dialog.cancel();
                                    }
                                });

                        // create alert dialog
                        AlertDialog alertDialog = alertDialogBuilder.create();

                        // show it
                        alertDialog.show();
                    }
                });

            }
        });
        lstViewDinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View v, int arg2,
                                    long arg3) {
                // TODO Auto-generated method stub
                ImageView view = (ImageView)v.findViewById(R.id.delete);
                view.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        // TODO Auto-generated method stub
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                                getActivity());

                        // set title
                        alertDialogBuilder.setTitle("Confirmation");

                        // set dialog message
                        alertDialogBuilder
                                .setMessage("Are you sure you want to delete this food?")
                                .setCancelable(false)
                                .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        // if this button is clicked, close
                                        // current activity
                                        dialog.cancel();
                                        if(isInternetOn()){
                                            CallDeleteTask task = new CallDeleteTask();
                                            task.activity =getActivity();
                                            task.execute();
                                        }else{
                                            Toast.makeText(getActivity(),"Network is not available....",Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                })
                                .setNegativeButton("No",new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        // if this button is clicked, just close
                                        // the dialog box and do nothing
                                        dialog.cancel();
                                    }
                                });

                        // create alert dialog
                        AlertDialog alertDialog = alertDialogBuilder.create();

                        // show it
                        alertDialog.show();
                    }
                });

            }
        });
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
        display = getActivity().getWindowManager().getDefaultDisplay();
        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        width = display.getWidth();
        height = display.getHeight();

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
            }else{
                bolbrk=true;
                if(lstResultBreakfast.size()>0){
                    lstViewBreakfast.setVisibility(View.VISIBLE);
                    lstViewLunch.setVisibility(View.GONE);
                    lstViewSnacks.setVisibility(View.GONE);
                    lstViewDinner.setVisibility(View.GONE);
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
            }else{
                bollunch=true;
                if(lstResultLunch.size()>0){
                    lstViewBreakfast.setVisibility(View.GONE);
                    lstViewLunch.setVisibility(View.VISIBLE);
                    lstViewSnacks.setVisibility(View.GONE);
                    lstViewDinner.setVisibility(View.GONE);
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
            }else{
                bolsnaks=true;
                if(lstResultSnacks.size()>0){
                    lstViewBreakfast.setVisibility(View.GONE);
                    lstViewLunch.setVisibility(View.GONE);
                    lstViewSnacks.setVisibility(View.VISIBLE);
                    lstViewDinner.setVisibility(View.GONE);
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
            }else{
                boldiner=true;
                if(lstResultDinner.size()>0){
                    lstViewBreakfast.setVisibility(View.GONE);
                    lstViewLunch.setVisibility(View.GONE);
                    lstViewSnacks.setVisibility(View.GONE);
                    lstViewDinner.setVisibility(View.VISIBLE);
                }
            }
        }
        if(v==back_food_layout){
            getActivity().finish();
        }
        if(v==btn_food_date_picker){
            new DatePickerDialog(getActivity(), d,pYear,
                    pMonth,
                    pDay).show();
        }
        if(v==btn_food_time_picker){
            new TimePickerDialog(getActivity(), t,
                    dateAndTime.get(Calendar.HOUR_OF_DAY),
                    dateAndTime.get(Calendar.MINUTE),
                    true).show();
        }

        if(v==addBreakfast){
            ga.setFoodType("Breakfast");
            Intent addfood = new Intent(getActivity(),AddBreakfast.class);
            User user = getArguments().getParcelable("user");
            addfood.putExtra("user", user);
            startActivity(addfood);
        }
        if(v==addLunch){
            ga.setFoodType("Lunch");
            Intent addfood = new Intent(getActivity(),AddBreakfast.class);
            User user = getArguments().getParcelable("user");
            addfood.putExtra("user", user);
            startActivity(addfood);
        }
        if(v==addSnacks){
            ga.setFoodType("Snacks");
            Intent addfood = new Intent(getActivity(),AddBreakfast.class);
            User user = getArguments().getParcelable("user");
            addfood.putExtra("user", user);
            startActivity(addfood);
        }
        if(v==addDinner){
            ga.setFoodType("Dinner");
            Intent addfood = new Intent(getActivity(),AddBreakfast.class);
            User user = getArguments().getParcelable("user");
            addfood.putExtra("user", user);
            startActivity(addfood);
        }

        if(v==addExercise){
            ga.setFoodType("Exercise");
            Intent addExercise = new Intent(getActivity(), AddExercise.class);
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
            updateDisplay();
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
    // async class for calling webservice and get responce message
    public class CallListTask extends AsyncTask <String, Void,String>
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

            Log.i("onPostExecute", "onPostExecute");
            //dialog1.dismiss();
            Log.e("TAG","lst size : " + lstResultBreakfast.size());

            if(lstResultBreakfast.size()>0){
                lblbrk.setText("Breakfast ("+lstResultBreakfast.get(0).getCount()+")" );
                lbltotalbrkcal.setText(Global_Application.totalcal+"");
                BreakfastAdapter adapter = new BreakfastAdapter(getActivity(),R.layout.breakfast_food_list, lstResultBreakfast);
                lstViewBreakfast.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                lstViewBreakfast.onRefreshComplete();
                if(isInternetOn()){
                    CallLunchListTask task = new CallLunchListTask();
                    task.activity =getActivity();
                    task.execute();
                }else{
                    Toast.makeText(getActivity(),"Network is not available....",Toast.LENGTH_SHORT).show();
                }
            }else{

                if(isInternetOn()){
                    CallLunchListTask task = new CallLunchListTask();
                    task.activity =getActivity();
                    task.execute();
                }else{
                    Toast.makeText(getActivity(),"Network is not available....",Toast.LENGTH_SHORT).show();
                }
            }

        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            Log.i("doInBackground--Object", "doInBackground--Object");
            //ga.lstResult=obj.manageGoal(appPrefs.getGoalname().toString(), type, goalvalue);
            Global_Application.totalcal=0;
            lstResultBreakfast = obj.FoodListing(Global_Application.url+"diet-tracker/?meal_type=BREAKFAST");
            return null;
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
            if(lstResultLunch.size()>0){
                lbllunch.setText("Lunch ("+lstResultLunch.get(0).getCount()+")" );
                lbllunchcal.setText(Global_Application.totalcal+"");
                LunchAdapter adapter = new LunchAdapter(getActivity(),R.layout.lunch_food_list, lstResultLunch);
                lstViewLunch.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                lstViewLunch.onRefreshComplete();
                if(isInternetOn()){
                    CallSnaksListTask task = new CallSnaksListTask();
                    task.activity =getActivity();
                    task.execute();
                }else{
                    Toast.makeText(getActivity(),"Network is not available....",Toast.LENGTH_SHORT).show();
                }
            }else{
                if(isInternetOn()){
                    CallSnaksListTask task = new CallSnaksListTask();
                    task.activity =getActivity();
                    task.execute();
                }else{
                    Toast.makeText(getActivity(),"Network is not available....",Toast.LENGTH_SHORT).show();
                }
            }

        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            Log.i("doInBackground--Object", "doInBackground--Object");
            //ga.lstResult=obj.manageGoal(appPrefs.getGoalname().toString(), type, goalvalue);
            Global_Application.totalcal=0;
            lstResultLunch = obj.FoodListing(Global_Application.url+"diet-tracker/?meal_type=LUNCH");
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

            //dialog = ProgressDialog.show(activity, "Calling", "Please wait...", true);
							/*	dialog1 = new ProgressDialog(getParent());
								dialog1.setMessage("Please Wait....");
								dialog1.show();*/
            Log.i("onPreExecute", "onPreExecute");

        }

        protected void onPostExecute(String result)
        {

            Log.i("onPostExecute", "onPostExecute");
            dialog1.dismiss();
            Log.e("TAG","lst size : " + lstResultSnacks.size());
            if(lstResultSnacks.size()>0){
                lblsnack.setText("Snacks ("+lstResultSnacks.get(0).getCount()+")" );
                lblsnakcal.setText(Global_Application.totalcal+"");
                SnacksAdapter adapter = new SnacksAdapter(getActivity(),R.layout.snacks_food_list, lstResultSnacks);
                lstViewSnacks.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                lstViewSnacks.onRefreshComplete();
                if(isInternetOn()){
                    CallDinnerListTask task = new CallDinnerListTask();
                    task.activity =getActivity();
                    task.execute();
                }else{
                    Toast.makeText(getActivity(),"Network is not available....",Toast.LENGTH_SHORT).show();
                }
            }else{
                if(isInternetOn()){
                    CallDinnerListTask task = new CallDinnerListTask();
                    task.activity =getActivity();
                    task.execute();
                }else{
                    Toast.makeText(getActivity(),"Network is not available....",Toast.LENGTH_SHORT).show();
                }
            }

        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            Log.i("doInBackground--Object", "doInBackground--Object");
            //ga.lstResult=obj.manageGoal(appPrefs.getGoalname().toString(), type, goalvalue);
            Global_Application.totalcal=0;
            lstResultSnacks = obj.FoodListing(Global_Application.url+"diet-tracker/?meal_type=SNACKS");
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
            dialog1.dismiss();
            Log.e("TAG","lst size : " + lstResultDinner.size());
            if(lstResultDinner.size()>0){
                lbldinner.setText("Dinner ("+lstResultDinner.get(0).getCount()+")" );
                lbldinnercal.setText(Global_Application.totalcal+"");
                DinnerAdapter adapter = new DinnerAdapter(activity,R.layout.dinner_food_list, lstResultDinner);
                lstViewDinner.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                lstViewDinner.onRefreshComplete();
            }else{
                dialog1.dismiss();
            }

        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            Log.i("doInBackground--Object", "doInBackground--Object");
            //ga.lstResult=obj.manageGoal(appPrefs.getGoalname().toString(), type, goalvalue);
            Global_Application.totalcal=0;
            lstResultDinner = obj.FoodListing(Global_Application.url+"diet-tracker/?meal_type=DINNER");
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
            dialog1 = new ProgressDialog(activity);
            dialog1.setCanceledOnTouchOutside(false);
            dialog1.setMessage("Please Wait....");
            dialog1.show();
            Log.i("onPreExecute", "onPreExecute");

        }

        protected void onPostExecute(String result)
        {

            Log.i("onPostExecute", "onPostExecute");
            dialog1.dismiss();
            Log.e("TAG","lst size : " + lstResultBreakfast.size());
            if(lstResultBreakfast.size()>0 && frm.equals("b")){
                lblbrk.setText("Breakfast ("+lstResultBreakfast.get(0).getCount()+")");
                lbltotalbrkcal.setText(Global_Application.totalcal+"");
                BreakfastAdapter adapter = new BreakfastAdapter(activity,R.layout.breakfast_food_list, lstResultBreakfast);
                lstViewBreakfast.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                lstViewBreakfast.onRefreshComplete();

            }
            if(lstResultLunch.size()>0 && frm.equals("l")){
                lbllunch.setText("Lunch ("+lstResultLunch.get(0).getCount()+")");
                lbllunchcal.setText(Global_Application.totalcal+"");
                LunchAdapter adapter = new LunchAdapter(activity,R.layout.lunch_food_list, lstResultLunch);
                lstViewLunch.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                lstViewLunch.onRefreshComplete();

            }
            if(lstResultSnacks.size()>0 && frm.equals("l")){
                lblsnack.setText("Breakfast ("+lstResultSnacks.get(0).getCount()+")" );
                lblsnakcal.setText(Global_Application.totalcal+"");
                SnacksAdapter adapter = new SnacksAdapter(activity,R.layout.snacks_food_list, lstResultSnacks);
                lstViewSnacks.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                lstViewSnacks.onRefreshComplete();
            }
            if(lstResultDinner.size()>0 && frm.equals("d")){
                lbldinner.setText("Breakfast ("+lstResultDinner.get(0).getCount()+")" );
                lbldinnercal.setText(Global_Application.totalcal+"");
                DinnerAdapter adapter = new DinnerAdapter(activity,R.layout.dinner_food_list, lstResultDinner);
                lstViewDinner.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                lstViewDinner.onRefreshComplete();
            }

        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            Log.i("doInBackground--Object", "doInBackground--Object");
            //ga.lstResult=obj.manageGoal(appPrefs.getGoalname().toString(), type, goalvalue);
            if(frm.equals("b")){
                lstResultBreakfast.addAll(obj.FoodListing(nexturl));
            }else if(frm.equals("l")){
                lstResultLunch.addAll(obj.FoodListing(nexturl));
            }else if(frm.equals("s")){
                lstResultSnacks.addAll(obj.FoodListing(nexturl));
            }else if(frm.equals("d")){
                lstResultDinner.addAll(obj.FoodListing(nexturl));
            }


            return null;
        }

    }
    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        if(isInternetOn()){
            CallListTask task = new CallListTask();
            task.activity = getActivity();
            task.execute();
        }else{
            Toast.makeText(getActivity(),"Network is not available....",Toast.LENGTH_SHORT).show();
        }
    }

    // async class for calling webservice and get responce message
    public class CallDeleteTask extends AsyncTask <String, Void,String>
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
            return obj.DeleteFood(Global_Application.selectedfoodid);
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

        ConnectivityManager connec = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

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

        return false;
    }



}
