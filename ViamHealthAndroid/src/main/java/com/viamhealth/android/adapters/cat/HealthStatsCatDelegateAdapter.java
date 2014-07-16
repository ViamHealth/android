package com.viamhealth.android.adapters.cat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.viamhealth.android.R;
import com.viamhealth.android.model.cat.CatData;
import com.viamhealth.android.model.cat.HealthStatsCatData;
import com.viamhealth.android.model.enums.CatAdapterType;

import java.text.SimpleDateFormat;

/**
 * Created by Kunal on 19/6/14.
 */
@com.viamhealth.android.adapters.cat.DelegateAdapterType(itemType = CatAdapterType.HealthStats)
public class HealthStatsCatDelegateAdapter implements DelegateAdapter {
    protected Context mContext;

    @Override
    public View getView(
            Context context, int position,
            View convertView, ViewGroup parent,
            LayoutInflater inflater, CatData item) {

        this.mContext = context;

        if (convertView == null) {
            final View rowView = inflater.inflate(R.layout.cat_list_health_stats_element, parent, false);

            LinearLayout weightLayout = (LinearLayout) rowView.findViewById(R.id.weightHealthStatsCat);
            TextView weightText = (TextView) rowView.findViewById(R.id.weight);
            TextView dateHeader = (TextView) rowView.findViewById(R.id.dateHeader);

            LinearLayout bpLayout = (LinearLayout) rowView.findViewById(R.id.bpHealthStatsCat);
            TextView diastolicText = (TextView) rowView.findViewById(R.id.diastolic_pressure);
            TextView systolicText = (TextView) rowView.findViewById(R.id.systolic_pressure);
            TextView pulseRateText = (TextView) rowView.findViewById(R.id.pulse_rate);


            final HealthStatsCatData tdObj = (HealthStatsCatData) item;
            SimpleDateFormat sdf = new SimpleDateFormat("dd, MMM");

            try {
                dateHeader.setText(sdf.format(tdObj.getStartDate()));
                if (tdObj.isSetWeightReading()) {
                    if (tdObj.getWeightReading() != null && tdObj.getWeightReading().getWeight() != null) {
                        weightLayout.setVisibility(View.VISIBLE);
                        weightText.setText(tdObj.getWeightReading().getWeight().toString());
                    }
                }
                if (tdObj.isSetBloodPressureReading()) {
                    if (tdObj.getBloodPressureReading() != null) {
                        bpLayout.setVisibility(View.VISIBLE);
                        if (tdObj.getBloodPressureReading().getDiastolicPressure() != null) {
                            diastolicText.setText(tdObj.getBloodPressureReading().getDiastolicPressure().toString());
                        }
                        if (tdObj.getBloodPressureReading().getSystolicPressure() != null) {
                            systolicText.setText(tdObj.getBloodPressureReading().getSystolicPressure().toString());
                        }
                        if (tdObj.getBloodPressureReading().getPulseRate() != null) {
                            pulseRateText.setText(tdObj.getBloodPressureReading().getPulseRate().toString());
                        }
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return rowView;

        } else {
            return convertView;
        }
    }

}
