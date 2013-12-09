package com.viamhealth.android.dao.rest.endpoints;

import android.app.Application;
import android.content.Context;

import com.viamhealth.android.Global_Application;
import com.viamhealth.android.ViamHealthPrefs;
import com.viamhealth.android.dao.restclient.core.RestClient;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by naren on 03/10/13.
 */
public abstract class BaseEP {
    protected Context context;
    protected Global_Application ga;
    protected ViamHealthPrefs appPrefs;

    public BaseEP(Context context, Application app) {
        this.context = context;
        this.ga = (Global_Application)app;
        this.appPrefs=new ViamHealthPrefs(context);
    }

    protected RestClient getRestClient(String url, Params params) {
        StringBuilder baseurlString = new StringBuilder(Global_Application.url)
                                        .append(url)
                                        .append("/");

        if(params!=null && !params.isEmpty()){
            baseurlString.append("?");
            int i=0;
            for (String key : params.keySet()){
                if(i>0) baseurlString.append("&");
                baseurlString.append(key)
                        .append("=")
                        .append(params.get(key));
                i++;
            }
        }

        RestClient client = new RestClient(baseurlString.toString());
        client.AddHeader("Authorization","Token " + appPrefs.getToken().toString());

        return client;
    }

    protected class Params extends HashMap<String, String> {

    }

}
