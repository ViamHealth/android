package com.viamhealth.android.dao.rest.endpoints;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.viamhealth.android.dao.restclient.core.RestClient;
import com.viamhealth.android.dao.restclient.old.RequestMethod;
import com.viamhealth.android.model.TaskData;

import org.apache.http.HttpStatus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kunal on 14/5/14.
 */
public class TaskEP extends BaseEP {
    private static String TAG = "TaskEP";
    private static String API_RESOURCE = "task-list";

    public TaskEP(Context context, Application app) {
        super(context, app);
    }

    public List<TaskData> list(long userId){
        List<TaskData> tasks = new ArrayList<TaskData>();
        Log.i("doInBackground--Object", "doInBackground--Object");
        TaskData td = new TaskData("1","Did you take medicine yesterday ?","Yes","No");
        tasks.add(td);
        TaskData td2 = new TaskData("2","Go for a walk today", "yes","no");
        tasks.add(td2);
        return tasks;
        /*
        Params params = new Params();
        params.put("user",String.valueOf(userId));
        RestClient client = getRestClient(API_RESOURCE, params);
        try {
            client.Execute(RequestMethod.GET);
            String responseString = client.getResponse();
            Log.i(TAG, client.toString());

            if(client.getResponseCode()== HttpStatus.SC_OK)
                return processTaskList(responseString);
        }catch (Exception e) {
            e.printStackTrace();
        }

        return new ArrayList<TaskData>();*/
    }

    public void selectChoice(String taskId, String choice){
        Params params = new Params();
        RestClient client = getRestClient(API_RESOURCE + "/" +taskId + "/update_choice/" + choice, params);
        try {
            client.Execute(RequestMethod.PUT);
            String responseString = client.getResponse();
            Log.i(TAG, client.toString());
            //if(client.getResponseCode()== HttpStatus.SC_OK)
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    private List<TaskData> processTaskList(String responseString){


        List<TaskData> tasks = new ArrayList<TaskData>();
        try{
            JSONObject response = new JSONObject(responseString);

            JSONArray array = response.getJSONArray("results");
            for(int i=0; i<array.length(); i++){
                try{
                    TaskData obj = processTaskObject(array.getJSONObject(i));
                    tasks.add(obj);
                } catch(RuntimeException e){
                    e.printStackTrace();
                    continue;
                }
            }
        } catch (JSONException e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        return tasks;

    }

    private TaskData processTaskObject(JSONObject jsonObject) {
        TaskData obj = new TaskData();
        try {
            obj.setId(jsonObject.getString("id"));
            obj.setMessage(jsonObject.getString("message"));
            obj.setLabel_choice_1(jsonObject.getString("label_choice_1"));
            obj.setLabel_choice_2(jsonObject.getString("label_choice_2"));
        } catch (JSONException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return obj;
    }

}
