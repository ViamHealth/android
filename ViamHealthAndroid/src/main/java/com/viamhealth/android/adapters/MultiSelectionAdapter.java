package com.viamhealth.android.adapters;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.viamhealth.android.R;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by naren on 27/10/13.
 */
public abstract class MultiSelectionAdapter<T> extends BaseAdapter {

    private LayoutInflater inflater;
    private List<T> items;
    private SparseArray<View> views = new SparseArray<View>();


    // all our checked indexes go here
    private HashSet<Integer> checkedItems;

    // multi selection mode flag
    private boolean multiMode;
    private OnItemToggledListener onItemToggledListener;

    public MultiSelectionAdapter(Context context, List<T> items){
        this.inflater = LayoutInflater.from(context);
        this.items = items;
        this.checkedItems = new HashSet<Integer>();
    }

    public MultiSelectionAdapter(Context context){
        this(context, new ArrayList<T>());
    }

    public void setOnItemToggledListener(OnItemToggledListener onItemToggledListener) {
        this.onItemToggledListener = onItemToggledListener;
    }

    public void enterMultiMode(){
        this.multiMode = true;
        this.notifyDataSetChanged();
    }

    public void exitMultiMode() {
        this.checkedItems.clear();
        this.multiMode = false;
        this.notifyDataSetChanged();
    }

    private void setBackground(int pos, boolean selected){
//        int version = android.os.Build.VERSION.SDK_INT;
//        View convertView = views.get(pos);
//        if(version < 11){
//            int value = selected ? 1 : -1;
//            value = value * android.R.attr.state_checked;
//            convertView.getBackground().setState(
//                    new int[] { android.R.attr.state_checked });
//        }else{
//            convertView.setActivated(selected);
//        }
    }

    public void setChecked(int pos, boolean checked)
    {

        this.setBackground(pos, checked);
        if (checked) {
            this.checkedItems.add(Integer.valueOf(pos));
        } else {
            this.checkedItems.remove(Integer.valueOf(pos));
        }
        if(this.onItemToggledListener!=null){
            this.onItemToggledListener.onItemToggled(items.get(pos), checked, this.multiMode);
        }
        if (this.multiMode) {
            this.notifyDataSetChanged();
        }
    }

    public boolean isChecked(int pos)
    {
        return this.checkedItems.contains(Integer.valueOf(pos));
    }

    public void toggleChecked(int pos)
    {
        final Integer v = Integer.valueOf(pos);
        boolean checked = true;
        if (this.checkedItems.contains(v)) {
            this.checkedItems.remove(v);
            checked = false;
        } else {
            this.checkedItems.add(v);
            checked = true;
        }
        this.setBackground(pos, checked);
        if(this.onItemToggledListener!=null){
            this.onItemToggledListener.onItemToggled(items.get(pos), checked, this.multiMode);
        }
        this.notifyDataSetChanged();
    }

    public int getCheckedItemCount() {
        return this.checkedItems.size();
    }

    // we use this convinience method for rename thingie.
    public T getFirstCheckedItem() {
        for (Integer i : this.checkedItems) {
            return this.items.get(i.intValue());
        }
        return null;
    }

    public Set<Integer> getCheckedItems()
    {
        return this.checkedItems;
    }

    public void clear() {
        this.items.clear();
    }

    public void updateData(T[] data) {
        if(this.onItemToggledListener!=null){
            for(Integer ci : this.checkedItems){
                this.onItemToggledListener.onItemToggled(items.get(ci), false, this.multiMode);
            }
        }
        this.checkedItems.clear();
        for (int i = 0; i < data.length; i++) {
            this.items.add(data[i]);
        }
        this.notifyDataSetChanged();
    }

    public void updateArray(List<T> data)
    {
       this.items=data;
       this.notifyDataSetChanged();
    }

    @Override
    public int getCount()
    {
        return this.items.size();
    }


    @Override
    public T getItem(int position)
    {
        return this.items.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public boolean hasStableIds()
    {
        return true;
    }

    public abstract View getItemView(int position, View convertView, ViewGroup parent);

    @Override
    public final View getView(int position, View convertView, ViewGroup parent)
    {
        convertView = getItemView(position, convertView, parent);

        // the 4 state change problem described above. We use a second selector with no pressed state color if in multi mode
        convertView.setBackgroundResource(this.multiMode ?
                R.drawable.selector_list_multimode : R.drawable.selector_list);

        int version = android.os.Build.VERSION.SDK_INT;
        if(version < 11){
            if (checkedItems.contains(Integer.valueOf(position))) {
                // if this item is checked - set checked state
                convertView.getBackground().setState(
                        new int[] { android.R.attr.state_checked });
            } else {
                // if this item is unchecked - set unchecked state (notice the minus)
                convertView.getBackground().setState(
                        new int[] { -android.R.attr.state_checked });
            }
        }else{
            if (checkedItems.contains(Integer.valueOf(position))) {
                convertView.setActivated(true);
            } else {
                convertView.setActivated(false);
            }
        }


        //views.put(position, convertView);
        return convertView;
    }


    public interface OnItemToggledListener {
        public void onItemToggled(Object item, boolean isChecked, boolean isMultiMode);
    }

}