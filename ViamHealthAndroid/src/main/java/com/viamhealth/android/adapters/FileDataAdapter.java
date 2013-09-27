package com.viamhealth.android.adapters;

import java.util.ArrayList;

import com.viamhealth.android.R;
import com.viamhealth.android.ViamHealthPrefs;
import com.viamhealth.android.model.FileData;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class FileDataAdapter extends ArrayAdapter<FileData> {
	Context context; 
	int layoutResourceId;    
	ArrayList<FileData> lstdata = null;
	Typeface tf;
	ViamHealthPrefs appPrefs;
	int height,width,h5,w5,w3,w100,w45;
	
	public FileDataAdapter(Context context, int layoutResourceId, ArrayList<FileData> lstdata) {
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
	        w100=(int)((width*32.25)/100);
	        w45=(int)((width*14.06)/100);
	        
	        
	        if(row == null)
	        {
	            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
	            row = inflater.inflate(layoutResourceId, parent, false);
	            
	            holder = new FileDataHolder();
	            holder.main_list_layout=(LinearLayout)row.findViewById(R.id.main_list_layout);
	            holder.main_list_layout.setPadding(0, h5, w5, h5);
	            
	            holder.img_icon=(ImageView)row.findViewById(R.id.img_icon);
	            holder.img_icon.setPadding(0, 0, w5, 0);
	            
	            holder.desc_layout=(LinearLayout)row.findViewById(R.id.desc_layout);
	            holder.desc_layout.getLayoutParams().width=w100;
	            
	           /* holder.end_layout=(LinearLayout)row.findViewById(R.id.end_layout);
	            holder.end_layout.setPadding(w5, h5, w5, h5);*/
	            
	            holder.img_name = (TextView)row.findViewById(R.id.img_name);
	            holder.img_desc = (TextView)row.findViewById(R.id.img_desc);
	          /*  holder.img_lbl = (TextView)row.findViewById(R.id.img_lbl);
	            holder.img_lbl.getLayoutParams().width=w45;
	            
	            holder.img_date = (TextView)row.findViewById(R.id.img_date);
	            holder.img_date.setPadding(w5,0 ,0, 0);*/
	            
	            holder.chk_select = (CheckBox)row.findViewById(R.id.chk_select);
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
				});
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
	        holder.chk_select.setTag(data);
	        holder.chk_select.setChecked(data.isChecked());
	        return row;
	    }
	    
	    static class FileDataHolder
	    {
	        TextView img_name,img_desc,img_lbl,img_date;
	        CheckBox chk_select;
	        LinearLayout main_list_layout,desc_layout,end_layout;
	        ImageView img_icon;
	    }
	    
	    
	}

