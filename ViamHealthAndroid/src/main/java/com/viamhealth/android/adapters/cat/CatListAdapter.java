package com.viamhealth.android.adapters.cat;

import android.content.Context;
import android.view.LayoutInflater;


import com.viamhealth.android.model.cat.CatData;
import com.viamhealth.android.adapters.cat.ChallengeDelegateAdapter;

import java.util.List;

@DelegateAdaptersAnnotation(delegateAdapters = {
        ChallengeDelegateAdapter.class,
        HealthStatsCatDelegateAdapter.class
        //AddAdapterHere.class
        //Increase count in getViewTypeCount
})
public class CatListAdapter extends DispatcherAdapter {

    private List<CatData> mData;

    public CatListAdapter(Context context) {
        super(context);
    }

    public void setListData(List<CatData> tasks) {
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
        return mData.get(i).getCatAdapterType() ;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

}
