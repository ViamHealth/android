package com.viamhealth.android.adapters.task;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.viamhealth.android.model.tasks.Task;

/**
 * Created by Kunal on 6/6/14.
 */
public interface DelegateAdapter {
    public View getView(
            Context context,
            int position,
            View convertView,
            ViewGroup parent,
            LayoutInflater inflater,
            Task item);
}
