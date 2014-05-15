package com.viamhealth.android.adapters;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.viamhealth.android.Global_Application;
import com.viamhealth.android.R;
import com.viamhealth.android.dao.rest.endpoints.TaskEP;
import com.viamhealth.android.model.TaskData;
import com.viamhealth.android.model.users.User;
import com.viamhealth.android.utils.Checker;


import java.util.List;

/**
 * Created by Kunal on 14/5/14.
 */
public class TaskListAdapter extends ArrayAdapter<TaskData> {
    private final Context context;
    private final List<TaskData> values;
    private Global_Application ga;
    private User selectedUser;

    private static final String TAG = "TaskListAdapter";

    public TaskListAdapter(Context context, Global_Application ga, List<TaskData> objects, User user) {
        super(context, R.layout.task_list_element, objects);
        this.ga = ga;
        this.context = context;
        this.values = objects;
        this.selectedUser = user;

    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View rowView = inflater.inflate(R.layout.task_list_element, parent, false);

        TextView message = (TextView) rowView.findViewById(R.id.task_message);
        final Button choice1 = (Button) rowView.findViewById(R.id.task_choice_1);
        final Button choice2 = (Button) rowView.findViewById(R.id.task_choice_2);

        try{
            message.setText(values.get(position).getMessage());
            choice1.setText(values.get(position).getLabelChoice1());
            choice1.setTag(values.get(position).getId());
            if(values.get(position).getSetChoice() == 1)
                choice1.setBackgroundColor(Color.parseColor("#c9c9c9"));

            choice1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    rowView.setBackgroundColor(Color.parseColor("#cccccc"));
                    choice1.setBackgroundColor(Color.parseColor("#c9c9c9"));
                    choice2.setBackgroundColor(Color.parseColor("green"));
                    if(values.get(position).getFeedbackMessageChoice1() != null ) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        StringBuilder strBuilder = new StringBuilder(values.get(position).getFeedbackMessageChoice1());
                        builder.setMessage(strBuilder.toString());
                        builder.show();
                    }
                    selectChoice(v, 1);

                }
            });

            choice2.setText(values.get(position).getLabelChoice2());
            choice2.setTag(values.get(position).getId());
            if(values.get(position).getSetChoice() == 2)
                choice2.setBackgroundColor(Color.parseColor("#c9c9c9"));

            choice2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    rowView.setBackgroundColor(Color.parseColor("#cccccc"));
                    choice2.setBackgroundColor(Color.parseColor("#c9c9c9"));
                    choice1.setBackgroundColor(Color.parseColor("green"));
                    if(values.get(position).getFeedbackMessageChoice2() != null ){
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        StringBuilder strBuilder = new StringBuilder(values.get(position).getFeedbackMessageChoice2());
                        builder.setMessage(strBuilder.toString());
                        builder.show();
                    }

                    selectChoice(v, 2);

                }
            });
            rowView.setTag(values.get(position).getId());
            Log.d(TAG, "Added " + values.get(position) + " at position " + position);
        }catch(Exception e){
            e.printStackTrace();
        }
        return rowView;
    }
    public void selectChoice(View view, int choice){
        String set_choice="1";
        Toast.makeText(context, "Working..", Toast.LENGTH_SHORT).show();
        if(choice == 1){
            set_choice = "1";
        } else if ( choice == 2 ){
            set_choice = "2";
        }
        if(Checker.isInternetOn(context)){
            CallTaskChoiceTask task = new CallTaskChoiceTask();
            task.execute(set_choice, view.getTag().toString());
        }else{
            Toast.makeText(context, "Network is not available....", Toast.LENGTH_SHORT).show();
        }

    }

    // async class for calling webservice and get responce message
    public class CallTaskChoiceTask extends AsyncTask<String, Void,String>
    {
        protected FragmentActivity activity;
        protected ProgressDialog dialog;

        @Override
        protected String doInBackground(String... params) {
            String choice = params[0];
            String id = params[1];
            TaskEP tep = new TaskEP(context,ga);
            tep.selectChoice(id,choice);
            return null;
        }

    }




}
