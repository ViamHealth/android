package com.viamhealth.android.adapters;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.viamhealth.android.R;
import com.viamhealth.android.ViamHealthPrefs;

public class MedicalDataAdapter extends ArrayAdapter<String> {
	Context context; 
	int layoutResourceId;    
	ArrayList<String> lstdata = null;
	Typeface tf;
	ViamHealthPrefs appPrefs;
	int height,width,w30,w150,w10;
	
	public MedicalDataAdapter(Context context, int layoutResourceId, ArrayList<String> lstdata) {
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
	            holder.txt_name.getLayoutParams().width = w150;
	          
	            holder.txt_morning = (TextView)row.findViewById(R.id.txt_morning);
	            holder.txt_morning.getLayoutParams().width = w30;
	            
	            holder.txt_noon = (TextView)row.findViewById(R.id.txt_noon);
	            holder.txt_noon.getLayoutParams().width = w30;
	            
	            holder.txt_night = (TextView)row.findViewById(R.id.txt_night);
	            holder.txt_night.getLayoutParams().width = w30;
	            
	            holder.txt1 = (TextView)row.findViewById(R.id.txt1);
	            holder.txt1.setPadding(w10, 0, 0, 0);
	            
	            holder.txt2 = (TextView)row.findViewById(R.id.txt2);
	            holder.txt2.setPadding(w10, 0, 0, 0);
	         
	            holder.txt_morning.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						TextView txt = (TextView)v.findViewById(R.id.txt_morning);
						txt.setBackgroundColor(Color.GREEN);
					}
				});
	            
	            holder.txt_noon.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						TextView txt = (TextView)v.findViewById(R.id.txt_noon);
						txt.setBackgroundColor(Color.GREEN);
					}
				});
	            holder.txt_night.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						TextView txt = (TextView)v.findViewById(R.id.txt_night);
						txt.setBackgroundColor(Color.GREEN);
					}
				});
	            
	            
	            row.setTag(holder);
	        }     
	        else
	        {  
	            holder = (FileDataHolder)row.getTag();
	        }
	        
	        String data = lstdata.get(position);
	        holder.txt_name.setText(data);
	      
	        return row;
	    }
	    
	    static class FileDataHolder
	    {
	        TextView txt_name,txt_morning,txt1,txt_noon,txt2,txt_night;
	      
	        
	    }
	    
	    
	}

