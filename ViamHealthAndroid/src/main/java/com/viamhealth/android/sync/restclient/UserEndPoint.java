package com.viamhealth.android.sync.restclient;

import android.content.Context;
import android.util.Log;

import com.viamhealth.android.Global_Application;
import com.viamhealth.android.dao.restclient.core.RestClient;
import com.viamhealth.android.dao.restclient.old.RequestMethod;
import com.viamhealth.android.model.users.BMIProfile;
import com.viamhealth.android.model.users.User;
import com.viamhealth.android.provider.parsers.UserJsonParser;
import com.viamhealth.android.utils.LogUtils;

import org.apache.http.HttpStatus;

import java.io.IOException;
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
            User user = parser.parseObject(client.getResponse());
            user.setLoggedInUser(true);
            //user.setBmiProfile(getUserBMIProfile(user.getId()));
            mApplication.setLoggedInUser(user);
            return user;
        }

        return null;
    }

    public String getFamilyMembers(Date lastUpdatedTime) throws IOException {
        RestClient client = getRestClient(null, Paths.USERS);

        try {
            client.Execute(RequestMethod.GET);
        }catch (Exception e) {
            e.printStackTrace();
        }

        int responseCode = client.getResponseCode();
        LogUtils.LOGD(TAG, client.toString());

        if(responseCode == HttpStatus.SC_OK){
            return client.getResponse();
        }else{
            throw new IOException(client.getResponseCode() + ": " + client.getResponse());
        }
    }

}
