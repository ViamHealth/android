package com.viamhealth.android.adapters;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.facebook.widget.ProfilePictureView;
import com.viamhealth.android.R;
import com.viamhealth.android.model.ChallengeData;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Kunal on 4/7/14.
 */
public class ChallengeDayValuesAdapter extends ArrayAdapter<String> {
    private final Context context;
    private final List<String> values;

    private static final String TAG = "FamilyListAdapter";

    public ChallengeDayValuesAdapter(Context context, List<String> objects) {
        super(context, R.layout.challenge_day_values_element, objects);
        this.context = context;
        this.values = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.challenge_day_values_element, parent, false);
        TextView data = (TextView) rowView.findViewById(R.id.text_data);
        ToggleButton toggleButton = (ToggleButton) rowView.findViewById(R.id.toggleButton);
        String dayVal = values.get(position).toString();
        String valText = "";
        int countVals = values.size();
        if(position == countVals -1 ){
            valText = "Today";
        }
        else if(position == countVals -2 ){
            valText = "Yesterday";
        }
        else {
            Date DBDByesterday = new Date(System.currentTimeMillis() - 24 * position * 60 * 60 * 1000L);
            SimpleDateFormat dateformatMMMdd = new SimpleDateFormat("MMM, dd");
            valText = dateformatMMMdd.format(DBDByesterday);
        }

        try{
            data.setText(valText);
            System.out.println(dayVal);
            if(dayVal.equalsIgnoreCase("false")|| dayVal.equalsIgnoreCase("true") ){
                //toggleButton.setVisibility(View.VISIBLE);
                if(dayVal.equalsIgnoreCase("true")){
                    toggleButton.setChecked(true);
                }
            }
            rowView.setTag(position);
            Log.d(TAG, "Added " + values.get(position) + " at position " + position);
        }catch(Exception e){
            e.printStackTrace();
        }
        return rowView;
    }
}
