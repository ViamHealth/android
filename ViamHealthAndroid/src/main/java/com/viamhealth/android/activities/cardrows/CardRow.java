package com.viamhealth.android.activities.cardrows;

import android.view.View;

/**
 * Created by kunal on 10/1/14.
 */
public interface CardRow {
    public View getView(View convertView);
    public int getViewType();
}
