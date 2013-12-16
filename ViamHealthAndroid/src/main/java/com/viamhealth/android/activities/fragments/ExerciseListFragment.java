package com.viamhealth.android.activities.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.SherlockListFragment;
import com.actionbarsherlock.view.ActionMode;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.viamhealth.android.Global_Application;
import com.viamhealth.android.R;
import com.viamhealth.android.ViamHealthPrefs;
import com.viamhealth.android.activities.Downlaod;
import com.viamhealth.android.adapters.ExerciseAdapter;
import com.viamhealth.android.adapters.FileDataAdapter;
import com.viamhealth.android.adapters.JournalExerciseAdapter;
import com.viamhealth.android.adapters.JournalFoodAdapter;
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
public class ExerciseListFragment extends BaseListFragment
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
    ProgressDialog dialog1;

    // if ActoinMode is null - assume we are in normal mode
    private ActionMode actionMode;
    private ViamHealthPrefs appPrefs;
    DialogInterface d1=null;

    private User user;
    ArrayList<MedicationData> otherData = new ArrayList<MedicationData>();

    String selected_reminder_name,selected_morning_val,selected_noon_val,selected_night_val;
    int selected_position;
    private static final int LIBRARY_FILE_VIEW = 1000;
    MedicalDataAdapter1 adapter4;
    CommunicationActivity mCallback;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.fragment_sherlock_list, null);

        user = getArguments().getParcelable("user");

        obj=new functionClass(getSherlockActivity());
        ga=((Global_Application)getSherlockActivity().getApplicationContext());
        appPrefs = new ViamHealthPrefs(getActivity());

        this.list = (ListView) v.findViewById(android.R.id.list);


        initListView();
        return v;
    }




    // Container Activity must implement this interface
    public interface CommunicationActivity {
        public void updateData();
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (CommunicationActivity) activity;
        } catch (ClassCastException e) {
             e.printStackTrace();
        }
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


    public final class ActionModeCallbackExercise implements ActionMode.Callback
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







        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {

            switch (item.getItemId()) {
                case R.id.action_mode_edit:


                    final AlertDialog.Builder alert = new AlertDialog.Builder(getSherlockActivity());
                    LayoutInflater li = getSherlockActivity().getLayoutInflater();// LayoutInflater.from(getBaseContext());
                    View view = li.inflate(R.layout.edit_exercise, null);

                    alert.setMessage("Enter Calories and Time");
                    alert.setView(view);
                    final EditText txt_calorie,txt_time;
                    Global_Application.selectedexerciseid=ga.lstResultExercise.get(selected_position).getId();
                    Global_Application.exercise_value=ga.lstResultExercise.get(selected_position).getValue();
                    txt_calorie=(EditText)view.findViewById(R.id.txt_calorie);
                    txt_time=(EditText)view.findViewById(R.id.txt_time);
                    alert.setPositiveButton("Edit", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            if(Checker.isInternetOn(getActivity())){
                                if(txt_calorie.getText()!=null)
                                {
                                    Global_Application.user_calories=txt_calorie.getText().toString();
                                }
                                else
                                {
                                    Global_Application.user_calories=ga.lstResultExercise.get(selected_position).getCalories();
                                }

                                if(txt_time.getText()!=null)
                                {
                                    Global_Application.time_spent=txt_time.getText().toString();
                                }
                                else
                                {
                                    Global_Application.time_spent=ga.lstResultExercise.get(selected_position).getTime();
                                }

                                CallEditExercise task = new CallEditExercise();
                                task.activity =getSherlockActivity();
                                task.execute();
                            }else{
                                Toast.makeText(getSherlockActivity(),"Network is not available....",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                    alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            dialog.cancel();
                        }
                    });
                    alert.show();
                    return true;






                case R.id.action_mode_delete:
                    ga.setSelectedExerciseid(ga.lstResultExercise.get(selected_position).getId());
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                            getSherlockActivity());

                    // set title

                    alertDialogBuilder.setTitle("Confirmation");

                    // set dialog message
                    alertDialogBuilder
                            .setMessage("Are you sure you want to delete this food?")
                            .setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    // if this button is clicked, close
                                    // current activity
                                    //dialog.cancel();
                                    d1=dialog;
                                    if (Checker.isInternetOn(getActivity())) {
                                        CallDeleteTask task = new CallDeleteTask();
                                        task.activity = getSherlockActivity();
                                        task.execute();
                                    } else {
                                        Toast.makeText(getSherlockActivity(), "Network is not available....", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    // if this button is clicked, just close
                                    // the dialog box and do nothing
                                    dialog.cancel();
                                }
                            });

                    // create alert dialog
                    AlertDialog alertDialog = alertDialogBuilder.create();

                    // show it
                    alertDialog.show();
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

    public void removefragment()
    {
       getSherlockActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
        getSherlockActivity().getSupportFragmentManager().executePendingTransactions();
    }

    public class CallDeleteTask extends AsyncTask <String, Void,String>
    {
        protected FragmentActivity activity;

        @Override
        protected void onPreExecute()
        {


            Log.i("onPreExecute", "onPreExecute");
            Log.i("onPreExecute", "before calling delete task");

        }

        protected void onPostExecute(String result)
        {
            // dialog1.dismiss();
            Log.i("onPostExecute", "onPostExecute");
            if(d1!=null)
            {
                d1.dismiss();
                d1=null;
            }
            //adapter.clear();
            //adapter.notifyDataSetChanged();
            removefragment();
            JournalFragment.task1.execute();

            //adapter.clear();
            //adapter.notifyDataSetChanged();
            //if(Checker.isInternetOn(getActivity()))
            //{
             //   CallExerciseListTask t1= new CallExerciseListTask();
               // t1.execute();
            //}

        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            Log.i("doInBackground--Object", "doInBackground--Object");
            User user = getArguments().getParcelable("user");
            return obj.DeleteFood("user-physical-activity/",Global_Application.selectedexerciseid,user.getId().toString());
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
            Log.i("onPreExecute", "onPreExecute");

        }

        protected void onPostExecute(String result)
        {
            dialog1.dismiss();
            if(Checker.isInternetOn(getActivity()))
            {
                CallExerciseListTask task= new CallExerciseListTask();
                task.execute();
            }
            else
            {
                Toast.makeText(activity,"Network is not available....",Toast.LENGTH_SHORT).show();
            }
            initListView();

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

            return obj.EditExercise(Global_Application.selectedexerciseid, Global_Application.weight, Global_Application.user_calories, Global_Application.time_spent, user.getId().toString(), Global_Application.exercise_value);
        }

    }


    public class CallExerciseListTask extends AsyncTask <String, Void,String>
    {
        protected FragmentActivity activity;

        @Override
        protected void onPreExecute()
        {

            //dialog = ProgressDialog.show(applicationContext, "Calling", "Please wait...", true);

            Log.i("onPreExecute", "onPreExecute");


        }

        protected void onPostExecute(String result)
        {

            initListView();
        }


        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            Log.i("doInBackground--Object", "doInBackground--Object");
            //ga.lstResult=obj.manageGoal(appPrefs.getGoalname().toString(), type, goalvalue);
            User user = getArguments().getParcelable("user");
            String date=getArguments().getString("activity_date");
            ga.lstResultExercise = obj.getExercise(user.getId().toString(),date);
            return null;
        }

    }




    private void initListView()
    {

        this.adapter = new JournalExerciseAdapter(getSherlockActivity(), R.layout.row_journal_list_exercise,ga.lstResultExercise);
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

                selected_position=arg2;
                adapter.setChecked(arg2, true);
                getSherlockActivity().startActionMode(new ActionModeCallbackExercise());
                actionMode.invalidate();
                return true;
            }
        });

    }


}