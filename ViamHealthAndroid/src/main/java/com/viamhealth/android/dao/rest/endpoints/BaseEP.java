package com.viamhealth.android.dao.rest.endpoints;

import android.app.Application;
import android.content.Context;

import com.viamhealth.android.Global_Application;
import com.viamhealth.android.ViamHealthPrefs;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by naren on 03/10/13.
 */
public class BaseEP {
    protected Context context;
    protected Global_Application ga;
    protected ViamHealthPrefs appPrefs;

    public BaseEP(Context context, Application app) {
        this.context = context;
        this.ga = (Global_Application)app;
        this.appPrefs=new ViamHealthPrefs(context);
    }


}
