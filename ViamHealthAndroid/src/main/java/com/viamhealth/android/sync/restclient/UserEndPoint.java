package com.viamhealth.android.sync.restclient;

import android.content.Context;
import android.util.Log;

import com.viamhealth.android.Global_Application;
import com.viamhealth.android.dao.restclient.core.RestClient;
import com.viamhealth.android.dao.restclient.old.RequestMethod;
import com.viamhealth.android.model.BaseModel;
import com.viamhealth.android.model.enums.BloodGroup;
import com.viamhealth.android.model.users.BMIProfile;
import com.viamhealth.android.model.users.Profile;
import com.viamhealth.android.model.users.User;
import com.viamhealth.android.provider.parsers.JsonParser;
import com.viamhealth.android.provider.parsers.UserJsonParser;
import com.viamhealth.android.utils.LogUtils;

import org.apache.http.HttpStatus;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by naren on 17/12/13.
 */
public class UserEndPoint extends BaseEndPoint {

    private final Global_Application mApplication;
    private final UserJsonParser parser = new UserJsonParser();

    public UserEndPoint(Context context) {
        super(context);
        mApplication = (Global_Application)context.getApplicationContext();
    }

    public User getMe() {
        if(mApplication.getLoggedInUser()!=null)
            return mApplication.getLoggedInUser();

        RestClient client = getRestClient(null, Paths.USERS, Paths.LOGGED_IN_USER);

        try{
            client.Execute(RequestMethod.GET);
        } catch (Exception e) {
            e.printStackTrace();
        }

        int responseCode = client.getResponseCode();
        LogUtils.LOGD(TAG, client.toString());

        if(responseCode == HttpStatus.SC_OK){
            User user = (User)parser.parseObject(client.getResponse());
            user.setLoggedInUser(true);
            //user.setBmiProfile(getUserBMIProfile(user.getId()));
            mApplication.setLoggedInUser(user);
            return user;
        }

        return null;
    }

    @Override
    protected List<BaseModel> newList() {
        return new ArrayList<BaseModel>();
    }

    @Override
    protected Params getQueryParams() {
        return null;
    }

    @Override
    protected String[] getPathSegments() {
        return new String[]{Paths.USERS};
    }

    @Override
    protected JsonParser getJsonParser() {
        return new UserJsonParser();
    }

    @Override
    protected void addParams(RestClient client, BaseModel model) {
        //Nothing to be done for user, as saveData in itself is overidden
    }

    @Override
    protected List<BaseModel> saveData(BaseModel model, RequestMethod method) throws IOException {
        String[] ps = getPathSegments();
        String[] pathSegments = null;
        if(method==RequestMethod.PUT){
            pathSegments = Arrays.copyOf(ps, ps.length + 1);
            pathSegments[ps.length] = model.getId().toString();
        }else{
            pathSegments = ps;
        }
        //update user data
        RestClient client = getRestClient(getQueryParams(), pathSegments);
        client.AddParam("first_name", ((User)model).getFirstName());
        client.AddParam("last_name", ((User)model).getLastName());

        Long userId = ((User)model).getId();
        String email = ((User)model).getEmail();
        if(userId==null || userId==0)
            if(email!=null && !email.isEmpty())
                client.AddParam("email", email);

        try {
            client.Execute(method);
        }catch (Exception e) {
            e.printStackTrace();
            LogUtils.LOGW(TAG, "response of save data failed", e.getCause());
        }

        User user = null;
        Log.i(TAG, client.toString());
        if(client.getResponseCode() == HttpStatus.SC_CREATED ||
                client.getResponseCode() == HttpStatus.SC_OK) {
            user =  (User)getJsonParser().parseObject(client.getResponse());
        }

        //update the profile
        user.setProfile(updateProfile(user.getId(), ((User)model).getProfile()));
        user.setBmiProfile(updateBMIProfile(user.getId(), ((User) model).getBmiProfile()));

        List<BaseModel> returnList = new ArrayList<BaseModel>(1);
        returnList.add(user);
        return returnList;
    }

