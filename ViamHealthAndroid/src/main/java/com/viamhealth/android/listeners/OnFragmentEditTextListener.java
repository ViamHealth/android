package com.viamhealth.android.listeners;

import android.view.View;
import android.widget.EditText;

/**
 * Created by naren on 10/10/13.
 */
public interface OnFragmentEditTextListener extends EditText.OnFocusChangeListener {

    @Override
    void onFocusChange(View v, boolean hasFocus);

    void onDateSelected(int year, int monthOfYear, int dayOfMonth);
}
