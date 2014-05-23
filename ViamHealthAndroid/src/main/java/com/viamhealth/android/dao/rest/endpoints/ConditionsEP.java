package com.viamhealth.android.dao.rest.endpoints;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.viamhealth.android.dao.restclient.core.RestClient;
import com.viamhealth.android.dao.restclient.old.RequestMethod;
import com.viamhealth.android.model.users.User;

/**
 * Created by shawn on 22/5/14.
 */
public class ConditionsEP extends BaseEP {

    private static String TAG = "ConditionsEP";
    private static String API_RESOURCE = "user_conditions";
    public ConditionsEP(Context context, Application app) {
        super(context, app);
    }

    public void postConditions(String list_conditions, User user){
        Params params = new Params();
        params.put("user", user.getId().toString());
        RestClient client = getRestClient(API_RESOURCE , params);
        client.AddParam("list_conditions", list_conditions);
        client.AddParam("user", user.getId().toString());
        try {
            client.Execute(RequestMethod.POST);
            //String responseString = client.getResponse();
            Log.i(TAG, client.toString());
            //if(client.getResponseCode()== HttpStatus.SC_OK)
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

}
