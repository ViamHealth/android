package com.viamhealth.android.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.viamhealth.android.model.enums.BloodGroup;

import java.util.EnumSet;

/**
 * Created by naren on 04/10/13.
 */
public class BloodGroupAdapter extends ArrayAdapter<BloodGroup> {

    public BloodGroupAdapter(Context context, int resource, BloodGroup[] objects) {
        super(context, resource, objects);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return super.getDropDownView(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return super.getView(position, convertView, parent);
    }

    //public View getCustomView(int position, View convertView, ViewGroup parent) {
// TODO Auto-generated method stub
//return super.getView(position, convertView, parent);

        //LayoutInflater inflater=getLayoutInflater();
        //View row=inflater.inflate(android.R.layout.simple_dropdown_item_1line, parent, false);
        //TextView label=(TextView)row.findViewById(R.id.weekofday);
        //label.setText();

        //return row;
    //}

}
