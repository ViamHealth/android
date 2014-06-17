package com.viamhealth.android.adapters.task;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.viamhealth.android.R;
import com.viamhealth.android.model.enums.TaskAdapterType;
import com.viamhealth.android.model.tasks.ChallengeTask;
import com.viamhealth.android.model.tasks.Task;
import com.viamhealth.android.model.tasks.TaskData;

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

            final ChallengeTask tdObj = (ChallengeTask) item;

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

                        Toast.makeText(mContext,"clicked",Toast.LENGTH_LONG);
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
}
