package com.viamhealth.android.activities.fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.EditText;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by naren on 10/10/13.
 */
public class DatePickerFragment extends BaseDialogFragment implements DatePickerDialog.OnDateSetListener {

    SimpleDateFormat formater = new SimpleDateFormat("dd/MM/yyyy");
    private EditText mEditText;
    private OnDateChangeListener mListener;

    public DatePickerFragment(EditText editText, OnDateChangeListener listener) {
        mEditText = editText;
        mListener = listener;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        try {
            Date date = formater.parse(mEditText.getText().toString());
            c.setTime(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        mEditText.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
        if (mListener != null) {
            mListener.OnDateChange();
        }
    }

    public interface OnDateChangeListener {
        public void OnDateChange();
    }

}
