package com.viamhealth.android.activities.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.ConsoleMessage;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ImageButton;

import com.viamhealth.android.R;
import com.viamhealth.android.activities.AddGoalValue;
import com.viamhealth.android.model.enums.MedicalConditions;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by naren on 18/10/13.
 */
public class GraphFragment extends Fragment implements GoalFragment.OnGoalDataChangeListener {

    private static final String TAG = "GraphFragment";

    public interface OnClickAddValueListener {
        public void onClick(View view);
    }
    
    View view;
    WebView webView;
    ImageButton addValue;

    String json = "";
    OnClickAddValueListener listener;

    MedicalConditions type;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_graph, container, false);

        type = (MedicalConditions) getArguments().getSerializable("type");
        json = getArguments().getString("json");

        webView = (WebView) view.findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.addJavascriptInterface(this, "goals");
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
                Log.i("GraphWebView", consoleMessage.message());
                return super.onConsoleMessage(consoleMessage);
            }
        });
        webView.loadUrl("file:///android_asset/" + type.assetName());


        addValue = (ImageButton) view.findViewById(R.id.addvalue);
        addValue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener != null){
                    listener.onClick(v);
                }
            }
        });

        return view;
    }

    public MedicalConditions getType() {
        return type;
    }

    @Override
    public void onChange(String json) {
        this.json = json;
        Log.i(TAG, this.json);
        //reload the webView
        webView.loadUrl( "javascript:window.location.reload( true )" );
    }

    @Override
    public void onAdd(String jsonReading) {
        Log.i(TAG, jsonReading);
        String url = "javascript:addPoint('" + jsonReading + "')";
        Log.i(TAG, url);
        webView.loadUrl(url);
    }

    public String getData() {
        return this.json;
    }

    public void setOnClickAddValueListener(OnClickAddValueListener listener) {
        this.listener = listener;
    }

}
