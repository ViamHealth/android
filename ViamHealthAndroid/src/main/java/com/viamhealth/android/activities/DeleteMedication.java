package com.viamhealth.android.activities;

import android.os.Bundle;
import android.view.Window;

import com.viamhealth.android.R;

/**
 * Created by Administrator on 10/3/13.
 */
public class DeleteMedication extends BaseActivity {
    public void onCreate(Bundle savedInstanceState)
    {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.delete_medication);
    }
}
