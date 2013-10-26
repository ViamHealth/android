package com.viamhealth.android.adapters;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.facebook.widget.ProfilePictureView;
import com.viamhealth.android.model.users.FbFamily;
import com.viamhealth.android.model.users.User;

import com.viamhealth.android.R;

import java.util.List;

/**
 * Created by naren on 26/10/13.
 */
public class UsersMenuAdapter extends ArrayAdapter<User> {

    private final Context context;
    private final List<User> values;

    private static final String TAG = "UsersMenuAdapter";

    public UsersMenuAdapter(Context context, List<User> objects) {
        super(context, R.layout.menu_users_row, objects);
        this.context = context;
        this.values = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.menu_users_row, parent, false);
        ProfilePictureView profilePic = (ProfilePictureView) rowView.findViewById(R.id.menu_row_profile_pic);
        profilePic.setDefaultProfilePicture(BitmapFactory.decodeResource(null, R.drawable.ic_social_person));
        profilePic.setCropped(true);
        profilePic.setPresetSize(ProfilePictureView.SMALL);

        TextView relation = (TextView) rowView.findViewById(R.id.menu_row_proile_relation);
        TextView name = (TextView) rowView.findViewById(R.id.menu_row_profile_name);

        User user = values.get(position);

        name.setText(user.getName());
        //TODO show the relation too
        //relation.setText(values.get(position).getRelationship());
        relation.setVisibility(View.GONE);

        if(user.getProfile()!=null && user.getProfile().getFbProfileId()!=null)
            profilePic.setProfileId(user.getProfile().getFbProfileId());
        else
            profilePic.setVisibility(View.GONE);

        rowView.setTag(values.get(position).getId());
        Log.d(TAG, "Added " + values.get(position) + " at position " + position);

        return rowView;
    }
}
