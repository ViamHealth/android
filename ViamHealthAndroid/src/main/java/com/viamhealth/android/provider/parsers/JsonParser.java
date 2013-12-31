package com.viamhealth.android.provider.parsers;

import com.viamhealth.android.model.BaseModel;
import com.viamhealth.android.model.users.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by naren on 17/12/13.
 */
public abstract class JsonParser {

    /**
     * This method would return the BaseModel from the jsonObject
     * @param jsonObject
     * @return - BaseModel
     * @throws JSONException
     */
    protected abstract BaseModel parse(JSONObject jsonObject) throws JSONException;

    public BaseModel parseObject(String json) {
        BaseModel obj = null;
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

    /**
     * This is required to create the new instance of the List
     * @return
     */
    public abstract List<BaseModel> newList();

    public List<BaseModel> parseArray(String jsonArray) {
        List<BaseModel> objs = newList();

        try{
            JSONArray jsonObjs = new JSONArray(jsonArray);
            for(int i=0; i<jsonObjs.length(); i++){
                BaseModel obj = parse(jsonObjs.getJSONObject(i));
                objs.add(obj);
            }
        }catch (JSONException e){
            e.printStackTrace();
            objs.add(parseObject(jsonArray));
        }

        return objs;
    }
}
