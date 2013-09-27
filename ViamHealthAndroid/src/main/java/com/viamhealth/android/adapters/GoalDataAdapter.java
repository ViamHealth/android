package com.viamhealth.android.adapters;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.viamhealth.android.R;
import com.viamhealth.android.ViamHealthPrefs;

public class GoalDataAdapter extends ArrayAdapter<String>{

	    Context context; 
	    int layoutResourceId;    
	    List<String> data;
	    Typeface tf;
	    ViamHealthPrefs appPrefs;
	    
	    public GoalDataAdapter(Context context, int layoutResourceId, List<String> data) {
	        super(context, layoutResourceId, data);
	        this.layoutResourceId = layoutResourceId;
	        this.context = context;
	        this.data = data;
	    }

	    @Override
	    public View getView(int position, View convertView, ViewGroup parent) {
	        View row = convertView;
	        WeatherHolder holder = null;
	        appPrefs = new ViamHealthPrefs(context);
	        tf = Typeface.createFromAsset(context.getAssets(),"Roboto-Condensed.ttf");
	        if(row == null)
	        {
	            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
	            row = inflater.inflate(layoutResourceId, parent, false);
	            
	            holder = new WeatherHolder();
	            holder.imgIcon = (ImageView)row.findViewById(R.id.imgIcon);
	            holder.txtTitle = (TextView)row.findViewById(R.id.txtName);
	            holder.txtTitle.setTypeface(tf);
	            row.setTag(holder);
	        }
	        else
	        {  
	            holder = (WeatherHolder)row.getTag();
	        }
	        
	       // GoalData weather = data[position];
	        holder.txtTitle.setText(data.get(position).toString());
	        if(holder.txtTitle.getText().toString().equals(appPrefs.getProfileName().toString())){
	        	Log.e("TAG","Clieckd true " + appPrefs.getProfileName().toString());
	        	holder.imgIcon.setVisibility(View.VISIBLE);
	        }else{
	        	holder.imgIcon.setVisibility(View.INVISIBLE);
	        }
	       // holder.imgIcon.setImageResource(weather.icon);
	        
	        return row;
	    }    
	    
	    static class WeatherHolder
	    {
	        ImageView imgIcon;
	        TextView txtTitle;
	    }
	    
	    
	}

