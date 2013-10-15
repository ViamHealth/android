package com.viamhealth.android.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.viamhealth.android.R;
import com.viamhealth.android.model.enums.MedicalConditions;

import java.util.EnumSet;
import java.util.List;

/**
 * Created by naren on 10/10/13.
 */
public class MedicalConditionsAdapter extends ArrayAdapter<MedicalConditions> {

    private final Context mContext;
    private final MedicalConditions[] mConditions = MedicalConditions.values();
    private static final String TAG = "MedicalConditionsAdapter";

    public MedicalConditionsAdapter(Context context) {
        super(context, android.R.layout.simple_spinner_item, MedicalConditions.values());
        mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(android.R.layout.simple_spinner_item, parent, false);

        TextView text = (TextView) rowView.findViewById(android.R.id.text1);
        text.setText(mContext.getString(mConditions[position].key()));

        return rowView;
    }
}
