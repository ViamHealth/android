package com.viamhealth.android.adapters;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.viamhealth.android.R;
import com.viamhealth.android.ViamHealthPrefs;

public class TestDataAdapter extends ArrayAdapter<String> {
	Context context; 
	int layoutResourceId;    
	ArrayList<String> lstdata = null;
	Typeface tf;
	ViamHealthPrefs appPrefs;
	int height,width,h5,w5,w3,w100,w45;
	int on=0,off=0;
	public TestDataAdapter(Context context, int layoutResourceId, ArrayList<String> lstdata) {
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
	        h5=(int)((height*1.042)/100);
	       
	        width=Integer.parseInt(appPrefs.getSwidth());
	        w5=(int)((width*1.7)/100);
	        w3=(int)((width*0.93)/100);
	        w100=(int)((width*38.25)/100);
	        w45=(int)((width*14.06)/100);
	        
	        
	        if(row == null)
	        {
	            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
	            row = inflater.inflate(layoutResourceId, parent, false);
	            
	            holder = new FileDataHolder();
	            
	            holder.txt_name = (TextView)row.findViewById(R.id.txt_name);
	            holder.txt_name.getLayoutParams().width = w100;
	            
	            holder.btn_on = (ImageView)row.findViewById(R.id.btn_on);
	            holder.btn_on.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						ImageView btnon = (ImageView)v;
						//ImageView btnoff = (ImageView)v;
					//	btnoff.setVisibility(View.VISIBLE);
						//btnon.setVisibility(View.GONE);
						if(on==0){
							on=1;
							btnon.setImageResource(R.drawable.btn_off);
						}else{
							on=0;
							btnon.setImageResource(R.drawable.btn_on);
						}
					}
				});
	            holder.btn_off = (ImageView)row.findViewById(R.id.btn_off);
	            holder.btn_off.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						//ImageView btnon = (ImageView)v;
						ImageView btnoff = (ImageView)v;
						if(off==0){
							off=1;
							btnoff.setImageResource(R.drawable.btn_on);
						}else{
							off=0;
							btnoff.setImageResource(R.drawable.btn_off);
						}
						//btnoff.setVisibility(View.GONE);
						//btnon.setVisibility(View.VISIBLE);
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
	        TextView txt_name;
	        ImageView btn_off,btn_on;
	        
	    }
	    
	    
	}

