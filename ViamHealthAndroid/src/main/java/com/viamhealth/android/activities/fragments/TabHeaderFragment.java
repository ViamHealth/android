package com.viamhealth.android.activities.fragments;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.widget.ProfilePictureView;
import com.viamhealth.android.R;
import com.viamhealth.android.activities.Home;
import com.viamhealth.android.model.users.User;

/**
 * Created by naren on 07/10/13.
 */
public class TabHeaderFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.tab_header_fragment, container, false);

        User user = getArguments().getParcelable("user");

        Typeface tf = Typeface.createFromAsset(getActivity().getAssets(),"Roboto-Condensed.ttf");
        TextView userNameTV = (TextView) view.findViewById(R.id.header_user_name);
        userNameTV.setText(user.getName());
        userNameTV.setTypeface(tf);

        ProfilePictureView profilePic = (ProfilePictureView) view.findViewById(R.id.header_user_icon);
        profilePic.setProfileId(user.getProfile().getFbProfileId());
        profilePic.setPresetSize(ProfilePictureView.SMALL);
        profilePic.setCropped(true);

        ImageView logo = (ImageView) view.findViewById(R.id.header_logo);
        logo.setMinimumHeight(300);
        logo.setMinimumWidth(300);
        logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Home.class);
                startActivity(intent);
            }
        });


        return view;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
