package com.viamhealth.android.adapters;
 
import java.util.ArrayList;
import java.util.HashMap;

import com.viamhealth.android.R;
import com.viamhealth.android.ViamHealthPrefs;
import com.viamhealth.android.model.ViewHolder;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.ExpandableListAdapter;

public class FoodExpandableListAdapter extends BaseExpandableListAdapter implements ExpandableListAdapter {
	public Context context;
	CheckBox checkBox;
    private LayoutInflater vi;
    private HashMap<String, ArrayList<String>> data = new HashMap<String, ArrayList<String>>();
    int _objInt;
    public String[] mKeys;
    ViamHealthPrefs appPrefs;
    int width,height,w110,w50,w30,w35,w10,w15,h5,w18;
  
    private static final int GROUP_ITEM_RESOURCE = R.layout.group_item;
    private static final int CHILD_ITEM_RESOURCE = R.layout.child_item;
    public String []check_string_array;
    
    public void setPadding(View v){
    	 ViewHolder holder = new ViewHolder(v);
    	 
    	 height=Integer.parseInt(appPrefs.getSheight());
    	 width=Integer.parseInt(appPrefs.getSwidth());
    	 
    	 w110=(int)((width*34.37)/100);
    	 w50=(int)((width*15.63)/100);
    	 w30=(int)((width*9.37)/100);
    	 w35=(int)((width*10.94)/100);
    	 w10=(int)((width*3.13)/100);
    	 w15=(int)((width*4.69)/100);
    	 w18=(int)((width*5.63)/100);
    	 
    	 h5=(int)((height*1.042)/100);
    	 
    	 holder.main.setPadding(0, h5, 0, h5);
    	 
    	 holder.txt_grp_food_name.getLayoutParams().width=w110;
    	 holder.txt_calories.setPadding(w35, 0, 0, 0);
    	 
    	 holder.txt_grp_food_name.setPadding(w35, 0, 0, 0);
    	 holder.txt_calories.setPadding(w35, 0, 0, 0);
    	

    	
    }
    
    public FoodExpandableListAdapter(Context context, Activity activity, HashMap<String, ArrayList<String>> data) {
        this.data = data;
        this.context = context;
        vi = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mKeys = data.keySet().toArray(new String[data.size()]);
        _objInt = data.size(); 
        appPrefs=new ViamHealthPrefs(context);
      
    }
   
    public String getChild(int groupPosition, int childPosition) {
        return data.get(mKeys[groupPosition]).get(childPosition);
    }

    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    public int getChildrenCount(int groupPosition) {
        return data.get(mKeys[groupPosition]).size();
    }

    public View getChildView(final int groupPosition, final int childPosition, final boolean isLastChild, final View convertView, final ViewGroup parent) {
        View v = convertView;
        String child = getChild(groupPosition, childPosition);
       if (child != null) {
            v = vi.inflate(CHILD_ITEM_RESOURCE, null);
            ViewHolder holder = new ViewHolder(v);
            setPadding(v);
            holder.btnAdd1.setPadding(0, 0, w18, 0);
            holder.txt_grp_food_name.setText(child.trim());
            holder.btnAdd1.setOnClickListener(new OnClickListener() {
				
				public void onClick(View v) {
					// TODO Auto-generated method stub
				/*	Log.e("TAG","data is deleted " + data.get(mKeys[groupPosition]).get(childPosition).toString());
					//getChildView(groupPosition, childPosition, isLastChild, convertView, parent);
					data.get(mKeys[groupPosition]).remove(childPosition);
					notifyDataSetChanged();*/
				}
			});
           
        }
        return v;
    }

    public String getGroup(int groupPosition) {
        return "group-" + groupPosition;
    }

    public int getGroupCount() {
        return data.size();
    }


    public long getGroupId(int groupPosition) {
        return groupPosition;
    }


    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        View v = convertView;
        v = vi.inflate(GROUP_ITEM_RESOURCE, null);
        final ViewHolder holder = new ViewHolder(v);
        setPadding(v);
         
            holder.txt_grp_food_name.setText(mKeys[groupPosition].toString());
            if(groupPosition%2 == 0){
            	holder.main.setBackgroundColor(Color.rgb(235, 111, 99));
            }else{
            	holder.main.setBackgroundColor(Color.rgb(230, 127, 34));
            }
            holder.btnAdd.setPadding(0, 0, w15, 0);
            holder.btnAdd.setOnClickListener(new OnClickListener() {
				
				public void onClick(View v) {
					// TODO Auto-generated method stub
					appPrefs.setGrpfoodname(holder.txt_grp_food_name.getText().toString());
					Log.e("TAG","food name : " + appPrefs.getGrpfoodname());
					//Intent AddBreakfast = new Intent(activity, com.viamhealth.android.activities.AddBreakfast.class);
					//TabGroupActivity parentoption = (TabGroupActivity)activity;
					//parentoption.startChildActivity("AddBreakfast",AddBreakfast);
					
				}
			});
       
        return v;
    }

    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }


    public boolean hasStableIds() {
        return true;
    }
	
} 