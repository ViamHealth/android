package com.viamhealth.android.adapters;

import java.util.ArrayList;

import com.viamhealth.android.R;
import com.viamhealth.android.ViamHealthPrefs;
import com.viamhealth.android.model.FoodData;
import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class FoodAdapter extends ArrayAdapter<FoodData> {
	Context context; 
	int layoutResourceId;    
	ArrayList<FoodData> lstdata = null;
	Typeface tf;
	ViamHealthPrefs appPrefs;
	int height,width,h5,w5,w3,w150,w10,w30;
	
	public FoodAdapter(Context context, int layoutResourceId, ArrayList<FoodData> lstdata) {
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
	        w150=(int)((width*46.88)/100);
	        w10=(int)((width*3.13)/100);
	        w30=(int)((width*9.38)/100);
	        
	        if(row == null)
	        {
	            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
	            row = inflater.inflate(layoutResourceId, parent, false);
	            
	            holder = new FileDataHolder();
	            
	            holder.main_list_layout=(LinearLayout)row.findViewById(R.id.main_list_layout);
	            holder.main_list_layout.setPadding(0, h5, 0, h5);
	           
	            holder.food_name = (TextView)row.findViewById(R.id.food_name);
	            //holder.food_name.getLayoutParams().width=w150;
	            holder.food_name.setPadding(w10, 0, 0, 0);
	            
	        
	            
	           
	            row.setTag(holder);
	        } 
	        else
	        {  
	            holder = (FileDataHolder)row.getTag();
	        }
	        
	        FoodData data = lstdata.get(position);
	        holder.food_name.setText(data.getName().toString());
	      
	        return row;
	    }
	    
	    static class FileDataHolder
	    {
	        TextView food_name,food_cal,food_egg,food_add;
	        CheckBox chk_select;
	        LinearLayout main_list_layout,desc_layout,end_layout;
	        ImageView img_icon;
	    }
	    
	    
	}

