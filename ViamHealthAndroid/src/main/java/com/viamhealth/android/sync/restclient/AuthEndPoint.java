package com.viamhealth.android.sync.restclient;

import android.content.Context;
import android.util.Log;

import com.viamhealth.android.Global_Application;
import com.viamhealth.android.dao.restclient.core.RestClient;
import com.viamhealth.android.dao.restclient.old.RequestMethod;
import com.viamhealth.android.model.users.User;
import com.viamhealth.android.utils.LogUtils;

import org.apache.http.HttpStatus;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by naren on 17/12/13.
 */
public class AuthEndPoint extends BaseEndPoint {

    public AuthEndPoint(Context context) {
        super(context);
    }

    public enum AuthType {
        Email,
        Mobile,
        FB,
        UserName;
    }

    public String login(String key, String password, AuthType type){
        RestClient client = getAuthenticatedRestClient(null, false, Paths.GET_TOKEN);

        switch(type){
            case FB:
                client.AddParam("access_token", key);
                break;

            case Email:
                client.AddParam("email",key);
                client.AddParam("password",password);
                break;

            case Mobile:
                client.AddParam("mobile",key);
                client.AddParam("password",password);
                break;

            case UserName:
                client.AddParam("username",key);
                client.AddParam("password",password);
                break;

        }

        try{
            client.Execute(RequestMethod.POST);
        } catch (Exception e) {
            e.printStackTrace();
        }

        String responseString = client.getResponse();
        LogUtils.LOGD(TAG, "login:" + client.toString());
        User user = null;
        try {
            JSONObject jObject = new JSONObject(responseString);
            //TODO::implement proper error handling
            String	token = jObject.getString("token");
            if(token.length()>0){
                LogUtils.LOGD(TAG,"token is " + token);
                return token;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            LogUtils.LOGW(TAG, e.getMessage(), e.getCause());
        }

        return null;
    }

    public boolean logout() {

        /* this will be the first rest call */
        RestClient client = getRestClient(null, "logout");

        try{
            client.Execute(RequestMethod.GET);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Log.i(TAG, client.toString());

        if(client.getResponseCode()== HttpStatus.SC_OK ||
                client.getResponseCode()==HttpStatus.SC_NO_CONTENT)
            return true;

        return false;
    }

}
