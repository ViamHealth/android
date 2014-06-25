package com.viamhealth.android.dao.rest.endpoints;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.viamhealth.android.dao.restclient.core.RestClient;
import com.viamhealth.android.dao.restclient.old.RequestMethod;
import com.viamhealth.android.model.ChallengeData;
import com.viamhealth.android.model.enums.TaskItemType;
import com.viamhealth.android.model.tasks.BloodPressureTask;
import com.viamhealth.android.model.tasks.Task;
import com.viamhealth.android.model.tasks.TaskData;

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
    private static String API_RESOURCE = "tasks";

    public TaskEP(Context context, Application app) {
        super(context, app);
    }

    public List<Task> list(long userId) {
        Params params = new Params();
        params.put("user", String.valueOf(userId));
        RestClient client = getRestClient(API_RESOURCE, params);
        try {
            client.Execute(RequestMethod.GET);
            String responseString = client.getResponse();
            Log.i(TAG, responseString);

            if (client.getResponseCode() == HttpStatus.SC_OK)
                return processTaskList(responseString);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ArrayList<Task>();
    }

    public void selectChoice(String taskId, String choice) {
        Params params = new Params();
        RestClient client = getRestClient(API_RESOURCE + "/" + taskId + "/set_choice", params);
        client.AddParam("set_choice", choice);
        try {
            client.Execute(RequestMethod.POST);
            //String responseString = client.getResponse();
            //Log.i(TAG, client.toString());
            //if(client.getResponseCode()== HttpStatus.SC_OK)
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean acceptChallenge(String taskId) {
        Params params = new Params();
        RestClient client = getRestClient(API_RESOURCE + "/" + taskId + "/accept_challenge", params);

        try {
            client.Execute(RequestMethod.POST);
            if(client.getResponseCode()== HttpStatus.SC_NO_CONTENT){
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void addBPData(String sBp, String dBp, String pulseRate, String user_id) {
        Params params = new Params();
        RestClient client = getRestClient(API_RESOURCE  + "/set_blood_pressure", params);
        client.AddParam("systolic_pressure", sBp);
        client.AddParam("diastolic_pressure", dBp);
        client.AddParam("pulse_rate", pulseRate);
        client.AddParam("user", user_id);

        try {
            client.Execute(RequestMethod.POST);
            //String responseString = client.getResponse();
            //Log.i(TAG, client.toString());
            //if(client.getResponseCode()== HttpStatus.SC_OK)
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addBPData(String taskId, String sBp, String dBp, String pulseRate, String user_id) {
        Params params = new Params();
        RestClient client = getRestClient(API_RESOURCE + "/" + taskId + "/set_blood_pressure", params);
        client.AddParam("systolic_pressure", sBp);
        client.AddParam("diastolic_pressure", dBp);
        client.AddParam("pulse_rate", pulseRate);
        client.AddParam("user", user_id);

        try {
            client.Execute(RequestMethod.POST);
            //String responseString = client.getResponse();
            //Log.i(TAG, client.toString());
            //if(client.getResponseCode()== HttpStatus.SC_OK)
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private List<Task> processTaskList(String responseString) {


        List<Task> tasks = new ArrayList<Task>();
        try {
            JSONObject response = new JSONObject(responseString);

            JSONArray array = response.getJSONArray("results");
            for (int i = 0; i < array.length(); i++) {
                try {
                    Task obj;
                    JSONObject jsonObject = array.getJSONObject(i);
                    int taskType = jsonObject.getInt("task_type");
                    if(taskType == TaskItemType.Challenge.value()) {
                        obj = processChallengeTaskObject(jsonObject);
                    } else if(taskType == TaskItemType.BloodPressure.value()){
                        obj = processBloodPressureTaskObject(jsonObject);
                    } else {
                         obj = processTaskObject(jsonObject);
                    }
                    tasks.add(obj);
                } catch (RuntimeException e) {
                    e.printStackTrace();
                    continue;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        /*ChallengeData tt = new ChallengeData();
        tt.setId("12322134");
        tt.setLabelChoice1("Accept");
        tt.setMessage("This is good for you");
       tt.setTitle("5 day walk challenge");
        tt.setBigMessage(" you have accepted the challenege");
        tt.setDayNum(0);
        tt.setDayWiseValues(new ArrayList<String>());
        tt.setJoinedCount(22);
        tt.setNumDays(5);
        tt.setWeight(999);
        tasks.add(tt);*/
        return tasks;

    }

    private TaskData processTaskObject(JSONObject jsonObject) {
        TaskData obj = new TaskData();
        try {
            obj.setId(jsonObject.getString("id"));
            obj.setMessage(jsonObject.getString("message"));
            obj.setLabelChoice1(jsonObject.getString("label_choice_1"));
            obj.setLabelChoice2(jsonObject.getString("label_choice_2"));
            obj.setFeedbackMessageChoice1(jsonObject.getString("choice_1_message"));
            obj.setFeedbackMessageChoice2(jsonObject.getString("choice_2_message"));
            obj.setSetChoice(jsonObject.getInt("set_choice"));
            obj.setWeight(jsonObject.getInt("weight"));
            //obj.setTaskType(jsonObject.getInt("task_type"));
        } catch (JSONException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return obj;
    }

    private BloodPressureTask processBloodPressureTaskObject(JSONObject jsonObject) {
        BloodPressureTask obj = new BloodPressureTask();
        try {
            obj.setId(jsonObject.getString("id"));
            obj.setMessage(jsonObject.getString("message"));
            obj.setLabelChoice1(jsonObject.getString("label_choice_1"));
            obj.setLabelChoice2(jsonObject.getString("label_choice_2"));
            obj.setFeedbackMessageChoice1(jsonObject.getString("choice_1_message"));
            obj.setFeedbackMessageChoice2(jsonObject.getString("choice_2_message"));
            obj.setSetChoice(jsonObject.getInt("set_choice"));
            obj.setWeight(jsonObject.getInt("weight"));
            //obj.setTaskType(jsonObject.getInt("task_type"));
        } catch (JSONException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return obj;
    }

    private Task processChallengeTaskObject(JSONObject jsonObject) {
        ChallengeData obj = new ChallengeData();
        try {
            obj.setId(jsonObject.getString("id"));
            obj.setMessage(jsonObject.getString("message"));
            obj.setLabelChoice1(jsonObject.getString("label_choice_1"));
            obj.setWeight(jsonObject.getInt("weight"));
        } catch (JSONException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return obj;
    }

}
