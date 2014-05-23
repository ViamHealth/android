package com.viamhealth.android.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.viamhealth.android.R;
import com.viamhealth.android.model.ConditionData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by KUnal on 22/5/14.
 */
public class ConditionListAdapter extends BaseAdapter implements View.OnClickListener {

    /** The inflator used to inflate the XML layout */
    private LayoutInflater inflator;

    /** A list containing some sample data to show. */
    public List dataList;

    public ConditionListAdapter(LayoutInflater inflator) {
        super();
        this.inflator = inflator;

        dataList = new ArrayList();

        dataList.add(new ConditionData("Blood Pressure", false));
        dataList.add(new ConditionData("Cholesterol", false));
        dataList.add(new ConditionData("Weight", false));
        dataList.add(new ConditionData("Blood Sugar", false));

    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public Object getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {

        // We only create the view if its needed
        if (view == null) {
            view = inflator.inflate(R.layout.condition_list_item, null);

            // Set the click listener for the checkbox
            view.findViewById(R.id.checkBox1).setOnClickListener(this);
        }

        ConditionData data = (ConditionData) getItem(position);

        // Set the example text and the state of the checkbox
        CheckBox cb = (CheckBox) view.findViewById(R.id.checkBox1);
        cb.setChecked(data.isSelected());
        // We tag the data object to retrieve it on the click listener.
        cb.setTag(data);

        TextView tv = (TextView) view.findViewById(R.id.textView1);
        tv.setText(data.getName());

        return view;
    }

    @Override
    /** Will be called when a checkbox has been clicked. */
    public void onClick(View view) {
        ConditionData data = (ConditionData) view.getTag();
        data.setSelected(((CheckBox) view).isChecked());
    }
}
