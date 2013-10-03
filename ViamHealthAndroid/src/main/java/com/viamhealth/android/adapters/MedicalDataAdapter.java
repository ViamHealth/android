package com.viamhealth.android.adapters;

import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;


import java.util.Calendar;
import java.util.Date;

import com.viamhealth.android.R;
import com.viamhealth.android.ViamHealthPrefs;
import com.viamhealth.android.model.MedicationData;

public class MedicalDataAdapter extends ArrayAdapter<MedicationData> {
	Context context; 
	int layoutResourceId;    
	ArrayList<MedicationData> lstdata = null;
	Typeface tf;
	ViamHealthPrefs appPrefs;
	int height,width,w30,w150,w10;
    int hour=0;
    ColorDrawable d1,d2;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    int pos=0;


    public MedicalDataAdapter(Context context, int layoutResourceId, ArrayList<MedicationData> lstdata) {
		super(context, layoutResourceId, lstdata);
		// TODO Auto-generated constructor stub
		
		 this.layoutResourceId = layoutResourceId;
	     this.context = context;
	     this.lstdata = lstdata;
         d1=new ColorDrawable();
	}
	 @Override
	    public View getView(int position, View convertView, ViewGroup parent) {
	        View row = convertView;
	        FileDataHolder holder = null;
            pos=position;
            appPrefs=new ViamHealthPrefs(context);
            DateFormat df = new SimpleDateFormat("MM-dd-yyyy");
            Date today = Calendar.getInstance().getTime();
            String reportDate = df.format(today);
            //String user=appPrefs.getUserid();
            pref = context.getSharedPreferences(appPrefs.firstname+reportDate, 0);
            editor = pref.edit();


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
                        d2 = (ColorDrawable)txt.getBackground();
                        Calendar rightNow = Calendar.getInstance();
                        hour=rightNow.get(Calendar.HOUR_OF_DAY);
                        if(hour > 9)
                        {
                            if(d2.getColor()==Color.GREEN)
                            {
                                txt.setBackgroundColor(Color.RED);
                                editor.putInt("M"+pos,Color.RED);

                            }
                            else
                            {
                                txt.setBackgroundColor(Color.GREEN);
                                editor.putInt("M"+pos,Color.GREEN);
                            }
                            editor.commit();
                        }
                        else
                        {
                            if(d2.getColor()==Color.GREEN)
                            {
                                txt.setBackgroundColor(Color.GRAY);
                                editor.putInt("M"+pos,Color.GRAY);
                            }
                            else
                            {
                                txt.setBackgroundColor(Color.GREEN);
                                editor.putInt("M"+pos,Color.GREEN);
                            }
                            editor.commit();
                        }
						//txt.setBackgroundColor(Color.GREEN);
                        //txt.setBackground(d1);
					}
				});
	            
	            holder.txt_noon.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						TextView txt2 = (TextView)v.findViewById(R.id.txt_noon);
                        Calendar rightNow = Calendar.getInstance();
                        hour=rightNow.get(Calendar.HOUR_OF_DAY);
                        d2 = (ColorDrawable)txt2.getBackground();
                        //txt2.setText(String.valueOf(hour));
                        if(hour > 15)
                        {
                            if(d2.getColor()==Color.GREEN)
                            {
                                txt2.setBackgroundColor(Color.RED);
                                editor.putInt("NN"+pos,Color.RED);
                            }
                            else
                            {
                                txt2.setBackgroundColor(Color.GREEN);
                                editor.putInt("NN"+pos,Color.GREEN);
                            }
                            editor.commit();
                        }
                        else
                        {
                            if(d2.getColor()==Color.GREEN)
                            {
                                txt2.setBackgroundColor(Color.GRAY);
                                editor.putInt("NN"+pos,Color.GRAY);
                            }
                            else
                            {
                                txt2.setBackgroundColor(Color.GREEN);
                                editor.putInt("NN"+pos,Color.GREEN);
                            }
                            editor.commit();
                        }
                        //txt.setBackgroundColor(Color.GREEN);
                       // txt2.setBackground(d1);
					}
				});
	            holder.txt_night.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						TextView txt3 = (TextView)v.findViewById(R.id.txt_night);
                        Calendar rightNow = Calendar.getInstance();
                        hour=rightNow.get(Calendar.HOUR_OF_DAY);
                        d2 = (ColorDrawable)txt3.getBackground();
                        if(hour > 21)
                        {
                            if(d2.getColor()==Color.GREEN)
                            {
                                txt3.setBackgroundColor(Color.RED);
                                editor.putInt("NT"+pos,Color.RED);
                            }
                            else
                            {
                                txt3.setBackgroundColor(Color.GREEN);
                                editor.putInt("NT"+pos,Color.GREEN);
                            }
                            editor.commit();
                        }
                        else
                        {
                            if(d2.getColor()==Color.GREEN)
                            {
                                txt3.setBackgroundColor(Color.GRAY);
                                editor.putInt("NT"+pos,Color.GRAY);
                            }
                            else
                            {
                                txt3.setBackgroundColor(Color.GREEN);
                                editor.putInt("NT"+pos,Color.GREEN);
                            }
                            editor.commit();
                        }
                        //txt.setBackgroundColor(Color.GREEN);
                        //txt3.setBackground(d1);
					}
				});
	            
	            
	            row.setTag(holder);
	        }     
	        else
	        {  
	            holder = (FileDataHolder)row.getTag();
	        }
	        
	        String data = lstdata.get(position).getName();
	        holder.txt_name.setText(data);

            Calendar rightNow = Calendar.getInstance();
            hour=rightNow.get(Calendar.HOUR_OF_DAY);


            int morning_color,noon_color,night_color;
            if(hour>9)
            {
                morning_color=Color.RED;
            }
            else
            {
                morning_color=Color.GRAY;
            }
            holder.txt_morning.setText(lstdata.get(position).getMorning_count());
            holder.txt_morning.setBackgroundColor(morning_color);
            //holder.txt_morning.setBackgroundColor(pref.getInt("M"+position,morning_color));
            editor.putInt("M"+position,pref.getInt("M"+position,morning_color));

            if(hour>15)
            {
               noon_color=Color.RED;
            }
            else
            {
               noon_color=Color.GRAY;
            }
            holder.txt_noon.setText(lstdata.get(position).getAfternoon_count());
            holder.txt_noon.setBackgroundColor(noon_color);
            //holder.txt_noon.setBackgroundColor(pref.getInt("NN"+position,noon_color));
            editor.putInt("NN"+position,pref.getInt("NN"+position,noon_color));

            if(hour>21)
            {
                night_color=Color.RED;
            }
            else
            {
                night_color=Color.GRAY;
            }
            holder.txt_night.setText(lstdata.get(position).getNight_count());
            holder.txt_night.setBackgroundColor(night_color);
            //holder.txt_night.setBackgroundColor(pref.getInt("NT"+position,night_color));
            editor.putInt("NT"+position,pref.getInt("NT"+position,night_color));
            editor.commit();



            //write code to read color values from shared preference and store it
	      
	        return row;
	    }
	    
	    static class FileDataHolder
	    {
	        TextView txt_name,txt_morning,txt1,txt_noon,txt2,txt_night;
	      
	        
	    }
	    
	    
	}

