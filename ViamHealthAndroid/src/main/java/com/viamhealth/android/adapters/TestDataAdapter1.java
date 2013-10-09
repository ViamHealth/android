package com.viamhealth.android.adapters;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.viamhealth.android.R;
import com.viamhealth.android.ViamHealthPrefs;
import com.viamhealth.android.model.MedicationData;

import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

public class TestDataAdapter1 extends ArrayAdapter<MedicationData> {
    Context context;
    int layoutResourceId;
    ArrayList<MedicationData> lstdata = null;
    Typeface tf;
    ViamHealthPrefs appPrefs;
    int height,width,w30,w150,w10;

    public TestDataAdapter1(Context context, int layoutResourceId, ArrayList<MedicationData> lstdata) {
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

        //calculate padding dynamically using get screen height width from preference
        height=Integer.parseInt(appPrefs.getSheight());

        width=Integer.parseInt(appPrefs.getSwidth());

        w150=(int)((width*37.5)/100);
        w30=(int)((width*9.37)/100);
        w10=(int)((width*9.37)/100);
        if(row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new FileDataHolder();

            holder.txt_name = (TextView)row.findViewById(R.id.txt_name);
            holder.txt_morning = (TextView)row.findViewById(R.id.txt_morning);
            holder.txt_noon = (TextView)row.findViewById(R.id.txt_noon);
            holder.txt_night = (TextView)row.findViewById(R.id.txt_night);
            holder.txt1 = (TextView)row.findViewById(R.id.txt1);
            // holder.txt1.setPadding(w10, 0, 0, 0);
            holder.txt2 = (TextView)row.findViewById(R.id.txt2);
            // holder.txt2.setPadding(w10, 0, 0, 0);
            row.setTag(holder);
        }
        else
        {
            holder = (FileDataHolder)row.getTag();
        }

        String data = lstdata.get(position).getName();
        holder.txt_name.setText(data);
        holder.txt_morning.setVisibility(View.INVISIBLE);
        holder.txt_noon.setVisibility(View.INVISIBLE);
        holder.txt_night.setVisibility(View.INVISIBLE);
        holder.img1=(ImageView)row.findViewById(R.id.img1);
        holder.img2=(ImageView)row.findViewById(R.id.img2);
        LinearLayout.LayoutParams lpar = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, Gravity.RIGHT);
        holder.img2.setLayoutParams(lpar);
        holder.img1.setLayoutParams(lpar);
        return row;
    }



    static class FileDataHolder
    {
        TextView txt_name,txt_morning,txt1,txt_noon,txt2,txt_night;
        ImageView img1,img2;

    }


}

