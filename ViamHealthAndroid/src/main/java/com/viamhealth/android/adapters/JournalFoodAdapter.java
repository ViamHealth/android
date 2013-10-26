package com.viamhealth.android.adapters;

import java.util.ArrayList;

import com.viamhealth.android.Global_Application;
import com.viamhealth.android.R;
import com.viamhealth.android.ViamHealthPrefs;
import com.viamhealth.android.model.CategoryFood;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Typeface;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class JournalFoodAdapter extends ArrayAdapter<CategoryFood> {
    Context context;
    int layoutResourceId;
    ArrayList<CategoryFood> lstdata = new ArrayList<CategoryFood>();
    Typeface tf;
    ViamHealthPrefs appPrefs;
    int height,width;
    ProgressDialog dialog1;
    String idDel;


    public JournalFoodAdapter(Context context, int layoutResourceId, ArrayList<CategoryFood> lstdata) {
        super(context, layoutResourceId, lstdata);
        // TODO Auto-generated constructor stub

        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.lstdata = lstdata;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        FileDataHolder holder = null;
        appPrefs=new ViamHealthPrefs(context);
        tf = Typeface.createFromAsset(context.getAssets(),"Aparajita.ttf");
        int w160,w30,h5;
        //calculate padding dynamically using get screen height width from preference
        height=Integer.parseInt(appPrefs.getSheight());
        h5 = (int)((height*1.041)/100);

        width=Integer.parseInt(appPrefs.getSwidth());
        w160=(int)((width*50)/100);
        w30=(int)((width*9.37)/100);

        holder = new FileDataHolder();


        if(row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);



            holder.foodname = (TextView)row.findViewById(R.id.foodname);
            holder.calory = (TextView)row.findViewById(R.id.cal_value);
            holder.surning = (TextView)row.findViewById(R.id.serving_val);

            // holder.surning.getLayoutParams().width = w30;
/*
            holder.delete = (ImageView)row.findViewById(R.id.delete);

            holder.delete.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    final ImageView img = (ImageView)v;
                    Log.e("TAG","Delete id is : " + img.getTag());
                    idDel=img.getTag().toString();
                    Global_Application.selectedfoodid = idDel;
                }

            });


            row.setTag(holder);
         */
        }
        else
        {
            holder = (FileDataHolder)row.getTag();
        }
        Log.e("TAG",position+"");

        Log.e("TAG","value of holder ="+holder);

        CategoryFood data = lstdata.get(position);

        if(holder!=null)
        {
            holder.foodname.setText(data.getName().toString());
            float calories=Float.valueOf(data.getCalories())*Float.valueOf(data.getMultiplier());
            holder.calory.setText(String.valueOf(calories));
            holder.surning.setText(data.getMultiplier().toString());
        }
       // holder.delete.setTag(data.getId().toString());
        return row;
    }

    static class FileDataHolder
    {
        TextView foodname,calory,surning;
        ImageView delete;
        LinearLayout main_list_layout;
    }



}

