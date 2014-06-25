package com.viamhealth.android.adapters.task;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.viamhealth.android.Global_Application;
import com.viamhealth.android.R;
import com.viamhealth.android.activities.ChallengeActivity;
import com.viamhealth.android.dao.rest.endpoints.TaskEP;
import com.viamhealth.android.model.ChallengeData;
import com.viamhealth.android.model.enums.TaskAdapterType;
import com.viamhealth.android.model.tasks.Task;

/**
 * Created by Kunal on 13/6/14.
 */
@DelegateAdapterType(itemType = TaskAdapterType.Challenge)
public class ChallengeDelegateAdapter implements DelegateAdapter {
    protected Context mContext;

    @Override
    public View getView(
            Context context, int position,
            View convertView, ViewGroup parent,
            LayoutInflater inflater, Task item) {

        this.mContext = context;

        if (convertView == null) {
            final View rowView = inflater.inflate(R.layout.task_list_challenge_element, parent, false);

            final TextView heading = (TextView) rowView.findViewById(R.id.task_heading);
            final TextView taskMessage = (TextView) rowView.findViewById(R.id.task_message);
            final TextView textViewStat = (TextView) rowView.findViewById(R.id.text_view_stat);
            final TextView textViewStatString = (TextView) rowView.findViewById(R.id.text_view_stat_string);
            final Button choice = (Button) rowView.findViewById(R.id.task_choice);

            final ChallengeData tdObj = (ChallengeData) item;

            try {
                heading.setText(tdObj.getTitle());
                taskMessage.setText(tdObj.getMessage());
                if (tdObj.getDayNum() == 0) {
                    textViewStat.setText(tdObj.getJoinedCount().toString());
                    textViewStatString.setText("Accepted");
                } else {
                    textViewStat.setText(tdObj.getDayNum().toString());
                    textViewStat.setText("Day");
                }
                choice.setText(tdObj.getLabelChoice1());

                choice.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {

                        Log.d("challenge data pre",tdObj.toString());
                        AcceptChallengeTask task = new AcceptChallengeTask();
                        task.task = tdObj;
                        task.execute();
                        //Toast.makeText(mContext,"clicked",Toast.LENGTH_LONG);


                        //Intent i = new Intent(mContext, BloodPressureTaskItemActivity.class);
                        //i.putExtra("taskData", tdObj);
                        //mContext.startActivity(i);
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            }
            return rowView;

        } else {
            return convertView;
        }
    }

    // async class for calling webservice and get responce message
    public class AcceptChallengeTask extends AsyncTask<String, Void,String>
    {
        protected FragmentActivity activity;
        protected ProgressDialog dialog;
        protected ChallengeData task;

        @Override
        protected void onPreExecute() {

            dialog = new ProgressDialog(mContext);
            dialog.setCanceledOnTouchOutside(false);
            dialog.setMessage("Please Wait....");
            dialog.show();

        }

        protected void onPostExecute(String result) {
            dialog.dismiss();
            if(result == "0"){
                Toast.makeText(mContext,R.string.apiSentError,Toast.LENGTH_LONG).show();
            }else {
                Intent i = new Intent(mContext, ChallengeActivity.class);
                Log.d("challenge task",task.toString());
                i.putExtra("taskData", task);
                mContext.startActivity(i);
            }
        }
        @Override
        protected String doInBackground(String... params) {
            TaskEP tep = new TaskEP(mContext, ((Global_Application)mContext.getApplicationContext()));
            return "1";
            /*if(tep.acceptChallenge(task.getId())){
                return "1";
            } else {
                return "0";
            }*/
        }

    }
}
