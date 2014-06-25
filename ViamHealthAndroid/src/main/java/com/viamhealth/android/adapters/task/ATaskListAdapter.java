package com.viamhealth.android.adapters.task;

import android.content.Context;
import android.view.LayoutInflater;

import com.viamhealth.android.model.TaskData;

import java.util.List;

@DelegateAdaptersAnnotation(delegateAdapters = {
        SimpleTextDelegateAdapter.class,
        BloodPressureDelegateAdapter.class
        //AddAdapterHere.class
})
public class ATaskListAdapter extends DispatcherAdapter {

    private LayoutInflater mLayoutInflater;
    private List<TaskData> mData;

    public ATaskListAdapter(Context context) {
        super(context);
    }

    public void setListData(List<TaskData> tasks) {
        this.mData = tasks;
    }

    @Override
    public int getCount() {
        return mData == null ? 0 : mData.size();
    }

    @Override
    public Object getItem(int i) {
        return mData.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public int getItemViewType(int i) {
        return mData.get(i).getTaskType() - 1;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

}
