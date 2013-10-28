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
	        
	        if(row == null)
	        {
	            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
	            row = inflater.inflate(layoutResourceId, parent, false);
	            
	            holder = new FileDataHolder();
	            holder.main_list_layout=(LinearLayout)row.findViewById(R.id.main_list_layout);

	            holder.img_icon=(ImageView)row.findViewById(R.id.img_icon);

	            holder.desc_layout=(LinearLayout)row.findViewById(R.id.desc_layout);

	           /* holder.end_layout=(LinearLayout)row.findViewById(R.id.end_layout);
	            holder.end_layout.setPadding(w5, h5, w5, h5);*/
	            
	            holder.img_name = (TextView)row.findViewById(R.id.img_name);
	            holder.img_desc = (TextView)row.findViewById(R.id.img_desc);
	          /*  holder.img_lbl = (TextView)row.findViewById(R.id.img_lbl);
	            holder.img_lbl.getLayoutParams().width=w45;
	            
	            holder.img_date = (TextView)row.findViewById(R.id.img_date);
	            holder.img_date.setPadding(w5,0 ,0, 0);*/
	            
	           /* holder.chk_select = (CheckBox)row.findViewById(R.id.chk_select);
	            holder.chk_select.setOnClickListener(new View.OnClickListener() {
					
					@Override  
					public void onClick(View v) {
						// TODO Auto-generated method stub
						final CheckBox cb = (CheckBox) v;
						 FileData data = (FileData)cb.getTag();
						 if(data.getDownload_url().length()>0){
							 data.setChecked(cb.isChecked());
						 }else{
								AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
						 
									// set title
									alertDialogBuilder.setTitle("Not available");
						 
									// set dialog message
									alertDialogBuilder
										.setMessage("No Download url available...")
										.setCancelable(false)
									
										.setNegativeButton("No",new DialogInterface.OnClickListener() {
											public void onClick(DialogInterface dialog,int id) {
												// if this button is clicked, just close
												// the dialog box and do nothing
												cb.setChecked(false);
												dialog.cancel();
											}
										});
						 
										// create alert dialog
										AlertDialog alertDialog = alertDialogBuilder.create();
						 
										// show it
										alertDialog.show();
						 }
					}
				});*/
	            row.setTag(holder);
	        } 
	        else
	        {  
	            holder = (FileDataHolder)row.getTag();
	        }
	        
	        FileData data = lstdata.get(position);
	        holder.img_name.setText(data.getName().toString());
	        holder.img_desc.setText(data.getDescription().toString());
	       // holder.img_lbl.setText(data.getImg_lbl().toString());
	       // holder.img_date.setText(data.getImg_date().toString());
	        //holder.chk_select.setTag(data);
	        //holder.chk_select.setChecked(data.isChecked());
	        return row;
	    }
	    
	    static class FileDataHolder
	    {
	        TextView img_name,img_desc,img_lbl,img_date;
	        //CheckBox chk_select;
	        LinearLayout main_list_layout,desc_layout,end_layout;
	        ImageView img_icon;
	    }
	    
	    
	}

