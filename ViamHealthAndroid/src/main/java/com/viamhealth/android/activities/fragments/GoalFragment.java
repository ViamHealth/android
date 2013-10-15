package com.viamhealth.android.activities.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;

import com.viamhealth.android.R;
import com.viamhealth.android.activities.AddGoalActivity;
import com.viamhealth.android.model.users.User;

/**
 * Created by naren on 07/10/13.
 */
public class GoalFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab_fragment_goal, container, false);

        final User user = getArguments().getParcelable("user");
        TextView btnAddGoal = (TextView) view.findViewById(R.id.btn_add_goal);
        btnAddGoal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), AddGoalActivity.class);
                i.putExtra("user", user);
                startActivity(i);
            }
        });

        WebView webView = (WebView) view.findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl("file:///android_asset/weightgoal.html");

        return view;
    }


}
