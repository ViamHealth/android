package com.viamhealth.android.adapters;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.viamhealth.android.R;
import com.viamhealth.android.ViamHealthPrefs;
import com.viamhealth.android.model.MedicationData;

import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

public class MedicalDataAdapter1 extends ArrayAdapter<MedicationData> {
	Context context; 
	int layoutResourceId;
    Date start_date,end_date;
	ArrayList<MedicationData> lstdata = null;
	Typeface tf;
	ViamHealthPrefs appPrefs;
	int height,width,w30,w150,w10;
	
	public MedicalDataAdapter1(Context context, int layoutResourceId, ArrayList<MedicationData> lstdata) {
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
	           // holder.txt1.setPadding(w10, 0, 0, 0);
	            
	            holder.txt2 = (TextView)row.findViewById(R.id.txt2);
	           // holder.txt2.setPadding(w10, 0, 0, 0);

                holder.txt_start_date=(TextView)row.findViewById(R.id.txt_start_date);
                holder.txt_duration=(TextView)row.findViewById(R.id.txt_duration);
                holder.txt_duration_type=(TextView)row.findViewById(R.id.txt_duration_type);
                holder.txt_start_date.setText("2009-08-14");
                holder.txt_duration.setText("");
                holder.txt_duration_type.setText("");

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
                String get_start_date,get_end_date;
                get_start_date=lstdata.get(position).getStart_date();
                get_end_date=lstdata.get(position).getEnd_date();

                if(get_start_date!=null && get_end_date!=null)
                {
                    holder.txt_start_date.setText(lstdata.get(position).getStart_date());
                    try {
                        start_date=new SimpleDateFormat("yyyy-MM-dd").parse(get_start_date);
                        end_date=new SimpleDateFormat("yyyy-MM-dd").parse(get_end_date);
                    } catch (ParseException e) {
                        e.printStackTrace();
                        end_date=null;
                    }
                    if(end_date!=null && start_date!=null)
                    {
                        int days=(int)((end_date.getTime()-start_date.getTime())/(1000*60*60*24));
                        int duration=0;
                        if (lstdata.get(position).getRepeat_mode().equals("1") || lstdata.get(position).getRepeat_mode().equals("0")) //days
                        {
                            duration=days;
                            holder.txt_duration.setText(String.valueOf(duration));
                            if(duration>1)
                            {
                                holder.txt_duration_type.setText("Days");
                            }
                            else
                            {
                                holder.txt_duration_type.setText("Day");
                            }

                        }
                        else if(lstdata.get(position).getRepeat_mode().equals("2"))
                        {
                            duration=days/7;
                            holder.txt_duration.setText(String.valueOf(duration));
                            if(duration>1)
                            {
                                holder.txt_duration_type.setText("Weeks");
                            }
                            else
                            {
                                holder.txt_duration_type.setText("Week");
                            }
                        }
                        else if(lstdata.get(position).getRepeat_mode().equals("3"))
                        {
                            duration=days/30;
                            holder.txt_duration.setText(String.valueOf(duration));
                            if(duration>1)
                            {
                                holder.txt_duration_type.setText("Months");
                            }
                            else
                            {
                                holder.txt_duration_type.setText("Month");
                            }
                        }
                        else if(lstdata.get(position).getRepeat_mode().equals("4"))
                        {
                            duration=days/365;

                            holder.txt_duration.setText(String.valueOf(duration));
                            if(duration>1)
                            {
                                holder.txt_duration_type.setText("Years");
                            }
                            else
                            {
                                holder.txt_duration_type.setText("Year");
                            }

                        }
                    }
                }
                row.setTag(holder);
	        }     
	        else
	        {  
	            holder = (FileDataHolder)row.getTag();
	        }
	        
	        String data = lstdata.get(position).getName();
	        holder.txt_name.setText(data);
            holder.txt_morning.setText(lstdata.get(position).getMorning_count());
            holder.txt_noon.setText(lstdata.get(position).getAfternoon_count());
            holder.txt_night.setText(lstdata.get(position).getNight_count());
	      
	        return row;
	    }



	    static class FileDataHolder
	    {
	        TextView txt_name,txt_morning,txt1,txt_noon,txt2,txt_night,txt_start_date,txt_duration,txt_duration_type;
	    }
	    
	    
	}

