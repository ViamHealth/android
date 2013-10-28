package com.viamhealth.android.adapters;

import java.net.URI;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import com.viamhealth.android.R;
import com.viamhealth.android.ViamHealthPrefs;
import com.viamhealth.android.model.FileData;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class FileDataAdapter extends MultiSelectionAdapter<FileData> {
	Context context; 
	int layoutResourceId;    
	List<FileData> lstdata = null;
	Typeface tf;

	
	public FileDataAdapter(Context context, int layoutResourceId, List<FileData> lstdata) {
		super(context, lstdata);
		// TODO Auto-generated constructor stub
		
		 this.layoutResourceId = layoutResourceId;
	     this.context = context;
	     this.lstdata = lstdata;
	}

    public FileDataAdapter(Context context) {
        super(context);
        this.context = context;
    }

    @Override
	public View getItemView(int position, View convertView, ViewGroup parent) {
	        View row = convertView;
	        FileDataHolder holder = null;
	        tf = Typeface.createFromAsset(context.getAssets(),"Aparajita.ttf");
	        
	        if(row == null){
	            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
	            row = inflater.inflate(layoutResourceId, parent, false);
	            holder = new FileDataHolder();
	            holder.main_list_layout=(LinearLayout)row.findViewById(R.id.main_list_layout);
	            holder.img_icon=(ImageView)row.findViewById(R.id.img_icon);
	            holder.desc_layout=(LinearLayout)row.findViewById(R.id.desc_layout);
	            holder.img_name = (TextView)row.findViewById(R.id.img_name);
	            holder.img_desc = (TextView)row.findViewById(R.id.img_desc);
	            row.setTag(holder);
	        } else {
	            holder = (FileDataHolder)row.getTag();
	        }
	        
	        FileData data = lstdata.get(position);
	        holder.img_name.setText(data.getName().toString());
	        return row;
	    }
	    
	    static class FileDataHolder
	    {
	        TextView img_name,img_desc,img_lbl,img_date;
	        LinearLayout main_list_layout,desc_layout,end_layout;
	        ImageView img_icon;
	    }
	    
	    
	}

