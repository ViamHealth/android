package com.viamhealth.android.adapters;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.facebook.widget.ProfilePictureView;
import com.viamhealth.android.R;
import com.viamhealth.android.model.FbFamily;

import java.util.List;

/**
 * Created by naren on 04/10/13.
 */
public class FamilyListAdapter extends ArrayAdapter<FbFamily> {

    private final Context context;
    private final List<FbFamily> values;

    private static final String TAG = "FamilyListAdapter";

    public FamilyListAdapter(Context context, List<FbFamily> objects) {
        super(context, R.layout.family_list_element, objects);
        this.context = context;
        this.values = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.family_list_element, parent, false);
        ProfilePictureView profilePic = (ProfilePictureView) rowView.findViewById(R.id.fam_profile_pic);
        profilePic.setDefaultProfilePicture(BitmapFactory.decodeResource(null, R.drawable.ic_social_person));
        profilePic.setCropped(true);
        profilePic.setPresetSize(ProfilePictureView.NORMAL);

        TextView relation = (TextView) rowView.findViewById(R.id.fam_proile_relation);
        TextView name = (TextView) rowView.findViewById(R.id.fam_profile_name);

        name.setText(values.get(position).getName());
        relation.setText(values.get(position).getRelationship());
        profilePic.setProfileId(values.get(position).getId());

        rowView.setTag(values.get(position).getId());
        Log.d(TAG, "Added " + values.get(position) + " at position "+ position);

        return rowView;
    }

}
