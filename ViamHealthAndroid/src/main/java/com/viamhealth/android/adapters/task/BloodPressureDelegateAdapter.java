package com.viamhealth.android.adapters.task;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.viamhealth.android.R;
import com.viamhealth.android.activities.BloodPressureTaskItemActivity;
import com.viamhealth.android.model.enums.TaskAdapterType;
import com.viamhealth.android.model.tasks.BloodPressureTask;
import com.viamhealth.android.model.tasks.Task;

@DelegateAdapterType(itemType = TaskAdapterType.BloodPressure)
public class BloodPressureDelegateAdapter implements DelegateAdapter {

    protected Context mContext;


    @Override
    public View getView(Context context, int position, View convertView, ViewGroup parent, LayoutInflater inflater, Task item) {
        this.mContext = context;
        final BloodPressureTask taskData = (BloodPressureTask) item;
        if (convertView == null) {
            final View rowView = inflater.inflate(R.layout.task_list_bp_element, parent, false);
            final TextView message = (TextView) rowView.findViewById(R.id.task_message);
            final TextView edittextBP = (TextView) rowView.findViewById(R.id.text_systolic_pressure_fake_edit);

            final BloodPressureTask tdObj = (BloodPressureTask) item;
            try {
                message.setText(tdObj.getMessage());
                rowView.setTag(tdObj.getId());

                edittextBP.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(mContext, BloodPressureTaskItemActivity.class);
                        i.putExtra("taskData", taskData);
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
