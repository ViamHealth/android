package com.viamhealth.android.activities.fragments;

import android.app.AlertDialog;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.ActionMode;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.viamhealth.android.Global_Application;
import com.viamhealth.android.R;
import com.viamhealth.android.ViamHealthPrefs;
import com.viamhealth.android.activities.AddMedication;
import com.viamhealth.android.activities.AddReminder;
import com.viamhealth.android.activities.AddTest;
import com.viamhealth.android.activities.DeleteMedication;
import com.viamhealth.android.activities.Home;
import com.viamhealth.android.activities.OnSwipeTouchListener;
import com.viamhealth.android.activities.oldones.InviteUser;
import com.viamhealth.android.adapters.MedicalDataAdapter;
import com.viamhealth.android.adapters.MedicalDataAdapter1;
import com.viamhealth.android.adapters.SeparatedListAdapter;
import com.viamhealth.android.adapters.TestDataAdapter;
import com.viamhealth.android.adapters.TestDataAdapter1;
import com.viamhealth.android.dao.rest.endpoints.UserEP;
import com.viamhealth.android.dao.restclient.old.functionClass;
import com.viamhealth.android.model.FileData;
import com.viamhealth.android.model.MedicalData;
import com.viamhealth.android.model.MedicationData;
import com.viamhealth.android.model.ReminderReadings;
import com.viamhealth.android.model.users.User;
import com.viamhealth.android.ui.RefreshableListView;
import com.viamhealth.android.utils.Checker;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Set;

/**
 * Created by naren on 08/10/13.
 */
public class ReminderFragment extends SherlockFragment implements View.OnClickListener {

    private User user;
    private View view;
    private Bundle savedInstanceState;

    Display display;
    int width,height;
    int w15,w20,w5,h40,h20,w10,h10,w150,w30;
    ProgressDialog dialog;
    ProgressDialog dialog1;
    ColorDrawable draw1,draw2;

    private ActionMode actionMode;
    TextView mSelected,lbl_add,lbl_delete,txt_test,txt_medication,txt_reminder,txt_name,txt_time,txt_mode;;
    ImageView back,person_icon;
    Button add_medicine,add_test,add_medicine_reminder,add_test_reminder;
    TextView lbl_invite_user_food,heding_Addfood_name;
    LinearLayout menu_invite_addfood,menu_invite_out_addfood,settiglayout_food,search_layout,watch_below_layout,lst_data;
    RefreshableListView lstReminderMedicine,lstdata;
    //Framelayout lstReminderTest;
    ScrollView test_scrl;
    LinearLayout medicine_scrl,reminder_scrl;
    LinearLayout main_list_edit,main_list_delete,main_list;

    String selected_reminder_name,selected_morning_val,selected_noon_val,selected_night_val;
    int selected_position;

    ViewPager mPager,mPager1;
    MedicalDataAdapter1 adapter,adapter4;

    ArrayList<MedicalData> lstResult = new ArrayList<MedicalData>();
    int selection=0,original_width_edit,original_width_delete;
    String delid;

    Typeface tf;
    ViamHealthPrefs appPrefs;
    String user_id,med_id;
    functionClass obj;
    Global_Application ga;
    String selecteduserid="0";
    private DisplayImageOptions options;
    ArrayList<MedicationData>	allData = new ArrayList<MedicationData>();
    ArrayList<MedicationData>	listData = new ArrayList<MedicationData>();

    ArrayList<ArrayList<ReminderReadings>>	remindercheckData = new ArrayList<ArrayList<ReminderReadings>>();

    ArrayList<ArrayList<MedicationData>> selected_list_data=new ArrayList<ArrayList<MedicationData>>();

    ArrayList<ArrayList<MedicationData>> selected_other_data=new ArrayList<ArrayList<MedicationData>>();


    ArrayList<MedicationData> otherData = new ArrayList<MedicationData>();

    MedicationData med_edit=new MedicationData();
    Intent edit_med=null;
    int edit_pos=0;
    int totalHeight = 0;
    ArrayList<ReminderReadings> rem_read=null;
    int current_pos;
    StoreReminders rem1=null;
    User selected_user;

    ActionBar actionBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        view = inflater.inflate(R.layout.tab_fragment_reminder, container, false);
        this.savedInstanceState = savedInstanceState;
        user=getArguments().getParcelable("user");

        appPrefs = new ViamHealthPrefs(getSherlockActivity());
        obj=new functionClass(getSherlockActivity());
        ga=((Global_Application)getSherlockActivity().getApplicationContext());

        tf = Typeface.createFromAsset(getSherlockActivity().getAssets(),"Roboto-Condensed.ttf");

        reminder_scrl = (LinearLayout)view.findViewById(R.id.reminder_scrl);
        medicine_scrl = (LinearLayout)view.findViewById(R.id.medicine_scrl);

        test_scrl = (ScrollView)view.findViewById(R.id.test_scrl);
        reminder_scrl.setVisibility(View.VISIBLE);


        FrameLayout initialLayout = (FrameLayout) view.findViewById(R.id.initial_layout);
        Button addMedicine = (Button) initialLayout.findViewById(R.id.add_medi_rem);
        Button addOthers = (Button) initialLayout.findViewById(R.id.add_test_rem);

