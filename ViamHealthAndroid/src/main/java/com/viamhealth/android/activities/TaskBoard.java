package com.viamhealth.android.activities;

import org.apache.cordova.Config;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.viamhealth.android.R;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by kunal on 12/2/14.
 */
public class TaskBoard extends Activity implements CordovaInterface {
    CordovaWebView cwv;
    private final ExecutorService threadPool = Executors.newCachedThreadPool();

    /* Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        cwv = (CordovaWebView) findViewById(R.id.tutorialView);
        Config.init(this);
        cwv.loadUrl(Config.getStartUrl());
    }

    @Override
    public void startActivityForResult(CordovaPlugin cordovaPlugin, Intent intent, int i) {

    }

    @Override
    public void setActivityResultCallback(CordovaPlugin cordovaPlugin) {

    }

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public Object onMessage(String s, Object o) {
        return null;
    }

    @Override
    public ExecutorService getThreadPool() {
        return threadPool;
    }
}
