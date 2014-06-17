package com.viamhealth.android.adapters.task;

import android.content.Context;
import android.view.LayoutInflater;

import com.viamhealth.android.model.tasks.Task;
import com.viamhealth.android.model.tasks.TaskData;

import java.util.List;

@DelegateAdaptersAnnotation(delegateAdapters = {
        SimpleTextDelegateAdapter.class,
        BloodPressureDelegateAdapter.class,
        ChallengeDelegateAdapter.class
        //AddAdapterHere.class
        //Increase count in getViewTypeCount
})
public class ATaskListAdapter extends DispatcherAdapter {

    private LayoutInflater mLayoutInflater;
    private List<Task> mData;

    public ATaskListAdapter(Context context) {
        super(context);
    }

    public void setListData(List<Task> tasks) {
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
        return mData.get(i).getAdapterType() ;
    }

    @Override
    public int getViewTypeCount() {
        return 3;
    }

}
