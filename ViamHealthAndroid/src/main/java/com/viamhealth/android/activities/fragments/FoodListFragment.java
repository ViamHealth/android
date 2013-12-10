package com.viamhealth.android.activities.fragments;

import android.app.AlertDialog;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockListFragment;
import com.actionbarsherlock.view.ActionMode;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.viamhealth.android.Global_Application;
import com.viamhealth.android.R;
import com.viamhealth.android.ViamHealthPrefs;
import com.viamhealth.android.activities.Downlaod;
import com.viamhealth.android.adapters.FileDataAdapter;
import com.viamhealth.android.adapters.MedicalDataAdapter1;
import com.viamhealth.android.adapters.MultiSelectionAdapter;
import com.viamhealth.android.dao.restclient.old.functionClass;
import com.viamhealth.android.model.FileData;
import com.viamhealth.android.model.MedicationData;
import com.viamhealth.android.model.enums.MedicalConditions;
import com.viamhealth.android.model.users.User;
import com.viamhealth.android.ui.RefreshableListView;
import com.viamhealth.android.ui.helper.FileLoader;
import com.viamhealth.android.utils.Checker;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * Created by naren on 27/10/13.
 */
public class FoodListFragment extends BaseListFragment
{

    private MultiSelectionAdapter adapter;
    private ListView list;
    ArrayList<MedicationData>	listData = new ArrayList<MedicationData>();
    ArrayList<MedicationData>	allData = new ArrayList<MedicationData>();

    private final List<FileData> files = new ArrayList<FileData>();
    private functionClass obj;
    private Global_Application ga;
    Intent edit_med=null;
    int edit_pos=0;
    String med_id;

    // if ActoinMode is null - assume we are in normal mode
    private ActionMode actionMode;
    private ViamHealthPrefs appPrefs;

    private User user;
    ArrayList<MedicationData> otherData = new ArrayList<MedicationData>();

    String selected_reminder_name,selected_morning_val,selected_noon_val,selected_night_val;
    int selected_position;
    private static final int LIBRARY_FILE_VIEW = 1000;
    MedicalDataAdapter1 adapter4;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.fragment_sherlock_list, null);

        user = getArguments().getParcelable("user");

        obj=new functionClass(getSherlockActivity());
        ga=((Global_Application)getSherlockActivity().getApplicationContext());
        appPrefs = new ViamHealthPrefs(getActivity());

        this.list = (ListView) v.findViewById(android.R.id.list);

        if(Checker.isInternetOn(getSherlockActivity())){
            RetrieveMedicalData task = new RetrieveMedicalData();
            //task.activity = getSherlockActivity();
            task.execute();
        }else{
            Toast.makeText(getSherlockActivity(),"Network is not available....",Toast.LENGTH_SHORT).show();
        }

        return v;
    }

    @Override
    public void onPause()
    {
        super.onPause();
        if (this.actionMode != null) {
            this.actionMode.finish();
        }
    }



    @Override
    public void onResume() {
        super.onResume();
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

//                        edit_med=new Intent(getSherlockActivity(),AddMedication.class);
//                        edit_pos=selected_position;
//                        edit_med.putExtra("iseditMed",true);
//                        edit_med.putExtra("user_id",user.getId().toString());
//                        med_id=listData.get(selected_position).getId();
//                        edit_med.putExtra("id",med_id);
//                        edit_med.putExtra("start_date",listData.get(selected_position).getStart_date());
//                        edit_med.putExtra("name",selected_reminder_name);
//                        edit_med.putExtra("morning",selected_morning_val);
//                        edit_med.putExtra("noon",selected_noon_val);
//                        edit_med.putExtra("night",selected_night_val);
//                        startActivity(edit_med);

                    }else{
                        Toast.makeText(getSherlockActivity(), "Please select atlest one file..", Toast.LENGTH_SHORT).show();
                    }

                    Toast.makeText(getActivity(), "Download", Toast.LENGTH_LONG).show();
                    return true;

                case R.id.action_mode_delete:
//                    Intent edit_med1=new Intent(getSherlockActivity(),DeleteMedication.class);
//                    edit_med1.putExtra("user_id",user.getId().toString());
//                    edit_med1.putExtra("id",listData.get(selected_position).getId());
//                    startActivity(edit_med1);
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



    private void initListView()
    {

        //goal_count.setText("("+files.size()+")");
        this.adapter = new MedicalDataAdapter1(getSherlockActivity(), R.layout.row_medical_list1,listData);
        this.list.setAdapter(adapter);
        int total_height_medicine_tab=0,len=0,i;

        for (i = 0, len = adapter.getCount(); i < len; i++) {
            View listItem = adapter.getView(i, null, this.list);
            listItem.measure(0, 0);
            int list_child_item_height = listItem.getMeasuredHeight()+this.list.getDividerHeight();//item height
            total_height_medicine_tab += list_child_item_height; //
        }

        LinearLayout.LayoutParams l3= new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,total_height_medicine_tab+40);
        this.list.setLayoutParams(l3);
        total_height_medicine_tab=0;


        this.list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
            {
                if (actionMode != null) {
                    // if already in action mode - do nothing
                    return false;
                }
                // set checked selected item and enter multi selection mode
                final TextView name=(TextView)arg1.findViewById(R.id.txt_name);
                final TextView txt_morn=(TextView)arg1.findViewById(R.id.txt_morning);
                final TextView txt_noon=(TextView)arg1.findViewById(R.id.txt_noon);
                final TextView txt_night=(TextView)arg1.findViewById(R.id.txt_night);

                selected_reminder_name=name.getText().toString();
                selected_morning_val=txt_morn.getText().toString();
                selected_noon_val=txt_noon.getText().toString();
                selected_night_val=txt_night.getText().toString();
                selected_position=arg2;
                adapter.setChecked(arg2, true);
                getSherlockActivity().startActionMode(new ActionModeCallbackMedicine());
                actionMode.invalidate();
                return true;
            }
        });

    }





    // async class for calling webservice and get responce message
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
            Toast.makeText(getSherlockActivity(),"size of medicine listData="+listData.size(),Toast.LENGTH_LONG).show();
            ga.listData=listData;
            initListView();

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




            ReminderFragment.RetrieveOtherData task1= new ReminderFragment.RetrieveOtherData();
            task1.applicationContext=getSherlockActivity();
            task1.execute();
            */



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




}