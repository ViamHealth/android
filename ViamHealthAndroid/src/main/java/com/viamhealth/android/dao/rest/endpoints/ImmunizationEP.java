package com.viamhealth.android.dao.rest.endpoints;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.actionbarsherlock.R;
import com.viamhealth.android.dao.restclient.core.RestClient;
import com.viamhealth.android.dao.restclient.old.RequestMethod;
import com.viamhealth.android.model.immunization.Immunization;
import com.viamhealth.android.model.immunization.UserImmunization;

import org.apache.http.HttpStatus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by kunal on 23/2/14.
 */
public class ImmunizationEP extends BaseEP {

    private static String TAG = "ImmunizationEP";
    private static String API_RESOURCE = "user-immunizations";

    public ImmunizationEP(Context context, Application app) {
        super(context, app);
    }

    public List<Immunization> list(long userId){
        Params params = new Params();
        params.put("user",String.valueOf(userId));
        RestClient client = getRestClient(API_RESOURCE, params);
        try {
            client.Execute(RequestMethod.GET);
            String responseString = client.getResponse();
            //Log.i(TAG, client.toString());

            if(client.getResponseCode()== HttpStatus.SC_OK)
                return processImmunizationList(responseString);
        }catch (Exception e) {
            e.printStackTrace();
        }

        return new ArrayList<Immunization>();
    }

    private RestClient addParams(RestClient client, UserImmunization obj){
        client.AddParam("user", obj.getUserId().toString());
        client.AddParam("immunization", obj.getImmunization().toString());
        client.AddParam("is_completed", String.valueOf(obj.isCompleted()));
        return client;
    }

    public UserImmunization create(UserImmunization obj){
        if(obj == null  ){
            throw new IllegalArgumentException();
        }
        Params params = new Params();
        RestClient client = getRestClient(API_RESOURCE, params);
        try {
            client = addParams(client, obj);
        } catch (Exception e){
            e.printStackTrace();
            throw new IllegalArgumentException();
        }
        try {
            client.Execute(RequestMethod.POST);
            String responseString = client.getResponse();
            //Log.i(TAG, client.toString());

            if(client.getResponseCode()== HttpStatus.SC_CREATED){
                try{
                    JSONObject response = new JSONObject(responseString);
                    return processUserImmunizationObject(response);
                } catch(JSONException e){
                    e.printStackTrace();
                    throw new RuntimeException(e);
                }
            }

        }catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return null;
    }


    public UserImmunization update(UserImmunization obj){
        if(obj == null || obj.getId() == null ){
            throw new IllegalArgumentException();
        }
        Params params = new Params();
        RestClient client = getRestClient(API_RESOURCE +"/"+ obj.getId() , params);
        try {
            client = addParams(client, obj);
        } catch (Exception e){
            e.printStackTrace();
            throw new IllegalArgumentException();
        }

        try {
            client.Execute(RequestMethod.PUT);
            String responseString = client.getResponse();
            //Log.i(TAG, client.toString());

            if(client.getResponseCode()== HttpStatus.SC_OK){
                try{
                    JSONObject response = new JSONObject(responseString);
                    return processUserImmunizationObject(response);
                } catch(JSONException e){
                    e.printStackTrace();
                    throw new RuntimeException(e);
                }
            }

        }catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return null;
    }

    private List<Immunization> processImmunizationList(String responseString){
        Map<Long, Immunization> immunizationMap= new HashMap<Long, Immunization>();

        List<Immunization> immunizations = new ArrayList<Immunization>();
        try{
            JSONObject response = new JSONObject(responseString);

            JSONArray array = response.getJSONArray("immunizations");
            for(int i=0; i<array.length(); i++){
                try{
                    Immunization obj = processImmunizationObject(array.getJSONObject(i));
                    immunizationMap.put(obj.getId(),obj);
                } catch(RuntimeException e){
                    e.printStackTrace();
                    continue;
                }
            }

            JSONArray uarr = response.getJSONArray("user_immunizations");
            for(int i=0; i<uarr.length(); i++){
                try{
                    UserImmunization obj = processUserImmunizationObject(uarr.getJSONObject(i));
                    Immunization immunization = immunizationMap.get(obj.getImmunization());
                    immunization.setUserImmunization(obj);
                    //immunizations.add(immunization);
                    immunizationMap.put(obj.getImmunization(),immunization);
                } catch(RuntimeException e){
                    e.printStackTrace();
                    continue;
                }
            }

        } catch (JSONException e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        for (Map.Entry<Long, Immunization> entry : immunizationMap.entrySet()){
            immunizations.add(entry.getValue());
        }

        return immunizations;

    }

    private UserImmunization processUserImmunizationObject(JSONObject jsonObject) {
        UserImmunization obj = new UserImmunization();
        try {
            obj.setId(jsonObject.getLong("id"));
            obj.setImmunization(jsonObject.getLong("immunization"));
            obj.setUserId(jsonObject.getLong("user"));
            obj.setCompleted(jsonObject.getBoolean("is_completed"));
        } catch (JSONException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return obj;
    }

    private Immunization processImmunizationObject(JSONObject jsonObject){
        Immunization obj = new Immunization();
        try{
            obj.setId(jsonObject.getLong("id"));
            obj.setLabel(jsonObject.getString("label"));
            obj.setRecommendedAge(jsonObject.getLong("recommended_age"));
        } catch (JSONException e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        return obj;
    }
}