    protected BMIProfile updateBMIProfile(Long userId, BMIProfile profile) {
        RestClient client = getRestClient(getQueryParams(), new String[]{"users", userId.toString(), "bmi-profile"});

        client.AddParam("height", profile.getHeight());
        client.AddParam("weight", profile.getWeight());
        //TODO::lifestyle

        if(profile.getSystolicPressure()>0)
            client.AddParam("systolic_pressure", profile.getSystolicPressure());

        if(profile.getDiastolicPressure()>0)
            client.AddParam("diastolic_pressure", profile.getDiastolicPressure());

        if(profile.getPulseRate()>0)
            client.AddParam("pulse_rate", profile.getPulseRate());

        if(profile.getFastingSugar()>0)
            client.AddParam("fasting", profile.getFastingSugar());

        if(profile.getRandomSugar()>0)
            client.AddParam("random", profile.getRandomSugar());

        if(profile.getHdl()>0)
            client.AddParam("hdl", profile.getHdl());

        if(profile.getLdl()>0)
            client.AddParam("ldl", profile.getLdl());

        if(profile.getTriglycerides()>0)
            client.AddParam("triglycerides", profile.getTriglycerides());

        try{
            client.Execute(RequestMethod.PUT);
        } catch (Exception e) {
            e.printStackTrace();
        }

        String responseString = client.getResponse();
        Log.i(TAG, client.toString());
        if(client.getResponseCode() == HttpStatus.SC_CREATED ||
                client.getResponseCode() == HttpStatus.SC_OK) {
            String json = client.getResponse();
            BMIProfile obj = null;
            try{
                if(json!=null)
                {
                    JSONObject jsonObj = new JSONObject(json);
                    UserJsonParser parser = new UserJsonParser();
                    obj = parser.processBMIProfileResponse(jsonObj);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return obj;
        }

        return null;
    }

    protected Profile updateProfile(Long userId, Profile profile) {
        RestClient client = getRestClient(getQueryParams(), new String[]{"users", userId.toString(), "profile"});

        client.AddParam("gender", profile.getGender().key());
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

        if(profile.getDob()!=null && profile.getDob().getTime()>0)
            client.AddParam("date_of_birth", formatter.format(profile.getDob()));

        if(profile.getProfilePicURL()!=null && !profile.getProfilePicURL().isEmpty())
            client.AddParam("profile_picture_url", profile.getProfilePicURL());

        if(profile.getMobileNumber()!=null && !profile.getMobileNumber().isEmpty())
            client.AddParam("mobile", profile.getMobileNumber());

        if(profile.getBloodGroup()!= BloodGroup.None)
            client.AddParam("blood_group", profile.getBloodGroup().value());

        if(profile.getOrganization()!=null && !profile.getOrganization().isEmpty())
            client.AddParam("organization", profile.getOrganization());

        if(profile.getLocation().getStreet()!=null && !profile.getLocation().getStreet().isEmpty())
            client.AddParam("street", profile.getLocation().getStreet());

        if(profile.getLocation().getCity()!=null && !profile.getLocation().getCity().isEmpty())
            client.AddParam("city", profile.getLocation().getCity());

        if(profile.getLocation().getState()!=null && !profile.getLocation().getState().isEmpty())
            client.AddParam("state", profile.getLocation().getState());

        if(profile.getLocation().getCountry()!=null && !profile.getLocation().getCountry().isEmpty())
            client.AddParam("country", profile.getLocation().getCountry());

        if(profile.getLocation().getZip()!=null && !profile.getLocation().getZip().isEmpty())
            client.AddParam("zip_code", profile.getLocation().getZip());

        if(profile.getLocation().getLattitude()>0)
            client.AddParam("lattitude", profile.getLocation().getLattitude());

        if(profile.getLocation().getLongitude()>0)
            client.AddParam("longitude", profile.getLocation().getLongitude());

        if(profile.getLocation().getAddress()!=null && !profile.getLocation().getAddress().isEmpty())
            client.AddParam("address", profile.getLocation().getAddress());

        if(profile.getFbProfileId()!=null && !profile.getFbProfileId().isEmpty())
            client.AddParam("fb_profile_id", profile.getFbProfileId());

        if(profile.getFbUsername()!=null && !profile.getFbUsername().isEmpty())
            client.AddParam("fb_username", profile.getFbUsername());

        try{
            client.Execute(RequestMethod.PUT);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.LOGW(TAG, "response of save profile failed", e.getCause());
        }

        String responseString = client.getResponse();
        Log.i(TAG, client.toString());
        if(client.getResponseCode() == HttpStatus.SC_CREATED ||
                client.getResponseCode() == HttpStatus.SC_OK) {
            String json = client.getResponse();
            Profile obj = null;
            try{
                if(json!=null)
                {
                    JSONObject jsonObj = new JSONObject(json);
                    UserJsonParser parser = new UserJsonParser();
                    obj = parser.processProfileResponse(jsonObj);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return obj;
        } 
        return null;
    }

}
