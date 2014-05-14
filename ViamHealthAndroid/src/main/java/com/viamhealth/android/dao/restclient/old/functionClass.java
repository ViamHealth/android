package com.viamhealth.android.dao.restclient.old;

import android.content.Context;
import android.util.Log;

import com.viamhealth.android.Global_Application;
import com.viamhealth.android.ViamHealthPrefs;
import com.viamhealth.android.dao.restclient.core.RestClient;
import com.viamhealth.android.model.FileData;
import com.viamhealth.android.model.MedicalData;
import com.viamhealth.android.model.MedicationData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class functionClass {
    Context context;
    JSONObject jObject;
    JSONArray jarray;
    String responseString = null;
    Global_Application ga;
    ViamHealthPrefs appPrefs;

    public functionClass(Context context) {
        super();
        this.context = context;
        appPrefs = new ViamHealthPrefs(context);

    }


    public String uploadFile(String description, String file) {
        String responce = "1";
        String baseurlString = Global_Application.url + "healthfiles/";
        RestClient client = new RestClient(baseurlString);
        client.AddHeader("Authorization", "Token " + appPrefs.getToken().toString());
        client.AddParam("description", description);
        client.AddParam("file", file);

        try {
            client.Execute(RequestMethod.POST);
        } catch (Exception e) {
            e.printStackTrace();
        }

        responseString = client.getResponse();
        Log.i("TAG", "Response : " + responseString);
        return responce;
    }


    // function for get file data
    public ArrayList<FileData> getFile(Long userId, String searchString) {
        String baseurlString = Global_Application.url + "healthfiles/?page_size=100&user=" + userId;
        if (searchString != null && !searchString.isEmpty())
            baseurlString += "&search=" + searchString;
        ArrayList<FileData> lstData = new ArrayList<FileData>();
        Log.i("TAG", "url is : " + baseurlString);

        RestClient client = new RestClient(baseurlString);
        client.AddHeader("Authorization", "Token " + appPrefs.getToken().toString());


        try {
            client.Execute(RequestMethod.GET);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (client.getResponseCode() > 400) {
            return null;
        }
        responseString = client.getResponse();
        Log.i("TAG", "Response string " + responseString);

        try {
            jObject = new JSONObject(responseString);
            Log.i("TAG", "res : " + responseString);
            Log.i("TAG", jObject.getString("next"));
            Global_Application.nextfile = jObject.getString("next");
            jarray = jObject.getJSONArray("results");
            for (int i = 0; i < jarray.length(); i++) {
                JSONObject c = jarray.getJSONObject(i);

                FileData data = new FileData(c.getString("id").toString(), c.getString("user"),
                        c.getString("name"), c.getString("description"),
                        c.getString("download_url"), c.getString("mime_type"));
                data.setUpdatedBy(c.getLong("updated_by"));
                data.setUpdatedByName(c.getString("updated_by_name"));
                data.setUpdatedOn(c.getLong("updated_at") * 1000);
                lstData.add(data);
            }

            Log.i("TAG", "lstdata count is " + lstData.size());

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return lstData;
    }

    // function for get file data
    public String FileDelete(String baseurlString) {
        Log.i("TAG", "url is : " + baseurlString);

        RestClient client = new RestClient(baseurlString);
        client.AddHeader("Authorization", "Token " + appPrefs.getToken().toString());


        try {
            client.Execute(RequestMethod.DELETE);
        } catch (Exception e) {
            e.printStackTrace();
        }

        responseString = client.getResponse();
        Log.i("TAG", "response string delete  : " + baseurlString);

        return "0";
    }


    // function for add medicates data
    public ArrayList<MedicalData> addMedical(String name, String detail, String start_timestamp,
                                             String repeat_hour, String repeat_day, String repeat_mode, String repeat_min,
                                             String repeat_weekday, String repeat_day_interval) {
        ArrayList<MedicalData> lstData = new ArrayList<MedicalData>();
        String baseurlString = Global_Application.url + "medicaltests/?user=" + appPrefs.getUserid();
        Log.i("TAG", "url is : " + baseurlString);

        RestClient client = new RestClient(baseurlString);
        client.AddHeader("Authorization", "Token " + appPrefs.getToken().toString());
        client.AddParam("name", name.toString());
        client.AddParam("details", detail.toString());
        client.AddParam("start_timestamp", start_timestamp.toString());
        client.AddParam("repeat_hour", repeat_hour.toString());
        client.AddParam("repeat_day", repeat_day.toString());
        client.AddParam("repeat_mode", repeat_mode.toString());
        client.AddParam("repeat_min", repeat_min.toString());
        client.AddParam("repeat_weekday", repeat_weekday.toString());
        client.AddParam("repeat_day_interval", repeat_day_interval.toString());

        try {
            client.Execute(RequestMethod.POST);
        } catch (Exception e) {
            e.printStackTrace();
        }

        responseString = client.getResponse();
        Log.i("TAG", "Response string " + responseString);

        try {
            jObject = new JSONObject(responseString);
            Log.i("TAG", "res : " + responseString);
            Log.i("TAG", jObject.getString("next"));
            jarray = jObject.getJSONArray("results");
            for (int i = 0; i < jarray.length(); i++) {
                JSONObject c = jarray.getJSONObject(i);
                lstData.add(new MedicalData(c.getString("id").toString(),
                        c.getString("name").toString(),
                        c.getString("details").toString(),
                        c.getString("start_timestamp").toString(),
                        c.getString("repeat_hour").toString(),
                        c.getString("repeat_day").toString(),
                        c.getString("repeat_mode").toString(),
                        c.getString("repeat_min").toString(),
                        c.getString("repeat_weekday").toString(),
                        c.getString("repeat_day_interval").toString()));

            }

            Log.i("TAG", "lstdata count is " + lstData.size());

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return lstData;
    }


    // function for add medicates data
    public ArrayList<MedicalData> getMedical() {
        ArrayList<MedicalData> lstData = new ArrayList<MedicalData>();
        String baseurlString = Global_Application.url + "medicaltests/?user=" + appPrefs.getUserid();
        Log.i("TAG", "url is : " + baseurlString);

        RestClient client = new RestClient(baseurlString);
        client.AddHeader("Authorization", "Token " + appPrefs.getToken().toString());

        try {
            client.Execute(RequestMethod.GET);
        } catch (Exception e) {
            e.printStackTrace();
        }

        responseString = client.getResponse();
        Log.i("TAG", "Response string " + responseString);

        try {
            jObject = new JSONObject(responseString);
            Log.i("TAG", "res : " + responseString);
            Log.i("TAG", jObject.getString("next"));
            Global_Application.nextmedical = jObject.getString("next");
            jarray = jObject.getJSONArray("results");
            Log.i("TAG", "json array size : " + jarray.length());
            for (int i = 0; i < jarray.length(); i++) {
                Log.i("TAG", "inside for loop");
                JSONObject c = jarray.getJSONObject(i);
                Log.i("TAG", "name is : " + c.getString("name"));
                lstData.add(new MedicalData(c.getString("id").toString(),
                        c.getString("name").toString(),
                        c.getString("details").toString(),
                        c.getString("start_timestamp").toString(),
                        c.getString("repeat_hour").toString(),
                        c.getString("repeat_day").toString(),
                        c.getString("repeat_mode").toString(),
                        c.getString("repeat_min").toString(),
                        c.getString("repeat_weekday").toString(),
                        c.getString("repeat_day_interval").toString()));

            }

            Log.i("TAG", "lstdata count is " + lstData.size());

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Log.i("TAG", "SIZE : " + lstData.size());
        return lstData;
    }

    // function for add medicates data
    public ArrayList<MedicalData> getMedical(String baseurlString) {
        ArrayList<MedicalData> lstData = new ArrayList<MedicalData>();
        Log.i("TAG", "url is : " + baseurlString);

        RestClient client = new RestClient(baseurlString);
        client.AddHeader("Authorization", "Token " + appPrefs.getToken().toString());

        try {
            client.Execute(RequestMethod.GET);
        } catch (Exception e) {
            e.printStackTrace();
        }

        responseString = client.getResponse();
        Log.i("TAG", "Response string " + responseString);

        try {
            jObject = new JSONObject(responseString);
            Log.i("TAG", "res : " + responseString);
            Log.i("TAG", jObject.getString("next"));
            Global_Application.nextmedical = jObject.getString("next");
            jarray = jObject.getJSONArray("results");
            for (int i = 0; i < jarray.length(); i++) {
                JSONObject c = jarray.getJSONObject(i);
                lstData.add(new MedicalData(c.getString("id").toString(),
                        c.getString("name").toString(),
                        c.getString("detail").toString(),
                        c.getString("start_timestamp").toString(),
                        c.getString("repeat_hour").toString(),
                        c.getString("repeat_day").toString(),
                        c.getString("repeat_mode").toString(),
                        c.getString("repeat_min").toString(),
                        c.getString("repeat_weekday").toString(),
                        c.getString("repeat_day_interval").toString()));

            }

            Log.i("TAG", "lstdata count is " + lstData.size());

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return lstData;
    }

    // function for add medicates data
    public ArrayList<MedicalData> addMedication(String name, String id, String type, String detail, String morning, String afternoon,
                                                String evening, String night, String start_timestamp,
                                                String repeat_hour, String repeat_day, String repeat_mode, String repeat_min,
                                                String repeat_weekday, String repeat_day_interval, String repeat_every_x, String start_date, String end_date) {
        ArrayList<MedicalData> lstData = new ArrayList<MedicalData>();
        //String baseurlString = Global_Application.url+"medications/?user="+appPrefs.getUserid();
        String baseurlString = Global_Application.url + "reminders/" + "?user=" + id.toString(); //MJ:api add
        Log.i("TAG", "url is : " + baseurlString);

        RestClient client = new RestClient(baseurlString);
        client.AddHeader("Authorization", "Token " + appPrefs.getToken().toString());
        client.AddParam("type", type);//MJ
        client.AddParam("user", id.toString());//MJ
        client.AddParam("name", name.toString());
        client.AddParam("details", detail.toString());
        if (type.equalsIgnoreCase("2")) {
            client.AddParam("morning_count", morning.toString());
            client.AddParam("afternoon_count", afternoon.toString());
            client.AddParam("evening_count", evening.toString());
            client.AddParam("night_count", night.toString());
        }
        client.AddParam("start_timestamp", start_timestamp.toString());
        client.AddParam("repeat_hour", repeat_hour.toString());
        client.AddParam("repeat_day", repeat_day.toString());
        client.AddParam("repeat_mode", repeat_mode.toString());
        client.AddParam("repeat_min", repeat_min.toString());
        client.AddParam("repeat_weekday", repeat_weekday.toString());
        client.AddParam("repeat_day_interval", repeat_day_interval.toString());
        client.AddParam("repeat_every_x", repeat_every_x.toString());
        client.AddParam("start_date", start_date.toString());
        client.AddParam("end_date", end_date.toString());

        try {
            client.Execute(RequestMethod.POST);
        } catch (Exception e) {
            e.printStackTrace();
        }

        responseString = client.getResponse();
        Log.i("TAG", "Response string " + responseString);

        try {
            jObject = new JSONObject(responseString);
            Log.i("TAG", "res : " + responseString);
            if ((jarray = jObject.getJSONArray("results")) != null)  //MJ
            {
                for (int i = 0; i < jarray.length(); i++) {
                    JSONObject c = jarray.getJSONObject(i);
                    lstData.add(new MedicalData(c.getString("id").toString(),
                            c.getString("name").toString(),
                            c.getString("details").toString(),
                            c.getString("start_timestamp").toString(),
                            c.getString("repeat_hour").toString(),
                            c.getString("repeat_day").toString(),
                            c.getString("repeat_mode").toString(),
                            c.getString("repeat_min").toString(),
                            c.getString("repeat_weekday").toString(),
                            c.getString("repeat_day_interval").toString()));

                }
            }

            Log.i("TAG", "lstdata count is " + lstData.size());

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return lstData;
    }

    public void storeReminderReading(String reminder_id, String morning_check, String evening_check, String afternoon_check, String night_check, String complete_check, String user_id) {
        ArrayList<MedicalData> lstData = new ArrayList<MedicalData>();
        String baseurlString = Global_Application.url + "reminderreadings/" + "?user=" + user_id + "&" + "type=2" + "&reading_date=2013-10-21";
        //String baseurlString = Global_Application.url+"reminderreadings/"+reminder_id + "/?user="+user_id;
        Log.i("TAG", "url is : " + baseurlString);

        RestClient client = new RestClient(baseurlString);
        client.AddHeader("Authorization", "Token " + appPrefs.getToken().toString());
        //client.AddParam("morning_check", morning_check);//MJ
        //client.AddParam("afternoon_check", afternoon_check);//MJ
        //client.AddParam("evening_check",evening_check);
        //client.AddParam("night_check", night_check);
        //client.AddParam("complete_check",complete_check);

        try {
            client.Execute(RequestMethod.GET);
        } catch (Exception e) {
            e.printStackTrace();
        }

        responseString = client.getResponse();
        Log.i("TAG", "Response string " + responseString);

    }


    public ArrayList<MedicationData> getReminderInfo(String user_id, String remindertype) {
        ArrayList<MedicationData> lstData = new ArrayList<MedicationData>();
        //String baseurlString = Global_Application.url+"reminders/?user="+appPrefs.getUserid()+"&"+"type=MEDICATION";
        String baseurlString = Global_Application.url + "reminders/?user=" + user_id + "&" + "type=" + remindertype;

        Log.i("TAG", "url is : " + baseurlString);

        RestClient client = new RestClient(baseurlString);
        client.AddHeader("Authorization", "Token " + appPrefs.getToken().toString());

        try {
            client.Execute(RequestMethod.GET);
        } catch (Exception e) {
            e.printStackTrace();
        }

        responseString = client.getResponse();
        Log.i("TAG", "Response string " + responseString);

        try {
            jObject = new JSONObject(responseString);
            Log.i("TAG", "res : " + responseString);
            Log.i("TAG", jObject.getString("next"));
            jarray = jObject.getJSONArray("results");
            for (int i = 0; i < jarray.length(); i++) {
                JSONObject c = jarray.getJSONObject(i);
                lstData.add(new MedicationData(c.getString("id").toString(),
                        "id",
                        c.getString("name").toString(),
                        c.getString("type").toString(),
                        "null",//c.getString("detail").toString()
                        c.getString("morning_count").toString(),
                        c.getString("afternoon_count").toString(),
                        c.getString("evening_count").toString(),
                        c.getString("night_count").toString(),
                        c.getString("user").toString(),
                        "null",
                        "null",
                        "null",
                        c.getString("repeat_mode").toString(),
                        "null",
                        "null",
                        "null", c.getString("start_date").toString(), c.getString("end_date").toString(), false, false, false, false, false));

            }
            Log.i("TAG", "lstdata count is " + lstData.size());

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


        return lstData;

    }


    // function for add medicates data
    public ArrayList<MedicalData> getMedication() {
        ArrayList<MedicalData> lstData = new ArrayList<MedicalData>();
        String baseurlString = Global_Application.url + "medications/?user=" + appPrefs.getUserid();
        Log.i("TAG", "url is : " + baseurlString);

        RestClient client = new RestClient(baseurlString);
        client.AddHeader("Authorization", "Token " + appPrefs.getToken().toString());

        try {
            client.Execute(RequestMethod.GET);
        } catch (Exception e) {
            e.printStackTrace();
        }

        responseString = client.getResponse();
        Log.i("TAG", "Response string " + responseString);

        try {
            jObject = new JSONObject(responseString);
            Log.i("TAG", "res : " + responseString);
            Log.i("TAG", jObject.getString("next"));
            Global_Application.nextmedication = jObject.getString("next");
            jarray = jObject.getJSONArray("results");
            Log.i("TAG", "json array size : " + jarray.length());
            for (int i = 0; i < jarray.length(); i++) {
                Log.i("TAG", "inside for loop");
                JSONObject c = jarray.getJSONObject(i);
                Log.i("TAG", "name is : " + c.getString("name"));
                lstData.add(new MedicalData(c.getString("id").toString(),
                        c.getString("name").toString(),
                        c.getString("details").toString(),
                        c.getString("start_timestamp").toString(),
                        c.getString("repeat_hour").toString(),
                        c.getString("repeat_day").toString(),
                        c.getString("repeat_mode").toString(),
                        c.getString("repeat_min").toString(),
                        c.getString("repeat_weekday").toString(),
                        c.getString("repeat_day_interval").toString()));

            }

            Log.i("TAG", "lstdata count is " + lstData.size());

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Log.i("TAG", "SIZE : " + lstData.size());
        return lstData;
    }

    // function for add medicates data
    public ArrayList<MedicalData> getMedication(String baseurlString) {
        ArrayList<MedicalData> lstData = new ArrayList<MedicalData>();
        Log.i("TAG", "url is : " + baseurlString);

        RestClient client = new RestClient(baseurlString);
        client.AddHeader("Authorization", "Token " + appPrefs.getToken().toString());

        try {
            client.Execute(RequestMethod.GET);
        } catch (Exception e) {
            e.printStackTrace();
        }

        responseString = client.getResponse();
        Log.i("TAG", "Response string " + responseString);

        try {
            jObject = new JSONObject(responseString);
            Log.i("TAG", "res : " + responseString);
            Log.i("TAG", jObject.getString("next"));
            Global_Application.nextmedication = jObject.getString("next");
            jarray = jObject.getJSONArray("results");
            for (int i = 0; i < jarray.length(); i++) {
                JSONObject c = jarray.getJSONObject(i);
                lstData.add(new MedicalData(c.getString("id").toString(),
                        c.getString("name").toString(),
                        c.getString("detail").toString(),
                        c.getString("start_timestamp").toString(),
                        c.getString("repeat_hour").toString(),
                        c.getString("repeat_day").toString(),
                        c.getString("repeat_mode").toString(),
                        c.getString("repeat_min").toString(),
                        c.getString("repeat_weekday").toString(),
                        c.getString("repeat_day_interval").toString()));

            }

            Log.i("TAG", "lstdata count is " + lstData.size());

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return lstData;
    }

    //delete medical
    public String DeleteMedical(String baseurlString) {
        Log.i("TAG", "url for delete" + baseurlString);
        RestClient client = new RestClient(baseurlString);
        client.AddHeader("Authorization", "Token " + appPrefs.getToken().toString());

        try {
            client.Execute(RequestMethod.DELETE);
        } catch (Exception e) {
            e.printStackTrace();
        }

        responseString = client.getResponse();

        return responseString;
    }

    //delete medication
    public String DeleteMedication(String baseurlString) {
        Log.i("TAG", "url for delete" + baseurlString);
        RestClient client = new RestClient(baseurlString);
        client.AddHeader("Authorization", "Token " + appPrefs.getToken().toString());

        try {
            client.Execute(RequestMethod.DELETE);
        } catch (Exception e) {
            e.printStackTrace();
        }

        responseString = client.getResponse();
        Log.i("TAG", "Response string " + responseString);

        return responseString;
    }


    // function for add medicates data
    public MedicationData getMedicationByID(String id) {
        String baseurlString = Global_Application.url + "medications/" + id;
        MedicationData medicationdt = new MedicationData();
        Log.i("TAG", "url is : " + baseurlString);

        RestClient client = new RestClient(baseurlString);
        client.AddHeader("Authorization", "Token " + appPrefs.getToken().toString());

        try {
            client.Execute(RequestMethod.GET);
        } catch (Exception e) {
            e.printStackTrace();
        }

        responseString = client.getResponse();
        Log.i("TAG", "Response string " + responseString);

        try {
            jObject = new JSONObject(responseString);
            Log.i("TAG", "res : " + responseString);
            medicationdt.setId(jObject.getString("id").toString());
            medicationdt.setName(jObject.getString("name").toString());
            medicationdt.setDetails(jObject.getString("details").toString());
            medicationdt.setMorning_count(jObject.getString("morning_count").toString());
            medicationdt.setAfternoon_count(jObject.getString("afternoon_count").toString());
            medicationdt.setEvening_count(jObject.getString("evening_count").toString());
            medicationdt.setNight_count(jObject.getString("night_count").toString());
            medicationdt.setUser(jObject.getString("user").toString());
            medicationdt.setStart_timestamp(jObject.getString("start_timestamp").toString());
            medicationdt.setRepeat_mode(jObject.getString("repeat_mode").toString());
            medicationdt.setRepeat_day(jObject.getString("repeat_day").toString());
            medicationdt.setRepeat_hour(jObject.getString("repeat_hour").toString());
            medicationdt.setRepeat_min(jObject.getString("repeat_min").toString());
            medicationdt.setRepeat_weekday(jObject.getString("repeat_weekday").toString());
            medicationdt.setRepeat_day_interval(jObject.getString("repeat_day_interval").toString());
            medicationdt.setStart_date(jObject.getString("start_date").toString());


        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return medicationdt;
    }


    // function for add medicates data
    public ArrayList<MedicalData> UpdateMedication(String id, String user_id, String type, String name, String detail, String morning, String afternoon,
                                                   String evening, String night, String start_timestamp,
                                                   String repeat_hour, String repeat_day, String repeat_mode, String repeat_min,
                                                   String repeat_weekday, String repeat_day_interval, String start_date, String end_date) {
        ArrayList<MedicalData> lstData = new ArrayList<MedicalData>();
        String baseurlString = Global_Application.url + "reminders/" + id + "/" + "?type=" + type;  //?user="+user_id;
        Log.i("TAG", "url is : " + baseurlString);

        RestClient client = new RestClient(baseurlString);
        client.AddHeader("Authorization", "Token " + appPrefs.getToken().toString());
        client.AddParam("user", user_id.toString());
        client.AddParam("name", name.toString());
        client.AddParam("details", detail.toString());
        client.AddParam("type", type);
        if (type.equalsIgnoreCase("2")) {
            client.AddParam("morning_count", morning.toString());
            client.AddParam("afternoon_count", afternoon.toString());
            client.AddParam("evening_count", evening.toString());
            client.AddParam("night_count", night.toString());
        }
        client.AddParam("start_timestamp", start_timestamp.toString());
        client.AddParam("repeat_hour", repeat_hour.toString());
        client.AddParam("repeat_day", repeat_day.toString());
        client.AddParam("repeat_mode", repeat_mode.toString());
        client.AddParam("repeat_min", repeat_min.toString());
        client.AddParam("repeat_weekday", repeat_weekday.toString());
        client.AddParam("repeat_day_interval", repeat_day_interval.toString());
        client.AddParam("start_date", start_date);
        client.AddParam("end_date", end_date);

        try {
            client.Execute(RequestMethod.PUT);
        } catch (Exception e) {
            e.printStackTrace();
        }

        responseString = client.getResponse();
        Log.i("TAG", "Response string " + responseString);

        try {
            jObject = new JSONObject(responseString);
            Log.i("TAG", "res : " + responseString);
            Log.i("TAG", jObject.getString("next"));
            jarray = jObject.getJSONArray("results");
            for (int i = 0; i < jarray.length(); i++) {
                JSONObject c = jarray.getJSONObject(i);
                lstData.add(new MedicalData(c.getString("id").toString(),
                        c.getString("name").toString(),
                        c.getString("details").toString(),
                        c.getString("start_timestamp").toString(),
                        c.getString("repeat_hour").toString(),
                        c.getString("repeat_day").toString(),
                        c.getString("repeat_mode").toString(),
                        c.getString("repeat_min").toString(),
                        c.getString("repeat_weekday").toString(),
                        c.getString("repeat_day_interval").toString()));

            }

            Log.i("TAG", "lstdata count is " + lstData.size());

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return lstData;
    }


    // function for add medicates data
    public ArrayList<MedicalData> UpdateMedical(String id, String name, String detail, String start_timestamp,
                                                String repeat_hour, String repeat_day, String repeat_mode, String repeat_min,
                                                String repeat_weekday, String repeat_day_interval) {
        ArrayList<MedicalData> lstData = new ArrayList<MedicalData>();
        String baseurlString = Global_Application.url + "medicaltests/" + id + "/?user=" + appPrefs.getUserid();
        Log.i("TAG", "url is : " + baseurlString);

        RestClient client = new RestClient(baseurlString);
        client.AddHeader("Authorization", "Token " + appPrefs.getToken().toString());
        client.AddParam("name", name.toString());
        client.AddParam("details", detail.toString());
        client.AddParam("start_timestamp", start_timestamp.toString());
        client.AddParam("repeat_hour", repeat_hour.toString());
        client.AddParam("repeat_day", repeat_day.toString());
        client.AddParam("repeat_mode", repeat_mode.toString());
        client.AddParam("repeat_min", repeat_min.toString());
        client.AddParam("repeat_weekday", repeat_weekday.toString());
        client.AddParam("repeat_day_interval", repeat_day_interval.toString());

        try {
            client.Execute(RequestMethod.PUT);
        } catch (Exception e) {
            e.printStackTrace();
        }

        responseString = client.getResponse();
        Log.i("TAG", "Response string " + responseString);

        try {
            jObject = new JSONObject(responseString);
            Log.i("TAG", "res : " + responseString);
            Log.i("TAG", jObject.getString("next"));
            jarray = jObject.getJSONArray("results");
            for (int i = 0; i < jarray.length(); i++) {
                JSONObject c = jarray.getJSONObject(i);
                lstData.add(new MedicalData(c.getString("id").toString(),
                        c.getString("name").toString(),
                        c.getString("details").toString(),
                        c.getString("start_timestamp").toString(),
                        c.getString("repeat_hour").toString(),
                        c.getString("repeat_day").toString(),
                        c.getString("repeat_mode").toString(),
                        c.getString("repeat_min").toString(),
                        c.getString("repeat_weekday").toString(),
                        c.getString("repeat_day_interval").toString()));

            }

            Log.i("TAG", "lstdata count is " + lstData.size());

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return lstData;
    }


    // function for add medicates data
    public MedicalData getMedicalByID(String id) {
        String baseurlString = Global_Application.url + "medicaltests/" + id;
        MedicalData medicationdt = new MedicalData();
        Log.i("TAG", "url is : " + baseurlString);

        RestClient client = new RestClient(baseurlString);
        client.AddHeader("Authorization", "Token " + appPrefs.getToken().toString());

        try {
            client.Execute(RequestMethod.GET);
        } catch (Exception e) {
            e.printStackTrace();
        }

        responseString = client.getResponse();
        Log.i("TAG", "Response string " + responseString);

        try {
            jObject = new JSONObject(responseString);
            Log.i("TAG", "res : " + responseString);
            medicationdt.setId(jObject.getString("id").toString());
            medicationdt.setName(jObject.getString("name").toString());
            medicationdt.setDetail(jObject.getString("details").toString());
            medicationdt.setStart_timestamp(jObject.getString("start_timestamp").toString());
            medicationdt.setRepeat_mode(jObject.getString("repeat_mode").toString());
            medicationdt.setRepeat_day(jObject.getString("repeat_day").toString());
            medicationdt.setRepeat_hour(jObject.getString("repeat_hour").toString());
            medicationdt.setRepeat_min(jObject.getString("repeat_min").toString());
            medicationdt.setRepeat_weekday(jObject.getString("repeat_weekday").toString());
            medicationdt.setRepeat_day_interval(jObject.getString("repeat_day_interval").toString());


        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return medicationdt;
    }

}
