package com.viamhealth.android.adapters.cat;

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
import com.viamhealth.android.model.cat.CatData;
import com.viamhealth.android.model.enums.CatAdapterType;


/**
 * Created by Kunal on 18/6/14.
 */
@com.viamhealth.android.adapters.cat.DelegateAdapterType(itemType = CatAdapterType.Challenge)
public class ChallengeDelegateAdapter implements DelegateAdapter {
    protected Context mContext;

    @Override
    public View getView(
            Context context, int position,
            View convertView, ViewGroup parent,
            LayoutInflater inflater, CatData item) {

        this.mContext = context;

        if (convertView == null) {
            final View rowView = inflater.inflate(R.layout.cat_list_challenge_element, parent, false);

            final TextView heading = (TextView) rowView.findViewById(R.id.task_heading);
            final TextView textViewStat = (TextView) rowView.findViewById(R.id.text_view_stat);
            final TextView textViewStatString = (TextView) rowView.findViewById(R.id.text_view_stat_string);
            final Button choice = (Button) rowView.findViewById(R.id.task_choice);

            final ChallengeData tdObj = (ChallengeData) item;

            try {
                heading.setText(tdObj.getTitle());
                if (tdObj.getDayNum() == 0) {
                    textViewStat.setText(tdObj.getJoinedCount().toString());
                    textViewStatString.setText("Accepted");
                } else {
                    textViewStat.setText(tdObj.getDayNum().toString());
                    textViewStatString.setText("Day");
                }
                //choice.setText(tdObj.getLabelChoice1());

                choice.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(mContext, ChallengeActivity.class);
                        i.putExtra("taskData", tdObj);
                        mContext.startActivity(i);
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
