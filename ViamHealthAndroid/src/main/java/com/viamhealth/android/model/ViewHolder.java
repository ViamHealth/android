package com.viamhealth.android.model;

import com.viamhealth.android.R;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ViewHolder {

    public TextView txt_grp_food_name,txt_calories;
    public ImageView btnAdd,btnAdd1;
    public LinearLayout main;
    public ViewHolder(View v) {
        this.txt_grp_food_name = (TextView)v.findViewById(R.id.txt_grp_food_name);
        this.txt_calories = (TextView)v.findViewById(R.id.txt_calory);
        
        this.btnAdd1 = (ImageView)v.findViewById(R.id.imgopt1);
        this.btnAdd = (ImageView)v.findViewById(R.id.imgopt);
        this.main=(LinearLayout)v.findViewById(R.id.main);
    }

}
