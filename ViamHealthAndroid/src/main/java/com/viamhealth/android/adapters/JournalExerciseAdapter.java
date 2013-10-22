package com.viamhealth.android.adapters;

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

import com.viamhealth.android.Global_Application;
import com.viamhealth.android.R;
import com.viamhealth.android.ViamHealthPrefs;
import com.viamhealth.android.model.CategoryExercise;
import com.viamhealth.android.model.CategoryFood;

import java.util.ArrayList;

/**
 * Created by Administrator on 10/12/13.
 */
public class JournalExerciseAdapter extends ArrayAdapter<CategoryExercise> {

    Context context;
    int layoutResourceId;
    ArrayList<CategoryExercise> lstdata = new ArrayList<CategoryExercise>();
    Typeface tf;
    ViamHealthPrefs appPrefs;
    int height,width;
    ProgressDialog dialog1;
    String idDel;


    public JournalExerciseAdapter(Context context, int layoutResourceId, ArrayList<CategoryExercise> lstdata) {
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

        if(row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new FileDataHolder();


            holder.exercisename = (TextView)row.findViewById(R.id.txt_name);
            //holder.foodname.getLayoutParams().width=w160;

            holder.calory = (TextView)row.findViewById(R.id.cal_value);
            holder.time = (TextView)row.findViewById(R.id.duration_val);
            //holder.surning.getLayoutParams().width = w30;

            holder.delete = (ImageView)row.findViewById(R.id.delete);


            row.setTag(holder);
        }
        else
        {
            holder = (FileDataHolder)row.getTag();
        }
        Log.e("TAG",position+"");
        CategoryExercise data = lstdata.get(position);
        holder.exercisename.setText(data.getName().toString());
        holder.calory.setText(data.getCalories().toString());
        holder.time.setText(data.getTime().toString());
        //holder.delete.setTag(data.getId().toString());
        return row;
    }

    static class FileDataHolder
    {
        TextView exercisename,calory,time;
        ImageView delete;
        LinearLayout main_list_layout;
    }
}
