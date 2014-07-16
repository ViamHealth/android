package com.viamhealth.android.dao.rest.endpoints;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.viamhealth.android.dao.restclient.core.RestClient;
import com.viamhealth.android.dao.restclient.old.RequestMethod;
import com.viamhealth.android.model.ChallengeData;
import com.viamhealth.android.model.cat.CatData;
import com.viamhealth.android.model.cat.HealthStatsCatData;
import com.viamhealth.android.model.enums.CatType;
import com.viamhealth.android.model.healthreadings.BloodPressureReading;
import com.viamhealth.android.model.healthreadings.WeightReading;


import org.apache.http.HttpStatus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Kunal on 18/6/14.
 */
public class CatEP extends BaseEP {
    private static String TAG = "CatEP";
    private static String API_RESOURCE = "cat";

    public CatEP(Context context, Application app) {
        super(context, app);
    }

    public List<CatData> list(long userId) {
        return processCatList("");
        /*Params params = new Params();
        params.put("user", String.valueOf(userId));
        RestClient client = getRestClient(API_RESOURCE, params);
        try {
            client.Execute(RequestMethod.GET);
            String responseString = client.getResponse();
            Log.i(TAG, responseString);

            if (client.getResponseCode() == HttpStatus.SC_OK)
                return processCatList(responseString);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ArrayList<CatData>();*/
    }

    private List<CatData> processCatList(String responseString) {
        List<CatData> tasks = new ArrayList<CatData>();
        if(responseString != "") {
            try {
                JSONObject response = new JSONObject(responseString);

                JSONArray array = response.getJSONArray("results");
                for (int i = 0; i < array.length(); i++) {
                    try {
                        CatData obj;
                        JSONObject jsonObject = array.getJSONObject(i);
                        int taskType = jsonObject.getInt("cat_type");
                        if (taskType == CatType.Challlenge.value()) {
                            obj = processChallengeCatObject(jsonObject);
                        } else if (taskType == CatType.HealthStats.value()) {
                            continue;
                            //obj = processHealthStatsCatObject(jsonObject);
                        } else {
                            continue;
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
        }
        Date yesterday = new Date(System.currentTimeMillis() - 24 * 60 * 60 * 1000L);
        Date DByesterday = new Date(System.currentTimeMillis() - 48 * 60 * 60 * 1000L);
        Date DBDByesterday = new Date(System.currentTimeMillis() - 72 * 60 * 60 * 1000L);
        ChallengeData tt = new ChallengeData();
        tt.setId("12322134");
        tt.setLabelChoice1("Accept");
        tt.setMessage("This is good for you");
        tt.setTitle("5 day walk challenge");
        tt.setBigMessage(" you have accepted the challenege");
        tt.setDayNum(0);
        tt.setDayValueString("Steps");
        tt.setDayValueType(2);
        List<String> dwv = new ArrayList<String>();
        dwv.add("false");
        dwv.add("true");
        dwv.add("false");
        dwv.add("true");
        dwv.add("false");
        tt.setDayWiseValues(dwv);
        tt.setJoinedCount(22);
        tt.setNumDays(5);
        tt.setWeight(999);
        tt.setAcceptedDate(Calendar.getInstance().getTime());
        tasks.add(tt);

        BloodPressureReading bp = new BloodPressureReading();
        bp.setId(123234L);
        bp.setReadingDate(Calendar.getInstance().getTime());
        bp.setDiastolicPressure(23);
        bp.setSystolicPressure(35);
        bp.setPulseRate(57);
        WeightReading wr = new WeightReading();
        wr.setId(12323L);
        wr.setReadingDate(Calendar.getInstance().getTime());
        wr.setWeight(50.0);
        HealthStatsCatData hh = new HealthStatsCatData();
        hh.setReadingDate(Calendar.getInstance().getTime());
        hh.setBloodPressureReading(bp);
        hh.setWeightReading(wr);
        tasks.add(hh);

        /*ChallengeData aa = new ChallengeData();
        aa.setId("12322134");
        aa.setLabelChoice1("Accept");
        aa.setMessage("This is good for you");
        aa.setTitle("drink water");
        aa.setBigMessage(" Are you drinking water ? ");
        aa.setDayNum(2);
        aa.setDayWiseValues(new ArrayList<String>());
        aa.setJoinedCount(202);
        aa.setNumDays(10);
        aa.setWeight(99);
        aa.setAcceptedDate(yesterday);
        tasks.add(aa);


        WeightReading wr1 = new WeightReading();
        wr1.setId(12323L);
        wr1.setReadingDate(Calendar.getInstance().getTime());
        wr1.setWeight(80.0);
        HealthStatsCatData hh1 = new HealthStatsCatData();
        hh1.setReadingDate(DByesterday);
        //hh1.setBloodPressureReading(bp);
        hh1.setWeightReading(wr);
        tasks.add(hh1);

        WeightReading wr2 = new WeightReading();
        wr2.setId(12323L);
        wr2.setReadingDate(Calendar.getInstance().getTime());
        wr2.setWeight(80.0);
        HealthStatsCatData hh2 = new HealthStatsCatData();
        hh2.setReadingDate(DBDByesterday);
        //hh1.setBloodPressureReading(bp);
        hh2.setWeightReading(wr);
        tasks.add(hh2);*/

        return tasks;
    }

    private CatData processChallengeCatObject(JSONObject jsonObject) {
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
