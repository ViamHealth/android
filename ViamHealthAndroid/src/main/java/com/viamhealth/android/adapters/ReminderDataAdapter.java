package com.viamhealth.android.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.viamhealth.android.R;
import com.viamhealth.android.model.enums.ReminderTime;
import com.viamhealth.android.model.enums.ReminderType;
import com.viamhealth.android.model.reminder.Action;
import com.viamhealth.android.model.reminder.ReminderReading;
import com.viamhealth.android.model.reminder.ReminderTimeData;
import com.viamhealth.android.utils.Checker;
import com.viamhealth.android.utils.DateUtils;

import java.util.Date;
import java.util.List;

/**
 * Created by naren on 24/11/13.
 */
public class ReminderDataAdapter extends MultiSelectionAdapter<ReminderReading> {

    Activity activity;
    int layoutResourceId;
    List<ReminderReading> readings = null;
    Typeface tf;
    Date currDate;

    final String TAG = "ReminderDataAdapter";

    public interface OnSaveReminderAction {
        public void OnSave(ReminderReading reading);
    }

    protected OnSaveReminderAction listener;

    public void setOnSaveReminderAction(OnSaveReminderAction listener){
        this.listener = listener;
    }

    protected void onSaveReminderAction(ReminderReading reading){
        if(this.listener!=null)
            listener.OnSave(reading);
    }

    public ReminderDataAdapter(Context context, int layoutResourceId, List<ReminderReading> readings, Date date) {
        super(context, readings);
        this.layoutResourceId = layoutResourceId;
        this.activity = (Activity) context;
        this.readings = readings;
        this.currDate = DateUtils.getToday(date);
    }

    public ReminderDataAdapter(Context context) {
        super(context);
        this.activity = (Activity) context;
    }

    private void medicineCheckViewInit(final ReminderTime time, final View row, final int position,
                                       final int checkResId, final int textResId, final int milliSecDiff) {

        final long currentDate = currDate.getTime();

        final Action action = readings.get(position).getAction(time);
        final ReminderTimeData data = readings.get(position).getReminder().getReminderTimeData(time);
        final FrameLayout check = (FrameLayout) row.findViewById(checkResId);
        final TextView count = (TextView) row.findViewById(textResId);
        if(data!=null && data.getCount()>0){
            if(action!=null && action.isCheck()){
                check.setBackground(activity.getResources().getDrawable(R.drawable.medicine_check_checked));
            }else{
                if(DateUtils.hasElapsed(new Date(currentDate + milliSecDiff))){
                    check.setBackground(activity.getResources().getDrawable(R.drawable.medicine_check_unchecked));
                }else{
                    check.setBackground(activity.getResources().getDrawable(R.drawable.medicine_check_amber));
                }
            }

            count.setText(data.getCount().toString());
            count.setTextColor(android.R.color.white);
        }else{
            //check.setVisibility(View.GONE);
            check.setBackground(activity.getResources().getDrawable(R.drawable.medicine_check_default));
            count.setText("0");
            count.setTextColor(android.R.color.black);
        }

        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(data!=null && data.getCount()>0){
                    if(!action.isCheck()){
                        action.setCheck(true);
                        check.setBackground(activity.getResources().getDrawable(R.drawable.medicine_check_checked));
                    }else{
                        action.setCheck(false);
                        if(DateUtils.hasElapsed(new Date(currentDate + milliSecDiff))){
                            check.setBackground(activity.getResources().getDrawable(R.drawable.medicine_check_unchecked));
                        }else{
                            check.setBackground(activity.getResources().getDrawable(R.drawable.medicine_check_amber));
                        }
                    }
                    count.setText(data.getCount().toString());
                    count.setTextColor(android.R.color.white);
                    readings.get(position).putAction(time, action);
                    onSaveReminderAction(readings.get(position));
                }else{
                    check.setBackground(activity.getResources().getDrawable(R.drawable.medicine_check_default));
                    count.setText("0");
                    count.setTextColor(android.R.color.black);
                }
            }
        });
    }

    private void completeCheckViewInit(final View row, final int position) {

        final long currentDate = currDate.getTime();

        final ReminderReading reading = readings.get(position);
        final FrameLayout check = (FrameLayout) row.findViewById(R.id.completeCheck);
        if(reading.isCompleteCheck()){
            check.setBackground(activity.getResources().getDrawable(R.drawable.medicine_check_checked));
        }else{
            if(DateUtils.hasElapsed(reading.getReadingDate())){
                check.setBackground(activity.getResources().getDrawable(R.drawable.medicine_check_unchecked));
            }else{
                check.setBackground(activity.getResources().getDrawable(R.drawable.medicine_check_amber));
            }
        }

        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!reading.isCompleteCheck()){
                    reading.setCompleteCheck(true);
                    check.setBackground(activity.getResources().getDrawable(R.drawable.medicine_check_checked));
                }else{
                    reading.setCompleteCheck(false);
                    if(DateUtils.hasElapsed(reading.getReadingDate())){
                        check.setBackground(activity.getResources().getDrawable(R.drawable.medicine_check_unchecked));
                    }else{
                        check.setBackground(activity.getResources().getDrawable(R.drawable.medicine_check_amber));
                    }
                }
                onSaveReminderAction(reading);
            }
        });
    }

    @Override
    public View getItemView(int position, View convertView, ViewGroup parent) {
        View row = convertView;

        Log.i(TAG, "creating row at " + position + " for date " + currDate);
        int mornTimeInMs = 9 * 60 * 60 * 1000;
        int noonTimeInMs = 15 * 60 * 60 * 1000;
        int nightTimeInMs = 21 * 60 * 60 * 1000;

        if(row == null){
            LayoutInflater inflater = activity.getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
        }

        LinearLayout medicineLayout = (LinearLayout) row.findViewById(R.id.medicineLayout);
        LinearLayout completeLayout = (LinearLayout) row.findViewById(R.id.completeLayout);

        TextView txtName = (TextView)row.findViewById(R.id.txtViewName);
        txtName.setText(readings.get(position).getReminder().getName());

        final ReminderType type = readings.get(position).getReminder().getType();
        if(type == ReminderType.Medicine){
            completeLayout.setVisibility(View.GONE);
            medicineLayout.setVisibility(View.VISIBLE);
            medicineCheckViewInit(ReminderTime.Morning, row, position, R.id.morningCheck, R.id.mornCount, mornTimeInMs);
            medicineCheckViewInit(ReminderTime.Noon, row, position, R.id.noonCheck, R.id.noonCount, noonTimeInMs);
            medicineCheckViewInit(ReminderTime.Night, row, position, R.id.nightCheck, R.id.nightCount, nightTimeInMs);
        }else{
            completeLayout.setVisibility(View.VISIBLE);
            medicineLayout.setVisibility(View.GONE);
            completeCheckViewInit(row, position);
        }

        return row;
    }

}