        addMedicine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addMedication = new Intent(getSherlockActivity(),AddMedication.class);
                addMedication.putExtra("user_id",user.getId().toString());
                startActivity(addMedication);
            }
        });

        addOthers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addMedication = new Intent(getSherlockActivity(),AddMedication.class);
                addMedication.putExtra("user_id",user.getId().toString());
                addMedication.putExtra("isOthersReminders",true);
                startActivity(addMedication);
            }
        });

        txt_reminder = (TextView)view.findViewById(R.id.txt_reminder);
        txt_reminder.setOnClickListener(this);
        txt_reminder.setBackgroundResource(R.drawable.tabpressed);

        txt_test = (TextView)view.findViewById(R.id.txt_test);
        txt_test.setOnClickListener(this);

        txt_medication = (TextView)view.findViewById(R.id.txt_medication);
        txt_medication.setOnClickListener(this);

        add_medicine = (Button)view.findViewById(R.id.add_medicine);
        //add_medicine.setTypeface(tf);
        add_medicine.setOnClickListener(this);

        add_test = (Button)view.findViewById(R.id.add_test);
        //add_medicine.setTypeface(tf);
        add_test.setOnClickListener(this);

        add_medicine_reminder = (Button)view.findViewById(R.id.add_medicine_reminder);
        add_medicine_reminder.setVisibility(View.GONE);
        //add_medicine.setTypeface(tf);
        add_medicine_reminder.setOnClickListener(this);

        add_test_reminder = (Button)view.findViewById(R.id.add_test_reminder);
        //add_medicine.setTypeface(tf);
        add_test_reminder.setOnClickListener(this);

        // for list heading
        /*lbl_name = (TextView)view.findViewById(R.id.lbl_name);
        lbl_name.getLayoutParams().width = w150;

        lbl_morning = (TextView)view.findViewById(R.id.lbl_morning);
        // lbl_morning.getLayoutParams().width = w30;

        lbl_noon = (TextView)view.findViewById(R.id.lbl_noon);
        // lbl_noon.getLayoutParams().width = w30;

        lbl_night = (TextView)view.findViewById(R.id.lbl_night);
        //lbl_night.getLayoutParams().width = w30;

        txt1 = (TextView)view.findViewById(R.id.txt1);
        txt1.setPadding(w5, 0, 0, 0);

        txt2 = (TextView)view.findViewById(R.id.txt2);
        txt2.setPadding(w10, 0, 0, 0);*/
        int i=0;

        rem1=new StoreReminders();
        for(i=0;i<5;i++)
        {
            selected_list_data.add(new ArrayList<MedicationData>());
        }



        for(i=0;i<5;i++)
        {
            remindercheckData.add(new ArrayList<ReminderReadings>());
        }


        for(i=0;i<5;i++)
        {
            selected_other_data.add(new ArrayList<MedicationData>());
        }


        mPager1 = (ViewPager)view.findViewById(R.id.pager1);

        mPager1.setOnPageChangeListener(new MyPageChangeListener());


        selected_user = getArguments().getParcelable("user");

        RetrieveMedicalData task=new RetrieveMedicalData();
        task.applicationContext=getSherlockActivity();
        task.execute();

        Bundle args = new Bundle();
        args.putParcelable("user", selected_user);
        FragmentTransaction fm = getSherlockActivity().getSupportFragmentManager().beginTransaction();
        MedicineListFragment fragment = (MedicineListFragment)SherlockFragment.instantiate(getSherlockActivity(), MedicineListFragment.class.getName(), args);
        fm.add(R.id.lstReminderMedicine, fragment, "Medicine-list");

        FragmentTransaction fm_others = getSherlockActivity().getSupportFragmentManager().beginTransaction();
        OthersListFragment fragment1 = (OthersListFragment) SherlockFragment.instantiate(getSherlockActivity(), OthersListFragment.class.getName(), args);
        fm_others.add(R.id.lstRemTest, fragment1, "Others-list");

        fm.commit();
        fm_others.commit();

        setHasOptionsMenu(true);


        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.add(Menu.NONE, R.drawable.ic_content_new, 1, "New")
                .setIcon(R.drawable.ic_content_new)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.drawable.ic_content_new){
            Intent addMedication = new Intent(getSherlockActivity(),AddReminder.class);
            addMedication.putExtra("user_id",user.getId().toString());
            startActivity(addMedication);
            return false;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {

        super.onResume();

        if(main_list_edit!=null)
        {
            main_list_edit.animate().translationX(0).withLayer();
            main_list_edit.setMinimumWidth(original_width_edit);
        }
        if(main_list_delete!=null)
        {
            main_list_delete.animate().translationX(0).withLayer();
            main_list_delete.setMinimumWidth(original_width_delete);
        }
    }

    public class StoreReminders extends AsyncTask <String, Void,String>
    {
        protected Context applicationContext;

        @Override
        protected void onPreExecute()
        {



        }

        protected void onPostExecute(String result)
        {




        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            Log.i("doInBackground--Object", "doInBackground--Object");
            int i=0;
            for(i=0;i<selected_list_data.get(current_pos).size();i++)
            {
                //obj.storeReminderReading(selected_list_data.get(current_pos).get(i).getId(),selected_list_data.get(current_pos).get(i).getMorning_check(),selected_list_data.get(current_pos).get(i).getNoon_check(),selected_list_data.get(current_pos).get(i).getNoon_check(),selected_list_data.get(current_pos).get(i).getNoon_check(),selected_list_data.get(current_pos).get(i).getNoon_check(),user.getId().toString());
            }

            return null;
        }

    }


    public class RetrieveMedicalData extends AsyncTask <String, Void,String>
    {
        protected Context applicationContext;

        @Override
        protected void onPreExecute()
        {

            //dialog = ProgressDialog.show(applicationContext, "Calling", "Please wait...", true);
            // dialog1 = new ProgressDialog(getSherlockActivity());
            //dialog1.setMessage("Please Wait....");
            //dialog1.show();
            //Log.i("onPreExecute", "onPreExecute");

        }

        protected void onPostExecute(String result)
        {

            Log.i("onPostExecute", "onPostExecute");
            ArrayList<String> lst = new ArrayList<String>();
            int i=0,j=0,k=0;
            listData.clear();
            otherData.clear();
            for (i=0;i<allData.size();i++)
            {
                if((allData.get(i).getType().equalsIgnoreCase("2")))
                {
                    listData.add(j,allData.get(i));
                    j++;
                }
                else
                {
                    otherData.add(k,allData.get(i));
                    k++;
                }
            }
            ga.listData=listData;
            ga.otherData=otherData;

            //mPager1.setAdapter(new ImagePagerAdapter(lst));

            /*
            RefreshableListView lstReminderMedicine=(RefreshableListView)view.findViewById(R.id.lstReminderMedicine);
            adapter4 = new MedicalDataAdapter1(getSherlockActivity(),R.layout.row_medical_list1, listData);
            lstReminderMedicine.setAdapter(adapter4);

            lstReminderMedicine.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                    if (actionMode != null) {
                        // if already in action mode - do nothing
                        return false;
                    }

                    final TextView name=(TextView)arg1.findViewById(R.id.txt_name);
                    final TextView txt_morn=(TextView)arg1.findViewById(R.id.txt_morning);
                    final TextView txt_noon=(TextView)arg1.findViewById(R.id.txt_noon);
                    final TextView txt_night=(TextView)arg1.findViewById(R.id.txt_night);

                    selected_reminder_name=name.getText().toString();
                    selected_morning_val=txt_morn.getText().toString();
                    selected_noon_val=txt_noon.getText().toString();
                    selected_night_val=txt_night.getText().toString();
                    selected_position=arg2;

                    // set checked selected item and enter multi selection mode

                    adapter4.setChecked(arg2, true);
                    getSherlockActivity().startActionMode(new ActionModeCallbackMedicine());
                    actionMode.invalidate();
                    return true;
                }
            });


            int total_height_medicine_tab=0,len=0;

            for (i = 0, len = adapter4.getCount(); i < len; i++) {
                View listItem = adapter4.getView(i, null, lstReminderMedicine);
                listItem.measure(0, 0);
                int list_child_item_height = listItem.getMeasuredHeight()+lstReminderMedicine.getDividerHeight();//item height
                total_height_medicine_tab += list_child_item_height; //
            }

            LinearLayout.LayoutParams l3= new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,total_height_medicine_tab+40);
            lstReminderMedicine.setLayoutParams(l3);
            total_height_medicine_tab=0;
            */


            if(listData.size()==0 && otherData.size()==0){
                getActivity().findViewById(R.id.initial_layout).setVisibility(View.VISIBLE);
                getActivity().findViewById(R.id.main_layout).setVisibility(View.GONE);
            }else{
                getActivity().findViewById(R.id.initial_layout).setVisibility(View.GONE);
                getActivity().findViewById(R.id.main_layout).setVisibility(View.VISIBLE);
            }

            RetrieveOtherData task1= new RetrieveOtherData();
            task1.applicationContext=getSherlockActivity();
            task1.execute();

        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            Log.i("doInBackground--Object", "doInBackground--Object");
            //ga.lstResult=obj.manageGoal(appPrefs.getGoalname().toString(), type, goalvalue);
            allData = obj.getReminderInfo(user.getId().toString(), "2");
            return null;
        }

    }

    public class RetrieveOtherData extends AsyncTask <String, Void,String>
    {
        protected Context applicationContext;

        @Override
        protected void onPreExecute()
        {

            //dialog = ProgressDialog.show(applicationContext, "Calling", "Please wait...", true);
            // dialog1 = new ProgressDialog(getSherlockActivity());
            //dialog1.setMessage("Please Wait....");
            //dialog1.show();
            //Log.i("onPreExecute", "onPreExecute");

        }

        protected void onPostExecute(String result)
        {

            Log.i("onPostExecute", "onPostExecute");
            ArrayList<String> lst = new ArrayList<String>();
            Date now = new Date();
            Calendar cal = Calendar.getInstance();
            cal.setTime(now);
            cal.add(Calendar.DAY_OF_YEAR,1);
            SimpleDateFormat fmt= new SimpleDateFormat("yyyy-MM-dd");
            lst.add(fmt.format(cal.getTime()));
            cal.add(Calendar.DAY_OF_YEAR,1);
            lst.add(fmt.format(cal.getTime()));
            cal.add(Calendar.DAY_OF_YEAR,1);
            lst.add(fmt.format(cal.getTime()));
            cal.add(Calendar.DAY_OF_YEAR,1);
            lst.add(fmt.format(cal.getTime()));
            cal.add(Calendar.DAY_OF_YEAR,1);
            lst.add(fmt.format(cal.getTime()));

            if(listData.size()==0 && otherData.size()==0){
                getActivity().findViewById(R.id.initial_layout).setVisibility(View.VISIBLE);
                getActivity().findViewById(R.id.main_layout).setVisibility(View.GONE);
            }else{
                getActivity().findViewById(R.id.initial_layout).setVisibility(View.GONE);
                getActivity().findViewById(R.id.main_layout).setVisibility(View.VISIBLE);
            }


/*
            RefreshableListView lstReminderTest=(RefreshableListView)view.findViewById(R.id.lstRemTest);
            TestDataAdapter1 adapter = new TestDataAdapter1(getSherlockActivity(),R.layout.row_medical_list1, otherData);
            lstReminderTest.setAdapter(adapter);

            int total_height_test_tab=0,i,len;

            for (i = 0, len = adapter.getCount(); i < len; i++) {
                View listItem = adapter.getView(i, null, lstReminderTest);
                listItem.measure(0, 0);
                int list_child_item_height = listItem.getMeasuredHeight()+lstReminderTest.getDividerHeight();//item height
                total_height_test_tab += list_child_item_height; //
            }

            LinearLayout.LayoutParams l4= new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,total_height_test_tab+40);
            lstReminderTest.setLayoutParams(l4);
            total_height_test_tab=0;
*/
            mPager1.setAdapter(new ImagePagerAdapter(lst));


        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            Log.i("doInBackground--Object", "doInBackground--Object");
            //ga.lstResult=obj.manageGoal(appPrefs.getGoalname().toString(), type, goalvalue);
            otherData = obj.getReminderInfo(user.getId().toString(),"1");
            return null;
        }

    }


    public void ScreenDimension()
    {
        display = getSherlockActivity().getWindowManager().getDefaultDisplay();
        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        width = display.getWidth();
        height = display.getHeight();
        appPrefs.setSwidth(String.valueOf(width));
        appPrefs.setSheight(String.valueOf(height));
    }




    public final class ActionModeCallbackTest implements ActionMode.Callback
    {

        // " selected" string resource to update ActionBar text
        private String selected = getActivity().getString(R.string.selected);

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu)
        {
            adapter.enterMultiMode();
            // save global action mode
            actionMode = mode;
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu)
        {
            // remove previous items
            menu.clear();
            final int checked = adapter.getCheckedItemCount();
            // update title with number of checked items
            mode.setTitle(checked + " " +this.selected);
            switch (checked) {
                case 0:
                    // if nothing checked - exit action mode
                    mode.finish();
                    return true;
                case 1:
                    // all items - rename + delete
                    getSherlockActivity().getSupportMenuInflater().inflate(
                            R.menu.action_menu_medicine, menu);
                    return true;
                default:
                    getSherlockActivity().getSupportMenuInflater().inflate(
                            R.menu.action_menu_medicine, menu);
                    return true;
            }
        }

        private void download(String strUrl, String fileName, String extension){

        }




        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {

            switch (item.getItemId()) {
                case R.id.action_mode_edit:
                    if(adapter.getCheckedItemCount()>0){


                    }else{
                        Toast.makeText(getSherlockActivity(), "Please select atlest one file..", Toast.LENGTH_SHORT).show();
                    }

                    Toast.makeText(getActivity(), "Download", Toast.LENGTH_LONG).show();
                    return true;

                case R.id.action_mode_delete:
                    if(adapter.getCheckedItemCount()>0){


                    }else{
                        Toast.makeText(getSherlockActivity(), "Please select atlest one file..", Toast.LENGTH_SHORT).show();
                    }

                    Toast.makeText(getActivity(), "Delete", Toast.LENGTH_LONG).show();
                    return true;


                default:
                    return false;
            }
        }

        @Override
        public void onDestroyActionMode(ActionMode mode)
        {
            adapter.exitMultiMode();
            // don't forget to remove it, because we are assuming that if it's not null we are in ActionMode
            actionMode = null;
        }

    }









    public final class ActionModeCallbackMedicine implements ActionMode.Callback
    {

        // " selected" string resource to update ActionBar text
        private String selected = getActivity().getString(R.string.selected);

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu)
        {
            adapter.enterMultiMode();
            // save global action mode
            actionMode = mode;
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu)
        {
            // remove previous items
            menu.clear();
            final int checked = adapter.getCheckedItemCount();
            // update title with number of checked items
            mode.setTitle(checked + " " +this.selected);
            switch (checked) {
                case 0:
                    // if nothing checked - exit action mode
                    mode.finish();
                    return true;
                case 1:
                    // all items - rename + delete
                    getSherlockActivity().getSupportMenuInflater().inflate(
                            R.menu.action_menu_medicine, menu);
                    return true;
                default:
                    getSherlockActivity().getSupportMenuInflater().inflate(
                            R.menu.action_menu_medicine, menu);
                    return true;
            }
        }

        private void download(String strUrl, String fileName, String extension){

        }




        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {

            switch (item.getItemId()) {
                case R.id.action_mode_edit:
                    if(adapter.getCheckedItemCount()>0){

                        edit_med=new Intent(getSherlockActivity(),AddMedication.class);
                        edit_pos=selected_position;
                        edit_med.putExtra("iseditMed",true);
                        edit_med.putExtra("user_id",user.getId().toString());
                        med_id=listData.get(selected_position).getId();
                        edit_med.putExtra("id",med_id);
                        edit_med.putExtra("start_date",listData.get(selected_position).getStart_date());
                        edit_med.putExtra("name",selected_reminder_name);
                        edit_med.putExtra("morning",selected_morning_val);
                        edit_med.putExtra("noon",selected_noon_val);
                        edit_med.putExtra("night",selected_night_val);
                        startActivity(edit_med);

                    }else{
                        Toast.makeText(getSherlockActivity(), "Please select atlest one file..", Toast.LENGTH_SHORT).show();
                    }

                    Toast.makeText(getActivity(), "Download", Toast.LENGTH_LONG).show();
                    return true;

                case R.id.action_mode_delete:
                    if(adapter.getCheckedItemCount()>0){


                    }else{
                        Toast.makeText(getSherlockActivity(), "Please select atlest one file..", Toast.LENGTH_SHORT).show();
                    }

                    Toast.makeText(getActivity(), "Delete", Toast.LENGTH_LONG).show();
                    return true;


                default:
                    return false;
            }
        }

        @Override
        public void onDestroyActionMode(ActionMode mode)
        {
            adapter.exitMultiMode();
            // don't forget to remove it, because we are assuming that if it's not null we are in ActionMode
            actionMode = null;
        }

    }






    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        if(v==txt_medication){
            lstResult.clear();
            selection=1;
            txt_test.setBackgroundResource(R.drawable.tabnormal);
            txt_medication.setBackgroundResource(R.drawable.tabpressed);
            txt_reminder.setBackgroundResource(R.drawable.tabnormal);
            reminder_scrl.setVisibility(View.GONE);
            medicine_scrl.setVisibility(View.VISIBLE);
            test_scrl.setVisibility(View.GONE);



/*
            RefreshableListView lstReminderTest=(RefreshableListView)view.findViewById(R.id.lstRemTest);
            TestDataAdapter1 adapter5 = new TestDataAdapter1(getSherlockActivity(),R.layout.row_medical_list1, otherData);
            lstReminderTest.setAdapter(adapter5);

            int total_height_test_tab=0,i,len;

            for (i = 0, len = adapter5.getCount(); i < len; i++) {
                View listItem = adapter5.getView(i, null, lstReminderTest);
                listItem.measure(0, 0);
                int list_child_item_height = listItem.getMeasuredHeight()+lstReminderTest.getDividerHeight();//item height
                total_height_test_tab += list_child_item_height; //
            }

            LinearLayout.LayoutParams l4= new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,total_height_test_tab+40);
            lstReminderTest.setLayoutParams(l4);
            total_height_test_tab=0;

            lstReminderTest.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                    if (actionMode != null) {
                        // if already in action mode - do nothing
                        return false;
                    }
                    // set checked selected item and enter multi selection mode
                    adapter.setChecked(arg2, true);
                    getSherlockActivity().startActionMode(new ActionModeCallbackTest());
                    actionMode.invalidate();
                    return true;
                }
            });

*/

            /* //MJ:comment
            RefreshableListView lstReminderMedicine=(RefreshableListView)view.findViewById(R.id.lstReminderMedicine);

            adapter = new MedicalDataAdapter1(getSherlockActivity(),R.layout.row_medical_list1, listData);
            lstReminderMedicine.setAdapter(adapter);
            lstReminderMedicine.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                    if (actionMode != null) {
                        // if already in action mode - do nothing
                        return false;
                    }

                    final TextView name=(TextView)arg1.findViewById(R.id.txt_name);
                    final TextView txt_morn=(TextView)arg1.findViewById(R.id.txt_morning);
                    final TextView txt_noon=(TextView)arg1.findViewById(R.id.txt_noon);
                    final TextView txt_night=(TextView)arg1.findViewById(R.id.txt_night);

                    selected_reminder_name=name.getText().toString();
                    selected_morning_val=txt_morn.getText().toString();
                    selected_noon_val=txt_noon.getText().toString();
                    selected_night_val=txt_night.getText().toString();
                    selected_position=arg2;

                    // set checked selected item and enter multi selection mode

                    adapter.setChecked(arg2, true);
                    getSherlockActivity().startActionMode(new ActionModeCallbackMedicine());
                    actionMode.invalidate();
                    return true;
                }
            });

            int total_height_medicine_tab=0;

            for (i = 0, len = adapter.getCount(); i < len; i++) {
                View listItem = adapter.getView(i, null, lstReminderMedicine);
                listItem.measure(0, 0);
                int list_child_item_height = listItem.getMeasuredHeight()+lstReminderMedicine.getDividerHeight();//item height
                total_height_medicine_tab += list_child_item_height; //
            }

            LinearLayout.LayoutParams l3= new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,total_height_medicine_tab+40);
            lstReminderMedicine.setLayoutParams(l3);
            total_height_medicine_tab=0;


            lstReminderMedicine.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                    if (actionMode != null) {
                        // if already in action mode - do nothing
                        return false;
                    }

                    final TextView name=(TextView)arg1.findViewById(R.id.txt_name);
                    final TextView txt_morn=(TextView)arg1.findViewById(R.id.txt_morning);
                    final TextView txt_noon=(TextView)arg1.findViewById(R.id.txt_noon);
                    final TextView txt_night=(TextView)arg1.findViewById(R.id.txt_night);

                    selected_reminder_name=name.getText().toString();
                    selected_morning_val=txt_morn.getText().toString();
                    selected_noon_val=txt_noon.getText().toString();
                    selected_night_val=txt_night.getText().toString();
                    selected_position=arg2;

                    // set checked selected item and enter multi selection mode

                    adapter4.setChecked(arg2, true);
                    getSherlockActivity().startActionMode(new ActionModeCallbackMedicine());
                    actionMode.invalidate();
                    return true;
                }
            });

            */

/*
            lstReminderMedicine.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                @Override
                public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                    main_list_edit=(LinearLayout)v.findViewById(R.id.main_list_edit);
                    main_list_delete=(LinearLayout)v.findViewById(R.id.main_list_delete);
                    main_list=(LinearLayout)v.findViewById(R.id.main_list);

                    final int pos=position;

                    final TextView name=(TextView)v.findViewById(R.id.txt_name);
                    final TextView txt_morn=(TextView)v.findViewById(R.id.txt_morning);
                    final TextView txt_noon=(TextView)v.findViewById(R.id.txt_noon);
                    final TextView txt_night=(TextView)v.findViewById(R.id.txt_night);

                    main_list_edit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //main_list_edit.animate().translationX((main_list.getWidth())/2).withLayer();
                            //original_width_edit=main_list_edit.getWidth();
                            //main_list_edit.setMinimumWidth((main_list.getWidth())/3);
                            //main_list_edit.setMinimumHeight(main_list.getHeight());
                            edit_med=new Intent(getSherlockActivity(),AddMedication.class);
                            edit_pos=pos;
                            edit_med.putExtra("iseditMed",true);
                            edit_med.putExtra("user_id",user.getId().toString());
                            med_id=listData.get(pos).getId();
                            edit_med.putExtra("id",med_id);
                            edit_med.putExtra("start_date",listData.get(pos).getStart_date());
                            edit_med.putExtra("name",name.getText().toString());
                            edit_med.putExtra("morning",txt_morn.getText().toString());
                            edit_med.putExtra("noon",txt_noon.getText().toString());
                            edit_med.putExtra("night",txt_night.getText().toString());
                            startActivity(edit_med);
                        }
                    });



                    main_list_delete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //main_list_delete.animate().translationX(-(main_list.getWidth())/2).withLayer();
                            //main_list_delete.setMinimumWidth((main_list.getWidth())/3);
                            Intent edit_med1=new Intent(getSherlockActivity(),DeleteMedication.class);
                            edit_med1.putExtra("user_id",user.getId().toString());
                            edit_med1.putExtra("id",listData.get(pos).getId());
                            startActivity(edit_med1);
                        }
                    });


         main_list_edit.setLayoutAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {
                            Toast.makeText(getSherlockActivity(), "Start anim", Toast.LENGTH_LONG).show();
                        }


                        @Override
                        public void onAnimationEnd(Animation animation) {
                            Toast.makeText(getSherlockActivity(),"end anim",Toast.LENGTH_LONG).show();
                        }


                        @Override
                        public void onAnimationRepeat(Animation animation) {
                            Toast.makeText(getSherlockActivity(),"repeat anim",Toast.LENGTH_LONG).show();
                        }
                    });

                }
            }); */

        }
        if(v==add_test_reminder){
            Intent AddTest = new Intent(getSherlockActivity(), com.viamhealth.android.activities.AddTest.class);
            startActivity(AddTest);
        }
        if(v==txt_test){
            selection = 0;
            txt_test.setBackgroundResource(R.drawable.tabpressed);
            txt_medication.setBackgroundResource(R.drawable.tabnormal);
            txt_reminder.setBackgroundResource(R.drawable.tabnormal);
            reminder_scrl.setVisibility(View.GONE);
            medicine_scrl.setVisibility(View.GONE);
            test_scrl.setVisibility(View.VISIBLE);
            int total_height_medicine_tab=0,i,len;

            /* //MJ:comment
            RefreshableListView lstReminderMedicine=(RefreshableListView)view.findViewById(R.id.lstReminderMedicine);
            adapter4 = new MedicalDataAdapter1(getSherlockActivity(),R.layout.row_medical_list1, listData);
            lstReminderMedicine.setAdapter(adapter4);



            for (i = 0, len = adapter4.getCount(); i < len; i++) {
                View listItem = adapter4.getView(i, null, lstReminderMedicine);
                listItem.measure(0, 0);
                int list_child_item_height = listItem.getMeasuredHeight()+lstReminderMedicine.getDividerHeight();//item height
                total_height_medicine_tab += list_child_item_height; //
            }

            LinearLayout.LayoutParams l3= new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,total_height_medicine_tab+40);
            lstReminderMedicine.setLayoutParams(l3);
            total_height_medicine_tab=0;

            lstReminderMedicine.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                    if (actionMode != null) {
                        // if already in action mode - do nothing
                        return false;
                    }

                    final TextView name=(TextView)arg1.findViewById(R.id.txt_name);
                    final TextView txt_morn=(TextView)arg1.findViewById(R.id.txt_morning);
                    final TextView txt_noon=(TextView)arg1.findViewById(R.id.txt_noon);
                    final TextView txt_night=(TextView)arg1.findViewById(R.id.txt_night);

                    selected_reminder_name=name.getText().toString();
                    selected_morning_val=txt_morn.getText().toString();
                    selected_noon_val=txt_noon.getText().toString();
                    selected_night_val=txt_night.getText().toString();
                    selected_position=arg2;

                    // set checked selected item and enter multi selection mode

                    adapter4.setChecked(arg2, true);
                    getSherlockActivity().startActionMode(new ActionModeCallbackMedicine());
                    actionMode.invalidate();
                    return true;
                }
            });


          */


/*
            RefreshableListView lstReminderTest=(RefreshableListView)view.findViewById(R.id.lstRemTest);
            TestDataAdapter1 adapter = new TestDataAdapter1(getSherlockActivity(),R.layout.row_medical_list1, otherData);
            lstReminderTest.setAdapter(adapter);

            int total_height_test_tab=0;

            for (i = 0, len = adapter.getCount(); i < len; i++) {
                View listItem = adapter.getView(i, null, lstReminderTest);
                listItem.measure(0, 0);
                int list_child_item_height = listItem.getMeasuredHeight()+lstReminderTest.getDividerHeight();//item height
                total_height_test_tab += list_child_item_height; //
            }

            LinearLayout.LayoutParams l4= new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,total_height_test_tab+40);
            lstReminderTest.setLayoutParams(l4);
            total_height_test_tab=0;

            lstReminderTest.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                @Override
                public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                    main_list_edit=(LinearLayout)v.findViewById(R.id.main_list_edit);
                    main_list_delete=(LinearLayout)v.findViewById(R.id.main_list_delete);
                    main_list=(LinearLayout)v.findViewById(R.id.main_list);

                    final int pos=position;

                    final TextView name=(TextView)v.findViewById(R.id.txt_name);
                    final TextView txt_morn=(TextView)v.findViewById(R.id.txt_morning);
                    final TextView txt_noon=(TextView)v.findViewById(R.id.txt_noon);
                    final TextView txt_night=(TextView)v.findViewById(R.id.txt_night);

                    main_list_edit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //main_list_edit.animate().translationX((main_list.getWidth())/2).withLayer();
                            //original_width_edit=main_list_edit.getWidth();
                           // main_list_edit.setMinimumWidth((main_list.getWidth())/3);
                            //main_list_edit.setMinimumHeight(main_list.getHeight());
                            edit_med=new Intent(getSherlockActivity(),AddMedication.class);
                            edit_pos=pos;
                            edit_med.putExtra("iseditOthers",true);
                            edit_med.putExtra("user_id",user.getId().toString());
                            med_id=otherData.get(pos).getId();
                            edit_med.putExtra("id",med_id);
                            edit_med.putExtra("start_date",otherData.get(pos).getStart_date());
                            edit_med.putExtra("name",name.getText().toString());
                            startActivity(edit_med);
                        }
                    });



                    main_list_delete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                           // main_list_delete.animate().translationX(-(main_list.getWidth())/2).withLayer();
                           // main_list_delete.setMinimumWidth((main_list.getWidth())/3);
                            Intent edit_med1=new Intent(getSherlockActivity(),DeleteMedication.class);
                            edit_med1.putExtra("user_id",user.getId().toString());
                            edit_med1.putExtra("id",otherData.get(pos).getId());
                            startActivity(edit_med1);
                        }
                    });


                    main_list_edit.setLayoutAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {
                            Toast.makeText(getSherlockActivity(), "Start anim", Toast.LENGTH_LONG).show();
                        }


                        @Override
                        public void onAnimationEnd(Animation animation) {
                            Toast.makeText(getSherlockActivity(),"end anim",Toast.LENGTH_LONG).show();
                        }


                        @Override
                        public void onAnimationRepeat(Animation animation) {
                            Toast.makeText(getSherlockActivity(),"repeat anim",Toast.LENGTH_LONG).show();
                        }
                    });

                }
            });

*/
        }
        if(v==txt_reminder){
            txt_test.setBackgroundResource(R.drawable.tabnormal);
            txt_medication.setBackgroundResource(R.drawable.tabnormal);
            txt_reminder.setBackgroundResource(R.drawable.tabpressed);
            reminder_scrl.setVisibility(View.VISIBLE);
            medicine_scrl.setVisibility(View.GONE);
            test_scrl.setVisibility(View.GONE);

        }
        if(v==add_medicine){
            Intent AddMedication = new Intent(getSherlockActivity(),AddMedication.class);
            AddMedication.putExtra("user_id",user.getId().toString());
            AddMedication.putExtra("isMedReminders",true);
            startActivity(AddMedication);
        }
        if(v==add_test){
            Intent AddMedication = new Intent(getSherlockActivity(),AddMedication.class);
            AddMedication.putExtra("user_id",user.getId().toString());
            AddMedication.putExtra("isOthersReminders",true);
            startActivity(AddMedication);
        }
    }

    public class MyPageChangeListener extends ViewPager.SimpleOnPageChangeListener {

        @Override
        public void onPageSelected(int position)
        {
            //Log.e("TAG","position is : " + position);
            //Toast.makeText(getSherlockActivity(),"selected postion is " + position, Toast.LENGTH_SHORT).show();

        }
    }

    private class ImagePagerAdapter extends PagerAdapter {
        ArrayList<String> lstData = new ArrayList<String>();
        private LayoutInflater inflater;

        ImagePagerAdapter(ArrayList<String> lstData) {
            this.lstData = lstData;
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
            return lstData.size();
        }

        @Override
        synchronized public Object instantiateItem(View localView, final int position)
        {
            View imageLayout = inflater.inflate(R.layout.item_pager_medicine, null);
            TextView todayDate=(TextView)imageLayout.findViewById(R.id.todayDate);

            current_pos=position;

            RefreshableListView lstReminderMedicine1=(RefreshableListView)imageLayout.findViewById(R.id.lstReminderMedicine1);

            todayDate.setText(lstData.get(position));
            String start_date,end_date,current_date;
            Date d1=null,d2=null,d3=null,d4=null,d5=null,d6=null;


            int i=0;

            selected_list_data.get(position).clear();
            selected_other_data.get(position).clear();

            for(i=0;i< listData.size();i++)
            {
                start_date=listData.get(i).getStart_date();
                end_date=listData.get(i).getEnd_date();
                SimpleDateFormat fmt=new SimpleDateFormat("yyyy-MM-dd");

                try {
                    d1=fmt.parse(start_date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                try {
                    d2=fmt.parse(end_date);
                } catch (ParseException e) {
                    e.printStackTrace();
                    d2=null;
                }

                try {
                    d3=fmt.parse(lstData.get(position));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                if(d3.compareTo(d1)>=0)
                {
                    if((d2!=null && d3.compareTo(d2)<=0) || d2==null)
                    {
                        selected_list_data.get(position).add(listData.get(i));
                    }
                }
            }




            for(i=0;i< otherData.size();i++)
            {
                start_date=otherData.get(i).getStart_date();
                end_date=otherData.get(i).getEnd_date();
                SimpleDateFormat fmt=new SimpleDateFormat("yyyy-MM-dd");

                try {
                    d4=fmt.parse(start_date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                try {
                    d5=fmt.parse(end_date);
                } catch (ParseException e) {
                    e.printStackTrace();
                    d5=null;
                }

                try {
                    d6=fmt.parse(lstData.get(position));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                if(d6.compareTo(d4)>=0)
                {
                    if((d5!=null && d6.compareTo(d5)<=0) || d5==null)
                    {
                        selected_other_data.get(position).add(otherData.get(i));
                    }
                }
            }


            MedicalDataAdapter adapter = new MedicalDataAdapter(getSherlockActivity(),R.layout.row_medical_list, selected_list_data.get(position));

            int total_height_medicine_tab=0,len=0;

/*
            RefreshableListView lstReminderMedicine=(RefreshableListView)view.findViewById(R.id.lstReminderMedicine);

            adapter4 = new MedicalDataAdapter1(getSherlockActivity(),R.layout.row_medical_list1,listData);
            lstReminderMedicine.setAdapter(adapter4);



            for (i = 0, len = adapter4.getCount(); i < len; i++) {
                View listItem = adapter4.getView(i, null, lstReminderMedicine1);
                listItem.measure(0, 0);
                int list_child_item_height = listItem.getMeasuredHeight()+lstReminderMedicine1.getDividerHeight();//item height
                total_height_medicine_tab += list_child_item_height; //
            }

            LinearLayout.LayoutParams l3= new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,total_height_medicine_tab+40);
            lstReminderMedicine.setLayoutParams(l3);
            total_height_medicine_tab=0;

            lstReminderMedicine.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                    if (actionMode != null) {
                        // if already in action mode - do nothing
                        return false;
                    }

                    final TextView name=(TextView)arg1.findViewById(R.id.txt_name);
                    final TextView txt_morn=(TextView)arg1.findViewById(R.id.txt_morning);
                    final TextView txt_noon=(TextView)arg1.findViewById(R.id.txt_noon);
                    final TextView txt_night=(TextView)arg1.findViewById(R.id.txt_night);

                    selected_reminder_name=name.getText().toString();
                    selected_morning_val=txt_morn.getText().toString();
                    selected_noon_val=txt_noon.getText().toString();
                    selected_night_val=txt_night.getText().toString();
                    selected_position=arg2;

                    // set checked selected item and enter multi selection mode

                    adapter4.setChecked(arg2, true);
                    getSherlockActivity().startActionMode(new ActionModeCallbackMedicine());
                    actionMode.invalidate();
                    return true;
                }
            });

*/
/*
            RefreshableListView lstReminderTest=(RefreshableListView)view.findViewById(R.id.lstRemTest);
            TestDataAdapter1 adapter5 = new TestDataAdapter1(getSherlockActivity(),R.layout.row_medical_list1, otherData);
            lstReminderTest.setAdapter(adapter5);

            int total_height_test_tab=0;

            for (i = 0, len = adapter5.getCount(); i < len; i++) {
                View listItem = adapter5.getView(i, null, lstReminderTest);
                listItem.measure(0, 0);
                int list_child_item_height = listItem.getMeasuredHeight()+lstReminderMedicine1.getDividerHeight();//item height
                total_height_test_tab += list_child_item_height; //
            }


            LinearLayout.LayoutParams l4= new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,total_height_test_tab+40);
            lstReminderTest.setLayoutParams(l4);

*/
            TestDataAdapter adapter1 = new TestDataAdapter(getSherlockActivity(),R.layout.row_test_list, selected_other_data.get(position));

            SeparatedListAdapter adapter3 = new SeparatedListAdapter(getSherlockActivity());
            adapter3.addSection("Medication",adapter);
            adapter3.addSection("Rest All",adapter1);

            ViewPager v1=(ViewPager)localView;

            ((ViewPager) localView).setOffscreenPageLimit(3);
            v1.setPageMargin(60);

            ((ViewPager) localView).addView(imageLayout, 0);

            lstReminderMedicine1.setAdapter(adapter3);
            int totalheight1=totalHeight;
            totalHeight=0;
            for (i = 0, len = adapter3.getCount(); i < len; i++) {
                View listItem = adapter3.getView(i, null, lstReminderMedicine1);
                listItem.measure(0, 0);
                int list_child_item_height = listItem.getMeasuredHeight()+lstReminderMedicine1.getDividerHeight();//item height
                totalHeight += list_child_item_height; //
            }

            if(totalheight1>totalHeight)
            {
                totalHeight=totalheight1;
            }

          //  if(rem1.getStatus()==AsyncTask.Status.PENDING)
           // {
             //   rem1.execute();
           // }

          //  if(rem1.getStatus() == AsyncTask.Status.FINISHED)
          //  {
            //   rem1=null;
              // rem1=new StoreReminders();
            //   rem1.execute();
         //   }

            LinearLayout.LayoutParams l2= new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,totalHeight+70);
            mPager1.setLayoutParams(l2);

            return imageLayout;
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


    }

}
