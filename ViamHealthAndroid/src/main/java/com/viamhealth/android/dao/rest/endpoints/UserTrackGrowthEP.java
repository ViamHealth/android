package com.viamhealth.android.dao.rest.endpoints;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.viamhealth.android.dao.restclient.core.RestClient;
import com.viamhealth.android.dao.restclient.old.RequestMethod;
import com.viamhealth.android.model.immunization.Immunization;
import com.viamhealth.android.model.immunization.UserImmunization;
import com.viamhealth.android.model.trackgrowth.TrackGrowth;
import com.viamhealth.android.model.trackgrowth.TrackGrowthData;
import com.viamhealth.android.model.trackgrowth.UserTrackGrowthData;

import org.apache.http.HttpStatus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by kunal on 10/3/14.
 */
public class UserTrackGrowthEP extends BaseEP {
    private static String TAG = "UserTrackGrowthEP";
    private static String API_RESOURCE = "user-track-growth";

    public UserTrackGrowthEP(Context context, Application app) {
        super(context, app);
    }

    private RestClient addParams(RestClient client, UserTrackGrowthData obj){
        DateFormat df = new SimpleDateFormat("yyyy-mm-dd");
        client.AddParam("user", obj.getUserId().toString());
        client.AddParam("entry_date", df.format(obj.getEntryDate()));
        client.AddParam("height", obj.getHeight().toString());
        client.AddParam("weight", obj.getWeight().toString());
        return client;
    }


    public boolean update(UserTrackGrowthData obj){
        Params params = new Params();
        DateFormat df = new SimpleDateFormat("yyyy-mm-dd");
        RestClient client = getRestClient(API_RESOURCE + "/" + df.format(obj.getEntryDate()), params);
        client = addParams(client, obj);
        try {
            client.Execute(RequestMethod.PUT);
            String responseString = client.getResponse();
            Log.i(TAG, client.toString());

            if(client.getResponseCode()== HttpStatus.SC_CREATED){
            }

        }catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return true;
    }

    public Map<String,List<TrackGrowth>> list(long userId){

        Params params = new Params();
        params.put("user",String.valueOf(userId));
        RestClient client = getRestClient(API_RESOURCE, params);
        try {
            client.Execute(RequestMethod.GET);
            String responseString = client.getResponse();
            Log.i(TAG, client.toString());

            if(client.getResponseCode()== HttpStatus.SC_OK)
                return processListResponse(responseString);
        }catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return null;
    }

    private Map<String,List<TrackGrowth>>
        processListResponse(String responseString){

        Map<String, List<TrackGrowth>> data = new HashMap<String, List<TrackGrowth>>();
        List<TrackGrowth> trackGrowthDataList = new ArrayList<TrackGrowth>();
        List<TrackGrowth> userTrackGrowthList = new ArrayList<TrackGrowth>();

        try{
            JSONObject response = new JSONObject(responseString);
            JSONArray array = response.getJSONArray("track_growth");
            for(int i=0; i < array.length(); i++){
                try{
                    TrackGrowthData obj = processTrackGrowthDataObject(array.getJSONObject(i));
                    trackGrowthDataList.add(obj);
                } catch(RuntimeException e){
                    e.printStackTrace();
                    continue;
                }
            }

            JSONArray uarray = response.getJSONArray("user_track_growth");
            for(int i=0; i < uarray.length(); i++){
                try{
                    UserTrackGrowthData obj = processUserTrackGrowthDataObject(uarray.getJSONObject(i));
                    userTrackGrowthList.add(obj);
                } catch(RuntimeException e){
                    e.printStackTrace();
                    continue;
                }
            }

        } catch (JSONException e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        data.put("track_growth",  trackGrowthDataList);
        data.put("user_track_growth", userTrackGrowthList);

        return data;

    }

    private TrackGrowthData processTrackGrowthDataObject(JSONObject jsonObject){
        TrackGrowthData obj = new TrackGrowthData();
        try {
            obj.setId(jsonObject.getLong("id"));
            obj.setAge(jsonObject.getLong("age"));
            obj.setLabel(jsonObject.getString("label"));
            obj.setWeight(Float.valueOf(jsonObject.getString("weight")));
            obj.setHeight(Float.valueOf(jsonObject.getString("height")));
        } catch (JSONException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return obj;
    }


    private UserTrackGrowthData processUserTrackGrowthDataObject(JSONObject jsonObject){
        UserTrackGrowthData obj = new UserTrackGrowthData();
        try {
            obj.setId(jsonObject.getLong("id"));
            obj.setUserId(jsonObject.getLong("user"));
            obj.setAge(jsonObject.getLong("age"));
            obj.setEntryDate(Date.valueOf(jsonObject.getString("entry_date")));
            String weight = jsonObject.getString("weight");
            if(weight != null)
                obj.setWeight(Float.valueOf(weight));
            String height = jsonObject.getString("height");
            if(height != null)
                obj.setHeight(Float.valueOf(jsonObject.getString("height")));

        } catch (JSONException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return obj;
    }
}
