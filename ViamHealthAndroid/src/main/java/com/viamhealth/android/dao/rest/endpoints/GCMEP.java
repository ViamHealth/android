package com.viamhealth.android.dao.rest.endpoints;

import android.app.Application;
import android.content.Context;

import com.viamhealth.android.Global_Application;
import com.viamhealth.android.ViamHealthPrefs;
import com.viamhealth.android.dao.restclient.core.RestClient;
import com.viamhealth.android.dao.restclient.old.RequestMethod;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by shawn on 18/5/14.
 */
public class GCMEP {

    protected Context context;
    protected Global_Application ga;
    protected ViamHealthPrefs appPrefs;


    public GCMEP(Context context, Application app) {
        this.context = context;
        this.ga = (Global_Application) app;
        this.appPrefs = new ViamHealthPrefs(context);
    }

    public void register(String device_id, String registration_id) {
        StringBuilder baseurlString = new StringBuilder(Global_Application.url)
                .append("gcm/v1/device/register")
                .append("/");

        try {
            JSONObject payload = new JSONObject();
            payload.put("dev_id", device_id);
            payload.put("reg_id", registration_id);
            RestClient client = new RestClient(baseurlString.toString());
            ;
            //client.AddHeader("Authorization","ApiKey " + ga.getLoggedInUser().getUsername()+":"+appPrefs.getToken().toString());
            client.AddHeader("Authorization", "ApiKey " + ga.getLoggedInUser().getUsername() + ":1");
            client.setJSONString(payload.toString());
            try {
                client.Execute(RequestMethod.POST);
                String responseString = client.getResponse();
                //Log.i("GCPEP", client.toString());
                //Log.i("GCPEP", responseString);
            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (JSONException e) {
            e.printStackTrace();
            return;
        }


    }

    protected class Params extends HashMap<String, String> {

    }
}
