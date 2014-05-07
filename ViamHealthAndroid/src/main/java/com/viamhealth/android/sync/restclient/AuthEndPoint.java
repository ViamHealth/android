package com.viamhealth.android.sync.restclient;

import android.content.Context;
import android.util.Log;

import com.viamhealth.android.Global_Application;
import com.viamhealth.android.dao.restclient.core.RestClient;
import com.viamhealth.android.dao.restclient.old.RequestMethod;
import com.viamhealth.android.model.BaseModel;
import com.viamhealth.android.model.users.User;
import com.viamhealth.android.provider.parsers.JsonParser;
import com.viamhealth.android.utils.LogUtils;

import org.apache.http.HttpStatus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

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

    @Override
    protected JsonParser getJsonParser() {
        return null;
    }

    @Override
    protected List<BaseModel> newList() {
        return new ArrayList<BaseModel>();
    }

    @Override
    protected void addParams(RestClient client, BaseModel model) {

    }

    @Override
    protected Params getQueryParams() {
        return null;
    }

    @Override
    protected String[] getPathSegments() {
        return new String[0];
    }
}
