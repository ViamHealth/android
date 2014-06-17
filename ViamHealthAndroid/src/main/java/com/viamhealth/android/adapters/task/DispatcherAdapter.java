package com.viamhealth.android.adapters.task;

/**
 * Created by Kunal on 6/6/14.
 */

import android.content.Context;
import android.support.v4.util.LongSparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.viamhealth.android.model.tasks.Task;
import com.viamhealth.android.model.tasks.TaskData;

public abstract class DispatcherAdapter extends BaseAdapter {
    private LongSparseArray<DelegateAdapter> mDelegateAdapterSparseArray;
    protected Context mContext;
    protected LayoutInflater mLayoutInflater;

    public DispatcherAdapter(Context context) {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);

        initDelegates();
    }

    /**
     * Init the delegates here, with reflection
     */
    private void initDelegates() {
        mDelegateAdapterSparseArray = new LongSparseArray<DelegateAdapter>();

        DelegateAdaptersAnnotation annotation = getClass().getAnnotation(DelegateAdaptersAnnotation.class);
        if (annotation != null) {
            Class[] clazzs = annotation.delegateAdapters();
            if (clazzs != null) {
                for (Class<?> clazz : clazzs) {
                    if (!DelegateAdapter.class.isAssignableFrom(clazz)) {
                        throw new RuntimeException("The class " + clazz.getName() + " is not a subclass of " + DelegateAdapter.class.getName());
                    }
                    DelegateAdapterType delegateAdapterType = clazz.getAnnotation(DelegateAdapterType.class);
                    if (delegateAdapterType == null) {
                        throw new RuntimeException("The class " + clazz.getName() + " should have the annotation DelegateAdapterField");
                    }

                    long itemtype = delegateAdapterType.itemType().value();

                    if (mDelegateAdapterSparseArray.get(itemtype) != null) {
                        throw new RuntimeException("The item type " + itemtype + " is already defined!");
                    }

                    DelegateAdapter adapter = null;
                    try {
                        adapter = (DelegateAdapter) clazz.newInstance();
                    } catch (Exception e) {
                        throw new RuntimeException("Error while instanciating " + clazz.getName() + " with default constructor: " + e.getMessage(), e);
                    }

                    mDelegateAdapterSparseArray.put(itemtype, adapter);
                }
            }
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        long itemtype = getItemViewType(position);
        DelegateAdapter delegateAdapter = mDelegateAdapterSparseArray.get(itemtype);
        if (delegateAdapter == null) {
            throw new RuntimeException("Unknown type " + itemtype + " called");
        }
        return mDelegateAdapterSparseArray.get(itemtype ).getView(mContext, position, convertView, parent, mLayoutInflater, (Task) getItem(position));
    }

    @Override
    public int getViewTypeCount() {
        return mDelegateAdapterSparseArray.size();
    }
}