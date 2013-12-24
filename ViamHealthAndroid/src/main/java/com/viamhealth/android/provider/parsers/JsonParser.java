package com.viamhealth.android.provider.parsers;

import com.viamhealth.android.model.users.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by naren on 17/12/13.
 */
public abstract class JsonParser<T> {
    protected abstract T parse(JSONObject jsonObject) throws JSONException;

    public T parseObject(String json) {
        T obj = null;
        try{
            if(json!=null)
            {
                JSONObject jsonObj = new JSONObject(json);
                obj = parse(jsonObj);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return obj;
    }

    public List<T> parseArray(String jsonArray) {
        List<T> objs = new ArrayList<T>();

        try{
            JSONArray jsonObjs = new JSONArray(jsonArray);
            for(int i=0; i<jsonObjs.length(); i++){
                T obj = parse(jsonObjs.getJSONObject(i));
                objs.add(obj);
            }
        }catch (JSONException e){
            e.printStackTrace();
        }

        return objs;
    }
}
