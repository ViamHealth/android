package com.viamhealth.android.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.viamhealth.android.R;
import com.viamhealth.android.dao.rest.endpoints.FileUploader;
import com.viamhealth.android.model.FileData;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class FileDataAdapter extends MultiSelectionAdapter<FileData> {
    Context context;
    int layoutResourceId;
    List<FileData> lstdata = null;
    Typeface tf;


    public FileDataAdapter(Context context, int layoutResourceId, List<FileData> lstdata) {
        super(context, lstdata);
        // TODO Auto-generated constructor stub

        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.lstdata = lstdata;
    }

    public FileDataAdapter(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    public View getItemView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        FileDataHolder holder = null;
        tf = Typeface.createFromAsset(context.getAssets(), "Aparajita.ttf");

        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new FileDataHolder();
            holder.main_list_layout = (LinearLayout) row.findViewById(R.id.main_list_layout);
            holder.img_icon = (ImageView) row.findViewById(R.id.img_icon);
            holder.desc_layout = (LinearLayout) row.findViewById(R.id.desc_layout);
            holder.img_name = (TextView) row.findViewById(R.id.img_name);
            holder.img_desc = (TextView) row.findViewById(R.id.img_desc);
            row.setTag(holder);
        } else {
            holder = (FileDataHolder) row.getTag();
        }

        FileData data = lstdata.get(position);
        holder.img_name.setText(data.getName().toString());
        Calendar cal = Calendar.getInstance();
        cal.setTime(data.getUpdatedOn());
        holder.img_icon.setImageDrawable(context.getResources().getDrawable(FileUploader.getFileIcon(data.getMimeType())));
        holder.img_desc.setText(new StringBuilder()
                .append("Uploaded by ")
                .append(data.getUpdatedByName())
                .append(" on ")
                .append(cal.get(Calendar.DATE)).append("-")
                .append(cal.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.US)).append("-")
                .append(cal.get(Calendar.YEAR)));

        return row;
    }

    static class FileDataHolder {
        TextView img_name, img_desc, img_lbl, img_date;
        LinearLayout main_list_layout, desc_layout, end_layout;
        ImageView img_icon;
    }
}

